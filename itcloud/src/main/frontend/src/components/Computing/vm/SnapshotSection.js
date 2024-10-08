import { faCamera, faChevronRight, faExclamationTriangle, faEye, faNewspaper, faServer, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';
import TableColumnsInfo from '../../table/TableColumnsInfo';

// 스냅샷
const SnapshotSection = () => {
  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => {
      setActivePopup(popupType);
    };
  const closePopup = () => {
      setActivePopup(null);
  };
    return (
      <>
          <div className="header_right_btns">
            <button className="snap_create_btn" onClick={() => openPopup('new')}>생성</button>
            <button className='disabled'>미리보기</button>
            <button className='disabled'>커밋</button>
            <button className='disabled'>되돌리기</button>
            <button className='disabled'>삭제</button>
            <button className='disabled'>복제</button>
            <button className='disabled'>템플릿 생성</button>
          </div>
          <div className="snapshot_content">
            <div className="snapshot_content_left">
              <div><FontAwesomeIcon icon={faCamera} fixedWidth/></div>
              <span>Active VM</span>
            </div>
            <div className="snapshot_content_right">
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>일반</span>
                <FontAwesomeIcon icon={faEye} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>디스크</span>
                <FontAwesomeIcon icon={faTrash} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>네트워크 인터페이스</span>
                <FontAwesomeIcon icon={faServer} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>설치된 애플리케이션</span>
                <FontAwesomeIcon icon={faNewspaper} fixedWidth/>
              </div>
            </div>
          </div>
          <div className="snapshot_content">
            <div className="snapshot_content_left">
              <div><FontAwesomeIcon icon={faCamera} fixedWidth/></div>
              <span>Active VM</span>
            </div>
            <div className="snapshot_content_right">
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>일반</span>
                <FontAwesomeIcon icon={faEye} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>디스크</span>
                <FontAwesomeIcon icon={faTrash} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>네트워크 인터페이스</span>
                <FontAwesomeIcon icon={faServer} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>설치된 애플리케이션</span>
                <FontAwesomeIcon icon={faNewspaper} fixedWidth/>
              </div>
            </div>
          </div>
          {/*생성 팝업 */}
          <Modal
        isOpen={activePopup === 'new'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="snapshot_new_popup">
          <div className="popup_header">
            <h1>스냅샷 생성</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='p-1'>
            <div className='host_textbox mb-1'>
                <label htmlFor="user_name">사용자 이름</label>
                <input type="text" id="user_name" />
            </div>
            <div>
              <div className='font-bold'>포함할 디스크 :</div>
              <div className='snapshot_new_table'>
                <TableOuter 
                    columns={TableColumnsInfo.SNAPSHOT_NEW}
                    data={[]}
                    onRowClick={() => console.log('Row clicked')}
                  />
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
      </>
    );
  };

  export default SnapshotSection;