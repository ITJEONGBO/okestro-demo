// import React, { useState, useEffect } from 'react';
// import Modal from 'react-modal';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
// import { useNavigate, useLocation } from 'react-router-dom';
// import {
//   useDeleteDataCenter,
//   useDeleteCluster,
//   useDeleteHost,
//   // useDeleteVm,
//   // useDeleteTemplate,
//   // useDeleteStorageDomain,
//   // useDeleteDisk,
//   useDeleteNetwork,
//   useDeleteVnicProfile, // 네트워크 삭제 추가
// } from '../../api/RQHook';

// const DeleteModal = ({ 
//     isOpen, 
//     type,
//     onRequestClose, 
//     contentLabel, 
//     data 
// }) => {
//   const [id, setId] = useState('');
//   const [name, setName] = useState('');
//   const [networkId, setNetworkId] = useState('');

//   const { mutate: deleteDataCenter } = useDeleteDataCenter();
//   const { mutate: deleteCluster } = useDeleteCluster();
//   const { mutate: deleteHost } = useDeleteHost();

//   // const { mutate: deleteVm } = useDeleteVm();
//   // const { mutate: deleteTemplate } = useDeleteTemplate();
//   // const { mutate: deleteStorageDomain } = useDeleteStorageDomain();
//   // const { mutate: deleteDisk } = useDeleteDisk();
//   const { mutate: deleteNetwork } = useDeleteNetwork(); // 네트워크 삭제 추가
//   const { mutate: deleteVnicProfile } = useDeleteVnicProfile();
//   const navigate = useNavigate();
//   const location = useLocation();
//   useEffect(() => {
//     if (data) {
//       setId(data.id || '');
//       setName(data.name || '');
//       console.log('**'+data.id);
//     }
//   }, [data]);

//   const handleFormSubmit = () => {
//     if (!id) {
//       console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
//       return;
//     }

//     if (type === 'Datacenter') {
//       console.log('Deleting Data Center');
//       handleDelete(deleteDataCenter);
//     } else if (type === 'Cluster') {
//       console.log('Deleting Cluster');
//       handleDelete(deleteCluster);
//     } else if (type === 'Host') {
//       console.log('Deleting Host');
//       handleDelete(deleteHost);
//     // } else if (type === 'Vm') {
//     //   console.log('Deleting Vm');
//     //   handleDelete(deleteVm);
//     // } else if (type === 'Template') {
//     //   console.log('Deleting Template');
//     //   handleDelete(deleteTemplate);
//     // } else if (type === 'StorageDoamin') {
//     //   console.log('Deleting StorageDoamin');
//     //   handleDelete(deleteStorageDoamin);
//     // } else if (type === 'Disk') {
//     //   console.log('Deleting Disk');
//     //   handleDelete(deleteDisk);
//     } else if (type === 'Network') { // Network 삭제 처리 추가
//       console.log('Deleting Network');
//       handleDelete(deleteNetwork);
//     } else if (type === 'vnic') {
//       console.log('Deleting vNIC');
//       if (!networkId) {
//         console.error('Network ID가 없습니다. 삭제 요청을 취소합니다.');
//         return;
//       }
//       handleDelete(() => deleteVnicProfile({ networkId, vnicProfileId: id }));
//   };
// }

//   // 삭제한 후 동시에 그전url로돌아가기
//   const handleDelete = (deleteFn) => {
//     deleteFn(id, {
//       onSuccess: () => {
//         onRequestClose(); // Close the modal after deletion
  
//         // Extract the current path and check for the presence of the ID
//         const currentPath = location.pathname;
//         if (currentPath.includes(id)) {
//           // Remove the ID from the path and navigate back
//           const newPath = currentPath.replace(`/${id}`, '');
//           navigate(newPath);
//         } else {
//           // If the ID is not in the URL, refresh the page
//           window.location.reload();
//         }
//       },
//       onError: (error) => {
//         console.error(`${contentLabel} ${name} 삭제 오류:`, error);
//       },
//     });
//   };

//   return (
//     <Modal
//       isOpen={isOpen}
//       onRequestClose={onRequestClose}
//       contentLabel={`${contentLabel}`}
//       className="Modal"
//       overlayClassName="Overlay"
//       shouldCloseOnOverlayClick={false}
   
//     >
//       <div className="storage_delete_popup">
//         <div className="popup_header">
//           <h1>{contentLabel} 삭제</h1>
//           <button onClick={onRequestClose}>
//             <FontAwesomeIcon icon={faTimes} fixedWidth />
//           </button>
//         </div>

//         <div className="disk_delete_box">
//           <div>
//             <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
//             <span>{contentLabel} {name} 를(을) 삭제하시겠습니까?</span>
//           </div>
//         </div>

//         <div className="edit_footer">
//           <button style={{ display: 'none' }}></button>
//           <button onClick={handleFormSubmit}>OK</button>
//           <button onClick={onRequestClose}>취소</button>
//         </div>
//       </div>
//     </Modal>
//   );
// };

// export default DeleteModal;import React, { useState, useEffect } from 'react';
import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  useDeleteDataCenter,
  useDeleteCluster,
  useDeleteHost,
  // useDeleteVm,
  // useDeleteTemplate,
  // useDeleteStorageDomain,
  // useDeleteDisk,
  useDeleteNetwork,
  useDeleteVnicProfile, // 네트워크 삭제 추가
  useDeleteDomain // 스토리지도메인 삭제
} from '../../api/RQHook';

const DeleteModal = ({ 
    isOpen, 
    type,
    onRequestClose, 
    contentLabel, 
    data,
    networkId // 외부에서 전달된 prop
}) => {
  const [id, setId] = useState('');
  const [name, setName] = useState('');

  const { mutate: deleteDataCenter } = useDeleteDataCenter();
  const { mutate: deleteCluster } = useDeleteCluster();
  const { mutate: deleteHost } = useDeleteHost();
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
    console.log('Current data and networkId in DeleteModal:', data, networkId);
  }, [data, networkId]);

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
            <span> {name} 를(을) 삭제하시겠습니까?</span>
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

