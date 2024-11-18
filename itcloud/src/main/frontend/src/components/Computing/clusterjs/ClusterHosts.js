import React, { useState } from 'react';
import TableInfo from '../../table/TableInfo';
import { useHostFromCluster } from "../../../api/RQHook";
import HostActionButtons from '../../button/HostActionButtons';
import HostTable from '../../table/HostTable';
import HostModals from '../../Modal/HostModals';

const ClusterHosts = ({ cId }) => {
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useHostFromCluster(cId, (e) => ({ 
    ...e, 
    status: e?.status,
    cluster: e?.clusterVo.name,
    dataCenter: e?.dataCenterVo.name,
    vmCnt: e?.vmSizeVo.allCnt,
    memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
    cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
    networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
  }));
        
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedHost, setSelectedHost] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <HostActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedHost?.id && handleActionClick('edit')}
        onDelete={() => selectedHost?.id && handleActionClick('delete')}
        onDeactivate={() => selectedHost?.id && handleActionClick('deactivate')}
        onActivate={() => selectedHost?.id && handleActionClick('activate')}
        onRestart={() => selectedHost?.id && handleActionClick('restart')}
        onStop={() => selectedHost?.id && handleActionClick('stop')}
        onReInstall={() => selectedHost?.id && handleActionClick('reinstall')}
        onRegister={() => selectedHost?.id && handleActionClick('register')}
        onHaOn={() => selectedHost?.id && handleActionClick('haon')}
        onHaOff={() => selectedHost?.id && handleActionClick('haoff')}
        isEditDisabled={!selectedHost?.id}
        status={selectedHost?.status} // 추가: 현재 호스트 상태 전달
      />
      <span>id = {selectedHost?.id || ''}</span>

      <HostTable
        columns={TableInfo.HOSTS}
        hosts={hosts || []}
        selectedHost={selectedHost}
        setSelectedHost={setSelectedHost}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <HostModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedHost={selectedHost}
      />
    </>
    );
  };
  
  export default ClusterHosts;