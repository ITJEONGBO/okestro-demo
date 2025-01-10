import React, { Suspense, useState } from 'react'; 
import TablesOuter from "../../../components/table/TablesOuter";
import TableRowClick from '../../../components/table/TableRowClick';
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import { formatBytesToMB, renderHostStatusIcon, renderUpDownStatusIcon } from '../../../utils/format';
import { 
  useConnectedHostsFromNetwork, 
  useDisconnectedHostsFromNetwork 
} from "../../../api/RQHook";

const NetworkHostModal = React.lazy(() => import('./modal/NetworkHostModal'));

const NetworkHosts = ({ networkId }) => {
  const { 
    data: connectedHosts = [], isLoading: isConnectedLoading, 
  } = useConnectedHostsFromNetwork(networkId, (e) => ({...e})); 
  const { 
    data: disconnectedHosts = [], isLoading: isDisconnectedLoading, 
  } = useDisconnectedHostsFromNetwork(networkId, (e) => ({...e})); 

  const [activeFilter, setActiveFilter] = useState("connected");
  const [selectedHost, setSelectedHost] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      <NetworkHostModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        // hostId={hosts} // 호스트 데이터 전달
      />
    </Suspense>
  );

  const buttonClass = (filter) => `filter_button ${activeFilter === filter ? "active" : ""}`;

  const transformHostData = (hosts) => {
    return hosts.map((host) => ({
      ...host,
      icon: renderHostStatusIcon(host?.status),
      host: <TableRowClick type="host" id={host?.id}>{host?.name}</TableRowClick>,
      cluster: <TableRowClick type="cluster" id={host?.clusterVo?.id}>{host?.clusterVo?.name}</TableRowClick>,
      dataCenter: <TableRowClick type="datacenter" id={host?.dataCenterVo?.id}>{host?.dataCenterVo?.name}</TableRowClick>,
      networkDeviceStatus: renderUpDownStatusIcon(host?.hostNicVos?.[0]?.status),
      networkDevice: host?.hostNicVos?.[0]?.name,
      speed: host?.hostNicVos?.[0]?.speed,
      rx: host?.hostNicVos?.[0]?.rxSpeed ? Math.round(formatBytesToMB(host.hostNicVos[0].rxSpeed)): "",
      tx: host?.hostNicVos?.[0]?.txSpeed ? Math.round(formatBytesToMB(host.hostNicVos[0].txSpeed)): "",
      totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed ? host.hostNicVos[0].rxTotalSpeed.toLocaleString(): "",
      totalTx: host?.hostNicVos?.[0]?.txTotalSpeed ? host.hostNicVos[0].txTotalSpeed.toLocaleString(): "",
    }));
  };

  return (
    <>
      <div className="header-right-btns">
        <button onClick={() => setIsModalOpen(true)}>호스트 네트워크 설정</button>
      </div>

      <div className="host-filter-btns">
        <button className={buttonClass("connected")} onClick={() => setActiveFilter("connected")}>
          연결됨
        </button>
        <button className={buttonClass("disconnected")} onClick={() => setActiveFilter("disconnected")}>
          연결 해제
        </button>
      </div>

      <span>id = {selectedHost?.id || ''}</span>

      {/* {isHostsLoading ? (
        <p>로딩 중...</p>
      ) : isHostsError ? (
        <p>호스트 데이터를 불러오는 데 실패했습니다.</p>
      ) : ( */}
      <TablesOuter
        columns={
          activeFilter === "connected"
            ? TableColumnsInfo.HOSTS_FROM_NETWORK
            : TableColumnsInfo.HOSTS_DISCONNECT_FROM_NETWORK
        }
        data={
          activeFilter === "connected"
            ? transformHostData(connectedHosts)
            : transformHostData(disconnectedHosts)
        }
        onRowClick={(row) => setSelectedHost(row)}
      />

      {/* 호스트 네트워크 모달창 */}
      { renderModals() }

    </>
    );
  };
  
export default NetworkHosts;