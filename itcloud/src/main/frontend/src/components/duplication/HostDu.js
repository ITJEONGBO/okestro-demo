import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faSearch } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../table/TableColumnsInfo';
import TableOuter from '../table/TableOuter';

const HostDu = ({ data, columns, handleRowClick, openPopup }) => {
    
  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => openPopup('host_new')}>새로 만들기</button>
        <button onClick={() => openPopup('host_edit')}>편집</button>
        <button>삭제</button>
        <button>관리</button>
        <button>설치</button>
        <button>호스트 네트워크 복사</button>
      </div>
      
      <TableOuter
        columns={columns} 
        data={data} 
        onRowClick={handleRowClick} 
        className="host_table"
        clickableColumnIndex={[2]} 
      />

      
    </>
    
  );
};

export default HostDu;
