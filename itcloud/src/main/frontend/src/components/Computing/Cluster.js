import React, { useState } from 'react';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import ClusterName from './ClusterName';
import { Table } from '../table/Table';

// React Modal 설정
Modal.setAppElement('#root');

const Cluster = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showClusterName, setShowClusterName] = useState(false);

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

  if (showClusterName) {
    return <ClusterName />;
  }

  // 테이블 컴포넌트
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
  const handleRowClick = () => {
    setShowClusterName(true);
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
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="마이그레이션"
        className="migration_popup"
        overlayClassName="migration_popup_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="domain_header">
          <h1>가상머신 마이그레이션</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>
        <div id="migration_article_outer">
          <span>1대의 가상 머신이 마이그레이션되는 호스트를 선택하십시오.</span>
          <div id="migration_article">
            <div>
              <div id="migration_dropdown">
                <label htmlFor="host">대상 호스트 <i className="fa fa-info-circle"></i></label>
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
          <div id="migration_footer">
            <button>마이그레이션</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default Cluster;
