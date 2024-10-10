import React, { useState,useEffect } from 'react';
import {useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Modal from 'react-modal';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import AffinityGroupModal from '../Modal/AffinityGroupModal';
import NetworkDetail from '../Network/NetworkDetail';
import Permission from '../Modal/Permission';
import { useClusterById, useEventFromCluster, useHostFromCluster, useLogicalFromCluster, usePermissionFromCluster, usePermissionromCluster, useVMFromCluster } from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faCrown, faUser, faBan,
  faTimes,
  faInfoCircle,
  faExclamationTriangle,
  faEarthAmericas,
  faEllipsisV
} from '@fortawesome/free-solid-svg-icons'
import './css/ClusterName.css';
import './vm/DiskSection.js';
import TableOuter from '../table/TableOuter';
import Path from '../Header/Path';
import DiskSection from './vm/DiskSection.js';
import TemplateDu from '../duplication/TemplateDu.js';
import HostDu from '../duplication/HostDu.js';
import VmDu from '../duplication/VmDu.js';

function ClusterName() {
    const { id } = useParams();
    const [activeTab, setActiveTab] = useState('general');
    const navigate = useNavigate();
    const location = useLocation();
    const locationState = location.state; 
    const [shouldRefresh, setShouldRefresh] = useState(false);
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);
    const [activePopup, setActivePopup] = useState(null);
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
    const [secondModalOpen, setSecondModalOpen] = useState(false); // 추가 모달 상태
    const handleTabClick = (tab) => {
        setActiveTab(tab);
        setShowNetworkDetail(false); // 탭이 변경되면 NetworkDetail 화면을 숨김
        localStorage.setItem('activeTab', tab); // 새로고침해도 값유지
    };
    useEffect(() => {
        const savedTab = localStorage.getItem('activeTab');
        if (savedTab) {
            setActiveTab(savedTab);  // 저장된 값이 있으면 해당 탭을 활성화
        } else {
            setActiveTab('general');  // 저장된 값이 없으면 '일반' 탭을 기본값으로 설정
        }
    }, []);

    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedPopupTab('cluster_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };

    const closePopup = () => {
        setActivePopup(null);
    };
    const handleTabClickModal = (tab) => {
        setSelectedTab(tab);
    };
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
    // 기존의 openPopup 함수 수정


    // 선호도 그룹 모달 핸들러
    const openAffinityGroupModal = () => setIsAffinityGroupModalOpen(true);
    const closeAffinityGroupModal = () => setIsAffinityGroupModalOpen(false);
    const [showTooltip, setShowTooltip] = useState(false); // hover하면 설명창 뜨게하기

    // ...버튼 클릭
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const togglePopup = () => {
      setIsPopupOpen(!isPopupOpen);
    };

    const [activeSection, setActiveSection] = useState('common_outer');
    const handleSectionChange = (section) => {
        setActiveSection(section);
      };
    

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
    } = useLogicalFromCluster(cluster?.id, (network) => {
    return {
        name: network?.name ?? 'Unknown',            
        status: network?.status ?? 'Unknown',       
        role: network?.role ? <FontAwesomeIcon icon={faCrown} fixedWidth/> : '', 
        description: network?.description ?? 'No description', 
      };
    });
      

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
            ? <i class="fa-solid fa-chevron-down text-red-500" fixedWidth/>
            : vm?.status === 'UP' || vm?.status === '실행 중'
            ? <i class="fa-solid fa-chevron-up text-green-500" fixedWidth/>
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
    // 스토리지
    const storagedata = [
        {
          icon: '👑', 
          icon2: '👑',
          domainName: (
            <span
              style={{ color: 'blue', cursor: 'pointer'}}
              onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
              onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
            >
            hosted_storage
            </span>
          ),
          domainType: '데이터 (마스터)',
          status: '활성화',
          freeSpace: '83 GiB',
          usedSpace: '16 GiB',
          totalSpace: '99 GiB',
          description: '',
        },
      ];
    // 권한
    const { 
        data: permissions, 
        status: permissionsStatus, 
        isLoading: isPermissionsLoading, 
        isError: isPermissionsError 
      } = usePermissionFromCluster(cluster?.id, toTableItemPredicatePermissions);

      function toTableItemPredicatePermissions(permission) {
        return {
          icon: <FontAwesomeIcon icon={faUser} fixedWidth/>, 
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


    // HeaderButton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick:() => openPopup('cluster_detail_edit') },
        { id: 'delete_btn', label: '삭제', onClick: () => openPopup('delete')},
    ];


    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'host', label: '호스트' },     
        { id: 'virtual_machine', label: '가상 머신' },
        { id: 'storage', label: '스토리지' },
        { id: 'logical_network', label: '논리 네트워크' },
        // { id: 'affinity_group', label: '선호도 그룹' },
        // { id: 'affinity_label', label: '선호도 레이블' },
        { id: 'permission', label: '권한' },
        { id: 'event', label: '이벤트' },
         { id: 'disk', label: '디스크' }
    ];


    const pathData = [
        cluster?.name,
        activeTab === 'virtual_machine' || activeTab === 'template' ? '가상머신' : 
        activeTab === 'storage' || activeTab === 'storage_disk' ? '스토리지' :
        sections.find(section => section.id === activeTab)?.label,
        activeTab === 'template' ? '템플릿' : 
        activeTab === 'storage_disk' ? '디스크' : ''  
    ].filter(Boolean);
    
    
    
  // 클러스터 팝업(보류)
  const clusterPopupData = [
    {
      id: id,
      name: 'Default',
      allAssigned: (
        <>
          <input type="checkbox" checked /> <label>할당</label>
        </>
      ),
      allRequired: (
        <>
          <input type="checkbox" checked/> <label>필요</label>
        </>
      ),
      vmNetMgmt: (
        <>
          <i class="fa-solid fa-star" style={{ color: 'green'}}fixedWidth/>
        </>
      ),
      networkOutput: <input type="checkbox" />,
      migrationNetwork: <input type="checkbox"/>,
      glusterNetwork: <input type="checkbox"/>,
      defaultRouting: <input type="checkbox"/>,
    },
  ];

    return (
        <div id='section'>
            {showNetworkDetail ? (
                <NetworkDetail />
            ) : (
                <>
                    <HeaderButton
                        titleIcon={faEarthAmericas}
                        title="클러스터"
                        subtitle={cluster?.name}
                        additionalText="목록이름"
                        buttons={buttons}
                        popupItems={[]}
                        uploadOptions={[]}
                    />
    
                    <div className="content_outer">
                        <NavButton
                            sections={sections}
                            activeSection={activeTab}
                            handleSectionClick={handleTabClick}
                        />
                        <div className="host_btn_outer">
                            {activeTab !== 'general' && <Path pathElements={pathData} />}
                            {/* 일반 */}
                            {activeTab === 'general' && (
                                <div className="cluster_general">
                                    <div className="table_container_center">
                                        <table className="table">
                                            <tbody>
                                                <tr>
                                                    <th>이름</th>
                                                    <td>{cluster?.name}</td>
                                                </tr>
                                                <tr>
                                                    <th>설명:</th>
                                                    <td>{cluster?.description}</td>
                                                </tr>
                                                <tr>
                                                    <th>데이터센터:</th>
                                                    <td>{cluster?.dataCenter?.id}</td>
                                                </tr>
                                                <tr>
                                                    <th>호환버전:</th>
                                                    <td>{cluster?.version}</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 노드 유형:</th>
                                                    <td>Virt</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 ID:</th>
                                                    <td>{cluster?.id}</td>
                                                </tr>
                                                <tr>
                                                    <th>클러스터 CPU 유형:</th>
                                                    <td>
                                                        {cluster?.cpuType}
                                                         <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)',marginLeft:'3px' }}fixedWidth/> 
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>스레드를 CPU 로 사용:</th>
                                                    <td>아니요</td>
                                                </tr>
                                                <tr>
                                                    <th>최대 메모리 오버 커밋:</th>
                                                    <td>{cluster?.memoryOverCommit}%</td>
                                                </tr>
                                                <tr>
                                                    <th>복구 정책:</th>
                                                    <td>예</td>
                                                </tr>
                                                <tr>
                                                    <th>칩셋/펌웨어 유형:</th>
                                                    <td>{cluster?.biosType}</td>
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
                                                    <td>{cluster?.vmSize?.allCnt}</td>
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
                            {/* 호스트 */}
                            {activeTab === 'host' && (
                                <>
                                 <HostDu 
                                data={hosts} 
                                columns={TableColumnsInfo.HOSTS_ALL_DATA} 
                                handleRowClick={handleRowClick}
                                openPopup={openPopup}
                              />
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                            //   <>
                            //   <div className="header_right_btns">
                            //         <button onClick={() => openPopup('vm_new')}>새로만들기</button>
                            //         <button onClick={() => openPopup('vm_edit')}>편집</button>
                            //         <button className='disabled'>실행</button>
                            //         <button className='disabled'>일시중지</button>
                            //         <button className='disabled'>종료</button>
                            //         <button className='disabled'>재부팅</button>
                            //         <button onClick={() => setActiveTab('template')}>템플릿</button>
                            //         <button>콘솔</button>
                            //         <button>스냅샷 생성</button>
                            //         <button className='disabled'>마이그레이션</button>
                            //         <button className="content_header_popup_btn" onClick={togglePopup}>
                            //                     <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                            //                     {isPopupOpen && (
                            //                       <div className="content_header_popup">
                            //                         <div>OVF 업데이트</div>
                            //                         <div>파괴</div>
                            //                         <div>디스크 검사</div>
                            //                         <div>마스터 스토리지 도메인으로 선택</div>
                            //                       </div>
                            //                     )}
                            //         </button>
                            //     </div>
                              
                            //     <TableOuter 
                            //       columns={TableColumnsInfo.VM_CHART} 
                            //       data={vms} 
                            //       onRowClick={() => console.log('Row clicked')}
                            //       showSearchBox={true}
                            //     />
                            
                            //   </>
                            <VmDu 
                                data={vms} 
                                columns={TableColumnsInfo.VM_CHART} 
                                handleRowClick={handleRowClick} 
                                openPopup={openPopup}
                                setActiveTab={setActiveTab}
                                togglePopup={togglePopup}
                                isPopupOpen={isPopupOpen}
                                />
                            )}
                                {/* 템플릿 */}
                                {activeTab === 'template' && (
                                    // <>
                                    // <div className="header_right_btns">
                                    //     <button>새로 만들기</button>
                                    //     <button>편집</button>
                                    //     <button>삭제</button>
                                    //     <button>관리</button>
                                    //     <button>설치</button>
                                    //     <button>호스트 네트워크 복사</button>
                                    //     <button className="content_header_popup_btn" onClick={togglePopup}>
                                    //     <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                                    //     {isPopupOpen && (
                                    //         <div className="content_header_popup">
                                    //         <div className='disabled'>OVF 업데이트</div>
                                    //         <div className='disabled'>파괴</div>
                                    //         <div className='disabled'>디스크 검사</div>
                                    //         <div className='disabled'>마스터 스토리지 도메인으로 선택</div>
                                    //         </div>
                                    //     )}
                                    //     </button>
                                    // </div>
                                    
                                    // <TableOuter 
                                    //     columns={TableColumnsInfo.HOSTS_FROM_CLUSTER} 
                                    //     data={hosts}
                                    //     onRowClick={() => console.log('Row clicked')} 
                                    // />
                                    
                                    // </>
                                    <TemplateDu 
                                    data={hosts} 
                                    columns={TableColumnsInfo.TEMPLATE_CHART} 
                                    handleRowClick={handleRowClick}
                                />
                                )}
                            {/* 스토리지 */}
                            {activeTab === 'storage' && (
                                        <>
                                        <div className="header_right_btns">
                                            <button>도메인 관리</button>
                                            <button className='disabled'>도메인 가져오기</button>
                                            <button className='disabled'>도메인 관리</button>
                                            <button>삭제</button>
                                            <button className='disabled'>Connections</button>
                                            <button>LUN 새로고침</button>
                                            <button onClick={() => setActiveTab('storage_disk')}>디스크</button>
                                        </div>
                                        <TableOuter 
                                            columns={TableColumnsInfo.STORAGES_FROM_DATACENTER} 
                                            data={storagedata}
                                            onRowClick={handleRowClick}
                                        />
                                        </>
                            )}
                                {/* 디스크 */}
                                {activeTab === 'storage_disk' && (
                                    <>
                                    <div className="header_right_btns">
                                        <button>새로 만들기</button>
                                        <button className='disabled'>분리</button>
                                        <button className='disabled'>활성</button>
                                        <button>유지보수</button>
                                        <button>디스크</button>
                                    </div>
                                    <TableOuter 
                                        columns={TableColumnsInfo.STORAGES_FROM_DATACENTER} 
                                        data={storagedata}
                                        onRowClick={handleRowClick}
                                    />
                                    </>
                                )}

                            {/* 논리 네트워크 */}
                            {activeTab === 'logical_network' && (
                                <>
                                <div className="header_right_btns">
                                    <button onClick={() => openPopup('newNetwork')}>네트워크 추가</button>
                                    <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
                                    <button>디스플레이로 설정</button>
                                    <button>모든 네트워크 동기화</button>
                                </div>
                                <TableOuter
                                  columns={TableColumnsInfo.LUNS} 
                                  data={networks} 
                                  onRowClick={handleRowClick} />
                                </>

                            )}
                             {/* 논리 네트워크 */}
                             {activeTab === 'disk' && (
                                <DiskSection/>
                            )}
                           
                            {/* 선호도 그룹/ 선호도 레이블 주석
                            {activeTab === 'affinity_group' && (
                              <>
                              <div className="content_header_right">
                                <button onClick={openAffinityGroupModal}>새로 만들기</button>
                                <button>편집</button>
                                <button>제거</button>
                              </div>
                              <TableOuter 
                                columns={TableColumnsInfo.AFFINITY_GROUP} 
                                data={affinityData} 
                                onRowClick={() => console.log('Row clicked')} 
                              />
                              </>
                            )}
                            {activeTab === 'affinity_label' && (
                                <>
                                <div className="content_header_right">
                                  <button>새로 만들기</button>
                                  <button>편집</button>
                                  <button>제거</button>
                                </div>
                                <TableOuter 
                                  columns={TableColumnsInfo.AFFINITY_LABELS} 
                                  data={memberData} 
                                  onRowClick={() => console.log('Row clicked')} 
                                />
                                </>
                            )}  */}

                            {/* 권한 */}
                            {activeTab === 'permission' && (
                                <>
                                <div className="header_right_btns">
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
                                <TableOuter 
                                  columns={TableColumnsInfo.PERMISSIONS}
                                  data={permissions}
                                  onRowClick={() => console.log('Row clicked')}
                                />
                                </>
                            )}
                            {/* 이벤트 */}
                            {activeTab === 'event' && (
                              <div className="event_table_outer">
                                <TableOuter 
                                  columns={TableColumnsInfo.EVENTS}
                                  data={events}
                                  onRowClick={() => console.log('Row clicked')} 
                                />
                              </div>
                            )}
                        </div>
                    </div>
                </>
            )}

            {/*편집 팝업 */}
            <Modal
                isOpen={activePopup === 'cluster_detail_edit'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="cluster_new_popup">
                    <div className="popup_header">
                        <h1>클러스터 수정</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className='flex'>
                    <div className="network_new_nav">
                        <div
                            id="cluster_common_btn"
                            className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => setSelectedPopupTab('cluster_common_btn')}  // 여기서 상태를 업데이트
                        >
                            일반
                        </div>
                        <div
                            id="cluster_migration_btn"
                            className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => setSelectedPopupTab('cluster_migration_btn')}  // 상태 업데이트
                        >
                        마이그레이션 정책
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedPopupTab === 'cluster_common_btn' && (
                        <form className="cluster_common_form py-1">
                            <div className="network_form_group">
                            <label htmlFor="data_center">데이터 센터</label>
                            <select id="data_center">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <div>
                                <label htmlFor="name">이름</label>
                            </div>
                            <input type="text" id="name" />
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="description">설명</label>
                            <input type="text" id="description" />
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="comment">코멘트</label>
                            <input type="text" id="comment" />
                            </div>
                      
                            {/* id 편집 */}
                            <div className="network_form_group">
                            <label htmlFor="management_network">관리 네트워크</label>
                            <select id="management_network">
                                <option value="ovirtmgmt">ovirtmgmt</option>
                                <option value="ddd">ddd</option>
                                <option value="hosted_engine">hosted_engine</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_architecture">CPU 아키텍처</label>
                            <select id="cpu_architecture">
                                <option value="정의되지 않음">정의되지 않음</option>
                                <option value="x86_64">x86_64</option>
                                <option value="ppc64">ppc64</option>
                                <option value="s390x">s390x</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="cpu_type">CPU 유형</label>
                            <select id="cpu_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="chipset_firmware_type">침셋/펌웨어 유형</label>
                            <select id="chipset_firmware_type">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="bios_change" name="bios_change" />
                            <label htmlFor="bios_change">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="fips_mode">FIPS 모드</label>
                            <select id="fips_mode">
                                <option value="자동 감지">자동 감지</option>
                                <option value="비활성화됨">비활성화됨</option>
                                <option value="활성화됨">활성화됨</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="compatibility_version">호환 버전</label>
                            <select id="compatibility_version">
                                <option value="4.7">4.7</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="switch_type">스위치 유형</label>
                            <select id="switch_type">
                                <option value="Linux Bridge">Linux Bridge</option>
                                <option value="OVS (기술 프리뷰)">OVS (기술 프리뷰)</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="firewall_type">방화벽 유형</label>
                            <select id="firewall_type">
                                <option value="iptables">iptables</option>
                                <option value="firewalld">firewalld</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="default_network_provider">기본 네트워크 공급자</label>
                            <select id="default_network_provider">
                                <option value="기본 공급자가 없습니다.">기본 공급자가 없습니다.</option>
                                <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                            </select>
                            </div>
                        
                            <div className="network_form_group">
                            <label htmlFor="max_memory_limit">로그인 최대 메모리 한계</label>
                            <select id="max_memory_limit">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="virt_service_enabled" name="virt_service_enabled" />
                            <label htmlFor="virt_service_enabled">Virt 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="gluster_service_enabled" name="gluster_service_enabled" />
                            <label htmlFor="gluster_service_enabled">Gluster 서비스 활성화</label>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <span>추가 난수 생성기 소스:</span>
                            </div>
                        
                            <div className="network_checkbox_type2">
                            <input type="checkbox" id="dev_hwrng_source" name="dev_hwrng_source" />
                            <label htmlFor="dev_hwrng_source">/dev/hwrng 소스</label>
                            </div>
                        </form>
                      
                    )}

                    {/* 마이그레이션 정책 */}
                    {selectedPopupTab === 'cluster_migration_btn' && (
                        <form className="py-2">
                            <div className="network_form_group">
                            <label htmlFor="migration_policy">마이그레이션 정책</label>
                            <select id="migration_policy">
                                <option value="default">Default</option>
                            </select>
                            </div>
                        
                            <div class="p-1.5">
                            <span class="font-bold">최소 다운타임</span>
                            <div>
                                일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.
                            </div>
                            </div>
                        
                            <div class="p-1.5 mb-1">
                            <span class="font-bold">대역폭</span>
                            <div className="cluster_select_box">
                                <div class="flex">
                                <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
                                <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
                                </div>
                                <select id="bandwidth_policy">
                                <option value="default">Default</option>
                                </select>
                            </div>
                            </div>
                        
                            <div className="px-1.5 flex relative">
                            <span className="font-bold">복구정책</span>
                            <FontAwesomeIcon
                                icon={faInfoCircle}
                                style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }}
                                onMouseEnter={() => setShowTooltip(true)} // 마우스를 올리면 툴팁을 보여줌
                                onMouseLeave={() => setShowTooltip(false)} // 마우스를 떼면 툴팁을 숨김
                            />
                            {showTooltip && (
                                <div className="tooltip-box">
                                마이그레이션 암호화에 대한 설명입니다.
                                </div>
                            )}
                            </div>
                      
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="password_option" name="encryption_option" />
                            <label htmlFor="password_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="certificate_option" name="encryption_option" />
                            <label htmlFor="certificate_option">암호</label>
                            </div>
                        
                            <div className='host_text_radio_box px-1.5 py-0.5 mb-2'>
                            <input type="radio" id="none_option" name="encryption_option" />
                            <label htmlFor="none_option">암호</label>
                            </div>
                        
                            <div class="m-1.5">
                            <span class="font-bold">추가 속성</span>
                            <div className="cluster_select_box">
                                <label htmlFor="encryption_usage">마이그레이션 암호화 사용</label>
                                <select id="encryption_usage">
                                <option value="default">시스템 기본값 (암호화하지 마십시오)</option>
                                <option value="encrypt">암호화</option>
                                <option value="no_encrypt">암호화하지 마십시오</option>
                                </select>
                            </div>
                            
                            <div className="cluster_select_box">
                                <label htmlFor="parallel_migration">마이그레이션 암호화 사용</label>
                                <select id="parallel_migration">
                                <option value="default">Disabled</option>
                                <option value="auto">Auto</option>
                                <option value="auto_parallel">Auto Parallel</option>
                                <option value="custom">Custom</option>
                                </select>
                            </div>
                        
                            <div className="cluster_select_box">
                                <label htmlFor="migration_encryption_text">마이그레이션 암호화 사용</label>
                                <input type="text" id="migration_encryption_text" />
                            </div>
                            </div>
                        </form>
                      
                    )}
                    </div>
                   
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 가상머신( 새로만들기)팝업 */}   
            <Modal
    isOpen={activePopup === 'vm_new'}
    onRequestClose={closePopup}
    contentLabel="가상머신 편집"
    className="edit_popup"
    overlayClassName="edit_popup_outer"
    shouldCloseOnOverlayClick={false}
  >
 <div id="edit_popup">
            <div className="popup_header">
              <h1>가상머신 생성</h1>
              <button onClick={closePopup}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            
            <div className="edit_body">
            <div className="edit_aside">
                <div
                  className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
                  id="common_outer_btn"
                  onClick={() => handleSectionChange('common_outer')}
                >
                  <span>일반</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
                  id="system_outer_btn"
                  onClick={() => handleSectionChange('system_outer')}
                >
                  <span>시스템</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
                  id="host_outer_btn"
                  onClick={() => handleSectionChange('host_outer')}
                >
                  <span>호스트</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
                  id="ha_mode_outer_btn"
                  onClick={() => handleSectionChange('ha_mode_outer')}
                >
                  <span>고가용성</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
                  id="res_alloc_outer_btn"
                  onClick={() => handleSectionChange('res_alloc_outer')}
                >
                  <span>리소스 할당</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
                  id="boot_outer_btn"
                  onClick={() => handleSectionChange('boot_outer')}
                >
                  <span>부트 옵션</span>
                </div>
                
              </div>


                    <form action="#">
                        {/* 일반 */}
                        <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                            </div>
                            <div className="edit_third_content">
                                <div>
                                    <span>하드디스크</span>
                                </div>
                                <div>
                                    <button>연결</button>
                                    <button>생성</button>
                                    <div className='flex'>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>
                            <div className="edit_fourth_content">
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                    
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>

                        {/* 시스템 */}
                        <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                            
                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="memory_size">메모리 크기</label>
                                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">최대 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="total_cpu" value="1" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                        <span>고급 매개 변수</span>
                                    </div>
                                </div>
                                <div style={{ fontWeight: 600 }}>일반</div>
                                <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                    <div>
                                        <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <select id="time_offset">
                                        <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        {/* 호스트 */}
                        <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                         

                            <div id="host_second_content">
                                <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                <div className="form_checks">
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                        <label className="form-check-label" htmlFor="flexRadioDefault1">
                                            클러스터 내의 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                            <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                특정 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <select id="specific_host_select">
                                                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="host_checkboxs">
                                    <span>CPU 옵션:</span>
                                    <div className="host_checkbox">
                                        <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                        <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                   
                                </div>
                            </div>

                            <div id="host_third_content">
                                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                <div>
                                    <div>
                                        <span>마이그레이션 모드</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_mode">
                                        <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>마이그레이션 정책</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_policy">
                                        <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <div>
                                        <span>Parallel Migrations</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="parallel_migrations" readOnly>
                                        <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                    </select>
                                </div>
                               
                            </div>
                        </div>

                        {/* 고가용성 */}
                        <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>

                            <div id="ha_mode_second_content">
                                <div className="checkbox_group">
                                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                    <label className="check_label" htmlFor="ha_mode_box">
                                        고가용성
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <span>가상 머신 임대 대상 스토리지 도메인</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="no_lease" disabled>
                                        <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>재개 동작</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="force_shutdown">
                                        <option value="강제 종료">강제 종료</option>
                                    </select>
                                </div>
                                <div className="ha_mode_article">
                                    <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                    <div>
                                        <span>우선 순위</span>
                                        <select id="priority">
                                            <option value="낮음">낮음</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="ha_mode_article">
                                    <span>위치독</span>
                                    <div>
                                        <span>위치독 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>위치독 작업</span>
                                        <select id="watchdog_action" disabled>
                                            <option value="없음">없음</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 리소스 할당 */}
                        <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>

                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                    <div className='cpu_res_box'>
                                        <span>CPU 프로파일</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU 공유</span>
                                        <div id="cpu_sharing">
                                            <select id="watchdog_action" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU Pinning Policy</span>
                                        <select id="watchdog_action">
                                            <option value="없음">None</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <div>
                                            <span>CPU 피닝 토폴로지</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" disabled />
                                    </div>
                                </div>

                                <span style={{ fontWeight: 600 }}>메모리 할당:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                                    </div>
                                
                                </div>

                                <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                    </div>
                                    <div>
                                        <input type="text" />
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                </div>

                                <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
                                
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                                        <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                                        <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='cpu_res_box mb-1' >
                                        <span>VirtIO-SCSI Multi Queues</span>
                                        <div id="cpu_sharing">
                                            <select id="multi_queue_status" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    
                            </div>
                        </div>

                        {/* 부트 옵션 */}
                        <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                                    <div className='cpu_res_box'>
                                        <span>첫 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">하드디스크</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>두 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="boot_checkboxs">
                                    <div>
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group mb-1.5'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>

                                <div className="cpu_res border-t border-gray-500 py-1">
                                    <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_path">커널 경로</label>
                                        <input type="text" id="kernel_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="initrd_path">initrd 경로</label>
                                        <input type="text" id="initrd_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_parameters">커널 매개 변수</label>
                                        <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                                    </div>


                                </div>
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>
            </Modal>
            {/* 가상머신(편집)팝업 */}
            <Modal
                 isOpen={activePopup === 'vm_edit'}
                onRequestClose={closePopup}
                contentLabel="가상머신 편집"
                className="edit_popup"
                overlayClassName="edit_popup_outer"
                shouldCloseOnOverlayClick={false}
            >
 <div id="edit_popup">
            <div className="popup_header">
              <h1>가상머신 편집</h1>
              <button onClick={closePopup}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            
            <div className="edit_body">
                <div className="edit_aside">
                    <div
                    className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
                    id="common_outer_btn"
                    onClick={() => handleSectionChange('common_outer')}
                    >
                    <span>일반</span>
                    </div>
                    <div
                    className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
                    id="system_outer_btn"
                    onClick={() => handleSectionChange('system_outer')}
                    >
                    <span>시스템</span>
                    </div>
                    <div
                    className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
                    id="host_outer_btn"
                    onClick={() => handleSectionChange('host_outer')}
                    >
                    <span>호스트</span>
                    </div>
                    <div
                    className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
                    id="ha_mode_outer_btn"
                    onClick={() => handleSectionChange('ha_mode_outer')}
                    >
                    <span>고가용성</span>
                    </div>
                    <div
                    className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
                    id="res_alloc_outer_btn"
                    onClick={() => handleSectionChange('res_alloc_outer')}
                    >
                    <span>리소스 할당</span>
                    </div>
                    <div
                    className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
                    id="boot_outer_btn"
                    onClick={() => handleSectionChange('boot_outer')}
                    >
                    <span>부트 옵션</span>
                    </div>
                </div>


                    <form action="#">
                        {/* 일반 */}
                        <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                            </div>
                            <div className="instance_image">
                                <span>인스턴스 이미지</span><br/>
                                <div>
                                    <div>on20-apm_Disk1_c1: (2 GB) 기존</div>
                                    <div className='flex'>
                                        <button className='mr-1'>편집</button>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>

                            <span className='edit_fourth_span'>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</span>
                            <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                               
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>

                        {/* 시스템 */}
                        <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                            
                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="memory_size">메모리 크기</label>
                                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">최대 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="total_cpu" value="1" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                        <span>고급 매개 변수</span>
                                    </div>
                                </div>
                                <div style={{ fontWeight: 600 }}>일반</div>
                                <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                    <div>
                                        <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <select id="time_offset">
                                        <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                    </select>
                                </div>
                            </div>
                        </div>
   
                        {/* 호스트 */}
                        <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                            <div id="host_second_content">
                                <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                <div className="form_checks">
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                        <label className="form-check-label" htmlFor="flexRadioDefault1">
                                            클러스터 내의 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                            <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                특정 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <select id="specific_host_select">
                                                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="host_checkboxs">
                                    <span>CPU 옵션:</span>
                                    <div className="host_checkbox">
                                        <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                        <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                   
                                </div>
                            </div>

                            <div id="host_third_content">
                                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                <div>
                                    <div>
                                        <span>마이그레이션 모드</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_mode">
                                        <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>마이그레이션 정책</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_policy">
                                        <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <div>
                                        <span>Parallel Migrations</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="parallel_migrations" readOnly>
                                        <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                    </select>
                                </div>
                               
                            </div>
                        </div>

                        {/* 고가용성 */}
                        <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                            <div id="ha_mode_second_content">
                                <div className="checkbox_group">
                                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                    <label className="check_label" htmlFor="ha_mode_box">
                                        고가용성
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <span>가상 머신 임대 대상 스토리지 도메인</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="no_lease" disabled>
                                        <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>재개 동작</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="force_shutdown">
                                        <option value="강제 종료">강제 종료</option>
                                    </select>
                                </div>
                                <div className="ha_mode_article">
                                    <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                    <div>
                                        <span>우선 순위</span>
                                        <select id="priority">
                                            <option value="낮음">낮음</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="ha_mode_article">
                                    <span>위치독</span>
                                    <div>
                                        <span>위치독 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>위치독 작업</span>
                                        <select id="watchdog_action" disabled>
                                            <option value="없음">없음</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* 리소스 할당 */}
                        <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                    <div className='cpu_res_box'>
                                        <span>CPU 프로파일</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU 공유</span>
                                        <div id="cpu_sharing">
                                            <select id="watchdog_action" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU Pinning Policy</span>
                                        <select id="watchdog_action">
                                            <option value="없음">None</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <div>
                                            <span>CPU 피닝 토폴로지</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" disabled />
                                    </div>
                                </div>

                                <span style={{ fontWeight: 600 }}>메모리 할당:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                                    </div>
                                
                                </div>

                                <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                    </div>
                                    <div>
                                        <input type="text" />
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                </div>

                                <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
                                
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                                        <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                                        <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='cpu_res_box mb-1' >
                                        <span>VirtIO-SCSI Multi Queues</span>
                                        <div id="cpu_sharing">
                                            <select id="multi_queue_status" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    
                            </div>
                        </div>

                        {/* 부트 옵션 */}
                        <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                                    <div className='cpu_res_box'>
                                        <span>첫 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">하드디스크</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>두 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="boot_checkboxs">
                                    <div>
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group mb-1.5'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>

                                <div className="cpu_res border-t border-gray-500 py-1">
                                    <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_path">커널 경로</label>
                                        <input type="text" id="kernel_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="initrd_path">initrd 경로</label>
                                        <input type="text" id="initrd_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_parameters">커널 매개 변수</label>
                                        <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                                    </div>


                                </div>
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
            </div>
            </Modal>

            {/* 논리네트워크(네트워크추가) 팝업 */}
            <Modal
                isOpen={activePopup === 'newNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_new_popup">
                    <div className="popup_header">
                        <h1 class="text-sm">새 논리적 네트워크</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className='flex'>
                        <div className="network_new_nav">
                            <div
                                id="network_new_common_btn"
                                className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClickModal('network_new_common_btn')}
                            >
                                일반
                            </div>
                            <div
                                id="network_new_cluster_btn"
                                className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClickModal('network_new_cluster_btn')}
                            >
                                클러스터
                            </div>
                            <div
                                id="network_new_vnic_btn"
                                className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClickModal('network_new_vnic_btn')}
                                style={{ borderRight: 'none' }}
                            >
                                vNIC 프로파일
                            </div>
                        </div>

                        {/* 일반 */}
                        {selectedTab === 'network_new_common_btn' && (
                            <form id="network_new_common_form">
                                <div className="network_first_contents">
                                    <div className="network_form_group">
                                        <label htmlFor="cluster">데이터 센터</label>
                                        <select id="cluster">
                                            <option value="default">Default</option>
                                        </select>
                                    </div>
                                    <div className="network_form_group">
                                        <div  className='checkbox_group'>
                                            <label htmlFor="name">이름</label>
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }}fixedWidth/>
                                        </div>
                                        <input type="text" id="name" />
                                    </div>
                                    <div className="network_form_group">
                                        <label htmlFor="description">설명</label>
                                        <input type="text" id="description" />
                                    </div>
                                    <div className="network_form_group">
                                        <label htmlFor="comment">코멘트</label>
                                        <input type="text" id="comment" />
                                    </div>
                                </div>

                                <div className="network_second_contents">
                                    <span>네트워크 매개변수</span>
                                    <div className="network_form_group">
                                        <label htmlFor="network_label">네트워크 레이블</label>
                                        <input type="text" id="network_label" />
                                    </div>
                                    <div className="network_checkbox_type1">
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                                            <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                                        </div>
                                        <input type="text" id="valn_tagging_input" disabled />
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="vm_network" name="vm_network" />
                                        <label htmlFor="vm_network">가상 머신 네트워크</label>
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="photo_separation" name="photo_separation" />
                                        <label htmlFor="photo_separation">포토 분리</label>
                                    </div>
                                    <div className="network_radio_group">
                                        <div style={{ marginTop: '0.2rem' }}>MTU</div>
                                        <div>
                                            <div className="radio_option">
                                                <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                                                <label htmlFor="default_mtu">기본값 (1500)</label>
                                            </div>
                                            <div className="radio_option">
                                                <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                                                <label htmlFor="user_defined_mtu">사용자 정의</label>
                                            </div>
                                        </div>
                                    
                                    </div>
                                    <div className="network_form_group">
                                        <label htmlFor="host_network_qos">호스트 네트워크 QoS</label>
                                        <select id="host_network_qos">
                                            <option value="default">[제한없음]</option>
                                        </select>
                                </div>
                                    <div className='popup_plus_btn'>
                                        <div className="popup_plus" onClick={() => setSecondModalOpen(true)}>새로만들기</div>
                                    </div>
                                    
                                        <Modal
                                            isOpen={secondModalOpen}
                                            onRequestClose={() => setSecondModalOpen(false)}
                                            contentLabel="추가 모달"
                                            className="SecondModal"
                                            overlayClassName="Overlay"
                                        >
                                                                
                                        <div className="plus_popup_outer">
                                            <div className="popup_header">
                                                <h1>새 호스트 네트워크 Qos</h1>
                                                <button  onClick={() => setSecondModalOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                                            </div>
                                            
                                            <div className='p-1' style={{ borderBottom: '1px solid #d3d3d3' }}>
                                                <div className="network_form_group">
                                                    <label htmlFor="network_provider">네트워크 공급자</label>
                                                    <select id="network_provider">
                                                    <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                                                    </select>
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="qos_name">QoS 이름</label>
                                                    <input type="text" id="qos_name" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="description">설명</label>
                                                    <input type="text" id="description" />
                                                </div>
                                                </div>

                                                <div className='p-1'>
                                                <span className="network_form_group font-bold">아웃바운드</span>
                                                <div className="network_form_group">
                                                    <label htmlFor="weighted_share">가중 공유</label>
                                                    <input type="text" id="weighted_share" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="speed_limit">속도 제한 [Mbps]</label>
                                                    <input type="text" id="speed_limit" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="commit_rate">커밋 속도 [Mbps]</label>
                                                    <input type="text" id="commit_rate" />
                                                </div>
                                            </div>


                                            <div className="edit_footer">
                                                <button style={{ display: 'none' }}></button>
                                                <button>가져오기</button>
                                                <button onClick={() => setSecondModalOpen(false)}>취소</button>
                                            </div>
                                        </div>
                                        
                                        </Modal>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="dns_settings" name="dns_settings" />
                                        <label htmlFor="dns_settings">DNS 설정</label>
                                    </div>
                                    <span>DB서버</span>
                                    <div className="network_checkbox_type3">
                                        <input type="text" id="name" disabled />
                                        <div>
                                            <button>+</button>
                                            <button>-</button>
                                        </div>
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation" />
                                        <label htmlFor="external_vendor_creation">외부 업체에서 작성</label>
                                    </div>
                                    <span>외부</span>
                                    <div className="network_form_group" style={{ paddingTop: 0 }}>
                                        <label htmlFor="external_provider">외부 공급자</label>
                                        <select id="external_provider">
                                            <option value="default">ovirt-provider-ovn</option>
                                        </select>
                                    </div>
                                    <div className="network_form_group">
                                        <label htmlFor="network_port_security">네트워크 포트 보안</label>
                                        <select id="network_port_security">
                                            <option value="default">활성화</option>
                                        </select>
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network" />
                                        <label htmlFor="connect_to_physical_network">물리적 네트워크에 연결</label>
                                    </div>
                                </div>
                            </form>
                        )}

                        {/* 클러스터 */}
                        {selectedTab === 'network_new_cluster_btn' && (
                            <form id="network_new_cluster_form">
                                <span>클러스터에서 네트워크를 연결/분리</span>
                                <div>
                                    <table className="network_new_cluster_table">
                                        <thead>
                                            <tr>
                                                <th>이름</th>
                                                <th>
                                                    <div className="checkbox_group">
                                                        <input type="checkbox" id="connect_all" />
                                                        <label htmlFor="connect_all"> 모두 연결</label>
                                                    </div>
                                                </th>
                                                <th>
                                                    <div className="checkbox_group">
                                                        <input type="checkbox" id="require_all" />
                                                        <label htmlFor="require_all"> 모두 필요</label>
                                                    </div>
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>Default</td>
                                                <td className="checkbox-group">
                                                    <div className="checkbox_group">
                                                        <input type="checkbox" id="connect_default" />
                                                        <label htmlFor="connect_default"> 연결</label>
                                                    </div>
                                                </td>
                                                <td className="checkbox-group">
                                                    <div className="checkbox_group">
                                                        <input type="checkbox" id="require_default" />
                                                        <label htmlFor="require_default"> 필수</label>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                        )}

                        {/* vNIC 프로파일 */}
                        {selectedTab === 'network_new_vnic_btn' && (
                            <form id="network_new_vnic_form">
                                <span>vNIC 프로파일</span>
                                <div>
                                    <input type="text" id="vnic_profile" />
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="public" disabled />
                                        <label htmlFor="public">공개</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                    <label htmlFor="qos">QoS</label>
                                    <select id="qos">
                                        <option value="none">제한 없음</option>
                                    </select>
                                    <div className="network_new_vnic_buttons">
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </form>
                        )}
                    </div>
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 선호도 그룹 모달 컴포넌트 */}
            <AffinityGroupModal isOpen={isAffinityGroupModalOpen} onRequestClose={closeAffinityGroupModal} />
            {/* 권한 모달 컴포넌트 */}
            <Permission isOpen={isPermissionModalOpen} onRequestClose={closePermissionModal} />
            {/*클러스터(네트워크 관리)팝업*/}
            <Modal
                isOpen={activePopup === 'cluster_network_popup'}
                onRequestClose={closePopup}
                contentLabel="네트워크 관리"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="manage_network_popup">
                <div className="popup_header">
                    <h1>네트워크 관리</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <TableOuter 
                    columns={TableColumnsInfo.CLUSTERS_POPUP} 
                    data={clusterPopupData} 
                    onRowClick={() => console.log('Row clicked')} 
                />
                
                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closePopup}>취소</button>
                </div>
                </div>
            </Modal>

            {/*삭제 팝업 */}
            <Modal
                isOpen={activePopup === 'delete'}
                onRequestClose={closePopup}
                contentLabel="디스크 업로드"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="storage_delete_popup">
                <div className="popup_header">
                    <h1>삭제</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                    <button onClick={closePopup}>취소</button>
                </div>
                </div>
            </Modal>
        </div>

    
    );
}

export default ClusterName;
