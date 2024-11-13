import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css'
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllVMs } from '../../../api/RQHook';
import renderStatusIcon from '../renderStatusIcon';
import VmActionButtons from '../../button/VmActionButtons';

const VmModal = React.lazy(() => import('../../Modal/VmModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const Vms = () => {
  const navigate = useNavigate();
  
  const { 
    data: vms, 
    status: vmsStatus,
    isRefetching: isVmsRefetching,
    refetch: refetchVms, 
    isError: isVmsError, 
    error: vmsError, 
    isLoading: isVmsLoading,
  } = useAllVMs((e) => {
    return {
      ...e,
      status: e?.status,
      host: e?.hostVo?.name, 
      cluster: e?.clusterVo?.name,        
      dataCenter: e?.dataCenterVo?.name,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  const [modals, setModals] = useState({ create: false, edit: false, delete: false, console: false });
  const [selectedVm, setSelectedVm] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));

    if (type === 'console' && isOpen && selectedVm?.id && selectedVm?.status !== 'DOWN') {
      navigate(`/computing/vms/${selectedVm.id}/console`);
      // window.open(navigate(`/computing/vms/${selectedVm.id}/console`), '_blank', 'noopener,noreferrer');
    }
  };

  const handleNameClick = (id) => {
      navigate(`/computing/vms/${id}`);
  };

  return (
    <>
      <VmActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedVm?.id && toggleModal('edit', true)}
        onDelete={() => selectedVm?.id && toggleModal('delete', true)}
        onConsole={() => selectedVm?.id && toggleModal('console', true)} // disabled={!selectedVm?.id || selectedVm?.status === 'DOWN'}
        isEditDisabled={!selectedVm?.id}
        // status={}
      />
      {/* <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedVm?.id && toggleModal('edit', true)} disabled={!selectedVm?.id}>편집</button>
        <button onClick={() => selectedVm?.id && toggleModal('delete', true)} disabled={!selectedVm?.id}>제거</button>
        <button onClick={() => selectedVm?.id && toggleModal('console', true)} disabled={!selectedVm?.id || selectedVm?.status === 'DOWN'}>콘솔</button>
      </div> */}
      <span>id = {selectedVm?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.VMS} 
        data={vms?.map((vm) => ({
          ...vm,
          status: renderStatusIcon(vm.status),
        }))} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedVm(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
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
            type={'Vm'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'가상머신'}
            data={selectedVm}
          />
        )}

        {/* {modals.console && selectedVm && (
          <VncViewerPage data={selectedVm}/>
        )} */}

      </Suspense>
    </>
  );
};

export default Vms;
