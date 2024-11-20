import React, { Suspense } from 'react';
import TablesOuter from '../table/TablesOuter';
import ClusterActionButtons from '../button/ClusterActionButtons';

const ClusterModal = React.lazy(() => import('../Modal/ClusterModal'));
const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

const ClusterDupl = ({
  clusters,
  columns,
  onRowClick,
  onCreate,
  onEdit,
  onDelete,
  selectedCluster,
  toggleModal,
  modals,
  handleNameClick,
  datacenterId = null,
}) => (
  <>
    <ClusterActionButtons
      onCreate={onCreate}
      onEdit={onEdit}
      onDelete={onDelete}
      isEditDisabled={!selectedCluster?.id}
    />
    <span>id = {selectedCluster?.id || ''}</span>

    <TablesOuter
      columns={columns}
      data={clusters}
      shouldHighlight1stCol={true}
      onRowClick={onRowClick}
      clickableColumnIndex={[0]}
      onClickableColumnClick={(row) => handleNameClick(row.id)}
    />

    {/* 모달 컴포넌트를 사용할 때만 로딩 */}
    <Suspense>
      {(modals.create || (modals.edit && selectedCluster)) && (
        <ClusterModal
          isOpen={modals.create || modals.edit}
          onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
          editMode={modals.edit}
          cId={selectedCluster?.id || null}
          datacenterId={datacenterId}
        />
      )}
      {modals.delete && selectedCluster && (
        <DeleteModal
          isOpen={modals.delete}
          type="Cluster"
          onRequestClose={() => toggleModal('delete', false)}
          contentLabel="클러스터"
          data={selectedCluster}
        />
      )}
    </Suspense>
  </>
);

export default ClusterDupl;
