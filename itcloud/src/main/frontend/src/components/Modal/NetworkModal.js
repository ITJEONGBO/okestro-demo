import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import '../Modal/css/MNetwork.css';
import { 
  useAllDataCenters,
  useClustersFromDataCenter,
  useAddNetwork, 
  useEditNetwork, 
  useNetworkById, 
} from '../../api/RQHook';

const FormGroup = ({ label, children }) => (
  <div className="network_form_group">
    <label style={{ 'font-size': "13px" }}>{label}</label>
    {children}
  </div>
);


const NetworkModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false,
  networkId // 네트워크 ID를 받아서 편집 모드에서 사용
}) => {
  const [formState, setFormState] = useState({
    id: '',
    name: '',
    description: '',
    comment: '',
    portIsolation: false,
    mtu: '0',
    vlan: '0',
    usageVm: true,
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');
  // const [clusterVoId, setClusterVoId] = useState('');
  const [clusterVoList, setClusterVoList] = useState([]);
  
  const { 
    data: network, 
    refetch: refetchNetworks,
    isLoading: isNetworkLoading, 
    isError: isNetworkError
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
  } = useClustersFromDataCenter(
    dataCenterVoId ? dataCenterVoId: undefined, 
    (e) => ({...e,})
  )

  const { mutate: addNetwork } = useAddNetwork();
  const { mutate: editNetwork } = useEditNetwork();
  

  useEffect(() => {
    console.log("네트워크 데이터:", network);
  
    if (editMode && network) {
      setFormState({
        id: network.id || '',
        name: network.name || '',
        description: network.description || '',
        comment: network.comment || '',
        portIsolation: false,
        mtu: network.mtu || '0',
        vlan: network.vlan || '0',
        usageVm: network.usage.vm || true,
      });
      setDataCenterVoId(network.datacenterVo?.id || '');
    } else if (!editMode && !isDatacentersLoading) {
      resetForm();
    }
  }, [editMode, network]);


  useEffect(() => {
    if (!editMode && datacenters && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);
    

  useEffect(() => {
    if (clusters) {
      setClusterVoList(
        clusters.map((cluster) => ({
          ...cluster,
          isConnected: true, // 처음엔 모두 연결 상태로 설정
          isRequired: true, // 처음엔 모두 필수 상태로 설정되지 않음
        }))
      );
    }
  }, [clusters]);
  
  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      description: '',
      comment: '',
      portIsolation: false,
      mtu: '0',
      vlan: '0',
      usageVm: true,
    });
    setDataCenterVoId('');
    setClusterVoList([]);
  };

  // 클러스터 연결 정보를 데이터에 추가
  const clusterConnectionData = clusterVoList.map((cluster) => ({
    id: cluster.id,
    name: cluster.name,
    // isConnected: cluster.isConnected,
    required: cluster.isRequired,
  }));

  const validateForm = () => {
    if (!formState.name) return '이름을 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    return null;
  };

  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }

    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);

    const dataToSubmit = {
      datacenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      ...formState,
      mtu: formState.mtu ? parseInt(formState.mtu, 10) : 0, // mtu가 빈 값이면 1500 설/정
      vlan: formState.vlan !== 0 ? parseInt(formState.vlan, 10) : 0, // 빈 문자열을 null로 설정
      usage: { vm: formState.usageVm },
      clusterVos: clusterConnectionData, 
    };
    
    console.log("Form Data: ", dataToSubmit);
    
    // if (editMode && network.usage?.management) {
    //   alert('관리 네트워크는 수정할 수 없습니다.');
    //   return;
    // }

    if (editMode) {
      editNetwork(
        { networkId: formState.id, networkData: dataToSubmit}, 
        {
          onSuccess: () => {
            alert('네트워크 편집 완료');
            onRequestClose();
          },
          onError: (error) => {
            console.error('Error editing network:', error);
          }
        }
      );
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
        <h1>{editMode ? '논리 네트워크 수정' : '새 논리 네트워크'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        {/* <div className="flex"> */}
        <div className="network_first_contents">

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
              onChange={(e) => setFormState((prev) => ({ ...prev, name: e.target.value }))}
            />
            {/* <FontAwesomeIcon
              icon={faInfoCircle}
              style={{ color: 'rgb(83, 163, 255)', marginLeft: '5px' }}
              data-tooltip-id="network-name-tooltip"
            />
            <Tooltip id="network-name-tooltip" className="icon_tooltip" place="top" effect="solid" >
              네트워크 이름에 공백이 있거나 15자를 초과하는 경우에는 호스트에서 UUID로 대체합니다.
            </Tooltip> */}
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

          <FormGroup >
            <div className='network_checkbox'>
            <input
              type="checkbox"
              id="vlan"
              checked={formState.vlan !== '0'}
              onChange={(e) => {
                const isChecked = e.target.checked;
                setFormState((prev) => ({
                  ...prev,
                  vlan: isChecked ? '' : '0', // 체크되면 빈 문자열로, 해제되면 null로 설정
                }));
              }}
            />
            <label style={{ fontSize: "13px" }}>VLAN 태깅 활성화</label>

            <input
              type="text"
              id="vlan"
              disabled={formState.vlan === '0'}
              value={formState.vlan === "0"  ? '' : formState.vlan } 
              onChange={(e) => {
                const value = e.target.value;
                setFormState((prev) => ({
                  ...prev,
                  vlan: value,
                }));
              }}
            />
            </div>
          </FormGroup>
          
          <FormGroup label="">
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
            <label style={{ fontSize: "13px" }}>가상 머신 네트워크</label>
          </FormGroup>
          
          <FormGroup>
            <input
              type="checkbox"
              id="portIsolation"
              name="portIsolation"
              checked={formState.portIsolation}
              onChange={(e) => setFormState((prev) => ({ ...prev, portIsolation: e.target.checked }))}
              disabled={!formState.usageVm} // 가상 머신 네트워크가 비활성화되면 비활성화
            />
            <label style={{ fontSize: "13px" }}>포트 분리</label>
          </FormGroup>

          <FormGroup label="MTU">
            <div className="network_radio_group">
              <input
                type="radio"
                name="mtu"
                value="default"
                checked={formState.mtu === '0'} // 기본값 상태 체크
                onChange={() => setFormState((prev) => ({ ...prev, mtu: '0' }))}
              />
              <label style={{ fontSize: "13px" }}>기본값 (1500)</label>

              <input
                type="radio"
                name="mtu"
                value="user_defined"
                checked={formState.mtu !== '0'} // 사용자 정의 상태 체크
                onChange={() => setFormState((prev) => ({ ...prev, mtu: '' }))} // 사용자 정의 선택 시 값 초기화
              />
              <label style={{ fontSize: "13px" }}>사용자 정의</label>

              <input
                type="number"
                id="mtu_input"
                value={formState.mtu === '0' ? '' : formState.mtu} // 편집 모드에서 기본값 처리
                onChange={(e) => setFormState((prev) => ({ ...prev, mtu: e.target.value }))}
                disabled={formState.mtu === '0'}
              />
            </div>
          </FormGroup>


          
          {/* <FormGroup>
            <input 
              type="checkbox" 
              id="dns_settings" 
              name="dns_settings" 
            />
            <label style={{ 'font-size': "13px" }}>DNS 설정</label>
            
            <label style={{ 'font-size': "13px" }}>DNS서버</label>
            <input 
              type="text" 
              id="dns_server" 
              disabled 
            />
            <div className='plusbtns' style={{ 'font-size': "13px", height: '32px' }}>
              <button class="border-r border-gray-500">+</button>
              <button>-</button>
            </div>
          </FormGroup> */}
          
          {!editMode &&
            <div id="network_new_cluster_form">
            <hr/>
              <span>클러스터에서 네트워크를 연결/분리</span>
              <div>
                <table className="network_new_cluster_table">
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

        <div className="edit_footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default NetworkModal;
