import React, { useEffect, useState } from 'react';
import {useLocation } from 'react-router-dom';
import './css/Setting.css';
import Modal from 'react-modal';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faChevronLeft, faChevronRight, faChevronDown, faEllipsisV, faSearch
  , faFilter,
  faTimes,
  faHeart,
  faInfoCircle
} from '@fortawesome/free-solid-svg-icons'
import HeaderButton from '../button/HeaderButton';
import NetworkDetail from '../Network/NetworkDetail';
import Footer from '../footer/Footer';
import NavButton from '../navigation/NavButton';


const Setting = ({ }) => {
    //테이블 컴포넌트
    const sessionData = [
      {
        sessionId: '3204',
        username: 'admin',
        authProvider: 'internal-authz',
        userId: 'b5e54b30-a5f3-11ee-81fa-00163...',
        sourceIp: '192.168.0.218',
        sessionStartTime: '2024. 1. 19. PM 1:04:09',
        lastSessionActive: '2024. 1. 19. PM 4:45:55',
      },
    ];
    //
    const location = useLocation();
    const locationState = location.state; 
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);
    const [activeTab, setActiveTab] = useState('host');
    const [settingPopupOpen, setSettingPopupOpen] = useState(false);
    const [activePopup, setActivePopup] = useState(null);
    const [isNewRolePopupOpen, setIsNewRolePopupOpen] = useState(false);
    const [activeSettingForm, setActiveSettingForm] = useState('part');


    const handleTabClick = (tab) => {
      if (tab === 'app_settings') {
        setActiveTab(tab); // 설정 탭 클릭 시 모달 표시
      } else {
        setActiveTab(tab); // 다른 탭 클릭 시 기본 동작
      }
    };
  const openSettingPopup = () => {
    setSettingPopupOpen(true);
};
// 새로 만들기 버튼 클릭 시
const openNewRolePopup = () => {
  setIsNewRolePopupOpen(true); // 새 역할 팝업 열기
};

// 새 역할 팝업 닫기
const closeNewRolePopup = () => {
  setIsNewRolePopupOpen(false); // 새 역할 팝업 닫기
};
const closeSettingPopup = () => {
  setActiveTab('host'); // 모달을 닫기 위해 'host'로 탭을 변경
};
const handleSettingNavClick = (form) => {
  setActiveSettingForm(form);
};

