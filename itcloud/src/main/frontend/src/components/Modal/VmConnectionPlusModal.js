import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import TableOuter from "../table/TableOuter";
import TableColumnsInfo from '../table/TableColumnsInfo';
import TableInfo from '../table/TableInfo';
import { useFindDiskListFromVM } from '../../api/RQHook';

const VmConnectionPlusModal = ({ isOpen, onRequestClose }) => {
  const [activeTab, setActiveTab] = useState('img'); // 현재 선택된 탭 상태 관리

  const handleTabClick = (tab) => {
    setActiveTab(tab); // 탭 클릭 시 상태 업데이트
  };

  const {
    data: disks,
  } = useFindDiskListFromVM((e) => ({
    ...e,
    storageDomainVo: e?.storageDomainVo?.name,
    status: e?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP'
  }));


  
  return (
    <Modal
      isOpen={isOpen} // 부모에서 모달 열림 상태 전달
      onRequestClose={onRequestClose} // 부모에서 전달받은 닫기 함수 호출
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="modalOverlay"
      shouldCloseOnOverlayClick={true} // 배경 클릭 시 모달 닫기
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>가상 디스크 연결</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick('img')}
            className={activeTab === 'img' ? 'active' : ''}
          >
            이미지
          </div>
          <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접 LUN
          </div>
        </div>
        {activeTab === 'img' && (
          <TableOuter
            columns={TableInfo.VIRTUAL_DISK}
            data={disks || []}
            onRowClick={() => console.log('Row clicked in 이미지 탭')}
          />
        )}
        {activeTab === 'directlun' && (
          <TableOuter
            columns={TableColumnsInfo.VMS_STOP}
            data={[]} // 데이터를 여기에 추가하세요.
            onRowClick={() => console.log('Row clicked in 직접 LUN 탭')}
          />
        )}
        <div className="edit_footer">
          <button>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
      
    </Modal>
  );
};

export default VmConnectionPlusModal;
