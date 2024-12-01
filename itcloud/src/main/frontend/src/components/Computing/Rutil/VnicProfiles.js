import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllVnicProfiles } from '../../../api/RQHook';
import VnicProfileActionButtons from '../../button/VnicProfileActionButtons';

// const VnicProfileModal = React.lazy(() => import('../../Modal/VnicProfileModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const VnicProfiles = () => {
  const navigate = useNavigate();
  
  const { 
    data: vnicProfiles, 
    status: vnicProfilesStatus,
    isRefetching: isVnicProfilesRefetching,
    refetch: refetchVnicProfiles, 
    isError: isVnicProfilesError, 
    error: vnicProfilesError, 
    isLoading: isVnicProfilesLoading,
  } = useAllVnicProfiles((e) => {
    return {
        ...e,
        network: e?.networkVo.name,
        dataCenter: e?.dataCenterVo.name,
        networkFilter: e?.networkFilterVo.name
    }
  });

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedVnicProfile, setSelectedVnicProfile] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };


  return (
    <>
      <VnicProfileActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedVnicProfile?.id && toggleModal('edit', true)}
        onDelete={() => selectedVnicProfile?.id && toggleModal('delete', true)}
        isEditDisabled={!selectedVnicProfile?.id}
      />
      <span>id = {selectedVnicProfile?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.VNIC_PROFILES} 
        data={vnicProfiles || []} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedVnicProfile(row)}
        // clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        // onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {/* {(modals.create || (modals.edit && selectedVnicProfile)) && (
          <VnicProfileModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedVnicProfile?.id || null}
          />
        )} */}
        {modals.delete && selectedVnicProfile && (
          <DeleteModal
            isOpen={modals.delete}
            type={'VnicProfile'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'Vnic 프로파일'}
            data={selectedVnicProfile}
          />
        )}
      </Suspense>
    </>
  );
};

export default VnicProfiles;
