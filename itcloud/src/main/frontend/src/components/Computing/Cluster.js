import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import { Table } from '../table/Table';
import './css/Cluster.css';
import Footer from '../footer/Footer';

Modal.setAppElement('#root');

const Cluster = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const sectionHeaderButtons = [
    { id: 'new_btn', label: '새로 만들기', onClick: () => {} },
    { id: 'edit_btn', label: '편집', icon: 'fa-pencil', onClick: () => {} },
    { id: 'upgrade_btn', label: '업그레이드', icon: 'fa-arrow-up', onClick: () => {} }
  ];

  const sectionHeaderPopupItems = [
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

  const columns = [
    { header: '상태', accessor: 'status', clickable: false },
    { header: '이름', accessor: 'name', clickable: true },
    { header: '코멘트', accessor: 'comment', clickable: false },
    { header: '호환 버전', accessor: 'version', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
    { header: '클러스터 CPU 유형', accessor: 'cpuType', clickable: false },
    { header: '호스트 수', accessor: 'hostCount', clickable: false },
    { header: '가상 머신 수', accessor: 'vmCount', clickable: false },
    { header: '업그레이드 상태', accessor: 'upgradeStatus', clickable: false },
  ];

  const data = [
    {
      status: '',
      name: 'Default',
      comment: '',
      version: '4.7',
      description: 'The default server cluster',
      cpuType: 'Secure Intel Cascadelake',
      hostCount: 2,
      vmCount: 7,
      upgradeStatus: '',
    },
  ];

  // 이름 열을 클릭했을 때 동작하는 함수
  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') {
      navigate(`/computing/cluster/${row.name}`);
    }
  };

  return (
    <div id="section">
      <HeaderButton
        title="컴퓨팅 > "
        subtitle="클러스터"
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className='empty_nav_outer'>
          <div className="section_table_outer">
            <button>
              <i className="fa fa-refresh"></i>
            </button>
            <Table columns={columns} data={data} onRowClick={handleRowClick} />
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  );
};

export default Cluster;
