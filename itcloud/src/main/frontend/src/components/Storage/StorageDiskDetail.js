import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import Modal from 'react-modal';
import Permission from '../Modal/Permission';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faUser, fa1, fa2,
  faTimes,
  faExclamationTriangle
} from '@fortawesome/free-solid-svg-icons'
import './css/StorageDiskDetail.css';
import { useAllDisk } from '../../api/RQHook';

function StorageDisk({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const { name } = useParams();
  const { id } = useParams();

  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
  const [activeTab, setActiveTab] = useState('general'); // 초기값은 'general'로 설정
  const [activePopup, setActivePopup] = useState(null);  // activePopup 선언은 이 아래에 있어야 함
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 추가
  const [modalTab, setModalTab] = useState('img'); // 모달 창 내 탭 관리

  const handlePermissionFilterClick = (filter) => setActivePermissionFilter(filter);
  const handleTabClick = (tab) => setActiveTab(tab);
  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  const openPopup = (popupType) => {
    setActivePopup(popupType);
    if (popupType === 'newDisk') {
      setModalTab('img'); // 팝업 내 탭을 'img'로 설정
    }
  };

  const closePopup = () => {
    setActivePopup(null);
  };



  const buttons = [
    { id: 'edit_btn', label: '수정', onClick: () => openPopup('editDisk') },
    { id: 'new_btn', label: '제거', onClick: () => openPopup('delete') },
    { id: 'get_btn', label: '이동', onClick: () => openPopup('move') },
    { id: 'new_btn', label: '복사', onClick: () => openPopup('copy') },
    { id: 'get_btn', label: '업로드', onClick: () => console.log('Move button clicked') },
    { id: 'edit_btn', label: '다운로드', onClick: () => openPopup('editDisk') },
   
  ];
  

  const uploadOptions = [
    '옵션 1',
    '옵션 2',
    '옵션 3',
  ];


  const sections = [
    { id: 'general', label: '일반' },
    { id: 'machine', label: '가상머신' },
    { id: 'storage', label: '스토리지' }
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

  // api
  const { 
    data: disk,
    status: diskStatus,
    isRefetching: isDiskRefetching,
    refetch: diskRefetch,
    isError: isDiskError,
    error: diskError,
    isLoading: isDiskLoading,
  } = useAllDisk(id);
  
  useEffect(() => {
    diskRefetch();
  }, [diskRefetch]);



  return (
    <div className="content_detail_section">
      <HeaderButton
        title="디스크"
        subtitle="디스크이름"
        additionalText={name}
        buttons={buttons}
        popupItems={[]}
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

        
        </div>
      </div>
           {/*디스크(편집)팝업 */}
           <Modal
        isOpen={activePopup === 'editDisk'}
        onRequestClose={closePopup}
        contentLabel="새 가상 디스크"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_new_popup">
          <div className="popup_header">
            <h1>가상 디스크 수정</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          <div className="disk_new_nav">
            {/* 이미지 탭 */}
            <div
              id="storage_img_btn"
              onClick={() => setModalTab('img')} // 클릭 가능
              className={modalTab === 'img' ? 'active' : ''}
            >
              이미지
            </div>

            {/* 직접LUN 탭 - 클릭 불가 */}
            <div
              id="storage_directlun_btn"
              onClick={(e) => e.preventDefault()} // 클릭 불가
              className={`disabled ${modalTab === 'directlun' ? 'active' : ''}`}
            >
              직접LUN
            </div>

            {/* 관리되는 블록 탭 - 클릭 불가 */}
            <div
              id="storage_managed_btn"
              onClick={(e) => e.preventDefault()} // 클릭 불가
          
              className={`disabled ${modalTab === 'managed' ? 'active' : ''}`}
            >
              관리되는 블록
            </div>
          </div>


          {/*이미지*/}
          {modalTab === 'img' && (
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
                  <input type="checkbox" id="reset_after_deletion" />
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
                <div>
                  <input type="checkbox" id="incremental_backup" defaultChecked />
                  <label htmlFor="incremental_backup">중복 백업 사용</label>
                </div>
              </div>
            </div>
          )}
          {/*직접LUN*/}
          {modalTab === 'directlun' && (
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
          {/*관리되는 블록 */}
          {modalTab === 'managed' && (
            <div id="storage_managed_outer">
              <div id="disk_managed_block_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" disabled />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" value="on20-ap01_Disk1" disabled />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" disabled />
                </div>
                <div className="img_select_box">
                  <label htmlFor="data_center_select">데이터 센터</label>
                  <select id="data_center_select" disabled>
                    <option value="dc_linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="storage_domain_select">스토리지 도메인</label>
                  <select id="storage_domain_select" disabled>
                    <option value="sd_linux">Linux</option>
                  </select>
                </div>
                <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
              </div>
              <div id="disk_managed_block_right">
                <div>
                  <input type="checkbox" id="disk_shared_option" disabled />
                  <label htmlFor="disk_shared_option">공유 가능</label>
                </div>
              </div>
            </div>
          )}
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
        </Modal>
        {/*디스크(삭제)팝업 */}
      <Modal
        isOpen={activePopup === 'delete'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_delete_popup">
          <div className="popup_header">
            <h1>디스크 삭제</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
       {/*디스크(이동)팝업 */}
       <Modal
        isOpen={activePopup === 'move'}
        onRequestClose={closePopup}
        contentLabel="디스크 이동"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="disk_move_popup">
          <div className="popup_header">
            <h1>디스크 이동</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className="section_table_outer py-1">
              <table >
        <thead>
          <tr>
            <th>별칭</th>
            <th>가상 크기</th>
            <th>소스</th>
            <th>대상</th>
            <th>디스크 프로파일</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>he_sanlock</td>
            <td>1 GiB</td>
            <td>hosted_storage</td>
            <td>
              <select>
                <option>NFS (499 GiB)</option>
                <option>Option 2</option>
              </select>
            </td>
            <td>
              <select>
                <option>NFS</option>
                <option>Option 2</option>
              </select>
            </td>
          </tr>
        </tbody>
              </table>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
        {/*디스크(복사)팝업 */}
        <Modal
        isOpen={activePopup === 'copy'}
        onRequestClose={closePopup}
        contentLabel="디스크 복사"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="disk_move_popup">
          <div className="popup_header">
            <h1>디스크 복사</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className="section_table_outer py-1">
              <table >
                <thead>
                  <tr>
                    <th>별칭</th>
                    <th>가상 크기</th>
                    <th>소스</th>
                    <th>대상</th>
                    <th>디스크 프로파일</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>
                      <input type='text' value={'별칭'}/>
                    </td>
                    <td>1 GiB</td>
                    <td>
                      <select>
                        <option>hosted_storage</option>
                     
                      </select>
                    </td>
                    <td>
                      <select>
                        <option>NFS (499 GiB)</option>
                        <option>Option 2</option>
                      </select>
                    </td>
                    <td>
                      <select>
                        <option>NFS</option>
                     
                      </select>
                    </td>
                  </tr>
                </tbody>
              </table>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
      <Footer/>

      {/* 모달 컴포넌트 */}
      <Permission isOpen={isModalOpen} onRequestClose={handleCloseModal} />
    
    </div>
  );
}

export default StorageDisk;
