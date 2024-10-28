import { useAllTemplatesFromNetwork } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react'; 
import TableInfo from "../../table/TableInfo";

// 애플리케이션 섹션
const NetworkTemplate = ({ network }) => {

    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);


    const { 
      data: templates = [], 
      status: templatesStatus, 
      isLoading: isTemplatesLoading, 
      isError: isTemplatesError 
    } = useAllTemplatesFromNetwork(network?.id, toTableItemPredicateTemplates);

    function toTableItemPredicateTemplates(template) {
      return {
        name: template?.name ?? '없음',  // 템플릿 이름
        nicId: template?.nicId ?? '없음',  // 템플릿 버전
        status: template?.status ?? '없음',  // 템플릿 상태
        clusterName: template?.clusterName ?? '없음',  // 클러스터 이름
        nicName: template?.nicName ?? '없음',  // vNIC 이름
      };
    }

    return (
      <>
        <div className="header_right_btns">
          <button onClick={() => openPopup('delete')}>제거</button>
        </div>

        <TableOuter 
          columns={TableInfo.TEMPLATES_FROM_NETWORK}
          data={templates}
          onRowClick={() => console.log('Row clicked')} 
          onContextMenuItems={() => [
            <div key="네트워크 템플릿 제거" onClick={() => console.log()}>제거</div>
          ]}
        />
      </>
    );
};

export default NetworkTemplate;
