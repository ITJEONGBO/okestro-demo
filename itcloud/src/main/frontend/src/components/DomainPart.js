import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from './button/HeaderButton';
import { Table } from './table/Table';
import Footer from './footer/Footer';
import { useParams } from 'react-router-dom';

Modal.setAppElement('#root');

const DomainParts = () => {
    const { name } = useParams();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  // 도메인 테이블 컴포넌트 
  const domaincolumns = [
    { header: '상태', accessor: 'status' },
    { header: '', accessor: 'icon' },
    { header: '도메인 이름', accessor: 'domainName' },
    { header: '코멘트', accessor: 'comment' },
    { header: '도메인 유형', accessor: 'domainType' },
    { header: '스토리지 유형', accessor: 'storageType' },
    { header: '포맷', accessor: 'format' },
    { header: '데이터 센터간 상태', accessor: 'dataCenterStatus' },
    { header: '전체 공간(GB)', accessor: 'totalSpace' },
    { header: '여유 공간(GB)', accessor: 'freeSpace' },
    { header: '확보된 여유 공간(GB)', accessor: 'reservedSpace' },
    { header: '설명', accessor: 'description' },
  ];

  const domaindata = [
    {
      status: <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>,
      icon: <i className="fa fa-glass"></i>,
      domainName: 'ㅁㅎㅇㅁㄹㄹ', // 여기에 도메인 이름을 설정합니다.
      comment: '',
      domainType: '',
      storageType: '',
      format: '',
      dataCenterStatus: '',
      totalSpace: '',
      freeSpace: '',
      reservedSpace: '',
      description: '',
    },
  ];

  // 이름 열을 클릭했을 때 동작하는 함수
  // 행 클릭 시 도메인 이름을 이용하여 이동하는 함수
  const handleDomainClick = (row) => {
    navigate(`/storage-domain/${row.domainName}`);
  };

  return (
    <div id="section">
      <HeaderButton
        title="스토리지도메인"
        subtitle=""
        buttons={[]} 
        popupItems={[]} 
        openModal={openModal}
        togglePopup={() => {}}
      />
       <div className="content_outer">
        <div className="empty_nav_outer">
                <div className="content_header_right">
                  <button id="new_domain_btn" onClick={() => openModal('newDomain')}>새로운 도메인</button>
                  <button id="get_domain_btn" onClick={() => openModal('getDomain')}>도메인 가져오기</button>
                  <button id="administer_domain_btn" onClick={() => openModal('manageDomain')}>도메인 관리</button>
                  <button>삭제</button>
                  <button>Connections</button>
                  <button className="content_header_popup_btn">
                    <i className="fa fa-ellipsis-v"></i>
                    <div className="content_header_popup" style={{ display: 'none' }}>
                      <div>활성</div>
                      <div>비활성화</div>
                      <div>이동</div>
                      <div>LUN 새로고침</div>
                    </div>
                  </button>
                </div>

                <div className="section_table_outer">
                  <div className="search_box">
                    <input type="text" />
                    <button><i className="fa fa-search"></i></button>
                  </div>
                  <Table columns={domaincolumns} data={domaindata} onRowClick={handleDomainClick} />
                </div>
            </div>
        </div>
      <Footer/>
    </div>
  );
};

export default DomainParts;
