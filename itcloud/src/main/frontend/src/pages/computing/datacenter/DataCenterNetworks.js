import React from 'react';
import '../datacenter/css/DataCenter.css';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useNetworksFromDataCenter } from '../../../api/RQHook';
import NetworkDupl from '../../network/network/NetworkDupl';

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