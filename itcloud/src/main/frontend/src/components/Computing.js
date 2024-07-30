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
  //const closeModal = () => setIsModalOpen(false);

  const [isPopupVisible, setIsPopupVisible] = useState(false);

  const togglePopup = () => {
    setIsPopupVisible(!isPopupVisible);
  };



  const handleRowClick = () => {
    navigate('/detail/computing'); // 경로를 /computing으로 변경
  };
    //   // 테이블 데이터
    //   const data = [
    //     { name: 'ovirtmgmt', description: 'Management Network' },
    //     { name: 'example1', description: 'Example Description 1' },
    //     { name: 'example2', description: 'Example Description 2' },
    //     // 필요한 만큼 데이터 추가
    // ];

    // const columns = [
    //     { header: '이름', accessor: 'name', clickable: true },
    //     { header: '설명', accessor: 'description', clickable: false },
    // ];
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
            <div className={activeSection === 'general' ? 'active' : ''} onClick={() => handleSectionClick('general')}>가상머신</div>
            <div className={activeSection === 'network' ? 'active' : ''} onClick={() => handleSectionClick('network')}>템플릿</div>
            <div className={activeSection === 'disk' ? 'active' : ''} onClick={() => handleSectionClick('disk')}>풀</div>
            <div className={activeSection === 'snapshot' ? 'active' : ''} onClick={() => handleSectionClick('snapshot')}>데이터 센터</div>
            <div className={activeSection === 'application' ? 'active' : ''} onClick={() => handleSectionClick('application')}>클러스터</div>
          </div>
        </div>

        {activeSection === 'general' && (
          <div className="tables">
            <div className="storage_domain_content">
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
                          <td onClick={handleRowClick}>on20-ap01</td>
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
          </div>
        )}
        {activeSection === 'network' && <NetworkSection />}
        {activeSection === 'disk' && <DiskSection />}
        {activeSection === 'snapshot' && <SnapshotSection />}
        {activeSection === 'application' && <ApplicationSection />}
      
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
      

    

    </div> //section끝


  );
};

export default Computing;