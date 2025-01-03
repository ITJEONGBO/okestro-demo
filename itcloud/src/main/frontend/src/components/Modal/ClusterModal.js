import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MCluster.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import {
  useAddCluster, 
  useEditCluster, 
  useCluster,
  useAllDataCenters, 
  useDataCenter,
  useNetworksFromDataCenter
} from '../../api/RQHook';

const FormGroup = ({ label, children }) => (
  <div className="network_form_group">
    <label>{label}</label>
    {children}
  </div>
);

const ClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false, 
  cId, 
  datacenterId
}) => {
  const [formState, setFormState] = useState({
    id: '',
    name: '',
    description: '',
    comment: '',
    cpuArc: '',
    cpuType: '',
    biosType: 'CLUSTER_DEFAULT',
    errorHandling: 'migrate',
  });
  const [dataCenterVoId, setDataCenterVoId] = useState(datacenterId || '');
  const [networkVoId, setNetworkVoId] = useState('');
  const [cpuOptions, setCpuOptions] = useState([]);

  
   // 클러스터 데이터 가져오기
   const {
    data: cluster,
    refetch: refetchCluster,
    isLoading: isClusterLoading
  } = useCluster(cId);
  
  // 데이터센터 가져오기
  const {
    data: datacenters = [],
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({...e,}));

  // 데이터센터에서 클러스터 생성시 자신의 데이터센터 아이디 넣기
  const {
    data: dataCenter,
    refetch: refetchDataCenter,
    isLoading: isDataCenterLoading,
  } = useDataCenter(datacenterId);

  // 네트워크 가져오기
  const {
    data: networks = [],
    refetch: refetchNetworks,
    isLoading: isNetworksLoading,
  } = useNetworksFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  const { mutate: addCluster } = useAddCluster();
  const { mutate: editCluster } = useEditCluster();

  
  const cpuArcs = [
    { value: "UNDEFINED", label: "정의되지 않음" },
    { value: "X86_64", label: "x86_64" },
    { value: "PPC64", label: "ppc64" },
    { value: "S390X", label: "s390x" },
  ];
  
  const biosTypeOptions = [
    { value: "CLUSTER_DEFAULT", label: "자동 감지" },
    { value: "Q35_OVMF", label: "UEFI의 Q35 칩셋" },
    { value: "I440FX_SEA_BIOS", label: "BIOS의 I440FX 칩셋" },
    { value: "Q35_SEA_BIOS", label: "BIOS의 Q35 칩셋" },
    { value: "Q35_SECURE_BOOT", label: "UEFI SecureBoot의 Q35 칩셋" },
  ];  
  
  const errorHandlingOptions = [
    { value: "migrate", label: "가상 머신을 마이그레이션함" },
    { value: "migrate_highly_available", label: "고가용성 가상 머신만 마이그레이션" },
    { value: "do_not_migrate", label: "가상 머신은 마이그레이션 하지 않음" },
  ];
  
  const cpuArcOptions = (arc) => {
    switch (arc) {
      case 'X86_64':
        return [
          'Intel Nehalem Family', 
          'Secure Intel Nehalem Family', 
          'Intel Westmere Family', 
          'Secure Intel Westmere Family',
          'Intel SandyBridge Family',
          'Secure Intel SandyBridge Family',
          'Intel IvyBridge Family',
          'Secure Intel IvyBridge Family',
          'Intel Haswell Family',
          'Secure Intel Haswell Family',
          'Intel Broadwell Family',
          'Secure Intel Broadwell Family',
          'Intel Skylake Client Family',
          'Secure Intel Skylake Client Family',
          'Intel Skylake Server Family',
          'Secure Intel Skylake Server Family',
          'Intel Cascadelake Server Family',
          'Secure Intel Cascadelake Server Family',
          'Intel Icelake Server Family',
          'Secure Intel Icelake Server Family',
          'AMD Opteron G4',
          'AMD Opteron G5',
          'AMD EPYC',
          'Secure AMD EPYC'
        ];
      case 'PPC64':
        return ['IBM POWER8', 'IBM POWER9'];
      case 'S390X':
        return [
          'IBM z114, z196',
          'IBM zBC12, zEC12',
          'IBM z13s, z13',
          'IBM z14'
        ];
      default: // UNDEFINED
        return [
          '자동 감지',
          'Intel Nehalem Family', 
          'Secure Intel Nehalem Family', 
          'Intel Westmere Family', 
          'Secure Intel Westmere Family',
          'Intel SandyBridge Family',
          'Secure Intel SandyBridge Family',
          'Intel IvyBridge Family',
          'Secure Intel IvyBridge Family',
          'Intel Haswell Family',
          'Secure Intel Haswell Family',
          'Intel Broadwell Family',
          'Secure Intel Broadwell Family',
          'Intel Skylake Client Family',
          'Secure Intel Skylake Client Family',
          'Intel Skylake Server Family',
          'Secure Intel Skylake Server Family',
          'Intel Cascadelake Server Family',
          'Secure Intel Cascadelake Server Family',
          'Intel Icelake Server Family',
          'Secure Intel Icelake Server Family',
          'AMD Opteron G4',
          'AMD Opteron G5',
          'AMD EPYC',
          'Secure AMD EPYC',
          'IBM POWER8',
          'IBM POWER9',
          'IBM z114, z196',
          'IBM zBC12, zEC12',
          'IBM z13s, z13',
          'IBM z14'
        ];
    }
  };

  
  useEffect(() => {
    if (editMode && cluster) {
      setFormState({
        id: cluster.id || '',
        name: cluster.name || '',
        description: cluster.description || '',
        comment: cluster.comment || '',
        cpuArc: cluster.cpuArc || '',
        cpuType: cluster.cpuType || '',
        biosType: cluster.biosType || '',
        errorHandling: cluster.errorHandling || 'migrate',
      });
      setDataCenterVoId(cluster?.dataCenterVo?.id || '');
      setNetworkVoId(cluster?.networkVo?.id || '');
    } else if (!editMode && !isDatacentersLoading) {
      resetForm();
    }
  }, [editMode, cluster, datacenterId]);


  useEffect(() => {
    if (!editMode && datacenters && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);
  
  useEffect(() => {
    if (!editMode && datacenterId) {
      setDataCenterVoId(datacenterId); // 초기값 설정
    }
  }, [datacenterId, editMode]);
  
  
  useEffect(() => {
    if (!editMode && networks && networks.length > 0) {
      setNetworkVoId(networks[0].id);
    }
  }, [networks, editMode]);

  useEffect(() => {
    const options = cpuArcOptions(formState.cpuArc);
    setCpuOptions(options);
    if (!editMode) {
      setFormState((prev) => ({
        ...prev,
        cpuType: '', // CPU 유형 초기화
        biosType: 'CLUSTER_DEFAULT', // BIOS 유형 초기화
      }));
    } else if (editMode && options.length > 0 && !options.includes(formState.cpuType)) {
      // 현재 CPU 유형이 새 옵션 리스트에 없으면 초기화
      setFormState((prev) => ({
        ...prev,
      }));
    }
  }, [formState.cpuArc, editMode]);
  
  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      description: '',
      comment: '',
      cpuArc: 'UNDEFINED',
      cpuType: '',
      biosType: 'CLUSTER_DEFAULT',
      errorHandling: 'migrate',
    });
    setCpuOptions([]);
    setDataCenterVoId('');
    setNetworkVoId('');
  };

  const validateForm = () => {
    if (!formState.name) return '이름을 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    if (!networkVoId) return '네트워크를 선택해주세요.';
    return null;
  };


  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }

    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);
    const selectedNetwork = networks.find((n) => n.id === networkVoId);

    const dataToSubmit = {
      dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      networkVo: { id: selectedNetwork.id, name: selectedNetwork.name },
      ...formState,
    };

    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그

    if (editMode) {
      editCluster(
        { clusterId: formState.id, clusterData: dataToSubmit },
        {
          onSuccess: () => {
            alert('클러스터 편집 완료');
            onRequestClose();
          },
        }
      );
    } else {
      addCluster(dataToSubmit, {
        onSuccess: () => {
          alert('클러스터 생성 완료');
          onRequestClose();
        },
      });
    }
  };


  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '클러스터 편집' : '생성'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="cluster_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '클러스터 편집' : '새 클러스터'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="cluster_new_content">
          <FormGroup label="데이터 센터">
            <select
              value={dataCenterVoId}
              onChange={(e) => setDataCenterVoId(e.target.value)}
              disabled={editMode}
            >
              {isDataCenterLoading ? (
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
          <hr/>

          <FormGroup label="이름">
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

          <FormGroup label="코멘트">
            <input
              type="text"
              value={formState.comment}
              onChange={(e) => setFormState((prev) => ({ ...prev, comment: e.target.value }))}
            />
          </FormGroup>

          <FormGroup label="관리 네트워크">
            <select
              value={networkVoId}
              onChange={(e) => setNetworkVoId(e.target.value)}
              disabled={editMode}
            >
              {isNetworksLoading ? (
                <option>로딩중...</option>
              ) : (
                networks.map((n) => (
                  <option key={n.id} value={n.id}>
                    {n.name} ({n.id})
                  </option>
                ))
              )}
            </select>
          </FormGroup>

          <FormGroup label="CPU 아키텍처">
            <select
              value={formState.cpuArc}
              onChange={(e) => setFormState((prev) => ({ ...prev, cpuArc: e.target.value }))}
            >
              {cpuArcs.map((arc) => (
                <option key={arc.value} value={arc.value}>
                  {arc.label}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="CPU 유형">
            <select
              value={formState.cpuType}
              onChange={(e) =>setFormState((prev) => ({ ...prev, cpuType: e.target.value }))}
            >
              <option value="">선택</option>
              {cpuOptions.map((opt) => (
                <option key={opt} value={opt}>
                  {opt}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="칩셋/펌웨어 유형">
            <select
              id="biosType"
              value={formState.biosType}
              onChange={(e) => setFormState((prev) => ({ ...prev, biosType: e.target.value }))}
              disabled={formState.cpuArc === "PPC64" || formState.cpuArc === "S390X"}
            >
              {biosTypeOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="복구 정책">
            {errorHandlingOptions.map((option) => (
              <div key={option.value} className="host_text_radio_box px-1.5 py-0.5">
                <input
                  type="radio"
                  name="recovery_policy"
                  value={option.value}
                  checked={formState.errorHandling === option.value}
                  onChange={(e) =>
                    setFormState((prev) => ({ ...prev, errorHandling: e.target.value }))
                  }
                />
                <label htmlFor={option.value}>{option.label}</label>
              </div>
            ))}
          </FormGroup>

        </div>

        <div className="edit_footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default ClusterModal;