import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import { Table } from '../table/Table';
import HostDetail from './HostDetail';
import './css/Host.css';

// React Modal 설정
Modal.setAppElement('#root');

const Host = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showHostName, setShowHostName] = useState(false);
  const [활성화된섹션, set활성화된섹션] = useState('일반_섹션'); // 기본 섹션을 '일반_섹션'으로 초기화

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
    // "일반" 섹션의 스타일을 기본 선택 상태로 설정
    const 기본섹션 = document.getElementById('일반_섹션_btn');
    if (기본섹션) {
      기본섹션.style.backgroundColor = '#EDEDED';
      기본섹션.style.color = '#1eb8ff';
      기본섹션.style.borderBottom = '1px solid blue';
    }
  }, []); // 빈 배열을 의존성으로 전달하여 컴포넌트가 마운트될 때만 실행되게 함

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

  const handleRowClick = () => {
    setShowHostName(true);
  };

  if (showHostName) {
    return <HostDetail />;
  }

  const columns = [
    { header: '상태', accessor: 'status', clickable: false },
    { header: '이름', accessor: 'name', clickable: true },
    { header: '호환 버전', accessor: 'version', clickable: false },
    { header: '설명', accessor: 'description', clickable: false },
    { header: '클러스터 CPU 유형', accessor: 'cpuType', clickable: false },
    { header: '호스트 수', accessor: 'hostCount', clickable: false },
    { header: '가상 머신 수', accessor: 'vmCount', clickable: false },
  ];

  const data = [
    {
      status: '',
      name: '192.168.0.80',
      version: '4.7',
      description: 'The default server cluster',
      cpuType: 'Secure Intel Cascadelak',
      hostCount: 2,
      vmCount: 7,
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
            <Table columns={columns} data={data} onRowClick={handleRowClick} />
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

    <div>
      <div>
          <input type="checkbox" id="memory_balloon" name="memory_balloon" />
          <label htmlFor="headless_mode">헤드리스 모드</label>
      </div>
      <div>
          <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
          <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
          <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
      </div>
    </div>

    <div>
      <div>
          <label htmlFor="name2">이름</label>
          <input type="text" id="name2" />
      </div>

      <div>
          <input type="radio" id="radio1" name="name_option" />
          <label htmlFor="radio1">옵션 1</label>
          <input type="text" id="radio1_name" />
      </div>

      <div>
          <input type="radio" id="radio2" name="name_option" />
          <label htmlFor="radio2">옵션 2</label>
      </div>
    </div>

</div>{/*일반섹션끝 */}

            {/* 전원 관리 섹션 */}
            <div
              id="전원관리_섹션"
              style={{ display: 활성화된섹션 === '전원관리_섹션' ? 'block' : 'none' }}
            >
              
            <div>
            <div>
                <input type="checkbox" id="enable_forwarding" name="enable_forwarding" />
                <label htmlFor="enable_forwarding">전송 관리 활성</label>
            </div>
            <div>
                <input type="checkbox" id="kdump_usage" name="kdump_usage" checked />
                <label htmlFor="kdump_usage">Kdump 통합</label>
            </div>
            <div>
                <input type="checkbox" id="disable_forwarding_policy" name="disable_forwarding_policy" />
                <label htmlFor="disable_forwarding_policy">전송 관리 정책 제어를 비활성화</label>
            </div>


              <span>순서대로 정렬된 에이전트</span>
            </div>

            <div>
              <span>펜스 에이전트 추가</span>
              <button>+</button>
            </div>

            <div>
              <button>+</button>
              <span>고급 매개 변수</span>
            </div>

            </div>

            {/*SPM */}
            <div
              id="SPM"
              style={{ display: 활성화된섹션 === 'SPM' ? 'block' : 'none' }}
            >
              <div>
                <span>SPM 우선순위</span>
                <div>
                  <input type="radio" id="none" name="priority_option" />
                  <label htmlFor="none">없음</label>
              </div>
              <div>
                  <input type="radio" id="low" name="priority_option" />
                  <label htmlFor="low">낮음</label>
              </div>
              <div>
                  <input type="radio" id="standard" name="priority_option" checked />
                  <label htmlFor="standard">표준</label>
              </div>
              <div>
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
              <div>
                <div>
                  <input type="checkbox" id="headless_mode_info" name="headless_mode_info" />
                  <label htmlFor="headless_mode_info">헤드리스 모드 정보</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
              </div>

              <div>
                  <div>
                    <span>vGPU 배지</span>
                    <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                  </div>
                  <div>
                      <input type="radio" id="integrated" name="distribution_option" checked />
                      <label htmlFor="integrated">통합</label>
                  </div>
                  <div>
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
              <div>
                <div>커널 부트 매개변수</div>
                <div>
                  <i className="fa fa-info-circle"></i>
                  <div>
                    커널 부트 매개 변수 설정을 변경하면..
                  </div>
                </div>
                <div>
                  <i className="fa fa-info-circle"></i>
                  <div>
                    커널 부트 매개 변수 설정을 변경하면..
                  </div>
                </div>
              </div>

              <div>
                <div>
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
                <div>
                  <input type="checkbox" id="smt_usage" name="smt_usage" />
                  <label htmlFor="smt_usage">SMT 사용안함</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                </div>
              </div>

              
                <div>
                  <input type="checkbox" id="smt_usage" name="smt_usage" />
                  <label htmlFor="smt_usage">SMT 사용안함</label>
                  <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
                  <input type="text" id="smt_usage" name="smt_usage" />
                </div>

                <button>재설정</button>
              
            </div>

            {/* 호스트 엔진 섹션 */}
            <div
              id="호스트엔진_섹션"
              style={{ display: 활성화된섹션 === '호스트엔진_섹션' ? 'block' : 'none' }}
            >
              <div id="host_policy">
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
              <div>
                
              </div>
            </div>

          </form>
        </div>

        <div className="host_edit_footer">

          <button>OK</button>
          <button onClick={closeModal}>취소</button>
        </div>
      </Modal>
    </div>
  );
};

export default Host;
