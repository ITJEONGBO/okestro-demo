import React from 'react'; 
import { useAllDiskFromDomain} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import DiskDupl from '../../duplication/DiskDupl';

const DomainDisks = ({ domainId }) => {
  const { 
    data: disks, 
    status: disksStatus, 
    isLoading: isDisksLoading, 
    isError: isDisksError,
  } = useAllDiskFromDomain(domainId, (e) => ({
    ...e,
  }));

  return (
    <>
      <DiskDupl
        columns={TableColumnsInfo.DISKS_FROM_STORAGE_DOMAIN}
        disks={disks || []}
      />
    </>
  );
};

export default DomainDisks;