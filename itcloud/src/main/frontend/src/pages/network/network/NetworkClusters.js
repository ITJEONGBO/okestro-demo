import { useState } from 'react'; 
import { useAllClustersFromNetwork} from "../../../api/RQHook";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TablesOuter from "../../../components/table/TablesOuter";
import NetworkClusterModal from "./modal/NetworkClusterModal";
import {renderStatusClusterIcon} from '../../../utils/format';

// 애플리케이션 섹션
const NetworkClusters = ({ networkId }) => {
  const [isModalOpen, setIsModalOpen] = useState(false); 
  
  const { 
    data: clusters = [], 
    status: clustersStatus, 
    isLoading: isClustersLoading, 
    isError: isClustersError 
  } = useAllClustersFromNetwork(networkId);

  return (
    <>
      <div className="header-right-btns">
        <button onClick={() => setIsModalOpen(true)}>네트워크 관리</button>
      </div>
    
      <TablesOuter
        columns={TableColumnsInfo.CLUSTERS_FRON_NETWORK}
        data={clusters || []}
        
      />

      <NetworkClusterModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        clusters={clusters}
        networkId={networkId}
      />
   </>
  );
};

export default NetworkClusters;