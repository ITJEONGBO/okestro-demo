import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronUp, faChevronDown } from '@fortawesome/free-solid-svg-icons';

const VmActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
  onConsole, 
  onStart,
  onPause,
  onStop,
  onReboot,
  templates,
  snapshots,
  migration,
  onExport,
  onCopy,
  addTemplate,
  exportOva,
  isEditDisabled,
  status
}) => {
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

  const manageActions = [
    { onClick: onStart, label: '실행', disabled: isEditDisabled || isUp },
    { onClick: onPause, label: '일시중지', disabled: isEditDisabled || !isUp },
    { onClick: onStop, label: '종료', disabled: isEditDisabled || !isUp },
    { onClick: onReboot, label: '재부팅', disabled: isEditDisabled || !isUp },
  ];

  const etcActions = [
    { onClick: onExport, label: '가져오기', disabled: isEditDisabled },
    { onClick: onCopy, label: '가상머신 복제', disabled: isEditDisabled },
    { onClick: addTemplate, label: '템플릿 생성', disabled: isEditDisabled},
    { onClick: exportOva, label: 'OVA로 내보내기', disabled: isEditDisabled}
  ];


  return (
    <div className="header_right_btns">
      {onCreate && 
        <button onClick={onCreate}>새로 만들기</button>
      }
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isEditDisabled}>제거</button>
      )}
      
      {manageActions.map(({ onClick, label, disabled }, index) => (
        <button
          key={index}
          onClick={() => handleClick(label, onClick)}
          disabled={disabled}
        >
          {label}
        </button>
      ))}

      {onConsole && (
        <button onClick={onConsole} disabled={isEditDisabled}>콘솔</button>
      )}

      {templates && (
        <button onClick={templates}>템플릿</button>
      )}
      {snapshots && (
        <button onClick={snapshots} disabled={isEditDisabled}>스냅샷</button>
      )}
      {migration && (
        <button onClick={migration}>마이그레이션</button>
      )}

      <div className="dropdown-container">
        <button onClick={toggleDropDown} className="manage-button">
          관리
          <FontAwesomeIcon icon={isDropDownOpen ? faChevronUp : faChevronDown} />
        </button>
        {isDropDownOpen && (
          <div className="dropdown-menu">
            {etcActions.map(({ onClick, label, disabled }, index) => (
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

export default VmActionButtons;