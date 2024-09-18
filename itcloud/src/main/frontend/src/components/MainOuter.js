import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Modal from 'react-modal';
import { adjustFontSize } from '../UIEvent';
import { useAllTreeNavigations, } from '../api/RQHook';
import './MainOuter.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
    faThLarge, faDesktop, faServer, faDatabase, faCog
    , faBuilding, faUser, faMicrochip, faChevronLeft, faChevronRight
    , faTimes, faEllipsisV, faHeart, faInfoCircle,
    faChevronDown
} from '@fortawesome/free-solid-svg-icons'

const MainOuter = ({ children }) => {
  const [selected, setSelected] = useState('dashboard');
  const [selectedDiv, setSelectedDiv] = useState('data_center');
  const [selectedDisk, setSelectedDisk] = useState(null);
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
  const [settingPopupOpen, setSettingPopupOpen] = useState(false);
  const [activeSection, setActiveSection] = useState('general');

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

  // dashboard일때 aside닫기
  useEffect(() => {
    const pathParts = location.pathname.split('/');
    const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);

    const updateSelectedState = (section, div, secondVisible = false, thirdVisible = false, lastVisible = false) => {
      setSelected(section);
      setSelectedDiv(div);

      setAsidePopupVisible(true);
      setAsidePopupBackgroundColor({
        dashboard: '',
        computing: section === 'computing' ? 'rgb(218, 236, 245)' : '',
        storage: section === 'storage' ? 'rgb(218, 236, 245)' : '',
        network: section === 'network' ? 'rgb(218, 236, 245)' : '',
        setting: section === 'setting' ? 'rgb(218, 236, 245)' : '',
        default: ''
      });
    };
    
    if (location.pathname.includes('/computing')) {
      if (location.pathname.includes('/computing/HostedEngine')) {
        updateSelectedState('computing', 'HostedEngine', true, true, true);
      } else if (location.pathname === '/computing/host') {  // 정확히 /computing/host 경로일 때만
        updateSelectedState('computing', 'host', true, true, true);
      } else if (location.pathname.includes('/computing/clusters')) {
        updateSelectedState('computing', 'clusters', true, true);
      } else if (location.pathname.includes('/computing/datacenters')) {
        updateSelectedState('computing', 'data_center', true);
      } else if (location.pathname.includes('/computing/vms')) {
        updateSelectedState('computing', 'vms', true, true, true);
      } else {
        updateSelectedState('computing', null);
      }
    } else if (location.pathname.includes('/storage')) {
      if (location.pathname.includes('/storage-domain')) {
        updateSelectedState('storage', 'storage_domain', true);
      } else if (location.pathname.includes('/storage-disk')) {
        updateSelectedState('storage', lastPart, true, true);
      } else {
        updateSelectedState('storage', 'data_center');
      }
    } else if (location.pathname.includes('/networks')) {
      if (location.pathname === '/networks' || lastPart === 'network') {
        updateSelectedState('network', 'default', true);
      } else {
        updateSelectedState('network', lastPart, true);
      }
    } else if (location.pathname.includes('/settings')) {
      updateSelectedState('setting', 'default');
    } else {
      setSelected('dashboard');
      setAsidePopupVisible(false);
      setAsidePopupBackgroundColor({
        dashboard: 'rgb(218, 236, 245)',
        computing: '',
        storage: '',
        network: '',
        setting: '',
        default: ''
      });
    }
  }, [location]);
  
  

  // setting일때 aside닫기
  useEffect(() => {
    const pathParts = location.pathname.split('/');
    const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);
  
    const updateSelectedState = (section, div, secondVisible = false, thirdVisible = false, lastVisible = false) => {
      setSelected(section);
      setSelectedDiv(div);
  
      // setting 경로일 때 asidePopup을 false로 설정
      setAsidePopupVisible(section !== 'setting');
      setAsidePopupBackgroundColor({
        dashboard: '',
        computing: section === 'computing' ? 'rgb(218, 236, 245)' : '',
        storage: section === 'storage' ? 'rgb(218, 236, 245)' : '',
        network: section === 'network' ? 'rgb(218, 236, 245)' : '',
        setting: section === 'setting' ? 'rgb(218, 236, 245)' : '',
        default: ''
      });
    };
  
    if (location.pathname.includes('/computing')) {
      if (location.pathname.includes('/computing/HostedEngine')) {
        updateSelectedState('computing', 'HostedEngine', true, true, true);
      } else if (location.pathname === '/computing/host') {  // 정확히 /computing/host 경로일 때만
        updateSelectedState('computing', 'host', true, true, true);
      } else if (location.pathname.includes('/computing/clusters')) {
        updateSelectedState('computing', 'clusters', true, true);
      } else if (location.pathname.includes('/computing/datacenters')) {
        updateSelectedState('computing', 'data_center', true);
      } else if (location.pathname.includes('/computing/vms')) {
        updateSelectedState('computing', 'vms', true, true, true);
      } else {
        updateSelectedState('computing', null);
      }
    } else if (location.pathname.includes('/storage')) {
      if (location.pathname.includes('/storage-domain')) {
        updateSelectedState('storage', 'storage_domain', true);
      } else if (location.pathname.includes('/storage-disk')) {
        updateSelectedState('storage', lastPart, true, true);
      } else {
        updateSelectedState('storage', 'data_center');
      }
    } else if (location.pathname.includes('/networks')) {
      if (location.pathname === '/networks' || lastPart === 'network') {
        updateSelectedState('network', 'default', true);
      } else {
        updateSelectedState('network', lastPart, true);
      }
    } else if (location.pathname.includes('/settings')) {
      updateSelectedState('setting', 'default');
    } else {
      setSelected('dashboard');
      setAsidePopupVisible(false); // 대시보드에서 aside 팝업을 닫음
      setAsidePopupBackgroundColor({
        dashboard: 'rgb(218, 236, 245)',
        computing: '',
        storage: '',
        network: '',
        setting: '',
        default: ''
      });
    }
  }, [location]);
  




  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

    // 네트워크 섹션에서 사용하는 것과 유사한 로직으로 편집
    useEffect(() => {
        const pathParts = location.pathname.split('/');
        const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);

        if (location.pathname.includes('/storage-disk')) {
            setSelected('storage');
            setSelectedDiv(null); 
            setSelectedDisk(lastPart);
        } else if (location.pathname.includes('/storage-domain')) {
            setSelected('storage');
            setSelectedDiv('storage_domain');
            setSelectedDisk(null); 
        } else if (location.pathname.includes('/storage')) {
            setSelected('storage');
            setSelectedDiv('data_center');  
            setSelectedDisk(null); 
        }
    }, [location]);

    const handleDetailClickStorage = (diskName) => {
        if (selectedDisk !== diskName) {
            setSelectedDisk(diskName);
            setSelectedDiv(null);
            navigate(`/storage-disk/${diskName}`);
        }
    };

    const getDiskDivStyle = (diskName) => {
        return {
            backgroundColor: selectedDisk === diskName ? 'rgb(218, 236, 245)' : 'transparent',
        };
    };

    const handleClick = (id) => {
        if (selected === id) return;
        setSelected(id);
        setSelectedDiv(null);
        toggleAsidePopup(id);
    };

    const toggleAsidePopup = (id) => {
        const newBackgroundColor = {
            dashboard: '',
            computing: '',
            storage: '',
            network: '',
            setting: '',
            default: ''
        };

        if (id === 'computing') {
            newBackgroundColor.computing = 'rgb(218, 236, 245)';
            navigate(`/computing/datacenters`);
        } else if (id === 'storage') {
            newBackgroundColor.storage = 'rgb(218, 236, 245)';
        } else if (id === 'network') {
            newBackgroundColor.network = 'rgb(218, 236, 245)';
        } else if (id === 'setting') {
            newBackgroundColor.setting = 'rgb(218, 236, 245)';
        } else {
            setAsidePopupVisible(false);
        }

        setAsidePopupBackgroundColor(newBackgroundColor);
    };

    // 컴퓨팅부분
    const handleFirstClick = (e) => {
        e.stopPropagation();
        setIsSecondVisible(!isSecondVisible);
    };

    const handleFirstDivClick = () => {
        setSelectedDiv('data_center');
        setIsSecondVisible(!isSecondVisible);
        navigate('/computing/datacenters');
    };
    
    const handleSecondClick = (e) => {
        e.stopPropagation();
        setIsThirdVisible(!isThirdVisible);
    };

    const handleSecondDivClick = (e) => {
        e.stopPropagation();
        setSelectedDiv('clusters');
        navigate('/computing/clusters');
    };
    
    const handleThirdClick = (e) => {
        e.stopPropagation();
        setIsFourthVisible(!isFourthVisible);
    };

    const handleThirdDivClick = () => {
        setSelectedDiv('host');
        setIsFourthVisible(!isSecondVisible);
        navigate('/computing/host');
    };
    
    const handleFourthClick = (e) => {
        e.stopPropagation();
        setIsLastVisible(!isLastVisible);
    };
    
    const handleFourthDivClick = () => {
        setSelectedDiv('vms');
        navigate('/computing/vms');
    };

    // 스토리지
    const handleFirstDivClickStorage = () => {
        if (selectedDiv !== 'data_center') {
            setSelectedDiv('data_center');
            setSelectedDisk(null);
            navigate('/storage');
        }
    };
    
    const handleSecondDivClickStorage = () => {
        if (selectedDiv !== 'storage_domain') {
            setSelectedDiv('storage_domain');
            setSelectedDisk(null);
            navigate('/storage-domain');
        }
    };

    const toggleSecondVisibleStorage = (e) => {
        e.stopPropagation();
        setIsSecondVisibleStorage(!isSecondVisibleStorage);
    };
    
    const toggleLastVisibleStorage = (e) => {
        e.stopPropagation();
        setIsLastVisibleStorage(!isLastVisibleStorage);
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
        setAsidePopupVisible(false); // setting_icon을 눌렀을 때 사이드바 닫기
    };
    
    const openPopup = (popupType) => {
        setActivePopup(popupType);
    };

    const closePopup = () => {
        setActivePopup(null);
    };

    const openSettingPopup = () => {
        setSettingPopupOpen(true);
    };

    const closeSettingPopup = () => {
        setSettingPopupOpen(false);
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
                    <Link to='/computing/datacenters' className="link-no-underline">
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
                </div>
                <Link to='/settings' className="link-no-underline">
                    <div id="setting_icon" style={{ backgroundColor: asidePopupBackgroundColor.setting }} onClick={handleSettingIconClick}>
                       <FontAwesomeIcon icon={faCog} fixedWidth/>
                    </div>
                </Link>
            </div>
            <div id="aside_popup" style={{ display: asidePopupVisible ? 'block' : 'none' }}>
                <button id='aside_popup_btn' onClick={handleAsidePopupBtnClick}><FontAwesomeIcon icon={faChevronLeft} fixedWidth/></button>

                {/*가상머신*/} 
                {selected === 'computing' && (
<div id="virtual_machine_chart">
    <div 
        className="aside_popup_content" 
        id="aside_popup_first" 
        style={{ backgroundColor: selectedDiv === 'data_center' ? 'rgb(218, 236, 245)' : '' }} 
        onClick={() => {
            if (selectedDiv !== 'data_center') {
                setSelectedDiv('data_center');
                navigate('/computing/datacenters');
            }
        }}
    >
        
        <FontAwesomeIcon 
         style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
            icon={isSecondVisible ? faChevronDown : faChevronRight} 
            onClick={(e) => {
                e.stopPropagation();
                setIsSecondVisible(!isSecondVisible);
                setIsThirdVisible(false); // 하위 항목들 모두 접기
                setIsFourthVisible(false); // 하위 항목들 모두 접기
                setIsLastVisible(false);  // 하위 항목들 모두 접기
            }}
            fixedWidth
        />
        <FontAwesomeIcon icon={faBuilding} fixedWidth/>
        <span>Rutil manager</span>
    </div>
    {isSecondVisible && (
        <div 
            className="aside_popup_second_content" 
            id="aside_popup_second" 
            style={{ backgroundColor: selectedDiv === 'clusters' ? 'rgb(218, 236, 245)' : '' }}
            onClick={() => {
                if (selectedDiv !== 'clusters') {
                    setSelectedDiv('clusters');
                    navigate('/computing/clusters');
                }
            }}
        >
                <FontAwesomeIcon
                 style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
                icon={isThirdVisible ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
                onClick={(e) => {
                    e.stopPropagation();
                    setIsThirdVisible(!isThirdVisible);
                    setIsFourthVisible(false);  // 하위 항목들 모두 접기
                    setIsLastVisible(false);    // 하위 항목들 모두 접기
                }}
                fixedWidth
                />

                <FontAwesomeIcon icon={faBuilding} fixedWidth/>
                <span>data_center</span>
            </div>
            )}
            {isThirdVisible && (
                <div 
                    className="aside_popup_third_content" 
                    id="aside_popup_third" 
                    style={{ backgroundColor: location.pathname === '/computing/host' ? 'rgb(218, 236, 245)' : '' }} 
                    onClick={() => {
                        if (selectedDiv !== 'host') {
                            setSelectedDiv('host');
                            navigate('/computing/host');
                        }
                    }}
                >
            <FontAwesomeIcon
             style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
                    icon={isFourthVisible ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsFourthVisible(!isFourthVisible);
                        setIsLastVisible(false);  // 하위 항목들 모두 접기
                    }}
                    fixedWidth
                />

            <FontAwesomeIcon icon={faBuilding} fixedWidth/>
            <span>Cluster</span>
        </div>
    )}  

    {isFourthVisible && (
        <div 
        className="aside_popup_fourth_content" 
        id="aside_popup_fourth" 
        style={{ backgroundColor: selectedDiv === 'vms' ? 'rgb(218, 236, 245)' : '' }} 
        onClick={() => {
            if (selectedDiv !== 'vms') {
                setSelectedDiv('vms');
                navigate('/computing/vms');
            }
        }}
    >
        <FontAwesomeIcon
         style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
    icon={isLastVisible ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
    onClick={(e) => {
        e.stopPropagation();
        setIsLastVisible(!isLastVisible);
    }}
    fixedWidth
/>

        <FontAwesomeIcon icon={faBuilding} fixedWidth/>
        <span>Host</span>
    </div>
    )}

    {isLastVisible && (
        <div id="aside_popup_last_machine">
            <div
                onClick={() => handleUserIconClick('host01.ititnfo.com')}
                onContextMenu={(e) => handleContextMenu(e, 'host01.ititnfo.com')}
                onMouseEnter={() => handleMouseEnter('host01.ititnfo.com')}
                onMouseLeave={handleMouseLeave}
                style={{
                    backgroundColor: location.pathname === '/computing/host/host01.ititnfo.com' ? 'rgb(218, 236, 245)' : 
                                    selectedDiv === 'host01.ititnfo.com' ? 'rgb(218, 236, 245)' : 
                                    (hoverTarget === 'host01.ititnfo.com' ? '#e6eefa' : 'transparent')
                }}
            >
                <FontAwesomeIcon icon={faUser} fixedWidth/>
                <span>host01.ititnfo.com</span>
            </div>
            <div
                onClick={() => handleMicrochipIconClick('HostedEngine')}
                onContextMenu={(e) => handleContextMenu(e, 'HostedEngine')}
                onMouseEnter={() => handleMouseEnter('HostedEngine')}
                onMouseLeave={handleMouseLeave}
                style={{
                    backgroundColor: location.pathname === '/computing/HostedEngine' ? 'rgb(218, 236, 245)' : 
                                    selectedDiv === 'HostedEngine' ? 'rgb(218, 236, 245)' : 
                                    (hoverTarget === 'HostedEngine' ? '#e6eefa' : 'transparent')
                }}
            >
                <FontAwesomeIcon icon={faMicrochip} fixedWidth/>
                <span>HostedEngine</span>
            </div>
        </div>
    )}
</div>
)}



                {/*스토리지 */} 
                {selected === 'storage' && (
                <div id="storage_chart">
                    <div
                        className="aside_popup_content"
                        id="aside_popup_first2"
                        style={{ backgroundColor: selectedDiv === 'data_center' ? 'rgb(218, 236, 245)' : '' }}
                        onClick={() => {
                            setSelectedDiv('data_center');
                            navigate('/storage');
                        }}
                    >
                <FontAwesomeIcon
                 style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
                    icon={isSecondVisibleStorage ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsSecondVisibleStorage(!isSecondVisibleStorage);
                        setIsLastVisibleStorage(false); // 하위 항목들 모두 접기
                    }}
                    fixedWidth
                />

        <FontAwesomeIcon icon={faBuilding} fixedWidth/>
        <span>data_center</span>
    </div>
    {isSecondVisibleStorage && (
        <div
            className="aside_popup_second_content"
            id="aside_popup_second2"
            style={{ backgroundColor: selectedDiv === 'storage_domain' ? 'rgb(218, 236, 245)' : '' }}
            onClick={() => {
                setSelectedDiv('storage_domain');
                navigate('/storage-domainpart');
            }}
        >
            <FontAwesomeIcon
             style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
    icon={isLastVisibleStorage ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
    onClick={(e) => {
        e.stopPropagation();
        setIsLastVisibleStorage(!isLastVisibleStorage);
    }}
    fixedWidth
/>

            <FontAwesomeIcon icon={faBuilding} fixedWidth/>
            <span>Domain</span>
        </div>
    )}
    {isLastVisibleStorage && (
        <div id="aside_popup_last_storage">
            <div
                onClick={() => handleDetailClickStorage('he_metadata')}
                onContextMenu={(e) => handleContextMenu(e, 'he_metadata')}
                onMouseEnter={() => handleMouseEnter('he_metadata')}
                onMouseLeave={handleMouseLeave}
                style={getDiskDivStyle('he_metadata')}
            >
              <FontAwesomeIcon icon={faMicrochip} fixedWidth/>
              <span>he_metadata</span>
            </div>
            <div
                onClick={() => handleDetailClickStorage('디스크2')}
                onContextMenu={(e) => handleContextMenu(e, '디스크2')}
                onMouseEnter={() => handleMouseEnter('디스크2')}
                onMouseLeave={handleMouseLeave}
                style={getDiskDivStyle('디스크2')}
            >
              <FontAwesomeIcon icon={faMicrochip} fixedWidth/>
              <span>디스크2</span>
            </div>
            <div
                onClick={() => handleDetailClickStorage('디스크3')}
                onContextMenu={(e) => handleContextMenu(e, '디스크3')}
                onMouseEnter={() => handleMouseEnter('디스크3')}
                onMouseLeave={handleMouseLeave}
                style={getDiskDivStyle('디스크3')}
            >
              <FontAwesomeIcon icon={faMicrochip} fixedWidth/>
              <span>디스크3</span>
            </div>
        </div>
    )}
</div>
)}
                {/* 네트워크 섹션 */}
                {
                  selected === 'network' && 
                  (
                    <div id="network_chart">
                      <div
                        className="aside_popup_content"
                        id="aside_popup_first3"
                        style={{ backgroundColor: selectedDiv === 'default' ? 'rgb(218, 236, 245)' : '' }}
                        onClick={() => {
                          setSelectedDiv('default');
                          navigate('/networks');
                          navNetworksRefetch()
                        }}
                      >
                        <FontAwesomeIcon
                         style={{ fontSize:'0.3rem' , marginRight: '0.04rem' }} 
    icon={isSecondVisibleNetwork ? faChevronDown : faChevronRight}  // 상태에 따라 아이콘 변경
    onClick={(e) => {
        e.stopPropagation();
        setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
    }}
    fixedWidth
/>

                        <FontAwesomeIcon icon={faBuilding} fixedWidth />
                        <span>Default</span>
                    </div>
                    {
                      isSecondVisibleNetwork && /*navNetworks && navNetworks[0] && (navNetworks[0]?.networks?.map*/(/*(n) => */
                        <>
                          {/* <div
                            className="aside_popup_second_content"
                            id="aside_popup_network_content"
                            style={{
                              backgroundColor: selectedDiv === 'ovirtmgmt' ? 'rgb(218, 236, 245)' : '',
                              paddingLeft: '0.85rem'
                            }}
                            onClick={() => {
                              setSelectedDiv('ovirtmgmt');
                              navigate(`/networks/${n?.id}`);
                            }}
                          >
                           <FontAwesomeIcon icon={faBuilding} style={{ fontSize: '0.34rem', marginRight: '0.05rem' }}fixedWidth/>
                            <span>{n?.name}</span>
                          </div> */}
                          <div
                            className="aside_popup_second_content"
                            id="aside_popup_network_content"
                            style={{
                              backgroundColor: selectedDiv === 'example1' ? 'rgb(218, 236, 245)' : '',
                              paddingLeft: '0.85rem'
                            }}
                            onClick={() => {
                                setSelectedDiv('example1');
                                navigate('/networks/example1');
                            }}
                          >
                            <FontAwesomeIcon icon={faBuilding} style={{ fontSize: '0.34rem', marginRight: '0.05rem' }} fixedWidth/>
                            <span>example1</span>
                          </div>
                          <div
                            className="aside_popup_second_content"
                            id="aside_popup_network_content"
                            style={{
                                backgroundColor: selectedDiv === 'example2' ? 'rgb(218, 236, 245)' : '',
                                paddingLeft: '0.85rem'
                            }}
                            onClick={() => {
                                setSelectedDiv('example2');
                                navigate('/networks/example2');
                            }}
                          >
                            <FontAwesomeIcon icon={faBuilding} style={{ fontSize: '0.34rem', marginRight: '0.05rem' }} fixedWidth/>
                            <span>example2</span>
                          </div>
                        </>
                      )
                    }
                    </div>
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
