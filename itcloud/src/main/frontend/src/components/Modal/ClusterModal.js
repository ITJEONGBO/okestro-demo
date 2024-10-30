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
    isLoading: isClusterrLoading
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
  useEffect(() => {
    if (editMode) {
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
    } else {
      resetForm();
      if (datacenters && datacenters.length > 0) {
        setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터를 기본 선택
      }
    }
  }, [editMode, cluster, datacenters]);

  // 데이터센터 선택 시 네트워크 업데이트
  useEffect(() => {
    if (datacenterVoId) {
      setNetworkVoId(''); // 네트워크 값을 먼저 초기화
  
      // 데이터센터가 선택되었을 때 네트워크를 다시 가져옴
      refetchNetworks({ datacenterId: datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setNetworkVoId(res.data[0].id); // 첫 번째 네트워크를 기본값으로 설정
        }
      });
    } else {
      setNetworkVoId(''); // 데이터센터 선택이 취소되었을 경우 네트워크 값 초기화
    }
  }, [datacenterVoId]);
  
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
    if (cpuArc === 'x86_64') {
      setCpuOptions([
        'Intel Nehalem Family', 
        'b', 
        'c'
      ]);
      setCpuType(''); // CPU 유형 초기화
    } else if (cpuArc === 'ppc64') {
      setCpuOptions([
        'd', 
        'g', 
        'h'
      ]);
      setCpuType(''); // CPU 유형 초기화
    } else if(cpuArc === 's390x') {
      setCpuOptions([]);
      setCpuType(''); // CPU 유형 초기화
    }
  }, [cpuArc]);

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

  // 폼 제출 핸들러
  const handleFormSubmit = () => {
    const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
    if (!selectedDataCenter) {
      alert("데이터 센터를 선택해주세요.");
      return;
    }
    const selectedNetwork = networks.find((n) => n.id === networkVoId);
    if (!selectedNetwork) {
      alert("네트워크를 선택해주세요.");
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
          <div>
            <label htmlFor="data_center">데이터 센터</label>
            <select
              id="data_center"
              value={datacenterVoId}
              onChange={(e) => setDatacenterVoId(e.target.value)}
            >
              <option value="">선택</option>
              {datacenters &&
                datacenters.map((dc) => (
                  <option key={dc.id} value={dc.id}>
                    {dc.name}
                  </option>
                ))}
            </select>
            <span>{datacenterVoId}</span>
          </div>
          <hr/>

          <div>
            <label htmlFor="name">이름</label>
              <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
          </div>

          <div>
            <label htmlFor="description">설명</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="comment">코멘트</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
          </div>

          <div>
            <label htmlFor="network">관리 네트워크</label>
            <select
              id="network"
              value={networkVoId}
              onChange={(e) => setNetworkVoId(e.target.value)}
              disabled={isNetworksLoading || !datacenterVoId}
            >
              {/* <option value="">선택</option> */}
              {networks &&
                networks.map((n) => (
                  <option key={n.id} value={n.id}>
                    {n.name}
                  </option>
                ))}
            </select>
            <span>{networkVoId}</span>
          </div>
          <div>
            <label htmlFor="cpuArc">CPU 아키텍처</label>
            <select
              id="cpuArc"
              value={cpuArc}
              onChange={(e) => setCpuArc(e.target.value)}
            >
              <option value="undefined">undefined</option>
              <option value="x86_64">x86_64</option>
              <option value="ppc64">ppc64</option>
              <option value="s390x">s390x</option>
            </select>
          </div>

          <div>
            <label htmlFor="cpuType">CPU 유형</label>
            <select
              id="cpuType"
              value={cpuType}
              onChange={(e) => setCpuType(e.target.value)}
              disabled={cpuOptions.length === 0}
            >
              {/* <option value="">선택</option> */}
              {cpuOptions.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </div>
          
          <div>
            <label htmlFor="biosType">칩셋/펌웨어 유형</label>
            <select
              id="biosType"
              value={biosType}
              onChange={(e) => setBiosType(e.target.value)}
            >
              <option value="cluster_default">cluster_default</option>
              <option value="i440fx_sea_bios">i440fx_sea_bios</option>
              <option value="q35_ovmf">q35_ovmf</option>
              <option value="q35_sea_bios">q35_sea_bios</option>
              <option value="q35_secure_boot">q35_secure_boot</option>
            </select>
          </div>

          {/* <div>
            <input type='checkbox' >BIOS를 사용하여...</input>
          </div>

          <div>
            <input type='radio' >가상머신을 </input>
            <input type='radio' >고가용성 가상머신만 </input>
            <input type='radio' >가상머신은 </input>
          </div> */}
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
