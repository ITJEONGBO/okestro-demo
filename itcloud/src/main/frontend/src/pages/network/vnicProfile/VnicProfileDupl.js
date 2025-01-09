import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import VnicProfileActionButtons from './button/VnicProfileActionButtons.js';
import TablesOuter from '../../../components/table/TablesOuter.js';
import TableRowClick from '../../../components/table/TableRowClick.js';

const VnicProfileModal = React.lazy(() => import('./modal/VnicProfileModal'));
const VnicProfileDeleteModal = React.lazy(() => import('./modal/VnicProfileDeleteModal'));

const VnicProfileDupl = ({ vnicProfiles = [], columns = [], networkId }) => {
  const navigate = useNavigate();
  
  const [activeModal, setActiveModal] = useState(null);
  const [selectedVnicProfiles, setSelectedVnicProfiles] = useState([]); // 다중 선택된 vNIC 프로파일
  const selectedIds = (Array.isArray(selectedVnicProfiles) ? selectedVnicProfiles : []).map(vnic => vnic.id).join(', ');

  const handleNameClick = (id) => navigate(`/vnicprofiles/${id}`);

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <VnicProfileModal
          onClose={closeModal}
        />
      )}
      {activeModal === 'edit' && (
        <VnicProfileModal
          editMode
          vnicProfileId={selectedVnicProfiles[0].id}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <VnicProfileDeleteModal
          data={selectedVnicProfiles}
          onClose={closeModal}
        />
      )}
    </Suspense>
  );

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <VnicProfileActionButtons
        openModal={openModal}
        isEditDisabled={selectedVnicProfiles.length !== 1}
        isDeleteDisabled={selectedVnicProfiles.length === 0}
        selectedVnicProfiles={selectedVnicProfiles}
      />
      <span> ID: {selectedIds}</span>

      <TablesOuter
        columns={columns}
        data={vnicProfiles.map((vnic) => ({
          ...vnic,
          network: (
            <TableRowClick type="network" id={vnic?.networkVo?.id}>
              {vnic?.networkVo?.name}
            </TableRowClick>
          ),
          dataCenter: (
            <TableRowClick type="datacenter" id={vnic?.dataCenterVo?.id}>
              {vnic?.dataCenterVo?.name}
            </TableRowClick>
          ),
          passThrough: vnic?.passThrough === 'DISABLED' ? '아니요' : '예',
          networkFilter: vnic?.networkFilterVo?.name || '-',
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedVnicProfiles(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* vnicProfile 모달창 */}
      { renderModals() }
    </div>
  );
};

export default VnicProfileDupl;
