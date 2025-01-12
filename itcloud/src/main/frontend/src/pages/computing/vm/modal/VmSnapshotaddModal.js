import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import TablesOuter from '../../../../components/table/TablesOuter';
import TableColumnsInfo from '../../../../components/table/TableColumnsInfo';
import { 
  useAddSnapshotFromVM, 
  useDisksFromVM 
} from '../../../../api/RQHook';
import '../css/MVm.css';
const VmSnapshotaddModal = ({ 
    isOpen, 
    onRequestClose,
    snapshotData,
    vmId,
}) => {
    const [id, setId] = useState(''); // 스냅샷 ID
    const [alias, setAlias] = useState(''); // 스냅샷 ID
    const [description, setDescription] = useState(''); // 스냅샷 설명
    const [persistMemory, setPersistMemory] = useState(false); // 메모리 저장 여부
 
    const { mutate: addSnapshotFromVM } = useAddSnapshotFromVM();

    const [bootable, setBootable] = useState(true);
    const { data: disks } = useDisksFromVM(vmId, (e) => ({
      ...e,
      snapshot_check: (
        <input
          type="checkbox"
          name="diskSelection"
          onChange={(e) => setBootable(e.target.checked)} 
        />
      ),
      alias: e?.diskImageVo?.alias,
      description: e?.diskImageVo?.description,
    }));

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
        <div className="popup-header">
          <h1>스냅샷 생성</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="p-1">
          <div className="host_textbox mb-1">
            <label htmlFor="description">설명</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)} // 사용자 입력 관리
            />
          </div>
          <div>
            <div className="font-bold">포함할 디스크 :</div>
            <div className="snapshot_new_table">
              <TablesOuter
                columns={TableColumnsInfo.SNAPSHOT_NEW}
                data={disks} // 디스크 데이터 삽입
                onRowClick={() => console.log('Row clicked')}
              />
            </div>
          </div>
        </div>

        <div className="edit-footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmSnapshotaddModal;
