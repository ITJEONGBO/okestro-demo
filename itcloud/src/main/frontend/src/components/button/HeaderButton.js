import React, { useState } from 'react';
import './Button.css';

const HeaderButton = ({ title, subtitle, buttons, popupItems }) => {
  const [isPopupBoxVisible, setIsPopupBoxVisible] = useState(false);

  const togglePopupBox = () => {
    setIsPopupBoxVisible(!isPopupBoxVisible);
  };

  const handlePopupBoxItemClick = (item) => {
    console.log(`Clicked on ${item}`);
    setIsPopupBoxVisible(false);
  };

  return (
    <div className="section_header">
      <div className="section_header_left">
        <div>{title}</div>
        <div>{subtitle}</div>
      </div>
      <div className="section_header_right">
        <div className="article_nav">
          {buttons.map((button, index) => (
            <button key={index} id={button.id} onClick={button.onClick}>
              {button.label}
            </button>
          ))}
          {popupItems && popupItems.length > 0 && (
            <button id="popup_btn" onClick={togglePopupBox}>
              <i className="fa fa-ellipsis-v"></i>
              <div id="popup_box" style={{ display: isPopupBoxVisible ? 'block' : 'none' }}>
                {popupItems.map((item, index) => (
                  <div key={index} onClick={() => handlePopupBoxItemClick(item)}>{item}</div>
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
