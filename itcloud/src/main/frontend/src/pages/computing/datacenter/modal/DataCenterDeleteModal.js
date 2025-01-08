import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useDeleteDataCenter } from '../../../../api/RQHook';

const DataCenterDeleteModal = ({ 
    onClose, 
    data,
}) => {
  const [id, setId] = useState([]);
  const [name, setName] = useState([]);

  const { mutate: deleteDataCenter } = useDeleteDataCenter();

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    setId([data.id]);
    setName([data.name]);
  }, [data]);

  useEffect(() => {
    console.log('DeleteModal:', data, + id);
  }, [data, id]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    } 
    console.log('Deleting Host');
    handleDelete(() => deleteDataCenter(id));
    
    onClose();
  };

  const handleDelete = (deleteFn) => {
    deleteFn(id, {
      onSuccess: () => {
        onClose();
        const currentPath = location.pathname;
        if (currentPath.includes(id)) {
          const newPath = currentPath.replace(`/${id}`, '');
          navigate(newPath);
        } else {
          window.location.reload();
        }
      },
      onError: (error) => {
        console.error(`데이터센터 ${name} 삭제 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={true} // isopen
      onRequestClose={onClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>데이터센터 삭제</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {name} 를(을) 삭제하시겠습니까? </span>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DataCenterDeleteModal;

