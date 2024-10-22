import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {useAllHostsFromNetwork} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { useState } from 'react'; 
import { faPlay } from "@fortawesome/free-solid-svg-icons";

// 애플리케이션 섹션
const NetworkHost = ({ network }) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);

    const [activeFilter, setActiveFilter] = useState('connected'); 
    const handleFilterClick = (filter) => {
      setActiveFilter(filter);
    };
    const { 
        data: hosts, 
        status: hostsStatus, 
        isLoading: isHostsLoading, 
        isError: isHostsError 
      } = useAllHostsFromNetwork(network?.id, toTableItemPredicateHosts);  
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
          speed: host?.hostNicVos?.[0]?.speed ?? 'N/A', 
          rx: host?.hostNicVos?.[0]?.rxSpeed ?? 'N/A', 
          tx: host?.hostNicVos?.[0]?.txSpeed ?? 'N/A', 
          totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed ?? 'N/A', 
          totalTx: host?.hostNicVos?.[0]?.txTotalSpeed ?? 'N/A', 
        };
      }
    return (
        <>
        <div className="header_right_btns">
                <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
        </div>
        <div className="host_filter_btns">
          <button
            className={activeFilter === 'connected' ? 'active' : ''}
            onClick={() => handleFilterClick('connected')}
          >
            연결됨
          </button>
          <button
            className={activeFilter === 'disconnected' ? 'active' : ''}
            onClick={() => handleFilterClick('disconnected')}
          >
            연결 해제
          </button>
        </div>
        {activeFilter === 'connected' && (
          <TableOuter
            columns={TableColumnsInfo.HOSTS}
            data={hosts}
            onRowClick={(row, column, colIndex) => {
              if (colIndex === 1) {
                navigate(`/computing/hosts/${row.id}`);  // 1번 컬럼 클릭 시 이동할 경로
              } else if (colIndex === 2) {
                navigate(`/computing/clusters/${row.clusterId}`);  // 2번 컬럼 클릭 시 이동할 경로
              } else if (colIndex === 3) {
                navigate(`/computing/datacenters/${row.dataCenterId}`);  // 3번 컬럼 클릭 시 이동할 경로
              }
            }}
            clickableColumnIndex={[1,2,3]} 
            onContextMenuItems={() => [
              <div key="호스트 네트워크 설정" onClick={() => console.log()}>호스트 네트워크 설정</div>,
            ]}
          />
        )}

        {activeFilter === 'disconnected' && (
          <TableOuter
            columns={TableColumnsInfo.HOSTS_DISCONNECTION}
            data={hosts}
            onRowClick={() => console.log('Row clicked')}
          />
        )}
   </>
    );
  };
  
  export default NetworkHost;