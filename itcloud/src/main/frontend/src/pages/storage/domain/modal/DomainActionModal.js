import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import {
  useActivateDomain, 
  useAttachDomain,
  useDetachDomain,
  useMaintenanceDomain
} from '../../../../api/RQHook';
import toast from 'react-hot-toast';

// 도메인에서 실행하는 거지만 데이터센터
const DomainActionModal = ({ isOpen, action, data, datacenterId, onClose }) => {
  // action으로 type 전달
  const { mutate: attachDomain } = useAttachDomain();
  const { mutate: detachDomain } = useDetachDomain();
  const { mutate: activateDomain } = useActivateDomain();
  const { mutate: maintenanceDomain } = useMaintenanceDomain();
  
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  
  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
    }
  }, [data]);

  const getContentLabel = () => {
    const labels = {
      attach: '연결',
      detach: '분리',
      activate: '활성',
      maintenance: '유지보수'
    };
    return labels[action] || '';
  };


  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다.');
      return;
    }

    const actionMap = {
      attach: attachDomain,
      detach: detachDomain,
      activate: activateDomain,
      maintenance: maintenanceDomain
    };
    const actionFn = actionMap[action];
    handleAction(actionFn);
  }

  const handleAction = (actionFn) => {
    actionFn(
      { domainId: id, dataCenterId: datacenterId }, 
      {
        onSuccess: () => {
          toast.success(`${data?.name} ${action} 성공`);
          onClose(); // 삭제 성공 시 모달 닫기
        },
        onError: (error) => {
          console.error(`${action} ${name} 액션 오류:`, error);
        },
      }
    );
  };
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={`${action}`}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage-delete-popup">
        <div className="popup-header">
          <h1> 스토리지 도메인 {getContentLabel(action)}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk-delete-box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {data?.name} 를(을) {getContentLabel(action)} 하시겠습니까?</span>
          </div>
        </div>

        <div className="edit-footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DomainActionModal;
