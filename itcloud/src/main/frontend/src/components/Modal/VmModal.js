import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle, faChevronCircleRight } from '@fortawesome/free-solid-svg-icons';

const VmModal = ({ 
    isOpen, 
    onRequestClose, 
    onSubmit, 
    vmId 
}) => {
  const [activeSection, setActiveSection] = useState('common_outer');

  const handleSectionChange = (section) => setActiveSection(section);

  const handleFormSubmit = (e) => {
    e.preventDefault();
    onSubmit(); // 부모 컴포넌트로 폼 제출 시 데이터를 전달
  };

  const [selectedModalTab, setSelectedModalTab] = useState('common');
  
  // 추가 모달
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
  const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
    // 새로만들기->초기실행 화살표 누르면 밑에열리기
    const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
    const toggleDomainHiddenBox = () => {
      setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
    };
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="가상머신 생성"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vm_edit_popup">
        <div className="popup_header">
          <h1>가상머신 생성</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className='vm_edit_popup_content'>
                <div className='vm_new_nav' style={{
                    fontSize: '0.33rem',
                    height: '71vh',
                    width: '30%',
                    backgroundColor: '#FAFAFA',
                    borderRight: '1px solid #ddd',
                    boxShadow: '1px 0 5px rgba(0, 0, 0, 0.1)',
                    fontWeight: 800
                }}>
                    <div
                    id="common_tab"
                    className={selectedModalTab === 'common' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('common')}
                    >
                    일반
                    </div>
                    <div
                    id="system_tab"
                    className={selectedModalTab === 'system' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('system')}
                    >
                    시스템
                    </div>
                    <div
                    id="beginning_tab"
                    className={selectedModalTab === 'beginning' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('beginning')}
                    >
                    초기 실행
                    </div>
                    <div
                    id="host_tab"
                    className={selectedModalTab === 'host' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('host')}
                    >
                    호스트
                    </div>
                    <div
                    id="ha_mode_tab"
                    className={selectedModalTab === 'ha_mode' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('ha_mode')}
                    >
                    고가용성
                    </div>
                    {/* <div
                    id="res_alloc_tab"
                    className={selectedModalTab === 'res_alloc' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('res_alloc')}
                    >
                    리소스 할당
                    </div> */}
                    <div
                    id="boot_option_tab"
                    className={selectedModalTab === 'boot_outer' ? 'active-tab' : 'inactive-tab'}
                    onClick={() => setSelectedModalTab('boot_outer')}
                    >
                    부트 옵션
                    </div>

                </div>

            {/* 탭 내용 */}
            <div className="vm_edit_select_tab">
              <div className="edit_first_content">
                          <div className='mb-1'>
                                <label htmlFor="cluster">클러스터</label>
                                <select id="cluster">
                                    <option value="default">Default</option>
                                </select>
                                <div className='datacenter_span'>데이터센터 Default</div>
                            </div>

                            <div className='disabled'>
                                <label htmlFor="template" style={{ color: 'gray' }}>템플릿</label>
                                <select id="template" disabled>
                                    <option value="test02">test02</option>
                                </select>
                            </div>
                            <div>
                                <label htmlFor="os">운영 시스템</label>
                                <select id="os">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div>
                                <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                <select id="firmware">
                                    <option value="bios">BIOS의 Q35 칩셋</option>
                                </select>
                            </div>
                            <div style={{ marginBottom: '2%' }}>
                                <label htmlFor="optimization">최적화 옵션</label>
                                <select id="optimization">
                                    <option value="server">서버</option>
                                </select>
                            </div>
                </div>
                {selectedModalTab === 'common' && 
                <>
            
                        <div className="edit_second_content mb-1">
                            <div>
                                <label htmlFor="name">이름</label>
                                <input type="text" id="name" value="test02" />
                            </div>
                            <div>
                                <label htmlFor="description">설명</label>
                                <input type="text" id="description" />
                            </div>
                            <div>
                                <label htmlFor="comment">코멘트</label>
                                <input type="text" id="comment" />
                            </div>
                        </div>
                        <div className='px-1 font-bold'>인스턴스 이미지</div>
                        <div className="edit_third_content" style={{ borderBottom: '1px solid gray', marginBottom:'0.2rem' }}>
                            <div>
                                <span>adfdsfadsf</span>
                            </div>
                            <div>
                            <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
                                <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>
                       
                        <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                            
                            <div className='edit_fourth_content_select flex'>
                                <label htmlFor="network_adapter">nic1</label>
                                <select id="network_adapter">
                                    <option value="default">Default</option>
                                </select>
                            </div>
                            <div className='flex'>
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                </>
                }
                {selectedModalTab === 'system' && 
                <>
                  
                    <div className="edit_second_content">
                            <div>
                                <label htmlFor="memory_size">메모리 크기</label>
                                <input type="text" id="memory_size" value="2048 MB" readOnly />
                            </div>
                            <div>
                                <div>
                                    <label htmlFor="max_memory">최대 메모리</label>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                </div>
                                <input type="text" id="max_memory" value="8192 MB" readOnly />
                            </div>

                            <div>
                                <div>
                                    <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                </div>
                                <input type="text" id="actual_memory" value="2048 MB" readOnly />
                            </div>

                            <div>
                                <div>
                                    <label htmlFor="total_cpu">총 가상 CPU</label>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                </div>
                                <input type="text" id="total_cpu" value="1" readOnly />
                            </div>
                            
                    </div>
                </>
                }
                {selectedModalTab === 'beginning' && 
                <>
                  <div className='p-1.5'>
                    <div className='checkbox_group mb-1.5'>
                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                        <label htmlFor="enableBootMenu">Cloud-lnit</label>
                    </div>

                    <div>
                      <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox}fixedWidth/>
                      <span>사용자 지정 스크립트</span>
                      <div className='mt-0.5' id="domain_hidden_box2" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                        <textarea name="content" cols="40" rows="8" >DVD</textarea>

                      </div>
                    </div>
                  </div>
                </>
                }
                {selectedModalTab === 'host' && 
                <>
              
                  <div id="host_second_content">
                            <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                            <div className="form_checks">
                                <div>
                                    <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                    <label className="form-check-label" htmlFor="flexRadioDefault1">
                                        클러스터 내의 호스트
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                        <label className="form-check-label" htmlFor="flexRadioDefault2">
                                            특정 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <select id="specific_host_select" disabled>
                                            <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            
                  </div>
                  <div id="host_third_content">
                            <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                            <div>
                                <div>
                                    <span>마이그레이션 모드</span>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                </div>
                                <select id="migration_mode">
                                    <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                </select>
                            </div>
                            <div>
                                <div>
                                    <span>마이그레이션 정책</span>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                </div>
                                <select id="migration_policy">
                                    <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                </select>
                            </div>
                            
                            <div>
                                <div>
                                    <span>마이그레이션 병행</span>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                </div>
                                <select id="parallel_migrations" readOnly>
                                    <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                </select>
                            </div>
                            <div className='network_checkbox_type1 disabled'>
                                <label htmlFor="memory_size">마이그레이션 병행 개수</label>
                                <input type="text" id="memory_size" value="" readOnly disabled/>
                            </div>
                            
                  </div>
                </>
                }
                {selectedModalTab === 'ha_mode' && 
                <>
                
                  <div id="ha_mode_second_content">
                            <div className="checkbox_group">
                                <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                <label className="check_label" htmlFor="ha_mode_box">
                                    고가용성
                                </label>
                            </div>
                            <div>
                                <div>
                                    <span>가상 머신 임대 대상 스토리지 도메인</span>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                </div>
                                <select id="no_lease" disabled>
                                    <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                </select>
                            </div>
                            <div>
                                <div>
                                    <span>재개 동작</span>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                </div>
                                <select id="force_shutdown">
                                    <option value="강제 종료">강제 종료</option>
                                </select>
                            </div>
                            <div className="ha_mode_article">
                                <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                <div>
                                    <span>우선 순위</span>
                                    <select id="priority">
                                        <option value="낮음">낮음</option>
                                    </select>
                                </div>
                            </div>

                            
                        </div>
                </>
                }
                {/* {selectedModalTab === 'res_alloc' && 
                <>
        <div className="res_second_content">
                            <div className="cpu_res">
                                <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                <div className='cpu_res_box'>
                                    <span>CPU 프로파일</span>
                                    <select id="watchdog_action">
                                        <option value="없음">Default</option>
                                    </select>
                                </div>
                                <div className='cpu_res_box'>
                                    <span>CPU 공유</span>
                                    <div id="cpu_sharing">
                                        <select id="watchdog_action" style={{ width: '63%' }}>
                                            <option value="없음">비활성화됨</option>
                                        </select>
                                        <input type="text" value="0" disabled />
                                    </div>
                                </div>
                                <div className='cpu_res_box'>
                                    <span>CPU Pinning Policy</span>
                                    <select id="watchdog_action">
                                        <option value="없음">None</option>
                                    </select>
                                </div>
                                <div className='cpu_res_box'>
                                    <div>
                                        <span>CPU 피닝 토폴로지</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                    <input type="text" disabled />
                                </div>
                            </div>

                            

                            <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                            <div id="threads">
                                <div className='checkbox_group'>
                                    <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                    <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                </div>
                                <div className='text_icon_box'>
                                    <input type="text" />
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                </div>
                            </div>

                                
                        </div>

                </>
                } */}
                {selectedModalTab === 'boot_outer' && 
                <>  
                  <div className='boot_outer_content'>
                    <div className="cpu_res">
                                <span style={{ fontWeight: 600 }}>부트순서:</span>
                                <div className='cpu_res_box'>
                                    <span>첫 번째 장치</span>
                                    <select id="watchdog_action">
                                        <option value="없음">하드디스크</option>
                                    </select>
                                </div>
                                <div className='cpu_res_box'>
                                    <span>두 번째 장치</span>
                                    <select id="watchdog_action">
                                        <option value="없음">Default</option>
                                    </select>
                                </div>
                    </div>
                    <div id="boot_checkboxs">
                        <div>
                            <div className='checkbox_group'>
                                <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                            </div>
                            <div className='text_icon_box'>
                                <input type="text" disabled />
                                <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                            </div>
                        </div>

                        <div className='checkbox_group mb-1.5'>
                            <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                            <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                        </div>
                    </div>
                  </div>
                </>
                }
            </div>
            </div>

        <div className="edit_footer">
          <button type="submit">OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmModal;


    {/* 가상머신( 새로만들기)팝업 */}   
{/* <Modal
isOpen={activePopup === 'vm_new'}
onRequestClose={closePopup}
contentLabel="가상머신 편집"
className="edit_popup"
overlayClassName="edit_popup_outer"
shouldCloseOnOverlayClick={false}
>
<div id="edit_popup">
    <div className="popup_header">
        <h1>가상머신 생성</h1>
        <button onClick={closePopup}>
        <FontAwesomeIcon icon={faTimes} fixedWidth />
        </button>
    </div>
    
    <div className="edit_body">
    <div className="edit_aside">
        <div
            className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
            id="common_outer_btn"
            onClick={() => handleSectionChange('common_outer')}
        >
            <span>일반</span>
        </div>
        <div
            className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
            id="system_outer_btn"
            onClick={() => handleSectionChange('system_outer')}
        >
            <span>시스템</span>
        </div>
        <div
            className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
            id="host_outer_btn"
            onClick={() => handleSectionChange('host_outer')}
        >
            <span>호스트</span>
        </div>
        <div
            className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
            id="ha_mode_outer_btn"
            onClick={() => handleSectionChange('ha_mode_outer')}
        >
            <span>고가용성</span>
        </div>
        <div
            className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
            id="res_alloc_outer_btn"
            onClick={() => handleSectionChange('res_alloc_outer')}
        >
            <span>리소스 할당</span>
        </div>
        <div
            className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
            id="boot_outer_btn"
            onClick={() => handleSectionChange('boot_outer')}
        >
            <span>부트 옵션</span>
        </div>
        
        </div>


            <form action="#">
                {/* 일반 */}
                // <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                //     <div className="edit_first_content">
                //         <div>
                //             <label htmlFor="cluster">클러스터</label>
                //             <select id="cluster">
                //                 <option value="default">Default</option>
                //             </select>
                //             <div>데이터센터 Default</div>
                //         </div>

                //         <div>
                //             <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                //             <select id="template" disabled>
                //                 <option value="test02">test02</option>
                //             </select>
                //         </div>
                //         <div>
                //             <label htmlFor="os">운영 시스템</label>
                //             <select id="os">
                //                 <option value="linux">Linux</option>
                //             </select>
                //         </div>
                //         <div>
                //             <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                //             <select id="firmware">
                //                 <option value="bios">BIOS의 Q35 칩셋</option>
                //             </select>
                //         </div>
                //         <div style={{ marginBottom: '2%' }}>
                //             <label htmlFor="optimization">최적화 옵션</label>
                //             <select id="optimization">
                //                 <option value="server">서버</option>
                //             </select>
                //         </div>
                //     </div>

                //     <div className="edit_second_content">
                //         <div>
                //             <label htmlFor="name">이름</label>
                //             <input type="text" id="name" value="test02" />
                //         </div>
                //         <div>
                //             <label htmlFor="description">설명</label>
                //             <input type="text" id="description" />
                //         </div>
                //     </div>
                //     <div className="edit_third_content">
                //         <div>
                //             <span>하드디스크</span>
                //         </div>
                //         <div>
                //             <button>연결</button>
                //             <button>생성</button>
                //             <div className='flex'>
                //                 <button>+</button>
                //                 <button>-</button>
                //             </div>
                //         </div>
                //     </div>
                //     <div className="edit_fourth_content">
                //         <div className='edit_fourth_content_select flex'>
                //             <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                //             <select id="network_adapter">
                //                 <option value="default">Default</option>
                //             </select>
                            
                //         </div>
                //         <div className='flex'>
                //             <button>+</button>
                //             <button>-</button>
                //         </div>
                //     </div>
                // </div>

                {/* 시스템 */}
                // <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                    
                //     <div className="edit_second_content">
                //         <div>
                //             <label htmlFor="memory_size">메모리 크기</label>
                //             <input type="text" id="memory_size" value="2048 MB" readOnly />
                //         </div>
                //         <div>
                //             <div>
                //                 <label htmlFor="max_memory">최대 메모리</label>
                //                 <i className="fa fa-info-circle"></i>
                //             </div>
                //             <input type="text" id="max_memory" value="8192 MB" readOnly />
                //         </div>

                //         <div>
                //             <div>
                //                 <label htmlFor="actual_memory">할당할 실제 메모리</label>
                //                 <i className="fa fa-info-circle"></i>
                //             </div>
                //             <input type="text" id="actual_memory" value="2048 MB" readOnly />
                //         </div>

                //         <div>
                //             <div>
                //                 <label htmlFor="total_cpu">총 가상 CPU</label>
                //                 <i className="fa fa-info-circle"></i>
                //             </div>
                //             <input type="text" id="total_cpu" value="1" readOnly />
                //         </div>
                //         <div>
                //             <div>
                //                 <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                //                 <span>고급 매개 변수</span>
                //             </div>
                //         </div>
                //         <div style={{ fontWeight: 600 }}>일반</div>
                //         <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                //             <div>
                //                 <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                //                 <i className="fa fa-info-circle"></i>
                //             </div>
                //             <select id="time_offset">
                //                 <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                //             </select>
                //         </div>
                //     </div>
                // </div>

                {/* 콘솔(삭ㅈ제) */}
                {/* <div id="console_outer" style={{ display: activeSection === 'console_outer' ? 'block' : 'none' }}>
                    <div className="edit_first_content">
                        <div>
                            <label htmlFor="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="default">Default</option>
                            </select>
                            <div>데이터센터 Default</div>
                        </div>

                        <div>
                            <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                            <select id="template" disabled>
                                <option value="test02">test02</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                            <select id="firmware">
                                <option value="bios">BIOS의 Q35 칩셋</option>
                            </select>
                        </div>
                        <div style={{ marginBottom: '2%' }}>
                            <label htmlFor="optimization">최적화 옵션</label>
                            <select id="optimization">
                                <option value="server">서버</option>
                            </select>
                        </div>
                    </div>

                    <div className="res_alloc_checkbox" style={{ marginBottom: 0 }}>
                        <span>그래픽 콘솔</span>
                        <div>
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                            <label htmlFor="memory_balloon">헤드리스(headless)모드</label>
                            <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                        </div>
                    </div>

                    <div className="edit_second_content">
                        <div style={{ paddingTop: 0 }}>
                            <label htmlFor="memory_size">비디오 유형</label>
                            <input type="text" id="memory_size" value="VGA" readOnly />
                        </div>
                        <div>
                            <div>
                                <label htmlFor="max_memory">그래픽 프로토콜</label>
                            </div>
                            <input type="text" id="max_memory" value="VNC" readOnly />
                        </div>

                        <div>
                            <div>
                                <label htmlFor="actual_memory">VNC 키보드 레이아웃</label>
                            </div>
                            <input type="text" id="actual_memory" value="기본값[en-us]" readOnly />
                        </div>

                        <div>
                            <div>
                                <label htmlFor="total_cpu">콘솔 분리 작업</label>
                            </div>
                            <input type="text" id="total_cpu" value="화면 잠금" readOnly />
                        </div>
                        <div>
                            <div>
                                <label htmlFor="disconnect_action_delay">Disconnect Action Delay in Minutes</label>
                            </div>
                            <input type="text" id="disconnect_action_delay" value="0" disabled />
                        </div>
                        <div id="monitor">
                            <label htmlFor="screen">모니터</label>
                            <select id="screen">
                                <option value="test02">1</option>
                            </select>
                        </div>
                    </div>

                    <div className="console_checkboxs">
                        <div className="checkbox_group">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                            <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">USB활성화</label>
                        </div>
                        <div className="checkbox_group">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                            <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">스마트카드 사용가능</label>
                        </div>
                        <span>단일 로그인 방식</span>
                        <div className="checkbox_group">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                            <label htmlFor="memory_balloon">USB활성화</label>
                        </div>
                        <div className="checkbox_group">
                            <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                            <label htmlFor="memory_balloon">스마트카드 사용가능</label>
                        </div>
                    </div>
                </div> */}

