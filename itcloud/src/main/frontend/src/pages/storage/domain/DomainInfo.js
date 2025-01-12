import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faDatabase } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../../components/navigation/NavButton';
import HeaderButton from '../../../components/button/HeaderButton';
import Footer from '../../../components/footer/Footer';
import { useDomainById } from '../../../api/RQHook';
import Path from '../../../components/Header/Path';
import DomainGeneral from './DomainGeneral';
import DomainDatacenters from './DomainDatacenters';
import DomainVms from './DomainVms';
import DomainEvents from './DomainEvents';
import DomainDisks from './DomainDisks';
import DomainTemplates from './DomainTemplates';
import DomainDiskSnapshots from './DomainDiskSnapshots';

const DomainModal = React.lazy(() => import('./modal/DomainModal'));
const DomainActionModal = React.lazy(() => import('./modal/DomainActionModal'));
const DomainDeleteModal = React.lazy(() => import('./modal/DomainDeleteModal'));

const DomainInfo = () => {
  const navigate = useNavigate();
  const { id: domainId, section } = useParams();
  const {
    data: domain, isLoading: isDomainLoading
  } = useDomainById(domainId);

  const [activeTab, setActiveTab] = useState('general');
  const [activeModal, setActiveModal] = useState(null);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'datacenters', label: '데이터 센터' },
    { id: 'vms', label: '가상머신' },
    { id: 'disks', label: '디스크' },
    { id: 'diskSnapshots', label: '디스크 스냅샷' },
    { id: 'templates', label: '템플릿' },
    { id: 'events', label: '이벤트' },
  ];

  useEffect(() => {
    setActiveTab(section || 'general');
  }, [section]);

  const handleTabClick = (tab) => {
    const path = tab === 'general' ? `/storages/domains/${domainId}` : `/storages/domains/${domainId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [domain?.name, sections.find((section) => section.id === activeTab)?.label];

  const renderSectionContent = () => {
    const SectionComponent = {
      general: DomainGeneral,
      datacenters: DomainDatacenters,
      vms: DomainVms,
      disks: DomainDisks,
      diskSnapshots: DomainDiskSnapshots,
      templates: DomainTemplates,
      events: DomainEvents,
    }[activeTab];
    return SectionComponent ? <SectionComponent domainId={domainId} /> : null;
  };

  const sectionHeaderButtons = [
    { type: 'edit', label: '도메인 편집', onClick: () => openModal("edit")},
    { type: 'delete', label: '삭제', onClick: () => openModal("delete")},
  ]

  const popupItems = [
    { type: 'deactivate', label: '유지보수', onClick: () => openModal("deactivate")},
    { type: 'activate', label: '활성', onClick: () => openModal("activate")},
    { type: 'restart', label: '재시작', onClick: () => openModal("restart")},
  ];

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'edit' && (
        <DomainModal
          editMode
          domainId={domainId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <DomainDeleteModal
          data={domain}
          onClose={closeModal}
        />
      )}
      {popupItems.some((item) => item.type === activeModal) && (
        <DomainActionModal
          action={activeModal}
          data={domain}
          contentLabel={activeModal}
          onClose={closeModal}
        />
      )}
    </Suspense>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDatabase}
        title={domain?.name}
        buttons={sectionHeaderButtons}
        popupItems={popupItems}
      />
      <div className="content-outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />
        <div className="host-btn-outer">
          <Path pathElements={pathData} />
          {renderSectionContent()}
        </div>
      </div>
      
      {/* domain 모달창 */}
      { renderModals() }
      <Footer/>
    </div>
  );
};

export default DomainInfo;