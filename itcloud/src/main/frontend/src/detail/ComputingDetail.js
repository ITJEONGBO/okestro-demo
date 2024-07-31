import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';

// 네트워크 인터페이스 섹션
const NetworkSection = () => {
  return <div>NetworkSection</div>;
};

// 디스크 섹션
const DiskSection = () => {
  return <div>DiskSection</div>;
};

// 스냅샷 섹션
const SnapshotSection = () => {
  return <div>SnapshotSection</div>;
};

// 애플리케이션 섹션
const ApplicationSection = () => {
  return <div>ApplicationSection</div>;
};

// 선호도 그룹 섹션
const PregroupSection = () => {
  return <div>PregroupSection</div>;
};

// 선호도 레이블 섹션
const PregroupLabelSection = () => {
  return <div>PregroupLabelSection</div>;
};

// 게스트 정보 섹션
const GuestInfoSection = () => {
  return <div>GuestInfoSection</div>;
};

// 권한 섹션
const PowerSection = () => {
  return <div>PowerSection</div>;
};

// 이벤트 섹션
const EventSection = () => {
  return <div>EventSection</div>;
};

const sections = [
  { id: 'general', label: '일반' },
  { id: 'network', label: '네트워크 인터페이스' },
  { id: 'disk', label: '디스크' },
  { id: 'snapshot', label: '스냅샷' },
  { id: 'application', label: '애플리케이션' },
  { id: 'pregroup', label: '선호도 그룹' },
  { id: 'pregroup_label', label: '선호도 레이블' },
  { id: 'guest_info', label: '게스트 정보' },
  { id: 'power', label: '권한' },
  { id: 'event', label: '이벤트' }
];

const ComputingDetail = () => {
  const { section } = useParams();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState(section || 'general');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

  useEffect(() => {
    console.log(`Navigating to /computing/${activeSection}`);
    navigate(`/computing/${activeSection}`);
  }, [activeSection, navigate]);

  const handleSectionClick = (section) => {
    console.log(`Section clicked: ${section}`);
    setActiveSection(section);
    navigate(`/computing/${section}`);
  };

  const toggleFooterContent = () => {
    setFooterContentVisibility(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const renderSectionContent = () => {
    switch (activeSection) {
      case 'network':
        return <NetworkSection />;
      case 'disk':
        return <DiskSection />;
      case 'snapshot':
        return <SnapshotSection />;
      case 'application':
        return <ApplicationSection />;
      case 'pregroup':
        return <PregroupSection />;
      case 'pregroup_label':
        return <PregroupLabelSection />;
      case 'guest_info':
        return <GuestInfoSection />;
      case 'power':
        return <PowerSection />;
      case 'event':
        return <EventSection />;
      default:
        return (
         
          <div className="tables">
            <div className="table_container_left">
              <table className="table">
              <div>ddfdsfadfsf</div>
                <tbody>
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
                </tbody>
              </table>
            </div>
            <div id="table_container_center">
              <table className="table">
                <tbody>
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
                </tbody>
              </table>
            </div>
            <div id="table_container_right">
              <table className="table">
                <tbody>
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
                  <tr className="empty">
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
                </tbody>
              </table>
            </div>
          </div>
        );
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
            <button id="migration_btn" onClick={openModal}>마이그레이션</button>
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
            {sections.map((sec) => (
              <div
                key={sec.id}
                className={activeSection === sec.id ? 'active' : ''}
                onClick={() => handleSectionClick(sec.id)}
              >
                {sec.label}
              </div>
            ))}
          </div>
        </div>
        {renderSectionContent()}
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

      {/* 마이그레이션 팝업 */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="마이그레이션"
        className="migration_popup"
        overlayClassName="migration_popup_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="domain_header">
          <h1>가상머신 마이그레이션</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>
        <div id="migration_article_outer">
          <span>1대의 가상 머신이 마이그레이션되는 호스트를 선택하십시오.</span>
          <div id="migration_article">
            <div>
              <div id="migration_dropdown">
                <label htmlFor="host">대상 호스트 <i className="fa fa-info-circle"></i></label>
                <select name="host_dropdown" id="host">
                  <option value="">호스트 자동 선택</option>
                  <option value="php">PHP</option>
                  <option value="java">Java</option>
                </select>
              </div>
            </div>
            <div>
              <div>가상머신</div>
              <div>on20-ap02</div>
            </div>
          </div>
          <div id="migration_footer">
            <button>마이그레이션</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default ComputingDetail;
