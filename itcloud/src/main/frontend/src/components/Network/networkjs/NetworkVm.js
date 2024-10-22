import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {useAllVmsFromNetwork} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { useState } from 'react'; 
import { faChevronDown,faPlay } from "@fortawesome/free-solid-svg-icons";

const NetworkVm = ({network}) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);
    const [activeVmFilter, setActiveVmFilter] = useState('running');
    const handleVmFilterClick = (filter) => {
      setActiveVmFilter(filter);
    };
    
  //가상머신
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useAllVmsFromNetwork(network?.id, toTableItemPredicateVms);
  function toTableItemPredicateVms(vm) {
    const status = vm?.status ?? '';
    const icon = status === 'UP' 
    ? <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem',transform: 'rotate(270deg)' }} />
    : status === 'DOWN' 
    ? <FontAwesomeIcon icon={faPlay} fixedWidth  style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)'}}/>
    : '';
    return {
      id: vm?.id ?? '없음',  // 가상 머신 ID
      name: vm?.name ?? '없음',  // 가상 머신 이름
      cluster: vm?.clusterVo?.name ?? '없음',  // 클러스터 이름
      ipAddress: vm?.ipAddress ?? '없음',  // IP 주소
      fqdn:  vm?.fqdn ?? '',
      icon: icon,
      status: status, 
      vnic: vm?.vnic ?? '',  // vNIC 이름
      vnicRx: vm?.vnicRx ?? '',  // vNIC 수신 속도
      vnicTx: vm?.vnicTx ?? '',  // vNIC 송신 속도
      totalRx: vm?.totalRx ?? '',  // 총 수신 데이터
      totalTx: vm?.totalTx ?? '',  // 총 송신 데이터
      description: vm?.description ?? '없음'  // 가상 머신 설명
    };
  }
    return (
      <>
      <div className="header_right_btns">
          <button onClick={() => openPopup('delete')}>제거</button>
      </div>
      <div className="host_filter_btns">
        <button
          className={activeVmFilter === 'running' ? 'active' : ''}
          onClick={() => handleVmFilterClick('running')}
        >
          실행중
        </button>
        <button
          className={activeVmFilter === 'stopped' ? 'active' : ''}
          onClick={() => handleVmFilterClick('stopped')}
        >
          정지중
        </button>
      </div>
      {activeVmFilter === 'running' && (
          <TableOuter
            columns={TableColumnsInfo.VMS_NIC}
            data={vms}
            onRowClick={() => console.log('Row clicked')}
            clickableColumnIndex={[1]} 
            onContextMenuItems={() => [
              <div key="제거" onClick={() => console.log()}>제거</div>,
            ]}
          />
        )}

        {/* 정지중 테이블 */}
        {activeVmFilter === 'stopped' && (
          <TableOuter
            columns={TableColumnsInfo.VMS_STOP}
            data={vms}
            onRowClick={() => console.log('Row clicked')}
          />
        )}
      </>
    );
  };
  
  export default NetworkVm;