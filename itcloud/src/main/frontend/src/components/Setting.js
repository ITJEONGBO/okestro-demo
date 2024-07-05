import React, { useEffect } from 'react';
import '../App.css';

function Setting() {
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
        <div id="setting_section">
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
            <table>
              <thead>
                <tr>
                  <th>세션 DB ID</th>
                  <th>사용자 이름</th>
                  <th>인증 공급자</th>
                  <th>사용자 ID</th>
                  <th>소스 IP</th>
                  <th>세션 시작 시간</th>
                  <th>마지막 세션 활성</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>3204</td>
                  <td>admin</td>
                  <td>internal-authz</td>
                  <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                  <td>192.168.0.218</td>
                  <td>2024. 1. 19. PM 1:04:09</td>
                  <td>2024. 1. 19. PM 4:45:55</td>
                </tr>
                <tr>
                  <td>3206</td>
                  <td>admin</td>
                  <td>internal-authz</td>
                  <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                  <td>192.168.0.214</td>
                  <td>2024. 1. 19. PM 3:46:55</td>
                  <td>2024. 1. 19. PM 4:45:55</td>
                </tr>
                <tr>
                  <td>3204</td>
                  <td>admin</td>
                  <td>internal-authz</td>
                  <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                  <td>192.168.0.218</td>
                  <td>2024. 1. 19. PM 1:04:09</td>
                  <td>2024. 1. 19. PM 4:45:55</td>
                </tr>
                <tr>
                  <td>3206</td>
                  <td>admin</td>
                  <td>internal-authz</td>
                  <td>b5e54b30-a5f3-11ee-81fa-00163...</td>
                  <td>192.168.0.214</td>
                  <td>2024. 1. 19. PM 3:46:55</td>
                  <td>2024. 1. 19. PM 4:45:55</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <div className="footer_outer">
          <div className="footer">
            <button><i className="fa fa-chevron-down"></i></button>
            <div>
              <a>최근 작업</a>
              <a>경보</a>
            </div>
          </div>
          <div className="footer_content">
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
        </div>
      </div>
    );
};

export default Setting;
