import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import IconButton from '../Input/IconButton';
import './css/HeaderButton.css';

const HeaderButton = ({ 
  title, 
  status, 
  buttons = [], 
  popupItems = [], 
  titleIcon 
}) => {
  const [isPopupBoxVisible, setIsPopupBoxVisible] = useState(false);

  const togglePopupBox = () => setIsPopupBoxVisible(!isPopupBoxVisible);

  const handlePopupBoxItemClick = (item) => {
    if (item.onClick) {
      item.onClick();
    }
    console.log(`Clicked on ${item.label}`);
    setIsPopupBoxVisible(false); // 팝업 닫기
  };

  // 팝업 외부 클릭 시 닫히도록 처리
  useEffect(() => {
    const handleClickOutside = (event) => {
      const popupBox = document.getElementById('popup_box');
      const popupBtn = document.getElementById('popup_btn');
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
        <div className="section_header_title">
          {titleIcon && (
            <FontAwesomeIcon 
              icon={titleIcon} 
              className="title_icon" 
              style={{ marginRight: '0.34rem' }} 
            />
          )}
          <div>{title}</div>
        </div>
      </div>
      <div className="section_header_right">
        <div className="article_nav">
          <div className="subStatus">
            <p>{status}</p>
          </div>
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
                      e.stopPropagation();
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
