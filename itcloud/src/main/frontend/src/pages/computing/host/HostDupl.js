import React, { Suspense, useState } from 'react';
import HostActionButtons from './button/HostActionButtons'
import HostTable from './HostTable';

const HostModal = React.lazy(() => import('./modal/HostModal'));
const DeleteModal = React.lazy(() => import('../../../components/DeleteModal'));
const HostActionModal = React.lazy(() => import('./modal/HostActionModal'));

const HostDupl = ({ 
  hosts, 
  columns, 
  clusterId
}) => {
  const [activeModal, setActiveModal] = useState(null);
  const [selectedHosts, setSelectedHosts] = useState([]);

  const selectedIds = selectedHosts.map((host) => host.id).join(', ');
  
  const openModal = (action) => {
    console.log('Opening modal:', action); // Debug log
    setActiveModal(action);
  };
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <HostModal
          clusterId={clusterId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'edit' && (
        <HostModal
          editMode
          hId={selectedHosts[0]?.id || null}
          clusterId={clusterId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <DeleteModal
          type="Host"
          contentLabel="호스트 삭제"
          data={selectedHosts}
          onRequestClose={closeModal}
        />
      )}
      {['deactivate', 'activate', 'restart', 'reInstall', 'register', 'haOn', 'haOff'].includes(activeModal) && (
        <HostActionModal
          action={activeModal} // `type` 전달
          host={selectedHosts?.[0]}
          onRequestClose={closeModal}
        />
      )}
    </Suspense>
  );

  return (
    <>
      <HostActionButtons
        openModal={openModal}
        isEditDisabled={selectedHosts.length !== 1}
        isDeleteDisabled={selectedHosts.length === 0}
        status={selectedHosts[0]?.status}
        selectedHosts={selectedHosts}
        clusterId={clusterId}
      />
      <span>ID: {selectedIds || ''}</span>

      <HostTable
        columns={columns}
        hosts={hosts}
        selectedHosts={selectedHosts} // 다중 선택 상태 전달
        setSelectedHosts={(selected) => Array.isArray(selected) && setSelectedHosts(selected)}
      />

      {/* 호스트 모달창 */}
      {renderModals()}
    </>
  );
};

export default HostDupl;