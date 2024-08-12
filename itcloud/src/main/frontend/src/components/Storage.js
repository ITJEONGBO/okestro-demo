import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import StorageDomain from '../detail/StorageDomain';
import StorageDisk from '../detail/StorageDisk';
import HeaderButton from './button/HeaderButton';
import './Storage.css';
import NavButton from './navigation/NavButton';
import { Table } from './table/Table';

Modal.setAppElement('#root'); // React 16 이상에서는 필수

const Storage = () => {
  // 테이블 컴포넌트
  //디스크
  const columns = [
    { header: '별칭', accessor: 'alias', clickable: true },
    { header: 'ID', accessor: 'id', clickable: false },
    { header: '', accessor: 'icon1', clickable: false },
    { header: '', accessor: 'icon2', clickable: false },
    { header: '연결 대상', accessor: 'connectionTarget', clickable: false },
    { header: '스토리지 도메인', accessor: 'storageDomain', clickable: false },
    { header: '가상 크기', accessor: 'virtualSize', clickable: false },
    { header: '상태', accessor: 'status', clickable: false },
    { header: '유형', accessor: 'type', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
  ];

  const data = [
    {
      alias: 'he_metadata',
      id: '289137398279301798',
      icon1: '',
      icon2: <i className="fa fa-glass"></i>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: 'he_metadata',
      id: '289137398279301798',
      icon1: '',
      icon2: <i className="fa fa-glass"></i>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: 'he_metadata',
      id: '289137398279301798',
      icon1: '',
      icon2: <i className="fa fa-glass"></i>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
  ];
  const handleRowClick = (row) => {
    if (row.alias === 'he_metadata') {
      handleDomainNameClick();
    }
  };
  //
  //도메인

  // 이벤트
  const eventcolumns = [
    { header: '', accessor: 'icon', clickable: false },
    { header: '시간', accessor: 'time', clickable: false },
    { header: '메세지', accessor: 'message', clickable: false },
    { header: '상관 관계 ID', accessor: 'correlationId', clickable: false },
    { header: '소스', accessor: 'source', clickable: false },
    { header: '사용자 지정 이벤트 ID', accessor: 'customEventId', clickable: false }
  ];
  const eventdata = [
    { icon: <i className="fa fa-check"></i>, time: '2024. 1. 17. PM 3:14:39', message: "Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' has been completed.", correlationId: '4b4b417a-c...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2024. 1. 17. PM 3:14:21', message: "Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' was initiated by admin@intern...", correlationId: '4b4b417a-c...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-times"></i>, time: '2024. 1. 5. AM 8:37:54', message: 'Failed to restart VM on2o-ap01 on host host01.ititinfo.com', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-times"></i>, time: '2024. 1. 5. PM 8:37:10', message: 'VM on2o-ap01 is down with error. Exit message: VM terminated with error.', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2024. 1. 5. PM 8:34:29', message: 'Trying to restart VM on2o-ap01 on host host01.ititinfo.com', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-exclamation"></i>, time: '2024. 1. 5. PM 8:29:10', message: 'VM on2o-ap01 was set to the Unknown status.', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2023. 12. 29. PM 12:55:08', message: 'VM on2o-ap01 started on Host host01.ititinfo.com', correlationId: 'a99b6ae8-8d...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2023. 12. 29. PM 12:54:48', message: 'VM on2o-ap01 was started by admin@internal-authz (Host: host01.ititinfo.com).', correlationId: 'a99b6ae8-8d...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2023. 12. 29. PM 12:54:18', message: 'VM on2o-ap01 configuration was updated by admin@internal-authz.', correlationId: 'e3b8355e-06...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2023. 12. 29. PM 12:54:15', message: 'VM on2o-ap01 configuration was updated by admin@internal-authz.', correlationId: '793fb95e-6df...', source: 'oVirt', customEventId: '' },
    { icon: <i className="fa fa-check"></i>, time: '2023. 12. 29. PM 12:53:53', message: 'VM on2o-ap01 has been successfully imported from the given configuration.', correlationId: 'ede53bc8-c6...', source: 'oVirt', customEventId: '' }
  ];

  useEffect(() => {
    function adjustFontSize() {
      const width = window.innerWidth;
      const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
      document.documentElement.style.fontSize = fontSize + 'px';
    }
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();

    return () => {
      window.removeEventListener('resize', adjustFontSize);
    };
  }, []);

  // State for active section
  const [activeSection, setActiveSection] = useState('disk');
  const [showStorageSection, setShowStorageSection] = useState(true);

  // Footer state
  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
  const [activeTab, setActiveTab] = useState('img');

  const toggleFooterContent = () => {
    setFooterContentVisibility(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };

  const handleSectionClick = (section) => {
    setActiveSection(section);
  };

  // 세부페이지
  const handleDomainNameClick = () => {
    if (activeSection === 'disk') {
      setShowStorageSection(false);
      setActivePopup('StorageDisk');
    } else if (activeSection === 'domain') {
      setShowStorageSection(false);
      setActivePopup('StorageDetail');
    }
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const [activePopup, setActivePopup] = useState(null);

  const openPopup = (popupType) => {
    setActivePopup(popupType);
  };

  const closePopup = () => {
    setActivePopup(null);
  };

  const [isPopupBoxVisible, setPopupBoxVisibility] = useState(false);

  const togglePopupBox = () => {
    setPopupBoxVisibility(!isPopupBoxVisible);
  };

  const handlePopupBoxItemClick = (e) => {
    e.stopPropagation();
  };

  const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
  const toggleDomainHiddenBox = () => {
    setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
  };
  const [isDomainHiddenBox2Visible, setDomainHiddenBox2Visible] = useState(false);
  const toggleDomainHiddenBox2 = () => {
    setDomainHiddenBox2Visible(!isDomainHiddenBox2Visible);
  };

  const sectionHeaderButtons = [
    { label: '편집', onClick: () => {} },
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
  const navSections = [
    { id: 'disk', label: '디스크' },
    { id: 'domain', label: '도메인' },
    { id: 'volume', label: '볼륨' },
    { id: 'storage', label: '스토리지' },
    { id: 'logic_network', label: '논리 네트워크' },
    { id: 'cluster', label: '클러스터' },
    { id: 'right', label: '권한' },
    { id: 'event', label: '이벤트' },
  ];
  return (
    <div id="storage_section">
      {!showStorageSection ? (
        activePopup === 'StorageDisk' ? (
          <StorageDisk
            togglePopupBox={togglePopupBox}
            isPopupBoxVisible={isPopupBoxVisible}
            handlePopupBoxItemClick={handlePopupBoxItemClick}
          />
        ) : (
          <StorageDomain
            togglePopupBox={togglePopupBox}
            isPopupBoxVisible={isPopupBoxVisible}
            handlePopupBoxItemClick={handlePopupBoxItemClick}
          />
        )
      ) : (
        <div>
          <HeaderButton
            title="스토리지"
            subtitle=""
            buttons={sectionHeaderButtons}
            popupItems={sectionHeaderPopupItems}
          />
          
          <div className="content_outer">
            <NavButton
              sections={navSections}
              activeSection={activeSection}
              handleSectionClick={handleSectionClick}
            />
            <div className="host_btn_outer">

              {/*디스크 */}
              {activeSection === 'disk' && (
                <>

                  <div className="content_header_right">
                    <button id="storage_disk_new_btn" onClick={() => openPopup('newDisk')}>새로 만들기</button>
                    <button>수정</button>
                    <button>제거</button>
                    <button>이동</button>
                    <button>복사</button>
                    <button id="storage_disk_upload" onClick={() => openPopup('uploadDisk')}>업로드</button>
                    <button>다운로드</button>
                    <button className="content_header_popup_btn">
                      <i className="fa fa-ellipsis-v"></i>
                      <div className="content_header_popup" style={{ display: 'none' }}>
                        <div>활성</div>
                        <div>비활성화</div>
                        <div>이동</div>
                        <div>LUN 새로고침</div>
                      </div>
                    </button>
                  </div>

                  <div className="section_table_outer">
                    <button>
                      <i className="fa fa-refresh"></i>
                    </button>
                    <Table columns={columns} data={data} onRowClick={() => handleRowClick(data[0])} />
                  </div>
               
                </>
              )}
              {activeSection === 'domain' && (
                <>

                    <div className="content_header_right">
                      <button id="new_domain_btn" onClick={() => openPopup('newDomain')}>새로운 도메인</button>
                      <button id="get_domain_btn" onClick={() => openPopup('getDomain')}>도메인 가져오기</button>
                      <button id="administer_domain_btn" onClick={() => openPopup('manageDomain')}>도메인 관리</button>
                      <button>삭제</button>
                      <button>Connections</button>
                      <button className="content_header_popup_btn">
                        <i className="fa fa-ellipsis-v"></i>
                        <div className="content_header_popup" style={{ display: 'none' }}>
                          <div>활성</div>
                          <div>비활성화</div>
                          <div>이동</div>
                          <div>LUN 새로고침</div>
                        </div>
                      </button>
                    </div>

                    <div className="section_table_outer">
                      <div className="search_box">
                        <input type="text" />
                        <button><i className="fa fa-search"></i></button>
                      </div>
                      <table>
                        <thead>
                          <tr>
                            <th>상태</th>
                            <th></th>
                            <th onClick={handleDomainNameClick}>도메인 이름</th>
                            <th>코멘트</th>
                            <th>도메인 유형</th>
                            <th>스토리지 유형</th>
                            <th>포맷</th>
                            <th>데이터 센터간 상태</th>
                            <th>전체 공간(GB)</th>
                            <th>여유 공간(GB)</th>
                            <th>확보된 여유 공간(GB)</th>
                            <th>설명</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                            <td><i className="fa fa-glass"></i></td>
                            <td onClick={handleDomainNameClick}>ddddddd</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                          </tr>
                          <tr>
                            <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                            <td><i className="fa fa-glass"></i></td>
                            <td onClick={handleDomainNameClick}>dddd</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                          </tr>
                          <tr>
                            <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                            <td><i className="fa fa-glass"></i></td>
                            <td onClick={handleDomainNameClick}>ddddd</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                </>
              )}
              {activeSection === 'volume' && (
                <>
                    <div className="content_header_right">
                      <button id="storage_volume_new_btn" onClick={() => openPopup('newVolume')}>새로 만들기</button>
                      <button>삭제</button>
                      <button>시작</button>
                      <button className="disabled_button">중지</button>
                      <button>프로파일링</button>
                      <button style={{ border: 'none' }}>
                        <button id="storage_volume_snap_btn" style={{ margin: 0 }} onClick={() => openPopup('volumeSnap')}>스냅샷</button>
                        <button id="storage_volume_option_boxbtn">
                          <i className="fa fa-chevron-down"></i>
                          <div className="storage_volume_option_box" style={{ display: 'none' }}>
                            <div>새로 만들기</div>
                            <div>스케줄 편집</div>
                            <div>옵션 - 클러스터</div>
                            <div>옵션 - 볼륨</div>
                          </div>
                        </button>
                      </button>
                      <button>지역 복제</button>
                      <button className="content_header_popup_btn">
                        <i className="fa fa-ellipsis-v"></i>
                        <div className="content_header_popup" style={{ display: 'none' }}>
                          <div>활성</div>
                          <div>비활성화</div>
                          <div>이동</div>
                          <div>LUN 새로고침</div>
                        </div>
                      </button>
                    </div>

                    <div className="section_table_outer">
                      <div className="search_box">
                        <input type="text" />
                        <button><i className="fa fa-search"></i></button>
                      </div>
                      <div className="empty_table">
                        <table>
                          <thead>
                            <tr>
                              <th>이름</th>
                              <th>클러스터</th>
                              <th>볼륨 유형</th>
                              <th>브릭</th>
                              <th>정보</th>
                              <th>사용한 공간</th>
                              <th>작업</th>
                              <th>스냅샷 수</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td colSpan="8" className="empty_content">표시할 항목이 없습니다</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                    </>
              )}
              {activeSection === 'storage' && (
                <>
                  <div className="section_table_outer">
                    <div className="content_header_right">
                      <button>데이터 연결</button>
                      <button>ISP 연결</button>
                      <button>내보내기 연결</button>
                      <button>분리</button>
                      <button>활성</button>
                      <button>유지보수</button>
                    </div>
                    
                    <table>
                      <thead>
                        <tr>
                          <th>상태</th>
                          <th></th>
                          <th>도메인 이름</th>
                          <th>코멘트</th>
                          <th>도메인 유형</th>
                          <th>스토리지 유형</th>
                          <th>포맷</th>
                          <th>데이터 센터간 상태</th>
                          <th>전체 공간(GB)</th>
                          <th>여유 공간(GB)</th>
                          <th>확보된 여유 공간(GB)</th>
                          <th>설명</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                        </tr>
                        <tr>
                          <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                        </tr>
                        <tr>
                          <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                          <td></td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </>
              )}
              {activeSection === 'logic_network' && (
                <>
                  <div className="section_table_outer">
                    <div className="content_header_right">
                      <button>새로만들기</button>
                      <button>편집</button>
                      <button>삭제</button>
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
                </>
              )}
              {activeSection === 'cluster' && (
                <>
                  <div className="host_empty_outer">
                    <div className="section_table_outer">
                      <table>
                        <thead>
                          <tr>
                            <th>이름</th>
                            <th>호환 버전</th>
                            <th>설명</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>ovirtmgmt</td>
                            <td>Management Network</td>
                            <td>The default server cluster</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>

                </>
              )}
              {activeSection === 'right' && (
                <>
                    <div className="content_header_right">
                      <button>추가</button>
                      <button>제거</button>
                    </div>
                    <div className="section_table_outer">
                      <div className="storage_right_btns">
                        <span>Permission Filters:</span>
                        <div>
                          <button>All</button>
                          <button>Direct</button>
                        </div>
                      </div>
                      
                      <table>
                        <thead>
                          <tr>
                            <th></th>
                            <th>사용자</th>
                            <th>인증 공급자</th>
                            <th>네임스페이스</th>
                            <th>역할</th>
                            <th>생성일</th>
                            <th>Inherited From</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td><i className="fa fa-user"></i></td>
                            <td>ovirtmgmt</td>
                            <td></td>
                            <td>*</td>
                            <td>SuperUser</td>
                            <td>2023.12.29 AM 11:40:58</td>
                            <td>(시스템)</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                </>
              )}
              {activeSection === 'event' && (
                <>
                <div className="content_header_right">
                <button>새로 만들기</button>
                <button>편집</button>
                <button>제거</button>
              </div>
              
              <div className="section_table_outer">
                <Table columns={eventcolumns} data={eventdata} onRowClick={() => console.log('Row clicked')} />
              </div>

              </>
              )}
            </div>
          </div>
          <div className="footer_outer">
            <div className="footer">
              <button onClick={toggleFooterContent}>
                <i className="fa fa-chevron-down"></i>
              </button>
              <div>
                <div
                  style={{
                    color: selectedFooterTab === 'recent' ? 'black' : '#4F4F4F',
                    borderBottom: selectedFooterTab === 'recent' ? '1px solid blue' : 'none',
                  }}
                  onClick={() => handleFooterTabClick('recent')}
                >
                  최근 작업
                </div>
                <div
                  style={{
                    color: selectedFooterTab === 'alerts' ? 'black' : '#4F4F4F',
                    borderBottom: selectedFooterTab === 'alerts' ? '1px solid blue' : 'none',
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
      )}

      {/*디스크(업로드)팝업 */}
      <Modal
        isOpen={activePopup === 'uploadDisk'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_upload_popup">
          <div className="network_popup_header">
            <h1>이미지 업로드</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          <div className="storage_upload_first">
            <button>파일 선택</button>
            <div>선택된 파일 없음</div>
          </div>
          <div className="storage_upload_second">
            <div className="disk_option">디스크 옵션</div>
            <div className="disk_new_img" style={{ paddingTop: '0.4rem' }}>
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" disabled />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="data_hub">데이터 센터</label>
                  <select id="data_hub">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="storage_zone">스토리지 도메인</label>
                  <select id="storage_zone">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="disk_pattern">디스크 프로파일</label>
                  <select id="disk_pattern">
                    <option value="nfs_storage">NFS-Storage</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="compute_unit">호스트</label>
                  <select id="compute_unit">
                    <option value="host01">host01.ititinfo.com</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" id="reset_after_deletion" />
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
                <div style={{ marginBottom: '0.4rem' }}>
                  <input type="checkbox" id="incremental_backup" defaultChecked />
                  <label htmlFor="incremental_backup">중복 백업 사용</label>
                </div>
                <div>
                  <button>연결 테스트</button>
                </div>
              </div>
            </div>
          </div>
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*디스크(새로만들기)팝업 */}
      <Modal
        isOpen={activePopup === 'newDisk'}
        onRequestClose={closePopup}
        contentLabel="새 가상 디스크"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_new_popup">
          <div className="network_popup_header">
            <h1>새 가상 디스크</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          <div id="disk_new_nav">
            <div
              id="storage_img_btn"
              onClick={() => handleTabClick('img')}
              className={activeTab === 'img' ? 'active' : ''}
            >
              이미지
            </div>
            <div
              id="storage_directlun_btn"
              onClick={() => handleTabClick('directlun')}
              className={activeTab === 'directlun' ? 'active' : ''}
            >
              직접LUN
            </div>
            <div
              id="storage_managed_btn"
              onClick={() => handleTabClick('managed')}
              className={activeTab === 'managed' ? 'active' : ''}
            >
              관리되는 블록
            </div>
          </div>
          {/*이미지*/}
          {activeTab === 'img' && (
            <div className="disk_new_img">
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">데이터 센터</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">스토리지 도메인</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">할당 정책</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">디스크 프로파일</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" id="reset_after_deletion" />
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
                <div>
                  <input type="checkbox" id="incremental_backup" defaultChecked />
                  <label htmlFor="incremental_backup">중복 백업 사용</label>
                </div>
              </div>
            </div>
          )}
          {/*직접LUN*/}
          {activeTab === 'directlun' && (
            <div id="storage_directlun_outer">
              <div id="storage_lun_first">
                <div className="disk_new_img_left">
                  <div className="img_input_box">
                    <span>별칭</span>
                    <input type="text" />
                  </div>
                  <div className="img_input_box">
                    <span>설명</span>
                    <input type="text" />
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">데이터 센터</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">호스트</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">스토리지 타입</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                </div>
                <div className="disk_new_img_right">
                  <div>
                    <input type="checkbox" className="shareable" />
                    <label htmlFor="shareable">공유 가능</label>
                  </div>
                </div>
              </div>
            </div>
          )}
          {/*관리되는 블록 */}
          {activeTab === 'managed' && (
            <div id="storage_managed_outer">
              <div id="disk_managed_block_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" disabled />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" value="on20-ap01_Disk1" disabled />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" disabled />
                </div>
                <div className="img_select_box">
                  <label htmlFor="data_center_select">데이터 센터</label>
                  <select id="data_center_select" disabled>
                    <option value="dc_linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="storage_domain_select">스토리지 도메인</label>
                  <select id="storage_domain_select" disabled>
                    <option value="sd_linux">Linux</option>
                  </select>
                </div>
                <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
              </div>
              <div id="disk_managed_block_right">
                <div>
                  <input type="checkbox" id="disk_shared_option" disabled />
                  <label htmlFor="disk_shared_option">공유 가능</label>
                </div>
              </div>
            </div>
          )}
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*도메인(새로운 도메인)팝업 */}
      <Modal
        isOpen={activePopup === 'newDomain'}
        onRequestClose={closePopup}
        contentLabel="새로운 도메인"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_domain_new_popup">
          <div className="network_popup_header">
            <h1>새로운 도메인</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="storage_domain_new_first">
            <div className="domain_new_left">
              <div className="domain_new_select">
                <label htmlFor="data_hub_location">데이터 센터</label>
                <select id="data_hub_location">
                  <option value="linux">Default(VS)</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="domain_feature_set">도메인 기능</label>
                <select id="domain_feature_set">
                  <option value="linux">데이터</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="storage_option_type">스토리지 유형</label>
                <select id="storage_option_type">
                  <option value="linux">NFS</option>
                </select>
              </div>
              <div className="domain_new_select" style={{ marginBottom: 0 }}>
                <label htmlFor="host_identifier">호스트</label>
                <select id="host_identifier">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
            </div>
            <div className="domain_new_right">
              <div className="domain_new_select">
                <label>이름</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>설명</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>코멘트</label>
                <input type="text" />
              </div>
            </div>
          </div>

          <div className="storage_domain_new_second">
            <div>
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}></i>
              <span>사용자 정의 연결 매개 변수</span>
              <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                <div className="domain_new_select">
                  <label htmlFor="nfs_version">NFS 버전</label>
                  <select id="nfs_version">
                    <option value="host02_ititinfo_com">host02.ititinfo.com</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">재전송</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">추가 마운트 옵션</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}></i>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                    <option value="linux">V5</option>
                  </select>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                </div>

              </div>
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*도메인(도메인 가져오기)팝업 */}
      <Modal
        isOpen={activePopup === 'getDomain'}
        onRequestClose={closePopup}
        contentLabel="도메인 가져오기"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_domain_get_popup">
          <div className="network_popup_header">
            <h1>사전 구성된 도메인 가져오기</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="storage_domain_new_first">
            <div className="domain_new_left">
              <div className="domain_new_select">
                <label htmlFor="data_hub_location">데이터 센터</label>
                <select id="data_hub_location">
                  <option value="linux">Default(VS)</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="domain_feature_set">도메인 기능</label>
                <select id="domain_feature_set">
                  <option value="linux">데이터</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="storage_option_type">스토리지 유형</label>
                <select id="storage_option_type">
                  <option value="linux">NFS</option>
                </select>
              </div>
              <div className="domain_new_select" style={{ marginBottom: 0 }}>
                <label htmlFor="host_identifier">호스트</label>
                <select id="host_identifier">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
            </div>
            <div className="domain_new_right">
              <div className="domain_new_select">
                <label>이름</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>설명</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>코멘트</label>
                <input type="text" />
              </div>
            </div>
          </div>

          <div className="storage_domain_new_second">
            <div>
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}></i>
              <span>사용자 정의 연결 매개 변수</span>
              <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                <div className="domain_new_select">
                  <label htmlFor="nfs_version">NFS 버전</label>
                  <select id="nfs_version">
                    <option value="host02_ititinfo_com">host02.ititinfo.com</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">재전송</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">추가 마운트 옵션</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}></i>
              <span>고급 매개 변수</span>
                <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                    <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                    </select>
                  </div>
                  <div className="hidden_checkbox">
                    <input type="checkbox" id="reset_after_deletion"/>
                    <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                  </div>
                  <div className="hidden_checkbox">
                    <input type="checkbox" id="backup_vault"/>
                    <label htmlFor="backup_vault">백업</label>
                  </div>
                  <div className="hidden_checkbox">
                    <input type="checkbox" id="activate_domain"/>
                    <label htmlFor="activate_domain">데이터 센터에 있는 도메인을 활성화</label>
                  </div>
                </div>
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*도메인(도메인 관리)팝업 */}
      <Modal
        isOpen={activePopup === 'manageDomain'}
        onRequestClose={closePopup}
        contentLabel="도메인 관리"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_domain_administer_popup">
          <div className="network_popup_header">
            <h1>도메인 관리</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="storage_domain_new_first">
            <div className="domain_new_left">
              <div className="domain_new_select">
                <label htmlFor="data_hub_location">데이터 센터</label>
                <select id="data_hub_location">
                  <option value="linux">Default(VS)</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="domain_feature_set">도메인 기능</label>
                <select id="domain_feature_set">
                  <option value="linux">데이터</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="storage_option_type">스토리지 유형</label>
                <select id="storage_option_type">
                  <option value="linux">NFS</option>
                </select>
              </div>
              <div className="domain_new_select" style={{ marginBottom: 0 }}>
                <label htmlFor="host_identifier">호스트</label>
                <select id="host_identifier">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
            </div>
            <div className="domain_new_right">
              <div className="domain_new_select">
                <label>이름</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>설명</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label>코멘트</label>
                <input type="text" />
              </div>
            </div>
          </div>

          <div className="storage_domain_new_second">
            <div>
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}></i>
              <span>사용자 정의 연결 매개 변수</span>
              <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                <div className="domain_new_select">
                  <label htmlFor="nfs_version">NFS 버전</label>
                  <select id="nfs_version" disabled style={{cursor:'no-drop'}}>
                    <option value="host02_ititinfo_com" >자동 협상(기본)</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">재전송(#)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="data_hub">추가 마운트 옵션</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div>
              <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}></i>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                    <option value="linux">V5</option>
                  </select>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                </div>

              </div>
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*볼륨(새로만들기)팝업 */}
      <Modal
        isOpen={activePopup === 'newVolume'}
        onRequestClose={closePopup}
        contentLabel="새 볼륨"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_volume_new_popup">
          <div className="network_popup_header">
            <h1>새 볼륨</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="volume_first_content">
            <div className="domain_new_select">
              <label htmlFor="data_center_selector">데이터 센터</label>
              <select id="data_center_selector">
                <option value="linux">host02.ititinfo.com</option>
              </select>
            </div>
            <div className="domain_new_select" style={{ marginBottom: 0 }}>
              <label htmlFor="volume_cluster_selector">볼륨 클러스터</label>
              <select id="volume_cluster_selector">
                <option value="linux">host02.ititinfo.com</option>
              </select>
            </div>
          </div>
          <div className="volume_second_content">
            <div className="domain_new_select">
              <label>볼륨 클러스터</label>
              <input type="text" />
            </div>
            <div className="domain_new_select">
              <label htmlFor="vol_cluster_dropdown">볼륨 클러스터</label>
              <select id="vol_cluster_dropdown">
                <option value="linux">host02.ititinfo.com</option>
              </select>
            </div>
            <div className="domain_new_select">
              <label htmlFor="data_hub">볼륨 클러스터</label>
              <input type="text" value="3" disabled />
            </div>
            <div className="domain_new_select">
              <label>전송 유형</label>
              <div className="volume_checkboxs">
                <div className="volume_checkbox" style={{ marginRight: '3rem' }}>
                  <input type="checkbox" />
                  <label htmlFor="photo_separation">TCP</label>
                </div>
                <div className="volume_checkbox">
                  <input type="checkbox" />
                  <label htmlFor="photo_separation">RDMA</label>
                </div>
              </div>
            </div>
            <div className="domain_new_select">
              <label>브릭</label>
              <button>브릭 추가</button>
            </div>
          </div>

          <div className="volume_third_content" style={{ paddingTop: 0 }}>
            <h2>접근 프로토콜</h2>
            <div className="volume_checkbox">
              <input type="checkbox" />
              <label htmlFor="photo_separation">RDMA</label>
            </div>
            <div className="volume_checkbox">
              <input type="checkbox" />
              <label htmlFor="photo_separation">RDMA</label>
            </div>
            <div className="volume_checkbox">
              <input type="checkbox" />
              <label htmlFor="photo_separation">RDMA</label>
            </div>
            <div className="domain_new_select">
              <label htmlFor="data_hub">액세스 허용할 호스트</label>
              <input type="text" />
            </div>
            <div className="volume_checkbox">
              <input type="checkbox" />
              <label htmlFor="photo_separation">RDMA</label>
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>

      {/*볼륨(스냅샷)팝업 */}
      <Modal
        isOpen={activePopup === 'volumeSnap'}
        onRequestClose={closePopup}
        contentLabel="스냅샷"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_volume_snap_popup">
          <div className="network_popup_header">
            <h1>볼륨 스냅샷 - 클러스터 옵션</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>

          <div className="volume_snap_first_content">
            <div className="domain_new_select">
              <label htmlFor="vol_cluster_dropdown">볼륨 클러스터</label>
              <select id="vol_cluster_dropdown">
                <option value="linux">host02.ititinfo.com</option>
              </select>
            </div>

            <h2>스냅샷 옵션</h2>
            <div className="volume_snap_table">
              <table>
                <thead>
                  <tr>
                    <th>이름</th>
                    <th>설명</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>ㅇ</td>
                    <td>ㅇ</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>업데이트</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
    </div>
    //스토리지 섹션끝

  );
};

export default Storage;

