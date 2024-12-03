import { useAllNicsFromTemplate, useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { Suspense, useState,useEffect } from 'react'; 
import Modal from 'react-modal';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import VnicProfileModal from "../../Modal/VnicProfileModal";
import DeleteModal from "../../Modal/DeleteModal";
import TableInfo from "../../table/TableInfo";
import VmNetworkNewInterfaceModal from "../../Modal/VmNetworkNewInterfaceModal";
import TemplateNeworkNewInterModal from "../../Modal/TemplateNeworkNewInterModal";

// 애플리케이션 섹션
const TemplateNics = ({templateId}) => {

    const [modals, setModals] = useState({ create: false, edit: false, delete: false });
    const [selectedVnicProfiles, setSelectedVnicProfiles] = useState(null);
    const toggleModal = (type, isOpen) => {
        setModals((prev) => ({ ...prev, [type]: isOpen }));
    };
    const { 
        data: vnicProfiles,
      } = useAllNicsFromTemplate(templateId, (e) => { 
        return {
        ...e ,
        networkVo: e?.networkVo.name,
        vnicProfileVo: e?.vnicProfileVo.name,
        }
      });

    return (
        <>
        <div className="header_right_btns">
            <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
            <button 
                onClick={() => selectedVnicProfiles?.id && toggleModal('edit', true)} 
                disabled={!selectedVnicProfiles?.id}
            >
                수정
            </button>
            <button 
                onClick={() => selectedVnicProfiles?.id && toggleModal('delete', true)} 
                disabled={!selectedVnicProfiles?.id}
            >
                제거
            </button>
        </div>
        <span>id = {selectedVnicProfiles?.id || ''}</span>
        {/* vNIC 프로파일 */}
        <TableOuter
          columns={TableInfo.NICS_FROM_TEMPLATES} 
          onRowClick={(row) => setSelectedVnicProfiles(row)}
          data={vnicProfiles}
          clickableColumnIndex={[]} 
        />
      {/*vNIC 프로파일(새로만들기)팝업 */}
      <Suspense>
            {(modals.create || (modals.edit && selectedVnicProfiles)) && (
                <TemplateNeworkNewInterModal
                    isOpen={modals.create || modals.edit}
                    onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
                    editMode={modals.edit}
                    nicData={selectedVnicProfiles}
                    templateId={templateId}
                />
            )}
            {modals.delete && selectedVnicProfiles && (
                <DeleteModal
                    isOpen={modals.delete}
                    type='NetworkInterface'
                    onRequestClose={() => toggleModal('delete', false)}
                    contentLabel={'NetworkInterface'}
                    data={ selectedVnicProfiles}
                    vmId={vnicProfiles?.id}
                />
            )}
            </Suspense>


   </>
    );
  };
  
  export default TemplateNics;