import React, { useEffect } from 'react';
import '../App.css';

function Footer() {
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
    <div className="footer_outer">
        <div className="footer">
        <button>
            <i className="fa fa-chevron-down"></i>
        </button>
        <div>
            <a>최근 작업</a>
            <a>경보</a>
        </div>
        </div>
        <div className="footer_content">
        <div className="footer_nav">
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
            <div style={{ borderRight: 'none' }}>
            <div>작업이름</div>
            <div>
                <i className="fa fa-filter"></i>
            </div>
            </div>
        </div>
        <div className="footer_img">
            <span>항목을 찾지 못했습니다</span>
        </div>
        </div>
    </div>    
  );
}

export default Footer;
