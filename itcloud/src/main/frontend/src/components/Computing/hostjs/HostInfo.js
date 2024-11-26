import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import NavButton from '../../navigation/NavButton';
import HeaderButton from '../../button/HeaderButton';
import Footer from '../../footer/Footer';
import '../css/Host.css';
import { useHost } from '../../../api/RQHook';
import Path from '../../Header/Path';
import HostGeneral from './HostGeneral';
import HostVms from './HostVms'
import HostNics from './HostNics'
import HostDevices from './HostDevices';
import HostEvents from './HostEvents'

const HostModal = React.lazy(() => import('../../Modal/HostModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));
const HostActionModal = React.lazy(() => import('../../Modal/HostActionModal'))

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
  const [modals, setModals] = useState({
    edit: false,
    delete: false,
  });
  
  const [modals2, setModals2] = useState({
    deactivate: false,
    activate: false,
    restart: false,
    stop: false,
    reinstall: false,
    register: false,
    haon: false,
    haoff: false,
  }); 
  
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

  const renderSectionContent = () => {
    const SectionComponent = {
      general: HostGeneral,
      vms: HostVms,
      nics: HostNics,
      devices: HostDevices,
      events: HostEvents,
    }[activeTab];
    return SectionComponent ? <SectionComponent hostId={hostId} /> : null;
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
    { id: 'edit_btn', label: '호스트 편집', onClick: () => toggleModal('edit', true),},
    { id: 'delete_btn', label: '삭제', onClick: () => toggleModal('delete', true), },
  ]

  const popupItems = [
    { id: 'deactivate_btn', label: '유지보수', onClick: () => toggleModal2('deactivate', true) },
    { id: 'activate_btn', label: '활성', onClick: () => toggleModal2('activate', true) },
    { id: 'restart_btn', label: '재시작', onClick: () => toggleModal2('restart', true) },
    { id: 'stop_btn', label: '중지', onClick: () => toggleModal2('stop', true) },
    { id: 'reinstall_btn', label: '다시 설치', onClick: () => toggleModal2('reinstall', true) },
    { id: 'register_btn', label: '인증서 등록', onClick: () => toggleModal2('register', true) },
    { id: 'haon_btn', label: '글로벌 HA 유지 활성화', onClick: () => toggleModal2('haon', true) },
    { id: 'haoff_btn', label: '글로벌 HA 유지 비활성화', onClick: () => toggleModal2('haoff', true) },
  ];

  const renderModals = () => (
    <>
      {modals.edit && (
        <HostModal
          isOpen={modals.edit}
          onRequestClose={() => toggleModal('edit', false)}
          editMode={modals.edit}
          hId={hostId}
        />
      )}
      {modals.delete && (
        <DeleteModal
          isOpen={modals.delete}
          type="Host"
          onRequestClose={() => toggleModal('delete', false)}
          contentLabel="호스트"
          data={host}
        />
      )}

      {Object.keys(modals2).map((key) => {
        const label = popupItems.find((item) => item.id === `${key}_btn`)?.label || key; // `label` 매칭
          return (
            modals2[key] && (
              <HostActionModal
                key={key}
                isOpen={modals2[key]}
                action={key}
                onRequestClose={() => toggleModal2(key, false)}
                contentLabel={label} // label 값 사용
                data={host}
              />
            )
          );
      })}
    </>
  );

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faUser}
        title={host?.name}
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

      {/* {modals2 && (
        <Suspense fallback={<div>Loading...</div>}>
          <HostActionModal
            isOpen={modals2}
            action={modals2}
            onRequestClose={() => toggleModal2(modals2, false)}
            contentLabel={modals2}
            data={host}
          />
        </Suspense>
      )} */}

      <Footer/>
    </div>
  );
};

export default HostInfo;