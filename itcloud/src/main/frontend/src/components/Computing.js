import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';

import './Computing.css';

// React Modal 설정
Modal.setAppElement('#root');

// 네트워크인터페이스
const NetworkSection = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [visibleDetails, setVisibleDetails] = useState([]);
  useEffect(() => {
    setVisibleDetails(Array(3).fill(false)); // 초기 상태: 모든 detail 숨김
  }, []);
  // 팝업 열기/닫기 핸들러
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  //table반복
  useEffect(() => {
    const container = document.getElementById("network_content_outer");
    const originalContent = document.querySelector('.network_content');
    

    for (let i = 0; i < 3; i++) {
      const clone = originalContent.cloneNode(true);
      container.appendChild(clone);
    }
  }, []);
 const toggleDetails = (index) => {
  setVisibleDetails((prevDetails) => {
    const newDetails = [...prevDetails];
    newDetails[index] = !newDetails[index];
    return newDetails;
  });
};

  return (
    <div id="network_outer">
      <div id="network_content_outer">
        <div className="content_header_right">
          <button id="network_popup_new" onClick={openModal}>새로 만들기</button>
          <button>수정</button>
          <button>제거</button>
        </div>
        {Array.from({ length: 3 }).map((_, index) => (
  <div key={index}>
    <div className="network_content">
      <div>
        <i className="fa fa-chevron-right" onClick={() => toggleDetails(index)}></i>
        <i className="fa fa-arrow-circle-o-up" style={{ color: '#21c50b', marginLeft: '0.3rem' }}></i>
        <i className="fa fa-plug"></i>
        <i className="fa fa-usb"></i>
        <span>nic1</span>
      </div>
      <div>
        <div>네트워크 이름</div>
        <div>ovirtmgmt</div>
      </div>
      <div>
        <div>IPv4</div>
        <div>192.168.10.147</div>
      </div>
      <div>
        <div>IPv6</div>
        <div>192.168.10.147</div>
      </div>
      <div style={{ paddingRight: '3%' }}>
        <div>MAC</div>
        <div>192.168.10.147</div>
      </div>
    </div>
    <div className='network_content_detail' style={{ display: visibleDetails[index] ? 'block' : 'none' }}>
      설명입력
    </div>
  </div>
))}

       
      </div>



    {/*네트워크 인터페이스(새로만들기) */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="새로만들기"
        className="network_popup"
        overlayClassName="network_popup_outer"
        shouldCloseOnOverlayClick={false}
      >
                    <div className="network_popup_header">
                <h1>네트워크 인터페이스 수정</h1>
                <button  onClick={closeModal}><i className="fa fa-times"></i></button>
            </div>

            <div className="network_popup_content">
                <div className="input_box">
                    <span>이름</span>
                    <input type="text"/>
                </div>
                <div className="select_box">
                    <label htmlFor="profile">프로파일</label>
                    <select id="profile">
                        <option value="test02">ovirtmgmt/ovirtmgmt</option>
                    </select>
                </div>
                <div className="select_box">
                    <label htmlFor="type" style={{ color: 'gray' }}>유형</label>
                    <select id="type" disabled>
                        <option value="test02">VirtIO</option>
                    </select>
                </div>
                <div className="select_box2" style={{ marginBottom: '2%' }}>
                    <div>
                        <input type="checkbox" id="custom_mac_box" disabled/>
                        <label htmlFor="custom_mac_box" style={{ color: 'gray' }}>
                            사용자 지정 MAC 주소
                        </label>
                    </div>
                    <div>
                        <select id="mac_address" disabled>
                            <option value="test02">VirtIO</option>
                        </select>
                    </div>
                </div>

                <div className="plug_radio_btn">
                    <span>링크 상태</span>
                    <div>
                        <div className="radio_outer">
                            <div>
                                <input type="radio" name="status" id="status_up"/>
                                <img src=".//img/스크린샷 2024-05-24 150455.png" alt="status_up"/>
                                <label htmlFor="status_up">Up</label>
                            </div>
                            <div>
                                <input type="radio" name="status" id="status_down"/>
                                <img src=".//img/Down.png" alt="status_down"/>
                                <label htmlFor="status_down">Down</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="plug_radio_btn">
                    <span>카드 상태</span>
                    <div>
                        <div className="radio_outer">
                            <div>
                                <input type="radio" name="connection_status" id="connected"/>
                                <img src=".//img/연결됨.png" alt="connected"/>
                                <label htmlFor="connected">연결됨</label>
                            </div>
                            <div>
                                <input type="radio" name="connection_status" id="disconnected"/>
                                <img src=".//img/분리.png" alt="disconnected"/>
                                <label htmlFor="disconnected">분리</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="network_parameter_outer">
                <span>네트워크 필터 매개변수</span>
                <div>
                    <div>
                        <span>이름</span>
                        <input type="text"/>
                    </div>
                    <div>
                        <span>값</span>
                        <input type="text"/>
                    </div>
                    <div id="buttons">
                        <button>+</button>
                        <button>-</button>
                    </div>
                </div>
            </div>

            <div className="edit_footer">
                <button style={{ display: 'none' }}></button>
                <button>OK</button>
                <button  onClick={closeModal}>취소</button>
            </div>
      </Modal>





    </div>
  );
};

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

  return (
      <div id="disk_outer">
          <div id="disk_content">
              <div className="content_header_right">
                  <button id="disk_popup_new" onClick={openNewDiskModal}>새로 만들기</button>
                  <button id="join_popup_btn" onClick={openJoinDiskModal}>연결</button>
                  <button>수정</button>
                  <button>제거</button>
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
              <div className="disk_content_header">
                  <span>디스크 유형:</span>
                  <button>모두</button>
                  <button>이미지</button>
                  <button>직접 LUN</button>
                  <button>관리되는 블록</button>
              </div>
              <table>
                  <thead>
                      <tr>
                          <th></th>
                          <th>변경</th>
                          <th><i className="fa fa-glass"></i></th>
                          <th><i className="fa fa-glass"></i></th>
                          <th><i className="fa fa-glass"></i></th>
                          <th>가상 크기</th>
                          <th>연결 대상</th>
                          <th>인터페이스</th>
                          <th>논리적 이름</th>
                          <th>상태</th>
                          <th>유형</th>
                          <th>설명</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr>
                          <td><i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i></td>
                          <td>on20-ap01</td>
                          <td><i className="fa fa-glass"></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td><i className="fa fa-glass"></i></td>
                          <td>on20-ap01</td>
                          <td>VirtIO-SCSI</td>
                          <td>/dev/sda</td>
                          <td>OK</td>
                          <td>이미지</td>
                          <td></td>
                          <td></td>
                      </tr>
                  </tbody>
              </table>
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
              <div className="network_popup_header">
                  <h1>새 가상 디스크</h1>
                  <button onClick={closeNewDiskModal}><i className="fa fa-times"></i></button>
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
                  <button onClick={closeJoinDiskModal}><i className="fa fa-times"></i></button>
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
                              <th><i className="fa fa-external-link"></i></th>
                              <th><i className="fa fa-external-link"></i></th>
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
                              <th><i className="fa fa-external-link"></i></th>
                              <th><i className="fa fa-external-link"></i></th>
                          </tr>
                          <tr id="join_managed_th" style={{ display: 'none' }}>
                              <th>별칭</th>
                              <th>설명</th>
                              <th>ID</th>
                              <th>가상 크기</th>
                              <th>스토리지 도메인</th>
                              <th>인터페이스</th>
                              <th>R/O</th>
                              <th><i className="fa fa-external-link"></i></th>
                              <th><i className="fa fa-external-link"></i></th>
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
      </div>
  );
};


