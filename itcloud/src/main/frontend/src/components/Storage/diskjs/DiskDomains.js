import React, { useState } from 'react';
import { useAllStorageDomainFromDisk } from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import DomainTable from '../../table/DomainTable';

const DiskDomains = ({diskId}) => {
  const { 
      data: domains, 
      status: domainsStatus, 
      isLoading: isDomainsLoading, 
      isError: isDomainsError,
  } = useAllStorageDomainFromDisk(diskId, (e) => ({ ...e, }));

  const [selectedDomain, setSelectedDomain] = useState(null);
  
  return (
    <DomainTable
      columns={TableInfo.STORAGE_DOMAINS_FROM_DISK}
      domains={domains || []}
      selectedDomain={selectedDomain}
      setSelectedDomain={setSelectedDomain}
    />
  );
};

export default DiskDomains;