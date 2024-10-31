import React, { useEffect, useState, useCallback  } from 'react';
import PropTypes from 'prop-types';
import { useLocation } from 'react-router-dom';

const NavButton = React.memo(({ sections, handleSectionClick }) => {
  const { pathname } = useLocation(); // 현재 URL 경로 가져오기
  const [activeSection, setActiveSection] = useState(sections[0]?.id || ''); // 기본값을 첫 번째 섹션으로 설정

  useEffect(() => {
    const pathParts = pathname.split('/');
    const lastPart = pathParts[pathParts.length - 1];
  
    if (lastPart !== activeSection && sections.some(section => section.id === lastPart)) {
      setActiveSection(lastPart);
    } else if (!sections.some(section => section.id === activeSection)) {
      setActiveSection(sections[0]?.id || '');
    }
  }, [pathname]);

  const handleClick = useCallback((sectionId) => {
    setActiveSection(sectionId);
    handleSectionClick(sectionId);
  }, [handleSectionClick]);

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
});

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
