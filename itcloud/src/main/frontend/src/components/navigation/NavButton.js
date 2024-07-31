import React from 'react';
import PropTypes from 'prop-types';

const NavButton = ({ sections, activeSection, handleSectionClick }) => {
  return (
    <div className="content_header">
      <div className="content_header_left">
        {sections.map((section) => (
          <div
            key={section.id}
            className={activeSection === section.id ? 'active' : ''}
            onClick={() => handleSectionClick(section.id)}
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
  activeSection: PropTypes.string.isRequired,
  handleSectionClick: PropTypes.func.isRequired,
};

export default NavButton;
