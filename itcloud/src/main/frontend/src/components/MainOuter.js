import React, { useEffect, useState } from 'react';
import '../App.css';
import { Link } from 'react-router-dom';

function MainOuter({ children }) {
    const [selected, setSelected] = useState('dashboard');
    const [asidePopupVisible, setAsidePopupVisible] = useState(false);
    const [asidePopupBackgroundColor, setAsidePopupBackgroundColor] = useState({
        dashboard: '',
        machine: '',
        storage: '',
        network: '',
        setting: ''
    });
    const [isSecondVisible, setIsSecondVisible] = useState(false);
    const [isLastVisible, setIsLastVisible] = useState(false);
    const [isSecondVisibleStorage, setIsSecondVisibleStorage] = useState(false);
    const [isLastVisibleStorage, setIsLastVisibleStorage] = useState(false);
    const [isSecondVisibleNetwork, setIsSecondVisibleNetwork] = useState(false);
    const [contextMenuVisible, setContextMenuVisible] = useState(false);
    const [contextMenuPosition, setContextMenuPosition] = useState({ x: 0, y: 0 });
    const [contextMenuTarget, setContextMenuTarget] = useState(null);
    const [hoverTarget, setHoverTarget] = useState(null);

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

    const handleClick = (id) => {
        setSelected(id);
        toggleAsidePopup(id);
    };

    const toggleAsidePopup = (id) => {
        const newBackgroundColor = {
            dashboard: '',
            machine: '',
            storage: '',
            network: '',
            setting: ''
        };

        if (id === 'machine') {
            setAsidePopupVisible(!asidePopupVisible || selected !== 'machine');
            newBackgroundColor.machine = 'rgb(218, 236, 245)';
        } else if (id === 'storage') {
            setAsidePopupVisible(!asidePopupVisible || selected !== 'storage');
            newBackgroundColor.storage = 'rgb(218, 236, 245)';
        } else if (id === 'network') {
            setAsidePopupVisible(!asidePopupVisible || selected !== 'network');
            newBackgroundColor.network = 'rgb(218, 236, 245)';
        } else if (id === 'setting') {
            setAsidePopupVisible(!asidePopupVisible || selected !== 'setting');
            newBackgroundColor.setting = 'rgb(218, 236, 245)';
        } else {
            setAsidePopupVisible(false);
        }

        setAsidePopupBackgroundColor(newBackgroundColor);
    };

    const handleFirstClick = () => {
        setIsSecondVisible(!isSecondVisible);
        setIsLastVisible(false);
    };

    const handleSecondClick = () => {
        setIsLastVisible(!isLastVisible);
    };

    const handleFirstClickStorage = () => {
        setIsSecondVisibleStorage(!isSecondVisibleStorage);
        setIsLastVisibleStorage(false);
    };

    const handleSecondClickStorage = () => {
        setIsLastVisibleStorage(!isLastVisibleStorage);
    };

    const handleFirstClickNetwork = () => {
        setIsSecondVisibleNetwork(!isSecondVisibleNetwork);
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
                        <Link to='/machine' className="link-no-underline">
                            <div
                                id="aside_popup_machine_btn"
                                className={getClassNames('machine')}
                                onClick={() => handleClick('machine')}
                                style={{ backgroundColor: asidePopupBackgroundColor.machine }}
                            >
                                <i className="fa fa-desktop"></i>
                            </div>
                        </Link>
                        <Link to='/storage' className="link-no-underline">
                            <div
                                id="aside_popup_storage_btn"
                                className={getClassNames('storage')}
                                onClick={() => handleClick('storage')}
                                style={{ backgroundColor: asidePopupBackgroundColor.storage }}
                            >
                                <i className="fa fa-server"></i>
                            </div>
                        </Link>
                        <Link to='/network' className="link-no-underline">
                            <div
                                id="aside_popup_network_btn"
                                className={getClassNames('network')}
                                onClick={() => handleClick('network')}
                                style={{ backgroundColor: asidePopupBackgroundColor.network }}
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
                    {selected === 'machine' && (
                        <div id="virtual_machine_chart">
                            <div className="aside_popup_content" id="aside_popup_first" onClick={handleFirstClick}>
                                <i className="fa fa-chevron-down"></i>
                                <i className="fa fa-building-o"></i>
                                <span>data_center</span>
                            </div>
                            <div className="aside_popup_content" id="aside_popup_second" style={{ display: isSecondVisible ? 'block' : 'none' }} onClick={handleSecondClick}>
                                <i className="fa fa-chevron-down"></i>
                                <i className="fa fa-building-o"></i>
                                <span>ITITINFO</span>
                            </div>
                            <div id="aside_popup_last_machine" style={{ display: isLastVisible ? 'block' : 'none' }}>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, '192.168.0.80')}
                                    onMouseEnter={() => handleMouseEnter('192.168.0.80')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === '192.168.0.80' ? '#e6eefa' : (hoverTarget === '192.168.0.80' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>192.168.0.80</span>
                                </div>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, 'HostedEngine')}
                                    onMouseEnter={() => handleMouseEnter('HostedEngine')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === 'HostedEngine' ? '#e6eefa' : (hoverTarget === 'HostedEngine' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>HostedEngine</span>
                                </div>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, 'on20-ap01')}
                                    onMouseEnter={() => handleMouseEnter('on20-ap01')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === 'on20-ap01' ? '#e6eefa' : (hoverTarget === 'on20-ap01' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>on20-ap01</span>
                                </div>
                            </div>
                        </div>
                    )}
                    {selected === 'storage' && (
                        <div id="storage_chart">
                            <div className="aside_popup_content" id="aside_popup_first2" onClick={handleFirstClickStorage}>
                                <i className="fa fa-chevron-down"></i>
                                <i className="fa fa-building-o"></i>
                                <span>data_center</span>
                            </div>
                            <div className="aside_popup_content" id="aside_popup_second2" style={{ display: isSecondVisibleStorage ? 'block' : 'none' }} onClick={handleSecondClickStorage}>
                                <i className="fa fa-chevron-down"></i>
                                <i className="fa fa-building-o"></i>
                                <span>Default</span>
                            </div>
                            <div id="aside_popup_last_storage" style={{ display: isLastVisibleStorage ? 'block' : 'none' }}>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, '192.168.0.80')}
                                    onMouseEnter={() => handleMouseEnter('192.168.0.80')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === '192.168.0.80' ? '#e6eefa' : (hoverTarget === '192.168.0.80' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>192.168.0.80</span>
                                </div>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, 'HostedEngine')}
                                    onMouseEnter={() => handleMouseEnter('HostedEngine')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === 'HostedEngine' ? '#e6eefa' : (hoverTarget === 'HostedEngine' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>HostedEngine</span>
                                </div>
                                <div
                                    onContextMenu={(e) => handleContextMenu(e, 'on20-ap01')}
                                    onMouseEnter={() => handleMouseEnter('on20-ap01')}
                                    onMouseLeave={handleMouseLeave}
                                    style={{
                                        backgroundColor: contextMenuTarget === 'on20-ap01' ? '#e6eefa' : (hoverTarget === 'on20-ap01' ? '#e6eefa' : 'transparent')
                                    }}
                                >
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>on20-ap01</span>
                                </div>
                            </div>
                        </div>
                    )}
                    {selected === 'network' && (
                        <div id="network_chart">
                            <div className="aside_popup_content" id="aside_popup_first3" onClick={handleFirstClickNetwork}>
                                <i className="fa fa-chevron-down"></i>
                                <i className="fa fa-building-o"></i>
                                <span>Default</span>
                            </div>
                            <div className="aside_popup_content" id="aside_popup_second3" style={{ display: isSecondVisibleNetwork ? 'block' : 'none' }}>
                                <i className="fa fa-chevron-down" style={{ color: 'white' }}></i>
                                <i className="fa fa-building-o"></i>
                                <span>ovirtmgmt</span>
                            </div>
                        </div>
                    )}
                    {selected === 'setting' && (
                        <div id="setting_chart">
                            <div id="setting_normal_btn">
                                <i className="fa fa-cog"></i>
                                <span>활성 사용자 세션</span>
                            </div>
                            <div id="setting_miniset_btn">
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
            {children}
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
