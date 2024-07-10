import React, { useEffect, useState } from 'react';
import '../App.css';
import { Link } from 'react-router-dom';

function MainOuter({ children }) {
    const [selected, setSelected] = useState('dashboard'); // 기본 선택된 상태 설정
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

    useEffect(() => {
        function adjustFontSize() {
            const width = window.innerWidth;
            const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
            document.documentElement.style.fontSize = fontSize + 'px';
        }

        // 창 크기가 변경될 때 adjustFontSize 함수 호출
        window.addEventListener('resize', adjustFontSize);

        // 컴포넌트가 마운트될 때 adjustFontSize 함수 호출
        adjustFontSize();

        // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
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

    return (
        <div id="main_outer">
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
                {/* aside 끝 */}

                <div id="aside_popup" style={{ display: asidePopupVisible ? 'block' : 'none' }}>
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
                            <div className="aside_popup_content" id="aside_popup_last" style={{ display: isLastVisible ? 'block' : 'none' }}>
                                <div>
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>192.168.0.80</span>
                                </div>
                                <div>
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>HostedEngine</span>
                                </div>
                                <div>
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
                            <div className="aside_popup_content" id="aside_popup_last2" style={{ display: isLastVisibleStorage ? 'block' : 'none' }}>
                                <div>
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>hosted-storage</span>
                                </div>
                                <div>
                                    <i></i>
                                    <i className="fa fa-microchip"></i>
                                    <span>NFS-Storage</span>
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
                {/* aside_popup 끝 */}
            </div>
            {/* aside_outer 끝 */}
            {children}

            {/* 우클릭메뉴박스 끝 */}
            <div id="context_menu">
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
