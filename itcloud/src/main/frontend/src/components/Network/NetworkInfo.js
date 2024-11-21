import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faFileEdit } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import './css/Network.css';
import { useNetwork } from '../../api/RQHook';
import Path from '../Header/Path';
import NetworkGenerals from './NetworkGenerals';

const NetworkModal = React.lazy(() => import('../Modal/NetworkModal'));
const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

const NetworkInfo = () => {
  const { id: networkId, section } = useParams();
  const {
    data: network,
    status: networkStatus,
    isRefetching: isNetworkRefetching,
    refetch: networkRefetch,
    isError: isNetworkError,
    error: networkError,
    isLoading: isNetworkLoading,
  } = useNetwork(networkId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({ edit: false, delete: false }); 

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vnicProfiles', label: 'vNic 프로파일' },
    { id: 'clusters', label: '클러스터' },
    { id: 'hosts', label: '호스트' },
    { id: 'vms', label: '가상머신' },
    { id: 'templates', label: '템플릿' },
  ];

  useEffect(() => {
    if (!section) {
      setActiveTab('general');
    } else {
      setActiveTab(section);
    }
  }, [section]);

  const handleTabClick = (tab) => {
    const path = tab === 'general' ? `/networks/${networkId}` : `/networks/${networkId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [network?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    general: NetworkGenerals,
    // vnicProfiles: NetworkVnicprofiles,
    // clusters: NetworkClusters,
    // hosts: NetworkHosts,
    // vms: NetworkVms,
    // templates: NetworksTemplates
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent nId={networkId} /> : null;
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
        titleIcon={faFileEdit}
        title={network?.name}
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
          <NetworkModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            nId={networkId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='Network'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'네트워크'}
            data={network}
          />
        </Suspense>
      )}

      <Footer/>
    </div>
  );
};

export default NetworkInfo;