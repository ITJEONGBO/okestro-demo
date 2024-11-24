import React from 'react';
import '../css/DataCenter.css';
import TableInfo from '../../table/TableInfo';
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
        columns={TableInfo.STORAGE_DOMAINS}
        type={'datacenter'}
      />
    </>
  );
};

export default DataCenterDomains;