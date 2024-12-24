import React from 'react';

const NetworkActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
  onImport, 
  isEditDisabled,
  isDeleteDisabled
}) => {
  return (
    <div className="header_right_btns">
      {onCreate && 
        <button onClick={onCreate}>새로 만들기</button>
      }
      {onEdit && (
        <button onClick={onEdit} disabled={isEditDisabled}>편집</button>
      )}
      {onDelete && (
        <button onClick={onDelete} disabled={isDeleteDisabled}>제거</button>
      )}
      {onImport && (
        <button onClick={onImport}>가져오기</button>
      )}
    </div>
  );
};

export default NetworkActionButtons;