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
import { faArrowDown, faChevronDown, faExclamationTriangle, faRefresh, faSearch, faTimes} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';
import './css/AllDisk.css';
import { useAllDisk } from '../../api/RQHook';

Modal.setAppElement('#root');

const AllDisk = () => {
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

  const [isVisible, setIsVisible] = useState(false);
  const toggleContent = () => {
    setIsVisible(!isVisible);
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
    data: data,
    status: networksStatus,
    isRefetching: isNetworksRefetching,
    refetch: networksRefetch, 
    isError: isNetworksError, 
    error: networksError, 
    isLoading: isNetworksLoading,
  } = useAllDisk((item) => {
    return {
      id: item?.id ?? '',  // ID
      name: item?.name ?? '',  // 이름
      description: item?.description ?? '',  // 설명
      dataCenter: item?.dataCenterVo?.name ?? '',  // 데이터 센터
      provider: item?.provider ?? 'Provider1',  // 제공자 (기본값: 'Provider1')
      portSeparation: item?.portIsolation ? '예' : '아니요',  // 포트 분리 여부
      alias: item?.alias ?? '',  // 별칭
      icon1: item?.icon1 ?? '',  // 아이콘 1
      icon2: item?.icon2 ?? '',  // 아이콘 2
      connectionTarget: item?.connectionTarget ?? '',  // 연결 대상
      storageDomain: item?.storageDomainVo?.name ?? '',
      virtualSize: item?.virtualSize ?? '',  // 가상 크기
      status: item?.status ?? '',  // 상태
      type: item?.type ?? '',  // 유형
    }
  })


  const handleRowClick = (row, column) => {
        if (column.accessor === 'alias') {
            navigate(
              `/storages/disks/${row.id}`, 
              { state: { name: row.name } }
            );
        }
    };



  
  return (
    <div id="section">
      <HeaderButton
        title="Disk Chart"
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
                  <button id="storage_delete_btn" onClick={() => openPopup('delete')}>제거</button>
                  <button onClick={() => openPopup('move')}>이동</button>
                  <button onClick={() => openPopup('copy')}>복사</button>
                  <button id="storage_disk_upload" onClick={() => openPopup('uploadDisk')}>업로드</button>
                  <button>다운로드</button>
                  <button>LUN 새로고침</button>
                </div>
                <div className="search_box">
                  <input type="text" />
                  <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
                  <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
                </div>

                <div className="disk_type">
                  <div>
                    <span>디스크유형 : </span>
                    <div>
                      <button>모두</button>
                      <button>이미지</button>
                      <button className='mr-1'>직접 LUN</button>
                    </div>
                  </div>
                      <span className='mt-1'>컨텐츠 유형 : </span>
                      <div  className="content_type_btn">
                      <button onClick={toggleContent}>모두<FontAwesomeIcon icon={faChevronDown} fixedWidth /></button>
                      {isVisible && (
                        <div className='content_type'>
                          <div>모두</div>
                          <div>데이터</div>
                          <div>OVF 스토어</div>
                          <div className='border-b border-gray-400'>메모리 덤프</div>
                          <div>ISO</div>
                          <div>Hosted Engine</div>
                          <div>Hosted Engine Sanlock</div>
                          <div>Hosted Engine Metadata</div>
                          <div>Hosted Engine Conf.</div>
                        </div>
                      )}
                  </div>
                </div>

                <TableOuter 
                  columns={TableColumnsInfo.ALL_DISK}
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
        {/*관리되는 블록 (삭제예정)*/}
        {/* {activeTab === 'managed' && (
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
        )} */}
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
    </div>
    
  );
};

export default AllDisk;
