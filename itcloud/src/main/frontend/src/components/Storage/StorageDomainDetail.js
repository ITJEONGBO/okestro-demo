import React, { useState,useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Modal from 'react-modal';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useNavigate } from 'react-router-dom';
import Permission from '../Modal/Permission';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faExclamation, faPlusCircle, faMinusCircle, faChevronLeft, faCheck
  , faUser, faTimes, faChevronCircleRight, faDesktop, faAngleDown,
  faGlassWhiskey,
  faExclamationTriangle,
  faCloud
} from '@fortawesome/free-solid-svg-icons'
import './css/StorageDomainDetail.css';
import TableOuter from '../table/TableOuter';
import Footer from '../footer/Footer';
import Path from '../Header/Path';

function StorageDomain({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const { name } = useParams();
  const navigate = useNavigate();
    //클릭한 이름 받아오기
    const handlePermissionFilterClick = (filter) => {
      setActivePermissionFilter(filter);
    };
    const [storageType, setStorageType] = useState('NFS'); // 기본값은 NFS로 설정
    // 스토리지 유형 변경 핸들러
  const handleStorageTypeChange = (e) => {
    setStorageType(e.target.value); // 선택된 옵션의 값을 상태로 저장
  };
    const [activePopup, setActivePopup] = useState(null); // modal
    const [activePermissionFilter, setActivePermissionFilter] = useState('all');
    const openModal = (popupType) => setActivePopup(popupType);
    const closeModal = () => setActivePopup(null);
    const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
    const toggleDomainHiddenBox = () => {
      setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
    };
    const [isDomainHiddenBox2Visible, setDomainHiddenBox2Visible] = useState(false);
    const toggleDomainHiddenBox2 = () => {
      setDomainHiddenBox2Visible(!isDomainHiddenBox2Visible);
    };

   
    const [isDomainHiddenBox4Visible, setDomainHiddenBox4Visible] = useState(false);
    const toggleDomainHiddenBox4 = () => {
      setDomainHiddenBox4Visible(!isDomainHiddenBox4Visible);
    };
    const [isDomainHiddenBox5Visible, setDomainHiddenBox5Visible] = useState(false);
    const toggleDomainHiddenBox5 = () => {
      setDomainHiddenBox5Visible(!isDomainHiddenBox5Visible);
    };
    const handleRowClick = (row, column) => {
      if (column.accessor === 'domainName') {
        navigate(`/storage-domain/${row.domainName.props.children}`);  
      }
    };
    const [activeLunTab, setActiveLunTab] = useState('target_lun'); 
  const handleLunTabClick = (tab) => {
    setActiveLunTab(tab); 
  };
    
  // 테이블컴포넌트
  // 데이터센터
  const dataCenterData = [
    {
      icon: <FontAwesomeIcon icon={faExclamation} fixedWidth/>,
      name: name,
      domainStatus: '활성화',
    },
  ];
  //가상머신(편집해야됨)
  //템플릿
  const templateColumns = [
    { header: '별칭', accessor: 'alias', clickable: false },
    { header: '디스크', accessor: 'disk', clickable: false },
    { header: '가상 크기', accessor: 'virtualSize', clickable: false },
    { header: '실제 크기', accessor: 'actualSize', clickable: false },
    { header: '생성 일자', accessor: 'creationDate', clickable: false },
  ];

  const templateData = [
    {
      alias: (
        <>
          <FontAwesomeIcon icon={faPlusCircle} fixedWidth/> test02
        </>
      ),
      disk: '',
      virtualSize: '1 GIB',
      actualSize: '5 GIB',
      creationDate: '2024.1.19 AM9:21:57',
    },
  ];

  // 디스크
  const diskcolumns = [
    { header: '별칭', accessor: 'alias' },
    { header: '', accessor: 'leftIcon1', clickable: true },
    { header: '', accessor: 'leftIcon2', clickable: true },
    { header: '가상 크기', accessor: 'virtualSize' },
    { header: '실제 크기', accessor: 'actualSize' },
    { header: '할당 정책', accessor: 'allocationPolicy' },
    { header: '스토리지 도메인', accessor: 'storageDomain' },
    { header: '생성 일자', accessor: 'createdDate' },
    { header: '최근 업데이트', accessor: 'lastUpdated' },
    { header: '', accessor: 'rightIcon', clickable: true },
    { header: '연결 대상', accessor: 'connectedTo' },
    { header: '상태', accessor: 'status' },
    { header: '유형', accessor: 'type' },
    { header: '설명', accessor: 'description' },
  ];
  const diskdata = [
    {
      alias: 'aa',
      leftIcon1: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,
      leftIcon2: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,
      virtualSize: '<1 GiB',
      actualSize: '<1 GiB',
      allocationPolicy: '씬 프로비저닝',
      storageDomain: 'hosted_storage',
      createdDate: '2024. 4. 26. PM 3:19:39',
      lastUpdated: '2024. 4. 26. PM 3:19:45',
      rightIcon: <FontAwesomeIcon icon={faChevronLeft} fixedWidth/>,
      connectedTo: '',
      status: 'OK',
      type: '이미지',
      description: 'testa',
    },
  ];
  //
  // 디스크 스냅샷
  const snapshotColumns = [
    { header: '크기', accessor: 'size', clickable: false },
    { header: '생성 일자', accessor: 'creationDate', clickable: false },
    { header: '스냅샷 생성일', accessor: 'snapshotCreationDate', clickable: false },
    { header: '디스크 별칭', accessor: 'diskAlias', clickable: false },
    { header: '스냅샷 설명', accessor: 'snapshotDescription', clickable: false },
    { header: '연결 대상', accessor: 'target', clickable: false },
    { header: '상태', accessor: 'status', clickable: false },
    { header: '디스크 스냅샷 ID', accessor: 'diskSnapshotId', clickable: false },
  ];

  const snapshotData = [];

  // 이벤트
  const eventData = [
    {
      statusIcon: <FontAwesomeIcon icon={faCheck} style={{ color: 'green' }}fixedWidth/>,
      time: '2024. 7. 29. PM 3:31:41',
      message: 'Image Download with disk he_metadata was cancelled.',
      correlationId: '2568d791:c08...',
      source: 'oVirt',
      customEventId: '',
    },
  ];



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
  //
  const [activeTab, setActiveTab] = useState('general');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  // 옵션박스 열고닫기
  const [isUploadOptionBoxVisible, setUploadOptionBoxVisible] = useState(false);
  const toggleUploadOptionBox = () => {
    setUploadOptionBoxVisible(!isUploadOptionBoxVisible);
  };
  //headerbutton 컴포넌트
  const buttons = [
    { id: 'manage_domain_btn', label: '도메인 관리', onClick: () => openModal('manageDomain') },
    { id: 'delete_btn', label: '삭제', onClick: () => openModal('delete') },
    { id: 'connections_btn', label: 'Connections', onClick: () => console.log('Connections button clicked') },
];


const popupItems = [
  { label: <div className="disabled">OVF 업데이트</div>, onClick: () => console.log('OVF 업데이트 clicked') },
  { label: <div className="disabled">파괴</div>, onClick: () => console.log('파괴 clicked') },
  { label: <div className="disabled">디스크 검사</div>, onClick: () => console.log('디스크 검사 clicked') },
  { label: <div className="disabled">마스터 스토리지 도메인으로 선택</div>, onClick: () => console.log('마스터 스토리지 도메인으로 선택 clicked') },
];

  // NAV컴포넌트
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'datacenter', label: '데이터 센터' },
    { id: 'machine', label: '가상머신' },
    { id: 'disk', label: '디스크' },
    { id: 'disk_snapshot', label: '디스크 스냅샷' },
    { id: 'template', label: '템플릿' },
    { id: 'event', label: '이벤트' },

  ];
  const pathData = [
    '#',

    sections.find(section => section.id === activeTab)?.label,
 
].filter(Boolean);
  // 바탕클릭하면 옵션박스 닫기
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        isUploadOptionBoxVisible &&
        !event.target.closest('.upload_option_box') &&
        !event.target.closest('.upload_option_boxbtn')
      ) {
        setUploadOptionBoxVisible(false);
      }
    };
    
    //
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isUploadOptionBoxVisible]);
  return (
    <div className="content_detail_section">
      <HeaderButton
      titleIcon={faCloud}
      title="스토리지도메인 > "
      subtitle={name}
      buttons={buttons}
      popupItems={popupItems}
    />

      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />
        <div className="host_btn_outer">
        <Path pathElements={pathData}/>
        {activeTab === 'general' && (
          <div className="tables">
            <div className="table_storage_domain_detail">
            <table className="table">
              <tbody>
                <tr>
                  <th>ID:</th>
                  <td>{name}</td>
                </tr>
                <tr>
                  <th>크기:</th>
                  <td>99 GiB</td>
                </tr>
                <tr>
                  <th>사용 가능:</th>
                  <td>83 GiB</td>
                </tr>
                <tr>
                  <th>사용됨:</th>
                  <td>16 GiB</td>
                </tr>
                <tr>
                  <th>할당됨:</th>
                  <td>92 GiB</td>
                </tr>
                <tr>
                  <th>오버 할당 비율:</th>
                  <td>89%</td>
                </tr>
                <tr>
                  <th>이미지:</th>
                  <td>6</td>
                </tr>
                <tr>
                  <th>경로:</th>
                  <td>192.168.0.73:/ovirt.ititinfo.com_engine</td>
                </tr>
                <tr>
                  <th>NFS 버전:</th>
                  <td>자동</td>
                </tr>
                <tr>
                  <th>디스크 공간 부족 경고 표시:</th>
                  <td>10% (9 GiB)</td>
                </tr>
                <tr>
                  <th>심각히 부족한 디스크 공간의 동작 차단:</th>
                  <td>5 GiB</td>
                </tr>
              </tbody>
            </table>

            </div> 
          </div> 
        )}

        {activeTab === 'datacenter' && (
          <>
           
              <div className="header_right_btns">
                <button className='disabled'>연결</button>
                <button className='disabled'>분리</button>
                <button className='disabled'>활성</button>
                <button onClick={() => openModal('maintenance')}>유지보수</button>
              </div>
              
              <TableOuter 
                columns={TableColumnsInfo.STORAGE_DOMAIN_FROM_DATACENTER}
                data={dataCenterData} 
                onRowClick={() => console.log('Row clicked')} 
              />
          </>
        )}

        {/*밑에딸린 박스 편집 */}
        {activeTab === 'machine' && (
          <>
          <div className="host_empty_outer">
            {/* TODO: TableOuter화 */}
            <div className="section_table_outer">
                <table>
                  <thead>
                    <tr>
                      <th>별칭</th>
                      <th>디스크</th>
                      <th>템플릿</th>
                      <th>가상 크기</th>
                      <th>실제 크기</th>
                      <th>생성 일자</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>
                        <FontAwesomeIcon icon={faPlusCircle} fixedWidth/>
                        <i class={faMinusCircle} style={{display:'none'}}fixedWidth/>
                        <i class={faDesktop} fixedWidth/>
                        test02
                      </td>
                      <td>1</td>
                      <td>Blank</td>
                      <td>1 GIB</td>
                      <td>5 GIB</td>
                      <td>2024.1.19 AM9:21:57</td>
                    </tr>
                  </tbody>
                  {/*+버튼누르면 밑에 딸려있는것 */}
                  <tbody className='detail_machine_second'>
                    <tr>
                      <td>
                        <FontAwesomeIcon icon={faPlusCircle} fixedWidth/>
                        <i class={faMinusCircle} style={{display:'none'}}fixedWidth/>
                        <i class={faDesktop} fixedWidth/>
                        test02
                      </td>
                      <td>1</td>
                      <td>Blank</td>
                      <td>1 GIB</td>
                      <td>5 GIB</td>
                      <td>2024.1.19 AM9:21:57</td>
                    </tr>
                  </tbody>
                  {/*+버튼누르면 밑에 딸려있는것 마지막 */}
                  <tbody className='detail_machine_last'>
                    <tr>
                      <td>
                        <FontAwesomeIcon icon={faPlusCircle} fixedWidth/>
                        <i class={faMinusCircle} style={{display:'none'}}fixedWidth/>
                        <i class={faDesktop} fixedWidth/>
                        test02
                      </td>
                      <td>1</td>
                      <td>Blank</td>
                      <td>1 GIB</td>
                      <td>5 GIB</td>
                      <td>2024.1.19 AM9:21:57</td>
                    </tr>
                  </tbody>

                </table>
            </div>
          </div>
          </>
        )}

        {activeTab === 'template' && (
          <>
          <div className="host_empty_outer">
            <TableOuter 
              columns={templateColumns}
              data={templateData}
              onRowClick={() => console.log('Row clicked')} 
            />
          </div>
        </>
        )}

        {activeTab === 'disk' && (
        <>
            <div className="header_right_btns">
                <button  onClick={() => openModal('move')}>이동</button>
                <button  onClick={() => openModal('copy')}>복사</button>
                <button  onClick={() => openModal('delete')}>제거</button>
                <button className='upload_option_boxbtn'>업로드 
                  <i class={faAngleDown} onClick={toggleUploadOptionBox}fixedWidth/>
                </button>
                <button>다운로드</button>
                {/*업로드 버튼 옵션박스 */}
                {isUploadOptionBoxVisible &&(
                <div className='upload_option_box'>
                  <div>시작</div>
                  <div>취소</div>
                  <div>일시정지</div>
                  <div>다시시작</div>
                </div>
                )}
            </div>
            
            <TableOuter 
              columns={diskcolumns}
              data={diskdata}
              onRowClick={() => console.log('Row clicked')}
            />
       </>
        )}

        {activeTab === 'disk_snapshot' && (
        <>
            <div className="header_right_btns">
                <button onClick={() => openModal('delete')}>제거</button>
            </div>
            
            <TableOuter 
              columns={snapshotColumns}
              data={snapshotData}
              onRowClick={() => console.log('Row clicked')}
            />
        </>
        )}

        {activeTab === 'event' && (
          <>
        <div className="host_empty_outer">
          <TableOuter 
            columns={TableColumnsInfo.EVENTS}
            data={eventData}
            onRowClick={() => console.log('Row clicked')} 
          />
        </div>
        </>
        )}

        {/* 권한(삭제예정)*/}
        {/* {activeTab === 'permission' && (
            <>
            <div className="header_right_btns">
            <button onClick={() => openModal('permission')}>추가</button>
              <button onClick={() => openModal('delete')}>제거</button>
            </div>
            <div className="host_filter_btns">
              <span>Permission Filters:</span>
              <div>
                <button
                  className={activePermissionFilter === 'all' ? 'active' : ''}
                  onClick={() => handlePermissionFilterClick('all')}
                >
                  All
                </button>
                <button
                  className={activePermissionFilter === 'direct' ? 'active' : ''}
                  onClick={() => handlePermissionFilterClick('direct')}
                >
                  Direct
                </button>
              </div>
            </div>
            <TableOuter
              columns={TableColumnsInfo.PERMISSIONS}
              data={activePermissionFilter === 'all' ? permissionData : []}
              onRowClick={() => console.log('Row clicked')}
            />
          </>
        )} */}



        

      </div>

      </div>
      {/*도메인(도메인 관리)팝업 */}
      <Modal
    isOpen={activePopup === 'manageDomain'}
    onRequestClose={closeModal}
    contentLabel="도메인 관리"
    className="Modal"
    overlayClassName="Overlay"
    shouldCloseOnOverlayClick={false}
      >
        <div className="storage_domain_administer_popup">
          <div className="popup_header">
            <h1>도메인 관리</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                            <option value="data">데이터</option>
                            <option value="iso">ISO</option>
                            <option value="export">내보내기</option>
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
                    <label htmlFor="data_hub">NFS 서버 경로</label>
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
                        {/* <div className="domain_new_select">
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
                        </div> */}
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
                        {/* <div className="domain_new_select">
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
                        </div> */}

                    </div>
                    </div>
                </div>
                )}

                

                {storageType === 'iSCSI' && (
                <div className="storage_popup_NFS">
                    <div className='target_btns flex'> 
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

                        {/* <div className="domain_new_select">
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
                        </div> */}
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
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>

       {/*유지보수 팝업 */}
       <Modal
        isOpen={activePopup === 'maintenance'}
        onRequestClose={closeModal}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="maintenance_popup">
          <div className="popup_header">
            <h1>스토리지 도메인 관리</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='disk_delete_box'>
            <div>
              <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
              <span>다음 스토리지 도메인을 유지 관리 모드로 설정하시겠습니까?</span>
            </div>

          </div>
            
          <div className='hosted_storage'>
              <div>- hosted_storage</div>
              <div className='host_checkbox'>
                <input type="checkbox" id="ignore_ovf_update_failure" name="ignore_ovf_update_failure" />
                <label htmlFor="ignore_ovf_update_failure">OVF 업데이트 실패 무시</label>
              </div>

              <div className='host_textbox'>
                <label htmlFor="reason">이유</label>
                <input type="text" id="user_name" />
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
        </Modal>
        {/*삭제 팝업 */}
        <Modal
        isOpen={activePopup === 'delete'}
        onRequestClose={closeModal}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_delete_popup">
          <div className="popup_header">
            <h1>삭제</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='disk_delete_box'>
            <div>
              <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
              <span>다음 항목을 삭제하시겠습니까?</span>
            </div>
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
        </Modal>
        {/*디스크(이동)팝업 */}
        <Modal
        isOpen={activePopup === 'move'}
        onRequestClose={closeModal}
        contentLabel="디스크 이동"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="disk_move_popup">
          <div className="popup_header">
            <h1>디스크 이동</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className="section_table_outer py-1">
              <table >
        <thead>
          <tr>
            <th>별칭</th>
            <th>가상 크기</th>
            <th>소스</th>
            <th>대상</th>
            <th>디스크 프로파일</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>he_sanlock</td>
            <td>1 GiB</td>
            <td>hosted_storage</td>
            <td>
              <select>
                <option>NFS (499 GiB)</option>
                <option>Option 2</option>
              </select>
            </td>
            <td>
              <select>
                <option>NFS</option>
                <option>Option 2</option>
              </select>
            </td>
          </tr>
        </tbody>
              </table>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
        </Modal>
        {/*디스크(복사)팝업 */}
        <Modal
        isOpen={activePopup === 'copy'}
        onRequestClose={closeModal}
        contentLabel="디스크 복사"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="disk_move_popup">
          <div className="popup_header">
            <h1>디스크 복사</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className="section_table_outer py-1">
              <table >
                <thead>
                  <tr>
                    <th>별칭</th>
                    <th>가상 크기</th>
                    <th>소스</th>
                    <th>대상</th>
                    <th>디스크 프로파일</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>
                      <input type='text' value={'별칭'}/>
                    </td>
                    <td>1 GiB</td>
                    <td>
                      <select>
                        <option>hosted_storage</option>
                     
                      </select>
                    </td>
                    <td>
                      <select>
                        <option>NFS (499 GiB)</option>
                        <option>Option 2</option>
                      </select>
                    </td>
                    <td>
                      <select>
                        <option>NFS</option>
                     
                      </select>
                    </td>
                  </tr>
                </tbody>
              </table>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>

      <Footer/>
     
    </div>
  );
}

export default StorageDomain;
