import React from 'react';

const VmActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
  onConsole, 
  isEditDisabled,
  status
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
        <button onClick={onDelete} disabled={isEditDisabled}>제거</button>
      )}
      {onConsole && (
        <button  disabled={isEditDisabled}>콘솔</button>
      )}
    </div>
  );
};

export default VmActionButtons;