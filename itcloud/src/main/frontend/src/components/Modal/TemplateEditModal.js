import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useEditTemplate, useTemplate } from '../../api/RQHook';

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
  const [stateless, setStateless] = useState(false); // 상태비저장
  const [startPaused, setStartPaused] = useState(false); // 일시정지상태에서시작
  const [deleteProtected, setDeleteProtected] = useState(false); // 일시정지상태에서시작
  
  const { mutate: editTemplate } = useEditTemplate();

// // 칩셋 옵션(ui에서안씀)
// const [chipsetOptions, setChipsetOptions] = useState([
//   { value: 'CLUSTER_DEFAULT', label: '클러스터 기본값' },
//   { value: 'I440FX_SEA_BIOS', label: 'BIOS의 I440FX 칩셋' },
//   { value: 'Q35_OVMF', label: 'UEFI의 Q35 칩셋' },
//   { value: 'Q35_SEA_BIOS', label: 'BIOS의 Q35 칩셋' },
//   { value: 'Q35_SECURE_BOOT', label: 'UEFI SecureBoot의 Q35 칩' },
// ]); 셋
  // 최적화옵션(영어로 값바꿔야됨)
  const [optimizeOption, setOptimizeOption] = useState([
    { value: 'desktop', label: '데스크톱' },
    { value: 'HIGH_PERFORMANCE', label: '고성능' },
    { value: 'server', label: '서버' }
  ]);

  
  const [selectedModalTab, setSelectedModalTab] = useState('general');

  //해당데이터 상세정보 가져오기
  const { data: templateData } = useTemplate(templateId);
  const [selectedOptimizeOption, setSelectedOptimizeOption] = useState('SERVER'); // 칩셋 선택
  const [selectedChipset, setSelectedChipset] = useState('Q35_OVMF'); // 칩셋 선택

  // 초기값설정
  useEffect(() => {
    if (isOpen) {
      if (editMode && templateData) {
        setId(templateData?.id);
        setName(templateData?.name || 'd');
        setDescription(templateData?.description);
        setComment(templateData?.comment || '');
        setOsSystem(templateData?.osSystem || '');
        setStateless(templateData?.stateless);
        setStartPaused(templateData?.startPaused);
        setDeleteProtected(templateData?.deleteProtected);
        setSelectedOptimizeOption(templateData?.optimizeOption || 'SERVER'); // 최적화 옵션
        setSelectedChipset(templateData?.chipsetFirmwareType || 'Q35_OVMF');
      }
    }
  }, [isOpen, editMode, templateData,templateId]);

  const handleFormSubmit = () => {
    if (!templateId) {
      console.error('템플릿 ID가 없습니다. 수정 요청을 취소합니다.');
      return;
    }
    if (name === '') {
      alert("이름을 입력해주세요.");
      return;
    }
      const dataToSubmit = {
        id,
        name,
        description,
        comment,
        chipsetFirmwareType:selectedChipset,
        optimizeOption:selectedOptimizeOption,
        osSystem
      };
      console.log('템플릿 Data:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인
      if (editMode) {
        dataToSubmit.id = id;
        editTemplate(
          {
            templateId: id,
            templateData: dataToSubmit,
          },
          {
            onSuccess: () => {
              alert("템플릿 편집 완료");
              onRequestClose();
            },
            onError: (error) => {
              console.error('Error editing cluster:', error);
            },
          }
        );
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
            <div className="vnic_new_box" style={{ borderBottom: '1px solid gray', paddingBottom: '0.3rem' }}>
                <label htmlFor="optimization">최적화 옵션</label>
                  <select
                    id="optimization"
                    value={selectedOptimizeOption} // 선택된 값과 동기화
                    onChange={(e) => setSelectedOptimizeOption(e.target.value)} // 값 변경 핸들러
                  >
                    {optimizeOption.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label} {/* UI에 표시되는 값 */}
                      </option>
                    ))}
                  </select>
                  <span>선택된 최적화 옵션: {optimizeOption.find(opt => opt.value === selectedOptimizeOption)?.label || ''}</span>
            </div>
            {selectedModalTab === 'general' && (
              <>
                <div className="template_edit_texts">
                  <div className="host_textbox">
                    <label htmlFor="template_name">이름</label>
                    <input
                      type="text"
                      id="template_name"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
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
                    <input
                      type="checkbox"
                      id="stateless"
                      checked={stateless} // 상태에 따라 체크 상태 설정
                      onChange={(e) => setStateless(e.target.checked)} // 값 변경 핸들러
                    />
                    <label htmlFor="stateless">상태 비저장</label>
                  </div>
                  <div className="vnic_new_checkbox">
                    <input
                      type="checkbox"
                      id="start_in_pause_mode"
                      checked={startPaused} // 상태에 따라 체크 상태 설정
                      onChange={(e) => setStartPaused(e.target.checked)} // 값 변경 핸들러
                    />
                    <label htmlFor="start_in_pause_mode">일시정지 모드에서 시작</label>
                  </div>
                  <div className="vnic_new_checkbox">
                    <input
                      type="checkbox"
                      id="prevent_deletion"
                      checked={deleteProtected} // 상태에 따라 체크 상태 설정
                      onChange={(e) => setDeleteProtected(e.target.checked)} // 값 변경 핸들러
                    />
                    <label htmlFor="prevent_deletion">삭제 방지</label>
                  </div>
                </div>
              </>
            )}
            {selectedModalTab === 'console' && (
              <>
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
          <button onClick={() => {handleFormSubmit();}}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default TemplateEditModal;
