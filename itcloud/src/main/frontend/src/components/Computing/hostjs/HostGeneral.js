import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faExclamation, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import Path from "../../Header/Path";


const HostGeneral = ({ host }) => {
    const pathData = [host?.name || 'Default', '일반']; 

    return (
<div className="host_content_outer">
<div className='ml-1'>
<Path pathElements={pathData} />
</div>
                    <div className='ml-2'>
                    </div>
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
                                      <td>중간</td>
                                  </tr>
                                  <tr>
                                      <th>활성 가상 머신:</th>
                                      <td>1</td>
                                  </tr>
                                  <tr>
                                      <th>논리 CPU 코어 수:</th>
                                      <td>8</td>
                                  </tr>
                                  <tr>
                                      <th>온라인 논리 CPU 코어 수:</th>
                                      <td>0, 1, 2, 3, 4, 5, 6, 7</td>
                                  </tr>
                                  <tr>
                                      <th>부팅 시간:</th>
                                      <td>{host?.bootingTime}</td>
                                  </tr>
                                  <tr>
                                      <th>Hosted Engine HA:</th>
                                      <td>활성 (접속: 3400)</td>
                                  </tr>
                                  <tr>
                                      <th>iSCSI 개시자 이름:</th>
                                      <td>{host?.iscsi}</td>
                                  </tr>
                                  <tr>
                                      <th>Kdump Integration Status:</th>
                                      <td>비활성화됨</td>
                                  </tr>
                                  <tr>
                                      <th>물리적 메모리:</th>
                                      <td>19743 MB 한계, 15794 MB 사용됨, 3949 MB 사용가능</td>
                                  </tr>
                                  <tr>
                                      <th>Swap 크기:</th>
                                      <td>10063 MB 한계, 0 MB 사용됨, 10063 MB 사용가능</td>
                                  </tr>
                                  <tr>
                                      <th>공유 메모리:</th>
                                      <td>9%</td>
                                  </tr>
                                  <tr>
                                      <th>장치 통과:</th>
                                      <td>비활성화됨</td>
                                  </tr>
                                  <tr>
                                      <th>새로운 가상 머신의 스케줄링을 위한 최대 여유 메모리:</th>
                                      <td>2837 MB</td>
                                  </tr>
                                  <tr>
                                      <th>메모리 페이지 공유:</th>
                                      <td>활성</td>
                                  </tr>
                                  <tr>
                                      <th>자동으로 페이지를 크게:</th>
                                      <td>항상</td>
                                  </tr>
                                  <tr>
                                      <th>Huge Pages (size: free/total):</th>
                                      <td>2048: 0/0, 1048576: 0/0</td>
                                  </tr>
                                  <tr>
                                      <th>SELinux 모드:</th>
                                      <td>{host?.seLinux}</td>
                                  </tr>
                                  <tr>
                                      <th>클러스터 호환 버전:</th>
                                      <td>4.2, 4.3, 4.4, 4.5, 4.6, 4.7</td>
                                  </tr>
                                  <tr>
                                      <th><FontAwesomeIcon icon={faExclamation} fixedWidth/></th>
                                      <td>이 호스트는 전원 관리가 설정되어 있지 않습니다.</td>
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
                                    <td>Intel(R) Xeon(R) Gold 6354 CPU @ 3.00GHz</td>
                                </tr>
                                <tr>
                                    <th>버전:</th>
                                    <td>1</td>
                                </tr>
                                <tr>
                                    <th>CPU 모델:</th>
                                    <td>{host?.hostHwVo.cpuName}</td>
                                </tr>
                                <tr>
                                    <th>소켓당 CPU 코어:</th>
                                    <td>1</td>
                                </tr>
                                <tr>
                                    <th>제품군:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>UUID:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>CPU 유형:</th>
                                    <td> {host?.hostHwVo.cpuType}</td>
                                </tr>
                                <tr>
                                    <th>코어당 CPU의 스레드:</th>
                                    <td>1 (SMT 사용안함)</td>
                                </tr>
                                <tr>
                                    <th>제품 이름:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>일련 번호:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>CPU 소켓:</th>
                                    <td>8</td>
                                </tr>
                                <tr>
                                    <th>TSC 주파수:</th>
                                    <td>2992968000 (스케일링 비활성화)</td>
                                </tr>
                            </tbody>

                            </table>
                        </div>


                        <div  className="table_container_left" style={{paddingTop:0}}>
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
  
  export default HostGeneral;