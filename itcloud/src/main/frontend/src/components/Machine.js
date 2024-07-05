import React, { useEffect } from 'react';
import '../App.css';

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
            <div className="active">일반</div>
            <div>네트워크 인터페이스</div>
            <div>디스크</div>
            <div>스냅샷</div>
            <div>애플리케이션</div>
            <div>선호도 그룹</div>
            <div>선호도 레이블</div>
            <div>게스트 정보</div>
            <div>권한</div>
            <div>이벤트</div>
          </div>
        </div>

        <div className="tables">
          <div className="table_container_left">
            <table className="table">
              <tr>
                <th>이름:</th>
                <td>on20-ap01</td>
              </tr>
              <tr>
                <th>설명:</th>
                <td></td>
              </tr>
              <tr>
                <th>상태:</th>
                <td>실행 중</td>
              </tr>
              <tr>
                <th>업타임:</th>
                <td>11 days</td>
              </tr>
              <tr className="empty">
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr>
                <th>템플릿:</th>
                <td>Blank</td>
              </tr>
              <tr>
                <th>운영 시스템:</th>
                <td>Linux</td>
              </tr>
              <tr className="empty">
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr>
                <th>펌웨어/장치의 유형:</th>
                <td>BIOS의 Q35 칩셋 <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i></td>                           
              </tr>
              <tr>
                <th>우선 순위:</th>
                <td>높음</td>
              </tr>
              <tr>
                <th>최적화 옵션:</th>
                <td>서버</td>
              </tr>
            </table>
          </div>
          <div id="table_container_center">
            <table className="table">
              <tr>
                <th>설정된 메모리:</th>
                <td>2048 MB</td>
              </tr>
              <tr>
                <th>할당할 실제 메모리:</th>
                <td>2048 MB</td>
              </tr>
              <tr className="empty">
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr>
                <th>게스트 OS의 여유/캐시+비퍼</th>
                <td>1003 / 0 MB</td>
              </tr>
              <tr>
                <th>된 메모리:</th>
                <td></td>
              </tr>
              <tr>
                <th>CPU 코어 수:</th>
                <td>2(2:1:1)</td>
              </tr>
              <tr>
                <th>게스트 CPU 수:</th>
                <td>2</td>
              </tr>
              <tr className="empty">
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr>
                <th>게스트 CPU</th>
                <td>Cascadelake-Server</td>
                <td></td>
              </tr>
              <tr>
                <th>고가용성:</th>
                <td>예</td>
              </tr>
              <tr>
                <th>모니터 수:</th>
                <td>1</td>
              </tr>
              <tr>
                <th>USB:</th>
                <td>비활성화됨</td>
              </tr>
            </table>
          </div>
          <div id="table_container_right">
            <table className="table">
              <tr>
                <th>작성자:</th>
                <td>admin</td>
              </tr>
              <tr>
                <th>소스:</th>
                <td>oVirt</td>
              </tr>
              <tr>
                <th>실행 호스트:</th>
                <td>클러스터 내의 호스트</td>
              </tr>
              <tr>
                <th>사용자 정의 속성:</th>
                <td>설정되지 않음</td>
              </tr>
              <tr>
                <th>클러스터 호환 버전:</th>
                <td>4.7</td>
              </tr>
              <tr>
                <th>가상 머신의 ID:</th>
                <td>Linuxdddddddddddddddddddddd</td>
              </tr>
              <tr className="empty" >
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr className="empty">
                <th>.</th>
                <td style={{ color: 'white' }}>.</td>
              </tr>
              <tr>
                <th>FQDN:</th>
                <td>on20-ap01</td>
              </tr>
              <tr>
                <th>하드웨어 클럭의 시간 오프셋:</th>
                <td>Asia/Seoul</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
    );
};

export default Machine;
