import { Suspense, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAllTemplatesFromNetwork } from "../../../api/RQHook";
import TablesOuter from '../../../components/table/TablesOuter';
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";

// 애플리케이션 섹션
const NetworkTemplates = ({ networkId }) => {
  const navigate = useNavigate();
  const { 
    data: templates = [], isLoading: isTemplatesLoading
  } = useAllTemplatesFromNetwork(networkId, (e) => ({ ...e }));

  const [selectedNics, setSelectedNics] = useState([]);
  const selectedIds = (Array.isArray(selectedNics) ? selectedNics : []).map(nic => nic.id).join(', ');

  const handleNameClick = (id) => navigate(`/computing/templates/${id}`);

  const [isModalOpen, setIsModalOpen] = useState(false); 

  // const renderModals = () => (
  //   <Suspense fallback={<div>Loading...</div>}>
  //     {isModalOpen && (
  //       <NicDeleteModal // 네트워크 인터페이스 삭제모달
  //         isOpen={isModalOpen}
  //         onRequestClose={() => setIsModalOpen(false)}
  //         networkId={networkId}
  //       />
  //     )}      
  //   </Suspense>
  // );

  return (
    <>
      <div className="header-right-btns">
        <button onClick={() => setIsModalOpen(true)}>제거</button>
      </div>
      
      <span>ID: {selectedIds || ''}</span>

      <TablesOuter
        columns={TableColumnsInfo.TEMPLATES_FROM_NETWORK}
        data={templates}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedNics(selectedRows)}
        // clickableColumnIndex={[0]} // id는 nic 아이디로 나와야함
        // onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 네트워크 인터페이스 제거 모달 */}
      {/* { renderModals() } */}
    </>
  );
};

export default NetworkTemplates;
