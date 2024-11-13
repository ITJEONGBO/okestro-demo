import React from 'react';

const DomainActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
  onSeparate,
  onActive,
  onMaintenance,
  disk,
  isEditDisabled 
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
      {onSeparate && (
        <button onClick={onSeparate} disabled={isEditDisabled}>분리</button>
      )}
      {onActive && (
        <button onClick={onActive} disabled={isEditDisabled}>활성</button>
      )}
      {onMaintenance && (
        <button onClick={onMaintenance} disabled={isEditDisabled}>유지보수</button>
      )}
      {disk && (
        <button >디스크</button>
      )}
    </div>
  );
};

export default DomainActionButtons;