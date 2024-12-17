import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { adjustFontSize } from '../UIEvent';
import { useAllTreeNavigations, } from '../api/RQHook';
import './MainOuter.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
    faThLarge, faDesktop, faServer, faDatabase, faCog, faBuilding, faUser, faMicrochip, faChevronLeft, faChevronRight,faChevronDown,
    faListUl,
    faFileEdit,
    faEarthAmericas,
    faLayerGroup,
    faHdd,
    faCloud,
    faCircle,
    faSquare
} from '@fortawesome/free-solid-svg-icons'

const MainOuter = ({ children }) => {

  const [lastSelected, setLastSelected] = useState(null); // 마지막 선택 항목 저장

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
  const [selected, setSelected] = useState(() => localStorage.getItem('selected') || 'dashboard');

  const [isSecondVisibleStorage, setIsSecondVisibleStorage] = useState(false);
  const [isLastVisibleStorage, setIsLastVisibleStorage] = useState(false);
  const [isSecondVisibleNetwork, setIsSecondVisibleNetwork] = useState(false);

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
  const [selectedDiv, setSelectedDiv] = useState(null);
    // url에 따라 맞는버튼 색칠
    useEffect(() => {
        const path = location.pathname;
        
        if (path.includes('/computing')) {
          handleClick('computing');  // /computing이 들어가 있을 때
        } else if (path.includes('/networks')) {
          handleClick('network');    // /networks가 들어가 있을 때
        } else if (path.includes('/storages')) {
          handleClick('storage');    // /storages가 들어가 있을 때
        } else if (path.includes('/events')) {
          handleClick('event');      // /events가 들어가 있을 때
        } else if (path.includes('/settings')) {
          handleClick('setting');    // /settings가 들어가 있을 때
        } else {
          handleClick('dashboard');  // 기본적으로 dashboard로 설정
        }
    }, [location.pathname]);
    
      const getClassNames = (id) => {
        return selectedDiv === id ? 'selected' : ''; // 선택된 항목에 대해 클래스 추가
      };
      
   
      const handleToggleSecondVisible = (e) => {
        e.stopPropagation();
        setIsSecondVisible(!isSecondVisible);
      };
    
      const handleRutilManagerClick = () => {
        let currentSection = '';
        if (selected === 'computing') {
            currentSection = 'computing';
        } else if (selected === 'network') {
            currentSection = 'networks';
        } else if (selected === 'storage') {
            currentSection = 'storages';
        }
    
        if (currentSection) {
            setSelectedDiv('rutil-manager');
            navigate(`/${currentSection}/rutil-manager`);
        }
    };
      useEffect(() => {
        const path = location.pathname;
        const pathParts = path.split('/'); // 경로를 "/"로 나누기
        const lastId = pathParts[pathParts.length - 1]; // 마지막 부분이 ID
    
        // ID에 따라 적절한 항목을 선택 및 강조
        setSelectedDiv(lastId);
      }, [location.pathname]);
// 클러스터(컴퓨팅)api
const { 
    data: navClusters,          
    status: navClustersStatus,   
    isRefetching: isNavClustersRefetching,
    refetch: navClustersRefetch, 
    isError: isNavClustersError, 
    error: navClustersError,    
    isLoading: isNavClustersLoading, 
  } = useAllTreeNavigations('cluster');
  
  // 네트워크 api
  const { 
    data: navNetworks,
    status: navNetworksStatus,
    isRefetching: isNavNetworksRefetching,
    refetch: navNetworksRefetch, 
    isError: isNavNetworkskError,
    error: navNetworksError, 
    isLoading: isNavNetworkskLoaindg,
  } = useAllTreeNavigations('network');
  
  // 스토리지 도메인 api
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
    const fetchData = async () => {
       try {
          await navClustersRefetch(); 
          console.log("Clusters:", navClusters);
       } catch (error) {
          console.error('Error fetching clusters:', error);
       }
    };
    fetchData();
 }, [navClustersRefetch]);
 
 useEffect(() => {
    const fetchData = async () => {
       try {
          await navNetworksRefetch();
          console.log("Networks:", navNetworks);
       } catch (error) {
          console.error('Error fetching networks:', error);
       }
    };
    fetchData();
 }, [navNetworksRefetch]);
 
 useEffect(() => {
    const fetchData = async () => {
       try {
          await navStorageDomainsRefetch();
          console.log("Storage Domains:", navStorageDomains);
       } catch (error) {
          console.error('Error fetching storage domains:', error);
       }
    };
    fetchData();
 }, [navStorageDomainsRefetch]);
 
  
