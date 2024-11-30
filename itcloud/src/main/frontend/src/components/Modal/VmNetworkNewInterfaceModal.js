import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import { useAddNicFromVM, useEditNicFromVM, useNetworkInterfaceFromVM } from '../../api/RQHook';

const VmNetworkNewInterfaceModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  nicData,
  vmId
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');
  const [profile, setProfile] = useState(''); 
  const [vnicProfileVoId, setVnicProfileVoId] = useState(''); 
  const [vnicProfileVoName, setVnicProfileVoName] = useState(''); 
  const [interface_, setInterface_] = useState('');//유형  NicInterface
  const [linked, setLinked] = useState(''); //링크상태(link state) t(up)/f(down) -> nic 상태도 같이 변함
  const [plugged, setPlugged] = useState(false); 
  const [status, setStatus] = useState('up');
  const [macAddress, setMacAddress] = useState('');
  const [connectionStatus, setConnectionStatus] = useState('connected');

  const { mutate: addNicFromVM } = useAddNicFromVM();
  const { mutate: editNicFromVM } = useEditNicFromVM();

  
  useEffect(() => {
    console.log('VM ID아아아:', vmId); // vmId 값 확인
  }, [vmId]);

    // 가상머신 내 네트워크인터페이스 목록
    const { 
      data: nics 
    } = useNetworkInterfaceFromVM(vmId);

      useEffect(() => {
        if (editMode && nicData) {
          setId(nicData.id);
          setName(nicData.name);
          setProfile(nicData.profile);
          setVnicProfileVoId(nicData.vnicProfileVo?.id || '');
          setVnicProfileVoName(nicData.vnicProfileVo.name || '');
          setInterface_(nicData.interface_);
          setLinked(nicData.linked);
          setPlugged(nicData.plugged);
          setStatus(nicData.status);
          setMacAddress(nicData.macAddress);
        } else {
          resetForm();
        }
      }, [isOpen, editMode, nicData, vmId, nics]);


  const resetForm = () => {
    setId('');
    setName('');
    setInterface_('');
    setLinked(false);
    setPlugged(false);
    setProfile('');
    setMacAddress('');
    setStatus('up');
    setConnectionStatus('connected');
  };

  const handleSubmit = () => {
    const dataToSubmit = {
      vnicProfileVo: {
        id: vnicProfileVoId || null, // null이면 서버에서 오류가 발생할 가능성 있음
        name: vnicProfileVoName || '', // 빈 문자열 처리 필요
      },
      name,
      interface_,
      linked,
      plugged,
      macAddress
      // profile,
      // status,
      // connectionStatus,
    };
    console.log('네트워크인터페이스 생성, 편집데이터:', dataToSubmit); 

    if (editMode) {
      dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
      editNicFromVM({
        nicId: id,
        nicData: dataToSubmit
      }, {
        onSuccess: () => {
          alert('네트워크인터페이스 편집 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error editing network:', error);
        }
      });
    } else {
      addNicFromVM(dataToSubmit, {
        onSuccess: () => {
          alert('네트워크인터페이스 생성 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding network:', error);
        }
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '네트워크 인터페이스 편집' : '새 네트워크 인터페이스 생성'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="new_network_interface p">
        <div className="popup_header">
          <h1>{editMode ? '네트워크 인터페이스 편집' : '새 네트워크 인터페이스 생성'}</h1>
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
          <button onClick={handleSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmNetworkNewInterfaceModal;
