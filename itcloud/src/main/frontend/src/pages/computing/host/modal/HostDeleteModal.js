import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useDeleteHost } from '../../../../api/RQHook';

const HostDeleteModal = ({ 
    onRequestClose, 
    data,
}) => {
  const [id, setId] = useState([]);
  const [name, setName] = useState([]);

  const { mutate: deleteHost } = useDeleteHost();

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (data) {
      if (Array.isArray(data)) {
        // data가 배열인 경우
        setId(data.map((item) => item.id));
        setName(data.map((item) => item.name || '').join(', '));
      } else {
        // data가 단일 객체인 경우
        setId([data.id]);
        setName([data.name || data.alias || '']);
      }
    }
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
    id.forEach((hostId, index) => {
      handleDelete(() => deleteHost(hostId), name[index]);
    });
    onRequestClose();
  };

  const handleDelete = (deleteFn) => {
    deleteFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 성공 시 모달 닫기
        
        
        // if (type === 'Host') {
        //   // Datacenter 삭제 후 특정 경로로 이동
        //   navigate('/computing/rutil-manager/hosts');
        // } else {
          // 다른 타입일 경우 기본 동작 수행
          const currentPath = location.pathname;
          if (currentPath.includes(id)) {
            const newPath = currentPath.replace(`/${id}`, '');
            navigate(newPath);
          } else {
            window.location.reload();
          }
        // }
      },
      onError: (error) => {
        console.error(`호스트 ${name} 삭제 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={true} // isopen
      onRequestClose={onRequestClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>호스트 삭제</h1>
          <button onClick={onRequestClose}>
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
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default HostDeleteModal;

