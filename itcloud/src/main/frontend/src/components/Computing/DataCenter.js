import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import { useParams, useNavigate } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Permission from '../Modal/Permission';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faUser, faCheck, faRefresh, faTimes,
  faInfoCircle,
  faBan,
  faArrowUp,
  faArrowDown,
  faMinus,
  faPlus
} from '@fortawesome/free-solid-svg-icons'
import './css/DataCenterDetail.css';
import TableOuter from '../table/TableOuter';
import { useDataCenter, useNetworkById } from '../../api/RQHook';

// React Modal 설정
Modal.setAppElement('#root');

const DataCenterDetail = () => {

  const { name } = useParams();
  const navigate = useNavigate();
  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const [showNetworkDetail, setShowNetworkDetail] = useState(false);
  const [활성화된섹션, set활성화된섹션] = useState('일반_섹션');
  const [isModalOpen, setIsModalOpen] = useState({
    edit: false,
    permission: false,
  });
  useEffect(() => {
    const 기본섹션 = document.getElementById('일반_섹션_btn');
    if (기본섹션) {
      기본섹션.style.backgroundColor = '#EDEDED';
      기본섹션.style.color = '#1eb8ff';
      기본섹션.style.borderBottom = '1px solid blue';
    }
  }, []);

  const 섹션변경 = (section) => {
    set활성화된섹션(section);
    const 모든섹션들 = document.querySelectorAll('.edit_aside > div');
    모든섹션들.forEach((el) => {
      el.style.backgroundColor = '#FAFAFA';
      el.style.color = 'black';
      el.style.borderBottom = 'none';
    });

    const 선택된섹션 = document.getElementById(`${section}_btn`);
    if (선택된섹션) {
      선택된섹션.style.backgroundColor = '#EDEDED';
      선택된섹션.style.color = '#1eb8ff';
      선택된섹션.style.borderBottom = '1px solid blue';
    }
  };
  const handleOpenModal = (type) => {
    setIsModalOpen((prev) => ({ ...prev, [type]: true }));
    setSelectedTab('cluster_common_btn');
  };
  const handleTabClick = (tab) => {
    setActiveTab(tab);
    setShowNetworkDetail(false);
};
const handleTabClickModal = (tab) => {
  setSelectedTab(tab); 
};
  const handleCloseModal = (type) => {
    setIsModalOpen((prev) => ({ ...prev, [type]: false }));
  };

  const handlePermissionFilterClick = (filter) => {
    setActivePermissionFilter(filter);
  };

  const [inputName, setInputName] = useState(name); // 데이터 센터 이름 관리 상태

  const handleInputChange = (event) => {
    setInputName(event.target.value); // input의 값을 상태로 업데이트
  };
  const [selectedTab, setSelectedTab] = useState('cluster_common_btn');
  const [showTooltip, setShowTooltip] = useState(false); // hover하면 설명창 뜨게하기

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick: () => handleOpenModal('edit') },
    { id: 'delete_btn', label: '삭제', onClick: () => {} },
  ];
  const [isHiddenParameterVisible, setHiddenParameterVisible] = useState(false);
  const toggleHiddenParameter = () => {
    setHiddenParameterVisible(!isHiddenParameterVisible);
  };
  
  const sectionHeaderPopupItems = [
    '강제 제거',
    '가이드',
    '데이터 센터의 재초기화',
    '완료된 작업 정리'
  ];

  const [activeTab, setActiveTab] = useState('general');


  //api
  const { id } = useParams(); // useParams로 URL에서 name을 가져옴
  const [shouldRefresh, setShouldRefresh] = useState(false);

  const { 
    data: dataCenter,
    status: dataCenterStatus,
    isRefetching: isDataCenterRefetching,
    refetch: dataCenterRefetch,
    isError: isDataCenterError,
    error: dataCenterError,
    isLoading: isDataCenterLoading,
  } = useDataCenter(id);
  
  const { 
    data: network,
    status: networkStatus,
    isRefetching: isNetworkRefetching,
    refetch: networkRefetch, 
    isError: isNetworkError,
    error: networkError, 
    isLoading: isNetworkLoaindg,
  } = useNetworkById(id);
  useEffect(() => {
    networkRefetch()
  }, [setShouldRefresh, networkRefetch])



  // Nav 컴포넌트
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'cluster', label: '클러스터' },
    { id: 'host', label: '호스트' },
    { id: 'vm', label: '가상머신' },
    { id: 'logical_network', label: '논리 네트워크' },
    { id: 'storage', label: '스토리지 도메인' },
    { id: 'permission', label: '권한' },
    { id: 'event', label: '이벤트' }


  ];

  // 테이블 컴포넌트 데이터
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

  const logicaldata = [
    {
      logicalName: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
        ovirtmgmt
        </span>
      ),
      description: 'Management Network'
    },
  ];

  const clusterdata = [
    {
      clusterName: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
        Default
        </span>
      ),
      version: '4.7',
      description: '',
    },
  ];


  const permissionData = [
    {
      icon: <FontAwesomeIcon icon={faUser} fixedWidth/>,
      user: 'ovirtmgmt',
      authProvider: '',
      namespace: '*',
      role: 'SuperUser',
      createdDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
    },
  ];

  const eventData = [
    {
      statusIcon: <FontAwesomeIcon icon={faCheck} style={{ color: 'green' }} fixedWidth/>,
      time: '2024. 7. 29. PM 3:31:41',
      message: 'Image Download with disk he_metadata was cancelled.',
      correlationId: '2568d791:c08...',
      source: 'oVirt',
      customEventId: '',
    },
  ];
  const vms = [
    {
      icon: <FontAwesomeIcon icon={faCheck} style={{ color: 'green' }} fixedWidth/>,
      name: 'Unknown',
      status: 'Unknown',
      upTime: '',
      cpu: 'oVirt',
      memory: '',
      network: '',             
      ipv4:''
    },
  ];
  const hosts = [
    {
      icon: <FontAwesomeIcon icon={faCheck} style={{ color: 'green' }} fixedWidth/>,
      name: 'Unknown',
      hostNameIP: 'Unknown',
      status: '',
      loading: 'oVirt',
      displayAddress: ''
    },
  ];



  const handleRowClick = (row, column) => {
    if (column.accessor === 'domainName') {
      navigate(`/storage-domain/${row.domainName.props.children}`);  
    }
    if (column.accessor === 'logicalName') {
      navigate(`/network/${row.logicalName.props.children}`); 
    }
    if (column.accessor === 'clusterName') {
      navigate(`/computing/cluster/${row.clusterName.props.children}`); 
    }
  };

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="데이터센터 "
        subtitle={dataCenter?.name}
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
      />
      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />

        <div className="empty_nav_outer">
        {activeTab === 'general' && (
            <>
             <div className="cluster_general">
                <div className="table_container_center">
                    <table className="table">
                        <tbody>
                            <tr>
                                <th>ID:</th>
                                <td>id</td>
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
            </>
          )}
          {activeTab === 'storage' && (
            <>
              <div className="content_header_right">
                <button>데이터 연결</button>
                <button>ISP 연결</button>
                <button>내보내기 연결</button>
                <button>분리</button>
                <button>활성</button>
                <button>유지보수</button>
              </div>
              <TableOuter 
                columns={TableColumnsInfo.STORAGES_FROM_DATACENTER} 
                data={storagedata}
                onRowClick={handleRowClick}
              />
            </>
          )}
          {activeTab === 'logical_network' && (
            <>
              <div className="content_header_right">
                <button>새로 만들기</button>
                <button>편집</button>
                <button>삭제</button>
              </div>
              <TableOuter 
                columns={TableColumnsInfo.LUN_SIMPLE}
                data={logicaldata}
                onRowClick={handleRowClick} 
              />
            </>
          )}
          {activeTab === 'cluster' && (
              <>
                <div className="content_header_right">
                  <button onClick={() => handleOpenModal('cluster_new')}>새로 만들기</button>
                  <button>편집</button>
                  <button>삭제</button>
                </div>
                <TableOuter
                  columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER}
                  data={clusterdata}
                  onRowClick={handleRowClick} 
                />
            </>
          
          )}
          {/* {activeTab === 'Qos' && (
            <>
              <div className="host_empty_outer">
                <TableOuter
                  columns={TableColumnsInfo.QOSS_FROM_DATACENTER} 
                  data={Qosdata}
                  onRowClick={handleRowClick} 
                />
              </div>
            </>
          )} */}
          {activeTab === 'permission' && (
            <>
            <div className="content_header_right">
              <button onClick={() => handleOpenModal('permission')}>추가</button> {/* 추가 버튼 */}
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
              data={activePermissionFilter === 'all' ? permissionData : []}
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
          {activeTab === 'vm' && (
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
          {activeTab === 'host' && (
            <>
            <div className="content_header_right">
                <button onClick={() => handleOpenModal('host_new')}>새로 만들기</button>
            </div>
              <TableOuter 
                columns={TableColumnsInfo.HOSTS_FROM_CLUSTER} 
                data={hosts}
                onRowClick={() => console.log('Row clicked')} 
              />
            
            </>
          )}
        </div>
        
      </div>
      <Footer/>

        {/* 데이터 센터 편집 모달 */}
        <Modal
          isOpen={isModalOpen.edit}
          onRequestClose={() => handleCloseModal('edit')}
          contentLabel="새로 만들기"
          className="Modal"
          overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
        >
          <div className="datacenter_new_popup">
            <div className="popup_header">
              <h1>새로운 데이터 센터</h1>
              <button onClick={() => handleCloseModal('edit')}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            </div>

            <div className="datacenter_new_content">
              <div>
                <label htmlFor="name1">이름</label>
                <input type="text" id="name1" value={inputName} onChange={handleInputChange}/>
              </div>
              <div>
                <label htmlFor="comment">설명</label>
                <input type="text" id="comment" />
              </div>
              <div>
                <label htmlFor="cluster">클러스터</label>
                <select id="cluster">
                  <option value="공유됨">공유됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="compatibility">호환버전</label>
                <select id="compatibility">
                  <option value="4.7">4.7</option>
                </select>
              </div>
              <div>
                <label htmlFor="quota_mode">쿼터 모드</label>
                <select id="quota_mode">
                  <option value="비활성화됨">비활성화됨</option>
                </select>
              </div>
              <div>
                <label htmlFor="comment">코멘트</label>
                <input type="text" id="comment" />
              </div>
            </div>

            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button onClick={() => handleCloseModal('edit')}>취소</button>
            </div>
          </div>
        </Modal>

         {/* 클러스터 새로 만들기 팝업 */}
         <Modal
            isOpen={isModalOpen.cluster_new}
            onRequestClose={handleCloseModal}
            contentLabel="새로 만들기"
            className="Modal"
            overlayClassName="Overlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="cluster_new_popup">
                <div className="popup_header">
                    <h1>새 클러스터</h1>
                    <button onClick={() =>handleCloseModal('cluster_new')}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="network_new_nav">
                <div
              id="cluster_common_btn"
              className={selectedTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => handleTabClickModal('cluster_common_btn')}
            >
              일반
            </div>
            <div
              id="cluster_migration_btn"
              className={selectedTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => handleTabClickModal('cluster_migration_btn')}
            >
              마이그레이션 정책
            </div>
                </div>

                {/* 일반 */}
                {selectedTab === 'cluster_common_btn' && (
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
                {selectedTab === 'cluster_migration_btn' && (
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
                    <button onClick={() =>handleCloseModal('cluster_new')}>취소</button>
                </div>
            </div>
        </Modal>
        {/* 호스트 새로 만들기 팝업 */}
        <Modal
        isOpen={isModalOpen.host_new}
        onRequestClose={handleCloseModal}
        contentLabel="새로 만들기"
        className="host_new_popup"
        overlayClassName="host_new_outer"
        shouldCloseOnOverlayClick={false}

      >
        <div className="popup_header">
          <h1>새 호스트</h1>
          <button onClick={() =>handleCloseModal('host_new')}>
            <FontAwesomeIcon icon={faTimes} fixedWidth/>
          </button>
        </div>

        <div className="edit_body">
          <div className="edit_aside">
            <div
              className={`edit_aside_item`}
              id="일반_섹션_btn"
              onClick={() => 섹션변경('일반_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>일반</span>
            </div>
            <div
              className={`edit_aside_item`}
              id="전원관리_섹션_btn"
              onClick={() => 섹션변경('전원관리_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>전원 관리</span>
            </div>
            <div
              className={`edit_aside_item`}
              id="호스트엔진_섹션_btn"
              onClick={() => 섹션변경('호스트엔진_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>호스트 엔진</span>
            </div>
            <div
              className={`edit_aside_item`}
              id="선호도_섹션_btn"
              onClick={() => 섹션변경('선호도_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>선호도</span>
            </div>
          </div>

          {/* 폼의 다양한 섹션들 */}
          <form action="#">
            {/* 공통 섹션 */}
            <div
              id="일반_섹션"
              style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
            >
          <div className="edit_first_content">
                  <div>
                      <label htmlFor="cluster">클러스터</label>
                      <select id="cluster">
                          <option value="default">Default</option>
                      </select>
                      <div>데이터센터 Default</div>
                  </div>
                  <div>
                      <label htmlFor="name1">이름</label>
                      <input type="text" id="name1" />
                  </div>
                  <div>
                      <label htmlFor="comment">코멘트</label>
                      <input type="text" id="comment" />
                  </div>
                  <div>
                      <label htmlFor="hostname">호스트이름/IP</label>
                      <input type="text" id="hostname" />
                  </div>
                  <div>
                      <label htmlFor="ssh_port">SSH 포트</label>
                      <input type="text" id="ssh_port" value="22" />
                  </div>
              </div>

    <div className='host_checkboxs'>
      <div className='host_checkbox'>
          <input type="checkbox" id="memory_balloon" name="memory_balloon" />
          <label htmlFor="headless_mode">헤드리스 모드</label>
      </div>
      <div className='host_checkbox'>
          <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
          <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
          <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
      </div>
    </div>

    <div className='host_checkboxs'>
      <div className='host_textbox'>
          <label htmlFor="user_name">사용자 이름</label>
          <input type="text" id="user_name" />
      </div>

      <div className='host_text_raido_box'>
          <div>
            <input type="radio" id="password" name="name_option" />
            <label htmlFor="password">암호</label>
          </div>
          <input type="text" id="radio1_name" />
      </div>

      <div className='host_radiobox'>
          <input type="radio" id="ssh_key" name="name_option" />
          <label htmlFor="ssh_key">SSH 공개키</label>
      </div>

    </div>

            </div>{/*일반섹션끝 */}

            {/* 전원 관리 섹션 */}
            <div
              id="전원관리_섹션"
              style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
            >
              
              <div className='host_checkboxs'>
                <div className='host_checkbox'>
                    <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                    <label htmlFor="enable_forwarding">전송 관리 활성</label>
                </div>
                <div className='host_checkbox'>
                    <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                    <label htmlFor="kdump_usage">Kdump 통합</label>
                </div>
                <div className='host_checkbox'>
                    <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                    <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
                </div>


                <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
              </div>
              
              
              <div className='addFence_agent'>
                <span>펜스 에이전트 추가</span>
                <button>+</button>
              </div>

              <div className='advanced_objec_add'>
                <button onClick={toggleHiddenParameter}>
                  {isHiddenParameterVisible ? '-' : '+'}
                </button>
                <span>고급 매개 변수</span>
                {isHiddenParameterVisible && (
                <div className='host_hidden_parameter'>
                 
                  <div>전원 관리 프록시 설정</div>
                  <div>
                    <div className='proxy_content'>
                      <div className='font-bold'>1.</div>
                      <div className='w-6'>cluster</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                    <div className='proxy_content'>
                      <div className='font-bold'>2.</div>
                      <div className='w-6'>dc</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                  </div>

                  <div className='proxy_add'>
                    <div>전원 관리 프록시 추가</div>
                    <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
                  </div>
                </div>
                )}
              </div>
              

            </div>

            {/* 호스트 엔진 섹션 */}
            <div
              id="호스트엔진_섹션"
              style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
            >
              <div className="host_policy">
                  <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
                  <select id="host_action">
                      <option value="none">없음</option>
                  </select>
              </div>


            </div>

            {/* 선호도 섹션 */}
            <div
              id="선호도_섹션"
              style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
            >
              <div className="preference_outer">
                <div className="preference_content">
                  <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
                    <div>
                      <select id="preference_group">
                        <option value="none"></option>
                      </select>
                      <button>추가</button>
                    </div>
                </div>
                <div className="preference_noncontent">
                  <div>선택된 선호도 그룹</div>
                  <div>선택된 선호도 그룹이 없습니다</div>
                </div>
                <div className="preference_content">
                  <label htmlFor="preference_label">선호도 레이블 선택</label>
                  <div>
                    <select id="preference_label">
                      <option value="none"></option>
                    </select>
                    <button>추가</button>
                  </div>
                </div>
                <div className="preference_noncontent">
                  <div>선택한 선호도 레이블</div>
                  <div>선호도 레이블이 선택되어있지 않습니다</div>
                </div>

              </div>
            </div>

          </form>
        </div>

        <div className="edit_footer">

          <button>OK</button>
          <button onClick={() =>handleCloseModal('host_new')}>취소</button>
        </div>
      </Modal>
      {/* Permission 모달 컴포넌트 */}
      <Permission isOpen={isModalOpen.permission} onRequestClose={() => handleCloseModal('permission')} />
    </div>
  );
};

export default DataCenterDetail;