// 새로고침해도 섹션유지-----------------------------
const [isSecondVisible, setIsSecondVisible] = useState(
    JSON.parse(localStorage.getItem('isSecondVisible')) || false
);
const [openDataCenters, setOpenDataCenters] = useState(() => 
    JSON.parse(localStorage.getItem('openDataCenters')) || {}
);
const [openClusters, setOpenClusters] = useState(() => 
    JSON.parse(localStorage.getItem('openClusters')) || {}
);
const [openHosts, setOpenHosts] = useState(() => 
    JSON.parse(localStorage.getItem('openHosts')) || {}
);
const [openDomains, setOpenDomains] = useState(() => 
    JSON.parse(localStorage.getItem('openDomains')) || {}
);
const [openNetworks, setOpenNetworks] = useState(() => 
    JSON.parse(localStorage.getItem('openNetworks')) || {}
);

//처음에 안열릴때
// const [isSecondVisible, setIsSecondVisible] = useState(
//     localStorage.getItem('isSecondVisible') !== null ? JSON.parse(localStorage.getItem('isSecondVisible')) : false
// );
// const [openDataCenters, setOpenDataCenters] = useState(() => 
//     localStorage.getItem('openDataCenters') !== null ? JSON.parse(localStorage.getItem('openDataCenters')) : {}
// );
// const [openClusters, setOpenClusters] = useState(() => 
//     localStorage.getItem('openClusters') !== null ? JSON.parse(localStorage.getItem('openClusters')) : {}
// );
// const [openHosts, setOpenHosts] = useState(() => 
//     localStorage.getItem('openHosts') !== null ? JSON.parse(localStorage.getItem('openHosts')) : {}
// );
// const [openDomains, setOpenDomains] = useState(() => 
//     localStorage.getItem('openDomains') !== null ? JSON.parse(localStorage.getItem('openDomains')) : {}
// );
// const [openNetworks, setOpenNetworks] = useState(() => 
//     localStorage.getItem('openNetworks') !== null ? JSON.parse(localStorage.getItem('openNetworks')) : {}
// );

// 상태가 변경될 때마다 localStorage에 저장
useEffect(() => {
    localStorage.setItem('isSecondVisible', JSON.stringify(isSecondVisible));
}, [isSecondVisible]);

useEffect(() => {
    localStorage.setItem('openDataCenters', JSON.stringify(openDataCenters));
}, [openDataCenters]);

useEffect(() => {
    localStorage.setItem('openClusters', JSON.stringify(openClusters));
}, [openClusters]);

useEffect(() => {
    localStorage.setItem('openHosts', JSON.stringify(openHosts));
}, [openHosts]);

useEffect(() => {
    localStorage.setItem('openDomains', JSON.stringify(openDomains));
}, [openDomains]);

useEffect(() => {
    localStorage.setItem('openNetworks', JSON.stringify(openNetworks));
}, [openNetworks]);



// 열림/닫힘 상태 변경 함수
const toggleDataCenter = (dataCenterId) => {
    setOpenDataCenters(prevState => ({
      ...prevState,
      [dataCenterId]: !prevState[dataCenterId]
    }));
  };
  
  const toggleCluster = (clusterId) => {
    setOpenClusters(prevState => ({
      ...prevState,
      [clusterId]: !prevState[clusterId]
    }));
  };
  
  const toggleHost = (hostId) => {
    setOpenHosts(prevState => ({
      ...prevState,
      [hostId]: !prevState[hostId]
    }));
  };




