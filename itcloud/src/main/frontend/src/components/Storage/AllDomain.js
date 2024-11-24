import React, { Suspense, useEffect, useState } from 'react';
import './css/AllDomain.css';
import HeaderButton from '../button/HeaderButton';
import DomainDupl from '../duplication/DomainDupl';
import TableInfo from '../table/TableInfo';
import { useAllStorageDomains } from '../../api/RQHook'
import { faDatabase } from '@fortawesome/free-solid-svg-icons'


const AllDomain = () => {
  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({...e,}));


  return(
    <div id="storage_section">
      <div>
        <HeaderButton
          titleIcon={faDatabase}
          title="스토리지 도메인"
          subtitle=""
          buttons={[]}
          popupItems={[]}
        />
        <div className="host_btn_outer">
          <DomainDupl
            domains={storageDomains || []}
            columns={TableInfo.STORAGE_DOMAINS}
            type={'domain'}
          />
        </div>
      </div>    
    </div>
  );
};

export default AllDomain;
