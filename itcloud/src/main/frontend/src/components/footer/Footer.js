import React, { useState } from 'react';

const Footer = () => {
  const [isFooterContentVisible, setIsFooterContentVisible] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

  const toggleFooterContent = () => {
    setIsFooterContentVisible(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };

  return (
    <div className="footer_outer">
      <div className="footer">
        <button onClick={toggleFooterContent}>
          <i className="fa fa-chevron-down"></i>
        </button>
        <div>
          <div
            style={{
              color: selectedFooterTab === 'recent' ? 'black' : '#4F4F4F',
              borderBottom: selectedFooterTab === 'recent' ? '1px solid blue' : 'none',
            }} 
            onClick={() => handleFooterTabClick('recent')}
          >
            최근 작업
          </div>
          <div
            style={{
              color: selectedFooterTab === 'alerts' ? 'black' : '#4F4F4F',
              borderBottom: selectedFooterTab === 'alerts' ? '1px solid blue' : 'none',
            }}
            onClick={() => handleFooterTabClick('alerts')}
          >
            경보
          </div>
        </div>
      </div>
      {isFooterContentVisible && (
        <div className="footer_content" style={{ display: 'block' }}>
          <div className="footer_nav">
            {[...Array(8)].map((_, index) => (
              <div key={index} style={index === 7 ? { borderRight: 'none' } : {}}>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
            ))}
          </div>
          <div className="footer_img">
            <img src="img/화면 캡처 2024-04-30 164511.png" alt="스크린샷" />
            <span>항목을 찾지 못했습니다</span>
          </div>
        </div>
      )}
    </div>
  );
};

export default Footer;