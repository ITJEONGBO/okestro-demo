import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
import { useDeleteDiskFromVM, useDeleteVm, useDisksFromVM } from '../../../../api/RQHook';
import ENDPOINTS from '../../../../api/Endpoints';

const VmDeleteModal = ({ 
  isOpen, onRequestClose, data 
}) => {
  const [selectedIds, setSelectedIds] = useState([]);
  const [selectedNames, setSelectedNames] = useState([]);
  const [diskDelete, setDiskDelete] = useState(true);
  const [hasDisks, setHasDisks] = useState(false);

  const { mutate: deleteVm } = useDeleteVm();
  const { mutate: deleteVmDisk } = useDeleteDiskFromVM();
  const { data: disks = [] } = useDisksFromVM(selectedIds[0]);
  useEffect(() => {
    console.log('Modal Data:', data); // 전달된 데이터 확인
    if (Array.isArray(data)) {
      setSelectedIds(data.map((item) => item.id));
      setSelectedNames(data.map((item) => typeof item.name === 'string' ? item.name : JSON.stringify(item)));
    } else if (data) {
      setSelectedIds([data.id]);
      setSelectedNames([typeof data.name === 'string' ? data.name : JSON.stringify(data)]);
    }
  }, [data]);
  useEffect(() => {
    console.log('Modal Data:', data); // 전달된 데이터 확인
    if (Array.isArray(data)) {
      setSelectedIds(data.map((item) => item.id));
      setSelectedNames(
        data.map((item) => item.name || item.alias || '알 수 없는 이름') // 기본값 설정
      );
    } else if (data) {
      setSelectedIds([data.id]);
      setSelectedNames([data.name || data.alias || '알 수 없는 이름']); // 기본값 설정
    }
  }, [data]);
  
  useEffect(() => {
    setHasDisks(disks.length > 0);
  }, [disks]);

  const handleDelete = () => {
    if (!selectedIds.length) {
      console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
    return;
  }

    selectedIds.forEach((id, index) => {
      deleteVm(
        { vmId: id, detachOnly: diskDelete },
        {
          onSuccess: () => {
            console.log(`DELETE 요청 URL: ${ENDPOINTS.DELETE_VM(id, diskDelete)}`);
            console.log(`VM 삭제 성공: ${selectedNames[index]} (ID: ${id})`);
            if (index === selectedIds.length - 1) {
              onRequestClose();
            }
          },
          onError: (error) => {
            console.error(`VM ${selectedNames[index]} 삭제 오류:`, error);
          },
        }
      );
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vm_delete_popup" style={{ height: hasDisks ? '29.4vh' : '22.4vh' }}>
        <div className="popup_header">
          <h1>가상머신 삭제</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="disk_delete_box">
          <div>
            <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
            <span>
              {selectedNames.length > 1
                ? `${selectedNames.join(', ')} 를(을) 삭제하시겠습니까?`
                : `${selectedNames[0]} 를(을) 삭제하시겠습니까?`}
            </span>
          </div>

          {hasDisks && (
            <>
            <div className='disk_delete_checkbox'>
              <input
                type="checkbox"
                id="diskDelete"
                checked={diskDelete}
                onChange={(e) => setDiskDelete(e.target.checked)}
              />
              <label htmlFor="diskDelete">디스크 삭제</label>
            </div>
            </>
          )}
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleDelete}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmDeleteModal;

// import React, { useState, useEffect } from 'react';
// import Modal from 'react-modal';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faTimes, faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';
// import {
//   useDeleteDiskFromVM,
//   useDeleteVm,
//   useDisksFromVM,
// } from '../../api/RQHook';
// import ENDPOINTS from '../../api/Endpoints';

// const VmDeleteModal = ({ 
//     isOpen, 
//     onRequestClose, 
//     data
// }) => {
//   const [id, setId] = useState('');
//   const [name, setName] = useState('');
//   const [diskDelete, setDiskDelete] = useState(true);
//   const [hasDisks, setHasDisks] = useState(false); // 디스크 정보 유무 상태

//   const { mutate: deleteVm } = useDeleteVm();
//   const { mutate: deleteVmDisk } = useDeleteDiskFromVM();//디스크삭제

//   const { data: disks } = useDisksFromVM(data?.id); // useDisksFromVM에서 디스크 데이터를 가져옴

//   useEffect(() => {
//     if (data) {
//       setId(data.id || '');
//       setName(data.name || data.alias || '');
//       console.log(`vm: ${data.id}`);
//     }
//   }, [data]);

//   useEffect(() => {
//     console.log('diskDelete state updated:', diskDelete);
//   }, [diskDelete]);

//   // 디스크유무무
//   useEffect(() => {
//     if (disks && disks.length > 0) {
//       setHasDisks(true); // 디스크가 있으면 true 설정
//     } else {
//       setHasDisks(false); // 디스크가 없으면 false 설정
//     }
//   }, [disks]);

//   const handleDelete = () => {
//     if (!id) {
//       console.error('ID가 없습니다. 삭제 요청을 취소합니다.');
//       return;
//     }
//     console.log('Deleting VM with:', { id, diskDelete });

//     deleteVm(
//       { vmId: id, detachOnly: diskDelete },
//       {
//         onSuccess: () => {
//           console.log(`DELETE 요청 URL: ${ENDPOINTS.DELETE_VM(id, diskDelete)}`);
//           console.log(`VM ${id}, ${diskDelete}`);
//           onRequestClose();
//         },
//         onError: (error) => {
//           console.error(`${name} 삭제 오류:`, error);
//         },
//       }
//     );
//   };
  
  

//   return (
//     <Modal
//       isOpen={isOpen}
//       onRequestClose={onRequestClose}
//       className="Modal"
//       overlayClassName="Overlay"
//       shouldCloseOnOverlayClick={false}
//     >
//       <div className="storage_delete_popup">
//         <div className="popup_header">
//           <h1> 가상머신 삭제</h1>
//           <button onClick={onRequestClose}>
//             <FontAwesomeIcon icon={faTimes} fixedWidth />
//           </button>
//         </div>

//         <div className="disk_delete_box">
//           <div>
//             <FontAwesomeIcon style={{ marginRight: '0.3rem' }} icon={faExclamationTriangle} />
//             <span> {name} 를(을) 삭제하시겠습니까? </span>
            
//             {hasDisks && (
//               <>
//                 <input
//                   type="checkbox"
//                   id="diskDelete"
//                   checked={diskDelete}
//                   onChange={(e) => {
//                     console.log('Checkbox changed:', e.target.checked);
//                     setDiskDelete(e.target.checked);
//                   }}
//                 />
//                 <label htmlFor="diskDelete">디스크 삭제</label>
//               </>
//             )}
//           </div>
//         </div>

//         {/* <div>
//           <input 
//             type='checkbox'
//             id="diskDelete" 
//             checked={diskDelete}
//             onChange={(e) => setDiskDelete(e.target.value)}
//           />
//           <label htmlFor="backup">디스크 삭제</label>
//         </div> */}

//         <div className="edit_footer">
//           <button style={{ display: 'none' }}></button>
//           <button onClick={handleDelete}>OK</button>
//           <button onClick={onRequestClose}>취소</button>
//         </div>
//       </div>
//     </Modal>
//   );
// };

// export default VmDeleteModal;

