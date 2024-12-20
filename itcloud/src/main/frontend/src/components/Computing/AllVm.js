import React, { useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import './css/Vm.css';
import Footer from '../footer/Footer';
import {useAllVMs } from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faDesktop, faInfoCircle, faPlay, faTimes } from '@fortawesome/free-solid-svg-icons';
import TableInfo from '../table/TableInfo';
import VmDupl from '../duplication/VmDupl';

// React Modal 설정
Modal.setAppElement('#root');

const AllVm = () => {
  const navigate = useNavigate();
  const [activePopup, setActivePopup] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [activeSection, setActiveSection] = useState('common');
  const openModal = () => setIsModalOpen(true);


 // 모달 관련 상태 및 함수
 const openPopup = (popupType) => {
    setActivePopup(popupType);
    setActiveSection('common'); // 팝업을 열 때 항상 '일반' 섹션으로 설정
    setSelectedModalTab('common'); // '일반' 탭이 기본으로 선택되게 설정
    setIsModalOpen(true); // 모달 창 열림
};

const closeModal = () => {
    setIsModalOpen(false); // 모달 창 닫힘
    setActivePopup(null);  // 팝업 상태 초기화
    setActiveSection('common'); // 모달 닫을 때 '일반' 섹션으로 초기화
    setSelectedModalTab('common'); // 편집모달창 초기화
};

// // 추가모달
// const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false); // 연결 팝업 상태
// const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false); // 생성 팝업 상태
// const [isEditPopupOpen, setIsEditPopupOpen] = useState(false); // 생성 팝업 상태
// const [selectedVms, setSelectedVms] = useState(null);

   // 탭 상태 정의 (기본 값: 'ipv4')
   const [selectedModalTab, setSelectedModalTab] = useState('common');
   // 탭 클릭 핸들러

 


   const { 
    data: vms, 
    status: vmsStatus,
  } = useAllVMs((e) => {
    return {
      ...e,
      status: e?.status,
      host: e?.hostVo?.name,
      hostId: e?.hostVo?.id,
      cluster: e?.clusterVo?.name,
      clusterId: e?.clusterVo?.id,
      dataCenter: e?.dataCenterVo?.name,
      dataCenterId: e?.dataCenterVo?.id,

      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });
  const renderStatusIcon = (status) => {
    if (status === 'UP') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
    return status;
  };
  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title="가상머신"
        subtitle=""
        buttons={[]}
        popupItems={[]}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="host_btn_outer">
      <>
      <VmDupl
        vms={vms || []}
        columns={TableInfo.VMS}
      />
    </>
      </div>  
  

      <Footer/>
    </div>
  );
};

export default AllVm;
