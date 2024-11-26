import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useTemplate } from '../../api/RQHook';

const TemplateEditModal = ({ isOpen, onRequestClose, editMode, templateId }) => {
  const [selectedModalTab, setSelectedModalTab] = useState('general');

  const { data: templateData, isLoading, isError } = useTemplate(templateId);

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '템플릿 수정' : '템플릿 생성'}
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="template_eidt_popup">
        <div className="popup_header">
          <h1>{editMode ? '템플릿 수정' : '템플릿 생성'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="flex">
          {/* 왼쪽 네비게이션 */}
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

          {/* 오른쪽 콘텐츠 */}
          <div className="backup_edit_content">
            {selectedModalTab === 'general' && (
              <>
                <div className="vnic_new_box" style={{ borderBottom: '1px solid gray', paddingBottom: '0.3rem' }}>
                  <label htmlFor="optimization">최적화 옵션</label>
                  <select id="optimization">
                    <option value="서버">서버</option>
                  </select>
                </div>

                <div className="template_edit_texts">
                  <div className="host_textbox">
                    <label htmlFor="template_name">이름</label>
                    <input
                      type="text"
                      id="template_name"
                      defaultValue={editMode ? `${templateId}` : ''}
                    />
                  </div>
                  <div className="host_textbox">
                    <label htmlFor="description">설명</label>
                    <input type="text" id="description" />
                  </div>
                  <div className="host_textbox">
                    <label htmlFor="comment">코멘트</label>
                    <input type="text" id="comment" />
                  </div>
                </div>

                <div className="flex">
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
            )}
            {selectedModalTab === 'console' && (
              <>
                <div className="vnic_new_box" style={{ borderBottom: '1px solid gray', paddingBottom: '0.3rem' }}>
                  <label htmlFor="console_optimization">최적화 옵션</label>
                  <select id="console_optimization">
                    <option value="서버">서버</option>
                  </select>
                </div>
                <div className="p-1.5">
                  <div className="font-bold">그래픽 콘솔</div>
                  <div className="monitor">
                    <label htmlFor="monitor_select">모니터</label>
                    <select id="monitor_select">
                      <option value="1">1</option>
                    </select>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>

        <div className="edit_footer">
          <button
            onClick={() =>
              alert(
                editMode
                  ? `템플릿 ID: ${templateId}가 수정되었습니다.`
                  : '새 템플릿이 생성되었습니다.'
              )
            }
          >
            OK
          </button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default TemplateEditModal;