{/* 호스트 */}
{/* <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                    

<div id="host_second_content">
<div style={{ fontWeight: 600 }}>실행 호스트:</div>
<div className="form_checks">
<div>
    <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
    <label className="form-check-label" htmlFor="flexRadioDefault1">
        클러스터 내의 호스트
    </label>
</div>
<div>
    <div>
        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
        <label className="form-check-label" htmlFor="flexRadioDefault2">
            특정 호스트
        </label>
    </div>
    <div>
        <select id="specific_host_select">
            <option value="host02.ititinfo.com">host02.ititinfo.com</option>
        </select>
    </div>
</div>
</div>
<div className="host_checkboxs">
<span>CPU 옵션:</span>
<div className="host_checkbox">
    <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
    <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
    <i className="fa fa-info-circle"></i>
</div>

</div>
</div>

<div id="host_third_content">
<div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
<div>
<div>
    <span>마이그레이션 모드</span>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<select id="migration_mode">
    <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
</select>
</div>
<div>
<div>
    <span>마이그레이션 정책</span>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<select id="migration_policy">
    <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
</select>
</div>

<div>
<div>
    <span>Parallel Migrations</span>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<select id="parallel_migrations" readOnly>
    <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
</select>
</div>

</div>
</div> */}

{/* 고가용성 */}
{/* <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}> */}
{/* <div className="edit_first_content">
<div>
<label htmlFor="cluster">클러스터</label>
<select id="cluster">
    <option value="default">Default</option>
</select>
<div>데이터센터 Default</div>
</div>

<div>
<label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
<select id="template" disabled>
    <option value="test02">test02</option>
</select>
</div>
<div>
<label htmlFor="os">운영 시스템</label>
<select id="os">
    <option value="linux">Linux</option>
</select>
</div>
<div>
<label htmlFor="firmware">칩셋/펌웨어 유형</label>
<select id="firmware">
    <option value="bios">BIOS의 Q35 칩셋</option>
</select>
</div>
<div style={{ marginBottom: '2%' }}>
<label htmlFor="optimization">최적화 옵션</label>
<select id="optimization">
    <option value="server">서버</option>
</select>
</div>
</div> */}

