import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faDatabase } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../../components/navigation/NavButton';
import HeaderButton from '../../../components/button/HeaderButton';
import Footer from '../../../components/footer/Footer';
import './css/StorageDomainDetail.css';
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
const DomainDeleteModal = React.lazy(() => import('./modal/DomainDeleteModal'));
const DomainActionModal = React.lazy(() => import('./modal/DomainActionModal'));

const DomainInfo = () => {
  const { id: domainId, section } = useParams();
  const {
    data: domain,
    refetch: domainRefetch,
    error: domainError,
    isLoading: isDomainLoading,
  } = useDomainById(domainId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({
    edit: false,
    delete: false,
  });
  
  const [modals2, setModals2] = useState({
    deactivate: false,
    activate: false,
    restart: false,
  }); 
  
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
    if (!section) {
      setActiveTab('general');
    } else {
      setActiveTab(section);
    }
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

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev;
      return { ...prev, [type]: isOpen };
    });
  };

  const toggleModal2 = (type, isOpen) => {
    setModals2((prev) => ({ ...prev, [type]: isOpen }));
  };

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '도메인 편집', onClick: () => toggleModal('edit', true),},
    { id: 'delete_btn', label: '삭제', onClick: () => toggleModal('delete', true), },
  ]

  const popupItems = [
    { id: 'deactivate_btn', label: '유지보수', onClick: () => toggleModal2('deactivate', true) },
    { id: 'activate_btn', label: '활성', onClick: () => toggleModal2('activate', true) },
    { id: 'restart_btn', label: '재시작', onClick: () => toggleModal2('restart', true) },
  ];

  const renderModals = () => (
    <>
      {modals.edit && (
        <DomainModal
          isOpen={modals.edit}
          onRequestClose={() => toggleModal('edit', false)}
          action={'edit'}
          editMode={modals.edit}
          domainId={domainId}
        />
      )}
      {modals.import && (
        <DomainModal
          isOpen={modals.edit}
          onRequestClose={() => toggleModal('import', false)}
          action={'import'}
          editMode={modals.edit}
          domainId={domainId}
        />
      )}
      {modals.delete && (
        <DomainDeleteModal
          isOpen={modals.delete}
          onRequestClose={() => toggleModal('delete', false)}
          data={domain}
        />
      )}

      {Object.keys(modals2).map((key) => {
        const label = popupItems.find((item) => item.id === `${key}_btn`)?.label || key; // `label` 매칭
          return (
            modals2[key] && (
              <DomainActionModal
                key={key}
                isOpen={modals2[key]}
                action={key}
                onRequestClose={() => toggleModal2(key, false)}
                contentLabel={label} // label 값 사용
                data={domain}
              />
            )
          );
      })}
    </>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDatabase}
        title={domain?.name}
        buttons={sectionHeaderButtons}
        popupItems={popupItems}
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

export default DomainInfo;