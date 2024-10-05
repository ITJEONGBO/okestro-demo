import React, { useEffect, useState } from 'react';
import { faCheck, faExclamation, faTimes } from '@fortawesome/free-solid-svg-icons';
import { useParams } from 'react-router-dom';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableOuter from '../../table/TableOuter';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useHostById, useHostdeviceFromHost } from '../../../api/RQHook';

// 이벤트 섹션
const EventSection = () => {
  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);
  // 호스트 장치
  const { id } = useParams();
  const { 
    data: host,
    status: networkStatus,
    isRefetching: isNetworkRefetching,
    refetch: hostRefetch, 
    isError: isNetworkError,
    error: networkError, 
    isLoading: isNetworkLoading,
  } = useHostById(id);
  const { 
    data: hostDevices,     
    status: hostDevicesStatus,  
    isLoading: isHostDevicesLoading,  
    isError: isHostDevicesError       
  } = useHostdeviceFromHost(host?.id, toTableItemPredicateHostDevices);  
  
  function toTableItemPredicateHostDevices(device) {
    return {
      name: device?.name ?? 'Unknown',
      capability: device?.capability ?? 'Unknown',
      vendorName: device?.vendorName ?? 'Unknown',
      productName: device?.productName ?? 'Unknown',
      driver: device?.driver ?? 'Unknown',
      currentlyUsed: device?.currentlyUsed ?? 'Unknown',
      connectedToVM: device?.connectedToVM ?? 'Unknown',
      iommuGroup: device?.iommuGroup ?? '해당 없음',
      mdevType: device?.mdevType ?? '해당 없음',
    };
  }
    return (
        <div className="host_btn_outer">
        <div className="content_header_right">
            <button onClick={() => openPopup('add_device')}>장치 추가</button>
            <button>장치 삭제</button>
            <button>vGPU 관리</button>
            <button>View CPU Pinning</button>
          </div>
          <TableOuter 
            columns={TableColumnsInfo.DEVICE_FROM_HOST} 
            data={hostDevices} 
            onRowClick={() => console.log('Row clicked')} 
          />
      


        {/*도메인(도메인 관리)팝업 */}
        <Modal
        isOpen={activePopup === 'newDomain'}
        onRequestClose={closePopup}
        contentLabel="도메인 관리"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="device_add_popup_outer">
          <div className="popup_header">
            <h1>호스트 장치 추가</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      </div>
    );
  };
  
  export default EventSection;
  