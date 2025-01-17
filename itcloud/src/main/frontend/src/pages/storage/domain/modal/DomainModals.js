import React from "react";
import DomainModal from "./DomainModal";
import DomainDeleteModal from "./DomainDeleteModal";
import DomainActionModal from "./DomainActionModal";

const DomainModals = ({ activeModal, domain, selectedDomains = [], datacenterId, onClose }) => {
  const modals = {
    create: (
      <DomainModal
        isOpen={activeModal === 'create'}
        datacenterId={datacenterId}
        onClose={onClose}
      />
    ),
    edit: (
      <DomainModal
        editMode
        isOpen={activeModal === 'edit'}
        domainId={domain?.id}
        onClose={onClose}
      />
    ),
    delete: (
      <DomainDeleteModal
        isOpen={activeModal === 'delete' }
        data={selectedDomains}
        onClose={onClose}
      />
    ),
    destroy: (
      <DomainDeleteModal
        isOpen={activeModal === 'destroy'}
        data={selectedDomains}
        deleteMode={false}
        onClose={onClose}
      />
    ),
    action: (
      <DomainActionModal
        isOpen={['attach', 'detach', 'activate', 'maintenance'].includes(activeModal)}
        action={activeModal}
        data={selectedDomains}
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

export default DomainModals;