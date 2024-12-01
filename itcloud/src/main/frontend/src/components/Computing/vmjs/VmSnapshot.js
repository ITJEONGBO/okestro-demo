import { faCamera, faChevronRight, faExclamationTriangle, faEye, faNewspaper, faServer, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useSnapshotFromVM } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import VmSnapshotAddModal from '../../Modal/VmSnapshotaddModal';

// 스냅샷(각각 밑에 열리는 창 만들어야함)
const VmSnapshot = ({vm}) => {
  const [activePopup, setActivePopup] = useState(null);
  const [selectedSnapshot, setSelectedSnapshot] = useState(null);


  const openPopup = (popupType) => {
      setActivePopup(popupType);
    };
  const closePopup = () => {
      setActivePopup(null);
  };

  const [activeSection, setActiveSection] = useState(null); // 단일 섹션 상태 관리
  const toggleSection = (section) => {
    setActiveSection(prev => prev === section ? null : section); // 클릭 시 해당 섹션만 열림
  };
    const { 
    data: snapshots, 
    status: snapshotsStatus, 
    isLoading: isSnapshotsLoading, 
    isError: isSnapshotsError 
  } = useSnapshotFromVM(vm?.id, toTableItemPredicateSnapshots);  
  
  function toTableItemPredicateSnapshots(snapshot) {
    return {
      id: snapshot?.id ?? '', 
      vmId: snapshot?.vm?.id ?? '',  
      name: snapshot?.description ?? '', 
      status: snapshot?.status ?? '',  
      fqdn: snapshot?.fqdn ?? '',  
      created: snapshot?.creationDate ?? 'N/A', 
      vmStatus: snapshot?.vm?.status ?? 'N/A', 
      memorySize: snapshot?.memorySize ?? 'N/A', 
      diskSize: snapshot?.diskSize ?? 'N/A', 
      actualSize: snapshot?.snapshotDiskVos?.[0]?.actualSize
      ? `${(snapshot.snapshotDiskVos[0].actualSize / (1024 ** 3)).toFixed(2)} GB`
      : 'N/A', // 크기
    creationDate: snapshot?.date ?? 'N/A', // 생성 일자
    snapshotCreationDate: snapshot?.snapshotDiskVos?.[0]?.createDate ?? 'N/A', // 스냅샷 생성일
    alias: snapshot?.snapshotDiskVos?.[0]?.alias || '없음', // 디스크 별칭
    description: snapshot?.snapshotDiskVos?.[0]?.description || '없음', // 스냅샷 설명
    target: snapshot?.vmVo?.name || '없음', // 연결 대상
    diskSnapshotId: snapshot?.snapshotDiskVos?.[0]?.id || '', // 디스크 스냅샷 ID
    };
  }

    return (
      <>
        <div className="header_right_btns">
        <button className="snap_create_btn" onClick={() => openPopup('new')}>
          생성
        </button>
          <button className='disabled'>미리보기</button>
          <button className='disabled'>커밋</button>
          <button className='disabled'>되돌리기</button>
          <button className='disabled'>삭제</button>
          <button className='disabled'>복제</button>

        </div>
    <span>id = {selectedSnapshot?.id || ''}</span>
    <div className="snapshot_list">
  {snapshots && snapshots.length > 0 ? (
    snapshots.map((snapshot) => (
      <div key={snapshot.id}>
        <div className="snapshot_content">
          <div className="snapshot_content_left">
            <div><FontAwesomeIcon icon={faCamera} fixedWidth /></div>
            <span>{snapshot.name || 'Unnamed Snapshot'}</span>
          </div>

          <div className="snapshot_content_right">
            {/* 일반 섹션 */}
            <div
              onClick={() => {
                setActiveSection(activeSection === 'general' ? null : 'general');
                setSelectedSnapshot(snapshot);
              }}
              style={{ color: activeSection === 'general' && selectedSnapshot?.id === snapshot.id ? '#449bff' : 'inherit' }}
            >
              <FontAwesomeIcon icon={faChevronRight} fixedWidth />
              <span>일반</span>
              <FontAwesomeIcon icon={faEye} fixedWidth />
            </div>

            {/* 디스크 섹션 */}
            <div
              onClick={() => {
                setActiveSection(activeSection === 'disk' ? null : 'disk');
                setSelectedSnapshot(snapshot);
              }}
              style={{ color: activeSection === 'disk' && selectedSnapshot?.id === snapshot.id ? '#449bff' : 'inherit' }}
            >
              <FontAwesomeIcon icon={faChevronRight} fixedWidth />
              <span>디스크</span>
              <FontAwesomeIcon icon={faTrash} fixedWidth />
            </div>

            {/* 네트워크 섹션 */}
            <div
              onClick={() => {
                setActiveSection(activeSection === 'network' ? null : 'network');
                setSelectedSnapshot(snapshot);
              }}
              style={{ color: activeSection === 'network' && selectedSnapshot?.id === snapshot.id ? '#449bff' : 'inherit' }}
            >
              <FontAwesomeIcon icon={faChevronRight} fixedWidth />
              <span>네트워크 인터페이스</span>
              <FontAwesomeIcon icon={faServer} fixedWidth />
            </div>

            {/* 설치된 애플리케이션 섹션 */}
            <div
              onClick={() => {
                setActiveSection(activeSection === 'applications' ? null : 'applications');
                setSelectedSnapshot(snapshot);
              }}
              style={{ color: activeSection === 'applications' && selectedSnapshot?.id === snapshot.id ? '#449bff' : 'inherit' }}
            >
              <FontAwesomeIcon icon={faChevronRight} fixedWidth />
              <span>설치된 애플리케이션</span>
              <FontAwesomeIcon icon={faNewspaper} fixedWidth />
            </div>
          </div>
        </div>

        {/* General Section */}
        {activeSection === 'general' && selectedSnapshot?.id === snapshot.id && (
          <div className="snap_hidden_content active">
            <table className="snap_table">
              <tbody>
                <tr>
                  <th>상태:</th>
                  <td>{snapshot.status || '알 수 없음'}</td>
                </tr>
                <tr>
                  <th>생성 날짜:</th>
                  <td>{snapshot.creationDate || '알 수 없음'}</td>
                </tr>
                <tr>
                  <th>크기:</th>
                  <td>{snapshot.actualSize || '알 수 없음'}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}

        {/* Disk Section */}
        {activeSection === 'disk' && selectedSnapshot?.id === snapshot.id && (
          <div className="snap_hidden_content active">
            <TableOuter
              columns={TableInfo.DISK_SNAPSHOT_FROM_STORAGE_DOMAIN}
              data={snapshots}
              onRowClick={() => console.log('Row clicked')}
            />
          </div>
        )}

        {/* Network Section */}
        {activeSection === 'network' && selectedSnapshot?.id === snapshot.id && (
          <div className="snap_hidden_content active">
            <table className="snap_table">
              <tbody>
                <tr>
                  <th>네트워크 이름:</th>
                  <td>{vm?.nicVos?.[0]?.networkVo?.name || '알 수 없음'}</td>
                </tr>
                <tr>
                  <th>MAC 주소:</th>
                  <td>{vm?.nicVos?.[0]?.macAddress || '알 수 없음'}</td>
                </tr>
                <tr>
                  <th>IPv4 주소:</th>
                  <td>{vm?.nicVos?.[0]?.ipv4 || '알 수 없음'}</td>
                </tr>
                <tr>
                  <th>IPv6 주소:</th>
                  <td>{vm?.nicVos?.[0]?.ipv6 || '알 수 없음'}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}

        {/* Applications Section */}
        {activeSection === 'applications' && selectedSnapshot?.id === snapshot.id && (
          <div className="snap_hidden_content active">
            설치된 애플리케이션 섹션 내용
          </div>
        )}
      </div>
    ))
  ) : (
    <div className="no_snapshots">스냅샷 데이터가 없습니다.</div>
  )}
</div>



        <VmSnapshotAddModal
        isOpen={activePopup === 'new'}
        onRequestClose={closePopup}
      />
          {/*생성 팝업 */}
          {/* <Modal
        isOpen={activePopup === 'new'}
        onRequestClose={closePopup}
        contentLabel="디스크 업로드"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
      >
        <div className="snapshot_new_popup">
          <div className="popup_header">
            <h1>스냅샷 생성</h1>
            <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>
         
          <div className='p-1'>
            <div className='host_textbox mb-1'>
                <label htmlFor="user_name">사용자 이름</label>
                <input type="text" id="user_name" />
            </div>
            <div>
              <div className='font-bold'>포함할 디스크 :</div>
              <div className='snapshot_new_table'>
                <TableOuter 
                    columns={TableColumnsInfo.SNAPSHOT_NEW}
                    data={[]}
                    onRowClick={() => console.log('Row clicked')}
                  />
              </div>
            </div>
          </div>


          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button>OK</button>
            <button onClick={closePopup}>취소</button>
          </div>
        </div>
          </Modal> */}
      </>
    );
  };

  export default VmSnapshot;