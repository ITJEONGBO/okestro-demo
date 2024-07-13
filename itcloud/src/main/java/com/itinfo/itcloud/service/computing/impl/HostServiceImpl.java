package com.itinfo.itcloud.service.computing.impl;

import com.itinfo.itcloud.model.UsageVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.dto.HostUsageDto;
import com.itinfo.itcloud.model.entity.HostSamplesHistoryEntity;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.repository.HostRepository;
import com.itinfo.itcloud.service.computing.ItAffinityService;
import com.itinfo.itcloud.service.computing.ItHostService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.services.HostsService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HostServiceImpl implements ItHostService {
    @Autowired private AdminConnectionService admin;
    @Autowired private CommonService commonService;
    @Autowired private ItAffinityService itAffinityService;
    @Autowired private HostRepository repository;


    /**
     * 전체 사용량 - Memory
     * @return 전체 GB, 사용 GB, 사용가능 GB, 사용 %
     */
    @Override
    public UsageVo totalMemory(){
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().search("status=up").send().hosts();
        double gb = 1073741824; /*Math.pow(1024,3)*/

        double total = hostList.stream().mapToDouble(Host::memoryAsLong).sum();
        double used = hostList.stream()
                .flatMap(host -> system.hostsService().hostService(host.id()).statisticsService().list().send().statistics().stream())
                .filter(stat -> "memory.used".equals(stat.name()))
                .mapToDouble(stat -> stat.values().get(0).datum().doubleValue())
                .sum();

        return UsageVo.builder()
                .total( total / gb )
                .used(used / gb)
                .free((total - used) / gb)

                .percent((int) (used/total * 100))
                .build();
    }


    /**
     * 전체 사용량(CPU, Memory) 원 그래프
     * @return 5분마다 한번씩 불려지게 해야함
     */
    @Override
    public HostUsageDto totalCpu() {
        // 여기서 hostId가 내가 넣는 방식이 아니고 전체의 hostList에서 값을 받아와서 넣어야됨
        SystemService system = admin.getConnection().systemService();
        List<String> hostIds = system.hostsService().list().send().hosts().stream().map(Host::id).collect(Collectors.toList());
        int hostCnt = hostIds.size();

        double totalCpu = 0;

        for(String hostId : hostIds){
            HostUsageDto usage = repository.findFirstByHostIdAndHostStatusOrderByHistoryDatetimeDesc(UUID.fromString(hostId), 1).totalCpuMemory();
            totalCpu += usage.getTotalCpuUsagePercent();
        }

        return HostUsageDto.builder()
                .totalCpuUsagePercent(Math.round(totalCpu / hostCnt))
                .build();
    }

    /**
     * 전체 사용량(CPU, Memory) 선 그래프
     * @param hostId 호스트 id
     * @return 10분마다 그래프에
     */
    @Override
    public List<HostUsageDto> totalUsageList(UUID hostId) {
        return repository.findByHostIdOrderByHistoryDatetimeDesc(hostId)
                        .stream()
                        .map(HostSamplesHistoryEntity::totalUsage)
                        .collect(Collectors.toList());
    }






    /**
     * 호스트 목록
     * @return 호스트 목록
     */
    @Override
    public List<HostVo> getList() {
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().allContent(true).send().hosts(); // hosted Engine의 정보가 나온다

        log.info("호스트 목록");
        return hostList.stream()
                .map(host -> {
                    Cluster cluster = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();
                    return HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .comment(host.comment())
                                .status(host.status().value())
                                .address(host.address())
                                .clusterId(host.cluster().id())
                                .clusterName(cluster.name())
                                .datacenterId(cluster.dataCenter().id())
                                .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                                .hostedEngine(host.hostedEnginePresent() ? host.hostedEngine().active() : null) // 별표
                                .vmCnt(
                                        (int) system.vmsService().list().send().vms().stream()
                                        .filter(vm -> vm.host() != null && vm.host().id().equals(host.id()))
                                        .count()
                                )
                            .build();
                })
                .collect(Collectors.toList());
    }



    /**
     * 호스트 생성 - 클러스터 리스트 출력
     * @return 클러스터 리스트
     */
    @Override
    public List<ClusterVo> setClusterList() {
        SystemService system = admin.getConnection().systemService();
        List<Cluster> clusterList = system.clustersService().list().send().clusters();

        log.info("호스트 생성창");
        return clusterList.stream()
                .map(cluster ->
                        ClusterVo.builder()
                            .id(cluster.id())
                            .name(cluster.name())
                            .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                        .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * 호스트 생성
     * 전원관리 없앰
     * @param hostCreateVo 호스트 객체
     * @return host 201(create) 404(fail)
     */
    @Override
    public CommonVo<Boolean> addHost(HostCreateVo hostCreateVo) {
        SystemService system = admin.getConnection().systemService();
        HostsService hostsService = system.hostsService();

        // ssh port가 22면 .ssh() 설정하지 않아도 알아서 지정됨.
        // ssh port 변경을 ovirt에서 해보신적은 없어서 우선 보류 (cmd로 하셨음)
        // 비밀번호 잘못되면 보여줄 코드?
        try {
            // 호스트 엔진 배치작업 선택 (없음/배포)  -> 호스트 생성
            Host host =
                hostsService.add()
                        .deployHostedEngine(hostCreateVo.isHostEngine())
                        .host(
                            new HostBuilder()
                                .cluster(new ClusterBuilder().id(hostCreateVo.getId()))
                                .name(hostCreateVo.getName())
                                .comment(hostCreateVo.getComment())
                                .address(hostCreateVo.getHostIp())  // 호스트이름/IP
                                .rootPassword(hostCreateVo.getSshPw())  // 암호
                                .spm(new SpmBuilder().priority(hostCreateVo.getSpm()))
                                .ssh(new SshBuilder().port(hostCreateVo.getSshPort()))  // 기본값이 22
                        )
                        .send().host();

            HostService hostService = system.hostsService().hostService(host.id());

            // 호스트 상태가 "UP"이 될 때까지 대기
            // 3초 씩 15분 기다림
            if(expectStatus(hostService, HostStatus.UP, 3000, 900000)){
                log.info("호스트 생성 완료: " + host.name());
                return CommonVo.createResponse();
            } else {
                log.error("호스트 생성 시간 초과: {}", host.name());
                return CommonVo.failResponse("호스트 생성 시간 초과");
            }

        } catch (Exception e) {
            log.error("호스트 추가 실패 {}", e.getMessage());
            e.getMessage();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    /**
     * 호스트 편집창
     * @param id 호스트 id
     * @return 호스트 객체
     */
    @Override
    public HostCreateVo setHost(String id) {
        SystemService system = admin.getConnection().systemService();
        Host host = system.hostsService().hostService(id).get().allContent(true).send().host();
        Cluster cluster = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();

        log.info("호스트 편집창");

        return HostCreateVo.builder()
                .clusterId(cluster.id())
                .clusterName(cluster.name())
                .datacenterName(system.dataCentersService().dataCenterService(cluster.dataCenter().id()).get().send().dataCenter().name())
                .id(id)
                .name(host.name())
                .comment(host.comment())
                .hostIp(host.address())
                .sshPort(host.ssh().portAsInteger())
                // 인증 - 사용자 이름, 공개키 오케는 지정사용
                .sshPw(host.rootPassword())
                .spm(host.spm().priorityAsInteger())
                .hostEngine(host.hostedEnginePresent()) // ?? 호스트 엔진 배치작업 없음
                .build();
    }


    /**
     * 호스트 편집
     * @param hcVo 호스트 객체
     * @return host 201(create) 404(fail)
     */
    @Override
    public CommonVo<Boolean> editHost(HostCreateVo hcVo) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(hcVo.getId());

        try {
            hostService.update()
                    .host(
                        new HostBuilder()
                            .id(hcVo.getId())
                            .name(hcVo.getName())
                            .comment(hcVo.getComment())
                            .spm(new SpmBuilder().priority(hcVo.getSpm()))
                            .build()
                    )
                    .send().host();

            log.info("호스트 편집");
            return CommonVo.createResponse();
        } catch (Exception e) {
            log.error("호스트 편집 error : ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }



    /**
     * 호스트 삭제
     * 삭제 여부 = 가상머신 돌아가는게 있는지 -> 유지보수 상태인지 -> 삭제
     * @param id 호스트 id
     * @return host 200(success) 404(fail)
     */
    @Override
    public CommonVo<Boolean> deleteHost(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();
            HostStatus status = host.status();

            if (status == HostStatus.MAINTENANCE) {
                hostService.remove().send();
                return CommonVo.successResponse();
            } else {
                log.error("호스트 삭제불가 : {}, 유지보수 모드로 바꾸세요", host.name());
                return CommonVo.failResponse("현재 호스트는 유지보수 모드가 아님");
            }
        }catch (Exception e){
            log.error("error ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }




    /**
     * 호스트 유지보수
     * @param id 호스트 id
     * @return host 200(success) 404(fail)
     */
    @Override
    public CommonVo<Boolean> deactiveHost(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();
            HostStatus status = host.status();

            if (status != HostStatus.MAINTENANCE) {
                hostService.deactivate().send();

                if (expectStatus(hostService, HostStatus.MAINTENANCE, 3000, 60000)) {
                    log.info("호스트 유지보수 모드 전환 완료: {}", host.name());
                    return CommonVo.successResponse();
                } else {
                    log.error("호스트 유지보수 모드 전환 시간 초과: {}", host.name());
                    return CommonVo.failResponse("유지보수 모드 전환 시간 초과");
                }
            } else {
                log.info("현재 호스트는 이미 유지보수 모드입니다: {}", host.name());
                return CommonVo.failResponse("현재 호스트는 이미 유지보수 모드입니다");
            }
        } catch (Exception e) {
            log.error("호스트 유지보수 모드 전환 중 오류: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    /**
     * 호스트 활성
     * @param id 호스트 id
     * @return host 200(success) 404(fail)
     */
    @Override
    public CommonVo<Boolean> activeHost(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            Host host = hostService.get().send().host();
            HostStatus status = host.status();

            if (status != HostStatus.UP) {
                hostService.activate().send();

                if (expectStatus(hostService, HostStatus.UP, 3000, 60000)) {
                    log.info("호스트 활성 전환 완료: {}", host.name());
                    return CommonVo.successResponse();
                } else {
                    log.error("호스트 활성 전환 시간 초과: {}", host.name());
                    return CommonVo.failResponse("활성 전환 시간 초과");
                }
            } else {
                log.info("현재 호스트는 이미 활성 상태입니다: {}", host.name());
                return CommonVo.failResponse("현재 호스트는 이미 활성 상태입니다");
            }
        } catch (Exception e) {
            log.error("호스트 활성 전환 중 오류: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    /**
     * 호스트 새로고침
     * @param id 호스트 id
     * @return host 200(success) 404(fail)
     */
    @Override
    public CommonVo<Boolean> refreshHost(String id) {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);

        try {
            hostService.refresh().send();

            if (expectStatus(hostService, HostStatus.UP, 3000, 60000)) {
                log.info("호스트 새로고침 완료: {}", hostService.get().send().host().name());
                return CommonVo.successResponse();
            } else {
                log.error("호스트 새로고침 시간 초과: {}", hostService.get().send().host().name());
                return CommonVo.failResponse("새로고침 시간 초과");
            }
        } catch (Exception e) {
            log.error("호스트 새로고침 중 오류: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }


    // host SSH 관리 - 재시작 부분
    private boolean rebootHostViaSSH(String hostAddress, String username, String password, int port) {
        com.jcraft.jsch.Session session = null;
        ChannelExec channel = null;
        log.debug("ssh 시작");

        try {
            // SSH 세션 생성 및 연결
            session = new JSch().getSession(username, hostAddress, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");   // 호스트 키 확인을 건너뛰기 위해 설정
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");  // SSH 채널 열기
            channel.setCommand("sudo reboot");// 재부팅 명령 실행
            channel.connect();

            // 명령 실행 완료 대기
            while (!channel.isClosed()) {
                Thread.sleep(100);
            }

            channel.disconnect();
            session.disconnect();
            int exitStatus = channel.getExitStatus();
            return exitStatus == 0;
        } catch (JSchException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * ssh 관리 - 재시작
     * 제외하고 cli 로 하는 식으로
     * @param id 호스트 id
     * @return host 200(success) 404(fail)
     */
    @Override
    public CommonVo<Boolean> reStartHost(String id) throws UnknownHostException {
        SystemService system = admin.getConnection().systemService();
        HostService hostService = system.hostsService().hostService(id);
        Host host = hostService.get().send().host();
        InetAddress address = InetAddress.getByName(host.address());

        try {
            if (!rebootHostViaSSH(address.getHostAddress(), "root", "adminRoot!@#", 22)) {
                return CommonVo.failResponse("SSH를 통한 호스트 재부팅 실패");
            }

            Thread.sleep(60000); // 60초 대기, 재부팅 시간 고려

            if (expectStatus(hostService, HostStatus.UP, 3000, 900000)) {
                log.info("호스트 재부팅 완료: {}", id);
                return CommonVo.successResponse();
            } else {
                return CommonVo.failResponse("재부팅 전환 시간 초과");
            }

        }catch (Exception e){
            log.error("error: ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }






    /**
     * 일반
     * @param id 호스트 id
     * @return 호스트 객체
     */
    @Override
    public HostVo getHost(String id) {
        SystemService system = admin.getConnection().systemService();
        Host host = system.hostsService().hostService(id).get().allContent(true).send().host();
        HostService hostService = system.hostsService().hostService(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        // 온라인 논리 CPU 코어수 - HostCpuUnit 이 없음 인식안됨
        // https://192.168.0.70/ovirt-engine/api/hosts/3bbd27b9-13d8-4fff-ad29-c0350994ca88/cpuunits,numanodes
        List<Statistic> statisticList = hostService.statisticsService().list().send().statistics();

        long bootTime = statisticList.stream()
                                .filter(statistic -> statistic.name().equals("boot.time"))
                                .map(statistic -> statistic.values().get(0).datum().longValue() * 1000)
                                .findAny()
                                .orElse(0L);

        log.info("호스트 일반");
        return HostVo.builder()
                .id(id)
                .name(host.name())
                .address(host.address())        //호스트 ip
                .spmPriority(host.spm().priorityAsInteger())    // spm 우선순위
                .status(host.status().value())
                // cpu 있으면 출력으로 바꿔야됨
                .cpuCnt(host.cpu().topologyPresent() ?
                        host.cpu().topology().coresAsInteger()
                        * host.cpu().topology().socketsAsInteger()
                        * host.cpu().topology().threadsAsInteger() : 0
                )
                .cpuOnline(
                        hostService.cpuUnitsService().list().send().cpuUnits().stream()
                                .map(HostCpuUnit::cpuIdAsInteger)
                                .collect(Collectors.toList())
                )
                .vmUpCnt(
                        (int) system.vmsService().list().send().vms().stream()
                                .filter(vm -> vm.host()!= null && vm.host().id().equals(host.id()) && vm.status().value().equals("up"))
                                .count()
                )
                .iscsi(host.iscsiPresent() ? host.iscsi().initiator() : null)   // iscsi 게시자 이름
                .kdump(host.kdumpStatus().value())      // kdump intergration status
                .devicePassThrough(host.devicePassthrough().enabled())  // 장치통과
                .memoryMax(host.maxSchedulingMemory())    // 최대 여유 메모리.
                .memory(commonService.getSpeed(statisticList, "memory.total"))
                .memoryFree(commonService.getSpeed(statisticList, "memory.free"))
                .memoryUsed(commonService.getSpeed(statisticList, "memory.used"))
                .memoryShared(commonService.getSpeed(statisticList, "memory.shared")) // 문제잇음
                .swapTotal(commonService.getSpeed(statisticList, "swap.total"))
                .swapFree(commonService.getSpeed(statisticList, "swap.free"))
                .swapUsed(commonService.getSpeed(statisticList, "swap.used"))
                .hugePage2048Total(commonService.getPage(statisticList, "hugepages.2048.total"))
                .hugePage2048Free(commonService.getPage(statisticList, "hugepages.2048.free"))
                .hugePage1048576Total(commonService.getPage(statisticList, "hugepages.1048576.total"))
                .hugePage1048576Free(commonService.getPage(statisticList, "hugepages.1048576.free"))
                .bootingTime(sdf.format(new Date(bootTime)))
//                .hostedEngine(host.hostedEnginePresent() && host.hostedEngine().active()) // 이 호스트 내에 호스트 가상머신이 있는지 보기
                .hostedActive(host.hostedEnginePresent() ? host.hostedEngine().active() : null)
                .hostedScore(host.hostedEnginePresent() ? host.hostedEngine().scoreAsInteger() : 0)
                .ksm(host.ksmPresent() && host.ksm().enabled())         // 메모리 페이지 공유  비활성
                .pageSize(host.transparentHugePages().enabled())    // 자동으로 페이지를 크게 (확실하지 않음. 매우)
                .seLinux(host.seLinux().mode().value())     // selinux모드: disabled, enforcing, permissive
                // 클러스터 호환버전
                .hostHwVo(getHardWare(host))
                .hostSwVo(getSoftWare(host))
            .build();
    }
    

    /**
     * 호스트 가상머신 목록
     * @param id 호스트 id
     * @return 가상머신 목록
     */
    @Override
    public List<VmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().allContent(true).send().vms();

        log.info("호스트 가상머신");
        return vmList.stream()
                .filter(vm ->
                            (vm.hostPresent() && vm.host().id().equals(id)) ||
                            (vm.placementPolicy().hostsPresent() && vm.placementPolicy().hosts().stream().anyMatch(host -> host.id().equals(id))))
                .map(vm ->
                        VmVo.builder()
                            .id(vm.id())
                            .name(vm.name())
                            .clusterName(system.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name())
                            .hostEngineVm(vm.origin().equals("managed_hosted_engine"))
                            .status(vm.status().value())
                            .fqdn(vm.fqdn())
                            .upTime(commonService.getVmUptime(system, vm.id()))
                            .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
                            .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
//                            .placement(vm.placementPolicy().hostsPresent()) // 호스트 고정여부
                                // vm.placementPolicy().hosts() // 고정된 호스트 id가 나옴
                            // 현재 호스트에 부착 여부
                        .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * 호스트 네트워크 인터페이스 목록
     * @param id 호스트 id
     * @return 네트워크 인터페이스 목록
     */
    @Override
    public List<NicVo> getNic(String id) {
        SystemService system = admin.getConnection().systemService();
        List<HostNic> hostNicList = system.hostsService().hostService(id).nicsService().list().send().nics();
        DecimalFormat df = new DecimalFormat("###,###");
        BigInteger bps = BigInteger.valueOf(1024).pow(2);
        
        log.info("호스트 네트워크 인터페이스");
        return hostNicList.stream()
                .map(hostNic -> {
                    List<Statistic> statisticList = system.hostsService().hostService(id).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();

                    return NicVo.builder()
                            .status(hostNic.status())
                            .name(hostNic.name())
                            .networkName(system.networksService().networkService(hostNic.network().id()).get().send().network().name())
                            .macAddress(hostNic.macPresent() ? hostNic.mac().address() : "")
                            .ipv4(hostNic.ip().address())
                            .ipv6(hostNic.ipv6().addressPresent() ? hostNic.ipv6().address() : null)
                            .speed(hostNic.speed().divide(BigInteger.valueOf(1024 * 1024)))
                            .rxSpeed(commonService.getSpeed(statisticList, "data.current.rx.bps").divide(bps))
                            .txSpeed(commonService.getSpeed(statisticList, "data.current.tx.bps").divide(bps))
                            .rxTotalSpeed(commonService.getSpeed(statisticList, "data.total.rx"))
                            .txTotalSpeed(commonService.getSpeed(statisticList, "data.total.tx"))
                            .stop(commonService.getSpeed(statisticList, "errors.total.rx").divide(bps))
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * 호스트 호스트장치 목록
     * @param id 호스트 id
     * @return 호스트 장치 목록
     */
    @Override
    public List<HostDeviceVo> getHostDevice(String id) {
        SystemService system = admin.getConnection().systemService();
        List<HostDevice> hostDeviceList = system.hostsService().hostService(id).devicesService().list().send().devices();

        log.info("호스트 호스트장치");
        return hostDeviceList.stream()
                .map(hostDevice ->
                        HostDeviceVo.builder()
                            .name(hostDevice.name())
                            .capability(hostDevice.capability())
                            .driver(hostDevice.driverPresent() ? hostDevice.driver() : null)
                            .vendorName(hostDevice.vendorPresent() ? hostDevice.vendor().name() + " (" +hostDevice.vendor().id() + ")" : "")
                            .productName(hostDevice.productPresent() ? hostDevice.product().name() + " (" + hostDevice.product().id() + ")" : "")
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 호스트 권한 목록
     * @param id 호스트 id
     * @return 권한 목록
     */
    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Permission> permissionList = system.hostsService().hostService(id).permissionsService().list().send().permissions();
        
        log.info("호스트 권한");
        return commonService.getPermission(system, permissionList);
    }



    // 호스트 선호도 레이블 목록
//    @Override
//    public List<AffinityLabelVo> getAffinitylabels(String id) {
////        SystemService system = admin.getConnection().systemService();
////        List<AffinityLabel> affinityLabelList = system.hostsService().hostService(id).affinityLabelsService().list().follow("hosts,vms").send().label();
////
////        log.info("Host 선호도 레이블");
////        return affinityLabelList.stream()
////                .map(al ->
////                        AffinityLabelVo.builder()
////                            .id(al.id())
////                            .name(al.name())
////                            .hosts(itAffinityService.getHostLabelMember(system, al.id()))
////                            .vms(itAffinityService.getVmLabelMember(system, al.id()))
////                        .build())
////                .collect(Collectors.toList());
//        return null;
//    }
//
//    // 선호도 레이블 생성 창
//    @Override
//    public AffinityHostVm setAffinityDefaultInfo(String id, String type) {
//        SystemService system = admin.getConnection().systemService();
//        String clusterId = system.hostsService().hostService(id).get().send().host().cluster().id();
//
//        log.info("Host 선호도 레이블 생성 창");
//        return AffinityHostVm.builder()
//                .clusterId(id)
//                .hostList(itAffinityService.getHostList(clusterId))
//                .vmList(itAffinityService.getVmList(clusterId))
//                .build();
//    }
//
//
//    @Override
//    public CommonVo<Boolean> addAffinitylabel(String id, AffinityLabelCreateVo alVo) {
//        SystemService system = admin.getConnection().systemService();
//        AffinityLabelsService alServices = system.affinityLabelsService();
//        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();
//
//        // 중복이름
//        boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));
//
//        try {
//            if(duplicateName) {
//                AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
//                alBuilder
//                        .name(alVo.getName())
//                        .hosts(
//                            alVo.getHostList().stream()
//                                .map(host -> new HostBuilder().id(host.getId()).build())
//                                .collect(Collectors.toList())
//                        )
//                        .vms(
//                            alVo.getVmList().stream()
//                                .map(vm -> new VmBuilder().id(vm.getId()).build())
//                                .collect(Collectors.toList())
//                        )
//                        .build();
//
//                alServices.add().label(alBuilder).send().label();
//                log.info("Host 선호도 레이블 생성");
//                return CommonVo.successResponse();
//            }else {
//                log.error("실패: Host 선호도레이블 이름 중복");
//                return CommonVo.failResponse("이름 중복");
//            }
//        } catch (Exception e) {
//            log.error("실패: Host 선호도 레이블");
//            e.printStackTrace();
//            return CommonVo.failResponse(e.getMessage());
//        }
//    }
//
//    @Override
//    public AffinityLabelCreateVo getAffinityLabel(String alId) {
////        SystemService system = admin.getConnection().systemService();
////        AffinityLabel al = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();
////
////        log.info("Host 선호도 레이블 편집창");
////        return AffinityLabelCreateVo.builder()
////                .id(alId)
////                .name(al.name())
////                .hostList(al.hostsPresent() ? itAffinityService.getHostLabelMember(system, alId) : null )
////                .vmList(al.vmsPresent() ? itAffinityService.getVmLabelMember(system, alId) : null)
////                .build();
//        return null;
//    }
//
//    @Override
//    public CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo) {
//
//        return null;
//    }
//
//    @Override
//    public CommonVo<Boolean> deleteAffinitylabel(String id, String alId) {
//
//        return null;
//    }


    /**
     * 호스트 이벤트 목록
     * @param id 호스트 id
     * @return 이벤트 목록
     */
    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().search("host.name=" + system.hostsService().hostService(id).get().send().host().name()).send().events();

        log.info("호스트 이벤트");
        return eventList.stream()
                .filter(event -> !(event.severity().value().equals("alert") && event.description().contains("Failed to verify Power Management configuration for Host")))
                .map(
                    event ->
                        EventVo.builder()
                            .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                            .time(sdf.format(event.time()))
                            .message(event.description())
                            .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                            .source(event.origin())
                        .build()
                )
                .collect(Collectors.toList());
    }




    //-------------------------------------------------------------------------------------------

    /**
     * 호스트 상태 체크하는 메소드
     * @param hostService 호스트 서비스
     * @param expectStatus 원하는 호스트 상태
     * @param check 상태 확인 간격(밀리초)
     * @param timeout 최대 대기 시간(밀리초)
     * @return true =pass / false=fail
     * @throws InterruptedException
     */
    private boolean expectStatus(HostService hostService, HostStatus expectStatus, long check, long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (true) {
            Host currentHost = hostService.get().send().host();
            HostStatus status = currentHost.status();

            if (status == expectStatus) {
                return true;
            } else if (System.currentTimeMillis() - startTime > timeout) {
                return false;
            }

            log.info("호스트 상태: {}", status);
            Thread.sleep(check);
        }
    }


    /**
     * 호스트 하드웨어 정보 받기
     * @param host  호스트 객체
     * @return 하드웨어 정보
     */
    private HostHwVo getHardWare(Host host){
        return HostHwVo.builder()
                .family(host.hardwareInformation().familyPresent() ? host.hardwareInformation().family() : "")           // 생산자
                .manufacturer(host.hardwareInformation().manufacturerPresent() ? host.hardwareInformation().manufacturer() : "")     // 제품군
                .productName(host.hardwareInformation().productNamePresent() ? host.hardwareInformation().productName() : "")      // 제품 이름
                .hwVersion(host.hardwareInformation().versionPresent() ? host.hardwareInformation().version() : "")        // 버전
                .cpuName(host.cpu().namePresent() ? host.cpu().name() : "")          // cpu 모델
                .cpuType(host.cpu().typePresent() ? host.cpu().type() : "")          // cpu 유형
                .uuid(host.hardwareInformation().uuidPresent() ? host.hardwareInformation().uuid() : "")             // uuid
                .serialNum(host.hardwareInformation().serialNumberPresent() ? host.hardwareInformation().serialNumber() : "")        // 일련번호
                .cpuSocket(host.cpu().topologyPresent() ? host.cpu().topology().socketsAsInteger() : 0)        // cpu 소켓
                .coreThread(host.cpu().topologyPresent() ? host.cpu().topology().threadsAsInteger() : 0)       // 코어당 cpu 스레드
                .coreSocket(host.cpu().topologyPresent() ? host.cpu().topology().coresAsInteger() : 0)       // 소켓당 cpu 코어
                .build();
    }

    

    /**
     * 호스트 소프트웨어 정보 받기
     * 구하는 방법이 db밖에는 없는건지 확인필요
     * @param host 호스트 객체
     * @return 소프트웨어 정보
     */
    private HostSwVo getSoftWare(Host host){
        return HostSwVo.builder()
                .osVersion((host.os().typePresent() ? host.os().type() : "") + (host.os().versionPresent() ? " " + host.os().version().fullVersion() : ""))    // os 버전
//                .osInfo()       // os 정보
                .kernalVersion(host.os().reportedKernelCmdlinePresent() ? host.os().reportedKernelCmdline() : "")// 커널 버전 db 수정해야함
                // kvm 버전 db
                .libvirtVersion(host.libvirtVersion().fullVersionPresent() ? host.libvirtVersion().fullVersion() : "")// LIBVIRT 버전
                .vdsmVersion(host.version().fullVersionPresent() ? host.version().fullVersion() : "")// VDSM 버전 db
                // SPICE 버전
                // GlusterFS 버전
                // CEPH 버전
                // Open vSwitch 버전
                // Nmstate 버전
                .build();
    }


}
