import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllStorageDomains } from '../../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import DomainActionButtons from '../../button/DomainActionButtons';

const StorageDomainModal = React.lazy(() => import('../../Modal/StorageDomainModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const StorageDomains = () => {
  const navigate = useNavigate();

  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({
    ...e,
    // domainTypeMaster: e?domainTypeMaster == true ? "마스터":"",
    hostedEngine: e?.hostedEngine ? 'O' : 'X',
    diskSize: e?.diskSize/(Math.pow(1024, 3))+" GB",
    availableSize: e?.availableSize/(Math.pow(1024, 3))+" GB",
    usedSize: e?.usedSize/(Math.pow(1024, 3))+" GB",
  }));

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
      {/* 도메인 가져오기와 생성은 같은 창, 관리가 편집 */}
      <DomainActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedDomain?.id && toggleModal('edit', true)}
        onDelete={() => selectedDomain?.id && toggleModal('delete', true)}
        onSeparate={() => selectedDomain?.id && toggleModal('separate', true)}
        onActive={() => selectedDomain?.id && toggleModal('active', true)}
        onMaintenance={() => selectedDomain?.id && toggleModal('maintenance', true)}
        isEditDisabled={!selectedDomain?.id}
      />
      <span>id = {selectedDomain?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.STORAGE_DOMAINS}
        data={storageDomains?.map((domain)=> ({
          ...domain,
          status: renderStatusIcon(domain.status),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDomain(row)}
        clickableColumnIndex={[2]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedDomain)) && (
          <StorageDomainModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            domainId={selectedDomain?.id || null}
          />
        )}
        {modals.delete && selectedDomain && (
          <DeleteModal
            isOpen={modals.delete}
            type='StorageDomain'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'스토리지 도메인'}
            data={selectedDomain}
          />
        )}
       </Suspense>
    </>
  );
};

export default StorageDomains;