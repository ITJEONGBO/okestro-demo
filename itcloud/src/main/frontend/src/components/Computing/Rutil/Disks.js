import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllDisks } from '../../../api/RQHook';

// const DiskModal = React.lazy(() => import('../../Modal/DiskModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const Disks = () => {
  const navigate = useNavigate();
  
  const { 
    data: disks, 
    status: disksStatus,
    isRefetching: isDisksRefetching,
    refetch: refetchDisks, 
    isError: isDisksError, 
    error: disksError, 
    isLoading: isDisksLoading,
  } = useAllDisks((e) => {
    return {
      ...e,
      name: e?.alias, 
      storageDomain: e?.storageDomainVo.name,
      virtualSize: e?.virtualSize/(Math.pow(1024, 3))+" GB",
      connectVm: e?.connectVm.name
    }
  });

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedDisk, setSelectedDisk] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
      navigate(`/storages/disks/${id}`);
  };


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedDisk?.id && toggleModal('edit', true)} disabled={!selectedDisk?.id}>편집</button>
        <button onClick={() => selectedDisk?.id && toggleModal('delete', true)} disabled={!selectedDisk?.id}>제거</button>
      </div>
      <span>id = {selectedDisk?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.DISKS} 
        data={disks} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDisk(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {/* {(modals.create || (modals.edit && selectedDisk)) && (
          <DiskModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            dId={selectedDisk?.id || null}
          />
        )} */}
        {modals.delete && selectedDisk && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Disk'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'디스크'}
            data={selectedDisk}
          />
        )}
      </Suspense>
    </>
  );
};

export default Disks;
