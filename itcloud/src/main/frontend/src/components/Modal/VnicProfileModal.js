import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { useAddVnicProfile, useAllDataCenters, useEditVnicProfile, useAllNetworks, useAllVnicProfilesFromNetwork } from '../../api/RQHook'; // 네트워크 훅 임포트

const VnicProfileModal = ({ 
  isOpen, 
  onRequestClose,
  editMode = false,
  vnicProfile,
  networkId ,
}) => {
  const [id, setId] = useState('');
  const [datacenterVoName, setDatacenterVoName] = useState(''); // 데이터 센터 이름
  const [networkVoName, setNetworkVoName] = useState('');           // 네트워크 이름
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [networkVoId, sentNetworkVoId] = useState('');  
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [passthrough, setPassthrough] = useState(vnicProfile?.passThrough !== 'DISABLED');
  const [portMirroring, setPortMirroring] = useState(vnicProfile?.portMirroring === true);
  const [migration, setMigration]  = useState('');
  const [networkFilter, setNetworkFilter] = useState(vnicProfile?.networkFilterVo?.name || '');


  const { mutate: addVnicProfile } = useAddVnicProfile();
  const { mutate: editVnicProfile } = useEditVnicProfile();


  const { 
    data: vnics 
  } = useAllVnicProfilesFromNetwork(networkId);

  const { 
    data: datacenters 
  } = useAllDataCenters((e) => ({ ...e }));
  const { 
    data: networks 
  } = useAllNetworks((n) => ({ id: n.id, name: n.name , networkFilter: n.networkFilterVo?.name })); // 네트워크 데이터 가져오기

  
  // 초기값 설정
  useEffect(() => {
    if (isOpen && editMode && vnicProfile) {

  
      setId(vnicProfile.id || '');
      setName(vnicProfile.name || '');
      setDatacenterVoName(vnicProfile.dataCenterVo || '');  // 데이터 센터 이름 설정
      setDatacenterVoId(vnicProfile.dataCenterVo || '');
      setNetworkVoName(vnicProfile.network || '');
      sentNetworkVoId(vnicProfile.network || '');
      setDescription(vnicProfile.description || '');
      setPassthrough(vnicProfile.passThrough !== 'DISABLED');
      setPortMirroring(vnicProfile.portMirroring === true);
      setMigration(vnicProfile.migration || '');
      setNetworkFilter(vnicProfile.networkFilterVo?.name || '');
      
    } else if (isOpen && !editMode && vnics && vnics.length > 0) {
      const defaultVnic = vnics[0];
      setDatacenterVoName(defaultVnic.dataCenterVo || '');
      setNetworkVoName(defaultVnic.network || '');
      resetForm();
    }else if (vnics && vnics.length > 0) {
      // 생성 모드일 때는 vnics의 첫 번째 항목으로 기본값 설정
      const defaultVnic = vnics[0];
      setDatacenterVoName(defaultVnic.dataCenterVo?.name || '');
      setNetworkVoName(defaultVnic.network?.name || '');
      resetForm();
    }
  }, [isOpen, editMode, vnicProfile, networkId,vnics]);

  const resetForm = () => {
    setName('');
    setDescription('');
    setPassthrough(false);
    setPortMirroring(false);
    setMigration(false);
    setNetworkFilter('');

  };

  const handleFormSubmit = () => {

    const newProfile = {
      name,
      networkVo: { 
        id: networkVoId,
        name: networkVoName 
      },
      description,
      migration,
 
    };

    if (isOpen &&editMode && vnicProfile) {
      editVnicProfile({ 
        dataCenterId: vnicProfile.id, 
        dataCenterData: newProfile 
      }, {
        onSuccess: () => {
          alert('vNIC 프로파일이 성공적으로 편집되었습니다.');
          onRequestClose();
        },
        onError: (error) => {
          console.error('vNIC 프로파일 편집 중 오류 발생:', error);
        }
      });
    } else {
      addVnicProfile(newProfile, {
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
            {/* 데이터 센터 */}
            <div className="vnic_new_box">
              <label htmlFor="data_center">데이터 센터</label>
              <select id="data_center" value={datacenterVoName} disabled>
                <option value={datacenterVoName}>{datacenterVoName}</option>
              </select>
            </div>
            {/* 네트워크 */}
            <div className="vnic_new_box">
            <label htmlFor="network">네트워크</label>
            <select id="network" value={networkVoName} disabled>
              <option value={networkVoName}>{networkVoName}</option>
            </select>
          </div>
            {/* 이름 */}
            <div className="vnic_new_box">
              <span>이름</span>
              <input 
                type="text" 
                value={name} 
                onChange={(e) => setName(e.target.value)} 
              />
            </div>
            {/* 설명 */}
            <div className="vnic_new_box">
              <span>설명</span>
              <input 
                type="text" 
                value={description} 
                onChange={(e) => setDescription(e.target.value)} 
              />
            </div>
            {/* 네트워크 필터 */}
            <div className="vnic_new_box">
              <label htmlFor="network_filter">네트워크 필터</label>
              <select id="network_filter" value={networkFilter} onChange={(e) => setNetworkFilter(e.target.value)}>
                <option value={networkFilter}>{networkFilter}</option>
              </select>
            </div>
            {/* 통과 */}
            <div className="vnic_new_checkbox">
              <input 
                type="checkbox" 
                id="passthrough" 
                checked={passthrough} 
                onChange={() => setPassthrough(!passthrough)} 
              />
              <label htmlFor="passthrough">통과</label>
            </div>
            {/* 마이그레이션 가능 */}
            <div className="vnic_new_checkbox">
              <input type="checkbox" id="migratable" disabled checked />
              <label htmlFor="migratable">마이그레이션 가능</label>
            </div>
            {/* 페일오버 vNIC 프로파일 */}
            <div className="vnic_new_box">
              <label htmlFor="failover_vnic_profile">페일오버 vNIC 프로파일</label>
              <select id="failover_vnic_profile" disabled>
                <option value="none">없음</option>
              </select>
            </div>
            {/* 포트 미러링 */}
            <div className="vnic_new_checkbox">
              <input 
                type="checkbox" 
                id="port_mirroring" 
                checked={portMirroring} 
                onChange={() => setPortMirroring(!portMirroring)} 
              />
              <label htmlFor="port_mirroring">포트 미러링</label>
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
