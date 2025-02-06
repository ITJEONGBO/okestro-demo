import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { toast } from 'react-hot-toast';
import '../css/MDatacenter.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import {
  useAddDataCenter, 
  useEditDataCenter,
  useDataCenter
} from '../../../../api/RQHook'
import { CheckKorenName, CheckName } from '../../../../utils/CheckName';
import LabelInput from '../../../../utils/LabelInput';

const DataCenterModal = ({ isOpen, editMode = false, dcId, onClose }) => {
  const { mutate: addDataCenter } = useAddDataCenter();
  const { mutate: editDataCenter } = useEditDataCenter();

  const [formState, setFormState] = useState({
    id: '',
    name: '',
    comment: '',
    description: '',
    storageType: false,
    version: '4.7',
    quotaMode: 'DISABLED'
  });

  const resetForm = () => {
    setFormState({
      name: '',
      comment: '',
      description: '',
      storageType: false,
      version: '4.7',
      quotaMode: 'DISABLED'
    });
  };

  const { data: datacenter } = useDataCenter(dcId);

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
    }
  }, [isOpen]);  
  
  // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && datacenter) {
      setFormState({
        id: datacenter.id,
        name: datacenter.name,
        comment: datacenter.comment,
        description: datacenter.description,
        storageType: datacenter.storageType,
        version: datacenter.version,
        quotaMode: datacenter.quotaMode,
      });
    }
  }, [editMode, datacenter]);


  const validateForm = () => {
    if (!CheckKorenName(formState.name) || !CheckName(formState.name)) {
      toast.error('이름이 유효하지 않습니다.');
      return false;
    }
    if (!CheckKorenName(formState.description)) {
      toast.error('설명이 유효하지 않습니다.');
      return false;
    }
    return true;
  };

  const handleFormSubmit = () => {
    if (!validateForm()) return;

    // 데이터 객체 생성
    const dataToSubmit = {
      ...formState
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.id = formState.id;  // 수정 모드에서는 id를 추가
      editDataCenter({
        dataCenterId: formState.id,              // 전달된 id
        dataCenterData: dataToSubmit   // 수정할 데이터
      }, {
        onSuccess: () => {
          onClose();  // 성공 시 모달 닫기
          toast.success("데이터센터 편집 완료")
        },
        onError: (error) => {
          toast.error('Error editing data center:', error);
        }
      });
    } else {
      addDataCenter(dataToSubmit, {
        onSuccess: () => {
          onClose();
          toast.success("데이터센터 생성 완료")
        },
        onError: (error) => {
          toast.error('Error adding data center:', error);
        }
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? '데이터 센터 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="datacenter-new-popup modal">
        <div className="popup-header">
          <h1>{editMode ? '데이터 센터 편집' : '새 데이터 센터'}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="datacenter-new-content modal-content">

          <LabelInput
            label="이름"
            id="name"
            value={formState.name}
            autoFocus={true}
            onChange={(e) => setFormState((prev) => ({ ...prev, name: e.target.value }))}
          />
          <LabelInput
            label="설명"
            id="description"
            value={formState.description}
            onChange={(e) => setFormState((prev) => ({ ...prev, description: e.target.value }))}
          />
          <LabelInput
            label="코멘트"
            id="comment"
            value={formState.comment}
            onChange={(e) => setFormState((prev) => ({ ...prev, comment: e.target.value }))}
          />

          <div>
            <label htmlFor="storageType">스토리지 타입</label>
            <select
              id="storageType"
              value={formState.storageType ? "true" : "false"}  // 불리언 값을 문자열로 변환하여 value와 일치하도록 설정
              onChange={(e) => setFormState((prev) => ({ ...prev, storageType: e.target.value === "true" }))}
            >
              <option value="false">공유됨</option>
              <option value="true">로컬</option>
            </select>
          </div>

          <div>
            <label htmlFor="version">호환버전</label>
            <select
              id="version"
              value={formState.version}
              onChange={(e) => setFormState((prev) => ({ ...prev, version: e.target.value }))}
            >
              <option value="4.7">4.7</option>
            </select>
          </div>
          <div>
          <label htmlFor="quota_mode">쿼터 모드</label>
            <select
              id="quota_mode"
              value={formState.quotaMode}
              onChange={(e) => setFormState((prev) => ({ ...prev, quotaMode: e.target.value }))}
            >
              <option value="DISABLED">비활성화됨</option>
              <option value="AUDIT">감사</option>
              <option value="ENABLED">활성화됨</option>
            </select>
          </div>
        </div>

        <div className="edit-footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DataCenterModal;
