import React, { useState, useEffect } from 'react';
import '../App.css';


// 각 섹션에 대한 컴포넌트를 추가합니다. 필요에 따라 세부 내용을 추가하세요.
const Disk = () => <div id="disk_outer">디스크 내용</div>;
const Snapshot = () => <div id="snapshot_outer">스냅샷 내용</div>;
const Application = () => <div id="application_outer">애플리케이션 내용</div>;
const Pregroup = () => <div id="pregroup_outer">선호도 그룹 내용</div>;
const PregroupLabel = () => <div id="pregroup_lable_outer">선호도 레이블 내용</div>;
const GuestInfo = () => <div id="guest_info_outer">게스트 정보 내용</div>;
const Power = () => <div id="power_outer">권한 내용</div>;
const Event = () => <div id="event_outer">이벤트 내용</div>;

// 일반
function Machine() {
  const [activeIndex, setActiveIndex] = useState(0);

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

  const renderContent = () => {
    switch (activeIndex) {
      case 0:
        return <div className="content_outer">일반 내용</div>;
      case 1:
        return <NetworkInterface onBack={() => setActiveIndex(0)} />;
      case 2:
        return <Disk />;
      case 3:
        return <Snapshot />;
      case 4:
        return <Application />;
      case 5:
        return <Pregroup />;
      case 6:
        return <PregroupLabel />;
      case 7:
        return <GuestInfo />;
      case 8:
        return <Power />;
      case 9:
        return <Event />;
      default:
        return null;
    }
  };

  return (
    <div id="section">
      <div className="section_header">
        <div className="section_header_left">
          <span>가상머신</span>
          <div>on20-ap01</div>
          <button><i className="fa fa-exchange"></i></button>
        </div>

        <div className="section_header_right">
          <div className="article_nav">
            <button id="edit_btn">편집</button>
            <div>
              <button>
                <i className="fa fa-play"></i>실행
              </button>
            </div>
            <button><i className="fa fa-pause"></i>일시중지</button>
            <div>
              <button>
                <i className="fa fa-stop"></i>종료
              </button>
            </div>
            <div>
              <button>
                <i className="fa fa-repeat"></i>재부팅
              </button>
            </div>
            <button><i className="fa fa-desktop"></i>콘솔</button>
            <button>스냅샷 생성</button>
            <button id="migration_btn">마이그레이션</button>
            <button id="popup_btn">
              <i className="fa fa-ellipsis-v"></i>
              <div id="popup_box">
                <div>
                  <div className="get_btn">가져오기</div>
                  <div className="get_btn">가상 머신 복제</div>
                </div>
                <div>
                  <div>삭제</div>
                </div>
                <div>
                  <div>마이그레이션 취소</div>
                  <div>변환 취소</div>
                </div>
                <div>
                  <div id="template_btn">템플릿 생성</div>
                </div>
                <div style={{ borderBottom: 'none' }}>
                  <div id="domain2">도메인으로 내보내기</div>
                  <div id="domain">Export to Data Domain</div>
                  <div id="ova_btn">OVA로 내보내기</div>
                </div>
              </div>
            </button>
          </div>
        </div>
      </div>

      <div className="content_outer">
        <div className="content_header">
          <div className="content_header_left">
            <div className={activeIndex === 0 ? 'active' : ''} onClick={() => setActiveIndex(0)}>일반</div>
            <div className={activeIndex === 1 ? 'active' : ''} onClick={() => setActiveIndex(1)}>네트워크 인터페이스</div>
            <div className={activeIndex === 2 ? 'active' : ''} onClick={() => setActiveIndex(2)}>디스크</div>
            <div className={activeIndex === 3 ? 'active' : ''} onClick={() => setActiveIndex(3)}>스냅샷</div>
            <div className={activeIndex === 4 ? 'active' : ''} onClick={() => setActiveIndex(4)}>애플리케이션</div>
            <div className={activeIndex === 5 ? 'active' : ''} onClick={() => setActiveIndex(5)}>선호도 그룹</div>
            <div className={activeIndex === 6 ? 'active' : ''} onClick={() => setActiveIndex(6)}>선호도 레이블</div>
            <div className={activeIndex === 7 ? 'active' : ''} onClick={() => setActiveIndex(7)}>게스트 정보</div>
            <div className={activeIndex === 8 ? 'active' : ''} onClick={() => setActiveIndex(8)}>권한</div>
            <div className={activeIndex === 9 ? 'active' : ''} onClick={() => setActiveIndex(9)}>이벤트</div>
          </div>
        </div>

        {renderContent()}
      </div>
    </div>
  );
}

// 네트워크 인터페이스
const NetworkInterface = ({ onBack }) => {
  useEffect(() => {
    const container = document.getElementById('network_content_outer');
    const originalContent = document.querySelector('.network_content');

    for (let i = 0; i < 3; i++) {
      const clone = originalContent.cloneNode(true);
      container.appendChild(clone);
    }
  }, []);

  return (
    <div id="network_outer">

      <div id="network_content_outer">
        <div className="content_header_right">
          <button id="network_popup_new">새로 만들기</button>
          <button>수정</button>
          <button>제거</button>
        </div>
        <div className="network_content">
          <div>
            <i className="fa fa-chevron-right"></i>
            <i className="fa fa-arrow-circle-o-up" style={{ color: '#21c50b', marginLeft: '0.3rem' }}></i>
            <i className="fa fa-plug"></i>
            <i className="fa fa-usb"></i>
            <span>nic1</span>
          </div>
          <div>
            <div>네트워크 이름</div>
            <div>ovirtmgmt</div>
          </div>
          <div>
            <div>IPv4</div>
            <div>192.168.10.147</div>
          </div>
          <div>
            <div>IPv6</div>
            <div>192.168.10.147</div>
          </div>
          <div style={{ paddingRight: '3%' }}>
            <div>MAC</div>
            <div>192.168.10.147</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Machine;
