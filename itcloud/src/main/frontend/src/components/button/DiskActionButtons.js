import React from 'react';

const DiskActionButtons = ({ 
  onCreate, 
  onEdit, 
  onDelete, 
  onMove,
  onCopy,
  onUpload,
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
      {onMove && (
        <button onClick={onMove} disabled={isEditDisabled}>이동</button>
      )}
      {onCopy && (
        <button onClick={onCopy} disabled={isEditDisabled}>복사</button>
      )}
      {onUpload && (
        <button onClick={onUpload} disabled={isEditDisabled}>업로드</button>
      )}
    </div>
  );
};

export default DiskActionButtons;