import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPencil, faWrench, faQuestionCircle, faRefresh } from '@fortawesome/free-solid-svg-icons';

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
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'green', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    } else if (status === 'MAINTENANCE') {
      return <FontAwesomeIcon icon={faWrench} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
    } else if (status === 'REBOOT') {
      return <FontAwesomeIcon icon={faRefresh} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
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
          ksm: host?.ksm ? 'o' : 'x',
          hostedEngine: 
          host?.hostedEngine && host?.ksm ? (
              <FontAwesomeIcon 
                  icon={faPencil} 
                  fixedWidth 
                  style={{ color: 'gold', fontSize: '0.3rem', transform: 'rotate(90deg)' }} 
              />
          ) : host?.hostedEngine ? (
              <FontAwesomeIcon 
                  icon={faPencil} 
                  fixedWidth 
                  style={{ color: 'grey', fontSize: '0.3rem', transform: 'rotate(90deg)' }} 
              />
          ) : (''),
          status: host?.status,
          spmStatus: host?.spmStatus === 'NONE' ? '보통' : host?.spmStatus,
          vmCnt: host?.vmSizeVo.allCnt,
          memoryUsage: host?.usageDto.memoryPercent === null ? '' : host?.usageDto.memoryPercent + '%',
          cpuUsage: host?.usageDto.cpuPercent === null ? '' : host?.usageDto.cpuPercent + '%',
          networkUsage: host?.usageDto.networkPercent === null ? '' : host?.usageDto.networkPercent + '%',
          cluster: (
            <TableRowClick type="cluster" id={host.clusterVo.id}>
              {host.clusterVo.name}
            </TableRowClick>
          ),          
          dataCenter: (
            <TableRowClick type="datacenter" id={host.dataCenterVo.id}>
              {host.dataCenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedHost(row)}
        clickableColumnIndex={[2]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default HostTable;