{/* <div id="ha_mode_second_content">
<div className="checkbox_group">
<input className="check_input" type="checkbox" value="" id="ha_mode_box" />
<label className="check_label" htmlFor="ha_mode_box">
    고가용성
</label>
</div>
<div>
<div>
    <span>가상 머신 임대 대상 스토리지 도메인</span>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<select id="no_lease" disabled>
    <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
</select>
</div>
<div>
<div>
    <span>재개 동작</span>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<select id="force_shutdown">
    <option value="강제 종료">강제 종료</option>
</select>
</div>
<div className="ha_mode_article">
<span>실행/마이그레이션 큐에서 우선순위 : </span>
<div>
    <span>우선 순위</span>
    <select id="priority">
        <option value="낮음">낮음</option>
    </select>
</div>
</div>

<div className="ha_mode_article">
<span>위치독</span>
<div>
    <span>위치독 모델</span>
    <select id="watchdog_model">
        <option value="감시 장치 없음">감시 장치 없음</option>
    </select>
</div>
<div>
    <span style={{ color: 'gray' }}>위치독 작업</span>
    <select id="watchdog_action" disabled>
        <option value="없음">없음</option>
    </select>
</div>
</div>
</div>
</div> */}

{/* 리소스 할당 */}
{/* <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}> */}
{/* <div className="edit_first_content">
<div>
<label htmlFor="cluster">클러스터</label>
<select id="cluster">
    <option value="default">Default</option>
</select>
<div>데이터센터 Default</div>
</div>

<div>
<label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
<select id="template" disabled>
    <option value="test02">test02</option>
</select>
</div>
<div>
<label htmlFor="os">운영 시스템</label>
<select id="os">
    <option value="linux">Linux</option>
</select>
</div>
<div>
<label htmlFor="firmware">칩셋/펌웨어 유형</label>
<select id="firmware">
    <option value="bios">BIOS의 Q35 칩셋</option>
</select>
</div>
<div style={{ marginBottom: '2%' }}>
<label htmlFor="optimization">최적화 옵션</label>
<select id="optimization">
    <option value="server">서버</option>
</select>
</div>
</div> */}

