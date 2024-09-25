import React, { useState } from 'react';
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
  faInfoCircle
} from '@fortawesome/free-solid-svg-icons'
import './css/DataCenterDetail.css';
import TableOuter from '../table/TableOuter';

// React Modal 설정
Modal.setAppElement('#root');

const DataCenterDetail = () => {
  const { name } = useParams();
  const navigate = useNavigate();
  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const [isModalOpen, setIsModalOpen] = useState({
    edit: false,
    permission: false,
  });

  const handleOpenModal = (type) => {
    setIsModalOpen((prev) => ({ ...prev, [type]: true }));
    // 모달 열 때 기본적으로 '일반' 탭을 선택
    setSelectedTab('cluster_common_btn'); 
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
  const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
  const [showTooltip, setShowTooltip] = useState(false); // hover하면 설명창 뜨게하기
  const handleTabClickModal = (tab) => {
    setSelectedTab(tab);
  };
  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick: () => handleOpenModal('edit') },
    { id: 'delete_btn', label: '삭제', onClick: () => {} },
  ];

  const sectionHeaderPopupItems = [
    '강제 제거',
    '가이드',
    '데이터 센터의 재초기화',
    '완료된 작업 정리'
  ];

  const [activeTab, setActiveTab] = useState('storage');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  // Nav 컴포넌트
  const sections = [
    { id: 'storage', label: '스토리지' },
    { id: 'logical_network', label: '논리 네트워크' },
    { id: 'cluster', label: '클러스터' },
    { id: 'Qos', label: 'Qos' },
    { id: 'permission', label: '권한' },
    { id: 'event', label: '이벤트' },
    { id: 'vm', label: '가상머신' },
    { id: 'host', label: '호스트' }
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

  const Qosdata = [
    {
      QosName: 'dd',  
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
        subtitle={name}
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
              <div className="host_empty_outer">
                <TableOuter
                  columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER}
                  data={clusterdata}
                  onRowClick={handleRowClick} 
                />
              </div>
            </>
          )}
          {activeTab === 'Qos' && (
            <>
              <div className="host_empty_outer">
                <TableOuter
                  columns={TableColumnsInfo.QOSS_FROM_DATACENTER} 
                  data={Qosdata}
                  onRowClick={handleRowClick} 
                />
              </div>
            </>
          )}
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
        {/* 호스트 새로 만들기 팝업 */}
        <Modal
            isOpen={isModalOpen.host_new}
            onRequestClose={handleCloseModal}
            contentLabel="새로 만들기"
            className="Modal"
            overlayClassName="Overlay"
            shouldCloseOnOverlayClick={false}
        >
            <div className="cluster_new_popup">
                <div className="popup_header">
                    <h1>새 클러스터</h1>
                    <button onClick={() =>handleCloseModal('host_new')}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>

                <div className="network_new_nav">
                    <div
                        id="cluster_common_btn"
                        className={selectedTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => handleTabClick('cluster_common_btn')}
                    >
                        일반
                    </div>
                    <div
                        id="cluster_migration_btn"
                        className={selectedTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => handleTabClick('cluster_migration_btn')}
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
                    <button onClick={handleCloseModal}>취소</button>
                </div>
            </div>
        </Modal>
      {/* Permission 모달 컴포넌트 */}
      <Permission isOpen={isModalOpen.permission} onRequestClose={() => handleCloseModal('permission')} />
    </div>
  );
};

export default DataCenterDetail;
