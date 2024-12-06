import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useAddSnapshotFromVM, useDisksFromVM } from '../../api/RQHook';
import TableInfo from '../table/TableInfo';

const VmSnapshotAddModal = ({ 
    isOpen, 
    onRequestClose,
    snapshotData,
    vmId,
    diskData
}) => {
    const [id, setId] = useState(''); // 스냅샷 ID
    const [alias, setAlias] = useState(''); // 스냅샷 ID
    const [description, setDescription] = useState(''); // 스냅샷 설명
    const [persistMemory, setPersistMemory] = useState(false); // 메모리 저장 여부
 
    const { mutate: addSnapshotFromVM } = useAddSnapshotFromVM();


  const {
    data: snapshot,
  } = useAddSnapshotFromVM(vmId);


    const handleFormSubmit = () => {
      // 데이터 객체 생성
      const dataToSubmit = {
        alias,
        description: description || "Default description", 
        persistMemory
      };
    
      console.log("snapshot Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그

      addSnapshotFromVM(   
        { vmId, snapshotData: dataToSubmit },
        {
        onSuccess: () => {
          alert("스냅샷 생성 완료(alert기능구현)")
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding snapshot:', error);
        }
      });
    }
    
    return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="스냅샷 생성"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="snapshot_new_popup">
        <div className="popup_header">
          <h1>스냅샷 생성</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="p-1">
          <div className="host_textbox mb-1">
            <label htmlFor="user_name">사용자 이름</label>
            <input
              type="text"
              id="user_name"
              value={alias}
              onChange={(e) => setAlias(e.target.value)} // 사용자 입력 관리
            />
          </div>
          <div>
            <div className="font-bold">포함할 디스크 :</div>
            <div className="snapshot_new_table">
              <TableOuter
                columns={TableInfo.SNAPSHOT_NEW}
                data={diskData} // 디스크 데이터 삽입
                onRowClick={() => console.log('Row clicked')}
              />
            </div>
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

export default VmSnapshotAddModal;
