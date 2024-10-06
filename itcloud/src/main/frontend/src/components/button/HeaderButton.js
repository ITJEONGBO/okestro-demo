import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisV, faExchange } from '@fortawesome/free-solid-svg-icons';
import IconButton from '../Input/IconButton';
import './HeaderButton.css';

const HeaderButton = ({ title, subtitle, buttons, popupItems }) => {
  const [isPopupBoxVisible, setIsPopupBoxVisible] = useState(false);

  const togglePopupBox = () => setIsPopupBoxVisible(!isPopupBoxVisible);

  const handlePopupBoxItemClick = (item) => {
    if (item.onClick) {
      item.onClick(); // 클릭 시 각 항목의 onClick을 호출
    }
    console.log(`Clicked on ${item.label}`);
    // 팝업 항목을 클릭해도 닫히지 않도록 아래 주석을 유지하거나 제거 가능
    // setIsPopupBoxVisible(false);
  };

  // 팝업 외부 클릭 시 닫히도록 처리
  useEffect(() => {
    const handleClickOutside = (event) => {
      const popupBox = document.getElementById('popup_box');
      const popupBtn = document.getElementById('popup_btn');

      // 팝업이나 팝업 버튼이 아닌 곳을 클릭했을 때 팝업 닫기
      if (popupBox && !popupBox.contains(event.target) && popupBtn && !popupBtn.contains(event.target)) {
        setIsPopupBoxVisible(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div className="section_header">
      <div className="section_header_left">
        <div>{title}</div>
        <div>{subtitle}</div>
      </div>
      <div className="section_header_right">
        <div className="article_nav">
          {buttons.map((button, index) => (
            <IconButton
              id={button.id}
              key={index}
              label={button.label}
              icon={button.icon}
              onClick={button.onClick}
            />
          ))}
          {popupItems && popupItems.length > 0 && (
            <button id="popup_btn" onClick={togglePopupBox}>
              <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
              <div
                id="popup_box"
                style={{ display: isPopupBoxVisible ? 'block' : 'none' }}
              >
                {popupItems.map((item, index) => (
                  <div
                    key={index}
                    className="popup_item"
                    onClick={(e) => {
                      e.stopPropagation(); // div를 클릭했을 때는 닫히지 않도록 이벤트 전파 차단
                      handlePopupBoxItemClick(item);
                    }}
                  >
                    {item.label}
                  </div>
                ))}
              </div>
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default HeaderButton;
