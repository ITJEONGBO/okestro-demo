import { faCaretUp, faEllipsisV, faExternalLink, faGlassWhiskey, faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import Table from '../../table/Table';



// 디스크
const DiskSection = () => {
    const [isNewDiskModalOpen, setIsNewDiskModalOpen] = useState(false);
    const [isJoinDiskModalOpen, setIsJoinDiskModalOpen] = useState(false);
    const [activeTab, setActiveTab] = useState('img');
  
    const handleTabClick = (tab) => setActiveTab(tab);
  
    // 새로 만들기 팝업 열기/닫기 핸들러
    const openNewDiskModal = () => setIsNewDiskModalOpen(true);
    const closeNewDiskModal = () => setIsNewDiskModalOpen(false);
  
    // 연결 팝업 열기/닫기 핸들러
    const openJoinDiskModal = () => setIsJoinDiskModalOpen(true);
    const closeJoinDiskModal = () => setIsJoinDiskModalOpen(false);
  
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
                    <button id="disk_popup_new" onClick={openNewDiskModal}>새로 만들기</button>
                    <button id="join_popup_btn" onClick={openJoinDiskModal}>연결</button>
                    <button>수정</button>
                    <button>제거</button>
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
  
                <div className="section_table_outer">
                  <Table columns={columns} data={data} onRowClick={() => console.log('Row clicked')} />
                </div>
            
            
            
            {/* 디스크(새로 만들기) 팝업 */}
            <Modal
                isOpen={isNewDiskModalOpen}
                onRequestClose={closeNewDiskModal}
                contentLabel="새로 만들기"
                className="disk_popup"
                overlayClassName="disk_popup_outer"
                shouldCloseOnOverlayClick={false}
            >
                <div className="popup_header">
                    <h1>새 가상 디스크</h1>
                    <button onClick={closeNewDiskModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                <div id="disk_new_nav">
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
                <div className="disk_new_img" style={{ display: activeTab === 'img' ? 'block' : 'none' }}>
                    <div className="disk_new_img_left">
                        <div className="img_input_box">
                            <span>크기(GIB)</span>
                            <input type="text"/>
                        </div>
                        <div className="img_input_box">
                            <span>별칭</span>
                            <input type="text"/>
                        </div>
                        <div className="img_input_box">
                            <span>설명</span>
                            <input type="text"/>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="os">운영 시스템</label>
                            <select id="os">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="storage_domain">스토리지 도메인</label>
                            <select id="storage_domain">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="allocation_policy">할당 정책</label>
                            <select id="allocation_policy">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="disk_profile">디스크 프로파일</label>
                            <select id="disk_profile">
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                    </div>
                    <div className="disk_new_img_right">
                        <div>
                            <input type="checkbox" className="disk_activation" defaultChecked/>
                            <label htmlFor="disk_activation">디스크 활성화</label>
                        </div>
                        <div>
                            <input type="checkbox" id="reset_after_deletion"/>
                            <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div>
                            <input type="checkbox" className="bootable" disabled/>
                            <label htmlFor="bootable" style={{ color: 'gray' }}>부팅 가능</label>
                        </div>
                        <div>
                            <input type="checkbox" className="shareable"/>
                            <label htmlFor="shareable">공유 가능</label>
                        </div>
                        <div>
                            <input type="checkbox" className="read_only"/>
                            <label htmlFor="read_only">읽기전용</label>
                        </div>
                        <div>
                            <input type="checkbox" id="cancellable"/>
                            <label htmlFor="cancellable">취소 활성화</label>
                        </div>
                        <div>
                            <input type="checkbox" id="incremental_backup" defaultChecked/>
                            <label htmlFor="incremental_backup">중복 백업 사용</label>
                        </div>
                    </div>
                </div>
  
                <div id="directlun_outer" style={{ display: activeTab === 'directlun' ? 'block' : 'none' }}>
                    {/* 직접LUN 내용 */}
                    <div>
                        <div id="disk_managed_block_left">
                            <div className="img_input_box">
                                <span>별칭</span>
                                <input type="text" defaultValue="on20-ap01_Disk1"/>
                            </div>
                            <div className="img_input_box">
                                <span>설명</span>
                                <input type="text"/>
                            </div>
                            <div className="img_select_box">
                                <label htmlFor="interface">인터페이스</label>
                                <select id="interface">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div className="img_select_box">
                                <label htmlFor="host">호스트</label>
                                <select id="host">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                            <div className="img_select_box">
                                <label htmlFor="storage_type">스토리지타입</label>
                                <select id="storage_type">
                                    <option value="linux">Linux</option>
                                </select>
                            </div>
                        </div>
                        <div id="disk_managed_block_right">
                            <div>
                                <input type="checkbox" className="disk_activation" defaultChecked/>
                                <label htmlFor="disk_activation">디스크 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" className="bootable" disabled/>
                                <label htmlFor="bootable">부팅 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" className="shareable"/>
                                <label htmlFor="shareable">공유 가능</label>
                            </div>
                            <div>
                                <input type="checkbox" className="read_only"/>
                                <label htmlFor="read_only">읽기전용</label>
                            </div>
                            <div>
                                <input type="checkbox" className="read_only" disabled/>
                                <label htmlFor="read_only">취소 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" className="read_only"/>
                                <label htmlFor="read_only">SCSI 통과 활성화</label>
                            </div>
                            <div>
                                <input type="checkbox" className="read_only" disabled/>
                                <label htmlFor="read_only">권한 부여</label>
                            </div>
                            <div>
                                <input type="checkbox" className="read_only" disabled/>
                                <label htmlFor="read_only">SCSI 혜택사용</label>
                            </div>
                        </div>
                    </div>
                    <div className="target_search">
                        <div className="target_buttons">
                            <div>대상 &gt; LUN</div>
                            <div>LUN &gt; 대상</div>
                        </div>
                        <div className="target_info">
                            <div>
                                <div style={{ marginBottom: '0.2rem' }}>
                                    <span>주소</span>
                                    <input type="text"/>
                                </div>
                                <div>
                                    <span>포트</span>
                                    <input type="text"/>
                                </div>
                            </div>
                            <div>
                                <div>
                                    <input type="checkbox" className="disk_activation"/>
                                    <label htmlFor="disk_activation">사용자 인증</label>
                                </div>
                                <div>
                                    <div className="target_input_text" style={{ marginBottom: '0.1rem' }}>
                                        <span>CHWP사용자 이름</span>
                                        <input type="text" disabled/>
                                    </div>
                                    <div className="target_input_text">
                                        <span>CHAP 암호</span>
                                        <input type="text" disabled/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="disk_search_btn">
                            <div>검색</div>
                            <div>전체 로그인</div>
                        </div>
                        <div className="target_table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>대상 이름</th>
                                        <th>주소</th>
                                        <th>포트</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <label htmlFor="diskActivation">디스크 활성화</label>
                                        </td>
                                        <td>
                                            <label htmlFor="diskActivation">디스크 활성화</label>
                                        </td>
                                        <td>
                                            <label htmlFor="diskActivation">디스크 활성화</label>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
  
                <div id="managed_block_outer" style={{ display: activeTab === 'managed' ? 'block' : 'none' }}>
                    {/* 관리되는 블록 내용 */}
                    <div id="disk_managed_block_left">
                        <div className="img_input_box">
                            <span>크기(GIB)</span>
                            <input type="text" disabled/>
                        </div>
                        <div className="img_input_box">
                            <span>별칭</span>
                            <input type="text" defaultValue="on20-ap01_Disk1" disabled/>
                        </div>
                        <div className="img_input_box">
                            <span>설명</span>
                            <input type="text" disabled/>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="allocation_policy">할당 정책</label>
                            <select id="allocation_policy" disabled>
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <div className="img_select_box">
                            <label htmlFor="disk_profile">디스크 프로파일</label>
                            <select id="disk_profile" disabled>
                                <option value="linux">Linux</option>
                            </select>
                        </div>
                        <span>해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이 없습니다.</span>
                    </div>
                    <div id="disk_managed_block_right">
                        <div>
                            <input type="checkbox" className="disk_activation" disabled/>
                            <label htmlFor="disk_activation">디스크 활성화</label>
                        </div>
                        <div>
                            <input type="checkbox" className="bootable" disabled/>
                            <label htmlFor="bootable">부팅 가능</label>
                        </div>
                        <div>
                            <input type="checkbox" className="shareable" disabled/>
                            <label htmlFor="shareable">공유 가능</label>
                        </div>
                        <div>
                            <input type="checkbox" className="read_only" disabled/>
                            <label htmlFor="read_only">읽기전용</label>
                        </div>
                    </div>
                </div>
                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button onClick={closeNewDiskModal}>취소</button>
                </div>
            </Modal>
  
            {/* 디스크(연결) 팝업 */}
            <Modal
                isOpen={isJoinDiskModalOpen}
                onRequestClose={closeJoinDiskModal}
                contentLabel="연결"
                className="join_popup"
                overlayClassName="join_popup_outer"
            >
                <div className="network_popup_header">
                    <h1>가상 디스크 연결</h1>
                    <button onClick={closeJoinDiskModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                    <button onClick={closeJoinDiskModal}>취소</button>
                </div>
            </Modal>
        </>
    );
  };
  export default DiskSection;