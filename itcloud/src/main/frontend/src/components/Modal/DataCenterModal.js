import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

const DataCenterModal = ({ 
  isOpen, 
  onRequestClose, 
  onSubmit, 
  editMode = false, 
  data = {}  // 부모 컴포넌트에서 전달된 데이터
}) => {
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [storageType, setStorageType] = useState('공유됨');
  const [version, setVersion] = useState('4.7');
  const [quotaMode, setQuotaMode] = useState('비활성화됨');

  // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && data) {
      setName(data.name || '');
      setComment(data.comment || '');
      setDescription(data.description || '');
      setStorageType(data.storageType == false ? '공유됨' : '로컬');
      setVersion(data.version || '4.7');
      setQuotaMode(data.quotaMode === 'DISABLED' ? '비활성화됨' : data.quotaMode);
    } else {
      // 새 데이터 센터 생성 모드
      setName('');
      setComment('');
      setDescription('');
      setStorageType('공유됨');
      setVersion('4.7');
      setQuotaMode('비활성화됨');
    }
  }, [editMode, data]);

  const handleFormSubmit = () => {
    const requestData = {
      name,
      comment,
      description,
      storageType: storageType === '공유됨' ? false : true,
      version,
      quotaMode: quotaMode === '비활성화됨' ? 'DISABLED' : quotaMode
    };
    onSubmit(requestData);  // 부모 컴포넌트로 데이터를 전달
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '데이터 센터 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="datacenter_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '데이터 센터 편집' : '새로운 데이터 센터'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="datacenter_new_content">
          <div>
            <label htmlFor="name1">이름</label>
            <input
              type="text"
              id="name1"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="comment">설명</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="description">세부 설명</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="storageType">스토리지 타입</label>
            <select
              id="storageType"
              value={storageType}
              onChange={(e) => setStorageType(e.target.value)}
            >
              <option value="공유됨">공유됨</option>
              <option value="로컬">로컬</option>
            </select>
          </div>
          <div>
            <label htmlFor="version">호환버전</label>
            <select
              id="version"
              value={version}
              onChange={(e) => setVersion(e.target.value)}
            >
              <option value="4.7">4.7</option>
            </select>
          </div>
          <div>
            <label htmlFor="quota_mode">쿼터 모드</label>
            <select
              id="quota_mode"
              value={quotaMode}
              onChange={(e) => setQuotaMode(e.target.value)}
            >
              <option value="비활성화됨">비활성화됨</option>
              <option value="활성화됨">활성화됨</option>
            </select>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DataCenterModal;


{/* 데이터 센터 생성 모달 */}
  {/* <Modal
    isOpen={activePopup === 'datacenter_new'}
    onRequestClose={closePopup}
    contentLabel="새로 만들기"
    className="Modal"
    overlayClassName="Overlay"
    shouldCloseOnOverlayClick={false}
  >
    <div className="datacenter_new_popup">
      <div className="popup_header">
        <h1>새로운 데이터 센터</h1>
        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
      </div>

      <div className="datacenter_new_content">
        <div>
          <label htmlFor="name1">이름</label>
          <input type="text" id="name1" />
        </div>
        <div>
          <label htmlFor="comment">설명</label>
          <input type="text" id="comment" />
        </div>
        <div>
          <label htmlFor="cluster">클러스터</label>
          <select id="cluster">
            <option value="공유됨">공유됨</option>
          </select>
        </div>
        <div>
          <label htmlFor="compatibility">호환버전</label>
          <select id="compatibility">
            <option value="4.7">4.7</option>
          </select>
        </div>
        <div>
          <label htmlFor="quota_mode">쿼터 모드</label>
          <select id="quota_mode">
            <option value="비활성화됨">비활성화됨</option>
          </select>
        </div>
        <div>
          <label htmlFor="comment">코멘트</label>
          <input type="text" id="comment" />
        </div>
      </div>

      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={() => closePopup('edit')}>취소</button>
      </div>
    </div>
  </Modal>
    */}

{/* 데이터 센터 편집 모달 */}
// <Modal
//   isOpen={activePopup === 'datacenter_edit'}
//   onRequestClose={closePopup}
//   contentLabel="새로 만들기"
//   className="Modal"
//   overlayClassName="Overlay"
//   shouldCloseOnOverlayClick={false}
// >
//   <div className="datacenter_new_popup">
//     <div className="popup_header">
//       <h1>데이터 센터 수정</h1>
//       <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
//     </div>

//     <div className="datacenter_new_content">
//       <div class="disabled">
//         <label htmlFor="name1">이름</label>
//         <input type="text" id="name1" />
//       </div>
//       <div>
//         <label htmlFor="comment">설명</label>
//         <input type="text" id="comment" />
//       </div>
//       <div>
//         <label htmlFor="cluster">클러스터</label>
//         <select id="cluster">
//           <option value="공유됨">공유됨</option>
//         </select>
//       </div>
//       <div>
//         <label htmlFor="compatibility">호환버전</label>
//         <select id="compatibility">
//           <option value="4.7">4.7</option>
//         </select>
//       </div>
//       <div>
//         <label htmlFor="quota_mode">쿼터 모드</label>
//         <select id="quota_mode">
//           <option value="비활성화됨">비활성화됨</option>
//         </select>
//       </div>
//       <div>
//         <label htmlFor="comment">코멘트</label>
//         <input type="text" id="comment" />
//       </div>
//     </div>

//     <div className="edit_footer">
//       <button style={{ display: 'none' }}></button>
//       <button>OK</button>
//       <button onClick={() => closePopup('edit')}>취소</button>
//     </div>
//   </div>
// </Modal>