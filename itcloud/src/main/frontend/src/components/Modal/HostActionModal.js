import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useHost,
  useDeactivateHost,
  useActivateHost
} from '../../api/RQHook';

const HostActionModal = ({ 
    isOpen, 
    action,
    onRequestClose, 
    contentLabel,
    data,
    hostId // 외부에서 전달된 prop TODO 바꿔야함
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');

  const { mutate: deactivateHost } = useDeactivateHost();
  const { mutate: activateHost } = useActivateHost();
  
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
      console.log('**' + data.id);
    }
  }, [data]);

  useEffect(() => {
    console.log('HostActionModal data:', data, id);
  }, [data, id]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다.');
      return;
    }

    if (action === 'deactivate') {
      console.log('deactivate Host');
      handleAction(deactivateHost);
    } else if (action === 'activate') {
      console.log('Deleting Host');
      handleAction(activateHost);
    }
  };

  const handleAction = (actionFn) => {
    if (!id) {
      console.error("Host ID is required for this action.");
      return;
    }
    actionFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 성공 시 모달 닫기
        
        // if (action === 'Datacenter') {
        //   // Datacenter 삭제 후 특정 경로로 이동
        //   navigate('/computing/rutil-manager/datacenters');
        // } else {
        //   // 다른 타입일 경우 기본 동작 수행
        //   const currentPath = location.pathname;
        //   if (currentPath.includes(id)) {
        //     const newPath = currentPath.replace(`/${id}`, '');
        //     navigate(newPath);
        //   } else {
        //     window.location.reload();
        //   }
        // }
      },
      onError: (error) => {
        console.error(`${contentLabel} ${name} 액션 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={`${contentLabel}`}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>호스트 {contentLabel} {id}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {name} 를(을) {contentLabel}하시겠습니까? </span>
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

export default HostActionModal;
