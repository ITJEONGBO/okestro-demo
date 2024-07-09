import React, { useEffect } from 'react';
import '../App.css';

function Network() {
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
      <div id="network_section">
        <div className="section_header">
          <div className="section_header_left">
            <div>Default</div>
            <button><i className="fa fa-exchange"></i></button>
          </div>
          <div className="section_header_right">
            <div className="article_nav">
              <button id="network_first_edit_btn">편집</button>
              <button>삭제</button>
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
                    <div id="domain">Export to Data Domai</div>
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
              <div className="active">논리 네트워크</div>
            </div>
          </div>

          <div className="storage_domain_content">
            <div className="content_header_right">
              <button id="network_new_btn">새로 만들기</button>
              <button id="network_bring_btn">가져오기</button>
              <button>편집</button>
              <button>삭제</button>
            </div>
            <div>
              <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-2</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
              </div>
            </div>
            <table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>ovirtmgmt</td>
                  <td>Management Network</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div id="network_logic_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div>스토리지</div>
              <div className="active">논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_domain_content">
            <div className="content_header_right">
              <button>새로만들기</button>
              <button>편집</button>
              <button>삭제</button>
            </div>
            <div>
              <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-2</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
              </div>
            </div>
            <table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>ovirtmgmt</td>
                  <td>Management Network</td>
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

export default Network;
