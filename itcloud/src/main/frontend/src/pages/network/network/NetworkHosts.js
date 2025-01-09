import { useState } from 'react'; 
import {useAllHostsFromNetwork} from "../../../api/RQHook";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TablesOuter from "../../../components/table/TablesOuter";
import NetworkHostModal from './modal/NetworkHostModal';
import TableRowClick from '../../../components/table/TableRowClick';
import { formatBytesToMB, renderHostStatusIcon, renderUpDownStatusIcon } from '../../../utils/format';


const NetworkHosts = ({ networkId }) => {
  const { 
    data: hosts = [], 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useAllHostsFromNetwork(networkId, (host) => ({
    ...host,
    icon: renderHostStatusIcon(host?.status),
    host: (
      <TableRowClick type="host" id={host?.id}>
        {host?.name}
      </TableRowClick>
    ),
    cluster: (
      <TableRowClick type="cluster" id={host?.clusterVo?.id}>
        {host?.clusterVo?.name}
      </TableRowClick>
    ),
    dataCenter: (
      <TableRowClick type="datacenter" id={host?.dataCenterVo?.id}>
        {host?.dataCenterVo?.name}
      </TableRowClick>
    ),
    networkDeviceStatus: renderUpDownStatusIcon(host?.hostNicVos?.[0]?.status), 
    networkDevice: host?.hostNicVos?.[0]?.name, 
    speed: host?.hostNicVos?.[0]?.speed,
    rx: host?.hostNicVos?.[0]?.rxSpeed ? Math.round(formatBytesToMB(host.hostNicVos[0].rxSpeed)): '',
    tx: host?.hostNicVos?.[0]?.txSpeed ? Math.round(formatBytesToMB(host.hostNicVos[0].txSpeed)): '',
    totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed ? host.hostNicVos[0].rxTotalSpeed.toLocaleString() : '',
    totalTx: host?.hostNicVos?.[0]?.txTotalSpeed ? host.hostNicVos[0].txTotalSpeed.toLocaleString() : '',
  })); 

  const [activeFilter, setActiveFilter] = useState("connected");
  const [selectedHost, setSelectedHost] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const buttonClass = (filter) =>
    `filter_button ${activeFilter === filter ? "active" : ""}`;

  // 필터링된 데이터 계산
  const filteredHosts =
    activeFilter === "connected"
      ? hosts.filter((host) => host.status === "connected")
      : hosts.filter((host) => host.status === "disconnected");

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
        data={filteredHosts}
        onRowClick={(row) => setSelectedHost(row)}
      />
      {/* )} */}

      <NetworkHostModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        hostId={hosts} // 호스트 데이터 전달
      />

    </>
    );
  };
  
export default NetworkHosts;