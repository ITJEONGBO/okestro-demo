import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';

const ClusterModal = ({ isOpen, onRequestClose, onSubmit, editMode = false, clusterData = {} }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
  const [showTooltip, setShowTooltip] = useState(false);

  // 편집 모드일 때 기존 데이터를 불러와서 입력 필드에 채움
  useEffect(() => {
    if (editMode && clusterData) {
      setName(clusterData.name || '');
      setDescription(clusterData.description || '');
      setComment(clusterData.comment || '');
    }
  }, [editMode, clusterData]);

  // 폼 제출 핸들러
  const handleFormSubmit = () => {
    const requestData = {
      name,
      description,
      comment,
    };
    onSubmit(requestData); // 부모 컴포넌트로 데이터를 전달
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '클러스터 편집' : '새 클러스터'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="cluster_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '클러스터 편집' : '새 클러스터'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="flex">
          <div className="network_new_nav">
            <div
              id="cluster_common_btn"
              className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => setSelectedPopupTab('cluster_common_btn')}
            >
              일반
            </div>
            <div
              id="cluster_migration_btn"
              className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => setSelectedPopupTab('cluster_migration_btn')}
            >
              마이그레이션 정책
            </div>
          </div>

          {/* 일반 탭 */}
          {selectedPopupTab === 'cluster_common_btn' && (
            <form className="cluster_common_form py-1">
              <div className="network_form_group">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center">
                  <option value="default">Default</option>
                </select>
              </div>

              <div className="network_form_group">
                <div>
                  <label htmlFor="name">이름</label>
                </div>
                <input
                  type="text"
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>

              <div className="network_form_group">
                <label htmlFor="description">설명</label>
                <input
                  type="text"
                  id="description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="network_form_group">
                <label htmlFor="comment">코멘트</label>
                <input
                  type="text"
                  id="comment"
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                />
              </div>
              {/* 나머지 폼 필드 생략 */}
            </form>
          )}

          {/* 마이그레이션 정책 탭 */}
          {selectedPopupTab === 'cluster_migration_btn' && (
            <form className="py-2">
              <div className="network_form_group">
                <label htmlFor="migration_policy">마이그레이션 정책</label>
                <select id="migration_policy">
                  <option value="default">Default</option>
                </select>
              </div>

              <div className="p-1.5">
                <span className="font-bold">최소 다운타임</span>
                <div>
                  일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다.
                </div>
              </div>

              <div className="p-1.5 mb-1">
                <span className="font-bold">대역폭</span>
                <div className="cluster_select_box">
                  <div className="flex">
                    <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
                  </div>
                  <select id="bandwidth_policy">
                    <option value="default">Default</option>
                  </select>
                </div>
              </div>

              {/* 나머지 마이그레이션 필드 생략 */}
            </form>
          )}
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

export default ClusterModal;
