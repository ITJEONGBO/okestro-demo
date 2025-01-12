import React, { Suspense, useState } from 'react'; 
import { useAllDiskSnapshotFromDomain} from "../../../api/RQHook";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TablesOuter from '../../../components/table/TablesOuter';
import { formatBytesToGBToFixedZero } from '../../../utils/format';

const DeleteModal = React.lazy(() => import('../../../components/DeleteModal'));

const DomainDiskSnapshots = ({ domainId }) => {
  const { 
      data: diskSnapshots = [], isLoading: isDiskSnapshotsLoading, 
  } = useAllDiskSnapshotFromDomain(domainId, (e) => ({...e,}));
  
  const [isModalOpen, setIsModalOpen] = useState(false); 

  return (
    <>
      <div className="header-right-btns">
        <button onClick={() => setIsModalOpen(true)}>제거</button>
      </div>

      <TablesOuter
        columns={TableColumnsInfo.DISK_SNAPSHOT_FROM_STORAGE_DOMAIN}
        data={diskSnapshots.map((e) => ({
          ...e,
          actualSize: formatBytesToGBToFixedZero(e?.actualSize) + ' GB'
        }))}
      />

      {/* <Suspense fallback={<div>Loading...</div>}>
        <DeleteModal
          isOpen={isModalOpen}
          type="DiskSnapShot"
          onRequestClose={() => setIsModalOpen(false)}
          contentLabel="디스크 스냅샷"
          // data={selectedDomain}
        />
      </Suspense> */}
    </>
  );
};

export default DomainDiskSnapshots;