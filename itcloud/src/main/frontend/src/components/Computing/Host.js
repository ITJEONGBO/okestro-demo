import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import HeaderButton from '../button/HeaderButton';
import { Table, TableColumnsInfo } from '../table/Table';
import Footer from '../footer/Footer';

Modal.setAppElement('#root');

const Host = () => {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [활성화된섹션, set활성화된섹션] = useState('일반_섹션');

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const 섹션헤더버튼들 = [
    { id: 'new_btn', label: '새로 만들기', onClick: openModal },
    { id: 'edit_btn', label: '편집', onClick: () => {} },
    { id: 'delete_btn', label: '삭제', onClick: () => {} },
    { id: 'manage_btn', label: '관리', onClick: () => {}, hasDropdown: true },
    { id: 'install_btn', label: '설치', onClick: () => {}, hasDropdown: true },
  ];

  const 섹션헤더팝업아이템들 = [
    '가져오기',
    '가상 머신 복제',
    '삭제',
    '마이그레이션 취소',
    '변환 취소',
    '템플릿 생성',
    '도메인으로 내보내기',
    'Export to Data Domain',
    'OVA로 내보내기',
  ];

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

  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') {
      navigate(`/computing/host/${row.name}`);
    } else if (column.accessor === 'cluster') {
      navigate(`/computing/cluster/${row.cluster.props.children}`);
    }
  };

  const columns = [
    { header: '', accessor: 'iconStatus', clickable: false },
    { header: '', accessor: 'iconWarning', clickable: false },
    { header: '', accessor: 'iconSPM', clickable: false },
    { header: '이름', accessor: 'name', clickable: true },
    { header: '코멘트', accessor: 'comment', clickable: false },
    { header: '호스트 이름/IP', accessor: 'hostname', clickable: false },
    { header: '클러스터', accessor: 'cluster', clickable: true }, // 클러스터 컬럼에 clickable 추가
    { header: '데이터 센터', accessor: 'dataCenter', clickable: false },
    { header: '상태', accessor: 'status', clickable: false },
    { header: '가상 머신', accessor: 'vmCount', clickable: false },
    { header: '메모리', accessor: 'memoryUsage', clickable: false },
    { header: 'CPU', accessor: 'cpuUsage', clickable: false },
    { header: '네트워크', accessor: 'networkUsage', clickable: false },
    { header: 'SPM', accessor: 'spm', clickable: false },
  ];

  const data = [
    {
      iconStatus: [
        <i className="fa fa-exclamation-triangle" style={{ color: 'yellowgreen' }} key="icon1"></i>,
        <i className="fa fa-exclamation-triangle" style={{ color: 'red' }} key="icon2"></i>,
      ],
      iconWarning: <i className="fa fa-exclamation-triangle" style={{ color: 'red' }}></i>,
      iconSPM: <i className="fa fa-crown" style={{ color: 'gold' }}></i>,
      name: 'host01.ititnfo.com',
      comment: '192.168.0.80',
      hostname: 'host01.ititinfo.com',
      cluster: (<span
        style={{ color: 'blue', cursor: 'pointer'}}
        onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
        onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
      > Default
        </span>
        ),
      dataCenter: 'Default',
      status: 'Up',
      vmCount: 1,
      memoryUsage: <div style={{ width: '50px', background: 'orange', color: 'white', textAlign: 'center' }}>80%</div>,
      cpuUsage: <div style={{ width: '50px', background: '#6699ff', color: 'white', textAlign: 'center' }}>6%</div>,
      networkUsage: <div style={{ width: '50px', background: '#99ccff', color: 'white', textAlign: 'center' }}>0%</div>,
      spm: 'SPM',
    },
  ];

  return (
    <div id="section">
      <HeaderButton
        title="컴퓨팅 > "
        subtitle="호스트"
        buttons={섹션헤더버튼들}
        popupItems={섹션헤더팝업아이템들}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <div className="section_table_outer">
            <button>
              <i className="fa fa-refresh"></i>
            </button>
            <Table columns={TableColumnsInfo.HOSTS_ALT} data={data} onRowClick={handleRowClick} />
          </div>
        </div>
      </div>

      {/* 새로 만들기 팝업 */}
      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="새로 만들기"
        className="host_new_popup"
        overlayClassName="host_new_outer"
        shouldCloseOnOverlayClick={false}
      >
        <div className="domain_header">
          <h1>새 호스트</h1>
          <button onClick={closeModal}>
            <i className="fa fa-times"></i>
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
              id="SPM_섹션_btn"
              onClick={() => 섹션변경('SPM')}
              style={{ backgroundColor: 활성화된섹션 === 'SPM' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === 'SPM_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === 'SPM_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>SPM</span>
            </div>
            <div
              className={`edit_aside_item`}
              id="콘솔및GPU_섹션_btn"
              onClick={() => 섹션변경('콘솔및GPU_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '콘솔및GPU_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '콘솔및GPU_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '콘솔및GPU_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>콘솔 및 GPU</span>
            </div>
          </div>
          <div className="edit_aside">
            <div
              className={`edit_aside_item`}
              id="커널_섹션_btn"
              onClick={() => 섹션변경('커널_섹션')}
              style={{ backgroundColor: 활성화된섹션 === '커널_섹션' ? '#EDEDED' : '#FAFAFA', color: 활성화된섹션 === '커널_섹션' ? '#1eb8ff' : 'black', borderBottom: 활성화된섹션 === '커널_섹션' ? '1px solid blue' : 'none' }}
            >
              <span>커널</span>
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
                      <select id="cluster">
                          <option value="default">Default</option>
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
      <div>
          <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
          <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
          <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
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
              <button>+</button>
              <span>고급 매개 변수</span>
            </div>
            

            </div>

            {/*SPM */}
            <div
              id="SPM"
              style={{ display: 활성화된섹션 === 'SPM' ? 'block' : 'none' }}
            >
              <div className='host_spm'>
                <span>SPM 우선순위</span>
                <div className='host_radiobox'>
                  <input type="radio" id="none" name="priority_option" />
                  <label htmlFor="none">없음</label>
              </div>
              <div className='host_radiobox'>
                  <input type="radio" id="low" name="priority_option" />
                  <label htmlFor="low">낮음</label>
              </div>
              <div className='host_radiobox'>
                  <input type="radio" id="standard" name="priority_option" checked />
                  <label htmlFor="standard">표준</label>
              </div>
              <div className='host_radiobox'>
                  <input type="radio" id="high" name="priority_option" />
                  <label htmlFor="high">높음</label>
              </div>


              </div>
            </div>

            {/* 콘솔 및 GPU 섹션 */}
            <div
              id="콘솔및GPU_섹션"
              style={{ display: 활성화된섹션 === '콘솔및GPU_섹션' ? 'block' : 'none' }}
            >
              <div className='host_text_checkbox_box'>
                <div>
                  <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                  <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="text"></input>
                </div>
              </div>

              <div className='host_cpu_radio'>
                  <div>
                    <span>vGPU 배지</span>
                    <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                  </div>
                  <div className='host_radiobox'>
                      <input type="radio" id="integrated" name="distribution_option" checked />
                      <label htmlFor="integrated">통합</label>
                  </div>
                  <div className='host_radiobox'>
                      <input type="radio" id="distributed" name="distribution_option" />
                      <label htmlFor="distributed">분산</label>
                  </div>

              </div>
            </div>

            {/* 커널 섹션 */}
            <div
              id="커널_섹션"
              style={{ display: 활성화된섹션 === '커널_섹션' ? 'block' : 'none' }}
            >
              <div className='kernel_first_content'>
                <span>커널 부트 매개변수</span>
                <div>
                  <i class="fa fa-exclamation-triangle"></i>
                  <div>
                    커널 부트 매개 변수 설정을 변경하면..
                  </div>
                </div>
                <div style={{marginBottom:'0'}}>
                  <i class="fa fa-exclamation-triangle"></i>
                  <div>
                    커널 부트 매개 변수 설정을 변경하면..
                  </div>
                </div>
              </div>

              <div className='kernel_second_content'>
                <div >
                  <input type="checkbox" id="host_device_passthrough" name="host_device_passthrough" />
                  <label htmlFor="host_device_passthrough">호스트 장치 통과 & SR-IOV</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="checkbox" id="unified_virtualization" name="unified_virtualization" />
                  <label htmlFor="unified_virtualization">통합된 가상화</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="checkbox" id="unsafe_interrupts" name="unsafe_interrupts" />
                  <label htmlFor="unsafe_interrupts">안전하지 않은 인터럽트</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="checkbox" id="pci_stub" name="pci_stub" />
                  <label htmlFor="pci_stub">PCI 저버치</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="checkbox" id="nouveau_blacklist" name="nouveau_blacklist" />
                  <label htmlFor="nouveau_blacklist">Nouveau 블랙리스트 등록</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                <div>
                  <input type="checkbox" id="fips_mode" name="fips_mode" />
                  <label htmlFor="fips_mode">FIPS 모드</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
                
              </div>

              
                <div className='host_text_checkbox_box' style={{padding:'0 0.5rem'}}>
                  <div>
                    <input type="checkbox" id="smt_usage" name="smt_usage" />
                    <label htmlFor="smt_usage">SMT 사용안함</label>
                    <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                  </div>
                  <div>
                    <input type="text" id="smt_usage" name="smt_usage" />
                  </div>
                </div>

                <div  className='kernel_reset_btn'>
                  <button>재설정</button>
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

        <div className="host_edit_footer">

          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>
      
      <Footer/>
    </div>
  );
};

export default Host;
