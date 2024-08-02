import React, { useState } from 'react';
import NavButton from '../components/navigation/NavButton';
import HeaderButton from '../components/button/HeaderButton';

function HostDetail() {

    const [activeTab, setActiveTab] = useState('general');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };
    //headerbutton 컴포넌트
    const buttons = [
        { id: 'edit_btn', label: '편집', onClick: () => console.log('Edit button clicked') },
        { id: 'delete_btn', label: '삭제', onClick: () => console.log('Delete button clicked') },
        { id: 'manage_btn', label: '관리', onClick: () => console.log('Manage button clicked') },
        { id: 'install_btn', label: '설치', onClick: () => console.log('Install button clicked') },
        { id: 'host_console_btn', label: '호스트 콘솔', onClick: () => console.log('Host Console button clicked') },
        { id: 'copy_network_btn', label: '호스트 네트워크 복사', onClick: () => console.log('Copy Host Network button clicked') },
      ];
    
      const popupItems = []; // 현재 팝업 아이템이 없으므로 빈 배열로 설정
      const uploadOptions = []; // 현재 업로드 옵션이 없으므로 빈 배열로 설정
    // nav컴포넌트
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'machine', label: '가상머신' },
        { id: 'networkinterface', label: '네트워크 인터페이스' },
        { id: 'hostdevice', label: '호스트 장치' },
        { id: 'hosthook', label: '호스트 후크' },
        { id: 'permission', label: '권한' },
        { id: 'lable', label: '선호도 레이블' },
        { id: 'errata', label: '에라타' },
        { id: 'event', label: '이벤트' }
      ];

    return (
        <div id='host_detail_section'>
             <HeaderButton
      title="컴퓨팅"
      subtitle="호스트"
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
                {/* 가상머신 */}
                {activeTab === 'machine' && (
                <div className="storage_right_outer">
                    <div className="storage_domain_content">
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
                            <div>1-2</div>
                            <button><i className="fa fa-chevron-right"></i></button>
                            <button><i className="fa fa-ellipsis-v"></i></button>
                            </div>
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
                )}
                {/* 네트워크 인터페이스 */}
                {activeTab === 'networkinterface' && (
                <div className="host_detail_outer">
                    <div className="pregroup_content">
                        <div className="host_detail_network">
                            <button><i className="fa fa-chevron-left"></i></button>
                            <div>1-1</div>
                            <button><i className="fa fa-chevron-right"></i></button>
                            <button><i className="fa fa-ellipsis-v"></i></button>
                        </div>
                    </div>
                </div>
                )}
                {/* 호스트 장치 */}
                {activeTab === 'hostdevice' && (
                <div className="host_detail_outer">
            
                <div className="pregroup_content">
                <div className="content_header_right">
                    <button>View CPU Pinning</button>
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
                {/* 호스트 후크 */}
                {activeTab === 'hosthook' && (
                <div className="host_detail_outer">
            
                    <div className="pregroup_content">
                    
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
                {/* 권한 */}
                {activeTab === 'permission' && (
                <div className="storage_right_outer">
                    <div className="storage_domain_content">
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
                            <div>1-2</div>
                            <button><i className="fa fa-chevron-right"></i></button>
                            <button><i className="fa fa-ellipsis-v"></i></button>
                            </div>
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
                )}
             
                {/* 선호도 레이블 */}
                {activeTab === 'lable' && (
                    <div id="detail_rent_outer">
                        <div className="pregroup_content">
                            <div className="content_header_right">
                                <button>새로 만들기</button>
                                <button>편집</button>
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
                                        <th>이름</th>
                                        <th>가상머신 멤버</th>
                                        <th>호스트 멤버</th>
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
                {/* 에라타 */}
                {activeTab === 'errata' && (
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
                {/* 이벤트 */}
                {activeTab === 'event' && (
                <div className="host_detail_outer">
                    <div className="pregroup_content">
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