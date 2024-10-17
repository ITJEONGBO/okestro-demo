import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight } from '@fortawesome/free-solid-svg-icons';

const StorageDomainsModal = ({
  isOpen,
  onRequestClose,
  onSubmit,
  storageType,
  handleStorageTypeChange,
  toggleDomainHiddenBox,
  toggleDomainHiddenBox2,
  isDomainHiddenBoxVisible,
  isDomainHiddenBox2Visible
}) => {
  const [domainName, setDomainName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');

  const handleFormSubmit = () => {
    const requestData = {
      domainName,
      description,
      comment,
      storageType
    };
    onSubmit(requestData);
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="새로운 도메인"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_domain_new_popup">
        <div className="popup_header">
          <h1>새로운 도메인</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
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
                <option value="data">데이터</option>
                <option value="iso">ISO</option>
                <option value="export">내보내기</option>
              </select>
            </div>
            <div className="domain_new_select">
              <label htmlFor="storage_option_type">스토리지 유형</label>
              <select id="storage_option_type" value={storageType} onChange={handleStorageTypeChange}>
                <option value="NFS">NFS</option>
                <option value="iSCSI">iSCSI</option>
                <option value="fc">파이버 채널</option>
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
              <input type="text" value={domainName} onChange={(e) => setDomainName(e.target.value)} />
            </div>
            <div className="domain_new_select">
              <label>설명</label>
              <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div className="domain_new_select">
              <label>코멘트</label>
              <input type="text" value={comment} onChange={(e) => setComment(e.target.value)} />
            </div>
          </div>
        </div>

        {/* NFS 스토리지 유형 관련 UI */}
        {storageType === 'NFS' && (
          <div className="storage_popup_NFS">
            <div className="network_form_group">
              <label htmlFor="data_hub">NFS 서버 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>

            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox} fixedWidth />
              <span>사용자 정의 연결 매개 변수</span>
              <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                <div className="domain_new_select">
                  <label htmlFor="nfs_version">NFS 버전</label>
                  <select id="nfs_version">
                    <option value="host02_ititinfo_com">host02.ititinfo.com</option>
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
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2} fixedWidth />
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
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
              </div>
            </div>
          </div>
        )}

        {/* iSCSI 스토리지 유형 관련 UI */}
        {storageType === 'iSCSI' && (
          <div className="storage_popup_iSCSI">
            {/* 추가적인 iSCSI 관련 UI 구성 */}
            <p>iSCSI 스토리지 설정이 여기에 표시됩니다.</p>
          </div>
        )}

        {/* 파이버 채널 스토리지 유형 관련 UI */}
        {storageType === 'fc' && (
          <div className="storage_popup_fc">
            {/* 추가적인 파이버 채널 관련 UI 구성 */}
            <p>파이버 채널 스토리지 설정이 여기에 표시됩니다.</p>
          </div>
        )}

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default StorageDomainsModal;


    
      {/*도메인(새로운 도메인)팝업 */}
      // <Modal
      //     isOpen={activePopup === 'newDomain'}
      //     onRequestClose={closePopup}
      //     contentLabel="새로운 도메인"
      //     className="Modal"
      //     overlayClassName="Overlay"
      //     shouldCloseOnOverlayClick={false}
      // >
      //     <div className="storage_domain_new_popup">
      //     <div className="popup_header">
      //         <h1>새로운 도메인</h1>
      //         <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
      //     </div>

      //     <div className="storage_domain_new_first">
      //         <div className="domain_new_left">
      //         <div className="domain_new_select">
      //             <label htmlFor="data_hub_location">데이터 센터</label>
      //             <select id="data_hub_location">
      //             <option value="linux">Default(VS)</option>
      //             </select>
      //         </div>
      //         <div className="domain_new_select">
      //             <label htmlFor="domain_feature_set">도메인 기능</label>
      //             <select id="domain_feature_set">
      //                 <option value="data">데이터</option>
      //                 <option value="iso">ISO</option>
      //                 <option value="export">내보내기</option>
      //             </select>
      //         </div>
      //         <div className="domain_new_select">
      //             <label htmlFor="storage_option_type">스토리지 유형</label>
      //             <select 
      //             id="storage_option_type"
      //             value={storageType}
      //             onChange={handleStorageTypeChange} // 선택된 옵션에 따라 상태 변경
      //             >
      //             <option value="NFS">NFS</option>
          
      //             <option value="iSCSI">iSCSI</option>
      //             <option value="fc">파이버 채널</option>
      //             </select>
      //         </div>
      //         <div className="domain_new_select" style={{ marginBottom: 0 }}>
      //             <label htmlFor="host_identifier">호스트</label>
      //             <select id="host_identifier">
      //             <option value="linux">host02.ititinfo.com</option>
      //             </select>
      //         </div>
      //         </div>
      //         <div className="domain_new_right">
      //         <div className="domain_new_select">
      //             <label>이름</label>
      //             <input type="text" />
      //         </div>
      //         <div className="domain_new_select">
      //             <label>설명</label>
      //             <input type="text" />
      //         </div>
      //         <div className="domain_new_select">
      //             <label>코멘트</label>
      //             <input type="text" />
      //         </div>
      //         </div>
      //     </div>

      //     {storageType === 'NFS' && (
      //     <div className="storage_popup_NFS">
      //         <div className ="network_form_group">
      //         <label htmlFor="data_hub">NFS 서버 경로</label>
      //         <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
      //         </div>

      //         <div>
      //         <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
      //         <span>사용자 정의 연결 매개 변수</span>
      //         <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
      //             <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
      //             <div className="domain_new_select">
      //             <label htmlFor="nfs_version">NFS 버전</label>
      //             <select id="nfs_version">
      //                 <option value="host02_ititinfo_com">host02.ititinfo.com</option>
      //             </select>
      //             </div>
      //             <div className="domain_new_select">
      //             <label htmlFor="data_hub">재전송</label>
      //             <input type="text" />
      //             </div>
      //             <div className="domain_new_select">
      //             <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
      //             <input type="text" />
      //             </div>
      //             <div className="domain_new_select">
      //             <label htmlFor="data_hub">추가 마운트 옵션</label>
      //             <input type="text" />
      //             </div>
      //         </div>
      //         </div>
      //         <div>
      //         <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
      //         <span>고급 매개 변수</span>
      //         <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
      //             <div className="domain_new_select">
      //             <label>디스크 공간 부족 경고 표시(%)</label>
      //             <input type="text" />
      //             </div>
      //             <div className="domain_new_select">
      //             <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
      //             <input type="text" />
      //             </div>
      //             <div className="domain_new_select">
      //             <label>디스크 공간 부족 경고 표시(%)</label>
      //             <input type="text" />
      //             </div>
      //             <div className="domain_new_select">
      //             <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
      //             <select id="format_type_selector" disabled>
      //                 <option value="linux">V5</option>
      //             </select>
      //             </div>
      //             <div className="hidden_checkbox">
      //             <input type="checkbox" id="reset_after_deletion"/>
      //             <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
      //             </div>
      //             <div className="hidden_checkbox">
      //             <input type="checkbox" id="backup_vault"/>
      //             <label htmlFor="backup_vault">백업</label>
      //             </div>

      //         </div>
      //         </div>
      //     </div>
      //     )}

          

      //     {storageType === 'iSCSI' && (
      //     <div className="storage_popup_NFS">
      //         <div className='target_btns flex'> 
      //         <button 
      //             className={`target_lun`}
      //             // className={`target_lun ${activeLunTab === 'target_lun' ? 'active' : ''}`}
      //             // onClick={() => handleLunTabClick('target_lun')}
      //         >
      //             대상 - LUN
      //         </button>
      //         <button 
      //             // className={`lun_target ${activeLunTab === 'lun_target' ? 'active' : ''}`}
      //             // onClick={() => handleLunTabClick('lun_target')}
      //             className={`lun_target`}
                  
      //         > 
      //             LUN - 대상
      //         </button>
      //         </div>


          
              {/* { activeLunTab === 'target_lun' &&(
              <div className='target_lun_outer'> 
                  <div className="search_target_outer">
                  <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn4" onClick={toggleDomainHiddenBox4}fixedWidth/>
                  <span>대상 검색</span>
                  <div id="domain_hidden_box4" style={{ display: isDomainHiddenBox4Visible ? 'block' : 'none' }}>
                      <div className="search_target ">

                      <div>
                          <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                          </div>
                          <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                          </div>
                      </div>

                      <div>
                          <div className='input_checkbox'>
                          <input type="checkbox" id="reset_after_deletion"/>
                          <label htmlFor="reset_after_deletion">사용자 인증 :</label>
                          </div>
                          <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                          </div>
                          <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                          </div>
                      </div>

                      
                      </div>
                      <button>검색</button>
                  </div>
                  </div>
              

                  <div>
                  <button className='all_login'>전체 로그인</button>
                  <div className='section_table_outer'>
                      <Table
                      columns={TableColumnsInfo.CLUSTERS_ALT} 
                      data={data} 
                      onRowClick={handleRowClick}
                      shouldHighlight1stCol={true}
                      />
                  </div>
                  </div>
              </div>
              )}      */}

              {/* { activeLunTab === 'lun_target' && (
              <div className='lun_target_outer'>
                  <div className='section_table_outer'>
                      <Table
                      columns={TableColumnsInfo.CLUSTERS_ALT} 
                      data={data} 
                      onRowClick={handleRowClick}
                      shouldHighlight1stCol={true}
                      />
                  </div>
              </div>
              )} */}
              {/* <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
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
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                  </select>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">삭제 후 폐기</label>
                  </div>
              </div>
              </div>                
          */}
          // </div>
          // )} 
{/* 
          {storageType === 'fc' && (
          <div className="storage_popup_NFS">
              <div className='section_table_outer'>
                  {/* <Table
                  columns={TableColumnsInfo.CLUSTERS_ALT} 
                  data={data} 
                  onRowClick={handleRowClick}
                  shouldHighlight1stCol={true}
                  /> */}
              {/* </div>
              <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
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
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                  </select>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">삭제 후 폐기</label>
                  </div>
              </div>
              </div>
          </div>
          )}


          <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button onClick={closePopup}>취소</button>
          </div>
          </div>
      </Modal> */}
        {/*도메인(도메인 관리)팝업 */}
      {/* <Modal
          isOpen={activePopup === 'manageDomain'}
          onRequestClose={closePopup}
          contentLabel="도메인 관리"
          className="Modal"
          overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
      >
          <div className="storage_domain_administer_popup">
          <div className="popup_header">
              <h1>도메인 관리</h1>
              <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
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
                      <option value="data">데이터</option>
                      <option value="iso">ISO</option>
                      <option value="export">내보내기</option>
                  </select>
              </div>
              <div className="domain_new_select">
                  <label htmlFor="storage_option_type">스토리지 유형</label>
                  <select id="storage_option_type">
                  <option value="linux">NFS</option>
                  <option value="linux">POSIX 호환 FS</option>
                  <option value="linux">GlusterFS</option>
                  <option value="linux">iSCSI</option>
                  <option value="linux">파이버 채널</option>
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

          <div className="storage_popup_NFS">
              <div>
              <label htmlFor="data_hub">NFS 서버 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
              </div>

              <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn" onClick={toggleDomainHiddenBox}fixedWidth/>
              <span>사용자 정의 연결 매개 변수</span>
              <div id="domain_hidden_box" style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }}>
                  <span>아래 필드에서 기본값을 변경하지 않을 것을 권장합니다.</span>
                  <div className="domain_new_select">
                  <label htmlFor="nfs_version">NFS 버전</label>
                  <select id="nfs_version" disabled style={{cursor:'no-drop'}}>
                      <option value="host02_ititinfo_com" >자동 협상(기본)</option>
                  </select>
                  </div> */}
                  {/* <div className="domain_new_select">
                  <label htmlFor="data_hub">재전송(#)</label>
                  <input type="text" />
                  </div>
                  <div className="domain_new_select">
                  <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                  <input type="text" />
                  </div>
                  <div className="domain_new_select">
                  <label htmlFor="data_hub">추가 마운트 옵션</label>
                  <input type="text" />
                  </div> */}
              {/* </div>
              </div>
              <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn2" onClick={toggleDomainHiddenBox2}fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box2" style={{ display: isDomainHiddenBox2Visible ? 'block' : 'none' }}>
                  <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                  </div>
                  <div className="domain_new_select">
                  <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                  <input type="text" />
                  </div> */}
                  {/* <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                  </div>
                  <div className="domain_new_select">
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                      <option value="linux">V5</option>
                  </select>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                  </div>
                  <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                  </div> */}
              {/* </div>
              </div>
          </div>

          <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button onClick={closePopup}>취소</button>
          </div>
          </div>
      </Modal> */}