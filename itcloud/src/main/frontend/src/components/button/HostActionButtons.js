import React, { useEffect, useState, useRef, Suspense } from 'react';
import './css/HostAction.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronUp, faChevronDown, faEllipsisVertical } from '@fortawesome/free-solid-svg-icons';

const HostActionButtons = ({
  // onCreate,
  // onEdit,
  // onDelete,
  // onDeactivate, 
  // onActivate, 
  // onRestart,
  // onReInstall,
  // onRegister,
  // onHaOn,
  // onHaOff,
  isEditDisabled,
  status,
  selectedHost, // 다중 선택된 호스트 전달
  selectedHosts = [],
  clusterId
}) => {
  const HostModal = React.lazy(() => import('../Modal/HostModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const HostActionModal = React.lazy(() => import('../Modal/HostActionModal'));
  
  const [modalState, setModalState] = useState({
    isOpen: false,
    type: null, // 'create', 'edit', 'delete', 'manageAction'
    actionData: null, // manageActions에서 필요한 데이터 전달
  });

  const [activeDropdown, setActiveDropdown] = useState(null); // 현재 활성화된 드롭다운
  const dropdownRef = useRef(null);

  const toggleDropDown = (dropdownName) => {
    setActiveDropdown((prev) => (prev === dropdownName ? null : dropdownName));
  };

  const closeDropdowns = () => {
    setActiveDropdown(null);
  };

  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      closeDropdowns();
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);
  
  const openModal = (type, actionData = null) => {
    closeDropdowns();
    setModalState({ isOpen: true, type, actionData });
  };

  const closeModal = () => {
    setModalState({ isOpen: false, type: null, actionData: null });
  };
  
  const isUp = status === 'UP';
  const isMaintenance = status === 'MAINTENANCE';


  const basicActions = [
    { type: 'create', label: '생성' },
    { type: 'edit', label: '편집', disabled: isEditDisabled || !isUp },
    { type: 'delete', label: '삭제', disabled: !isMaintenance }
  ];

  const manageActions = [
    { type: 'deactivate', label: '유지보수', disabled: !isUp },
    { type: 'activate', label: '활성', disabled: isEditDisabled || !isMaintenance },
    { type: 'restart', label: '재시작', disabled: isEditDisabled || !isUp },
    { type: 'reInstall', label: '다시 설치', disabled: isEditDisabled || isUp },
    { type: 'register', label: '인증서 등록', disabled: isEditDisabled || isUp },
    { type: 'haOn', label: '글로벌 HA 유지 관리를 활성화', disabled: isEditDisabled || !isUp },
    { type: 'haOff', label: '글로벌 HA 유지 관리를 비활성화', disabled: isEditDisabled || !isUp },
  ];

  const getContentLabel = (action) => {
    switch (action) {
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

  return (
    <>
      <div className="header_right_btns">
        {basicActions.map(({ type, label, onClick, disabled }, index) => (
          <button 
            key={index} 
            onClick={() => {
              onClick?.(); // 핸들러 실행
              openModal(type); // 모달 열기
            }}
            disabled={disabled}
          >
            {label}
          </button>
        ))}

        {/* 관리 버튼 */}
        <div ref={dropdownRef} className="dropdown-container">
          <button onClick={() => toggleDropDown('manage')} className="manage-button">
            관리
            <FontAwesomeIcon icon={activeDropdown === 'manage' ? faChevronUp : faChevronDown} />
          </button>
          {activeDropdown === 'manage' && (
            <div className="dropdown-menu">
              {manageActions.map(({ type, label, onClick, disabled }, index) => (
                <button 
                  key={index} 
                  onClick={() => {
                    onClick?.(); // 핸들러 실행
                    openModal('manageAction', { actionType: type }); // 모달 열기
                  }}
                  disabled={disabled} 
                  className="dropdown-item"
                >
                  {label}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      <Suspense fallback={<div>Loading...</div>}>
        {modalState.isOpen && modalState.type === 'create' && (
          <HostModal
            isOpen={modalState.isOpen}
            clusterId={clusterId}
            onRequestClose={closeModal}
          />
        )}
        {modalState.isOpen && modalState.type === 'edit' && (
          <HostModal
            isOpen={modalState.isOpen}
            editMode
            hId={selectedHost?.id || null}
            clusterId={clusterId}
            onRequestClose={closeModal}
          />
        )}
        {modalState.isOpen && modalState.type === 'delete' && (
          <DeleteModal
            isOpen={modalState.isOpen}
            type="Host"
            contentLabel="호스트 삭제"
            data={selectedHosts}
            onRequestClose={closeModal}
          />
        )}
        {modalState.isOpen && modalState.type === 'manageAction' && (
          <HostActionModal
            isOpen={modalState.isOpen}
            action={modalState.actionData}
            contentLabel={getContentLabel(modalState.actionData?.actionType)}
            data={selectedHost}
            onRequestClose={closeModal}
          />
        )}
      </Suspense>
    </>
  );
};

export default HostActionButtons;