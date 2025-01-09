import React from 'react';
import HeaderButton from '../../../components/button/HeaderButton';
// import Footer from '../../../components/footer/Footer';
import DomainDupl from './DomainDupl';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useAllStorageDomains } from '../../../api/RQHook'
import { faDatabase } from '@fortawesome/free-solid-svg-icons'


const AllDomain = () => {
  const {
    data: storageDomains,
  } = useAllStorageDomains((e) => ({...e,}));


  return(
    <div id="section">
      <div>
        <HeaderButton
          titleIcon={faDatabase}
          title="스토리지 도메인"
        />
        <div className="host-btn-outer">
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
