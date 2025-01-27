import React from 'react';

const TemplateActionButtons = ({ openModal, isEditDisabled, isDeleteDisabled, status }) => {
  
  const basicActions = [
    // { type: 'create', label: '생성', disabled: false },
    { type: 'edit', label: '편집',  },
    { type: 'delete', label: '삭제', disabled: isDeleteDisabled },
  ];

  return (
    <div className="header-right-btns">
      {basicActions.map(({ type, label, disabled }) => (
        <button key={type} onClick={() => openModal(type)} disabled={disabled}>
          {label}
        </button>
      ))}
    </div>
  );
};

export default TemplateActionButtons;