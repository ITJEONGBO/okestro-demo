import { faCaretUp, faEllipsisV, faExclamationTriangle, faExternalLink, faGlassWhiskey, faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';


// 디스크
const DiskSection = () => {

    const [isJoinDiskModalOpen, setIsJoinDiskModalOpen] = useState(false);
    const [activeTab, setActiveTab] = useState('img');
  
    const handleTabClick = (tab) => setActiveTab(tab);
  
   
  
    // 연결 팝업 열기/닫기 핸들러

    const closeJoinDiskModal = () => setIsJoinDiskModalOpen(false);
   
    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => {
        setActivePopup(popupType);
      };
    const closePopup = () => {
        setActivePopup(null);
    };
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
    return (
        <>
                <div className="content_header_right">
                    <button id="disk_popup_new"  onClick={() => openPopup('newDisk')}>새로 만들기</button>
                    <button id="join_popup_btn" onClick={() => openPopup('disk_connection')}>연결</button>
                    <button onClick={() => openPopup('disk_edit')}>편집</button>
                    <button onClick={() => openPopup('delete')}>제거</button>
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
                <div className='disk_type'>
                    <div>
                    <span>디스크유형 : </span>
                    <div>
                      <button>모두</button>
                      <button>이미지</button>
                      <button className='mr-1'>직접 LUN</button>
                    </div>
                    </div>
                </div>
                <TableOuter
                  columns={columns}
                  data={data}
                  onRowClick={() => console.log('Row clicked')} 
                />
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
      contentLabel="새 가상 디스크"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
          <div className="storage_disk_new_popup">
                <div className="popup_header">
                    <h1>가상 디스크 연결</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
  
                <div id="join_header">
                    <div id="join_new_nav">
                        <div id="img_btn2">이미지</div>
                        <div id="directlun_btn2">직접LUN</div>
                        <div id="managed_block_btn2">관리되는 블록</div>
                    </div>
                    <div>
                        <input type="checkbox" id="diskActivation" defaultChecked />
                        <label htmlFor="diskActivation">디스크 활성화</label>
                    </div>
                </div>
  
                <div id="join_img_content">
                    <table>
                        <thead>
                            <tr id="join_img_th">
                                <th>별칭</th>
                                <th>설명</th>
                                <th>ID</th>
                                <th>가상 크기</th>
                                <th>실제 크기</th>
                                <th>스토리지 도메인</th>
                                <th>인터페이스</th>
                                <th>R/O</th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                            </tr>
                            <tr id="join_directlun_th" style={{ display: 'none' }}>
                                <th>별칭</th>
                                <th>설명</th>
                                <th>LUN ID</th>
                                <th>ID</th>
                                <th>크기</th>
                                <th>#경로</th>
                                <th>벤더ID</th>
                                <th>제품ID</th>
                                <th>시리얼</th>
                                <th>인터페이스</th>
                                <th>R/O</th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                            </tr>
                            <tr id="join_managed_th" style={{ display: 'none' }}>
                                <th>별칭</th>
                                <th>설명</th>
                                <th>ID</th>
                                <th>가상 크기</th>
                                <th>스토리지 도메인</th>
                                <th>인터페이스</th>
                                <th>R/O</th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                                <th><FontAwesomeIcon icon={faExternalLink} fixedWidth/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr className="join_img_td">
                                <td>OK</td>
                                <td>test02</td>
                                <td>asd</td>
                                <td>5</td>
                                <td></td>
                                <td>소프트</td>
                                <td></td>
                                <td>소프트</td>
                                <td>멤버없음</td>
                                <td>레이블없음</td>
                            </tr>
                            <tr className="join_directlun_td" style={{ display: 'none' }}>
                                <td>OK</td>
                                <td>test02</td>
                                <td>asd</td>
                                <td>5</td>
                                <td></td>
                                <td>소프트</td>
                                <td></td>
                                <td>소프트</td>
                                <td>멤버없음</td>
                                <td>레이블없음</td>
                                <td>레이블없음</td>
                                <td>레이블없음</td>
                                <td>레이블없음</td>
                            </tr>
                            <tr className="join_managed_td" style={{ display: 'none' }}>
                                <td>OK</td>
                                <td>test02</td>
                                <td>asd</td>
                                <td>5</td>
                                <td></td>
                                <td>소프트</td>
                                <td></td>
                                <td>소프트</td>
                                <td>멤버없음</td>
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

            {/*디스크(편집)팝업 */}
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
  export default DiskSection;