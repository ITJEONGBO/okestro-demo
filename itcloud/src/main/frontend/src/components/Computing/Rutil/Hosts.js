import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowUp, faArrowDown, faMinus, faPlus, faPlay } from '@fortawesome/free-solid-svg-icons';
import HostActionButtons from '../../button/HostActionButtons';

const HostModal = React.lazy(() => import('../../Modal/HostModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));
const HostActionModal = React.lazy(() => import('../../Modal/HostActionModal'))

const Hosts = () => {
  const {
      data: hosts,
      status: hostsStatus,
      isRefetching: isHostsRefetching,
      refetch: refetchHosts,
      isError: isHostsError,
      error: hostsError,
      isLoading: isHostsLoading
  } = useAllHosts((e) => {
    return {
      ...e,
      status: e?.status,
      cluster: e?.clusterVo.name,
      dataCenter: e?.dataCenterVo.name,
      spmStatus: e?.spmStatus === 'SPM' ? 'SPM' : '',
      vmCnt: e?.vmSizeVo.allCnt,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false, deactivate: false });
  const [selectedHost, setSelectedHost] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const renderStatusIcon = (status) => {
    if (status === 'UP') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
    return status;
  };

  const handleNameClick = (id) => {
    navigate(`/computing/hosts/${id}`);
  };

  return (
    <>
      <HostActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedHost?.id && toggleModal('edit', true)}
        onDelete={() => selectedHost?.id && toggleModal('delete', true)}
        // onManage={() => selectedHost?.id && toggleModal('manage', true)}
        onDeactivate={() => selectedHost?.id && toggleModal('deactivate', true)}
        isEditDisabled={!selectedHost?.id}
      />
      <span>id = {selectedHost?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.HOSTS}
        data={hosts?.map((host)=> ({
          ...host,
          status: renderStatusIcon(host.status),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedHost(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedHost)) && (
          <HostModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            hId={selectedHost?.id || null}
          />
        )}
        {modals.delete && selectedHost && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Host'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'호스트'}
            data={selectedHost}
          />
        )}
        {modals.deactivate && selectedHost && (
          <HostActionModal
            isOpen={modals.deactivate}
            action={'deactivate'}
            onRequestClose={() => toggleModal('deactivate', false)}
            contentLabel={'유지보수'}
            data={selectedHost}
          />
        )}
        

      </Suspense>
    </>
  );
};

export default Hosts;