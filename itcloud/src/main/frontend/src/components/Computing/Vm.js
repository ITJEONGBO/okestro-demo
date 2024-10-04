import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import Table from '../table/Table';
import HeaderButton from '../button/HeaderButton';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faTimes,
  faInfoCircle,
  faDesktop, 
  faRepeat, 
  faPlay, 
  faPause,
  faBan,
  faStop
} from '@fortawesome/free-solid-svg-icons';
import './css/Vm.css';
import Footer from '../footer/Footer';
import ApplicationSection from './vm/ApplicationSection';
import DiskSection from './vm/DiskSection';
import EventSection from './vm/EventSection';
import GuestInfoSection from './vm/GuestInfoSection';
import NetworkSection from './vm/NetworkSection';
import SnapshotSection from './vm/SnapshotSection';
import PowerSection from './vm/PowerSection'; 
import HostDevice from './vm/HostDevice'; 
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';

// React Modal 설정
Modal.setAppElement('#root');



const Vm = () => {
    const { id } = useParams();
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);
 
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'network', label: '네트워크 인터페이스' },
    { id: 'disk', label: '디스크' },
    { id: 'snapshot', label: '스냅샷' },
    { id: 'application', label: '애플리케이션' },
    // { id: 'guest_info', label: '게스트 정보' },
    // { id: 'power', label: '권한' },
    { id: 'event', label: '이벤트' },
    { id: 'host_device', label: '호스트 장치' }
  ];
  
  // headerbutton 컴포넌트
  const buttons = [
    { id: 'new_btn', label: '새로 만들기'},
    { id: 'edit_btn', label: '편집', onClick: () => showEditPopup() },
    { id: 'delete_btn', label: '삭제'},
    { id: 'run_btn', label: <><FontAwesomeIcon icon={faPlay} fixedWidth/>실행</>, onClick: () => console.log('실행 clicked') },
    { id: 'pause_btn', label: <><FontAwesomeIcon icon={faPause} fixedWidth/>일시중지</>, onClick: () => console.log('일시중지 clicked') },
    { id: 'stop_btn', label: <><FontAwesomeIcon icon={faStop} fixedWidth/>종료</>, onClick: () => console.log('종료 clicked') },
    { id: 'reboot_btn', label: <><FontAwesomeIcon icon={faRepeat} fixedWidth/>재부팅</>, onClick: () => console.log('재부팅 clicked') },
    { id: 'console_btn', label: <><FontAwesomeIcon icon={faDesktop} fixedWidth/>콘솔</>, onClick: () => console.log('콘솔 clicked') },
    { id: 'snapshot_btn', label: '스냅샷 생성', onClick: () => console.log('스냅샷 생성 clicked') },
    { id: 'migration_btn', label: '마이그레이션', onClick: openModal }
  ];
  
  const popupItems = [
    '가져오기',
    '가상 머신 복제',
    '템플릿 생성',
    'OVA로 내보내기',
  ];
  const [activeSection, setActiveSection] = useState('general');
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
  const [selectedFooterTab, setSelectedFooterTab] = useState('recent');



  const handleSectionClick = (name) => {
    setActiveSection(name);
    navigate(`/computing/vms/${id}/${name}`);
  };

  const toggleFooterContent = () => {
    setFooterContentVisibility(!isFooterContentVisible);
  };

  const handleFooterTabClick = (tab) => {
    setSelectedFooterTab(tab);
  };


    // 편집 팝업
        useEffect(() => {
          const showEditPopup = () => {
              setActiveSection('common_outer');
              const editPopupBg = document.getElementById('edit_popup_bg');
              if (editPopupBg) {
                  editPopupBg.style.display = 'block';
              }
          }

          const editButton = document.getElementById('network_first_edit_btn');
          if (editButton) {
              editButton.addEventListener('click', showEditPopup);
          }

          return () => {
              if (editButton) {
                  editButton.removeEventListener('click', showEditPopup);
              }
          };
      }, []);

      // 편집 팝업 기본 섹션 스타일 적용
      useEffect(() => {
          const defaultElement = document.getElementById('common_outer_btn');
          if (defaultElement) {
              defaultElement.style.backgroundColor = '#EDEDED';
              defaultElement.style.color = '#1eb8ff';
              defaultElement.style.borderBottom = '1px solid blue';
          }
      }, []);

      // 편집 팝업 스타일 변경
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
      const showEditPopup = () => {
        setActiveSection('common_outer');
        const editPopupBg = document.getElementById('edit_popup_bg');
        if (editPopupBg) {
          editPopupBg.style.display = 'block';
        }
    };
  const renderSectionContent = () => {
    switch (activeSection) {
      case 'network':
        return <NetworkSection/>;
      case 'disk':
        return <DiskSection />;
      case 'snapshot':
        return <SnapshotSection />;
      case 'application':
        return <ApplicationSection/>;
    //   case 'guest_info':
    //     return <GuestInfoSection />;
    //   case 'power':
    //     return <PowerSection/>;
      case 'event':
        return <EventSection />;
      case 'host_device':
        return <HostDevice />;
      default:
        return (
        //   <div className="tables">
        //     <div className="table_container_center">
        //       <table className="table">
        //         <tbody>
        //           <tr>
        //             <th>이름:</th>
        //             <td>{id}</td>
        //           </tr>
        //           <tr>
        //             <th>설명:</th>
        //             <td></td>
        //           </tr>
        //           <tr>
        //             <th>상태:</th>
        //             <td>실행 중</td>
        //           </tr>
        //           <tr>
        //             <th>업타임:</th>
        //             <td>11 days</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr>
        //             <th>템플릿:</th>
        //             <td>Blank</td>
        //           </tr>
        //           <tr>
        //             <th>운영 시스템:</th>
        //             <td>Linux</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr>
        //             <th>펌웨어/장치의 유형:</th>
        //             <td>BIOS의 Q35 칩셋 <FontAwesomeIcon icon={faBan} style={{ marginLeft: '13%', color: 'orange' }}fixedWidth/></td>
        //           </tr>
        //           <tr>
        //             <th>우선 순위:</th>
        //             <td>높음</td>
        //           </tr>
        //           <tr>
        //             <th>최적화 옵션:</th>
        //             <td>서버</td>
        //           </tr>
        //         </tbody>
        //       </table>
        //     </div>
        //     <div className="table_container_center">
        //       <table className="table">
        //         <tbody>
        //           <tr>
        //             <th>설정된 메모리:</th>
        //             <td>2048 MB</td>
        //           </tr>
        //           <tr>
        //             <th>할당할 실제 메모리:</th>
        //             <td>2048 MB</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr>
        //             <th>게스트 OS의 여유/캐시+비퍼</th>
        //             <td>1003 / 0 MB</td>
        //           </tr>
        //           <tr>
        //             <th>된 메모리:</th>
        //             <td></td>
        //           </tr>
        //           <tr>
        //             <th>CPU 코어 수:</th>
        //             <td>2(2:1:1)</td>
        //           </tr>
        //           <tr>
        //             <th>게스트 CPU 수:</th>
        //             <td>2</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr>
        //             <th>게스트 CPU</th>
        //             <td>Cascadelake-Server</td>
        //             <td></td>
        //           </tr>
        //           <tr>
        //             <th>고가용성:</th>
        //             <td>예</td>
        //           </tr>
        //           <tr>
        //             <th>모니터 수:</th>
        //             <td>1</td>
        //           </tr>
        //           <tr>
        //             <th>USB:</th>
        //             <td>비활성화됨</td>
        //           </tr>
        //         </tbody>
        //       </table>
        //     </div>
        //     <div className="table_container_center">
        //       <table className="table">
        //         <tbody>
        //           <tr>
        //             <th>작성자:</th>
        //             <td>admin</td>
        //           </tr>
        //           <tr>
        //             <th>소스:</th>
        //             <td>oVirt</td>
        //           </tr>
        //           <tr>
        //             <th>실행 호스트:</th>
        //             <td>클러스터 내의 호스트</td>
        //           </tr>
        //           <tr>
        //             <th>사용자 정의 속성:</th>
        //             <td>설정되지 않음</td>
        //           </tr>
        //           <tr>
        //             <th>클러스터 호환 버전:</th>
        //             <td>4.7</td>
        //           </tr>
        //           <tr>
        //             <th>가상 머신의 ID:</th>
        //             <td>Linuxdddddddddddddddddddddd</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr className="empty">
        //             <th>.</th>
        //             <td style={{ color: 'white' }}>.</td>
        //           </tr>
        //           <tr>
        //             <th>FQDN:</th>
        //             <td>on20-ap01</td>
        //           </tr>
        //           <tr>
        //             <th>하드웨어 클럭의 시간 오프셋:</th>
        //             <td>Asia/Seoul</td>
        //           </tr>
        //         </tbody>
        //       </table>
        //     </div>
        //   </div>
        <>
        <div className='vm_detail_general_boxs'>
            <div className='detail_general_box'>
                <table className="table">
                    <tbody>
                    <tr>
                        <th>전원상태</th>
                        <td>전원 켜짐</td>
                    </tr>
                    <tr>
                        <th>게스트 운영 체제</th>
                        <td>Red Hat Enterprise Linux 8.x x64</td>
                    </tr>
                    <tr>
                        <th>게스트 에이전트</th>
                        <td>실행 중, 버전 : 123456(최신)</td>
                    </tr>
                    <tr>
                        <th>업타임</th>
                        <td>28 days</td>
                    </tr>
                    <tr>
                        <th>FQDN</th>
                        <td>on20-ap01</td>
                    </tr>
                    <tr>
                        <th>실행 호스트</th>
                        <td>클러스터 내 호스트</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div className='detail_general_box'>
                
                <div>VM하드웨어</div>
                <table className="table">
                    <tbody>
                    <tr>
                        <th>CPU</th>
                        <td>2(2:1:1)</td>
                    </tr>
                    <tr>
                        <th>메모리</th>
                        <td>16GB</td>
                    </tr>
                    <tr>
                        <th>하드 디스크1</th>
                        <td>300 GB | 씬 프로비전<br/>hosted-engine</td>
                    </tr>
                    <tr>
                        <th>네트워트 어댑터1</th>
                        <td>ovirt-mgmgt</td>
                    </tr>
                    <tr>
                        <th>칩셋/펌웨어 유형</th>
                        <td>BIOS Q35 칩셋</td>
                    </tr>
                  
                    </tbody>
                </table>
            </div>
            <div className='detail_general_mini_box'>
                <div>용량 및 사용량</div>
                <div className='capacity_outer'>
                    <div className='capacity'>
                        <div>CPU</div>
                        <div className='capacity_box'>
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>메모리</div>
                        <div className='capacity_box'>
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지</div>
                        <div className='capacity_box'>
                            <div>20%</div>
                            <div>사용됨</div>
                            <div>10CPU<br/>할당됨</div>
                        </div>
                    </div>
                </div>  
            </div>

            <div className='detail_general_mini_box'>
                <div>관련개체</div>
                <div className='capacity_outer'>
                    <div className='capacity'>
                        <div>클러스터</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span class="text-blue-500 font-bold">ITITINFO</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>호스트</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span class="text-blue-500 font-bold">192.168.0.4</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>네트워크</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span>ovirt-mgmt</span>
                        </div>
                    </div>
                    <div className='capacity'>
                        <div>스토리지 도메인</div>
                        <div className='related_object'>
                            <div><FontAwesomeIcon icon={faTimes} fixedWidth/></div>
                            <span >hosted-engine</span>
                        </div>
                    </div>
                
            </div>
            </div>
        </div>

        <div className='detail_general_boxs_bottom'>
        <div className="tables">
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>유형:</th>
                    <td>Linux</td>
                  </tr>
                  <tr>
                    <th>아키텍처:</th>
                    <td>x86_64</td>
                  </tr>
                  <tr>
                    <th>운영체제:</th>
                    <td>ContOs Linux7</td>
                  </tr>
                  <tr>
                    <th>커널버전:</th>
                    <td>3.10.38343344</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>시간대:</th>
                    <td>KST(UTC+09:00)</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div className="table_container_center">
              <table className="table">
                <tbody>
                  <tr>
                    <th>로그인된 사용자:</th>
                    <td>root</td>
                  </tr>
                  <tr>
                    <th>콘솔 사용자:</th>
                    <td></td>
                  </tr>
                  <tr>
                    <th>콘솔 클라이언트IP:</th>
                    <td></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

        </div>
        </>
        );
    }
  };

  return (
    <div id="section">
       <HeaderButton
      title="가상머신"
      subtitle="d"
      buttons={buttons}
      popupItems={popupItems}
    />
      <div className="content_outer">
        <div className="content_header">
          <div className="content_header_left">
            {sections.map((sec) => (
              <div
                key={sec.id}
                className={activeSection === sec.id ? 'active' : ''}
                onClick={() => handleSectionClick(sec.id)}
              >
                {sec.label}
              </div>
            ))}
          </div>
        </div>
        <div className="host_btn_outer">
          {renderSectionContent()}
        </div>
      </div>


      {/* 편집팝업 */}
      <div id="edit_popup_bg" style={{ display: 'none' }}>
            <div id="edit_popup">
                <div className="popup_header">
                    <h1>가상머신 편집</h1>
                    <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>
                        <FontAwesomeIcon icon={faTimes} fixedWidth/>
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
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                    </div>
                                    <input type="text" id="actual_memory" value="2048 MB" readOnly />
                                </div>

                                <div>
                                    <div>
                                        <label htmlFor="total_cpu">총 가상 CPU</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
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
                               
                                <div className='checkbox_group'>
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                    <label htmlFor="memory_balloon">헤드리스(headless)모드</label>
                                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
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
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">USB활성화</label>
                                </div>
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" disabled />
                                    <label style={{ color: '#A1A1A1' }} htmlFor="memory_balloon">스마트카드 사용가능</label>
                                </div>
                                <span>단일 로그인 방식</span>
                                <div className="checkbox_group">
                                    <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                                    <label htmlFor="memory_balloon">USB활성화</label>
                                </div>
                                <div className="checkbox_group">
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
                                <div className="checkbox_group">
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
                                    <span>감시</span>
                                    <div>
                                        <span>감시 모델</span>
                                        <select id="watchdog_model">
                                            <option value="감시 장치 없음">감시 장치 없음</option>
                                        </select>
                                    </div>
                                    <div>
                                        <span style={{ color: 'gray' }}>감시 작업</span>
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
                                    <div className='checkbox_group'>
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
                                        <div className='checkbox_group'>
                                            <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                                            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                                        </div>
                                        <div>
                                            <input type="text" disabled />
                                            <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                                        </div>
                                    </div>

                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                                        <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

                <div className="edit_footer">

                    <button>OK</button>
                    <button onClick={() => document.getElementById('edit_popup_bg').style.display = 'none'}>취소</button>
                </div>
            </div>
      </div>
    

      <Footer/>
    </div>
  );
};

export default Vm;
