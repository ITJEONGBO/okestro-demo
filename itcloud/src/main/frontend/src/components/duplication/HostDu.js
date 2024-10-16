import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faSearch, faChevronDown, faEllipsisV, faInfoCircle, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../table/TableColumnsInfo';
import TableOuter from '../table/TableOuter';
import './css/HostDu.css';
import Modal from 'react-modal';

const HostDu = ({ data, columns, handleRowClick,togglePopup }) => {

  // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => {
    setActivePopup(popupType);
  };

const closePopup = () => {
    setActivePopup(null);
};
  // 토글 방식으로 열고 닫기(관리)
  const [isManageBoxVisible, setIsManageBoxVisible] = useState(false);

  // 관리버튼
  const handleManageClick = () => {
    setIsManageBoxVisible(!isManageBoxVisible);
  };
    // 팝업 외부 클릭 시 닫히도록 처리
    useEffect(() => {
      const handleClickOutside = (event) => {
        const manageBox = document.getElementById('manage_hidden_box');
        const manageBtn = document.getElementById('manage_btn');
        const ellipsisBox = document.getElementById('ellipsis_hidden_box');
        const ellipsisBtn = document.getElementById('ellipsis_btn');
        
        // 클릭한 요소가 manage_box 내부의 li인지 확인
        const isLiElement = event.target.tagName === 'LI';
  
        // manage_box, manage_btn, ellipsis_box, ellipsis_btn 그리고 각 li가 아닌 곳을 클릭했을 때만 팝업 닫기
        if (
          (manageBox && !manageBox.contains(event.target) && manageBtn && !manageBtn.contains(event.target)) ||
          (ellipsisBox && !ellipsisBox.contains(event.target) && ellipsisBtn && !ellipsisBtn.contains(event.target)) &&
          !isLiElement // li 요소를 클릭한 경우는 제외
        ) {
          setIsManageBoxVisible(false);
          setPopupOpen(false); // ellipsis 팝업도 닫기
        }
      };
  
      document.addEventListener('mousedown', handleClickOutside);
  
      return () => {
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, []);

    // ...버튼
    const [popupOpen, setPopupOpen] = useState(false);
    const togglePopupMenu = () => {
      setPopupOpen(!popupOpen);
    };
  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => openPopup('host_new')}>새로 만들기</button>
        <button onClick={() => openPopup('host_edit')}>편집</button>
        <button onClick={() => openPopup('delete')}>삭제</button>
        <div className="manage_container">
          <button id="manage_btn" onClick={handleManageClick} className="btn">
            관리 <FontAwesomeIcon icon={faChevronDown} />
          </button>
          
          {isManageBoxVisible && (
            <ul id="manage_hidden_box" className="dropdown-menu">
              <li onClick={() => openPopup('maintenance')}>유지보수</li>
              <li>활성</li>
              <li>기능을 새로 고침</li>
              <li style={{borderTop:'1px solid #DDDDDD',borderBottom:'1px solid #DDDDDD'}}>인증서 등록</li>
              <li>재시작</li>
              <li>중지</li>
            </ul>
          )}
        </div>
        <button>설치</button>
        <button>호스트 네트워크 복사</button>
        <div className="ellipsis_container">
          <button id="ellipsis_btn" onClick={togglePopupMenu} className="btn">
            <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
          </button>

          {popupOpen && (
            <ul id="ellipsis_hidden_box" className="dropdown-menu">
              <li className='disabled'>글로벌 HA 유지 관리를 활성화</li>
              <li>글로벌 HA 유지 관리를 비활성화</li>
            </ul>
          )}
        </div>
      </div>
      
      <TableOuter
        columns={columns} 
        data={data} 
        onRowClick={handleRowClick} 
        className="host_table"
        clickableColumnIndex={[2]} 
      />

      {/* 호스트 새로 만들기 */}
      <Modal
      isOpen={activePopup === 'host_new'}
      onRequestClose={closePopup}
      contentLabel="새로 만들기"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}

    >
        <div className="host_new_add">
          <div className="popup_header">
            <h1>새 호스트</h1>
            <button onClick={() =>closePopup('host_new')}>
              <FontAwesomeIcon icon={faTimes} fixedWidth/>
            </button>
          </div>

    
        
        <form action="#">
          <div className="edit_first_content">
                  <div>
                      <label htmlFor="host_cluster">호스트 클러스터</label>
                      <select id="cluster">
                          <option value="default">Default</option>
                      </select>
                  
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
                      <label htmlFor="hostname">호스트이름/IP<FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/></label>
                      <input type="text" id="hostname" />
                  </div>
                  <div>
                      <label htmlFor="ssh_port">SSH 포트</label>
                      <input type="text" id="ssh_port" value="22" />
                  </div>
            </div>

          <div className='py-1'>
            <div className='host_checkboxs'>
              <div className='host_checkbox'>
                  <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">설치 후 호스트를 활성화</label>
              </div>
              <div className='host_checkbox'>
                  <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                  <label htmlFor="headless_mode_info">설치 후 호스트를 다시 시작</label>
                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
              </div>
            </div>

            <div className='host_checkboxs'>
              <span className='px-1'>인증</span>
              <div className='host_textbox' style={{paddingTop:'0'}}>
                  <label htmlFor="user_name">사용자 이름</label>
                  <input type="text" id="user_name" />
              </div>

              <div className='host_text_raido_box'>
                  <div>
                    <input type="radio" id="password" name="name_option" />
                    <label htmlFor="password">암호</label>
                  </div>
                  <input type="text" id="radio1_name" />
              </div>
            </div>

            <div className='vGPU_radiobox'>
              <div className='font-bold'>
                vGPU 배치<FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
              </div>
              <div>
                  <input type="radio" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">통합</label>
              </div>
              <div>
                  <input type="radio" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">분산</label>
              </div>
            </div>
            
            <div className="host_select_set">
                      <label htmlFor="host_related_action">호스트 연관 배포 작업 선택</label>
                      <select id="host_related_action">
                        <option value="none">없음</option>
                      </select>
            </div>
          </div>
        </form>
      

        <div className="edit_footer">
          <button>OK</button>
          <button onClick={() =>closePopup('host_new')}>취소</button>
        </div>
      </div>
      </Modal>
      {/* 호스트 편집*/}
      <Modal
      isOpen={activePopup === 'host_edit'}
      onRequestClose={closePopup}
      contentLabel="새로 만들기"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}

    >
        <div className="host_new_add">
          <div className="popup_header">
            <h1>호스트 수정</h1>
            <button onClick={() =>closePopup('host_new')}>
              <FontAwesomeIcon icon={faTimes} fixedWidth/>
            </button>
          </div>

    
        
        <form action="#">
          <div className="edit_first_content">
                  <div>
                      <label htmlFor="host_cluster">호스트 클러스터</label>
                      <select id="cluster">
                          <option value="default">Default</option>
                      </select>
                  
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
                      <label htmlFor="hostname">호스트이름/IP<FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/></label>
                      <input type="text" id="hostname" />
                  </div>
                  <div>
                      <label htmlFor="ssh_port">SSH 포트</label>
                      <input type="text" id="ssh_port" value="22" />
                  </div>
            </div>

          <div className='py-1'>
            <div className='host_checkboxs'>
              <div className='host_checkbox'>
                  <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">설치 후 호스트를 활성화</label>
              </div>
              <div className='host_checkbox'>
                  <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                  <label htmlFor="headless_mode_info">설치 후 호스트를 다시 시작</label>
                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
              </div>
            </div>

            <div className='host_checkboxs'>
              <span className='px-1'>인증</span>
              <div className='host_textbox' style={{paddingTop:'0'}}>
                  <label htmlFor="user_name">사용자 이름</label>
                  <input type="text" id="user_name" />
              </div>

              <div className='host_text_raido_box'>
                  <div>
                    <input type="radio" id="password" name="name_option" />
                    <label htmlFor="password">암호</label>
                  </div>
                  <input type="text" id="radio1_name" />
              </div>
            </div>

            <div className='vGPU_radiobox'>
              <div className='font-bold'>
                vGPU 배치<FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
              </div>
              <div>
                  <input type="radio" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">통합</label>
              </div>
              <div>
                  <input type="radio" id="memory_balloon" name="memory_balloon" />
                  <label htmlFor="headless_mode">분산</label>
              </div>
            </div>
            
            <div className="host_select_set">
                      <label htmlFor="host_related_action">호스트 연관 배포 작업 선택</label>
                      <select id="host_related_action">
                        <option value="none">없음</option>
                      </select>
            </div>
          </div>
        </form>
      

        <div className="edit_footer">
          <button>OK</button>
          <button onClick={() =>closePopup('host_new')}>취소</button>
        </div>
      </div>
      </Modal>
      {/*삭제 팝업 */}
      <Modal
        isOpen={activePopup === 'delete'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_delete_popup">
          <div className="popup_header">
            <h1>삭제</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='disk_delete_box'>
            <div>
              <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
              <span>다음 항목을 삭제하시겠습니까?</span>
            </div>
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
    </>
    
  );
};

export default HostDu;
