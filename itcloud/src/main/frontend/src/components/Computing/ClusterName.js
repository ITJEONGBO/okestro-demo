import React, { useState,useEffect } from 'react';
import {useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';

import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import AffinityGroupModal from '../Modal/AffinityGroupModal';
import './css/ClusterName.css';
import NetworkDetail from '../Network/NetworkDetail';
import Permission from '../Modal/Permission';
import { useClusterById, useEventFromCluster, useHostFromCluster, useLogicalFromCluster, usePermissionFromCluster, usePermissionromCluster, useVMFromCluster } from '../../api/RQHook';

function ClusterName() {
    const { id } = useParams();
 
    const navigate = useNavigate();
    const location = useLocation();
    const locationState = location.state; 
    const [shouldRefresh, setShouldRefresh] = useState(false);
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);

    const handlePermissionFilterClick = (filter) => {
        setActivePermissionFilter(filter);
      };
      const [activePermissionFilter, setActivePermissionFilter] = useState('all');
      const handleRowClick = (row, column) => {
        if (column.accessor === 'name') {
          navigate(`/networks/${row.name.props.children}`);  
        }
    };
    const [isPermissionModalOpen, setIsPermissionModalOpen] = useState(false); // 권한 모달 상태
    const [isAffinityGroupModalOpen, setIsAffinityGroupModalOpen] = useState(false); // 선호도 그룹 모달 상태
    
    // 권한 모달 핸들러
    const openPermissionModal = () => setIsPermissionModalOpen(true);
    const closePermissionModal = () => setIsPermissionModalOpen(false);
    
    // 선호도 그룹 모달 핸들러
    const openAffinityGroupModal = () => setIsAffinityGroupModalOpen(true);
    const closeAffinityGroupModal = () => setIsAffinityGroupModalOpen(false);

    const { 
        data: cluster,
        status: networkStatus,
        isRefetching: isNetworkRefetching,
        refetch: clusterRefetch, 
        isError: isNetworkError,
        error: networkError, 
        isLoading: isNetworkLoading,
      } = useClusterById(id);
      
      useEffect(() => {
        clusterRefetch();  // 함수 이름을 일치시킴
      }, [setShouldRefresh, clusterRefetch]);

    // 논리네트워크
    const { 
        data: networks, 
        status: networksStatus, 
        isLoading: isNetworksLoading, 
        isError: isNetworksError 
      } = useLogicalFromCluster(cluster?.id, toTableItemPredicateLogicalNetworks);
      
      function toTableItemPredicateLogicalNetworks(network) {
        return {
          name: network?.name ?? 'Unknown',            
          status: network?.status ?? 'Unknown',       
          role: network?.role ? <i className="fa fa-crown"></i> : '', 
          description: network?.description ?? 'No description', 
        };
      }
      

    // 호스트
    const { 
        data: hosts, 
        status: hostsStatus, 
        isLoading: isHostsLoading, 
        isError: isHostsError 
      } = useHostFromCluster(cluster?.id, toTableItemPredicateHosts);
      
      function toTableItemPredicateHosts(host) {
        return {
          icon: '', 
          name: host?.name ?? 'Unknown',  // 호스트 이름, 없으면 'Unknown'
          hostNameIP: host?.name ?? 'Unknown',
          status: host?.status ?? 'Unknown',  
          loading: `${host?.vmCount ?? 0} 대의 가상머신`, // 0으로 기본값 설정
          displayAddress: host?.displayAddress ?? '아니요',
        };
      }
    // 가상머신
    const { 
        data: vms, 
        status: vmsStatus, 
        isLoading: isVmsLoading, 
        isError: isVmsError 
      } = useVMFromCluster(cluster?.id, toTableItemPredicateVms);
      
      function toTableItemPredicateVms(vm) {
        const statusIcon = vm?.status === 'DOWN' 
            ? <i class="fa fa-chevron-down text-red-500"></i>
            : vm?.status === 'UP' || vm?.status === '실행 중'
            ? <i class="fa fa-chevron-up text-green-500"></i>
            : ''; // 기본값
        return {
          icon: statusIcon,      
          name: vm?.name ?? 'Unknown',               
          status: vm?.status ?? 'Unknown',           
          upTime: vm?.upTime ?? '',             
          cpu: vm?.cpu ?? '',                    
          memory: vm?.memory ?? '',              
          network: vm?.network ?? '',             
          ipv4: vm?.ipv4 ?? '',         
        };
      }
    // 권한
    const { 
        data: permissions, 
        status: permissionsStatus, 
        isLoading: isPermissionsLoading, 
        isError: isPermissionsError 
      } = usePermissionFromCluster(cluster?.id, toTableItemPredicatePermissions);

      function toTableItemPredicatePermissions(permission) {
        return {
          icon: <i className="fa fa-user"></i>,  
          user: permission?.user ?? '없음',  
          provider: permission?.provider ?? '없음',  
          nameSpace: permission?.nameSpace ?? '없음', 
          role: permission?.role ?? '없음',  
          createDate: permission?.createDate ?? '없음',  
          inheritedFrom: permission?.inheritedFrom ?? '없음', 
        };
      }
    // 이벤트
    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useEventFromCluster(cluster?.id, toTableItemPredicateEvents);

    function toTableItemPredicateEvents(event) {
        return {
          icon: '',                      
          time: event?.time ?? '',                
          description: event?.description ?? 'No message', 
          correlationId: event?.correlationId ?? '',
          source: event?.source ?? 'ovirt',     
          userEventId: event?.userEventId ?? '',   
        };
      }

    // 선호도 그룹
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
    // 선호도 레이블
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
                        subtitle={locationState?.name}
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
                                                    <td>{id}</td>
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
                                    <button>네트워크 추가</button>
                                    <button>네트워크 관리</button>
                                    <button>디스플레이로 설정</button>
                                    <button>모든 네트워크 동기화</button>
                                </div>

                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.LUNS} data={networks} onRowClick={handleRowClick} />
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
                                        <Table columns={TableColumnsInfo.HOSTS_FROM_CLUSTER} data={hosts} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                                <div className="host_empty_outer">
                                    <div className="section_table_outer">

                                        <Table columns={TableColumnsInfo.CLUSTER_VM} data={vms} onRowClick={() => console.log('Row clicked')} />

                                    </div>
                                </div>
                            )}
    
                            {/* 선호도 그룹 */}
                            {activeTab === 'affinity_group' && (
                                <>
                                    <div className="content_header_right">
                                        <button onClick={openAffinityGroupModal}>새로 만들기</button>
                                        <button>편집</button>
                                        <button>제거</button>
                                    </div>
    
                                    <div className="section_table_outer">

                                        <Table columns={TableColumnsInfo.AFFINITY_GROUP} data={affinityData} onRowClick={() => console.log('Row clicked')} />

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
                                <>
                                <div className="content_header_right">
                                <button onClick={openPermissionModal}>추가</button> {/* 추가 버튼 */}
                                <button>제거</button>
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
                                    columns={TableColumnsInfo.PERMISSIONS}
                                    data={permissions}
                                    onRowClick={() => console.log('Row clicked')}
                                />
                                </div>
                                </>
                            )}


                            {/* 이벤트 */}
                            {activeTab === 'event' && (
                                <div className="event_table_outer">
                                    <div className="section_table_outer">
                                        <Table columns={TableColumnsInfo.EVENTS} data={events} onRowClick={() => console.log('Row clicked')} />
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </>
            )}
             {/* 새로 만들기 팝업 */}

            {/* 선호도 그룹 모달 컴포넌트 */}
            <AffinityGroupModal isOpen={isAffinityGroupModalOpen} onRequestClose={closeAffinityGroupModal} />
            {/* 권한 모달 컴포넌트 */}
            <Permission isOpen={isPermissionModalOpen} onRequestClose={closePermissionModal} />
        </div>

    
    );
}

export default ClusterName;
