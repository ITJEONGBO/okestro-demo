import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MHost.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle, faArrowUp, faArrowDown, faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';
import {
  useAddHost,
  useEditHost,
  useHost, 
  useAllClusters,
  useClustersFromDataCenter
} from '../../api/RQHook';

const HostModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false, 
  hId, 
  dataCenterId,
  clusterId,
}) => {
  const [id, setId] = useState('');
  const [clusterVoId, setClusterVoId] = useState('');
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [address, setAddress] = useState('');
  const [sshPort, setSshPort] = useState('');
  // 설치 후 호스트 활성화
  // 설치 후 호스트 다시 시작
  const [sshPassWord, setSshPassWord] = useState('');
  const [vgpu, setVgpu] = useState('');
  const [hostEngine, setHostEngine] = useState('');
  
  const { mutate: addHost } = useAddHost();
  const { mutate: editHost } = useEditHost();

   // 호스트 데이터 가져오기
   const {
    data: host,
    status: hostStatus,
    isRefetching: isHostRefetching,
    refetch: refetchHost,
    isError: isHostError,
    error: hostError,
    isLoading: isHostLoading
  } = useHost(hId);

  const { 
    data: clusters, 
    status: clustersStatus,
    isRefetching: isClustersRefetching,
    refetch: refetchClusters, 
    isError: isClustersError, 
    error: clustersError, 
    isLoading: isClustersLoading,
  } = useAllClusters((e) => ({
    ...e,
  }));

  // 데이터센터에서 호스트 생성시
  // const {
  //   data: dcClusters,
  //   status: dcClustersStatus,
  //   isLoading: isDcClustersLoading,
  //   isError: isDcClustersError,
  // } = useClustersFromDataCenter(dataCenterId, (e) => ({ 
  //   ...e 
  // }));

   // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && host) {
      setId(host?.id);
      setClusterVoId(host?.clusterVo?.id || '')
      setName(host?.name);
      setComment(host?.comment);
      setAddress(host?.address);
      setSshPort(host?.sshPort);
      setSshPassWord(host?.sshPassWord);
      setVgpu(host?.vgpu);
      setHostEngine(host?.hostEngine);
    } else {
      resetForm();
      if (clusters && clusters.length > 0) {
        setClusterVoId(clusters[0].id); // 첫 번째를 기본 선택
      }
    }
  }, [editMode, host, clusters]);

  const resetForm = () => {
    setName('');
    setClusterVoId('');
    setComment('');
    setAddress('');
    setSshPort('22');
    setSshPassWord('');
    setVgpu('consolidated');
    setHostEngine(false);
  };

  const handleFormSubmit = () => {
    const selectedCluster = clusters.find((c) => c.id === clusterVoId);
    if (!selectedCluster) {
      alert("클러스터를 선택해주세요.");
      return;
    }

    if(name === ''){
      alert("이름을 입력해주세요.");
      return;
    }

    if(!editMode && sshPassWord === ''){
      alert("비밀번호를 입력해주세요.");
      return;
    }

    // 데이터 객체 생성
    const dataToSubmit = {
      clusterVo: {
        id: selectedCluster.id
      },
      name,
      comment,
      address,
      sshPort,
      sshPassWord,
      vgpu,
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
      editHost({
        hostId: id,              // 전달된 id
        hostData: dataToSubmit   // 수정할 데이터
      }, {
        onSuccess: () => {
          alert("Host 편집 완료")
          onRequestClose();  // 성공 시 모달 닫기
        },
        onError: (error) => {
          console.error('Error editing Host:', error);
        }
      });
    } else {
      dataToSubmit.sshPassWord = sshPassWord;  // 생성 모드에서는 ssh 비밀번호 추가
      dataToSubmit.hostEngine = hostEngine;
      addHost(dataToSubmit, {
        onSuccess: () => {
          alert("Host 생성 완료")
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding Host:', error);
        }
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '호스트 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="host_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '호스트 편집' : '새 호스트'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="host_new_content">
          <div>
            <label htmlFor="cluster">호스트 클러스터</label>
            <select
              id="cluster"
              value={clusterVoId}
              onChange={(e) => setClusterVoId(e.target.value)}
              disabled={editMode}
            >
              {isClustersLoading ? (
                <option>로딩중~</option>
              ) : (
                clusters.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.name} ({c.id})
                  </option>
                ))
              )}
            </select>
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
            <label htmlFor="comment">코멘트</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
          </div>
          
          <div>
            <label htmlFor="address">호스트 이름/IP</label>
            <input
              type="text"
              id="address"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              disabled={editMode}
            />
          </div>

          <div>
            <label htmlFor="sshPort">SSH 포트</label>
            <input
              type="text"
              id="sshPort"
              value={sshPort}
              onChange={(e) => setSshPort(e.target.value)}
              disabled={editMode}
            />
          </div>
          <hr/>

          {/* <div>
            <input type="checkbox" id="" name="" />
            <label htmlFor="">설치 후 호스트를 활성화</label>
          </div>
          <div>
            <input type="checkbox" id="" name="" />
            <label htmlFor="">설치 후 호스트를 다시 시작</label>
          </div> */}

          {!editMode && (
            <>
            <div><label>인증</label></div>
            <div>
              <label htmlFor="userName">사용자 이름</label>
              <input
                type="text"
                id="userName"
                value="root"
                disabled
              />
            </div>
            
            <div>            
              <label htmlFor="sshPassWord">암호</label>
              <input
                type="password"
                id="sshPassWord"
                value={sshPassWord}
                onChange={(e) => setSshPassWord(e.target.value)}
              />
            </div>
            </>
          )}

          <div>
            <div>vGPU 배치</div>
            <div>
              <input 
                type="radio" 
                id="vgpu" 
                name="consolidated" 
                value="consolidated"
                checked={vgpu === 'consolidated'}
                onChange={(e) => setVgpu(e.target.value)}
              />
              <label htmlFor="consolidated">통합</label>
              <input 
                type="radio" 
                id="vgpu" 
                name="separated" 
                value="separated"
                
                checked={vgpu === 'separated'}
                onChange={(e) => setVgpu(e.target.value)}
              />
              <label htmlFor="separated">분산</label>
            </div>
          </div>

          <div>
            <label htmlFor="hostEngine">호스트 엔진 배포 작업 선택</label>
            <select
              id="hostEngine"
              value={hostEngine}
              onChange={(e) => setHostEngine(e.target.value)}
            >
              <option value="false">없음</option>
              <option value="true">배포</option>
            </select>
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

export default HostModal;