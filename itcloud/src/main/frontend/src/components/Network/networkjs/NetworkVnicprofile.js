import { useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { useState } from 'react'; 

// 애플리케이션 섹션
const NetworkVnicprofile = (network) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => setActivePopup(popupType);
    const closePopup = () => setActivePopup(null);
    const { 
        data: vnicProfiles,
        status: vnicProfilesStatus,
        isRefetching: isvnicProfilesRefetching,
        refetch: vnicProfilesRefetch,
        isError, 
        error, 
        isLoading
      } = useAllVnicProfilesFromNetwork(network?.id, toTableItemPredicateVnicProfiles);
      function toTableItemPredicateVnicProfiles(vnicProfile) {
        return {
          id: vnicProfile?.id ?? '없음',
          name: vnicProfile?.name ?? '없음',
          network: vnicProfile?.networkVo?.name ?? '',  // 네트워크 이름
          dataCenter: vnicProfile?.dataCenterVo?.name ?? '',  // 데이터 센터
          compatVersion: vnicProfile?.compatVersion ?? '없음',  // 호환 버전
          qosName: vnicProfile?.qosName ?? '',  // QoS 이름
          networkFilter: vnicProfile?.networkFilterVo?.name ?? '없음',  // 네트워크 필터
          portMirroring: vnicProfile?.portMirroring ? '사용' : '사용 안함',  // 포트 미러링 여부
          passThrough: vnicProfile?.passThrough ? '통과' : '아니요',  // 통과 여부
          description: vnicProfile?.description ?? '없음',  // 설명
        };
      }
    return (
        <>
        <div className="header_right_btns">
            <button onClick={() => openPopup('vnic_new_popup')}>새로 만들기</button>
            <button onClick={() => openPopup('vnic_eidt_popup')}>편집</button>
            <button onClick={() => openPopup('delete')} >제거</button>
        </div>
        {/* vNIC 프로파일 */}
        <TableOuter
          columns={TableColumnsInfo.VNIC_PROFILES} 
          data={vnicProfiles}
          onRowClick={(row) => {
            navigate(`/computing/datacenters/${network.id}`);
          }}
          clickableColumnIndex={[2]} 
        />
     </>
    );
  };
  
  export default NetworkVnicprofile;