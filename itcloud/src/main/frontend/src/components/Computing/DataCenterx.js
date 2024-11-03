import React, { useState,useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import TableColumnsInfo from '../table/TableColumnsInfo';
import {
  faLayerGroup,
} from '@fortawesome/free-solid-svg-icons'
import './css/DataCenter.css';
import TableOuter from '../table/TableOuter';
import { 
  useClustersFromDataCenter, 
  useDataCenter, 
} from '../../api/RQHook';
import Path from '../Header/Path';

const DataCenterx = () => {
  const { id,section,name } = useParams();
  const dataCenterId = id;      // dataCenterId로 설정
  const navigate = useNavigate();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState('clusters');
  
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  
    let basePath;
    if (location.pathname.startsWith('/computing')) {
      basePath = `/computing/datacenters/${id}`;
    } else if (location.pathname.startsWith('/networks')) {
      basePath = `/networks/datacenters/${id}`;
    } else if (location.pathname.startsWith('/storages')) {
      basePath = `/storages/datacenters/${id}`;
    }
  
    if (tab !== 'clusters') {
      navigate(`${basePath}/${tab}`);
    } else {
      navigate(basePath);
    }
  };
  
  
useEffect(() => {
  if (!section) {
    setActiveTab('clusters');
  } else {
    setActiveTab(section);
  }
}, [section]); 


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

  const [inputName, setInputName] = useState(name); // 데이터 센터 이름 관리 상태

  const handleInputChange = (event) => {
    setInputName(event.target.value); // input의 값을 상태로 업데이트
  };

  // 안씀
  const handleRowClick = (row, column) => {
    if (column.accessor === 'id') {
      navigate(`/networks/${row.id}`);  // row에서 id를 사용하여 경로로 이동
    }
  };

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '데이터센터 편집', onClick: () => handleOpenModal('edit') },
    { id: 'delete_btn', label: '삭제', onClick: () => handleOpenModal('delete') },
  ];

  // VmDu...버튼
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const togglePopup = () => {
      setIsPopupOpen(!isPopupOpen);
    };
 





  //api
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
  useEffect(() => {
    dataCenterRefetch()
  }, [setShouldRefresh, dataCenterRefetch]);


  // Nav 컴포넌트
  const sections = [
    { id: 'clusters', label: '클러스터' },
    { id: 'hosts', label: '호스트' },
    { id: 'vms', label: '가상머신' },
    { 
      id: 'storageDomains', 
      label: '스토리지', 
      isActive: activeTab === 'storageDomains' || activeTab === 'storage_disk' 
    },
    { id: 'networks', label: '논리 네트워크' },
    { id: 'events', label: '이벤트' },
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





  const pathData = [dataCenter?.name, sections.find(section => section.id === activeTab)?.label];
  const renderSectionContent = () => {
    switch (activeTab) {
      case 'clusters':
        return <DatacenterCluster dataCenter={dataCenter} />;
      case 'hosts':
        return <DatacenterHost dataCenter={dataCenter} />;
      case 'vms':
        return <DatacenterVm dataCenter={dataCenter} />;
      case 'storageDomains':
        return <DatacenterStorage dataCenter={dataCenter} />;
      case 'networks':
        return <DatacenterNetwork dataCenter={dataCenter} />;
      case 'events':
        return <DatacenterEvent dataCenter={dataCenter} />;
      default:
        return <DatacenterCluster dataCenter={dataCenter} />;
    }
  };
  return (
    <div className="content_detail_section">

      <HeaderButton
        titleIcon={faLayerGroup}
        title={dataCenter?.name}
        buttons={sectionHeaderButtons}
        popupItems={[]}
      />
      <div className="content_outer">
  
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />
        
        <div className="empty_nav_outer">
          <Path pathElements={pathData} />
          {renderSectionContent()}
          {/* {activeTab === 'clusters' && (
              <>
                <div className="header_right_btns">
                  <button onClick={() => handleOpenModal('cluster_new')}>새로 만들기</button>
                  <button onClick={() => handleOpenModal('cluster_new')}>편집</button>
                  <button onClick={() => handleOpenModal('delete')}>삭제</button>
                </div>
                <TableOuter
                  columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER}
                  data={clusters}
                  onRowClick={handleRowClick} 
                />
            </>
          
          )} */}

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


        </div>
        
      </div>
      <Footer/>

    </div>
  );
};

export default DataCentex;
