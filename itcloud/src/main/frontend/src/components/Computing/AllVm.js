import React, { useState,  useEffect } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import './css/Vm.css';
import Footer from '../footer/Footer';
import { useAllTemplates, useAllVMs } from '../../api/RQHook';
import Templates from './Templates';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDesktop, faExclamationTriangle, faInfoCircle, faSearch, faTimes } from '@fortawesome/free-solid-svg-icons';
import TableOuter from '../table/TableOuter';

// React Modal 설정
Modal.setAppElement('#root');

const AllVm = () => {
  const navigate = useNavigate();
  const [activePopup, setActivePopup] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [activeSection, setActiveSection] = useState('common');
  const openModal = () => setIsModalOpen(true);



  
  const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
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
const [activeTab, setActiveTab] = useState('img');
const handleTabClick = (tab) => {
    setActiveTab(tab);
  };
// 추가모달
const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false); // 연결 팝업 상태
const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false); // 생성 팝업 상태

  const buttons = [
    { id: 'new_btn', label: '새로 만들기',onClick:() => openPopup('new')},
    { id: 'edit_btn', label: '편집', onClick:() => openPopup('edit')},
    { id: 'delete_btn', label: '삭제',onClick:() => openPopup('delete')},
    { id: 'run_btn', label: <><i className="fa fa-play"></i>실행</>, onClick: () => console.log() },
    { id: 'pause_btn', label: <><i className="fa fa-pause"></i>일시중지</>, onClick: () => console.log() },
    { id: 'stop_btn', label: <><i className="fa fa-stop"></i>종료</>, onClick: () => console.log() },
    { id: 'reboot_btn', label: <><i className="fa fa-repeat"></i>재부팅</>, onClick: () => console.log() },
    { id: 'console_btn', label: <><i className="fa fa-desktop"></i>콘솔</>, onClick: () => console.log() },
    { id: 'template_btn', label: <><i className="fa fa-desktop"></i>템플릿</>, onClick: () => navigate('/computing/templates') },
    { id: 'snapshot_btn', label: '스냅샷 생성', onClick: () => console.log() },
    { id: 'migration_btn', label: '마이그레이션', onClick:() => openPopup('migration')} ,
  ];

  const popupItems = [
    { label: '가져오기', onClick: () => console.log('가져오기 clicked') },
    { label: '가상 머신 복제', onClick: () => console.log('가상 머신 복제 clicked') },
    { label: '템플릿 생성', onClick: () => console.log('템플릿 생성 clicked') },
    { label: 'OVA로 내보내기', onClick: () => console.log('OVA로 내보내기 clicked') },
  ];

  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') {
      navigate(`/computing/vms/${row.id}`); // 해당 이름을 URL로 전달하며 HostDetail로 이동합니다.
    }
  };

   // 탭 상태 정의 (기본 값: 'ipv4')
   const [selectedModalTab, setSelectedModalTab] = useState('common');
   // 탭 클릭 핸들러
   const handleTabModalClick = (tab) => {
     setSelectedModalTab(tab);
   };
 
 

  const { 
    data: vms, 
    status: vmsStatus,
    isRefetching: isVMsRefetching,
    refetch: refetchVMs, 
    isError: isVMsError, 
    error: vmsError, 
    isLoading: isVMsLoading,
  } = useAllVMs(toTableItemPredicateVMs);
  
  function toTableItemPredicateVMs(vm) {
    return {
      status: vm?.status ?? 'Unknown',       
      id: vm?.id ?? '',
      icon: '',                                   
      name: vm?.name ?? 'Unknown',               
      comment: vm?.comment ?? '',                 
      host: vm?.host ?? 'Unknown',           
      ipv4: vm?.ipv4 ?? 'Unknown',              
      fqdn: vm?.fqdn ?? '',                      
      cluster: vm?.cluster ?? 'Unknown',          
      datacenter: vm?.datacenter ?? 'Unknown',
      memory: vm?.memory ?? '',  
      cpu: vm?.cpu ?? '',  
      network: vm?.network ?? '',  
      upTime: vm?.upTime ?? '',                    
      description: vm?.description ?? 'No description',  
    };
  }



  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title="가상머신"
        subtitle=""
        buttons={buttons}
        popupItems={popupItems}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">

          <div className="section_table_outer">
                
              <TableOuter
                columns={TableColumnsInfo.VM_CHART} 
                data={vms} onRowClick={handleRowClick} 
                className='machine_chart' 
                clickableColumnIndex={[1]} 
                showSearchBox={true}
              /> 
           
          </div>
        </div>
      </div>
  
           
            {/* 새로만들기팝업 */}
       
            <Modal
                isOpen={activePopup === 'new'}
        onRequestClose={closeModal}
        contentLabel="가상머신 편집"
        className="Modal"
        overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
             >
            <div className="vm_edit_popup">
                <div className="popup_header">
                    <h1>가상머신 생성</h1>
                    <button onClick={closeModal}>
                        <FontAwesomeIcon icon={faTimes} fixedWidth />
                    </button>
                </div>
                <div className='vm_edit_popup_content'>
                    <div className='vm_new_nav' style={{
                        fontSize: '0.33rem',
                        height: '67vh',
                        width: '30%',
                        backgroundColor: '#FAFAFA',
                        borderRight: '1px solid #ddd',
                        boxShadow: '1px 0 5px rgba(0, 0, 0, 0.1)',
                        fontWeight: 800
                    }}>
                        <div
                        id="common_tab"
                        className={selectedModalTab === 'common' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('common')}
                        >
                        일반
                        </div>
                        <div
                        id="system_tab"
                        className={selectedModalTab === 'system' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('system')}
                        >
                        시스템
                        </div>
                        <div
                        id="host_tab"
                        className={selectedModalTab === 'host' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('host')}
                        >
                        호스트
                        </div>
                        <div
                        id="ha_mode_tab"
                        className={selectedModalTab === 'ha_mode' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('ha_mode')}
                        >
                        고가용성
                        </div>
                        <div
                        id="res_alloc_tab"
                        className={selectedModalTab === 'res_alloc' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('res_alloc')}
                        >
                        리소스 할당
                        </div>
                        <div
                        id="boot_option_tab"
                        className={selectedModalTab === 'boot_outer' ? 'active-tab' : 'inactive-tab'}
                        onClick={() => setSelectedModalTab('boot_outer')}
                        >
                        부트 옵션
                        </div>

                    </div>

                {/* 탭 내용 */}
                <div className="vm_edit_select_tab">
                    {selectedModalTab === 'common' && 
                    <>
                        <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content mb-1">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                                <div>
                                    <label htmlFor="comment">코멘트</label>
                                    <input type="text" id="comment" />
                                </div>
                            </div>
                            <div className='px-1 font-bold'>인스턴스 이미지</div>
                            <div className="edit_third_content" style={{ borderBottom: '1px solid gray', marginBottom:'0.2rem' }}>
                                <div>
                                    <span>adfdsfadsf</span>
                                </div>
                                <div>
                                <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
                                    <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
                                    <div className='flex'>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>
                            <span className='edit_fourth_span'>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</span>
                            <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                               
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                    </>
                    }
                    {selectedModalTab === 'system' && 
                    <>
            
                    </>
                    }
                    {selectedModalTab === 'host' && 
                    <>
            

                    </>
                    }
                    {selectedModalTab === 'ha_mode' && 
                    <>
            

                    </>
                    }
                    {selectedModalTab === 'res_alloc' && 
                    <>
            

                    </>
                    }
                    {selectedModalTab === 'boot_outer' && 
                    <>
            

                    </>
                    }
                </div>
                </div>

                <div className="edit_footer">
                <button style={{ display: 'none' }}></button>
                <button>OK</button>
                <button onClick={closeModal}>취소</button>
                </div>
            </div>
            </Modal>





            {/* 편집팝업 */}
            {/* {activePopup === 'edit' && (
            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                contentLabel="가상머신 편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
            <div className="vm_edit_popup ">
            <div className="popup_header">
              <h1>가상머신 편집</h1>
              <button onClick={closeModal}>
                <FontAwesomeIcon icon={faTimes} fixedWidth />
              </button>
            </div>
            
            <div className="edit_body flex">
            <div className="edit_aside">
                <div
                  className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`}
                  id="common_outer_btn"
                  onClick={() => handleSectionChange('common_outer')}
                >
                  <span>일반</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`}
                  id="system_outer_btn"
                  onClick={() => handleSectionChange('system_outer')}
                >
                  <span>시스템</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`}
                  id="host_outer_btn"
                  onClick={() => handleSectionChange('host_outer')}
                >
                  <span>호스트</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`}
                  id="ha_mode_outer_btn"
                  onClick={() => handleSectionChange('ha_mode_outer')}
                >
                  <span>고가용성</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`}
                  id="res_alloc_outer_btn"
                  onClick={() => handleSectionChange('res_alloc_outer')}
                >
                  <span>리소스 할당</span>
                </div>
                <div
                  className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`}
                  id="boot_outer_btn"
                  onClick={() => handleSectionChange('boot_outer')}
                >
                  <span>부트 옵션</span>
                </div>
              </div>


                    <form action="#">
                      
                        <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                            <div className="edit_first_content">
                                <div>
                                    <label htmlFor="cluster">클러스터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                    <div>데이터센터 Default</div>
                                </div>

                                <div>
                                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                                    <select id="template" disabled>
                                        <option value="test02">test02</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="os">운영 시스템</label>
                                    <select id="os">
                                        <option value="linux">Linux</option>
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                                    <select id="firmware">
                                        <option value="bios">BIOS의 Q35 칩셋</option>
                                    </select>
                                </div>
                                <div style={{ marginBottom: '2%' }}>
                                    <label htmlFor="optimization">최적화 옵션</label>
                                    <select id="optimization">
                                        <option value="server">서버</option>
                                    </select>
                                </div>
                            </div>

                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="name">이름</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                            </div>
                            <div className="instance_image">
                                <span>인스턴스 이미지</span><br/>
                                <div>
                                    <div>on20-apm_Disk1_c1: (2 GB) 기존</div>
                                    <div className='flex'>
                                        <button className='mr-1'>편집</button>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </div>

                            <span className='edit_fourth_span'>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</span>
                            <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                               
                                <div className='edit_fourth_content_select flex'>
                                    <label htmlFor="network_adapter">네트워크 어댑터 1</label>
                                    <select id="network_adapter">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className='flex'>
                                    <button>+</button>
                                    <button>-</button>
                                </div>
                            </div>
                        </div>

                        
                        <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                            
                            <div className="edit_second_content">
                                <div>
                                    <label htmlFor="memory_size">메모리 크기</label>
                                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <label htmlFor="max_memory">최대 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="actual_memory">할당할 실제 메모리</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <input type="text" id="total_cpu" value="1" readOnly />
                                </div>
                                <div>
                                    <div>
                                        <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                                        <span>고급 매개 변수</span>
                                    </div>
                                </div>
                                <div style={{ fontWeight: 600 }}>일반</div>
                                <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                                    <div>
                                        <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                    <select id="time_offset">
                                        <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                                    </select>
                                </div>
                            </div>
                        </div>
   
                       
                        <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                            <div id="host_second_content">
                                <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                                <div className="form_checks">
                                    <div>
                                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                                        <label className="form-check-label" htmlFor="flexRadioDefault1">
                                            클러스터 내의 호스트
                                        </label>
                                    </div>
                                    <div>
                                        <div>
                                            <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                                            <label className="form-check-label" htmlFor="flexRadioDefault2">
                                                특정 호스트
                                            </label>
                                        </div>
                                        <div>
                                            <select id="specific_host_select">
                                                <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="host_checkboxs">
                                    <span>CPU 옵션:</span>
                                    <div className="host_checkbox">
                                        <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                                        <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                                        <i className="fa fa-info-circle"></i>
                                    </div>
                                   
                                </div>
                            </div>

                            <div id="host_third_content">
                                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                                <div>
                                    <div>
                                        <span>마이그레이션 모드</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_mode">
                                        <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>마이그레이션 정책</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="migration_policy">
                                        <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                                    </select>
                                </div>
                                
                                <div>
                                    <div>
                                        <span>Parallel Migrations</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="parallel_migrations" readOnly>
                                        <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                                    </select>
                                </div>
                               
                            </div>
                        </div>

                       
                        <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                            <div id="ha_mode_second_content">
                                <div className="checkbox_group">
                                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                                    <label className="check_label" htmlFor="ha_mode_box">
                                        고가용성
                                    </label>
                                </div>
                                <div>
                                    <div>
                                        <span>가상 머신 임대 대상 스토리지 도메인</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="no_lease" disabled>
                                        <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                                    </select>
                                </div>
                                <div>
                                    <div>
                                        <span>재개 동작</span>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <select id="force_shutdown">
                                        <option value="강제 종료">강제 종료</option>
                                    </select>
                                </div>
                                <div className="ha_mode_article">
                                    <span>실행/마이그레이션 큐에서 우선순위 : </span>
                                    <div>
                                        <span>우선 순위</span>
                                        <select id="priority">
                                            <option value="낮음">낮음</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="ha_mode_article">
                                    <span>위치독</span>
                                    <div>
                                        <span>위치독 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>위치독 작업</span>
                                        <select id="watchdog_action" disabled>
                                            <option value="없음">없음</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                
                        <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                                    <div className='cpu_res_box'>
                                        <span>CPU 프로파일</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU 공유</span>
                                        <div id="cpu_sharing">
                                            <select id="watchdog_action" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>CPU Pinning Policy</span>
                                        <select id="watchdog_action">
                                            <option value="없음">None</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <div>
                                            <span>CPU 피닝 토폴로지</span>
                                            <i className="fa fa-info-circle"></i>
                                        </div>
                                        <input type="text" disabled />
                                    </div>
                                </div>

                                <span style={{ fontWeight: 600 }}>메모리 할당:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">메모리 Balloon 활성화</label>
                                    </div>
                                
                                </div>

                                <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                                <div id="threads">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                                        <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                                    </div>
                                    <div>
                                        <input type="text" />
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                </div>

                                <span className='mb-1' style={{ fontWeight: 600 }}>큐:</span>
                                
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_multi_queues" name="enable_multi_queues" />
                                        <label htmlFor="enable_multi_queues">멀티 큐 사용</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='checkbox_group mb-1'>
                                        <input type="checkbox" id="enable_virtio_scsi" name="enable_virtio_scsi" />
                                        <label htmlFor="enable_virtio_scsi">VirtIO-SCSI 활성화</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <div className='cpu_res_box mb-1' >
                                        <span>VirtIO-SCSI Multi Queues</span>
                                        <div id="cpu_sharing">
                                            <select id="multi_queue_status" style={{ width: '63%' }}>
                                                <option value="없음">비활성화됨</option>
                                            </select>
                                            <input type="text" value="0" disabled />
                                        </div>
                                    </div>
                                    
                            </div>
                        </div>

                 
                        <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                            <div className="res_second_content">
                                <div className="cpu_res">
                                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                                    <div className='cpu_res_box'>
                                        <span>첫 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">하드디스크</option>
                                        </select>
                                    </div>
                                    <div className='cpu_res_box'>
                                        <span>두 번째 장치</span>
                                        <select id="watchdog_action">
                                            <option value="없음">Default</option>
                                        </select>
                                    </div>
                                </div>

                                <div id="boot_checkboxs">
                                    <div>
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group mb-1.5'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>

                                <div className="cpu_res border-t border-gray-500 py-1">
                                    <span style={{ fontWeight: 600 }}>Linux 부팅 옵션:</span>
                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_path">커널 경로</label>
                                        <input type="text" id="kernel_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="initrd_path">initrd 경로</label>
                                        <input type="text" id="initrd_path" value="2048 MB" readOnly />
                                    </div>

                                    <div className='cpu_res_box'>
                                        <label htmlFor="kernel_parameters">커널 매개 변수</label>
                                        <input type="text" id="kernel_parameters" value="2048 MB" readOnly />
                                    </div>


                                </div>
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">
                    <button>OK</button>
                    <button onClick={closeModal}>취소</button>
                </div>
            </div>
            </Modal>
)} */}

        {/*새로만들기(연결)추가팝업*/}
        <Modal
     isOpen={isConnectionPopupOpen}
     onRequestClose={() => setIsConnectionPopupOpen(false)}
      contentLabel="새 가상 디스크"
      className="Modal"
      overlayClassName="modalOverlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>가상 디스크 연결</h1>
          <button onClick={() => setIsConnectionPopupOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
        </div>
        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick('img')}
            className={activeTab === 'img' ? 'active' : ''}
          >
            이미지
          </div>
          <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접LUN
          </div>
          
        </div>
        {/*이미지(내용바꿔야함)*/}
        {activeTab === 'img' && (
            <TableOuter 
            columns={TableColumnsInfo.VMS_FROM_HOST}
            data={[]}
            onRowClick={() => console.log('Row clicked')}
      />
        )}
        {/*직접LUN(내용바꿔야함)*/}
        {activeTab === 'directlun' && (
            <TableOuter 
            columns={TableColumnsInfo.VMS_STOP}
            data={[]}
            onRowClick={() => console.log('Row clicked')}
        />
        )}
      
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={() => setIsConnectionPopupOpen(false)}>취소</button>
        </div>
      </div>
        </Modal>

        {/*새로만들기(생성)추가팝업 */}
        <Modal
       isOpen={isCreatePopupOpen}
       onRequestClose={() => setIsCreatePopupOpen(false)}
      contentLabel="새 가상 디스크"
      className="Modal"
      overlayClassName="modalOverlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>새 가상 디스크</h1>
          <button onClick={() => setIsCreatePopupOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
        </div>
        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick('img')}
            className={activeTab === 'img' ? 'active' : ''}
          >
            이미지
          </div>
          <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접LUN
          </div>
          
        </div>
        {/*이미지*/}
        {activeTab === 'img' && (
          <div className="disk_new_img">
            <div className="disk_new_img_left">
              <div className="img_input_box">
                <span>크기(GIB)</span>
                <input type="text" />
              </div>
              <div className="img_input_box">
                <span>별칭</span>
                <input type="text" />
              </div>
              <div className="img_input_box">
                <span>설명</span>
                <input type="text" />
              </div>
              <div className="img_select_box">
                <label htmlFor="os">데이터 센터</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="img_select_box">
                <label htmlFor="os">스토리지 도메인</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="img_select_box">
                <label htmlFor="os">할당 정책</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="img_select_box">
                <label htmlFor="os">디스크 프로파일</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
            </div>
            <div className="disk_new_img_right">
              <div>
                <input type="checkbox" id="reset_after_deletion" checked/>
                <label htmlFor="reset_after_deletion">부팅 가능</label>
              </div>
              <div>
                <input type="checkbox" className="shareable" />
                <label htmlFor="shareable">공유 가능</label>
              </div>
              <div>
                <input type="checkbox" id="incremental_backup" defaultChecked />
                <label htmlFor="incremental_backup">읽기 전용</label>
              </div>
            </div>
          </div>
        )}
        {/*직접LUN*/}
        {activeTab === 'directlun' && (
          <div id="storage_directlun_outer">
            <div id="storage_lun_first">
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">데이터 센터</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">호스트</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">스토리지 타입</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
              </div>
            </div>
          </div>
        )}
      
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={() => setIsCreatePopupOpen(false)}>취소</button>
        </div>
      </div>
        </Modal>
            {/*삭제 팝업 */}
            <Modal
        isOpen={activePopup === 'delete'}
        onRequestClose={closeModal}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_delete_popup">
          <div className="popup_header">
            <h1>디스크 삭제</h1>
            <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='disk_delete_box'>
            <div>
              <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
              <span>다음 항목을 삭제하시겠습니까?</span>
            </div>
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
            </Modal>

             {/* 설정팝업 시스템권한(추가 팝업) */}
             <Modal
                        isOpen={activePopup === 'addSystemRole'}
                        onRequestClose={closeModal}
                        contentLabel="추가"
                        className="Modal"
                        overlayClassName="Overlay newRolePopupOverlay"
                        shouldCloseOnOverlayClick={false}
                    >
                        <div className="setting_system_new_popup">
                            <div className="popup_header">
                                <h1>사용자에게 권한 추가</h1>
                                <button onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                            </div>

                            <div className="power_radio_group">
                                <input type="radio" id="user" name="option" defaultChecked />
                                <label htmlFor="user">사용자</label>
                                
                                <input type="radio" id="group" name="option" />
                                <label htmlFor="group">그룹</label>
                            </div>

                            <div className="power_contents_outer">
                                <div>
                                    <label htmlFor="cluster">검색:</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>   
                                    </select>
                                </div>
                                <div>
                                    <label htmlFor="cluster">네임스페이스:</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>   
                                    </select>
                                </div>
                                <div>
                                    <label style={{ color: "white" }}>.</label>
                                    <input type="text" id="name" value="test02" />
                                </div>
                                <div>
                                    <div style={{ color: "white" }}>.</div>
                                    <input type="submit" value="검색" />
                                </div>
                            </div>

                           

                            <div className="power_last_content" style={{ padding: "0.1rem 0.3rem" }}>
                                <label htmlFor="cluster">할당된 역할:</label>
                                <select id="cluster" style={{ width: "65%" }}>
                                    <option value="default">UserRole</option>   
                                </select>
                            </div>

                            <div className="edit_footer">
                                <button style={{ display: "none" }}></button>
                                <button>OK</button>
                                <button onClick={closeModal}>취소</button>
                            </div>
                        </div>
                    </Modal>
              {/* 마이그레이션 팝업(default) */}
              {/* {activePopup === 'migration' && (
        <Modal
          isOpen={isModalOpen}
          onRequestClose={closeModal}
          contentLabel="마이그레이션"
          className="migration_popup"
          overlayClassName="migration_popup_outer"
          shouldCloseOnOverlayClick={false}
        >
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
              <div>
                <div>가상머신</div>
                <div>on20-ap02</div>
              </div>
            </div>
            <div className="edit_footer">
                <button style={{ display: 'none' }}></button>
                <button>OK</button>
                <button onClick={closeModal}>취소</button>
            </div>
          </div>
        </Modal>
      )} */}
               
      <Footer/>
    </div>
  );
};

export default AllVm;
