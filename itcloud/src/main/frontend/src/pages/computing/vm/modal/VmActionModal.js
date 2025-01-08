import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useStartVM, 
  usePauseVM, 
  usePowerOffVM,
  useShutdownVM,
  useRebootVM,
  useResetVM,
  selectedVms
} from '../../../../api/RQHook';

const VmActionModal = ({ 
    isOpen, 
    action,
    onRequestClose, 
    contentLabel,
    data
}) => {
  const { mutate: startVM } = useStartVM();
  const { mutate: pauseVM } = usePauseVM(); // 일시중지
  const { mutate: powerOffVM } = usePowerOffVM(); // 종료
  const { mutate: shutdownVM } = useShutdownVM();
  const { mutate: rebootVM } = useRebootVM();
  const { mutate: resetVM } = useResetVM();

  const navigate = useNavigate();
  const location = useLocation();

  const [id, setId] = useState('');
  const [name, setName] = useState('');
  
  useEffect(() => {
    if (data) {
      if (Array.isArray(data)) {
        // 다중 선택된 경우
        setId(data.map((vm) => vm.id).join(', ')); // ID를 ','로 연결
        setName(data.map((vm) => vm.name || '이름 없음').join(', '));
      } else {
        // 단일 선택된 경우
        setId(data.id || ''); // 단일 VM의 ID 설정
        setName(data.name || '이름 없음');
      }
      console.log('Selected VM data:', data);
      console.log('Selected VM ids:', data.map ? data.map((vm) => vm.id) : data.id);
    }
  }, [data]);

  
  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다.');
      return;
    }

    if (action === 'start') {
      const invalidVms = Array.isArray(data)
      ? data.filter((vm) => !vm.diskAttachmentVo || !vm.diskAttachmentVo.id)
      : !data.diskAttachmentVo || !data.diskAttachmentVo.id;
  
    if (invalidVms && invalidVms.length > 0) {
      alert('선택된 가상머신 중 디스크가 연결되지 않은 항목이 있어 실행할 수 없습니다.');
      return;
    }
      console.log('start ' + id)
      handleAction(startVM)
    } else if (action === 'pause') {
      console.log('pause ' + {id})
      handleAction(pauseVM)
    } else if (action === 'powerOff') {
      console.log('powerOff Vm');
      handleAction(powerOffVM);
    } else if (action === 'shutdown') {
      console.log('shutdown Vm');
      handleAction(shutdownVM);
    } else if (action === 'reboot') {
      console.log('reboot Vm');
      handleAction(rebootVM);
    } else if (action === 'reset') {
      console.log('reset Vm');
      handleAction(resetVM);
    } 

    
  };

  const handleAction = (actionFn) => {
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
          <h1>가상머신 {contentLabel}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            {Array.isArray(data) ? (
              <span>{name} 를(을) {contentLabel}하시겠습니까?</span>
            ) : (
              <span>{data.name} 를(을) {contentLabel}하시겠습니까?</span>
            )}
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

export default VmActionModal;
