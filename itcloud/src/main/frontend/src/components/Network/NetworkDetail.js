import React, { useState, useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import Modal from 'react-modal';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import NetworkDetailGeneral from './NetworkDetailGeneral';
import './css/NetworkDetail.css';
import Permission from '../Modal/Permission';
import { 
  useNetworkById, 
  useAllVnicProfilesFromNetwork, 
  useAllClustersFromNetwork, 
  useAllHostsFromNetwork, 
  useAllVmsFromNetwork, 
  useAllTemplatesFromNetwork, 
  useAllPermissionsFromNetwork 
} from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faChevronLeft, faUser, faTimes,
  faCircle,
  faDesktop,
  faArrowsAltH,
  faCheck,
  faFan,
  faBan,
  faExclamationTriangle,
  faPencilAlt,
  faInfoCircle,
  faCaretDown,
  faNetworkWired,
  faTag
} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';

const NetworkDetail = ({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) => {
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
    };

    const handleButtonClick = (button) => {
      setActiveButton(button);
      setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
    };
    
  
  const location = useLocation();
  const locationState = location.state  
  const { id } = useParams(); // useParams로 URL에서 name을 가져옴
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
  
  const { 
    data: vnicProfiles,
    status: vnicProfilesStatus,
    isRefetching: isvnicProfilesRefetching,
    refetch: vnicProfilesRefetch,
    isError, 
    error, 
    isLoading
  } = useAllVnicProfilesFromNetwork(network?.id, toTableItemPredicateVnicProfiles)
  useEffect(() => {
    vnicProfilesRefetch()
  }, [setShouldRefresh, vnicProfilesRefetch])

  const { 
    data: clusters, 
    status: clustersStatus, 
    isLoading: isClustersLoading, 
    isError: isClustersError 
  } = useAllClustersFromNetwork(network?.id, toTableItemPredicateClusters);
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useAllHostsFromNetwork(network?.id, toTableItemPredicateHosts);  
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useAllVmsFromNetwork(network?.id, toTableItemPredicateVms);
  const { 
    data: templates, 
    status: templatesStatus, 
    isLoading: isTemplatesLoading, 
    isError: isTemplatesError 
  } = useAllTemplatesFromNetwork(network?.id, toTableItemPredicateTemplates);
  const { 
    data: permissions, 
    status: permissionsStatus, 
    isLoading: isPermissionsLoading, 
    isError: isPermissionsError 
  } = useAllPermissionsFromNetwork(network?.id, toTableItemPredicatePermissions);


  // 함수들
  function toTableItemPredicateVnicProfiles(e) {
    return {
        id: e?.id ?? '없음',
        name: e?.name ?? '없음',
        description: (e?.description == '') ? '없음' : (e?.description ?? '없음'),
        network: e?.networkVo?.name ?? '없음',
        /*    
        networkId: e?.networkVo?.id ?? '없음',
        networkName: e?.networkVo?.name ?? '없음',
        */
        dataCenter: e?.dataCenterVo?.name ?? '없음',
        /*
        dataCenterId: e?.dataCenterVo?.id ?? '없음',
        dataCenterName: e?.dataCenterVo?.name ?? '없음',
        */
        networkFilter: e?.networkFilterVo?.name ?? '없음',
        /*
        networkFilterId: e?.networkFilterVo?.id ?? '없음',
        networkFilterName: e?.networkFilterVo?.name ?? '없음',
        */
        passthrough: e?.passThrough ?? '없음',
    }
  }
  function toTableItemPredicateClusters(cluster) {
    return {
      id: cluster?.id ?? '없음',
      name: cluster?.name ?? '없음',
      description: cluster?.description ?? '없음',
      version: cluster?.version ?? '없음',
      connectedNetwork: cluster?.connected ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkStatus: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,
      requiredNetwork: cluster?.requiredNetwork ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkRole: cluster?.networkRole ?? '',
    };
  }

  function toTableItemPredicateHosts(host) {
    return {
      id: host?.id ?? '',               
      name: host?.name ?? 'Unknown',           
      cluster: host?.clusterVo?.name ?? 'N/A',
      dataCenter: host?.dataCenterVo?.name ?? 'N/A',
      networkDeviceStatus: host?.hostNicVos?.[0]?.status ?? 'Unknown', 
      networkDevice: host?.hostNicVos?.[0]?.name ?? 'N/A', 
      speed: host?.hostNicVos?.[0]?.speed ?? 'N/A', 
      rx: host?.hostNicVos?.[0]?.rxSpeed ?? 'N/A', 
      tx: host?.hostNicVos?.[0]?.txSpeed ?? 'N/A', 
      totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed ?? 'N/A', 
      totalTx: host?.hostNicVos?.[0]?.txTotalSpeed ?? 'N/A', 
    };
  }
  function toTableItemPredicateVms(vm) {
    return {
      id: vm?.id ?? '없음',  // 가상 머신 ID
      icon: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,  // 가상 머신 아이콘
      name: vm?.name ?? '없음',  // 가상 머신 이름
      cluster: vm?.cluster ?? '없음',  // 클러스터 이름
      ipAddress: vm?.ipAddress ?? '없음',  // IP 주소
      fqdn:  vm?.fqdn ?? '',
      vnicStatus: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,  // vNIC 상태 아이콘
      vnic: vm?.vnic ?? '없음',  // vNIC 이름
      vnicRx: vm?.vnicRx ?? '없음',  // vNIC 수신 속도
      vnicTx: vm?.vnicTx ?? '없음',  // vNIC 송신 속도
      totalRx: vm?.totalRx ?? '없음',  // 총 수신 데이터
      totalTx: vm?.totalTx ?? '없음',  // 총 송신 데이터
      description: vm?.description ?? '없음'  // 가상 머신 설명
    };
  }
  function toTableItemPredicateTemplates(template) {
    return {
      name: template?.name ?? '없음',  // 템플릿 이름
      nicId: template?.nicId ?? '없음',  // 템플릿 버전
      status: template?.status ?? '없음',  // 템플릿 상태
      clusterName: template?.clusterName ?? '없음',  // 클러스터 이름
      nicName: template?.nicName ?? '없음',  // vNIC 이름
    };
  }
  function toTableItemPredicatePermissions(permission) {
    return {
      icon: <FontAwesomeIcon icon={faUser} fixedWidth/>,  // 사용자 아이콘
      user: permission?.user ?? '없음',  // 사용자 이름
      provider: permission?.provider ?? '없음',  // 인증 제공자
      nameSpace: permission?.nameSpace ?? '없음',  // 네임스페이스
      role: permission?.role ?? '없음',  // 역할
      createDate: permission?.createDate ?? '없음',  // 생성 날짜
      inheritedFrom: permission?.inheritedFrom ?? '없음',  // 상속된 위치
    };
  }
  // 클러스터 팝업(보류)
  const clusterPopupData = [
    {
      id: id,
      name: 'Default',
      allAssigned: (
        <>
          <input type="checkbox" checked /> <label>할당</label>
        </>
      ),
      allRequired: (
        <>
          <input type="checkbox" checked/> <label>필요</label>
        </>
      ),
      vmNetMgmt: (
        <>
          <i class="fa-solid fa-star" style={{ color: 'green'}}fixedWidth/>
        </>
      ),
      networkOutput: <input type="checkbox" />,
      migrationNetwork: <input type="checkbox"/>,
      glusterNetwork: <input type="checkbox"/>,
      defaultRouting: <input type="checkbox"/>,
    },
  ];
  

  // 
  const [activeTab, setActiveTab] = useState('general');
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  //headerbutton 컴포넌트
  const buttons = [
    { 
      id: 'edit_btn', 
      label: '편집', 
      onClick: () => {
        openPopup('edit_popup');
        console.log('Edit button clicked');
      }
    },
    { 
      id: 'delete_btn', 
      label: '삭제', 
      onClick: () => console.log('Delete button clicked') 
    },
  ];
  


  // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);


  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vNIC_profile', label: 'vNIC 프로파일' },
    { id: 'cluster', label: '클러스터' },
    { id: 'host', label: '호스트' },
    { id: 'virtual_machine', label: '가상 머신' },
    { id: 'template', label: '템플릿' },
    { id: 'permission', label: '권한' },
  ];

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="네트워크"
        subtitle={locationState?.name} // 여기서도 네트워크 이름을 표시
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
        {
          activeTab === 'general' && <NetworkDetailGeneral network={network} />
        }
        {
          activeTab === 'vNIC_profile' && (
        <>
          <div className="content_header_right">
              <button onClick={() => openPopup('vnic_new_popup')}>새로 만들기</button>
              <button onClick={() => openPopup('vnic_eidt_popup')}>편집</button>
              <button>제거</button>
          </div>
          {/* vNIC 프로파일 */}
          <TableOuter
            columns={TableColumnsInfo.VNIC_PROFILES} 
            data={vnicProfiles}
            onRowClick={() => console.log('Row clicked')} 
          />
       </>
        )}

        {activeTab === 'cluster' && (
        <>
            <div className="content_header_right">
                <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
            </div>
          
            <TableOuter
              columns={TableColumnsInfo.CLUSTERS} 
              data={clusters} 
              onRowClick={() => console.log('Row clicked')}
            />
       </>
       
        )}
        
        {activeTab === 'host' && (
        <>
            <div className="content_header_right">
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
            </div>
            <div className="host_filter_btns">
              <button
                 
              >
                  연결됨
              </button>
              <button
                  
              >
                  연결 해제
              </button>
            </div>
            <TableOuter 
              columns={TableColumnsInfo.HOSTS}
              data={hosts}
              onRowClick={() => console.log('Row clicked')} 
            />
       </>
       
        )}

        {activeTab === 'virtual_machine' && (
        <>
              <div className="content_header_right">
                  <button>제거</button>
              </div>
              <div className="host_filter_btns">
                <button
              
                >
                    실행중
                </button>
                <button
                
                >
                    정지중
                </button>
            </div>
            <TableOuter 
              columns={TableColumnsInfo.VMS}
              data={vms}
              onRowClick={() => console.log('Row clicked')}
            />
       </>
        )}

        {activeTab === 'template' && (
        <>
            <div className="content_header_right">
                <button>제거</button>
            </div>

            <TableOuter 
              columns={TableColumnsInfo.TEMPLATES}
              data={templates} 
              onRowClick={() => console.log('Row clicked')} 
            />
        </>
        )}

        {activeTab === 'permission' && (

        <>
              <div className="content_header_right">
                <button onClick={() => openPopup('permission')}>추가</button>
                <button>제거</button>
              </div>
              
     
                <div className="host_filter_btns">
                  <span>Permission Filters:</span>
                  <div>
                    <button
                      className={activePermissionFilter === 'all' ? 'active' : ''}
                      onClick={() => handlePermissionFilterClick('all')}
                    >
                      All
                    </button>
                    <button
                      className={activePermissionFilter === 'direct' ? 'active' : ''}
                      onClick={() => handlePermissionFilterClick('direct')}
                    >
                      Direct
                    </button>
                  </div>
                </div>
              
              <TableOuter
                columns={TableColumnsInfo.PERMISSIONS}
                data={permissions}
                onRowClick={() => console.log('Row clicked')}
              />
          </>
        )}

        </div>
      </div>
      

      {/*header 편집버튼 팝업 */}
      <Modal
        isOpen={activePopup === 'edit_popup'}
        onRequestClose={closePopup}
        contentLabel="편집"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="network_detail_edit_popup">
            <div className="popup_header">
                <h1>논리 네트워크 수정</h1>
                <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            </div>

            <form id="network_detail_edit_form">
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
                                <span>네트워크 매개변수</span>
                                <div className="network_form_group">
                                    <label htmlFor="network_label">네트워크 레이블</label>
                                    <input type="text" id="network_label" />
                                </div>
                                <div className="network_checkbox_type1">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                                        <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                                    </div>
                                    <input type="text" id="valn_tagging_input" disabled />
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
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
                                <div className="network_form_group">
                                    <label htmlFor="host_network_qos">호스트 네트워크 QoS</label>
                                    <select id="host_network_qos">
                                        <option value="default">[제한없음]</option>
                                    </select>
                               </div>
                                <div className='popup_plus_btn'>
                                    <div className="popup_plus" onClick={() => setSecondModalOpen(true)}>새로만들기</div>
                                </div>
                                
                                    <Modal
                                        isOpen={secondModalOpen}
                                        onRequestClose={() => setSecondModalOpen(false)}
                                        contentLabel="추가 모달"
                                        className="SecondModal"
                                        overlayClassName="Overlay"
                                    >
                                                            
                                    <div className="plus_popup_outer">
                                        <div className="popup_header">
                                            <h1>새 호스트 네트워크 Qos</h1>
                                            <button  onClick={() => setSecondModalOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                                        </div>
                                        
                                        <div className='p-1' style={{ borderBottom: '1px solid #d3d3d3' }}>
                                            <div className="network_form_group">
                                                <label htmlFor="network_provider">네트워크 공급자</label>
                                                <select id="network_provider">
                                                <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                                                </select>
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="qos_name">QoS 이름</label>
                                                <input type="text" id="qos_name" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="description">설명</label>
                                                <input type="text" id="description" />
                                            </div>
                                            </div>

                                            <div className='p-1'>
                                            <span className="network_form_group font-bold">아웃바운드</span>
                                            <div className="network_form_group">
                                                <label htmlFor="weighted_share">가중 공유</label>
                                                <input type="text" id="weighted_share" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="speed_limit">속도 제한 [Mbps]</label>
                                                <input type="text" id="speed_limit" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="commit_rate">커밋 속도 [Mbps]</label>
                                                <input type="text" id="commit_rate" />
                                            </div>
                                        </div>


                                        <div className="edit_footer">
                                            <button style={{ display: 'none' }}></button>
                                            <button>가져오기</button>
                                            <button onClick={() => setSecondModalOpen(false)}>취소</button>
                                        </div>
                                    </div>
                                     
                                    </Modal>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DB서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation" />
                                    <label htmlFor="external_vendor_creation">외부 업체에서 작성</label>
                                </div>
                                <span>외부</span>
                                <div className="network_form_group" style={{ paddingTop: 0 }}>
                                    <label htmlFor="external_provider">외부 공급자</label>
                                    <select id="external_provider">
                                        <option value="default">ovirt-provider-ovn</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="network_port_security">네트워크 포트 보안</label>
                                    <select id="network_port_security">
                                        <option value="default">활성화</option>
                                    </select>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network" />
                                    <label htmlFor="connect_to_physical_network">물리적 네트워크에 연결</label>
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
      {/*vNIC 프로파일(새로만들기)팝업 */}
      <Modal
        isOpen={activePopup === 'vnic_new_popup'}
        onRequestClose={closePopup}
        contentLabel="새로만들기"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
                </div>
              </div>

              <div className="vnic_new_checkbox">
                <input type="checkbox" id="allow_all_users" checked />
                <label htmlFor="allow_all_users">모든 사용자가 이 프로파일을 사용하도록 허용</label>
              </div>

            </div>
              
            
              
            
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
       {/*vNIC 프로파일(편집)팝업 */}
       <Modal
        isOpen={activePopup === 'vnic_eidt_popup'}
        onRequestClose={closePopup}
        contentLabel="편집"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
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
      </Modal>
       {/*클러스터(네트워크 관리)팝업*/}
       <Modal
        isOpen={activePopup === 'cluster_network_popup'}
        onRequestClose={closePopup}
        contentLabel="네트워크 관리"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="manage_network_popup">
          <div className="popup_header">
            <h1>네트워크 관리</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          
          <TableOuter 
            columns={TableColumnsInfo.CLUSTERS_POPUP} 
            data={clusterPopupData} 
            onRowClick={() => console.log('Row clicked')} 
          />
          
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      {/*호스트(호스트 네트워크 설정)*/}
      <Modal
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
      <div>
        <span>외부 논리적 네트워크</span>
        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }} fixedWidth />
      </div>
    </div>
  )}

  {/* lable_part는 레이블 버튼이 클릭된 경우만 보임 */}
  {isLabelVisible && (

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
      </Modal>
      <Footer/>

       {/* 모달 컴포넌트 */}
       <Permission isOpen={activePopup === 'permission'} onRequestClose={closePopup} />
    </div>
  );
}

export default NetworkDetail;
