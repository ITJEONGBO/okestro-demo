import React, { useState,useEffect } from 'react';
import {useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Modal from 'react-modal';
import TableColumnsInfo from '../table/TableColumnsInfo';
import NetworkDetail from '../Network/NetworkDetail';
import { useClusterById, useEventFromCluster, useHostFromCluster, useLogicalFromCluster, usePermissionFromCluster, useVMFromCluster } from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faCrown, faUser,
  faTimes,
  faInfoCircle,
  faExclamationTriangle,
  faEarthAmericas,
} from '@fortawesome/free-solid-svg-icons'
import './css/ClusterName.css';
import './vm/DiskSection.js';
import TableOuter from '../table/TableOuter';
import Path from '../Header/Path';
import HostDu from '../duplication/HostDu.js';
import VmDu from '../duplication/VmDu.js';
import EventDu from '../duplication/EventDu.js';

function ClusterName() {
    const { id , section} = useParams();
    const [activeTab, setActiveTab] = useState('general');
    const navigate = useNavigate();
    const location = useLocation();

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        if (tab !== 'general') {
          navigate(`/computing/clusters/${id}/${tab}`); 
        } else {
          navigate(`/computing/clusters/${id}`); 
        }
      };
      useEffect(() => {
        if (!section) {
          setActiveTab('general'); 
        } else {
          setActiveTab(section);
        }
      }, [section]);

    const locationState = location.state; 
    const [shouldRefresh, setShouldRefresh] = useState(false);
    const [showNetworkDetail, setShowNetworkDetail] = useState(false);
    const [activePopup, setActivePopup] = useState(null);
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
    const [secondModalOpen, setSecondModalOpen] = useState(false); // 추가 모달 상태


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
    // const [isPermissionModalOpen, setIsPermissionModalOpen] = useState(false); // 권한 모달 상태
    // const [isAffinityGroupModalOpen, setIsAffinityGroupModalOpen] = useState(false); // 선호도 그룹 모달 상태

    // // 권한 모달 핸들러
    // const openPermissionModal = () => setIsPermissionModalOpen(true);
    // const closePermissionModal = () => setIsPermissionModalOpen(false);
    // // 기존의 openPopup 함수 수정

    // // 선호도 그룹 모달 핸들러
    // const openAffinityGroupModal = () => setIsAffinityGroupModalOpen(true);
    // const closeAffinityGroupModal = () => setIsAffinityGroupModalOpen(false);
    // const [showTooltip, setShowTooltip] = useState(false); // hover하면 설명창 뜨게하기

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
        { id: 'edit_btn', label: '클러스터 편집', onClick:() => openPopup('cluster_detail_edit') },
        { id: 'delete_btn', label: '삭제', onClick: () => openPopup('delete')},
    ];


    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'host', label: '호스트' },     
        { id: 'virtual_machine', label: '가상 머신' },
        { id: 'logical_network', label: '논리 네트워크' },
        { id: 'event', label: '이벤트' }
        //{ id: 'storage', label: '스토리지' },
        // { id: 'affinity_group', label: '선호도 그룹' },
        // { id: 'affinity_label', label: '선호도 레이블' },
        //{ id: 'permission', label: '권한' },
        // { id: 'disk', label: '디스크' }
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
                            <Path pathElements={pathData}/>
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
                            {/* 논리 네트워크 */}
                            {activeTab === 'logical_network' && (
                                <>
                                <div className="header_right_btns">
                                    <button onClick={() => openPopup('newNetwork')}>새로 만들기</button>
                                    <button onClick={() => openPopup('editNetwork')}>편집</button>
                                    <button onClick={() => openPopup('delete')}> 삭제</button>
                                </div>
                                <TableOuter
                                  columns={TableColumnsInfo.LUNS} 
                                  data={networks} 
                                  onRowClick={handleRowClick} />
                                </>

                            )}
                            {/* 이벤트 */}
                            {activeTab === 'event' && (
                                <EventDu 
                                    columns={TableColumnsInfo.EVENTS}
                                    data={events}
                                    handleRowClick={() => console.log('Row clicked')}
                                />
                            )}


                            {/* 템플릿(삭제예정) */}
                            {/* {activeTab === 'template' && (
                                <TemplateDu 
                                data={hosts} 
                                columns={TableColumnsInfo.TEMPLATE_CHART} 
                                handleRowClick={handleRowClick}
                            />
                            )} */}
                            {/* 스토리지(삭제예정) */}
                            {/* {activeTab === 'storage' && (
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
                            )} */}
                            {/* 디스크(삭제예정) */}
                            {/* {activeTab === 'disk' && (
                                <DiskSection/>
                            )} */}
                            {/* 선호도 그룹/ 선호도 레이블(삭제예정) */}
                            {/* {activeTab === 'affinity_group' && (
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
                            {/* 권한(삭제예정) */}
                            {/* {activeTab === 'permission' && (
                                <>
                                <div className="header_right_btns">
                                <button onClick={openPermissionModal}>추가</button>
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
                            )} */}
                            
                        </div>
                    </div>
                </>
            )}

       
            {/* 클러스터 편집 팝업*/}
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
                        <button onClick={() =>closePopup('cluster_new')}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

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

                        <div>
                        <div className='font-bold px-1.5 py-0.5'>복구 정책<FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/></div>
                        <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="migration_option" name="recovery_policy" />
                            <label htmlFor="migration_option">가상 머신을 마이그레이션함</label>
                        </div>

                        <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="high_usage_migration_option" name="recovery_policy" />
                            <label htmlFor="high_usage_migration_option">고가용성 가상 머신만 마이그레이션</label>
                        </div>

                        <div className='host_text_radio_box px-1.5 py-0.5'>
                            <input type="radio" id="no_migration_option" name="recovery_policy" />
                            <label htmlFor="no_migration_option">가상 머신은 마이그레이션 하지 않음</label>
                        </div>
                        </div>
        
                    </form>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={() =>closePopup('cluster_new')}>취소</button>
                    </div>
                </div>
            </Modal>
         

            {/* 논리네트워크 새로 만들기*/}
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
                        <h1>새 논리적 네트워크</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className='flex'>
                        {/* <div className="network_new_nav">
                            <div
                                id="network_new_common_btn"
                                className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_common_btn')}
                            >
                                일반
                            </div>
                            <div
                                id="network_new_cluster_btn"
                                className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_cluster_btn')}
                            >
                                클러스터
                            </div>
                            <div
                                id="network_new_vnic_btn"
                                className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
                                onClick={() => handleTabClick('network_new_vnic_btn')}
                                style={{ borderRight: 'none' }}
                            >
                                vNIC 프로파일
                            </div>
                        </div> */}

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
                                 
                               
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="dns_settings" name="dns_settings" />
                                        <label htmlFor="dns_settings">DNS 설정</label>
                                    </div>
                                    <span>DNS서버</span>
                                    <div className="network_checkbox_type3">
                                        <input type="text" id="name" disabled />
                                        <div>
                                            <button>+</button>
                                            <button>-</button>
                                        </div>
                                    </div>
                                   
                                </div>
                                <div id="network_new_cluster_form">
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
            {/* 논리네트워크 편집 */}
            <Modal
                isOpen={activePopup === 'editNetwork'}
                onRequestClose={closePopup}
                contentLabel="편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_edit_popup">
                    <div className="popup_header">
                        <h1>논리 네트워크 수정</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>
                    
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
                               
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DNS서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                              
                            </div>
                        </form>
                   

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>
            {/* 선호도 그룹 모달 컴포넌트 */}
            {/* <AffinityGroupModal isOpen={isAffinityGroupModalOpen} onRequestClose={closeAffinityGroupModal} /> */}
            {/* 권한 모달 컴포넌트 */}
            {/* <Permission isOpen={isPermissionModalOpen} onRequestClose={closePermissionModal} /> */}

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
