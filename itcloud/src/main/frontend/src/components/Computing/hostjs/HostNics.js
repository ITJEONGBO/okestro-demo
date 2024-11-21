import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowsAltH, faBan, faCaretDown, faCheck, faCircle, faDesktop, faExclamationTriangle, faFan, faInfoCircle, faNetworkWired, faPencilAlt, faTag, faTimes, faUniversity, faWrench } from "@fortawesome/free-solid-svg-icons";


const HostNics = ({ hostId }) => {

  // 네트워크인터페이스 박스열고닫기
  const [visibleBoxes, setVisibleBoxes] = useState([]);

  const toggleHiddenBox = (index) => {
    setVisibleBoxes((prevVisibleBoxes) => {
      if (prevVisibleBoxes.includes(index)) {
        return prevVisibleBoxes.filter((i) => i !== index); // 이미 열려 있으면 닫기
      } else {
        return [...prevVisibleBoxes, index]; // 아니면 열기
      }
    });
  };

  // 모든 박스를 확장 또는 숨기기
  const toggleAllBoxes = () => {
    if (visibleBoxes.length === networkInterfaceData.length) {
      setVisibleBoxes([]); // 모두 닫기
    } else {
      setVisibleBoxes(networkInterfaceData.map((_, index) => index)); // 모두 열기
    }
  };

  // 네트워크(임시데이터)
  const networkInterfaceData = [
[]
  ];
  const networkdata = [
 []
  ];
  console.log('networkInterfaceData:', networkInterfaceData);
  console.log('networkdata:', networkdata);
  // 연필 추가모달
  const [isSecondModalOpen, setIsSecondModalOpen] = useState(false);
  useEffect(() => {
    if (isSecondModalOpen) {
      handleTabModalClick('ipv4');
    }
  }, [isSecondModalOpen]);
   const [selectedModalTab, setSelectedModalTab] = useState('ipv4');
   const handleTabModalClick = (tab) => {
     setSelectedModalTab(tab);
   };
  // 추가 모달 닫기 핸들러
  const closeSecondModal = () => {
    setIsSecondModalOpen(false);
    setSelectedModalTab('ipv4'); // 모달이 닫힐 때 첫 번째 탭으로 초기화
  };

  

  const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
  const [activePopup, setActivePopup] = useState(null);
      // 모달 관련 상태 및 함수
      const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedTab('network_new_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };
    const closePopup = () => setActivePopup(null);
    const [activeButton, setActiveButton] = useState('network');
    const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리
    const handleButtonClick = (button) => {
        setActiveButton(button);
        setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
      };
    return (
       
          <>
                    <div className="header_right_btns">
                      <button>VF 보기</button>
                      <button onClick={toggleAllBoxes}>
                        {visibleBoxes.length === networkInterfaceData.length ? '모두 숨기기' : '모두 확장'}
                      </button>
                      <button onClick={() => openPopup('host_network_set')}>호스트 네트워크 설정</button>
                      <button className="disabled">네트워크 설정 저장</button>
                      <button className="disabled">모든 네트워크 동기화</button>
                    </div>
              
                    <div>테이블넣기(오류남)</div>

                

  {/*호스트(호스트 네트워크 설정)*/}
  <Modal
              isOpen={activePopup === 'host_network_set'}
              onRequestClose={closePopup}
              contentLabel="호스트 네트워크 설정"
              className="Modal"
              overlayClassName="Overlay"
              shouldCloseOnOverlayClick={false}
            >
              <div className="host_network_content_popup">
                <div className="popup_header">
                  <h1> 네트워크 설정</h1>
                  <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className="host_network_outer px-1.5 pt-1.5 text-sm">
                

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


        {/* unconfigured_network는 네트워크 버튼이 클릭된 경우만 보임 */}
        {!isLabelVisible && (
          <div className="unconfigured_network">
            <div>할당되지 않은 논리 네트워크</div>
            <div style={{ backgroundColor: '#d1d1d1' }}>필수</div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div style={{ backgroundColor: '#d1d1d1' }}>필요하지 않음</div>
            
          </div>
        )}


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
                  <button onClick={closePopup}>취소</button>
                </div>
              </div>
            </Modal>

            </>
    
    );
  };
  
  export default HostNics;