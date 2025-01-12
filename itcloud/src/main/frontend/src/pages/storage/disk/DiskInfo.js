import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faHdd } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../../components/navigation/NavButton';
import HeaderButton from '../../../components/button/HeaderButton';
import Footer from '../../../components/footer/Footer';
import Path from '../../../components/Header/Path';
import { useDiskById } from '../../../api/RQHook.js';
import DiskGeneral from './DiskGeneral.js';
import DiskVms from './DiskVms.js'
import DiskDomains from './DiskDomains.js';

const DiskModal = React.lazy(() => import('./modal/DiskModal'))
const DiskDeleteModal = React.lazy(() => import('./modal/DiskDeleteModal'));

const DiskInfo = () => {
  const navigate = useNavigate();
  const { id: diskId, section } = useParams();
  const {
    data: disk, isError, isLoading
  } = useDiskById(diskId);

  const [activeTab, setActiveTab] = useState('general');
  const [activeModal, setActiveModal] = useState(null);

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  useEffect(() => {
    if (isError || (!isLoading && !disk)) {
      navigate('/storages/disks');
    }
  }, [isError, isLoading, disk, navigate]);

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'vms', label: '가상머신' },
    { id: 'domains', label: '스토리지' },
  ];

  useEffect(() => {
    setActiveTab(section || 'general');
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

  const sectionHeaderButtons = [
    { type: 'edit', label: '편집', onClick: () => openModal('edit'),},
    { type: 'delete', label: '삭제', onClick: () => openModal('delete'), },
    { type: 'move', label: '이동', onClick: () => openModal('deactivate') },
    { type: 'copy', label: '복사', onClick: () => openModal('activate') },
    // { type: 'upload', label: '업로드', onClick: () => openModal('restart') },
  ];

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'edit' && (
        <DiskModal
          editMode
          diskId={diskId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <DiskDeleteModal
          data={disk}
          onClose={closeModal}
        />
      )}
      </Suspense>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faHdd}
        title={disk?.alias}
        buttons={sectionHeaderButtons}
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

      {/* 디스크 모달창 */}
      { renderModals() }
      <Footer/>
    </div>
  );
};

export default DiskInfo;