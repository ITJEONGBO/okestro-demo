import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useAllClustersFromNetwork} from "../../../api/RQHook";
import { useState } from 'react'; 
import { faChevronLeft, faPlay } from "@fortawesome/free-solid-svg-icons";
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import NetworkClusterModal from "../../Modal/NetworkClusterModal";

// 애플리케이션 섹션
const NetworkClusters = ({ networkId }) => {
  const [isModalOpen, setIsModalOpen] = useState(false); 

  const renderStatusIcon = (status) => {
    if (status === 'OPERATIONAL') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
  };
  
  const { 
    data: clusters, 
    status: clustersStatus, 
    isLoading: isClustersLoading, 
    isError: isClustersError 
  } = useAllClustersFromNetwork(networkId, (e) => ({
    ...e,
    name: e?.name,
    // TODO 연결된 네트워크 값 구해야함
    connect: e?.name === null ? <input type="checkbox" checked disabled/> : <input type="checkbox" disabled/>,
    status: renderStatusIcon(e?.networkVo?.status),
    required: e?.networkVo?.required === true ? <input type="checkbox" checked disabled/> : <input type="checkbox" disabled/>,
  }));

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => setIsModalOpen(true)}>네트워크 관리</button>
      </div>
    
      <TablesOuter
        columns={TableInfo.CLUSTERS_FRON_NETWORK}
        data={clusters}
      />

      <NetworkClusterModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        clusters={clusters}
      />
   </>
  );
};

export default NetworkClusters;