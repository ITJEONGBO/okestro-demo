import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import '../css/Host.css';
import { useHost } from '../../../api/RQHook';
import Path from '../../Header/Path';
import HostGenerals from './HostGenerals';
import HostVms from './HostVms'
import HostNics from './HostNics'
import HostDevices from './HostDevices';
import HostEvents from './HostEvents'

const HostModal = React.lazy(() => import('../../Modal/HostModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const HostInfo = () => {
  const { id: hostId, section } = useParams();
  const {
    data: host,
    status: hostStatus,
    isRefetching: isHostRefetching,
    refetch: hostRefetch,
    isError: isHostError,
    error: hostError,
    isLoading: isHostLoading,
  } = useHost(hostId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({ edit: false, delete: false }); 

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vms', label: '가상머신' },
    { id: 'nics', label: '네트워크 인터페이스' },
    { id: 'devices', label: '호스트 장치' },
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
    const path = tab === 'general' ? `/computing/hosts/${hostId}` : `/computing/hosts/${hostId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [host?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    general: HostGenerals,
    vms: HostVms,
    nics: HostNics,
    devices: HostDevices,
    events: HostEvents
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent hostId={hostId} /> : null;
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
      label: '호스트 편집',
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
        titleIcon={faUser}
        title={host?.name}
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
          <HostModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            hId={hostId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='Host'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'호스트'}
            data={host}
          />
        </Suspense>
      )}

      <Footer/>
    </div>
  );
};

export default HostInfo;