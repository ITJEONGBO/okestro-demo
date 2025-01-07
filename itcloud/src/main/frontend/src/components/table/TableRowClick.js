import React from 'react';
import { useNavigate } from 'react-router-dom';

const TableRowClick = ({ 
  type, 
  id, 
  children, 
  style 
}) => {
  const navigate = useNavigate();

  const handleClick = (e) => {
    e.stopPropagation(); // 테이블 row 클릭과 충돌 방지

    const paths = {
      datacenter: `/computing/datacenters/${id}/clusters`,
      cluster: `/computing/clusters/${id}`,
      host: `/computing/hosts/${id}`,
      vms: `/computing/vms/${id}`,
      domains: `/storages/domains/${id}`,
      disks: `/storages/disks/${id}`,
      network: `/networks/${id}`,
      templates: `/computing/templates/${id}`
    };

    const path = paths[type];
    if (path) 
        navigate(path);
    else 
      console.warn(`Unknown navigation type: ${type}`);
  };

  return (
    <div style={{textAlign: 'left'}}>
      <span
        className='row-click'
        onClick={handleClick}
      >
        {children}
      </span>
    </div>
  );
};

export default TableRowClick;
