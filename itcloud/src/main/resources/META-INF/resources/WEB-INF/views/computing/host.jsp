<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Host</h1>
                    컴퓨팅 > <a href="/computing/hosts" style="text-decoration-line: none">호스트</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/host?id=${id}">일반</a> |
                                <a href="/computing/host-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/host-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/host-device?id=${id}" style="text-decoration-line: none">호스트 장치</a> |
                                <a href="/computing/host-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/host-aff?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/host-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        호스트이름/IP:&nbsp;${host.name} <br>
                        SPM 우선순위: ${host.spmPriority} <br>
                        활성 가상머신: ${host.vmUpCnt} <br><br>

                        논리 CPU 코어 수: ${host.cpuCnt} <br>
                        온라인 논리 CPU 코어 수: <br>
                        부팅 시간: ${host.bootingTime}<br>
                        Hosted Engine HA: <br><br>

                        iSCSI 개시자 이름: ${host.iscsi} <br>
                        Kdump Intergration Status: ${host.kdump} <br>
                        물리적 메모리: ${host.memory / (1024*1024)}MB 합계, ${host.memoryUsed / (1024*1024)}MB 사용됨, ${host.memoryFree / (1024*1024)}MB 사용가능 <br>
                        Swap 크기: ${host.swapTotal / (1024*1024)}MB 합계, ${host.swapUsed / (1024*1024)}MB 사용됨, ${host.swapFree / (1024*1024)}MB 사용가능 <br>
                        공유 메모리: ${host.memoryShared}% <br>
                        장치 통과: ${host.devicePassThrough == "true" ? "활성화" : "비활성화" } <br><br>


                        새로운 가상머신의 스케줄링을 위한 최대 여유 메모리: ${host.memoryMax / (1024*1024)}MB <br>
                        메모리 페이지 공유: <br>
                        자동으로 페이지를 크게: ${host.pageSize ? "항상" : host.pageSize}<br>
                        Huge Pages (size: free/total) : 2048: ${host.hugePage2048Free}/${host.hugePage2048Free}, 1048576: ${host.hugePage1048576Free}/${host.hugePage1048576Free}<br>
                        SELinux 모드: ${host.seLinux} <br>
                        클러스터 호환 버전: <br>

                    <hr>
                        <h5>하드웨어</h5>
                    <hr>
                        생산자: ${host.hostHwVo.manufacturer} <br>
                        제품군: ${host.hostHwVo.family} <br>
                        제품 이름: ${host.hostHwVo.productName} <br>
                        버전: ${host.hostHwVo.hwVersion} <br>
                        uuid: ${host.hostHwVo.uuid} <br>
                        일련번호: ${host.hostHwVo.serialNum} <br>
                        cpu 모델: ${host.hostHwVo.cpuName} <br>
                        cpu 유형: ${host.hostHwVo.cpuType} <br>
                        cpu 소켓: ${host.hostHwVo.cpuSocket} <br>
                        소켓당 cpu 코어: ${host.hostHwVo.coreSocket} <br>
                        코어당 cpu 스레드: ${host.hostHwVo.coreThread} <br>

                    <hr>
                        <h5>소프트웨어</h5>
                    <hr>
                        os 버전: ${host.hostSwVo.osVersion} <br>
                        os 정보:  DB<br>
                        커널 버전: DB<br>
                        kvm 버전: DB<br>
                        LIBVIRT 버전: ${host.hostSwVo.libvirtVersion} <br>
                        VDSM 버전: ${host.hostSwVo.vdsmVersion} <br>
                        SPICE 버전: DB<br>
                        GlusterFS 버전: DB<br>
                        CEPH 버전: DB<br>
                        Open vSwitch 버전: DB<br>
                        Nmstate 버전: DB<br>
                        <br><br><br><br><br><br>
                    </div>
                </div>
            </main>
        </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="js/scripts.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
    <script src="assets/demo/chart-area-demo.js"></script>
    <script src="assets/demo/chart-bar-demo.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js" crossorigin="anonymous"></script>
    <script src="js/datatables-simple-demo.js"></script>
    </body>
</html>
