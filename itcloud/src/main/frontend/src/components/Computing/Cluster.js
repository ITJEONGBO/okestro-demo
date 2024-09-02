import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import './css/Cluster.css';

Modal.setAppElement('#root');

const Cluster = () => {
  const navigate = useNavigate();

  const [isModalOpen, setIsModalOpen] = useState(false);

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

  const initialData = [
    {
      status: '',
      name: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          Cluster1
        </span>
      ),
      comment: '',
      version: '4.6',
      description: 'This is the first cluster',
      cpuType: 'Intel Xeon',
      hostCount: 5,
      vmCount: 10,
      upgradeStatus: 'Up to date'
    }
  ];
  

  const [data, setData] = useState(initialData);

  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') {
      navigate(`/computing/cluster/${row.name.props.children}`);
    }
  };
  return (
    <div id="section">
      <HeaderButton
        title="DataCenter > "
        subtitle="Cluster"
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <div className="section_table_outer">
            <button>
              <i className="fa fa-refresh"></i>
            </button>
            <Table columns={TableColumnsInfo.CLUSTERS_ALT} data={data} onRowClick={handleRowClick} />
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Cluster;
