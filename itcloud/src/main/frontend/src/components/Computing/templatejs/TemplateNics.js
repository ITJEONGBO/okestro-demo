import { useAllNicsFromTemplate } from "../../../api/RQHook";
import TablesOuter from "../../table/TablesOuter";
import { Suspense, useState } from "react";
import TemplateNeworkNewInterModal from "../../Modal/TemplateNeworkNewInterModal";
import DeleteModal from "../../Modal/DeleteModal";
import TableInfo from "../../table/TableInfo";

const TemplateNics = ({ templateId }) => {
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedVnicProfiles, setSelectedVnicProfiles] = useState([]); // 다중 선택된 네트워크 인터페이스

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  // 템플릿에 연결된 vNIC 프로파일 데이터 가져오기
  const { data: vnicProfiles = [] } = useAllNicsFromTemplate(templateId, (e) => {
    return {
      ...e,
      networkVo: e?.networkVo?.name || 'N/A',
      vnicProfileVo: e?.vnicProfileVo?.name || 'N/A',
    };
  });

  const handleRowSelection = (selectedRows) => {
    setSelectedVnicProfiles(Array.isArray(selectedRows) ? selectedRows : []);
  };

  return (
    <>
      {/* 액션 버튼 */}
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button
          onClick={() => selectedVnicProfiles.length === 1 && toggleModal('edit', true)}
          disabled={selectedVnicProfiles.length !== 1} // 1개만 선택 가능
        >
          수정
        </button>
        <button
          onClick={() => selectedVnicProfiles.length > 0 && toggleModal('delete', true)}
          disabled={selectedVnicProfiles.length === 0} // 선택된 항목이 없으면 비활성화
        >
          제거
        </button>
      </div>
      <span>선택된 ID: {selectedVnicProfiles.map((profile) => profile.id).join(', ') || '선택된 항목이 없습니다.'}</span>

      {/* vNIC 프로파일 테이블 */}
      <TablesOuter
        columns={TableInfo.NICS_FROM_TEMPLATES}
        onRowClick={handleRowSelection} // 다중 선택된 행 업데이트
        data={vnicProfiles}
        clickableColumnIndex={[3, 4]} // 클릭 가능한 열 인덱스
      />

      {/* 모달 관리 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedVnicProfiles.length === 1)) && (
          <TemplateNeworkNewInterModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            nicData={selectedVnicProfiles[0]} // 수정 시 첫 번째 항목 전달
            templateId={templateId}
          />
        )}

        {modals.delete && selectedVnicProfiles.length > 0 && (
          <DeleteModal
            isOpen={modals.delete}
            type="NetworkInterfaceFromTemplate"
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'네트워크 인터페이스'}
            data={selectedVnicProfiles} // 다중 선택된 데이터 전달
            templateId={templateId}
          />
        )}
      </Suspense>
    </>
  );
};

export default TemplateNics;



// import { useAllNicsFromTemplate, useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
// import TableColumnsInfo from "../../table/TableColumnsInfo";
// import TableOuter from "../../table/TableOuter";
// import { useNavigate} from 'react-router-dom';
// import { Suspense, useState,useEffect } from 'react'; 
// import Modal from 'react-modal';
// import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
// import { faTimes } from "@fortawesome/free-solid-svg-icons";
// import VnicProfileModal from "../../Modal/VnicProfileModal";
// import DeleteModal from "../../Modal/DeleteModal";
// import TableInfo from "../../table/TableInfo";
// import VmNetworkNewInterfaceModal from "../../Modal/VmNetworkNewInterfaceModal";
// import TemplateNeworkNewInterModal from "../../Modal/TemplateNeworkNewInterModal";

// // 애플리케이션 섹션
// const TemplateNics = ({templateId}) => {

//     const [modals, setModals] = useState({ create: false, edit: false, delete: false });
//     const [selectedVnicProfiles, setSelectedVnicProfiles] = useState(null);
//     const toggleModal = (type, isOpen) => {
//         setModals((prev) => ({ ...prev, [type]: isOpen }));
//     };
//     const { 
//         data: vnicProfiles,
//       } = useAllNicsFromTemplate(templateId, (e) => { 
//         return {
//         ...e ,
//         networkVo: e?.networkVo.name,
//         vnicProfileVo: e?.vnicProfileVo.name,
//         }
//       });

//     return (
//         <>
//         <div className="header_right_btns">
//             <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
//             <button 
//                 onClick={() => selectedVnicProfiles?.id && toggleModal('edit', true)} 
//                 disabled={!selectedVnicProfiles?.id}
//             >
//                 수정
//             </button>
//             <button 
//                 onClick={() => selectedVnicProfiles?.id && toggleModal('delete', true)} 
//                 disabled={!selectedVnicProfiles?.id}
//             >
//                 제거
//             </button>
//         </div>
//         <span>id = {selectedVnicProfiles?.id || ''}</span>
//         {/* vNIC 프로파일 */}
//         <TableOuter
//           columns={TableInfo.NICS_FROM_TEMPLATES} 
//           onRowClick={(row) => setSelectedVnicProfiles(row)}
//           data={vnicProfiles}
//           clickableColumnIndex={[3,4]} 
//         />
//       {/*vNIC 프로파일(새로만들기)팝업 */}
//       <Suspense>
//             {(modals.create || (modals.edit && selectedVnicProfiles)) && (
//                 <TemplateNeworkNewInterModal
//                     isOpen={modals.create || modals.edit}
//                     onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
//                     editMode={modals.edit}
//                     nicData={selectedVnicProfiles}
//                     templateId={templateId}
//                 />
//             )}
//             {modals.delete && selectedVnicProfiles && (
//                 <DeleteModal
//                     isOpen={modals.delete}
//                     type='NetworkInterfaceFromTemplate'
//                     onRequestClose={() => toggleModal('delete', false)}
//                     contentLabel={'네트워크 인터페이스'}
//                     data={ selectedVnicProfiles}
//                     templateId={templateId}
//                 />
//             )}
//             </Suspense>


//    </>
//     );
//   };
  
//   export default TemplateNics;