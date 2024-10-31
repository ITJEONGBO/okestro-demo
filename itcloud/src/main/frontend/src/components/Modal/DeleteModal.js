import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import {
  useDeleteDataCenter,
  useDeleteCluster,
  useDeleteNetwork, // 네트워크 삭제 추가
} from '../../api/RQHook';

const DeleteModal = ({ 
    isOpen, 
    type,
    onRequestClose, 
    contentLabel, 
    data 
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');

  const { mutate: deleteDataCenter } = useDeleteDataCenter();
  const { mutate: deleteCluster } = useDeleteCluster();
  const { mutate: deleteNetwork } = useDeleteNetwork(); // 네트워크 삭제 추가

  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
      console.log('**'+data.id);
    }
  }, [data]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }

    if (type === 'Datacenter') {
      console.log('Deleting Data Center');
      handleDelete(deleteDataCenter);
    } else if (type === 'Cluster') {
      console.log('Deleting Cluster');
      handleDelete(deleteCluster);
    } else if (type === 'Network') { // Network 삭제 처리 추가
      console.log('Deleting Network');
      handleDelete(deleteNetwork);
    }
  };

  const handleDelete = (deleteFn) => {
    deleteFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 완료 후 모달 닫기
      },
      onError: (error) => {
        console.error(`${contentLabel} ${name} 삭제 오류:`, error);
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
      style={{ overlay: { backgroundColor: 'transparent' } }}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>{contentLabel} 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span>{contentLabel} {name} 를(을) 삭제하시겠습니까?</span>
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

export default DeleteModal;
