import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TablesOuter from '../table/TablesOuter';
import TableInfo from '../table/TableInfo';
import { faArrowsAltH, faBan, faCaretDown, faCheck, faCircle, faDesktop, faExclamationTriangle, faFan, faNetworkWired, faPencilAlt, faPlay, faTimes } from "@fortawesome/free-solid-svg-icons";
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';

const NetworkHostModal = ({ 
  isOpen, 
  onRequestClose, 
  hostId
}) => {

    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);
    const [isSecondModalOpen, setIsSecondModalOpen] = useState(false);
    const openSecondModal = () => {
        setIsSecondModalOpen(true);
    };
    const closeSecondModal = () => {
        setIsSecondModalOpen(false);
        setSelectedModalTab('ipv4'); // 모달이 닫힐 때 첫 번째 탭으로 초기화
    };
      // 탭 상태 정의 (기본 값: 'ipv4')
      const [selectedModalTab, setSelectedModalTab] = useState('ipv4');
        const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리
    const [activeButton, setActiveButton] = useState('network');
      const handleTabModalClick = (tab) => {
        setSelectedModalTab(tab);
      };
        const handleButtonClick = (button) => {
          setActiveButton(button);
          setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
        };
	return (
      <Modal
        isOpen={isOpen}
        onRequestClose={onRequestClose}
        contentLabel="호스트 네트워크 설정"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="popup_header">
            <h1>호스트 host01.ititinfo.com 네트워크 설정</h1>
            <button onClick={onRequestClose}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          
          <div className="host_network_outer px-1.5 text-sm">
          <div className="py-2 font-bold underline">드래그 하여 변경</div>

          <div className="host_network_separation">
      <div className="network_separation_left">
        <div>
          <div>인터페이스</div>
          <div>할당된 논리 네트워크</div>
        </div>

        <div className="separation_left_content">
          <div className="container gap-1">
            <FontAwesomeIcon icon={faCircle} style={{ fontSize: '0.1rem', color: '#00FF00' }} />
            <FontAwesomeIcon icon={faDesktop} />
            <span>ens192</span>
          </div>
          <div className="flex items-center justify-center">
            <FontAwesomeIcon icon={faArrowsAltH} style={{ color: 'grey', width: '5vw', fontSize: '0.6rem' }} />
          </div>

          <div className="container">
            <div className="left-section">
              <FontAwesomeIcon icon={faCheck} className="icon green-icon" />
              <span className="text">ovirtmgmt</span>
            </div>
            <div className="right-section">
              <FontAwesomeIcon icon={faFan} className="icon" />
              <FontAwesomeIcon icon={faDesktop} className="icon" />
              <FontAwesomeIcon icon={faDesktop} className="icon" />
              <FontAwesomeIcon icon={faBan} className="icon" />
              <FontAwesomeIcon icon={faExclamationTriangle} className="icon" />
              <FontAwesomeIcon icon={faPencilAlt} className="icon" onClick={() => setIsSecondModalOpen(true)} />
            </div>
          </div>
        </div>
      </div>

      <div className="network_separation_right">



  {/* {!isLabelVisible && (
      <DragDropContext onDragEnd={handleDragEnd}>
        <Droppable droppableId="unconfiguredNetwork" direction="vertical">
          {(provided) => (
            <div className="unconfigured_network" ref={provided.innerRef} {...provided.droppableProps}>
              <div>할당되지 않은 논리 네트워크</div>
              <div style={{ backgroundColor: '#d1d1d1' }}>필수</div>
              
              {items.map((item, index) => (
                <Draggable key={item.id} draggableId={item.id} index={index}>
                  {(provided) => (
                    <div
                      ref={provided.innerRef}
                      className="unconfigured_content flex items-center space-x-2"
                    >
           
                      <div {...provided.dragHandleProps}>
                        <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                        <span>{item.name}</span>
                      </div>
                      <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
                    </div>
                  )}
                </Draggable>
              ))}

              {provided.placeholder}
              <div style={{ backgroundColor: '#d1d1d1' }}>필요하지 않음</div>
            </div>
          )}
        </Droppable>
      </DragDropContext>
    )} */}

</div>

{/*연필아이콘 클릭하면 추가모달 */}
<Modal
  isOpen={isSecondModalOpen}
  onRequestClose={closeSecondModal} // 모달 닫기 핸들러 연결
  contentLabel="추가"
  className="Modal"
  overlayClassName="Overlay newRolePopupOverlay"
  shouldCloseOnOverlayClick={false}
>
  <div className="network_backup_edit">
    <div className="popup_header">
      <h1>관리 네트워크 인터페이스 수정:ovirtmgmt</h1>
      <button onClick={closeSecondModal}>
        <FontAwesomeIcon icon={faTimes} fixedWidth />
      </button>
    </div>

    <div className='flex'>
      <div className="network_backup_edit_nav">
        <div
          id="ipv4_tab"
          className={selectedModalTab === 'ipv4' ? 'active-tab' : 'inactive-tab'}
          onClick={() => setSelectedModalTab('ipv4')}
        >
          IPv4
        </div>
        <div
          id="ipv6_tab"
          className={selectedModalTab === 'ipv6' ? 'active-tab' : 'inactive-tab'}
          onClick={() => setSelectedModalTab('ipv6')}
        >
          IPv6
        </div>
        <div
          id="dns_tab"
          className={selectedModalTab === 'dns' ? 'active-tab' : 'inactive-tab'}
          onClick={() => setSelectedModalTab('dns')}
        >
          DNS 설정
        </div>
      </div>

      {/* 탭 내용 */}
      <div className="backup_edit_content">
        {selectedModalTab === 'ipv4' && 
        <>
          <div className="vnic_new_checkbox" style={{ borderBottom: '1px solid gray' }}>
              <input type="checkbox" id="allow_all_users" checked />
              <label htmlFor="allow_all_users">네트워크 동기화</label>
          </div>

          <div className='backup_edit_radiobox'>
            <div className='font-bold'>부트 프로토콜</div>
            <div className="radio_option">
              <input type="radio" id="default_mtu" name="mtu" value="default" checked />
              <label htmlFor="default_mtu">없음</label>
            </div>
            <div className="radio_option">
              <input type="radio" id="dhcp_mtu" name="mtu" value="dhcp" />
              <label htmlFor="dhcp_mtu">DHCP</label>
            </div>
            <div className="radio_option">
              <input type="radio" id="static_mtu" name="mtu" value="static" />
              <label htmlFor="static_mtu">정적</label>
            </div>

          </div>

          <div>
            <div className="vnic_new_box">
              <label htmlFor="ip_address">IP</label>
              <select id="ip_address" disabled>
                <option value="#">#</option>
              </select>
            </div>
            <div className="vnic_new_box">
              <label htmlFor="netmask">넷마스크 / 라우팅 접두사</label>
              <select id="netmask" disabled>
                <option value="#">#</option>
              </select>
            </div>
            <div className="vnic_new_box">
              <label htmlFor="gateway">게이트웨이</label>
              <select id="gateway" disabled>
                <option value="#">#</option>
              </select>
            </div>
          </div>
          </>
        }
        {selectedModalTab === 'ipv6' && 
        <>
        <div className="vnic_new_checkbox" style={{ borderBottom: '1px solid gray' }}>
            <input type="checkbox" id="allow_all_users" className='disabled' />
            <label htmlFor="allow_all_users" className='disabled'>네트워크 동기화</label>
        </div>

        <div className='backup_edit_radiobox'>
          <div className='font-bold mb-0.5'>부트 프로토콜</div>
          <div className="radio_option mb-0.5">
            <input type="radio" id="default_mtu" name="mtu" value="default" checked />
            <label htmlFor="default_mtu">없음</label>
          </div>
          <div className="radio_option mb-0.5">
            <input type="radio" id="dhcp_mtu" name="mtu" value="dhcp" />
            <label htmlFor="dhcp_mtu">DHCP</label>
          </div>
          <div className="radio_option mb-0.5">
            <input type="radio" id="slaac_mtu" name="mtu" value="slaac" />
            <label htmlFor="slaac_mtu">상태 비저장 주소 자동 설정</label>
          </div>
          <div className="radio_option mb-0.5">
            <input type="radio" id="dhcp_slaac_mtu" name="mtu" value="dhcp_slaac" />
            <label htmlFor="dhcp_slaac_mtu">DHCP 및 상태 비저장 주소 자동 설정</label>
          </div>
          <div className="radio_option mb-0.5">
            <input type="radio" id="static_mtu" name="mtu" value="static" />
            <label htmlFor="static_mtu">정적</label>
          </div>
        </div>

        <div className='mt-3'>
          <div className="vnic_new_box">
            <label htmlFor="ip_address" className='disabled'>IP</label>
            <select id="ip_address" className='disabled' disabled>
              <option value="#">#</option>
            </select>
          </div>
          <div className="vnic_new_box">
            <label htmlFor="netmask" className='disabled'>넷마스크 / 라우팅 접두사</label>
            <select id="netmask"className='disabled' disabled>
              <option value="#">#</option>
            </select>
          </div>
          <div className="vnic_new_box">
            <label htmlFor="gateway" className='disabled'>게이트웨이</label>
            <select id="gateway"className='disabled' disabled>
              <option value="#">#</option>
            </select>
          </div>
        </div>
        </>
        }
        {selectedModalTab === 'dns' && 
        <>
        <div className="vnic_new_checkbox" style={{ borderBottom: '1px solid gray' }}>
          <input type="checkbox" id="network_sync" className='disabled' />
          <label htmlFor="network_sync" className='disabled'>네트워크 동기화</label>
        </div>
        <div className="vnic_new_checkbox">
          <input type="checkbox" id="qos_override"/>
          <label htmlFor="qos_override">QoS 덮어쓰기</label>
        </div>
        <div className='p-1 font-bold'>아웃바운드</div>
        <div className="network_form_group">
          <label htmlFor="weighted_share" className='disabled'>가중 공유</label>
          <input type="text" id="weighted_share" disabled />
        </div>
        <div className="network_form_group">
          <label htmlFor="rate_limit disabled">속도 제한 [Mbps]</label>
          <input type="text" id="rate_limit" disabled />
        </div>
        <div className="network_form_group">
          <label htmlFor="commit_rate disabled">커밋 속도 [Mbps]</label>
          <input type="text" id="commit_rate" disabled/>
        </div>

        </>
        }
      </div>
    </div>

    <div className="edit_footer">
      <button style={{ display: 'none' }}></button>
      <button>OK</button>
      <button onClick={closeSecondModal}>취소</button>
    </div>
  </div>
</Modal>

         </div>

           
          </div>
          


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={onRequestClose}>취소</button>
          </div>
        </div>
      </Modal>
	);
};

export default NetworkHostModal;
