import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { 
  useAddVnicProfile, 
  useAllDataCenters, 
  useEditVnicProfile, 
  useNetworksFromDataCenter, 
  useAllVnicProfilesFromNetwork 
} from '../../api/RQHook'; // 네트워크 훅 임포트

const FormGroup = ({ label, children }) => (
  <div className="vnic_new_box">
    <label>{label}</label>
    {children}
  </div>
);

const VnicProfileModal = ({ 
  // 편집기능은 되지만 테이블 행이제일첫번째로 올라감
  isOpen, 
  onRequestClose,
  editMode = false,
  vnicProfile,
  networkId ,
}) => {
  const [formState, setFormState] = useState({
    id: '',
    name: '',
    description: '',
    // passthrough: '',
    portMirroring: false,
    // migration: '',
    // networkFilter: 'vdsm-no-mac-spoofing'
  })
  // const [id, setId] = useState('');
  // const [name, setName] = useState('');
  // const [description, setDescription] = useState('');
  // const [passthrough, setPassthrough] = useState(vnicProfile?.passThrough !== 'DISABLED');
  // const [portMirroring, setPortMirroring] = useState(vnicProfile?.portMirroring === true);
  // const [migration, setMigration]  = useState('');

  // const [networkFilter, setNetworkFilter] = useState('');
  const [dataCenterVoId, setDataCenterVoId] = useState('');  
  const [networkVoId, setNetworkVoId] = useState('');  

  const { mutate: addVnicProfile } = useAddVnicProfile();
  const { mutate: editVnicProfile } = useEditVnicProfile();

  const nFilters = [ 
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

  const {
    data: datacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({...e,}));

  const {
    data: networks = [],
    isLoading: isNetworksLoading
  } = useNetworksFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  const { 
    data: vnics 
  } = useAllVnicProfilesFromNetwork(networkId);
  
  
  // 초기값 설정
  useEffect(() => {
    if (editMode && vnicProfile) {
      setFormState({
        id: vnicProfile.id || '',
        name: vnicProfile.name || '',
        description: vnicProfile.description || '',
        passthrough: vnicProfile.passThrough !== 'DISABLED',
        migration: vnicProfile.migration || '', 
        networkFilter: vnicProfile?.networkFilterVo?.id || ''
      });
      setDataCenterVoId(vnicProfile.dataCenterVo.id || '');
      setNetworkVoId(vnicProfile.networkVo.id || '');        
    } else if (!editMode ) {        
      resetForm();
    }
  }, [editMode, vnicProfile]);

  useEffect(() => {
    if (!editMode && datacenters && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);

  useEffect(() => {
    if (!editMode && networks && networks.length > 0) {
      setNetworkVoId(networks[0].id);
    }
  }, [networks, editMode]);

  // useEffect(() => {
  //   if (!editMode && nFilters && nFilters.length > 0) {
  //     setFormState((prev) => ({...prev, networkFilter: nFilters[0].value}));
  //   }
  // }, [nFilters, editMode]);
  

  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      description: '',
      // passthrough: '',
      portMirroring: '',
      // migration: '',
      // networkFilter: ''
    })
    setDataCenterVoId('');
    setNetworkVoId('');
  };

  const handleFormSubmit = () => {

  const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);
  const selectedNetwork = networks.find((n) => n.id === networkVoId);    

    const dataToSubmit = {
      networkVo: { id: selectedNetwork.id,},
      ...formState,      
      // migration : false, 
    };
    console.log('dataToSubmit:', dataToSubmit); 

    if (editMode) {
      dataToSubmit.id = formState.id;
      editVnicProfile(
        { 
          networkId: networkVoId,
          vnicId: formState.id, 
          vnicData: dataToSubmit 
        }, 
      {
        onSuccess: () => {
          alert('vNIC 프로파일이 성공적으로 편집되었습니다.');
          onRequestClose();
        },
        onError: (error) => {
          console.error('vNIC 프로파일 편집 중 오류 발생:', error);
        }
      });
    } else {
      addVnicProfile(
        { networkId: networkVoId,
          vnicData: dataToSubmit
        }, 
        {
          onSuccess: () => {
            alert('vNIC 프로파일이 성공적으로 추가되었습니다.');
            onRequestClose();
        },
          onError: (error) => {
            console.error('vNIC 프로파일 추가 중 오류 발생:', error);
        }
      });
    } 
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? 'vNIC 프로파일 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vnic_new_content_popup">
        <div className="popup_header">
          <h1>{editMode ? '가상 머신 인터페이스 프로파일 편집' : '가상 머신 인터페이스 프로파일'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="vnic_new_content">
          <div className="vnic_new_contents" style={{ paddingTop: '0.2rem' }}>

            <FormGroup label="데이터 센터">
              {isDatacentersLoading ? (
                  <p>데이터 센터를 불러오는 중...</p>
                ) : datacenters.length === 0 ? (
                  <p>사용 가능한 데이터 센터가 없습니다.</p>
                ) : (
                <select
                  value={dataCenterVoId}
                  onChange={(e) => setDataCenterVoId(e.target.value)}
                  disabled={editMode}
                >
                  {datacenters && datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name}: {dataCenterVoId}
                    </option>
                  ))}
                </select>
              )}
            </FormGroup>

            <FormGroup label="네트워크">
              {isNetworksLoading ? (
                  <p>네트워크를 불러오는 중...</p>
                ) : networks.length === 0 ? (
                  <p>사용 가능한 네트워크가 없습니다.</p>
                ) : (
                <select
                  value={networkVoId}
                  onChange={(e) => setNetworkVoId(e.target.value)}
                  disabled={editMode}
                >
                  {networks && networks.map((n) => (
                    <option key={n.id} value={n.id}>
                      {n.name}: {networkVoId}
                    </option>
                  ))}
                </select>
              )}
            </FormGroup>

            <FormGroup label="별칭">
              <input
                type="text"
                value={formState.name}
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
                      
            {/* <FormGroup label="네트워크 필터">
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
            </FormGroup> */}


            {/* <div className="vnic_new_checkbox">
              <input 
                type="checkbox" 
                id="passthrough" 
                checked={formState.passthrough} 
                onChange={(e) => setFormState((prev) => ({ ...prev, passthrough: e.target.checked }))}
              />
              <label htmlFor="passthrough">통과</label>
            </div>

            <div className="vnic_new_checkbox">
              <input 
                type="checkbox" 
                id="migratable" 
                checked={formState.migratable} 
                onChange={(e) => setFormState((prev) => ({ ...prev, migratable: e.target.checked }))}
              />
              <label htmlFor="passthrough">마이그레이션 가능</label>
            </div> */}
            
            {/* 페일오버 vNIC 프로파일 */}
            {/* <div className="vnic_new_box">
              <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
              <select id="failover_vnic_profile" disabled>
                <option value="none">없음</option>
              </select>
            </div> */}


            <div className="vnic_new_checkbox">
              <input 
                type="checkbox" 
                id="portMirroring" 
                checked={formState.portMirroring} 
                onChange={(e) => setFormState((prev) => ({ ...prev, portMirroring: e.target.checked }))}
              />
              <label htmlFor="portMirroring">포트 미러링</label>
            </div>

            {/* 모든 사용자 허용 - 편집 모드가 아닌 경우에만 표시 */}
            {/* {!editMode && (
              <div className="vnic_new_checkbox">
                <input 
                  type="checkbox" 
                  id="allow_all_users" 
                  checked={allowAllUsers} 
                  onChange={() => setAllowAllUsers(!allowAllUsers)} 
                />
                <label htmlFor="allow_all_users">모든 사용자가 이 프로파일을 사용하도록 허용</label>
              </div>
            )} */}
          </div>
        </div>

        <div className="edit_footer">
          <button onClick={onRequestClose}>취소</button>
          <button onClick={handleFormSubmit}>OK</button>
        </div>
      </div>
    </Modal>
  );
};

export default VnicProfileModal;
