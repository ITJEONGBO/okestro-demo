import React from 'react';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import DomainDupl from '../duplication/DomainDupl';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useAllStorageDomains } from '../../api/RQHook'
import { faDatabase } from '@fortawesome/free-solid-svg-icons'


const AllDomain = () => {
  const {
    data: storageDomains,
    refetch: refetchStorageDomains,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({...e,}));


  return(
    <div id="storage_section">
      <div>
        <HeaderButton
          titleIcon={faDatabase}
          title="스토리지 도메인"
        />
        <div className="host_btn_outer">
          <DomainDupl
            columns={TableColumnsInfo.STORAGE_DOMAINS}
            domains={storageDomains || []}            
            type={'domain'}
          />
        </div>
        {/* <Footer/> */}
      </div>    
    </div>
  );
};

export default AllDomain;
