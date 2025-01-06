import React from 'react';
import '../css/Computing.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllNetworks } from '../../../api/RQHook';
import NetworkDupl from '../../duplication/NetworkDupl';

const Networks = () => {
  const { 
    data: networks = [], 
    refetch: refetchNetworks, 
    error: networksError, 
    isLoading: isNetworksLoading,
  } = useAllNetworks((e) => ({...e,}));

  return (
    <>
      <NetworkDupl
        columns={TableColumnsInfo.NETWORKS}
        networks={networks || []}
      />
    </> 
  );
};

export default Networks;
