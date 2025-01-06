import { Suspense, useState } from 'react';
import { useAllVmsFromNetwork } from "../../../api/RQHook";
import TablesOuter from "../../table/TablesOuter";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableRowClick from "../../table/TableRowClick";
import { formatBytesToMB, renderUpDownStatusIcon, renderVmStatusIcon } from "../../util/format";
import VmDeleteModal from '../../Modal/VmDeleteModal';


const NetworkVms = ({ networkId }) => {
  const { 
    data: vms = [],  // 기본값 설정
    status: vmsStatus,
    isLoading: isVmsLoading,
    isError: isVmsError
  } = useAllVmsFromNetwork(networkId, (vm) => ({ 
    ...vm,
    icon: renderVmStatusIcon(vm?.status),
    name: (
      <TableRowClick type="vms" id={vm.id}>
        {vm.name}
      </TableRowClick>
    ),
    cluster: (
      <TableRowClick type="cluster" id={vm.clusterVo.id}>
        {vm.clusterVo.name}
      </TableRowClick>
    ),
    vnicStatus: renderUpDownStatusIcon(vm?.nicVos[0]?.status),
    vnic: vm?.nicVos?.[0]?.name || '알 수 없음',
    vnicRx: vm?.nicVos?.[0]?.rxSpeed ? Math.round(formatBytesToMB(vm?.nicVos[0].rxSpeed)): '',
    vnicTx: vm?.nicVos?.[0]?.txSpeed ? Math.round(formatBytesToMB(vm?.nicVos[0].txSpeed)): '',
    totalRx: vm?.nicVos?.[0]?.rxTotalSpeed ? vm?.nicVos?.[0]?.rxTotalSpeed.toLocaleString() : '',
    totalTx: vm?.nicVos?.[0]?.txTotalSpeed ? vm?.nicVos?.[0]?.txTotalSpeed.toLocaleString() : '',
  }));

  const [activeFilter, setActiveFilter] = useState("running");
  const [selectedVms, setSelectedVms] = useState([]);
  const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);

  const isDeleteButtonDisabled = !selectedVms.length || selectedVms.some(vm => vm?.nicVos[0]?.status !== 'DOWN');
  const selectedIds = (Array.isArray(selectedVms) ? selectedVms : []).map((network) => network.id).join(', ');
  const toggleDeleteModal = (isOpen) => setDeleteModalOpen(isOpen);

  // 필터링된 VM 데이터 계산
  const filteredVms = activeFilter === 'running' 
    ? vms.filter(vm => vm.status !== 'DOWN')
    : vms.filter(vm => vm.status === 'DOWN');

    console.log('All VMs:', vms);
    console.log('Filtered VMs:', filteredVms);

  const buttonClass = (filter) =>
    `filter_button ${activeFilter === filter ? 'active' : ''}`;

  const modalData = selectedVms.map(vm => ({
    id: vm.id,
    name: vm.vnic || vm.name || '알 수 없음', // vnic 이름을 우선으로 사용
  }));

  return (
    <>
      <div className="header_right_btns">
        <button 
          onClick={() => toggleDeleteModal(true)} 
          disabled={isDeleteButtonDisabled} // 선택된 VM이 없을 때 비활성화
        >
          제거
        </button>
      </div>
      
      <div className="host_filter_btns">
        <button className={buttonClass("running")} onClick={() => setActiveFilter("running")}>
          실행중
        </button>
        <button className={buttonClass("stopped")} onClick={() => setActiveFilter("stopped")}>
          정지중
        </button>
      </div>

      <span>id = {selectedIds || '선택된 항목이 없습니다.'}</span>

      <TablesOuter
        columns={
          activeFilter === "running" 
          ? TableColumnsInfo.VMS_NIC 
          : TableColumnsInfo.VMS_STOP
        }
        data={filteredVms}
        onRowClick={(rows) => {
          console.log('Selected Rows:', rows); // 선택된 데이터 확인
          setSelectedVms(Array.isArray(rows) ? rows : []);
        }}
      />

      <Suspense>
        {isDeleteModalOpen && (
          <VmDeleteModal
            isOpen={isDeleteModalOpen}
            onRequestClose={() => toggleDeleteModal(false)}
            data={modalData}
          />
        )}
      </Suspense>
    </>
  );
};

export default NetworkVms;
