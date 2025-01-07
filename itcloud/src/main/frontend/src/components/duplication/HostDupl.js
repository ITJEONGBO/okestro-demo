import React, { useState } from 'react';
import HostActionButtons from '../button/HostActionButtons';
import HostTable from '../table/HostTable';

const HostDupl = ({ 
  hosts, 
  columns, 
  clusterId
}) => {
  const [selectedHosts, setSelectedHosts] = useState([]); // 다중 선택된 호스트
  const selectedIds = (Array.isArray(selectedHosts) ? selectedHosts : [])
    .map((host) => host.id)
    .join(', ');

  return (
    <>
      <HostActionButtons    
        isEditDisabled={selectedHosts.length !== 1}
        isDeleteDisabled={selectedHosts.length === 0}
        status={selectedHosts[0]?.status} // 첫 번째 선택된 호스트의 상태
        selectedHost={selectedHosts.length > 0 ? selectedHosts[0] : null} // 다중 선택된 호스트 전달
        selectedHosts={selectedHosts}
        clusterId={clusterId}
      />
      <span>ID: {selectedIds || ''}</span>

      <HostTable
        columns={columns}
        hosts={hosts}
        selectedHosts={selectedHosts} // 다중 선택 상태 전달
        setSelectedHosts={(selected) => {
          if (Array.isArray(selected)) setSelectedHosts(selected); // 유효한 선택만 반영
        }}
      />
    </>
  );
};

export default HostDupl;