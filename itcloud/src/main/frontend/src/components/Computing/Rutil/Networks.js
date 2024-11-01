import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllNetworks } from '../../../api/RQHook';

const NetworkModal = React.lazy(() => import('../../Modal/NetworkModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const Networks = () => {
  const navigate = useNavigate();
  
  const { 
    data: networks, 
    status: networksStatus,
    isRefetching: isNetworksRefetching,
    refetch: refetchNetworks, 
    isError: isNetworksError, 
    error: networksError, 
    isLoading: isNetworksLoading,
  } = useAllNetworks((e) => {
    return {
        ...e,
        datacenter: e?.datacenterVo.name
    }
  });

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedNetwork, setSelectedNetwork] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
      navigate(`/networks/${id}`);
  };


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedNetwork?.id && toggleModal('edit', true)} disabled={!selectedNetwork?.id}>편집</button>
        <button onClick={() => selectedNetwork?.id && toggleModal('delete', true)} disabled={!selectedNetwork?.id}>제거</button>
      </div>
      <span>id = {selectedNetwork?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.NETWORKS} 
        data={networks} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedNetwork(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedNetwork)) && (
          <NetworkModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedNetwork?.id || null}
          />
        )}
        {modals.delete && selectedNetwork && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Network'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'네트워크'}
            data={selectedNetwork}
          />
        )}
      </Suspense>
    </>
  );
};

export default Networks;
