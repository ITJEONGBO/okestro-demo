import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import { useAddNicFromVM, useEditNicFromVM, useNetworkInterfaceFromVM } from '../../api/RQHook';

const VmNetworkNewInterfaceModal = ({
  isOpen,
  onRequestClose,
  type = 'create', // 'create' or 'edit'
  networkInterfaceData = {}, // 편집 모드에서 사용할 초기 데이터
  onSubmit, // 부모 컴포넌트로 데이터 전달
}) => {
  const [name, setName] = useState('');
  const [profile, setProfile] = useState('');
  const [status, setStatus] = useState('up');
  const [connectionStatus, setConnectionStatus] = useState('connected');

  const { mutate: addNicFromVM } = useAddNicFromVM();
  const { mutate: editNicFromVM } = useEditNicFromVM();

    // 가상머신 내 네트워크인터페이스 목록
    const {
        data: nics,
        } = useNetworkInterfaceFromVM((e) => ({
        ...e,
        }));
    


  useEffect(() => {
    if (type === 'edit' && networkInterfaceData) {
      setName(networkInterfaceData.name || '');
      setProfile(networkInterfaceData.profile || '');
      setStatus(networkInterfaceData.status || 'up');
      setConnectionStatus(networkInterfaceData.connectionStatus || 'connected');
    } else {
      resetForm();
    }
  }, [type, networkInterfaceData]);

  const resetForm = () => {
    setName('');
    setProfile('');
    setStatus('up');
    setConnectionStatus('connected');
  };

  const handleSubmit = () => {
    const dataToSubmit = {
      name,
      profile,
      status,
      connectionStatus,
    };

    onSubmit(dataToSubmit);
    onRequestClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={type === 'edit' ? '네트워크 인터페이스 편집' : '새 네트워크 인터페이스'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="new_network_interface p">
        <div className="popup_header">
          <h1>{type === 'edit' ? '네트워크 인터페이스 편집' : '새 네트워크 인터페이스'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="network_popup_content">
          <div className="input_box pt-1">
            <span>이름</span>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="이름을 입력하세요"
            />
          </div>
          <div className="select_box">
            <label htmlFor="profile">프로파일</label>
            <select
              id="profile"
              value={profile}
              onChange={(e) => setProfile(e.target.value)}
            >
              <option value="default">Default</option>
              <option value="custom">Custom</option>
            </select>
          </div>
          <div className="select_box">
            <label htmlFor="type" className="disabled">유형</label>
            <select id="type" disabled>
              <option value="VirtIO">VirtIO</option>
            </select>
          </div>

          <div className="plug_radio_btn">
            <span>링크 상태</span>
            <div>
              <div className="radio_outer">
                <div>
                  <input
                    type="radio"
                    name="status"
                    id="status_up"
                    checked={status === 'up'}
                    onChange={() => setStatus('up')}
                  />
                  <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth />
                  <label htmlFor="status_up">Up</label>
                </div>
                <div>
                  <input
                    type="radio"
                    name="status"
                    id="status_down"
                    checked={status === 'down'}
                    onChange={() => setStatus('down')}
                  />
                  <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth />
                  <label htmlFor="status_down">Down</label>
                </div>
              </div>
            </div>
          </div>
          <div className="plug_radio_btn">
            <span>카드 상태</span>
            <div>
              <div className="radio_outer">
                <div>
                  <input
                    type="radio"
                    name="connection_status"
                    id="connected"
                    checked={connectionStatus === 'connected'}
                    onChange={() => setConnectionStatus('connected')}
                  />
                  <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth />
                  <label htmlFor="connected">연결됨</label>
                </div>
                <div>
                  <input
                    type="radio"
                    name="connection_status"
                    id="disconnected"
                    checked={connectionStatus === 'disconnected'}
                    onChange={() => setConnectionStatus('disconnected')}
                  />
                  <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth />
                  <label htmlFor="disconnected">분리</label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleSubmit}>{type === 'edit' ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmNetworkNewInterfaceModal;
