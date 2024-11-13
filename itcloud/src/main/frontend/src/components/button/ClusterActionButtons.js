import React from 'react';

const ClusterActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
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
    </div>
  );
};

export default ClusterActionButtons;