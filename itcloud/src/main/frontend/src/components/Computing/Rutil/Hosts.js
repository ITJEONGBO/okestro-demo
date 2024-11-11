import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import { faArrowUp, faArrowDown, faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';

const HostModal = React.lazy(() => import('../../Modal/HostModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

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
      vmCnt: e?.vmSizeVo.allCnt,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedHost, setSelectedHost] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
    navigate(`/computing/hosts/${id}`);
  };

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedHost?.id && toggleModal('edit', true)} disabled={!selectedHost?.id}>편집</button>
        <button onClick={() => selectedHost?.id && toggleModal('delete', true)} disabled={!selectedHost?.id}>제거</button>
      </div>
      <span>id = {selectedHost?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.HOSTS}
        data={hosts}
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
      </Suspense>
    </>
  );
};

export default Hosts;