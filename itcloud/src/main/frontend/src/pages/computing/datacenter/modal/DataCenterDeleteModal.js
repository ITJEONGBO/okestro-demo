import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useDeleteDataCenter } from '../../../../api/RQHook';

const DataCenterDeleteModal = ({ onClose, data }) => {
  const [ids, setIds] = useState([]);
  const [names, setNames] = useState([]);
  const { mutate: deleteDataCenter } = useDeleteDataCenter();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (Array.isArray(data)) {
      const ids = data.map((item) => item.id);
      const names = data.map((item) => item.name || '이름 없음'); // name이 없는 경우 처리
      setIds(ids);
      setNames(names);
    }
  }, [data]);

  const handleFormSubmit = () => {
    if (!ids.length) {
      console.error('삭제할 데이터센터 ID가 없습니다.');
      return;
    }

    console.log('Deleting DataCenters:', ids);
    ids.forEach((datacenterId, index) => {
      handleDelete(() => deleteDataCenter(datacenterId), names[index]);
    });

    onClose();
  };

  const handleDelete = (deleteFn, name) => {
    deleteFn({
      onSuccess: () => {
        console.log(`데이터센터 ${name} 삭제 성공`);
        const currentPath = location.pathname;

        // 현재 경로가 삭제된 데이터센터와 관련되면 리다이렉션
        if (currentPath.includes(name)) {
          const newPath = currentPath.replace(`/${name}`, '');
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
      isOpen={true}
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
            <span> {names.join(', ')} 를(을) 삭제하시겠습니까? </span>
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
