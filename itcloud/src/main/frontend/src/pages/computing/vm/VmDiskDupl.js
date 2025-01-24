import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TablesOuter from '../../../components/table/TablesOuter';
import TableRowClick from '../../../components/table/TableRowClick';
import VmDiskActionButtons from './button/VmDiskActionButtons';
import VmDiskModals from './modal/VmDIskModals';
import { icon } from '@fortawesome/fontawesome-svg-core';

const VmDiskDupl = ({ vmDisks = [], columns = [], vmId }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크
  const selectedIds = (Array.isArray(selectedDisks) ? selectedDisks : []).map((disk) => disk.id).join(', ');

  const handleNameClick = (id) => navigate(`/storages/disks/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);  

  const status = selectedDisks.length === 0 ? 'none': selectedDisks.length === 1 ? 'single': 'multiple';
  
  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <VmDiskActionButtons
        openModal={openModal}
        isEditDisabled={selectedDisks?.length !== 1}
        isDeleteDisabled={selectedDisks?.length === 0}
        status={selectedDisks[0]?.status}
      />
      <span>ID: {selectedIds || ''}</span>

      <TablesOuter
        columns={columns}
        data={vmDisks.map((d) => {
          return {
            ...d,
            alias: d?.alias || d?.diskImageVo?.alias,
            icon: icon(d.status),
            storageDomain: <TableRowClick type="domains" id={d?.diskImageVo?.storageDomainVo?.id}>{d?.diskImageVo?.storageDomainVo?.name}</TableRowClick>,
            storageType: d?.diskImageVo?.storageType,
          };
        })}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedDisks(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
        // onContextMenuItems={(row) => [ // 마우스 버튼
        //   <DiskActionButtons
        //     openModal={openModal}
        //     isEditDisabled={!row} 
        //     type='context'
        //   />
        // ]}
      />

      {/* 디스크 모달창 */}
      <VmDiskModals
        activeModal={activeModal}
        disk={selectedDisks[0]}
        selectedDisks={selectedDisks}
        vmId={vmId}
        onClose={closeModal}
      />
    </div>
  );
};

export default VmDiskDupl;
