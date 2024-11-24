import React from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllStorageDomains } from '../../../api/RQHook';
import DomainDupl from '../../duplication/DomainDupl';

const StorageDomains = () => {
  const {
    data: storageDomains,
    status: storageDomainsStatus,
    isRefetching: isStorageDomainsRefetching,
    refetch: refetchStorageDomains,
    isError: isStorageDomainsError,
    error: storageDomainsError,
    isLoading: isStorageDomainsLoading
  } = useAllStorageDomains((e) => ({...e,}));

  return (
    <>    
      <DomainDupl
        domains={storageDomains || []}
        columns={TableInfo.STORAGE_DOMAINS}
        type={'domain'}
      />
    </>
  );
};

export default StorageDomains;


      {/* 도메인 가져오기와 생성은 같은 창, 관리가 편집 */}
{/*       
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
          icon: renderStatusIcon(domain.status),
          status: domain.status,
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDomain(row)}
        clickableColumnIndex={[2]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

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
       </Suspense> */}
