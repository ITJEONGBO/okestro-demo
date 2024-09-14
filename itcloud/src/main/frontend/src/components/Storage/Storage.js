import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faGlassWhiskey, faCaretUp, faEllipsisV, faSearch, faChevronCircleRight
  , faTimes, faPencil, faArrowUp
} from '@fortawesome/free-solid-svg-icons'
import { adjustFontSize } from '../../UIEvent';
import HeaderButton from '../button/HeaderButton';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import Permission from '../Modal/Permission';
import './css/Storage.css';
import Table from '../table/Table';

Modal.setAppElement('#root'); // React 16 이상에서는 필수

const Storage = () => {
  const navigate = useNavigate();
  // 모달창 옵션에따라 화면변경
  const [storageType, setStorageType] = useState('NFS'); // 기본값은 NFS로 설정

  // 스토리지 유형 변경 핸들러
  const handleStorageTypeChange = (e) => {
    setStorageType(e.target.value); // 선택된 옵션의 값을 상태로 저장
  };

  const [activeLunTab, setActiveLunTab] = useState('target_lun'); 
  const handleLunTabClick = (tab) => {
    setActiveLunTab(tab); 
  };

  // 테이블 컴포넌트
  const data = [
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer' }}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          he_metadata
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          디스크2
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          디스크3
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
  ];

  const handleRowClick = (row, column) => {
    if (column.accessor === 'alias') {  // 'alias' 컬럼만 체크
        navigate(`/storage-disk/${row.alias.props.children}`);
    }
  };

  // 도메인 테이블 컴포넌트 
  const domaindata = [
    {
      status: <FontAwesomeIcon icon={faCaretUp} style={{ color: '#1DED00' }}fixedWidth/>,
      icon: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      domainName: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          ㅇㄻㄹㅇㄻ
        </span>
      ),
      comment: '',
      domainType: '',
      storageType: '',
      format: '',
      dataCenterStatus: '',
      totalSpace: '',
      freeSpace: '',
      reservedSpace: '',
      description: '',
    },
  ];


  // 행 클릭 시 도메인 이름을 이용하여 이동하는 함수
  const handleDomainClick = (row) => {
    navigate(`/storage-domain/${row.domainName.props.children}`);
  };


  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

  // State for active section
  const [activeSection, setActiveSection] = useState('disk');
  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
  const [activeTab, setActiveTab] = useState('img');

  const toggleFooterContent = () => setFooterContentVisibility(!isFooterContentVisible);
  const handleFooterTabClick = (tab) => setSelectedFooterTab(tab);
  const handleSectionClick = (section) => setActiveSection(section);
  const handleTabClick = (tab) => setActiveTab(tab);

  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);
  const [isPopupBoxVisible, setPopupBoxVisibility] = useState(false);
  
  const togglePopupBox = () => setPopupBoxVisibility(!isPopupBoxVisible);
  const handlePopupBoxItemClick = (e) => e.stopPropagation();

  const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
  const toggleDomainHiddenBox = () => {
    setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
  };
  const [isDomainHiddenBox2Visible, setDomainHiddenBox2Visible] = useState(false);
  const toggleDomainHiddenBox2 = () => {
    setDomainHiddenBox2Visible(!isDomainHiddenBox2Visible);
  };
  const [isDomainHiddenBox3Visible, setDomainHiddenBox3Visible] = useState(false);
  const toggleDomainHiddenBox3 = () => {
    setDomainHiddenBox3Visible(!isDomainHiddenBox3Visible);
  };
  const [isDomainHiddenBox4Visible, setDomainHiddenBox4Visible] = useState(false);
  const toggleDomainHiddenBox4 = () => {
    setDomainHiddenBox4Visible(!isDomainHiddenBox4Visible);
  };
  const [isDomainHiddenBox5Visible, setDomainHiddenBox5Visible] = useState(false);
  const toggleDomainHiddenBox5 = () => {
    setDomainHiddenBox5Visible(!isDomainHiddenBox5Visible);
  };
  const [isDomainHiddenBox6Visible, setDomainHiddenBox6Visible] = useState(false);
  const toggleDomainHiddenBox6 = () => {
    setDomainHiddenBox6Visible(!isDomainHiddenBox6Visible);
  };

  const sectionHeaderButtons = [
    { id: 'new_btn', label: '편집', icon: faPencil, onClick: () => {} },
    { id: 'edit_btn', label: '삭제', icon: faArrowUp, onClick: () => {} },
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
    <div id="storage_section">
      <div>
        <HeaderButton
          title="스토리지 도메인"
          subtitle=""
          buttons={sectionHeaderButtons}
          popupItems={sectionHeaderPopupItems}
        />
        
        <div className="content_outer">

          <div className="empty_nav_outer">
              <>
                <div className="content_header_right">
                  <button id="new_domain_btn" onClick={() => openPopup('newDomain')}>새로운 도메인</button>
                  <button id="get_domain_btn" onClick={() => openPopup('getDomain')}>도메인 가져오기</button>
                  <button id="administer_domain_btn" onClick={() => openPopup('manageDomain')}>도메인 관리</button>
                  <button>삭제</button>
                  <button>Connections</button>
                  <button className="content_header_popup_btn">
                    <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                    <div className="content_header_popup" style={{ display: 'none' }}>
                      <div>활성</div>
                      <div>비활성화</div>
                      <div>이동</div>
                      <div>LUN 새로고침</div>
                    </div>
                  </button>
                </div>

                {/* Table 컴포넌트를 이용하여 테이블을 생성합니다. */}
                <TableOuter
                  columns={TableColumnsInfo.STORAGE_DOMAINS} 
                  data={domaindata} 
                  onRowClick={handleDomainClick} 
                />
              </>
        

          </div>
        </div>

        <Footer/>
      </div> 


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
          <div className="popup_header">
            <h1>새로운 도메인</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                <select 
                id="storage_option_type"
                value={storageType}
                onChange={handleStorageTypeChange} // 선택된 옵션에 따라 상태 변경
                >
                  <option value="NFS">NFS</option>
                  <option value="POSIX">POSIX 호환 FS</option>
                  <option value="GlusterFS">GlusterFS</option>
                  <option value="iSCSI">iSCSI</option>
                  <option value="fc">파이버 채널</option>
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

          {storageType === 'NFS' && (
          <div className="storage_popup_NFS">
            <div className ="network_form_group">
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
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
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
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
          )}

          {storageType === 'POSIX' && (
          <div className="storage_popup_NFS">
            <div className="network_form_group">
              <label htmlFor="description">경로</label>
              <input type="text" id="description" placeholder="예: /path/to/my/data" />
            </div>
            <div className="network_form_group">
              <label htmlFor="description">VFS 유형</label>
              <input type="text" id="description" />
            </div>
            <div className="network_form_group">
              <label htmlFor="description">마운트 옵션</label>
              <input type="text" id="description" />
            </div>


            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn3" onClick={toggleDomainHiddenBox3}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box3" style={{ display: isDomainHiddenBox3Visible ? 'block' : 'none' }}>
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
          )}

          {storageType === 'GlusterFS' && (
          <div className="storage_popup_NFS">
            <div>
                <div className='text-red-600'>데이터 무결성을 위해 서버가 쿼럼 (클라이언트 및 서버 쿼럼 모두)으로 설정되어 있는지 확인합니다</div>
                <div className='input_checkbox'>
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
            </div>
            <div className="network_form_group">
              <label htmlFor="description">경로</label>
              <input type="text" id="description" placeholder="예: myserver.mydomain.com:/myvolumename" />
            </div>
            <div className="network_form_group">
              <label htmlFor="description">VFS 유형</label>
              <input type="text" id="description" />
            </div>
            <div className="network_form_group">
              <label htmlFor="description">마운트 옵션</label>
              <input type="text" id="description" />
            </div>


            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn3" onClick={toggleDomainHiddenBox3}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box3" style={{ display: isDomainHiddenBox3Visible ? 'block' : 'none' }}>
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
        )}

          {storageType === 'iSCSI' && (
          <div className="storage_popup_NFS">
            <div className='target_btns'> 
              <button 
                className={`target_lun ${activeLunTab === 'target_lun' ? 'active' : ''}`}
                onClick={() => handleLunTabClick('target_lun')}
              >
                대상 - LUN
              </button>
              <button 
                className={`lun_target ${activeLunTab === 'lun_target' ? 'active' : ''}`}
                onClick={() => handleLunTabClick('lun_target')}
              > 
                LUN - 대상
              </button>
            </div>


          
            {activeLunTab === 'target_lun' &&(
              <div className='target_lun_outer'>
                <div className="search_target_outer">
                  <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn4" onClick={toggleDomainHiddenBox4}fixedWidth/>
                  <span>대상 검색</span>
                  <div id="domain_hidden_box4" style={{ display: isDomainHiddenBox4Visible ? 'block' : 'none' }}>
                    <div className="search_target ">

                      <div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                      </div>

                      <div>
                        <div className='input_checkbox'>
                          <input type="checkbox" id="reset_after_deletion"/>
                          <label htmlFor="reset_after_deletion">사용자 인증 :</label>
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                      </div>

                      
                    </div>
                    <button>검색</button>
                  </div>
                </div>
              

                <div>
                  <button className='all_login'>전체 로그인</button>
                  <div className='section_table_outer'>
                    <Table
                    columns={TableColumnsInfo.CLUSTERS_ALT} 
                    data={data} 
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                    />
                  </div>
                </div>
              </div>
            )}      

            {activeLunTab === 'lun_target' && (
              <div className='lun_target_outer'>
                 <div className='section_table_outer'>
                    <Table
                    columns={TableColumnsInfo.CLUSTERS_ALT} 
                    data={data} 
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                    />
                </div>
              </div>
            )}
            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
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
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">삭제 후 폐기</label>
                </div>
              </div>
            </div>

          </div>
          )}

          {storageType === 'fc' && (
          <div className="storage_popup_NFS">
            <div className='section_table_outer'>
                  <Table
                  columns={TableColumnsInfo.CLUSTERS_ALT} 
                  data={data} 
                  onRowClick={handleRowClick}
                  shouldHighlight1stCol={true}
                  />
            </div>
            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
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
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">삭제 후 폐기</label>
                </div>
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
          <div className="popup_header">
            <h1>사전 구성된 도메인 가져오기</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                  <option value="linux">POSIX 호환 FS</option>
                  <option value="linux">GlusterFS</option>
                  <option value="linux">iSCSI</option>
                  <option value="linux">파이버 채널</option>
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

          <div className="storage_popup_NFS">
            <div>
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
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
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn6" onClick={toggleDomainHiddenBox6}fixedWidth/>
              <span>고급 매개 변수</span>
                <div id="domain_hidden_box6" style={{ display: isDomainHiddenBox6Visible ? 'block' : 'none' }}>
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
          <div className>
            ㅇㅇ
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
          <div className="popup_header">
            <h1>도메인 관리</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                  <option value="linux">POSIX 호환 FS</option>
                  <option value="linux">GlusterFS</option>
                  <option value="linux">iSCSI</option>
                  <option value="linux">파이버 채널</option>
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

          <div className="storage_popup_NFS">
            <div>
              <label htmlFor="data_hub">내보내기 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
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
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
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


      {/* 모달 컴포넌트 */}
      <Permission isOpen={activePopup === 'permission'} onRequestClose={closePopup} />
    </div>
  );
};

export default Storage;
