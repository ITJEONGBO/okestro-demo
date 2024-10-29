import { useAllDataCenterFromDomain} from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";
import { useState,useEffect } from 'react'; 
import { useNavigate} from 'react-router-dom';
import Modal from 'react-modal';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faExclamationTriangle, faTimes } from "@fortawesome/free-solid-svg-icons";


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

               {/*유지보수 팝업 */}
              <Modal
                isOpen={activePopup === 'maintenance'}
                onRequestClose={closeModal}
                contentLabel="디스크 업로드"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
              >
                <div className="maintenance_popup">
                  <div className="popup_header">
                    <h1>스토리지 도메인 관리</h1>
                    <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                  </div>
                
                  <div className='disk_delete_box'>
                    <div>
                      <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
                      <span>다음 스토리지 도메인을 유지 관리 모드로 설정하시겠습니까?</span>
                    </div>

                  </div>
                    
                  <div className='hosted_storage'>
                      <div>- hosted_storage</div>
                      <div className='host_checkbox'>
                        <input type="checkbox" id="ignore_ovf_update_failure" name="ignore_ovf_update_failure" />
                        <label htmlFor="ignore_ovf_update_failure">OVF 업데이트 실패 무시</label>
                      </div>

                      <div className='host_textbox'>
                        <label htmlFor="reason">이유</label>
                        <input type="text" id="user_name" />
                    </div>
                  </div>

                  <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closeModal}>취소</button>
                  </div>
                </div>
                </Modal>
          </>
    );
  };
  
  export default DomainDatacenter;