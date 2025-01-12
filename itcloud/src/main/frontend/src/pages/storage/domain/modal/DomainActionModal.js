import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useActivateDomain, 
  useAttachDomain,
  useDestroyDomain,
  useDetachDomain,
  useMaintenanceDomain
} from '../../../../api/RQHook';

// 도메인에서 실행하는 거지만 데이터센터
const DomainActionModal = ({ action, data, datacenterId, onClose }) => {
  // action으로 type 전달
  const { mutate: activateDomain } = useActivateDomain();
  const { mutate: attachDomain } = useAttachDomain();
  const { mutate: detachDomain } = useDetachDomain();
  const { mutate: maintenanceDomain } = useMaintenanceDomain();
  const { mutate: destroyDomain } = useDestroyDomain();


  const [id, setId] = useState('');
  const [name, setName] = useState('');
  
  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
      console.log('**' + data.id);
    }
  }, [data]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다.');
      return;
    }

    if (action === 'activate') {
      console.log(`activate ${id}, dc: ${datacenterId}` )
      handleAction(activateDomain)
    } else if (action === 'attach') {
      console.log(`attach ${id}, dc: ${datacenterId}` )
      handleAction(attachDomain)
    } else if (action === 'detach') {
      console.log(`detach ${id}, dc: ${datacenterId}` )
      handleAction(detachDomain)
    } else if (action === 'maintenance') {
      console.log(`maintenance ${id}, dc: ${datacenterId}` )
      handleAction(maintenanceDomain)
    } else if (action === 'destroy') { 
      console.log(`destroy ${id}` )
      handleAction(destroyDomain)
    }
  }

  const handleAction = (actionFn) => {
    actionFn(
      { domainId: id, dataCenterId: datacenterId }, 
      {
        onSuccess: () => {
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
      isOpen={true}
      onRequestClose={onClose}
      contentLabel={`${action}`}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1> 스토리지 도메인 {action}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {data.name} 를(을) {action} 하시겠습니까? {datacenterId}</span>
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
