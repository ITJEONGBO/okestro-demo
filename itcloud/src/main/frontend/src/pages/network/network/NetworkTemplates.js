import { Suspense, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAllTemplatesFromNetwork } from "../../../api/RQHook";
import TablesOuter from '../../../components/table/TablesOuter';
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";

const NetworkTemplates = ({ networkId }) => {
  const navigate = useNavigate();
  const { 
    data: templates = [], isLoading: isTemplatesLoading
  } = useAllTemplatesFromNetwork(networkId, (e) => ({ ...e }));

  const [selectedNics, setSelectedNics] = useState([]); // 선택된 항목
  const selectedTemplateIds = (Array.isArray(selectedNics) ? selectedNics : []).map(template => template.id).join(', '); // templateId 추출
  const selectedNicIds = (Array.isArray(selectedNics) ? selectedNics : []).map(template => template.nicId).join(', '); // nicId 추출

  const handleNameClick = (id) => navigate(`/computing/templates/${id}`);

  const [isModalOpen, setIsModalOpen] = useState(false); 

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {/* Modal Placeholder */}
    </Suspense>
  );

  return (
    <>
      <div className="header-right-btns">
        <button onClick={() => setIsModalOpen(true)} disabled={!selectedNicIds}>제거</button>
      </div>
      
      {/* 선택된 Template ID와 NIC ID 표시 */}
      <span>선택된 Template ID: {selectedTemplateIds || '없음'}</span>
      <br />
      <span>선택된 NIC ID: {selectedNicIds || '없음'}</span>

      <TablesOuter
        columns={TableColumnsInfo.TEMPLATES_FROM_NETWORK}
        data={templates}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedNics(selectedRows)} // 선택된 항목 업데이트
        multiSelect={true}
        onContextMenuItems={(row) => [
          <div className='right-click-menu-box'>
            <button className='right-click-menu-btn' onClick={() => setIsModalOpen(true)} >제거</button>
        </div>
        ]}
      />

      {/* 템플릿 NIC 삭제 */}
      { renderModals() }
    </>
  );
};

export default NetworkTemplates;
