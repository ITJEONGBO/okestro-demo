import React, { useEffect, useState, useRef } from 'react';
import './css/HostAction.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronUp, faChevronDown } from '@fortawesome/free-solid-svg-icons';

const HostActionButtons = ({
  onCreate,
  onEdit,
  onDelete,
  onDeactivate, 
  onActivate, 
  onRestart,
  onReInstall,
  onRegister,
  onHaOn,
  onHaOff,
  isEditDisabled,
  status
}) => {
  const [activeDropdown, setActiveDropdown] = useState(null); // 현재 활성화된 드롭다운
  const dropdownRef = useRef(null);
  const dropdownRef2 = useRef(null);

  const toggleDropDown = (dropdownName) => {
    setActiveDropdown((prev) => (prev === dropdownName ? null : dropdownName));
  };

  const closeDropdowns = () => {
    setActiveDropdown(null);
  };

  const handleClickOutside = (event) => {
    if (
      dropdownRef.current && !dropdownRef.current.contains(event.target) &&
      dropdownRef2.current && !dropdownRef2.current.contains(event.target)
    ) {
      closeDropdowns();
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);
  
  const isUp = status === 'UP';
  const isMaintenance = status === 'MAINTENANCE';

  const handleClick = (label, action) => {
    console.log(`Button clicked: ${label}`);
    action?.();
    closeDropdowns(); // 동작 후 드롭다운 닫기
  };

  const manageActions = [
    { onClick: onDeactivate, label: '유지보수', disabled: !isUp },
    { onClick: onActivate, label: '활성', disabled: isEditDisabled || !isMaintenance },
    { onClick: onRestart, label: '재시작', disabled: isEditDisabled || !isUp },
    // { onClick: onStop, label: '중지', disabled: isEditDisabled || isUp },
    { onClick: onReInstall, label: '다시 설치', disabled: isEditDisabled || isUp },
    { onClick: onRegister, label: '인증서 등록', disabled: isEditDisabled || isUp },
  ];
  const settingActions = [
    { onClick: onHaOn, label: '글로벌 HA 유지 관리를 활성화', disabled: isEditDisabled || !isUp },
    { onClick: onHaOff, label: '글로벌 HA 유지 관리를 비활성화', disabled: isEditDisabled || !isUp },
  ];

  return (
    <div className="header_right_btns">
      {onCreate && 
        <button onClick={onCreate}>새로 만들기</button>
      }
      {onEdit && 
        <button onClick={onEdit} disabled={isEditDisabled || !isUp}>편집</button>
      }
      {onDelete && 
        <button onClick={onDelete} disabled={isEditDisabled || isUp || !isMaintenance }>삭제</button>
      } 
      {/* 관리 버튼 */}
      <div ref={dropdownRef} className="dropdown-container">
        <button onClick={() => toggleDropDown('manage')} className="manage-button">
          관리
          <FontAwesomeIcon icon={activeDropdown === 'manage' ? faChevronUp : faChevronDown} />
        </button>
        {activeDropdown === 'manage' && (
          <div className="dropdown-menu">
            {manageActions.map(({ onClick, label, disabled }, index) => (
              <button
                key={index}
                onClick={() => handleClick(label, onClick)}
                disabled={disabled}
                className="dropdown-item"
              >
                {label}
              </button>
            ))}
          </div>
        )}
      </div>

      <div ref={dropdownRef2} className="dropdown-container">
        <button onClick={() => toggleDropDown('settings')} className="manage-button">
          :
          <FontAwesomeIcon icon={activeDropdown === 'settings' ? faChevronUp : faChevronDown} />
        </button>
        {activeDropdown === 'settings' && (
          <div className="dropdown-menu">
            {settingActions.map(({ onClick, label, disabled }, index) => (
              <button
                key={index}
                onClick={() => handleClick(label, onClick)}
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
  );
};

export default HostActionButtons;