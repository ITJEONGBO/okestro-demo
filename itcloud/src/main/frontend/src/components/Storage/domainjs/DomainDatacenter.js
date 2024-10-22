import { useAllDataCenterFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";
import { useState,useEffect } from 'react'; 
import { useNavigate} from 'react-router-dom';


const DomainDatacenter = ({ domain }) => {
    const navigate = useNavigate();
    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);

    const openModal = (popupType) => setActivePopup(popupType);
    const closeModal = () => setActivePopup(null);


  //데이터센터(???????정보안나옴)
  const { 
    data: dataCenters, 
    status: dataCentersStatus, 
    isLoading: isDataCentersLoading, 
    isError: isDataCentersError,
  } = useAllDataCenterFromDomain(domain?.id, toTableItemPredicateDataCenters);
  function toTableItemPredicateDataCenters(dataCenter) {
    return {
      id: dataCenter?.id ?? '없음',
      dataCenterId: dataCenter?.dataCenterVo?.id ?? '',
      name: dataCenter?.name ?? '',
      status:dataCenter?.status ?? '',
      dataCenterStatus: dataCenter?.dataCenterStatus ? '활성화' : '비활성화'
    };
  }

    return (
        <>
              <div className="header_right_btns">
                <button className='disabled'>연결</button>
                <button className='disabled'>분리</button>
                <button className='disabled'>활성</button>
                <button onClick={() => openModal('maintenance')}>유지보수</button>
              </div>
              
              <TableOuter 
                columns={TableInfo.DATACENTERS_FROM_STORAGE_DOMAIN}
                data={dataCenters} 
                onRowClick={(row, column, colIndex) => {
                  if (colIndex === 1) {
                    navigate(`/computing/datacenters/${row.dataCenterId}`);  // 1번 컬럼 클릭 시 이동할 경로
                  }
                }}
                clickableColumnIndex={[1]} 
                onContextMenuItems={() => [
                  <div key="연결" onClick={() => console.log()}>연결</div>,
                  <div key="분리" onClick={() => console.log()}>분리</div>,
                  <div key="활성" onClick={() => console.log()}>활성</div>,
                  <div key="유지보수" onClick={() => console.log()}>유지보수</div>
                ]}
              />
          </>
    );
  };
  
  export default DomainDatacenter;