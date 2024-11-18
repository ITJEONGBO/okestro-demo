import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faLayerGroup } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import '../css/Cluster.css';
import { useCluster } from '../../../api/RQHook';
import Path from '../../Header/Path';
import ClusterGenerals from './ClusterGenerals';
import ClusterHosts from './ClusterHosts';
import ClusterEvents from './ClusterEvents';
import ClusterVms from './ClusterVms';

const ClusterModal = React.lazy(() => import('../../Modal/ClusterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const ClusterInfo = () => {
  const { id: clusterId, section } = useParams();
  const {
    data: cluster,
    status: clusterStatus,
    isRefetching: isClusterRefetching,
    refetch: clusterRefetch,
    isError: isClusterError,
    error: clusterError,
    isLoading: isClusterLoading,
  } = useCluster(clusterId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({ edit: false, delete: false }); 

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'hosts', label: '호스트' },
    { id: 'vms', label: '가상머신' },
    { id: 'networks', label: '논리 네트워크' },
    { id: 'events', label: '이벤트' },
  ];

  useEffect(() => {
    if (!section) {
      setActiveTab('general');
    } else {
      setActiveTab(section);
    }
  }, [section]);

  const handleTabClick = (tab) => {
    const path = tab === 'general' ? `/computing/clusters/${clusterId}` : `/computing/clusters/${clusterId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [cluster?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    general: ClusterGenerals,
    hosts: ClusterHosts,
    // vms: ClusterVms,
    // storageDomains: DataCenterDomains,
    // networks: DataCenterNetworks,
    events: ClusterEvents
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent cId={clusterId} /> : null;
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
      label: '클러스터 편집',
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
        title={cluster?.name}
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
          <ClusterModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            cId={clusterId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='Cluster'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'클러스터'}
            data={cluster}
          />
        </Suspense>
      )}

      <Footer/>
    </div>
  );
};

export default ClusterInfo;