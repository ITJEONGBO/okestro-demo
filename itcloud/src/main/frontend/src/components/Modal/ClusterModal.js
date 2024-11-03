import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MCluster.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import {
  useAddCluster, 
  useEditCluster, 
  useCluster,
  useAllDataCenters, 
  useNetworksFromDataCenter
} from '../../api/RQHook';

const ClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false, 
  cId
}) => {
  const [id, setId] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [networkVoId, setNetworkVoId] = useState('');  
  const [cpuArc, setCpuArc] = useState('');
  const [cpuType, setCpuType] = useState('');
  const [cpuOptions, setCpuOptions] = useState([]);
  const [biosType, setBiosType] = useState('');
  // const [biosChange, setBiosChange] = useState('');
  const [errorHandling, setErrorHandling] = useState('');
  
  const { mutate: addCluster } = useAddCluster();
  const { mutate: editCluster } = useEditCluster();

   // 클러스터 데이터 가져오기
   const {
    data: cluster,
    status: clusterStatus,
    isRefetching: isClusterRefetching,
    refetch: refetchCluster,
    isError: isClusterError,
    error: clusterError,
    isLoading: isClusterLoading
  } = useCluster(cId);
  
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

  // 네트워크 가져오기
  const {
    data: networks,
    refetch: refetchNetworks,
    isLoading: isNetworksLoading
  } = useNetworksFromDataCenter(datacenterVoId, (e) => ({
    ...e,
  }));

  // 모달이 열릴 때 데이터 초기화
  // useEffect(() => {
  //   if (editMode && cluster) {
  //     // 데이터가 모두 로드된 경우에만 설정
  //     setId(cluster?.id);
  //     setDatacenterVoId(cluster?.datacenterVo?.id || '');
  //     setName(cluster?.name);
  //     setDescription(cluster?.description);
  //     setComment(cluster?.comment);
  //     setNetworkVoId(cluster?.networkVo?.id || '');
  //     setCpuArc(cluster?.cpuArc);
  //     setCpuType(cluster?.cpuType);
  //     setBiosType(cluster?.biosType);
  //     setErrorHandling(cluster?.errorHandling);
  //   } else {
  //     resetForm();
  //     if (datacenters && datacenters.length > 0) {
  //       setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터를 기본 선택
  //     }
  //   }
  // }, [editMode, cluster, datacenters]);

  useEffect(() => {
    if (editMode && cluster && !isClusterLoading && !isClusterRefetching) {
      // 데이터가 모두 로드된 경우에만 설정
      setId(cluster?.id);
      setDatacenterVoId(cluster?.datacenterVo?.id || '');
      setName(cluster?.name);
      setDescription(cluster?.description);
      setComment(cluster?.comment);
      setNetworkVoId(cluster?.networkVo?.id || '');
      setCpuArc(cluster?.cpuArc);
      setCpuType(cluster?.cpuType);
      setBiosType(cluster?.biosType);
      setErrorHandling(cluster?.errorHandling);
    } else if (!editMode) {
      resetForm();
      if (datacenters && datacenters.length > 0) {
        setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터를 기본 선택
      }
    }
  }, [editMode, cluster, datacenters, isClusterLoading, isClusterRefetching]);
  
  const resetForm = () => {
    setDatacenterVoId('');
    setName('');
    setDescription('');
    setComment('');
    setNetworkVoId('');
    setCpuArc('');
    setCpuType('');
    setBiosType('');
    setErrorHandling('');
  };

  // 데이터센터 선택 시 네트워크 업데이트
  // useEffect(() => {
  //   if (datacenterVoId) {
  //     setNetworkVoId(''); // 네트워크 값을 먼저 초기화
  
  //     // 데이터센터가 선택되었을 때 네트워크를 다시 가져옴
  //     refetchNetworks({ datacenterId: datacenterVoId }).then((res) => {
  //       if (res?.data && res.data.length > 0) {
  //         setNetworkVoId(res.data[0].id); // 첫 번째 네트워크를 기본값으로 설정
  //       }
  //     });
  //   } 
  // }, [datacenterVoId]);

  useEffect(() => {
    if (datacenterVoId) {
      setNetworkVoId(''); // 네트워크 값을 먼저 초기화
  
      // 데이터센터가 선택되었을 때 네트워크를 다시 가져옴
      refetchNetworks({ datacenterId: datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setNetworkVoId(res.data[0].id); // 첫 번째 네트워크를 기본값으로 설정
        }
      });
    }
  }, [datacenterVoId, refetchNetworks]);
  
  // 데이터센터 리스트가 업데이트될 때 초기값 설정
  useEffect(() => {
    if (datacenters && datacenters.length > 0) {
      if (!editMode) {
        // 데이터센터 리스트의 첫 번째 값으로 초기화
        setDatacenterVoId(datacenters[0].id);
      }
    }
  }, [datacenters, editMode]);

  useEffect(() => {
    let options = [];
    switch (cpuArc) {
      case 'X86_64':
        options = [
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
        break;
      case 'PPC64':
        options = [
          'IBM POWER8',
          'IBM POWER9'
        ];
        break;
      case 'S390X':
        options = [
          'IBM z114, z196',
          'IBM zBC12, zEC12',
          'IBM z13s, z13',
          'IBM z14'
        ];
        break;
      default:
        options = [
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
    setCpuOptions(options);
    setCpuType(''); // CPU 유형 초기화
    setBiosType('');
  }, [cpuArc]);

  // cpuArc에 따른 cpuType 변화
  useEffect(() => {
    if (editMode && cluster?.cpuType && cpuOptions.includes(cluster.cpuType)) {
      setCpuType(cluster.cpuType);
    }
  }, [cpuOptions, editMode, cluster]);

  // useEffect(() => {
  //   if (biosType) {
  //     setBiosChange(false);
  //   }
  // }, [biosType]);

  // 폼 제출 핸들러
  const handleFormSubmit = () => {
    const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
    if (!selectedDataCenter) {
      alert("데이터 센터를 선택해주세요.");
      return;
    }

    // 선택된 네트워크 찾기
    const selectedNetwork = networks.find((n) => n.id === networkVoId);
    if (!selectedNetwork) {
      alert("네트워크를 선택해주세요.");
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
      networkVo: {
        id: selectedNetwork.id,
        name: selectedNetwork.name
      },
      cpuArc,
      cpuType,
      biosType,
      errorHandling
    };
    
    if (editMode) {
      dataToSubmit.id = id;
      editCluster({
        clusterId: id,
        clusterData: dataToSubmit
      }, {
        onSuccess: () => {
          alert("클러스터 편집 완료");
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error editing cluster:', error);
        }
      });
    } else {
      addCluster(dataToSubmit, {
        onSuccess: () => {
          alert("클러스터 생성 완료");
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding cluster:', error);
        }
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
          <div className="network_form_group">
            <label htmlFor="data_center">데이터 센터</label>
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
          <span>{datacenterVoId}</span>
          <hr/>

          <div className="network_form_group">
            <label htmlFor="name">이름</label>
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
              onChange={(e) => setDescription(e.target.value)}
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

          <div className="network_form_group">
            <label htmlFor="network">관리 네트워크</label>
            <select
              id="network"
              value={networkVoId}
              onChange={(e) => setNetworkVoId(e.target.value)}
              disabled={editMode || isNetworksLoading || !datacenterVoId}
            >
              {/* <option value="" defaultValue>선택</option> */}
              {networks &&
                networks.map((n) => (
                  <option key={n.id} value={n.id}>
                    {n.name}
                  </option>
                ))}
            </select>
            <span>{networkVoId}</span>
          </div>

          <div className="network_form_group">
            <label htmlFor="cpuArc">CPU 아키텍처</label>
            <select
              id="cpuArc"
              value={cpuArc}
              onChange={(e) => setCpuArc(e.target.value)}
            >
              <option value="undefined">정의되지 않음</option>
              <option value="X86_64">x86_64</option>
              <option value="PPC64">ppc64</option>
              <option value="S390X">s390x</option>
            </select>
          </div>

          <div className="network_form_group">
            <label htmlFor="cpuType">CPU 유형</label>
            <select
              id="cpuType"
              value={cpuType}
              onChange={(e) => setCpuType(e.target.value)}
              disabled={cpuOptions.length === 0}
            >
            <option value="">선택</option>
              {cpuOptions.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </div>
          
          <div className="network_form_group">
            <label htmlFor="biosType">칩셋/펌웨어 유형</label>
            <select
              id="biosType"
              value={biosType}
              onChange={(e) => setBiosType(e.target.value)}
              disabled={cpuArc === 'PPC64' || cpuArc === 'S390X'}
            >
              <option value="CLUSTER_DEFAULT">자동 감지</option>
              <option value="I440FX_SEA_BIOS">BIOS의 I440FX 칩셋</option>
              <option value="Q35_OVMF">UEFI의 Q35 칩셋</option>
              <option value="Q35_SEA_BIOS">BIOS의 Q35 칩셋</option>
              <option value="Q35_SECURE_BOOT">UEFI SecureBoot의 Q35 칩셋</option>
            </select>
          </div>

          {/* <div className="network_checkbox_type2">
            <input 
              type="checkbox" 
              value={''}
              id="biosChange" 
              name="biosChange" 
              checked={isBiosChangeChecked}
              onChange={(e) => setIsBiosChangeChecked(e.target.checked)}
              disabled={!!biosType} // biosType이 선택된 경우 체크박스 비활성화
            />
            <label htmlFor="biosChange">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
          </div> */}

          <div className='font-bold px-1.5 py-0.5'>
            <label htmlFor="errorHandling">복구 정책</label>
            <div className='host_text_radio_box px-1.5 py-0.5'>
              <input 
                type="radio" 
                name="recovery_policy" 
                value="migrate" 
                checked={errorHandling === 'migrate'}
                onChange={(e) => setErrorHandling(e.target.value)}
              />
              <label htmlFor="migration_option">가상 머신을 마이그레이션함</label>
            </div>
            <div className='host_text_radio_box px-1.5 py-0.5'>
              <input 
                type="radio" 
                name="recovery_policy" 
                value="migrate_highly_available"
                checked={errorHandling === 'migrate_highly_available'}
                onChange={(e) => setErrorHandling(e.target.value)}
              />
              <label htmlFor="high_usage_migration_option">고가용성 가상 머신만 마이그레이션</label>
            </div>
            <div className='host_text_radio_box px-1.5 py-0.5'>
              <input 
                type="radio"
                name="recovery_policy" 
                value="do_not_migrate" 
                checked={errorHandling === 'do_not_migrate'}
                onChange={(e) => setErrorHandling(e.target.value)}
              />
              <label htmlFor="no_migration_option">가상 머신은 마이그레이션 하지 않음</label>
            </div>
          </div>

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