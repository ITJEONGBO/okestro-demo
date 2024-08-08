import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import NetworkDetail from '../detail/NetworkDetail';
import './Network.css';
import { Table} from './table/Table';
import HeaderButton from './button/HeaderButton';

// React Modal 설정
Modal.setAppElement('#root');

const Network = () => {
    const [activeSection, setActiveSection] = useState('common_outer');
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [activePopup, setActivePopup] = useState(null);

    // 세부페이지이동
    const [showNetworkSection, setShowNetworkSection] = useState(true);
    const handleNetworkNameClick = () => {
        setShowNetworkSection(false);
    };

    // 폰트사이즈 조절
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

    // 편집 팝업
    useEffect(() => {
        const showEditPopup = () => {
            setActiveSection('common_outer');
            const editPopupBg = document.getElementById('edit_popup_bg');
            if (editPopupBg) {
                editPopupBg.style.display = 'block';
            }
        }

        const editButton = document.getElementById('network_first_edit_btn');
        if (editButton) {
            editButton.addEventListener('click', showEditPopup);
        }

        return () => {
            if (editButton) {
                editButton.removeEventListener('click', showEditPopup);
            }
        };
    }, []);

    // 편집 팝업 기본 섹션 스타일 적용
    useEffect(() => {
        const defaultElement = document.getElementById('common_outer_btn');
        if (defaultElement) {
            defaultElement.style.backgroundColor = '#EDEDED';
            defaultElement.style.color = '#1eb8ff';
            defaultElement.style.borderBottom = '1px solid blue';
        }
    }, []);

    // 편집 팝업 스타일 변경
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

    // footer
    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

    const toggleFooterContent = () => {
        setFooterContentVisibility(!isFooterContentVisible);
    };

    const handleFooterTabClick = (tab) => {
        setSelectedFooterTab(tab);
    };

    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
    };

    const closePopup = () => {
        setActivePopup(null);
    };

    const handleTabClick = (tab) => {
        setSelectedTab(tab);
    };

    // 테이블 데이터
    const data = [
        { name: 'ovirtmgmt', description: 'Management Network', dataCenter: 'DC1', provider: 'Provider1', portSeparation: '아니요' },
        { name: 'example1', description: 'Example Description 1', dataCenter: 'DC2', provider: 'Provider2', portSeparation: '아니요' },
        { name: 'example2', description: 'Example Description 2', dataCenter: 'DC3', provider: 'Provider3', portSeparation: '아니요' },
        // 필요한 만큼 데이터 추가
    ];

    const columns = [
        { header: '이름', accessor: 'name', clickable: true },
        { header: '설명', accessor: 'description', clickable: false },
        { header: '데이터센터', accessor: 'dataCenter', clickable: false },
        { header: '공급자', accessor: 'provider', clickable: false },
        { header: '포트분리', accessor: 'portSeparation', clickable: false },
    ];
    
      // Button and popup items for the SectionHeader
  const sectionHeaderButtons = [
    { id: 'network_first_edit_btn', label: '편집', onClick: () => {} },
    { label: '삭제', onClick: () => {} },
  ];

  const sectionHeaderPopupItems = [
    '가져오기',
    '가상 머신 복제',
    '삭제',
    '마이그레이션 취소',
    '변환 취소',
    '템플릿 생성',
    '도메인으로 내보내기',
    'Export to Data Domai',
    'OVA로 내보내기',
  ];
    return (
        <div id="network_section">
            {showNetworkSection ? ( // 꼭 div 하나로 감싸주기
                <div>
                     <HeaderButton
            title="네트워크"
            buttons={sectionHeaderButtons}
            popupItems={sectionHeaderPopupItems}
          />

                    <div className="content_outer">
                        
                        <div className='empty_nav_outer'>
                            
                                <div className="content_header_right">
                                    <button id="network_new_btn" onClick={() => openPopup('newNetwork')}>새로 만들기</button>
                                    <button id="network_bring_btn" onClick={() => openPopup('getNetwork')}>가져오기</button>
                                    <button>편집</button>
                                    <button>삭제</button>
                                </div>
                            <div className="section_table_outer">
                                <Table columns={columns} data={data} onRowClick={handleNetworkNameClick} />
                            </div>
                        </div>
                        
                    </div>
                </div>
            ) : (
                <NetworkDetail />
            )}

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

            {/* 편집팝업 */}
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
                                        <label htmlFor="memory_balloon">헤드리스(headless)모드</label>
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
                                        <span>감시</span>
                                        <div>
                                            <span>감시 모델</span>
                                            <select id="watchdog_model">
                                                <option value="감시 장치 없음">감시 장치 없음</option>
                                            </select>
                                        </div>
                                        <div>
                                            <span style={{ color: 'gray' }}>감시 작업</span>
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

            {/* 새로만들기 팝업 */}
            <Modal
                isOpen={activePopup === 'newNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_new_popup">
                    <div className="network_popup_header">
                        <h1>새 논리적 네트워크</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="network_new_nav">
                        <div
                            id="network_new_common_btn"
                            className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('network_new_common_btn')}
                        >
                            일반
                        </div>
                        <div
                            id="network_new_cluster_btn"
                            className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('network_new_cluster_btn')}
                        >
                            클러스터
                        </div>
                        <div
                            id="network_new_vnic_btn"
                            className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('network_new_vnic_btn')}
                            style={{ borderRight: 'none' }}
                        >
                            vNIC 프로파일
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedTab === 'network_new_common_btn' && (
                        <form id="network_new_common_form">
                            <div className="network_first_contents">
                                <div className="network_form_group">
                                    <label htmlFor="cluster">데이터 센터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <div>
                                        <label htmlFor="name">이름</label>
                                        <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                                    </div>
                                    <input type="text" id="name" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="comment">코멘트</label>
                                    <input type="text" id="comment" />
                                </div>
                            </div>

                            <div className="network_second_contents">
                                <span>네트워크 매개변수</span>
                                <div className="network_form_group">
                                    <label htmlFor="network_label">네트워크 레이블</label>
                                    <input type="text" id="network_label" />
                                </div>
                                <div className="network_checkbox_type1">
                                    <div>
                                        <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                                        <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                                    </div>
                                    <input type="text" id="valn_tagging_input" disabled />
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">가상 머신 네트워크</label>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                                    <label htmlFor="photo_separation">포토 분리</label>
                                </div>
                                <div className="network_radio_group">
                                    <div style={{ marginTop: '0.2rem' }}>MTU</div>
                                    <div>
                                        <div className="radio_option">
                                            <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                                            <label htmlFor="default_mtu">기본값 (1500)</label>
                                        </div>
                                        <div className="radio_option">
                                            <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                                            <label htmlFor="user_defined_mtu">사용자 정의</label>
                                        </div>
                                    </div>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DB서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation" />
                                    <label htmlFor="external_vendor_creation">외부 업체에서 작성</label>
                                </div>
                                <span>외부</span>
                                <div className="network_form_group" style={{ paddingTop: 0 }}>
                                    <label htmlFor="external_provider">외부 공급자</label>
                                    <select id="external_provider">
                                        <option value="default">ovirt-provider-ovn</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="network_port_security">네트워크 포트 보안</label>
                                    <select id="network_port_security">
                                        <option value="default">활성화</option>
                                    </select>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network" />
                                    <label htmlFor="connect_to_physical_network">물리적 네트워크에 연결</label>
                                </div>
                            </div>
                        </form>
                    )}
                    {/* 클러스터 */}
                    {selectedTab === 'network_new_cluster_btn' && (
                        <form id="network_new_cluster_form">
                            <span>클러스터에서 네트워크를 연결/분리</span>
                            <div>
                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th>이름</th>
                                            <th><input type="checkbox" id="connect_all" /><label htmlFor="connect_all"> 모두 연결</label></th>
                                            <th><input type="checkbox" id="require_all" /><label htmlFor="require_all"> 모두 필요</label></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Default</td>
                                            <td className="checkbox-group"><input type="checkbox" id="connect_default" /><label htmlFor="connect_default"> 연결</label></td>
                                            <td className="checkbox-group"><input type="checkbox" id="require_default" /><label htmlFor="require_default"> 필수</label></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {/* vNIC 프로파일 */}
                    {selectedTab === 'network_new_vnic_btn' && (
                        <form id="network_new_vnic_form">
                            <span>vNIC 프로파일</span>
                            <div>
                                <input type="text" id="vnic_profile" />
                                <div>
                                    <input type="checkbox" id="public" disabled />
                                    <label htmlFor="public">공개</label>
                                    <i className="fa fa-info-circle" style={{ color: 'rgb(83, 163, 255)' }}></i>
                                </div>
                                <label htmlFor="qos">QoS</label>
                                <select id="qos">
                                    <option value="none">제한 없음</option>
                                </select>
                                <div className="network_new_vnic_buttons">
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </form>
                    )}
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>

            {/* 가져오기 팝업 */}
            <Modal
                isOpen={activePopup === 'getNetwork'}
                onRequestClose={closePopup}
                contentLabel="가져오기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_bring_popup">
                    <div className="network_popup_header">
                        <h1>네트워크 가져오기</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="network_form_group">
                        <label htmlFor="cluster" style={{ fontSize: '0.33rem' }}>네트워크 공급자</label>
                        <select id="cluster">
                            <option value="default">Default</option>
                        </select>
                    </div>

                    <div id="network_bring_table_outer">
                        <span>공급자 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="provider_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화' },
                                    // 필요한 만큼 데이터 추가
                                ]}
                            />
                        </div>
                    </div>

                    <div id="network_bring_table_outer">
                        <span>가져올 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                    { header: '데이터 센터', accessor: 'dataCenter' },
                                    { header: '모두허용', accessor: 'allowAll' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="import_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화', dataCenter: '디스크 활성화', allowAll: '디스크 활성화' },
                                    // 필요한 만큼 데이터 추가
                                ]}
                            />
                        </div>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>가져오기</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
        </div>
    );
};

export default Network;
