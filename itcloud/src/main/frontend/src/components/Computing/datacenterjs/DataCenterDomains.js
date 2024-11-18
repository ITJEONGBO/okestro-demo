import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/DataCenter.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useDomainsFromDataCenter } from '../../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import DomainActionButtons from '../../button/DomainActionButtons';

const StorageDomainModal = React.lazy(() => import('../../Modal/StorageDomainModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenterDomains = ({datacenterId}) => {
  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useDomainsFromDataCenter(datacenterId, (e) => ({
    ...e,
    domainTypeMaster: e?.domainTypeMaster ? '마스터' : '',
    diskSize: e?.diskSize/(Math.pow(1024, 3))+" GB",
    availableSize: e?.availableSize/(Math.pow(1024, 3))+" GB",
    usedSize: e?.usedSize/(Math.pow(1024, 3))+" GB",
    hostedEngine: e?.hostedEngine ? 'O' : 'X'
  }));
  
  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });  
  const [selectedDomain, setSelectedDomain] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const renderStatusIcon = (status) => {
    if (status === 'ACTIVE') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    }
    return status;
  };

  const handleNameClick = (id) => {
    navigate(`/storages/domains/${id}`);
  };

  return (
    <>
      <DomainActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedDomain?.id && toggleModal('edit', true)}
        onDelete={() => selectedDomain?.id && toggleModal('delete', true)}
        onSeparate={() => selectedDomain?.id && toggleModal('separate', true)}
        onActive={() => selectedDomain?.id && toggleModal('active', true)}
        onMaintenance={() => selectedDomain?.id && toggleModal('maintenance', true)}
        disk
        isEditDisabled={!selectedDomain?.id}
      />

      <span>id = {selectedDomain?.id || ''}</span>
      <TablesOuter
        columns={TableInfo.STORAGES_FROM_DATACENTER}
        data={storageDomains?.map((domain)=> ({
          ...domain,
          status: renderStatusIcon(domain.status),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => {setSelectedDomain(row)}}
        clickableColumnIndex={[2]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedDomain)) && (
          <StorageDomainModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            hId={selectedDomain?.id || null}
          />
        )}
        {modals.delete && selectedDomain && (
          <DeleteModal
            isOpen={modals.delete}
            type={'StorageDomain'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'스토리지 도메인'}
            data={selectedDomain}
          />
        )}
      </Suspense>
    </>
  );
};

export default DataCenterDomains;