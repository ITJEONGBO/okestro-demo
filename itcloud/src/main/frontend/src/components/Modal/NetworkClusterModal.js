import React from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TablesOuter from '../table/TablesOuter';
import TableInfo from '../table/TableInfo';
import { faPlay, faTimes } from "@fortawesome/free-solid-svg-icons";

const NetworkClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  clusters
}) => {
  const renderStatusIcon = (status) => {
    if (status === 'OPERATIONAL') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="네트워크 관리"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="manage_network_popup">
        <div className="popup_header">
          <h1>네트워크 관리</h1>
          <button onClick={onRequestClose}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
        </div>
        
        <TablesOuter 
          columns={TableInfo.CLUSTERS_POPUP} 
          data={clusters}
          onRowClick={() => console.log('Row clicked')} 
        />
        
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};
export default NetworkClusterModal;    