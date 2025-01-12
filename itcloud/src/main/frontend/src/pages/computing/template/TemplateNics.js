import { Suspense, useState } from "react";
import { useAllNicsFromTemplate } from "../../../api/RQHook";
import TablesOuter from "../../../components/table/TablesOuter";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import NicActionButtons from '../../network/network/button/NicActionButton';
import TableRowClick from "../../../components/table/TableRowClick";
import { renderUpDownStatusIcon } from "../../../utils/format";

// const TemplateNeworkNewInterModal = React.lazy(() => import('./modal/'));

const TemplateNics = ({ templateId }) => {
  const { 
    data: vnicProfiles = [], isLoading: isVnicLoading 
  } = useAllNicsFromTemplate(templateId, (e) => ({ ...e }));

  const [activeModal, setActiveModal] = useState(null);
  const [selectedVnicProfiles, setSelectedVnicProfiles] = useState([]); 
  const selectedIds = (Array.isArray(selectedVnicProfiles) ? selectedVnicProfiles : []).map(vnic => vnic.id).join(', ');

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  // 템플릿에 연결된 vNIC 프로파일 데이터 가져오기

  // const renderModals = () => (
  //   <Suspense fallback={<div>Loading...</div>}>
  //     {activeModal === 'create' && (
  //       <TemplateNeworkNewInterModal
  //       isOpen={modals.create || modals.edit}
  //       onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
  //       editMode={modals.edit}
  //       nicData={selectedVnicProfiles[0]} // 수정 시 첫 번째 항목 전달
  //       templateId={templateId}
  //     />
  //     )}
  //     {activeModal === 'edit' && (
  //       <HostModal
  //         editMode
  //         hId={selectedHosts[0]?.id || null}
  //         clusterId={clusterId}
  //         onClose={closeModal}
  //       />
  //     )}
  //     {activeModal === 'delete' && (
  //       <HostDeleteModal
  //       data={selectedHosts}
  //       onClose={closeModal}
  //       />
  //     )}
  // );

  return (
    <>
      <NicActionButtons
        openModal={openModal}
        isEditDisabled={selectedVnicProfiles.length !== 1}
      />
      <span>id = {selectedIds || ''}</span>

      <TablesOuter
        columns={TableColumnsInfo.NICS_FROM_TEMPLATES}
        data={vnicProfiles.map((nic) => ({
          ...nic,
          status: renderUpDownStatusIcon(nic?.status),
          network: <TableRowClick type="network" id={nic?.networkVo?.id}>{nic?.networkVo?.name}</TableRowClick>,
          vnicProfile: <TableRowClick type="vnicProfile" id={nic?.vnicProfileVo?.id}>{nic?.vnicProfileVo?.name}</TableRowClick>,
          linked: nic?.linked === true ? "Up" : 'Down',
          plugged: nic?.plugged === true ? <input type="checkbox" checked disabled/> : <input type="checkbox" disabled/>,
        }))}
        onRowClick={(selectedRows) => setSelectedVnicProfiles(selectedRows)}
        clickableColumnIndex={[3, 4]} // 클릭 가능한 열 인덱스
      />

      {/* nic 모달창 */}
      {/* { renderModals() } */}
    </>
  );
};

export default TemplateNics;
