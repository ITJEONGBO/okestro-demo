import React, { useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import './css/Host.css';
import Footer from '../footer/Footer';
import { useAllTemplates, useAllVMs } from '../../api/RQHook';

// React Modal 설정
Modal.setAppElement('#root');

const VmHostChart = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [activeChart, setActiveChart] = useState('machine'); // Default to 'machine' chart

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const 섹션헤더버튼들 = [
    { id: 'edit_btn', label: '편집', onClick: () => {} },
    { id: 'delete_btn', label: '삭제', onClick: () => {} },
    { id: 'manage_btn', label: '관리', onClick: () => {}, hasDropdown: true },
    { id: 'install_btn', label: '설치', onClick: () => {}, hasDropdown: true },
  ];

  const 섹션헤더팝업아이템들 = [
    '가져오기',
    '가상 머신 복제',
    '삭제',
    '마이그레이션 취소',
    '변환 취소',
    '템플릿 생성',
    '도메인으로 내보내기',
    'Export to Data Domain',
    'OVA로 내보내기',
  ];

  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') {
      navigate(`/computing/host/${row.name}`); // 해당 이름을 URL로 전달하며 HostDetail로 이동합니다.
    }
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
      icon: '',                                   
      name: vm?.name ?? 'Unknown',               
      comment: vm?.comment ?? '',                 
      host: vm?.host ?? 'Unknown',           
      ipv4: vm?.ipv4 ?? 'Unknown',              
      fqdn: vm?.fqdn ?? '',                      
      cluster: vm?.cluster ?? 'Unknown',          
      datacenter: vm?.datacenter ?? 'Unknown',
      status: vm?.status ?? 'Unknown',             
      upTime: vm?.upTime ?? '',                    
      description: vm?.description ?? 'No description',  
    };
  }
  
  const { 
    data: templates, 
    status: templatesStatus,
    isRefetching: isTemplatesRefetching,
    refetch: refetchTemplates, 
    isError: isTemplatesError, 
    error: templatesError, 
    isLoading: isTemplatesLoading,
  } = useAllTemplates(toTableItemPredicateTemplates);
  
  function toTableItemPredicateTemplates(template) {
    return {
      status: template?.status ?? 'Unknown',                // 템플릿 상태
      name: template?.name ?? 'Unknown',                    // 템플릿 이름
      version: template?.version ?? 'N/A',                  // 템플릿 버전 정보
      description: template?.description ?? 'No description',// 템플릿 설명
      cpuType: template?.cpuType ?? 'CPU 정보 없음',         // CPU 유형 정보
      hostCount: template?.hostCount ?? 0,                  // 템플릿에 연결된 호스트 수
      vmCount: template?.vmCount ?? 0,                      // 템플릿에 연결된 VM 수
    };
  }
  


  return (
    <div id="section">
      <HeaderButton
        title="Host > "
        subtitle="Virtual Machine"
        buttons={섹션헤더버튼들}
        popupItems={섹션헤더팝업아이템들}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <div className="section_table_outer">
            <div className='host_filter_btns'>
              <button
                onClick={() => setActiveChart('machine')}
                className={activeChart === 'machine' ? 'active' : ''}
              >
                가상머신 목록
              </button>
              <button
                onClick={() => setActiveChart('template')}
                className={activeChart === 'template' ? 'active' : ''}
              >
                템플릿목록
              </button>
            </div>
            {activeChart === 'machine' && (
              <Table columns={TableColumnsInfo.VM_CHART} data={vms} onRowClick={handleRowClick} className='machine_chart' />
            )}
            {activeChart === 'template' && (
              <Table columns={TableColumnsInfo.TEMPLATE_CHART} data={templates} onRowClick={() => {}} className='template_chart' />
            )}
          </div>
        </div>
      </div>

      <Footer/>
    </div>
  );
};

export default VmHostChart;
