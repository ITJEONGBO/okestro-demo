import React, { useState } from 'react';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';
import { Table } from '../components/table/Table';

function HostDetail() {
     // 가상머신 테이블컴포넌트
     const columns = [
        { header: '이름', accessor: 'name', clickable: false },
        { header: '클러스터', accessor: 'cluster', clickable: false },
        { header: 'IP 주소', accessor: 'ipAddress', clickable: false },
        { header: 'FQDN', accessor: 'fqdn', clickable: false },
        { header: '메모리', accessor: 'memory', clickable: false },
        { header: 'CPU', accessor: 'cpu', clickable: false },
        { header: '네트워크', accessor: 'network', clickable: false },
        { header: '상태', accessor: 'status', clickable: false },
        { header: '업타임', accessor: 'uptime', clickable: false },
      ];
      
      const data = [
        {
          name: (
            <div>
              <i className="fa fa-caret-up" style={{ color: 'green' }}></i>
              HostedEngine
            </div>
          ),
          cluster: (
            <div>
              <i className="fa fa-desktop"></i>
              Default
            </div>
          ),
          ipAddress: '192.168.0.80 fe80::216:3eff:fe6c:208',
          fqdn: 'ovirt.ititinfo.com',
          memory: (
            <div>
              <span>52%</span>
              <div style={{ width: '52%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          cpu: (
            <div>
              <span>2%</span>
              <div style={{ width: '2%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          network: (
            <div>
              <span>0%</span>
              <div style={{ width: '0%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          status: '실행 중',
          uptime: '36 days',
        },
      ];
      
   // 호스트장치 테이블 컴포넌트
   const volumeColumns = [
    { header: '이름', accessor: 'name', clickable: false },
    { header: '기능', accessor: 'function', clickable: false },
    { header: '벤더', accessor: 'vendor', clickable: false },
    { header: '제품', accessor: 'product', clickable: false },
    { header: '드라이버', accessor: 'driver', clickable: false },
    { header: '현재 사용중', accessor: 'currentlyUsed', clickable: false },
    { header: '가상 머신에 연결됨', accessor: 'connectedToVM', clickable: false },
    { header: 'IOMMU 그룹', accessor: 'iommuGroup', clickable: false },
    { header: 'Mdev 유형', accessor: 'mdevType', clickable: false },
  ];
  
  const volumeData = [
    {
      name: 'block_sda',
      function: 'storage',
      vendor: 'VMware (null)',
      product: 'Virtual disk (null)',
      driver: '',
      currentlyUsed: '',
      connectedToVM: '',
      iommuGroup: '해당 없음',
      mdevType: '해당 없음',
    },
  ];

    // 권한 테이블 컴포넌트
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
      //선호도 레이블 테이블 컴포넌트
      const memberColumns = [
        { header: '이름', accessor: 'name', clickable: false },
        { header: '가상머신 멤버', accessor: 'vmMember', clickable: false },
        { header: '호스트 멤버', accessor: 'hostMember', clickable: false },
      ];
    
      const memberData = [
        {
          name: '',
          vmMember: '',
          hostMember: '',
          colSpan: 3,
          style: { textAlign: 'center' },
          noItemsText: '표시할 항목이 없습니다',
        },
      ];

      // 이벤트
      const eventColumns = [
        { header: '', accessor: 'icon', clickable: false },
        { header: '시간', accessor: 'time', clickable: false },
        { header: '메세지', accessor: 'message', clickable: false },
        { header: '상관 관계 ID', accessor: 'correlationId', clickable: false },
        { header: '소스', accessor: 'source', clickable: false },
        { header: '사용자 지정 이벤트 ID', accessor: 'userEventId', clickable: false },
      ];
      
      const eventData = [
        {
          icon: <i className="fa fa-check-circle" style={{ color: 'green' }}></i>,
          time: '2024. 8. 7. PM 12:24:14',
          message: 'Check for available updates on host host01.ittinfo.com was completed successfully with message \'no updates available.\'',
          correlationId: '2568d791:c08...',
          source: 'oVirt',
          userEventId: '',
        },
      ];
      
    //
    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };


    //headerbutton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick: () => console.log('Edit button clicked') },
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
        { id: 'manage_btn', label: '관리', onClick: () => console.log('Manage button clicked') },
        { id: 'install_btn', label: '설치', onClick: () => console.log('Install button clicked') },
        { id: 'host_console_btn', label: '호스트 콘솔', onClick: () => console.log('Host Console button clicked') },
        { id: 'copy_network_btn', label: '호스트 네트워크 복사', onClick: () => console.log('Copy Host Network button clicked') },
      ];
    
      const popupItems = []; // 현재 팝업 아이템이 없으므로 빈 배열로 설정
      const uploadOptions = []; // 현재 업로드 옵션이 없으므로 빈 배열로 설정
    // nav컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'machine', label: '가상머신' },
        { id: 'networkinterface', label: '네트워크 인터페이스' },
        { id: 'hostdevice', label: '호스트 장치' },
        { id: 'permission', label: '권한' },
        { id: 'lable', label: '선호도 레이블' },
        { id: 'event', label: '이벤트' }
      ];

      
    return (
        <div id='section'>
             <HeaderButton
      title="호스트"
      subtitle="192.168.0.80"
      additionalText="목록이름"
      buttons={buttons}
      popupItems={popupItems}
      uploadOptions={uploadOptions}
    />



            <div className="content_outer">
                <NavButton
                    sections={sections} 
                    activeSection={activeTab} 
                    handleSectionClick={handleTabClick} 
                />

                {/* 일반 */}
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
                {/* 가상머신 */}
                {activeTab === 'machine' && (
                    
                <div className="storage_right_outer">
                    <div className="storage_domain_content">
                        <div className="content_header_right">
                            <button>추가</button>
                            <button>제거</button>
                        </div>
                        <div className="storage_right_btns">
                            <span>Permission Filters:</span>
                            <div>
                                <button>현재 호스트에서 실행 중</button>
                                <button>현재 호스트에 고정</button>
                                <button>모두</button>
                            </div>
                        </div>
                       
                        <Table columns={columns} data={data} onRowClick={() => console.log('Row clicked')} />
                    </div>
            </div>
                )}
                {/* 네트워크 인터페이스 */}
                {activeTab === 'networkinterface' && (
                <div className="host_detail_outer">
                    <div className="pregroup_content">
                        <div className="host_detail_network">
                            <button><i className="fa fa-chevron-left"></i></button>
                            <div>1-1</div>
                            <button><i className="fa fa-chevron-right"></i></button>
                            <button><i className="fa fa-ellipsis-v"></i></button>
                        </div>
                    </div>
                </div>
                )}
                {/* 호스트 장치 */}
                {activeTab === 'hostdevice' && (
                <div className="host_detail_outer">
            
                <div className="pregroup_content">
                <div className="content_header_right">
                    <button>편집</button>
                    <button>유지보수</button>
                    <button>활성</button>
                    <button>기능을 새로 고침</button>
                    <button>재시작</button>
                </div>
                <div className="application_content_header">
                    <button><i className="fa fa-chevron-left"></i></button>
                    <div>1-1</div>
                    <button><i className="fa fa-chevron-right"></i></button>
                    <button><i className="fa fa-ellipsis-v"></i></button>
                </div>
                
                <Table columns={volumeColumns} data={volumeData} onRowClick={() => console.log('Row clicked')} />
                
                </div>
                </div>
                )}
               
                {/* 권한 */}
                {activeTab === 'permission' && (
                <div className="storage_right_outer">
                    <div className="storage_domain_content">
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
                        
                        <Table columns={permissionColumns} data={permissionData} onRowClick={() => console.log('Row clicked')} />
                    </div>
                </div>
                )}
             
                {/* 선호도 레이블 */}
                {activeTab === 'lable' && (
                    <div id="detail_rent_outer">
                        <div className="pregroup_content">
                            <div className="content_header_right">
                                <button>새로 만들기</button>
                                <button>편집</button>
                            </div>
                            <Table columns={memberColumns} data={memberData} onRowClick={() => console.log('Row clicked')} />
                            
                        </div>
                </div>
                )}
          
                {/* 이벤트 */}
                {activeTab === 'event' && (
                <div className="detail_machine_outer">
                    <div className="pregroup_content">
                  
                    
                    <Table columns={eventColumns} data={eventData} onRowClick={() => console.log('Row clicked')} />
                    
                    </div>
                </div>
                
                )}

                
            </div>




        </div>
    );
}

export default HostDetail;