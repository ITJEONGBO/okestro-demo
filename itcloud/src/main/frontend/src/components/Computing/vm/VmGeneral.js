import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";

// 애플리케이션 섹션
const VmGeneral = ({ vm }) => {

  
    return (
        <>
        <div className='vm_detail_general_boxs'>
            <div className='detail_general_box'>
                <table className="table">
                    <tbody>
                    <tr>
                        <th>전원상태</th>
                        <td>전원 켜짐</td>
                    </tr>
                    <tr>
                        <th>게스트 운영 체제</th>
                        <td>Red Hat Enterprise Linux 8.x x64</td>
                    </tr>
                    <tr>
                        <th>게스트 에이전트</th>
                        <td>실행 중, 버전 : 123456(최신)</td>
                    </tr>
                    <tr>
                        <th>업타임</th>
                        <td>28 days</td>
                    </tr>
                    <tr>
                        <th>FQDN</th>
                        <td>on20-ap01</td>
                    </tr>
                    <tr>
                        <th>실행 호스트</th>
                        <td>클러스터 내 호스트</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div className='detail_general_box'>
                
                <div>VM하드웨어</div>
                <table className="table">
                    <tbody>
                    <tr>
                        <th>CPU</th>
                        <td>2(2:1:1)</td>
                    </tr>
                    <tr>
                        <th>메모리</th>
                        <td>16GB</td>
                    </tr>
                    <tr>
                        <th>하드 디스크1</th>
                        <td>300 GB | 씬 프로비전<br/>hosted-engine</td>
                    </tr>
                    <tr>
                        <th>네트워트 어댑터1</th>
                        <td>ovirt-mgmgt</td>
                    </tr>
                    <tr>
                        <th>칩셋/펌웨어 유형</th>
                        <td>BIOS Q35 칩셋</td>
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
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>메모리</div>
                        <div className='capacity_box'>
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지</div>
                        <div className='capacity_box'>
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                </div>  
            </div>

            <div className='detail_general_mini_box'>
                <div>관련개체</div>
                <div className='capacity_outer'>
                    <div className='capacity'>
                        <div>클러스터</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span class="text-blue-500 font-bold">ITITINFO</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>호스트</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span class="text-blue-500 font-bold">192.168.0.4</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>네트워크</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span>ovirt-mgmt</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지 도메인</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span >hosted-engine</span>
                        </div>
                    </div>
                
            </div>
            </div>
        </div>

        <div className='detail_general_boxs_bottom'>
        <div className="tables">
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>유형:</th>
                    <td>Linux</td>
                  </tr>
                  <tr>
                    <th>아키텍처:</th>
                    <td>x86_64</td>
                  </tr>
                  <tr>
                    <th>운영체제:</th>
                    <td>ContOs Linux7</td>
                  </tr>
                  <tr>
                    <th>커널버전:</th>
                    <td>3.10.38343344</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>시간대:</th>
                    <td>KST(UTC+09:00)</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>로그인된 사용자:</th>
                    <td>root</td>
                  </tr>
                  <tr>
                    <th>콘솔 사용자:</th>
                    <td></td>
                  </tr>
                  <tr>
                    <th>콘솔 클라이언트IP:</th>
                    <td></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

        </div>
        </>
    );
  };
  
  export default VmGeneral;