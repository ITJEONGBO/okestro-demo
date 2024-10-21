import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useAllClustersFromNetwork} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { useState } from 'react'; 
import { faChevronLeft } from "@fortawesome/free-solid-svg-icons";

// 애플리케이션 섹션
const NetworkCluster = ({ network }) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);

  const { 
    data: clusters, 
    status: clustersStatus, 
    isLoading: isClustersLoading, 
    isError: isClustersError 
  } = useAllClustersFromNetwork(network?.id, toTableItemPredicateClusters);
  function toTableItemPredicateClusters(cluster) {
    return {
      id: cluster?.id ?? '없음',
      name: cluster?.name ?? '없음',
      description: cluster?.description ?? '없음',
      version: cluster?.version ?? '없음',
      connectedNetwork: cluster?.connected ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkStatus: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,
      requiredNetwork: cluster?.requiredNetwork ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkRole: cluster?.networkRole ?? '',
    };
  }
    return (
        <>
        <div className="header_right_btns">
            <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
        </div>
      
        <TableOuter
          columns={TableColumnsInfo.CLUSTERS}
          data={clusters}
          onRowClick={(row, column, colIndex) => {
            const clickableCols = [0];
            if (clickableCols.includes(colIndex)) {
                if (colIndex === 0) {
                    navigate(`/computing/clusters/${row.id}`);
                }
            } else {
              console.log('Selected Cluster ID:', row.id);
            }
        }}
          clickableColumnIndex={[0]}
          onContextMenuItems={() => ['1', '2', '3']}
        />


   </>
    );
  };
  
  export default NetworkCluster;