{/* <div className="res_second_content">
<div className="cpu_res">
<span style={{ fontWeight: 600 }}>CPU 할당:</span>
<div className='cpu_res_box'>
    <span>CPU 프로파일</span>
    <select id="watchdog_action">
        <option value="없음">Default</option>
    </select>
</div>
<div className='cpu_res_box'>
    <span>CPU 공유</span>
    <div id="cpu_sharing">
        <select id="watchdog_action" style={{ width: '63%' }}>
            <option value="없음">비활성화됨</option>
        </select>
        <input type="text" value="0" disabled />
    </div>
</div>
<div className='cpu_res_box'>
    <span>CPU Pinning Policy</span>
    <select id="watchdog_action">
        <option value="없음">None</option>
    </select>
</div>
<div className='cpu_res_box'>
    <div>
        <span>CPU 피닝 토폴로지</span>
        <i className="fa fa-info-circle"></i>
    </div>
    <input type="text" disabled />
</div>
</div>

<span style={{ fontWeight: 600 }}>메모리 할당:</span>
<div id="threads">
<div className='checkbox_group'>
    <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
    <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
</div>

</div>

<span style={{ fontWeight: 600 }}>I/O 스레드:</span>
<div id="threads">
<div className='checkbox_group'>
    <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
    <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
</div>
<div>
    <input type="text" />
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
</div>
</div>

<span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>

<div className='checkbox_group mb-1'>
    <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
    <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<div className='checkbox_group mb-1'>
    <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
    <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
</div>
<div className='cpu_res_box mb-1' >
    <span>VirtIO-SCSI Multi Queues</span>
    <div id="cpu_sharing">
        <select id="multi_queue_status" style={{ width: '63%' }}>
            <option value="없음">비활성화됨</option>
        </select>
        <input type="text" value="0" disabled />
    </div>
</div>

</div>
</div> */}

