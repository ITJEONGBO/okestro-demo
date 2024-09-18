import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import Permission from '../Modal/Permission';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faUser, fa1, fa2
} from '@fortawesome/free-solid-svg-icons'
import './css/StorageDiskDetail.css';

function StorageDisk({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const { name } = useParams();

  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const [activeTab, setActiveTab] = useState('general');
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 추가

  const handlePermissionFilterClick = (filter) => setActivePermissionFilter(filter);
  const handleTabClick = (tab) => setActiveTab(tab);
  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  const buttons = [
    { id: 'edit_btn', label: '편집', onClick: () => console.log('Edit button clicked') },
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
    'Export to Data Domain',
    'OVA로 내보내기',
  ];

  const sections = [
    { id: 'general', label: '일반' },
    { id: 'machine', label: '가상머신' },
    { id: 'storage', label: '스토리지' },
    { id: 'permission', label: '권한' },
  ];

  const vmData = [];
  const storageData = [
    {
      icon1: <FontAwesomeIcon icon={fa1} fixedWidth/>,
      icon2: <FontAwesomeIcon icon={fa2} fixedWidth/>,
      domainName: name,
      domainType: '데이터 (마스터)',
      status: '활성화',
      freeSpace: '83 GiB',
      usedSpace: '16 GiB',
      totalSpace: '99 GiB',
      description: '',
    },
  ];

  const permissionData = [
    {
      icon: <FontAwesomeIcon icon={faUser} fixedWidth/>,
      user: 'ovirtmgmt',
      authProvider: '',
      namespace: '*',
      role: 'SuperUser',
      createdDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
    },
  ];

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        !event.target.closest('.upload_option_box') &&
        !event.target.closest('.upload_option_boxbtn')
      ) {
        // 여기에 원래 setUploadOptionBoxVisible(false) 관련 코드가 있었다면 삭제합니다.
      }
    };
  
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div className="content_detail_section">
      <HeaderButton
        title="디스크"
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
              <TableOuter 
                columns={TableColumnsInfo.VMS_FROM_DISK} 
                data={vmData}
                onRowClick={() => console.log('Row clicked')} 
              />
            </div>
          )}

          {activeTab === 'storage' && (
            <div className="host_empty_outer">
              <TableOuter 
                columns={TableColumnsInfo.STORAGES_FROM_DISK} 
                data={storageData}
                onRowClick={() => console.log('Row clicked')} 
              />
            </div>
          )}

          {activeTab === 'permission' && (
            <>
              <div className="content_header_right">
                <button onClick={handleOpenModal}>추가</button> {/* 추가 버튼 */}
                <button>제거</button>
              </div>
              <div className="host_filter_btns">
                <span>Permission Filters:</span>
                <div>
                  <button
                    className={activePermissionFilter === 'all' ? 'active' : ''}
                    onClick={() => handlePermissionFilterClick('all')}
                  >
                    All
                  </button>
                  <button
                    className={activePermissionFilter === 'direct' ? 'active' : ''}
                    onClick={() => handlePermissionFilterClick('direct')}
                  >
                    Direct
                  </button>
                </div>
              </div>
              <TableOuter
                columns={TableColumnsInfo.PERMISSIONS}
                data={activePermissionFilter === 'all' ? permissionData : []}
                onRowClick={() => console.log('Row clicked')}
              />
            </>
          )}
        </div>
      </div>
      
      <Footer/>

      {/* 모달 컴포넌트 */}
      <Permission isOpen={isModalOpen} onRequestClose={handleCloseModal} />
    </div>
  );
}

export default StorageDisk;
