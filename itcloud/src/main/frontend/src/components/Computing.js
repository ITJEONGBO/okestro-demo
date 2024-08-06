import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';

import HeaderButton from './button/HeaderButton';
import NavButton from './navigation/NavButton';
import ComputingDetail from './Computing/Vm';

// React Modal 설정
Modal.setAppElement('#root');

// 템플릿
const NetworkSection = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [visibleDetails, setVisibleDetails] = useState([]);
  useEffect(() => {
    setVisibleDetails(Array(3).fill(false)); // 초기 상태: 모든 detail 숨김
  }, []);
  // 팝업 열기/닫기 핸들러
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <div id="network_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="network_popup_new" onClick={openModal}>가져오기</button>
          <button>편집</button>
          <button>삭제</button>
          <button>내보내기</button>
          <button>새 가상머신</button>
        </div>
        <table>
          <thead>
            <tr>
              <th>이름</th>
              <th>버전</th>
              <th>코멘트</th>
              <th>생성 일자</th>
              <th>상태</th>
              <th>보관</th>
              <th>클러스터</th>
              <th>데이터 센터</th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Blank</td>
              <td></td>
              <td></td>
              <td>2008.4.1.AM</td>
              <td>OK</td>
              <td></td>
              <td></td>
              <td></td>
              <td>설명</td>
            </tr>
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="새로만들기"
        className="network_popup"
        overlayClassName="network_popup_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="network_popup_header">
          <h1>네트워크 인터페이스 수정</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>
        <div className="network_popup_content">
          <div className="input_box">
            <span>이름</span>
            <input type="text"/>
          </div>
          <div className="select_box">
            <label htmlFor="profile">프로파일</label>
            <select id="profile">
              <option value="test02">ovirtmgmt/ovirtmgmt</option>
            </select>
          </div>
          <div className="select_box">
            <label htmlFor="type" style={{ color: 'gray' }}>유형</label>
            <select id="type" disabled>
              <option value="test02">VirtIO</option>
            </select>
          </div>
          <div className="select_box2" style={{ marginBottom: '2%' }}>
            <div>
              <input type="checkbox" id="custom_mac_box" disabled/>
              <label htmlFor="custom_mac_box" style={{ color: 'gray' }}>
                사용자 지정 MAC 주소
              </label>
            </div>
            <div>
              <select id="mac_address" disabled>
                <option value="test02">VirtIO</option>
              </select>
            </div>
          </div>
          <div className="plug_radio_btn">
            <span>링크 상태</span>
            <div>
              <div className="radio_outer">
                <div>
                  <input type="radio" name="status" id="status_up"/>
                  <img src=".//img/스크린샷 2024-05-24 150455.png" alt="status_up"/>
                  <label htmlFor="status_up">Up</label>
                </div>
                <div>
                  <input type="radio" name="status" id="status_down"/>
                  <img src=".//img/Down.png" alt="status_down"/>
                  <label htmlFor="status_down">Down</label>
                </div>
              </div>
            </div>
          </div>
          <div className="plug_radio_btn">
            <span>카드 상태</span>
            <div>
              <div className="radio_outer">
                <div>
                  <input type="radio" name="connection_status" id="connected"/>
                  <img src=".//img/연결됨.png" alt="connected"/>
                  <label htmlFor="connected">연결됨</label>
                </div>
                <div>
                  <input type="radio" name="connection_status" id="disconnected"/>
                  <img src=".//img/분리.png" alt="disconnected"/>
                  <label htmlFor="disconnected">분리</label>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="network_parameter_outer">
          <span>네트워크 필터 매개변수</span>
          <div>
            <div>
              <span>이름</span>
              <input type="text"/>
            </div>
            <div>
              <span>값</span>
              <input type="text"/>
            </div>
            <div id="buttons">
              <button>+</button>
              <button>-</button>
            </div>
          </div>
        </div>
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>
    </div>
  );
};

const SnapshotSection = () => {
  return (
    <div id="network_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button>새로 만들기</button>
          <button>편집</button>
          <button>삭제</button>
        </div>
        <table>
          <thead>
            <tr>
              <th></th>
              <th></th>
              <th>이름</th>
              <th>코멘트</th>
              <th>스토리지 유형</th>
              <th>상태</th>
              <th>호환 버전</th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><i className="fa fa-external-link"></i></td>
              <td></td>
              <td>Default</td>
              <td></td>
              <td>공유됨</td>
              <td>Up</td>
              <td>4.7</td>
              <td>The default Data Center</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

const ApplicationSection = () => {
  return (
    <div id="application_outer">
      <div div className="pregroup_content">
        <div className="content_header_right">
          <button>새로 만들기</button>
          <button>편집</button>
          <button>업그레이드</button>
        </div>
        <div className="section_table_outer">
          <button>
            <i className="fa fa-refresh"></i>
          </button>
          <table>
            <thead>
              <tr>
                <th>상태</th>
                <th>이름</th>
                <th>코멘트</th>
                <th>호환 버전</th>
                <th>설명</th>
                <th>클러스터 CPU 유형</th>
                <th>호스트 수</th>
                <th>가상 머신 수</th>
                <th>업그레이드 상태</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td></td>
                <td>Default</td>
                <td></td>
                <td>4.7</td>
                <td>The derault server cluster</td>
                <td>Secure Intel Cascadelak</td>
                <td>2</td>
                <td>7</td>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

const Computing = () => {
  const { section } = useParams();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState('snapshot');  // 기본 섹션을 'snapshot'으로 설정
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showDetail, setShowDetail] = useState(false);  // 추가된 상태

  useEffect(() => {
    navigate(`/computing/${activeSection}`);
  }, [activeSection, navigate, showDetail]);

  const handleSectionClick = (section) => {
    setActiveSection(section);
  };

  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

  const toggleFooterContent = () => {
    setFooterContentVisibility(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const [isPopupVisible, setIsPopupVisible] = useState(false);

  const togglePopup = () => {
    setIsPopupVisible(!isPopupVisible);
  };

  const handleRowClick = () => {
    setShowDetail(true);  // detail 표시 설정
  };

  const sectionHeaderButtons = [

  ];
  
  const sectionHeaderPopupItems = [

  ];

  const navSections = [
    { id: 'snapshot', label: '데이터 센터' },
    { id: 'application', label: '클러스터' },
    { id: 'network', label: '템플릿' },
  ];

  if (showDetail) {
    return <ComputingDetail />;  // detail 표시
  }

  return (
    <div id="section">
      <HeaderButton
        title="데이터 센터"
        subtitle="on20-ap01"
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
        openModal={openModal}
        togglePopup={togglePopup}
      />
      <div className="content_outer">
        <NavButton
          sections={navSections}
          activeSection={activeSection}
          handleSectionClick={handleSectionClick}
        />

        {activeSection === 'snapshot' && <SnapshotSection />}
        {activeSection === 'application' && <ApplicationSection />}
        {activeSection === 'network' && <NetworkSection />}
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
                <div >작업이름</div>
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


      {/*나중에 지우기 */}
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

export default Computing;
