import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisV, faExchange, faPencil, faArrowUp } from '@fortawesome/free-solid-svg-icons'
import IconButton from '../Input/IconButton';
import './HeaderButton.css';

const HeaderButton = ({ title, subtitle, buttons, popupItems }) => {
  const [isPopupBoxVisible, setIsPopupBoxVisible] = useState(false);
  const togglePopupBox = () => setIsPopupBoxVisible(!isPopupBoxVisible);
  const handlePopupBoxItemClick = (item) => {
    console.log(`Clicked on ${item}`);
    setIsPopupBoxVisible(false);
  };

  return (
    <div className="section_header">
      <div className="section_header_left">
        <div>{title}</div>
        <div>{subtitle}</div>
        <button><FontAwesomeIcon icon={faExchange} fixedWidth/></button>
      </div>
      <div className="section_header_right">
        <div className="article_nav">
          {buttons.map((button, index) => (
            <>
            <IconButton id={button.id}
              key={index} 
              label={button.label}
              icon={button.icon}
              onClick={button.onClick} />
            </>
          ))}
          {popupItems && popupItems.length > 0 && (
            <button id="popup_btn" onClick={togglePopupBox}>
              <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
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
