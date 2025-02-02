import React from 'react';
import VmDeleteModal from './VmDeleteModal';
import VmActionModal from './VmActionModal';
import VmSnapshotModal from './VmSnapshotModal';
import VmNewModal from './VmNewModal';

const VmModals = ({ activeModal, vm, selectedVms = [], onClose }) => {
  const modals = {
    create: (
      // <VmModal 
      //   isOpen={activeModal === 'create'} 
      //   onClose={onClose} 
      // />
      <VmNewModal
        isOpen={activeModal === 'create'} 
        onClose={onClose} 
      />
    ),
    edit: (
      // <VmModal
      //   editMode
      //   isOpen={activeModal === 'edit'}
      //   vmId={vm?.id}
      //   onClose={onClose}
      // />
      <VmNewModal
        editMode
        isOpen={activeModal === 'edit'} 
        vmId={vm?.id}
        onClose={onClose} 
      />
    ),
    delete: (
      <VmDeleteModal
        isOpen={activeModal === 'delete'}
        onClose={onClose}
        data={selectedVms}
      />
    ),
    snapshot: (
      <VmSnapshotModal
        isOpen={activeModal === 'snapshot'}
        vmId={vm?.id}
        onClose={onClose}
      />
    ),
    action: (
      <VmActionModal
        isOpen={['start', 'pause', 'reboot', 'reset', 'stop', 'powerOff'].includes(activeModal)}
        action={activeModal}
        data={selectedVms}
        onClose={onClose}
      />
    ),
  };

  return (
    <>
      {Object.keys(modals).map((key) => (
        <React.Fragment key={key}>{modals[key]}</React.Fragment>
      ))}
    </>
  );
};

export default VmModals;
