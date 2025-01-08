import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faComputer, faDatabase, faEarthAmericas, faNetworkWired, faPlus, faServer, faSitemap, faTimes, faUser } from "@fortawesome/free-solid-svg-icons";
import VmGeneralChart from "./VmGeneralChart";

// 애플리케이션 섹션
const VmGeneral = ({ vm }) => {

    
  return (
    <>
        <div className='vm_detail_general_boxs'>
            <div className='detail_general_box'>
                <table className="table">
                    <tbody>
                        <tr>
                            <th >전원상태</th>
                            <td class="!text-blue-500 font-bold">{vm?.status}</td>
                        </tr>
                        <tr>
                            <th>IP 주소</th>
                            <td>{vm?.osSystem}</td>
                        </tr>
                        <tr>
                            <th>게스트 운영 체제</th>
                            <td>{vm?.osSystem}</td>
                        </tr>
                        <tr>
                            <th>게스트 에이전트</th>
                            <td>{vm?.guestInterfaceName}</td>
                        </tr>
                        <tr>
                            <th>업타임</th>
                            <td>{vm?.upTime}</td>
                        </tr>
                        <tr>
                            <th>FQDN</th>
                            <td>{vm?.fqdn}</td>
                        </tr>
                        <tr>
                            <th>실행 호스트</th>
                            <td>{vm?.hostVo?.name}</td>
                        </tr>
                        <tr>
                            <th>클러스터</th>
                            <td>
                                <div className='related_object'>
                                    <FontAwesomeIcon icon={faEarthAmericas} fixedWidth className="mr-0.5"/>
                                    <span className="text-blue-500 font-bold">{vm?.clusterVo?.name || '#'}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>호스트</th>
                            <td>
                                <div className='related_object'>
                                    <FontAwesomeIcon icon={faUser} fixedWidth className="mr-0.5"/>
                                    <span className="text-blue-500 font-bold"> {vm?.hostVo?.name || '#'}</span>
                                </div>
                            </td>   
                        </tr>
                        <tr>
                            <th>네트워크</th>
                            <td>
                                <div className='related_object'>
                                    <FontAwesomeIcon icon={faServer} fixedWidth className="mr-0.5"/>
                                    <span className="text-blue-500 font-bold"> {vm?.hostVo?.name || '#'}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>스토리지 도메인</th>
                            <td>
                                <div className='related_object'>
                                    <FontAwesomeIcon icon={faDatabase} fixedWidth className="mr-0.5"/>
                                    <span>{vm?.storageDomainVo?.name || ''}</span>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
                
            <div className='detail_general_box mr-8'>
                <div>VM 하드웨어</div>
                <table className="table">
                    <tbody>
                        <tr>
                            <th>CPU</th>
                            <td>{`${vm?.cpuTopologyCnt || '#'}(${vm?.cpuTopologySocket || '#'}:${vm?.cpuTopologyCore || '#'}:${vm?.cpuTopologyThread || '#'})`}</td>
                        </tr>
                        <tr>
                            <th>메모리</th>
                            <td>{vm?.memorySize ? Math.round(vm.memorySize / 1024 / 1024) + ' MB' : '#'}</td>
                        </tr>
                        <tr>
                            <th>하드 디스크1</th>
                            <td>300 GB | 씬 프로비전<br />{vm?.storageDomainVo?.name || '#'}</td>
                        </tr>
                        <tr>
                            <th>네트워크 어댑터</th>
                            <td>{vm?.nicVos?.[0]?.name || '#'}</td>
                        </tr>
                        <tr>
                            <th>칩셋/펌웨어 유형</th>
                            <td>{vm?.chipsetFirmwareType || '#'}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
                
            <div className='detail_general_mini_box'>
                <div>용량 및 사용량</div>
                <div className='capacity_outer'>
                    <div className='capacity'>
                        <div>CPU</div>
                        <div className='capacity_box'>
                            <div>{vm?.usageDto?.cpuPercent || '#'}% 사용됨</div>
                       
                            <div>{vm?.cpuTopologyCnt || '#'} CPU 할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>메모리</div>
                        <div className='capacity_box'>
                            <div>{vm?.memoryUsed || '#'}% 사용됨</div>
              
                            <div>{Math.round(vm?.memoryActual / 1024 / 1024) || '#'} MB 할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지</div>
                        <div className='capacity_box'>
                            <div>20% 사용됨</div>
                
                            <div>300 GB 할당됨</div>
                        </div>
                    </div>
                </div>  
            </div>

            {/* <div className='detail_general_mini_box'>
                <div>관련 개체</div>
                <div className='capacity_outer'>
                    <div className='capacity'>
                        <div>클러스터</div>
                        <div className='related_object'>
                            <FontAwesomeIcon icon={faTimes} fixedWidth />
                            <span className="text-blue-500 font-bold">{vm?.clusterVo?.name || '#'}</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>호스트</div>
                        <div className='related_object'>
                            <FontAwesomeIcon icon={faTimes} fixedWidth />
                            <span className="text-blue-500 font-bold">{vm?.hostVo?.name || '#'}</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>네트워크</div>
                        <div className='related_object'>
                            <FontAwesomeIcon icon={faTimes} fixedWidth />
                            <span>{vm?.nicVos?.[0]?.networkVo?.name || ''}</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지 도메인</div>
                        <div className='related_object'>
                            <FontAwesomeIcon icon={faTimes} />
                            <span>{vm?.storageDomainVo?.name || ''}</span>
                        </div>
                    </div>
                </div>
            </div> */}
        </div>

        <div className='detail_general_boxs_bottom'>

            <div className="vm-general-bottom-box">     
                <div className="vm_table_container">
                    <table className="table">
                        <tbody>
                            <tr>
                                <th>유형:</th>
                                <td>{vm?.osSystem || ''}</td>
                            </tr>
                            <tr>
                                <th>아키텍처:</th>
                                <td>{vm?.cpuArc || ''}</td>
                            </tr>
                            <tr>
                                <th>운영체제:</th>
                                <td>{vm?.osSystem || ''}</td>
                            </tr>
                            <tr>
                                <th>커널 버전:</th>
                                <td>{vm?.kernelVersion || ''}</td>
                            </tr>
                            <tr>
                                <th>시간대:</th>
                                <td>{vm?.timeOffset || 'KST(UTC+09:00)'}</td>
                            </tr>
                            <tr>
                                <th>로그인된 사용자:</th>
                                <td>{vm?.loggedInUser || ''}</td>
                            </tr>
                            <tr>
                                <th>콘솔 사용자:</th>
                                <td>{vm?.consoleUser || ''}</td>
                            </tr>
                            <tr>
                                <th>콘솔 클라이언트 IP:</th>
                                <td>{vm?.consoleClientIp || ''}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="vm-general-bottom-box">
                <div className="vm-general-box">
                    <FontAwesomeIcon icon={faComputer} className="mr-0.5"/>
                    <div className="mr-0.5">스냅샷</div>
                    <div>2</div>
                </div>

                <div className="vm-add-snapshot-btn">
                    <FontAwesomeIcon icon={faPlus} className="mr-0.5"/>
                    <div className="mr-0.5">스냅샷 생성</div>
                </div>
            </div>

            <div className="vm-general-bottom-box">
                <div className="vm-general-box">
                    <FontAwesomeIcon icon={faComputer} className="mr-0.5"/>
                    <div>디스크</div>
                </div>
                <div className="disk-bar">
                    <VmGeneralChart /> 
                </div>
            </div>
        </div>

     
    </>
  );
};

export default VmGeneral;
