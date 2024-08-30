import React, { useState } from 'react';
import Modal from 'react-modal';
import { useParams, useNavigate } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import './css/DataCenterDetail.css';

// React Modal 설정
Modal.setAppElement('#root');

const DataCenterDetail = () => {
  const { name } = useParams();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [inputName, setInputName] = useState(name); // 상태로 관리

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);


  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick: openModal},
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
  const handleInputChange = (event) => {
    setInputName(event.target.value); // input의 값을 상태로 업데이트
  };


  // Nav 컴포넌트
  const sections = [
    { id: 'storage', label: '스토리지' },
    { id: 'logical_network', label: '논리 네트워크' },
    { id: 'cluster', label: '클러스터' },
    { id: 'Qos', label: 'Qos' },
    { id: 'permission', label: '권한' },
    { id: 'event', label: '이벤트' }
  ];

  // 테이블 컴포넌트
  // 스토리지
  const storagedata = [
    {
      icon: '👑', // 이모티콘 추가
      icon2: '👑', // 이모티콘 추가
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

  // 논리네트워크
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

  //클러스터
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

  //Qos
  const Qosdata = [
    {
      QosName: 'dd',  
      version: '4.7',
      description: '',
    },
  ];

  //권한
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

  // 이벤트
  const eventData = [
    {
      statusIcon: <i className="fa fa-check" style={{ color: 'green' }}></i>,
      time: '2024. 7. 29. PM 3:31:41',
      message: 'Image Download with disk he_metadata was cancelled.',
      correlationId: '2568d791:c08...',
      source: 'oVirt',
      customEventId: '',
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
    
    console.log('Clicked on row:', row);
    console.log('Clicked on column:', column);
  };

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="데이터센터 "
        subtitle={name}
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
        openModal={openModal}
        togglePopup={() => {}}
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
              <div className="section_table_outer">
                <button>
                  <i className="fa fa-refresh"></i>
                </button>
                <Table columns={TableColumnsInfo.STORAGES_FROM_DATACENTER} data={storagedata} onRowClick={handleRowClick} />
              </div>
            </>
          )}
          {activeTab === 'logical_network' && (
                <>
                <div className="content_header_right">
                  <button>새로 만들기</button>
                  <button>편집</button>
                  <button>삭제</button>
                </div>
                <div className="section_table_outer">
                  <Table columns={TableColumnsInfo.LUN_SIMPLE} data={logicaldata} onRowClick={handleRowClick} />
                </div>
              </>
          )}
          {activeTab === 'cluster' && (
              <>
              <div className="host_empty_outer">
                <div className="section_table_outer">
                  <Table columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER} data={clusterdata} onRowClick={handleRowClick} />
                </div>
              </div>
            </>
        )}
          {activeTab === 'Qos' && (
              <>
              <div className="host_empty_outer">
                <div className="section_table_outer">
                  <Table columns={TableColumnsInfo.QOSS_FROM_DATACENTER} data={Qosdata} onRowClick={handleRowClick} />
                </div>
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
                <Table columns={TableColumnsInfo.PERMISSIONS} data={permissionData} onRowClick={() => console.log('Row clicked')} />
                </div>
              </div>
        </>
        )}
        {activeTab === 'event' && (
          <>
        <div className="host_empty_outer">
            <div className="section_table_outer">
                <Table columns={TableColumnsInfo.EVENTS} data={eventData} onRowClick={() => console.log('Row clicked')} />
            </div>
        </div>
        </>
        )}

          </div>
        
      </div>
        <Footer/>
        <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="datacenter_new_popup">
                    <div className="network_popup_header">
                        <h1>새로운 데이터 센터</h1>
                        <button onClick={closeModal}><i className="fa fa-times"></i></button>
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
                        <button onClick={closeModal}>취소</button>
                    </div>
                </div>
        </Modal>
    </div>
  );
};

export default DataCenterDetail;


