import React, { useState , useEffect} from 'react';
import { useParams } from 'react-router-dom';
import Modal from 'react-modal';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faCaretUp, faDesktop, faUniversity, faWrench, faUser
  , faCheckCircle, faExclamation, faFilm, faArrowCircleUp,
  faTimes,
  faInfoCircle,
  faArrowUp,
  faArrowDown,
  faMinus,
  faPlus,
  faCircle,
  faArrowsAltH,
  faCheck,
  faBan,
  faFan,
  faExclamationTriangle,
  faPencilAlt,
  faCaretDown,
  faNetworkWired,
  faTag
} from '@fortawesome/free-solid-svg-icons'
import './css/HostDetail.css';
import TableOuter from '../table/TableOuter';



function HostDetail() {
  const { name } = useParams();
  //클릭한 이름 받아오기
  const handlePermissionFilterClick = (filter) => {
    setActivePermissionFilter(filter);
  };

  const [activePopup, setActivePopup] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [활성화된섹션, set활성화된섹션] = useState('일반_섹션');
  const [activeButton, setActiveButton] = useState('network');
  const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리
  const openPopup = (type) => {
    setActivePopup(type); // 'new' 또는 'edit' 등으로 설정
  };

  const closePopup = () => {
    setActivePopup(null); // 모달을 닫을 때 상태 초기화
  };
  const [isHiddenParameterVisible, setHiddenParameterVisible] = useState(false);

  const toggleHiddenParameter = () => {
    setHiddenParameterVisible(!isHiddenParameterVisible);
  };
  
  const handleButtonClick = (button) => {
    setActiveButton(button);
    setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
  };
  
  useEffect(() => {
    const 기본섹션 = document.getElementById('일반_섹션_btn');
    if (기본섹션) {
      기본섹션.style.backgroundColor = '#EDEDED';
      기본섹션.style.color = '#1eb8ff';
      기본섹션.style.borderBottom = '1px solid blue';
    }
  }, []);

  const 섹션변경 = (section) => {
    set활성화된섹션(section);
    const 모든섹션들 = document.querySelectorAll('.edit_aside > div');
    모든섹션들.forEach((el) => {
      el.style.backgroundColor = '#FAFAFA';
      el.style.color = 'black';
      el.style.borderBottom = 'none';
    });

    const 선택된섹션 = document.getElementById(`${section}_btn`);
    if (선택된섹션) {
      선택된섹션.style.backgroundColor = '#EDEDED';
      선택된섹션.style.color = '#1eb8ff';
      선택된섹션.style.borderBottom = '1px solid blue';
    }
  };

  const [activePermissionFilter, setActivePermissionFilter] = useState('all');
    //테이블컴포넌트
     // 가상머신 
      const data = [
        {
          name: (
            <div>
              <FontAwesomeIcon icon={faCaretUp} style={{ color: 'green' }}fixedWidth/>
              HostedEngine
            </div>
          ),
          cluster: (
            <div>
              <FontAwesomeIcon icon={faDesktop} fixedWidth/>
              Default
            </div>
          ),
          ipAddress: '192.168.0.80 fe80::216:3eff:fe6c:208',
          fqdn: 'ovirt.ititinfo.com',
          memory: (
            <div>
              <span>52%</span>
              <div style={{ width: '52%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          cpu: (
            <div>
              <span>2%</span>
              <div style={{ width: '2%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          network: (
            <div>
              <span>0%</span>
              <div style={{ width: '0%', backgroundColor: 'green', height: '4px' }}></div>
            </div>
          ),
          status: '실행 중',
          uptime: '36 days',
        },
      ];
      

  const volumeData = [
    {
      name: 'block_sda',
      function: 'storage',
      vendor: 'VMware (null)',
      product: 'Virtual disk (null)',
      driver: '',
      currentlyUsed: '',
      connectedToVM: '',
      iommuGroup: '해당 없음',
      mdevType: '해당 없음',
    },
  ];

  // 네트워크인터페이스 박스열고닫기
  const [visibleBoxes, setVisibleBoxes] = useState([]);

  const toggleHiddenBox = (index) => {
    setVisibleBoxes((prevVisibleBoxes) => {
      if (prevVisibleBoxes.includes(index)) {
        return prevVisibleBoxes.filter((i) => i !== index); // 이미 열려 있으면 닫기
      } else {
        return [...prevVisibleBoxes, index]; // 아니면 열기
      }
    });
  };

  
  const networkdata = [
    {
      icon: <FontAwesomeIcon icon={faUniversity} fixedWidth/>,
      unmanaged: <FontAwesomeIcon icon={faWrench} fixedWidth/>,
      vlan: 'VLAN',
      networkName: 'ovirtmgmt',
      ipv4: '192.168.0.81',
      ipv6: ''
    }
  ];
    // 권한

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

      // 이벤트
      const eventData = [
        {
          icon: <FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }}fixedWidth/>,
          time: '2024. 8. 7. PM 12:24:14',
          message: 'Check for available updates on host host01.ittinfo.com was completed successfully with message \'no updates available.\'',
          correlationId: '2568d791:c08...',
          source: 'oVirt',
          userEventId: '',
        },
      ];
      
    //
    const [activeTab, setActiveTab] = useState('general');
    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };


    //headerbutton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick: () => openPopup('host_edit') },
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
        { id: 'manage_btn', label: '관리', onClick: () => console.log('Manage button clicked') },
        { id: 'install_btn', label: '설치', onClick: () => console.log('Install button clicked') }
      ];
    
      const popupItems = []; // 현재 팝업 아이템이 없으므로 빈 배열로 설정
      const uploadOptions = []; // 현재 업로드 옵션이 없으므로 빈 배열로 설정
    // nav컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'machine', label: '가상머신' },
        { id: 'networkinterface', label: '네트워크 인터페이스' },
        { id: 'hostdevice', label: '호스트 장치' },
        { id: 'permission', label: '권한' },
        { id: 'event', label: '이벤트' }
      ];

      
    return (
        <div id='section'>
             <HeaderButton
      title="호스트"
      subtitle={name}
      additionalText="목록이름"
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
                
                  
                {/* 일반 */}
                {activeTab === 'general' && (
                <div className="host_content_outer">

                    <div className="host_tables">

                        <div className="table_container_left">
                            <h2 style={{color:'white',border:'none'}}>하드웨어</h2>
                            <table className="host_table">
                              <tbody>
                                  <tr>
                                      <th>호스트이름/IP:</th>
                                      <td>{name}</td>
                                  </tr>
                                  <tr>
                                      <th>SPM 우선순위:</th>
                                      <td>중간</td>
                                  </tr>
                                  <tr>
                                      <th>활성 가상 머신:</th>
                                      <td>1</td>
                                  </tr>
                                  <tr>
                                      <th>논리 CPU 코어 수:</th>
                                      <td>8</td>
                                  </tr>
                                  <tr>
                                      <th>온라인 논리 CPU 코어 수:</th>
                                      <td>0, 1, 2, 3, 4, 5, 6, 7</td>
                                  </tr>
                                  <tr>
                                      <th>부팅 시간:</th>
                                      <td>2024. 7. 2. AM 10:12:36</td>
                                  </tr>
                                  <tr>
                                      <th>Hosted Engine HA:</th>
                                      <td>활성 (접속: 3400)</td>
                                  </tr>
                                  <tr>
                                      <th>iSCSI 개시자 이름:</th>
                                      <td>iqn.1994-05.com.redhat:d33a11d7f51b</td>
                                  </tr>
                                  <tr>
                                      <th>Kdump Integration Status:</th>
                                      <td>비활성화됨</td>
                                  </tr>
                                  <tr>
                                      <th>물리적 메모리:</th>
                                      <td>19743 MB 한계, 15794 MB 사용됨, 3949 MB 사용가능</td>
                                  </tr>
                                  <tr>
                                      <th>Swap 크기:</th>
                                      <td>10063 MB 한계, 0 MB 사용됨, 10063 MB 사용가능</td>
                                  </tr>
                                  <tr>
                                      <th>공유 메모리:</th>
                                      <td>9%</td>
                                  </tr>
                                  <tr>
                                      <th>장치 통과:</th>
                                      <td>비활성화됨</td>
                                  </tr>
                                  <tr>
                                      <th>새로운 가상 머신의 스케줄링을 위한 최대 여유 메모리:</th>
                                      <td>2837 MB</td>
                                  </tr>
                                  <tr>
                                      <th>메모리 페이지 공유:</th>
                                      <td>활성</td>
                                  </tr>
                                  <tr>
                                      <th>자동으로 페이지를 크게:</th>
                                      <td>항상</td>
                                  </tr>
                                  <tr>
                                      <th>Huge Pages (size: free/total):</th>
                                      <td>2048: 0/0, 1048576: 0/0</td>
                                  </tr>
                                  <tr>
                                      <th>SELinux 모드:</th>
                                      <td>강제 적용</td>
                                  </tr>
                                  <tr>
                                      <th>클러스터 호환 버전:</th>
                                      <td>4.2, 4.3, 4.4, 4.5, 4.6, 4.7</td>
                                  </tr>
                                  <tr>
                                      <th><FontAwesomeIcon icon={faExclamation} fixedWidth/></th>
                                      <td>이 호스트는 전원 관리가 설정되어 있지 않습니다.</td>
                                  </tr>
                              </tbody>
                            </table>
                        </div>

                        <div className="table_container_left">
                            <h2>하드웨어</h2>
                            <table className="host_table">
                              <tbody>
                                <tr>
                                    <th>제조사:</th>
                                    <td>Intel(R) Xeon(R) Gold 6354 CPU @ 3.00GHz</td>
                                </tr>
                                <tr>
                                    <th>버전:</th>
                                    <td>1</td>
                                </tr>
                                <tr>
                                    <th>CPU 모델:</th>
                                    <td>Intel(R) Xeon(R) Gold 6354 CPU @ 3.00GHz</td>
                                </tr>
                                <tr>
                                    <th>소켓당 CPU 코어:</th>
                                    <td>1</td>
                                </tr>
                                <tr>
                                    <th>제품군:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>UUID:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>CPU 유형:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>코어당 CPU의 스레드:</th>
                                    <td>1 (SMT 사용안함)</td>
                                </tr>
                                <tr>
                                    <th>제품 이름:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>일련 번호:</th>
                                    <td>Secure Intel Cascadelake Server Family</td>
                                </tr>
                                <tr>
                                    <th>CPU 소켓:</th>
                                    <td>8</td>
                                </tr>
                                <tr>
                                    <th>TSC 주파수:</th>
                                    <td>2992968000 (스케일링 비활성화)</td>
                                </tr>
                            </tbody>

                            </table>
                        </div>


                        <div  className="table_container_left">
                            <h2>소프트웨어</h2>
                            <table className="host_table">
                              <tbody>
                                <tr>
                                  <th>OS 버전:</th>
                                  <td>RHEL - 9.1.2206.0 - 23.el9</td>
                                </tr>
                                <tr>
                                  <th>OS 정보:</th>
                                  <td>oVirt Node 4.5.5</td>
                                </tr>
                                <tr>
                                  <th>커널 버전:</th>
                                  <td>5.14.0 - 388.el9.x86_64</td>
                                </tr>
                                <tr>
                                  <th>KVM 버전:</th>
                                  <td>8.1.0 - 4.el9</td>
                                </tr>
                                <tr>
                                  <th>LIBVIRT 버전:</th>
                                  <td>libvirt-9.5.0-6.el9</td>
                                </tr>
                                <tr>
                                  <th>VDSM 버전:</th>
                                  <td>vdsm-4.50.5-1.1.el9</td>
                                </tr>
                                <tr>
                                  <th>SPICE 버전:</th>
                                  <td></td>
                                </tr>
                                <tr>
                                  <th>GlusterFS 버전:</th>
                                  <td>glusterfs-10.5-1.el9s</td>
                                </tr>
                                <tr>
                                  <th>CEPH 버전:</th>
                                  <td>librbd1-16.2.14-1.el9</td>
                                </tr>
                                <tr>
                                  <th>Open vSwitch 버전:</th>
                                  <td>openvswitch-2.17-1.el9</td>
                                </tr>
                                <tr>
                                  <th>Nmstate 버전:</th>
                                  <td>nmstate-2.2-19.1.el9</td>
                                </tr>
                                <tr>
                                  <th>커널 기능:</th>
                                  <td>
                                    MDS: (Not affected), L1TF: (Not affected), SRBDS: (Not affected), MELTDOWN: (Not affected), RETBLEED: (Not affected), SPECTRE_V1: (Mitigation: usercopy/swapgs barriers and __user pointer sanitization), SPECTRE_V2: (Mitigation: Enhanced / Automatic IBRS, IBPB: conditional, RSB filling, PBRSE-eIBRS: SW sequence), ITLB_MULTIHIT: (KVM: Mitigation: Split huge pages), MMIO_STALE_DATA: (Vulnerable: Clear CPU buffers attempted, no microcode; SMT Host state unknown), TSX_ASYNC_ABORT: (Not affected), SPEC_STORE_BYPASS: (Mitigation: Speculative Store Bypass disabled via prctl), GATHER_DATA_SAMPLING: (Unknown: Dependent on hypervisor status), SPEC_RSTACK_OVERFLOW: (Not affected)
                                  </td>
                                </tr>
                                <tr>
                                  <th>VNC 암호화:</th>
                                  <td>비활성화됨</td>
                                </tr>
                                <tr>
                                  <th>OVN configured:</th>
                                  <td>예</td>
                                </tr>
                              </tbody>
                            </table>

                        </div>

                    </div>
                   

                    
                </div>
               
                )}
                {/* 가상머신 */}
                {activeTab === 'machine' && (
                <div className="host_btn_outer">
                    
                        <div className="content_header_right">
                            <button>실행</button>
                            <button>일시중지</button>
                            <button>종료</button>
                            <button>전원 끔</button>
                            <button>콘솔</button>
                            <button>마이그레이션</button>
                        </div>
                        <div className="host_filter_btns">
                            <span>가상 머신 필터:</span>
                            <div>
                                <button>현재 호스트에서 실행 중</button>
                                <button>현재 호스트에 고정</button>
                                <button>모두</button>
                            </div>
                        </div>
                        <TableOuter 
                          columns={TableColumnsInfo.VMS_FROM_HOST}
                          data={data}
                          onRowClick={() => console.log('Row clicked')}
                        />
                </div>
                )}
             
                {/* 네트워크 인터페이스 */}
                {activeTab === 'networkinterface' && (
                <div className="host_btn_outer">
                  <div className="content_header_right">
                    <button>VF 보기</button>
                    <button>모두 확장</button>
                    <button onClick={() => openPopup('host_network_popup')}>호스트 네트워크 설정</button>
                    <button>네트워크 설정 저장</button>
                    <button>모든 네트워크 동기화</button>
                  </div>

                  {[0, 1].map((index) => (
                    <div className='host_network_boxs' key={index}>
                      <div
                        className='host_network_firstbox'
                        onClick={() => toggleHiddenBox(index)}
                      >
                        <div>
                          <FontAwesomeIcon icon={faArrowCircleUp} fixedWidth/>
                          <FontAwesomeIcon icon={faFilm} fixedWidth/>
                          <span>ens192</span>
                        </div>
                        <div className='firstbox_flex'>
                          <div>
                            <div>MAC</div>
                            <span>00:4234324</span>
                          </div>
                        </div>
                        <div className='firstbox_flex'>
                          <div>
                            <div>Rx 속도(mbps)</div>
                            <span>103</span>
                          </div>
                          <div>
                            <div>총 Rx(바이트)</div>
                            <span>42,214,343,32,522</span>
                          </div>
                        </div>
                        <div className='firstbox_flex'>
                          <div>
                            <div>Rx 속도(mbps)</div>
                            <span>103</span>
                          </div>
                          <div>
                            <div>총 Rx(바이트)</div>
                            <span>42,214,343,32,522</span>
                          </div>
                        </div>
                        <div>
                          <FontAwesomeIcon icon={faFilm} fixedWidth/>
                          <span>Mbps</span>
                        </div>
                        <div>
                          <FontAwesomeIcon icon={faFilm} fixedWidth/>
                          <span>0 Pkts</span>
                        </div>
                      </div>
                      <div
                        className='host_network_hiddenbox'
                        style={{ display: visibleBoxes.includes(index) ? 'block' : 'none' }}
                      >
                         <TableOuter 
                           columns={TableColumnsInfo.NETWORKS_FROM_HOST}
                           data={networkdata}
                           onRowClick={() => console.log('Row clicked')}
                         />
                      </div>
                    </div>
                  ))}
                </div>
                )}
                {/* 호스트 장치 */}
                {activeTab === 'hostdevice' && (
                <div className="host_btn_outer">
                  <div className="host_empty_outer">
                    <TableOuter 
                      columns={TableColumnsInfo.VOLUMES_FROM_HOST} 
                      data={volumeData} 
                      onRowClick={() => console.log('Row clicked')} 
                    />
                  </div>
                </div>
                )}
               
                {/* 권한 */}
                {activeTab === 'permission' && (
              <div className="host_btn_outer">
              <div className="content_header_right">
                <button>추가</button>
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
            </div>
            )}
          
                {/* 이벤트 */}
                {activeTab === 'event' && (
                <div className="host_empty_outer">
                   <TableOuter
                     columns={TableColumnsInfo.EVENTS}
                     data={eventData}
                     onRowClick={() => console.log('Row clicked')} 
                    />
                </div>
                )}
               
            </div>


            {/* 편집 팝업*/}
            <Modal
              isOpen={activePopup === 'host_edit'}
              onRequestClose={closePopup}
              contentLabel="편집"
              className="host_new_popup"
              overlayClassName="host_new_outer"
              shouldCloseOnOverlayClick={false}
            >
              <div className="popup_header">
                <h1>호스트 수정</h1>
                <button onClick={closePopup}>
                  <FontAwesomeIcon icon={faTimes} fixedWidth/>
                </button>
              </div>

              <div className="edit_body">
                <div className="edit_aside">
                  <div
                    className={`edit_aside_item`}
                    id="일반_섹션_btn"
                    onClick={() => 섹션변경('일반_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '일반_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '일반_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '일반_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>일반</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="전원관리_섹션_btn"
                    onClick={() => 섹션변경('전원관리_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '전원관리_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '전원관리_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '전원관리_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>전원 관리</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="호스트엔진_섹션_btn"
                    onClick={() => 섹션변경('호스트엔진_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '호스트엔진_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '호스트엔진_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '호스트엔진_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>호스트 엔진</span>
                  </div>
                  <div
                    className={`edit_aside_item`}
                    id="선호도_섹션_btn"
                    onClick={() => 섹션변경('선호도_섹션')}
                    style={{ backgroundColor: 활성화된섹션 === '선호도_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '선호도_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '선호도_섹션' ? '1px solid blue' : 'none' }}
                  >
                    <span>선호도</span>
                  </div>
                </div>

                {/* 폼의 다양한 섹션들 */}
                <form action="#">
                  {/* 공통 섹션 */}
                  <div
                    id="일반_섹션"
                    style={{ display: 활성화된섹션 === '일반_섹션' ? 'block' : 'none' }}
                  >
                <div className="edit_first_content">
                        <div>
                            <label htmlFor="cluster">클러스터</label>
                            <select id="cluster" disabled>
                                <option value="default" >Default</option>
                            </select>
                            <div>데이터센터 Default</div>
                        </div>
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div>
                            <label htmlFor="comment">코멘트</label>
                            <input type="text" id="comment" />
                        </div>
                        <div>
                            <label htmlFor="hostname">호스트이름/IP</label>
                            <input type="text" id="hostname" />
                        </div>
                        <div>
                            <label htmlFor="ssh_port">SSH 포트</label>
                            <input type="text" id="ssh_port" value="22" />
                        </div>
                    </div>

          <div className='host_checkboxs'>
            <div className='host_checkbox'>
                <input type="checkbox" id="memory_balloon" name="memory_balloon" />
                <label htmlFor="headless_mode">헤드리스 모드</label>
            </div>
            <div className='host_checkbox'>
                <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth/>
            </div>
          </div>

          <div className='host_checkboxs'>
            <div className='host_textbox'>
                <label htmlFor="user_name">사용자 이름</label>
                <input type="text" id="user_name" />
            </div>

            <div className='host_text_raido_box'>
                <div>
                  <input type="radio" id="password" name="name_option" />
                  <label htmlFor="password">암호</label>
                </div>
                <input type="text" id="radio1_name" />
            </div>

            <div className='host_radiobox'>
                <input type="radio" id="ssh_key" name="name_option" />
                <label htmlFor="ssh_key">SSH 공개키</label>
            </div>

          </div>

                  </div>{/*일반섹션끝 */}

                  {/* 전원 관리 섹션 */}
                  <div
                    id="전원관리_섹션"
                    style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
                  >
                    
                    <div className='host_checkboxs'>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                          <label htmlFor="enable_forwarding">전송 관리 활성</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                          <label htmlFor="kdump_usage">Kdump 통합</label>
                      </div>
                      <div className='host_checkbox'>
                          <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                          <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
                      </div>


                      <span className='sorted_agents'>순서대로 정렬된 에이전트</span>
                    </div>
                    
                    
                    <div className='addFence_agent'>
                      <span>펜스 에이전트 추가</span>
                      <button>+</button>
                    </div>

                    <div className='advanced_objec_add'>
                <button onClick={toggleHiddenParameter}>
                  {isHiddenParameterVisible ? '-' : '+'}
                </button>
                <span>고급 매개 변수</span>
                {isHiddenParameterVisible && (
                <div className='host_hidden_parameter'>
                 
                  <div>전원 관리 프록시 설정</div>
                  <div>
                    <div className='proxy_content'>
                      <div className='font-bold'>1.</div>
                      <div className='w-6'>cluster</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                    <div className='proxy_content'>
                      <div className='font-bold'>2.</div>
                      <div className='w-6'>dc</div>
                      <div>  
                        <button> <FontAwesomeIcon icon={faArrowUp} fixedWidth /></button>
                        <button><FontAwesomeIcon icon={faArrowDown} fixedWidth /></button>
                      </div>
                      <button><FontAwesomeIcon icon={faMinus} fixedWidth /></button>
                    </div>
                  </div>

                  <div className='proxy_add'>
                    <div>전원 관리 프록시 추가</div>
                    <button><FontAwesomeIcon icon={faPlus} fixedWidth /></button>
                  </div>
                </div>
                )}
              </div>
                    

                  </div>

                  {/* 호스트 엔진 섹션 */}
                  <div
                    id="호스트엔진_섹션"
                    style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
                  >
                    <div className="host_policy">
                        <label htmlFor="host_action">호스트 연관 전처리 작업 선택</label>
                        <select id="host_action">
                            <option value="none">없음</option>
                        </select>
                    </div>


                  </div>

                  {/* 선호도 섹션 */}
                  <div
                    id="선호도_섹션"
                    style={{ display: 활성화된섹션 === '선호도_섹션' ? 'block' : 'none' }}
                  >
                    <div className="preference_outer">
                      <div className="preference_content">
                        <label htmlFor="preference_group">선호도 그룹을 선택하십시오</label>
                          <div>
                            <select id="preference_group">
                              <option value="none"></option>
                            </select>
                            <button>추가</button>
                          </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택된 선호도 그룹</div>
                        <div>선택된 선호도 그룹이 없습니다</div>
                      </div>
                      <div className="preference_content">
                        <label htmlFor="preference_label">선호도 레이블 선택</label>
                        <div>
                          <select id="preference_label">
                            <option value="none"></option>
                          </select>
                          <button>추가</button>
                        </div>
                      </div>
                      <div className="preference_noncontent">
                        <div>선택한 선호도 레이블</div>
                        <div>선호도 레이블이 선택되어있지 않습니다</div>
                      </div>

                    </div>
                  </div>

                </form>
              </div>

              <div className="edit_footer">
                <button>OK</button>
                <button onClick={closePopup}>취소</button>
              </div>
            </Modal>
            {/*호스트(호스트 네트워크 설정)*/}
            <Modal
              isOpen={activePopup === 'host_network_popup'}
              onRequestClose={closePopup}
              contentLabel="호스트 네트워크 설정"
              className="Modal"
              overlayClassName="Overlay"
              shouldCloseOnOverlayClick={false}
            >
              <div className="vnic_new_content_popup">
                <div className="popup_header">
                  <h1>호스트 host01.ititinfo.com 네트워크 설정</h1>
                  <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className="host_network_outer px-1.5 text-sm">
                <div className="py-2 font-bold underline">드래그 하여 변경</div>

                <div className="host_network_separation">
            <div className="network_separation_left">
              <div>
                <div>인터페이스</div>
                <div>할당된 논리 네트워크</div>
              </div>

              <div className="separation_left_content">
                <div className="container gap-1">
                  <FontAwesomeIcon icon={faCircle} style={{ fontSize: '0.1rem', color: '#00FF00' }} />
                  <FontAwesomeIcon icon={faDesktop} />
                  <span>ens192</span>
                </div>
                <div className="flex items-center justify-center">
                  <FontAwesomeIcon icon={faArrowsAltH} style={{ color: 'grey', width: '5vw', fontSize: '0.6rem' }} />
                </div>

                <div className="container">
                  <div className="left-section">
                    <FontAwesomeIcon icon={faCheck} className="icon green-icon" />
                    <span className="text">ovirtmgmt</span>
                  </div>
                  <div className="right-section">
                    <FontAwesomeIcon icon={faFan} className="icon" />
                    <FontAwesomeIcon icon={faDesktop} className="icon" />
                    <FontAwesomeIcon icon={faDesktop} className="icon" />
                    <FontAwesomeIcon icon={faBan} className="icon" />
                    <FontAwesomeIcon icon={faExclamationTriangle} className="icon" />
                    <FontAwesomeIcon icon={faPencilAlt} className="icon" />
                  </div>
                </div>
              </div>
            </div>

            <div className="network_separation_right">
            <div className="network_filter_btns">
        <button
          className={`btn ${activeButton === 'network' ? 'bg-gray-200' : ''}`}
          onClick={() => handleButtonClick('network')}
        >
          네트워크
        </button>
        <button
          className={`btn border-l border-gray-800 ${activeButton === 'label' ? 'bg-gray-200' : ''}`}
          onClick={() => handleButtonClick('label')}
        >
          레이블
        </button>
      </div>

        {/* unconfigured_network는 네트워크 버튼이 클릭된 경우만 보임 */}
        {!isLabelVisible && (
          <div className="unconfigured_network">
            <div>할당되지 않은 논리 네트워크</div>
            <div style={{ backgroundColor: '#d1d1d1' }}>필수</div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div className="unconfigured_content flex items-center space-x-2">
              <div>
                <FontAwesomeIcon icon={faCaretDown} style={{ color: 'red', marginRight: '0.2rem' }} />
                <span>ddd</span>
              </div>
              <FontAwesomeIcon icon={faNetworkWired} style={{ color: 'green', fontSize: '20px' }} />
            </div>
            <div style={{ backgroundColor: '#d1d1d1' }}>필요하지 않음</div>
            <div>
              <span>외부 논리적 네트워크</span>
              <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }} fixedWidth />
            </div>
          </div>
        )}

        {/* lable_part는 레이블 버튼이 클릭된 경우만 보임 */}
        {isLabelVisible && (

            <div class="lable_part">
              <FontAwesomeIcon icon={faTag} style={{ color: 'orange', marginRight: '0.2rem' }} />
              <span>[새 레이블]</span>
            </div>

        )}
      </div>


              </div>

                  <div className="border-t-[1px] border-gray-500 mt-4">
                      <div className='py-1 checkbox_group'>
                        <input type="checkbox" id="checkHostConnection" checked />
                        <label htmlFor="checkHostConnection">호스트와 Engine간의 연결을 확인</label>
                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)', cursor: 'pointer' }} fixedWidth />
                      </div>
                      <div className='checkbox_group'>
                        <input type="checkbox" id="saveNetworkConfig" disabled />
                        <label htmlFor="saveNetworkConfig">네트워크 설정 저장</label>
                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)', cursor: 'pointer' }} fixedWidth />
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
            <Footer/>
        </div>
    );
}

export default HostDetail;