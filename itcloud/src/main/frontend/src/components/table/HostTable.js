import React, { useState } from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';

const HostTable = ({
  columns,
  hosts,
  setSelectedHost,
}) => {
  const navigate = useNavigate();
  
  const handleNameClick = (id) => {
    navigate(`/computing/hosts/${id}`);
  };

  const renderStatusIcon = (status) => {
    if (status === 'UP') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
    return status;
  };

  return (
    <>
      {/* 테이블 */}
      <TablesOuter
        columns={columns}
        data={hosts.map((host) => ({
          ...host,
          icon: renderStatusIcon(host.status),
          cluster: (
            <TableRowClick type="cluster" id={host.clusterVo.id}>
              {host.cluster}
            </TableRowClick>
          ),          
          dataCenter: (
            <TableRowClick type="datacenter" id={host.dataCenterVo.id}>
              {host.dataCenter}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedHost(row)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default HostTable;
