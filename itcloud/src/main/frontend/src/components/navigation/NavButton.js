import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useLocation } from 'react-router-dom';

const NavButton = ({ sections, handleSectionClick }) => {
  const { pathname } = useLocation(); // 현재 URL 경로 가져오기
  const [activeSection, setActiveSection] = useState('general'); // 기본값을 'general'로 설정

  useEffect(() => {
    // URL이 변경되면 해당 URL의 끝부분을 activeSection으로 설정
    const pathParts = pathname.split('/');
    const lastPart = pathParts[pathParts.length - 1];
    
    // 마지막 부분이 section에 해당하는지 확인
    if (sections.some(section => section.id === lastPart)) {
      setActiveSection(lastPart);
    } else {
      setActiveSection('general'); // 기본값으로 'general' 설정
    }
  }, [pathname, sections]);

  const handleClick = (sectionId) => {
    setActiveSection(sectionId); // 선택된 섹션을 업데이트
    handleSectionClick(sectionId); // 선택한 섹션을 부모 컴포넌트에 알림
  };

  return (
    <div className="content_header">
      <div className="content_header_left">
        {sections.map((section) => (
          <div
            key={section.id}
            className={activeSection === section.id ? 'active' : ''}
            onClick={() => handleClick(section.id)}
          >
            {section.label}
          </div>
        ))}
      </div>
    </div>
  );
};

NavButton.propTypes = {
  sections: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
    })
  ).isRequired,
  handleSectionClick: PropTypes.func.isRequired,
};

export default NavButton;
