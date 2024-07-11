import React, { useEffect, useState} from 'react';
import '../App.css';

function Storage() {

  // 폰트사이즈
  useEffect(() => {
      function adjustFontSize() {
          const width = window.innerWidth;
          const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
          document.documentElement.style.fontSize = fontSize + 'px';
      }
      window.addEventListener('resize', adjustFontSize);
      adjustFontSize();

      return () => {
          window.removeEventListener('resize', adjustFontSize);
      };
  }, []);
    //footer
    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

    const toggleFooterContent = () => {
        setFooterContentVisibility(!isFooterContentVisible);
    };

    const handleFooterTabClick = (tab) => {
        setSelectedFooterTab(tab);
    };

    return (
        <div id="storage_section">
        <div className="section_header">
          <div className="section_header_left">
            <span>데이터 센터</span>
            <div>Default</div>
            <button>
              <i className="fa fa-exchange"></i>
            </button>
          </div>
          <div className="section_header_right">
            <div className="article_nav">
              <button>편집</button>
              <button>삭제</button>
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
                    <div id="domain">Export to Data Domai</div>
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
                <div class="active">디스크</div>
                <div>도메인</div>
                <div>볼륨</div>
                <div>스토리지</div>
                <div>논리 네트워크</div>
                <div>클러스터</div>
                <div>권한</div>
                <div>이벤트</div>
            </div>
          </div>
  
          <div className="section_content_outer">
            <div className="content_header_right">
              <button id="storage_disk_new_btn">새로 만들기</button>
              <button>수정</button>
              <button>제거</button>
              <button>이동</button>
              <button>복사</button>
              <button id="storage_disk_upload">업로드</button>
              <button>다운로드</button>
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
            {/* <div className="section_option">
              <div>
                <label htmlFor="disk_type">디스크 유형:</label>
                <select id="disk_type" value={diskType} onChange={(e) => setDiskType(e.target.value)}>
                  <option value="default">모두</option>
                  <option value="image">이미지</option>
                  <option value="direct_lun">직접LUN</option>
                  <option value="managed_block">관리되는 블록</option>
                </select>
              </div>
              <div>
                <label htmlFor="content_type">컨텐츠 유형:</label>
                <select id="content_type" value={contentType} onChange={(e) => setContentType(e.target.value)}>
                  <option value="default">모두</option>
                </select>
              </div>
              <div className="search_box">
                <input type="text" />
                <button>
                  <i className="fa fa-search"></i>
                </button>
              </div>
            </div> */}
  
            <div className="section_table_outer">
              <button>
                <i className="fa fa-refresh"></i>
              </button>
              <table>
                <thead>
                  <tr>
                    <th>별칭</th>
                    <th>ID</th>
                    <th>
                      <i className="fa fa-glass"></i>
                    </th>
                    <th></th>
                    <th>연결 대상</th>
                    <th>스토리지 도메인</th>
                    <th>가상 크기</th>
                    <th>상태</th>
                    <th>유형</th>
                    <th>설명</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>he_metadata</td>
                    <td style={{ width: '10%' }}>289137398279301798</td>
                    <td></td>
                    <td>
                      <i className="fa fa-glass"></i>
                    </td>
                    <td>on20-ap01</td>
                    <td>VirtIO-SCSI</td>
                    <td>/dev/sda</td>
                    <td>OK</td>
                    <td>이미지</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td>he_metadata</td>
                    <td>289137398279301798</td>
                    <td></td>
                    <td>
                      <i className="fa fa-glass"></i>
                    </td>
                    <td>on20-ap01</td>
                    <td>VirtIO-SCSI</td>
                    <td>/dev/sda</td>
                    <td>OK</td>
                    <td>이미지</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td>he_metadata</td>
                    <td style={{ width: '10%' }}>289137398279301798</td>
                    <td></td>
                    <td>
                      <i className="fa fa-glass"></i>
                    </td>
                    <td>on20-ap01</td>
                    <td>VirtIO-SCSI</td>
                    <td>/dev/sda</td>
                    <td>OK</td>
                    <td>이미지</td>
                    <td></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
  
          <div id="storage_context_menu">
            <div>새로 만들기</div>
            <div>새로운 도메인</div>
            <div>도메인 가져오기</div>
            <div>도메인 관리</div>
            <div>삭제</div>
            <div>Connections</div>
          </div>
        </div>
  
        <div className="storage_disk_new_outer">
          <div className="storage_disk_new_popup">
            <div className="network_popup_header">
              <h1>새 가상 디스크</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div id="disk_new_nav">
              <div id="storage_img_btn">이미지</div>
              <div id="storage_directlun_btn">직접LUN</div>
              <div id="storage_managed_btn">관리되는 블록</div>
            </div>
            <div className="disk_new_img">
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">데이터 센터</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">스토리지 도메인</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">할당 정책</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">디스크 프로파일</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" id="reset_after_deletion" />
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
                <div>
                  <input type="checkbox" id="incremental_backup" checked />
                  <label htmlFor="incremental_backup">중복 백업 사용</label>
                </div>
              </div>
            </div>
  
            <div id="storage_directlun_outer" style={{ display: 'none' }}>
              <div id="storage_lun_first">
                <div className="disk_new_img_left">
                  <div className="img_input_box">
                    <span>별칭</span>
                    <input type="text" />
                  </div>
                  <div className="img_input_box">
                    <span>설명</span>
                    <input type="text" />
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">데이터 센터</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">호스트</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="os">스토리지 타입</label>
                    <select id="os">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                </div>
                <div className="disk_new_img_right">
                  <div>
                    <input type="checkbox" className="shareable" />
                    <label htmlFor="shareable">공유 가능</label>
                  </div>
                </div>
              </div>
            </div>
  
            <div id="storage_managed_outer" style={{ display: 'none' }}>
              <div id="disk_managed_block_left">
                <div className="img_input_box">
                  <span>크기(GIB)</span>
                  <input type="text" disabled />
                </div>
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" value="on20-ap01_Disk1" disabled />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" disabled />
                </div>
                <div className="img_select_box">
                  <label htmlFor="data_center_select">데이터 센터</label>
                  <select id="data_center_select" disabled>
                    <option value="dc_linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="storage_domain_select">스토리지 도메인</label>
                  <select id="storage_domain_select" disabled>
                    <option value="sd_linux">Linux</option>
                  </select>
                </div>
                <span>
                  해당 데이터 센터에 디스크를 생성할 수 있는 권한을 갖는 사용 가능한 관리 블록 스토리지 도메인이
                  없습니다.
                </span>
              </div>
              <div id="disk_managed_block_right">
                <div>
                  <input type="checkbox" id="disk_shared_option" disabled />
                  <label htmlFor="disk_shared_option">공유 가능</label>
                </div>
              </div>
            </div>
  
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div className="storage_disk_upload_outer">
          <div className="storage_disk_upload_popup">
            <div className="network_popup_header">
              <h1>이미지 업로드</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="storage_upload_first">
              <button>파일 선택</button>
              <div>선택된 파일 없음</div>
            </div>
            <div className="storage_upload_second">
              <div className="disk_option">디스크옵션</div>
              <div className="disk_new_img" style={{ paddingTop: '0.4rem' }}>
                <div className="disk_new_img_left">
                  <div className="img_input_box">
                    <span>크기(GIB)</span>
                    <input type="text" disabled />
                  </div>
                  <div className="img_input_box">
                    <span>별칭</span>
                    <input type="text" />
                  </div>
                  <div className="img_input_box">
                    <span>설명</span>
                    <input type="text" />
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="data_hub">데이터 센터</label>
                    <select id="data_hub">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="storage_zone">스토리지 도메인</label>
                    <select id="storage_zone">
                      <option value="linux">Linux</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="disk_pattern">디스크 프로파일</label>
                    <select id="disk_pattern">
                      <option value="nfs_storage">NFS-Storage</option>
                    </select>
                  </div>
                  <div className="img_select_box">
                    <label htmlFor="compute_unit">호스트</label>
                    <select id="compute_unit">
                      <option value="host01">host01.ititinfo.com</option>
                    </select>
                  </div>
                </div>
                <div className="disk_new_img_right">
                  <div>
                    <input type="checkbox" id="reset_after_deletion" />
                    <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                  </div>
                  <div>
                    <input type="checkbox" className="shareable" />
                    <label htmlFor="shareable">공유 가능</label>
                  </div>
                  <div style={{ marginBottom: '0.4rem' }}>
                    <input type="checkbox" id="incremental_backup" checked />
                    <label htmlFor="incremental_backup">중복 백업 사용</label>
                  </div>
                  <div>
                    <button>연결테스트</button>
                  </div>
                </div>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div id="storage_domain_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div>스토리지</div>
              <div>논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_domain_content">
            <div className="content_header_right">
              <button id="new_domain_btn">새로운 도메인</button>
              <button id="get_domain_btn">도메인 가져오기</button>
              <button id="administer_domain_btn">도메인 관리</button>
              <button>삭제</button>
              <button>Connections</button>
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
            <div className="search_box">
              <input type="text" />
              <button>
                <i className="fa fa-search"></i>
              </button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>상태</th>
                  <th></th>
                  <th>도메인 이름</th>
                  <th>코멘트</th>
                  <th>도메인 유형</th>
                  <th>스토리지 유형</th>
                  <th>포맷</th>
                  <th>데이터 센터간 상태</th>
                  <th>전체 공간(GB)</th>
                  <th>여유 공간(GB)</th>
                  <th>확보된 여유 공간(GB)</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <div className="storage_domain_new_outer">
          <div className="storage_domain_new_popup">
            <div className="network_popup_header">
              <h1>새로운 도메인</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="storage_domain_new_first">
              <div className="domain_new_left">
                <div className="domain_new_select">
                  <label htmlFor="data_hub_location">데이터 센터</label>
                  <select id="data_hub_location">
                    <option value="linux">Default(VS)</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="domain_feature_set">도메인 기능</label>
                  <select id="domain_feature_set">
                    <option value="linux">데이터</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="storage_option_type">스토리지 유형</label>
                  <select id="storage_option_type">
                    <option value="linux">NFS</option>
                  </select>
                </div>
                <div className="domain_new_select" style={{ marginBottom: 0 }}>
                  <label htmlFor="host_identifier">호스트</label>
                  <select id="host_identifier">
                    <option value="linux">host02.ititinfo.com</option>
                  </select>
                </div>
              </div>
              <div className="domain_new_right">
                <div className="domain_new_select">
                  <label>호스트</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>설명</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>코멘트</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div className="storage_domain_new_second">
              <div>
                <label htmlFor="data_hub">내보내기 경로</label>
                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                <span>사용자 정의 연결 매개 변수</span>
                <div id="domain_hidden_box" style={{ display: 'none' }}>
                  <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">호스트</label>
                    <select id="data_hub">
                      <option value="linux">host02.ititinfo.com</option>
                    </select>
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">재전송</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">추가 마운트 옵션</label>
                    <input type="text" />
                  </div>
                </div>
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                <span>고급 매개 변수</span>
                <div id="domain_hidden_box2" style={{ display: 'none' }}>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="format_type_selector" style={{ color: 'gray' }}>
                      포맷
                    </label>
                    <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                    </select>
                  </div>
                  <div className="network_checkbox_type2">
                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                    <label htmlFor="photo_separation">포토 분리</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div className="storage_domain_get_outer">
          <div className="storage_domain_get_popup">
            <div className="network_popup_header">
              <h1>사전 구성된 도메인 가져오기</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="storage_domain_new_first">
              <div className="domain_new_left">
                <div className="domain_new_select">
                  <label htmlFor="data_hub_location">데이터 센터</label>
                  <select id="data_hub_location">
                    <option value="linux">Default(VS)</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="domain_feature_set">도메인 기능</label>
                  <select id="domain_feature_set">
                    <option value="linux">데이터</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="storage_option_type">스토리지 유형</label>
                  <select id="storage_option_type">
                    <option value="linux">NFS</option>
                  </select>
                </div>
                <div className="domain_new_select" style={{ marginBottom: 0 }}>
                  <label htmlFor="host_identifier">호스트</label>
                  <select id="host_identifier">
                    <option value="linux">host02.ititinfo.com</option>
                  </select>
                </div>
              </div>
              <div className="domain_new_right">
                <div className="domain_new_select">
                  <label>이름</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>설명</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>코멘트</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div className="storage_domain_new_second">
              <div>
                <label htmlFor="data_hub">내보내기 경로</label>
                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                <span>사용자 정의 연결 매개 변수</span>
                <div id="domain_hidden_box" style={{ display: 'none' }}>
                  <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">호스트</label>
                    <select id="data_hub">
                      <option value="linux">host02.ititinfo.com</option>
                    </select>
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">재전송</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">추가 마운트 옵션</label>
                    <input type="text" />
                  </div>
                </div>
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                <span>고급 매개 변수</span>
                <div id="domain_hidden_box2" style={{ display: 'none' }}>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="format_type_selector" style={{ color: 'gray' }}>
                      포맷
                    </label>
                    <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                    </select>
                  </div>
                  <div className="network_checkbox_type2">
                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                    <label htmlFor="photo_separation">포토 분리</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div className="storage_domain_administer_outer">
          <div className="storage_domain_administer_popup">
            <div className="network_popup_header">
              <h1>도메인 관리</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="storage_domain_new_first">
              <div className="domain_new_left">
                <div className="domain_new_select">
                  <label htmlFor="data_hub_location">데이터 센터</label>
                  <select id="data_hub_location">
                    <option value="linux">Default(VS)</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="domain_feature_set">도메인 기능</label>
                  <select id="domain_feature_set">
                    <option value="linux">데이터</option>
                  </select>
                </div>
                <div className="domain_new_select">
                  <label htmlFor="storage_option_type">스토리지 유형</label>
                  <select id="storage_option_type">
                    <option value="linux">NFS</option>
                  </select>
                </div>
                <div className="domain_new_select" style={{ marginBottom: 0 }}>
                  <label htmlFor="host_identifier">호스트</label>
                  <select id="host_identifier">
                    <option value="linux">host02.ititinfo.com</option>
                  </select>
                </div>
              </div>
              <div className="domain_new_right">
                <div className="domain_new_select">
                  <label>이름</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>설명</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>코멘트</label>
                  <input type="text" />
                </div>
              </div>
            </div>
            <div className="storage_domain_new_second">
              <div>
                <label htmlFor="data_hub">내보내기 경로</label>
                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn"></i>
                <span>사용자 정의 연결 매개 변수</span>
                <div id="domain_hidden_box" style={{ display: 'none' }}>
                  <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">호스트</label>
                    <select id="data_hub">
                      <option value="linux">host02.ititinfo.com</option>
                    </select>
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">재전송</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="data_hub">추가 마운트 옵션</label>
                    <input type="text" />
                  </div>
                </div>
              </div>
              <div>
                <i className="fa fa-chevron-circle-right" id="domain_hidden_box_btn2"></i>
                <span>고급 매개 변수</span>
                <div id="domain_hidden_box2" style={{ display: 'none' }}>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label>디스크 공간 부족 경고 표시(%)</label>
                    <input type="text" />
                  </div>
                  <div className="domain_new_select">
                    <label htmlFor="format_type_selector" style={{ color: 'gray' }}>
                      포맷
                    </label>
                    <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                    </select>
                  </div>
                  <div className="network_checkbox_type2">
                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                    <label htmlFor="photo_separation">포토 분리</label>
                  </div>
                </div>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div id="storage_volume_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div className="active">볼륨</div>
              <div>스토리지</div>
              <div>논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_volume_content">
            <div className="content_header_right">
              <button id="storage_volume_new_btn">새로 만들기</button>
              <button>삭제</button>
              <button>시작</button>
              <button className="disabled_button">중지</button>
              <button>프로파일링</button>
              <div>
                <button id="storage_volume_snap_btn" style={{ margin: 0 }}>
                  스냅샷
                </button>
                <button id="storage_volume_option_boxbtn">
                  <i className="fa fa-chevron-down"></i>
                  <div className="storage_volume_option_box" style={{ display: 'none' }}>
                    <div>새로 만들기</div>
                    <div>스케줄 편집</div>
                    <div>옵션 - 클러스터</div>
                    <div>옵션 - 볼륨</div>
                  </div>
                </button>
              </div>
              <button>지역 복제</button>
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
            <div className="search_box">
              <input type="text" />
              <button>
                <i className="fa fa-search"></i>
              </button>
            </div>
            <div className="empty_table">
              <table>
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
        </div>
  
        <div className="storage_volume_new_outer">
          <div className="storage_volume_new_popup">
            <div className="network_popup_header">
              <h1>새 볼륨</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="volume_first_content">
              <div className="domain_new_select">
                <label htmlFor="data_center_selector">데이터 센터</label>
                <select id="data_center_selector">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
              <div className="domain_new_select" style={{ marginBottom: 0 }}>
                <label htmlFor="volume_cluster_selector">볼륨 클러스터</label>
                <select id="volume_cluster_selector">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
            </div>
            <div className="volume_second_content">
              <div className="domain_new_select">
                <label>볼륨 클러스터</label>
                <input type="text" />
              </div>
              <div className="domain_new_select">
                <label htmlFor="vol_cluster_dropdown">볼륨 클러스터</label>
                <select id="vol_cluster_dropdown">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
              <div className="domain_new_select">
                <label htmlFor="data_hub">볼륨 클러스터</label>
                <input type="text" value="3" disabled />
              </div>
              <div className="domain_new_select">
                <label>전송 유형</label>
                <div className="volume_checkboxs">
                  <div className="volume_checkbox" style={{ marginRight: '3rem' }}>
                    <input type="checkbox" />
                    <label htmlFor="photo_separation">TCP</label>
                  </div>
                  <div className="volume_checkbox">
                    <input type="checkbox" />
                    <label htmlFor="photo_separation">RDMA</label>
                  </div>
                </div>
              </div>
              <div className="domain_new_select">
                <label>브릭</label>
                <button>브릭 추가</button>
              </div>
            </div>
            <div className="volume_third_content" style={{ paddingTop: 0 }}>
              <h2>접근 프로토콜</h2>
              <div className="volume_checkbox">
                <input type="checkbox" />
                <label htmlFor="photo_separation">RDMA</label>
              </div>
              <div className="volume_checkbox">
                <input type="checkbox" />
                <label htmlFor="photo_separation">RDMA</label>
              </div>
              <div className="volume_checkbox">
                <input type="checkbox" />
                <label htmlFor="photo_separation">RDMA</label>
              </div>
              <div className="domain_new_select">
                <label htmlFor="data_hub">액세스 허용할 호스트</label>
                <input type="text" />
              </div>
              <div className="volume_checkbox">
                <input type="checkbox" />
                <label htmlFor="photo_separation">RDMA</label>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div className="storage_volume_snap_outer">
          <div className="storage_volume_snap_popup">
            <div className="network_popup_header">
              <h1>볼륨 스냅샷 - 클러스터 옵션</h1>
              <button>
                <i className="fa fa-times"></i>
              </button>
            </div>
            <div className="volume_snap_first_content">
              <div className="domain_new_select">
                <label htmlFor="vol_cluster_dropdown">볼륨 클러스터</label>
                <select id="vol_cluster_dropdown">
                  <option value="linux">host02.ititinfo.com</option>
                </select>
              </div>
              <h2>스냅샷 옵션</h2>
              <div className="volume_snap_table">
                <table>
                  <thead>
                    <tr>
                      <th>이름</th>
                      <th>설명</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>ㅇ</td>
                      <td>ㅇ</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>업데이트</button>
              <button>취소</button>
            </div>
          </div>
        </div>
  
        <div id="storage_storage_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div className="active">스토리지</div>
              <div>논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_domain_content">
            <div className="content_header_right">
              <button>데이터 연결</button>
              <button>ISP 연결</button>
              <button>내보내기 연결</button>
              <button>분리</button>
              <button>활성</button>
              <button>유지보수</button>
            </div>
            <div>
              <div className="application_content_header">
                <button>
                  <i className="fa fa-chevron-left"></i>
                </button>
                <div>1-2</div>
                <button>
                  <i className="fa fa-chevron-right"></i>
                </button>
                <button>
                  <i className="fa fa-ellipsis-v"></i>
                </button>
              </div>
            </div>
            <table>
              <thead>
                <tr>
                  <th>상태</th>
                  <th></th>
                  <th>도메인 이름</th>
                  <th>코멘트</th>
                  <th>도메인 유형</th>
                  <th>스토리지 유형</th>
                  <th>포맷</th>
                  <th>데이터 센터간 상태</th>
                  <th>전체 공간(GB)</th>
                  <th>여유 공간(GB)</th>
                  <th>확보된 여유 공간(GB)</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td>
                    <i className="fa fa-caret-up" style={{ color: '#1DED00' }}></i>
                  </td>
                  <td>
                    <i className="fa fa-glass"></i>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <div id="storage_logic_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div>스토리지</div>
              <div className="active">논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_domain_content">
            <div className="content_header_right">
              <button>새로만들기</button>
              <button>편집</button>
              <button>삭제</button>
            </div>
            <div>
              <div className="application_content_header">
                <button>
                  <i className="fa fa-chevron-left"></i>
                </button>
                <div>1-2</div>
                <button>
                  <i className="fa fa-chevron-right"></i>
                </button>
                <button>
                  <i className="fa fa-ellipsis-v"></i>
                </button>
              </div>
            </div>
            <table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>ovirtmgmt</td>
                  <td>Management Network</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <div id="storage_cluster_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div>스토리지</div>
              <div className="active">논리 네트워크</div>
              <div>클러스터</div>
              <div>권한</div>
              <div>이벤트</div>
            </div>
          </div>
          <div className="storage_domain_content">
            <div>
              <div className="application_content_header">
                <button>
                  <i className="fa fa-chevron-left"></i>
                </button>
                <div>1-2</div>
                <button>
                  <i className="fa fa-chevron-right"></i>
                </button>
                <button>
                  <i className="fa fa-ellipsis-v"></i>
                </button>
              </div>
            </div>
            <table>
              <thead>
                <tr>
                  <th>이름</th>
                  <th>호환 버전</th>
                  <th>설명</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>ovirtmgmt</td>
                  <td>Management Network</td>
                  <td>The default server cluster</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <div id="storage_right_outer" style={{ display: 'none' }}>
          <div className="content_header">
            <div className="content_header_left">
              <div>디스크</div>
              <div>도메인</div>
              <div>볼륨</div>
              <div>스토리지</div>
              <div>논리 네트워크</div>
              <div>클러스터</div>
              <div className="active">권한</div>
              <div>이벤트</div>
            </div>
          </div>
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
                <button>
                  <i className="fa fa-chevron-left"></i>
                </button>
                <div>1-2</div>
                <button>
                  <i className="fa fa-chevron-right"></i>
                </button>
                <button>
                  <i className="fa fa-ellipsis-v"></i>
                </button>
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
                  <td>
                    <i className="fa fa-user"></i>
                  </td>
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
      </div>
    );
};

export default Storage;
