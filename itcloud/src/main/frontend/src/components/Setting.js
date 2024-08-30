import React, { useEffect, useState } from 'react';
import './Setting.css';
import { Table, TableColumnsInfo } from './table/Table';

const Setting = ({ }) => {
    //테이블 컴포넌트
    const sessionData = [
      {
        sessionId: '3204',
        username: 'admin',
        authProvider: 'internal-authz',
        userId: 'b5e54b30-a5f3-11ee-81fa-00163...',
        sourceIp: '192.168.0.218',
        sessionStartTime: '2024. 1. 19. PM 1:04:09',
        lastSessionActive: '2024. 1. 19. PM 4:45:55',
      },
      {
        sessionId: '3206',
        username: 'admin',
        authProvider: 'internal-authz',
        userId: 'b5e54b30-a5f3-11ee-81fa-00163...',
        sourceIp: '192.168.0.214',
        sessionStartTime: '2024. 1. 19. PM 3:46:55',
        lastSessionActive: '2024. 1. 19. PM 4:45:55',
      },
      {
        sessionId: '3204',
        username: 'admin',
        authProvider: 'internal-authz',
        userId: 'b5e54b30-a5f3-11ee-81fa-00163...',
        sourceIp: '192.168.0.218',
        sessionStartTime: '2024. 1. 19. PM 1:04:09',
        lastSessionActive: '2024. 1. 19. PM 4:45:55',
      },
      {
        sessionId: '3206',
        username: 'admin',
        authProvider: 'internal-authz',
        userId: 'b5e54b30-a5f3-11ee-81fa-00163...',
        sourceIp: '192.168.0.214',
        sessionStartTime: '2024. 1. 19. PM 3:46:55',
        lastSessionActive: '2024. 1. 19. PM 4:45:55',
      },
    ];
    //
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
      //footer
      const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
      const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
  
      const toggleFooterContent = () => {
          setFooterContentVisibility(!isFooterContentVisible);
      };
  
      const handleFooterTabClick = (tab) => {
          setSelectedFooterTab(tab);
      };

    return (
        <div id="setting_section" style={{ padding: '0.5rem ' }}>
        <div className="section_header">
          <div className="section_header_left">
            <div style={{ color: 'gray' }}>관리 &gt; </div>
            <div>활성 사용자 세션</div>
          </div>
        </div>
  
        <div className="content_outer">
          <div className="storage_domain_content">
            <div>
              <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-2</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
                <div className="search_box">
                  <input type="text" />
                  <button><i className="fa fa-search"></i></button>
                </div>
                <button>세션종료</button>
              </div>
            </div>
            <Table columns={TableColumnsInfo.SESSIONS} data={sessionData} onRowClick={() => console.log('Row clicked')} />
          </div>
        </div>
  
        <div className="footer_outer">
                <div className="footer">
                    <button onClick={toggleFooterContent}><i className="fa fa-chevron-down"></i></button>
                    <div>
                        <div
                            style={{
                                color: selectedFooterTab === 'recent' ? 'black' : '#4F4F4F',
                                borderBottom: selectedFooterTab === 'recent' ? '1px solid blue' : 'none'
                            }}
                            onClick={() => handleFooterTabClick('recent')}
                        >
                            최근 작업
                        </div>
                        <div
                            style={{
                                color: selectedFooterTab === 'alerts' ? 'black' : '#4F4F4F',
                                borderBottom: selectedFooterTab === 'alerts' ? '1px solid blue' : 'none'
                            }}
                            onClick={() => handleFooterTabClick('alerts')}
                        >
                            경보
                        </div>
                    </div>
                </div>
                {isFooterContentVisible && (
                    <div className="footer_content" style={{ display: 'block' }}>
                        <div className="footer_nav">
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            <div style={{ borderRight: 'none' }}>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                        </div>
                        <div className="footer_img">
                            <img src="img/화면 캡처 2024-04-30 164511.png" alt="스크린샷" />
                            <span>항목을 찾지 못했습니다</span>
                        </div>
                    </div>
                )}
            </div>
      </div>
    );
};

export default Setting;
