import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllStorageDomains } from '../../../api/RQHook';

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
    diskSize: e?.diskSize/(Math.pow(1024, 3))+" GB",
    availableSize: e?.availableSize/(Math.pow(1024, 3))+" GB",
    usedSize: e?.usedSize/(Math.pow(1024, 3))+" GB",
  }));

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedStorageDomain, setSelectedStorageDomain] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
    navigate(`/storages/domains/${id}`);
  };


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedStorageDomain?.id && toggleModal('edit', true)} disabled={!selectedStorageDomain?.id}>편집</button>
        <button onClick={() => selectedStorageDomain?.id && toggleModal('delete', true)} disabled={!selectedStorageDomain?.id}>제거</button>
      </div>
      <span>id = {selectedStorageDomain?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.STORAGE_DOMAINS}
        data={storageDomains}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedStorageDomain(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedStorageDomain)) && (
          <StorageDomainModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            dcId={selectedStorageDomain?.id || null}
          />
        )}
        {modals.delete && selectedStorageDomain && (
          <DeleteModal
            isOpen={modals.delete}
            type='StorageDomain'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'스토리지 도메인'}
            data={selectedStorageDomain}
          />
        )}
       </Suspense>
    </>
  );
};

export default StorageDomains;