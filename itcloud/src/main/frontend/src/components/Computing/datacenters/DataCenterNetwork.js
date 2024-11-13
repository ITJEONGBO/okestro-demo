import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/DataCenter.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useNetworksFromDataCenter } from '../../../api/RQHook';
import NetworkActionButtons from '../../button/NetworkActionButtons';

const NetworkModal = React.lazy(() => import('../../Modal/NetworkModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenterNetworks = ({datacenterId}) => {
  const {
    data: networks,
    status: networksStatus,
    isLoading: isNetworksLoading,
    isError: isNetworksError,
  } = useNetworksFromDataCenter(datacenterId, (e) => ({ 
    ...e 
  }));
  
  const navigate = useNavigate();
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
      <NetworkActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedNetwork?.id && toggleModal('edit', true)}
        onDelete={() => selectedNetwork?.id && toggleModal('delete', true)}
        isEditDisabled={!selectedNetwork?.id}
      />

      <span>id = {selectedNetwork?.id || ''}</span>
      <TablesOuter
        columns={TableInfo.NETWORK_FROM_DATACENTER}
        data={networks}
        shouldHighlight1stCol={true}
        onRowClick={(row) => {setSelectedNetwork(row)}}
        clickableColumnIndex={[0]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedNetwork)) && (
          <NetworkModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedNetwork?.id || null}
            datacenterId={datacenterId}
          />
        )}
        {modals.delete && selectedNetwork && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Cluster'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'클러스터'}
            data={selectedNetwork}
          />
        )}
      </Suspense>
    </>
  );
};

export default DataCenterNetworks;