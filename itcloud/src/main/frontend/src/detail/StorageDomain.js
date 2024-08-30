import React, { useState,useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';
import Table from '../components/table/Table';
import TableColumnsInfo from '../components/table/TableColumnsInfo';
import './css/StorageDomain.css';

function StorageDomain({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const { name } = useParams(); // URL에서 name 파라미터를 가져옵니다.
  // 테이블컴포넌트
  // 데이터센터
  const dataCenterData = [
    {
      icon: <i className="fa fa-exclamation"></i>,
      name: name,
      domainStatus: '활성화',
    },
  ];
  //가상머신(수정해야됨)
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
          <i className="fa fa-plus-circle"></i> test02
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
      leftIcon1: <i className="fa fa-chevron-left"></i>,
      leftIcon2: <i className="fa fa-chevron-left"></i>,
      virtualSize: '<1 GiB',
      actualSize: '<1 GiB',
      allocationPolicy: '씬 프로비저닝',
      storageDomain: 'hosted_storage',
      createdDate: '2024. 4. 26. PM 3:19:39',
      lastUpdated: '2024. 4. 26. PM 3:19:45',
      rightIcon: <i className="fa fa-chevron-left"></i>,
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
      statusIcon: <i className="fa fa-check" style={{ color: 'green' }}></i>,
      time: '2024. 7. 29. PM 3:31:41',
      message: 'Image Download with disk he_metadata was cancelled.',
      correlationId: '2568d791:c08...',
      source: 'oVirt',
      customEventId: '',
    },
  ];

  //권한
  const permissionData = [
    {
      icon: <i className="fa fa-user"></i>,
      user: 'ovirtmgmt',
      authProvider: '',
      namespace: '*',
      role: 'SuperUser',
      createdDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
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
    { id: 'manage_domain_btn', label: '도메인 관리', onClick: () => console.log('Manage Domain button clicked') },
    { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
    { id: 'connections_btn', label: 'Connections', onClick: () => console.log('Connections button clicked') },
  ];

  const popupItems = [
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
  // NAV컴포넌트
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'datacenter', label: '데이터 센터' },
    { id: 'machine', label: '가상머신' },
    { id: 'template', label: '템플릿' },
    { id: 'disk', label: '디스크' },
    { id: 'disk_snapshot', label: '디스크 스냅샷' },
    { id: 'event', label: '이벤트' },
    { id: 'permission', label: '권한' },
  ];
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
        {activeTab === 'general' && (
          <div className="tables">
            <div className="table_container_center">
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
           
              <div className="content_header_right">
                <button>연결</button>
                <button>분리</button>
                <button>활성</button>
                <button>유지보수</button>
              </div>
              
              <div className="section_table_outer">
                <Table columns={TableColumnsInfo.STORAGE_DOMAIN_FROM_DATACENTER} data={dataCenterData} onRowClick={() => console.log('Row clicked')} />
              </div>

          </>
        )}

        {/*밑에딸린 박스 수정 */}
        {activeTab === 'machine' && (
          <>
          <div className="host_empty_outer">
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
                        <i className="fa fa-plus-circle"></i>
                        <i class="fa fa-minus-circle" style={{display:'none'}}></i>
                        <i class="fa fa-desktop"></i>
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
                        <i className="fa fa-plus-circle"></i>
                        <i class="fa fa-minus-circle" style={{display:'none'}}></i>
                        <i class="fa fa-desktop"></i>
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
                        <i className="fa fa-plus-circle"></i>
                        <i class="fa fa-minus-circle" style={{display:'none'}}></i>
                        <i class="fa fa-desktop"></i>
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
            <div className="section_table_outer">
              <Table columns={templateColumns} data={templateData} onRowClick={() => console.log('Row clicked')} />
            </div>
          </div>
        </>
        )}

        {activeTab === 'disk' && (
        <>
            <div className="content_header_right">
                <button>이동</button>
                <button>복사</button>
                <button>제거</button>
                <button className='upload_option_boxbtn'>업로드 
                  <i class="fa fa-angle-down" onClick={toggleUploadOptionBox}></i>
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
            
            <div className="section_table_outer">
              <Table columns={diskcolumns} data={diskdata} onRowClick={() => console.log('Row clicked')}/>
            </div>
       </>
        )}

        {activeTab === 'disk_snapshot' && (
        <>
            <div className="content_header_right">
                <button>제거</button>
            </div>
            
            <div className="section_table_outer">
              <Table columns={snapshotColumns} data={snapshotData} onRowClick={() => console.log('Row clicked')} />
            </div>
        </>
        )}

        {activeTab === 'event' && (
          <>
        <div className="host_empty_outer">
            <div className="section_table_outer">
                <Table columns={TableColumnsInfo.EVENTS} data={eventData} onRowClick={() => console.log('Row clicked')} />
            </div>
        </div>
        </>
        )}

      {activeTab === 'permission' && (
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
                
                <div className="section_table_outer">
                <Table columns={TableColumnsInfo.PERMISSIONS} data={permissionData} onRowClick={() => console.log('Row clicked')} />
                </div>
              </div>
        </>
        )}
      </div>

      </div>
    </div>
  );
}

export default StorageDomain;
