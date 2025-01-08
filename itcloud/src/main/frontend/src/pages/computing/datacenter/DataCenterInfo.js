import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../../components/navigation/NavButton';
import HeaderButton from '../../../components/button/HeaderButton';
import Footer from '../../../components/footer/Footer';
import '../datacenter/css/DataCenter.css';

import { useDataCenter } from '../../../api/RQHook';
import Path from '../../../components/Header/Path';
import DataCenterClusters from './DataCenterClusters';
import DataCenterHosts from './DataCenterHosts';
import DataCenterVms from './DataCenterVms';
import DataCenterDomains from './DataCenterDomains';
import DataCenterNetworks from './DataCenterNetworks';
import DataCenterEvents from './DataCenterEvents';

const DataCenterModal = React.lazy(() => import('./modal/DataCenterModal'));
const DeleteModal = React.lazy(() => import('../../../components/DeleteModal'));

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
  } = useDataCenter(dataCenterId, (e) => ({...e,}));

  const navigate = useNavigate();
  const location = useLocation();
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
    // 현재 경로에서 섹션 추출: computing, storages, networks 중 하나
    const section = location.pathname.split('/')[1]; // 첫 번째 세그먼트가 섹션 정보
  
    // 섹션이 유효한 값인지 확인 (예외 처리 포함)
    const validSections = ['computing', 'storages', 'networks'];
    const currentSection = validSections.includes(section) ? section : 'computing'; // 기본값을 'computing'으로 설정
  
    // 동적 경로 생성 및 이동
    const path = `/${currentSection}/datacenters/${dataCenterId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [dataCenter?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    clusters: DataCenterClusters,
    hosts: DataCenterHosts,
    vms: DataCenterVms,
    storageDomains: DataCenterDomains,
    networks: DataCenterNetworks,
    events: DataCenterEvents
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent datacenterId={dataCenterId} /> : null;
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev;
      return { ...prev, [type]: isOpen };
    });
  };

  const sectionHeaderButtons = [
    {
      id: 'edit_btn',
      label: '편집',
      onClick: () => toggleModal('edit', true),
    },
    {
      id: 'delete_btn',
      label: '삭제',
      onClick: () => toggleModal('delete', true),
    },
  ]

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

      {modals.edit && (
        <Suspense fallback={<div>Loading...</div>}>
          <DataCenterModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            dcId={dataCenterId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='DataCenter'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'데이터센터'}
            data={dataCenter}
          />
        </Suspense>
      )}

      <Footer/>
    </div>
  );
};

export default DataCenterInfo;