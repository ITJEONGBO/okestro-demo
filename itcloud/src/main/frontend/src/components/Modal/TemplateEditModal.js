import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useTemplate } from '../../api/RQHook';

const TemplateEditModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode, 
  templateId 
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [osSystem, setOsSystem] = useState(''); // 운영체제 string
  const [chipsetFirmwareType, setChipsetFirmwareType] = useState('');// string
  // 최적화옵션
  const [optimizeOption, setOptimizeOption] = useState([
    { value: 'DESKTOP', label: '데스크톱' },
    { value: 'HIGH_PERFORMANCE', label: '고성능' },
    { value: 'SERVER', label: '서버' }
  ]);

  
  const [selectedModalTab, setSelectedModalTab] = useState('general');

  //해당데이터 상세정보 가져오기
  const { data: templateData } = useTemplate(templateId);



  // 초기값설정
  useEffect(() => {
    if (isOpen) {
      if (editMode && templateData) {
        setId(templateData?.id);
        setName(templateData?.name);
        setDescription(templateData?.description);
        setComment(templateData?.comment || '');
        setOsSystem(templateData?.osSystem || '');
        setOptimizeOption(templateData?.optimizeOption || '');
        setChipsetFirmwareType(templateData?.chipsetFirmwareType || '');
      }
    }
  }, [isOpen, editMode, templateData]);

  const handleFormSubmit = () => {
    if (editMode) {
      if (name === '') {
        alert("이름을 입력해주세요.");
        return;
      }
      const dataToSubmit = {
        id,
        name,
        description,
        comment,
      };
  
      console.log('Data:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인
    }
  };


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
                    <input
                        type="text"
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)} 
                      />
                  </div>
                  <div className="host_textbox">
                    <label htmlFor="comment">코멘트</label>
                    <input
                        type="text"
                        id="comment"
                        value={comment}
                        onChange={(e) => setComment(e.target.value)} 
                      />
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
            onClick={() => {
              handleFormSubmit();
            }}
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
