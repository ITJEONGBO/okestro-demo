import React from 'react';
import TableColumnsInfo from '../../components/table/TableColumnsInfo';
import { useAllStorageDomains } from '../../api/RQHook';
import DomainDupl from '../../pages/storage/domain/DomainDupl';

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
