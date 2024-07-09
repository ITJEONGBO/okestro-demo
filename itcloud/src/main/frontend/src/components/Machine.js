import React, { useState, useEffect } from 'react';
import '../App.css';

  // 네트워크 table 반복문
const NetworkSection = () => {
  useEffect(() => {
    const container = document.getElementById("network_content_outer");
    const originalContent = document.querySelector('.network_content');

    for (let i = 0; i < 3; i++) {
      const clone = originalContent.cloneNode(true);
      container.appendChild(clone);
    }
  }, []);

  return (
    <div id="network_outer">
      <div id="network_content_outer">
        <div className="content_header_right">
          <button id="network_popup_new">새로 만들기</button>
          <button>수정</button>
          <button>제거</button>
        </div>
        <div className="network_content">
          <div>
            <i className="fa fa-chevron-right"></i>
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
      </div>
    </div>
  );
};

const DiskSection = () => {
  return (
    <div id="disk_outer">
      <div id="disk_content">
        <div className="content_header_right">
          <button id="disk_popup_new">새로 만들기</button>
          <button id="join_popup_btn">연결</button>
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
    </div>
  );
};

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

const PregroupSection = () => {
  return (
    <div id="pregroup_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="pregroup_create_btn">새로 만들기</button>
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
    </div>
  );
};

const PregroupLabelSection = () => {
  return (
    <div id="pregroup_lable_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="lable_create_btn">새로 만들기</button>
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
    </div>
  );
};
const PowerSection = () => {
  return (
    <div id="power_outer">
      <div className="pregroup_content">
        <div className="content_header_right">
          <button id="power_add_btn">추가</button>
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
    </div>
  );
};

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

// 편집
const editPopup = () => {
  alert("Edit button clicked!");
};


const Machine = () => {
  const [activeSection, setActiveSection] = useState('general');

  const handleSectionClick = (section) => {
    setActiveSection(section);
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
            <button id="edit_btn" onClick={editPopup}>편집</button>
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
            <button id="migration_btn">마이그레이션</button>
            <button id="popup_btn">
              <i className="fa fa-ellipsis-v"></i>
              <div id="popup_box">
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

      
    </div>

      
  );
};

export default Machine;
