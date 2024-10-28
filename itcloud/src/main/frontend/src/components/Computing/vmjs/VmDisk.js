import { faCaretUp, faChevronDown, faChevronLeft, faEllipsisV, faExclamationTriangle, faExternalLink, faGlassWhiskey, faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';
import TableColumnsInfo from '../../table/TableColumnsInfo';


// 디스크
const VmDisk = () => {

    const [isJoinDiskModalOpen, setIsJoinDiskModalOpen] = useState(false);
    const [activeTab, setActiveTab] = useState('img');
  
    const handleTabClick = (tab) => setActiveTab(tab);
  
    const [isVisible, setIsVisible] = useState(false);
    const toggleContent = () => {
      setIsVisible(!isVisible);
    };
  
    const [activeDiskType, setActiveDiskType] = useState('all');
  const handleDiskTypeClick = (type) => {
    setActiveDiskType(type);  // 여기서 type을 설정해야 함
  };
  
    // 연결 팝업 열기/닫기 핸들러

    const closeJoinDiskModal = () => setIsJoinDiskModalOpen(false);
   
    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => {
        setActivePopup(popupType);
      };
    const closePopup = () => {
        setActivePopup(null);
    };

    const [activeContentType, setActiveContentType] = useState('all'); // 컨텐츠 유형 상태

  // 컨텐츠 유형 변경 핸들러
  const handleContentTypeChange = (event) => {
    setActiveContentType(event.target.value);
  };
  const [activeJoinTab, setActiveJoinTab] = useState('image'); 

    // 테이블 컴포넌트
    const columns = [
      { header: '', accessor: 'statusIcon', clickable: false },
      { header: '변경', accessor: 'change', clickable: false },
      { header: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>, accessor: 'glass1', clickable: false },
      { header: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>, accessor: 'glass2', clickable: false },
      { header: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>, accessor: 'glass3', clickable: false },
      { header: '가상 크기', accessor: 'virtualSize', clickable: false },
      { header: '연결 대상', accessor: 'connectionTarget', clickable: false },
      { header: '인터페이스', accessor: 'interface', clickable: false },
      { header: '논리적 이름', accessor: 'logicalName', clickable: false },
      { header: '상태', accessor: 'status', clickable: false },
      { header: '유형', accessor: 'type', clickable: false },
      { header: '설명', accessor: 'description', clickable: false },
    ];
  
    const data = [
      {
        statusIcon: <FontAwesomeIcon icon={faCaretUp} style={{ color: '#1DED00' }}fixedWidth/>,
        change: 'on20-ap01',
        glass1: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
        glass2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
        glass3: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
        virtualSize: 'on20-ap01',
        connectionTarget: 'VirtIO-SCSI',
        interface: '/dev/sda',
        logicalName: 'OK',
        status: '이미지',
        type: '',
        description: '',
      },
    ];

      // 연결 데이터
      // 이미지
      const imageDiskColumns = [
        { header: '별칭', accessor: 'alias' },
        { header: '가상 크기', accessor: 'virtualSize' },
        { header: '실제 크기', accessor: 'actualSize' },
        { header: '스토리지 도메인', accessor: 'storageDomain' },
        { header: '생성 일자', accessor: 'createdDate' },
      ];
    
      const imageDiskData = [
        {
          alias: 'Disk1',
          virtualSize: '10 GiB',
          actualSize: '8 GiB',
          storageDomain: 'Storage1',
          createdDate: '2023-10-01',
        },
      ];
        // LUN 테이블 컬럼 및 데이터
  const lunDiskColumns = [
    { header: 'LUN ID', accessor: 'lunId' },
    { header: '가상 크기', accessor: 'virtualSize' },
    { header: '실제 크기', accessor: 'actualSize' },
    { header: '호스트', accessor: 'host' },
    { header: '생성 일자', accessor: 'createdDate' },
  ];

  const lunDiskData = [
    {
      lunId: 'LUN1',
      virtualSize: '500 GiB',
      actualSize: '480 GiB',
      host: 'Host1',
      createdDate: '2023-09-15',
    },
  ];
    return (
        <>
                <div className="header_right_btns">
                    <button id="disk_popup_new"  onClick={() => openPopup('newDisk')}>새로 만들기</button>
                    <button id="join_popup_btn" onClick={() => openPopup('disk_connection')}>연결</button>
                    <button onClick={() => openPopup('disk_edit')}>수정</button>
                    <button onClick={() => openPopup('delete')}>제거</button>
                    {/* <button className="content_header_popup_btn">
                        <FontAwesomeIcon icon={faEllipsisV} fixedWidth/>
                        <div className="content_header_popup" style={{ display: 'none' }}>
                            <div>활성</div>
                            <div>비활성화</div>
                            <div>이동</div>
                            <div>LUN 새로고침</div>
                        </div>
                    </button> */}
                </div>
                <div className="disk_type">
                  <div>
                    <span>디스크유형 : </span>
                    <div className='flex'>
                      <button className={activeDiskType === 'all' ? 'active' : ''} onClick={() => handleDiskTypeClick('all')}>모두</button>
                      <button className={activeDiskType === 'image' ? 'active' : ''} onClick={() => handleDiskTypeClick('image')}>이미지</button>
                      <button style={{ marginRight: '0.2rem' }} className={activeDiskType === 'lun' ? 'active' : ''} onClick={() => handleDiskTypeClick('lun')}>직접 LUN</button>
                    </div>
                  </div>
                  <div className="content_type">
                    <label className='mr-1' htmlFor="contentType">컨텐츠 유형:</label>
                    <select id="contentType" value={activeContentType} onChange={handleContentTypeChange}>
                      <option value="all">모두</option>
                      <option value="data">데이터</option>
                      <option value="ovfStore">OVF 스토어</option>
                      <option value="memoryDump">메모리 덤프</option>
                      <option value="iso">ISO</option>
                      <option value="hostedEngine">Hosted Engine</option>
                      <option value="sanlock">Hosted Engine Sanlock</option>
                      <option value="metadata">Hosted Engine Metadata</option>
                      <option value="conf">Hosted Engine Conf.</option>
                    </select>
                  </div>
                </div>

                {activeDiskType === 'all' && (
                  <TableOuter 
                    columns={TableColumnsInfo.ALL_DISK}
                    data={data}
                    onRowClick={() => console.log('Row clicked')}
                  />
                )}

                {activeDiskType === 'image' && (
                  <TableOuter 
                    columns={TableColumnsInfo.IMG_DISK}
                    data={data}
                    onRowClick={() => console.log('Row clicked')}
                  />
                )}

                {activeDiskType === 'lun' && (
                  <TableOuter 
                    columns={TableColumnsInfo.LUN_DISK}
                    data={data}
                    onRowClick={() => console.log('Row clicked')}
                  />
                )}
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
                            <input type="text" value='#' />
                        </div>
                        <div className="img_input_box">
                            <span>설명</span>
                            <input type="text" />
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="interface">인터페이스</label>
                            <select id="interface">
                                <option value="#">#</option>
                            </select>
                            </div>
                            <div className="img_select_box">
                            <label htmlFor="storageDomain">스토리지 도메인</label>
                            <select id="storageDomain">
                                <option value="#">#</option>
                            </select>
                            </div>
                            <div className="img_select_box">
                            <label htmlFor="allocationPolicy">할당 정책</label>
                            <select id="allocationPolicy">
                                <option value="#">#</option>
                            </select>
                            </div>
                            <div className="img_select_box">
                            <label htmlFor="diskProfile">디스크 프로파일</label>
                            <select id="diskProfile">
                                <option value="#">#</option>
                            </select>
                        </div>
                    </div>
                    <div className="disk_new_img_right">
                    <div>
                      <input type="checkbox" id="disk_activation" defaultChecked />
                      <label htmlFor="disk_activation">디스크 활성화</label>
                    </div>
                    <div>
                      <input type="checkbox" id="reset_after_deletion" />
                      <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                    </div>
                    <div>
                      <input type="checkbox" id="bootable" disabled/>
                      <label htmlFor="bootable">부팅 가능</label>
                    </div>
                    <div>
                      <input type="checkbox" id="shareable" />
                      <label htmlFor="shareable">공유 가능</label>
                    </div>
                    <div>
                      <input type="checkbox" id="read_only" />
                      <label htmlFor="read_only">읽기전용</label>
                    </div>
                    <div>
                      <input type="checkbox" id="snapshot_activation" />
                      <label htmlFor="snapshot_activation">취소 활성화</label>
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
            
                <div className="edit_footer">
                <button style={{ display: 'none' }}></button>
                <button>OK</button>
                <button onClick={closePopup}>취소</button>
                </div>
            </div>
            </Modal>
  
            {/* 디스크(연결) 팝업 */}
            <Modal
        isOpen={activePopup === 'disk_connection'}
        onRequestClose={closePopup}
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="storage_disk_new_popup">
          <div className="popup_header">
            <h1>가상 디스크 연결</h1>
            <button onClick={closePopup}>
              <FontAwesomeIcon icon={faTimes} fixedWidth />
            </button>
          </div>
          
          {/* 탭 선택 */}
          <div id="join_header">
            <div className="disk_new_nav">
    <div
      id="storage_img_btn"
      onClick={() => handleTabClick('img')}
      className={activeTab === 'img' ? 'active' : ''}
      style={{
        color: activeTab === 'img' ? 'rgb(35, 132, 243)' : '',
        fontWeight: activeTab === 'img' ? '600' : '',
        borderBottom: activeTab === 'img' ? '2px solid rgb(35, 132, 243)' : 'none',
      }}
    >
      이미지
    </div>
    <div
      id="storage_directlun_btn"
      onClick={() => handleTabClick('directlun')}
      className={activeTab === 'directlun' ? 'active' : ''}
      style={{
        color: activeTab === 'directlun' ? 'rgb(35, 132, 243)' : '',
        fontWeight: activeTab === 'directlun' ? '600' : '',
        borderBottom: activeTab === 'directlun' ? '2px solid rgb(35, 132, 243)' : 'none',
      }}
    >
      직접LUN
    </div>
            </div>
          </div>
            {/* 이미지 테이블 */}
            {activeTab === 'img' && (
              <TableOuter columns={imageDiskColumns} data={imageDiskData} />
            )}

            {/* LUN 테이블 */}
            {activeTab === 'directlun' && (
              <TableOuter columns={lunDiskColumns} data={lunDiskData} />
            )}


          <div className="edit_footer">
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
       
            </Modal>
            {/*디스크(수정)팝업 */}
            <Modal
            isOpen={activePopup === 'disk_edit'}
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
                  className={activeTab === 'directlun' ? 'active' : 'disabled'}
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
        </>
    );
  };
  export default VmDisk;