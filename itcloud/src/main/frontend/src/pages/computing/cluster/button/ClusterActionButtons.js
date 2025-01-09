import React from 'react';

const ClusterActionButtons = ({ openModal, status }) => {
  const basicActions = [
    { type: 'create', label: '생성', disabled: false }, 
    { type: 'edit', label: '편집', disabled: status !== 'single' },
    { type: 'delete', label: '삭제', disabled: status === 'none' }, 
  ];

  return (
    <div className="header_right_btns">
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

export default ClusterActionButtons;