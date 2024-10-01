import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import ApiManager from '../../api/ApiManager';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCaretUp, faGlassWhiskey, faEllipsisV, faRefresh, faTimes
} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';

Modal.setAppElement('#root');

const DomainParts = () => {
  const navigate = useNavigate();
  const { name } = useParams();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [activePopup, setActivePopup] = useState(null);
  const [activeTab, setActiveTab] = useState('img');
  const openPopup = (popupType) => {
    setActivePopup(popupType);
  };
  const closePopup = () => {
    setActivePopup(null);
  };
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };


  /*
  const [data, setData] = useState([
    {
      status: <FontAwesomeIcon icon={faCaretUp} style={{ color: '#1DED00' }}fixedWidth/>,
      icon: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
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
  ])
  useEffect(() => {
    const fetchData = async () => {
        const res = await ApiManager.findAllStorageDomains() ?? []
        const items = res.map((e) => toTableItemPredicate(e))
        setData(items)
    }
    fetchData()
  }, [])
  */
  const { 
    data,
    status,
    isRefetching,
    refetch, 
    isError, 
    error, 
    isLoading
  } = useQuery({
    queryKey: ['allStorageDomains'],
    queryFn: async () => {
      const res = await ApiManager.findAllStorageDomains()
      return res?.map((e) => toTableItemPredicate(e)) ?? []
    },
    refetchOnMount: false,

  })

  const toTableItemPredicate = (e) => {
    return {
      status: <FontAwesomeIcon icon={faCaretUp} style={{ color: '#1DED00' }}fixedWidth/>,
      icon: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
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
    }
  }

  // 이름 열을 클릭했을 때 동작하는 함수
  // 행 클릭 시 도메인 이름을 이용하여 이동하는 함수
  const handleRowClick = (row, column) => {
    if (column.accessor === 'alias') {  // 'alias' 컬럼만 체크
        navigate(`/storage-disk/${row.alias.props.children}`);
    }
  };

  return (
    <div id="section">
      <HeaderButton
        title="스토리지 디스크"
        subtitle=""
        buttons={[]} 
        popupItems={[]} 
        togglePopup={() => {}}
      />
       <div className="host_btn_outer">
       <>
                <div className="content_header_right">
                  <button id="storage_disk_new_btn" onClick={() => openPopup('newDisk')}>새로 만들기</button>
                  <button>편집</button>
                  <button>제거</button>
                  <button>이동</button>
                  <button>복사</button>
                  <button id="storage_disk_upload" onClick={() => openPopup('uploadDisk')}>업로드</button>
                  <button>다운로드</button>
                  <button className="content_header_popup_btn">
                    <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                    <div className="content_header_popup" style={{ display: 'none' }}>
                      <div>활성</div>
                      <div>비활성화</div>
                      <div>이동</div>
                      <div>LUN 새로고침</div>
                    </div>
                  </button>
                </div>
                <TableOuter 
                  columns={TableColumnsInfo.STORAGES_FROM_DATACENTER}
                  data={data}
                  onRowClick={handleRowClick}
                />
              </>
        </div>
      <Footer/>

      {/*디스크(업로드)팝업 */}
      <Modal
        isOpen={activePopup === 'uploadDisk'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_upload_popup">
          <div className="popup_header">
            <h1>이미지 업로드</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          <div className="storage_upload_first">
            <button>파일 선택</button>
            <div>선택된 파일 없음</div>
          </div>
          <div className="storage_upload_second">
            <div className="disk_option">디스크 옵션</div>
            <div className="disk_new_img" style={{ paddingTop: '0.4rem' }}>
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" disabled />
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
                  <label htmlFor="data_hub">데이터 센터</label>
                  <select id="data_hub">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="storage_zone">스토리지 도메인</label>
                  <select id="storage_zone">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="disk_pattern">디스크 프로파일</label>
                  <select id="disk_pattern">
                    <option value="nfs_storage">NFS-Storage</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="compute_unit">호스트</label>
                  <select id="compute_unit">
                    <option value="host01">host01.ititinfo.com</option>
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
                <div style={{ marginBottom: '0.4rem' }}>
                  <input type="checkbox" id="incremental_backup" defaultChecked />
                  <label htmlFor="incremental_backup">중복 백업 사용</label>
                </div>
                <div>
                  <button>연결 테스트</button>
                </div>
              </div>
            </div>
          </div>
          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
         {/*디스크(새로만들기)팝업 */}
         <Modal
        isOpen={activePopup === 'newDisk'}
        onRequestClose={closePopup}
        contentLabel="새 가상 디스크"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_new_popup">
          <div className="popup_header">
            <h1>새 가상 디스크</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
          <div className="disk_new_nav">
            <div
              id="storage_img_btn"
              onClick={() => handleTabClick('img')}
              className={activeTab === 'img' ? 'active' : ''}
            >
              이미지
            </div>
            <div
              id="storage_directlun_btn"
              onClick={() => handleTabClick('directlun')}
              className={activeTab === 'directlun' ? 'active' : ''}
            >
              직접LUN
            </div>
            <div
              id="storage_managed_btn"
              onClick={() => handleTabClick('managed')}
              className={activeTab === 'managed' ? 'active' : ''}
            >
              관리되는 블록
            </div>
          </div>
          {/*이미지*/}
          {activeTab === 'img' && (
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
          {activeTab === 'directlun' && (
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
          {activeTab === 'managed' && (
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
    </div>
    
  );
};

export default DomainParts;
