import React from 'react';
import '../css/DataCenter.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useNetworksFromDataCenter } from '../../../api/RQHook';
import NetworkDupl from '../../duplication/NetworkDupl';

const DataCenterNetworks = ({datacenterId}) => {
  const {
    data: networks,
    status: networksStatus,
    isLoading: isNetworksLoading,
    isError: isNetworksError,
  } = useNetworksFromDataCenter(datacenterId, (e) => ({ 
    ...e 
  }));

  return (
    <>
      <NetworkDupl
        columns={TableColumnsInfo.NETWORK_FROM_DATACENTER}
        networks={networks || []}
      />
    </>
  );
};

export default DataCenterNetworks;