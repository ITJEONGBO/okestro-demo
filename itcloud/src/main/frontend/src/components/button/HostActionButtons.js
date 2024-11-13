import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faEllipsisV } from '@fortawesome/free-solid-svg-icons';

const HostActionButtons = ({
  onCreate,
  onEdit,
  onDelete,
  onManage,
  isEditDisabled
}) => {

  // 토글 방식으로 열고 닫기(관리)
  const [isManageBoxVisible, setIsManageBoxVisible] = useState(false);
  // 관리버튼
  const handleManageClick = () => {
    setIsManageBoxVisible(!isManageBoxVisible);
  };
  
  useEffect(() => {
    const handleClickOutside = (event) => {
      const manageBox = document.getElementById('manage_hidden_box');
      const manageBtn = document.getElementById('manage_btn');
      
      // 클릭한 요소가 각 팝업 내부의 li인지 확인
      const isLiElement = event.target.tagName === 'LI';

      // 관리, 설치, ... 버튼과 해당 요소 외부 클릭 시 팝업 닫기
      if (
        !(
          (manageBox && manageBox.contains(event.target)) ||
          (manageBtn && manageBtn.contains(event.target)) ||
          isLiElement // li 요소 클릭 시 제외
        )
      ) {
        setIsManageBoxVisible(false);
        setPopupOpen(false); // ellipsis 팝업 닫기
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

    // ...버튼
    const [popupOpen, setPopupOpen] = useState(false);
    const togglePopupMenu = () => {
      setPopupOpen(!popupOpen);
    };

  return (
    <div className="header_right_btns">
      {onCreate && 
        <button onClick={onCreate}>새로 만들기</button>
      }
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isEditDisabled}>삭제</button>
      )}
      {onManage && (
        <button onClick={onManage} disabled={isEditDisabled}>관리</button>
      )}

      <div className="manage_container">
        <button id="manage_btn" onClick={handleManageClick} className="btn">
          관리 <FontAwesomeIcon icon={faChevronDown} style={{ marginLeft: '3px' }} />
        </button>
        {isManageBoxVisible && (
          <ul id="manage_hidden_box" className="dropdown-menu">
            <li onClick={() => onManage('maintenance')}>유지보수</li>
            <li>활성</li>
            <li>기능을 새로 고침</li>
            <li style={{ borderTop: '1px solid #DDDDDD' }}>다시 설치</li>
            <li style={{ borderBottom: '1px solid #DDDDDD' }}>인증서 등록</li>
            <li>재시작</li>
            <li>중지</li>
          </ul>
        )}
      </div>

      {/* <div className="ellipsis_container">
        <button id="ellipsis_btn" onClick={togglePopupMenu} className="btn">
          <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
        </button>
        {isEllipsisVisible && (
          <ul id="ellipsis_hidden_box" className="dropdown-menu">
            <li className="disabled">글로벌 HA 유지 관리를 활성화</li>
            <li>글로벌 HA 유지 관리를 비활성화</li>
          </ul>
        )}
      </div> */}

    </div>
  );
};

export default HostActionButtons;