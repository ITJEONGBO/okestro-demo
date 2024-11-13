import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import '../css/DataCenter.css';
import { useDataCenter } from '../../../api/RQHook';
import Path from '../../Header/Path';
import DataCenterClusters from './DataCenterClusters';
import DataCenterHosts from './DataCenterHosts';
import DataCenterVms from './DataCenterVms';
import DataCenterDomains from './DataCenterDomains';
import DataCenterNetworks from './DataCenterNetwork';
import DataCenterEvents from './DataCenterEvents';

const DataCenterModal = React.lazy(() => import('../../Modal/DataCenterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

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
      label: '데이터센터 편집',
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
            type='Datacenter'
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