//-----------------------------


  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

    const handleDetailClickStorage = (diskName) => {
        if (selectedDisk !== diskName) {
            setSelectedDisk(diskName);
            setSelectedDiv(null);
            navigate(`/storages/disks/${diskName}`);
        }
    };

 
    useEffect(() => {
        if (location.pathname === '/') {
            setAsidePopupVisible(false);  // 대시보드일 때 aside_popup을 닫음
        }
    }, [location.pathname]);

    // 대시보드 경로일 때 aside_popup을 열지 않도록 처리
    const handleAsidePopupBtnClick = () => {
        if (location.pathname !== '/') {  // 대시보드 경로가 아닌 경우에만 토글 가능
            setAsidePopupVisible(prev => !prev);
        }
    };

    const handleClick = (id) => {
        setSelected(id);  // 선택한 섹션으로 업데이트
        toggleAsidePopup(id);  // 선택한 섹션의 배경색 설정
        setAsidePopupVisible(true);  // 팝업을 표시
        localStorage.setItem('selected', id);  // 선택한 섹션을 로컬 스토리지에 저장
        
        // 이벤트와 설정을 제외한 경우에만 마지막 선택 항목을 저장
        if (id !== 'event' && id !== 'setting' && id !== 'dashboard') {
            setLastSelected(id);
            localStorage.setItem('lastSelected', id);  // 로컬 스토리지에 저장
        }
    };
      
      const renderAsidePopupContent = () => {
        if (selected === 'event' || selected === 'setting') {
            // 이벤트와 설정에서는 이전에 선택한 섹션의 콘텐츠를 표시
            return lastSelected ? renderAsidePopup(lastSelected) : <div>선택된 내용이 없습니다.</div>;
        }
        return renderAsidePopup(selected); // 현재 선택된 항목의 콘텐츠를 표시
    };

  const renderAsidePopup = (selected) => {
    return (
      <>
{/*가상머신 섹션*/} 
{selected === 'computing' && (
  <div id="virtual_machine_chart">
      {/* 첫 번째 레벨 (Rutil Manager) */}
      <div
        className="aside_popup_content"
        id="aside_popup_first"
        style={{ backgroundColor: getBackgroundColor('rutil-manager') }}
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
              setIsSecondVisible(!isSecondVisible); // Only toggles on icon click
          }}
          fixedWidth
        />
        <FontAwesomeIcon icon={faBuilding} fixedWidth />
        <span>Rutil manager</span>
      </div>

      {/* 두 번째 레벨 (Data Center) */}
      {isSecondVisible && navClusters && navClusters.map((dataCenter) => {
          const isDataCenterOpen = openDataCenters[dataCenter.id] || false;
          const hasClusters = Array.isArray(dataCenter.clusters) && dataCenter.clusters.length > 0;
          return (
              <div key={dataCenter.id}>
                  <div
                      className="aside_popup_second_content"
                      style={{
                        backgroundColor: getBackgroundColor(dataCenter.id),
                          paddingLeft: hasClusters ? '0.4rem' : '0.8rem'
                      }}
                      onClick={() => {
                          setSelectedDiv(dataCenter.id);
                          navigate(`/computing/datacenters/${dataCenter.id}/clusters`);
                      }}
                  >
                      {hasClusters && (
                          <FontAwesomeIcon
                              style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                              icon={isDataCenterOpen ? faChevronDown : faChevronRight}
                              onClick={(e) => {
                                  e.stopPropagation();
                                  toggleDataCenter(dataCenter.id); // Only toggles on icon click
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
                      const hasHosts = Array.isArray(cluster.hosts) && cluster.hosts.length > 0;
                      return (
                          <div key={cluster.id}>
                              <div
                                  className="aside_popup_third_content"
                                  style={{
                                    backgroundColor: getBackgroundColor(cluster.id),
                                      paddingLeft: hasHosts ? '0.6rem' : '1rem'
                                  }}
                                  onClick={() => {
                                      setSelectedDiv(cluster.id);
                                      navigate(`/computing/clusters/${cluster.id}`);
                                  }}
                              >
                                  {hasHosts && (
                                      <FontAwesomeIcon
                                          style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
                                          icon={isClusterOpen ? faChevronDown : faChevronRight}
                                          onClick={(e) => {
                                              e.stopPropagation();
                                              toggleCluster(cluster.id); // Only toggles on icon click
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
                                  const hasVMs = Array.isArray(host.vms) && host.vms.length > 0;
                                  return (
                                      <div key={host.id}>
                                          <div
                                              className="aside_popup_fourth_content"
                                              style={{
                                                backgroundColor: getBackgroundColor(host.id),
                                                  paddingLeft: hasVMs ? '0.8rem' : '1.2rem'
                                              }}
                                              onClick={() => {
                                                  setSelectedDiv(host.id);
                                                  navigate(`/computing/hosts/${host.id}`);
                                              }}
                                          >
                                              {hasVMs && (
                                                  <FontAwesomeIcon
                                                      style={{ fontSize: '0.3rem', marginRight: '0.04rem'}}
                                                      icon={isHostOpen ? faChevronDown : faChevronRight}
                                                      onClick={(e) => {
                                                          e.stopPropagation();
                                                          toggleHost(host.id); // Only toggles on icon click
                                                      }}
                                                      fixedWidth
                                                  />
                                              )}
                                              <FontAwesomeIcon icon={faUser} fixedWidth />
                                              <span>{host.name}</span>
                                          </div>

                                          {/* 다섯 번째 레벨 (VMs) */}
                                          {isHostOpen && Array.isArray(host.vms) && host.vms.map((vm) => (
                                              <div
                                                  key={vm.id}
                                                  className="aside_popup_last_content"
                                                  style={{
                                                    backgroundColor: getBackgroundColor(vm.id),
                                                      paddingLeft: '1.5rem'
                                                  }}
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
                              
                              {/* vmDowns 정보 추가 */}
                              {isClusterOpen && Array.isArray(cluster.vmDowns) && cluster.vmDowns.map((vmDown) => (
                                  <div
                                      key={vmDown.id}
                                      className="aside_popup_fourth_content" 
                                      style={{
                                            backgroundColor: getBackgroundColor(vmDown.id),
                                          paddingLeft: '1.2rem'
                                      }}
                                      onClick={() => {
                                          setSelectedDiv(vmDown.id);
                                          navigate(`/computing/vms/${vmDown.id}`);
                                      }}
                                  >
                                      <div style={{ position: 'relative', display: 'inline-block' }}>
                                          <FontAwesomeIcon icon={faMicrochip} fixedWidth />
                                          <FontAwesomeIcon 
                                              icon={faSquare} 
                                              fixedWidth 
                                              style={{ 
                                              position: 'absolute', 
                                              bottom: '4', 
                                              right: '0', 
                                              fontSize: '0.5em', 
                                              color: 'rgb(200 0 0)' 
                                              }} 
                                          />
                                      </div>
                                      <span>{vmDown.name}</span>
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

{/* 네트워크 섹션 */}
{selected === 'network' && (
  <div id="network_chart">
      {/* 첫 번째 레벨 (Rutil Manager) */}
      <div
          className="aside_popup_content"
          id="aside_popup_first"
          style={{ backgroundColor: getBackgroundColor('rutil-manager') }}
          onClick={() => {
              setSelectedDiv('rutil-manager');
              navigate('/networks/rutil-manager');
          }}
      >
          <FontAwesomeIcon
              style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
              icon={openDataCenters.network ? faChevronDown : faChevronRight}
              onClick={(e) => {
                  e.stopPropagation();
                  setOpenDataCenters((prev) => ({ ...prev, network: !prev.network })); // Toggle only on icon click
              }}
              fixedWidth
          />
          <FontAwesomeIcon icon={faBuilding} fixedWidth />
          <span>Rutil manager</span>
      </div>

      {/* 두 번째 레벨 (Data Center) */}
      {openDataCenters.network && navNetworks && navNetworks.map((dataCenter) => {
          const hasNetworks = Array.isArray(dataCenter.networks) && dataCenter.networks.length > 0;
          return (
              <div key={dataCenter.id}>
                  <div
                      className="aside_popup_second_content"
                      style={{ 
                          backgroundColor: getBackgroundColor(dataCenter.id),
                          paddingLeft: hasNetworks ? '0.4rem' : '0.8rem'
                      }}
                      onClick={() => {
                          setSelectedDiv(dataCenter.id);
                          navigate(`/networks/datacenters/${dataCenter.id}/clusters`);
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
                              backgroundColor: getBackgroundColor(network.id),
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
          style={{ backgroundColor: getBackgroundColor('rutil-manager') }}
          onClick={() => {
              if (selectedDiv !== 'rutil-manager') {
                  setSelectedDiv('rutil-manager');
                  navigate('/storages/rutil-manager');
              }
          }}
      >
          <FontAwesomeIcon
              style={{ fontSize: '0.3rem', marginRight: '0.04rem' }}
              icon={isSecondVisible ? faChevronDown : faChevronRight}
              onClick={(e) => {
                  e.stopPropagation();
                  setIsSecondVisible(!isSecondVisible); // Toggle only on icon click
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
                          backgroundColor: getBackgroundColor(dataCenter.id),
                          paddingLeft: hasDomains ? '0.4rem' : '0.8rem'
                      }}
                      onClick={() => {
                          setSelectedDiv(dataCenter.id);
                          navigate(`/storages/datacenters/${dataCenter.id}/clusters`);
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
                                      backgroundColor: getBackgroundColor(domain.id),
                                      paddingLeft: hasDisks ? '0.6rem' : '1rem'
                                  }}
                                  onClick={() => {
                                      setSelectedDiv(domain.id);
                                      navigate(`/storages/domains/${domain.id}`);
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
                                  <FontAwesomeIcon icon={faCloud} fixedWidth />
                                  <span>{domain.name}</span>
                              </div>
                          </div>
                      );
                  })}
              </div>
          );
      })}
  </div>
)}

      </>
    );
  };
  

  useEffect(() => {
    // 페이지가 처음 로드될 때 기본적으로 dashboard가 선택되도록 설정
    setSelected('dashboard');
    toggleAsidePopup('dashboard');
  }, []);
  useEffect(() => {
    // 페이지가 처음 로드될 때 기본적으로 computing 섹션이 선택되도록 설정
    setSelected('computing');
    toggleAsidePopup('computing');
    setSelectedDiv(null); // 루틸매니저가 선택되지 않도록 초기화
  }, []);



  useEffect(() => {
    navNetworksRefetch();
  }, [navNetworksRefetch]);

    // 네트워크 열림/닫힘 토글 함수

  const toggleNetwork = (networkId) => {
    setOpenNetworks((prevState) => ({
      ...prevState,
      [networkId]: !prevState[networkId], // 해당 네트워크만 열림/닫힘 토글
    }));
  };




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
    const savedLastSelected = localStorage.getItem('lastSelected');

    if (savedSelected) {
        setSelected(savedSelected);
        toggleAsidePopup(savedSelected);
    } else {
        setSelected('dashboard');
        toggleAsidePopup('dashboard');
    }

    if (savedLastSelected) {
        setLastSelected(savedLastSelected);
    }
}, []);
// id포함유무에 따라 배경색결정
const getBackgroundColor = (id) => {
    const path = location.pathname;
    return path.includes(id) ? 'rgb(218, 236, 245)' : '';
};



    const handleMainClick = () => {
        setContextMenuVisible(false);
        setContextMenuTarget(null);
    };

    const handleSettingIconClick = () => {
        navigate('/settings');
        setSelected('setting');  // 'setting'이 선택되었음을 설정
        toggleAsidePopup('setting');  // 배경색을 파랑으로 변경하기 위해 호출
    };

    return (
      <div id="main_outer" onClick={handleMainClick}>
        <div id="aside_outer" style={{ width: asidePopupVisible ? '20%' : '3%' }}>
            <div id="aside">
                <div id="nav">
                    {/*대시보드버튼 */}
                    <Link to='/' className="link-no-underline">
                        <div
                            id="aside_popup_dashboard_btn"
                            className={getClassNames('dashboard')}
                            onClick={() => {
                                if (location.pathname !== '/') {  // 대시보드 경로가 아닌 경우에만 동작
                                    setAsidePopupVisible(true);
                                }
                            }}
                            style={{ backgroundColor: asidePopupBackgroundColor.dashboard }}
                        >
                            <FontAwesomeIcon icon={faThLarge} fixedWidth/>
                        </div>
                    </Link>
                    {/*가상머신 버튼 */}
                    <Link to='/computing/vms' className="link-no-underline">
                        <div
                            id="aside_popup_machine_btn"
                            className={getClassNames('computing')}
                            onClick={() => {
                                handleClick('computing');
                                setSelectedDiv(null); // 선택된 div를 null로 설정하여 루틸 매니저가 선택되지 않도록 함
                            }}
                            style={{ backgroundColor: asidePopupBackgroundColor.computing }}
                        >
                            <FontAwesomeIcon icon={faDesktop} fixedWidth/>
                        </div>
                    </Link>
                    {/*네트워크 버튼 */}
                    <Link to='/networks' className="link-no-underline">
                        <div
                            id="aside_popup_network_btn"
                            className={getClassNames('network')}
                            onClick={() => {
                                handleClick('network');
                                setSelectedDiv(null); // 루틸 매니저 선택을 방지하기 위해 selectedDiv를 null로 설정
                            }}
                            style={{ backgroundColor: asidePopupBackgroundColor.network }}
                        >
                        <FontAwesomeIcon icon={faServer} fixedWidth/>
                        </div>
                    </Link>
                    {/*스토리지 버튼 */}
                    <Link to='/storages/domains' className="link-no-underline">
                        <div
                            id="aside_popup_storage_btn"
                            className={getClassNames('storage')}
                            onClick={() => {
                                handleClick('storage');
                                setSelectedDiv(null); // 루틸 매니저 선택을 방지하기 위해 selectedDiv를 null로 설정
                            }}
                            style={{ backgroundColor: asidePopupBackgroundColor.storage }}
                        >
                        <FontAwesomeIcon icon={faDatabase} fixedWidth/>
                        </div>
                    </Link>
                    {/*이벤트 버튼 */}
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
                <div>
                    <Link to='/settings' className="link-no-underline">
                        <div id="setting_icon" 
                        className={getClassNames('setting')}
                        style={{ backgroundColor: asidePopupBackgroundColor.setting }} onClick={handleSettingIconClick}>
                        <FontAwesomeIcon icon={faCog} fixedWidth/>
                        </div>
                    </Link>
                    <button id='aside_popup_btn' onClick={handleAsidePopupBtnClick}>
                        <FontAwesomeIcon icon={asidePopupVisible ? faChevronLeft : faChevronRight} fixedWidth />
                    </button>
                </div>
            </div>
            <div id="aside_popup" style={{ display: asidePopupVisible ? 'block' : 'none' }}>
                {renderAsidePopupContent()}
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
