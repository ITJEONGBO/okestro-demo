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

  const renderStatusIcon = (connect, status) => {
    if (connect && status === 'OPERATIONAL') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if(connect && status === 'NON_OPERATIONAL'){
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    } else if(!connect){
      return ''
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
    connect: e?.connected ? <input type="checkbox" checked disabled/> : <input type="checkbox" disabled/>,
    status: renderStatusIcon(e?.connected, e?.networkVo?.status),
    required: e?.networkVo?.required === true ? <input type="checkbox" checked disabled/> : <input type="checkbox" disabled/>,
    networkRole: [
      e?.networkVo?.usage?.management === true ? '관리' : null,
      e?.networkVo?.usage?.display === true ? '출력' : null,
      e?.networkVo?.usage?.migration === true ? '마이그레이션' : null,
      e?.networkVo?.usage?.gluster === true ? '글러스터' : null,
      e?.networkVo?.usage?.defaultRoute === true ? '기본라우팅' : null,
    ].filter(Boolean).join('/'),
  }));

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => setIsModalOpen(true)}>네트워크 관리</button>
      </div>
    
      <TablesOuter
        columns={TableInfo.CLUSTERS_FRON_NETWORK}
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