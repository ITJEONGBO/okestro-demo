import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';
import Table from '../components/table/Table';
import TableColumnsInfo from '../components/table/TableColumnsInfo';
import './css/StorageDisk.css';
import Footer from '../components/footer/Footer';

function StorageDisk({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const { name } = useParams(); // URL에서 name 파라미터를 가져옵니다.

  const handlePermissionFilterClick = (filter) => {
    setActivePermissionFilter(filter);
  };
  const [activePermissionFilter, setActivePermissionFilter] = useState('all');

  // 테이블 컴포넌트
  // 가상머신
  const vmData = [];

  // 스토리지
  const storageData = [
    {
      icon1: <i className="fa fa-icon1"></i>,
      icon2: <i className="fa fa-icon2"></i>,
      domainName: name,
      domainType: '데이터 (마스터)',
      status: '활성화',
      freeSpace: '83 GiB',
      usedSpace: '16 GiB',
      totalSpace: '99 GiB',
      description: '',
    },
    // 추가 데이터
  ];


  // 권한
  const permissionData = [
    {
      icon: <i className="fa fa-user"></i>,
      user: 'ovirtmgmt',
      authProvider: '',
      namespace: '*',
      role: 'SuperUser',
      createdDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
    },
  ];


  const [activeTab, setActiveTab] = useState('general');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  // HeaderButton 컴포넌트
  const buttons = [
    { id: 'edit_btn', label: '수정', onClick: () => console.log('Edit button clicked') },
    { id: 'remove_btn', label: '제거', onClick: () => console.log('Remove button clicked') },
    { id: 'move_btn', label: '이동', onClick: () => console.log('Move button clicked') },
    { id: 'copy_btn', label: '복사', onClick: () => console.log('Copy button clicked') },
  ];

  const uploadOptions = [
    '옵션 1',
    '옵션 2',
    '옵션 3',
  ];

  const popupItems = [
    '가져오기',
    '가상 머신 복제',
    '삭제',
    '마이그레이션 취소',
    '변환 취소',
    '템플릿 생성',
    '도메인으로 내보내기',
    'Export to Data Domai',
    'OVA로 내보내기',
  ];

  // Nav 컴포넌트
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'machine', label: '가상머신' },
    { id: 'storage', label: '스토리지' },
    { id: 'permission', label: '권한' },
  ];

  // 옵션박스 열고닫기
  const [isUploadOptionBoxVisible, setUploadOptionBoxVisible] = useState(false);
  const toggleUploadOptionBox = () => {
    setUploadOptionBoxVisible(!isUploadOptionBoxVisible);
  };

  // 바탕 클릭하면 옵션박스 닫기
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        isUploadOptionBoxVisible &&
        !event.target.closest('.upload_option_box') &&
        !event.target.closest('.upload_option_boxbtn')
      ) {
        setUploadOptionBoxVisible(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isUploadOptionBoxVisible]);

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="스토리지디스크"
        subtitle={name}
        additionalText={name}
        buttons={buttons}
        popupItems={popupItems}
        uploadOptions={uploadOptions}
      />

      <div className="content_outer">
        <NavButton 
          sections={sections} 
          activeSection={activeTab} 
          handleSectionClick={handleTabClick} 
        />
        <div className="host_btn_outer">
          {activeTab === 'general' && (
            <div className="tables">
              <div className="table_container_center">
                <table className="table">
                  <tbody>
                    <tr>
                      <th>별칭:</th>
                      <td>{name}</td>
                    </tr>
                    <tr>
                      <th>설명:</th>
                      <td>Hosted-Engine metadata disk</td>
                    </tr>
                    <tr>
                      <th>ID:</th>
                      <td>b7bad714-6d4d-4fbf-83a4-f4b1eff449b3</td>
                    </tr>
                    <tr>
                      <th>디스크 프로파일:</th>
                      <td>hosted_storage</td>
                    </tr>
                    <tr>
                      <th>삭제 후 초기화:</th>
                      <td>아니요</td>
                    </tr>
                    <tr>
                      <th>가상 크기:</th>
                      <td>&lt; 1 GiB</td>
                    </tr>
                    <tr>
                      <th>실제 크기:</th>
                      <td>&lt; 1 GiB</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {activeTab === 'machine' && (
            <div className="host_empty_outer">
              <div className="section_table_outer">
                <Table columns={TableColumnsInfo.VMS_FROM_DISK} data={vmData} onRowClick={() => console.log('Row clicked')} />
              </div>
            </div>
          )}

          {activeTab === 'storage' && (
            <div className="host_empty_outer">
              <div className="section_table_outer">
                <Table columns={TableColumnsInfo.STORAGES_FROM_DISK} data={storageData} onRowClick={() => console.log('Row clicked')} />
              </div>
            </div>
          )}


          {activeTab === 'permission' && (
            <>
              <div className="content_header_right">
                <button>추가</button>
                <button>제거</button>
              </div>
              
              <div className="section_table_outer">
                <div className="storage_right_btns">
                  <span>Permission Filters:</span>
                  <div>
                    <button>All</button>
                    <button>Direct</button>
                  </div>
                </div>
                <Table columns={TableColumnsInfo.PERMISSIONS} data={permissionData} onRowClick={() => console.log('Row clicked')} />
              </div>
            </>
          )}

        </div>
      </div>
      <Footer/>
    </div>
  );
}

export default StorageDisk;
