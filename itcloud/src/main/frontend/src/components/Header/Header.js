import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
    const navigate = useNavigate();
    const [isLoginBoxVisible, setLoginBoxVisible] = useState(false);
    const [isBellActive, setBellActive] = useState(false);

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

    const handleTitleClick = () => {
        navigate('/'); 
    };

    const toggleLoginBox = () => {
        setLoginBoxVisible(!isLoginBoxVisible);
        setBellActive(false); // 다른 버튼을 누르면 Bell 비활성화
    };

    const toggleBellActive = () => {
        setBellActive(!isBellActive);
        setLoginBoxVisible(false); // 다른 버튼을 누르면 LoginBox 비활성화
    };

    const handleOutsideClick = (event) => {
        if (
            !event.target.closest('.user_btn') && 
            !event.target.closest('.user_loginbox') && 
            !event.target.closest('.fa-bell-wrapper') && 
            !event.target.closest('.bell_box')
        ) {
            setLoginBoxVisible(false);
            setBellActive(false);
        }
    };

    const stopPropagation = (event) => {
        event.stopPropagation();
    };

    useEffect(() => {
        if (isLoginBoxVisible || isBellActive) {
            document.addEventListener('click', handleOutsideClick);
        } else {
            document.removeEventListener('click', handleOutsideClick);
        }

        return () => {
            document.removeEventListener('click', handleOutsideClick);
        };
    }, [isLoginBoxVisible, isBellActive]);

    return (
        <div id="header">
            <div id="header_right" onClick={handleTitleClick} style={{ cursor: 'pointer' }}>
                <span>Rutil Vm</span>
            </div>

            <div id="header_left">
                {/*알림 */}
                <div 
                    className="fa-bell-wrapper" 
                    onClick={toggleBellActive}
                    style={{ 
                        backgroundColor: isBellActive ? '#ebececd8' : 'transparent',
                        borderRadius: '50%'
                    }}
                >
                    <i className="fa fa-bell"></i>
                    <div className='bell_box' style={{ display: isBellActive ? 'block' : 'none' }} onClick={stopPropagation}>
                        <div>알림</div>
                    </div>
                </div>

                {/*user */}
                <div 
                    className='user_btn' 
                    onClick={toggleLoginBox}
                    style={{ 
                        backgroundColor: isLoginBoxVisible ? '#ebececd8' : 'transparent',
                        borderRadius: '50%'
                    }}
                >
                    <i className="fa fa-user"></i>
                    <span>(user name)</span>
                    <div className='user_loginbox' style={{ display: isLoginBoxVisible ? 'block' : 'none' }} onClick={stopPropagation}>
                        <div>계정설정</div>
                        <div>로그아웃</div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Header;