import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useDeleteDataCenter,
  useDeleteCluster,
  useDeleteHost,
  useDeleteVm,
  useDeleteTemplate,
  useDeleteNetwork,
  useDeleteVnicProfile, // 네트워크 삭제 추가
  useDeleteDomain // 스토리지도메인 삭제
  // useDeleteDisk,
} from '../../api/RQHook';

const DeleteModal = ({ 
    isOpen, 
    type,
    onRequestClose, 
    contentLabel, 
    data,
    networkId // 외부에서 전달된 prop TODO 바꿔야함
    
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');

  const { mutate: deleteDataCenter } = useDeleteDataCenter();
  const { mutate: deleteCluster } = useDeleteCluster();
  const { mutate: deleteHost } = useDeleteHost();
  const { mutate: deleteVm } = useDeleteVm();
  const { mutate: deleteTemplate } = useDeleteTemplate();
  const { mutate: deleteNetwork } = useDeleteNetwork();
  const { mutate: deleteVnicProfile } = useDeleteVnicProfile();
  const { mutate: deleteDomain } = useDeleteDomain();
  
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (data) {
      setId(data.id || '');
      setName(data.name || '');
      console.log('**' + data.id);
    }
  }, [data]);

  useEffect(() => {
    console.log('Current data and Id in DeleteModal삭제데이터:', data, '아아'+id);
  }, [data, id]);

  const handleFormSubmit = () => {
    if (!id) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
      return;
    }

    if (type === 'Datacenter') {
      console.log('Deleting Data Center');
      handleDelete(deleteDataCenter);
    } else if (type === 'Cluster') {
      console.log('Deleting Cluster');
      handleDelete(deleteCluster);
    } else if (type === 'Host') {
      console.log('Deleting Host');
      handleDelete(deleteHost);
    } else if (type === 'Vm') {
      console.log('Deleting Vm');
      handleDelete(deleteVm);
    } else if (type === 'Template') {
      console.log('Deleting Template');
      handleDelete(deleteTemplate);
    } else if (type === 'Network') {
      console.log('Deleting Network');
      handleDelete(deleteNetwork);
    } else if (type === 'vnic') {
      console.log('Deleting vNIC');
      if (!networkId) {
        console.error('Network ID가 없습니다. 삭제 요청을 취소합니다.');
        return;
      }
      handleDelete(() => deleteVnicProfile({ networkId, vnicProfileId: id }));
      onRequestClose();
    } else if (type === 'Domain') {
      console.log('Deleting Domain');
      handleDelete(deleteDomain);
    }
  };

  const handleDelete = (deleteFn) => {
    deleteFn(id, {
      onSuccess: () => {
        onRequestClose(); // 삭제 성공 시 모달 닫기
        
        if (type === 'Datacenter') {
          // Datacenter 삭제 후 특정 경로로 이동
          navigate('/computing/rutil-manager/datacenters');
        } else if (type === 'Cluster') {
          // Datacenter 삭제 후 특정 경로로 이동
          navigate('/computing/rutil-manager/clusters');
        } else if (type === 'Host') {
          // Datacenter 삭제 후 특정 경로로 이동
          navigate('/computing/rutil-manager/hosts');
        } else if (type === 'Vm') {
          // Datacenter 삭제 후 특정 경로로 이동
          // navigate('/computing/rutil-manager/vms');
          navigate('/computing/vms');
        } else {
          // 다른 타입일 경우 기본 동작 수행
          const currentPath = location.pathname;
          if (currentPath.includes(id)) {
            const newPath = currentPath.replace(`/${id}`, '');
            navigate(newPath);
          } else {
            window.location.reload();
          }
        }
      },
      onError: (error) => {
        console.error(`${contentLabel} ${name} 삭제 오류:`, error);
      },
    });
  };
  

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={`${contentLabel}`}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_delete_popup">
        <div className="popup_header">
          <h1>{contentLabel} 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span> {name} 를(을) 삭제하시겠습니까? </span>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DeleteModal;