const openPopup = (popupType) => {
  setActivePopup(popupType);
};
const closePopup = () => {
  setActivePopup(null);
};

    useEffect(() => {
        function adjustFontSize() {
            const width = window.innerWidth;
            const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
            document.documentElement.style.fontSize = fontSize + 'px';
        }

        // 창 크기가 변경될 때 adjustFontSize 함수 호출
        window.addEventListener('resize', adjustFontSize);

        // 컴포넌트가 마운트될 때 adjustFontSize 함수 호출
        adjustFontSize();

        // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
        return () => {
            window.removeEventListener('resize', adjustFontSize);
        };
    }, []);



     // nav 컴포넌트
     const sections = [
      { id: 'host', label: '활성 사용자 세션' },
      { id: 'user', label: '사용자' },
      { id: 'app_settings', label: '설정' },
      { id: 'user_sessionInfo', label: '계정설정' },
    ];
    // HeaderButton 컴포넌트
      const buttons = [
        { id: 'edit_btn', label: '버튼1', onClick: () => console.log('Edit button clicked') },
        { id: 'delete_btn', label: '버튼2', onClick: () => console.log('Delete button clicked') },
    ];

      return (
        <div id="section">
          {showNetworkDetail ? (
            <NetworkDetail />
          ) : (
            <>
              <HeaderButton
                title="관리"
                subtitle=" > 사용자 세션"
                additionalText="목록이름"
                buttons={buttons}
                popupItems={[]}
                uploadOptions={[]}
              />
  
            <div className="content_outer">
                <NavButton
                  sections={sections}
                  activeSection={activeTab}
                  handleSectionClick={handleTabClick}
                />
                 <div className="host_btn_outer">
                {/* 사용자 세션 */}
                {activeTab === 'host' && (
                    <>
                      <div className="content_header_right"> 
                          <button>세션 종료</button>
                      </div>

                      <div className="section_table_outer">
                          <Table columns={TableColumnsInfo.SESSIONS} data={sessionData}/>
                      </div>
                            
                    </>
                )}
                {/* 사용자 */}
                {activeTab === 'user' && (
                     <>
                     <div className="content_header_right"> 
                         <button>추가</button>
                         <button>삭제</button>
                         <button>태그설정</button>
                     </div>

                     <div className="section_table_outer">
                         <Table columns={TableColumnsInfo.SESSIONS} data={sessionData}/>
                     </div>
                           
                   </>
                )}
                {/* 설정 */}
                {activeTab === 'app_settings' && !isNewRolePopupOpen &&(
                  <Modal
                     isOpen={true}
                     onRequestClose={closeSettingPopup}
                     contentLabel="설정"
                     className="Modal"
                     overlayClassName="Overlay"
                     shouldCloseOnOverlayClick={true}
                 >
                     <div className="setting_setting_popup">
                         <div className="popup_header">
                             <h1>설정</h1>
                             <button onClick={closeSettingPopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                         </div>
         
                         <div className="network_new_nav">
                             <div id="setting_part_btn" className={activeSettingForm === 'part' ? 'active' : ''} onClick={() => handleSettingNavClick('part')}>역할</div>
                             <div id="setting_system_btn" className={activeSettingForm === 'system' ? 'active' : ''} onClick={() => handleSettingNavClick('system')}>시스템 권한</div>
                             <div id="setting_schedule_btn" className={activeSettingForm === 'schedule' ? 'active' : ''} onClick={() => handleSettingNavClick('schedule')}>스케줄링 정책</div>
                             <div id="setting_instant_btn" className={activeSettingForm === 'instant' ? 'active' : ''} onClick={() => handleSettingNavClick('instant')}>인스턴스 유형</div>
                             <div id="setting_mac_btn" className={activeSettingForm === 'mac' ? 'active' : ''} onClick={() => handleSettingNavClick('mac')}>MAC주소 풀</div>
                         </div>
         
                         {activeSettingForm === 'part' && (
                             <form id="setting_part_form">
                                 <div>보기</div>
                                 <div className="setting_part_nav">
                                     <div className="radio_toolbar">
                                         <div>
                                             <input type="radio" id="all_roles" name="roles" value="all" defaultChecked />
                                             <label htmlFor="all_roles">모든역할</label>
                                         </div>
                                         <div>
                                             <input type="radio" id="admin_roles" name="roles" value="admin" />
                                             <label htmlFor="admin_roles">관리자 역할</label>
                                         </div>
                                         <div>
                                             <input type="radio" id="user_roles" name="roles" value="user" />
                                             <label htmlFor="user_roles">사용자 역할</label>
                                         </div>
                                     </div>
         
                                     <div className="setting_buttons">
                                         <div id="setting_part_new_btn" onClick={() => openPopup('newRole')}>새로 만들기</div>
                                         <div>편집</div>
                                         <div>복사</div>
                                         <div>삭제</div>
                                     </div>
                                 </div>
         
                                 <div className="setting_part_table_outer">
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-36</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th></th>
                                                 <th></th>
                                                 <th>이름</th>
                                                 <th>설명</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>dddddddddddddddddddddd</td>
                                                 <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                             </tr>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>dddddddddddddddddddddd</td>
                                                 <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
                             </form>
                         )}
         
                         {activeSettingForm === 'system' && (
                             <form id="setting_system_form">
                                 <div className="setting_part_nav">
                                     <div className="radio_toolbar">
                                         <div>
                                             <input type="radio" id="all_roles" name="roles" value="all" defaultChecked />
                                             <label htmlFor="all_roles">모든역할</label>
                                         </div>
                                         <div>
                                             <input type="radio" id="admin_roles" name="roles" value="admin" />
                                             <label htmlFor="admin_roles">관리자 역할</label>
                                         </div>
                                         <div>
                                             <input type="radio" id="user_roles" name="roles" value="user" />
                                             <label htmlFor="user_roles">사용자 역할</label>
                                         </div>
                                     </div>
         
                                     <div className="setting_buttons">
                                         <div id="setting_system_add_btn" onClick={() => openPopup('addSystemRole')}>추가</div>
                                         <div>제거</div>
                                     </div>
                                 </div>
         
                                 <div className="setting_part_table_outer">
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-3</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th></th>
                                                 <th>사용자</th>
                                                 <th>인증 공급자</th>
                                                 <th>네임 스페이스</th>
                                                 <th>역할</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td></td>
                                                 <td>*</td>
                                                 <td>SuperUser</td>
                                             </tr>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td></td>
                                                 <td>*</td>
                                                 <td>SuperUser</td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
                             </form>
                         )}
         
                         {activeSettingForm === 'schedule' && (
                             <form id="setting_schedule_form">
                                 <div className="setting_part_nav">
                                     <div className="setting_buttons">
                                         <div id="setting_schedule_new_btn" onClick={() => openPopup('newSchedule')}>새로 만들기</div>
                                         <div>편집</div>
                                         <div>복사</div>
                                         <div>제거</div>
                                         <div id="setting_schedule_unit">정책 유닛 관리</div>
                                     </div>
                                 </div>
         
                                 <div className="setting_part_table_outer">
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-5</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th></th>
                                                 <th>이름</th>
                                                 <th>설명</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td>ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                             </tr>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td></td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
                             </form>
                         )}
                 
                         {activeSettingForm === 'instant' && (
                             <form id="setting_instant_form">
                                 <div className="setting_part_nav">
                                     <div className="setting_buttons">
                                         <div id="setting_instant_new_btn">새로 만들기</div>
                                         <div>편집</div>
                                         <div>제거</div>
                                     </div>
                                 </div>
         
                                 <div className="setting_part_table_outer">
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-5</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th>이름</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td>ovirt-administrator</td>
                                             </tr>
                                             <tr>
                                                 <td>ovirt-administrator</td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
                             </form>
                         )}
         
                         {activeSettingForm === 'mac' && (
                             <form id="setting_mac_form">
                                 <div className="setting_part_nav">
                                     <div className="setting_buttons">
                                         <div id="setting_mac_new_btn" onClick={() => openPopup('macNew')}>새로 만들기</div>
                                         <div id="setting_mac_edit_btn" onClick={() => openPopup('macEdit')}>편집</div>
                                         <div>제거</div>
                                     </div>
                                 </div>
         
                                 <div className="setting_part_table_outer" style={{ borderBottom: 'none' }}>
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-5</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th></th>
                                                 <th>이름</th>
                                                 <th>설명</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td>ovirt-administrator</td>
                                             </tr>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td>ovirt-administrator</td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
         
                                 <div className="setting_part_table_outer">
                                     <div className="application_content_header">
                                         <button><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>
                                         <div>1-5</div>
                                         <button><FontAwesomeIcon icon={faChevronRight} fixedWidth/></button>
                                         <button><FontAwesomeIcon icon={faEllipsisV} fixedWidth/></button>
                                     </div>
         
                                     <table className="network_new_cluster_table">
                                         <thead>
                                             <tr>
                                                 <th></th>
                                                 <th>사용자</th>
                                                 <th>인증 공급자</th>
                                                 <th>네임 스페이스</th>
                                                 <th>역할</th>
                                                 <th>생성일</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td>ovirt-administrator</td>
                                                 <td>*</td>
                                                 <td>ovirt-adm</td>
                                                 <td>2023.12.29AM11:40:58</td>
                                             </tr>
                                             <tr>
                                                 <td><FontAwesomeIcon icon={faHeart} fixedWidth/></td>
                                                 <td>ovirt-administrator</td>
                                                 <td>ovirt-administrator</td>
                                                 <td>*</td>
                                                 <td>ovirt-adm</td>
                                                 <td>2023.12.29AM11:40:58</td>
                                             </tr>
                                         </tbody>
                                     </table>
                                 </div>
                             </form>
                         )}
         
                         <div className="edit_footer">
                             <button style={{ display: 'none' }} onClick={closeSettingPopup}></button>
                             <button>OK</button>
                             <button onClick={closeSettingPopup}>취소</button>
                         </div>
         
                     </div>
                  </Modal>
                )}

                    {/* 설정팝업 역할(새로만들기 팝업) */}
                    <Modal

            isOpen={activePopup === 'newRole'}
            onRequestClose={closePopup}
            contentLabel="새로 만들기"
            className="Modal"
              overlayClassName="Overlay newRolePopupOverlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="setting_part_new_popup" >
                <div className="popup_header">
                    <h1>새 역할</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="set_part_text">
                    <div>
                        <div>
                            <label htmlFor="new_role_name">이름</label><br />
                            <input type="text" id="new_role_name" value="test02" />
                        </div>
                        <div>
                            <label htmlFor="new_role_desc">설명</label><br />
                            <input type="text" id="new_role_desc" value="test02" />
                        </div>
                    </div>
                    <span>계정 유형:</span>
                    <div>
                        <div>
                            <input type="radio" id="new_role_user" name="new_role_type" value="user" checked />
                            <label htmlFor="new_role_user" style={{ marginRight: '0.3rem' }}>사용자</label>
                        </div>
                        <div>
                            <input type="radio" id="new_role_admin" name="new_role_type" value="admin" />
                            <label htmlFor="new_role_admin">관리자</label>
                        </div>
                    </div>
                </div>

                <div className="set_part_checkboxs">
                    <span>작업 허용을 위한 확인란</span>
                    <div className="set_part_buttons">
                        <div>모두 확장</div>
                        <div>모두 축소</div>
                    </div>
                    <div className="checkbox_toolbar">
                        <div>
                            <input type="checkbox" id="new_role_system" name="new_role_permissions" />
                            <label htmlFor="new_role_system">시스템</label>
                        </div>
                        <div>
                            <input type="checkbox" id="new_role_network" name="new_role_permissions" />
                            <label htmlFor="new_role_network">네트워크</label>
                        </div>
                        <div>
                            <input type="checkbox" id="new_role_template" name="new_role_permissions" />
                            <label htmlFor="new_role_template">템플릿</label>
                        </div>
                        <div>
                            <input type="checkbox" id="new_role_vm" name="new_role_permissions" />
                            <label htmlFor="new_role_vm">가상머신</label>
                        </div>
                        <div>
                            <input type="checkbox" id="new_role_vm_pool" name="new_role_permissions" />
                            <label htmlFor="new_role_vm_pool">가상머신 풀</label>
                        </div>
                        <div>
                            <input type="checkbox" id="new_role_disk" name="new_role_permissions" />
                            <label htmlFor="new_role_disk">디스크</label>
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

                    {/* 설정팝업 시스템권한(추가 팝업) */}
                    <Modal
            isOpen={activePopup === 'addSystemRole'}
            onRequestClose={closePopup}
            contentLabel="추가"
            className="Modal"
            overlayClassName="Overlay newRolePopupOverlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="setting_system_new_popup">
                <div className="popup_header">
                    <h1>사용자에게 권한 추가</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="power_radio_group">
                    <input type="radio" id="user" name="option" defaultChecked />
                    <label htmlFor="user">사용자</label>
                    
                    <input type="radio" id="group" name="option" />
                    <label htmlFor="group">그룹</label>
                </div>

                <div className="power_contents_outer">
                    <div>
                        <label htmlFor="cluster">검색:</label>
                        <select id="cluster">
                            <option value="default">Default</option>   
                        </select>
                    </div>
                    <div>
                        <label htmlFor="cluster">네임스페이스:</label>
                        <select id="cluster">
                            <option value="default">Default</option>   
                        </select>
                    </div>
                    <div>
                        <label style={{ color: "white" }}>.</label>
                        <input type="text" id="name" value="test02" />
                    </div>
                    <div>
                        <div style={{ color: "white" }}>.</div>
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

                <div className="power_last_content" style={{ padding: "0.1rem 0.3rem" }}>
                    <label htmlFor="cluster">할당된 역할:</label>
                    <select id="cluster" style={{ width: "65%" }}>
                        <option value="default">UserRole</option>   
                    </select>
                </div>

                <div className="edit_footer">
                    <button style={{ display: "none" }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>
                    </Modal>
                    
                    {/* 설정팝업 스케줄링정책(새로만들기 팝업) */}
                    <Modal
            isOpen={activePopup === 'newSchedule'}
            onRequestClose={closePopup}
            contentLabel="새로 만들기"
            className="Modal"
              overlayClassName="Overlay newRolePopupOverlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="setting_schedule_new_popup">
                <div className="popup_header">
                    <h1>새 스케줄링 정책</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className="set_part_text" style={{ borderBottom: 'none' }}>
                    <div>
                        <div>
                            <label htmlFor="name">이름</label><br />
                            <input type="text" id="name" defaultValue="test02" />
                        </div>
                        <div>
                            <label htmlFor="name">설명</label><br />
                            <input type="text" id="name" defaultValue="test02" />
                        </div>
                    </div>
                </div>

                <div className="set_schedule_contents">
                    <div className="set_schedule_contents_left">
                        <div>
                            <h1>필터 모듈</h1>
                            <div style={{ fontSize: '0.26rem' }}>드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                            <div></div>
                        </div>
                        <div>
                            <h1>필터 모듈</h1>
                            <div style={{ fontSize: '0.26rem' }}>드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                            <div></div>
                        </div>
                    </div>
                    <div className="set_schedule_contents_right">
                        <div>
                            <span>비활성화된 필터</span>
                            <div className="schedule_boxs">
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                            </div>
                        </div>
                        <div>
                            <span>비활성화된 가중치</span>
                            <div className="schedule_boxs">
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                                <div>Migration</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="set_schedule_balance">
                    <label htmlFor="network_port_security">
                        비활성화된 필터<FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }}/>
                    </label>
                    <select>
                        <option value="default">활성화</option>   
                    </select>
                </div>

                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>

                    </Modal>

                    {/* 설정팝업 MAC주소 풀(추가 팝업) */}
                    <Modal
            isOpen={activePopup === 'macNew'}
            onRequestClose={closePopup}
            contentLabel="새로만들기"
            className="Modal"
                overlayClassName="Overlay newRolePopupOverlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="setting_mac_new_popup">
                <div className="popup_header">
                    <h1>새 MAC주소 풀</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className="setting_mac_textboxs">
                    <div>
                        <span>이름</span>
                        <input type="text" />
                    </div>
                    <div>
                        <span>설명</span>
                        <input type="text" />
                    </div>
                </div>
                <div className="setting_mac_checkbox">
                    <input type="checkbox" id="allow_duplicate" name="allow_duplicate" />
                    <label htmlFor="allow_duplicate">중복 허용</label>
                </div>
                
                <div className="network_parameter_outer">
                    <span>MAC 주소 범위</span>
                    <div style={{ marginBottom: '0.2rem' }}>
                        <div>
                            <span style={{ marginRight: '0.3rem' }}>범위 시작</span>
                            <input type="text" />
                        </div>
                        <div>
                            <span>범위 끝</span>
                            <input type="text" />
                        </div>
                        <div id="buttons">
                            <button>+</button>
                            <button>-</button>
                        </div>
                    </div>
                    <div>
                        MAC수 : 해당없음
                    </div>
                </div>

                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>

                    </Modal>
                    {/* 설정팝업 MAC주소 풀(편집 팝업) */}
                    <Modal
            isOpen={activePopup === 'macEdit'}
            onRequestClose={closePopup}
            contentLabel="편집"
            className="Modal"
               overlayClassName="Overlay newRolePopupOverlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="setting_mac_edit_popup">
                <div className="popup_header">
                    <h1>새 MAC주소 풀</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className="setting_mac_textboxs">
                    <div>
                        <span>이름</span>
                        <input type="text" />
                    </div>
                    <div>
                        <span>설명</span>
                        <input type="text" />
                    </div>
                </div>
                <div className="setting_mac_checkbox">
                    <input type="checkbox" id="allow_duplicate" name="allow_duplicate" />
                    <label htmlFor="allow_duplicate">중복 허용</label>
                </div>
                
                <div className="network_parameter_outer">
                    <span>MAC 주소 범위</span>
                    <div style={{ marginBottom: '0.2rem' }}>
                        <div>
                            <span style={{ marginRight: '0.3rem' }}>범위 시작</span>
                            <input type="text" />
                        </div>
                        <div>
                            <span>범위 끝</span>
                            <input type="text" />
                        </div>
                        <div id="buttons">
                            <button>+</button>
                            <button>-</button>
                        </div>
                    </div>
                    <div>
                        MAC수 : 해당없음
                    </div>
                </div>

                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>

                    </Modal>
              
              {/* 계정설정 */}
                {activeTab === 'user_sessionInfo' && (
                 <Modal
                 isOpen={true}
                 onRequestClose={closeSettingPopup}
                 contentLabel="계정 설정"
                 className="Modal"
                 overlayClassName="Overlay"
                 shouldCloseOnOverlayClick={true}
             >
                 <div className="user_sessionInfo_popup">
                     <div className="popup_header">
                         <h1>계정 설정</h1>
                         <button onClick={closeSettingPopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                     </div>
     
                     <div className="network_new_nav">
                         <div id="setting_part_btn" className={activeSettingForm === 'part' ? 'active' : ''} onClick={() => handleSettingNavClick('part')}>일반</div>
                         <div id="setting_system_btn" className={activeSettingForm === 'system' ? 'active' : ''} onClick={() => handleSettingNavClick('system')}>Confirmations</div>
                     </div>
     
                     {activeSettingForm === 'part' && (
                         <form id="setting_part_form">
                             <div>보기</div>
                          
     
                           
                         </form>
                     )}
     
                     {activeSettingForm === 'system' && (
                        <></>
                     )}
     
                
     
                     <div className="edit_footer">
                         <button style={{ display: 'none' }} onClick={closeSettingPopup}></button>
                         <button>OK</button>
                         <button onClick={closeSettingPopup}>취소</button>
                     </div>
     
                 </div>
              </Modal>
              )}



              </div>
    
              <Footer/>
              </div>
    
            </>
          )}
        </div>
      );
    };
export default Setting;