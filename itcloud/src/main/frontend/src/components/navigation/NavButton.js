import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';

const NavButton = ({ sections, handleSectionClick }) => {
  const [activeSection, setActiveSection] = useState('');

  useEffect(() => {
    // 로컬 스토리지에서 저장된 섹션이 있는지 확인
    const savedSection = localStorage.getItem('activeSection');
    
    if (savedSection && sections.some(section => section.id === savedSection)) {
      // 저장된 섹션이 있고, 해당 섹션이 존재하면 그 섹션을 활성화
      setActiveSection(savedSection);
    } else if (sections.length > 0) {
      // 저장된 섹션이 없고 섹션 배열이 비어있지 않으면 첫 번째 섹션을 기본값으로 설정
      const firstSectionId = sections[0]?.id;
      setActiveSection(firstSectionId);
      localStorage.setItem('activeSection', firstSectionId); // 첫 번째 섹션을 로컬 스토리지에 저장
      handleSectionClick(firstSectionId);  // 첫 번째 섹션을 선택했음을 부모 컴포넌트에 알림
    }
  }, [sections]);

  const handleClick = (sectionId) => {
    setActiveSection(sectionId);
    localStorage.setItem('activeSection', sectionId); // 클릭한 섹션을 로컬 스토리지에 저장
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
