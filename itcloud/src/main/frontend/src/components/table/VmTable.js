import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';

const VmTable = ({
  columns,
  vms,
  setSelectedVm, // 선택된 VM을 저장하는 함수
}) => {
  const navigate = useNavigate();

  const handleNameClick = (id) => {
    navigate(`/computing/vms/${id}`);
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
        data={vms.map((vm) => ({
          ...vm,
          icon: renderStatusIcon(vm.status),
          host: (
            <TableRowClick type="host" id={vm.hostVo.id}>
              {vm.host}
            </TableRowClick>
          ),
          cluster: (
            <TableRowClick type="cluster" id={vm.clusterVo.id}>
              {vm.cluster}
            </TableRowClick>
          ),
          dataCenter: (
            <TableRowClick type="datacenter" id={vm.dataCenterVo.id}>
              {vm.dataCenter}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => {
          // 선택된 VM 정보와 데이터센터 ID 포함
          setSelectedVm({
            ...row,
            dataCenterId: row?.dataCenterVo?.id, // 데이터센터 ID를 추가
          });
        }}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default VmTable;
