import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import {
  useDeleteVm,
} from '../../api/RQHook';
import ENDPOINTS from '../../api/Endpoints';

const VmDeleteModal = ({ 
    isOpen, 
    onRequestClose, 
    data
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [diskDelete, setDiskDelete] = useState(true);

  const { mutate: deleteVm } = useDeleteVm();
  
  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || data.alias || '');
      console.log(`vm: ${data.id}`);
    }
  }, [data]);

  useEffect(() => {
    console.log('diskDelete state updated:', diskDelete);
  }, [diskDelete]);

  const handleDelete = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }
    console.log('Deleting VM with:', { id, diskDelete });

    deleteVm(
      { vmId: id, detachOnly: diskDelete },
      {
        onSuccess: () => {
          console.log(`DELETE 요청 URL: ${ENDPOINTS.DELETE_VM(id, diskDelete)}`);
          console.log(`VM ${id}, ${diskDelete}`);
          onRequestClose();
        },
        onError: (error) => {
          console.error(`${name} 삭제 오류:`, error);
        },
      }
    );
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
              type="checkbox"
              id="diskDelete"
              checked={diskDelete}
              onChange={(e) => {
                console.log('Checkbox changed:', e.target.checked); // Debug log 추가
                setDiskDelete(e.target.checked);
              }}
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

export default VmDeleteModal;

