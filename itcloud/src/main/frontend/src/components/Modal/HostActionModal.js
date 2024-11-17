import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useHost,
  useDeactivateHost,
  useActivateHost, 
  
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
      console.log(`Calling deactivate API for host ID: ${id}`);
      deactivateHost(id, {
        onSuccess: () => {
          console.log('Deactivate successful');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Deactivate failed:', error);
        },
      });
    } else if (action === 'activate') {
      console.log('활성화 API 호출');
      activateHost(id, {
        onSuccess: () => {
          alert(`${name} 활성화 성공!`);
          onRequestClose(); // 모달 닫기
        },
        onError: (error) => {
          alert('활성화 실패:', error.message);
        },
      });
    }
    // } else if (action === 'restart') {
    //   console.log('restart Host');
    //   handleAction(restartHost);
    // } else if (action === 'stop') {
    //   console.log('stop Host');
    //   handleAction(stopHost);
    // } else if (action === 'reinstall') {
    //   console.log('reinstall Host');
    //   handleAction(reinstallHost);
    // } else if (action === 'register') {
    //   console.log('register Host');
    //   handleAction(registerHost);
    // } else if (action === 'haon') {
    //   console.log('haon Host');
    //   handleAction(haonHost);
    // } else if (action === 'haoff') {
    //   console.log('haoff Host');
    //   handleAction(haoffHost);
    // }
  };

  const handleAction = (actionFn) => {
    if (!id) {
      console.error("Host ID is required for this action.");
      return;
    }
    actionFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 성공 시 모달 닫기
        
        const currentPath = location.pathname;
        if (currentPath.includes(id)) {
          const newPath = currentPath.replace(`/${id}`, '');
          navigate(newPath);
        } else {
          window.location.reload();
        }        
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
            <span> {data.name} 를(을) {contentLabel}하시겠습니까? </span>
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
