import React, { useState,useEffect } from 'react';
import {useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Modal from 'react-modal';
import './css/RutilManager.css';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import AffinityGroupModal from '../Modal/AffinityGroupModal';
import NetworkDetail from '../Network/NetworkDetail';
import Permission from '../Modal/Permission';
import { 
    useDashboard,
    useDashboardCpuMemory,
    useDashboardStorage,

    useAllDataCenters,
    useAllClusters, 
    useAllHosts,
    useAllVMs, 
    useAllStorageDomains,
    useAllNetworks,

    useDataCenter, 
    useNetworkById,
    
    useClusterById, 
    useEventFromCluster, 
    useHostFromCluster, 
    useLogicalFromCluster, 
    usePermissionFromCluster, 
    usePermissionromCluster, 
    useVMFromCluster, 
    useAllTemplates,
    useAllDisk
 } from '../../api/RQHook';
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
  faChevronCircleRight,
  faMinus,
  faArrowDown,
  faArrowUp,
  faPlus,
  faExclamationTriangle,
  faBuilding
} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';
import logo from '../../img/logo.png'
import Path from '../Header/Path';
import TemplateDu from '../duplication/TemplateDu';
import HostDu from '../duplication/HostDu';

function RutilManager() {
    const { id } = useParams();

    const {
        data: dashboard,
        status: dashboardStatus,
        isRefetching: isDashboardRefetching,
        refetch: dashboardRefetch, 
        isError: isDashboardError, 
        error: dashboardError, 
        isLoading: isDashboardLoading,
    } = useDashboard()

    const {
        data: cpuMemory,
        status: cpuMemoryStatus,
        isRefetching: isCpuMemoryRefetching,
        refetch: cpuMemoryRefetch, 
        isError: isCpuMemoryError, 
        error: cpuMemoryError, 
        isLoading: isCpuMemoryLoading,
      } = useDashboardCpuMemory()
      
    const {
        data: storage,
        status: storageStatus,
        isRefetching: isStorageRefetching,
        refetch: storageRefetch, 
        isError: isStorageError, 
        error: storageError, 
        isLoading: isStorageLoading,
    } = useDashboardStorage()
 
    // 데이터센터
    const {
        data: allDataCenters,        // 데이터센터 목록
        status: allDataCentersStatus, // 쿼리 상태
        isRefetching: isAllDataCentersRefetching, // 리패칭 중인지 여부
        refetch: allDataCentersRefetch,           // 수동으로 리패칭하는 함수
        isError: isAllDataCentersError,           // 에러가 발생했는지 여부
        error: allDataCentersError,               // 실제 에러 객체
        isLoading: isAllDataCentersLoading,       // 로딩 중인지 여부
    } = useAllDataCenters((dataCenter) => {
        return {
            ...dataCenter,
            storageType: dataCenter.storageType
        };
    });
    
    // 클러스터
    const {
        data: allClusters,
        status: allClustersStatus,
        isRefetching: isAllClustersRefetching,
        refetch: allClustersRefetch,
        isError: isAllClustersError,
        error: allClustersError,
        isLoading: isAllClustersLoading,
      } = useAllClusters((cluster) => {
        return {
          ...cluster,
        };
    });

    // 호스트
    const {
        data: allHosts,
        status: allHostsStatus,
        isRefetching: isAllHostsRefetching,
        refetch: allHostsRefetch,
        isError: isAllHostsError,
        error: allHostsError,
        isLoading: isAllHostsLoading,
      } = useAllHosts((host) => {
        return {
          ...host,
        };
    });

    // 가상머신
    const {
        data: allVMs,
        status: allVmsStatus,
        isRefetching: isAllVmsRefetching,
        refetch: allVmsRefetch,
        isError: isAllVmsError,
        error: allVmsError,
        isLoading: isAllVmsLoading,
      } = useAllVMs((vm) => {
        return {
          ...vm,
        };
    });
    //템플릿
    const { 
        data: templates, 
        status: templatesStatus,
        isRefetching: isTemplatesRefetching,
        refetch: refetchTemplates, 
        isError: isTemplatesError, 
        error: templatesError, 
        isLoading: isTemplatesLoading,
      } = useAllTemplates(toTableItemPredicateTemplates);
      
      function toTableItemPredicateTemplates(template) {
        return {
          id: template?.id ?? '',
          name: template?.name ?? 'Unknown', 
          status: template?.status ?? 'Unknown',                // 템플릿 상태
          version: template?.version ?? 'N/A',                  // 템플릿 버전 정보
          description: template?.description ?? 'No description',// 템플릿 설명
          cpuType: template?.cpuType ?? 'CPU 정보 없음',         // CPU 유형 정보
          hostCount: template?.hostCount ?? 0,                  // 템플릿에 연결된 호스트 수
          vmCount: template?.vmCount ?? 0,                      // 템플릿에 연결된 VM 수
        };
      }

    // 스토리지 도메인
    const {
        data: allStorageDomains,
        status: allStorageDomainsStatus,
        isRefetching: isAllStorageDomainsRefetching,
        refetch: allStorageDomainsRefetch,
        isError: isAllStorageDomainsError,
        error: allStorageDomainsError,
        isLoading: isAllStorageDomainsLoading,
      } = useAllStorageDomains((storageDomain) => {
        return {
          ...storageDomain,
        };
    });

    // 디스크
    const { 
        data: dsikData,
        status: networksStatus,
        isRefetching: isNetworksRefetching,
        refetch: networksRefetch, 
        isError: isNetworksError, 
        error: networksError, 
        isLoading: isNetworksLoading,
      } = useAllDisk((item) => {
        return {
          id: item?.id ?? '',  // ID
          name: item?.name ?? '',  // 이름
          description: item?.description ?? '',  // 설명
          dataCenter: item?.dataCenterVo?.name ?? '',  // 데이터 센터
          provider: item?.provider ?? 'Provider1',  // 제공자 (기본값: 'Provider1')
          portSeparation: item?.portIsolation ? '예' : '아니요',  // 포트 분리 여부
          alias: item?.alias ?? '',  // 별칭
          icon1: item?.icon1 ?? '',  // 아이콘 1
          icon2: item?.icon2 ?? '',  // 아이콘 2
          connectionTarget: item?.connectionTarget ?? '',  // 연결 대상
          storageDomain: item?.storageDomainVo?.name ?? '',
          virtualSize: item?.virtualSize ?? '',  // 가상 크기
          status: item?.status ?? '',  // 상태
          type: item?.type ?? '',  // 유형
        }
      })
    
    // 네트워크
    // const {
    //     data: allNetworks = [],  // 기본값을 빈 배열로 설정
    //     status: allNetworksStatus,
    //     isRefetching: isAllNetworksRefetching,
    //     refetch: allNetworksRefetch,
    //     isError: isAllNetworksError,
    //     error: allNetworksError,
    //     isLoading: isAllNetworksLoading,
    // } = useAllNetworks((network) => {
    //     return {
    //       ...network,
    //     };
    // });
      
 
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
    useEffect(() => {
        if (activePopup === 'cluster_new') {
          setSelectedTab('cluster_common_btn');
        }
      }, [activePopup]);
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
    const [activeSection, setActiveSection] = useState('common_outer');
    const handleSectionChange = (section) => {
        setActiveSection(section);
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
  const [활성화된섹션, set활성화된섹션] = useState('일반_섹션');
  useEffect(() => {
    const 기본섹션 = document.getElementById('일반_섹션_btn');
    if (기본섹션) {
      기본섹션.style.backgroundColor = '#EDEDED';
      기본섹션.style.color = '#1eb8ff';
      기본섹션.style.borderBottom = '1px solid blue';
    }
  }, []);

  const 섹션변경 = (section) => {
    set활성화된섹션(section);
    const 모든섹션들 = document.querySelectorAll('.edit_aside > div');
    모든섹션들.forEach((el) => {
      el.style.backgroundColor = '#FAFAFA';
      el.style.color = 'black';
      el.style.borderBottom = 'none';
    });

    const 선택된섹션 = document.getElementById(`${section}_btn`);
    if (선택된섹션) {
      선택된섹션.style.backgroundColor = '#EDEDED';
      선택된섹션.style.color = '#1eb8ff';
      선택된섹션.style.borderBottom = '1px solid blue';
    }
  };
  const [isHiddenParameterVisible, setHiddenParameterVisible] = useState(false);
  const handleSectionClick = (section) => {
    setActiveSection(section); // 탭 클릭 시 상태 변경
  };
  const toggleHiddenParameter = () => {
    setHiddenParameterVisible(!isHiddenParameterVisible);
  };

  const [activeDiskType, setActiveDiskType] = useState('all');
  const handleDiskTypeClick = (type) => {
    setActiveDiskType(type);  // 여기서 type을 설정해야 함
  };
  const [activeContentType, setActiveContentType] = useState('all'); // 컨텐츠 유형 상태
    // 컨텐츠 유형 변경 핸들러
    const handleContentTypeChange = (event) => {
      setActiveContentType(event.target.value);
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

    
    const [activeTab, setActiveTab] = useState(() => {
        return localStorage.getItem('activeTab') || 'general';
      });
    const handleTabClick = (tab) => {
        setActiveTab(tab);
        localStorage.setItem('activeTab', tab); // 새로고침해도 값유지
    };
 
    

    // HeaderButton 컴포넌트
    const buttons = [
       
    ];

    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'data_center', label: '데이터센터' },
        { id: 'cluster', label: '클러스터' },
        { id: 'host', label: '호스트' },
        { id: 'virtual_machine', label: '가상머신' },
        { id: 'template', label: '템플릿' },
        { id: 'storage_domain', label: '스토리지 도메인' },
        { id: 'disk', label: '디스크' },
        { id: 'network', label: '네트워크' },
        { id: 'vNIC', label: 'vNIC 프로파일' },
    ];
    const pathData = ['Rutil Manager', sections.find(section => section.id === activeTab)?.label];
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
                        titleIcon={faBuilding}
                        title="Rutil Manager"
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
                            {activeTab !== 'general' && <Path pathElements={pathData} />}
                            {/* 일반 */}
                            {activeTab === 'general' && (
                                <div className='rutil_general'>
                                    <div className='rutil_general_first_contents'>
                                        <div>
                                            <img className='logo_general' src={logo} alt="logo Image" />
                                            <span>
                                                버전: ###<br/>
                                                빌드:###
                                            </span>
                                        </div>
                                        <div>
                                            <div className='mb-2'>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                                    <span>데이터센터:</span>
                                                    <span>{dashboard?.datacenters ?? 0}</span>
                                                </div>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                                    <span>클러스터:</span>
                                                    <span>{dashboard?.clusters ?? 0}</span>
                                                </div>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                                    <span>호스트:</span>
                                                    <span>{dashboard?.hosts ?? 0}</span>
                                                </div>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                                    <span>가상머신:</span>
                                                    <span>{dashboard?.vmsUp ?? 0} / {dashboard?.vms}</span>
                                                </div>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                                    <span>스토리지 도메인:</span>
                                                    <span>{dashboard?.storageDomains ?? 0}</span>
                                                </div>
                                            </div>
                                            <br/>
                                            <div>
                                                부팅시간(업타임)<br/>
                                                <span className='font-bold'>2024-**-** 20:15:45</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div className='type_info_boxs'>
                                        <div className='type_info_box'>
                                                <span className='font-bold'>CPU</span><br/>
                                                {Math.floor( 100 - cpuMemory?.totalCpuUsagePercent ?? 0)} % 사용가능<br/>
                                                가상 리소스 - 사용됨: {Math.floor((cpuMemory?.usedCpuCore)/(cpuMemory?.totalCpuCore)*100 )} %, 할당됨: { Math.floor((cpuMemory?.commitCpuCore)/(cpuMemory?.totalCpuCore)*100 )} % 
                                        </div>
                                        <div className='type_info_box'>
                                                <span className='font-bold'>메모리</span><br/>
                                                {Math.floor(100 - cpuMemory?.totalMemoryUsagePercent ?? 0)} % 사용가능<br/>
                                                가상 리소스 - 사용됨: --%, 할당됨: --%
                                        </div>
                                        <div className='type_info_box'>
                                                <span className='font-bold'>스토리지</span><br/>
                                                {Math.floor(100 - storage?.usedPercent ?? 0)} % 사용가능<br/>
                                                가상 리소스 - 사용됨: --%, 할당됨: --%
                                        </div>
                                    </div>
                                </div>
                            )}
                            {/*데이터 센터 */}
                            {activeTab === 'data_center' && (
                                <>
                                <div className="header_right_btns">
                                    <button onClick={() => openPopup('datacenter_new')}>새로 만들기</button>
                                    <button onClick={() => openPopup('datacenter_edit')}>편집</button>
                                    <button>삭제</button>
                                    
                                </div>
                                
                                <TableOuter
                                  columns={TableColumnsInfo.DATACENTERS} 
                                  data={allDataCenters} 
                                  onRowClick={handleRowClick} />
                                </>

                            )}
                            {/* 클러스터 */}
                            {activeTab === 'cluster' && (
                              <>
                               <div className="header_right_btns">
                                    <button onClick={() => openPopup('cluster_new')}>새로만들기</button>
                                    <button onClick={() => openPopup('cluster_detail_edit')}>편집</button>
                                    <button onClick={() => openPopup('delete')}>삭제</button>
                                </div>
                                
                                <TableOuter 
                                  columns={TableColumnsInfo.CLUSTERS_DATA} 
                                  data={allClusters} 
                                  onRowClick={() => console.log('Row clicked')}
                                />
                             
                              </>
                            )}
                            {/* 호스트 */}
                            {activeTab === 'host' && (
                                <HostDu 
                                data={allHosts} 
                                columns={TableColumnsInfo.HOSTS_ALL_DATA} 
                                handleRowClick={handleRowClick}
                                openPopup={openPopup}
                              />
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                              <>
                              <div className="header_right_btns">
                                    <button onClick={() => openPopup('vm_new')}>새로만들기</button>
                                    <button onClick={() => openPopup('vm_edit')}>편집</button>
                                    <button onClick={() => openPopup('delete')}>삭제</button>
                                    <button>실행</button>
                                    <button>일시중지</button>
                                    <button>종료</button>
                                    <button>재부팅</button>
                                    <button>콘솔</button>
                                    <button>스냅샷 생성</button>
                                    <button>마이그레이션</button>
                                    {/* <button className="content_header_popup_btn" onClick={togglePopup}>
                                                <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                                                {isPopupOpen && (
                                                  <div className="content_header_popup">
                                                    <div>OVF 업데이트</div>
                                                    <div>파괴</div>
                                                    <div>디스크 검사</div>
                                                    <div>마스터 스토리지 도메인으로 선택</div>
                                                  </div>
                                                )}
                                    </button> */}
                                </div>
                              
                                <TableOuter 
                                  columns={TableColumnsInfo.VM_CHART} 
                                  data={allVMs} 
                                  onRowClick={() => console.log('Row clicked')}
                                  showSearchBox={true}
                                />
                            
                              </>
                            )}
                            {/*  템플릿 */}
                            {activeTab === 'template' && (
                            //   <>
                            // <div className="content_header_right">
                            //     <div className="search_box">
                            //         <input type="text" />
                            //         <button><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                            //         <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                            //     </div>

                            //     <div className='header_right_btns'>
                            //         <button>가져오기</button>
                            //         <button>편집</button>
                            //         <button>삭제</button>
                            //         <button className='disabled'>내보내기</button>
                            //         <button className='disabled'>새 가상머신</button>
                            //     </div>        
                            // </div>
                                
                            //     <TableOuter
                            //     columns={TableColumnsInfo.TEMPLATE_CHART} 
                            //     data={templates} 
                            //     onRowClick={handleRowClick} 
                            //     className='template_chart'
                            //     clickableColumnIndex={[0]} 
                            //  />
                            
                            //   </>
                                    <TemplateDu 
                                    data={templates} 
                                    columns={TableColumnsInfo.TEMPLATE_CHART} 
                                    handleRowClick={handleRowClick}
                                />
                            )}
                            {/* 스토리지 도메인*/}
                            {activeTab === 'storage_domain' && (
                                <>
                                <div className="header_right_btns">
                                    <button id="new_domain_btn" onClick={() => openPopup('newDomain')}>도메인 생성</button>
                                    <button id="get_domain_btn" onClick={() => openPopup('newDomain')}>도메인 가져오기</button>
                                    <button id="administer_domain_btn" onClick={() => openPopup('manageDomain')}>도메인 관리</button>
                                    <button onClick={() => openPopup('delete')}>삭제</button>
                                    <button className='disabled'>Connections</button>
                                    <button className='disabled'>LUN 새로고침</button>
                                    <button className="content_header_popup_btn" onClick={togglePopup}>
                                    <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                                    {isPopupOpen && (
                                        <div className="content_header_popup">
                                        <div className='disabled'>OVF 업데이트</div>
                                        <div className='disabled'>파괴</div>
                                        <div className='disabled'>디스크 검사</div>
                                        <div className='disabled'>마스터 스토리지 도메인으로 선택</div>
                                        </div>
                                    )}
                                    </button>
                                </div>
                
                                {/* Table 컴포넌트를 이용하여 테이블을 생성합니다. */}
                                <TableOuter
                                    columns={TableColumnsInfo.STORAGE_DOMAINS} 
                                    data={allStorageDomains} 
                                    onRowClick={() => console.log('Row clicked')}
                                />
                                </>
                            )}

                            {/*디스크*/}
                            {activeTab === 'disk' && (
                                        <>
                                        <div className="disk_type">
                                          <div>
                                            <span>디스크유형 : </span>
                                            <div className='flex'>
                                              <button className={activeDiskType === 'all' ? 'active' : ''} onClick={() => handleDiskTypeClick('all')}>모두</button>
                                              <button className={activeDiskType === 'image' ? 'active' : ''} onClick={() => handleDiskTypeClick('image')}>이미지</button>
                                              <button style={{ marginRight: '0.2rem' }} className={activeDiskType === 'lun' ? 'active' : ''} onClick={() => handleDiskTypeClick('lun')}>직접 LUN</button>
                                            </div>
                                          </div>
                                          <div className="content_type">
                                            <label className='mr-1' htmlFor="contentType">컨텐츠 유형:</label>
                                            <select id="contentType" value={activeContentType} onChange={handleContentTypeChange}>
                                              <option value="all">모두</option>
                                              <option value="data">데이터</option>
                                              <option value="ovfStore">OVF 스토어</option>
                                              <option value="memoryDump">메모리 덤프</option>
                                              <option value="iso">ISO</option>
                                              <option value="hostedEngine">Hosted Engine</option>
                                              <option value="sanlock">Hosted Engine Sanlock</option>
                                              <option value="metadata">Hosted Engine Metadata</option>
                                              <option value="conf">Hosted Engine Conf.</option>
                                            </select>
                                          </div>    
                                        </div>
                        
                                        {activeDiskType === 'all' && (
                                          <TableOuter 
                                            columns={TableColumnsInfo.ALL_DISK}
                                            data={dsikData}
                                            onRowClick={handleRowClick}
                                            showSearchBox={true}
                                          />
                                        )}
                        
                                        {activeDiskType === 'image' && (
                                          <TableOuter 
                                            columns={TableColumnsInfo.IMG_DISK}
                                            data={dsikData}
                                            onRowClick={handleRowClick}
                                            showSearchBox={true}
                                          />
                                        )}
                        
                                        {activeDiskType === 'lun' && (
                                          <TableOuter 
                                            columns={TableColumnsInfo.LUN_DISK}
                                            data={dsikData}
                                            onRowClick={handleRowClick}
                                            showSearchBox={true}
                                          />
                                        )}
                                      </>
                            )}
                            {/* 네트워크 */}
                            {activeTab === 'network' && (
                                <>
                                <div className="header_right_btns">
                                    <button onClick={ () => openPopup('makeNetwork') }>새로만들기</button>
                                    <button onClick={() => openPopup('getNetwork')}>가져오기</button>
                                    <button onClick={() => openPopup('editNetwork')}>편집</button>
                                    <button onClick={() => openPopup('delete')}>삭제</button>
                                  
                                </div>
                                <TableOuter
                                    columns={TableColumnsInfo.NETWORKS} 
                                    data={[]} 
                                    onRowClick={() => console.log('Row clicked')}
                                />
                               </>
                            )}
                            {/* vNIC프로파일 */}
                            {activeTab === 'vNIC' && (
                                    <>
                                <div className="header_right_btns">
                                    <button onClick={ () => openPopup('makeNetwork') }>새로만들기</button>
                                    <button onClick={() => openPopup('getNetwork')}>가져오기</button>
                                    <button onClick={() => openPopup('editNetwork')}>편집</button>
                                    <button onClick={() => openPopup('delete')}>삭제</button>
                                  
                                </div>
                                <TableOuter
                                    columns={TableColumnsInfo.NETWORKS} 
                                    data={[]} 
                                    onRowClick={() => console.log('Row clicked')}
                                />
                               </>
                            )}
                        </div>
                    </div>
                </>
            )}

        {/* 데이터 센터 편집 모달 */}
        <Modal
          isOpen={activePopup === 'datacenter_new'}
          onRequestClose={closePopup}
          contentLabel="새로 만들기"
          className="Modal"
          overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
        >
          <div className="datacenter_new_popup">
            <div className="popup_header">
              <h1>새로운 데이터 센터</h1>
              <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            </div>

            <div className="datacenter_new_content">
              <div>
                <label htmlFor="name1">이름</label>
                <input type="text" id="name1" />
              </div>
              <div>
                <label htmlFor="comment">설명</label>
                <input type="text" id="comment" />
              </div>
              <div>
                <label htmlFor="cluster">클러스터</label>
                <select id="cluster">
                  <option value="공유됨">공유됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="compatibility">호환버전</label>
                <select id="compatibility">
                  <option value="4.7">4.7</option>
                </select>
              </div>
              <div>
                <label htmlFor="quota_mode">쿼터 모드</label>
                <select id="quota_mode">
                  <option value="비활성화됨">비활성화됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="comment">코멘트</label>
                <input type="text" id="comment" />
              </div>
            </div>

            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button onClick={() => closePopup('edit')}>취소</button>
            </div>
          </div>
        </Modal>

        {/* 데이터 센터 편집 모달 */}
        <Modal
          isOpen={activePopup === 'datacenter_edit'}
          onRequestClose={closePopup}
          contentLabel="새로 만들기"
          className="Modal"
          overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
        >
          <div className="datacenter_new_popup">
            <div className="popup_header">
              <h1>데이터 센터 수정</h1>
              <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            </div>

            <div className="datacenter_new_content">
              <div>
                <label htmlFor="name1">이름</label>
                <input type="text" id="name1" />
              </div>
              <div>
                <label htmlFor="comment">설명</label>
                <input type="text" id="comment" />
              </div>
              <div>
                <label htmlFor="cluster">클러스터</label>
                <select id="cluster">
                  <option value="공유됨">공유됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="compatibility">호환버전</label>
                <select id="compatibility">
                  <option value="4.7">4.7</option>
                </select>
              </div>
              <div>
                <label htmlFor="quota_mode">쿼터 모드</label>
                <select id="quota_mode">
                  <option value="비활성화됨">비활성화됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="comment">코멘트</label>
                <input type="text" id="comment" />
              </div>
            </div>

            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button onClick={() => closePopup('edit')}>취소</button>
            </div>
          </div>
        </Modal>
        
            {/* 클러스터 새로 만들기 팝업 */}
            <Modal
                        isOpen={activePopup === 'cluster_new'}
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

                            <div className='flex'>
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
</div>   
                        
                            <div className="edit_footer">
                                <button style={{ display: 'none' }}></button>
                                <button>OK</button>
                                <button onClick={closePopup}>취소</button>
                            </div>
                        </div>
            </Modal>
            {/*클러스터(편집) 팝업 */}
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

                    <div className='flex'>
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
</div>
                   
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

            {/* 호스트(새로만들기) 팝업*/}
            <Modal
              isOpen={activePopup === 'host_new'}
              onRequestClose={closePopup}
              contentLabel="편집"
              className="host_new_popup"
              overlayClassName="host_new_outer"
              shouldCloseOnOverlayClick={false}
            >
              <div className="popup_header">
                <h1>호스트 새로만들기</h1>
                <button onClick={closePopup}>
                  <FontAwesomeIcon icon={faTimes} fixedWidth/>
                </button>
              </div>

              <div className="edit_body">
                <div className="edit_aside">
                  <div
                    className={`edit_aside_item`}
                    id="일반_섹션_btn"
                    onClick={() => 섹션변경('일반_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>일반</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="전원관리_섹션_btn"
                    onClick={() => 섹션변경('전원관리_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>전원 관리</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="호스트엔진_섹션_btn"
                    onClick={() => 섹션변경('호스트엔진_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>호스트 엔진</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="선호도_섹션_btn"
                    onClick={() => 섹션변경('선호도_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>선호도</span>
                  </div>
                </div>

                {/* 폼의 다양한 섹션들 */}
                <form action="#">
                  {/* 공통 섹션 */}
                  <div
                    id="일반_섹션"
                    style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
                  >
                <div className="edit_first_content">
                        <div>
                            <label htmlFor="cluster">클러스터</label>
                            <select id="cluster" disabled>
                                <option value="default" >Default</option>
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div>
                            <label htmlFor="comment">코멘트</label>
                            <input type="text" id="comment" />
                        </div>
                        <div>
                            <label htmlFor="hostname">호스트이름/IP</label>
                            <input type="text" id="hostname" />
                        </div>
                        <div>
                            <label htmlFor="ssh_port">SSH 포트</label>
                            <input type="text" id="ssh_port" value="22" />
                        </div>
                    </div>

          <div className='host_checkboxs'>
            <div className='host_checkbox'>
                <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                <label htmlFor="headless_mode">헤드리스 모드</label>
            </div>
            <div className='host_checkbox'>
                <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
            </div>
          </div>

          <div className='host_checkboxs'>
            <div className='host_textbox'>
                <label htmlFor="user_name">사용자 이름</label>
                <input type="text" id="user_name" />
            </div>

            <div className='host_text_raido_box'>
                <div>
                  <input type="radio" id="password" name="name_option" />
                  <label htmlFor="password">암호</label>
                </div>
                <input type="text" id="radio1_name" />
            </div>

            <div className='host_radiobox'>
                <input type="radio" id="ssh_key" name="name_option" />
                <label htmlFor="ssh_key">SSH 공개키</label>
            </div>

          </div>

                  </div>{/*일반섹션끝 */}

                  {/* 전원 관리 섹션 */}
                  <div
                    id="전원관리_섹션"
                    style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
                  >
                    
                    <div className='host_checkboxs'>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                          <label htmlFor="enable_forwarding">전송 관리 활성</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                          <label htmlFor="kdump_usage">Kdump 통합</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                          <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
                      </div>


                      <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
                    </div>
                    
                    
                    <div className='addFence_agent'>
                      <span>펜스 에이전트 추가</span>
                      <button>+</button>
                    </div>

                    <div className='advanced_objec_add'>
                    <div className='flex'>
                        <button className='mr-1'onClick={toggleHiddenParameter}>
                        {isHiddenParameterVisible ? '-' : '+'}
                        </button>
                        <span>고급 매개 변수</span>
                    </div>
                {isHiddenParameterVisible && (
                <div className='host_hidden_parameter'>
                 
                  <div>전원 관리 프록시 설정</div>
                  <div>
                    <div className='proxy_content'>
                      <div className='font-bold'>1.</div>
                      <div className='w-6'>cluster</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                    <div className='proxy_content'>
                      <div className='font-bold'>2.</div>
                      <div className='w-6'>dc</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                  </div>

                  <div className='proxy_add'>
                    <div>전원 관리 프록시 추가</div>
                    <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
                  </div>
                </div>
                )}
              </div>
                    

                  </div>

                  {/* 호스트 엔진 섹션 */}
                  <div
                    id="호스트엔진_섹션"
                    style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
                  >
                    <div className="host_policy">
                        <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
                        <select id="host_action">
                            <option value="none">없음</option>
                        </select>
                    </div>


                  </div>

                  {/* 선호도 섹션 */}
                  <div
                    id="선호도_섹션"
                    style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
                  >
                    <div className="preference_outer">
                      <div className="preference_content">
                        <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
                          <div>
                            <select id="preference_group">
                              <option value="none"></option>
                            </select>
                            <button>추가</button>
                          </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택된 선호도 그룹</div>
                        <div>선택된 선호도 그룹이 없습니다</div>
                      </div>
                      <div className="preference_content">
                        <label htmlFor="preference_label">선호도 레이블 선택</label>
                        <div>
                          <select id="preference_label">
                            <option value="none"></option>
                          </select>
                          <button>추가</button>
                        </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택한 선호도 레이블</div>
                        <div>선호도 레이블이 선택되어있지 않습니다</div>
                      </div>

                    </div>
                  </div>

                </form>
              </div>

              <div className="edit_footer">
                <button>OK</button>
                <button onClick={closePopup}>취소</button>
              </div>
            </Modal>
            {/* 호스트(편집) 팝업*/}
            <Modal
              isOpen={activePopup === 'host_edit'}
              onRequestClose={closePopup}
              contentLabel="편집"
              className="host_new_popup"
              overlayClassName="host_new_outer"
              shouldCloseOnOverlayClick={false}
            >
              <div className="popup_header">
                <h1>호스트 수정</h1>
                <button onClick={closePopup}>
                  <FontAwesomeIcon icon={faTimes} fixedWidth/>
                </button>
              </div>

              <div className="edit_body">
                <div className="edit_aside">
                  <div
                    className={`edit_aside_item`}
                    id="일반_섹션_btn"
                    onClick={() => 섹션변경('일반_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>일반</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="전원관리_섹션_btn"
                    onClick={() => 섹션변경('전원관리_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>전원 관리</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="호스트엔진_섹션_btn"
                    onClick={() => 섹션변경('호스트엔진_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>호스트 엔진</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="선호도_섹션_btn"
                    onClick={() => 섹션변경('선호도_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>선호도</span>
                  </div>
                </div>

                {/* 폼의 다양한 섹션들 */}
                <form action="#">
                  {/* 공통 섹션 */}
                  <div
                    id="일반_섹션"
                    style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
                  >
                <div className="edit_first_content">
                        <div>
                            <label htmlFor="cluster">클러스터</label>
                            <select id="cluster" disabled>
                                <option value="default" >Default</option>
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div>
                            <label htmlFor="comment">코멘트</label>
                            <input type="text" id="comment" />
                        </div>
                        <div>
                            <label htmlFor="hostname">호스트이름/IP</label>
                            <input type="text" id="hostname" />
                        </div>
                        <div>
                            <label htmlFor="ssh_port">SSH 포트</label>
                            <input type="text" id="ssh_port" value="22" />
                        </div>
                    </div>

          <div className='host_checkboxs'>
            <div className='host_checkbox'>
                <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                <label htmlFor="headless_mode">헤드리스 모드</label>
            </div>
            <div className='host_checkbox'>
                <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
            </div>
          </div>

          <div className='host_checkboxs'>
            <div className='host_textbox'>
                <label htmlFor="user_name">사용자 이름</label>
                <input type="text" id="user_name" />
            </div>

            <div className='host_text_raido_box'>
                <div>
                  <input type="radio" id="password" name="name_option" />
                  <label htmlFor="password">암호</label>
                </div>
                <input type="text" id="radio1_name" />
            </div>

            <div className='host_radiobox'>
                <input type="radio" id="ssh_key" name="name_option" />
                <label htmlFor="ssh_key">SSH 공개키</label>
            </div>

          </div>

                  </div>{/*일반섹션끝 */}

                  {/* 전원 관리 섹션 */}
                  <div
                    id="전원관리_섹션"
                    style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
                  >
                    
                    <div className='host_checkboxs'>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                          <label htmlFor="enable_forwarding">전송 관리 활성</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                          <label htmlFor="kdump_usage">Kdump 통합</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                          <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
                      </div>


                      <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
                    </div>
                    
                    
                    <div className='addFence_agent'>
                      <span>펜스 에이전트 추가</span>
                      <button>+</button>
                    </div>

                    <div className='advanced_objec_add'>
                <button onClick={toggleHiddenParameter}>
                  {isHiddenParameterVisible ? '-' : '+'}
                </button>
                <span>고급 매개 변수</span>
                {isHiddenParameterVisible && (
                <div className='host_hidden_parameter'>
                 
                  <div>전원 관리 프록시 설정</div>
                  <div>
                    <div className='proxy_content'>
                      <div className='font-bold'>1.</div>
                      <div className='w-6'>cluster</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                    <div className='proxy_content'>
                      <div className='font-bold'>2.</div>
                      <div className='w-6'>dc</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                  </div>

                  <div className='proxy_add'>
                    <div>전원 관리 프록시 추가</div>
                    <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
                  </div>
                </div>
                )}
              </div>
                    

                  </div>

                  {/* 호스트 엔진 섹션 */}
                  <div
                    id="호스트엔진_섹션"
                    style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
                  >
                    <div className="host_policy">
                        <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
                        <select id="host_action">
                            <option value="none">없음</option>
                        </select>
                    </div>


                  </div>

                  {/* 선호도 섹션 */}
                  <div
                    id="선호도_섹션"
                    style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
                  >
                    <div className="preference_outer">
                      <div className="preference_content">
                        <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
                          <div>
                            <select id="preference_group">
                              <option value="none"></option>
                            </select>
                            <button>추가</button>
                          </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택된 선호도 그룹</div>
                        <div>선택된 선호도 그룹이 없습니다</div>
                      </div>
                      <div className="preference_content">
                        <label htmlFor="preference_label">선호도 레이블 선택</label>
                        <div>
                          <select id="preference_label">
                            <option value="none"></option>
                          </select>
                          <button>추가</button>
                        </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택한 선호도 레이블</div>
                        <div>선호도 레이블이 선택되어있지 않습니다</div>
                      </div>

                    </div>
                  </div>

                </form>
              </div>

              <div className="edit_footer">
                <button>OK</button>
                <button onClick={closePopup}>취소</button>
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
                    <div className='target_btns flex'> 
                    
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

            {/* 가상머신( 새로만들기)팝업 */}   
            <Modal
    isOpen={activePopup === 'vm_new'}
    onRequestClose={closePopup}
    contentLabel="가상머신 편집"
    className="edit_popup"
    overlayClassName="edit_popup_outer"
    shouldCloseOnOverlayClick={false}
  >
 <div id="edit_popup">
            <div className="popup_header">
              <h1>가상머신 생성</h1>
              <button onClick={closePopup}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            
            <div className="edit_body">
            <div className="edit_aside">
                <div
                  className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
                  id="common_outer_btn"
                  onClick={() => handleSectionChange('common_outer')}
                >
                  <span>일반</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
                  id="system_outer_btn"
                  onClick={() => handleSectionChange('system_outer')}
                >
                  <span>시스템</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
                  id="host_outer_btn"
                  onClick={() => handleSectionChange('host_outer')}
                >
                  <span>호스트</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
                  id="ha_mode_outer_btn"
                  onClick={() => handleSectionChange('ha_mode_outer')}
                >
                  <span>고가용성</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
                  id="res_alloc_outer_btn"
                  onClick={() => handleSectionChange('res_alloc_outer')}
                >
                  <span>리소스 할당</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
                  id="boot_outer_btn"
                  onClick={() => handleSectionChange('boot_outer')}
                >
                  <span>부트 옵션</span>
                </div>
                
              </div>


                    <form action="#">
                        {/* 일반 */}
                        <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                            </div>
                            <div className="edit_third_content">
                                <div>
                                    <span>하드디스크</span>
                                </div>
                                <div>
                                    <button>연결</button>
                                    <button>생성</button>
                                    <div className='flex'>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>
                            <div className="edit_fourth_content">
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                    
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>

                        {/* 시스템 */}
                        <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                            
                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="memory_size">메모리 크기</label>
                                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">최대 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="total_cpu" value="1" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                        <span>고급 매개 변수</span>
                                    </div>
                                </div>
                                <div style={{ fontWeight: 600 }}>일반</div>
                                <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                    <div>
                                        <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <select id="time_offset">
                                        <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        {/* 콘솔(삭ㅈ제) */}
                        {/* <div id="console_outer" style={{ display: activeSection === 'console_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="res_alloc_checkbox" style={{ marginBottom: 0 }}>
                                <span>그래픽 콘솔</span>
                                <div>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                    <label htmlFor="memory_balloon">헤드리스(headless)모드</label>
                                    <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div style={{ paddingTop: 0 }}>
                                    <label htmlFor="memory_size">비디오 유형</label>
                                    <input type="text" id="memory_size" value="VGA" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">그래픽 프로토콜</label>
                                    </div>
                                    <input type="text" id="max_memory" value="VNC" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">VNC 키보드 레이아웃</label>
                                    </div>
                                    <input type="text" id="actual_memory" value="기본값[en-us]" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">콘솔 분리 작업</label>
                                    </div>
                                    <input type="text" id="total_cpu" value="화면 잠금" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="disconnect_action_delay">Disconnect Action Delay in Minutes</label>
                                    </div>
                                    <input type="text" id="disconnect_action_delay" value="0" disabled />
                                </div>
                                <div id="monitor">
                                    <label htmlFor="screen">모니터</label>
                                    <select id="screen">
                                        <option value="test02">1</option>
                                    </select>
                                </div>
                            </div>

                            <div className="console_checkboxs">
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">USB활성화</label>
                                </div>
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">스마트카드 사용가능</label>
                                </div>
                                <span>단일 로그인 방식</span>
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                    <label htmlFor="memory_balloon">USB활성화</label>
                                </div>
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                    <label htmlFor="memory_balloon">스마트카드 사용가능</label>
                                </div>
                            </div>
                        </div> */}

                        {/* 호스트 */}
                        <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                         

                            <div id="host_second_content">
                                <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                <div className="form_checks">
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                        <label className="form-check-label" htmlFor="flexRadioDefault1">
                                            클러스터 내의 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                            <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                특정 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <select id="specific_host_select">
                                                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="host_checkboxs">
                                    <span>CPU 옵션:</span>
                                    <div className="host_checkbox">
                                        <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                        <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                   
                                </div>
                            </div>

                            <div id="host_third_content">
                                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                <div>
                                    <div>
                                        <span>마이그레이션 모드</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_mode">
                                        <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>마이그레이션 정책</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_policy">
                                        <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <div>
                                        <span>Parallel Migrations</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="parallel_migrations" readOnly>
                                        <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                    </select>
                                </div>
                               
                            </div>
                        </div>

                        {/* 고가용성 */}
                        <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                            {/* <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div> */}

                            <div id="ha_mode_second_content">
                                <div className="checkbox_group">
                                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                    <label className="check_label" htmlFor="ha_mode_box">
                                        고가용성
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <span>가상 머신 임대 대상 스토리지 도메인</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="no_lease" disabled>
                                        <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>재개 동작</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="force_shutdown">
                                        <option value="강제 종료">강제 종료</option>
                                    </select>
                                </div>
                                <div className="ha_mode_article">
                                    <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                    <div>
                                        <span>우선 순위</span>
                                        <select id="priority">
                                            <option value="낮음">낮음</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="ha_mode_article">
                                    <span>위치독</span>
                                    <div>
                                        <span>위치독 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>위치독 작업</span>
                                        <select id="watchdog_action" disabled>
                                            <option value="없음">없음</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 리소스 할당 */}
                        <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                            {/* <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div> */}

                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                    <div className='cpu_res_box'>
                                        <span>CPU 프로파일</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU 공유</span>
                                        <div id="cpu_sharing">
                                            <select id="watchdog_action" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU Pinning Policy</span>
                                        <select id="watchdog_action">
                                            <option value="없음">None</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <div>
                                            <span>CPU 피닝 토폴로지</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" disabled />
                                    </div>
                                </div>

                                <span style={{ fontWeight: 600 }}>메모리 할당:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                                    </div>
                                
                                </div>

                                <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                    </div>
                                    <div>
                                        <input type="text" />
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                </div>

                                <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
                                
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                                        <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                                        <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='cpu_res_box mb-1' >
                                        <span>VirtIO-SCSI Multi Queues</span>
                                        <div id="cpu_sharing">
                                            <select id="multi_queue_status" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    
                            </div>
                        </div>

                        {/* 부트 옵션 */}
                        <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                                    <div className='cpu_res_box'>
                                        <span>첫 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">하드디스크</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>두 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="boot_checkboxs">
                                    <div>
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group mb-1.5'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>

                                <div className="cpu_res border-t border-gray-500 py-1">
                                    <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_path">커널 경로</label>
                                        <input type="text" id="kernel_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="initrd_path">initrd 경로</label>
                                        <input type="text" id="initrd_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_parameters">커널 매개 변수</label>
                                        <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                                    </div>


                                </div>
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>
            </Modal>
            {/* 가상머신(편집)팝업 */}
            <Modal
                 isOpen={activePopup === 'vm_edit'}
                onRequestClose={closePopup}
                contentLabel="가상머신 편집"
                className="edit_popup"
                overlayClassName="edit_popup_outer"
                shouldCloseOnOverlayClick={false}
            >
 <div id="edit_popup">
            <div className="popup_header">
              <h1>가상머신 편집</h1>
              <button onClick={closePopup}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            
            <div className="edit_body">
            <div className="edit_aside">
                <div
                  className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
                  id="common_outer_btn"
                  onClick={() => handleSectionChange('common_outer')}
                >
                  <span>일반</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
                  id="system_outer_btn"
                  onClick={() => handleSectionChange('system_outer')}
                >
                  <span>시스템</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
                  id="host_outer_btn"
                  onClick={() => handleSectionChange('host_outer')}
                >
                  <span>호스트</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
                  id="ha_mode_outer_btn"
                  onClick={() => handleSectionChange('ha_mode_outer')}
                >
                  <span>고가용성</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
                  id="res_alloc_outer_btn"
                  onClick={() => handleSectionChange('res_alloc_outer')}
                >
                  <span>리소스 할당</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
                  id="boot_outer_btn"
                  onClick={() => handleSectionChange('boot_outer')}
                >
                  <span>부트 옵션</span>
                </div>
              </div>


                    <form action="#">
                        {/* 일반 */}
                        <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                            </div>
                            <div className="instance_image">
                                <span>인스턴스 이미지</span><br/>
                                <div>
                                    <div>on20-apm_Disk1_c1: (2 GB) 기존</div>
                                    <div className='flex'>
                                        <button className='mr-1'>편집</button>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>

                            <span className='edit_fourth_span'>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</span>
                            <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                               
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>

                        {/* 시스템 */}
                        <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                            
                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="memory_size">메모리 크기</label>
                                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">최대 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="total_cpu" value="1" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                        <span>고급 매개 변수</span>
                                    </div>
                                </div>
                                <div style={{ fontWeight: 600 }}>일반</div>
                                <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                    <div>
                                        <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <select id="time_offset">
                                        <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                    </select>
                                </div>
                            </div>
                        </div>
   
                        {/* 호스트 */}
                        <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                            <div id="host_second_content">
                                <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                <div className="form_checks">
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                        <label className="form-check-label" htmlFor="flexRadioDefault1">
                                            클러스터 내의 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                            <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                특정 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <select id="specific_host_select">
                                                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="host_checkboxs">
                                    <span>CPU 옵션:</span>
                                    <div className="host_checkbox">
                                        <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                        <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                   
                                </div>
                            </div>

                            <div id="host_third_content">
                                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                <div>
                                    <div>
                                        <span>마이그레이션 모드</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_mode">
                                        <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>마이그레이션 정책</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_policy">
                                        <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <div>
                                        <span>Parallel Migrations</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="parallel_migrations" readOnly>
                                        <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                    </select>
                                </div>
                               
                            </div>
                        </div>

                        {/* 고가용성 */}
                        <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                            <div id="ha_mode_second_content">
                                <div className="checkbox_group">
                                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                    <label className="check_label" htmlFor="ha_mode_box">
                                        고가용성
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <span>가상 머신 임대 대상 스토리지 도메인</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="no_lease" disabled>
                                        <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>재개 동작</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="force_shutdown">
                                        <option value="강제 종료">강제 종료</option>
                                    </select>
                                </div>
                                <div className="ha_mode_article">
                                    <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                    <div>
                                        <span>우선 순위</span>
                                        <select id="priority">
                                            <option value="낮음">낮음</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="ha_mode_article">
                                    <span>위치독</span>
                                    <div>
                                        <span>위치독 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>위치독 작업</span>
                                        <select id="watchdog_action" disabled>
                                            <option value="없음">없음</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 리소스 할당 */}
                        <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                    <div className='cpu_res_box'>
                                        <span>CPU 프로파일</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU 공유</span>
                                        <div id="cpu_sharing">
                                            <select id="watchdog_action" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU Pinning Policy</span>
                                        <select id="watchdog_action">
                                            <option value="없음">None</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <div>
                                            <span>CPU 피닝 토폴로지</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" disabled />
                                    </div>
                                </div>

                                <span style={{ fontWeight: 600 }}>메모리 할당:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                                    </div>
                                
                                </div>

                                <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                    </div>
                                    <div>
                                        <input type="text" />
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                </div>

                                <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
                                
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                                        <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                                        <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='cpu_res_box mb-1' >
                                        <span>VirtIO-SCSI Multi Queues</span>
                                        <div id="cpu_sharing">
                                            <select id="multi_queue_status" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    
                            </div>
                        </div>

                        {/* 부트 옵션 */}
                        <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                                    <div className='cpu_res_box'>
                                        <span>첫 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">하드디스크</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>두 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="boot_checkboxs">
                                    <div>
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group mb-1.5'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>

                                <div className="cpu_res border-t border-gray-500 py-1">
                                    <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_path">커널 경로</label>
                                        <input type="text" id="kernel_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="initrd_path">initrd 경로</label>
                                        <input type="text" id="initrd_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_parameters">커널 매개 변수</label>
                                        <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                                    </div>


                                </div>
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>
            </Modal>



              {/* 네트워크(새로 만들기) */}
              <Modal
                isOpen={activePopup === 'makeNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_new_popup">
                    <div className="popup_header">
                        <h1>새 논리적 네트워크</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>
                    
                    <div className='flex'>
                        <div className="network_new_nav">
                            <div
                                id="network_new_common_btn"
                                className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_common_btn')}
                            >
                                일반
                            </div>
                            <div
                                id="network_new_cluster_btn"
                                className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_cluster_btn')}
                            >
                                클러스터
                            </div>
                            <div
                                id="network_new_vnic_btn"
                                className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_vnic_btn')}
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
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 네트워크(가져오기) */}
            <Modal
                isOpen={activePopup === 'getNetwork'}
                onRequestClose={closePopup}
                contentLabel="가져오기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_bring_popup">
                    <div className="popup_header">
                        <h1>네트워크 가져오기</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="network_form_group">
                        <label htmlFor="cluster" style={{ fontSize: '0.33rem',fontWeight:'600' }}>네트워크 공급자</label>
                        <select id="cluster">
                            <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                        </select>
                    </div>

                    <div id="network_bring_table_outer">
                        <span className='font-bold'>공급자 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="provider_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화' },
                                ]}
                            />
                        </div>
                    </div>

                    <div id="network_bring_table_outer">
                        <span>가져올 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                    { header: '데이터 센터', accessor: 'dataCenter' },
                                    { header: '모두허용', accessor: 'allowAll' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="import_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화', dataCenter: '디스크 활성화', allowAll: '디스크 활성화' },
                                ]}
                            />
                        </div>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>가져오기</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
              {/* 네트워크(편집) 팝업 */}
              <Modal
                isOpen={activePopup === 'editNetwork'}
                onRequestClose={closePopup}
                contentLabel="편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_edit_popup">
                    <div className="popup_header">
                        <h1>논리 네트워크 편집</h1>
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

              {/*삭제 팝업 */}
      <Modal
        isOpen={activePopup === 'delete'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_delete_popup">
          <div className="popup_header">
            <h1>디스크 삭제</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='disk_delete_box'>
            <div>
              <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
              <span>다음 항목을 삭제하시겠습니까?</span>
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
