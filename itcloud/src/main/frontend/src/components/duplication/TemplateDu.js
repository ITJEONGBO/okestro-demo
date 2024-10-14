import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faSearch, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../table/TableColumnsInfo';
import TableOuter from '../table/TableOuter';
import './css/TemplateDu.css';
import { useAllTemplates } from '../../api/RQHook';

const TemplateDu = ({ columns, handleRowClick }) => {
  const [activePopup, setActivePopup] = useState(null);
  const [selectedModalTab, setSelectedModalTab] = useState('ipv4');

  const openPopup = (popupType) => {
    setActivePopup(popupType);
    if (popupType === 'edit') {
      setSelectedModalTab('general'); // 모달이 열릴 때 첫 번째 탭으로 설정
    }
  };

  const closePopup = () => {
    setActivePopup(null);
    setSelectedModalTab('general'); // 모달이 닫힐 때 첫 번째 탭으로 초기화
  };
  const { 
    data: templates, 
    status: templatesStatus,
    isRefetching: isTemplatesRefetching,
    refetch: refetchTemplates, 
    isError: isTemplatesError, 
    error: templatesError, 
    isLoading: isTemplatesLoading,
  } = useAllTemplates(toTableItemPredicateTemplates);
  
  function toTableItemPredicateTemplates(template) {
    return {
      id: template?.id ?? '',
      name: template?.name ?? 'Unknown', 
      status: template?.status ?? 'Unknown',                // 템플릿 상태
      versionName: template?.versionName ?? 'N/A',           // 템플릿 버전 정보
      description: template?.description ?? 'No description',// 템플릿 설명
      cluster: template?.clusterVo?.name ?? 'Unknown', 
      datacenter: template?.dataCenterVo?.name ?? 'Unknown', 
      creationTime: template?.creationTime ?? '',
      comment: template?.comment ?? ''
    };
  }
    return (
      <>
        <div className="content_header_right">
          <div className="search_box">
            <input type="text" />
            <button><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
          </div>
  
          <div className="header_right_btns">
          <button onClick={() => openPopup('bring')}>가져오기</button>
            <button onClick={() => openPopup('edit')}>편집</button>
            <button onClick={() => openPopup('delete')}>삭제</button>
            <button className="disabled">내보내기</button>
            <button className="disabled">새 가상머신</button>
          </div>        
        </div>
          
        <TableOuter
          columns={columns} 
          data={templates} 
          onRowClick={handleRowClick} 
          className="template_chart"
          clickableColumnIndex={[0]} 
        />
      {/*편집 모달 */}
      <Modal
        isOpen={activePopup === 'edit'}
        onRequestClose={closePopup} // 모달 닫기 핸들러 연결
        contentLabel="추가"
        className="Modal"
        overlayClassName="Overlay newRolePopupOverlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="template_eidt_popup">
          <div className="popup_header">
            <h1>템플릿 수정</h1>
            <button onClick={closePopup}>
              <FontAwesomeIcon icon={faTimes} fixedWidth />
            </button>
          </div>

          <div className='flex'>
            <div className="network_backup_edit_nav">
            <div
                id="general_tab"
                className={selectedModalTab === 'general' ? 'active-tab' : 'inactive-tab'}
                onClick={() => setSelectedModalTab('general')}
              >
                일반
              </div>
              <div
                id="console_tab"
                className={selectedModalTab === 'console' ? 'active-tab' : 'inactive-tab'}
                onClick={() => setSelectedModalTab('console')}
              >
                콘솔
              </div>

             
            </div>

            {/* 탭 내용 */}
            <div className="backup_edit_content">
              {selectedModalTab === 'general' && 
              <>
              <div className="vnic_new_box" style={{borderBottom:'1px solid gray', paddingBottom:'0.3rem'}}>
                <label htmlFor="ip_address">최적화 옵션</label>
                <select id="ip_address">
                  <option value="서버">서버</option>
                </select>
              </div>
              
              <div className='template_edit_texts'>
                <div className='host_textbox'>
                  <label htmlFor="user_name">이름</label>
                  <input type="text" id="user_name" value={'#'} />
                </div>
                <div className='host_textbox'>
                  <label htmlFor="description">설명</label>
                  <input type="text" id="description" />
                </div>
                <div className='host_textbox'>
                  <label htmlFor="comment">코멘트</label>
                  <input type="text" id="comment" />
                </div>
              </div>

              <div className='flex'>
                <div className="vnic_new_checkbox">
                  <input type="checkbox" id="stateless" />
                  <label htmlFor="stateless">상태 비저장</label>
                </div>
                <div className="vnic_new_checkbox">
                  <input type="checkbox" id="start_in_pause_mode" />
                  <label htmlFor="start_in_pause_mode">일시정지 모드에서 시작</label>
                </div>
                <div className="vnic_new_checkbox">
                  <input type="checkbox" id="prevent_deletion" />
                  <label htmlFor="prevent_deletion">삭제 방지</label>
                </div>
              </div>
                
                </>
              }
              {selectedModalTab === 'console' && 
              <>
                <div className="vnic_new_box" style={{borderBottom:'1px solid gray', paddingBottom:'0.3rem'}}>
                  <label htmlFor="ip_address">최적화 옵션</label>
                  <select id="ip_address">
                    <option value="서버">서버</option>
                  </select>
                </div>
                <div className='p-1.5'>
                  <div className='font-bold'>그래픽 콘솔</div>
                  <div className='monitor'>
                    <label htmlFor="monitor_select">모니터</label>
                    <select id="monitor_select">
                      <option value="1">1</option>
                    </select>
                  </div>
                </div>
              </>
              }
           
              
            </div>
          </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
      </Modal>
        {/*가져오기 팝업 */}
        <Modal
        isOpen={activePopup === 'bring'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="vm_bring_popup">
          <div className="popup_header">
            <h1>가상머신 가져오기</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className="border-b border-gray-400">
            <div className="vm_select_box">
                <label htmlFor="host_action">데이터 센터</label>
                <select id="host_action">
                    <option value="none">Default</option>
                </select>
            </div>
            <div className="vm_select_box">
                <label htmlFor="host_action">소스</label>
                <select id="host_action">
                    <option value="none">가상 어플라이언스(OVA)</option>
                </select>
            </div>
          </div>

          <div>
            <div className="vm_select_box">
                <label htmlFor="host_action">호스트</label>
                <select id="host_action">
                    <option value="none">Default</option>
                </select>
            </div>
            <div className="vm_select_box">
                <label htmlFor="host_action">파일 경로</label>
                <input type='text'/>
            </div>
          </div>
          <div className='px-1.5'>
            <div className='load_btn'>로드</div>
          </div>

        <div className='vm_bring_table'>

            <div>
                <div className='font-bold'>소스 상의 가상 머신</div>
                <TableOuter 
                    columns={TableColumnsInfo.VM_BRING_POPUP}
                    data={[]}
                    onRowClick={() => console.log('Row clicked')}
                />
            </div>
            <div>
                <div className='font-bold'>가져오기할 가상 머신</div>
                <TableOuter 
                    columns={TableColumnsInfo.VM_BRING_POPUP}
                    data={[]}
                    onRowClick={() => console.log('Row clicked')}
                />
            </div>

        </div>

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
        </Modal>
        {/*삭제 팝업 */}
        <Modal
    isOpen={activePopup === 'delete'}
    onRequestClose={closePopup}
    contentLabel="디스크 업로드"
    className="Modal"
    overlayClassName="Overlay"
    shouldCloseOnOverlayClick={false}
  >
    <div className="storage_delete_popup">
      <div className="popup_header">
        <h1>디스크 삭제</h1>
        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
      </div>
      
      <div className='disk_delete_box'>
        <div>
          <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
          <span>다음 항목을 삭제하시겠습니까?</span>
        </div>
      </div>


      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={closePopup}>취소</button>
      </div>
    </div>
        </Modal>
      </>

    );
  };

export default TemplateDu;
