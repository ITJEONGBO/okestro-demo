import React, { useState,useEffect } from 'react';
import {useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Modal from 'react-modal';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import AffinityGroupModal from '../Modal/AffinityGroupModal';
import NetworkDetail from '../Network/NetworkDetail';
import Permission from '../Modal/Permission';
import { useClusterById, useEventFromCluster, useHostFromCluster, useLogicalFromCluster, usePermissionFromCluster, usePermissionromCluster, useVMFromCluster } from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faCrown, faUser, faBan,
  faTimes,
  faInfoCircle,
  faSearch,
  faRefresh,
  faEllipsisV,
  faCaretUp,
  faGlassWhiskey,
  faChevronCircleRight
} from '@fortawesome/free-solid-svg-icons'
import './css/ClusterName.css';
import TableOuter from '../table/TableOuter';

function RutilManager() {
    const { id } = useParams();
 
    const navigate = useNavigate();
    const location = useLocation();
    const locationState = location.state; 
    const [shouldRefresh, setShouldRefresh] = useState(false);
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);
    const [activePopup, setActivePopup] = useState(null);
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
    const [secondModalOpen, setSecondModalOpen] = useState(false); // 추가 모달 상태
    const [storageType, setStorageType] = useState('NFS'); // 기본값은 NFS로 설정
    // 스토리지 유형 변경 핸들러
  const handleStorageTypeChange = (e) => {
    setStorageType(e.target.value); // 선택된 옵션의 값을 상태로 저장
  };
    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedPopupTab('cluster_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };

    const closePopup = () => {
        setActivePopup(null);
    };
    const handleTabClickModal = (tab) => {
        setSelectedTab(tab);
    };
    const handlePermissionFilterClick = (filter) => {
        setActivePermissionFilter(filter);
      };
      const [activePermissionFilter, setActivePermissionFilter] = useState('all');
      const handleRowClick = (row, column) => {
        if (column.accessor === 'name') {
          navigate(`/networks/${row.name.props.children}`);  
        }
    };
    const [isPermissionModalOpen, setIsPermissionModalOpen] = useState(false); // 권한 모달 상태
    const [isAffinityGroupModalOpen, setIsAffinityGroupModalOpen] = useState(false); // 선호도 그룹 모달 상태
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    // 버튼 클릭 시 팝업의 열림/닫힘 상태를 토글하는 함수
    const togglePopup = () => {
      setIsPopupOpen(!isPopupOpen);
    };
    const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
    const toggleDomainHiddenBox = () => {
      setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
    };
    const [isDomainHiddenBox2Visible, setDomainHiddenBox2Visible] = useState(false);
    const toggleDomainHiddenBox2 = () => {
      setDomainHiddenBox2Visible(!isDomainHiddenBox2Visible);
    };
   
    const [isDomainHiddenBox4Visible, setDomainHiddenBox4Visible] = useState(false);
    const toggleDomainHiddenBox4 = () => {
      setDomainHiddenBox4Visible(!isDomainHiddenBox4Visible);
    };
    const [isDomainHiddenBox5Visible, setDomainHiddenBox5Visible] = useState(false);
    const toggleDomainHiddenBox5 = () => {
      setDomainHiddenBox5Visible(!isDomainHiddenBox5Visible);
    };
    const [activeLunTab, setActiveLunTab] = useState('target_lun'); 
  const handleLunTabClick = (tab) => {
    setActiveLunTab(tab); 
  };
    // 권한 모달 핸들러
    const openPermissionModal = () => setIsPermissionModalOpen(true);
    const closePermissionModal = () => setIsPermissionModalOpen(false);
    // 기존의 openPopup 함수 수정


    // 선호도 그룹 모달 핸들러
    const openAffinityGroupModal = () => setIsAffinityGroupModalOpen(true);
    const closeAffinityGroupModal = () => setIsAffinityGroupModalOpen(false);
    const [showTooltip, setShowTooltip] = useState(false); // hover하면 설명창 뜨게하기
    const { 
        data: cluster,
        status: networkStatus,
        isRefetching: isNetworkRefetching,
        refetch: clusterRefetch, 
        isError: isNetworkError,
        error: networkError, 
        isLoading: isNetworkLoading,
      } = useClusterById(id);
      
      useEffect(() => {
        clusterRefetch();  // 함수 이름을 일치시킴
      }, [setShouldRefresh, clusterRefetch]);

    // 논리네트워크
    const { 
      data: networks, 
      status: networksStatus, 
      isLoading: isNetworksLoading, 
      isError: isNetworksError 
    } = useLogicalFromCluster(cluster?.id, (network) => {
    return {
        name: network?.name ?? 'Unknown',            
        status: network?.status ?? 'Unknown',       
        role: network?.role ? <FontAwesomeIcon icon={faCrown} fixedWidth/> : '', 
        description: network?.description ?? 'No description', 
      };
    });
      

    // 호스트
    const { 
        data: hosts, 
        status: hostsStatus, 
        isLoading: isHostsLoading, 
        isError: isHostsError 
      } = useHostFromCluster(cluster?.id, toTableItemPredicateHosts);
      
      function toTableItemPredicateHosts(host) {
        return {
          icon: '', 
          name: host?.name ?? 'Unknown',  // 호스트 이름, 없으면 'Unknown'
          hostNameIP: host?.name ?? 'Unknown',
          status: host?.status ?? 'Unknown',  
          loading: `${host?.vmCount ?? 0} 대의 가상머신`, // 0으로 기본값 설정
          displayAddress: host?.displayAddress ?? '아니요',
        };
      }
    // 가상머신
    const { 
        data: vms, 
        status: vmsStatus, 
        isLoading: isVmsLoading, 
        isError: isVmsError 
      } = useVMFromCluster(cluster?.id, toTableItemPredicateVms);
      
      function toTableItemPredicateVms(vm) {
        const statusIcon = vm?.status === 'DOWN' 
            ? <i class="fa-solid fa-chevron-down text-red-500" fixedWidth/>
            : vm?.status === 'UP' || vm?.status === '실행 중'
            ? <i class="fa-solid fa-chevron-up text-green-500" fixedWidth/>
            : ''; // 기본값
        return {
          icon: statusIcon,      
          name: vm?.name ?? 'Unknown',               
          status: vm?.status ?? 'Unknown',           
          upTime: vm?.upTime ?? '',             
          cpu: vm?.cpu ?? '',                    
          memory: vm?.memory ?? '',              
          network: vm?.network ?? '',             
          ipv4: vm?.ipv4 ?? '',         
        };
      }
        // 도메인 테이블 컴포넌트 
  const domaindata = [
    {
      status: <FontAwesomeIcon icon={faCaretUp} style={{ color: '#1DED00' }}fixedWidth/>,
      icon: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      domainName: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          ㅇㄻㄹㅇㄻ
        </span>
      ),
      comment: '',
      domainType: '',
      storageType: '',
      format: '',
      dataCenterStatus: '',
      totalSpace: '',
      freeSpace: '',
      reservedSpace: '',
      description: '',
    },
  ];
    // 권한
    const { 
        data: permissions, 
        status: permissionsStatus, 
        isLoading: isPermissionsLoading, 
        isError: isPermissionsError 
      } = usePermissionFromCluster(cluster?.id, toTableItemPredicatePermissions);

      function toTableItemPredicatePermissions(permission) {
        return {
          icon: <FontAwesomeIcon icon={faUser} fixedWidth/>, 
          user: permission?.user ?? '없음',  
          provider: permission?.provider ?? '없음',  
          nameSpace: permission?.nameSpace ?? '없음', 
          role: permission?.role ?? '없음',  
          createDate: permission?.createDate ?? '없음',  
          inheritedFrom: permission?.inheritedFrom ?? '없음', 
        };
      }
    // 이벤트
    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useEventFromCluster(cluster?.id, toTableItemPredicateEvents);

    function toTableItemPredicateEvents(event) {
        return {
          icon: '',                      
          time: event?.time ?? '',                
          description: event?.description ?? 'No message', 
          correlationId: event?.correlationId ?? '',
          source: event?.source ?? 'ovirt',     
          userEventId: event?.userEventId ?? '',   
        };
      }

      const data = [
        {
          alias: (
            <span
              style={{ color: 'blue', cursor: 'pointer' }}
              onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
              onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
            >
              he_metadata
            </span>
          ),
          id: '289137398279301798',
          icon1: '',
          icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
          connectionTarget: 'on20-ap01',
          storageDomain: 'VirtIO-SCSI',
          virtualSize: '/dev/sda',
          status: 'OK',
          type: '이미지',
          description: '',
        },
        {
          alias: (
            <span
              style={{ color: 'blue', cursor: 'pointer'}}
              onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
              onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
            >
              디스크2
            </span>
          ),
          id: '289137398279301798',
          icon1: '',
          icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
          connectionTarget: 'on20-ap01',
          storageDomain: 'VirtIO-SCSI',
          virtualSize: '/dev/sda',
          status: 'OK',
          type: '이미지',
          description: '',
        },
        {
          alias: (
            <span
              style={{ color: 'blue', cursor: 'pointer'}}
              onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
              onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
            >
              디스크3
            </span>
          ),
          id: '289137398279301798',
          icon1: '',
          icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
          connectionTarget: 'on20-ap01',
          storageDomain: 'VirtIO-SCSI',
          virtualSize: '/dev/sda',
          status: 'OK',
          type: '이미지',
          description: '',
        },
      ];

    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        setShowNetworkDetail(false); // 탭이 변경되면 NetworkDetail 화면을 숨김
    };

    // HeaderButton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick:() => openPopup('cluster_detail_edit') },
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
    ];


    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'data_center', label: '데이터센터' },
        { id: 'cluster', label: '클러스터' },
        { id: 'host', label: '호스트' },
        { id: 'virtual_machine', label: '가상머신' },
        { id: 'storage', label: '스토리지' },
        { id: 'network', label: '네트워크' },
    ];
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

    return (
        <div id='section'>
            {showNetworkDetail ? (
                <NetworkDetail />
            ) : (
                <>
                    <HeaderButton
                        title="Rutil Manager"
                        subtitle={locationState?.name}
                        additionalText="목록이름"
                        buttons={buttons}
                        popupItems={[]}
                        uploadOptions={[]}
                    />
    
                    <div className="content_outer">
                        <NavButton
                            sections={sections}
                            activeSection={activeTab}
                            handleSectionClick={handleTabClick}
                        />
                        <div className="host_btn_outer">
                            {/* 일반 */}
                            {activeTab === 'general' && (
                                <div>
                                    
                                </div>
                            )}
                            {/* 일반(삭제예정) */}
                            {/* {activeTab === 'general' && (
                                <div className="cluster_general">
                                    <div className="table_container_center">
                                        <table className="table">
                                            <tbody>
                                                <tr>
                                                    <th>ID:</th>
                                                    <td>{id}</td>
                                                </tr>
                                                <tr>
                                                    <th>설명:</th>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <th>데이터센터:</th>
                                                    <td>Default</td>
                                                </tr>
                                                <tr>
                                                    <th>호환버전:</th>
                                                    <td>4.7</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 노드 유형:</th>
                                                    <td>Virt</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 ID:</th>
                                                    <td>f0adf4f6-274b-4533-b6b3-6a683b062c9a</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 CPU 유형:</th>
                                                    <td>
                                                         Intel Nehalem Family
                                                        <FontAwesomeIcon icon={faBan} style={{ marginLeft: '13%', color: 'orange' }} fixedWidth/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>스레드를 CPU 로 사용:</th>
                                                    <td>아니요</td>
                                                </tr>
                                                <tr>
                                                    <th>최대 메모리 오버 커밋:</th>
                                                    <td>100%</td>
                                                </tr>
                                                <tr>
                                                    <th>복구 정책:</th>
                                                    <td>예</td>
                                                </tr>
                                                <tr>
                                                    <th>칩셋/펌웨어 유형:</th>
                                                    <td>UEFI의 Q35 칩셋</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div className="table_container_center">
                                        <table className="table">
                                            <tbody>
                                                <tr>
                                                    <th>에뮬레이션된 시스템:</th>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <th>가상 머신 수:</th>
                                                    <td>0</td>
                                                </tr>
                                                <tr>
                                                    <th>총 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                                <tr>
                                                    <th>Up 상태의 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                                <tr>
                                                    <th>Down 상태의 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            )} */}
                            {/*데이터 센터 */}
                            {activeTab === 'data_center' && (
                                <>
                                <div className="content_header_right">
                                    <button onClick={() => openPopup('newNetwork')}>네트워크 추가</button>
                                    <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
                                    <button>디스플레이로 설정</button>
                                    <button>모든 네트워크 동기화</button>
                                </div>
                                <div className="search_box">
                                    <input type="text" />
                                    <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                                    <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
                                </div>
                                <TableOuter
                                  columns={TableColumnsInfo.DATACENTERS} 
                                  data={networks} 
                                  onRowClick={handleRowClick} />
                                </>

                            )}
                            {/* 클러스터 */}
                            {activeTab === 'cluster' && (
                              <>
                               <div className="content_header_right">
                                    <button onClick={() => openPopup('newNetwork')}>새로만들기</button>
                                    <button onClick={() => openPopup('cluster_network_popup')}>편집</button>
                                    <button>업그레이드</button>
                                </div>
                                <div className="search_box">
                                    <input type="text" />
                                    <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                                    <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
                                </div>
                                <TableOuter 
                                  columns={TableColumnsInfo.CLUSTERS_DATA} 
                                  data={vms} 
                                  onRowClick={() => console.log('Row clicked')}
                                />
                             
                              </>
                            )}
                            {/* 호스트 */}
                            {activeTab === 'host' && (
                                <>
                                <div className="content_header_right">
                                    <button onClick={() => openPopup('host_new')}>새로 만들기</button>
                                </div>
                                <div className="search_box">
                                    <input type="text" />
                                    <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                                    <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
                                </div>
                                  <TableOuter 
                                    columns={TableColumnsInfo.HOSTS_ALL_DATA} 
                                    data={hosts}
                                    onRowClick={() => console.log('Row clicked')} 
                                  />
                                
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                              <>
                              <div className="content_header_right">
                                    <button onClick={() => openPopup('newNetwork')}>새로만들기</button>
                                    <button onClick={() => openPopup('cluster_network_popup')}>편집</button>
                                    <button>실행</button>
                                    <button>일시중지</button>
                                    <button>종료</button>
                                    <button>재부팅</button>
                                    <button>콘솔</button>
                                    <button>스냅샷 생성</button>
                                    <button>마이그레이션</button>
                                </div>
                              <div className="search_box">
                                    <input type="text" />
                                    <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                                    <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
                                </div>
                                <TableOuter 
                                  columns={TableColumnsInfo.VM_CHART} 
                                  data={vms} 
                                  onRowClick={() => console.log('Row clicked')}
                                />
                            
                              </>
                            )}
                            {/* 스토리지*/}
                            {activeTab === 'storage' && (
                                            <>
                                            <div className="content_header_right">
                                              <button id="new_domain_btn" onClick={() => openPopup('newDomain')}>생성</button>
                                              <button id="get_domain_btn" onClick={() => openPopup('newDomain')}>가져오기</button>
                                              <button id="administer_domain_btn" onClick={() => openPopup('manageDomain')}>편집</button>
                                              <button>삭제</button>
                                              <button>Connections</button>
                                              <button className="content_header_popup_btn" onClick={togglePopup}>
                                                <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                                                {isPopupOpen && (
                                                  <div className="content_header_popup">
                                                    <div>OVF 업데이트</div>
                                                    <div>파괴</div>
                                                    <div>디스크 검사</div>
                                                    <div>마스터 스토리지 도메인으로 선택</div>
                                                  </div>
                                                )}
                                              </button>
                                            </div>
                            
                                            {/* Table 컴포넌트를 이용하여 테이블을 생성합니다. */}
                                            <TableOuter
                                              columns={TableColumnsInfo.STORAGE_DOMAINS} 
                                              data={domaindata} 
                                              onRowClick={() => console.log('Row clicked')}
                                            />
                                          </>
                            )}

                            {/* 네트워크 */}
                            {activeTab === 'network' && (
                                <>
                                <div>네트워크 차트 나오게하는부분0</div>
                                </>
                            )}
                          
                        </div>
                    </div>
                </>
            )}

            {/*편집 팝업 */}
            <Modal
                isOpen={activePopup === 'cluster_detail_edit'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="cluster_new_popup">
                    <div className="popup_header">
                        <h1>클러스터 수정</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="network_new_nav">
                        <div
                            id="cluster_common_btn"
                            className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => setSelectedPopupTab('cluster_common_btn')}  // 여기서 상태를 업데이트
                        >
                            일반
                        </div>
                        <div
                            id="cluster_migration_btn"
                            className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => setSelectedPopupTab('cluster_migration_btn')}  // 상태 업데이트
                        >
                        마이그레이션 정책
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedPopupTab === 'cluster_common_btn' && (
                        <form className="cluster_common_form py-1">
                            <div className="network_form_group">
                            <label htmlFor="data_center">데이터 센터</label>
                            <select id="data_center">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <div>
                                <label htmlFor="name">이름</label>
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
                      
                            {/* id 편집 */}
                            <div className="network_form_group">
                            <label htmlFor="management_network">관리 네트워크</label>
                            <select id="management_network">
                                <option value="ovirtmgmt">ovirtmgmt</option>
                                <option value="ddd">ddd</option>
                                <option value="hosted_engine">hosted_engine</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_architecture">CPU 아키텍처</label>
                            <select id="cpu_architecture">
                                <option value="정의되지 않음">정의되지 않음</option>
                                <option value="x86_64">x86_64</option>
                                <option value="ppc64">ppc64</option>
                                <option value="s390x">s390x</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_type">CPU 유형</label>
                            <select id="cpu_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="chipset_firmware_type">침셋/펌웨어 유형</label>
                            <select id="chipset_firmware_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="bios_change" name="bios_change" />
                            <label htmlFor="bios_change">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="fips_mode">FIPS 모드</label>
                            <select id="fips_mode">
                                <option value="자동 감지">자동 감지</option>
                                <option value="비활성화됨">비활성화됨</option>
                                <option value="활성화됨">활성화됨</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="compatibility_version">호환 버전</label>
                            <select id="compatibility_version">
                                <option value="4.7">4.7</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="switch_type">스위치 유형</label>
                            <select id="switch_type">
                                <option value="Linux Bridge">Linux Bridge</option>
                                <option value="OVS (기술 프리뷰)">OVS (기술 프리뷰)</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="firewall_type">방화벽 유형</label>
                            <select id="firewall_type">
                                <option value="iptables">iptables</option>
                                <option value="firewalld">firewalld</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="default_network_provider">기본 네트워크 공급자</label>
                            <select id="default_network_provider">
                                <option value="기본 공급자가 없습니다.">기본 공급자가 없습니다.</option>
                                <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="max_memory_limit">로그인 최대 메모리 한계</label>
                            <select id="max_memory_limit">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="virt_service_enabled" name="virt_service_enabled" />
                            <label htmlFor="virt_service_enabled">Virt 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="gluster_service_enabled" name="gluster_service_enabled" />
                            <label htmlFor="gluster_service_enabled">Gluster 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <span>추가 난수 생성기 소스:</span>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="dev_hwrng_source" name="dev_hwrng_source" />
                            <label htmlFor="dev_hwrng_source">/dev/hwrng 소스</label>
                            </div>
                        </form>
                      
                    )}

                    {/* 마이그레이션 정책 */}
                    {selectedPopupTab === 'cluster_migration_btn' && (
                        <form className="py-2">
                            <div className="network_form_group">
                            <label htmlFor="migration_policy">마이그레이션 정책</label>
                            <select id="migration_policy">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div class="p-1.5">
                            <span class="font-bold">최소 다운타임</span>
                            <div>
                                일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.
                            </div>
                            </div>
                        
                            <div class="p-1.5 mb-1">
                            <span class="font-bold">대역폭</span>
                            <div className="cluster_select_box">
                                <div class="flex">
                                <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
                                <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
                                </div>
                                <select id="bandwidth_policy">
                                <option value="default">Default</option>
                                </select>
                            </div>
                            </div>
                        
                            <div className="px-1.5 flex relative">
                            <span className="font-bold">복구정책</span>
                            <FontAwesomeIcon
                                icon={faInfoCircle}
                                style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }}
                                onMouseEnter={() => setShowTooltip(true)} // 마우스를 올리면 툴팁을 보여줌
                                onMouseLeave={() => setShowTooltip(false)} // 마우스를 떼면 툴팁을 숨김
                            />
                            {showTooltip && (
                                <div className="tooltip-box">
                                마이그레이션 암호화에 대한 설명입니다.
                                </div>
                            )}
                            </div>
                      
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="password_option" name="encryption_option" />
                            <label htmlFor="password_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="certificate_option" name="encryption_option" />
                            <label htmlFor="certificate_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5 mb-2'>
                            <input type="radio" id="none_option" name="encryption_option" />
                            <label htmlFor="none_option">암호</label>
                            </div>
                        
                            <div class="m-1.5">
                            <span class="font-bold">추가 속성</span>
                            <div className="cluster_select_box">
                                <label htmlFor="encryption_usage">마이그레이션 암호화 사용</label>
                                <select id="encryption_usage">
                                <option value="default">시스템 기본값 (암호화하지 마십시오)</option>
                                <option value="encrypt">암호화</option>
                                <option value="no_encrypt">암호화하지 마십시오</option>
                                </select>
                            </div>
                            
                            <div className="cluster_select_box">
                                <label htmlFor="parallel_migration">마이그레이션 암호화 사용</label>
                                <select id="parallel_migration">
                                <option value="default">Disabled</option>
                                <option value="auto">Auto</option>
                                <option value="auto_parallel">Auto Parallel</option>
                                <option value="custom">Custom</option>
                                </select>
                            </div>
                        
                            <div className="cluster_select_box">
                                <label htmlFor="migration_encryption_text">마이그레이션 암호화 사용</label>
                                <input type="text" id="migration_encryption_text" />
                            </div>
                            </div>
                        </form>
                      
                    )}

                   
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 논리네트워크(네트워크추가) 팝업 */}
            <Modal
                isOpen={activePopup === 'newNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_new_popup">
                    <div className="popup_header">
                        <h1 class="text-sm">새 논리적 네트워크</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="network_new_nav">
                        <div
                            id="network_new_common_btn"
                            className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClickModal('network_new_common_btn')}
                        >
                            일반
                        </div>
                        <div
                            id="network_new_cluster_btn"
                            className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClickModal('network_new_cluster_btn')}
                        >
                            클러스터
                        </div>
                        <div
                            id="network_new_vnic_btn"
                            className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClickModal('network_new_vnic_btn')}
                            style={{ borderRight: 'none' }}
                        >
                            vNIC 프로파일
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedTab === 'network_new_common_btn' && (
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
                    )}

                    {/* 클러스터 */}
                    {selectedTab === 'network_new_cluster_btn' && (
                        <form id="network_new_cluster_form">
                            <span>클러스터에서 네트워크를 연결/분리</span>
                            <div>
                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th>이름</th>
                                            <th>
                                                <div className="checkbox_group">
                                                    <input type="checkbox" id="connect_all" />
                                                    <label htmlFor="connect_all"> 모두 연결</label>
                                                </div>
                                            </th>
                                            <th>
                                                <div className="checkbox_group">
                                                    <input type="checkbox" id="require_all" />
                                                    <label htmlFor="require_all"> 모두 필요</label>
                                                </div>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Default</td>
                                            <td className="checkbox-group">
                                                <div className="checkbox_group">
                                                    <input type="checkbox" id="connect_default" />
                                                    <label htmlFor="connect_default"> 연결</label>
                                                </div>
                                            </td>
                                            <td className="checkbox-group">
                                                <div className="checkbox_group">
                                                    <input type="checkbox" id="require_default" />
                                                    <label htmlFor="require_default"> 필수</label>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {/* vNIC 프로파일 */}
                    {selectedTab === 'network_new_vnic_btn' && (
                        <form id="network_new_vnic_form">
                            <span>vNIC 프로파일</span>
                            <div>
                                <input type="text" id="vnic_profile" />
                                <div className='checkbox_group'>
                                    <input type="checkbox" id="public" disabled />
                                    <label htmlFor="public">공개</label>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                </div>
                                <label htmlFor="qos">QoS</label>
                                <select id="qos">
                                    <option value="none">제한 없음</option>
                                </select>
                                <div className="network_new_vnic_buttons">
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </form>
                    )}
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 선호도 그룹 모달 컴포넌트 */}
            <AffinityGroupModal isOpen={isAffinityGroupModalOpen} onRequestClose={closeAffinityGroupModal} />
            {/* 권한 모달 컴포넌트 */}
            <Permission isOpen={isPermissionModalOpen} onRequestClose={closePermissionModal} />
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

            {/* 클러스터 새로 만들기 팝업 */}
            <Modal
                isOpen={activePopup === 'host_new'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="cluster_new_popup">
                    <div className="popup_header">
                        <h1 class="text-sm">새 클러스터</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="network_new_nav">
                        <div
                            id="cluster_common_btn"
                            className={selectedTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('cluster_common_btn')}
                        >
                            일반
                        </div>
                        <div
                            id="cluster_migration_btn"
                            className={selectedTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('cluster_migration_btn')}
                        >
                           마이그레이션 정책
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedTab === 'cluster_common_btn' && (
                        <form className="cluster_common_form py-1">
                            <div className="network_form_group">
                            <label htmlFor="data_center">데이터 센터</label>
                            <select id="data_center">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <div>
                                <label htmlFor="name">이름</label>
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
                      
                            {/* id 편집 */}
                            <div className="network_form_group">
                            <label htmlFor="management_network">관리 네트워크</label>
                            <select id="management_network">
                                <option value="ovirtmgmt">ovirtmgmt</option>
                                <option value="ddd">ddd</option>
                                <option value="hosted_engine">hosted_engine</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_architecture">CPU 아키텍처</label>
                            <select id="cpu_architecture">
                                <option value="정의되지 않음">정의되지 않음</option>
                                <option value="x86_64">x86_64</option>
                                <option value="ppc64">ppc64</option>
                                <option value="s390x">s390x</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_type">CPU 유형</label>
                            <select id="cpu_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="chipset_firmware_type">침셋/펌웨어 유형</label>
                            <select id="chipset_firmware_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="bios_change" name="bios_change" />
                            <label htmlFor="bios_change">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="fips_mode">FIPS 모드</label>
                            <select id="fips_mode">
                                <option value="자동 감지">자동 감지</option>
                                <option value="비활성화됨">비활성화됨</option>
                                <option value="활성화됨">활성화됨</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="compatibility_version">호환 버전</label>
                            <select id="compatibility_version">
                                <option value="4.7">4.7</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="switch_type">스위치 유형</label>
                            <select id="switch_type">
                                <option value="Linux Bridge">Linux Bridge</option>
                                <option value="OVS (기술 프리뷰)">OVS (기술 프리뷰)</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="firewall_type">방화벽 유형</label>
                            <select id="firewall_type">
                                <option value="iptables">iptables</option>
                                <option value="firewalld">firewalld</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="default_network_provider">기본 네트워크 공급자</label>
                            <select id="default_network_provider">
                                <option value="기본 공급자가 없습니다.">기본 공급자가 없습니다.</option>
                                <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="max_memory_limit">로그인 최대 메모리 한계</label>
                            <select id="max_memory_limit">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="virt_service_enabled" name="virt_service_enabled" />
                            <label htmlFor="virt_service_enabled">Virt 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="gluster_service_enabled" name="gluster_service_enabled" />
                            <label htmlFor="gluster_service_enabled">Gluster 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <span>추가 난수 생성기 소스:</span>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="dev_hwrng_source" name="dev_hwrng_source" />
                            <label htmlFor="dev_hwrng_source">/dev/hwrng 소스</label>
                            </div>
                        </form>
                      
                    )}

                    {/* 마이그레이션 정책 */}
                    {selectedTab === 'cluster_migration_btn' && (
                        <form className="py-2">
                            <div className="network_form_group">
                            <label htmlFor="migration_policy">마이그레이션 정책</label>
                            <select id="migration_policy">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div class="p-1.5">
                            <span class="font-bold">최소 다운타임</span>
                            <div>
                                일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.
                            </div>
                            </div>
                        
                            <div class="p-1.5 mb-1">
                            <span class="font-bold">대역폭</span>
                            <div className="cluster_select_box">
                                <div class="flex">
                                <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
                                <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
                                </div>
                                <select id="bandwidth_policy">
                                <option value="default">Default</option>
                                </select>
                            </div>
                            </div>
                        
                            <div className="px-1.5 flex relative">
                            <span className="font-bold">복구정책</span>
                            <FontAwesomeIcon
                                icon={faInfoCircle}
                                style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }}
                                onMouseEnter={() => setShowTooltip(true)} // 마우스를 올리면 툴팁을 보여줌
                                onMouseLeave={() => setShowTooltip(false)} // 마우스를 떼면 툴팁을 숨김
                            />
                            {showTooltip && (
                                <div className="tooltip-box">
                                마이그레이션 암호화에 대한 설명입니다.
                                </div>
                            )}
                            </div>
                      
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="password_option" name="encryption_option" />
                            <label htmlFor="password_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="certificate_option" name="encryption_option" />
                            <label htmlFor="certificate_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5 mb-2'>
                            <input type="radio" id="none_option" name="encryption_option" />
                            <label htmlFor="none_option">암호</label>
                            </div>
                        
                            <div class="m-1.5">
                            <span class="font-bold">추가 속성</span>
                            <div className="cluster_select_box">
                                <label htmlFor="encryption_usage">마이그레이션 암호화 사용</label>
                                <select id="encryption_usage">
                                <option value="default">시스템 기본값 (암호화하지 마십시오)</option>
                                <option value="encrypt">암호화</option>
                                <option value="no_encrypt">암호화하지 마십시오</option>
                                </select>
                            </div>
                            
                            <div className="cluster_select_box">
                                <label htmlFor="parallel_migration">마이그레이션 암호화 사용</label>
                                <select id="parallel_migration">
                                <option value="default">Disabled</option>
                                <option value="auto">Auto</option>
                                <option value="auto_parallel">Auto Parallel</option>
                                <option value="custom">Custom</option>
                                </select>
                            </div>
                        
                            <div className="cluster_select_box">
                                <label htmlFor="migration_encryption_text">마이그레이션 암호화 사용</label>
                                <input type="text" id="migration_encryption_text" />
                            </div>
                            </div>
                        </form>
                      
                    )}

                   
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/*도메인(새로운 도메인)팝업 */}
            <Modal
                isOpen={activePopup === 'newDomain'}
                onRequestClose={closePopup}
                contentLabel="새로운 도메인"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="storage_domain_new_popup">
                <div className="popup_header">
                    <h1>새로운 도메인</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="storage_domain_new_first">
                    <div className="domain_new_left">
                    <div className="domain_new_select">
                        <label htmlFor="data_hub_location">데이터 센터</label>
                        <select id="data_hub_location">
                        <option value="linux">Default(VS)</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="domain_feature_set">도메인 기능</label>
                        <select id="domain_feature_set">
                            <option value="data">데이터</option>
                            <option value="iso">ISO</option>
                            <option value="export">내보내기</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="storage_option_type">스토리지 유형</label>
                        <select 
                        id="storage_option_type"
                        value={storageType}
                        onChange={handleStorageTypeChange} // 선택된 옵션에 따라 상태 변경
                        >
                        <option value="NFS">NFS</option>
                
                        <option value="iSCSI">iSCSI</option>
                        <option value="fc">파이버 채널</option>
                        </select>
                    </div>
                    <div className="domain_new_select" style={{ marginBottom: 0 }}>
                        <label htmlFor="host_identifier">호스트</label>
                        <select id="host_identifier">
                        <option value="linux">host02.ititinfo.com</option>
                        </select>
                    </div>
                    </div>
                    <div className="domain_new_right">
                    <div className="domain_new_select">
                        <label>이름</label>
                        <input type="text" />
                    </div>
                    <div className="domain_new_select">
                        <label>설명</label>
                        <input type="text" />
                    </div>
                    <div className="domain_new_select">
                        <label>코멘트</label>
                        <input type="text" />
                    </div>
                    </div>
                </div>

                {storageType === 'NFS' && (
                <div className="storage_popup_NFS">
                    <div className ="network_form_group">
                    <label htmlFor="data_hub">NFS 서버 경로</label>
                    <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                    </div>

                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
                    <span>사용자 정의 연결 매개 변수</span>
                    <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                        <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                        <div className="domain_new_select">
                        <label htmlFor="nfs_version">NFS 버전</label>
                        <select id="nfs_version">
                            <option value="host02_ititinfo_com">host02.ititinfo.com</option>
                        </select>
                        </div>
                        {/* <div className="domain_new_select">
                        <label htmlFor="data_hub">재전송</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">추가 마운트 옵션</label>
                        <input type="text" />
                        </div> */}
                    </div>
                    </div>
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>
                        {/* <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                        <select id="format_type_selector" disabled>
                            <option value="linux">V5</option>
                        </select>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="reset_after_deletion"/>
                        <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">백업</label>
                        </div> */}

                    </div>
                    </div>
                </div>
                )}

                

                {storageType === 'iSCSI' && (
                <div className="storage_popup_NFS">
                    <div className='target_btns'> 
                    <button 
                        className={`target_lun ${activeLunTab === 'target_lun' ? 'active' : ''}`}
                        onClick={() => handleLunTabClick('target_lun')}
                    >
                        대상 - LUN
                    </button>
                    <button 
                        className={`lun_target ${activeLunTab === 'lun_target' ? 'active' : ''}`}
                        onClick={() => handleLunTabClick('lun_target')}
                    > 
                        LUN - 대상
                    </button>
                    </div>


                
                    {activeLunTab === 'target_lun' &&(
                    <div className='target_lun_outer'>
                        <div className="search_target_outer">
                        <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn4" onClick={toggleDomainHiddenBox4}fixedWidth/>
                        <span>대상 검색</span>
                        <div id="domain_hidden_box4" style={{ display: isDomainHiddenBox4Visible ? 'block' : 'none' }}>
                            <div className="search_target ">

                            <div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                            </div>

                            <div>
                                <div className='input_checkbox'>
                                <input type="checkbox" id="reset_after_deletion"/>
                                <label htmlFor="reset_after_deletion">사용자 인증 :</label>
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                            </div>

                            
                            </div>
                            <button>검색</button>
                        </div>
                        </div>
                    

                        <div>
                        <button className='all_login'>전체 로그인</button>
                        <div className='section_table_outer'>
                            <Table
                            columns={TableColumnsInfo.CLUSTERS_ALT} 
                            data={data} 
                            onRowClick={handleRowClick}
                            shouldHighlight1stCol={true}
                            />
                        </div>
                        </div>
                    </div>
                    )}      

                    {activeLunTab === 'lun_target' && (
                    <div className='lun_target_outer'>
                        <div className='section_table_outer'>
                            <Table
                            columns={TableColumnsInfo.CLUSTERS_ALT} 
                            data={data} 
                            onRowClick={handleRowClick}
                            shouldHighlight1stCol={true}
                            />
                        </div>
                    </div>
                    )}
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>

                        {/* <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                        <select id="format_type_selector" disabled>
                            <option value="linux">V5</option>
                        </select>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="reset_after_deletion"/>
                        <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">백업</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">삭제 후 폐기</label>
                        </div> */}
                    </div>
                    </div>

                </div>
                )}

                {storageType === 'fc' && (
                <div className="storage_popup_NFS">
                    <div className='section_table_outer'>
                        <Table
                        columns={TableColumnsInfo.CLUSTERS_ALT} 
                        data={data} 
                        onRowClick={handleRowClick}
                        shouldHighlight1stCol={true}
                        />
                    </div>
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                        <select id="format_type_selector" disabled>
                            <option value="linux">V5</option>
                        </select>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="reset_after_deletion"/>
                        <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">백업</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">삭제 후 폐기</label>
                        </div>
                    </div>
                    </div>
                </div>
                )}


                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
                </div>
            </Modal>
             {/*도메인(도메인 관리)팝업 */}
            <Modal
                isOpen={activePopup === 'manageDomain'}
                onRequestClose={closePopup}
                contentLabel="도메인 관리"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="storage_domain_administer_popup">
                <div className="popup_header">
                    <h1>도메인 관리</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="storage_domain_new_first">
                    <div className="domain_new_left">
                    <div className="domain_new_select">
                        <label htmlFor="data_hub_location">데이터 센터</label>
                        <select id="data_hub_location">
                        <option value="linux">Default(VS)</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="domain_feature_set">도메인 기능</label>
                        <select id="domain_feature_set">
                            <option value="data">데이터</option>
                            <option value="iso">ISO</option>
                            <option value="export">내보내기</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="storage_option_type">스토리지 유형</label>
                        <select id="storage_option_type">
                        <option value="linux">NFS</option>
                        <option value="linux">POSIX 호환 FS</option>
                        <option value="linux">GlusterFS</option>
                        <option value="linux">iSCSI</option>
                        <option value="linux">파이버 채널</option>
                        </select>
                    </div>
                    <div className="domain_new_select" style={{ marginBottom: 0 }}>
                        <label htmlFor="host_identifier">호스트</label>
                        <select id="host_identifier">
                        <option value="linux">host02.ititinfo.com</option>
                        </select>
                    </div>
                    </div>
                    <div className="domain_new_right">
                    <div className="domain_new_select">
                        <label>이름</label>
                        <input type="text" />
                    </div>
                    <div className="domain_new_select">
                        <label>설명</label>
                        <input type="text" />
                    </div>
                    <div className="domain_new_select">
                        <label>코멘트</label>
                        <input type="text" />
                    </div>
                    </div>
                </div>

                <div className="storage_popup_NFS">
                    <div>
                    <label htmlFor="data_hub">NFS 서버 경로</label>
                    <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                    </div>

                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
                    <span>사용자 정의 연결 매개 변수</span>
                    <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                        <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                        <div className="domain_new_select">
                        <label htmlFor="nfs_version">NFS 버전</label>
                        <select id="nfs_version" disabled style={{cursor:'no-drop'}}>
                            <option value="host02_ititinfo_com" >자동 협상(기본)</option>
                        </select>
                        </div>
                        {/* <div className="domain_new_select">
                        <label htmlFor="data_hub">재전송(#)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">추가 마운트 옵션</label>
                        <input type="text" />
                        </div> */}
                    </div>
                    </div>
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>
                        {/* <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                        <select id="format_type_selector" disabled>
                            <option value="linux">V5</option>
                        </select>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="reset_after_deletion"/>
                        <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">백업</label>
                        </div> */}
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
        </div>

    
    );
}

export default RutilManager;
