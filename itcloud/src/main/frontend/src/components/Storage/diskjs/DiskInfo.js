import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faHdd } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
// import '../css/Disk.css';
import { useDiskById } from '../../../api/RQHook';
import Path from '../../Header/Path';
import DiskGeneral from './DiskGeneral';
import DiskVms from './DiskVms.js'
import DiskDomains from './DiskDomains.js';

const DiskModal = React.lazy(() => import('../../Modal/DiskModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DiskInfo = () => {
  const { id: diskId, section } = useParams();
  const {
    data: disk,
  } = useDiskById(diskId, (e) => ({
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
    { id: 'vms', label: '가상머신' },
    { id: 'domains', label: '스토리지' },
  ];

  useEffect(() => {
    if (!section) {
      setActiveTab('general');
    } else {
      setActiveTab(section);
    }
  }, [section]);

  const handleTabClick = (tab) => {
    const path = tab === 'general' ? `/storages/disks/${diskId}` : `/storages/disks/${diskId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [disk?.alias, sections.find((section) => section.id === activeTab)?.label];

  const renderSectionContent = () => {
    const SectionComponent = {
      general: DiskGeneral,
      vms: DiskVms,
      domains: DiskDomains
    }[activeTab];
    return SectionComponent ? <SectionComponent diskId={diskId} /> : null;
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
    { id: 'move_btn', label: '이동', onClick: () => toggleModal('deactivate', true) },
    { id: 'copy_btn', label: '복사', onClick: () => toggleModal('activate', true) },
    { id: 'upload_btn', label: '업로드', onClick: () => toggleModal('restart', true) },
  ]


  const renderModals = () => (
    <>
      {modals.edit && (
        <DiskModal
          isOpen={modals.edit}
          onRequestClose={() => toggleModal('edit', false)}
          editMode={modals.edit}
          hId={diskId}
        />
      )}
      {modals.delete && (
        <DeleteModal
          isOpen={modals.delete}
          type="Disk"
          onRequestClose={() => toggleModal('delete', false)}
          contentLabel="디스크"
          data={disk}
        />
      )}
    </>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faHdd}
        title={disk?.alias}
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

export default DiskInfo;