import React, { useState,useEffect } from 'react';
import NavButton from '../components/navigation/NavButton';


function StorageDisk({ togglePopupBox, isPopupBoxVisible, handlePopupBoxItemClick }) {
  const [activeTab, setActiveTab] = useState('general');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  // nav 컴포넌트
  const sections = [
    { id: 'general', label: '일반' },
    { id: 'machine', label: '가상머신' },
    { id: 'storage', label: '스토리지' },
    { id: 'permission', label: '권한' },
  ];
  // 옵션박스 열고닫기
  const [isUploadOptionBoxVisible, setUploadOptionBoxVisible] = useState(false);
  const toggleUploadOptionBox = () => {
    setUploadOptionBoxVisible(!isUploadOptionBoxVisible);
  };
  // 바탕클릭하면 옵션박스 닫기
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        isUploadOptionBoxVisible &&
        !event.target.closest('.upload_option_box') &&
        !event.target.closest('.upload_option_boxbtn')
      ) {
        setUploadOptionBoxVisible(false);
      }
    };
  
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isUploadOptionBoxVisible]);
  return (
    <div className="content_detail_section">
      <div className="section_header">
        <div className="section_header_left">
          <span>스토리지 </span>
          <span>디스크</span>
          <div>hosted_storage</div>
          <button>
            <i className="fa fa-exchange"></i>
          </button>
        </div>

        <div className="section_header_right">
          <div className="article_nav">
            <button>수정</button>
            <button>제거</button>
            <button>이동</button>
            <button>복사</button>
            <button className='upload_option_boxbtn'>업로드 
                  <i class="fa fa-angle-down" onClick={toggleUploadOptionBox}></i>
            </button>
            <button id="popup_btn" onClick={togglePopupBox}>
              <i className="fa fa-ellipsis-v"></i>
              {isPopupBoxVisible && (
                <div id="popup_box" onClick={handlePopupBoxItemClick}>
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
                    <div id="domain">Export to Data Domai</div>
                    <div id="ova_btn">OVA로 내보내기</div>
                  </div>
                </div>
              )}
            </button>
          </div>
        </div>
      </div>

      <div className="content_outer">
      <NavButton 
                sections={sections} 
                activeSection={activeTab} 
                handleSectionClick={handleTabClick} 
            />

        {activeTab === 'general' && (
          <div className="section_content_outer">
            <div className="table_container_left">
              <table className="table">
                <tbody>
                  <tr>
                    <th>별칭:</th>
                    <td>he_metadata</td>
                  </tr>
                  <tr>
                    <th>설명:</th>
                    <td>Hosted-Engine metadata disk</td>
                  </tr>
                  <tr>
                    <th>ID:</th>
                    <td>b7bad714-6d4d-4fbf-83a4-f4b1eff449b3</td>
                  </tr>
                  <tr>
                    <th>디스크 프로파일:</th>
                    <td>hosted_storage</td>
                  </tr>
                  <tr>
                    <th>삭제 후 초기화:</th>
                    <td>아니요</td>
                  </tr>
                  <tr>
                    <th>가상 크기:</th>
                    <td>1 GiB</td>
                  </tr>
                  <tr>
                    <th>실제 크기:</th>
                    <td> 1 GiB</td>
                  </tr>
                  
                </tbody>
              </table>
            </div>
          </div>
        )}


        {activeTab === 'machine' && (
          <div id="detail_machine_outer">

          <div className="pregroup_content">
              <table className="empty_table">
                <thead>
                  <tr>
                    <th>이름</th>
                    <th>클러스터</th>
                    <th>볼륨 유형</th>
                    <th>브릭</th>
                    <th>정보</th>
                    <th>사용한 공간</th>
                    <th>작업</th>
                    <th>스냅샷 수</th>
                  </tr>
                </thead>
                <tbody>
                  <span className="empty_content">표시할 항복이 없습니다</span>
                  <tr></tr>
                </tbody>
              </table>
            </div>
          </div>
        )}

        {activeTab === 'storage' && (
        <div id="detail_disk_outer">
       
            <div className="pregroup_content">
            
            

            
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
                    <td>OK</td>
                    <td>이미지</td>
                    <td>testa</td>
                    </tr>
                </tbody>
                </table>
            </div>
            </div>
       </div>
       
        )}


        {activeTab === 'permission' && (
        <div id="detail_right_outer">
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

export default StorageDisk;
