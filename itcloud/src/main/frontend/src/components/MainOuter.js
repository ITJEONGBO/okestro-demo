import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Modal from 'react-modal';
import { adjustFontSize } from '../UIEvent';
import { useAllTreeNavigations, } from '../api/RQHook';
import './MainOuter.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
    faThLarge, faDesktop, faServer, faDatabase, faCog, faBuilding, faUser, faMicrochip, faChevronLeft, faChevronRight,faChevronDown,
    faListUl,
    faFileEdit,
    faEarthAmericas,
    faLayerGroup
} from '@fortawesome/free-solid-svg-icons'

const MainOuter = ({ children }) => {
  const [selected, setSelected] = useState(() => localStorage.getItem('selected') || 'dashboard');
  const [selectedDiv, setSelectedDiv] = useState('data_center');
  const [selectedDisk, setSelectedDisk] = useState(null);
  const [savedStates, setSavedStates] = useState({
    computing: null,
    storage: null,
    network: null,
    event: null,
    setting: null,
  });
  const [shouldRefresh, setShouldRefresh] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
   
  const [asidePopupVisible, setAsidePopupVisible] = useState(true);
  const [asidePopupBackgroundColor, setAsidePopupBackgroundColor] = useState({
    dashboard: '',
    computing: '',
    storage: '',
    network: '',
    setting: '',
    event: '',
    default: 'rgb(218, 236, 245)'
  });

  const [isSecondVisibleStorage, setIsSecondVisibleStorage] = useState(false);
  const [isLastVisibleStorage, setIsLastVisibleStorage] = useState(false);
  const [isSecondVisibleNetwork, setIsSecondVisibleNetwork] = useState(false);
  const [isSecondVisible, setIsSecondVisible] = useState(false);
  const [isThirdVisible, setIsThirdVisible] = useState(false);
  const [isFourthVisible, setIsFourthVisible] = useState(false);
  const [isLastVisible, setIsLastVisible] = useState(false);
    
  const [contextMenuVisible, setContextMenuVisible] = useState(false);
  const [contextMenuPosition, setContextMenuPosition] = useState({ x: 0, y: 0 });
  const [contextMenuTarget, setContextMenuTarget] = useState(null);
  const [hoverTarget, setHoverTarget] = useState(null);
  const [activePopup, setActivePopup] = useState(null);
  const [activeSettingForm, setActiveSettingForm] = useState('part');

  const [activeSection, setActiveSection] = useState('general');

  const [isThirdVisibleNetwork, setIsThirdVisibleNetwork] = useState(false); // 3단계 열림/닫힘 상태
  const [isFourthVisibleNetwork, setIsFourthVisibleNetwork] = useState(false); // 4단계 열림/닫힘 상태


   //클러스터(컴퓨팅)api
   const { 
    data: navClusters,          
    status: navClustersStatus,   
    isRefetching: isNavClustersRefetching,
    refetch: navClustersRefetch, 
    isError: isNavClustersError, 
    error: navClustersError,    
    isLoading: isNavClustersLoading, 
  } = useAllTreeNavigations('cluster');
  
  useEffect(() => {
    navClustersRefetch();
  }, [setShouldRefresh, navClustersRefetch]);

  //네트워크api
  const { 
    data: navNetworks,
    status: navNetworksStatus,
    isRefetching: isNavNetworksRefetching,
    refetch: navNetworksRefetch, 
    isError: isNavNetworkskError,
    error: navNetworksError, 
    isLoading: isNavNetworkskLoaindg,
  } = useAllTreeNavigations('network');
  useEffect(() => {
    navNetworksRefetch()
  }, [setShouldRefresh, navNetworksRefetch])
  // 스토리지도메인api
    const { 
        data: navStorageDomains,         
        status: navStorageDomainsStatus,   
        isRefetching: isNavStorageDomainsRefetching,
        refetch: navStorageDomainsRefetch, 
        isError: isNavStorageDomainsError,
        error: navStorageDomainsError,    
        isLoading: isNavStorageDomainsLoading,
    } = useAllTreeNavigations('storagedomain'); 
    useEffect(() => {
        navStorageDomainsRefetch(); 
    }, [setShouldRefresh, navStorageDomainsRefetch]);



  
  // dashboard일때 aside닫기
  // useEffect(() => {
  //   const pathParts = location.pathname.split('/');
  //   const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);
  
  //   const updateSelectedState = (section, div, secondVisible = false, thirdVisible = false, lastVisible = false) => {
  //     setSelected(section);
  //     setSelectedDiv(div);
  //   };

  //   if (location.pathname.includes('/computing')) {
  //     if (location.pathname.includes('/computing/rutil-manager')) {
  //       updateSelectedState('computing', 'rutil-manager', true, true, true);
  //     } else if (location.pathname === '/computing/host') {
  //       updateSelectedState('computing', 'host', true, true, true);
  //     } else if (location.pathname.includes('/computing/clusters')) {
  //       updateSelectedState('computing', 'clusters', true, true);
  //     } else if (location.pathname.includes('/computing/data-center')) {
  //       updateSelectedState('computing', 'data-center', true);
  //     } else if (location.pathname.includes('/computing/host')) {
  //       updateSelectedState('computing', 'host', true, true, true);
  //     } else {
  //       updateSelectedState('computing', null);
  //     }
  //   } else if (location.pathname.includes('/storage')) {
  //     if (location.pathname.includes('/storage-domain/:id')) {
  //       updateSelectedState('storage', 'storage_domain/:id', true);
  //     } else if (location.pathname.includes('/storages/disks')) {
  //       updateSelectedState('storage', lastPart, true, true);
  //     } else {
  //       updateSelectedState('storage', 'data-center');
  //     }
  //   } else if (location.pathname.includes('/networks')) {
  //     if (location.pathname === '/networks' || lastPart === 'network') {
  //       updateSelectedState('network', 'data-centerdd', true);
  //     } else {
  //       updateSelectedState('network', lastPart, true);
  //     }
  //   } else if (location.pathname.includes('/events')) {
  //     updateSelectedState('event', 'default');
  //   } else if (location.pathname.includes('/settings')) {
  //     updateSelectedState('setting', 'default');
  //   } else {
  //     updateSelectedState('dashboard', 'default');  // 대시보드일 때도 aside 팝업을 열리게 설정
  //   }
  // }, [location]);
  
  
  
  


  




  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

    // 네트워크 섹션에서 사용하는 것과 유사한 로직으로 편집
    // useEffect(() => {
    //     const pathParts = location.pathname.split('/');
    //     const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);

    //     if (location.pathname.includes('/storages/disks')) {
    //         setSelected('storage');
    //         setSelectedDiv(null); 
    //         setSelectedDisk(lastPart);
    //     } else if (location.pathname.includes('/storage-domain/:id')) {
    //         setSelected('storage');
    //         setSelectedDiv('storage_domain');
    //         setSelectedDisk(null); 
    //     } else if (location.pathname.includes('/storage')) {
    //         setSelected('storage');
    //         setSelectedDiv('data_centerdd');  
    //         setSelectedDisk(null); 
    //     }
    // }, [location]);

    const handleDetailClickStorage = (diskName) => {
        if (selectedDisk !== diskName) {
            setSelectedDisk(diskName);
            setSelectedDiv(null);
            navigate(`/storages/disks/${diskName}`);
        }
    };

 

    const handleClick = (id) => {
      setSelected(id);  // selected 값을 변경
      toggleAsidePopup(id);  // 색상을 변경하는 로직 호출
      setAsidePopupVisible(true);  // aside_popup을 열리게 함
      localStorage.setItem('selected', id); // 로컬 스토리지에 저장

      if (selected !== id) {
        setSavedStates((prevState) => ({
          ...prevState,
          [selected]: savedStates[selected], // 이전 섹션 상태 저장
        }));
      }
      setSelected(id);
      localStorage.setItem('selected', id); // 선택 상태를 로컬 스토리지에 저장
  };


  useEffect(() => {
    navNetworksRefetch();
  }, [navNetworksRefetch]);

    // 네트워크 열림/닫힘 토글 함수
  const [openNetworks, setOpenNetworks] = useState({});
  const toggleNetwork = (networkId) => {
    setOpenNetworks((prevState) => ({
      ...prevState,
      [networkId]: !prevState[networkId], // 해당 네트워크만 열림/닫힘 토글
    }));
  };
    // 데이터 센터 열림/닫힘 토글 함수
  const [openDataCenters, setOpenDataCenters] = useState({});
  const toggleDataCenter = (dataCenterId) => {
    setOpenDataCenters((prevState) => ({
      ...prevState,
      [dataCenterId]: !prevState[dataCenterId], // 해당 데이터 센터만 열림/닫힘 토글
    }));
  };

  // 클러스터 
  const [openClusters, setOpenClusters] = useState({})
  const [openHosts, setOpenHosts] = useState({});
  const toggleCluster = (id) => {
      setOpenClusters((prev) => ({ ...prev, [id]: !prev[id] }));
  };
  const toggleHost = (id) => {
      setOpenHosts((prev) => ({ ...prev, [id]: !prev[id] }));
  };


    const [openDomains, setOpenDomains] = useState({});
    const toggleDomain = (domainId) => {
        setOpenDomains((prevState) => ({
            ...prevState,
            [domainId]: !prevState[domainId], 
        }));
    };


  const toggleAsidePopup = (id) => {
    const newBackgroundColor = {
        dashboard: '',
        computing: '',
        storage: '',
        network: '',
        setting: '',
        event: '',
        default: ''
    };

    // selected 값에 따라 색상을 변경하는 로직
    if (id === 'dashboard') {
        newBackgroundColor.dashboard = 'rgb(218, 236, 245)';
    } else if (id === 'computing') {
        newBackgroundColor.computing = 'rgb(218, 236, 245)';
    } else if (id === 'storage') {
        newBackgroundColor.storage = 'rgb(218, 236, 245)';
    } else if (id === 'network') {
        newBackgroundColor.network = 'rgb(218, 236, 245)';
    } else if (id === 'event') {
        newBackgroundColor.event = 'rgb(218, 236, 245)';
    } else if (id === 'setting') {
        newBackgroundColor.setting = 'rgb(218, 236, 245)';
    }

    setAsidePopupBackgroundColor(newBackgroundColor);
};

// 저장된 항목에 맞춰 배경색 초기화
useEffect(() => {
  const savedSelected = localStorage.getItem('selected');
  if (savedSelected) {
    toggleAsidePopup(savedSelected); 
  }
}, []);

    // 스토리지
    const handleFirstDivClickStorage = () => {
        if (selectedDiv !== 'data_center') {
            setSelectedDiv('data_center');
            setSelectedDisk(null);
            navigate('/storage');
        }
    };
 

    const getClassNames = (id) => {
        return selected === id ? 'selected' : '';
    };

    const handleMouseEnter = (target) => {
        setHoverTarget(target);
    };

    const handleMouseLeave = () => {
        setHoverTarget(null);
    };

    const handleContextMenu = (event, target) => {
        event.preventDefault();
        setContextMenuPosition({ x: event.clientX, y: event.clientY });
        setContextMenuVisible(true);
        setContextMenuTarget(target);
    };

    const handleMainClick = () => {
        setContextMenuVisible(false);
        setContextMenuTarget(null);
    };

    const handleAsidePopupBtnClick = () => {
        setAsidePopupVisible(false);
    };

    const handleSettingNavClick = (form) => {
        setActiveSettingForm(form);
    };
    const handleSettingIconClick = () => {
        navigate('/settings');
        setSelected('setting');  // 'setting'이 선택되었음을 설정
        toggleAsidePopup('setting');  // 배경색을 파랑으로 변경하기 위해 호출
    };
    

    const handleUserIconClick = (name) => {
        navigate(`/computing/host/${name}`);
        setSelectedDiv(name);
    };

    const handleMicrochipIconClick = (name) => {
        setSelectedDiv(name);
   
        setIsThirdVisible(true);
        navigate(`/computing/${name}`);
    };

    const handleStorageDiskClick = (name) => {
        setSelectedDiv(name);
   
        setIsThirdVisible(true);
        navigate(`/storages/${name}`);
    };
    return (
      <div id="main_outer" onClick={handleMainClick}>
        <div id="aside_outer" style={{ width: asidePopupVisible ? '20%' : '3%' }}>
            <div id="aside">
                <div id="nav">
                    <Link to='/dashboard' className="link-no-underline">
                        <div
                            id="aside_popup_dashboard_btn"
                            className={getClassNames('dashboard')}
                            onClick={() => handleClick('dashboard')}
                            style={{ backgroundColor: asidePopupBackgroundColor.dashboard }}
                        >
                            <FontAwesomeIcon icon={faThLarge} fixedWidth/>
                        </div>
                    </Link>
                    <Link to='/computing/vms' className="link-no-underline">
                        <div
                            id="aside_popup_machine_btn"
                            className={getClassNames('computing')}
                            onClick={() => handleClick('computing')}
                            style={{ backgroundColor: asidePopupBackgroundColor.computing }}
                        >
                            <FontAwesomeIcon icon={faDesktop} fixedWidth/>
                        </div>
                    </Link>
                    <Link to='/networks' className="link-no-underline">
                        <div
                            id="aside_popup_network_btn"
                            className={getClassNames('network')}
                            onClick={() => handleClick('network')}
                            style={{ backgroundColor: asidePopupBackgroundColor.network }}
                        >
                           <FontAwesomeIcon icon={faServer} fixedWidth/>
                        </div>
                    </Link>
                    <Link to='/storage' className="link-no-underline">
                        <div
                            id="aside_popup_storage_btn"
                            className={getClassNames('storage')}
                            onClick={() => handleClick('storage')}
                            style={{ backgroundColor: asidePopupBackgroundColor.storage }}
                        >
                           <FontAwesomeIcon icon={faDatabase} fixedWidth/>
                        </div>
                    </Link>
                    <Link to='/events' className="link-no-underline">
                        <div
                            id="aside_popup_storage_btn"
                            className={getClassNames('event')}
                            onClick={() => handleClick('event')}
                            style={{ backgroundColor: asidePopupBackgroundColor.event }}
                        >
                           <FontAwesomeIcon icon={faListUl} fixedWidth/>
                        </div>
                    </Link>
                </div>
                <Link to='/settings' className="link-no-underline">
                    <div id="setting_icon" 
                    className={getClassNames('setting')}
                    style={{ backgroundColor: asidePopupBackgroundColor.setting }} onClick={handleSettingIconClick}>
                    <FontAwesomeIcon icon={faCog} fixedWidth/>
                    </div>
                </Link>


            </div>
            <div id="aside_popup" style={{ display: asidePopupVisible ? 'block' : 'none' }}>
                <button id='aside_popup_btn' onClick={handleAsidePopupBtnClick}><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>

                {/*가상머신*/} 
                {selected === 'computing' && (
                  <div id="virtual_machine_chart">
                      {/* 첫 번째 레벨 (Rutil Manager) */}
                      <div
                          className="aside_popup_content"
                          id="aside_popup_first"
                          style={{ backgroundColor: selectedDiv === 'rutil-manager' ? 'rgb(218, 236, 245)' : '' }}
                          onClick={() => {
                              if (selectedDiv !== 'rutil-manager') {
                                  setSelectedDiv('rutil-manager');
                                  navigate('/computing/rutil-manager');
                              }
                          }}
                      >
                          <FontAwesomeIcon
                              style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                              icon={isSecondVisible ? faChevronDown : faChevronRight}
                              onClick={(e) => {
                                  e.stopPropagation();
                                  setIsSecondVisible(!isSecondVisible);
                                  setIsThirdVisible(false); // 하위 항목들 모두 접기
                                  setIsFourthVisible(false); // 하위 항목들 모두 접기
                                  setIsLastVisible(false);   // 하위 항목들 모두 접기
                              }}
                              fixedWidth
                          />
                          <FontAwesomeIcon icon={faBuilding} fixedWidth />
                          <span>Rutil manager</span>
                      </div>

                      {/* 두 번째 레벨 (Data Center) */}
                      {isSecondVisible && navClusters && navClusters.map((dataCenter) => {
                          const isDataCenterOpen = openDataCenters[dataCenter.id] || false;
                          const hasClusters = Array.isArray(dataCenter.clusters) && dataCenter.clusters.length > 0; // 클러스터 여부
                          return (
                              <div key={dataCenter.id}>
                                  <div
                                      className="aside_popup_second_content"
                                      style={{ 
                                          backgroundColor: selectedDiv === dataCenter.id ? 'rgb(218, 236, 245)' : '', 
                                          paddingLeft: hasClusters ? '0.4rem' : '0.8rem' 
                                      }}
                                      onClick={() => {
                                          setSelectedDiv(dataCenter.id);
                                          navigate(`/computing/datacenters/${dataCenter.id}`);
                                      }}
                                  >
                                      {hasClusters && ( // 클러스터가 있는 경우에만 아이콘 표시
                                          <FontAwesomeIcon
                                              style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                              icon={isDataCenterOpen ? faChevronDown : faChevronRight}
                                              onClick={(e) => {
                                                  e.stopPropagation();
                                                  toggleDataCenter(dataCenter.id); // Data Center 열림/닫힘 토글
                                              }}
                                              fixedWidth
                                          />
                                      )}
                                      <FontAwesomeIcon icon={faLayerGroup} fixedWidth />
                                      <span>{dataCenter.name}</span>
                                  </div>

                                  {/* 세 번째 레벨 (Clusters) */}
                                  {isDataCenterOpen && Array.isArray(dataCenter.clusters) && dataCenter.clusters.map((cluster) => {
                                      const isClusterOpen = openClusters[cluster.id] || false;
                                      const hasHosts = Array.isArray(cluster.hosts) && cluster.hosts.length > 0; // 호스트가 있는지 여부 확인
                                      return (
                                          <div key={cluster.id}>
                                              <div
                                                  className="aside_popup_third_content"
                                                  style={{ 
                                                      backgroundColor: selectedDiv === cluster.id ? 'rgb(218, 236, 245)' : '', 
                                                      paddingLeft: hasHosts ? '0.6rem' : '1rem' // 호스트가 없으면 더 많은 padding 적용
                                                  }}
                                                  onClick={() => {
                                                      setSelectedDiv(cluster.id);
                                                      navigate(`/computing/clusters/${cluster.id}`);
                                                  }}
                                              >
                                              {hasHosts && ( // 호스트가 있는 경우에만 아이콘 표시
                                                      <FontAwesomeIcon
                                                          style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                                          icon={isClusterOpen ? faChevronDown : faChevronRight}
                                                          onClick={(e) => {
                                                              e.stopPropagation();
                                                              toggleCluster(cluster.id); // Cluster 열림/닫힘 토글
                                                          }}
                                                          fixedWidth
                                                      />
                                                  )}
                                                  <FontAwesomeIcon icon={faEarthAmericas} fixedWidth />
                                                  <span>{cluster.name}</span>
                                              </div>

                                              {/* 네 번째 레벨 (Hosts) */}
                                              {isClusterOpen && Array.isArray(cluster.hosts) && cluster.hosts.map((host) => {
                                                  const isHostOpen = openHosts[host.id] || false;
                                                  const hasVMs = Array.isArray(host.vms) && host.vms.length > 0; //vm여부
                                                  return (
                                                      <div key={host.id}>
                                                          <div
                                                              className="aside_popup_fourth_content"
                                                              style={{ 
                                                                  backgroundColor: selectedDiv === host.id ? 'rgb(218, 236, 245)' : '', 
                                                                  paddingLeft: hasVMs ? '0.8rem' : '1.2rem' 
                                                              }}
                                                              onClick={() => {
                                                                  setSelectedDiv(host.id);
                                                                  navigate(`/computing/host/${host.id}`);
                                                              }}
                                                          >
                                                              <FontAwesomeIcon
                                                                  style={{ fontSize: '0.3rem', marginRight: '0.04rem'}}
                                                                  icon={isHostOpen ? faChevronDown : faChevronRight}
                                                                  onClick={(e) => {
                                                                      e.stopPropagation();
                                                                      toggleHost(host.id); // Host 열림/닫힘 토글
                                                                  }}
                                                                  fixedWidth
                                                              />
                                                              <FontAwesomeIcon icon={faUser} fixedWidth />
                                                              <span>{host.name}</span>
                                                          </div>

                                                          {/* 다섯 번째 레벨 (VMs) */}
                                                          {isHostOpen && Array.isArray(host.vms) && host.vms.map((vm) => (
                                                              <div
                                                                  key={vm.id}
                                                                  className="aside_popup_last_content"
                                                                  style={{ backgroundColor: selectedDiv === vm.id ? 'rgb(218, 236, 245)': '',paddingLeft: '1.5rem'}}
                                                                  onClick={() => {
                                                                      setSelectedDiv(vm.id);
                                                                      navigate(`/computing/vms/${vm.id}`);
                                                                  }}
                                                              >
                                                                  <FontAwesomeIcon icon={faMicrochip} fixedWidth />
                                                                  <span>{vm.name}</span>
                                                              </div>
                                                          ))}
                                                      </div>
                                                  );
                                              })}
                                          </div>
                                      );
                                  })}
                              </div>
                          );
                      })}
                  </div>
                )}

                {/* 네트워크 섹션 */}
                {selected === 'network' && (
                    <div id="network_chart">
                        {/* 첫 번째 레벨 (Rutil Manager) */}
                        <div
                            className="aside_popup_content"
                            id="aside_popup_first"
                            style={{ backgroundColor: selectedDiv === 'rutil-manager' ? 'rgb(218, 236, 245)' : '' }}
                            onClick={() => {
                                setSelectedDiv('rutil-manager');
                                navigate('/computing/rutil-manager');
                            }}
                        >
                            <FontAwesomeIcon
                                style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                icon={openDataCenters.network ? faChevronDown : faChevronRight}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setOpenDataCenters((prev) => ({ ...prev, network: !prev.network })); // 2단계 열림/닫힘 토글
                                }}
                                fixedWidth
                            />
                            <FontAwesomeIcon icon={faBuilding} fixedWidth />
                            <span>Rutil manager</span>
                        </div>

                        {/* 두 번째 레벨 (Data Center) */}
                        {openDataCenters.network && navNetworks && navNetworks.map((dataCenter) => {
                            const hasNetworks = Array.isArray(dataCenter.networks) && dataCenter.networks.length > 0; // 네트워크 여부
                            return (
                                <div key={dataCenter.id}>
                                    <div
                                        className="aside_popup_second_content"
                                        style={{ 
                                            backgroundColor: selectedDiv === dataCenter.id ? 'rgb(218, 236, 245)' : '', 
                                            paddingLeft: hasNetworks ? '0.4rem' : '0.8rem'
                                        }}
                                        onClick={() => {
                                            setSelectedDiv(dataCenter.id);
                                            navigate(`/computing/datacenters/${dataCenter.id}`);
                                        }}
                                    >
                                        {hasNetworks && (
                                            <FontAwesomeIcon
                                                style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                                icon={openDataCenters[dataCenter.id] ? faChevronDown : faChevronRight}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    toggleDataCenter(dataCenter.id);
                                                }}
                                                fixedWidth
                                            />
                                        )}
                                        <FontAwesomeIcon icon={faLayerGroup} fixedWidth />
                                        <span>{dataCenter.name}</span>
                                    </div>

                                    {/* 세 번째 레벨 (Networks) */}
                                    {openDataCenters[dataCenter.id] && hasNetworks && dataCenter.networks.map((network) => (
                                        <div
                                            key={network.id}
                                            className="aside_popup_third_content"
                                            style={{ 
                                                backgroundColor: selectedDiv === network.id ? 'rgb(218, 236, 245)' : '', 
                                                paddingLeft: '1rem' 
                                            }}
                                            onClick={() => {
                                                setSelectedDiv(network.id);
                                                navigate(`/networks/${network.id}`);
                                            }}
                                        >
                                            <FontAwesomeIcon icon={faFileEdit} style={{ fontSize: '0.34rem', marginRight: '0.05rem' }} fixedWidth />
                                            <span>{network.name}</span>
                                        </div>
                                    ))}
                                </div>
                            );
                        })}
                    </div>
                )}

                {/* 스토리지 섹션 */} 
                {selected === 'storage' && (
                    <div id="storage_chart">
                        {/* 첫 번째 레벨 (Rutil Manager) */}
                        <div
                            className="aside_popup_content"
                            id="aside_popup_first"
                            style={{ backgroundColor: selectedDiv === 'rutil-manager' ? 'rgb(218, 236, 245)' : '' }}
                            onClick={() => {
                                if (selectedDiv !== 'rutil-manager') {
                                    setSelectedDiv('rutil-manager');
                                    navigate('/computing/rutil-manager');
                                    setIsSecondVisible(false); // 모든 하위 섹션 접기
                                    setIsSecondVisibleStorage(false);
                                    setIsLastVisibleStorage(false);
                                }
                            }}
                        >
                            <FontAwesomeIcon
                                style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                icon={isSecondVisible ? faChevronDown : faChevronRight}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setIsSecondVisible(!isSecondVisible); // 2단계 열림/닫힘 토글
                                    setIsSecondVisibleStorage(false);
                                    setIsLastVisibleStorage(false);
                                }}
                                fixedWidth
                            />
                            <FontAwesomeIcon icon={faBuilding} fixedWidth />
                            <span>Rutil manager</span>
                        </div>

                        {/* 두 번째 레벨 (Data Center) */}
                        {isSecondVisible && navStorageDomains && navStorageDomains.map((dataCenter) => {
                            const isDataCenterOpen = openDataCenters[dataCenter.id] || false;
                            const hasDomains = Array.isArray(dataCenter.storageDomains) && dataCenter.storageDomains.length > 0;
                            return (
                                <div key={dataCenter.id}>
                                    <div
                                        className="aside_popup_second_content"
                                        style={{
                                            backgroundColor: selectedDiv === dataCenter.id ? 'rgb(218, 236, 245)' : '',
                                            paddingLeft: hasDomains ? '0.4rem' : '0.8rem'
                                        }}
                                        onClick={() => {
                                            setSelectedDiv(dataCenter.id);
                                            navigate(`/computing/datacenters/${dataCenter.id}`);
                                        }}
                                    >
                                        {hasDomains && (
                                            <FontAwesomeIcon
                                                style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                                icon={isDataCenterOpen ? faChevronDown : faChevronRight}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    toggleDataCenter(dataCenter.id); 
                                                }}
                                                fixedWidth
                                            />
                                        )}
                                        <FontAwesomeIcon icon={faLayerGroup} fixedWidth />
                                        <span>{dataCenter.name}</span>
                                    </div>

                                    {/* 세 번째 레벨 (Storage Domains) */}
                                    {isDataCenterOpen && Array.isArray(dataCenter.storageDomains) && dataCenter.storageDomains.map((domain) => {
                                        const isDomainOpen = openDomains[domain.id] || false;
                                        const hasDisks = Array.isArray(domain.disks) && domain.disks.length > 0; 
                                        return (
                                            <div key={domain.id}>
                                                <div
                                                    className="aside_popup_third_content"
                                                    style={{
                                                        backgroundColor: selectedDiv === domain.id ? 'rgb(218, 236, 245)' : '',
                                                        paddingLeft: hasDisks ? '0.6rem' : '1rem'
                                                    }}
                                                    onClick={() => {
                                                        setSelectedDiv(domain.id);
                                                        navigate(`/storage-domain/${domain.id}`);
                                                    }}
                                                >
                                                    {hasDisks && ( 
                                                        <FontAwesomeIcon
                                                            style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                                            icon={isDomainOpen ? faChevronDown : faChevronRight}
                                                            onClick={(e) => {
                                                                e.stopPropagation();
                                                                toggleDomain(domain.id); 
                                                            }}
                                                            fixedWidth
                                                        />
                                                    )}
                                                    <FontAwesomeIcon icon={faBuilding} fixedWidth />
                                                    <span>{domain.name}</span>
                                                </div>

                                                {/* 네 번째 레벨 (Disks) */}
                                                {isDomainOpen && Array.isArray(domain.disks) && domain.disks.map((disk) => (
                                                    <div
                                                        key={disk.id}
                                                        className="aside_popup_fourth_content"
                                                        style={{
                                                            backgroundColor: selectedDiv === disk.id ? 'rgb(218, 236, 245)' : '',
                                                            paddingLeft: '1.5rem'
                                                        }}
                                                        onClick={() => {
                                                            setSelectedDiv(disk.id);
                                                            navigate(`/storages/disks/${disk.id}`);
                                                        }}
                                                    >
                                                        <FontAwesomeIcon icon={faMicrochip} fixedWidth />
                                                        <span>{disk.name}</span>
                                                    </div>
                                                ))}
                                            </div>
                                        );
                                    })}
                                </div>
                            );
                        })}
                    </div>
                )}

                {/* 이벤트 섹션 */}
                {selected === 'event' && (
                    <></>
                )}
              
            </div>
        </div>

        
        {React.cloneElement(children, { 
            activeSection, 
            setActiveSection, 
            selectedDisk, // 선택된 디스크 이름을 자식 컴포넌트로 전달
            onDiskClick: handleDetailClickStorage // 디스크 클릭 핸들러 전달
        })}

        <div id="context_menu"
                style={{
                display: contextMenuVisible ? 'block' : 'none',
                top: `${contextMenuPosition.y}px`,
                left: `${contextMenuPosition.x}px`
            }}
        >
            <div>새로 만들기</div>
            <div>새로운 도메인</div>
            <div>도메인 가져오기</div>
            <div>도메인 관리</div>
            <div>삭제</div>
            <div>Connections</div>
        </div>



      </div>
    );
}

export default MainOuter;
