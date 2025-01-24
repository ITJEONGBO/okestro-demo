import React from "react";
import VmDiskConnectionModal from "./VmDiskConnectionModal";
import VmDiskModal from "./VmDiskModal";
import { useVmById } from "../../../../api/RQHook";

const VmDiskModals = ({ activeModal, disk, selectedDisks = [], vmId, onClose }) => {
  const { data: vm } = useVmById(vmId);

  const modals = {
    create: 
      <VmDiskModal
        isOpen={activeModal === 'create'} 
        // datacenterId={datacenterId}
        onClose={onClose} 
      />,
    edit: (
      <VmDiskModal
        editMode
        isOpen={activeModal === 'edit'}
        diskId={disk?.id}
        onClose={onClose}
    />
    ),    
    delete: (
    //   <DiskDeleteModal
    //     isOpen={activeModal === 'delete' }
    //     data={selectedDisks}
    //     onClose={onClose}
    //   />
        <></>
    ), 
    connect: (
      <VmDiskConnectionModal
        isOpen={activeModal === 'connect'}
        vmId={vmId}
        dataCenterId={vm?.dataCenterVo?.id}
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

export default VmDiskModals;
