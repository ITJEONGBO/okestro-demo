import React, { useState, useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import Modal from 'react-modal';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import NetworkDetailGeneral from './NetworkDetailGeneral';
import './css/NetworkDetail.css';
import Permission from '../Modal/Permission';
import { useNetworkById, useAllVnicProfilesFromNetwork, useAllClustersFromNetwork, useAllHostsFromNetwork, useAllVmsFromNetwork, useAllTemplateFromNetwork, useAllPermissionFromNetwork } from '../../api/RQHook';

const NetworkDetail = ({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) => {
  // 테이블컴포넌트
  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const handlePermissionFilterClick = (filter) => {
    setActivePermissionFilter(filter);
  };

  const location = useLocation();
  const locationState = location.state  
  const { id } = useParams(); // useParams로 URL에서 name을 가져옴
  const [shouldRefresh, setShouldRefresh] = useState(false);

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
  
  const { 
    data: vnicProfiles,
    status: vnicProfilesStatus,
    isRefetching: isvnicProfilesRefetching,
    refetch: vnicProfilesRefetch,
    isError, 
    error, 
    isLoading
  } = useAllVnicProfilesFromNetwork(network?.id, toTableItemPredicateVnicProfiles)
  useEffect(() => {
    vnicProfilesRefetch()
  }, [setShouldRefresh, vnicProfilesRefetch])

  const { 
    data: clusters, 
    status: clustersStatus, 
    isLoading: isClustersLoading, 
    isError: isClustersError 
  } = useAllClustersFromNetwork(network?.id, toTableItemPredicateClusters);
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useAllHostsFromNetwork(network?.id, toTableItemPredicateHosts);  

  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useAllVmsFromNetwork(network?.id, toTableItemPredicateVms);
  const { 
    data: templates, 
    status: templatesStatus, 
    isLoading: isTemplatesLoading, 
    isError: isTemplatesError 
  } = useAllTemplateFromNetwork(network?.id, toTableItemPredicateTemplates);
  const { 
    data: permissions, 
    status: permissionsStatus, 
    isLoading: isPermissionsLoading, 
    isError: isPermissionsError 
  } = useAllPermissionFromNetwork(network?.id, toTableItemPredicatePermissions);


  // 함수들
  function toTableItemPredicateVnicProfiles(e) {
    return {
        id: e?.id ?? '없음',
        name: e?.name ?? '없음',
        description: (e?.description == '') ? '없음' : (e?.description ?? '없음'),
        network: e?.networkVo?.name ?? '없음',
        /*    
        networkId: e?.networkVo?.id ?? '없음',
        networkName: e?.networkVo?.name ?? '없음',
        */
        dataCenter: e?.dataCenterVo?.name ?? '없음',
        /*
        dataCenterId: e?.dataCenterVo?.id ?? '없음',
        dataCenterName: e?.dataCenterVo?.name ?? '없음',
        */
        networkFilter: e?.networkFilterVo?.name ?? '없음',
        /*
        networkFilterId: e?.networkFilterVo?.id ?? '없음',
        networkFilterName: e?.networkFilterVo?.name ?? '없음',
        */
        passthrough: e?.passThrough ?? '없음',
    }
  }
  function toTableItemPredicateClusters(cluster) {
    return {
      id: cluster?.id ?? '없음',
      name: cluster?.name ?? '없음',
      description: cluster?.description ?? '없음',
      version: cluster?.version ?? '없음',
      connectedNetwork: cluster?.connected ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkStatus: <i className="fa fa-chevron-left"></i>,
      requiredNetwork: cluster?.requiredNetwork ? <input type="checkbox" checked /> : <input type="checkbox" />,
      networkRole: cluster?.networkRole ?? '',
    };
  }

  function toTableItemPredicateHosts(host) {
    return {
      id: host?.id ?? '',               
      name: host?.name ?? 'Unknown',           
      cluster: host?.clusterVo?.name ?? 'N/A',
      dataCenter: host?.dataCenterVo?.name ?? 'N/A',
      networkDeviceStatus: host?.hostNicVos?.[0]?.status ?? 'Unknown', 
      networkDevice: host?.hostNicVos?.[0]?.name ?? 'N/A', 
      speed: host?.hostNicVos?.[0]?.speed ?? 'N/A', 
      rx: host?.hostNicVos?.[0]?.rxSpeed ?? 'N/A', 
      tx: host?.hostNicVos?.[0]?.txSpeed ?? 'N/A', 
      totalRx: host?.hostNicVos?.[0]?.rxTotalSpeed ?? 'N/A', 
      totalTx: host?.hostNicVos?.[0]?.txTotalSpeed ?? 'N/A', 
    };
  }
  function toTableItemPredicateVms(vm) {
    return {
      id: vm?.id ?? '없음',  // 가상 머신 ID
      icon: <i className="fa fa-chevron-left"></i>,  // 가상 머신 아이콘
      name: vm?.name ?? '없음',  // 가상 머신 이름
      cluster: vm?.cluster ?? '없음',  // 클러스터 이름
      ipAddress: vm?.ipAddress ?? '없음',  // IP 주소
      fqdn:  vm?.fqdn ?? '',
      vnicStatus: <i className="fa fa-chevron-left"></i>,  // vNIC 상태 아이콘
      vnic: vm?.vnic ?? '없음',  // vNIC 이름
      vnicRx: vm?.vnicRx ?? '없음',  // vNIC 수신 속도
      vnicTx: vm?.vnicTx ?? '없음',  // vNIC 송신 속도
      totalRx: vm?.totalRx ?? '없음',  // 총 수신 데이터
      totalTx: vm?.totalTx ?? '없음',  // 총 송신 데이터
      description: vm?.description ?? '없음'  // 가상 머신 설명
    };
  }
  function toTableItemPredicateTemplates(template) {
    return {
      name: template?.name ?? '없음',  // 템플릿 이름
      nicId: template?.nicId ?? '없음',  // 템플릿 버전
      status: template?.status ?? '없음',  // 템플릿 상태
      clusterName: template?.clusterName ?? '없음',  // 클러스터 이름
      nicName: template?.nicName ?? '없음',  // vNIC 이름
    };
  }
  function toTableItemPredicatePermissions(permission) {
    return {
      icon: <i className="fa fa-user"></i>,  // 사용자 아이콘
      user: permission?.user ?? '없음',  // 사용자 이름
      provider: permission?.provider ?? '없음',  // 인증 제공자
      nameSpace: permission?.nameSpace ?? '없음',  // 네임스페이스
      role: permission?.role ?? '없음',  // 역할
      createDate: permission?.createDate ?? '없음',  // 생성 날짜
      inheritedFrom: permission?.inheritedFrom ?? '없음',  // 상속된 위치
    };
  }
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
          <i class="fa fa-star" style={{ color: 'green'}}></i>
        </>
      ),
      networkOutput: <input type="checkbox" />,
      migrationNetwork: <input type="checkbox"/>,
      glusterNetwork: <input type="checkbox"/>,
      defaultRouting: <input type="checkbox"/>,
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
  ];
  const popupItems = ['Option 1', 'Option 2', 'Option 3'];
  
  // 모달 관련 상태 및 함수
  const [activePopup, setActivePopup] = useState(null);

  const openPopup = (popupType) => setActivePopup(popupType);
  const closePopup = () => setActivePopup(null);

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vNIC_profile', label: 'vNIC 프로파일' },
    { id: 'cluster', label: '클러스터' },
    { id: 'host', label: '호스트' },
    { id: 'virtual_machine', label: '가상 머신' },
    { id: 'template', label: '템플릿' },
    { id: 'permission', label: '권한' },
  ];

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="네트워크"
        subtitle={locationState?.name} // 여기서도 네트워크 이름을 표시
        buttons={buttons}
        popupItems={popupItems}
      />

      <div className="content_outer">
      <NavButton 
        sections={sections} 
        activeSection={activeTab} 
        handleSectionClick={handleTabClick}  
      />

      <div className="host_btn_outer">
        {
          activeTab === 'general' && <NetworkDetailGeneral network={network} />
        }
        {
          activeTab === 'vNIC_profile' && (
        <>
          <div className="content_header_right">
              <button onClick={() => openPopup('vnic_new_popup')}>새로 만들기</button>
              <button onClick={() => openPopup('vnic_eidt_popup')}>편집</button>
              <button>제거</button>
          </div>
          <div className="section_table_outer">
            {/* vNIC 프로파일 */}
            <Table
              columns={TableColumnsInfo.VNIC_PROFILES} 
              data={vnicProfiles}
              onRowClick={() => console.log('Row clicked')} 
            />
          </div>
       </>
        )}

        {activeTab === 'cluster' && (
        <>
            <div className="content_header_right">
                <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
            </div>
          
            <div className="section_table_outer">
              <Table 
                columns={TableColumnsInfo.CLUSTERS} 
                data={clusters} 
                onRowClick={() => console.log('Row clicked')}
              />
            </div>
       </>
       
        )}
        
        {activeTab === 'host' && (
        <>
            <div className="content_header_right">
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
            </div>
            <div className="host_filter_btns">
              <button
                 
              >
                  연결됨
              </button>
              <button
                  
              >
                  연결 해제
              </button>
            </div>
            <div className="section_table_outer">
              <Table columns={TableColumnsInfo.HOSTS} data={hosts} onRowClick={() => console.log('Row clicked')} />
            </div>
       </>
       
        )}

        {activeTab === 'virtual_machine' && (
        <>
              <div className="content_header_right">
                  <button>제거</button>
              </div>
              <div className="host_filter_btns">
                <button
              
                >
                    실행중
                </button>
                <button
                
                >
                    정지중
                </button>
            </div>
            <div className="section_table_outer">
              <Table columns={TableColumnsInfo.VMS} data={vms} onRowClick={() => console.log('Row clicked')} />
            </div>   
       </>
        )}

        {activeTab === 'template' && (
        <>
            <div className="content_header_right">
                <button>제거</button>
            </div>

            <div className="section_table_outer">
              <Table columns={TableColumnsInfo.TEMPLATES} data={templates} onRowClick={() => console.log('Row clicked')} />
            </div>
          
        </>
        )}

        {activeTab === 'permission' && (

        <>
              <div className="content_header_right">
                <button onClick={() => openPopup('permission')}>추가</button>
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

        </div>
      </div>

      {/*vNIC 프로파일(새로만들기)팝업 */}
      <Modal
        isOpen={activePopup === 'vnic_new_popup'}
        onRequestClose={closePopup}
        contentLabel="새로만들기"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
                </div>
              </div>

              <div className="vnic_new_checkbox">
                <input type="checkbox" id="allow_all_users" checked />
                <label htmlFor="allow_all_users">모든 사용자가 이 프로파일을 사용하도록 허용</label>
              </div>

            </div>
              
            
              
            
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
       {/*vNIC 프로파일(편집)팝업 */}
       <Modal
        isOpen={activePopup === 'vnic_eidt_popup'}
        onRequestClose={closePopup}
        contentLabel="편집"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>가상 머신 인터페이스 프로파일</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="vnic_new_content">
            
            <div className="vnic_new_contents" style={{ paddingTop: '0.4rem' }}>
              
              
              <div className="vnic_new_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center" disabled>
                  <option value="none">Default</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network">네트워크</label>
                <select id="network" disabled>
                  <option value="none">ovirtmgmt</option>
                </select>
              </div>
              <div className="vnic_new_box">
                <span>이름</span>
                <input type="text" id="name" disabled />
              </div>
              <div className="vnic_new_box">
                <span>설명</span>
                <input type="text" id="description" disabled />
              </div>
              <div className="vnic_new_box">
                <label htmlFor="network_filter">네트워크 필터</label>
                <select id="network_filter">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="passthrough" />
                <label htmlFor="passthrough">통과</label>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="migratable" disabled checked />
                <label htmlFor="migratable">마이그레이션 가능</label>
              </div>
              <div className="vnic_new_box">
                <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
                <select id="failover_vnic_profile">
                  <option value="none">없음</option>
                </select>
              </div>
              <div className="vnic_new_checkbox">
                <input type="checkbox" id="port_mirroring" />
                <label htmlFor="port_mirroring">포트 미러링</label>
              </div>
              
              <div className="vnic_new_inputs">
                <span>사용자 정의 속성</span>
                <div className="vnic_new_buttons">
                  <select id="custom_property_key">
                    <option value="none">키를 선택하십시오</option>
                  </select>
                  <div>
                    <div>+</div>
                    <div>-</div>
                  </div>
                </div>
              </div>


            </div>
              
            
              
            
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
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
          <div className="network_popup_header">
            <h1>네트워크 관리</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="section_table_outer">
            <Table 
              columns={TableColumnsInfo.CLUSTERS_POPUP} 
              data={clusterPopupData} 
              onRowClick={() => console.log('Row clicked')} 
            />
          </div>
          
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      {/*호스트(호스트 네트워크 설정)*/}
      <Modal
        isOpen={activePopup === 'host_network_popup'}
        onRequestClose={closePopup}
        contentLabel="호스트 네트워크 설정"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>호스트 host01.ititinfo.com 네트워크 설정</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="host_network_contents_outer">
            <div className="host_network_top">
              dd
            </div>
            <div className="host_network_bottom">

            </div>
          </div>
          


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      <Footer/>

       {/* 모달 컴포넌트 */}
       <Permission isOpen={activePopup === 'permission'} onRequestClose={closePopup} />
    </div>
  );
}

export default NetworkDetail;
