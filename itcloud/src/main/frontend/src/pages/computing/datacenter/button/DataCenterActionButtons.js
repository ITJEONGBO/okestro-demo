import React from 'react';

const DataCenterActionButtons = ({ openModal, status }) => {
  const basicActions = [
    { type: 'create', label: '생성' },
    { type: 'edit', label: '편집' },
    { type: 'delete', label: '삭제' },
  ];

  return (
    <div className="header_right_btns">
      {basicActions.map(({ type, label }) => (
        <button key={type} onClick={() => openModal(type)} >
          {label}
        </button>
      ))}
    </div>
  );
};

export default DataCenterActionButtons;