import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import {
  useAddCluster, 
  useEditCluster, 
  useAllDataCenters, 
  useNetworksFromDataCenter
} from '../../api/RQHook'

const ClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false, 
  data = {} 
}) => {
  console.log("ClusterModal ")
  const [id, setId] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [networkVoId, setNetworkVoId] = useState('');  
  const [cpuArc, setCpuArc] = useState('');
  const [cpuType, setCpuType] = useState('');
  const [biosType, setBiosType] = useState('');
  const [errorHandling, setErrorHandling] = useState('');
  
  const { mutate: addCluster } = useAddCluster();
  const { mutate: editCluster } = useEditCluster();

  // 데이터센터
  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => {
      return {
          ...e,
      }
  });

  // 네트워크
  const {
    data: networks,
    status: networksStatus,
    isRefetching: isNetworksRefetching,
    refetch: refetchNetworks,
    isError: isNetworksError,
    error: networksError,
    isLoading: isNetworksLoading
  } = useNetworksFromDataCenter(datacenterVoId, (e) => {
    return {
        ...e,
    }
});


  // 편집 모드일 때 기존 데이터를 불러와서 입력 필드에 채움
  useEffect(() => {
    if (editMode && data) {
      setId(data.id);
      setDatacenterVoId(data.datacenterVo?.id);
      setName(data.name);
      setDescription(data.description);
      setComment(data.comment);
      setNetworkVoId(data.networkVo?.id);
      setCpuArc(data.cpuArc);
      setCpuType(data.cpuType);
      setBiosType(data.biosType);
      setErrorHandling(data.errorHandling);
    } else{
      resetForm();
    }
  }, [editMode, data]);
  

  const resetForm = () => {
    setDatacenterVoId('')
    setName('');
    setDescription('');
    setComment('');
    setNetworkVoId('');
    setCpuArc('undefined');
    setCpuType('default');
    setBiosType('cluster_default');
    setErrorHandling('migrate');
  };

  useEffect(() => {
    if (datacenterVoId) {
      refetchNetworks();
    }
  }, [datacenterVoId, refetchNetworks]);

  // 폼 제출 핸들러
  const handleFormSubmit = () => {
    const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
    if (!selectedDataCenter) {
      alert("데이터 센터를 선택해주세요."); // Prompt user to select a data center
      return;
    }

    const dataToSubmit = {
      // datacenterVoId,
      datacenterVo: {
        id: selectedDataCenter.id,
        name: selectedDataCenter.name,
      },
      name,
      description,
      comment,
      networkVo: {
        id: networkVoId,
      },
      cpuArc,
      cpuType,
      // cpu: {
      //   architecture: cpuArc,
      //   type: cpuType,
      // },
      biosType,
      errorHandling
    };
    console.log("Form Data: ", dataToSubmit);

    if (editMode) {
      dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
      editCluster({
        clusterId: id,              // 전달된 id
        clusterData: dataToSubmit   // 수정할 데이터
      }, {
        onSuccess: () => {
          alert("클러스터 편집 완료(alert기능구현)")
          onRequestClose();  // 성공 시 모달 닫기
        },
        onError: (error) => {
          console.error('Error editing cluster:', error);
        }
      });
    } else {
      addCluster(dataToSubmit, {
        onSuccess: () => {
          alert("클러스터 생성 완료(alert기능구현)")
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

        <div className="network_new_nav">
          <div>
            <input type="hidden" id="id" value={id} onChange={() => {}} /> {/* id는 읽기 전용이므로 onChange를 추가하지 않음 */}
          </div>

          <div className="datacenter_new_content">
          <label htmlFor="data_center">데이터 센터</label>
            <select
              id="data_center"
              value={datacenterVoId}
              onChange={(e) => setDatacenterVoId(e.target.value)}
              disabled={isDatacentersLoading}
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
          {/* 선 */}
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
              <option value="">선택</option>
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
            >
              <option value="default">Default</option>
              <option value="Intel Nehalem Family">Intel Nehalem Family</option>
              {/* TODO CPU 유형 목록이 들어가야함 */}
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
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default ClusterModal;