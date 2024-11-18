import React from 'react';
import PagingTableOuter from '../table/PagingTableOuter';

const EventDu = ({ 
  columns, 
  data, 
  handleRowClick,
  showSearchBox = true  
}) => {
  
  return (
    <div className="host_empty_outer">
      <PagingTableOuter
        columns={columns}
        data={data}
        onRowClick={handleRowClick}
        showSearchBox={showSearchBox} // 검색 박스 표시 여부 제어
      />
    </div>
  );
};

export default EventDu;
