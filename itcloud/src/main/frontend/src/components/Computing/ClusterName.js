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
  faInfoCircle
} from '@fortawesome/free-solid-svg-icons'
import './css/ClusterName.css';
import TableOuter from '../table/TableOuter';

function ClusterName() {
    const { id } = useParams();
 
    const navigate = useNavigate();
    const location = useLocation();
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



    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        setShowNetworkDetail(false); // 탭이 변경되면 NetworkDetail 화면을 숨김
    };

    // HeaderButton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick:() => openPopup('cluster_detail_edit') },
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
    ];


    // nav 컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'logical_network', label: '논리 네트워크' },
        { id: 'host', label: '호스트' },
        { id: 'virtual_machine', label: '가상 머신' },
        // { id: 'affinity_group', label: '선호도 그룹' },
        // { id: 'affinity_label', label: '선호도 레이블' },
        { id: 'permission', label: '권한' },
        { id: 'event', label: '이벤트' }
    ];
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
                        title="클러스터"
                        subtitle={locationState?.name}
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
                                                        <FontAwesomeIcon icon={faBan} style={{ marginLeft: '13%', color: 'orange' }} fixedWidth/>
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
                            {/* 호스트 */}
                            {activeTab === 'host' && (
                                <>
                                <div className="host_empty_outer">
                                  <TableOuter 
                                    columns={TableColumnsInfo.HOSTS_FROM_CLUSTER} 
                                    data={hosts}
                                    onRowClick={() => console.log('Row clicked')} 
                                  />
                                </div>
                                </>
                            )}
                            {/* 가상 머신 */}
                            {activeTab === 'virtual_machine' && (
                              <>
                              <div className="host_empty_outer">
                                <TableOuter 
                                  columns={TableColumnsInfo.CLUSTER_VM} 
                                  data={vms} 
                                  onRowClick={() => console.log('Row clicked')}
                                />
                              </div>
                              </>
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

                   
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
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
        </div>

    
    );
}

export default ClusterName;
