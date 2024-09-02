import React, { useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import './css/Host.css';
import Footer from '../footer/Footer';

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

  const columns = [
    { header: '상태', accessor: 'status', clickable: false },
    { header: '이름', accessor: 'name', clickable: true },
    { header: '호환 버전', accessor: 'version', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
    { header: '클러스터 CPU 유형', accessor: 'cpuType', clickable: false },
    { header: '호스트 수', accessor: 'hostCount', clickable: false },
    { header: '가상 머신 수', accessor: 'vmCount', clickable: false },
  ];

  const data = [
    {
      status: '',
      name: '192.168.0.80',
      version: '4.7',
      description: 'The default server cluster',
      cpuType: 'Secure Intel Cascadelak',
      hostCount: 2,
      vmCount: 7,
    },
  ];

  const templatedata = [
    {
      status: '',
      name: '템플릿1',
      version: '4.7',
      description: 'The default server cluster',
      cpuType: 'Secure Intel Cascadelak',
      hostCount: 2,
      vmCount: 7,
    },
  ];

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
              <Table columns={columns} data={data} onRowClick={handleRowClick} className='machine_chart' />
            )}
            {activeChart === 'template' && (
              <Table columns={columns} data={templatedata} onRowClick={() => {}} className='template_chart' />
            )}
          </div>
        </div>
      </div>

      <Footer/>
    </div>
  );
};

export default VmHostChart;
