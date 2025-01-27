import { Suspense, useState } from "react";
import { useAllNicsFromTemplate } from "../../../api/RQHook";
import TablesOuter from "../../../components/table/TablesOuter";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TableRowClick from "../../../components/table/TableRowClick";
import TemplateNeworkNewInterModal from './modal/TemplateNeworkNewInterModal';
import DeleteModal from "../../../components/DeleteModal";
import NicActionButtons from "../../network/nic/NicActionButton"
import { renderUpDownStatusIcon } from "../../../utils/Icon";

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

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <TemplateNeworkNewInterModal
          isOpen={true}
          onRequestClose={closeModal}
          editMode={false}
          templateId={templateId}
          nicData={selectedVnicProfiles[0]} // 수정 시 첫 번째 항목 전달
        />
      )}
      {activeModal === 'edit' && (
        <TemplateNeworkNewInterModal
          isOpen={true}
          onRequestClose={closeModal}
          editMode={true}
          templateId={templateId}
          nicData={selectedVnicProfiles[0]} // 수정 시 첫 번째 항목 전달
        />
      )}
      
      {activeModal === 'delete' && (
        <DeleteModal
          isOpen={true}
          onRequestClose={closeModal}
          type="NetworkInterfaceFromTemplate" // 전달되는 타입
          templateId={templateId} // 템플릿 ID 전달
          data={selectedVnicProfiles} // 선택된 NIC 데이터 전달
          contentLabel="NIC 삭제"
        />
      )}
    </Suspense>
  );

  return (
    <>
      <NicActionButtons
        openModal={openModal}
        isEditDisabled={selectedVnicProfiles.length !== 1}
      />
      <span>id = {selectedIds || ''}</span>

      <TablesOuter
        columns={TableColumnsInfo.NICS_FROM_TEMPLATE}
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
      { renderModals() }
    </>
  );
};

export default TemplateNics;
