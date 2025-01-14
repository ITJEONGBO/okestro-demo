import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useDeleteDiskFromVM, useDeleteVm, useDisksFromVM } from '../../../../api/RQHook';
import { useNavigate } from 'react-router-dom';

const VmDeleteModal = ({ isOpen, onClose, data }) => {
  const navigate = useNavigate();
  const [ids, setIds] = useState([]);
  const [names, setNames] = useState([]);
  const [diskDeleteStates, setDiskDeleteStates] = useState({});
  
  const { data: disks = [] } = useDisksFromVM(setIds[0]);

  const { mutate: deleteVm } = useDeleteVm();
  const { mutate: deleteVmDisk } = useDeleteDiskFromVM();

  useEffect(() => {
    if (Array.isArray(data)) {
      const ids = data.map((item) => item.id);
      const names = data.map((item) => item.name);
      setIds(ids);
      setNames(names);
      setDiskDeleteStates(ids.reduce((acc, id) => ({ ...acc, [id]: true }), {}));
    } else if (data) {
      setIds([data.id]);
      setNames([data.name]);
      setDiskDeleteStates({ [data.id]: true });
    }
  }, [data]);
  
  const handleCheckboxChange = (vmId) => {
    setDiskDeleteStates((prevStates) => ({
      ...prevStates,
      [vmId]: !prevStates[vmId],
    }));
  };

  const handleFormSubmit = () => {
    if (!ids.length) {
      console.error('삭제할 가상머신 ID가 없습니다.');
      return;
    }
  
    ids.forEach((vmId, index) => {
      deleteVm(
        { vmId: vmId, detachOnly: !diskDeleteStates[vmId] },
        {
          onSuccess: () => {
            if (ids.length === 1 || index === ids.length - 1) {
              onClose();
              navigate('/computing/vms');
            }
          },
          onError: (error) => {
            console.error(`가상머신 삭제 오류:`, error);
          },
        }
      );
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vm-delete-popup" style={{ height: 'auto' }}>
        <div className="popup-header">
          <h1>가상머신 삭제</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk-delete-box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span>
            선택한 가상머신을 삭제하시겠습니까?
          </span>
          </div>

          {/* <div className='disk-delete-checkbox'> */}
            {ids.map((vmId, index) => (
              <div key={vmId}>
                <strong>{names[index]}</strong>
                <input
                  type="checkbox"
                  id={`diskDelete-${vmId}`}
                  checked={diskDeleteStates[vmId]}
                  onChange={() => handleCheckboxChange(vmId)}
                />
                <label htmlFor={`diskDelete-${vmId}`}>디스크 삭제</label>
              </div>
            ))}
          </div>
        {/* </div> */}

        <div className="edit-footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={ handleFormSubmit }>OK</button>
          <button onClick={ onClose }>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmDeleteModal;
