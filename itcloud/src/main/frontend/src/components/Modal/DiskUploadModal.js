import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { 
  
} from '../../api/RQHook';

const DiskUploadModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  diskId,
}) => {
  const [id, setId] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [domainType, setDomainType] = useState('');
  const [storageType, setStorageType] = useState('');
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [warning, setWarning] = useState('');
  const [hostVoName, setHostVoName] = useState('');
  const [spaceBlocker, setSpaceBlocker] = useState('');

  // const { mutate: addDomain } = useAddDomain();
  const [activePopup, setActivePopup] = useState(null);
  const [activeTab, setActiveTab] = useState('img');
  const openPopup = (popupType) => {
    setActivePopup(popupType);
  };
  const closePopup = () => {
    setActivePopup(null);
  };
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const [isVisible, setIsVisible] = useState(false);
  const toggleContent = () => {
    setIsVisible(!isVisible);
  };

  const [activeDiskType, setActiveDiskType] = useState('all');
const handleDiskTypeClick = (type) => {
  setActiveDiskType(type);  // 여기서 type을 설정해야 함
};
const [activeContentType, setActiveContentType] = useState('all'); // 컨텐츠 유형 상태
  // 컨텐츠 유형 변경 핸들러
  const handleContentTypeChange = (event) => {
    setActiveContentType(event.target.value);
  };  
  return (
    <Modal
      isOpen={activePopup === 'uploadDisk'}
      onRequestClose={closePopup}
      contentLabel="디스크 업로드"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
    <div className="storage_disk_upload_popup">
      <div className="popup_header">
        <h1>이미지 업로드</h1>
        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
      </div>
      <div className="storage_upload_first">
        <button>파일 선택</button>
        <div>선택된 파일 없음</div>
      </div>
      <div className="storage_upload_second">
        <div className="disk_option">디스크 옵션</div>
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
              <input type="checkbox" id="incremental_backup" defaultChecked />
              <label htmlFor="incremental_backup">중복 백업 사용</label>
            </div>
            <div>
              <button>연결 테스트</button>
            </div>
          </div>
        </div>
      </div>
      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={closePopup}>취소</button>
      </div>
    </div>
    </Modal>
  );
};

export default DiskUploadModal;
