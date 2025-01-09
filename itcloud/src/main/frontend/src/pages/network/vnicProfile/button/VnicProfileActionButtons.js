import React from 'react';

const VnicProfileActionButtons = ({ openModal, isEditDisabled, status, selectedHosts }) => {
  const basicActions = [
    { type: 'create', label: '생성', disabled: false },
    { type: 'edit', label: '편집', disabled: isEditDisabled },
    { type: 'delete', label: '삭제' },
  ];
  
  return (
    <div className="header-right-btns">
      {basicActions.map(({ type, label, disabled }) => (
        <button 
          key={type} 
          onClick={() => openModal(type)} 
          disabled={disabled}
        >
          {label}
        </button>
      ))}
    </div>
  );
};

export default VnicProfileActionButtons;