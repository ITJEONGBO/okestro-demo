import React, { useState } from 'react';

function DomainDetail({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const [activeTab, setActiveTab] = useState('general');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <div className="content_detail_section">
      <div className="section_header">
        <div className="section_header_left">
          <span>네트워크 </span>
          <span>논리네트워크</span>
          <div>hosted_storage</div>
          <button>
            <i className="fa fa-exchange"></i>
          </button>
        </div>

        <div className="section_header_right">
          <div className="article_nav">
            <button>편집</button>
            <button>삭제</button> 
          </div>
        </div>

      </div>

      <div className="content_outer">
        <div className="content_header">
        <div className="content_header_left">
            <div
            className={activeTab === 'general' ? 'active' : ''}
            style={{ color: activeTab === 'general' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('general')}
            >
            일반
            </div>
            <div
            className={activeTab === 'vNIC_profile' ? 'active' : ''}
            style={{ color: activeTab === 'vNIC_profile' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('vNIC_profile')}
            >
            vNIC 프로파일
            </div>
            <div
            className={activeTab === 'cluster' ? 'active' : ''}
            style={{ color: activeTab === 'cluster' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('cluster')}
            >
            클러스터
            </div>
            <div
            className={activeTab === 'host' ? 'active' : ''}
            style={{ color: activeTab === 'host' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('host')}
            >
            호스트
            </div>
            <div
            className={activeTab === 'virtual_machine' ? 'active' : ''}
            style={{ color: activeTab === 'virtual_machine' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('virtual_machine')}
            >
            가상 머신
            </div>
            <div
            className={activeTab === 'template' ? 'active' : ''}
            style={{ color: activeTab === 'template' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('template')}
            >
            템플릿
            </div>
            <div
            className={activeTab === 'permission' ? 'active' : ''}
            style={{ color: activeTab === 'permission' ? '#6999D9' : 'black' }}
            onClick={() => handleTabClick('permission')}
            >
            권한
            </div>
        </div>
        </div>


        {activeTab === 'general' && (
          <div className="section_content_outer">
            <div className="table_container_left">
              <table className="table">
                <tbody>
                  <tr>
                    <th>ID:</th>
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
                  <tr>
                    <th>템플릿:</th>
                    <td>Blank</td>
                  </tr>
                  <tr>
                    <th>운영 시스템:</th>
                    <td>Linux</td>
                  </tr>
                  <tr>
                    <th>펌웨어/장치의 유형:</th>
                    <td>
                      BIOS의 Q35 칩셋{' '}
                      <i className="fa fa-ban" style={{ marginLeft: '13%', color: 'orange' }}></i>
                    </td>
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
          </div>
        )}

        {activeTab === 'vNIC_profile' && (
        <div id="detail_vnic_outer">
       
            <div className="pregroup_content">
                <div className="content_header_right">
                    <button>새로 만들기</button>
                    <button>편집</button>
                    <button>제거</button>
                </div>
                <div className="application_content_header">
                    <button><i className="fa fa-chevron-left"></i></button>
                    <div>1-1</div>
                    <button><i className="fa fa-chevron-right"></i></button>
                    <button><i className="fa fa-ellipsis-v"></i></button>
                </div>

                <div className="table_outer2">
                    <table>
                    <thead>
                        <tr>
                            <th>이름</th>
                            <th>네트워크</th>
                            <th>데이터 센터</th>
                            <th>호환 버전</th>
                            <th>QoS 이름</th>
                            <th>네트워크 필터</th>
                            <th>포트 미러링</th>
                            <th>통과</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>ovirtmgmt</td>
                            <td>ovirtmgmt</td>
                            <td>Default</td>
                            <td>4.7</td>
                            <td></td>
                            <td>wdsm-no-mac-spoofing</td>
                            <td></td>
                            <td>아니요</td>
                        </tr>
                    </tbody>
                    </table>
                </div>
            </div>
       </div>
       
        )}

        {activeTab === 'cluster' && (
        <div id="detail_cluster_outer">
       
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>네트워크 관리</button>
            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>호환 버전</th>
                        <th>연결된 네트워크</th>
                        <th>네트워크 상태</th>
                        <th>필수 네트워크</th>
                        <th>네트워크 역할</th>
                        <th>설명</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Default</td>
                        <td>4.7</td>
                        <td><i className="fa fa-chevron-left"></i></td>
                        <td><i className="fa fa-chevron-left"></i></td>
                        <td ><i className="fa fa-chevron-left"></i></td>
                        <td style={{ textAlign: 'center' }}>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                            <i className="fa fa-chevron-left"></i>
                        </td>
                        <td>The default server cluster</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}
        
        {activeTab === 'host' && (
        <div id="detail_host_outer">
       
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>호스트 네트워크 설정</button>

            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>클러스터</th>
                        <th>데이터 센터</th>
                        <th>네트워크 장치 상태</th>
                        <th>비동기</th>
                        <th>네트워크 장치</th>
                        <th>속도</th>
                        <th>Rx</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>aa</td>
                    <td></td>
                    <td></td>
                    <td>&lt;1 GiB</td>
                    <td>&lt;1 GiB</td>
                    <td>씬 프로비저닝</td>
                    <td>hosted_storage</td>
                    <td>2024. 4. 26. PM 3:19:39</td>
                    <td>2024. 4. 26. PM 3:19:45</td>
                    <td><i className="fa fa-chevron-left"></i></td>
                    <td></td>
                    <td>잠김</td>
                    <td>이미지</td>
                    <td>testa</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}

        {activeTab === 'virtual_machine' && (
        <div id="detail_virtual_outer">
       
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>이동</button>
                <button>복사</button>
                <button>제거</button>
                <button>업로드</button>
                <button>다운로드</button>
            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>1-1</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                    <th>별칭</th>
                    <th><i className="fa fa-chevron-left"></i></th>
                    <th><i className="fa fa-chevron-left"></i></th>
                    <th>가상 크기</th>
                    <th>실제 크기</th>
                    <th>할당 정책</th>
                    <th>스토리지 도메인</th>
                    <th>생성 일자</th>
                    <th>최근 업데이트</th>
                    <th></th>
                    <th>연결 대상</th>
                    <th>상태</th>
                    <th>유형</th>
                    <th>설명</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>aa</td>
                    <td></td>
                    <td></td>
                    <td>&lt;1 GiB</td>
                    <td>&lt;1 GiB</td>
                    <td>씬 프로비저닝</td>
                    <td>hosted_storage</td>
                    <td>2024. 4. 26. PM 3:19:39</td>
                    <td>2024. 4. 26. PM 3:19:45</td>
                    <td><i className="fa fa-chevron-left"></i></td>
                    <td></td>
                    <td>잠김</td>
                    <td>이미지</td>
                    <td>testa</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}

        {activeTab === 'template' && (
        <div id="detail_template_outer">
            <div className="pregroup_content">
            <div className="content_header_right">
                <button>제거</button>
            </div>
            <div className="application_content_header">
                <button><i className="fa fa-chevron-left"></i></button>
                <div>0-0</div>
                <button><i className="fa fa-chevron-right"></i></button>
                <button><i className="fa fa-ellipsis-v"></i></button>
            </div>
            <div className="table_outer2">
                <table>
                <thead>
                    <tr>
                    <th>크기</th>
                    <th>생성 일자</th>
                    <th>스냅샷 생성일</th>
                    <th>디스크 별칭</th>
                    <th>스냅샷 설명</th>
                    <th>연결 대상</th>
                    <th>상태</th>
                    <th>디스크 스냅샷 ID</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td colSpan="8" style={{ textAlign: 'center' }}>표시할 항목이 없습니다</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
        </div>
        )}

        {activeTab === 'permission' && (
        <div id="detail_permission_outer">
            <div className="pregroup_content">
              <div className="content_header_right">
                <button>추가</button>
                <button>제거</button>
              </div>
              <div className="storage_right_btns">
                <span>Permission Filters:</span>
                <div>
                  <button>All</button>
                  <button>Direct</button>
                </div>
              </div>
              <div>
                <div className="application_content_header">
                  <button><i className="fa fa-chevron-left"></i></button>
                  <div>1-3</div>
                  <button><i className="fa fa-chevron-right"></i></button>
                  <button><i className="fa fa-ellipsis-v"></i></button>
                </div>
              </div>
              <div className="table_outer2">
                <table style={{ marginTop: 0 }}>
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
                      <td>ovirtmgmt</td>
                      <td></td>
                      <td>*</td>
                      <td>SuperUser</td>
                      <td>2023.12.29 AM 11:40:58</td>
                      <td>(시스템)</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
        </div>
        )}

      </div>
    </div>
  );
}

export default DomainDetail;
