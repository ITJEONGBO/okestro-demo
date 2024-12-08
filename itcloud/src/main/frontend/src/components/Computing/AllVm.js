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

// 추가모달
const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false); // 연결 팝업 상태
const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false); // 생성 팝업 상태
const [isEditPopupOpen, setIsEditPopupOpen] = useState(false); // 생성 팝업 상태

   // 탭 상태 정의 (기본 값: 'ipv4')
   const [selectedModalTab, setSelectedModalTab] = useState('common');
   // 탭 클릭 핸들러

 
   const [selectedVms, setSelectedVms] = useState(null);

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
  
        {/* 마이그레이션 팝업*/}
        <Modal
        isOpen={activePopup === 'migration'}
          onRequestClose={closeModal}
          contentLabel="마이그레이션"
          className="Modal"
          overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
        >
          <div className='migration_popup_content'>
            <div className="popup_header">
              <h1>가상머신 마이그레이션</h1>
              <button onClick={closeModal}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            <div id="migration_article_outer">
              <span>1대의 가상 머신이 마이그레이션되는 호스트를 선택하십시오.</span>
              
              <div id="migration_article">
                <div>
                  <div id="migration_dropdown">
                    <label htmlFor="host">대상 호스트 <FontAwesomeIcon icon={faInfoCircle} fixedWidth /></label>
                  
                    <select name="host_dropdown" id="host">
                      <option value="">호스트 자동 선택</option>
                      <option value="php">PHP</option>
                      <option value="java">Java</option>
                    </select>
                  </div>
                </div>

                <div className="checkbox_group">
                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                    <label className="check_label" htmlFor="ha_mode_box">
                    선택한 가상 머신을 사용하여 양극 강제 연결 그룹의 모든 가상 시스템을 마이그레이션합니다.
                    </label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                </div>

                <div>
                  <div className='font-bold'>가상머신</div>
                  <div>on20-ap02</div>
                </div>
              </div>
            
            </div>
            
      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={closeModal}>취소</button>
      </div>
          </div>
        </Modal>
      <Footer/>
    </div>
  );
};

export default AllVm;
