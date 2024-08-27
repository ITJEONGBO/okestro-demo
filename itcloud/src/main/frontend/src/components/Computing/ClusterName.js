import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import { Table } from '../table/Table';

import { useParams } from 'react-router-dom';

import './css/ClusterName.css';
import NetworkDetail from '../Network/NetworkDetail';

function ClusterName() {
    const { name } = useParams();
    const navigate = useNavigate();
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);

    const handleRowClick = (row, column) => {
        console.log('Clicked column:', column);
        if (column && column.accessor === 'user') {
            alert('dfadf');
            setShowNetworkDetail(true);
            navigate('/network-detail'); // 이 부분이 중요한 부분입니다.
        }
    };

    // 논리 네트워크 테이블 컴포넌트
    const columns = [
        { header: '', accessor: 'icon', clickable: false },
        { header: '사용자', accessor: 'user', clickable: true },
        { header: '인증 공급자', accessor: 'authProvider', clickable: false },
        { header: '네임스페이스', accessor: 'namespace', clickable: false },
        { header: '역할', accessor: 'role', clickable: false },
        { header: '생성일', accessor: 'createdDate', clickable: false },
        { header: 'Inherited From', accessor: 'inheritedFrom', clickable: false },
    ];

    const data = [
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

    // 호스트 테이블 컴포넌트
    const hostColumns = [
        { header: '', accessor: 'icon', clickable: false },  // 이모티콘 칸
        { header: '이름', accessor: 'name', clickable: false },
        { header: '호스트이름/IP', accessor: 'hostNameIP', clickable: false },
        { header: '상태', accessor: 'status', clickable: false },
        { header: '불러오기', accessor: 'loading', clickable: false },
        { header: '디스플레이 주소 덮어쓰기', accessor: 'displayAddress', clickable: false }
    ];

    const hostData = [
        {
            icon: <i className="fa fa-exclamation-triangle" style={{ color: 'red' }}></i>,  // 예시 이모티콘
            name: 'host01.ititinfo.com',
            hostNameIP: 'host01.ititinfo.com',
            status: 'Up',
            loading: '1 대의 가상머신',
            displayAddress: '아니요',
        },
    ];

    // 호스트장치 테이블 컴포넌트
    const volumeColumns = [
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

    const volumeData = [
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

    // 선호도 레이블 테이블 컴포넌트
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

    // 이벤트 테이블 컴포넌트
    const storageColumns = [
        { header: '', accessor: 'icon', clickable: false },  // 이모티콘 또는 상태 아이콘 칸
        { header: '시간', accessor: 'time', clickable: false },
        { header: '메시지', accessor: 'message', clickable: false },
        { header: '상관 관계 ID', accessor: 'correlationId', clickable: false },
        { header: '소스', accessor: 'source', clickable: false },
        { header: '사용자 지정 이벤트 ID', accessor: 'userEventId', clickable: false },
    ];

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
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
    ];

    const popupItems = []; // 현재 팝업 아이템이 없으므로 빈 배열로 설정
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
                                <div className="tables">
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
                                        <Table columns={columns} data={data} onRowClick={handleRowClick} />
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
                                        <Table columns={hostColumns} data={hostData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                                <>
                                    <div className="content_header_right">
                                        <button>편집</button>
                                        <button>유지보수</button>
                                        <button>활성</button>
                                        <button>기능을 새로 고침</button>
                                        <button>재시작</button>
                                    </div>
    
                                    <div className="section_table_outer">
                                        <Table columns={volumeColumns} data={volumeData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
    
                            {/* 선호도 그룹 */}
                            {activeTab === 'affinity_group' && (
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
                                        <Table columns={permissionColumns} data={permissionData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
    
                            {/* 선호도 레이블 */}
                            {activeTab === 'affinity_label' && (
                                <>
                                    <div className="content_header_right">
                                        <button>새로 만들기</button>
                                        <button>편집</button>
                                    </div>
    
                                    <div className="section_table_outer">
                                        <Table columns={memberColumns} data={memberData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
    
                            {/* 권한 */}
                            {activeTab === 'permission' && (
                                <div className="host_empty_outer">
                                    <div className="section_table_outer">
                                        <Table columns={storageColumns} data={storageData} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </div>
                            )}
    
                            {/* 이벤트 */}
                            {activeTab === 'event' && (
                                <div className="host_empty_outer">
                                    <div className="section_table_outer">
                                        <Table columns={storageColumns} data={storageData} onRowClick={() => console.log('Row clicked')} />
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
