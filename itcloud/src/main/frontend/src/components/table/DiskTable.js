import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';

const DiskTable = ({
  columns,
  disks,
  setSelectedDisk,
}) => {
  const navigate = useNavigate();
  
  const handleNameClick = (id) => {
    navigate(`/storages/disks/${id}`);
  };

  const renderStatusIcon = (status) => {
    if (status === 'ACTIVE') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
    return status;
  };

  return (
    <>
      <TablesOuter
        columns={columns}
        data={disks.map((disk) => {
          const sizeInGB = disk?.virtualSize / Math.pow(1024, 3);
          return {
            ...disk,
            // icon: renderStatusIcon(disk.status),
            // storageDomain: (
            //   <TableRowClick type="domains" id={disk.storageDomainVo.id}>
            //     {disk.storageDomainVo.name}
            //   </TableRowClick>
            // ),
            // connectVm: (
            //   <TableRowClick type="vms" id={disk.connectVm.id}>
            //     {disk.connectVm.name}
            //   </TableRowClick>
            // ),
            // virtualSize: sizeInGB < 1 ? "< 1 GB" : `${sizeInGB.toFixed(0)} GB`,
          };
        })}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDisk(row)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default DiskTable;
