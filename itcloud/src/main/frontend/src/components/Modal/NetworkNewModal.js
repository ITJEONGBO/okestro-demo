import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { useAddNetwork, useAllDataCenters, useDataCenter, useEditNetwork, useNetworkById } from '../../api/RQHook';

const NetworkNewModal = ({ 
    isOpen, 
    onRequestClose, 
    editMode = false,
    networkId // 네트워크 ID를 받아서 편집 모드에서 사용
  }) => {
    const [id, setId] = useState('');
    const [datacenterVoId, setDatacenterVoId] = useState('');
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [comment, setComment] = useState('');
    const [portIsolation,setPortIsolation] = useState('');
    const [mtu, setMtu] = useState('');
    const [vlan, setVlan] = useState('');
    const [usageVm, setUsageVm] = useState(false);
    const [vmNetwork, setVmNetwork] = useState('');

    const { mutate: addNetwork } = useAddNetwork();
    const { mutate: editNetwork } = useEditNetwork();
    
  
    //  네트워크 데이터
    const { 
        data: network, 
        refetch: refetchNetworks,
        isLoading, 
        isError 
    } = useNetworkById(networkId);


    // 데이터센터 가져오기
    const {
      data: datacenters,
      status: datacentersStatus,
      isRefetching: isDatacentersRefetching,
      refetch: refetchDatacenters,
      isError: isDatacentersError,
      error: datacentersError,
      isLoading: isDatacentersLoading
    } = useAllDataCenters((e) => ({
      ...e,
    }));
  
    
    useEffect(() => {
      if (editMode && network) {
        setId(network.id);
        setDatacenterVoId(network.datacenterVo?.id || '');
        setName(network.name);
        setDescription(network.description);
        setComment(network.comment);
        setPortIsolation(network.portIsolation);
        setVlan(network.vlan);
        setVmNetwork(network.vmNetwork);
        setUsageVm(network.usage?.vm || false);
        setMtu(network.mtu);
      } else {
        resetForm();
        setDatacenterVoId(networkId || datacenters?.[0]?.id || '');
      }
    }, [isOpen, editMode, network, datacenters, networkId]);
    
    
      const resetForm = () => {
        setId('');
        setName('');
        setDescription('');
        setComment('');
        setVmNetwork('');
        setMtu('1500');
        setUsageVm(false);
        setVlan('0')
      };



      const handleFormSubmit = () => {
        const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
        if (!selectedDataCenter) {
          alert("데이터 센터를 선택해주세요.");
          return;
        }
    
        if(name === ''){
          alert("이름을 입력해주세요.");
          return;
        }
        const dataToSubmit = {
          datacenterVo: {
            id: selectedDataCenter.id,
            name: selectedDataCenter.name,
          },
          name,
          description,
          comment,
          portIsolation: Boolean(portIsolation), // 빈 문자열을 false로 처리
          mtu: mtu ? parseInt(mtu, 10) : 0, // mtu가 빈 값이면 1500 설정
          vlan: vlan ? parseInt(vlan, 10) : null, // 빈 문자열을 null로 설정
          usage: {
            defaultRoute: false,
            display: false,
            gluster: false,
            management: false,
            migration: false,
            vm: true // 기본값을 false로 설정하여 빈 값 방지
          },
          vmNetwork:  true
        };
        
    
      console.log('Data to submit:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인
  // 편집 모드에서 관리 네트워크 수정 제한 로직 추가
  if (editMode && network.usage?.management) {
    alert('관리 네트워크는 수정할 수 없습니다.');
    return;
  }

  if (editMode) {
    dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
    editNetwork({
      networkId: id,
      networkData: dataToSubmit
    }, {
      onSuccess: () => {
        alert('네트워크 편집 완료');
        onRequestClose();
      },
      onError: (error) => {
        console.error('Error editing network:', error);
      }
    });
  } else {
    addNetwork(dataToSubmit, {
      onSuccess: () => {
        alert('네트워크 생성 완료');
        onRequestClose();
      },
      onError: (error) => {
        console.error('Error adding network:', error);
      }
    });
  }
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
      <div className={`network_new_popup ${editMode ? 'edit-mode' : ''}`}>
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
                    id="data_center"
                    value={datacenterVoId}
                    onChange={(e) => setDatacenterVoId(e.target.value)}
                    disabled={editMode}
                  >
                    {datacenters &&
                      datacenters.map((dc) => (
                        <option value={dc.id}>
                          {dc.name}
                        </option>
                      ))}
                  </select>
                </div>
                <div className="network_form_group">
                  <div className="checkbox_group">
                    <label htmlFor="name">이름</label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }} fixedWidth />
                  </div>
                  <input
                    type="text"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)} 
                  />
                </div>
                <div className="network_form_group">
                  <label htmlFor="description">설명</label>
                  <input
                    type="text"
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)} // onChange 핸들러 추가
                  />
                </div>
                <div className="network_form_group">
                  <label htmlFor="comment">코멘트</label>
                  <input
                    type="text"
                    id="comment"
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                  />
                </div>
              </div>

              <div className="network_second_contents">
              <div className="network_checkbox_type1">
  <div className="checkbox_group">
    <input
      type="checkbox"
      id="valn_tagging"
      name="valn_tagging"
      checked={vlan !== ''}
      onChange={(e) => setVlan(e.target.checked ? '' : '')} // 체크 시 공백으로 설정, 비활성화 시 공백 유지
    />
    <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
  </div>
  <input
    type="text"
    id="valn_tagging_input"
    disabled={vlan === ''}
    value={vlan}
    onChange={(e) => setVlan(e.target.value)}
  />
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
      <input
        type="radio"
        id="default_mtu"
        name="mtu"
        value="default"
        checked={mtu === '1500'}
        onChange={() => setMtu('1500')} // 기본값 설정
      />
      <label htmlFor="default_mtu">기본값 (1500)</label>
    </div>
    <div className="radio_option">
      <input
        type="radio"
        id="user_defined_mtu"
        name="mtu"
        value="user_defined"
        checked={mtu !== '1500'} // 사용자 정의가 선택되었을 때 체크
        onChange={() => setMtu('')} // 사용자 정의 시 빈 값으로 설정
      />
      <label htmlFor="user_defined_mtu">사용자 정의</label>
    </div>
    <input
      type="text"
      id="mtu_input"
      value={mtu}
      onChange={(e) => setMtu(e.target.value)}
      disabled={mtu === '1500'} // 사용자 정의가 선택된 경우에만 활성화
    />
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
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default NetworkNewModal;
