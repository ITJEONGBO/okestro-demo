import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronUp, faChevronDown } from '@fortawesome/free-solid-svg-icons';

const DomainActionButtons = ({ 
  onCreate,
  onImport, 
  onEdit, 
  onDelete, 
  onDestory,
  // connection,
  // onLunRefresh,
  // onMaster,
  onAttach,
  onSeparate,
  onActive,
  onMaintenance,
  isEditDisabled, 
  disk = false,
  status
}) => {
  // 도메인 생성, 도메인 가져오기, 도메인 관리(편집), 삭제, connection, lun 새로고침, 파괴, 마스터 스토리지 도메인으로 선택
  // 데이터센터: 연결, 분리, 활성, 유지보수
  const navigate = useNavigate();

  const [isDropDownOpen, setIsDropDownOpen] = useState(false);

  const toggleDropDown = () => {
    setIsDropDownOpen(!isDropDownOpen);
  };

  const isUp = status === 'UP';
  const isMaintenance = status === 'MAINTENANCE';

  const handleClick = (label, action) => {
    console.log(`Button clicked: ${label}`);
    action?.();
  };

  const settingActions = [
    { onClick: onDestory, label: '파괴', disabled: !isUp },
    // { onClick: onMaster, label: '마스터 스토리지 도메인으로 선택', disabled: isEditDisabled || !isMaintenance }
  ];

  return (
    <div className="header_right_btns">
      {onCreate && 
        <button onClick={onCreate}>생성</button>
      }
      {onImport && (
        <button onClick={onImport}>가져오기</button>
      )}
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집(도메인 관리)</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isEditDisabled || isUp }>제거</button>
      )}
      {onAttach && (
        <button onClick={onAttach} disabled={isEditDisabled}>연결</button>
      )}
      {onSeparate && (
        <button onClick={onSeparate} disabled={isEditDisabled}>분리</button>
      )}
      {onActive && (
        <button onClick={onActive} disabled={isEditDisabled || !isMaintenance }>활성</button>
      )}
      {onMaintenance && (
        <button onClick={onMaintenance} disabled={isEditDisabled || isUp}>유지보수</button>
      )}

      <div className="dropdown-container">
        <button onClick={toggleDropDown} className="manage-button">
          관리
          <FontAwesomeIcon icon={isDropDownOpen ? faChevronUp : faChevronDown} />
        </button>
        {isDropDownOpen && (
          <div className="dropdown-menu">
            { settingActions.map(({ onClick, label, disabled }, index) => (
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

      {disk === true && (
        <button onClick={() => navigate('/storages/disks')}>디스크</button>
      )}
    </div>
  );
};

export default DomainActionButtons;