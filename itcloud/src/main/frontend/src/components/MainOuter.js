import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Modal from 'react-modal';
import './MainOuter.css';

const MainOuter = ({ children }) => {
    const [selected, setSelected] = useState('dashboard');
    const [selectedDiv, setSelectedDiv] = useState('data_center');
    const [selectedDisk, setSelectedDisk] = useState(null);
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
            } else if (location.pathname.includes('/computing/cluster')) {
                updateSelectedState('computing', 'cluster', true, true);
            } else if (location.pathname.includes('/computing/datacenter')) {
                updateSelectedState('computing', 'data_center', true);
            } else if (location.pathname.includes('/computing/vmhost-chart')) {
                updateSelectedState('computing', 'vmhost-chart', true, true, true);
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
        } else if (location.pathname.includes('/network')) {
            if (location.pathname === '/network' || lastPart === 'network') {
                updateSelectedState('network', 'default', true);
            } else {
                updateSelectedState('network', lastPart, true);
            }
        } else if (location.pathname.includes('/setting')) {
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
    
    
    

    useEffect(() => {
        function adjustFontSize() {
            const width = window.innerWidth;
            const fontSize = width / 40;
            document.documentElement.style.fontSize = fontSize + 'px';
        }
    
        window.addEventListener('resize', adjustFontSize);
        adjustFontSize();

        return () => {
            window.removeEventListener('resize', adjustFontSize);
        };
    }, []);

    // 네트워크 섹션에서 사용하는 것과 유사한 로직으로 수정
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
            navigate(`/computing/datacenter`);
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
        navigate('/computing/datacenter');
    };
    
    const handleSecondClick = (e) => {
        e.stopPropagation();
        setIsThirdVisible(!isThirdVisible);
    };

    const handleSecondDivClick = (e) => {
        e.stopPropagation();
        setSelectedDiv('cluster');
        navigate('/computing/cluster');
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
        setSelectedDiv('vmhost-chart');
        navigate('/computing/vmhost-chart');
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
                        <Link to='/' className="link-no-underline">
                            <div
                                id="aside_popup_dashboard_btn"
                                className={getClassNames('dashboard')}
                                onClick={() => handleClick('dashboard')}
                                style={{ backgroundColor: asidePopupBackgroundColor.dashboard }}
                            >
                                <i className="fa fa-th-large"></i>
                            </div>
                        </Link>
                        <Link to='/computing/datacenter' className="link-no-underline">
                            <div
                                id="aside_popup_machine_btn"
                                className={getClassNames('computing')}
                                onClick={() => handleClick('computing')}
                                style={{ backgroundColor: asidePopupBackgroundColor.computing }}
                            >
                                <i className="fa fa-desktop"></i>
                            </div>
                        </Link>
                        <Link to='/network' className="link-no-underline">
                            <div
                                id="aside_popup_network_btn"
                                className={getClassNames('network')}
                                onClick={() => handleClick('network')}
                                style={{ backgroundColor: asidePopupBackgroundColor.network }}
                            >
                                <i className="fa fa-server"></i>
                            </div>
                        </Link>
                        <Link to='/storage' className="link-no-underline">
                            <div
                                id="aside_popup_storage_btn"
                                className={getClassNames('storage')}
                                onClick={() => handleClick('storage')}
                                style={{ backgroundColor: asidePopupBackgroundColor.storage }}
                            >
                                <i className="fa fa-database"></i>
                            </div>
                        </Link>
                    </div>
                    <Link to='/setting' className="link-no-underline">
                        <div id="setting_icon" style={{ backgroundColor: asidePopupBackgroundColor.setting }} onClick={() => handleClick('setting')}>
                            <i className="fa fa-cog"></i>
                        </div>
                    </Link>
                </div>
                <div id="aside_popup" style={{ display: asidePopupVisible ? 'block' : 'none' }}>
                    <button id='aside_popup_btn' onClick={handleAsidePopupBtnClick}><i className="fa fa-chevron-left"></i></button>

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
                    navigate('/computing/datacenter');
                }
            }}
        >
            <i 
                className={`fa fa-chevron-${isSecondVisible ? 'down' : 'right'}`} 
                onClick={(e) => {
                    e.stopPropagation();
                    setIsSecondVisible(!isSecondVisible);
                    setIsThirdVisible(false); // 하위 항목들 모두 접기
                    setIsFourthVisible(false); // 하위 항목들 모두 접기
                    setIsLastVisible(false);  // 하위 항목들 모두 접기
                }}
            ></i>
            <i className="fa fa-building-o"></i>
            <span>Rutil manager</span>
        </div>
        {isSecondVisible && (
            <div 
                className="aside_popup_second_content" 
                id="aside_popup_second" 
                style={{ backgroundColor: selectedDiv === 'cluster' ? 'rgb(218, 236, 245)' : '' }}
                onClick={() => {
                    if (selectedDiv !== 'cluster') {
                        setSelectedDiv('cluster');
                        navigate('/computing/cluster');
                    }
                }}
            >
                <i
                    className={`fa fa-chevron-${isThirdVisible ? 'down' : 'right'}`}
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsThirdVisible(!isThirdVisible);
                        setIsFourthVisible(false);  // 하위 항목들 모두 접기
                        setIsLastVisible(false);  // 하위 항목들 모두 접기
                        
                    }}
                ></i>
                <i className="fa fa-building-o"></i>
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
                <i className={`fa fa-chevron-${isFourthVisible ? 'down' : 'right'}`} 
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsFourthVisible(!isFourthVisible);
                        setIsLastVisible(false);  // 하위 항목들 모두 접기
                    }}
                ></i>
                <i className="fa fa-building-o"></i>
                <span>클러스터</span>
            </div>
        )}  

        {isFourthVisible && (
            <div 
            className="aside_popup_fourth_content" 
            id="aside_popup_fourth" 
            style={{ backgroundColor: selectedDiv === 'vmhost-chart' ? 'rgb(218, 236, 245)' : '' }} 
            onClick={() => {
                if (selectedDiv !== 'vmhost-chart') {
                    setSelectedDiv('vmhost-chart');
                    navigate('/computing/vmhost-chart');
                }
            }}
        >
            <i className={`fa fa-chevron-${isLastVisible ? 'down' : 'right'}`} 
                onClick={(e) => {
                    e.stopPropagation();
                    setIsLastVisible(!isLastVisible);
                }}
            ></i>
            <i className="fa fa-building-o"></i>
            <span>호스트</span>
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
                    <i className="fa fa-user"></i>
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
                    <i className="fa fa-microchip"></i>
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
            <i
                className={`fa fa-chevron-${isSecondVisibleStorage ? 'down' : 'right'}`}
                onClick={(e) => {
                    e.stopPropagation();
                    setIsSecondVisibleStorage(!isSecondVisibleStorage);
                    setIsLastVisibleStorage(false); // 하위 항목들 모두 접기
                }}
            ></i>
            <i className="fa fa-building-o"></i>
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
                <i
                    className={`fa fa-chevron-${isLastVisibleStorage ? 'down' : 'right'}`}
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsLastVisibleStorage(!isLastVisibleStorage);
                    }}
                ></i>
                <i className="fa fa-building-o"></i>
                <span>도메인</span>
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
                    <i className="fa fa-microchip"></i>
                    <span>he_metadata</span>
                </div>
                <div
                    onClick={() => handleDetailClickStorage('디스크2')}
                    onContextMenu={(e) => handleContextMenu(e, '디스크2')}
                    onMouseEnter={() => handleMouseEnter('디스크2')}
                    onMouseLeave={handleMouseLeave}
                    style={getDiskDivStyle('디스크2')}
                >
                    <i className="fa fa-microchip"></i>
                    <span>디스크2</span>
                </div>
                <div
                    onClick={() => handleDetailClickStorage('디스크3')}
                    onContextMenu={(e) => handleContextMenu(e, '디스크3')}
                    onMouseEnter={() => handleMouseEnter('디스크3')}
                    onMouseLeave={handleMouseLeave}
                    style={getDiskDivStyle('디스크3')}
                >
                    <i className="fa fa-microchip"></i>
                    <span>디스크3</span>
                </div>
            </div>
        )}
    </div>
)}

                    {/* 네트워크 섹션 */}
                    {selected === 'network' && (
    <div id="network_chart">
            <div
                className="aside_popup_content"
                id="aside_popup_first3"
                style={{ backgroundColor: selectedDiv === 'default' ? 'rgb(218, 236, 245)' : '' }}
                onClick={() => {
                    setSelectedDiv('default');
                   
                    navigate('/network');
                }}
            >
            <i
                className={`fa fa-chevron-${isSecondVisibleNetwork ? 'down' : 'right'}`}
                onClick={(e) => {
                    e.stopPropagation();
                    setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
                }}
            ></i>
            <i className="fa fa-building-o"></i>
            <span>Default</span>
        </div>
        {isSecondVisibleNetwork && (
            <>
                <div
                    className="aside_popup_second_content"
                    id="aside_popup_network_content"
                    style={{
                        backgroundColor: selectedDiv === 'ovirtmgmt' ? 'rgb(218, 236, 245)' : '',
                        paddingLeft: '0.85rem'
                    }}
                    onClick={() => {
                        setSelectedDiv('ovirtmgmt');
                        navigate('/network/ovirtmgmt');
                    }}
                >
                    <i className="fa fa-building-o" style={{ fontSize: '0.34rem', marginRight: '0.05rem' }}></i>
                    <span>ovirtmgmt</span>
                </div>

                <div
                    className="aside_popup_second_content"
                    id="aside_popup_network_content"
                    style={{
                        backgroundColor: selectedDiv === 'example1' ? 'rgb(218, 236, 245)' : '',
                        paddingLeft: '0.85rem'
                    }}
                    onClick={() => {
                        setSelectedDiv('example1');
                        navigate('/network/example1');
                    }}
                >
                    <i className="fa fa-building-o" style={{ fontSize: '0.34rem', marginRight: '0.05rem' }}></i>
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
                        navigate('/network/example2');
                    }}
                >
                    <i className="fa fa-building-o" style={{ fontSize: '0.34rem', marginRight: '0.05rem' }}></i>
                    <span>example2</span>
                </div>
            </>
        )}
    </div>
)}



                    {selected === 'setting' && (
                        <div id="setting_chart">
                            <div id="setting_normal_btn">
                                <i className="fa fa-cog"></i>
                                <span>활성 사용자 세션</span>
                            </div>
                            <div id="setting_miniset_btn" onClick={openSettingPopup}>
                                <i className="fa fa-cog"></i>
                                <span>설정</span>
                            </div>
                            <div id="setting_user_btn">
                                <i className="fa fa-cog"></i>
                                <span>사용자</span>
                            </div>
                            <div id="setting_account_btn">
                                <i className="fa fa-cog"></i>
                                <span>계정설정</span>
                            </div>
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

            <Modal
                isOpen={settingPopupOpen}
                onRequestClose={closeSettingPopup}
                contentLabel="설정"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_setting_popup">
                    <div className="network_popup_header">
                        <h1>설정</h1>
                        <button onClick={closeSettingPopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="network_new_nav">
                        <div id="setting_part_btn" className={activeSettingForm === 'part' ? 'active' : ''} onClick={() => handleSettingNavClick('part')}>역할</div>
                        <div id="setting_system_btn" className={activeSettingForm === 'system' ? 'active' : ''} onClick={() => handleSettingNavClick('system')}>시스템 권한</div>
                        <div id="setting_schedule_btn" className={activeSettingForm === 'schedule' ? 'active' : ''} onClick={() => handleSettingNavClick('schedule')}>스케줄링 정책</div>
                        <div id="setting_instant_btn" className={activeSettingForm === 'instant' ? 'active' : ''} onClick={() => handleSettingNavClick('instant')}>인스턴스 유형</div>
                        <div id="setting_mac_btn" className={activeSettingForm === 'mac' ? 'active' : ''} onClick={() => handleSettingNavClick('mac')}>MAC주소 풀</div>
                    </div>

                    {activeSettingForm === 'part' && (
                        <form id="setting_part_form">
                            <div>보기</div>
                            <div className="setting_part_nav">
                                <div className="radio_toolbar">
                                    <div>
                                        <input type="radio" id="all_roles" name="roles" value="all" defaultChecked />
                                        <label htmlFor="all_roles">모든역할</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="admin_roles" name="roles" value="admin" />
                                        <label htmlFor="admin_roles">관리자 역할</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="user_roles" name="roles" value="user" />
                                        <label htmlFor="user_roles">사용자 역할</label>
                                    </div>
                                </div>

                                <div className="setting_buttons">
                                    <div id="setting_part_new_btn" onClick={() => openPopup('newRole')}>새로 만들기</div>
                                    <div>편집</div>
                                    <div>복사</div>
                                    <div>삭제</div>
                                </div>
                            </div>

                            <div className="setting_part_table_outer">
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-36</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                            <th>이름</th>
                                            <th>설명</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>dddddddddddddddddddddd</td>
                                            <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                        </tr>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>dddddddddddddddddddddd</td>
                                            <td>ddddddddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {activeSettingForm === 'system' && (
                        <form id="setting_system_form">
                            <div className="setting_part_nav">
                                <div className="radio_toolbar">
                                    <div>
                                        <input type="radio" id="all_roles" name="roles" value="all" defaultChecked />
                                        <label htmlFor="all_roles">모든역할</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="admin_roles" name="roles" value="admin" />
                                        <label htmlFor="admin_roles">관리자 역할</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="user_roles" name="roles" value="user" />
                                        <label htmlFor="user_roles">사용자 역할</label>
                                    </div>
                                </div>

                                <div className="setting_buttons">
                                    <div id="setting_system_add_btn" onClick={() => openPopup('addSystemRole')}>추가</div>
                                    <div>제거</div>
                                </div>
                            </div>

                            <div className="setting_part_table_outer">
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-3</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>사용자</th>
                                            <th>인증 공급자</th>
                                            <th>네임 스페이스</th>
                                            <th>역할</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td></td>
                                            <td>*</td>
                                            <td>SuperUser</td>
                                        </tr>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td></td>
                                            <td>*</td>
                                            <td>SuperUser</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {activeSettingForm === 'schedule' && (
                        <form id="setting_schedule_form">
                            <div className="setting_part_nav">
                                <div className="setting_buttons">
                                    <div id="setting_schedule_new_btn" onClick={() => openPopup('newSchedule')}>새로 만들기</div>
                                    <div>편집</div>
                                    <div>복사</div>
                                    <div>제거</div>
                                    <div id="setting_schedule_unit">정책 유닛 관리</div>
                                </div>
                            </div>

                            <div className="setting_part_table_outer">
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-5</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>이름</th>
                                            <th>설명</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td>ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ</td>
                                        </tr>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}
            
                    {activeSettingForm === 'instant' && (
                        <form id="setting_instant_form">
                            <div className="setting_part_nav">
                                <div className="setting_buttons">
                                    <div id="setting_instant_new_btn">새로 만들기</div>
                                    <div>편집</div>
                                    <div>제거</div>
                                </div>
                            </div>

                            <div className="setting_part_table_outer">
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-5</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th>이름</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>ovirt-administrator</td>
                                        </tr>
                                        <tr>
                                            <td>ovirt-administrator</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {activeSettingForm === 'mac' && (
                        <form id="setting_mac_form">
                            <div className="setting_part_nav">
                                <div className="setting_buttons">
                                    <div id="setting_mac_new_btn" onClick={() => openPopup('macNew')}>새로 만들기</div>
                                    <div id="setting_mac_edit_btn" onClick={() => openPopup('macEdit')}>편집</div>
                                    <div>제거</div>
                                </div>
                            </div>

                            <div className="setting_part_table_outer" style={{ borderBottom: 'none' }}>
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-5</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>이름</th>
                                            <th>설명</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td>ovirt-administrator</td>
                                        </tr>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td>ovirt-administrator</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div className="setting_part_table_outer">
                                <div className="application_content_header">
                                    <button><i className="fa fa-chevron-left"></i></button>
                                    <div>1-5</div>
                                    <button><i className="fa fa-chevron-right"></i></button>
                                    <button><i className="fa fa-ellipsis-v"></i></button>
                                </div>

                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>사용자</th>
                                            <th>인증 공급자</th>
                                            <th>네임 스페이스</th>
                                            <th>역할</th>
                                            <th>생성일</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td>ovirt-administrator</td>
                                            <td>*</td>
                                            <td>ovirt-adm</td>
                                            <td>2023.12.29AM11:40:58</td>
                                        </tr>
                                        <tr>
                                            <td><i className="fa fa-heart"></i></td>
                                            <td>ovirt-administrator</td>
                                            <td>ovirt-administrator</td>
                                            <td>*</td>
                                            <td>ovirt-adm</td>
                                            <td>2023.12.29AM11:40:58</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    <div className="edit_footer">
                        <button style={{ display: 'none' }} onClick={closeSettingPopup}></button>
                        <button>OK</button>
                        <button onClick={closeSettingPopup}>취소</button>
                    </div>

                </div>
            </Modal>

            <Modal
                isOpen={activePopup === 'newRole'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_part_new_popup">
                    <div className="network_popup_header">
                        <h1>새 역할</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="set_part_text">
                        <div>
                            <div>
                                <label htmlFor="new_role_name">이름</label><br />
                                <input type="text" id="new_role_name" value="test02" />
                            </div>
                            <div>
                                <label htmlFor="new_role_desc">설명</label><br />
                                <input type="text" id="new_role_desc" value="test02" />
                            </div>
                        </div>
                        <span>계정 유형:</span>
                        <div>
                            <div>
                                <input type="radio" id="new_role_user" name="new_role_type" value="user" checked />
                                <label htmlFor="new_role_user" style={{ marginRight: '0.3rem' }}>사용자</label>
                            </div>
                            <div>
                                <input type="radio" id="new_role_admin" name="new_role_type" value="admin" />
                                <label htmlFor="new_role_admin">관리자</label>
                            </div>
                        </div>
                    </div>

                    <div className="set_part_checkboxs">
                        <span>작업 허용을 위한 확인란</span>
                        <div className="set_part_buttons">
                            <div>모두 확장</div>
                            <div>모두 축소</div>
                        </div>
                        <div className="checkbox_toolbar">
                            <div>
                                <input type="checkbox" id="new_role_system" name="new_role_permissions" />
                                <label htmlFor="new_role_system">시스템</label>
                            </div>
                            <div>
                                <input type="checkbox" id="new_role_network" name="new_role_permissions" />
                                <label htmlFor="new_role_network">네트워크</label>
                            </div>
                            <div>
                                <input type="checkbox" id="new_role_template" name="new_role_permissions" />
                                <label htmlFor="new_role_template">템플릿</label>
                            </div>
                            <div>
                                <input type="checkbox" id="new_role_vm" name="new_role_permissions" />
                                <label htmlFor="new_role_vm">가상머신</label>
                            </div>
                            <div>
                                <input type="checkbox" id="new_role_vm_pool" name="new_role_permissions" />
                                <label htmlFor="new_role_vm_pool">가상머신 풀</label>
                            </div>
                            <div>
                                <input type="checkbox" id="new_role_disk" name="new_role_permissions" />
                                <label htmlFor="new_role_disk">디스크</label>
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

            <Modal
                isOpen={activePopup === 'addSystemRole'}
                onRequestClose={closePopup}
                contentLabel="추가"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_system_new_popup">
                    <div className="network_popup_header">
                        <h1>사용자에게 권한 추가</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="power_radio_group">
                        <input type="radio" id="user" name="option" defaultChecked />
                        <label htmlFor="user">사용자</label>
                        
                        <input type="radio" id="group" name="option" />
                        <label htmlFor="group">그룹</label>
                    </div>

                    <div className="power_contents_outer">
                        <div>
                            <label htmlFor="cluster">검색:</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div>
                            <label htmlFor="cluster">네임스페이스:</label>
                            <select id="cluster">
                                <option value="default">Default</option>   
                            </select>
                        </div>
                        <div>
                            <label style={{ color: "white" }}>.</label>
                            <input type="text" id="name" value="test02" />
                        </div>
                        <div>
                            <div style={{ color: "white" }}>.</div>
                            <input type="submit" value="검색" />
                        </div>
                    </div>

                    <div className="power_table">
                        <table>
                            <thead>
                                <tr>
                                    <th>이름</th>
                                    <th>성</th>
                                    <th>사용자 이름</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>dddddddddddddddddddddd</td>
                                    <td>2024. 1. 17. PM 3:14:39</td>
                                    <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div className="power_last_content" style={{ padding: "0.1rem 0.3rem" }}>
                        <label htmlFor="cluster">할당된 역할:</label>
                        <select id="cluster" style={{ width: "65%" }}>
                            <option value="default">UserRole</option>   
                        </select>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: "none" }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>

            <Modal
                isOpen={activePopup === 'newSchedule'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_schedule_new_popup">
                    <div className="network_popup_header">
                        <h1>새 스케줄링 정책</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>
                    
                    <div className="set_part_text" style={{ borderBottom: 'none' }}>
                        <div>
                            <div>
                                <label htmlFor="name">이름</label><br />
                                <input type="text" id="name" defaultValue="test02" />
                            </div>
                            <div>
                                <label htmlFor="name">설명</label><br />
                                <input type="text" id="name" defaultValue="test02" />
                            </div>
                        </div>
                    </div>

                    <div className="set_schedule_contents">
                        <div className="set_schedule_contents_left">
                            <div>
                                <h1>필터 모듈</h1>
                                <div style={{ fontSize: '0.26rem' }}>드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                                <div></div>
                            </div>
                            <div>
                                <h1>필터 모듈</h1>
                                <div style={{ fontSize: '0.26rem' }}>드래그하거나 또는 컨텍스트 메뉴를 사용하여 변경 활성화된 필터</div>
                                <div></div>
                            </div>
                        </div>
                        <div className="set_schedule_contents_right">
                            <div>
                                <span>비활성화된 필터</span>
                                <div className="schedule_boxs">
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                </div>
                            </div>
                            <div>
                                <span>비활성화된 가중치</span>
                                <div className="schedule_boxs">
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                    <div>Migration</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="set_schedule_balance">
                        <label htmlFor="network_port_security">
                            비활성화된 필터 <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                        </label>
                        <select>
                            <option value="default">활성화</option>   
                        </select>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>

            </Modal>

            <Modal
                isOpen={activePopup === 'macNew'}
                onRequestClose={closePopup}
                contentLabel="새로만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_mac_new_popup">
                    <div className="network_popup_header">
                        <h1>새 MAC주소 풀</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>
                    
                    <div className="setting_mac_textboxs">
                        <div>
                            <span>이름</span>
                            <input type="text" />
                        </div>
                        <div>
                            <span>설명</span>
                            <input type="text" />
                        </div>
                    </div>
                    <div className="setting_mac_checkbox">
                        <input type="checkbox" id="allow_duplicate" name="allow_duplicate" />
                        <label htmlFor="allow_duplicate">중복 허용</label>
                    </div>
                    
                    <div className="network_parameter_outer">
                        <span>MAC 주소 범위</span>
                        <div style={{ marginBottom: '0.2rem' }}>
                            <div>
                                <span style={{ marginRight: '0.3rem' }}>범위 시작</span>
                                <input type="text" />
                            </div>
                            <div>
                                <span>범위 끝</span>
                                <input type="text" />
                            </div>
                            <div id="buttons">
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                        <div>
                            MAC수 : 해당없음
                        </div>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>

            </Modal>

            <Modal
                isOpen={activePopup === 'macEdit'}
                onRequestClose={closePopup}
                contentLabel="편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="setting_mac_edit_popup">
                    <div className="network_popup_header">
                        <h1>새 MAC주소 풀</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>
                    
                    <div className="setting_mac_textboxs">
                        <div>
                            <span>이름</span>
                            <input type="text" />
                        </div>
                        <div>
                            <span>설명</span>
                            <input type="text" />
                        </div>
                    </div>
                    <div className="setting_mac_checkbox">
                        <input type="checkbox" id="allow_duplicate" name="allow_duplicate" />
                        <label htmlFor="allow_duplicate">중복 허용</label>
                    </div>
                    
                    <div className="network_parameter_outer">
                        <span>MAC 주소 범위</span>
                        <div style={{ marginBottom: '0.2rem' }}>
                            <div>
                                <span style={{ marginRight: '0.3rem' }}>범위 시작</span>
                                <input type="text" />
                            </div>
                            <div>
                                <span>범위 끝</span>
                                <input type="text" />
                            </div>
                            <div id="buttons">
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                        <div>
                            MAC수 : 해당없음
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

export default MainOuter;
