import React, { useEffect } from 'react';
import './Header.css';

/**
 * @name Header
 * @description 컴포넌트 > 헤더 
 * @returns 
 */
const Header = ({ }) => {
    useEffect(() => {
        /**
         * @name adjustFontSize
         * @description 
         */
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
    <div id="header">
        <div id="header_right">
            <span>Rutil Vm</span>
        </div>

        <div id="header_left">
            <div>
                <i className="fa fa-hdd-o"></i>
            </div>
            <div>
                <i className="fa fa-bell"></i>
            </div>
            <div>
                <i className="fa fa-user"></i>
                <span>(user name)</span>
            </div>
        </div>
  </div>
  );
}

export default Header;
