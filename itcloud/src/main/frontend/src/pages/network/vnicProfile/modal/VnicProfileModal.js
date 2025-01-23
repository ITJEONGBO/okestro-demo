import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import '../css/MVnic.css';
import { 
  useAddVnicProfile, 
  useAllDataCenters, 
  useAllnicFromVM, 
  useEditVnicProfile, 
  useNetworksFromDataCenter, 
  useVnicProfile
} from '../../../../api/RQHook';
import toast from 'react-hot-toast';

const FormGroup = ({ label, children }) => (
  <div className="vnic-new-box">
    <label>{label}</label>
    {children}
  </div>
);

const VnicProfileModal = ({ isOpen, editMode = false, vnicProfileId, networkId, onClose }) => {
  const { mutate: addVnicProfile } = useAddVnicProfile();
  const { mutate: editVnicProfile } = useEditVnicProfile();

  const [formState, setFormState] = useState({
    id: '',
    name: '',
    description: '',
    // passthrough: '',
    portMirroring: false,
    migration: true,
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');  
  const [networkVoId, setNetworkVoId] = useState(networkId || '');  
  // const [networkFilter, setNetworkFilter] = useState('');

  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      description: '',
      // passthrough: '',
      portMirroring: false,
      migration: true,
    });
    setDataCenterVoId('');
    setNetworkVoId(networkId || '');
  };

  const { 
    data: vnic,
    isLoading: isVnicLoading
  } = useVnicProfile( vnicProfileId);
  
  const {
    data: datacenters = [],
    isLoading: isDataCentersLoading
  } = useAllDataCenters((e) => ({...e,}));

  const {
    data: networks = [],
    isLoading: isNetworksLoading
  } = useNetworksFromDataCenter(dataCenterVoId || undefined, (e) => ({...e,}));
  
  //페일오버버
  // const { 
  //   data: failoverNics = [], 
  //   isLoading: isFailoverNicsLoading 
  // } = useAllnicFromVM(dataCenterVoId, (e) => ({
  //   id: e?.id,
  //   name: e?.name,
  // }));

  const nFilters = [  // api로 들어감
    { value: "vdsm-no-mac-spoofing", label: "vdsm-no-mac-spoofing" },
    { value: "allow-arp", label: "allow-arp" },
    { value: "allow-dhcp", label: "allow-dhcp" },
    { value: "allow-incoming-ipv4", label: "allow-incoming-ipv4" },
    { value: "allow-ipv4", label: "allow-ipv4" },
    { value: "clean-traffic", label: "clean-traffic" },
    { value: "no-arp-ip-spoofing", label: "no-arp-ip-spoofing" },
    { value: "no-arp-spoofing", label: "no-arp-spoofing" },
    { value: "no-ip-multicast", label: "no-ip-multicast" },
    { value: "no-ip-spoofing", label: "no-ip-spoofing" },
    { value: "no-mac-broadcast", label: "no-mac-broadcast" },
    { value: "no-mac-spoofing", label: "no-mac-spoofing" },
    { value: "no-other-l2-traffic", label: "no-other-l2-traffic" },
    { value: "no-other-rarp-traffic", label: "no-other-rarp-traffic" },
    { value: "qemu-announce-self", label: "qemu-announce-self" },
    { value: "qemu-announce-self-rarp", label: "qemu-announce-self-rarp" },
    { value: "clean-traffic-gateway", label: "clean-traffic-gateway" },
    { value: "", label: "No Network Filter" },
  ];

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && !isVnicLoading && !isDataCentersLoading && !isNetworksLoading) {
      if (editMode && vnic) {
        setFormState({
          id: vnic?.id || '',
          name: vnic?.name || '',
          description: vnic?.description || '',
          migration: vnic?.migration || false,
          portMirroring: vnic?.portMirroring || false,
        });
        setDataCenterVoId(vnic?.dataCenterVo?.id || '');
        setNetworkVoId(vnic?.networkVo?.id || '');
      } else if (!editMode && datacenters.length > 0) {
        resetForm();
        setDataCenterVoId(datacenters[0].id);
        if (networkId) {
          setNetworkVoId(networkId);
        } else if (networks.length > 0) {
          setNetworkVoId(networks[0].id);
        }
      }
    }
  }, [isOpen, editMode, vnic, datacenters, networks, isVnicLoading, isDataCentersLoading, isNetworksLoading]);
  

  useEffect(() => {
    if (!editMode && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);

  useEffect(() => {
    if (!editMode && networks.length > 0) {
      setNetworkVoId(networks[0].id);
    }
  }, [networks, editMode]);
  useEffect(() => {
    if (!editMode && networkId) {
      setNetworkVoId(networkId); // networkId 값을 설정
    } else if (!editMode && networks.length > 0) {
      setNetworkVoId(networks[0].id); // networks의 첫 번째 값을 설정
    }
  }, [editMode, networkId, networks]);

  const handleFormSubmit = () => {
    if (!formState.name) return toast.error('이름을 입력해주세요.');

    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);
    const selectedNetwork = networks.find((n) => n.id === networkVoId);    

    const dataToSubmit = {
      networkVo: { id: selectedNetwork.id, name: selectedNetwork.name},
      ...formState,
    };
    console.log('dataToSubmit:', dataToSubmit); 

    const mutation = editMode ? editVnicProfile : addVnicProfile;
    mutation(
      editMode ? { vnicId: formState.id, vnicData: dataToSubmit } : dataToSubmit,
      {
        onSuccess: () => {
          toast.success(editMode ? 'vNIC 프로파일이 성공적으로 편집되었습니다.' : 'vNIC 프로파일이 성공적으로 추가되었습니다.');
          onClose();
        },
        onError: (error) => {
          toast.error(`vNIC 프로파일 ${editMode ? '편집' : '추가'} 중 오류 발생: ${error}`);
        },
      }
    );
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? 'vNIC 프로파일 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vnic-new-content-popup">
        <div className="popup-header">
          <h1>{editMode ? '가상 머신 인터페이스 프로파일 편집' : '가상 머신 인터페이스 프로파일'}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="vnic-new-content">
          <div className="vnic-new-contents" style={{ paddingTop: '0.2rem' }}>

            <FormGroup label="데이터 센터">
              <select
                value={dataCenterVoId}
                onChange={(e) => setDataCenterVoId(e.target.value)}
                disabled={editMode}
              >
                {isDataCentersLoading ? (
                  <option>로딩중~</option>
                ) : (
                  datacenters && datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name}: {dc.id}
                    </option>
                  ))
                )}
              </select>
            </FormGroup>

            <FormGroup label="네트워크">
              <select
                value={networkVoId}
                onChange={(e) => setNetworkVoId(e.target.value)}
                disabled={editMode}
              >
                {isNetworksLoading ? (
                  <option>loading ~~</option>
                ) : (
                  networks && networks.map((n) => (
                    <option key={n.id} value={n.id}>
                      {n.name}: {networkVoId}
                    </option>
                  ))
                )}
              </select>
            </FormGroup>

            <FormGroup label="별칭">
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
                      
            <FormGroup label="네트워크 필터">
              <select
                id="networkFilter"
                value={formState.networkFilter}
                onChange={(e) => setFormState((prev) => ({ ...prev, networkFilter: e.target.value }))}
              >
                {nFilters.map((f) => (
                  <option key={f.value} value={f.value}>
                    {f.label}
                  </option>
                ))}
              </select>
            </FormGroup>


            <div className="vnic-new-checkbox">
              <input 
                type="checkbox" 
                id="passthrough" 
                checked={formState.passthrough} 
                onChange={(e) =>
                  setFormState((prev) => ({
                    ...prev,
                    passthrough: e.target.checked,
                    migration: e.target.checked ? prev.migration : true, // 통과 체크 해제 시 migration 기본값 유지
                  }))
                }
              />
              <label htmlFor="passthrough">통과</label>
            </div>

            <div className="vnic-new-checkbox">
              <input
                type="checkbox"
                id="migration"
                checked={formState.migration}
                disabled={!formState.passthrough} // 통과 여부에 따라 활성화
                onChange={(e) =>
                  setFormState((prev) => ({ ...prev, migration: e.target.checked }))
                }
              />
              <label htmlFor="migration">마이그레이션 가능</label>
            </div>
            
            {/* 페일오버 vNIC 프로파일 */}
            {/* <div className="vnic-new-box">
              <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
              <select
                id="failover_vnic_profile"
                disabled={!formState.migration || !formState.passthrough}
              >
                <option value="none">없음</option>
                {!isFailoverNicsLoading &&
                  failoverNics.map((nic) => (
                    <option key={nic.id} value={nic.id}>
                      {nic.name}
                    </option>
                  ))}
              </select>
            </div> */}


            <div className="vnic-new-checkbox">
              <input 
                type="checkbox" 
                id="portMirroring" 
                checked={formState.portMirroring} 
                disabled={formState.passthrough} // 통과 여부에 따라 활성화
                onChange={(e) => 
                  setFormState((prev) => ({ ...prev, portMirroring: e.target.checked }))
                }
              />
              <label htmlFor="portMirroring">포트 미러링</label>
            </div>

            {/* 모든 사용자 허용 - 편집 모드가 아닌 경우에만 표시 */}
            {/* {!editMode && (
              <div className="vnic-new-checkbox">
                <input 
                  type="checkbox" 
                  id="allow_all_users" 
                  checked
                />
                <label htmlFor="allow_all_users">모든 사용자가 이 프로파일을 사용하도록 허용</label>
              </div>
            )} */}
          </div>
        </div>

        <div className="edit-footer">
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VnicProfileModal;
