import React, {useState } from 'react';
import {faTimes } from '@fortawesome/free-solid-svg-icons';
import { useParams } from 'react-router-dom';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TablesOuter from '../../../components/table/TablesOuter';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useHost, useHostdeviceFromHost, useHostdevicesFromVM } from '../../../api/RQHook';
import VmDeviceAddModal from './modal/VmDeviceAddModal';
import VmCPUPinningModal from './modal/VmCPUPinningModal';
// 이벤트 섹션
const VmHostDevice = ({vm}) => {
  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);
  // 호스트 장치
  const { id } = useParams();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { 
    data: hostDevices,     
    status: hostDevicesStatus,  
    isLoading: isHostDevicesLoading,  
    isError: isHostDevicesError       
  } = useHostdevicesFromVM(vm?.id, toTableItemPredicateHostDevices);  
  
  function toTableItemPredicateHostDevices(hostDevice) {
    return {
      name: hostDevice?.name ?? 'Unknown',
      capability: hostDevice?.capability ?? 'Unknown',
      vendorName: hostDevice?.vendorName ?? 'Unknown',
      productName: hostDevice?.productName ?? 'Unknown',
      driver: hostDevice?.driver ?? 'Unknown',
      currentlyUsed: hostDevice?.currentlyUsed ?? 'Unknown',
      connectedToVM: hostDevice?.connectedToVM ?? 'Unknown',
      iommuGroup: hostDevice?.iommuGroup ?? '해당 없음',
      mdevType: hostDevice?.mdevType ?? '해당 없음',
    };
  }
    return (
      <>
        <div className="header_right_btns">
            <button onClick={() => setIsModalOpen(true)}>장치 추가</button>
            <button className='disabled'>장치 삭제</button>
            <button className='disabled'>vGPU 관리</button>
            <button onClick={() => openPopup('view_cpu')}>View CPU Pinning</button>
        </div>
        <TablesOuter 
          columns={TableColumnsInfo.DEVICE_FROM_HOST} 
          data={hostDevices} 
          onRowClick={() => console.log('Row clicked')} 
        />

        {/*장치추가 팝업 */}
        <VmDeviceAddModal
          isOpen={isModalOpen}
          onRequestClose={() => setIsModalOpen(false)}
          hostDevices={hostDevices}
        />
        {/*View CPU Pinning 팝업 */}
        <VmCPUPinningModal
          isOpen={activePopup === 'view_cpu'}
          onRequestClose={closePopup}
        />
       {/*장치추가 팝업 */}
        {/* <Modal
        isOpen={activePopup === 'add_device'}
        onRequestClose={closePopup}
        contentLabel="장치추가"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="device_add_popup_outer">
          <div className="popup_header">
            <h1>호스트 장치 추가</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className='p-1'>
            <div className="select_box mb-1">
              <label className='mr-1 ' htmlFor="fixed_host">고정된 호스트</label>
              <select id="fixed_host">
                <option value="host01.ititinfo.com">host01.ititinfo.com</option>
                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
              </select>
            </div>
            <div className="select_box mb-1 flex">
              <label className='mr-1 w-9 block' htmlFor="features">기능</label>
              <select id="features">
                <option value="pci">pci</option>
                <option value="scsi">scsi</option>
                <option value="sub_device">sub_device</option>
                <option value="nvdimm">nvdimm</option>
              </select>
            </div>
          </div>

          <div className='p-1'>
              <span className='font-bold'>사용 가능한 호스트 장치</span>
              <div className='able_host_device_table'>
              <TableOuter 
                    columns={TableColumnsInfo.ALL_DISK}
                    data={hostDevices}
                    onRowClick={() => console.log('Row clicked')}
                  />
            </div>
          </div>

          <div className='p-1'>
            <span className='font-bold'>연결 호스트 장치</span>
              <div className='able_host_device_table'>
              <TableOuter 
                    columns={TableColumnsInfo.ALL_DISK}
                    data={hostDevices}
                    onRowClick={() => console.log('Row clicked')}
                  />
            </div>
          </div>
          
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
        </Modal> */}

        {/*View CPU Pinning 팝업 */}
        {/* <Modal
        isOpen={activePopup === 'view_cpu'}
        onRequestClose={closePopup}
        contentLabel="장치추가"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="device_view_popup_outer">
          <div className="popup_header">
            <h1>CPU Pinning</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className='device_view_popup'>
            <div className='device_view_text'>
              <div className='font-bold mb-1'>CPU Pinning Policy</div>
              <div className='mb-1'>None</div>
            </div>
            <div className='device_view_text'>
              <div className='font-bold mb-1'>CPU Pinning</div>
              <div className='mb-1'>No CPU Pinning specified for the VM</div>
            </div>
            <div className='device_view_text'>
              <div className='font-bold mb-1'>CPU Topology</div>
              <div>The CPU Topology shows mapping from the VM's vCPU to the host physical CPU</div>
            </div>

            <div>
              <div className='px-2 py-1.5'>Socket 0</div>
              <div className='device_view_boxs'>
                <span>Core 0</span>
                <div className='device_view_box'>
                  vCPU 0
                </div>
              </div>
              <div className='device_view_boxs'>
                <span>Core 1</span>
                <div className='device_view_box'>
                  vCPU 1
                </div>
              </div>
              <div className='device_view_boxs'>
                <span>Core 2</span>
                <div className='device_view_box'>
                  vCPU 2
                </div>
              </div>
            </div>
          </div>
          
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
        </Modal> */}
      </>
    );
  };
  
  export default VmHostDevice;
  