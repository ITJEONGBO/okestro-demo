import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import toast from 'react-hot-toast';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import '../css/MNetwork.css';
import { 
  useAllDataCenters,
  useClustersFromDataCenter,
  useAddNetwork, 
  useEditNetwork, 
  useNetworkById, 
} from '../../../../api/RQHook';
import { CheckKorenName, CheckName } from '../../../../utils/CheckName';

const FormGroup = ({ label, children }) => (
  <div className="network-form-group center">
    <label style={{ 'font-size': "0.32rem" }}>{label}</label>
    {children}
  </div>
);

const NetworkModal = ({ isOpen, editMode = false, networkId, dcId, onClose }) => {
  const { mutate: addNetwork } = useAddNetwork();
  const { mutate: editNetwork } = useEditNetwork();
  
  //  Fault reason is "Operation Failed". Fault detail is "[Cannot edit Network. This logical network is used by host: rutilvm-dev.host04
  const [formState, setFormState] = useState({
    id: '',
    name: '',
    description: '',
    comment: '',
    mtu: '0',
    vlan: '0',
    usageVm: true,
    portIsolation: false,
  });
  const [dataCenterVoId, setDataCenterVoId] = useState(dcId || '');
  const [clusterVoList, setClusterVoList] = useState([]);
  const [dnsEnabled, setDnsEnabled] = useState(false);
  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      description: '',
      comment: '',
      mtu: '0',
      vlan: '0',
      usageVm: true,
      portIsolation: false,
    });
    setDataCenterVoId(dcId ||'');
    setClusterVoList([]);
  };

  const { 
    data: network, 
    refetch: refetchNetworks,
    isLoading: isNetworkLoading
  } = useNetworkById(networkId);

  const {
    data: datacenters = [],
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({...e,}));

  const {
    data: clusters = [],
    refetch: refetchClusters,
    isLoading: isNetworksLoading,
  } = useClustersFromDataCenter(dataCenterVoId ?? dcId, (e) => ({...e,}))

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
    }
  }, [isOpen]);
  
  useEffect(() => {
    if (editMode && network) {
      setFormState({
        id: network?.id || '',
        name: network?.name || '',
        description: network?.description || '',
        comment: network?.comment || '',
        mtu: network?.mtu || '0',
        vlan: network?.vlan || '0',
        usageVm: network?.usage?.vm || true,
      });
      setDataCenterVoId(network?.datacenterVo?.id || '');
    } else if(!editMode){
      resetForm();
    }
  }, [editMode, network]);  

  useEffect(() => {
    if (!editMode && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);
  
  useEffect(() => {
    if (clusters && clusters.length > 0) {
      setClusterVoList((prev) => 
        clusters.map((cluster, index) => ({
          ...cluster,
          isConnected: prev[index]?.isConnected ?? true,
          isRequired: prev[index]?.isRequired ?? false,
        }))
      );
    }
  }, [clusters]);
  
  const validateForm = () => {
    if (!CheckKorenName(formState.name) || !CheckName(formState.name)) {
      toast.error('이름이 유효하지 않습니다.');
      return false;
    }
    if (!CheckKorenName(formState.description)) {
      toast.error('설명이 유효하지 않습니다.');
      return false;
    }
    if (!CheckName(dataCenterVoId)) {
      toast.error('데이터센터를 선택해주세요.');
      return false;
    }
    return true;
  };


  const handleFormSubmit = () => {
    if (!validateForm()) return;

    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);

    const dataToSubmit = {
      datacenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      clusterVos: clusterVoList
        .filter((cluster) => cluster.isConnected) // 🔥 연결된 클러스터만 필터링
        .map((cluster) => ({
          id: cluster.id,
          name: cluster.name,
          required: cluster.isRequired,
        })),
      ...formState,
      mtu: formState.mtu ? parseInt(formState.mtu, 10) : 0, // mtu가 빈 값이면 1500 설/정
      vlan: formState.vlan !== 0 ? parseInt(formState.vlan, 10) : 0, // 빈 문자열을 null로 설정
      portIsolation: formState.portIsolation, 
      usage: { vm: formState.usageVm },
    };
    
    console.log("Form Data: ", dataToSubmit);

    if (editMode) {
      editNetwork(
        { networkId: formState.id, networkData: dataToSubmit },
        {
          onSuccess: () => {
            // 호스트가 붙어있다면 편집불가
            toast.success('네트워크 편집 완료');
            onClose(); // 모달 닫기
          },
          onError: (error) => {
            toast.error(`Error editing network: ${error.message}`);
          },
        }
      );
    } else {
      addNetwork(dataToSubmit, {
        onSuccess: () => {
          toast.success('네트워크 생성 완료');
          onClose(); // 모달 닫기
        },
        onError: (error) => {
          toast.error(`Error adding network: ${error.message}`);
        },
      });
    }
  };   
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? '논리 네트워크 수정' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className={`network-new-popup modal ${editMode ? 'edit-mode' : ''}`}>
        <div className="popup-header">
          <h1>{editMode ? '논리 네트워크 수정' : '새 논리 네트워크'}</h1>
            <button onClick={onClose}>
              <FontAwesomeIcon icon={faTimes} fixedWidth />
            </button>
        </div>
        <div className='network-new-content'>
          <div>
            <FormGroup label="데이터 센터">
              <select
                value={dataCenterVoId}
                onChange={(e) => setDataCenterVoId(e.target.value)}
                disabled={editMode}
              >
                {isDatacentersLoading ? (
                  <option>로딩중~</option>
                ) : (
                  datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name} ({dc.id})
                    </option>
                  ))
                )}
              </select>
            </FormGroup>

            <FormGroup label="이름">
              <input
                type="text"
                value={formState.name}
                autoFocus
                onChange={(e) => setFormState((prev) => ({ ...prev, name: e.target.value }))}
              />
    
            </FormGroup>
          
            <FormGroup label="설명">
              <input
                type="text"
                value={formState.description}
                onChange={(e) => setFormState((prev) => ({ ...prev, description: e.target.value }))}
              />
            </FormGroup>

            <FormGroup label="코멘트">
              <input
                type="text"
                value={formState.comment}
                onChange={(e) => setFormState((prev) => ({ ...prev, comment: e.target.value }))}
              />
            </FormGroup>
            <hr/>

            <FormGroup>
              <div className='network-new-input center'>
                <div className='network-checkbox'>
                  <input
                    type="checkbox"
                    id="vlan"
                    checked={formState.vlan !== null} // ✅ 기본적으로 체크 해제
                    onChange={(e) => {
                      const isChecked = e.target.checked;
                      setFormState((prev) => ({
                        ...prev,
                        vlan: isChecked ? "" : null, // ✅ 체크하면 빈 문자열, 해제하면 null
                      }));
                    }}
                  />
                  <label>VLAN 태깅 활성화</label>
                </div>

                <input
                  type="number"
                  id="vlan"
                  className="input_number"
                  min="1"
                  step="1"
                  disabled={formState.vlan === null} // ✅ 체크되지 않으면 비활성화
                  value={formState.vlan === null ? '' : formState.vlan} 
                  onChange={(e) => {
                    setFormState((prev) => ({
                      ...prev,
                      vlan: e.target.value, // ✅ 입력값 그대로 반영
                    }));
                  }}
                />
              </div>
            </FormGroup>


            <FormGroup label="">
              <div className='network-checkbox-only'>
                <input
                  type="checkbox"
                  id="usageVm"
                  name="usageVm"
                  checked={formState.usageVm}
                  onChange={(e) => {
                    const isChecked = e.target.checked;
                    setFormState((prev) => ({
                      ...prev,
                      usageVm: isChecked,
                      portIsolation: isChecked ? prev.portIsolation : false, // 포트 분리를 비활성화
                    }));
                  }}
                />
                <label>가상 머신 네트워크</label>
              </div>
            </FormGroup>
            
            <FormGroup>
              <div className='network-checkbox-only'>
                <input
                  type="checkbox"
                  id="portIsolation"
                  name="portIsolation"
                  checked={formState.portIsolation}
                  onChange={(e) => setFormState((prev) => ({ ...prev, portIsolation: e.target.checked }))}
                  disabled={!formState.usageVm} // 가상 머신 네트워크가 비활성화되면 비활성화
                />
                <label>포트 분리</label>
              </div>
            </FormGroup>

            <FormGroup label="MTU" className="mtu-form">
              <div className="mtu-input-outer">
                <div className="mtu-radio-input">
                  <div className='flex'>
                    <input
                      type="radio"
                      checked={formState.mtu === "0"} // 기본값 1500 선택됨
                      onChange={() => setFormState((prev) => ({ ...prev, mtu: "0" }))}
                    />
                    <label>기본값 (1500)</label>
                  </div>
                  <div className='flex'>
                    <input
                      type="radio"
                      checked={formState.mtu !== "0"} // 사용자 정의 값이 있을 때 선택됨
                      onChange={() => setFormState((prev) => ({ ...prev, mtu: "" }))} // 빈 문자열로 설정해 사용자가 입력할 수 있도록
                    />
                    <label>사용자 정의</label>
                  </div>
                </div>
                <div className="mtu-text-input">
                  <input 
                    type="number" 
                    style={{ width: '100%' }}
                    min="68"
                    step="1" 
                    disabled={formState.mtu === "0"} // 기본값 선택 시 비활성화
                    value={formState.mtu === "0" ? "" : formState.mtu} // 기본값일 경우 빈 값 표시
                    onChange={(e) => {
                      const value = e.target.value;
                      setFormState((prev) => ({
                        ...prev,
                        mtu: value, // 입력값 반영
                      }));
                    }}
                  />
                </div>
              </div>
            </FormGroup>

            <FormGroup>
              <div className="network-checkbox-only">
                <input 
                  type="checkbox" 
                  id="dns_settings" 
                  name="dns_settings" 
                  checked={dnsEnabled}
                  onChange={(e) => setDnsEnabled(e.target.checked)} 
                />
                <label>DNS 설정</label>
              </div>
            </FormGroup>

            <div style={{fontSize: "0.32rem",padding:'0 1em',fontWeight:'800' }}>DNS 서버</div>
            <FormGroup>
              <div className='network-form-group center' style={{width:'100%',padding:0}}>
                  <input 
                    type="text" 
                    id="dns_server" 
                    disabled={!dnsEnabled} 
                  />
                <div className='plusbtns' style={{ 'font-size': "13px", height: '32px' }}>
                  <button 
                   type="button"
                   className="border-r border-gray-500"
                   onClick={() => console.log("Add DNS Server")}
                   disabled={!dnsEnabled} // 버튼도 비활성화
                  >
                    +
                  </button>
                  <button
                    type="button"
                    onClick={() => console.log("Remove DNS Server")}
                    disabled={!dnsEnabled} 
                  >
                    -
                  </button>
                </div>
              </div>
            </FormGroup>
            
            {!editMode &&
              <div className="network-new-cluster-form">
              <hr/>
                <span>클러스터에서 네트워크를 연결/분리</span>
                <div>
                  <table className="network-new-cluster-table">
                    <thead>
                      <tr>
                        <th>이름</th>
                        <th>
                          <div className="checkbox_group">
                            <input
                              type="checkbox"
                              id="connect_all"
                              checked={clusterVoList.every((cluster) => cluster.isConnected)} // 모든 클러스터 연결 상태 확인
                              onChange={(e) => {
                                const isChecked = e.target.checked;
                                setClusterVoList((prevState) =>
                                  prevState.map((cluster) => ({
                                    ...cluster,
                                    isConnected: isChecked,
                                    isRequired: isChecked ? cluster.isRequired : false, // 연결 해제 시 필수도 해제
                                  }))
                                );
                              }}
                            />
                            <label htmlFor="connect_all"> 모두 연결</label>
                          </div>
                        </th>
                        <th>
                          <div className="checkbox_group">
                            <input
                              type="checkbox"
                              id="require_all"
                              checked={
                                clusterVoList.every((cluster) => cluster.isRequired) &&
                                clusterVoList.every((cluster) => cluster.isConnected) // 연결 상태가 모두 체크된 경우에만 가능
                              }
                              disabled={!clusterVoList.every((cluster) => cluster.isConnected)} // 연결 상태가 아닌 경우 비활성화
                              onChange={(e) => {
                                const isChecked = e.target.checked;
                                setClusterVoList((prevState) =>
                                  prevState.map((cluster) => ({
                                    ...cluster,
                                    isRequired: isChecked, // "모두 필요" 상태 설정
                                  }))
                                );
                              }}
                            />
                            <label htmlFor="require_all"> 모두 필요</label>
                          </div>
                        </th>
                      </tr>
                    </thead>

                    <tbody>
                      {clusterVoList.map((cluster, index) => (
                        <tr key={cluster.id}>
                          <td>{cluster.name} / {cluster.id} </td>
                          <td className="checkbox-group">
                            <div className="checkbox_group">
                              <input
                                type="checkbox"
                                id={`connect_${cluster.id}`}
                                checked={cluster.isConnected} // 연결 상태
                                onChange={(e) => {
                                  const isChecked = e.target.checked;
                                  setClusterVoList((prevState) =>
                                    prevState.map((c, i) =>
                                      i === index
                                        ? { ...c, isConnected: isChecked, isRequired: isChecked ? c.isRequired : false } // 연결 해제 시 필수 상태도 해제
                                        : c
                                    )
                                  );
                                }}
                              />
                              <label htmlFor={`connect_${cluster.id}`}> 연결</label>
                            </div>
                          </td>
                          <td className="checkbox-group">
                            <div className="checkbox_group">
                              <input
                                type="checkbox"
                                id={`require_${cluster.id}`}
                                checked={cluster.isRequired} // 필수 상태
                                disabled={!cluster.isConnected} // 연결 상태가 체크되지 않으면 비활성화
                                onChange={(e) => {
                                  const isChecked = e.target.checked;
                                  setClusterVoList((prevState) =>
                                    prevState.map((c, i) =>
                                      i === index
                                        ? { ...c, isRequired: isChecked }
                                        : c
                                    )
                                  );
                                }}
                              /> 
                              <label htmlFor={`require_${cluster.id}`}> 필수</label>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            }
          </div>
        </div>

        <div className="edit-footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default NetworkModal;
