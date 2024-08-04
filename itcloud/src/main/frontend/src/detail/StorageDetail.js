import React, { useState,useEffect } from 'react';
import './StorageDetail.css';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';
import { Table } from '../components/table/Table';

function StorageDetail({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  // 테이블컴포넌트
  // 데이터센터
  const dataCenterColumns = [
    { header: '', accessor: 'icon', clickable: false, style: { textAlign: 'center' } },
    { header: '이름', accessor: 'name', clickable: false },
    { header: '데이터 센터 내의 도메인 상태', accessor: 'domainStatus', clickable: false },
  ];

  const dataCenterData = [
    {
      icon: <i className="fa fa-exclamation"></i>,
      name: 'Default',
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
  const eventColumns = [
    { header: '별칭', accessor: 'alias', clickable: false },
    { header: <i className="fa fa-chevron-left"></i>, accessor: 'icon1', clickable: false },
    { header: <i className="fa fa-chevron-left"></i>, accessor: 'icon2', clickable: false },
    { header: '가상 크기', accessor: 'virtualSize', clickable: false },
    { header: '실제 크기', accessor: 'actualSize', clickable: false },
    { header: '할당 정책', accessor: 'allocationPolicy', clickable: false },
    { header: '스토리지 도메인', accessor: 'storageDomain', clickable: false },
    { header: '생성 일자', accessor: 'creationDate', clickable: false },
    { header: '최근 업데이트', accessor: 'lastUpdate', clickable: false },
    { header: '', accessor: 'icon3', clickable: false },
    { header: '연결 대상', accessor: 'connectionTarget', clickable: false },
    { header: '상태', accessor: 'status', clickable: false },
    { header: '유형', accessor: 'type', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
  ];

  const eventData = [
    {
      alias: 'aa',
      icon1: '',
      icon2: '',
      virtualSize: '<1 GiB',
      actualSize: '<1 GiB',
      allocationPolicy: '씬 프로비저닝',
      storageDomain: 'hosted_storage',
      creationDate: '2024. 4. 26. PM 3:19:39',
      lastUpdate: '2024. 4. 26. PM 3:19:45',
      icon3: <i className="fa fa-chevron-left"></i>,
      connectionTarget: '',
      status: '잠김',
      type: '이미지',
      description: 'testa',
    },
  ];
  //권한
  const permissionColumns = [
    { header: '', accessor: 'icon', clickable: false },
    { header: '사용자', accessor: 'user', clickable: false },
    { header: '인증 공급자', accessor: 'authProvider', clickable: false },
    { header: '네임스페이스', accessor: 'namespace', clickable: false },
    { header: '역할', accessor: 'role', clickable: false },
    { header: '생성일', accessor: 'createdDate', clickable: false },
    { header: 'Inherited From', accessor: 'inheritedFrom', clickable: false },
  ];

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
      title="스토리지"
      subtitle="스토리지 도메인"
      additionalText="hosted_storage"
      buttons={buttons}
      popupItems={popupItems}
    />

      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />

        {activeTab === 'general' && (
          <div className="section_content_outer">
            <div className="table_container_left">
              <table className="table">
                <tbody>
                  <tr>
                    <th>ID:</th>
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
                  <tr>
                    <th>템플릿:</th>
                    <td>Blank</td>
                  </tr>
                  <tr>
                    <th>운영 시스템:</th>
                    <td>Linux</td>
                  </tr>
                  <tr>
                    <th>펌웨어/장치의 유형:</th>
                    <td>
                      BIOS의 Q35 칩셋{' '}
                      <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i>
                    </td>
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
          </div>
        )}

        {activeTab === 'datacenter' && (
          <div id="detail_datacenter_outer">
            <div className="pregroup_content">
              <div className="content_header_right">
                <button>연결</button>
                <button>분리</button>
                <button>활성</button>
                <button>유지보수</button>
              </div>
              <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
              </div>

              <Table columns={dataCenterColumns} data={dataCenterData} onRowClick={() => console.log('Row clicked')} />
            </div>
          </div>
        )}

        {/*밑에딸린 박스 수정 */}
        {activeTab === 'machine' && (
          <div id="detail_machine_outer">

            <div className="pregroup_content">
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
        )}

        {activeTab === 'template' && (
          <div id="detail_template_outer">
          
            <div className="pregroup_content">
            <Table columns={templateColumns} data={templateData} onRowClick={() => console.log('Row clicked')} />
            </div>
        </div>
        )}

        {activeTab === 'disk' && (
        <div id="detail_disk_outer">
       
            <div className="pregroup_content">
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
            

            
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                    <th>별칭</th>
                    <th><i className="fa fa-chevron-left"></i></th>
                    <th><i className="fa fa-chevron-left"></i></th>
                    <th>가상 크기</th>
                    <th>실제 크기</th>
                    <th>할당 정책</th>
                    <th>스토리지 도메인</th>
                    <th>생성 일자</th>
                    <th>최근 업데이트</th>
                    <th></th>
                    <th>연결 대상</th>
                    <th>상태</th>
                    <th>유형</th>
                    <th>설명</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>aa</td>
                    <td></td>
                    <td></td>
                    <td>&lt;1 GiB</td>
                    <td>&lt;1 GiB</td>
                    <td>씬 프로비저닝</td>
                    <td>hosted_storage</td>
                    <td>2024. 4. 26. PM 3:19:39</td>
                    <td>2024. 4. 26. PM 3:19:45</td>
                    <td><i className="fa fa-chevron-left"></i></td>
                    <td></td>
                    <td>OK</td>
                    <td>이미지</td>
                    <td>testa</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}

        {activeTab === 'disk_snapshot' && (
        <div id="detail_snapshot_outer">
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>제거</button>
            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>0-0</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
            <Table columns={snapshotColumns} data={snapshotData} onRowClick={() => console.log('Row clicked')} />
            </div>
            </div>
        </div>
        )}

        {activeTab === 'event' && (
        <div id="detail_event_outer">
            <div className="pregroup_content">
              <div className="content_header_right">
                
              </div>
              <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
              </div>
              <div className="table_outer2">
              <Table columns={eventColumns} data={eventData} onRowClick={() => console.log('Row clicked')} />
              </div>
            </div>
        </div>
          
        )}

        {activeTab === 'permission' && (
        <div id="detail_right_outer">
            <div className="pregroup_content">
              <div className="content_header_right">
                <button>추가</button>
                <button>제거</button>
              </div>
              <div className="storage_right_btns">
                <span>Permission Filters:</span>
                <div>
                  <button>All</button>
                  <button>Direct</button>
                </div>
              </div>
              <div>
                <div className="application_content_header">
                  <button><i className="fa fa-chevron-left"></i></button>
                  <div>1-3</div>
                  <button><i className="fa fa-chevron-right"></i></button>
                  <button><i className="fa fa-ellipsis-v"></i></button>
                </div>
              </div>
              <div className="table_outer2">
              <Table columns={permissionColumns} data={permissionData} onRowClick={() => console.log('Row clicked')} />
              </div>
            </div>
        </div>
        )}
      </div>
    </div>
  );
}

export default StorageDetail;
