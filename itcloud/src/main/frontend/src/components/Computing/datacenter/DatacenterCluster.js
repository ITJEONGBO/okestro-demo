import {useClustersFromDataCenter} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import { useState } from 'react'; 


const DatacenterCluster = ({ dataCenter }) => {
    const navigate = useNavigate();

    const [isModalOpen, setIsModalOpen] = useState({
        edit: false,
        permission: false,
      });
      const handleOpenModal = (type) => {
        setIsModalOpen((prev) => ({ ...prev, [type]: true }));
      };
      const handleCloseModal = (type) => {
        setIsModalOpen((prev) => ({ ...prev, [type]: false }));
      };

      const { 
        data: clusters, 
        status: clustersStatus, 
        isLoading: isClustersLoading, 
        isError: isClustersError 
      } = useClustersFromDataCenter(dataCenter?.id, toTableItemPredicateClusters);
      function toTableItemPredicateClusters(cluster) {
        return {
          id: cluster?.id ?? '', 
          name: cluster?.name ?? '없음',
          description: cluster?.description ?? '없음',
          version: cluster?.version ?? '없음',
        };
      }

    return (
        <>
        <div className="header_right_btns">
          <button onClick={() => handleOpenModal('cluster_new')}>새로 만들기</button>
          <button onClick={() => handleOpenModal('cluster_new')}>편집</button>
          <button onClick={() => handleOpenModal('delete')}>삭제</button>
        </div>
        <TableOuter
          columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER}
          data={clusters}
          onRowClick={(row, column, colIndex) => {
            if (colIndex === 0) {
              navigate(`/computing/clusters/${row.id}`); 
            }
          }}
          clickableColumnIndex={[0]} 
          onContextMenuItems={() => [
            <div key="새로 만들기" onClick={() => console.log()}>새로 만들기</div>,
            <div key="편집" onClick={() => console.log()}>편집</div>,
            <div key="삭제" onClick={() => console.log()}>삭제</div>,
          ]}
        />
    </>
    );
  };
  
  export default DatacenterCluster;