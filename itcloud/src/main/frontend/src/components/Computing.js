import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';

import HeaderButton from './button/HeaderButton';
import NavButton from './navigation/NavButton';
import ComputingDetail from '../detail/ComputingDetail';

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



    {/*네트워크 인터페이스(새로만들기) */}
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
                <button  onClick={closeModal}><i className="fa fa-times"></i></button>
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
                <button  onClick={closeModal}>취소</button>
            </div>
      </Modal>

    </div>
  );
};

// 풀
const DiskSection = () => {
  const [isNewDiskModalOpen, setIsNewDiskModalOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('img');

  const handleTabClick = (tab) => setActiveTab(tab);

  // 새로 만들기 팝업 열기/닫기 핸들러
  const openNewDiskModal = () => setIsNewDiskModalOpen(true);
  const closeNewDiskModal = () => setIsNewDiskModalOpen(false);



  return (
      <div id="disk_outer">
          <div id="disk_content">
              <div className="content_header_right">
                  <button id="disk_popup_new" onClick={openNewDiskModal}>새로 만들기</button>
                  <button>편집</button>
                  <button>삭제</button>
                 
              </div>
              <div className="disk_content_header">
                  <span>디스크 유형:</span>
                  <button>모두</button>
                  <button>이미지</button>
                  <button>직접 LUN</button>
                  <button>관리되는 블록</button>
              </div>
              <table>
                  <thead>
                      <tr>
                          <th>이름</th>
                          <th>코멘트</th>
                          <th>할당된 가상 머신</th>
                          <th>실행중인 가상머신</th>
                          <th>유형</th>
                          <th>설명</th>
                        
                      </tr>
                  </thead>
                  <tbody>
                      
                          <div>표시할 항목x</div>
                      
                  </tbody>
              </table>
          </div>

          {/* 풀(새로 만들기) 팝업 */}
          <Modal
              isOpen={isNewDiskModalOpen}
              onRequestClose={closeNewDiskModal}
              contentLabel="새로 만들기"
              className="disk_popup"
              overlayClassName="disk_popup_outer"
              shouldCloseOnOverlayClick={false}
          >
              <div className="network_popup_header">
                  <h1>새 가상 디스크</h1>
                  <button onClick={closeNewDiskModal}><i className="fa fa-times"></i></button>
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
              <div className="disk_new_img" style={{ display: activeTab === 'img' ? 'block' : 'none' }}>
                  <div className="disk_new_img_left">
                      <div className="img_input_box">
                          <span>크기(GIB)</span>
                          <input type="text"/>
                      </div>
                      <div className="img_input_box">
                          <span>별칭</span>
                          <input type="text"/>
                      </div>
                      <div className="img_input_box">
                          <span>설명</span>
                          <input type="text"/>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="os">운영 시스템</label>
                          <select id="os">
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="storage_domain">스토리지 도메인</label>
                          <select id="storage_domain">
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="allocation_policy">할당 정책</label>
                          <select id="allocation_policy">
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="disk_profile">디스크 프로파일</label>
                          <select id="disk_profile">
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                  </div>
                  <div className="disk_new_img_right">
                      <div>
                          <input type="checkbox" className="disk_activation" defaultChecked/>
                          <label htmlFor="disk_activation">디스크 활성화</label>
                      </div>
                      <div>
                          <input type="checkbox" id="reset_after_deletion"/>
                          <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                      </div>
                      <div>
                          <input type="checkbox" className="bootable" disabled/>
                          <label htmlFor="bootable" style={{ color: 'gray' }}>부팅 가능</label>
                      </div>
                      <div>
                          <input type="checkbox" className="shareable"/>
                          <label htmlFor="shareable">공유 가능</label>
                      </div>
                      <div>
                          <input type="checkbox" className="read_only"/>
                          <label htmlFor="read_only">읽기전용</label>
                      </div>
                      <div>
                          <input type="checkbox" id="cancellable"/>
                          <label htmlFor="cancellable">취소 활성화</label>
                      </div>
                      <div>
                          <input type="checkbox" id="incremental_backup" defaultChecked/>
                          <label htmlFor="incremental_backup">중복 백업 사용</label>
                      </div>
                  </div>
              </div>

              <div id="directlun_outer" style={{ display: activeTab === 'directlun' ? 'block' : 'none' }}>
                  {/* 직접LUN 내용 */}
                  <div>
                      <div id="disk_managed_block_left">
                          <div className="img_input_box">
                              <span>별칭</span>
                              <input type="text" defaultValue="on20-ap01_Disk1"/>
                          </div>
                          <div className="img_input_box">
                              <span>설명</span>
                              <input type="text"/>
                          </div>
                          <div className="img_select_box">
                              <label htmlFor="interface">인터페이스</label>
                              <select id="interface">
                                  <option value="linux">Linux</option>
                              </select>
                          </div>
                          <div className="img_select_box">
                              <label htmlFor="host">호스트</label>
                              <select id="host">
                                  <option value="linux">Linux</option>
                              </select>
                          </div>
                          <div className="img_select_box">
                              <label htmlFor="storage_type">스토리지타입</label>
                              <select id="storage_type">
                                  <option value="linux">Linux</option>
                              </select>
                          </div>
                      </div>
                      <div id="disk_managed_block_right">
                          <div>
                              <input type="checkbox" className="disk_activation" defaultChecked/>
                              <label htmlFor="disk_activation">디스크 활성화</label>
                          </div>
                          <div>
                              <input type="checkbox" className="bootable" disabled/>
                              <label htmlFor="bootable">부팅 가능</label>
                          </div>
                          <div>
                              <input type="checkbox" className="shareable"/>
                              <label htmlFor="shareable">공유 가능</label>
                          </div>
                          <div>
                              <input type="checkbox" className="read_only"/>
                              <label htmlFor="read_only">읽기전용</label>
                          </div>
                          <div>
                              <input type="checkbox" className="read_only" disabled/>
                              <label htmlFor="read_only">취소 활성화</label>
                          </div>
                          <div>
                              <input type="checkbox" className="read_only"/>
                              <label htmlFor="read_only">SCSI 통과 활성화</label>
                          </div>
                          <div>
                              <input type="checkbox" className="read_only" disabled/>
                              <label htmlFor="read_only">권한 부여</label>
                          </div>
                          <div>
                              <input type="checkbox" className="read_only" disabled/>
                              <label htmlFor="read_only">SCSI 혜택사용</label>
                          </div>
                      </div>
                  </div>
                  <div className="target_search">
                      <div className="target_buttons">
                          <div>대상 &gt; LUN</div>
                          <div>LUN &gt; 대상</div>
                      </div>
                      <div className="target_info">
                          <div>
                              <div style={{ marginBottom: '0.2rem' }}>
                                  <span>주소</span>
                                  <input type="text"/>
                              </div>
                              <div>
                                  <span>포트</span>
                                  <input type="text"/>
                              </div>
                          </div>
                          <div>
                              <div>
                                  <input type="checkbox" className="disk_activation"/>
                                  <label htmlFor="disk_activation">사용자 인증</label>
                              </div>
                              <div>
                                  <div className="target_input_text" style={{ marginBottom: '0.1rem' }}>
                                      <span>CHWP사용자 이름</span>
                                      <input type="text" disabled/>
                                  </div>
                                  <div className="target_input_text">
                                      <span>CHAP 암호</span>
                                      <input type="text" disabled/>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div className="disk_search_btn">
                          <div>검색</div>
                          <div>전체 로그인</div>
                      </div>
                      <div className="target_table">
                          <table>
                              <thead>
                                  <tr>
                                      <th>대상 이름</th>
                                      <th>주소</th>
                                      <th>포트</th>
                                  </tr>
                              </thead>
                              <tbody>
                                  <tr>
                                      <td>
                                          <label htmlFor="diskActivation">디스크 활성화</label>
                                      </td>
                                      <td>
                                          <label htmlFor="diskActivation">디스크 활성화</label>
                                      </td>
                                      <td>
                                          <label htmlFor="diskActivation">디스크 활성화</label>
                                      </td>
                                  </tr>
                              </tbody>
                          </table>
                      </div>
                  </div>
              </div>

              <div id="managed_block_outer" style={{ display: activeTab === 'managed' ? 'block' : 'none' }}>
                  {/* 관리되는 블록 내용 */}
                  <div id="disk_managed_block_left">
                      <div className="img_input_box">
                          <span>크기(GIB)</span>
                          <input type="text" disabled/>
                      </div>
                      <div className="img_input_box">
                          <span>별칭</span>
                          <input type="text" defaultValue="on20-ap01_Disk1" disabled/>
                      </div>
                      <div className="img_input_box">
                          <span>설명</span>
                          <input type="text" disabled/>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="allocation_policy">할당 정책</label>
                          <select id="allocation_policy" disabled>
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                      <div className="img_select_box">
                          <label htmlFor="disk_profile">디스크 프로파일</label>
                          <select id="disk_profile" disabled>
                              <option value="linux">Linux</option>
                          </select>
                      </div>
                      <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
                  </div>
                  <div id="disk_managed_block_right">
                      <div>
                          <input type="checkbox" className="disk_activation" disabled/>
                          <label htmlFor="disk_activation">디스크 활성화</label>
                      </div>
                      <div>
                          <input type="checkbox" className="bootable" disabled/>
                          <label htmlFor="bootable">부팅 가능</label>
                      </div>
                      <div>
                          <input type="checkbox" className="shareable" disabled/>
                          <label htmlFor="shareable">공유 가능</label>
                      </div>
                      <div>
                          <input type="checkbox" className="read_only" disabled/>
                          <label htmlFor="read_only">읽기전용</label>
                      </div>
                  </div>
              </div>
              <div className="edit_footer">
                  <button style={{ display: 'none' }}></button>
                  <button>OK</button>
                  <button onClick={closeNewDiskModal}>취소</button>
              </div>
          </Modal>

         
      </div>
  );
};


// 데이터 센터
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

// 클러스터
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


// 가상머신
const Computing = () => {
  const { section } = useParams();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState(section || 'general');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showDetail, setShowDetail] = useState(false);  // 추가된 상태

  useEffect(() => {
    navigate(`/computing/${activeSection}`);
  }, [activeSection, navigate, showDetail]);

  const handleSectionClick = (section) => {
    setActiveSection(section);
  };

  useEffect(() => {
    const defaultElement = document.getElementById('common_outer_btn');
    if (defaultElement) {
      defaultElement.style.backgroundColor = '#EDEDED';
      defaultElement.style.color = '#1eb8ff';
      defaultElement.style.borderBottom = '1px solid blue';
    }
  }, []);


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

  

    //   // 테이블 데이터
    //   const data = [
    //     { name: 'ovirtmgmt', description: 'Management Network' },
    //     { name: 'example1', description: 'Example Description 1' },
    //     { name: 'example2', description: 'Example Description 2' },
    //     // 필요한 만큼 데이터 추가
    // ];

    // const columns = [
    //     { header: '이름', accessor: 'name', clickable: true },
    //     { header: '설명', accessor: 'description', clickable: false },
    // ];
      const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick: () => {} },
    { id: 'run_btn', label: '실행', icon: 'fa-play', onClick: () => {} },
    { id: 'pause_btn', label: '일시중지', icon: 'fa-pause', onClick: () => {} },
    { id: 'stop_btn', label: '종료', icon: 'fa-stop', onClick: () => {} },
    { id: 'reboot_btn', label: '재부팅', icon: 'fa-repeat', onClick: () => {} },
    { id: 'console_btn', label: '콘솔', icon: 'fa-desktop', onClick: () => {} },
    { id: 'snapshot_btn', label: '스냅샷 생성', onClick: () => {} },
    { id: 'migration_btn', label: '마이그레이션', onClick: openModal },
  ];
    const sectionHeaderPopupItems = [
      '가져오기',
      '가상 머신 복제',
      '삭제',
      '마이그레이션 취소',
      '변환 취소',
      '템플릿 생성',
      '도메인으로 내보내기',
      'Export to Data Domain',
      'OVA로 내보내기',
    ];
    const navSections = [
      { id: 'general', label: '가상머신' },
      { id: 'network', label: '템플릿' },
      { id: 'disk', label: '풀' },
      { id: 'snapshot', label: '데이터 센터' },
      { id: 'application', label: '클러스터' },
    ];
    if (showDetail) {
      return <ComputingDetail />;  // detail 표시
    }
  return (
    <div id="section">
       <HeaderButton
        title="가상머신"
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

        {activeSection === 'general' && (
          <div className="tables">
            <div className="storage_domain_content">
            <table>
                  <thead>
                      <tr>
                          <th></th>
                          <th>변경</th>
                          <th><i className="fa fa-glass"></i></th>
                          <th><i className="fa fa-glass"></i></th>
                          <th><i className="fa fa-glass"></i></th>
                          <th>가상 크기</th>
                          <th>연결 대상</th>
                          <th>인터페이스</th>
                          <th>논리적 이름</th>
                          <th>상태</th>
                          <th>유형</th>
                          <th>설명</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                          <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                          <td onClick={handleRowClick}>on20-ap01</td>
                          <td><i className="fa fa-glass"></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td>on20-ap01</td>
                          <td>VirtIO-SCSI</td>
                          <td>/dev/sda</td>
                          <td>OK</td>
                          <td>이미지</td>
                          <td></td>
                          <td></td>
                      </tr>
                  </tbody>
              </table>
            </div>
          </div>
        )}
        {activeSection === 'network' && <NetworkSection />}
        {activeSection === 'disk' && <DiskSection />}
        {activeSection === 'snapshot' && <SnapshotSection />}
        {activeSection === 'application' && <ApplicationSection />}
      
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
      
{/*header팝업창----------------------- */}
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

    

    </div> //section끝


  );
};

export default Computing;