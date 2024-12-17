import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faServer } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import { useNetworkById } from '../../../api/RQHook';
import Path from '../../Header/Path';
import NetworkGeneral from './NetworkGeneral.js';
import NetworkVnicProfiles from './NetworkVnicProfiles.js';
// import NetworkHost from '../../zNotuse/NetworkHost.js'
import NetworkHosts from './NetworkHosts.js';

import NetworkVms from './NetworkVms.js'
import NetworkTemplates from './NetworkTemplates.js'
import NetworkClusters from './NetworkClusters.js';

const NetworkNewModal = React.lazy(() => import('../../Modal/NetworkNewModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const NetworkInfo = () => {
  const { id: networkId, section } = useParams();
  const {
    data: network,
    status: networkStatus,
    isRefetching: isnetworkRefetching,
    refetch: networkRefetch,
    isError: isnetworkError,
    error: networkError,
    isLoading: isnetworkLoading,
  } = useNetworkById(networkId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({
    edit: false,
    delete: false,
  });
  
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vnicProfiles', label: 'vNIC 프로파일' },
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

  const renderSectionContent = () => {
    const SectionComponent = {
      general: NetworkGeneral,
      vnicProfiles: NetworkVnicProfiles,
      clusters: NetworkClusters,
      // hosts: NetworkHost,
      hosts: NetworkHosts,
      vms: NetworkVms,
      templates: NetworkTemplates,
    }[activeTab];
    return SectionComponent ? <SectionComponent networkId={networkId} /> : null;
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev;
      return { ...prev, [type]: isOpen };
    });
  };

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick: () => toggleModal('edit', true),},
    { id: 'delete_btn', label: '삭제', onClick: () => toggleModal('delete', true), },
  ]

  const renderModals = () => (
    <>
      {modals.edit && (
        <NetworkNewModal
          isOpen={modals.edit}
          onRequestClose={() => toggleModal('edit', false)}
          editMode={true}
          networkId={networkId}
        />
      )}
      {modals.delete && (
        <DeleteModal
          isOpen={modals.delete}
          type="Network"
          onRequestClose={() => toggleModal('delete', false)}
          contentLabel="네트워크"
          data={network}
        />
      )}

    </>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faServer}
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

      <Suspense fallback={<div>Loading...</div>}>{renderModals()}</Suspense>
      <Footer/>
    </div>
  );
};

export default NetworkInfo;