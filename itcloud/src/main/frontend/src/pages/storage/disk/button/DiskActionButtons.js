import React from 'react';

const DiskActionButtons = ({ openModal, isEditDisabled, isDeleteDisabled, status}) => {
  const isOk = status === "OK"

  const basicActions = [
    { type: 'create', label: '생성', disabled: false },
    { type: 'edit', label: '편집', disabled: isEditDisabled },
    { type: 'delete', label: '삭제', disabled: isDeleteDisabled || !isOk },
    { type: 'edit', label: '이동', disabled: isEditDisabled },
    { type: 'edit', label: '복사', disabled: isEditDisabled },
    { type: 'edit', label: '업로드' },
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

export default DiskActionButtons;