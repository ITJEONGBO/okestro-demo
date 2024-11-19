import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import {useVMFromCluster} from "../../../api/RQHook";
import VmDu from "../../duplication/VmDu"
import renderStatusIcon from '../renderStatusIcon';

const VmModal = React.lazy(() => import('../../Modal/VmModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const ClusterVms = ({ cId }) => {
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useVMFromCluster(cId, (e) => ({ 
    ...e,
    status: e?.status,
    host: e?.hostVo?.name, 
    cluster: e?.clusterVo?.name,        
    dataCenter: e?.dataCenterVo?.name,
    memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
    cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
    networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
  }));
  
  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });  
  const [selectedVm, setSelectedVm] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
    navigate(`/computing/vms/${id}`);
  };

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedVm?.id && toggleModal('edit', true)} disabled={!selectedVm?.id}>편집</button>
        <button onClick={() => selectedVm?.id && toggleModal('delete', true)} disabled={!selectedVm?.id}>제거</button>
      </div>

      <span>id = {selectedVm?.id || ''}</span>
      <TablesOuter
        columns={TableInfo.VMS}
        data={vms?.map((vm) => ({
          ...vm,
          status: renderStatusIcon(vm.status),
        }))} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => {setSelectedVm(row)}}
        clickableColumnIndex={[1]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedVm)) && (
          <VmModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            vmId={selectedVm?.id || null}
          />
        )}
        {modals.delete && selectedVm && (
          <DeleteModal
            isOpen={modals.delete}
            type={'VM'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'가상머신'}
            data={selectedVm}
          />
        )}
      </Suspense>
    </>
  );
};

export default ClusterVms;