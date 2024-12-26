import React from 'react';

const TemplateActionButtons = ({ 
  onEdit, 
  onDelete, 
  isEditDisabled,
  isDeleteDisabled
}) => {
  return (
    <div className="header_right_btns">
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isDeleteDisabled}>제거</button>
      )}
    </div>
  );
};

export default TemplateActionButtons;