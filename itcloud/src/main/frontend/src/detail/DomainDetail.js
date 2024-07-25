import React, { useState } from 'react';
import Modal from 'react-modal';

function DomainDetail({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const [activeTab, setActiveTab] = useState('general');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

   // 모달 관련 상태 및 함수
   const [activePopup, setActivePopup] = useState(null);

   const openPopup = (popupType) => {
     setActivePopup(popupType);
   };
 
   const closePopup = () => {
     setActivePopup(null);
   };
  return (
    <div className="content_detail_section">
      <div className="section_header">
        <div className="section_header_left">
          <span>네트워크 </span>
          <span>논리네트워크</span>
          <div>hosted_storage</div>
          <button>
            <i className="fa fa-exchange"></i>
          </button>
        </div>

        <div className="section_header_right">
          <div className="article_nav">
            <button>편집</button>
            <button>삭제</button> 
          </div>
        </div>

      </div>

      <div className="content_outer">
        <div className="content_header">
        <div className="content_header_left">
            <div
            className={activeTab === 'general' ? 'active' : ''}
            style={{ color: activeTab === 'general' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('general')}
            >
            일반
            </div>
            <div
            className={activeTab === 'vNIC_profile' ? 'active' : ''}
            style={{ color: activeTab === 'vNIC_profile' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('vNIC_profile')}
            >
            vNIC 프로파일
            </div>
            <div
            className={activeTab === 'cluster' ? 'active' : ''}
            style={{ color: activeTab === 'cluster' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('cluster')}
            >
            클러스터
            </div>
            <div
            className={activeTab === 'host' ? 'active' : ''}
            style={{ color: activeTab === 'host' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('host')}
            >
            호스트
            </div>
            <div
            className={activeTab === 'virtual_machine' ? 'active' : ''}
            style={{ color: activeTab === 'virtual_machine' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('virtual_machine')}
            >
            가상 머신
            </div>
            <div
            className={activeTab === 'template' ? 'active' : ''}
            style={{ color: activeTab === 'template' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('template')}
            >
            템플릿
            </div>
            <div
            className={activeTab === 'permission' ? 'active' : ''}
            style={{ color: activeTab === 'permission' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('permission')}
            >
            권한
            </div>
        </div>
        </div>


        {activeTab === 'general' && (
          <div className="section_content_outer">
            <div className="table_container_left">
              <table className="table">
                <tbody>
                  <tr>
                    <th>ID:</th>
                    <td>on20-ap01</td>
                  </tr>
                  <tr>
                    <th>설명:</th>
                    <td></td>
                  </tr>
                  <tr>
                    <th>상태:</th>
                    <td>실행 중</td>
                  </tr>
                  <tr>
                    <th>업타임:</th>
                    <td>11 days</td>
                  </tr>
                  <tr>
                    <th>템플릿:</th>
                    <td>Blank</td>
                  </tr>
                  <tr>
                    <th>운영 시스템:</th>
                    <td>Linux</td>
                  </tr>
                  <tr>
                    <th>펌웨어/장치의 유형:</th>
                    <td>
                      BIOS의 Q35 칩셋{' '}
                      <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i>
                    </td>
                  </tr>
                  <tr>
                    <th>우선 순위:</th>
                    <td>높음</td>
                  </tr>
                  <tr>
                    <th>최적화 옵션:</th>
                    <td>서버</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        )}

        {activeTab === 'vNIC_profile' && (
        <div id="detail_vnic_outer">
       
            <div className="pregroup_content">
                <div className="content_header_right">
                    <button onClick={() => openPopup('vnic_new_popup')}>새로 만들기</button>
                    <button onClick={() => openPopup('vnic_eidt_popup')}>편집</button>
                    <button>제거</button>
                </div>
                <div className="application_content_header">
                    <button><i className="fa fa-chevron-left"></i></button>
                    <div>1-1</div>
                    <button><i className="fa fa-chevron-right"></i></button>
                    <button><i className="fa fa-ellipsis-v"></i></button>
                </div>

                <div className="table_outer2">
                    <table>
                    <thead>
                        <tr>
                            <th>이름</th>
                            <th>네트워크</th>
                            <th>데이터 센터</th>
                            <th>호환 버전</th>
                            <th>QoS 이름</th>
                            <th>네트워크 필터</th>
                            <th>포트 미러링</th>
                            <th>통과</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>ovirtmgmt</td>
                            <td>ovirtmgmt</td>
                            <td>Default</td>
                            <td>4.7</td>
                            <td></td>
                            <td>wdsm-no-mac-spoofing</td>
                            <td></td>
                            <td>아니요</td>
                        </tr>
                    </tbody>
                    </table>
                </div>
            </div>
       </div>
       
        )}

        {activeTab === 'cluster' && (
        <div id="detail_cluster_outer">
       
            <div className="pregroup_content">
            <div className="content_header_right">
                <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>호환 버전</th>
                        <th>연결된 네트워크</th>
                        <th>네트워크 상태</th>
                        <th>필수 네트워크</th>
                        <th>네트워크 역할</th>
                        <th>설명</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Default</td>
                        <td>4.7</td>
                        <td><i className="fa fa-chevron-left"></i></td>
                        <td><i className="fa fa-chevron-left"></i></td>
                        <td ><i className="fa fa-chevron-left"></i></td>
                        <td style={{ textAlign: 'center' }}>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                        </td>
                        <td>The default server cluster</td>
                    </tr>
                </tbody>
                </table>
            </div>

            </div>
       </div>
       
        )}
        
        {activeTab === 'host' && (
        <div id="detail_host_outer">
       
            <div className="pregroup_content">
            <div className="content_header_right">
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
                </div>
                <div className="application_content_header">
                  <button>연결됨</button>
                  <button>연결 해제</button>
                </div>
                <div className="application_content_header">
                    <button><i className="fa fa-chevron-left"></i></button>
                    <div>1-1</div>
                    <button><i className="fa fa-chevron-right"></i></button>
                    <button><i className="fa fa-ellipsis-v"></i></button>
                </div>
            
            <div className="table_outer2">
                <table>
                  <thead>
                      <tr>
                          <th></th>
                          <th>이름</th>
                          <th>클러스터</th>
                          <th>데이터 센터</th>
                          <th>네트워크 장치 상태</th>
                          <th>비동기</th>
                          <th>네트워크 장치</th>
                          <th>속도</th>
                          <th>Rx</th>
                          <th>Tx</th>
                          <th>총 Rx</th>
                          <th>총 Tx</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                        <td><i className="fa fa-chevron-left"></i></td> 
                        <td>host01.ititinfo.com</td>
                        <td>Default</td>
                        <td>Default</td>
                        <td><i className="fa fa-chevron-left"></i></td>
                        <td></td>
                        <td>ens192</td>
                        <td>10000</td>
                        <td>118</td>
                        <td>1</td>
                        <td>25,353,174,284,042</td>
                        <td>77,967,054,294</td>
                      </tr>
                  </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}

        {activeTab === 'virtual_machine' && (
        <div id="detail_virtual_outer">
       
            <div className="pregroup_content">
              <div className="content_header_right">
                  <button>제거</button>
              </div>
              <div className="application_content_header">
                  <button>실행중</button>
                  <button>정지중</button>
                </div>
              <div className="application_content_header">
                  <button><i className="fa fa-chevron-left"></i></button>
                  <div>1-1</div>
                  <button><i className="fa fa-chevron-right"></i></button>
                  <button><i className="fa fa-ellipsis-v"></i></button>
              </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                      <th></th>
                      <th>이름</th>
                      <th>클러스터</th>
                      <th>IP 주소</th>
                      <th>vNIC 상태</th>
                      <th>vNIC</th>
                      <th>vNIC Rx</th>
                      <th>vNIC Tx</th>
                      <th>총 Rx</th>
                      <th>총 Tx</th>
                      <th>설명</th>
                      <th></th>

                    </tr>
                </thead>
                <tbody>
                  <tr>
                    <td style={{ textAlign: 'center' }}><i className="fa fa-chevron-left"></i></td> 
                    <td>HostedEngine</td>
                    <td>Default</td>
                    <td>192.168.0.08 fe80::2342</td>
                    <td><i className="fa fa-chevron-left"></i></td>
                    <td>vnet0</td>
                    <td>1</td>
                    <td>1</td>
                    <td>5,353,174,284</td>
                    <td>5,353,174,284</td>
                    <td>Hosted engine VM</td>
                    <td></td>
                  </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}

        {activeTab === 'template' && (
        <div id="detail_template_outer">
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>제거</button>
            </div>
            <div className="application_content_header">
                  <button><i className="fa fa-chevron-left"></i></button>
                  <div>1-1</div>
                  <button><i className="fa fa-chevron-right"></i></button>
                  <button><i className="fa fa-ellipsis-v"></i></button>
              </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                      <th>이름</th>
                      <th>버전</th>
                      <th>상태</th>
                      <th>클러스터</th>
                      <th>vNIC</th>
                      
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    {/*<td colSpan="8" style={{ textAlign: 'center' }}>표시할 항목이 없습니다</td>*/}
                      <td>test02</td>
                      <td>1</td>
                      <td>OK</td>
                      <td>Default</td>
                      <td>nic1</td>
                      
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
        </div>
        )}

        {activeTab === 'permission' && (
        <div id="detail_permission_outer">
            <div className="pregroup_content">
              <div className="content_header_right">
                <button onClick={() => openPopup('perm_boost')}>추가</button>
                <button>제거</button>
              </div>
              <div className="storage_right_btns">
                <span>Permission Filters:</span>
                <div>
                  <button>All</button>
                  <button>Direct</button>
                </div>
              </div>
              <div>
                <div className="application_content_header">
                  <button><i className="fa fa-chevron-left"></i></button>
                  <div>1-3</div>
                  <button><i className="fa fa-chevron-right"></i></button>
                  <button><i className="fa fa-ellipsis-v"></i></button>
                </div>
              </div>
              <div className="table_outer2">
                <table style={{ marginTop: 0 }}>
                  <thead>
                    <tr>
                      <th></th>
                      <th>사용자</th>
                      <th>인증 공급자</th>
                      <th>네임스페이스</th>
                      <th>역할</th>
                      <th>생성일</th>
                      <th>Inherited From</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td><i className="fa fa-user"></i></td>
                      <td>ovirtmgmt</td>
                      <td></td>
                      <td>*</td>
                      <td>SuperUser</td>
                      <td>2023.12.29 AM 11:40:58</td>
                      <td>(시스템)</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
        </div>
        )}

      </div>

      {/*vNIC 프로파일(새로만들기)팝업 */}
      <Modal
        isOpen={activePopup === 'vnic_new_popup'}
        onRequestClose={closePopup}
        contentLabel="새로만들기"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
                </div>
              </div>

              <div className="vnic_new_checkbox">
                <input type="checkbox" id="allow_all_users" checked />
                <label htmlFor="allow_all_users">모든 사용자가 이 프로파일을 사용하도록 허용</label>
              </div>

            </div>
              
            
              
            
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
       {/*vNIC 프로파일(편집)팝업 */}
       <Modal
        isOpen={activePopup === 'vnic_eidt_popup'}
        onRequestClose={closePopup}
        contentLabel="편집"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
                </div>
              </div>


            </div>
              
            
              
            
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
       {/*클러스터(네트워크 관리)팝업 -> 수정필요*/}
       <Modal
        isOpen={activePopup === 'cluster_network_popup'}
        onRequestClose={closePopup}
        contentLabel="네트워크 관리"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="net_keeper_popup">
          <div className="network_popup_header">
            <h1>네트워크 관리</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="network_popup_table">
                <table>
                  <thead>
                      <tr>
                          <th>이름</th>
                          <th>모두 할당</th>
                          <th>모두 필요</th>
                          <th>가상 머신 네트</th>
                          <th>관리</th>
                          <th>네트워크 출력</th>
                          <th>마이그레이션 네트워크</th>
                          <th>Gluster 네트워크</th>
                          <th>기본 라우팅</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                          <td>Default</td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings">네트워크 설정 저장</label>
                            </div>
                          </td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings">네트워크 설정 저장</label>
                            </div>
                          </td>
                          <td style={{textAlign:'center'}}><i class="fa fa-caret-up" style={{ fontSize:'0.5rem',color:'greenyellow'}}></i></td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings"> </label>
                            </div>
                          </td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings"> </label>
                            </div>
                          </td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings"> </label>
                            </div>
                          </td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings"> </label>
                            </div>
                          </td>
                          <td>
                            <div className="vnic_new_checkbox">
                              <input type="checkbox" id="network_settings" disabled checked />
                              <label htmlFor="network_settings"> </label>
                            </div>
                          </td>
                      </tr>
                  </tbody>
                </table>
            </div>
          


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      {/*호스트(호스트 네트워크 설정)*/}
      <Modal
        isOpen={activePopup === 'host_network_popup'}
        onRequestClose={closePopup}
        contentLabel="호스트 네트워크 설정"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>호스트 host01.ititinfo.com 네트워크 설정</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="host_network_contents_outer">
            <div className="host_network_header">
              <i class="fa fa-long-arrow-right"></i>
              <span>드래그하여 변경</span>
            </div>
            <div className="host_network_top">
              <div className="host_network_top_left">
                <div className="interface_logical">
                  <div>인터페이스</div>
                  <div>할당된 논리 네트워크</div>
                </div>
                <div className="interface_logical_icon">
                  <div>
                    <i class="fa fa-circle" style={{ fontSize:'0.1rem',color:'greenyellow', border: '1px solid white'}}></i>
                    <i class="fa fa-desktop"></i>
                    <div>ens192</div>
                  </div>
                  <div>
                    <i class="fa fa-arrows-h"></i>
                  </div>
                  <div>
                    <i class="fa fa-caret-up" style={{ fontSize:'0.5rem',color:'greenyellow'}}></i>
                    <div style={{fontWeight:'600'}}>ovirtmgmt</div>
                    <div>
                      <button><i class="fa fa-check-square-o"></i></button>
                      <button><i class="fa fa-check-square-o"></i></button>
                      <button><i class="fa fa-check-square-o"></i></button>
                      <button><i class="fa fa-check-square-o"></i></button>
                      <button><i class="fa fa-check-square-o"></i></button>
                      <button style={{border:'none'}}><i class="fa fa-check-square-o"></i></button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="host_network_top_right">
                <div className="host_right_btns">
                  <button>네트워크</button>
                  <button>레이블</button>
                </div>
                <div className="unassigned_logical">
                  <div  style={{background:'none'}}>할당되지 않은 논리 네트워크</div>
                  <div>필수</div>
                  <div>중요하지않음</div>
                </div>
                <div className="external_logical">
                  <div>외부 논리적 네트워크</div>
                  <i class="fa fa-info-circle"></i>
                </div>
              </div>
            </div>
            <div className="host_network_bottom">
              <div>
                <div className="vnic_new_checkbox">
                  <input type="checkbox" id="host_engine_connection" checked />
                  <label htmlFor="host_engine_connection">호스트와 Engine간의 연결을 확인</label>
                </div>
                <i className="fa fa-info-circle"></i>
              </div>
              <div>
                <div className="vnic_new_checkbox">
                  <input type="checkbox" id="network_settings" disabled checked />
                  <label htmlFor="network_settings">네트워크 설정 저장</label>
                </div>
                <i className="fa fa-info-circle"></i>
              </div>
            </div>
          </div>
          


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*권한(추가)팝업 */}
      <Modal
        isOpen={activePopup === 'perm_boost'}
        onRequestClose={closePopup}
        contentLabel="추가"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>사용자에게 권한 추가</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="power_radio_group">
            <input type="radio" id="user" name="option" defaultChecked />
            <label htmlFor="user">사용자</label>
            
            <input type="radio" id="group" name="option" />
            <label htmlFor="group">그룹</label>
            
            <input type="radio" id="all" name="option" />
            <label htmlFor="all">모두</label>
            
            <input type="radio" id="my_group" name="option" />
            <label htmlFor="my_group">내 그룹</label>
          </div>

          <div className="power_contents_outer">
            <div>
              <label htmlFor="search">검색:</label>
              <select id="search">
                <option value="default">Default</option>
              </select>
            </div>
            <div>
              <label htmlFor="namespace">네임스페이스:</label>
              <select id="namespace">
                <option value="default">Default</option>
              </select>
            </div>
            <div>
              <label htmlFor="placeholder" style={{ color: 'white' }}>.</label>
              <select id="placeholder">
                <option value="default">Default</option>
              </select>
            </div>
            <div>
              <div style={{ color: 'white' }}>.</div>
              <input type="submit" value="검색" />
            </div>
          </div>

          <div className="power_table">
            <table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>성</th>
                  <th>사용자 이름</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>dddddddddddddddddddddd</td>
                  <td>2024. 1. 17. PM 3:14:39</td>
                  <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div className="power_last_content">
            <label htmlFor="assigned_role">할당된 역할:</label>
            <select id="assigned_role" style={{ width: '65%' }}>
              <option value="default">UserRole</option>
            </select>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      
    </div>
    
    
  );
}

export default DomainDetail;
