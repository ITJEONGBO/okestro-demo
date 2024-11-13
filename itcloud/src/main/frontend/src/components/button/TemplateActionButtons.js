import React from 'react';

const TemplateActionButtons = ({ onEdit, onDelete, isEditDisabled }) => {
  return (
    <div className="header_right_btns">
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isEditDisabled}>제거</button>
      )}
    </div>
  );
};

export default TemplateActionButtons;