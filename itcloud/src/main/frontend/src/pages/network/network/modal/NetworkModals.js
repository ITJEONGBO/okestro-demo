import React from "react";
import NetworkModal from "./NetworkModal";
import NetworkDeleteModal from "./NetworkDeleteModal";
import NetworkImportModal from "./NetworkImportModal";

const NetworkModals = ({ activeModal, network, selectedNetworks = [], dcId, onClose }) => {
  const modals = {
    create: 
      <NetworkModal 
        isOpen={activeModal === 'create'} 
        dcId={dcId}
        onClose={onClose} 
      />,
    edit: (
      <NetworkModal
        editMode
        isOpen={activeModal === 'edit'}
        networkId={network?.id}
        onClose={onClose}
    />
    ),
    delete: (
      <NetworkDeleteModal
        isOpen={activeModal === 'delete' }
        data={selectedNetworks}
        onClose={onClose}
      />
    ),
    // import: (
    //   <NetworkImportModal
    //     isOpen={isModalOpen}
    //     action={action}
    //     onRequestClose={onRequestClose}
    //     contentLabel={getContentLabel(action)}
    //     data={selectedNetwork}
    //   />
    // )
  };

  return (
    <>
      {Object.keys(modals).map((key) => (
          <React.Fragment key={key}>{modals[key]}</React.Fragment>
      ))}
    </>
  );
};

export default NetworkModals;
