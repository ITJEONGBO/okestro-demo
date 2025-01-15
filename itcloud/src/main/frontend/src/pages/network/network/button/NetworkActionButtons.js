import React from 'react';
import { useNavigate } from 'react-router-dom';

const NetworkActionButtons = ({ openModal, isEditDisabled, isDeleteDisabled, status, selectedNetworks }) => {
  const navigate = useNavigate();
  const basicActions = [
    { type: 'create', label: '생성', disabled: false },
    { type: 'edit', label: '편집', disabled: isEditDisabled },
    { type: 'delete', label: '삭제', disabled: isDeleteDisabled },
    { type: 'import', label: '가져오기' },
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
      <button onClick={() => navigate('/vnicProfiles')} >vnicProfile</button>
    </div>
  );
};

export default NetworkActionButtons;