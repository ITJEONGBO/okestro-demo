import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import '../css/DataCenter.css';
import { useDataCenter } from '../../../api/RQHook';
import Path from '../../Header/Path';
import DataCenterClusters from './DataCenterClusters';
import DataCenterHosts from './DataCenterHosts';

const DataCenterInfo = () => {
  const { id: dataCenterId, section } = useParams();
  const {
    data: dataCenter,
    status: dataCenterStatus,
    isRefetching: isDataCenterRefetching,
    refetch: dataCenterRefetch,
    isError: isDataCenterError,
    error: dataCenterError,
    isLoading: isDataCenterLoading,
  } = useDataCenter(dataCenterId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('clusters');
  const [modals, setModals] = useState({ edit: false, delete: false });

  const sections = [
    { id: 'clusters', label: '클러스터' },
    { id: 'hosts', label: '호스트' },
    { id: 'vms', label: '가상머신' },
    { id: 'storageDomains', label: '스토리지' },
    { id: 'networks', label: '논리 네트워크' },
    { id: 'events', label: '이벤트' },
  ];

  useEffect(() => {
    if (!section) {
      setActiveTab('clusters');
    } else {
      setActiveTab(section);
    }
  }, [section]);

  const handleTabClick = (tab) => {
    const path = `/computing/datacenters/${dataCenterId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [dataCenter?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    clusters: DataCenterClusters,
    hosts: DataCenterHosts,
    // hosts, vms, storageDomains, networks, events 컴포넌트를 추가로 정의하여 사용할 수 있습니다.
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent datacenterId={dataCenterId} /> : null;
  };

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '데이터센터 편집', onClick: () => setModals({ ...modals, edit: true }) },
    { id: 'delete_btn', label: '삭제', onClick: () => setModals({ ...modals, delete: true }) },
  ];

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faLayerGroup}
        title={dataCenter?.name}
        buttons={sectionHeaderButtons}
      />
      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />
        <div className="host_btn_outer">
          <Path pathElements={pathData} />
          {renderSectionContent()}
        </div>
      </div>
      <Footer/>
    </div>
  );
};

export default DataCenterInfo;