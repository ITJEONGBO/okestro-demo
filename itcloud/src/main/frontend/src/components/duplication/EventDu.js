import React from 'react';
import PagingTableOuter from '../table/PagingTableOuter';

const EventDu = ({ columns, data, handleRowClick }) => {
  return (
    <div className="host_empty_outer">
      <PagingTableOuter
        columns={columns}
        data={data}
        onRowClick={handleRowClick}
      />
    </div>
  );
};

export default EventDu;
