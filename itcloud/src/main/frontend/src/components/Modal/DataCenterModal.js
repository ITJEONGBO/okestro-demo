import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

const DataCenterModal = ({ isOpen, onRequestClose, onSubmit, editMode = false, data = {} }) => {
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [cluster, setCluster] = useState('공유됨');
  const [compatibility, setCompatibility] = useState('4.7');
  const [quotaMode, setQuotaMode] = useState('비활성화됨');

  // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && data) {
      setName(data.name || '');
      setComment(data.comment || '');
      setCluster(data.cluster || '공유됨');
      setCompatibility(data.compatibility || '4.7');
      setQuotaMode(data.quotaMode || '비활성화됨');
    } else {
      setName('');
      setComment('');
      setCluster('공유됨');
      setCompatibility('4.7');
      setQuotaMode('비활성화됨');
    }
  }, [editMode, data]);

  const handleFormSubmit = () => {
    const requestData = {
      name,
      comment,
      cluster,
      compatibility,
      quotaMode
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
              onChange={(e) => setName(e.target.value)}  // 상태 업데이트
            />
          </div>
          <div>
            <label htmlFor="comment">설명</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}  // 상태 업데이트
            />
          </div>
          <div>
            <label htmlFor="cluster">클러스터</label>
            <select
              id="cluster"
              value={cluster}
              onChange={(e) => setCluster(e.target.value)}  // 상태 업데이트
            >
              <option value="공유됨">공유됨</option>
            </select>
          </div>
          <div>
            <label htmlFor="compatibility">호환버전</label>
            <select
              id="compatibility"
              value={compatibility}
              onChange={(e) => setCompatibility(e.target.value)}  // 상태 업데이트
            >
              <option value="4.7">4.7</option>
            </select>
          </div>
          <div>
            <label htmlFor="quota_mode">쿼터 모드</label>
            <select
              id="quota_mode"
              value={quotaMode}
              onChange={(e) => setQuotaMode(e.target.value)}  // 상태 업데이트
            >
              <option value="비활성화됨">비활성화됨</option>
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
