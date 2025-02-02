import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import '../css/MHost.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import {
  useAddHost,
  useEditHost,
  useHost, 
  useAllClusters,
} from '../../../../api/RQHook';
import toast from 'react-hot-toast';
import LabelSelectOptionsID from '../../../../utils/LabelSelectOptionsID';
import LabelInput from '../../../../utils/LabelInput';
import LabelInputNum from '../../../../utils/LabelInputNum';

const HostModal = ({ isOpen, editMode = false, hId, clusterId, onClose }) => {
  const [formState, setFormState] = useState({
    id: '',
    name: '',
    comment: '',
    address: '',
    sshPort: '',
    sshPassWord: '',
    vgpu: '',
    hostedEngine: false,
    // 설치 후 호스트 활성화
    // 설치 후 호스트 다시시작
  });
  const [clusterVoId, setClusterVoId] = useState(clusterId || '');

  const { mutate: addHost } = useAddHost();
  const { mutate: editHost } = useEditHost();

   // 호스트 데이터 가져오기
   const {
    data: host,
    refetch: refetchHost,
    error: hostError,
    isLoading: isHostLoading
  } = useHost(hId);

  const { 
    data: clusters, 
    refetch: refetchClusters, 
    error: clustersError, 
    isLoading: isClustersLoading,
  } = useAllClusters((e) => ({...e,}));

   useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
    }
  }, [isOpen]);

   // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (editMode && host) {
      console.log('hostModal', host);
      setFormState({
        id:host?.id || '',
        name: host?.name || '',
        comment: host?.comment || '',
        address: host?.address || '',
        sshPort: host?.sshPort || '', 
        sshPassWord: host?.sshPassWord || '',
        vgpu: host?.vgpu || '',
        hostedEngine: host?.hostedEngine || false,
      });
      setClusterVoId(host?.clusterVo?.id || '');   
    } else if (!editMode && !isClustersLoading) {
      resetForm();
    }
  }, [editMode, host, isClustersLoading]);
  
  const resetForm = () => {
    setFormState({
      id: '',
      name: '',
      comment: '',
      address: '',
      sshPort: '22',
      sshPassWord: '',
      vgpu: 'consolidated',
      hostedEngine: false,
    });
    setClusterVoId('');
  };

  useEffect(() => {
    if (!editMode && clusters && clusters.length > 0) {
      setClusterVoId(clusters[0].id);
    }
  }, [clusters, editMode]);

  useEffect(() => {
    if (!editMode && clusterId) {
      setClusterVoId(clusterId); // 초기값 설정
    }
  }, [clusterId, editMode]);


  const validateForm = () => {
    if (!formState.name) return '이름을 입력해주세요.';
    if (!editMode && !formState.sshPassWord) return '비밀번호를 입력해주세요.';
    if (!clusterVoId) return '클러스터를 선택해주세요.';
    return null;
  };

  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      toast.error(error);
      return;
    }

    const selectedCluster = clusters.find((c) => c.id === clusterVoId);
    
    // 데이터 객체 생성
    const dataToSubmit = {
      clusterVo: { id: selectedCluster.id },
      ...formState,
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.id = formState.id;  // 수정 모드에서는 id를 추가
      editHost(
        { hostId: formState.id, hostData: dataToSubmit }, 
        {
          onSuccess: () => {
            onClose();  // 성공 시 모달 닫기
            toast.success("Host 편집 완료")
          },
          onError: (error) => {
            toast.error('Error editing Host:', error);
          }
        }
      );
    } else {
      dataToSubmit.sshPassWord = formState.sshPassWord;  // 생성 모드에서는 ssh 비밀번호 추가
      dataToSubmit.hostedEngine = formState.hostedEngine;
      addHost(dataToSubmit, {
        onSuccess: () => {
          onClose();
          toast.success("Host 생성 완료")
        },
        onError: (error) => {
          toast.error('Error adding Host:', error);
        }
      });
    }
  };


  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? '호스트 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="host-new-popup">
        <div className="popup-header">
          <h1>{editMode ? '호스트 편집' : '새 호스트'}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="host-new-content">
          <LabelSelectOptionsID
            label="호스트 클러스터"
            value={clusterVoId}
            onChange={(e) => setClusterVoId(e.target.value)}
            disabled={editMode}
            loading={isClustersLoading}
            options={clusters}
          />
          <hr/>

          <LabelInput
            label="이름"
            id="name"
            value={formState.name}
            autoFocus={true}
            onChange={(e) => setFormState((prev) => ({ ...prev, name: e.target.value }))}
          />
          <LabelInput
            label="코멘트"
            id="comment"
            value={formState.comment}
            onChange={(e) => setFormState((prev) => ({ ...prev, comment: e.target.value }))}
          />
          <LabelInput
            label="호스트 이름/IP"
            id="address"
            value={formState.address}
            onChange={(e) => setFormState((prev) => ({ ...prev, address: e.target.value }))}
            disabled={editMode}
          />
          <LabelInputNum
            label="SSH 포트"
            id="sshPort"
            value={formState.sshPort}
            onChange={(e) => setFormState((prev) => ({ ...prev, sshPort: e.target.value }))}
            disabled={editMode}
          />
          <hr/>

        {!editMode && (
          <>
            <div><label>인증</label></div>

            <LabelInput
              label="사용자 이름"
              id="userName"
              value="root"
              disabled={true}
            />
                        
            <div>            
              <label htmlFor="sshPassWord">암호</label>
              <input
                type="password"
                id="sshPassWord"
                value={formState.sshPassWord}
                onChange={(e) => setFormState((prev) => ({ ...prev, sshPassWord: e.target.value }))}
              />
            </div>
          </>
        )}

          <div>
            <div>vGPU 배치</div>
            <div className='flex'>
              <input 
                type="radio" 
                id="vgpu" 
                name="consolidated" 
                value="consolidated"
                checked={formState.vgpu === 'consolidated'}
                onChange={(e) => setFormState((prev) => ({ ...prev, vgpu: e.target.value }))}
              />
              <label htmlFor="consolidated" style={{marginRight:'0.2rem'}}>통합</label>
              <input 
                type="radio" 
                id="vgpu" 
                name="separated" 
                value="separated"
                
                checked={formState.vgpu === 'separated'}
                onChange={(e) => setFormState((prev) => ({ ...prev, vgpu: e.target.value }))}
              />
              <label htmlFor="separated">분산</label>
            </div>
          </div>

          <div>
            <label htmlFor="hostEngine">호스트 엔진 배포 작업 선택</label>
            <select
              id="hostEngine"
              value={formState.hostedEngine}
              onChange={(e) =>
                setFormState((prev) => ({
                  ...prev,
                  hostedEngine: e.target.value === "true", // 문자열을 boolean으로 변환
                }))
              }
            >
              <option value="false">없음</option>
              <option value="true">배포</option>
            </select>
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

export default HostModal;