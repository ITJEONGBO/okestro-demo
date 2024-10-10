import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faSearch } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../table/TableColumnsInfo';
import TableOuter from '../table/TableOuter';

const Template = ({ data, columns, handleRowClick }) => {
    return (
      <>
        <div className="content_header_right">
          <div className="search_box">
            <input type="text" />
            <button><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
          </div>
  
          <div className="header_right_btns">
            <button>가져오기</button>
            <button>편집</button>
            <button>삭제</button>
            <button className="disabled">내보내기</button>
            <button className="disabled">새 가상머신</button>
          </div>        
        </div>
          
        <TableOuter
          columns={columns} 
          data={data} 
          onRowClick={handleRowClick} 
          className="template_chart"
          clickableColumnIndex={[0]} 
        />
      </>
    );
  };

export default Template;
