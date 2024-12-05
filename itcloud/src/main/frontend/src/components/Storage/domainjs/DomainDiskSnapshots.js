import { useState } from 'react'; 
import { useAllDiskSnapshotFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import DeleteModal from '../../Modal/DeleteModal';

const DomainDiskSnapshots = ({ domainId }) => {
  const { 
      data: diskSnapshots, 
      status: diskSnapshotsStatus, 
      isLoading: isDiskSnapshotsLoading, 
      isError: isDiskSnapshotsError,
  } = useAllDiskSnapshotFromDomain(domainId, (e) => ({
    ...e,
    actualSize: e?.actualSize/ Math.pow(1024, 3) + " GB",
  }));
  
  const [isModalOpen, setIsModalOpen] = useState(false); 


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => setIsModalOpen(true)}>제거</button>
      </div>

      <TablesOuter
        columns={TableInfo.DISK_SNAPSHOT_FROM_STORAGE_DOMAIN}
        data={diskSnapshots}
      />

      <DeleteModal
        isOpen={isModalOpen}
        type="DiskSnapShot"
        onRequestClose={() => setIsModalOpen(false)}
        contentLabel="디스크 스냅샷"
        // data={selectedDomain}
      />
    </>
  );
};

export default DomainDiskSnapshots;