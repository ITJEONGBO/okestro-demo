import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import Modal from 'react-modal';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import NetworkDetailGeneral from '../zNotuse/NetworkDetailGeneral';
import './css/NetworkDetail.css';
import { 
  useNetworkById, 
} from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faTimes,
  faInfoCircle,
  faFileEdit,
} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';
import Path from '../Header/Path';
import NetworkVnicprofile from '../zNotuse/NetworkVnicprofile';
import NetworkCluster from '../zNotuse/NetworkCluster';
import NetworkHost from '../zNotuse/NetworkHost';
import NetworkVm from '../zNotuse/NetworkVm';
import NetworkTemplate from '../zNotuse/NetworkTemplate';
import LogicalNetworkEdit from '../Modal/LogicalNetworkEdit';
import DeleteModal from '../Modal/DeleteModal';
import NetworkNewModal from '../Modal/NetworkNewModal';

const NetworkDetail = ({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) => {
  
  const { id,section} = useParams(); 
  const navigate = useNavigate();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState('general'); 

 const handleTabClick = (tab) => {
  setActiveTab(tab);
  if (tab !== 'general') {
    navigate(`/networks/${id}/${tab}`);
  } else {
    navigate(`/networks/${id}`);
  }
};
useEffect(() => {
  if (!section) {
    setActiveTab('general');
  } else {
    setActiveTab(section);
  }
}, [section]); 


  // 테이블컴포넌트
  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const [activeButton, setActiveButton] = useState('network');
  const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리
  const [secondModalOpen, setSecondModalOpen] = useState(false); // 추가 모달 상태
  const handlePermissionFilterClick = (filter) => {
    setActivePermissionFilter(filter);

    setActivePermissionFilter(filter);
    if (filter === 'direct') {
      setIsLabelVisible(true); // 레이블 버튼을 누르면 라벨을 보이게 함
    } else {
      setIsLabelVisible(false); // 네트워크 버튼을 누르면 라벨을 숨김
    }
    }

  // 탭 상태 정의 (기본 값: 'ipv4')
  const [selectedModalTab, setSelectedModalTab] = useState('ipv4');
  // 탭 클릭 핸들러
  const handleTabModalClick = (tab) => {
    setSelectedModalTab(tab);
  };
    const handleButtonClick = (button) => {
      setActiveButton(button);
      setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
    };
    
  const [prevPath, setPrevPath] = useState(location.pathname);
  const locationState = location.state  

    


  const [shouldRefresh, setShouldRefresh] = useState(false);
  const { 
    data: network,
    status: networkStatus,
    isRefetching: isNetworkRefetching,
    refetch: networkRefetch, 
    isError: isNetworkError,
    error: networkError, 
    isLoading: isNetworkLoaindg,
  } = useNetworkById(id);
  useEffect(() => {
    networkRefetch()
  }, [setShouldRefresh, networkRefetch])
  

 
  const [activeVmFilter, setActiveVmFilter] = useState('running');
  const handleVmFilterClick = (filter) => {
    setActiveVmFilter(filter);
  };

  const toggleModal = (type, isOpen) => {
      setModals((prev) => ({ ...prev, [type]: isOpen }));
  };
  const buttons = [
    { 
      id: 'edit_btn', 
      label: '편집', 
      onClick: () => id ? toggleModal('edit', true) : alert('편집할 네트워크를 선택하세요.'), 
      disabled: !id 
    },
    { 
      id: 'delete_btn', 
      label: '삭제', 
      onClick: () => id 
        ? toggleModal('delete', true) 
        : alert('삭제할 네트워크를 선택할 수 없습니다.'), 
      disabled: !id 
    }
  ];

  
  const [activeFilter, setActiveFilter] = useState('connected'); 
  const handleFilterClick = (filter) => {
    setActiveFilter(filter);
  };

  // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);
  // 모달 열기 핸들러
  const openSecondModal = () => {
    setIsSecondModalOpen(true);
  };
  // 모달 닫기 핸들러
  const closeSecondModal = () => {
    setIsSecondModalOpen(false);
    setSelectedModalTab('ipv4'); // 모달이 닫힐 때 첫 번째 탭으로 초기화
  };
  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);

  // 추가모달
  const [isSecondModalOpen, setIsSecondModalOpen] = useState(false);
  useEffect(() => {
    if (isSecondModalOpen) {
      handleTabModalClick('ipv4');
    }
  }, [isSecondModalOpen]);

  const [modals, setModals] = useState({ edit: false, delete: false });



  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vnicProfiles', label: 'vNIC 프로파일' },
    { id: 'clusters', label: '클러스터' },
    { id: 'hosts', label: '호스트' },
    { id: 'vms', label: '가상 머신' },
    { id: 'templates', label: '템플릿' },

  ];
  const pathData = [network?.name, sections.find(section => section.id === activeTab)?.label];
  const renderSectionContent = () => {
    switch (activeTab) {
      case 'general':
        return <NetworkDetailGeneral network={network} />;
      case 'vnicProfiles':
        return <NetworkVnicprofile network={network} />;
      case 'clusters':
        return <NetworkCluster network={network} />;
      case 'hosts':
        return <NetworkHost network={network} />;
      case 'vms':
        return <NetworkVm network={network} />;
      case 'templates':
        return <NetworkTemplate network={network} />;
      default:
        return <NetworkDetailGeneral network={network} />;
    }
  };
  return (
    <div className="content_detail_section">
      <HeaderButton
        titleIcon={faFileEdit}
        title={network?.name}
        buttons={buttons}
        popupItems={[]}
      />

      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick}  
        />

      <div className="host_btn_outer">
        <Path pathElements={pathData} />
        {renderSectionContent()}


        {/* {activeTab === 'clusters' && (
        <>
            <div className="header_right_btns">
                <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
            </div>
          
            <TableOuter
              columns={TableColumnsInfo.CLUSTERS}
              data={clusters}
              onRowClick={(row, column, colIndex) => {
                const clickableCols = [0];
                if (clickableCols.includes(colIndex)) {
                    if (colIndex === 0) {
                        navigate(`/computing/clusters/${row.id}`);
                    }
                } else {
                  console.log('Selected Cluster ID:', row.id);
                }
            }}
              clickableColumnIndex={[0]}
              onContextMenuItems={() => ['1', '2', '3']}
            />


       </>
        )} */}
        
        {/* {activeTab === 'hosts' && (
        <>
            <div className="header_right_btns">
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
            </div>
            <div className="host_filter_btns">
              <button
                className={activeFilter === 'connected' ? 'active' : ''}
                onClick={() => handleFilterClick('connected')}
              >
                연결됨
              </button>
              <button
                className={activeFilter === 'disconnected' ? 'active' : ''}
                onClick={() => handleFilterClick('disconnected')}
              >
                연결 해제
              </button>
            </div>
            {activeFilter === 'connected' && (
              <TableOuter
                columns={TableColumnsInfo.HOSTS}
                data={hosts}
                onRowClick={(row, column, colIndex) => {
                  if (colIndex === 1) {
                    navigate(`/computing/hosts/${row.id}`);  // 1번 컬럼 클릭 시 이동할 경로
                  } else if (colIndex === 2) {
                    navigate(`/computing/clusters/${row.id}`);  // 2번 컬럼 클릭 시 이동할 경로
                  } else if (colIndex === 3) {
                    navigate(`/computing/datacenters/${row.id}`);  // 3번 컬럼 클릭 시 이동할 경로
                  }
                }}
                clickableColumnIndex={[1,2,3]} 
              />
            )}

            {activeFilter === 'disconnected' && (
              <TableOuter
                columns={TableColumnsInfo.HOSTS_DISCONNECTION}
                data={hosts}
                onRowClick={() => console.log('Row clicked')}
              />
            )}
       </>
        )} */}



        </div>
      </div>
      <Suspense>
        {modals.edit && (
          <NetworkNewModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={true}
            networkId={id}  // Pass the network ID to NetworkNewModal
          />
        )}
        {modals.delete && network && (
          <DeleteModal
            isOpen={modals.delete}
            type='Network'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel='네트워크'
            data={network} 
        />

        )}
      </Suspense>

      {/*header 편집버튼 팝업 */}
      <Modal
                isOpen={activePopup === 'edit_popup'}
                onRequestClose={closePopup}
                contentLabel="편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_edit_popup">
                    <div className="popup_header">
                        <h1>논리 네트워크 수정</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>
                    
                    <form id="network_new_common_form">
                    <div className="network_first_contents">
                                <div className="network_form_group">
                                    <label htmlFor="cluster">데이터 센터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <div  className='checkbox_group'>
                                        <label htmlFor="name">이름</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }}fixedWidth/>
                                    </div>
                                    <input type="text" id="name" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="comment">코멘트</label>
                                    <input type="text" id="comment" />
                                </div>
                            </div>

                            <div className="network_second_contents">
                                
                                <div className="network_checkbox_type1">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                                        <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                                    </div>
                                    <input type="text" id="valn_tagging_input" disabled />
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" checked/>
                                    <label htmlFor="vm_network">가상 머신 네트워크</label>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                                    <label htmlFor="photo_separation">포토 분리</label>
                                </div>
                                <div className="network_radio_group">
                                    <div style={{ marginTop: '0.2rem' }}>MTU</div>
                                    <div>
                                        <div className="radio_option">
                                            <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                                            <label htmlFor="default_mtu">기본값 (1500)</label>
                                        </div>
                                        <div className="radio_option">
                                            <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                                            <label htmlFor="user_defined_mtu">사용자 정의</label>
                                        </div>
                                    </div>
                                   
                                </div>
                               
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DNS서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                              
                            </div>
                        </form>
                   

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
      </Modal>
 



 

      {/*호스트(호스트 네트워크 설정 Before)*/}
      {/* <Modal
        isOpen={activePopup === 'host_network_popup'}
        onRequestClose={closePopup}
        contentLabel="호스트 네트워크 설정"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="popup_header">
            <h1>호스트 host01.ititinfo.com 네트워크 설정</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
              <FontAwesomeIcon icon={faPencilAlt} className="icon" />
            </div>
          </div>
        </div>
      </div>

      <div className="network_separation_right">
      <div className="network_filter_btns">
  <button
    className={`btn ${activeButton === 'network' ? 'bg-gray-200' : ''}`}
    onClick={() => handleButtonClick('network')}
  >
    네트워크
  </button>
  <button
    className={`btn border-l border-gray-800 ${activeButton === 'label' ? 'bg-gray-200' : ''}`}
    onClick={() => handleButtonClick('label')}
  >
    레이블
  </button>
</div>

  {/* unconfigured_network는 네트워크 버튼이 클릭된 경우만 보임 */}
  {/* {!isLabelVisible && (
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
      <div>
        <span>외부 논리적 네트워크</span>
        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }} fixedWidth />
      </div>
    </div>
  )} */}

  {/* lable_part는 레이블 버튼이 클릭된 경우만 보임 */}
  {/* {isLabelVisible && (

      <div class="lable_part">
        <FontAwesomeIcon icon={faTag} style={{ color: 'orange', marginRight: '0.2rem' }} />
        <span>[새 레이블]</span>
      </div>

  )}
</div>


         </div>

            <div className="border-t-[1px] border-gray-500 mt-4">
                <div className='py-1 checkbox_group'>
                  <input type="checkbox" id="checkHostConnection" checked />
                  <label htmlFor="checkHostConnection">호스트와 Engine간의 연결을 확인</label>
                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)', cursor: 'pointer' }} fixedWidth />
                </div>
                <div className='checkbox_group'>
                  <input type="checkbox" id="saveNetworkConfig" disabled />
                  <label htmlFor="saveNetworkConfig">네트워크 설정 저장</label>
                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)', cursor: 'pointer' }} fixedWidth />
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
   

      <Footer/>
    </div>
  );
}

export default NetworkDetail;
