import React, { useEffect, useState } from 'react';
import '../App.css';
import Modal from 'react-modal';
// React Modal 설정
Modal.setAppElement('#root');

function Network() {
    const [activeSection, setActiveSection] = useState('common_outer');
    const [isModalOpen, setIsModalOpen] = useState(false);


    // 폰트사이즈
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


      //편집팝업
      useEffect(() => {
        const showEditPopup = () => {
          setActiveSection('common_outer'); // 상태 초기화
          const editPopupBg = document.getElementById('edit_popup_bg');
          if (editPopupBg) {
              editPopupBg.style.display = 'block';
          }
      }

          const editButton = document.getElementById('network_first_edit_btn');
          if (editButton) {
              editButton.addEventListener('click', showEditPopup);
          }

          // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
          return () => {
              if (editButton) {
                  editButton.removeEventListener('click', showEditPopup);
              }
          };
      }, []);
     // 편집팝업 기본 섹션에 스타일 적용
      useEffect(() => {
      
        const defaultElement = document.getElementById('common_outer_btn');
          if (defaultElement) {
              defaultElement.style.backgroundColor = '#EDEDED';
              defaultElement.style.color = '#1eb8ff';
              defaultElement.style.borderBottom = '1px solid blue';
          }
        }, []);
      // 편집팝업스타일 변환 부분
      const handleSectionChange = (section) => {
        setActiveSection(section);
          const elements = document.querySelectorAll('.edit_aside > div');
          elements.forEach(el => {
              el.style.backgroundColor = '#FAFAFA';
              el.style.color = 'black';
              el.style.borderBottom = 'none';
          });

          const activeElement = document.getElementById(`${section}_btn`);
          if (activeElement) {
              activeElement.style.backgroundColor = '#EDEDED';
              activeElement.style.color = '#1eb8ff';
              activeElement.style.borderBottom = '1px solid blue';
          }
      };

      //footer
      const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
      const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
  
      const toggleFooterContent = () => {
          setFooterContentVisibility(!isFooterContentVisible);
      };
  
      const handleFooterTabClick = (tab) => {
          setSelectedFooterTab(tab);
      };

    // 팝업 열기/닫기 핸들러
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

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

                <div className="storage_domain_content" style={{ padding: '0.5rem 0.3rem' }}>
                    <div className="content_header_right">
                        <button id="network_new_btn" onClick={openModal}>새로 만들기</button>
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

            {/*팝업창 예시 */}
                <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                contentLabel="새로 만들기"
                className="network_new_popup"
                overlayClassName="network_new_outer"
            >
                <div>dd</div>
                <button onClick={closeModal}>x</button> 
            </Modal>

             {/*편집팝업 */}
            <div id="edit_popup_bg" style={{ display: 'none' }}>
                <div id="edit_popup">
                    <div className="edit_header">
                        <h1>템플릿 수정</h1>
                        <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>
                            <i className="fa fa-times"></i>
                        </button>
                    </div>
                    <div className="edit_body">
                      <div className="edit_aside">
                          <div className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`} id="common_outer_btn" onClick={() => handleSectionChange('common_outer')}>
                              <span>일반</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`} id="system_outer_btn" onClick={() => handleSectionChange('system_outer')}>
                              <span>시스템</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'start_outer' ? 'active' : ''}`} id="start_outer_btn" onClick={() => handleSectionChange('start_outer')}>
                              <span>초기 실행</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'console_outer' ? 'active' : ''}`} id="console_outer_btn" onClick={() => handleSectionChange('console_outer')}>
                              <span>콘솔</span>
                          </div>
                      </div>
                      <div className="edit_aside">
                          <div className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`} id="host_outer_btn" onClick={() => handleSectionChange('host_outer')}>
                              <span>호스트</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`} id="ha_mode_outer_btn" onClick={() => handleSectionChange('ha_mode_outer')}>
                              <span>고가용성</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`} id="res_alloc_outer_btn" onClick={() => handleSectionChange('res_alloc_outer')}>
                              <span>리소스 할당</span>
                          </div>
                          <div className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`} id="boot_outer_btn" onClick={() => handleSectionChange('boot_outer')}>
                              <span>부트 옵션</span>
                          </div>
                      </div>

                        <form action="#">
                            {/* 일반 */}
                            <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="edit_second_content">
                                    <div>
                                        <label htmlFor="name">이름</label>
                                        <input type="text" id="name" value="test02" />
                                    </div>
                                    <div>
                                        <label htmlFor="base-version" style={{ color: 'gray' }}>하위 버전 이름</label>
                                        <input type="text" id="base-version" value="base version" disabled />
                                    </div>
                                    <div>
                                        <label htmlFor="description">설명</label>
                                        <input type="text" id="description" />
                                    </div>
                                </div>
                            </div>

                            {/* 시스템 */}
                            <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="edit_second_content">
                                    <div>
                                        <label htmlFor="memory_size">메모리 크기</label>
                                        <input type="text" id="memory_size" value="2048 MB" readOnly />
                                    </div>
                                    <div>
                                        <div>
                                            <label htmlFor="max_memory">최대 메모리</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" id="max_memory" value="8192 MB" readOnly />
                                    </div>

                                    <div>
                                        <div>
                                            <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                    </div>

                                    <div>
                                        <div>
                                            <label htmlFor="total_cpu">총 가상 CPU</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" id="total_cpu" value="1" readOnly />
                                    </div>
                                    <div>
                                        <div>
                                            <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                            <span>고급 매개 변수</span>
                                        </div>
                                    </div>
                                    <div style={{ fontWeight: 600 }}>일반</div>
                                    <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                        <div>
                                            <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="time_offset">
                                            <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            {/* 콘솔 */}
                            <div id="console_outer" style={{ display: activeSection === 'console_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="res_alloc_checkbox" style={{ marginBottom: 0 }}>
                                    <span>그래픽 콘솔</span>
                                    <div>
                                        <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                        <label htmlFor="memory_balloon">헤드릭스(headless)모드</label>
                                        <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                                    </div>
                                </div>

                                <div className="edit_second_content">
                                    <div style={{ paddingTop: 0 }}>
                                        <label htmlFor="memory_size">비디오 유형</label>
                                        <input type="text" id="memory_size" value="VGA" readOnly />
                                    </div>
                                    <div>
                                        <div>
                                            <label htmlFor="max_memory">그래픽 프로토콜</label>
                                        </div>
                                        <input type="text" id="max_memory" value="VNC" readOnly />
                                    </div>

                                    <div>
                                        <div>
                                            <label htmlFor="actual_memory">VNC 키보드 레이아웃</label>
                                        </div>
                                        <input type="text" id="actual_memory" value="기본값[en-us]" readOnly />
                                    </div>

                                    <div>
                                        <div>
                                            <label htmlFor="total_cpu">콘솔 분리 작업</label>
                                        </div>
                                        <input type="text" id="total_cpu" value="화면 잠금" readOnly />
                                    </div>
                                    <div>
                                        <div>
                                            <label htmlFor="disconnect_action_delay">Disconnect Action Delay in Minutes</label>
                                        </div>
                                        <input type="text" id="disconnect_action_delay" value="0" disabled />
                                    </div>
                                    <div id="monitor">
                                        <label htmlFor="screen">모니터</label>
                                        <select id="screen">
                                            <option value="test02">1</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="console_checkboxs">
                                    <div className="console_checkbox">
                                        <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                        <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">USB활성화</label>
                                    </div>
                                    <div className="console_checkbox">
                                        <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                        <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">스마트카드 사용가능</label>
                                    </div>
                                    <span>단일 로그인 방식</span>
                                    <div className="console_checkbox">
                                        <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                        <label htmlFor="memory_balloon">USB활성화</label>
                                    </div>
                                    <div className="console_checkbox">
                                        <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                        <label htmlFor="memory_balloon">스마트카드 사용가능</label>
                                    </div>
                                </div>
                            </div>

                            {/* 호스트 */}
                            <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="host_second_content">
                                    <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                    <div className="form_checks">
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                            <label className="form-check-label" htmlFor="flexRadioDefault1">
                                                클러스터 내의 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <div>
                                                <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                                <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                    특정 호스트
                                                </label>
                                            </div>
                                            <div>
                                                <select id="specific_host_select">
                                                    <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="host_checkboxs">
                                        <span>CPU 옵션:</span>
                                        <div className="host_checkbox">
                                            <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                            <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <div className="host_checkbox">
                                            <input type="checkbox" id="tsc_migration" name="tsc_migration" />
                                            <label htmlFor="tsc_migration">TSC 주파수가 동일한 호스트에서만 마이그레이션</label>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                    </div>
                                </div>

                                <div id="host_third_content">
                                    <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                    <div>
                                        <div>
                                            <span>마이그레이션 모드</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="migration_mode">
                                            <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                        </select>
                                    </div>
                                    <div>
                                        <div>
                                            <span>마이그레이션 정책</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="migration_policy">
                                            <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                        </select>
                                    </div>
                                    <div>
                                        <div>
                                            <span>마이그레이션 암호화 사용</span>
                                        </div>
                                        <select id="migration_encryption">
                                            <option value="클러스터 기본값(암호화하지 마십시오)">클러스터 기본값(암호화하지 마십시오)</option>
                                        </select>
                                    </div>
                                    <div>
                                        <div>
                                            <span>Parallel Migrations</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="parallel_migrations" readOnly>
                                            <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                        </select>
                                    </div>
                                    <div>
                                        <div style={{ paddingBottom: '4%' }}>
                                            <span style={{ color: 'gray' }}>Number of VM Migration Connection</span>
                                        </div>
                                        <select id="vm_migration_connections" disabled>
                                            <option value=""></option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            {/* 고가용성 */}
                            <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="ha_mode_second_content">
                                    <div className="check_box">
                                        <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                        <label className="check_label" htmlFor="ha_mode_box">
                                            고가용성
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <span>가상 머신 임대 대상 스토리지 도메인</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="no_lease" disabled>
                                            <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <div>
                                            <span>재개 동작</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <select id="force_shutdown">
                                            <option value="강제 종료">강제 종료</option>
                                        </select>
                                    </div>
                                    <div className="ha_mode_article">
                                        <span>실행/마이그레이션 큐에서 우선순위</span>
                                        <div>
                                            <span>우선 순위</span>
                                            <select id="priority">
                                                <option value="낮음">낮음</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div className="ha_mode_article">
                                        <span>위치독</span>
                                        <div>
                                            <span>위치독 모델</span>
                                            <select id="watchdog_model">
                                                <option value="감시 장치 없음">감시 장치 없음</option>
                                            </select>
                                        </div>
                                        <div>
                                            <span style={{ color: 'gray' }}>위치독 작업</span>
                                            <select id="watchdog_action" disabled>
                                                <option value="없음">없음</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            {/* 리소스 할당 */}
                            <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="res_second_content">
                                    <div className="cpu_res">
                                        <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                        <div>
                                            <span>CPU 프로파일</span>
                                            <select id="watchdog_action">
                                                <option value="없음">Default</option>
                                            </select>
                                        </div>
                                        <div>
                                            <span>CPU 공유</span>
                                            <div id="cpu_sharing">
                                                <select id="watchdog_action" style={{ width: '63%' }}>
                                                    <option value="없음">비활성화됨</option>
                                                </select>
                                                <input type="text" value="0" disabled />
                                            </div>
                                        </div>
                                        <div>
                                            <span>CPU Pinning Policy</span>
                                            <select id="watchdog_action">
                                                <option value="없음">None</option>
                                            </select>
                                        </div>
                                        <div>
                                            <div>
                                                <span>CPU 피닝 토폴로지</span>
                                                <i className="fa fa-info-circle"></i>
                                            </div>
                                            <input type="text" disabled />
                                        </div>
                                    </div>

                                    <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                    <div id="threads">
                                        <div>
                                            <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                            <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                        </div>
                                        <div>
                                            <input type="text" />
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            {/* 부트 옵션 */}
                            <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                                <div className="edit_first_content">
                                    <div>
                                        <label htmlFor="cluster">클러스터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                        <div>데이터센터 Default</div>
                                    </div>

                                    <div>
                                        <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                        <select id="template" disabled>
                                            <option value="test02">test02</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="os">운영 시스템</label>
                                        <select id="os">
                                            <option value="linux">Linux</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                        <select id="firmware">
                                            <option value="bios">BIOS의 Q35 칩셋</option>
                                        </select>
                                    </div>
                                    <div style={{ marginBottom: '2%' }}>
                                        <label htmlFor="optimization">최적화 옵션</label>
                                        <select id="optimization">
                                            <option value="server">서버</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="res_second_content">
                                    <div className="cpu_res">
                                        <span style={{ fontWeight: 600 }}>부트순서:</span>
                                        <div>
                                            <span>첫 번째 장치</span>
                                            <select id="watchdog_action">
                                                <option value="없음">하드디스크</option>
                                            </select>
                                        </div>
                                        <div>
                                            <span>두 번째 장치</span>
                                            <select id="watchdog_action">
                                                <option value="없음">Default</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div id="boot_checkboxs">
                                        <div>
                                            <div>
                                                <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                                <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                            </div>
                                            <div>
                                                <input type="text" disabled />
                                                <i className="fa fa-info-circle"></i>
                                            </div>
                                        </div>

                                        <div>
                                            <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                            <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div className="edit_footer">
                        <button>고급 옵션 숨기기</button>
                        <button>OK</button>
                        <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>취소</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Network;
