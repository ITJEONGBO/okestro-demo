import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/DataCenter.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useHostsFromDataCenter } from '../../../api/RQHook';
import renderStatusIcon from '../renderStatusIcon';
import HostActionButtons from '../../button/HostActionButtons';

const HostModal = React.lazy(() => import('../../Modal/HostModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenterHosts = ({datacenterId}) => {
  const {
    data: hosts,
    status: hostsStatus,
    isLoading: isHostsLoading,
    isError: isHostsError,
  } = useHostsFromDataCenter(datacenterId, (e) => ({ 
    ...e, 
    status: e?.status,
    cluster: e?.clusterVo.name,
    dataCenter: e?.dataCenterVo.name,
    vmCnt: e?.vmSizeVo.allCnt,
    memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
    cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
    networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
  }));
  
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
      <HostActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedHost?.id && toggleModal('edit', true)}
        onDelete={() => selectedHost?.id && toggleModal('delete', true)}
        onManage={() => selectedHost?.id && toggleModal('manage', true)}
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
        onRowClick={(row) => {setSelectedHost(row)}}
        clickableColumnIndex={[1]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
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

export default DataCenterHosts;