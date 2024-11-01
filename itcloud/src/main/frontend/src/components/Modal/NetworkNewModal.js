import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { useNetworkById } from '../../api/RQHook';

const NetworkNewModal = ({ 
    isOpen, 
    onRequestClose, 
    onSave, 
    editMode = false,
    networkId // 네트워크 ID를 받아서 편집 모드에서 사용
  }) => {
    const [id, setId] = useState('');
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [comment, setComment] = useState('');
    const [valnTagging, setValnTagging] = useState(false);
    const [vmNetwork, setVmNetwork] = useState(true);
    const [photoSeparation, setPhotoSeparation] = useState(false);
    const [mtu, setMtu] = useState('default');
    const [dnsSettings, setDnsSettings] = useState(false);
    const [dnsServer, setDnsServer] = useState('');
    const [quotaMode, setQuotaMode] = useState();
  
    // 예시: useNetwork Hook을 사용하여 기존 데이터를 가져옴
    const { 
        data: network, 
        isLoading, 
        isError 
    } = useNetworkById(networkId);
  
    // 모달이 열릴 때 기존 데이터를 상태에 설정
    useEffect(() => {
      if (editMode && network) {
        
        setId(network.id);
        setName(network.name);
        setDescription(network.description);
        setComment(network.comment);
        setValnTagging(network.valnTagging);
        setVmNetwork(network.vmNetwork);
        setPhotoSeparation(network.photoSeparation);
        setMtu(network.mtu);
        setDnsSettings(network.dnsSettings);
        setDnsServer(network.dnsServer);
      } else { // 그게아니면 리셋 = 새로만들기
        resetForm();
      }
    }, [editMode, network]);
  
    const resetForm = () => {
      setName('');
      setDescription('');
      setComment('');
      setValnTagging(false);
      setVmNetwork(true);
      setPhotoSeparation(false);
      setMtu('default');
      setDnsSettings(false);
      setDnsServer('');
    };
  
    const handleFormSubmit = () => {
      const dataToSubmit = {
        name,
        description,
        comment,
        valnTagging,
        vmNetwork,
        photoSeparation,
        mtu,
        dnsSettings,
        dnsServer,
      };
      onSave(dataToSubmit); // 데이터를 부모 컴포넌트로 전달
      onRequestClose(); // 모달 닫기
    };
  
  const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
  const handleTabClick = (tab) => setSelectedTab(tab);

  
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '논리 네트워크 수정' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="network_new_popup">
        <div className="popup_header">
        <h1>{editMode ? '논리 네트워크 수정' : '새 논리적 네트워크'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="flex">
          {/* 일반 */}
          {selectedTab === 'network_new_common_btn' && (
            <form id="network_new_common_form">
              <div className="network_first_contents">
                <div className="network_form_group">
                  <label htmlFor="cluster">데이터 센터</label>
                  <select
                    id="quota_mode"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    />
                </div>
                <div className="network_form_group">
                  <div className="checkbox_group">
                    <label htmlFor="name">이름</label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth />
                  </div>
                  <input type="text" id="name" />
                </div>
                <div className="network_form_group">
                  <label htmlFor="description">설명</label>
                  <input type="text" id="description" />
                </div>
                <div className="network_form_group">
                  <label htmlFor="comment">코멘트</label>
                  <input type="text" id="comment" />
                </div>
              </div>

              <div className="network_second_contents">
                <div className="network_checkbox_type1">
                  <div className="checkbox_group">
                    <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                    <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                  </div>
                  <input type="text" id="valn_tagging_input" disabled />
                </div>
                <div className="network_checkbox_type2">
                  <input type="checkbox" id="vm_network" name="vm_network" checked />
                  <label htmlFor="vm_network">가상 머신 네트워크</label>
                </div>
                <div className="network_checkbox_type2">
                  <input type="checkbox" id="photo_separation" name="photo_separation" />
                  <label htmlFor="photo_separation">포토 분리</label>
                </div>
                <div className="network_radio_group">
                  <div style={{ marginTop: '0.2rem' }}>MTU</div>
                  <div>
                    <div className="radio_option">
                      <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                      <label htmlFor="default_mtu">기본값 (1500)</label>
                    </div>
                    <div className="radio_option">
                      <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                      <label htmlFor="user_defined_mtu">사용자 정의</label>
                    </div>
                  </div>
                </div>
                <div className="network_checkbox_type2">
                  <input type="checkbox" id="dns_settings" name="dns_settings" />
                  <label htmlFor="dns_settings">DNS 설정</label>
                </div>
                <span>DNS서버</span>
                <div className="network_checkbox_type3">
                  <input type="text" id="dns_server" disabled />
                  <div>
                    <button>+</button>
                    <button>-</button>
                  </div>
                </div>
              </div>

              <div id="network_new_cluster_form" style={{ display: editMode ? 'none' : 'block' }}>
  <span>클러스터에서 네트워크를 연결/분리</span>
  <div>
    <table className="network_new_cluster_table">
      <thead>
        <tr>
          <th>이름</th>
          <th>
            <div className="checkbox_group">
              <input type="checkbox" id="connect_all" />
              <label htmlFor="connect_all"> 모두 연결</label>
            </div>
          </th>
          <th>
            <div className="checkbox_group">
              <input type="checkbox" id="require_all" />
              <label htmlFor="require_all"> 모두 필요</label>
            </div>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Default</td>
          <td className="checkbox-group">
            <div className="checkbox_group">
              <input type="checkbox" id="connect_default" />
              <label htmlFor="connect_default"> 연결</label>
            </div>
          </td>
          <td className="checkbox-group">
            <div className="checkbox_group">
              <input type="checkbox" id="require_default" />
              <label htmlFor="require_default"> 필수</label>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
            </form>
          )}
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={onSave}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default NetworkNewModal;
