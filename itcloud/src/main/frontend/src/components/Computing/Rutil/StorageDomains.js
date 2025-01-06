import React from 'react';
import '../css/Computing.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllStorageDomains } from '../../../api/RQHook';
import DomainDupl from '../../duplication/DomainDupl';

const StorageDomains = () => {
  const {
    data: storageDomains = [],
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({...e,}));

  return (
    <>    
      <DomainDupl
        domains={storageDomains || []}
        columns={TableColumnsInfo.STORAGE_DOMAINS}
        type={'rutil'}
      />
    </>
  );
};

export default StorageDomains;
