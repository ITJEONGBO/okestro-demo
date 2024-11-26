import React from 'react'; 
import { useAllDiskFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
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
      {/* <DiskDupl
        columns={TableInfo.DISKS_FROM_STORAGE_DOMAIN}
        data={disks || []}
      /> */}
    </>
  );
};

export default DomainDisks;