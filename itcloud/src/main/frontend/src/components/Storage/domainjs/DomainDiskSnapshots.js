import { useState } from 'react'; 
import { useAllDiskSnapshotFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";

const DomainDiskSnapshots = ({ domainId }) => {
  const { 
      data: diskSnapshots, 
      status: diskSnapshotsStatus, 
      isLoading: isDiskSnapshotsLoading, 
      isError: isDiskSnapshotsError,
  } = useAllDiskSnapshotFromDomain(domainId, (e) => ({...e,}));

  return (
      <>
        
      </>
  );
};

export default DomainDiskSnapshots;