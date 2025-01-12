import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DiskActionButtons from './button/DiskActionButtons';
import TablesOuter from '../../../components/table/TablesOuter';
import { formatBytesToGBToFixedZero, icon } from '../../../utils/format';
import TableRowClick from '../../../components/table/TableRowClick';

const DiskModal = React.lazy(() => import('./modal/DiskModal'));
const DiskUploadModal = React.lazy(() => import('./modal/DiskUploadModal'))
const DiskActionModal = React.lazy(() => import('./modal/DiskActionModal'))
const DiskDeleteModal = React.lazy(() => import('./modal/DiskDeleteModal'))

const DiskDupl = ({ disks = [], columns = [] }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크
  const selectedIds = (Array.isArray(selectedDisks) ? selectedDisks : []).map((disk) => disk.id).join(', ');

  const handleNameClick = (id) => navigate(`/storages/disks/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <DiskModal
          onClose={closeModal}
        />
      )}
      {activeModal === 'edit' && (
        <DiskModal
          editMode
          diskId={selectedDisks[0]?.id}
          onClose={closeModal}
        />
      )}
      {activeModal === 'upload' && (
        <DiskUploadModal
          diskId={selectedDisks[0]?.id}
          onClose={closeModal}
        />
      )}
      {['move', 'copy'].includes(activeModal) && (
        <DiskActionModal
          action={activeModal} // `type` 전달
          disk={selectedDisks?.[0]}
          onRequestClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <DiskDeleteModal
          data={selectedDisks}
          onClose={closeModal}
        />
      )}
    </Suspense>
  );
  

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <DiskActionButtons
        openModal={openModal}
        isEditDisabled={selectedDisks.length !== 1}
        isDeleteDisabled={selectedDisks.length === 0}
        status={selectedDisks[0]?.status}
      />
      <span>ID: {selectedIds || ''}</span>

      <TablesOuter
        columns={columns}
        data={disks.map((d) => ({
          ...d,
          icon: icon(d.status),
          storageDomain: <TableRowClick type="domains" id={d?.storageDomainVo.id}>{d?.storageDomainVo.name}</TableRowClick>,
          sharable: d?.sharable ? 'O' : '',
          sparse: d?.sparse ? '씬 프로비저닝' : '사전 할당',
          connectVm: (
            <TableRowClick type={d?.connectVm?.id ? "vms" : "templates"} id={d?.connectVm?.id || d?.connectTemplate?.id}>
              {d?.connectVm?.name || d?.connectTemplate?.name}
            </TableRowClick>
          ),
          virtualSize: formatBytesToGBToFixedZero(d?.virtualSize),
          actualSize: formatBytesToGBToFixedZero(d?.actualSize),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedDisks(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 디스크 모달창 */}
      { renderModals() }
    </div>
  );
};

export default DiskDupl;
