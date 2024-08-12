import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import Modal from 'react-modal';
import HostDetail from './Computing/HostDetail';
import './MainOuter.css';
import Cluster from './Computing/Cluster';
import Host from './Computing/Host';
import Vm from './Computing/Vm';
import Computing from './Computing';
import Dashboard from './Dashboard'; // Dashboard 컴포넌트를 import
import Network from './Network';
import NetworkDetail from '../detail/NetworkDetail';
import Storage from './Storage'; // Storage.js 파일에서 가져옴
import StorageDomain from '../detail/StorageDomain'; // StorageDomain.js 파일에서 가져옴
import StorageDisk from '../detail/StorageDisk'; // StorageDisk.js 파일에서 가져옴

function MainOuter({ children }) {
    const [selected, setSelected] = useState('dashboard');
    const [selectedDiv, setSelectedDiv] = useState('data_center');
    const [asidePopupVisible, setAsidePopupVisible] = useState(false);
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
    const [isLastVisible, setIsLastVisible] = useState(false);
    
    const [contextMenuVisible, setContextMenuVisible] = useState(false);
    const [contextMenuPosition, setContextMenuPosition] = useState({ x: 0, y: 0 });
    const [contextMenuTarget, setContextMenuTarget] = useState(null);
    const [hoverTarget, setHoverTarget] = useState(null);
    const [activePopup, setActivePopup] = useState(null);
    const [activeSettingForm, setActiveSettingForm] = useState('part');
    const [settingPopupOpen, setSettingPopupOpen] = useState(false);
    const [sectionContent, setSectionContent] = useState('default');
    const [activeSection, setActiveSection] = useState('general');

    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        function adjustFontSize() {
            const width = window.innerWidth;
            const fontSize = width / 40;
            document.documentElement.style.fontSize = fontSize + 'px';
        }
    
        window.addEventListener('resize', adjustFontSize);
        adjustFontSize();
    
        if (location.pathname.includes('/computing')) {
            setSelected('computing');
            setAsidePopupVisible(true);
            setAsidePopupBackgroundColor(prevState => ({
                ...prevState,
                computing: 'rgb(218, 236, 245)'
            }));
            setSelectedDiv('data_center'); // 'computing'의 기본 선택
        } else if (location.pathname.includes('/storage')) {
            setSelected('storage');
            setAsidePopupVisible(true);
            setAsidePopupBackgroundColor(prevState => ({
                ...prevState,
                storage: 'rgb(218, 236, 245)'
            }));
            setSelectedDiv('data_center'); // 'storage'의 기본 선택
        } else if (location.pathname.includes('/network')) {
            setSelected('network');
            setAsidePopupVisible(true);
            setAsidePopupBackgroundColor(prevState => ({
                ...prevState,
                network: 'rgb(218, 236, 245)'
            }));
            setSelectedDiv('default'); // 'network'의 기본 선택
        } else if (location.pathname.includes('/setting')) {
            setSelected('setting');
            setAsidePopupVisible(true);
            setAsidePopupBackgroundColor(prevState => ({
                ...prevState,
                setting: 'rgb(218, 236, 245)'
            }));
        } else {
            setSelected('dashboard');
            setAsidePopupVisible(false);
            setAsidePopupBackgroundColor(prevState => ({
                ...prevState,
                dashboard: 'rgb(218, 236, 245)'
            }));
        }
    
        return () => {
            window.removeEventListener('resize', adjustFontSize);
        };
    }, [location]);
    
    const handleClick = (id) => {
        setSelected(id);
        setSectionContent('default'); 
        toggleAsidePopup(id)
        setSelectedDiv(null);  // 중복 선택 방지
        setSelectedDetail(null);  // 내부 아이템 배경색 제거
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
            setAsidePopupVisible(true);
            newBackgroundColor.computing = 'rgb(218, 236, 245)';
            navigate(`/computing/general`);
        } else if (id === 'storage') {
            setAsidePopupVisible(true);
            newBackgroundColor.storage = 'rgb(218, 236, 245)';
        } else if (id === 'network') {
            setAsidePopupVisible(true);
            newBackgroundColor.network = 'rgb(218, 236, 245)';
        } else if (id === 'setting') {
            setAsidePopupVisible(true);
            newBackgroundColor.setting = 'rgb(218, 236, 245)';
        } else {
            setAsidePopupVisible(false);
        }
    
        setAsidePopupBackgroundColor(newBackgroundColor);
    };

    const handleFirstClick = (e) => {
        e.stopPropagation();
        setIsSecondVisible(!isSecondVisible);
    };

    const handleFirstDivClick = () => {
        setSelectedDiv('data_center');
        setSectionContent('computing');
        navigate('/computing/general');

    };
    
    const handleSecondClick = (e) => {
        e.stopPropagation();
        setSelectedDiv('cluster');
        setIsThirdVisible(!isThirdVisible);
    };

    const handleSecondDivClick = (e) => {
        e.stopPropagation(); // 이벤트 버블링 방지
        setSelectedDiv('cluster');
        console.log('selectedDiv:', 'cluster'); // 상태 업데이트 확인용 로그
        setSectionContent('cluster');
        navigate(`/computing/application`);
        
    };
    
    const handleThirdClick = (e) => {
        e.stopPropagation();
        setIsLastVisible(!isLastVisible);
    };

    const handleThirdDivClick = () => {
        setSelectedDiv('host');
        setSelectedDetail(null);  // 호스트 내부 아이템 선택 해제
        setSectionContent('host');
    };
    
    const handleFirstClickStorage = () => {
        setSelectedDiv('data_center'); // 첫 번째 div 클릭 시 'data_center'로 설정
        setIsSecondVisibleStorage(!isSecondVisibleStorage);
        setIsLastVisibleStorage(false);
        setSectionContent('storage'); // 섹션 콘텐츠를 Storage로 설정
        navigate('/storage'); // Storage.js로 이동
    };
    const handleSecondDivClickStorage = () => {
        setSelectedDiv('storage_domain'); // 두 번째 div 클릭 시 'storage_domain'으로 설정
        setIsLastVisibleStorage(!isLastVisibleStorage);
        setSectionContent('storageDomain'); // 섹션 콘텐츠를 StorageDomain로 설정
    navigate('/storage/storagedomain'); // StorageDomain.js로 이동
    };
    
    const handleDetailClickStorage = (diskId) => {
        setSelectedDiv(diskId); // 선택된 디스크를 설정
        
        setSectionContent('storageDisk'); // 섹션 콘텐츠를 StorageDisk로 설정
        
        // StorageDisk 팝업을 활성화하고 싶다면 아래 코드를 추가합니다.
        setActivePopup('StorageDisk'); 
    
        navigate(`/storagedisk/${diskId}`); // 해당 디스크의 StorageDisk.js로 이동
    };
    


    
    const handleSecondClickStorage = () => {
        setIsLastVisibleStorage(!isLastVisibleStorage);
    };

    const handleFirstClickNetwork = () => {
        setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
        setSelectedDiv('default');
        setSectionContent('network');  // Network.js 컴포넌트로 설정
    };
    const handleSecondDivClickNetwork = () => {
        setSelectedDiv('ovirtmgmt'); // 두 번째 div 클릭 시 'ovirtmgmt'로 설정
        setSectionContent('networkDetail'); 
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

    const handleDetailClick = (content, target) => {
        setSectionContent(content);
        setSelectedDetail(target); 
    };
    const handleFirstIconClickStorage = (e) => {
        e.stopPropagation(); // 이벤트 버블링을 방지하여 div 클릭 이벤트와 혼동되지 않도록 합니다.
        setIsSecondVisibleStorage(!isSecondVisibleStorage);
        setIsLastVisibleStorage(false); // 첫 번째 섹션을 클릭하면 세 번째 섹션을 닫습니다.
    };
    
    const handleSecondIconClickStorage = (e) => {
        e.stopPropagation();
        setIsLastVisibleStorage(!isLastVisibleStorage);
    };
      // 네트워크 섹션을 클릭하면 Network 컴포넌트로 이동
      const handleNetworkClick = () => {
        setSectionContent('network');
    };

    // 네트워크 상세 항목을 클릭하면 NetworkDetail 컴포넌트로 이동
    const handleNetworkDetailClick = () => {
        setSectionContent('networkDetail');
    };
    
    const handleFirstIconClickNetwork = (e) => {
        e.stopPropagation();
        setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
    };
    const handleSecondIconClickNetwork = (e) => {
        e.stopPropagation();
        setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
    };
    const [selectedDetail, setSelectedDetail] = useState(null);
    const handleDivClick = (target) => {
        setSectionContent('vm');
        setSelectedDetail(target); 
        navigate('/computing/vm');
    };

    const getBackgroundColor = (target) => {
        if (selectedDetail === target) {
            return 'rgb(218, 236, 245)';
        }
        if (contextMenuTarget === target) {
            return '#e6eefa';
        }
        if (hoverTarget === target) {
            return '#e6eefa';
        }
        return 'transparent';
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
                        <Link to='/computing/general' className="link-no-underline">
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
    onClick={handleFirstDivClick}
>
    <i 
        className={`fa fa-chevron-${isSecondVisible ? 'down' : 'right'}`} 
        onClick={handleFirstClick}
    ></i>
    <i className="fa fa-building-o"></i>
    <span>data_center</span>
</div>
        {isSecondVisible && (
           <div 
           className="aside_popup_second_content" 
           id="aside_popup_second" 
           style={{ backgroundColor: selectedDiv === 'cluster' ? 'rgb(218, 236, 245)' : '' }} 
           onClick={handleSecondDivClick}  // 여기서 상태가 변경됨
       >
           <i className={`fa fa-chevron-${isThirdVisible ? 'down' : 'right'}`} 
               onClick={handleSecondClick}
           ></i>
           <i className="fa fa-building-o"></i>
           <span>클러스터</span>
       </div>
        )}
        {isThirdVisible && (
            <div 
                className="aside_popup_third_content" 
                id="aside_popup_third" 
                style={{ backgroundColor: selectedDiv === 'host' ? 'rgb(218, 236, 245)' : '' }} 
                onClick={handleThirdDivClick}
            >
                <i className={`fa fa-chevron-${isLastVisible ? 'down' : 'right'}`} 
                    onClick={handleThirdClick}
                ></i>
                <i className="fa fa-building-o"></i>
                <span>호스트</span>
            </div>
        )}
        {isLastVisible && (
            <div id="aside_popup_last_machine">
                <div
                    onClick={() => handleDetailClick('detail1', '192.168.0.80')}
                    onContextMenu={(e) => handleContextMenu(e, '192.168.0.80')}
                    onMouseEnter={() => handleMouseEnter('192.168.0.80')}
                    onMouseLeave={handleMouseLeave}
                    style={{
                        backgroundColor: selectedDiv === '192.168.0.80' ? 'rgb(218, 236, 245)' : (hoverTarget === '192.168.0.80' ? '#e6eefa' : 'transparent')
                    }}
                >
                    <i></i>
                    <i className="fa fa-user"></i>
                    <span>192.168.0.80</span>
                </div>
                <div
                    onClick={() => handleDivClick('HostedEngine')}
                    onContextMenu={(e) => handleContextMenu(e, 'HostedEngine')}
                    onMouseEnter={() => handleMouseEnter('HostedEngine')}
                    onMouseLeave={handleMouseLeave}
                    style={{
                        backgroundColor: selectedDiv === 'HostedEngine' ? 'rgb(218, 236, 245)' : (hoverTarget === 'HostedEngine' ? '#e6eefa' : 'transparent')
                    }}
                >
                    <i></i>
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
            onClick={handleFirstClickStorage} // Storage.js로 이동
            style={{
                backgroundColor: selectedDiv === 'data_center' ? 'rgb(218, 236, 245)' : '', // 첫 번째 div의 배경색 설정
            }}
        >
            <i className={`fa fa-chevron-${isSecondVisibleStorage ? 'down' : 'right'}`}></i>
            <i className="fa fa-building-o"></i>
            <span>data_center</span>
        </div>
        {isSecondVisibleStorage && (
            <div
                className="aside_popup_second_content"
                id="aside_popup_second2"
                onClick={handleSecondDivClickStorage} // StorageDomain.js로 이동
                style={{
                    backgroundColor: selectedDiv === 'storage_domain' ? 'rgb(218, 236, 245)' : '', // 두 번째 div의 배경색 설정
                }}
            >
                <i className={`fa fa-chevron-${isLastVisibleStorage ? 'down' : 'right'}`}></i>
                <i className="fa fa-building-o"></i>
                <span>스토리지 도메인</span>
            </div>
        )}
        {isLastVisibleStorage && (
          
            <div id="aside_popup_last_storage">
                
                <div
                    onClick={() => handleDetailClickStorage('192.168.0.80')} // 특정 디스크의 StorageDisk.js로 이동
                    onContextMenu={(e) => handleContextMenu(e, '192.168.0.80')}
                    onMouseEnter={() => handleMouseEnter('192.168.0.80')}
                    onMouseLeave={handleMouseLeave}
                    style={{
                        backgroundColor: selectedDiv === '192.168.0.80' ? 'rgb(218, 236, 245)' : (hoverTarget === '192.168.0.80' ? '#e6eefa' : 'transparent'), // 첫 번째 디스크 이름의 배경색 설정
                    }}
                >
                    <i className="fa fa-microchip"></i>
                    <span>he_metadata</span>
                </div>
                <div
                    onClick={() => handleDetailClickStorage('HostedEngine')} // 특정 디스크의 StorageDisk.js로 이동
                    onContextMenu={(e) => handleContextMenu(e, 'HostedEngine')}
                    onMouseEnter={() => handleMouseEnter('HostedEngine')}
                    onMouseLeave={handleMouseLeave}
                    style={{
                        backgroundColor: selectedDiv === 'HostedEngine' ? 'rgb(218, 236, 245)' : (hoverTarget === 'HostedEngine' ? '#e6eefa' : 'transparent'), // 두 번째 디스크 이름의 배경색 설정
                    }}
                >
                    <i className="fa fa-microchip"></i>
                    <span>he_metadata</span>
                </div>
                <div
                    onClick={() => handleDetailClickStorage('on20-ap01')} // 특정 디스크의 StorageDisk.js로 이동
                    onContextMenu={(e) => handleContextMenu(e, 'on20-ap01')}
                    onMouseEnter={() => handleMouseEnter('on20-ap01')}
                    onMouseLeave={handleMouseLeave}
                    style={{
                        backgroundColor: selectedDiv === 'on20-ap01' ? 'rgb(218, 236, 245)' : (hoverTarget === 'on20-ap01' ? '#e6eefa' : 'transparent'), // 세 번째 디스크 이름의 배경색 설정
                    }}
                >
                    <i className="fa fa-microchip"></i>
                    <span>he_metadata</span>
                </div>
            </div>
          
        )}
    </div>
)}
{/*네트워크 */} 
{selected === 'network' && (
    <div id="network_chart">
        <div
            className="aside_popup_content"
            id="aside_popup_first3"
            onClick={() => {
                setSelectedDiv('default');
                setSectionContent('network');
                navigate('/network'); // Network.js로 이동
            }}
            style={{
                backgroundColor: selectedDiv === 'default' ? 'rgb(218, 236, 245)' : '', // 첫 번째 div의 배경색 설정
            }}
        >
            <i 
                className={`fa fa-chevron-${isSecondVisibleNetwork ? 'down' : 'right'}`}
                onClick={handleFirstIconClickNetwork} // 아이콘 클릭 시 섹션을 접고 펼치기
            ></i>
            <i className="fa fa-building-o"></i>
            <span>Default</span>
        </div>
        {isSecondVisibleNetwork && (
            <div
                className="aside_popup_second_content"
                id="aside_popup_network_content"
                onClick={() => {
                    setSelectedDiv('ovirtmgmt');
                    setSectionContent('networkDetail');
                    navigate('/network/ovirtmgmt'); // NetworkDetail.js로 이동
                }}
                style={{
                    backgroundColor: selectedDiv === 'ovirtmgmt' ? 'rgb(218, 236, 245)' : '',
                    paddingLeft: '0.85rem' // 두 번째 div의 배경색 설정
                }}
            >
                <i className="fa fa-building-o" style={{ fontSize: '0.34rem', marginRight: '0.05rem' }}></i>
                <span>ovirtmgmt</span>
            </div>
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
            {sectionContent === 'default' ? React.cloneElement(children, { activeSection, setActiveSection }) : (
                    <>
                    {sectionContent === 'detail1' && <HostDetail />}
                    {sectionContent === 'cluster' && <Cluster />}
                    {sectionContent === 'host' && <Host />}
                    {sectionContent === 'vm' && <Vm />}
                    {sectionContent === 'computing' && <Computing />}
                    {sectionContent === 'network' && <Network />} 
                    {sectionContent === 'networkDetail' && <NetworkDetail />}  
                    {sectionContent === 'storage' && <Storage />} 
                    {sectionContent === 'storageDomain' && <StorageDomain />} 
                    {sectionContent === 'storageDisk' && <StorageDisk />} 
                    </>
            )}
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
