import React, { useState } from 'react';
import Modal from 'react-modal';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import TableOuter from "../table/TableOuter";
import TableColumnsInfo from '../table/TableColumnsInfo';

const VmConnectionPlusModal = ({ isOpen, onRequestClose }) => {
  const [activeTab, setActiveTab] = useState('img');

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="modalOverlay"
      shouldCloseOnOverlayClick={false}
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
            columns={TableColumnsInfo.VMS_FROM_HOST}
            data={[]}
            onRowClick={() => console.log('Row clicked')}
          />
        )}
        {activeTab === 'directlun' && (
          <TableOuter
            columns={TableColumnsInfo.VMS_STOP}
            data={[]}
            onRowClick={() => console.log('Row clicked')}
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
