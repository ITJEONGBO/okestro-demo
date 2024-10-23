import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import {
  useAddDataCenter, 
  useEditDataCenter
} from '../../api/RQHook'

const DataCenterModal = ({ 
  isOpen,
  onRequestClose,
  editMode = false,  // 기본이 생성모드
  data = {}          // 부모 컴포넌트에서 전달된 데이터
}) => {
  console.log("DataCenterModal ")
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [storageType, setStorageType] = useState(false);
  const [version, setVersion] = useState('4.7');
  const [quotaMode, setQuotaMode] = useState('DISABLED');

  const { mutate: addDataCenter } = useAddDataCenter();
  const { mutate: editDataCenter } = useEditDataCenter();

  // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && data) {
      setId(data.id);
      setName(data.name);
      setComment(data.comment);
      setDescription(data.description);
      setStorageType(data.storageType === true);  // true/false에 따른 저장 방식
      setVersion(data.version);
      setQuotaMode(data.quotaMode);
    } else {
      resetForm();
    }
  }, [editMode, data]);

  const resetForm = () => {
    setName('');
    setComment('');
    setDescription('');
    setStorageType(false);
    setVersion('4.7');
    setQuotaMode('DISABLED');
  };

  const handleFormSubmit = () => {
    // 데이터 객체 생성
    const dataToSubmit = {
      name,
      comment,
      description,
      storageType,
      version,
      quotaMode,
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
      editDataCenter({
        dataCenterId: id,              // 전달된 id
        dataCenterData: dataToSubmit   // 수정할 데이터
      }, {
        onSuccess: () => {
          alert("데이터센터 편집 완료(alert기능구현)")
          onRequestClose();  // 성공 시 모달 닫기
        },
        onError: (error) => {
          console.error('Error editing data center:', error);
        }
      });
    } else {
      addDataCenter(dataToSubmit, {
        onSuccess: () => {
          alert("데이터센터 생성 완료(alert기능구현)")
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding data center:', error);
        }
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '데이터 센터 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="datacenter_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '데이터 센터 편집' : '새 데이터 센터'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="datacenter_new_content">
          <div>
            <input type="hidden" id="id" value={id} onChange={() => {}} /> {/* id는 읽기 전용이므로 onChange를 추가하지 않음 */}
          </div>
          <div>
            <label htmlFor="name1">이름</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)} // onChange 핸들러 추가
            />
          </div>
          <div>
            <label htmlFor="comment">코멘트</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)} // onChange 핸들러 추가
            />
          </div>
          <div>
            <label htmlFor="description">설명</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)} // onChange 핸들러 추가
            />
          </div>
          <div>
            <label htmlFor="storageType">스토리지 타입</label>
            <select
              id="storageType"
              value={storageType}
              onChange={(e) => setStorageType(e.target.value === "true")} // onChange 핸들러 수정
            >
              <option value="false">공유됨</option>
              <option value="true">로컬</option>
            </select>
          </div>
          <div>
            <label htmlFor="version">호환버전</label>
            <select
              id="version"
              value={version}
              onChange={(e) => setVersion(e.target.value)} // onChange 핸들러 추가
            >
              <option value="4.7">4.7</option>
            </select>
          </div>
          <div>
            <label htmlFor="quota_mode">쿼터 모드</label>
            <select
              id="quota_mode"
              value={quotaMode}
              onChange={(e) => setQuotaMode(e.target.value)} // onChange 핸들러 추가
            >
              <option value="DISABLED">비활성화됨</option>
              <option value="AUDIT">감사</option>
              <option value="ENABLED">활성화됨</option>
            </select>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DataCenterModal;
