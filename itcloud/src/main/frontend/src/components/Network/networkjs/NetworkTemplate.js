import { useAllTemplatesFromNetwork } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react'; 

// 애플리케이션 섹션
const NetworkTemplate = ({ network }) => {

    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);

    useEffect(() => {
      if (!network?.id) {
        console.error('Network ID is undefined or missing');
      } else {
        console.log('Fetching templates for Network ID:', network.id);
      }
    }, [network?.id]);

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
          columns={TableColumnsInfo.TEMPLATES}
          data={templates}
          emptyMessage="내용이 없습니다" // 데이터가 없을 때 표시할 메시지
          onRowClick={() => console.log('Row clicked')} 
        />
      </>
    );
};

export default NetworkTemplate;
