import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faExclamation, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { useHost } from "../../../api/RQHook";

const HostGenerals = ({ hostId }) => {
  const { data: host } = useHost(hostId);
  console.log("host structure: ", host);

  return (
    <div className="host_content_outer">
      <div className="host_tables">
        <div className="table_container_left" style={{paddingTop:0}}>
          <h2 className='font-bold'>호스트</h2>
            <table className="host_table">
              <tbody>
                <tr>
                  <th>호스트이름/IP:</th>
                  <td>{host?.name}</td>
                </tr>
                <tr>
                  <th>SPM 우선순위:</th>
                  <td>{host?.spmPriority}</td>
                </tr>
                <tr>
                  <th>활성 가상 머신:</th>
                  <td>{host?.vmSizeVo.upCnt}</td>
                </tr>
                <tr>
                  <th>논리 CPU 코어 수:</th>
                  <td>{host?.hostHwVo.cpuTopologySocket}</td>
                </tr>
                <tr>
                  <th>온라인 논리 CPU 코어 수:</th>
                  <td>{host?.hostHwVo.cpuOnline}</td>
                </tr>
                <tr>
                  <th>부팅 시간:</th>
                  <td>{host?.bootingTime} 이게무슨일이야</td>
                </tr>
                <tr>
                  <th>Hosted Engine HA:</th>
                  <td>(점수: {host?.hostedScore})</td>
                </tr>
                <tr>
                  <th>iSCSI 개시자 이름:</th>
                  <td>{host?.iscsi}</td>
                </tr>
                <tr>
                  <th>Kdump Integration Status:</th>
                  <td>{host?.kdump}</td>
                </tr>
                <tr>
                  <th>물리적 메모리:</th>
                  <td>{host?.memoryTotal} MB 합계, {host?.memoryUsed} MB 사용됨, {host?.memoryFree} MB 사용가능</td>
                </tr>
                <tr>
                  <th>Swap 크기:</th>
                  <td>{host?.swapTotal} MB 합계, {host?.swapUsed} MB 사용됨, {host?.swapFree} MB 사용가능</td>
                </tr>
                <tr>
                  <th>공유 메모리:</th>
                  <td>{host?.name}</td>
                </tr>
                <tr>
                  <th>장치 통과:</th>
                  <td>{host?.devicePassThrough}</td>
                </tr>
                <tr>
                  <th>새로운 가상 머신의 스케줄링을 위한 최대 여유 메모리:</th>
                  <td>{host?.memoryMax} MB</td>
                </tr>
                <tr>
                  <th>메모리 페이지 공유:</th>
                  <td>{host?.name}</td>
                </tr>
                <tr>
                  <th>자동으로 페이지를 크게:</th>
                  <td>{host?.name}</td>
                </tr>
                <tr>
                  <th>Huge Pages (size: free/total):</th>
                  <td>2048: {host?.hugePage2048Free}/{host?.hugePage2048Total}, 1048576: {host?.hugePage1048576Free}/{host?.hugePage1048576Total}</td>
                </tr>
                <tr>
                  <th>SELinux 모드:</th>
                  <td>{host?.seLinux}</td>
                </tr>
                
              </tbody>
            </table>
          </div>

          <div className="table_container_left" style={{paddingTop:0}}>
            <h2 className='font-bold'>하드웨어</h2>
              <table className="host_table">
                <tbody>
                  <tr>
                    <th>제조사:</th>
                    <td>{host?.hostHwVo.name}</td>
                  </tr>
                  <tr>
                    <th>버전:</th>
                    <td>{host?.hostHwVo.hwVersion}</td>
                  </tr>
                  <tr>
                    <th>CPU 모델:</th>
                    <td>{host?.hostHwVo.cpuName}</td>
                  </tr>
                  <tr>
                    <th>소켓당 CPU 코어:</th>
                    <td>{host?.hostHwVo.cpuTopologyCore}</td>
                  </tr>
                  <tr>
                    <th>제품군:</th>
                    <td>{host?.hostHwVo.name}</td>
                  </tr>
                  <tr>
                    <th>UUID:</th>
                    <td>{host?.hostHwVo.uuid}</td>
                  </tr>
                  <tr>
                    <th>CPU 유형:</th>
                    <td>{host?.hostHwVo.cpuType}</td>
                  </tr>
                  <tr>
                    <th>코어당 CPU의 스레드:</th>
                    <td>{host?.hostHwVo.cpuTopologyThread}</td>
                  </tr>
                  <tr>
                    <th>제품 이름:</th>
                    <td>{host?.hostHwVo.productName}</td>
                  </tr>
                  <tr>
                    <th>일련 번호:</th>
                    <td>{host?.hostHwVo.serialNum}</td>
                  </tr>
                  <tr>
                    <th>CPU 소켓:</th>
                    <td>{host?.hostHwVo.cpuTopologySocket}</td>
                  </tr>
                  <tr>
                    <th>TSC 주파수:</th>
                    <td>{host?.hostHwVo.name}</td>
                  </tr>
              </tbody>
            </table>
          </div>

          <div className="table_container_left" style={{paddingTop:0}}>
            <h2 className='font-bold'>소프트웨어</h2>
              <table className="host_table">
                <tbody>
                  <tr>
                    <th>OS 버전:</th>
                    <td>{host?.hostSwVo.osVersion}</td>
                  </tr>
                  <tr>
                    <th>OS 정보:</th>
                    <td>{host?.hostSwVo.osInfo}</td>
                  </tr>
                  <tr>
                    <th>커널 버전:</th>
                    <td>{host?.hostSwVo.kernalVersion}</td>
                  </tr>
                  <tr>
                    <th>KVM 버전:</th>
                    <td>{host?.hostSwVo.kvmVersion}</td>
                  </tr>
                  <tr>
                    <th>LIBVIRT 버전:</th>
                    <td>{host?.hostSwVo.libvirtVersion}</td>
                  </tr>
                  <tr>
                    <th>VDSM 버전:</th>
                    <td>{host?.hostSwVo.vdsmVersion}</td>
                  </tr>
                  <tr>
                    <th>SPICE 버전:</th>
                    <td>{host?.hostSwVo.spiceVersion}</td>
                  </tr>
                  <tr>
                    <th>GlusterFS 버전:</th>
                    <td>{host?.hostSwVo.glustersfsVersion}</td>
                  </tr>
                  <tr>
                    <th>CEPH 버전:</th>
                    <td>{host?.hostSwVo.cephVersion}</td>
                  </tr>
                  <tr>
                    <th>Open vSwitch 버전:</th>
                    <td>{host?.hostSwVo.openVswitchVersion}</td>
                  </tr>
                  <tr>
                    <th>Nmstate 버전:</th>
                    <td>{host?.hostSwVo.nmstateVersion}</td>
                  </tr>
                  <tr>
                    <th>커널 기능:</th>
                    <td>
                      MDS: (Not affected), L1TF: (Not affected), SRBDS: (Not affected), MELTDOWN: (Not affected), RETBLEED: (Not affected), SPECTRE_V1: (Mitigation: usercopy/swapgs barriers and __user pointer sanitization), SPECTRE_V2: (Mitigation: Enhanced / Automatic IBRS, IBPB: conditional, RSB filling, PBRSE-eIBRS: SW sequence), ITLB_MULTIHIT: (KVM: Mitigation: Split huge pages), MMIO_STALE_DATA: (Vulnerable: Clear CPU buffers attempted, no microcode; SMT Host state unknown), TSX_ASYNC_ABORT: (Not affected), SPEC_STORE_BYPASS: (Mitigation: Speculative Store Bypass disabled via prctl), GATHER_DATA_SAMPLING: (Unknown: Dependent on hypervisor status), SPEC_RSTACK_OVERFLOW: (Not affected)
                    </td>
                  </tr>
                  <tr>
                    <th>VNC 암호화:</th>
                    <td>비활성화됨</td>
                  </tr>
                  <tr>
                    <th>OVN configured:</th>
                    <td>예</td>
                  </tr>
                </tbody>
              </table>
          </div>
        </div>
      </div>
    );
  };
  
  export default HostGenerals;