// 스냅샷
const SnapshotSection = () => {
  return (
    <div id="snapshot_outer">
      <div id="snapshot_content_outer">
        <div className="content_header_right">
          <button className="snap_create_btn">생성</button>
          <button>미리보기</button>
          <button>커밋</button>
          <button>되돌리기</button>
          <button>삭제</button>
          <button>복제</button>
          <button>템플릿 생성</button>
        </div>
        <div className="snapshot_content">
          <div className="snapshot_content_left">
            <div><i className="fa fa-camera"></i></div>
            <span>Active VM</span>
          </div>
          <div className="snapshot_content_right">
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>일반</span>
              <i className="fa fa-eye"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>디스크</span>
              <i className="fa fa-trash-o"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>네트워크 인터페이스</span>
              <i className="fa fa-server"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>설치된 애플리케이션</span>
              <i className="fa fa-newspaper-o"></i>
            </div>
          </div>
        </div>
        <div className="snapshot_content">
          <div className="snapshot_content_left">
            <div><i className="fa fa-camera"></i></div>
            <span>Active VM</span>
          </div>
          <div className="snapshot_content_right">
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>일반</span>
              <i className="fa fa-eye"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>디스크</span>
              <i className="fa fa-trash-o"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>네트워크 인터페이스</span>
              <i className="fa fa-server"></i>
            </div>
            <div>
              <i className="fa fa-chevron-right"></i>
              <span>설치된 애플리케이션</span>
              <i className="fa fa-newspaper-o"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

// 애플리케이션
const ApplicationSection = () => {


  return (
    <div id="application_outer">
      <div id="application_content">
        <div className="application_content_header">
          <button><i className="fa fa-chevron-left"></i></button>
          <div>1-2</div>
          <button><i className="fa fa-chevron-right"></i></button>
          <button><i className="fa fa-ellipsis-v"></i></button>
        </div>
        <table>
          <thead>
            <tr>
              <th>설치된 애플리케이션</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>kernel-3.10.0-1062.el7.x86_64</td>
            </tr>
            <tr>
              <td>qemu-guest-agent-2.12.0</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

// 선호도그룹
const PregroupSection = () => {

  const [isModalOpen, setIsModalOpen] = useState(false);
  // 팝업 열기/닫기 핸들러
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <div id="pregroup_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="pregroup_create_btn" onClick={openModal}>새로 만들기</button>
          <button>편집</button>
          <button>제거</button>
        </div>
        <div className="application_content_header">
          <button><i className="fa fa-chevron-left"></i></button>
          <div>1-2</div>
          <button><i className="fa fa-chevron-right"></i></button>
          <button><i className="fa fa-ellipsis-v"></i></button>
        </div>
        <table>
          <thead>
            <tr>
              <th>상태</th>
              <th>이름</th>
              <th>설명</th>
              <th>우선 순위</th>
              <th>가상 머신 축 극성</th>
              <th>가상 머신 강제 적용</th>
              <th>호스트 축 극성</th>
              <th>호스트 강제 적용</th>
              <th>가상 머신 멤버</th>
              <th>가상 머신 레이블</th>
              <th>내용1</th>
              <th>내용2</th>
              <th>내용3</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td style={{ textAlign: 'center' }}>
                <i className="fa fa-exclamation"></i> 중단
              </td>
              <td>test02</td>
              <td>asd</td>
              <td>5</td>
              <td></td>
              <td>소프트</td>
              <td></td>
              <td>소프트</td>
              <td>멤버없음</td>
              <td>레이블없음</td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td style={{ textAlign: 'center' }}>OK</td>
              <td>test02</td>
              <td>asd</td>
              <td>5</td>
              <td></td>
              <td>소프트</td>
              <td></td>
              <td>소프트</td>
              <td>멤버없음</td>
              <td>레이블없음</td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
          </tbody>
        </table>
      </div>

      {/*선호도그룹(새로만들기) */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="새 선호도 그룹"
        className="pregroup_create"
        overlayClassName="pregroup_create_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="network_popup_header">
          <h1>새 선호도 그룹</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>
        <div id="pregroup_create_content">
          <div className="snap_create_inputbox">
            <span>이름</span>
            <input type="text" />
          </div>
          <div className="snap_create_inputbox">
            <span>설명</span>
            <input type="text" />
          </div>
          <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
            <div>
              <span>우선 순위</span>
              <i className="fa fa-info-circle"></i>
            </div>
            <input type="text" />
          </div>
          <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
            <div>
              <label htmlFor="disk_profile">가상 머신 선호도 규칙</label>
              <i className="fa fa-info-circle"></i>
            </div>
            <div className="pregroup_create_select">
              <div>
                <select id="disk_profile">
                  <option value="disabled">비활성화됨</option>
                </select>
              </div>
              <div>
                <input type="checkbox" id="enforce_disk_profile" />
                <label htmlFor="enforce_disk_profile">강제 적용</label>
              </div>
            </div>
          </div>
          <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
            <div>
              <label htmlFor="host_preference_rule">호스트 선호도 규칙</label>
              <i className="fa fa-info-circle"></i>
            </div>
            <div className="pregroup_create_select">
              <div>
                <select id="host_preference_rule">
                  <option value="disabled">비활성화됨</option>
                </select>
              </div>
              <div>
                <input type="checkbox" id="enforce_rule" />
                <label htmlFor="enforce_rule">강제 적용</label>
              </div>
            </div>
          </div>
        </div>
        <div className="pregroup_create_buttons">
          <div className="pregroup_buttons_content">
            <label htmlFor="cluster">가상머신</label>
            <div className="pregroup_buttons_select">
              <div>
                <select id="cluster">
                  <option value="default">가상머신:on20-ap01</option>
                </select>
              </div>
              <div>
                <button>+</button>
                <button>-</button>
              </div>
            </div>
          </div>
          <div className="pregroup_buttons_content">
            <label htmlFor="cluster">호스트</label>
            <div className="pregroup_buttons_select">
              <div>
                <select id="cluster">
                  <option value="default">호스트 선택</option>
                </select>
              </div>
              <div>
                <button>+</button>
                <button>-</button>
              </div>
            </div>
          </div>
        </div>
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>


    </div>
  );
};

// 선호도 레이블
const PregroupLabelSection = () => {

  const [isModalOpen, setIsModalOpen] = useState(false);
  // 팝업 열기/닫기 핸들러
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <div id="pregroup_lable_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="lable_create_btn" onClick={openModal}>새로 만들기</button>
          <button>편집</button>
          <button>제거</button>
        </div>
        <div className="application_content_header">
          <button><i className="fa fa-chevron-left"></i></button>
          <div>1-2</div>
          <button><i className="fa fa-chevron-right"></i></button>
          <button><i className="fa fa-ellipsis-v"></i></button>
        </div>
        <table>
          <thead>
            <tr>
              <th>이름</th>
              <th>가상머신 멤버</th>
              <th>호스트 멤버</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>test</td>
              <td>HostedEngine</td>
              <td>host02.ititinfo.com</td>
              <td style={{ textAlign: 'right' }}><i className="fa fa-caret-up"></i></td>
            </tr>
            <tr>
              <td>test</td>
              <td>HostedEngine</td>
              <td>host02.ititinfo.com</td>
              <td style={{ textAlign: 'right' }}><i className="fa fa-caret-up"></i></td>
            </tr>
          </tbody>
        </table>
      </div>

      {/*선호도 레이블(새로만들기) */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="새 선호도 레이블"
        className="lable_create"
        overlayClassName="lable_create_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="network_popup_header">
          <h1>새로운 선호도 레이블</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>
        <div>
          <div className="snap_create_inputbox" style={{ padding: '0.5rem' }}>
            <span>이름</span>
            <input type="text" />
          </div>
        </div>
        <div className="pregroup_create_buttons" style={{ paddingTop: 0 }}>
          <div className="pregroup_buttons_content">
            <label htmlFor="cluster">가상머신</label>
            <div className="pregroup_buttons_select">
              <div>
                <select id="cluster">
                  <option value="default">가상머신:on20-ap01</option>
                </select>
              </div>
              <div>
                <button>+</button>
                <button>-</button>
              </div>
            </div>
          </div>
          <div className="pregroup_buttons_content">
            <label htmlFor="cluster">호스트</label>
            <div className="pregroup_buttons_select">
              <div>
                <select id="cluster">
                  <option value="default">호스트 선택</option>
                </select>
              </div>
              <div>
                <button>+</button>
                <button>-</button>
              </div>
            </div>
          </div>
        </div>
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>
    </div>
  );
};

// 게스트정보
const GuestInfoSection = () => {
  return (
    <div id="guest_info_outer">
      <div className="tables">
        <div className="table_container_left">
          <table className="table">
            <tbody>
              <tr>
                <th>유형:</th>
                <td>Linux</td>
              </tr>
              <tr>
                <th>아키텍쳐:</th>
                <td>x86_64</td>
              </tr>
              <tr>
                <th>운영체제:</th>
                <td>CentOS Linux 7</td>
              </tr>
              <tr>
                <th>커널 버전</th>
                <td>3.10.0-1062.el7_x86_64</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div className="table_container_center">
          <table className="table">
            <tbody>
              <tr>
                <th>시간대:</th>
                <td>KST (UTC + 09:00)</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div className="table_container_right">
          <table className="table">
            <tbody>
              <tr>
                <th>로그인된 사용자:</th>
                <td></td>
              </tr>
              <tr>
                <th>콘솔 사용자:</th>
                <td></td>
              </tr>
              <tr>
                <th>콘솔 클라이언트 IP:</th>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

//권한
const PowerSection = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  // 팝업 열기/닫기 핸들러
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <div id="power_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button onClick={openModal} id="power_add_btn">추가</button>
          <button>제거</button>
        </div>
        <div className="application_content_header">
          <span>Permission Filters:</span>
          <button>All</button>
          <button>Direct</button>
        </div>
        <table>
          <thead>
            <tr>
              <th></th>
              <th>사용자</th>
              <th>인증 공급자</th>
              <th>네임스페이스</th>
              <th>역할</th>
              <th>생성일</th>
              <th>Inherited From</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><i className="fa fa-user"></i></td>
              <td>ovirt-administrator</td>
              <td></td>
              <td>*</td>
              <td>SuperUser</td>
              <td>2023. 12. 29. AM 11:40:58</td>
              <td>(시스템)</td>
            </tr>
            <tr>
              <td><i className="fa fa-user"></i></td>
              <td>admin (admin)</td>
              <td>internal-authz</td>
              <td>*</td>
              <td>SuperUser</td>
              <td>2023. 12. 29. AM 11:40:58</td>
              <td>(시스템)</td>
            </tr>
            <tr>
              <td><i className="fa fa-user"></i></td>
              <td>Everyone</td>
              <td></td>
              <td>*</td>
              <td>UserProfileEditor</td>
              <td>2017. 3. 16. PM 6:52:29</td>
              <td>(시스템)</td>
            </tr>
          </tbody>
        </table>
      </div>

      {/*권한(추가)팝업 */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="사용자에게 권한 추가"
        className="power_add"
        overlayClassName="power_add_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="network_popup_header">
          <h1>사용자에게 권한 추가</h1>
          <button onClick={closeModal}><i className="fa fa-times"></i></button>
        </div>

        <div className="power_radio_group">
          <input type="radio" id="user" name="option" defaultChecked />
          <label htmlFor="user">사용자</label>
          
          <input type="radio" id="group" name="option" />
          <label htmlFor="group">그룹</label>
          
          <input type="radio" id="all" name="option" />
          <label htmlFor="all">모두</label>
          
          <input type="radio" id="my_group" name="option" />
          <label htmlFor="my_group">내 그룹</label>
        </div>

        <div className="power_contents_outer">
          <div>
            <label htmlFor="search">검색:</label>
            <select id="search">
              <option value="default">Default</option>
            </select>
          </div>
          <div>
            <label htmlFor="namespace">네임스페이스:</label>
            <select id="namespace">
              <option value="default">Default</option>
            </select>
          </div>
          <div>
            <label htmlFor="placeholder" style={{ color: 'white' }}>.</label>
            <select id="placeholder">
              <option value="default">Default</option>
            </select>
          </div>
          <div>
            <div style={{ color: 'white' }}>.</div>
            <input type="submit" value="검색" />
          </div>
        </div>

        <div className="power_table">
          <table>
            <thead>
              <tr>
                <th>이름</th>
                <th>성</th>
                <th>사용자 이름</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>dddddddddddddddddddddd</td>
                <td>2024. 1. 17. PM 3:14:39</td>
                <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed.</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="power_last_content">
          <label htmlFor="assigned_role">할당된 역할:</label>
          <select id="assigned_role" style={{ width: '65%' }}>
            <option value="default">UserRole</option>
          </select>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>
    </div>
  );
};

// 이벤트
const EventSection = () => {
  return (
    <div id="event_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button>새로 만들기</button>
          <button>편집</button>
          <button>제거</button>
        </div>
        <div className="application_content_header">
          <button><i className="fa fa-chevron-left"></i></button>
          <div>1-2</div>
          <button><i className="fa fa-chevron-right"></i></button>
          <button><i className="fa fa-ellipsis-v"></i></button>
        </div>
        <table>
          <thead>
            <tr>
              <th></th>
              <th>시간</th>
              <th>메세지</th>
              <th>상관 관계 ID</th>
              <th>소스</th>
              <th>사용자 지정 이벤트 ID</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2024. 1. 17. PM 3:14:39</td>
              <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' has been completed.</td>
              <td>4b4b417a-c...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2024. 1. 17. PM 3:14:21</td>
              <td>Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' was initiated by admin@intern...</td>
              <td>4b4b417a-c...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-times"></i></td>
              <td>2024. 1. 5. AM 8:37:54</td>
              <td>Failed to restart VM on2o-ap01 on host host01.ititinfo.com</td>
              <td>3400e0dc</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-times"></i></td>
              <td>2024. 1. 5. PM 8:37:10</td>
              <td>VM on2o-ap01 is down with error. Exit message: VM terminated with error.</td>
              <td>3400e0dc</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2024. 1. 5. PM 8:34:29</td>
              <td>Trying to restart VM on2o-ap01 on host host01.ititinfo.com</td>
              <td>3400e0dc</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-exclamation"></i></td>
              <td>2024. 1. 5. PM 8:29:10</td>
              <td>VM on2o-ap01 was set to the Unknown status.</td>
              <td>3400e0dc</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2023. 12. 29. PM 12:55:08</td>
              <td>VM on2o-ap01 started on Host host01.ititinfo.com</td>
              <td>a99b6ae8-8d...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2023. 12. 29. PM 12:54:48</td>
              <td>VM on2o-ap01 was started by admin@internal-authz (Host: host01.ititinfo.com).</td>
              <td>a99b6ae8-8d...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2023. 12. 29. PM 12:54:18</td>
              <td>VM on2o-ap01 configuration was updated by admin@internal-authz.</td>
              <td>e3b8355e-06...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2023. 12. 29. PM 12:54:15</td>
              <td>VM on2o-ap01 configuration was updated by admin@internal-authz.</td>
              <td>793fb95e-6df...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
            <tr>
              <td><i className="fa fa-check"></i></td>
              <td>2023. 12. 29. PM 12:53:53</td>
              <td>VM on2o-ap01 has been successfully imported from the given configuration.</td>
              <td>ede53bc8-c6...</td>
              <td>oVirt</td>
              <td></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

const Computing = () => {
  const { section } = useParams();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState(section || 'general');
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    navigate(`/computing/${activeSection}`);
  }, [activeSection, navigate]);

  const handleSectionClick = (section) => {
    setActiveSection(section);
  };

  useEffect(() => {
    const defaultElement = document.getElementById('common_outer_btn');
    if (defaultElement) {
      defaultElement.style.backgroundColor = '#EDEDED';
      defaultElement.style.color = '#1eb8ff';
      defaultElement.style.borderBottom = '1px solid blue';
    }
  }, []);

  const handleSectionChange = (section) => {
    setActiveSection(section);
    const elements = document.querySelectorAll('.edit_aside > div');
    elements.forEach(el => {
      el.style.backgroundColor = '#FAFAFA';
      el.style.color = 'black';
      el.style.borderBottom = 'none';
    });

    const activeElement = document.getElementById(`${section}_btn`);
    if (activeElement) {
      activeElement.style.backgroundColor = '#EDEDED';
      activeElement.style.color = '#1eb8ff';
      activeElement.style.borderBottom = '1px solid blue';
    }
  };

  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

  const toggleFooterContent = () => {
    setFooterContentVisibility(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const [isPopupVisible, setIsPopupVisible] = useState(false);

  const togglePopup = () => {
    setIsPopupVisible(!isPopupVisible);
  };
  return (
    <div id="section">
      <div className="section_header">
        <div className="section_header_left">
          <span>가상머신</span>
          <div>on20-ap01</div>
          <button><i className="fa fa-exchange"></i></button>
        </div>
        <div className="section_header_right">
          <div className="article_nav">
            <button id="edit_btn">편집</button>
            <div>
              <button>
                <i className="fa fa-play"></i>실행
              </button>
            </div>
            <button><i className="fa fa-pause"></i>일시중지</button>
            <div>
              <button>
                <i className="fa fa-stop"></i>종료
              </button>
            </div>
            <div>
              <button>
                <i className="fa fa-repeat"></i>재부팅
              </button>
            </div>
            <button><i className="fa fa-desktop"></i>콘솔</button>
            <button>스냅샷 생성</button>
            <button id="migration_btn" onClick={openModal}>마이그레이션</button>
            <button id="popup_btn"  onClick={togglePopup}>
              <i className="fa fa-ellipsis-v"></i>
              <div id="popup_box"style={{ display: isPopupVisible ? 'block' : 'none' }}>
                
                <div>
                  <div className="get_btn">가져오기</div>
                  <div className="get_btn">가상 머신 복제</div>
                </div>
                <div>
                  <div>삭제</div>
                </div>
                <div>
                  <div>마이그레이션 취소</div>
                  <div>변환 취소</div>
                </div>
                <div>
                  <div id="template_btn">템플릿 생성</div>
                </div>
                <div style={{ borderBottom: 'none' }}>
                  <div id="domain2">도메인으로 내보내기</div>
                  <div id="domain">Export to Data Domain</div>
                  <div id="ova_btn">OVA로 내보내기</div>
                </div>
              </div>
            </button>
          </div>
        </div>
      </div>
      <div className="content_outer">
        <div className="content_header">
          <div className="content_header_left">
            <div className={activeSection === 'general' ? 'active' : ''} onClick={() => handleSectionClick('general')}>일반</div>
            <div className={activeSection === 'network' ? 'active' : ''} onClick={() => handleSectionClick('network')}>네트워크 인터페이스</div>
            <div className={activeSection === 'disk' ? 'active' : ''} onClick={() => handleSectionClick('disk')}>디스크</div>
            <div className={activeSection === 'snapshot' ? 'active' : ''} onClick={() => handleSectionClick('snapshot')}>스냅샷</div>
            <div className={activeSection === 'application' ? 'active' : ''} onClick={() => handleSectionClick('application')}>애플리케이션</div>
            <div className={activeSection === 'pregroup' ? 'active' : ''} onClick={() => handleSectionClick('pregroup')}>선호도 그룹</div>
            <div className={activeSection === 'pregroup_label' ? 'active' : ''} onClick={() => handleSectionClick('pregroup_label')}>선호도 레이블</div>
            <div className={activeSection === 'guest_info' ? 'active' : ''} onClick={() => handleSectionClick('guest_info')}>게스트 정보</div>
            <div className={activeSection === 'power' ? 'active' : ''} onClick={() => handleSectionClick('power')}>권한</div>
            <div className={activeSection === 'event' ? 'active' : ''} onClick={() => handleSectionClick('event')}>이벤트</div>
          </div>
        </div>
        {activeSection === 'general' && (
          <div className="tables">
            <div className="table_container_left">
              <table className="table">
                <tbody>
                  <tr>
                    <th>이름:</th>
                    <td>on20-ap01</td>
                  </tr>
                  <tr>
                    <th>설명:</th>
                    <td></td>
                  </tr>
                  <tr>
                    <th>상태:</th>
                    <td>실행 중</td>
                  </tr>
                  <tr>
                    <th>업타임:</th>
                    <td>11 days</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr>
                    <th>템플릿:</th>
                    <td>Blank</td>
                  </tr>
                  <tr>
                    <th>운영 시스템:</th>
                    <td>Linux</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr>
                    <th>펌웨어/장치의 유형:</th>
                    <td>BIOS의 Q35 칩셋 <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i></td>
                  </tr>
                  <tr>
                    <th>우선 순위:</th>
                    <td>높음</td>
                  </tr>
                  <tr>
                    <th>최적화 옵션:</th>
                    <td>서버</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div id="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>설정된 메모리:</th>
                    <td>2048 MB</td>
                  </tr>
                  <tr>
                    <th>할당할 실제 메모리:</th>
                    <td>2048 MB</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr>
                    <th>게스트 OS의 여유/캐시+비퍼</th>
                    <td>1003 / 0 MB</td>
                  </tr>
                  <tr>
                    <th>된 메모리:</th>
                    <td></td>
                  </tr>
                  <tr>
                    <th>CPU 코어 수:</th>
                    <td>2(2:1:1)</td>
                  </tr>
                  <tr>
                    <th>게스트 CPU 수:</th>
                    <td>2</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr>
                    <th>게스트 CPU</th>
                    <td>Cascadelake-Server</td>
                    <td></td>
                  </tr>
                  <tr>
                    <th>고가용성:</th>
                    <td>예</td>
                  </tr>
                  <tr>
                    <th>모니터 수:</th>
                    <td>1</td>
                  </tr>
                  <tr>
                    <th>USB:</th>
                    <td>비활성화됨</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div id="table_container_right">
              <table className="table">
                <tbody>
                  <tr>
                    <th>작성자:</th>
                    <td>admin</td>
                  </tr>
                  <tr>
                    <th>소스:</th>
                    <td>oVirt</td>
                  </tr>
                  <tr>
                    <th>실행 호스트:</th>
                    <td>클러스터 내의 호스트</td>
                  </tr>
                  <tr>
                    <th>사용자 정의 속성:</th>
                    <td>설정되지 않음</td>
                  </tr>
                  <tr>
                    <th>클러스터 호환 버전:</th>
                    <td>4.7</td>
                  </tr>
                  <tr>
                    <th>가상 머신의 ID:</th>
                    <td>Linuxdddddddddddddddddddddd</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr className="empty">
                    <th>.</th>
                    <td style={{ color: 'white' }}>.</td>
                  </tr>
                  <tr>
                    <th>FQDN:</th>
                    <td>on20-ap01</td>
                  </tr>
                  <tr>
                    <th>하드웨어 클럭의 시간 오프셋:</th>
                    <td>Asia/Seoul</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        )}
        {activeSection === 'network' && <NetworkSection />}
        {activeSection === 'disk' && <DiskSection />}
        {activeSection === 'snapshot' && <SnapshotSection />}
        {activeSection === 'application' && <ApplicationSection />}
        {activeSection === 'pregroup' && <PregroupSection />}
        {activeSection === 'pregroup_label' && <PregroupLabelSection />}
        {activeSection === 'guest_info' && <GuestInfoSection />}
        {activeSection === 'power' && <PowerSection />}
        {activeSection === 'event' && <EventSection />}
        {/* Add similar sections for snapshot, application, etc. */}
      </div>

      <div className="footer_outer">
        <div className="footer">
          <button onClick={toggleFooterContent}><i className="fa fa-chevron-down"></i></button>
          <div>
            <div
              style={{
                color: selectedFooterTab === 'recent' ? 'black' : '#4F4F4F',
                borderBottom: selectedFooterTab === 'recent' ? '1px solid blue' : 'none'
              }}
              onClick={() => handleFooterTabClick('recent')}
            >
              최근 작업
            </div>
            <div
              style={{
                color: selectedFooterTab === 'alerts' ? 'black' : '#4F4F4F',
                borderBottom: selectedFooterTab === 'alerts' ? '1px solid blue' : 'none'
              }}
              onClick={() => handleFooterTabClick('alerts')}
            >
              경보
            </div>
          </div>
        </div>
        {isFooterContentVisible && (
          <div className="footer_content" style={{ display: 'block' }}>
            <div className="footer_nav">
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
              <div style={{ borderRight: 'none' }}>
                <div>작업이름</div>
                <div><i className="fa fa-filter"></i></div>
              </div>
            </div>
            <div className="footer_img">
              <img src="img/화면 캡처 2024-04-30 164511.png" alt="스크린샷" />
              <span>항목을 찾지 못했습니다</span>
            </div>
          </div>
        )}
      </div>
      
{/*header팝업창----------------------- */}
      {/*편집팝업 */}
      <div id="edit_popup_bg" style={{ display: 'none' }}>
        <div id="edit_popup">
          <div className="edit_header">
            <h1>템플릿 수정</h1>
            <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>
              <i className="fa fa-times"></i>
            </button>
          </div>
          <div className="edit_body">
            <div className="edit_aside">
              <div className={`edit_aside_item ${activeSection === 'common_outer' ? 'active' : ''}`} id="common_outer_btn" onClick={() => handleSectionChange('common_outer')}>
                <span>일반</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'system_outer' ? 'active' : ''}`} id="system_outer_btn" onClick={() => handleSectionChange('system_outer')}>
                <span>시스템</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'start_outer' ? 'active' : ''}`} id="start_outer_btn" onClick={() => handleSectionChange('start_outer')}>
                <span>초기 실행</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'console_outer' ? 'active' : ''}`} id="console_outer_btn" onClick={() => handleSectionChange('console_outer')}>
                <span>콘솔</span>
              </div>
            </div>
            <div className="edit_aside">
              <div className={`edit_aside_item ${activeSection === 'host_outer' ? 'active' : ''}`} id="host_outer_btn" onClick={() => handleSectionChange('host_outer')}>
                <span>호스트</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'ha_mode_outer' ? 'active' : ''}`} id="ha_mode_outer_btn" onClick={() => handleSectionChange('ha_mode_outer')}>
                <span>고가용성</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'res_alloc_outer' ? 'active' : ''}`} id="res_alloc_outer_btn" onClick={() => handleSectionChange('res_alloc_outer')}>
                <span>리소스 할당</span>
              </div>
              <div className={`edit_aside_item ${activeSection === 'boot_outer' ? 'active' : ''}`} id="boot_outer_btn" onClick={() => handleSectionChange('boot_outer')}>
                <span>부트 옵션</span>
              </div>
            </div>

            <form action="#">
              {/* 일반 */}
              <div id="common_outer" style={{ display: activeSection === 'common_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div className="edit_second_content">
                  <div>
                    <label htmlFor="name">이름</label>
                    <input type="text" id="name" value="test02" />
                  </div>
                  <div>
                    <label htmlFor="base-version" style={{ color: 'gray' }}>하위 버전 이름</label>
                    <input type="text" id="base-version" value="base version" disabled />
                  </div>
                  <div>
                    <label htmlFor="description">설명</label>
                    <input type="text" id="description" />
                  </div>
                </div>
              </div>

              {/* 시스템 */}
              <div id="system_outer" style={{ display: activeSection === 'system_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div className="edit_second_content">
                  <div>
                    <label htmlFor="memory_size">메모리 크기</label>
                    <input type="text" id="memory_size" value="2048 MB" readOnly />
                  </div>
                  <div>
                    <div>
                      <label htmlFor="max_memory">최대 메모리</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <input type="text" id="max_memory" value="8192 MB" readOnly />
                  </div>

                  <div>
                    <div>
                      <label htmlFor="actual_memory">할당할 실제 메모리</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                  </div>

                  <div>
                    <div>
                      <label htmlFor="total_cpu">총 가상 CPU</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <input type="text" id="total_cpu" value="1" readOnly />
                  </div>
                  <div>
                    <div>
                      <i className="fa fa-arrow-circle-o-right" style={{ color: 'rgb(56, 56, 56)' }}></i>
                      <span>고급 매개 변수</span>
                    </div>
                  </div>
                  <div style={{ fontWeight: 600 }}>일반</div>
                  <div style={{ paddingTop: 0, paddingBottom: '4%' }}>
                    <div>
                      <label htmlFor="time_offset">하드웨어 클릭의 시간 오프셋</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="time_offset">
                      <option value="(GMT+09:00) Korea Standard Time">(GMT+09:00) Korea Standard Time</option>
                    </select>
                  </div>
                </div>
              </div>

              {/* 콘솔 */}
              <div id="console_outer" style={{ display: activeSection === 'console_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div className="res_alloc_checkbox" style={{ marginBottom: 0 }}>
                  <span>그래픽 콘솔</span>
                  <div>
                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                    <label htmlFor="memory_balloon">헤드릭스(headless)모드</label>
                    <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                  </div>
                </div>

                <div className="edit_second_content">
                  <div style={{ paddingTop: 0 }}>
                    <label htmlFor="memory_size">비디오 유형</label>
                    <input type="text" id="memory_size" value="VGA" readOnly />
                  </div>
                  <div>
                    <div>
                      <label htmlFor="max_memory">그래픽 프로토콜</label>
                    </div>
                    <input type="text" id="max_memory" value="VNC" readOnly />
                  </div>

                  <div>
                    <div>
                      <label htmlFor="actual_memory">VNC 키보드 레이아웃</label>
                    </div>
                    <input type="text" id="actual_memory" value="기본값[en-us]" readOnly />
                  </div>

                  <div>
                    <div>
                      <label htmlFor="total_cpu">콘솔 분리 작업</label>
                    </div>
                    <input type="text" id="total_cpu" value="화면 잠금" readOnly />
                  </div>
                  <div>
                    <div>
                      <label htmlFor="disconnect_action_delay">Disconnect Action Delay in Minutes</label>
                    </div>
                    <input type="text" id="disconnect_action_delay" value="0" disabled />
                  </div>
                  <div id="monitor">
                    <label htmlFor="screen">모니터</label>
                    <select id="screen">
                      <option value="test02">1</option>
                    </select>
                  </div>
                </div>

                <div className="console_checkboxs">
                  <div className="console_checkbox">
                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">USB활성화</label>
                  </div>
                  <div className="console_checkbox">
                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">스마트카드 사용가능</label>
                  </div>
                  <span>단일 로그인 방식</span>
                  <div className="console_checkbox">
                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                    <label htmlFor="memory_balloon">USB활성화</label>
                  </div>
                  <div className="console_checkbox">
                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                    <label htmlFor="memory_balloon">스마트카드 사용가능</label>
                  </div>
                </div>
              </div>

              {/* 호스트 */}
              <div id="host_outer" style={{ display: activeSection === 'host_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div id="host_second_content">
                  <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                  <div className="form_checks">
                    <div>
                      <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked />
                      <label className="form-check-label" htmlFor="flexRadioDefault1">
                        클러스터 내의 호스트
                      </label>
                    </div>
                    <div>
                      <div>
                        <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" />
                        <label className="form-check-label" htmlFor="flexRadioDefault2">
                          특정 호스트
                        </label>
                      </div>
                      <div>
                        <select id="specific_host_select">
                          <option value="host02.ititinfo.com">host02.ititinfo.com</option>
                        </select>
                      </div>
                    </div>
                  </div>
                  <div className="host_checkboxs">
                    <span>CPU 옵션:</span>
                    <div className="host_checkbox">
                      <input type="checkbox" id="host_cpu_passthrough" name="host_cpu_passthrough" />
                      <label htmlFor="host_cpu_passthrough">호스트 CPU 통과</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <div className="host_checkbox">
                      <input type="checkbox" id="tsc_migration" name="tsc_migration" />
                      <label htmlFor="tsc_migration">TSC 주파수가 동일한 호스트에서만 마이그레이션</label>
                      <i className="fa fa-info-circle"></i>
                    </div>
                  </div>
                </div>

                <div id="host_third_content">
                  <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                  <div>
                    <div>
                      <span>마이그레이션 모드</span>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="migration_mode">
                      <option value="수동 및 자동 마이그레이션 허용">수동 및 자동 마이그레이션 허용</option>
                    </select>
                  </div>
                  <div>
                    <div>
                      <span>마이그레이션 정책</span>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="migration_policy">
                      <option value="클러스터 기본값(Minimal downtime)">클러스터 기본값(Minimal downtime)</option>
                    </select>
                  </div>
                  <div>
                    <div>
                      <span>마이그레이션 암호화 사용</span>
                    </div>
                    <select id="migration_encryption">
                      <option value="클러스터 기본값(암호화하지 마십시오)">클러스터 기본값(암호화하지 마십시오)</option>
                    </select>
                  </div>
                  <div>
                    <div>
                      <span>Parallel Migrations</span>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="parallel_migrations" readOnly>
                      <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                    </select>
                  </div>
                  <div>
                    <div style={{ paddingBottom: '4%' }}>
                      <span style={{ color: 'gray' }}>Number of VM Migration Connection</span>
                    </div>
                    <select id="vm_migration_connections" disabled>
                      <option value=""></option>
                    </select>
                  </div>
                </div>
              </div>

              {/* 고가용성 */}
              <div id="ha_mode_outer" style={{ display: activeSection === 'ha_mode_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div id="ha_mode_second_content">
                  <div className="check_box">
                    <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                    <label className="check_label" htmlFor="ha_mode_box">
                      고가용성
                    </label>
                  </div>
                  <div>
                    <div>
                      <span>가상 머신 임대 대상 스토리지 도메인</span>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="no_lease" disabled>
                      <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                    </select>
                  </div>
                  <div>
                    <div>
                      <span>재개 동작</span>
                      <i className="fa fa-info-circle"></i>
                    </div>
                    <select id="force_shutdown">
                      <option value="강제 종료">강제 종료</option>
                    </select>
                  </div>
                  <div className="ha_mode_article">
                    <span>실행/마이그레이션 큐에서 우선순위</span>
                    <div>
                      <span>우선 순위</span>
                      <select id="priority">
                        <option value="낮음">낮음</option>
                      </select>
                    </div>
                  </div>

                  <div className="ha_mode_article">
                    <span>위치독</span>
                    <div>
                      <span>위치독 모델</span>
                      <select id="watchdog_model">
                        <option value="감시 장치 없음">감시 장치 없음</option>
                      </select>
                    </div>
                    <div>
                      <span style={{ color: 'gray' }}>위치독 작업</span>
                      <select id="watchdog_action" disabled>
                        <option value="없음">없음</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>

              {/* 리소스 할당 */}
              <div id="res_alloc_outer" style={{ display: activeSection === 'res_alloc_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div className="res_second_content">
                  <div className="cpu_res">
                    <span style={{ fontWeight: 600 }}>CPU 할당:</span>
                    <div>
                      <span>CPU 프로파일</span>
                      <select id="watchdog_action">
                        <option value="없음">Default</option>
                      </select>
                    </div>
                    <div>
                      <span>CPU 공유</span>
                      <div id="cpu_sharing">
                        <select id="watchdog_action" style={{ width: '63%' }}>
                          <option value="없음">비활성화됨</option>
                        </select>
                        <input type="text" value="0" disabled />
                      </div>
                    </div>
                    <div>
                      <span>CPU Pinning Policy</span>
                      <select id="watchdog_action">
                        <option value="없음">None</option>
                      </select>
                    </div>
                    <div>
                      <div>
                        <span>CPU 피닝 토폴로지</span>
                        <i className="fa fa-info-circle"></i>
                      </div>
                      <input type="text" disabled />
                    </div>
                  </div>

                  <span style={{ fontWeight: 600 }}>I/O 스레드:</span>
                  <div id="threads">
                    <div>
                      <input type="checkbox" id="enableIOThreads" name="enableIOThreads" />
                      <label htmlFor="enableIOThreads">I/O 스레드 활성화</label>
                    </div>
                    <div>
                      <input type="text" />
                      <i className="fa fa-info-circle"></i>
                    </div>
                  </div>
                </div>
              </div>

              {/* 부트 옵션 */}
              <div id="boot_outer" style={{ display: activeSection === 'boot_outer' ? 'block' : 'none' }}>
                <div className="edit_first_content">
                  <div>
                    <label htmlFor="cluster">클러스터</label>
                    <select id="cluster">
                      <option value="default">Default</option>
                    </select>
                    <div>데이터센터 Default</div>
                  </div>

                  <div>
                    <label htmlFor="template" style={{ color: 'gray' }}>템플릿에 근거</label>
                    <select id="template" disabled>
                      <option value="test02">test02</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="os">운영 시스템</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div>
                    <label htmlFor="firmware">칩셋/펌웨어 유형</label>
                    <select id="firmware">
                      <option value="bios">BIOS의 Q35 칩셋</option>
                    </select>
                  </div>
                  <div style={{ marginBottom: '2%' }}>
                    <label htmlFor="optimization">최적화 옵션</label>
                    <select id="optimization">
                      <option value="server">서버</option>
                    </select>
                  </div>
                </div>

                <div className="res_second_content">
                  <div className="cpu_res">
                    <span style={{ fontWeight: 600 }}>부트순서:</span>
                    <div>
                      <span>첫 번째 장치</span>
                      <select id="watchdog_action">
                        <option value="없음">하드디스크</option>
                      </select>
                    </div>
                    <div>
                      <span>두 번째 장치</span>
                      <select id="watchdog_action">
                        <option value="없음">Default</option>
                      </select>
                    </div>
                  </div>

                  <div id="boot_checkboxs">
                    <div>
                      <div>
                        <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                        <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                      </div>
                      <div>
                        <input type="text" disabled />
                        <i className="fa fa-info-circle"></i>
                      </div>
                    </div>

                    <div>
                      <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                      <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>

          <div className="edit_footer">
            <button>고급 옵션 숨기기</button>
            <button>OK</button>
            <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>취소</button>
          </div>
        </div>
      </div>

      {/*마이그레이션 팝업 */}
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
            <button >마이그레이션</button>
            <button onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>

    </div> //section끝


  );
};

export default Computing;