{/* 부트 옵션 */}
{/* <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
<div className="res_second_content">
<div className="cpu_res">
<span style={{ fontWeight: 600 }}>부트순서:</span>
<div className='cpu_res_box'>
    <span>첫 번째 장치</span>
    <select id="watchdog_action">
        <option value="없음">하드디스크</option>
    </select>
</div>
<div className='cpu_res_box'>
    <span>두 번째 장치</span>
    <select id="watchdog_action">
        <option value="없음">Default</option>
    </select>
</div>
</div>

<div id="boot_checkboxs">
<div>
    <div className='checkbox_group'>
        <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
        <label htmlFor="connectCdDvd">CD/DVD 연결</label>
    </div>
    <div>
        <input type="text" disabled />
        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
    </div>
</div>

<div className='checkbox_group mb-1.5'>
    <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
    <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
</div>
</div>

<div className="cpu_res border-t border-gray-500 py-1">
<span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
<div className='cpu_res_box'>
    <label htmlFor="kernel_path">커널 경로</label>
    <input type="text" id="kernel_path" value="2048 MB" readOnly />
</div>

<div className='cpu_res_box'>
    <label htmlFor="initrd_path">initrd 경로</label>
    <input type="text" id="initrd_path" value="2048 MB" readOnly />
</div>

<div className='cpu_res_box'>
    <label htmlFor="kernel_parameters">커널 매개 변수</label>
    <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
</div>


</div>

</div>
</div>
</form>
</div>

<div className="edit_footer">
<button>OK</button>
<button onClick={closePopup}>취소</button>
</div>
</div>
</Modal> */}




