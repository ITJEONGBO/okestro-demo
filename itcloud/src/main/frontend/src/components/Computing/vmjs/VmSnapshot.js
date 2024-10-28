import { faCamera, faChevronRight, faExclamationTriangle, faEye, faNewspaper, faServer, faTimes, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useSnapshotFromVM } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';

// 스냅샷(각각 밑에 열리는 창 만들어야함)
const VmSnapshot = ({vm}) => {
  const [activePopup, setActivePopup] = useState(null);
  
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
      name: snapshot?.description ?? 'Unknown', 
      status: snapshot?.snapshotStatus ?? 'Unknown',  
      created: snapshot?.creationDate ?? 'N/A', 
      vmStatus: snapshot?.vm?.status ?? 'N/A', 
      memorySize: snapshot?.memorySize ?? 'N/A', 
      diskSize: snapshot?.diskSize ?? 'N/A', 
    };
  }

    return (
      <>
        <div className="header_right_btns">
          <button className="snap_create_btn" onClick={() => openPopup('new')}>생성</button>
          <button className='disabled'>미리보기</button>
          <button className='disabled'>커밋</button>
          <button className='disabled'>되돌리기</button>
          <button className='disabled'>삭제</button>
          <button className='disabled'>복제</button>
          <button className='disabled'>템플릿 생성</button>
        </div>

        <div className="snapshot_content">
        <div className="snapshot_content_left">
          <div><FontAwesomeIcon icon={faCamera} fixedWidth /></div>
          <span>Active VM</span>
        </div>

        <div className="snapshot_content_right">
          <div
            onClick={() => toggleSection('general')}
            style={{ color: activeSection === 'general' ? '#449bff' : 'inherit' }} // 조건부 색상
          >
            <FontAwesomeIcon icon={faChevronRight} fixedWidth />
            <span>일반</span>
            <FontAwesomeIcon icon={faEye} fixedWidth />
          </div>

          <div
            onClick={() => toggleSection('disk')}
            style={{ color: activeSection === 'disk' ? '#449bff' : 'inherit' }} // 조건부 색상
          >
            <FontAwesomeIcon icon={faChevronRight} fixedWidth />
            <span>디스크</span>
            <FontAwesomeIcon icon={faTrash} fixedWidth />
          </div>

          <div
            onClick={() => toggleSection('network')}
            style={{ color: activeSection === 'network' ? '#449bff' : 'inherit' }} // 조건부 색상
          >
            <FontAwesomeIcon icon={faChevronRight} fixedWidth />
            <span>네트워크 인터페이스</span>
            <FontAwesomeIcon icon={faServer} fixedWidth />
          </div>

          <div
            onClick={() => toggleSection('applications')}
            style={{ color: activeSection === 'applications' ? '#449bff' : 'inherit' }} // 조건부 색상
          >
            <FontAwesomeIcon icon={faChevronRight} fixedWidth />
            <span>설치된 애플리케이션</span>
            <FontAwesomeIcon icon={faNewspaper} fixedWidth />
          </div>
        </div>
      </div>

        
        <div className={`snap_hidden_content ${activeSection === 'general' ? 'active' : ''}`}>
          <table className="snap_table">
              <tbody>
                <tr>
                  <th>유형:</th>
                  <td>Linux</td>
                </tr>
                <tr>
                  <th>아키텍쳐:</th>
                  <td>x86_64</td>
                </tr>
                <tr>
                  <th>운영체제:</th>
                  <td>CentOS Linux 7</td>
                </tr>
                <tr>
                  <th>커널 버전</th>
                  <td>3.10.0-1062.el7_x86_64</td>
                </tr>
                <tr>
                  <th>시간대:</th>
                  <td>KST (UTC + 09:00)</td>
                </tr>
                <tr>
                  <th>로그인된 사용자:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>콘솔 사용자:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>콘솔 클라이언트 IP:</th>
                  <td></td>
                </tr>
              </tbody>
          </table>
        </div>

        <div className={`snap_hidden_content ${activeSection === 'disk' ? 'active' : ''}`}>
            <TableOuter 
              columns={TableInfo.DISK_SNAPSHOT_FROM_STORAGE_DOMAIN}
              data={snapshots}
              onRowClick={() => console.log('Row clicked')}
            />
        </div>

        <div className={`snap_hidden_content ${activeSection === 'network' ? 'active' : ''}`}>
          <table className="snap_table">
              <tbody>
                <tr>
                  <th>유형:</th>
                  <td>Linux</td>
                </tr>
                <tr>
                  <th>아키텍쳐:</th>
                  <td>x86_64</td>
                </tr>
                <tr>
                  <th>운영체제:</th>
                  <td>CentOS Linux 7</td>
                </tr>
                <tr>
                  <th>커널 버전</th>
                  <td>3.10.0-1062.el7_x86_64</td>
                </tr>
                <tr>
                  <th>시간대:</th>
                  <td>KST (UTC + 09:00)</td>
                </tr>
                <tr>
                  <th>로그인된 사용자:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>콘솔 사용자:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>콘솔 클라이언트 IP:</th>
                  <td></td>
                </tr>
              </tbody>
          </table>
        </div>

        <div className={`snap_hidden_content ${activeSection === 'applications' ? 'active' : ''}`}>
          설치된 애플리케이션 섹션 내용
        </div>

          {/*생성 팝업 */}
          <Modal
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
          </Modal>
      </>
    );
  };

  export default VmSnapshot;