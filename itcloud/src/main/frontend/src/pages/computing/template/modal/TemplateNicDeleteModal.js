import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useDeleteNetworkFromTemplate } from '../../../../api/RQHook';

const TemplateNicDeleteModal = ({ isOpen, onClose, data }) => {
  const [ids, setIds] = useState([]);
  const [names, setNames] = useState([]);
  const { mutateAsync: deleteNetworkFromTemplate } = useDeleteNetworkFromTemplate(); // 비동기 삭제 훅

  useEffect(() => {
    if (Array.isArray(data)) {
      setIds(data.map((item) => ({ templateId: item.templateId, nicId: item.id }))); // 템플릿 ID와 NIC ID 설정
      setNames(data.map((item) => item.name || 'Unnamed NIC'));
    } else if (data) {
      setIds([{ templateId: data.templateId, nicId: data.id }]);
      setNames([data.name || 'Unnamed NIC']);
    }
  }, [data]);

  const handleFormSubmit = async () => {
    if (!ids.length) {
      console.error('삭제할 NIC ID가 없습니다.');
      return;
    }

    console.log('Attempting to delete NICs:', ids);

    for (const { templateId, nicId } of ids) {
      try {
        await deleteNetworkFromTemplate({ templateId, nicId }); // NIC 삭제 API 호출
        console.log(`NIC ${nicId} deleted successfully from Template ${templateId}.`);
      } catch (error) {
        console.error(`Error deleting NIC ${nicId} from Template ${templateId}:`, error);
      }
    }

    console.log('All NIC deletion attempts completed.');
    onClose(); // 삭제 완료 후 모달 닫기
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage-delete-popup">
        <div className="popup-header">
          <h1>NIC 삭제</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk-delete-box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {names.join(', ')} 를(을) 삭제하시겠습니까? </span>
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

export default TemplateNicDeleteModal;
