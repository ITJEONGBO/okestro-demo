import React, { useState,  useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAllTemplates } from "../../api/RQHook";
import VmTemplateChart from './VmTemplateChart';
import Table from "../table/Table";
import TableColumnsInfo from "../table/TableColumnsInfo";
import HeaderButton from "../button/HeaderButton";

// 애플리케이션 섹션
const Templates = () => {
    const [activeSection, setActiveSection] = useState(null);
    const navigate = useNavigate();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [activeChart, setActiveChart] = useState('template'); // Default to 'machine' chart
  
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);
  
      // 편집 팝업
          useEffect(() => {
            const showEditPopup = () => {
                setActiveSection('common_outer');
                const editPopupBg = document.getElementById('edit_popup_bg');
                if (editPopupBg) {
                    editPopupBg.style.display = 'block';
                }
            }
  
            const editButton = document.getElementById('network_first_edit_btn');
            if (editButton) {
                editButton.addEventListener('click', showEditPopup);
            }
  
            return () => {
                if (editButton) {
                    editButton.removeEventListener('click', showEditPopup);
                }
            };
        }, []);
  
        // 편집 팝업 기본 섹션 스타일 적용
        useEffect(() => {
            const defaultElement = document.getElementById('common_outer_btn');
            if (defaultElement) {
                defaultElement.style.backgroundColor = '#EDEDED';
                defaultElement.style.color = '#1eb8ff';
                defaultElement.style.borderBottom = '1px solid blue';
            }
        }, []);
  
        // 편집 팝업 스타일 변경
        const handleSectionChange = (section) => {
            setActiveSection(section);
            const elements = document.querySelectorAll('.edit_aside > div');
            elements.forEach(el => {
                el.style.backgroundColor = '#FAFAFA';
                el.style.color = 'black';
                el.style.borderBottom = 'none';
            });
  
            const activeElement = document.getElementById(`${section}_btn`);
            if (activeElement) {
                activeElement.style.backgroundColor = '#EDEDED';
                activeElement.style.color = '#1eb8ff';
                activeElement.style.borderBottom = '1px solid blue';
            }
        };
        const showEditPopup = () => {
          setActiveSection('common_outer');
          const editPopupBg = document.getElementById('edit_popup_bg');
          if (editPopupBg) {
            editPopupBg.style.display = 'block';
          }
      };
  
  
    const buttons = [
      { id: 'new_btn', label: '새로 만들기'},
      { id: 'edit_btn', label: '편집', onClick: () => showEditPopup()},
      { id: 'delete_btn', label: '삭제'},
      { id: 'run_btn', label: <><i className="fa fa-play"></i>실행</>, onClick: () => console.log() },
      { id: 'pause_btn', label: <><i className="fa fa-pause"></i>일시중지</>, onClick: () => console.log() },
      { id: 'stop_btn', label: <><i className="fa fa-stop"></i>종료</>, onClick: () => console.log() },
      { id: 'reboot_btn', label: <><i className="fa fa-repeat"></i>재부팅</>, onClick: () => console.log() },
      { id: 'console_btn', label: <><i className="fa fa-desktop"></i>콘솔</>, onClick: () => console.log() },
      { id: 'snapshot_btn', label: '스냅샷 생성', onClick: () => console.log() },
      { id: 'migration_btn', label: '마이그레이션', onClick: () => console.log() },
    ];
  
    const popupItems = [
      '가져오기',
      '가상 머신 복제',
      '템플릿 생성',
      'OVA로 내보내기',
    ];

    const handleRowClick = (row, column) => {
        if (column.accessor === 'name') {
          navigate(`/computing/templates/${row.id}`); // 해당 이름을 URL로 전달하며 HostDetail로 이동합니다.
        }
      };
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
          id: template?.id ?? '',
          name: template?.name ?? 'Unknown', 
          status: template?.status ?? 'Unknown',                // 템플릿 상태
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
            title="Template "
            subtitle=""
            buttons={buttons}
            popupItems={popupItems}
            openModal={openModal}
            togglePopup={() => {}}
            />
            <div className="content_outer">
                <div className="empty_nav_outer">
                    {/* TODO: TableOuter화 */}
                <div className="section_table_outer">
                    <div className='host_filter_btns'>
                        <button
                        onClick={() => {
                        navigate('/computing/vms'); // 가상머신 목록 경로로 이동
                        }}
                    >
                        가상머신 목록
                    </button>
                    <button
                        onClick={() => {
                            setActiveChart('template');
                            navigate('/computing/templates'); // 템플릿 목록 경로로 이동
                        }}
                        className={activeChart === 'template' ? 'active' : ''}
                        >
                        템플릿 목록
                        </button>
                    </div>
                
                   
                    <Table 
                    columns={TableColumnsInfo.TEMPLATE_CHART} 
                    data={templates} onRowClick={handleRowClick} 
                    className='template_chart'
                    clickableColumnIndex={[0]} 
                    />
                    
                </div>
                </div>
            </div>
        </div>
          
    );
  };
  
  export default Templates;