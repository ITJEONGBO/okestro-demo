import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import { useTemplate } from '../../../api/RQHook';
import Path from '../../Header/Path';
import TemplateGeneral from './TemplateGeneral.js';
import TemplateVm from './TemplateVm.js';
import TemplateEvents from './TemplateEvents.js';
import TemplateNics from './TemplateNics.js';
// import TemplateNetworks from './TemplateNetworks';
// import TemplateEvents from './TemplateEvents';

const TemplateModal = React.lazy(() => import('../../Modal/TemplateModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const TemplateInfo = () => {
  const { id: templateId, section } = useParams();
  const {
    data: template,
    status: templateStatus,
    isRefetching: isTemplateRefetching,
    refetch: templateRefetch,
    isError: isTemplateError,
    error: templateError,
    isLoading: isTemplateLoading,
  } = useTemplate(templateId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({ edit: false, delete: false }); 

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vms', label: '가상머신' },
    { id: 'nics', label: '네트워크 인터페이스' },
    { id: 'disks', label: '디스크' },
    { id: 'storageDoamin', label: '스토리지' },
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
    const path = tab === 'general' ? `/computing/templates/${templateId}` : `/computing/templates/${templateId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [template?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    general: TemplateGeneral,
    vms: TemplateVm,
    nics: TemplateNics,
    // disks: TemplateDisks,
    // storageDomains: TemplateStorageDomain,
    events: TemplateEvents,
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent templateId={templateId} /> : null;
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev;
      return { ...prev, [type]: isOpen };
    });
  };

  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', onClick:() => toggleModal('edit', true)},
    { id: 'delete_btn', label: '삭제', onClick:() => toggleModal('delete', true)},
    { id: 'addVm_btn', label: '새 가상머신', onClick: () => toggleModal('addVm', true)},
  ]

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title={template?.name}
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
          <TemplateModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            templateId={templateId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='Template'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'템플릿'}
            data={template}
          />
        </Suspense>
      )}
      <Footer/>
    </div>
  );
};

export default TemplateInfo;