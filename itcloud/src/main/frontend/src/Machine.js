import React, { useEffect } from 'react';
import './App.css';


function Machine() {
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
    
    <div>dd</div>
    
  );
}

export default Machine;
