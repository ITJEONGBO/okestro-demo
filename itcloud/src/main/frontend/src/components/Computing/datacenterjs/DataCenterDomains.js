import React from 'react';
import '../css/DataCenter.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useDomainsFromDataCenter } from '../../../api/RQHook';
import DomainDupl from '../../duplication/DomainDupl';

const DataCenterDomains = ({datacenterId}) => {
  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useDomainsFromDataCenter(datacenterId, (e) => ({...e,}));
  
  return (
    <>
      <DomainDupl
        domains={storageDomains || []}
        columns={TableColumnsInfo.STORAGE_DOMAINS}
        type={'datacenter'}
        datacenterId={datacenterId}
      />
    </>
  );
};

export default DataCenterDomains;