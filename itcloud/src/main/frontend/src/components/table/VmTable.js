import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { renderVmStatusIcon } from '../util/format';

const VmTable = ({
  columns,
  vms,
  setSelectedVms, // 선택된 VM을 저장하는 함수
}) => {
  const navigate = useNavigate();

  const handleNameClick = (id) => {
    navigate(`/computing/vms/${id}`);
  };
  const handleRowSelection = (selectedRows) => {
    setSelectedVms(selectedRows); // 선택된 데이터 전달
  };
  return (
    <>
      {/* 테이블 */}
      <TablesOuter
        columns={columns}
        data={vms.map((vm) => ({
          ...vm,
          icon: renderVmStatusIcon(vm.status),
          host: vm.hostVo?.id ? (
            <TableRowClick type="host" id={vm.hostVo.id}>
              {vm.hostVo.name}
            </TableRowClick>
          ): '',
          cluster: vm.clusterVo?.id ?(
            <TableRowClick type="cluster" id={vm.clusterVo.id}>
              {vm.clusterVo.name}
            </TableRowClick>
          ): '',
          dataCenter: vm.dataCenterVo?.id ?(
            <TableRowClick type="datacenter" id={vm.dataCenterVo.id}>
              {vm.dataCenterVo.name}
            </TableRowClick>
          ): '',
          ipv4: vm.ipv4 + ' ' + vm.ipv6,
          memoryUsage: vm.usageDto?.memoryPercent === null || vm.usageDto?.memoryPercent === undefined 
          ? '' : `${vm.usageDto.memoryPercent}%`,
          cpuUsage: vm.usageDto?.cpuPercent === null || vm.usageDto?.cpuPercent === undefined 
          ? '' : `${vm.usageDto.cpuPercent}%`,
        networkUsage: vm.usageDto?.networkPercent === null || vm.usageDto?.networkPercent === undefined 
          ? '' : `${vm.usageDto.networkPercent}%`,
        }))}
        shouldHighlight1stCol={true}
        // onRowClick={(row) => {
        //   console.log('Selected VM row:', row); // 선택된 행의 데이터를 확인
        //   // 선택된 VM 정보와 데이터센터 ID 포함
        //   setSelectedVms({
        //     ...row,
        //     dataCenterId: row?.dataCenterVo?.id, // 데이터센터 ID를 추가
        //   });
        // }}
        onRowClick={handleRowSelection}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default VmTable;
