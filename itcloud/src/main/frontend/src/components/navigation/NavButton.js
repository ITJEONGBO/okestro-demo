import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useLocation } from 'react-router-dom';

const NavButton = ({ sections, handleSectionClick }) => {
  const { pathname } = useLocation(); // URL 경로 추적
  const [prevPath, setPrevPath] = useState(pathname); // 이전 경로 저장
  const [activeSection, setActiveSection] = useState(() => {
    return localStorage.getItem('activeSection') || 'general'; // 기본값 'general' 설정
  });

  useEffect(() => {
    if (pathname !== prevPath) {
      // URL 경로가 변경된 경우에만 'general'로 초기화
      setActiveSection('general');
      setPrevPath(pathname); // 이전 경로를 현재 경로로 업데이트
    }
    // 경로가 동일한 경우(즉, 새로고침)에는 activeSection을 유지
  }, [pathname, prevPath]);

  useEffect(() => {
    // activeSection을 로컬스토리지에 저장
    localStorage.setItem('activeSection', activeSection);
  }, [activeSection]);

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
