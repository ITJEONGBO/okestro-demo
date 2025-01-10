import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NetworkActionButtons from './button/NetworkActionButtons';
import TablesOuter from '../../../components/table/TablesOuter';
import TableRowClick from '../../../components/table/TableRowClick';

const NetworkModal = React.lazy(() => import('./modal/NetworkModal'));
const NetworkImportModal = React.lazy(() => import('./modal/NetworkImportModal'));
const NetworkDeleteModal = React.lazy(() => import('./modal/NetworkDeleteModal'));
  
const NetworkDupl = ({ networks = [], columns = [] }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedNetworks, setSelectedNetworks] = useState([]);
  const selectedIds = (Array.isArray(selectedNetworks) ? selectedNetworks : []).map(network => network.id).join(', ');

  const handleNameClick = (id) => navigate(`/networks/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <NetworkModal
          onClose={closeModal}
        />
      )}
      {activeModal === 'edit' && (
        <NetworkModal
          editMode
          networkId={selectedNetworks[0]?.id}
          onClose={closeModal}
        />
      )}
      {activeModal === 'import' && (
        <NetworkImportModal
          networkId={selectedNetworks[0]?.id}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <NetworkDeleteModal
          data={selectedNetworks}
          onClose={closeModal}
        />
      )}
    </Suspense>
  );

  return (
    <>
      <NetworkActionButtons
        openModal={openModal}
        isEditDisabled={selectedNetworks.length !== 1}
      />
      <span>ID: {selectedIds}</span>

      <TablesOuter
        columns={columns}
        data={networks.map((network) => ({
          ...network,
          // name: 
          //   <TableRowClick type="network" id={network?.id}>
          //     {network?.name}
          //   </TableRowClick>,
          vlan: network?.vlan === 0 ? '-' : network?.vlan,
          mtu: network?.mtu === 0 ? '기본값(1500)' : network?.mtu,
          datacenter: (
            <TableRowClick type="datacenter" id={network?.datacenterVo.id}>
              {network?.datacenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedNetworks(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true} // 다중 선택 활성화
      />

      {/* <NetworkTable
        columns={columns}
        networks={networks}
        setSelectedNetworks={(selected) => {
          if (Array.isArray(selected)) setSelectedNetworks(selected); // 유효한 선택만 반영
        }}
      /> */}

      {/* 네트워크 모달창 */}
      { renderModals() }
    </>
  );
};

export default NetworkDupl;
