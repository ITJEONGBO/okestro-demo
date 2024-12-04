import React from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllStorageDomains } from '../../../api/RQHook';
import DomainDupl from '../../duplication/DomainDupl';

const StorageDomains = () => {
  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({...e,}));

  return (
    <>    
      <DomainDupl
        domains={storageDomains || []}
        columns={TableInfo.STORAGE_DOMAINS}
        type={'rutil'}
      />
    </>
  );
};

export default StorageDomains;
