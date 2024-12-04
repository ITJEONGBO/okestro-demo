import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {useAllHostsFromNetwork} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import { useNavigate} from 'react-router-dom';
import { useState,useEffect } from 'react'; 
import Modal from 'react-modal';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { 
  faArrowsAltH, faBan, faCaretDown, faCheck, faCircle, faDesktop, faExclamationTriangle, faFan, faNetworkWired, faPencilAlt, faPlay, faTimes 
} from "@fortawesome/free-solid-svg-icons";

// 애플리케이션 섹션
const NetworkHosts = ({ networkId }) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);

    const [activeFilter, setActiveFilter] = useState('connected'); 
    const handleFilterClick = (filter) => {
      setActiveFilter(filter);
    };

  // 팝업(연필)추가모달
  const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리
  const [isSecondModalOpen, setIsSecondModalOpen] = useState(false);
  useEffect(() => {
    if (isSecondModalOpen) {
      handleTabModalClick('ipv4');
    }
  }, [isSecondModalOpen]);
  const [selectedModalTab, setSelectedModalTab] = useState('ipv4');
  const handleTabModalClick = (tab) => {
    setSelectedModalTab(tab);
  };
  const closeSecondModal = () => {
    setIsSecondModalOpen(false);
    setSelectedModalTab('ipv4');
  };

  // 드레그
  const [items, setItems] = useState([
    { id: 'item-1', name: 'ddd 1' },
    { id: 'item-2', name: 'ddd 2' },
    { id: 'item-3', name: 'ddd 3' },
  ]);

  const handleDragEnd = (result) => {
    if (!result.destination) return;
    const reorderedItems = Array.from(items);
    const [removed] = reorderedItems.splice(result.source.index, 1);
    reorderedItems.splice(result.destination.index, 0, removed);
    setItems(reorderedItems);
  };


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
          <TablesOuter
            columns={TableInfo.HOSTS}
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
          <TablesOuter
            columns={TableInfo.HOSTS_DISCONNECTION}
            data={hosts}
            onRowClick={() => console.log('Row clicked')}
          />
        )}

   </>
    );
  };
  
export default NetworkHosts;