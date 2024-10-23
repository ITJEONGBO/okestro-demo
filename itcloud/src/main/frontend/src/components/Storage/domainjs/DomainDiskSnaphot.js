import { useAllDiskSnapshotFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";
import { useState } from 'react'; 


const DomainDiskSnaphot = ({ domain }) => {
    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);

    const openModal = (popupType) => setActivePopup(popupType);
    const closeModal = () => setActivePopup(null);



    const { 
        data: diskSnapshots, 
        status: diskSnapshotsStatus, 
        isLoading: isDiskSnapshotsLoading, 
        isError: isDiskSnapshotsError,
      } = useAllDiskSnapshotFromDomain(domain?.id, toTableItemPredicateDiskSnapshots);
      function toTableItemPredicateDiskSnapshots(diskSnapshot) {
        return {
          id: diskSnapshot?.id ?? '',
          actualSize: diskSnapshot?.actualSize ? `${diskSnapshot.actualSize} GiB` : '알 수 없음',
          creationDate: diskSnapshot?.creationDate ?? '알 수 없음',
          snapshotCreationDate: diskSnapshot?.snapshotCreationDate ?? '알 수 없음',
          alias: diskSnapshot?.alias ?? '없음',
          description: diskSnapshot?.description ?? '없음',
          target: diskSnapshot?.target ?? '없음',
          status: diskSnapshot?.status ?? '알 수 없음',
          diskSnapshotId: diskSnapshot?.diskSnapshotId ?? '없음',
        };
    }

    return (
        <>
            <div className="header_right_btns">
                <button onClick={() => openModal('delete')}>제거</button>
            </div>
            
            <TableOuter 
              columns={TableInfo.DISK_SNAPSHOT_FROM_STORAGE_DOMAIN}
              data={diskSnapshots}
              onRowClick={() => console.log('Row clicked')}
              onContextMenuItems={() => [
                <div key="제거" onClick={() => console.log()}>제거</div>
              ]}
            />
        </>
    );
  };
  
  export default DomainDiskSnaphot;