import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useAllHosts } from '../../api/RQHook';

const VmExportOVAModal = ({ isOpen, onRequestClose, selectedVm }) => {
  const [host, setHost] = useState('#');
  const [directory, setDirectory] = useState('#');
  const [name, setName] = useState(selectedVm?.name || '#');

  const handleExport = () => {
    // 내보내기 로직 추가
    console.log('Exporting OVA:', { host, directory, name });
    onRequestClose(); // 모달 닫기
  };

  // 모든 호스트 목록가져오기 
  const { 
    data: hosts, 
  } = useAllHosts(toTableItemPredicateHosts);
  function toTableItemPredicateHosts(host) {
    return {                
      name: host?.name ?? '',                                                     
    };
  }

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="Export VM as OVA"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vm_ova_popup">
        <div className="popup_header">
          <h1>가상 어플라이언스로 가상 머신 내보내기</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="py-1">
          <div className="vnic_new_box">
            <label htmlFor="host_select">호스트</label>
            <select id="host_select" value={host} onChange={(e) => setHost(e.target.value)}>  
              {hosts?.map((hostItem, index) => (
                <option key={index} value={hostItem.name}>
                  {hostItem.name}
                </option>
              ))}
            </select>
          </div>
          <div className="vnic_new_box">
            <label htmlFor="directory">디렉토리</label>
            <input
              type="text"
              id="directory"
              value={directory}
              onChange={(e) => setDirectory(e.target.value)}
            />
          </div>
          <div className="vnic_new_box">
            <label htmlFor="name">이름</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
        </div>

        <div className="edit_footer">
          <button onClick={handleExport}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmExportOVAModal;
