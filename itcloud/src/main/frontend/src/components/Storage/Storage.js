import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import './css/Storage.css';
import Permission from '../Modal/Permission';


Modal.setAppElement('#root'); // React 16 이상에서는 필수

const Storage = () => {
  const navigate = useNavigate();
  
  /* 
    const [storageData, setStorageData] = useState([]);
    useEffect(() => {
        async function fetchData() {
            const res = await ApiManager.findAllStorage();
            setStorageData(res);
        }
        fetchData()
    }, [])
    */



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
      icon2: <i className="fa fa-glass"></i>,
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
      icon2: <i className="fa fa-glass"></i>,
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
      icon2: <i className="fa fa-glass"></i>,
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
      status: <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>,
      icon: <i className="fa fa-glass"></i>,
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
                  {/* Table 컴포넌트를 이용하여 테이블을 생성합니다. */}
                  <Table columns={TableColumnsInfo.STORAGE_DOMAINS} data={domaindata} onRowClick={handleDomainClick} />
                </div>
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


      {/* 모달 컴포넌트 */}
      <Permission isOpen={activePopup === 'permission'} onRequestClose={closePopup} />
    </div>
  );
};

export default Storage;
