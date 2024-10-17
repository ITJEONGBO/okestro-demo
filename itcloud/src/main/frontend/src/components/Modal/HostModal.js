import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle, faArrowUp, faArrowDown, faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';

const HostModal = ({ isOpen, onRequestClose, onSubmit }) => {
  const [activeSection, setActiveSection] = useState('일반_섹션');
  const [isHiddenParameterVisible, setHiddenParameterVisible] = useState(false);

  const handleSectionChange = (section) => setActiveSection(section);

  const toggleHiddenParameter = () => {
    setHiddenParameterVisible(!isHiddenParameterVisible);
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    onSubmit();  // 제출 시 부모 컴포넌트로 데이터를 전달
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="호스트 새로 만들기"
      className="host_new_popup"
      overlayClassName="host_new_outer"
      shouldCloseOnOverlayClick={false}
    >
      <div className="popup_header">
        <h1>호스트 새로 만들기</h1>
        <button onClick={onRequestClose}>
          <FontAwesomeIcon icon={faTimes} fixedWidth />
        </button>
      </div>

      <div className="edit_body">
        <div className="edit_aside">
          <div
            className={`edit_aside_item`}
            id="일반_섹션_btn"
            onClick={() => handleSectionChange('일반_섹션')}
            style={{ backgroundColor: activeSection === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: activeSection === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: activeSection === '일반_섹션' ? '1px solid blue' : 'none' }}
          >
            <span>일반</span>
          </div>
          <div
            className={`edit_aside_item`}
            id="전원관리_섹션_btn"
            onClick={() => handleSectionChange('전원관리_섹션')}
            style={{ backgroundColor: activeSection === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: activeSection === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: activeSection === '전원관리_섹션' ? '1px solid blue' : 'none' }}
          >
            <span>전원 관리</span>
          </div>
          <div
            className={`edit_aside_item`}
            id="호스트엔진_섹션_btn"
            onClick={() => handleSectionChange('호스트엔진_섹션')}
            style={{ backgroundColor: activeSection === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: activeSection === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: activeSection === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
          >
            <span>호스트 엔진</span>
          </div>
          <div
            className={`edit_aside_item`}
            id="선호도_섹션_btn"
            onClick={() => handleSectionChange('선호도_섹션')}
            style={{ backgroundColor: activeSection === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: activeSection === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: activeSection === '선호도_섹션' ? '1px solid blue' : 'none' }}
          >
            <span>선호도</span>
          </div>
        </div>

        {/* 폼 섹션 */}
        <form onSubmit={handleFormSubmit}>
          {/* 일반 섹션 */}
          {activeSection === '일반_섹션' && (
            <div id="일반_섹션">
              <div className="edit_first_content">
                <div>
                  <label htmlFor="cluster">클러스터</label>
                  <select id="cluster" disabled>
                    <option value="default">Default</option>
                  </select>
                  <div>데이터센터 Default</div>
                </div>
                <div>
                  <label htmlFor="name1">이름</label>
                  <input type="text" id="name1" />
                </div>
                <div>
                  <label htmlFor="comment">코멘트</label>
                  <input type="text" id="comment" />
                </div>
                <div>
                  <label htmlFor="hostname">호스트 이름/IP</label>
                  <input type="text" id="hostname" />
                </div>
                <div>
                  <label htmlFor="ssh_port">SSH 포트</label>
                  <input type="text" id="ssh_port" value="22" />
                </div>
              </div>

              <div className="host_checkboxs">
                <div className="host_checkbox">
                  <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">헤드리스 모드</label>
                </div>
                <div className="host_checkbox">
                  <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                  <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth />
                </div>
              </div>

              <div className="host_checkboxs">
                <div className="host_textbox">
                  <label htmlFor="user_name">사용자 이름</label>
                  <input type="text" id="user_name" />
                </div>

                <div className="host_text_raido_box">
                  <div>
                    <input type="radio" id="password" name="name_option" />
                    <label htmlFor="password">암호</label>
                  </div>
                  <input type="text" id="radio1_name" />
                </div>

                <div className="host_radiobox">
                  <input type="radio" id="ssh_key" name="name_option" />
                  <label htmlFor="ssh_key">SSH 공개키</label>
                </div>
              </div>
            </div>
          )}

          {/* 전원 관리 섹션 */}
          {activeSection === '전원관리_섹션' && (
            <div id="전원관리_섹션">
              <div className="host_checkboxs">
                <div className="host_checkbox">
                  <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                  <label htmlFor="enable_forwarding">전송 관리 활성</label>
                </div>
                <div className="host_checkbox">
                  <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                  <label htmlFor="kdump_usage">Kdump 통합</label>
                </div>
                <div className="host_checkbox">
                  <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                  <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
                </div>

                <span className="sorted_agents">순서대로 정렬된 에이전트</span>
              </div>

              <div className="addFence_agent">
                <span>펜스 에이전트 추가</span>
                <button>+</button>
              </div>

              <div className="advanced_objec_add">
                <div className="flex">
                  <button className="mr-1" onClick={toggleHiddenParameter}>
                    {isHiddenParameterVisible ? '-' : '+'}
                  </button>
                  <span>고급 매개 변수</span>
                </div>
                {isHiddenParameterVisible && (
                  <div className="host_hidden_parameter">
                    <div>전원 관리 프록시 설정</div>
                    <div>
                      <div className="proxy_content">
                        <div className="font-bold">1.</div>
                        <div className="w-6">cluster</div>
                        <div>
                          <button>
                            <FontAwesomeIcon icon={faArrowUp} fixedWidth />
                          </button>
                          <button>
                            <FontAwesomeIcon icon={faArrowDown} fixedWidth />
                          </button>
                        </div>
                        <button>
                          <FontAwesomeIcon icon={faMinus} fixedWidth />
                        </button>
                      </div>
                      <div className="proxy_content">
                        <div className="font-bold">2.</div>
                        <div className="w-6">dc</div>
                        <div>
                          <button>
                            <FontAwesomeIcon icon={faArrowUp} fixedWidth />
                          </button>
                          <button>
                            <FontAwesomeIcon icon={faArrowDown} fixedWidth />
                          </button>
                        </div>
                        <button>
                          <FontAwesomeIcon icon={faMinus} fixedWidth />
                        </button>
                      </div>
                    </div>

                    <div className="proxy_add">
                      <div>전원 관리 프록시 추가</div>
                      <button>
                        <FontAwesomeIcon icon={faPlus} fixedWidth />
                      </button>
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* 호스트 엔진 섹션 */}
          {activeSection === '호스트엔진_섹션' && (
            <div id="호스트엔진_섹션">
              <div className="host_policy">
                <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
                <select id="host_action">
                  <option value="none">없음</option>
                </select>
              </div>
            </div>
          )}

          {/* 선호도 섹션 */}
          {activeSection === '선호도_섹션' && (
            <div id="선호도_섹션">
              <div className="preference_outer">
                <div className="preference_content">
                  <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
                  <div>
                    <select id="preference_group">
                      <option value="none"></option>
                    </select>
                    <button>추가</button>
                  </div>
                </div>
                <div className="preference_noncontent">
                  <div>선택된 선호도 그룹</div>
                  <div>선택된 선호도 그룹이 없습니다</div>
                </div>
                <div className="preference_content">
                  <label htmlFor="preference_label">선호도 레이블 선택</label>
                  <div>
                    <select id="preference_label">
                      <option value="none"></option>
                    </select>
                    <button>추가</button>
                  </div>
                </div>
                <div className="preference_noncontent">
                  <div>선택한 선호도 레이블</div>
                  <div>선호도 레이블이 선택되어있지 않습니다</div>
                </div>
              </div>
            </div>
          )}

          <div className="edit_footer">
            <button type="submit">OK</button>
            <button onClick={onRequestClose}>취소</button>
          </div>
        </form>
      </div>
    </Modal>
  );
};

export default HostModal;



            

    // {/* 호스트(새로만들기) 팝업*/}
    // <Modal
    //     isOpen={activePopup === 'host_new'}
    //     onRequestClose={closePopup}
    //     contentLabel="편집"
    //     className="host_new_popup"
    //     overlayClassName="host_new_outer"
    //     shouldCloseOnOverlayClick={false}
    // >
    //     <div className="popup_header">
    //     <h1>호스트 새로만들기</h1>
    //     <button onClick={closePopup}>
    //         <FontAwesomeIcon icon={faTimes} fixedWidth/>
    //     </button>
    //     </div>

    //     <div className="edit_body">
    //     <div className="edit_aside">
    //         <div
    //         className={`edit_aside_item`}
    //         id="일반_섹션_btn"
    //         onClick={() => 섹션변경('일반_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>일반</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="전원관리_섹션_btn"
    //         onClick={() => 섹션변경('전원관리_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>전원 관리</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="호스트엔진_섹션_btn"
    //         onClick={() => 섹션변경('호스트엔진_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>호스트 엔진</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="선호도_섹션_btn"
    //         onClick={() => 섹션변경('선호도_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>선호도</span>
    //         </div>
    //     </div>

    //     {/* 폼의 다양한 섹션들 */}
    //     <form action="#">
    //         {/* 공통 섹션 */}
    //         <div
    //         id="일반_섹션"
    //         style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
    //         >
    //     <div className="edit_first_content">
    //             <div>
    //                 <label htmlFor="cluster">클러스터</label>
    //                 <select id="cluster" disabled>
    //                     <option value="default" >Default</option>
    //                 </select>
    //                 <div>데이터센터 Default</div>
    //             </div>
    //             <div>
    //                 <label htmlFor="name1">이름</label>
    //                 <input type="text" id="name1" />
    //             </div>
    //             <div>
    //                 <label htmlFor="comment">코멘트</label>
    //                 <input type="text" id="comment" />
    //             </div>
    //             <div>
    //                 <label htmlFor="hostname">호스트이름/IP</label>
    //                 <input type="text" id="hostname" />
    //             </div>
    //             <div>
    //                 <label htmlFor="ssh_port">SSH 포트</label>
    //                 <input type="text" id="ssh_port" value="22" />
    //             </div>
    //         </div>

    // <div className='host_checkboxs'>
    // <div className='host_checkbox'>
    //     <input type="checkbox" id="memory_balloon" name="memory_balloon" />
    //     <label htmlFor="headless_mode">헤드리스 모드</label>
    // </div>
    // <div className='host_checkbox'>
    //     <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
    //     <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
    //     <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
    // </div>
    // </div>

    // <div className='host_checkboxs'>
    // <div className='host_textbox'>
    //     <label htmlFor="user_name">사용자 이름</label>
    //     <input type="text" id="user_name" />
    // </div>

    // <div className='host_text_raido_box'>
    //     <div>
    //         <input type="radio" id="password" name="name_option" />
    //         <label htmlFor="password">암호</label>
    //     </div>
    //     <input type="text" id="radio1_name" />
    // </div>

    // <div className='host_radiobox'>
    //     <input type="radio" id="ssh_key" name="name_option" />
    //     <label htmlFor="ssh_key">SSH 공개키</label>
    // </div>

    // </div>

    //         </div>{/*일반섹션끝 */}

    //         {/* 전원 관리 섹션 */}
    //         <div
    //         id="전원관리_섹션"
    //         style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
    //         >
            
    //         <div className='host_checkboxs'>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
    //                 <label htmlFor="enable_forwarding">전송 관리 활성</label>
    //             </div>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
    //                 <label htmlFor="kdump_usage">Kdump 통합</label>
    //             </div>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
    //                 <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
    //             </div>


    //             <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
    //         </div>
            
            
    //         <div className='addFence_agent'>
    //             <span>펜스 에이전트 추가</span>
    //             <button>+</button>
    //         </div>

    //         <div className='advanced_objec_add'>
    //         <div className='flex'>
    //             <button className='mr-1'onClick={toggleHiddenParameter}>
    //             {isHiddenParameterVisible ? '-' : '+'}
    //             </button>
    //             <span>고급 매개 변수</span>
    //         </div>
    //     {isHiddenParameterVisible && (
    //     <div className='host_hidden_parameter'>
            
    //         <div>전원 관리 프록시 설정</div>
    //         <div>
    //         <div className='proxy_content'>
    //             <div className='font-bold'>1.</div>
    //             <div className='w-6'>cluster</div>
    //             <div>  
    //             <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
    //             <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
    //             </div>
    //             <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
    //         </div>
    //         <div className='proxy_content'>
    //             <div className='font-bold'>2.</div>
    //             <div className='w-6'>dc</div>
    //             <div>  
    //             <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
    //             <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
    //             </div>
    //             <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
    //         </div>
    //         </div>

    //         <div className='proxy_add'>
    //         <div>전원 관리 프록시 추가</div>
    //         <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
    //         </div>
    //     </div>
    //     )}
    //     </div>
            

    //         </div>

    //         {/* 호스트 엔진 섹션 */}
    //         <div
    //         id="호스트엔진_섹션"
    //         style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
    //         >
    //         <div className="host_policy">
    //             <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
    //             <select id="host_action">
    //                 <option value="none">없음</option>
    //             </select>
    //         </div>


    //         </div>

    //         {/* 선호도 섹션 */}
    //         <div
    //         id="선호도_섹션"
    //         style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
    //         >
    //         <div className="preference_outer">
    //             <div className="preference_content">
    //             <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
    //                 <div>
    //                 <select id="preference_group">
    //                     <option value="none"></option>
    //                 </select>
    //                 <button>추가</button>
    //                 </div>
    //             </div>
    //             <div className="preference_noncontent">
    //             <div>선택된 선호도 그룹</div>
    //             <div>선택된 선호도 그룹이 없습니다</div>
    //             </div>
    //             <div className="preference_content">
    //             <label htmlFor="preference_label">선호도 레이블 선택</label>
    //             <div>
    //                 <select id="preference_label">
    //                 <option value="none"></option>
    //                 </select>
    //                 <button>추가</button>
    //             </div>
    //             </div>
    //             <div className="preference_noncontent">
    //             <div>선택한 선호도 레이블</div>
    //             <div>선호도 레이블이 선택되어있지 않습니다</div>
    //             </div>

    //         </div>
    //         </div>

    //     </form>
    //     </div>

    //     <div className="edit_footer">
    //     <button>OK</button>
    //     <button onClick={closePopup}>취소</button>
    //     </div>
    // </Modal>
    // {/* 호스트(편집) 팝업*/}
    // <Modal
    //     isOpen={activePopup === 'host_edit'}
    //     onRequestClose={closePopup}
    //     contentLabel="편집"
    //     className="host_new_popup"
    //     overlayClassName="host_new_outer"
    //     shouldCloseOnOverlayClick={false}
    // >
    //     <div className="popup_header">
    //     <h1>호스트 수정</h1>
    //     <button onClick={closePopup}>
    //         <FontAwesomeIcon icon={faTimes} fixedWidth/>
    //     </button>
    //     </div>

    //     <div className="edit_body">
    //     <div className="edit_aside">
    //         <div
    //         className={`edit_aside_item`}
    //         id="일반_섹션_btn"
    //         onClick={() => 섹션변경('일반_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>일반</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="전원관리_섹션_btn"
    //         onClick={() => 섹션변경('전원관리_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>전원 관리</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="호스트엔진_섹션_btn"
    //         onClick={() => 섹션변경('호스트엔진_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>호스트 엔진</span>
    //         </div>
    //         <div
    //         className={`edit_aside_item`}
    //         id="선호도_섹션_btn"
    //         onClick={() => 섹션변경('선호도_섹션')}
    //         style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
    //         >
    //         <span>선호도</span>
    //         </div>
    //     </div>

    //     {/* 폼의 다양한 섹션들 */}
    //     <form action="#">
    //         {/* 공통 섹션 */}
    //         <div
    //         id="일반_섹션"
    //         style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
    //         >
    //     <div className="edit_first_content">
    //             <div>
    //                 <label htmlFor="cluster">클러스터</label>
    //                 <select id="cluster" disabled>
    //                     <option value="default" >Default</option>
    //                 </select>
    //                 <div>데이터센터 Default</div>
    //             </div>
    //             <div>
    //                 <label htmlFor="name1">이름</label>
    //                 <input type="text" id="name1" />
    //             </div>
    //             <div>
    //                 <label htmlFor="comment">코멘트</label>
    //                 <input type="text" id="comment" />
    //             </div>
    //             <div>
    //                 <label htmlFor="hostname">호스트이름/IP</label>
    //                 <input type="text" id="hostname" />
    //             </div>
    //             <div>
    //                 <label htmlFor="ssh_port">SSH 포트</label>
    //                 <input type="text" id="ssh_port" value="22" />
    //             </div>
    //         </div>

    // <div className='host_checkboxs'>
    // <div className='host_checkbox'>
    //     <input type="checkbox" id="memory_balloon" name="memory_balloon" />
    //     <label htmlFor="headless_mode">헤드리스 모드</label>
    // </div>
    // <div className='host_checkbox'>
    //     <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
    //     <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
    //     <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
    // </div>
    // </div>

    // <div className='host_checkboxs'>
    // <div className='host_textbox'>
    //     <label htmlFor="user_name">사용자 이름</label>
    //     <input type="text" id="user_name" />
    // </div>

    // <div className='host_text_raido_box'>
    //     <div>
    //         <input type="radio" id="password" name="name_option" />
    //         <label htmlFor="password">암호</label>
    //     </div>
    //     <input type="text" id="radio1_name" />
    // </div>

    // <div className='host_radiobox'>
    //     <input type="radio" id="ssh_key" name="name_option" />
    //     <label htmlFor="ssh_key">SSH 공개키</label>
    // </div>

    // </div>

    //         </div>{/*일반섹션끝 */}

    //         {/* 전원 관리 섹션 */}
    //         <div
    //         id="전원관리_섹션"
    //         style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
    //         >
            
    //         <div className='host_checkboxs'>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
    //                 <label htmlFor="enable_forwarding">전송 관리 활성</label>
    //             </div>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
    //                 <label htmlFor="kdump_usage">Kdump 통합</label>
    //             </div>
    //             <div className='host_checkbox'>
    //                 <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
    //                 <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
    //             </div>


    //             <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
    //         </div>
            
            
    //         <div className='addFence_agent'>
    //             <span>펜스 에이전트 추가</span>
    //             <button>+</button>
    //         </div>

    //         <div className='advanced_objec_add'>
    //     <button onClick={toggleHiddenParameter}>
    //         {isHiddenParameterVisible ? '-' : '+'}
    //     </button>
    //     <span>고급 매개 변수</span>
    //     {isHiddenParameterVisible && (
    //     <div className='host_hidden_parameter'>
            
    //         <div>전원 관리 프록시 설정</div>
    //         <div>
    //         <div className='proxy_content'>
    //             <div className='font-bold'>1.</div>
    //             <div className='w-6'>cluster</div>
    //             <div>  
    //             <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
    //             <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
    //             </div>
    //             <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
    //         </div>
    //         <div className='proxy_content'>
    //             <div className='font-bold'>2.</div>
    //             <div className='w-6'>dc</div>
    //             <div>  
    //             <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
    //             <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
    //             </div>
    //             <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
    //         </div>
    //         </div>

    //         <div className='proxy_add'>
    //         <div>전원 관리 프록시 추가</div>
    //         <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
    //         </div>
    //     </div>
    //     )}
    //     </div>
            

    //         </div>

    //         {/* 호스트 엔진 섹션 */}
    //         <div
    //         id="호스트엔진_섹션"
    //         style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
    //         >
    //         <div className="host_policy">
    //             <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
    //             <select id="host_action">
    //                 <option value="none">없음</option>
    //             </select>
    //         </div>


    //         </div>

    //         {/* 선호도 섹션 */}
    //         <div
    //         id="선호도_섹션"
    //         style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
    //         >
    //         <div className="preference_outer">
    //             <div className="preference_content">
    //             <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
    //                 <div>
    //                 <select id="preference_group">
    //                     <option value="none"></option>
    //                 </select>
    //                 <button>추가</button>
    //                 </div>
    //             </div>
    //             <div className="preference_noncontent">
    //             <div>선택된 선호도 그룹</div>
    //             <div>선택된 선호도 그룹이 없습니다</div>
    //             </div>
    //             <div className="preference_content">
    //             <label htmlFor="preference_label">선호도 레이블 선택</label>
    //             <div>
    //                 <select id="preference_label">
    //                 <option value="none"></option>
    //                 </select>
    //                 <button>추가</button>
    //             </div>
    //             </div>
    //             <div className="preference_noncontent">
    //             <div>선택한 선호도 레이블</div>
    //             <div>선호도 레이블이 선택되어있지 않습니다</div>
    //             </div>

    //         </div>
    //         </div>

    //     </form>
    //     </div>

    //     <div className="edit_footer">
    //     <button>OK</button>
    //     <button onClick={closePopup}>취소</button>
    //     </div>
    // </Modal>
