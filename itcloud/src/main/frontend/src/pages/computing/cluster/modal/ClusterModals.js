import React from "react";
import ClusterDeleteModal from "./ClusterDeleteModal";
import ClusterModal from "./ClusterModal";

const ClusterModals = ({ activeModal, cluster, selectedClusters = [], dcId, onClose }) => {
  const modals = {
    create: 
      <ClusterModal 
        isOpen={activeModal === 'create'} 
        dcId={dcId}
        onClose={onClose} 
        />,
    edit: (
      <ClusterModal
        editMode
        isOpen={activeModal === 'edit'}
        cId={cluster?.id}
        onClose={onClose}
    />
    ),
    delete: (
      <ClusterDeleteModal
        isOpen={activeModal === 'delete' }
        data={selectedClusters}
        onClose={onClose}
      />
    )
  };

  return (
    <>
      {Object.keys(modals).map((key) => (
          <React.Fragment key={key}>{modals[key]}</React.Fragment>
      ))}
    </>
  );
};

export default ClusterModals;