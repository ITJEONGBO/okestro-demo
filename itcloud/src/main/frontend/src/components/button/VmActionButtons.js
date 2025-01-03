import React, { useEffect, useRef, useState } from 'react';
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
  onPowerOff,
  onReboot,
  onReset,
  templates,
  snapshots,
  migration,
  onExport,
  onCopy,
  addTemplate,
  exportOva,
  isEditDisabled,
  isMigrationDisabled,
  isDeleteDisabled,
  isPauseDisabled,
  status,
}) => {
  const [isDropDownOpen, setIsDropDownOpen] = useState(false);
  const [isStopDropDownOpen, setIsStopDropDownOpen] = useState(false); // 종료 버튼의 드롭다운 상태
  const [isRebootDropDownOpen, setIsRebootDropDownOpen] = useState(false); // 재부팅 버튼의 드롭다운 상태

  const toggleDropDown = () => {
    setIsDropDownOpen(!isDropDownOpen);
  };

  const toggleStopDropDown = () => {
    setIsStopDropDownOpen(!isStopDropDownOpen); // 종료 버튼의 드롭다운 토글 함수
  };
  const toggleRebootDropDown = () => {
    setIsRebootDropDownOpen(!isRebootDropDownOpen); // 재부팅 버튼의 드롭다운 토글 함수
  };
  const isUp = status === 'UP';

  const handleClick = (label, action) => {
    console.log(`Button clicked: ${label}`);
    action?.();
  };

  const manageActions = [
    { onClick: onStart, label: '실행', disabled: !isPauseDisabled },
    { onClick: onPause, label: '일시중지',  disabled: isPauseDisabled || isDeleteDisabled},
  ];

  const rebootOptions = [
    { onClick: onReboot, label: '재부팅', disabled: isPauseDisabled || isDeleteDisabled },
    { onClick: onReset, label: '재설정', disabled: isPauseDisabled  },
  ];

  const stopOptions = [
    { onClick: onStop, label: '종료', disabled: isPauseDisabled  },
    { onClick: onPowerOff, label: '전원끔', disabled: isPauseDisabled  },
  ];

  // 드롭다운 외부 클릭 감지 코드 추가
  const dropdownRef = useRef(null);
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target) &&
        !event.target.closest('.dropdown-item') &&
        !event.target.closest('.section_table_outer') &&
        !event.target.closest('.Modal')
      ) {
        setIsDropDownOpen(false);
        setIsStopDropDownOpen(false);
        setIsRebootDropDownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div className="header_right_btns">
      {onCreate && <button onClick={onCreate}>새로 만들기</button>}
      {onEdit && <button onClick={onEdit} disabled={isEditDisabled}>편집</button>}
      
      {manageActions.map(({ onClick, label, disabled }, index) => (
        <button
          key={index}
          onClick={() => handleClick(label, onClick)}
          disabled={disabled}
        >
          {label}
        </button>
      ))}

      <div className="dropdown-container">
          <button onClick={toggleRebootDropDown} disabled={isPauseDisabled || isDeleteDisabled}>
            재부팅
            <FontAwesomeIcon icon={isRebootDropDownOpen ? faChevronUp : faChevronDown} />
          </button>
          {isRebootDropDownOpen && (
            <div className="dropdown-menu">
              {rebootOptions.map(({ onClick, label }, index) => (
                <button
                  key={index}
                  onClick={onClick}
                  className="dropdown-item"
                >
                  {label}
                </button>
              ))}
            </div>
          )}
      </div>

      <div className="dropdown-container">
          <button onClick={toggleStopDropDown} disabled={isPauseDisabled}>
             종료
            <FontAwesomeIcon icon={isStopDropDownOpen ? faChevronUp : faChevronDown} />
          </button>
          {isStopDropDownOpen && (
            <div className="dropdown-menu">
              {stopOptions.map(({ onClick, label }, index) => (
                <button
                  key={index}
                  onClick={onClick}
                  className="dropdown-item"
                >
                  {label}
                </button>
              ))}
            </div>
          )}
      </div>

      {onConsole && <button onClick={onConsole} disabled={isEditDisabled}>콘솔</button>}
      {templates && <button onClick={templates}>템플릿</button>}
      {snapshots && <button onClick={snapshots} disabled={isEditDisabled}>스냅샷</button>}
      {migration && <button onClick={migration} disabled={isMigrationDisabled || isEditDisabled}>마이그레이션</button>}

      <div className="dropdown-container">
        <button ref={dropdownRef} onClick={toggleDropDown} className="manage-button">
          관리
          <FontAwesomeIcon icon={isDropDownOpen ? faChevronUp : faChevronDown} />
        </button>
        {isDropDownOpen && (
          <div className="dropdown-menu">
            {onExport && <button className="dropdown-item"onClick={onExport}>가져오기</button>}
            {onCopy && <button className="dropdown-item" onClick={onCopy} disabled={isEditDisabled}>가상머신 복제</button>}
            {onDelete && <button className="dropdown-item" onClick={onDelete} disabled={isDeleteDisabled}>삭제</button>}
            {addTemplate && <button className="dropdown-item" onClick={addTemplate} disabled={isEditDisabled || !isPauseDisabled}>템플릿 생성</button>}
            {exportOva && <button className="dropdown-item" onClick={exportOva}>OVA로 내보내기</button>}
          </div>
        )}
      </div>

    </div>
  );
};

export default VmActionButtons;
