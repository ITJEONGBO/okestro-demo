import React, { useEffect } from 'react';
import '../App.css';
import { Link } from 'react-router-dom';

function MainOuter({ children }) {
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

    return (
        <div id="main_outer">
            <div id="aside_outer">
                <div id="aside">
                    <div id="nav">
                        <Link to='/' className="link-no-underline">
                            <div id="aside_popup_dashboard_btn">
                                <i className="fa fa-th-large"></i>
                            </div>
                        </Link>
                        <Link to='/machine' className="link-no-underline">
                            <div id="aside_popup_machine_btn">
                                <i className="fa fa-desktop"></i>
                            </div>
                        </Link>
                        <Link to='/storage' className="link-no-underline">
                            <div id="aside_popup_storage_btn">
                                <i className="fa fa-server"></i>
                            </div>
                        </Link>
                        <Link to='/network' className="link-no-underline">
                            <div id="aside_popup_network_btn">
                                <i className="fa fa-database"></i>
                            </div>
                        </Link>
                    </div>
                    <Link to='/setting' className="link-no-underline">
                        <div id="setting_icon">
                            <i className="fa fa-cog"></i>
                        </div>
                    </Link>
                </div>
                {/* aside끝 */}

                <div id="aside_popup"></div>
                {/* aside_popup끝 */}
            </div>
            {/* aside_outer끝 */}
            {children}

        </div>
    );
}

export default MainOuter;
