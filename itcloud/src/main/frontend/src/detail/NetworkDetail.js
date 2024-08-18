import React, { useState } from 'react';
import Modal from 'react-modal';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';
import { Table } from '../components/table/Table';
import './css/NetworkDetail.css';
import { useParams } from 'react-router-dom';

function NetworkDetail({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  // 테이블컴포넌트
  const { name } = useParams(); // useParams로 URL에서 name을 가져옴
  //vnic
  const vnicColumns = [
    { header: '이름', accessor: 'name', clickable: false },
    { header: '네트워크', accessor: 'network', clickable: false },
    { header: '데이터 센터', accessor: 'dataCenter', clickable: false },
    { header: '호환 버전', accessor: 'compatVersion', clickable: false },
    { header: 'QoS 이름', accessor: 'qosName', clickable: false },
    { header: '네트워크 필터', accessor: 'networkFilter', clickable: false },
    { header: '포트 미러링', accessor: 'portMirroring', clickable: false },
    { header: '통과', accessor: 'passthrough', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
  ];

  const vnicData = [
    {
      name: name,
      network: 'ovirtmgmt',
      dataCenter: 'Default',
      compatVersion: '4.7',
      qosName: '',
      networkFilter: 'wdsm-no-mac-spoofing',
      portMirroring: '',
      passthrough: '아니요',
      description: ''
    },
  ];
  //클러스터
  const clusterColumns = [
    { header: '이름', accessor: 'name', clickable: false },
    { header: '호환 버전', accessor: 'compatVersion', clickable: false },
    { header: '연결된 네트워크', accessor: 'connectedNetwork', clickable: false },
    { header: '네트워크 상태', accessor: 'networkStatus', clickable: false },
    { header: '필수 네트워크', accessor: 'requiredNetwork', clickable: false },
    { header: '네트워크 역할', accessor: 'networkRole', clickable: false, style: { textAlign: 'center' } },
    { header: '설명', accessor: 'description', clickable: false },
  ];

  const clusterData = [
    {
      name: 'Default',
      compatVersion: '4.7',
      connectedNetwork: <i className="fa fa-chevron-left"></i>,
      networkStatus: <i className="fa fa-chevron-left"></i>,
      requiredNetwork: <i className="fa fa-chevron-left"></i>,
      networkRole: (
        <>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
        </>
      ),
      description: 'The default server cluster',
    },
  ];
  // 클러스터 팝업
  const clusterPopupColumns = [
    { header: '이름', accessor: 'name', clickable: false },
    { header: '호환 버전', accessor: 'compatVersion', clickable: false },
    { header: '연결된 네트워크', accessor: 'connectedNetwork', clickable: false },
    { header: '네트워크 상태', accessor: 'networkStatus', clickable: false },
    { header: '필수 네트워크', accessor: 'requiredNetwork', clickable: false },
    { header: '네트워크 역할', accessor: 'networkRole', clickable: false, style: { textAlign: 'center' } },
    { header: '설명', accessor: 'description', clickable: false },
  ];

  const clusterPopupData = [
    {
      name: 'Default',
      compatVersion: '4.7',
      connectedNetwork: <i className="fa fa-chevron-left"></i>,
      networkStatus: <i className="fa fa-chevron-left"></i>,
      requiredNetwork: <i className="fa fa-chevron-left"></i>,
      networkRole: (
        <>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
          <i className="fa fa-chevron-left"></i>
        </>
      ),
      description: 'The default server cluster',
    },
  ];
  // 호스트
  const hostColumns = [
    { header: '', accessor: 'icon', clickable: false },
    { header: '이름', accessor: 'name', clickable: false },
    { header: '클러스터', accessor: 'cluster', clickable: false },
    { header: '데이터 센터', accessor: 'dataCenter', clickable: false },
    { header: '네트워크 장치 상태', accessor: 'networkDeviceStatus', clickable: false },
    { header: '비동기', accessor: 'async', clickable: false },
    { header: '네트워크 장치', accessor: 'networkDevice', clickable: false },
    { header: '속도', accessor: 'speed', clickable: false },
    { header: 'Rx', accessor: 'rx', clickable: false },
    { header: 'Tx', accessor: 'tx', clickable: false },
    { header: '총 Rx', accessor: 'totalRx', clickable: false },
    { header: '총 Tx', accessor: 'totalTx', clickable: false },
  ];

  const hostData = [
    {
      icon: <i className="fa fa-chevron-left"></i>,
      name: 'host01.ititinfo.com',
      cluster: 'Default',
      dataCenter: 'Default',
      networkDeviceStatus: <i className="fa fa-chevron-left"></i>,
      async: '',
      networkDevice: 'ens192',
      speed: '10000',
      rx: '118',
      tx: '1',
      totalRx: '25,353,174,284,042',
      totalTx: '77,967,054,294',
    },
  ];
  //가상머신
  const vmColumns = [
    { header: '', accessor: 'icon', clickable: false, style: { textAlign: 'center' } },
    { header: '이름', accessor: 'name', clickable: false },
    { header: '클러스터', accessor: 'cluster', clickable: false },
    { header: 'IP 주소', accessor: 'ipAddress', clickable: false },
    { header: 'vNIC 상태', accessor: 'vnicStatus', clickable: false },
    { header: 'vNIC', accessor: 'vnic', clickable: false },
    { header: 'vNIC Rx', accessor: 'vnicRx', clickable: false },
    { header: 'vNIC Tx', accessor: 'vnicTx', clickable: false },
    { header: '총 Rx', accessor: 'totalRx', clickable: false },
    { header: '총 Tx', accessor: 'totalTx', clickable: false },
    { header: '설명', accessor: 'description', clickable: false }
   
  ];

  const vmData = [
    {
      icon: <i className="fa fa-chevron-left"></i>,
      name: 'HostedEngine',
      cluster: 'Default',
      ipAddress: '192.168.0.08 fe80::2342',
      vnicStatus: <i className="fa fa-chevron-left"></i>,
      vnic: 'vnet0',
      vnicRx: '1',
      vnicTx: '1',
      totalRx: '5,353,174,284',
      totalTx: '5,353,174,284',
      description: 'Hosted engine VM'
  
    },
  ];
  //템플릿
  const templateColumns = [
    { header: '이름', accessor: 'name', clickable: false },
    { header: '버전', accessor: 'version', clickable: false },
    { header: '상태', accessor: 'status', clickable: false },
    { header: '클러스터', accessor: 'cluster', clickable: false },
    { header: 'vNIC', accessor: 'vnic', clickable: false },
  ];

  const templateData = [
    {
      name: 'test02',
      version: '1',
      status: 'OK',
      cluster: 'Default',
      vnic: 'nic1',
    },
  ];
  //권한
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

   const openPopup = (popupType) => {
     setActivePopup(popupType);
   };
 
   const closePopup = () => {
     setActivePopup(null);
   };
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
      subtitle={name} // 여기서도 네트워크 이름을 표시
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

        {activeTab === 'vNIC_profile' && (
        <>

                <div className="content_header_right">
                    <button onClick={() => openPopup('vnic_new_popup')}>새로 만들기</button>
                    <button onClick={() => openPopup('vnic_eidt_popup')}>편집</button>
                    <button>제거</button>
                </div>
              

                <div className="section_table_outer">
                  <Table columns={vnicColumns} data={vnicData} onRowClick={() => console.log('Row clicked')} />
                </div>

       </>
       
        )}

        {activeTab === 'cluster' && (
        <>

            <div className="content_header_right">
                <button onClick={() => openPopup('cluster_network_popup')}>네트워크 관리</button>
            </div>
          
            <div className="section_table_outer">
              <Table columns={clusterColumns} data={clusterData} onRowClick={() => console.log('Row clicked')} />
            </div>

          
       </>
       
        )}
        
        {activeTab === 'host' && (
        <>
            <div className="content_header_right">
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
                </div>
                <div className="application_content_header">
                  <button>연결됨</button>
                  <button>연결 해제</button>
                </div>
                
            
            <div className="section_table_outer">
              <Table columns={hostColumns} data={hostData} onRowClick={() => console.log('Row clicked')} />
            </div>
       </>
       
        )}

        {activeTab === 'virtual_machine' && (
        <>
              <div className="content_header_right">
                  <button>제거</button>
              </div>
              <div className="application_content_header">
                  <button>실행중</button>
                  <button>정지중</button>
                </div>
             
            <div className="section_table_outer">
              <Table columns={vmColumns} data={vmData} onRowClick={() => console.log('Row clicked')} />
            </div>
            
       </>
       
        )}

        {activeTab === 'template' && (
        <>
           
            <div className="content_header_right">
                <button>제거</button>
            </div>
            

            <div className="section_table_outer">
              <Table columns={templateColumns} data={templateData} onRowClick={() => console.log('Row clicked')} />
            </div>
          
        </>
        )}

        {activeTab === 'permission' && (
        <>
        
              <div className="content_header_right">
                <button>추가</button>
                <button>제거</button>
              </div>
              
              <div className="section_table_outer">
                <div className="storage_right_btns">
                  <span>Permission Filters:</span>
                  <div>
                    <button>All</button>
                    <button>Direct</button>
                  </div>
                </div>
              
              <div className="section_table_outer">
                <Table columns={permissionColumns} data={permissionData} onRowClick={() => console.log('Row clicked')} />
              </div>
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
       {/*클러스터(네트워크 관리)팝업 -> 수정필요*/}
       <Modal
        isOpen={activePopup === 'cluster_network_popup'}
        onRequestClose={closePopup}
        contentLabel="네트워크 관리"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vnic_new_content_popup">
          <div className="network_popup_header">
            <h1>네트워크 관리</h1>
            <button onClick={closePopup}><i className="fa fa-times"></i></button>
          </div>
          
          <div className="table_outer2">
            <Table columns={clusterPopupColumns} data={clusterPopupData} onRowClick={() => console.log('Row clicked')} />
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
    </div>
  );
}

export default NetworkDetail;