{/* 가상머신(편집)팝업 */}
{/* <Modal
isOpen={activePopup === 'vm_edit'}
onRequestClose={closePopup}
contentLabel="가상머신 편집"
className="edit_popup"
overlayClassName="edit_popup_outer"
shouldCloseOnOverlayClick={false}
>
<div id="edit_popup">
<div className="popup_header">
<h1>가상머신 편집</h1>
<button onClick={closePopup}>
<FontAwesomeIcon icon={faTimes} fixedWidth />
</button>
</div>

<div className="edit_body">
<div className="edit_aside">
<div
 className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
 id="common_outer_btn"
 onClick={() => handleSectionChange('common_outer')}
>
 <span>일반</span>
</div>
<div
 className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
 id="system_outer_btn"
 onClick={() => handleSectionChange('system_outer')}
>
 <span>시스템</span>
</div>
<div
 className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
 id="host_outer_btn"
 onClick={() => handleSectionChange('host_outer')}
>
 <span>호스트</span>
</div>
<div
 className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
 id="ha_mode_outer_btn"
 onClick={() => handleSectionChange('ha_mode_outer')}
>
 <span>고가용성</span>
</div>
<div
 className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
 id="res_alloc_outer_btn"
 onClick={() => handleSectionChange('res_alloc_outer')}
>
 <span>리소스 할당</span>
</div>
<div
 className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
 id="boot_outer_btn"
 onClick={() => handleSectionChange('boot_outer')}
>
 <span>부트 옵션</span>
</div>
</div>


   <form action="#"> */}
       {/* 일반 */}
       {/* <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
           <div className="edit_first_content">
               <div>
                   <label htmlFor="cluster">클러스터</label>
                   <select id="cluster">
                       <option value="default">Default</option>
                   </select>
                   <div>데이터센터 Default</div>
               </div>

               <div>
                   <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                   <select id="template" disabled>
                       <option value="test02">test02</option>
                   </select>
               </div>
               <div>
                   <label htmlFor="os">운영 시스템</label>
                   <select id="os">
                       <option value="linux">Linux</option>
                   </select>
               </div>
               <div>
                   <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                   <select id="firmware">
                       <option value="bios">BIOS의 Q35 칩셋</option>
                   </select>
               </div>
               <div style={{ marginBottom: '2%' }}>
                   <label htmlFor="optimization">최적화 옵션</label>
                   <select id="optimization">
                       <option value="server">서버</option>
                   </select>
               </div>
           </div>

           <div className="edit_second_content">
               <div>
                   <label htmlFor="name">이름</label>
                   <input type="text" id="name" value="test02" />
               </div>
               <div>
                   <label htmlFor="description">설명</label>
                   <input type="text" id="description" />
               </div>
           </div>
           <div className="instance_image">
               <span>인스턴스 이미지</span><br/>
               <div>
                   <div>on20-apm_Disk1_c1: (2 GB) 기존</div>
                   <div className='flex'>
                       <button className='mr-1'>편집</button>
                       <button>+</button>
                       <button>-</button>
                   </div>
               </div>
           </div>

           <span className='edit_fourth_span'>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</span>
           <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
              
               <div className='edit_fourth_content_select flex'>
                   <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                   <select id="network_adapter">
                       <option value="default">Default</option>
                   </select>
               </div>
               <div className='flex'>
                   <button>+</button>
                   <button>-</button>
               </div>
           </div>
       </div> */}

       {/* 시스템 */}
       {/* <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
           
           <div className="edit_second_content">
               <div>
                   <label htmlFor="memory_size">메모리 크기</label>
                   <input type="text" id="memory_size" value="2048 MB" readOnly />
               </div>
               <div>
                   <div>
                       <label htmlFor="max_memory">최대 메모리</label>
                       <i className="fa fa-info-circle"></i>
                   </div>
                   <input type="text" id="max_memory" value="8192 MB" readOnly />
               </div>

               <div>
                   <div>
                       <label htmlFor="actual_memory">할당할 실제 메모리</label>
                       <i className="fa fa-info-circle"></i>
                   </div>
                   <input type="text" id="actual_memory" value="2048 MB" readOnly />
               </div>

               <div>
                   <div>
                       <label htmlFor="total_cpu">총 가상 CPU</label>
                       <i className="fa fa-info-circle"></i>
                   </div>
                   <input type="text" id="total_cpu" value="1" readOnly />
               </div>
               <div>
                   <div>
                       <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                       <span>고급 매개 변수</span>
                   </div>
               </div>
               <div style={{ fontWeight: 600 }}>일반</div>
               <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                   <div>
                       <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                       <i className="fa fa-info-circle"></i>
                   </div>
                   <select id="time_offset">
                       <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                   </select>
               </div>
           </div>
       </div> */}

       {/* 호스트 */}
       {/* <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
           <div id="host_second_content">
               <div style={{ fontWeight: 600 }}>실행 호스트:</div>
               <div className="form_checks">
                   <div>
                       <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                       <label className="form-check-label" htmlFor="flexRadioDefault1">
                           클러스터 내의 호스트
                       </label>
                   </div>
                   <div>
                       <div>
                           <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                           <label className="form-check-label" htmlFor="flexRadioDefault2">
                               특정 호스트
                           </label>
                       </div>
                       <div>
                           <select id="specific_host_select">
                               <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                           </select>
                       </div>
                   </div>
               </div>
               <div className="host_checkboxs">
                   <span>CPU 옵션:</span>
                   <div className="host_checkbox">
                       <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                       <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                       <i className="fa fa-info-circle"></i>
                   </div>
                  
               </div>
           </div>

           <div id="host_third_content">
               <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
               <div>
                   <div>
                       <span>마이그레이션 모드</span>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <select id="migration_mode">
                       <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                   </select>
               </div>
               <div>
                   <div>
                       <span>마이그레이션 정책</span>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <select id="migration_policy">
                       <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                   </select>
               </div>
               
               <div>
                   <div>
                       <span>Parallel Migrations</span>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <select id="parallel_migrations" readOnly>
                       <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                   </select>
               </div>
              
           </div>
       </div> */}

       {/* 고가용성 */}
       {/* <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
           <div id="ha_mode_second_content">
               <div className="checkbox_group">
                   <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                   <label className="check_label" htmlFor="ha_mode_box">
                       고가용성
                   </label>
               </div>
               <div>
                   <div>
                       <span>가상 머신 임대 대상 스토리지 도메인</span>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <select id="no_lease" disabled>
                       <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                   </select>
               </div>
               <div>
                   <div>
                       <span>재개 동작</span>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <select id="force_shutdown">
                       <option value="강제 종료">강제 종료</option>
                   </select>
               </div>
               <div className="ha_mode_article">
                   <span>실행/마이그레이션 큐에서 우선순위 : </span>
                   <div>
                       <span>우선 순위</span>
                       <select id="priority">
                           <option value="낮음">낮음</option>
                       </select>
                   </div>
               </div>

               <div className="ha_mode_article">
                   <span>위치독</span>
                   <div>
                       <span>위치독 모델</span>
                       <select id="watchdog_model">
                           <option value="감시 장치 없음">감시 장치 없음</option>
                       </select>
                   </div>
                   <div>
                       <span style={{ color: 'gray' }}>위치독 작업</span>
                       <select id="watchdog_action" disabled>
                           <option value="없음">없음</option>
                       </select>
                   </div>
               </div>
           </div>
       </div> */}

       {/* 리소스 할당 */}
       {/* <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
           <div className="res_second_content">
               <div className="cpu_res">
                   <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                   <div className='cpu_res_box'>
                       <span>CPU 프로파일</span>
                       <select id="watchdog_action">
                           <option value="없음">Default</option>
                       </select>
                   </div>
                   <div className='cpu_res_box'>
                       <span>CPU 공유</span>
                       <div id="cpu_sharing">
                           <select id="watchdog_action" style={{ width: '63%' }}>
                               <option value="없음">비활성화됨</option>
                           </select>
                           <input type="text" value="0" disabled />
                       </div>
                   </div>
                   <div className='cpu_res_box'>
                       <span>CPU Pinning Policy</span>
                       <select id="watchdog_action">
                           <option value="없음">None</option>
                       </select>
                   </div>
                   <div className='cpu_res_box'>
                       <div>
                           <span>CPU 피닝 토폴로지</span>
                           <i className="fa fa-info-circle"></i>
                       </div>
                       <input type="text" disabled />
                   </div>
               </div>

               <span style={{ fontWeight: 600 }}>메모리 할당:</span>
               <div id="threads">
                   <div className='checkbox_group'>
                       <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                       <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                   </div>
               
               </div>

               <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
               <div id="threads">
                   <div className='checkbox_group'>
                       <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                       <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                   </div>
                   <div>
                       <input type="text" />
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                   </div>
               </div>

               <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
               
                   <div className='checkbox_group mb-1'>
                       <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                       <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <div className='checkbox_group mb-1'>
                       <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                       <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                       <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                   </div>
                   <div className='cpu_res_box mb-1' >
                       <span>VirtIO-SCSI Multi Queues</span>
                       <div id="cpu_sharing">
                           <select id="multi_queue_status" style={{ width: '63%' }}>
                               <option value="없음">비활성화됨</option>
                           </select>
                           <input type="text" value="0" disabled />
                       </div>
                   </div>
                   
           </div>
       </div> */}

       {/* 부트 옵션 */}
       {/* <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
           <div className="res_second_content">
               <div className="cpu_res">
                   <span style={{ fontWeight: 600 }}>부트순서:</span>
                   <div className='cpu_res_box'>
                       <span>첫 번째 장치</span>
                       <select id="watchdog_action">
                           <option value="없음">하드디스크</option>
                       </select>
                   </div>
                   <div className='cpu_res_box'>
                       <span>두 번째 장치</span>
                       <select id="watchdog_action">
                           <option value="없음">Default</option>
                       </select>
                   </div>
               </div>

               <div id="boot_checkboxs">
                   <div>
                       <div className='checkbox_group'>
                           <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                           <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                       </div>
                       <div>
                           <input type="text" disabled />
                           <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                       </div>
                   </div>

                   <div className='checkbox_group mb-1.5'>
                       <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                       <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                   </div>
               </div>

               <div className="cpu_res border-t border-gray-500 py-1">
                   <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                   <div className='cpu_res_box'>
                       <label htmlFor="kernel_path">커널 경로</label>
                       <input type="text" id="kernel_path" value="2048 MB" readOnly />
                   </div>

                   <div className='cpu_res_box'>
                       <label htmlFor="initrd_path">initrd 경로</label>
                       <input type="text" id="initrd_path" value="2048 MB" readOnly />
                   </div>

                   <div className='cpu_res_box'>
                       <label htmlFor="kernel_parameters">커널 매개 변수</label>
                       <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                   </div>


               </div>
               
           </div>
       </div>
   </form>
</div>

<div className="edit_footer">
   <button>OK</button>
   <button onClick={closePopup}>취소</button>
</div>
</div>
</Modal> */}
