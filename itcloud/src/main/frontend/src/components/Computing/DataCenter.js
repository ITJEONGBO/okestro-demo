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
  faUser, faCheck, faRefresh, faTimes
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
          <div className="network_popup_header">
            <h1 class="text-sm">새로운 데이터 센터</h1>
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

      {/* Permission 모달 컴포넌트 */}
      <Permission isOpen={isModalOpen.permission} onRequestClose={() => handleCloseModal('permission')} />
    </div>
  );
};

export default DataCenterDetail;
