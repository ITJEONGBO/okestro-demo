import { useState } from 'react'; 
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {useAllHostsFromNetwork} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import { faPlay } from "@fortawesome/free-solid-svg-icons";
import NetworkHostModal from '../../Modal/NetworkHostModal';

// 애플리케이션 섹션
const NetworkHosts = ({ networkId }) => {
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useAllHostsFromNetwork(networkId, toTableItemPredicateHosts);  
  function toTableItemPredicateHosts(host) {
    const status = host?.status ?? '';
    const icon = status === 'UP' 
    ? <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem',transform: 'rotate(270deg)' }} />
    : status === 'DOWN' 
    ? <FontAwesomeIcon icon={faPlay} fixedWidth  style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)'}}/>
    : '';
    return {
      id: host?.id ?? '', 
      clusterId: host?.clusterVo?.id ?? '',  // 클러스터의 ID
      dataCenterId: host?.dataCenterVo?.id ?? '',  // 데이터 센터의 ID     
      icon: icon,
      status: status,  
      name: host?.name ?? 'Unknown',           
      clusterVo: host?.clusterVo?.name ?? 'N/A',
      dataCenterVo: host?.dataCenterVo?.name ?? 'N/A',
      networkDeviceStatus: host?.hostNicVos?.[0]?.status ?? 'Unknown', 
      networkDevice: host?.hostNicVos?.[0]?.name ?? 'N/A', 
      speed: host?.hostNicVos?.[0]?.speed
      ? Math.round(host.hostNicVos[0].speed / (1024 ** 2)) // Mbps 단위로 변환하고 반올림
      : 'N/A',
  
  rx: host?.hostNicVos?.[0]?.rxSpeed
      ? Math.round(host.hostNicVos[0].rxSpeed / (1024 ** 2)) // Mbps 단위로 변환하고 반올림
      : 'N/A',
  
  tx: host?.hostNicVos?.[0]?.txSpeed
      ? Math.round(host.hostNicVos[0].txSpeed / (1024 ** 2)) // Mbps 단위로 변환하고 반올림
      : 'N/A',
  
      totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed
      ? host.hostNicVos[0].rxTotalSpeed.toLocaleString() // 천 단위 구분 기호 추가
      : 'N/A',
  
  totalTx: host?.hostNicVos?.[0]?.txTotalSpeed
      ? host.hostNicVos[0].txTotalSpeed.toLocaleString() // 천 단위 구분 기호 추가
      : 'N/A',
    };
  }

  const [activeFilter, setActiveFilter] = useState("connected");
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태 관리


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => setIsModalOpen(true)}>호스트 네트워크 설정</button>
      </div>
      <div className="host_filter_btns">
        <button onClick={() => setActiveFilter("connected")}>연결됨</button>
        <button onClick={() => setActiveFilter("disconnected")}>연결 해제</button>
      </div>

      <TablesOuter
        columns={
          activeFilter === "connected"
            ? TableInfo.HOSTS_FROM_NETWORK
            : TableInfo.HOSTS_DISCONNECT_FROM_NETWORK
        }
        data={hosts}
        onRowClick={() => console.log("Row clicked")}
      />

      <NetworkHostModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        hosts={hosts} // 호스트 데이터 전달
      />

    </>
    );
  };
  
export default NetworkHosts;