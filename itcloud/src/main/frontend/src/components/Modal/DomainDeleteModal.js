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
  data, // 선택된 도메인들 (단일 객체 또는 배열)
}) => {
  const [format, setFormat] = useState(false);
  const [hostName, setHostName] = useState('');
  const [selectedIds, setSelectedIds] = useState([]);
  const [selectedNames, setSelectedNames] = useState([]);

  const { mutate: deleteDomain } = useDeleteDomain();

  // 호스트 목록 가져오기
  const {
    data: hosts = [],
    isLoading: isHostsLoading,
  } = useAllHosts();

  // data가 배열 또는 단일 객체일 경우를 처리
  useEffect(() => {
    if (Array.isArray(data)) {
      setSelectedIds(data.map((item) => item.id));
      setSelectedNames(data.map((item) => item.name || item.alias || ''));
    } else if (data) {
      setSelectedIds([data.id]);
      setSelectedNames([data.name || data.alias || '']);
    }
  }, [data]);

  useEffect(() => {
    if (hosts && hosts.length > 0) {
      setHostName(hosts[0].name); // 기본 호스트 이름 설정
    }
  }, [hosts]);

  const handleFormSubmit = () => {
    if (!selectedIds.length) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }

    console.log('Deleting domains:', { selectedIds, format, hostName });

    selectedIds.forEach((id, index) => {
      deleteDomain(
        { domainId: id, format: format, hostName: hostName },
        {
          onSuccess: () => {
            console.log(`도메인 삭제 성공: ${selectedNames[index]} (ID: ${id})`);
            if (index === selectedIds.length - 1) {
              onRequestClose(); // 모든 삭제가 완료되면 모달 닫기
            }
          },
          onError: (error) => {
            console.error(`도메인 ${selectedNames[index]} 삭제 오류:`, error);
          },
        }
      );
    });
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
      <div className="domain_delete_popup">
        <div className="popup_header">
          <h1>스토리지 도메인 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span>
              {selectedNames.length > 1 
                ? `${selectedNames.join(', ')} 를(을) 삭제하시겠습니까?`
                : `${selectedNames[0]} 를(을) 삭제하시겠습니까?`}
            </span>
          </div>
        </div>

        <div className="disk_delete_box" style={{display : 'flex'}}>
          <div className='flex'>
            <input
              type="checkbox"
              id="format"
              checked={format}
              onChange={(e) => setFormat(e.target.checked)} // 체크 여부에 따라 true/false 설정
            />
            <label htmlFor="format">포맷 하시겠습니까?</label>
          </div>
          <div className="disk_delete_box">
            <select
              value={hostName}
              onChange={(e) => setHostName(e.target.value)}
              disabled={!format} // format이 false면 비활성화
            >
              {hosts.map((host) => (
                <option key={host.id} value={host.name}>
                  {host.name} : {host.id}
                </option>
              ))}
            </select>
          </div>
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
