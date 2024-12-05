import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useDeleteVm,
} from '../../api/RQHook';

const DeleteVmModal = ({ 
    isOpen, 
    onRequestClose, 
    data,
    vmId
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [diskDelete, setDiskDelete] = useState(true);

  const { mutate: deleteVm } = useDeleteVm();
  

  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || data.alias || '');
      console.log('vm' + data.id);
    }
  }, [data]);

  useEffect(() => {
    console.log('Current data and Id in DeleteModal 삭제데이터:', data, '아아'+id);
  }, [data, id]);


  const handleDelete = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }
    console.log('Deleting Vm ', id, diskDelete);

    deleteVm(
      { vmId: id, detachOnly: diskDelete},
      {
      onSuccess: () => {
        console.log(`VM ${id} deleted successfully with detachOnly: ${diskDelete}`);
        onRequestClose(); // 삭제 성공 시 모달 닫기      
      },
      onError: (error) => {
        console.error(` ${name} 삭제 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1> 가상머신 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {name} 를(을) 삭제하시겠습니까? </span>
            <input 
            type='checkbox'
            id="diskDelete" 
            checked={diskDelete}
            onChange={(e) => setDiskDelete(e.target.value)}
          />
          <label htmlFor="backup">디스크 삭제</label>
          </div>
        </div>

        {/* <div>
          <input 
            type='checkbox'
            id="diskDelete" 
            checked={diskDelete}
            onChange={(e) => setDiskDelete(e.target.value)}
          />
          <label htmlFor="backup">디스크 삭제</label>
        </div> */}

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleDelete}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DeleteVmModal;

