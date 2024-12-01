import React, { useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useAddSnapshotFromVM } from '../../api/RQHook';

const VmSnapshotAddModal = ({ 
    isOpen, 
    onRequestClose,
    snapshotData,
    vmId
}) => {
    const [id, setId] = useState(''); // 스냅샷 ID
    const [description, setDescription] = useState(''); // 스냅샷 설명
    const [date, setDate] = useState(''); // 스냅샷 생성 날짜
    const [persistMemory, setPersistMemory] = useState(false); // 메모리 저장 여부
    const [status, setStatus] = useState(''); // 스냅샷 상태
    const [vmVo, setVmVo] = useState({}); // VM 정보
    const [snapshotDiskVos, setSnapshotDiskVos] = useState([]); // 스냅샷 디스크 목록
    const [nicVos, setNicVos] = useState([]); // 네트워크 인터페이스 목록
    const [applicationVos, setApplicationVos] = useState([]); // 설치된 애플리케이션 목록
 
    const { mutate: addSnapshotFromVM } = useAddSnapshotFromVM();
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
            <input type="text" id="user_name" />
          </div>
          <div>
            <div className="font-bold">포함할 디스크 :</div>
            <div className="snapshot_new_table">
              <TableOuter
                columns={TableColumnsInfo.SNAPSHOT_NEW}
                data={[]} // 디스크 데이터 삽입
                onRowClick={() => console.log('Row clicked')}
              />
            </div>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmSnapshotAddModal;
