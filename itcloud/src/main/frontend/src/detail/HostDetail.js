import React, { useState } from 'react';

function HostDetail() {

    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };


    return (
        <div id='host_detail_section'>
            <div className="section_header">
                <div className="section_header_left">
                    <span>컴퓨팅</span>
                    <span>호스트</span>
                    <div>목록이름</div>
                    <button>
                    <i className="fa fa-exchange"></i>
                    </button>
                </div>
                <div className="section_header_right">
                    <div className="article_nav">
                        <button>편집</button>
                        <button>삭제</button>
                        <button>관리</button>
                        <button>설치</button>
                        <button>호스트 콘솔</button>
                        <button>호스트 네트워크 복사</button>
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
                        className={activeTab === 'datacenter' ? 'active' : ''}
                        style={{ color: activeTab === 'datacenter' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('datacenter')}
                        >
                        가상머신
                        </div>
                        <div
                        className={activeTab === 'machine' ? 'active' : ''}
                        style={{ color: activeTab === 'machine' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('machine')}
                        >
                        네트워크 인터페이스
                        </div>
                        <div
                        className={activeTab === 'template' ? 'active' : ''}
                        style={{ color: activeTab === 'template' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('template')}
                        >
                        호스트 장치
                        </div>
                        <div
                        className={activeTab === 'disk' ? 'active' : ''}
                        style={{ color: activeTab === 'disk' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('disk')}
                        >
                        호스트 후크
                        </div>
                        <div
                        className={activeTab === 'disk_snapshot' ? 'active' : ''}
                        style={{ color: activeTab === 'disk_snapshot' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('disk_snapshot')}
                        >
                        권한
                        </div>
                        <div
                        className={activeTab === 'lease' ? 'active' : ''}
                        style={{ color: activeTab === 'lease' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('lease')}
                        >
                        선호도 레이블
                        </div>
                        <div
                        className={activeTab === 'disk_profile' ? 'active' : ''}
                        style={{ color: activeTab === 'disk_profile' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('disk_profile')}
                        >
                        에라타
                        </div>
                        <div
                        className={activeTab === 'event' ? 'active' : ''}
                        style={{ color: activeTab === 'event' ? '#6999D9' : 'black' }}
                        onClick={() => handleTabClick('event')}
                        >
                        이벤트
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

                {activeTab === 'datacenter' && (
                <div id="detail_datacenter_outer">
                    <div className="pregroup_content">
                    <div className="content_header_right">
                        <button>연결</button>
                        <button>분리</button>
                        <button>활성</button>
                        <button>유지보수</button>
                    </div>
                    <div className="application_content_header">
                        <button><i className="fa fa-chevron-left"></i></button>
                        <div>1-1</div>
                        <button><i className="fa fa-chevron-right"></i></button>
                        <button><i className="fa fa-ellipsis-v"></i></button>
                    </div>

                    <table>
                        <thead>
                        <tr>
                            <th></th>
                            <th>이름</th>
                            <th>데이터 센터 내의 도메인 상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td style={{ textAlign: 'center' }}>
                            <i className="fa fa-exclamation"></i>
                            </td>
                            <td>Default</td>
                            <td>활성화</td>
                        </tr>
                        </tbody>
                    </table>
                    </div>
                </div>
                )}

                {activeTab === 'machine' && (
                <div id="detail_machine_outer">

                    <div className="pregroup_content">
                    <table>
                        <thead>
                        <tr>
                            <th>별칭</th>
                            <th>디스크</th>
                            <th>템플릿</th>
                            <th>가상 크기</th>
                            <th>실제 크기</th>
                            <th>생성 일자</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                            <i className="fa fa-plus-circle"></i>
                            test02
                            </td>
                            <td>1</td>
                            <td>Blank</td>
                            <td>1 GIB</td>
                            <td>5 GIB</td>
                            <td>2024.1.19 AM9:21:57</td>
                        </tr>
                        </tbody>
                    </table>
                    </div>
                </div>
                )}

                {activeTab === 'template' && (
                <div id="detail_template_outer">
                
                    <div className="pregroup_content">
                        <table>
                        <thead>
                            <tr>
                            <th>별칭</th>
                            <th>디스크</th>
                            <th>가상 크기</th>
                            <th>실제 크기</th>
                            <th>생성 일자</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td>
                                <i className="fa fa-plus-circle"></i>
                                test02
                            </td>
                            <td></td>
                            <td>1 GIB</td>
                            <td>5 GIB</td>
                            <td>2024.1.19 AM9:21:57</td>
                            </tr>
                        </tbody>
                        </table>
                    </div>
                </div>
                )}

                {activeTab === 'disk' && (
                <div id="detail_disk_outer">
            
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
                    <div className="table_outer">
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

                {activeTab === 'disk_snapshot' && (
                <div id="detail_snapshot_outer">
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
                    <div className="table_outer">
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

                {activeTab === 'lease' && (
                    <div id="detail_rent_outer">
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
                            <div className="table_outer">
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

                {activeTab === 'disk_profile' && (
                <div id="detail_profile_outer">
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
                        <div className="table_outer">
                        <table>
                            <thead>
                            <tr>
                                <th>이름</th>
                                <th>설명</th>
                                <th>QoS이름</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>hosted_storage</td>
                                <td></td>
                                <td>제한 없음</td>
                            </tr>
                            </tbody>
                        </table>
                        </div>
                    </div>
                </div>
                
                )}

                {activeTab === 'event' && (
                <div id="detail_event_outer">
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
                    <div className="table_outer">
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

                
            </div>




        </div>
    );
}

export default HostDetail;