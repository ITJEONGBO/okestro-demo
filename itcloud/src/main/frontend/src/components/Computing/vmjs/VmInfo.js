import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faMicrochip } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import '../css/Vm.css';
import { useVm } from '../../../api/RQHook';
import Path from '../../Header/Path';
import VmGenerals from './VmGenerals';
import VmNetworks from './VmNetworks';
import VmEvents from './VmEvents';

const VmModal = React.lazy(() => import('../../Modal/VmModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));
const VmActionModal = React.lazy(() => import('../../Modal/VmActionModal'));

const VmInfo = () => {
  const { id: vmId, section } = useParams();
  const {
    data: vm,
    status: vmStatus,
    isRefetching: isVmRefetching,
    refetch: vmRefetch,
    isError: isVmError,
    error: vmError,
    isLoading: isVmLoading,
  } = useVm(vmId, (e) => ({
    ...e,
  }));

  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('general');
  const [modals, setModals] = useState({ edit: false, delete: false }); 

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'disks', label: '디스크' },
    { id: 'snapshots', label: '스냅샨' },
    { id: 'nics', label: '네트워크 인터페이스' },
    { id: 'applications', label: '애플리케이션' },
    { id: 'hostDevices', label: '호스트 장치' },
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
    const path = tab === 'general' ? `/computing/vms/${vmId}` : `/computing/vms/${vmId}/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = [vm?.name, sections.find((section) => section.id === activeTab)?.label];

  const sectionComponents = {
    general: VmGenerals,
    disks: VmDisks,
    snapshots: VmSnapshots,
    nics: VmNics,
    applications: VmApplications,
    hostDevices: VmHostDevies,
    events: VmEvents
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab];
    return SectionComponent ? <SectionComponent vmId={vmId} /> : null;
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
    { id: 'start_btn', label: <><i className="fa fa-play"></i>실행</>, onClick: () => toggleModal('delete', true)},
    { id: 'pause_btn', label: <><i className="fa fa-pause"></i>일시중지</>, onClick: () => toggleModal('delete', true)},
    { id: 'stop_btn', label: <><i className="fa fa-stop"></i>종료</>, onClick: () => toggleModal('delete', true)},
    { id: 'reboot_btn', label: <><i className="fa fa-repeat"></i>재부팅</>, onClick: () => toggleModal('delete', true)},
    { id: 'snapshot_btn', label: '스냅샷 생성', onClick:() => toggleModal('snapshot', true)},
    { id: 'migration_btn', label: '마이그레이션', onClick:() => toggleModal('migration', true)} ,
  ]

  const popupItems = [
    { id: 'import', label: '가져오기', onClick: () => toggleModal('bring', true)},
    { id: 'clone_vm', label: '가상 머신 복제', onClick: () => toggleModal('vm_copy', true)},
    { id: 'create_template', label: '템플릿 생성' },
    { id: 'export_ova', label: 'OVA로 내보내기', onClick: () => toggleModal('OVA', true)}
  ];

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faMicrochip}
        title={vm?.name}
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
          <VmModal
            isOpen={modals.edit}
            onRequestClose={() => toggleModal('edit', false)}
            editMode={modals.edit}
            vmId={vmId}
          />
        </Suspense>
      )}

      {modals.delete && (
        <Suspense fallback={<div>Loading...</div>}>
          <DeleteModal
            isOpen={modals.delete}
            type='Vm'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'가상머신'}
            data={vm}
          />
        </Suspense>
      )}


      <Footer/>
    </div>
  );
};

export default VmInfo;