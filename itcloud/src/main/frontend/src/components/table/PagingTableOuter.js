import React from 'react';
import PagingTable from './PagingTable';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './Table.css';

const PagingTableOuter = ({ columns, data, onRowClick, shouldHighlight1stCol = false, clickableColumnIndex = [0], itemsPerPage = 10, showSearchBox = true }) => {
  return (
    <div className="section_table_outer">

      <PagingTable 
        columns={columns} 
        data={data}
        onRowClick={onRowClick} 
        clickableColumnIndex={clickableColumnIndex} // 클릭 가능 컬럼 전달
        shouldHighlight1stCol={shouldHighlight1stCol} // 첫 번째 컬럼 강조 여부 (필요 시)
        itemsPerPage={itemsPerPage} // 페이지당 표시할 항목 수
        showSearchBox={showSearchBox} // 검색 박스 표시 여부 제어
      />
    </div>
  );
};

export default PagingTableOuter;
