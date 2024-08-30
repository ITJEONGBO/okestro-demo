import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';

import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';

import './css/ClusterName.css';
import NetworkDetail from '../Network/NetworkDetail';

function ClusterName() {
    const { name } = useParams();
    const navigate = useNavigate();
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);
    const handlePermissionFilterClick = (filter) => {
        setActivePermissionFilter(filter);
      };
      const [activePermissionFilter, setActivePermissionFilter] = useState('all');

    const handleRowClick = (row, column) => {
        console.log('Clicked column:', column);
        if (column && column.accessor === 'user') {
            alert('dfadf');
            setShowNetworkDetail(true);
            navigate('/network-detail'); // 이 부분이 중요한 부분입니다.
        }
    };


    // 논리 네트워크 테이블 컴포넌트

    const data = [
        {
            name: (
                <span>
                    <i className="fa fa-caret-up" style={{ color: '#00FF00' }}></i> ovirtmgmt
                </span>
            ),
            status: '가동 중',
            role: (
                <span>
                    <i className="fa fa-crown"></i>
                    <i className="fa fa-link" style={{ marginLeft: '5px' }}></i>
                    <i className="fa fa-exclamation-triangle" style={{ color: 'red', marginLeft: '5px' }}></i>
                </span>
            ),
            description: 'Management Network',
        },
    ];
    


    // 호스트 테이블 컴포넌트

    const hostData = [
        {
            icon: '',  // 예시 이모티콘
            name: 'host01.ititinfo.com',
            hostNameIP: 'host01.ititinfo.com',
            status: 'Up',
            loading: '1 대의 가상머신',
            displayAddress: '아니요',
        }
    ];

    // 가상머신
    const vmColumns = [
        { header: '이름', accessor: 'name', clickable: false },
        { header: '상태', accessor: 'status', clickable: false },
        { header: '업타임', accessor: 'uptime', clickable: false },
        { header: 'CPU', accessor: 'cpu', clickable: false },
        { header: '메모리', accessor: 'memory', clickable: false },
        { header: '네트워크', accessor: 'network', clickable: false },
        { header: 'IP 주소', accessor: 'ipAddress', clickable: false },
    ];
    const vmData = [
        {
            name: 'vm01',
            status: '실행 중',
            uptime: '12 days',
            cpu: '2 vCPU',
            memory: '4 GiB',
            network: 'virtio',
            ipAddress: '192.168.0.101',
        }
    ];

    // 선호도 그룹
    const affinityColumns = [
        { header: '상태', accessor: 'status', clickable: false },
        { header: '이름', accessor: 'name', clickable: false },
        { header: '설명', accessor: 'description', clickable: false },
        { header: '우선 순위', accessor: 'priority', clickable: false },
        { header: '가상 머신 측 극성', accessor: 'vmConfig', clickable: false },
        { header: '가상 머신 강제 적용', accessor: 'vmEnforce', clickable: false },
        { header: '호스트 측 극성', accessor: 'hostConfig', clickable: false },
        { header: '호스트 강제 적용', accessor: 'hostEnforce', clickable: false },
        { header: '가상머신 멤버', accessor: 'vmMember', clickable: false },
        { header: '가상 머신 레이블', accessor: 'vmLabel', clickable: false },
        { header: '호스트 멤버', accessor: 'hostMember', clickable: false },
        { header: '호스트 레이블', accessor: 'hostLabel', clickable: false },
    ];
    const affinityData = [
        {
            status: '',
            name: '',
            description: '',
            priority: '',
            vmConfig: '',
            vmEnforce: '',
            hostConfig: '',
            hostEnforce: '',
            vmMember: '',
            hostMember: '',
            vmLabel: '',
            hostLabel: '',
            colSpan: 12,
            style: { textAlign: 'center' },
            noItemsText: '표시할 항목이 없습니다',
        },
    ];

    // 권한 테이블 컴포넌트

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


    // 선호도 레이블 테이블 컴포넌트
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

    // 이벤트 테이블 컴포넌트

    const storageData = [
        {
            icon: <i className="fa fa-check-circle" style={{ color: 'green' }}></i>,  // 상태 아이콘
            time: '2024. 8. 12. PM 12:24:11',
            message: 'Check for update of host host02.ititinfo.com. Delete yum_updates file from host.',
            correlationId: '',
            source: 'oVirt',
            userEventId: '',
        },
    ];

    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        setShowNetworkDetail(false); // 탭이 변경되면 NetworkDetail 화면을 숨김
    };

    // HeaderButton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick: () => console.log('Edit button clicked') },
        { id: 'delete_btn', label: '업그레이드', onClick: () => console.log('Delete button clicked') },
    ];

    const popupItems = [
        '삭제',
        '가이드',
        '에뮬레이션된 시스템 재설정',
    ]; 
    const uploadOptions = []; // 현재 업로드 옵션이 없으므로 빈 배열로 설정

    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'logical_network', label: '논리 네트워크' },
        { id: 'host', label: '호스트' },
        { id: 'virtual_machine', label: '가상 머신' },
        { id: 'affinity_group', label: '선호도 그룹' },
        { id: 'affinity_label', label: '선호도 레이블' },
        { id: 'permission', label: '권한' },
        { id: 'event', label: '이벤트' }
    ];

    return (
        <div id='section'>
            {showNetworkDetail ? (
                <NetworkDetail />
            ) : (
                <>
                    <HeaderButton
                        title="클러스터"
                        subtitle={name}
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
                        <div className="host_btn_outer">
                            {/* 일반 */}
                            {activeTab === 'general' && (
                                <div className="cluster_general">
                                    <div className="table_container_center">
                                        <table className="table">
                                            <tbody>
                                                <tr>
                                                    <th>ID:</th>
                                                    <td>{name}</td>
                                                </tr>
                                                <tr>
                                                    <th>설명:</th>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <th>데이터센터:</th>
                                                    <td>Default</td>
                                                </tr>
                                                <tr>
                                                    <th>호환버전:</th>
                                                    <td>4.7</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 노드 유형:</th>
                                                    <td>Virt</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 ID:</th>
                                                    <td>f0adf4f6-274b-4533-b6b3-6a683b062c9a</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 CPU 유형:</th>
                                                    <td>
                                                         Intel Nehalem Family
                                                        <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>스레드를 CPU 로 사용:</th>
                                                    <td>아니요</td>
                                                </tr>
                                                <tr>
                                                    <th>최대 메모리 오버 커밋:</th>
                                                    <td>100%</td>
                                                </tr>
                                                <tr>
                                                    <th>복구 정책:</th>
                                                    <td>예</td>
                                                </tr>
                                                <tr>
                                                    <th>칩셋/펌웨어 유형:</th>
                                                    <td>UEFI의 Q35 칩셋</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div className="table_container_center">
                                        <table className="table">
                                            <tbody>
                                                <tr>
                                                    <th>에뮬레이션된 시스템:</th>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <th>가상 머신 수:</th>
                                                    <td>0</td>
                                                </tr>
                                                <tr>
                                                    <th>총 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                                <tr>
                                                    <th>Up 상태의 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                                <tr>
                                                    <th>Down 상태의 볼륨 수:</th>
                                                    <td>해당 없음</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            )}
                            {/* 논리 네트워크 */}
                            {activeTab === 'logical_network' && (
                                <>

                                    <div className="content_header_right">
                                        <button>추가</button>
                                        <button>제거</button>
                                    </div>
                                    <div className="host_filter_btns">
                                        <span>Permission Filters:</span>
                                        <div>
                                            <button>All</button>
                                            <button>Direct</button>
                                        </div>
                                    </div>
                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.LUNS} data={data} onRowClick={handleRowClick} />
                                    </div>
                                </>

                            )}
                            {/* 호스트 */}
                            {activeTab === 'host' && (
                                <>
                                    <div className="content_header_right">
                                        <button>MOM 정책 동기화</button>
                                    </div>
    
                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.HOSTS_FROM_CLUSTER} data={hostData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                                <div className="host_empty_outer">
                                    <div className="section_table_outer">

                                        <Table columns={TableColumnsInfo.VOLUMES_FROM_CLUSTER}  onRowClick={() => console.log('Row clicked')} />

                                    </div>
                                </div>
                            )}
    
                            {/* 선호도 그룹 */}
                            {activeTab === 'affinity_group' && (
                                <>
                                    <div className="content_header_right">
                                        <button>새로 만들기</button>
                                        <button>편집</button>
                                        <button>제거</button>
                                    </div>
    
                                    <div className="section_table_outer">

                                        <Table columns={TableColumnsInfo.PERMISSIONS} data={permissionData} onRowClick={() => console.log('Row clicked')} />

                                    </div>
                                </>
                            )}
    
                            {/* 선호도 레이블 */}
                            {activeTab === 'affinity_label' && (
                                <>
                                    <div className="content_header_right">
                                        <button>새로 만들기</button>
                                        <button>편집</button>
                                        <button>제거</button>
                                    </div>
    
                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.AFFINITY_LABELS} data={memberData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
    

{/* 권한 */}
{activeTab === 'permission' && (
    <div className="host_empty_outer">
        <div className="section_table_outer">
            <Table columns={TableColumnsInfo.PERMISSIONS} data={storageData} onRowClick={() => console.log('Row clicked')} />
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
        <div className="section_table_outer">
            <Table
              
                data={activePermissionFilter === 'all' ? permissionData : []}
                onRowClick={() => console.log('Row clicked')}
            />
        </div>
    </div>
)}


                            {/* 이벤트 */}
                            {activeTab === 'event' && (
                                <div className="host_empty_outer">
                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.EVENTS} data={storageData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
}

export default ClusterName;
