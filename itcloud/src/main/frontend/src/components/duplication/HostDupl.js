import React, { Suspense, useState } from 'react';
import HostActionButtons from '../button/HostActionButtons';
import HostTable from '../table/HostTable';
import HostModal from '../Modal/HostModal';
import HostActionModal from '../Modal/HostActionModal';
import DeleteModal from '../Modal/DeleteModal';

// const HostModal = React.lazy(() => import('../Modal/HostModal'));
// const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
// const HostActionModal = React.lazy(() => import('../Modal/HostActionModal'));

const HostDupl = ({ 
  hosts, 
  columns, 
  clusterId
}) => {
  const [activeModal, setActiveModal] = useState(null);
  const [selectedHosts, setSelectedHosts] = useState([]);

  const selectedIds = selectedHosts.map((host) => host.id).join(', ');
  
  // const openModal = (action) => setActiveModal(action);
  const openModal = (action) => {
    console.log('Opening modal:', action); // Debug log
    setActiveModal(action);
  };
  const closeModal = () => setActiveModal(null);

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

      
      {/* <Suspense fallback={<div>Loading...</div>}> */}
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
        {activeModal === 'manageAction' && (
          <HostActionModal
            action={activeModal}
            data={selectedHosts?.[0]}
            onRequestClose={closeModal}
          />
        )}
      {/* </Suspense> */}
    </>
  );
};

export default HostDupl;