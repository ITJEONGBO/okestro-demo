import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import {
  useAllHosts,
  useDeleteDomain,
} from '../../api/RQHook';

const DomainDeleteModal = ({ 
    isOpen, 
    onRequestClose, 
    data,
}) => {
  
  // 호스트 목록 가져오기
  const {
    data: hosts = [],
    refetch: refetchHosts,
    isLoading: isHostsLoading
  } = useAllHosts((e) => ({...e,}));

  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || data.alias || '');
      console.log('**' + data.id);
    }
  }, [data]);

  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [format, setFormat] = useState(false);
  const [hostName, setHostName] = useState('');
  
  const { mutate: deleteDomain } = useDeleteDomain();

  useEffect(() => {
    console.log('삭제데이터:', data, id);
  }, [data, id]);
  
  useEffect(() => {
    if (hosts && hosts.length > 0) {
      setHostName(hosts[0].name);
    }
  }, [hosts]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }
  
    console.log(`Data to submit: ID=${id}, Format=${format}, HostName=${hostName}`);
  
    deleteDomain(
      { domainId: id, format: format, hostName: hostName },
      {
        onSuccess: () => {
          console.log(`스토리지 도메인 삭제: ${id}, ${format}, ${hostName}`);
          onRequestClose();
        },
        onError: (error) => {
          console.error(`스토리지 도메인 ${name} 삭제 오류:`, error);
        },
      }
    );    
  };

  
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={'스토리지 도메인'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>스토리지 도메인 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {name} 를(을) 삭제하시겠습니까? </span>
          </div>
        </div>

        <div className="disk_delete_box">
          <input
            type="checkbox"
            id="format"
            checked={format}
            onChange={(e) => setFormat(e.target.checked)} // 체크 여부에 따라 true/false 설정
          />
          <label htmlFor="format">포멧 하시겠습니까?</label>
        </div>

        <div className="disk_delete_box">
          <select
            value={hostName}
            onChange={(e) => setHostName(e.target.value)}
            disabled={!format} // format이 false면 비활성화
          >
            {hosts && hosts.map((h) => (
              <option key={h.id} value={h.id}>
                {h.name} : {h.id}
              </option>
            ))}
          </select>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DomainDeleteModal;

