import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import {
  useDeactivateHost,
  useActivateHost, 
  useRestartHost
} from '../../../../api/RQHook';

const HostActionModal = ({ 
    isOpen, 
    action,
    onRequestClose, 
    contentLabel,
    data
}) => {
  const { mutate: deactivateHost } = useDeactivateHost();
  const { mutate: activateHost } = useActivateHost();
  const { mutate: restartHost } = useRestartHost();

  const [id, setId] = useState('');
  const [name, setName] = useState('');
  
  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
      console.log('**' + data.id);
    }
  }, [data]);

  const getContentLabel = (contentLabel) => {
    switch (contentLabel) {
      case 'deactivate': return '유지보수';
      case 'activate': return '활성';
      case 'restart': return '재시작';
      case 'reinstall': return '다시 설치';
      case 'register': return '인증서 등록';
      case 'haon': return 'HA 활성화';
      case 'haoff': return 'HA 비활성화';
      default: return '';
    }
  };

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다.');
      return;
    }

    if (action === 'deactivate') {
      console.log('deactivate ' + id)
      handleAction(deactivateHost)
    } else if (action === 'activate') {
      console.log('activate ' + {id})
      handleAction(activateHost)
    } 
    else if (action === 'restart') {
      console.log('restart Host');
      handleAction(restartHost);
    }
    // } else if (action === 'stop') {
    //   console.log('stop Host');
    //   handleAction(stopHost);
    // } 
    // else if (action === 'reinstall') {
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
    actionFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 성공 시 모달 닫기
        
        // const currentPath = location.pathname;
        // if (currentPath.includes(id)) {
        //   const newPath = currentPath.replace(`/${id}`, '');
        //   navigate(newPath);
        // } else {
        //   window.location.reload();
        // }        
      },
      onError: (error) => {
        console.error(`${contentLabel} ${name} 액션 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={true}
      onRequestClose={onRequestClose}
      contentLabel={`${contentLabel}`}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>호스트 {contentLabel}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {data.name} 를(을) {getContentLabel}하시겠습니까? </span>
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
