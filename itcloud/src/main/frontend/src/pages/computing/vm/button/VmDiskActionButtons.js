import React from 'react';

const VmDiskActionButtons = ({ openModal, isEditDisabled, isDeleteDisabled, status }) => {
  const isOk = status === "OK"

  const basicActions = [
    { type: 'create', label: '생성' },
    { type: 'connect', label: '연결' },
    { type: 'edit', label: '편집', disabled: isEditDisabled },
    { type: 'delete', label: '삭제', disabled: isDeleteDisabled || !isOk },
  ];

//   const wrapperClass = type === 'context' ? 'right-click-menu-box' : 'header-right-btns';
  return (
    <div className="header-right-btns">
      {basicActions.map(({ type, label, disabled }) => (
        <button key={type} className='right-click-menu-btn' onClick={() => openModal(type)} disabled={disabled}>
          {label}
        </button>
      ))}
    </div>
  );
};

export default VmDiskActionButtons;