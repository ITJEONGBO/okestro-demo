import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import VmActionButtons from './button/VmActionButtons';
import TablesOuter from '../../../components/table/TablesOuter';
import TableRowClick from '../../../components/table/TableRowClick';
import { renderVmStatusIcon } from '../../../utils/format';

const VmModal = React.lazy(() => import('./modal/VmModal'));
const VmDeleteModal = React.lazy(() => import('./modal/VmDeleteModal'));
const VmActionModal = React.lazy(() => import('./modal/VmActionModal'));

const VmDupl = ({ vms = [], columns =[], actionType, status }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedVms, setSelectedVms] = useState([]);
  const selectedIds = (Array.isArray(selectedVms) ? selectedVms : []).map(v => v.id).join(', ');
  
  const handleNameClick = (id) => navigate(`/computing/vms/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      <VmModal
        isOpen={activeModal === 'create'}
        onClose={closeModal}
      />
      <VmModal
        editMode
        isOpen={activeModal === 'edit'}
        vmId={selectedVms[0]?.id || null}
        onClose={closeModal}
      />
      <VmDeleteModal
        isOpen={activeModal === 'delete'}
        data={selectedVms}
        onClose={closeModal}
      />
      <VmActionModal
        isOpen={['start', 'pause', 'reboot', 'reset', 'stop', 'powerOff'].includes(activeModal)}
        action={activeModal} // `type` 전달
        data={selectedVms}
        onClose={closeModal}
      />
      {/* 스냅샷 생성, 마이그레이션 등 모달 넣어야함 */}
    </Suspense>
  );

  return (
    <>
      <div onClick={(e) => e.stopPropagation()}> 
      <VmActionButtons
        openModal={openModal}
        isEditDisabled={selectedVms.length !== 1}
        isDeleteDisabled={selectedVms.length === 0}
        status={selectedVms[0]?.status}
        selectedVms={selectedVms}
      />
      <span>id = {selectedIds || ''}</span>  
      
      <TablesOuter
        columns={columns}
        data={vms.map((vm) => ({
          ...vm,
          icon: renderVmStatusIcon(vm.status),
          host: <TableRowClick type="host" id={vm?.hostVo?.id}> {vm?.hostVo?.name} </TableRowClick>,
          cluster: <TableRowClick type="cluster" id={vm?.clusterVo?.id}>{vm?.clusterVo?.name}</TableRowClick>,
          dataCenter: <TableRowClick type="datacenter" id={vm?.dataCenterVo?.id}>{vm?.dataCenterVo?.name} </TableRowClick>,
          ipv4: vm.ipv4 + ' ' + vm.ipv6,
          memoryUsage: vm.usageDto?.memoryPercent === null || vm.usageDto?.memoryPercent === undefined ? '' : `${vm.usageDto.memoryPercent}%`,
          cpuUsage: vm.usageDto?.cpuPercent === null || vm.usageDto?.cpuPercent === undefined ? '' : `${vm.usageDto.cpuPercent}%`,
          networkUsage: vm.usageDto?.networkPercent === null || vm.usageDto?.networkPercent === undefined ? '' : `${vm.usageDto.networkPercent}%`,
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedVms(selectedRows)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}        
        multiSelect={true}
      />
      
      {/* vm 모달 */}
      { renderModals() }
      </div>
    </>
  );
};

export default VmDupl;
