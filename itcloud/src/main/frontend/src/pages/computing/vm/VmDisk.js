import { faChevronLeft, faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect, Suspense } from 'react';
import Modal from 'react-modal';
import TablesOuter from '../../../components/table/TablesOuter';
import { useDisksFromVM } from '../../../api/RQHook';
import DiskModal from '../../storage/disk/modal/DiskModal';
import DeleteModal from '../../../components/DeleteModal';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';

const VmDisk = ({ vm }) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태
  const [modals, setModals] = useState({ create: false, edit: false, delete: false }); // 동작별 모달 관리
  const [action, setAction] = useState(''); // 현재 동작 (create, edit 등)
  const [activeDiskType, setActiveDiskType] = useState('all'); // 필터링된 디스크 유형
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크 관리

  const handleDiskTypeClick = (type) => {
    setActiveDiskType(type); // 디스크 유형 변경
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen })); // 모달 열기/닫기
  };

  const handleActionClick = (actionType) => {
    setAction(actionType);
    setIsModalOpen(true);
  };

  const { 
    data: disks, 
    status: disksStatus,
    isLoading: isDisksLoading,
    refetch: refetchDisks,
  } = useDisksFromVM(vm?.id);
  const formattedDisks = disks?.map((disk) => ({
    alias: disk.alias,
    id: disk.id,
    icon1: disk.bootable ? '🔑' : '',
    icon2: disk.readOnly ? '🔒' : '',
    connectionTarget: disk.vmVo?.name || 'N',
    storageDomain: disk.diskImageVo.storageDomainVo?.name || 'N/A',
    virtualSize: `${(disk.diskImageVo.virtualSize / (1024 ** 3)).toFixed(0)} GB`,
    status: disk.diskImageVo.status,
    storageType: disk.diskImageVo.storageType,
    description: disk.diskImageVo.description || '',
  })) || [];
  const selectedIds = selectedDisks.map((disk) => disk.id).join(', ');

  return (
    <div>
      <div className="disk_type">
        <div className="flex">
          <span>디스크 유형: </span>
          <button
            className={activeDiskType === 'all' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('all')}
          >
            모두
          </button>
          <button
            className={activeDiskType === 'image' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('image')}
          >
            이미지
          </button>
          <button
            className={activeDiskType === 'lun' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('lun')}
          >
            직접 LUN
          </button>
        </div>

        <div className="header_right_btns">
          <button onClick={() => handleActionClick('create')}>새로 만들기</button>
          <button
            onClick={() => handleActionClick('edit')}
            disabled={selectedDisks.length !== 1} // 수정은 단일 선택만 가능
          >
            수정
          </button>
          <button
            onClick={() => toggleModal('delete', true)}
            disabled={selectedDisks.length === 0} // 삭제는 다중 선택 가능
          >
            제거
          </button>
        </div>
      </div>

      <span>선택된 디스크 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      {/* 테이블 렌더링 */}
      <TablesOuter
        columns={
          activeDiskType === 'all'
            ? TableColumnsInfo.ALL_DISK
            : activeDiskType === 'image'
            ? TableColumnsInfo.DISKS_FROM_
            : TableColumnsInfo.LUN_DISK
        }
        data={formattedDisks}
        onRowClick={(selected) => {
          if (Array.isArray(selected)) setSelectedDisks(selected); // 다중 선택된 행 업데이트
        }}
      />

      {/* 모달 처리 */}
      <Suspense>
        {(action === 'create' || action === 'edit') && (
          <DiskModal
            isOpen={isModalOpen}
            onRequestClose={() => setIsModalOpen(false)}
            editMode={action === 'edit'}
            diskId={selectedDisks[0]?.id || null} // 선택된 디스크 중 첫 번째
            vmId={vm?.id || ''}
          />
        )}
        {modals.delete && selectedDisks.length > 0 && (
          <DeleteModal
            isOpen={modals.delete}
            type="vmDisk"
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel="디스크"
            data={selectedDisks}
            vmId={vm?.id}
          />
        )}
      </Suspense>
    </div>
  );
};

// import {  faChevronLeft, faEllipsisV} from '@fortawesome/free-solid-svg-icons';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import React, { useState, useEffect, Suspense } from 'react';
// import Modal from 'react-modal';
// import TableOuter from '../../table/TableOuter';
// import { useDisksFromVM } from '../../../api/RQHook';
// import DiskModal from '../../Modal/DiskModal';
// import DeleteModal from '../../Modal/DeleteModal';
// import TableColumnsInfo from '../../table/TableColumnsInfo';


// // 디스크
// const VmDisk = ({vm}) => {

//     const [isJoinDiskModalOpen, setIsJoinDiskModalOpen] = useState(false);
//     const [activeTab, setActiveTab] = useState('img');
  
//     const handleTabClick = (tab) => setActiveTab(tab);
  
//     const [isVisible, setIsVisible] = useState(false);
//     const toggleContent = () => {
//       setIsVisible(!isVisible);
//     };
//     const [modals, setModals] = useState({ create: false, edit: false, delete: false });
//     const toggleModal = (type, isOpen) => {
//       setModals((prev) => ({ ...prev, [type]: isOpen }));
//   };
    
//     const [activeDiskType, setActiveDiskType] = useState('all');
//   const handleDiskTypeClick = (type) => {
//     setActiveDiskType(type);  // 여기서 type을 설정해야 함
//   };
  
//     // 연결 팝업 열기/닫기 핸들러
//     const closeJoinDiskModal = () => setIsJoinDiskModalOpen(false);
//     const [activePopup, setActivePopup] = useState(null);
//     const openPopup = (popupType) => {
//         setActivePopup(popupType);
//       };
//     const closePopup = () => {
//         setActivePopup(null);
//     };
//     const [activeContentType, setActiveContentType] = useState('all'); // 컨텐츠 유형 상태
//   // 컨텐츠 유형 변경 핸들러
//   const handleContentTypeChange = (event) => {
//     setActiveContentType(event.target.value);
//   };

//   // ...버튼
//   const [isPopupOpen, setIsPopupOpen] = useState(false);
//   const togglePopup = () => {
//     setIsPopupOpen(!isPopupOpen);
//   };
//   // 팝업 외부 클릭 시 닫히도록 처리
//   const handlePopupBoxItemClick = (e) => e.stopPropagation();
//   useEffect(() => {
//     const handleClickOutside = (event) => {
//       const popupBox = document.querySelector(".content_header_popup"); // 팝업 컨테이너 클래스
//       const popupBtn = document.querySelector(".content_header_popup_btn"); // 팝업 버튼 클래스
//       if (
//         popupBox &&
//         !popupBox.contains(event.target) &&
//         popupBtn &&
//         !popupBtn.contains(event.target)
//       ) {
//         setIsPopupOpen(false); // 팝업 외부 클릭 시 팝업 닫기
//       }
//     };
//     document.addEventListener("mousedown", handleClickOutside); // 이벤트 리스너 추가
//     return () => {
//       document.removeEventListener("mousedown", handleClickOutside); // 컴포넌트 언마운트 시 이벤트 리스너 제거
//     };
//   }, []);

//   // 모달들
//  const [isModalOpen, setIsModalOpen] = useState(false);
//     const [action, setAction] = useState(''); // 'create' 또는 'edit'
//     const [selectedDisk, setSelectedDisk] = useState(null); // 선택된 디스크
//     const handleActionClick = (actionType) => {
//       setAction(actionType); // 동작 설정
//       setIsModalOpen(true); // 모달 열기
//     };


//   const { 
//     data: disks, 
//     status: disksStatus,
//     isRefetching: isDisksRefetching,
//     refetch: refetchDisks, 
//     isError: isDisksError, 
//     error: disksError, 
//     isLoading: isDisksLoading,
//   } = useDisksFromVM(vm?.id, toTableItemPredicateDisks);
//   function toTableItemPredicateDisks(disk) {
//     return {
//         id: disk?.id,
//         alias: disk?.diskImageVo?.alias || '',  // 별칭
//         size:disk?.size || '',
//         icon1: <FontAwesomeIcon icon={faChevronLeft} fixedWidth />, 
//         icon2: <FontAwesomeIcon icon={faChevronLeft} fixedWidth />,
//         connectionTarget: disk?.vmVo?.name || '', // 연결 대상
//         storageDomain: disk?.diskImageVo?.storageDomainVo?.name || '',  // 스토리지 도메인
//         virtualSize: disk?.diskImageVo?.virtualSize ? `${(disk.diskImageVo.virtualSize / (1024 ** 3)).toFixed(2)} GIB` : '',
//         status: disk?.diskImageVo?.status || '알 수 없음', // 상태
//         contentType: disk?.diskImageVo?.contentType || '알 수 없음', // 콘텐츠 유형
//         storageType: disk?.diskImageVo?.storageType || '',  // 유형
//         description: disk?.diskImageVo?.description || '' // 설명
//       }
    
//   };
  

  

  
//     return (
//         <>
//                 <div className="disk_type">

//                   <div>
//                     <div className='flex'>
//                       <span>디스크유형 : </span>
//                       <div className='flex'>
//                         <button className={activeDiskType === 'all' ? 'active' : ''} onClick={() => handleDiskTypeClick('all')}>모두</button>
//                         <button className={activeDiskType === 'image' ? 'active' : ''} onClick={() => handleDiskTypeClick('image')}>이미지</button>
//                         <button style={{ marginRight: '0.2rem' }} className={activeDiskType === 'lun' ? 'active' : ''} onClick={() => handleDiskTypeClick('lun')}>직접 LUN</button>
//                       </div>
//                     </div>
        
//                   </div>

//                   <div className="header_right_btns">
//                     {/* <button id="disk_popup_new"  onClick={() => openPopup('newDisk')}>새로 만들기</button> */}
//                     <button id="disk_popup_new" onClick={() => handleActionClick('create')}>새로 만들기</button>
//                     <button id="join_popup_btn" onClick={() => openPopup('disk_connection')}>연결</button>
//                     {/* <button onClick={() => openPopup('disk_edit')}>수정</button> */}
//                     <button onClick={() => selectedDisk?.id && handleActionClick('edit')}>수정</button>
//                     {/* <button onClick={() => openPopup('delete')}>제거</button> */}
//                     <button onClick={() => selectedDisk?.id && toggleModal('delete', true)} disabled={!selectedDisk?.id}>제거</button>
//                     <button className="content_header_popup_btn" onClick={togglePopup}>
//                       <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
//                       {isPopupOpen && (
//                           <div className="content_header_popup">
//                             <div onClick={(e) => { handlePopupBoxItemClick(e); openPopup(); }}>활성</div>
//                             <div onClick={(e) => { handlePopupBoxItemClick(e); openPopup(''); }}>비활성화</div>
//                             <div onClick={(e) => { handlePopupBoxItemClick(e); openPopup(''); }}>이동</div>
//                             <div onClick={(e) => { handlePopupBoxItemClick(e); openPopup(''); }}>LUN 새로고침</div>
//                           </div>
//                         )}
//                     </button>
//                   </div>
//                 </div>



//                 <span>id = {selectedDisk?.id || ''}</span>

//                 {activeDiskType === 'all' && (
//                   <TableOuter 
//                     columns={TableColumnsInfo.ALL_DISK}
//                     data={disks}
//                     onRowClick={(disk) => setSelectedDisk(disk)}
        
           
//                   />
//                 )}

//                 {activeDiskType === 'image' && (
//                   <TableOuter 
//                     columns={TableColumnsInfo.DISKS_FROM_}
//                     data={disks}
//                     onRowClick={(disk) => setSelectedDisk(disk)}
//                   />
//                 )}

//                 {activeDiskType === 'lun' && (
//                   <TableOuter 
//                     columns={TableColumnsInfo.LUN_DISK}
//                     data={disks}
//                     onRowClick={(disk) => setSelectedDisk(disk)}
//                   />
//                 )}

//               {/* 모달 */}
//               <Suspense>
       
//                     <>
//                       {(action === 'create' || action === 'edit') && (
//                         <DiskModal
//                           isOpen={isModalOpen}
//                           onRequestClose={() => setIsModalOpen(false)}
//                           editMode={action === 'edit'}
//                           diskId={selectedDisk?.id || null}
//                           vmId={vm?.id || ''}
//                           type="vmDisk"
//                         />
//                       )}
//                        {modals.delete  && selectedDisk &&(
//                         <DeleteModal
//                           isOpen={modals.delete}
//                           type='vmDisk'
//                           onRequestClose={() => toggleModal('delete', false)}
//                           contentLabel={"디스크"}
//                           data={selectedDisk}
//                           vmId={vm?.id}
//                         />
//                       )}
//                     </>
                
//                 </Suspense>
//             </>
//                 );
//               };




    //         {/*디스크(새로만들기)팝업 */}
    //         <Modal
    //         isOpen={activePopup === 'newDisk'}
    //         onRequestClose={closePopup}
    //         contentLabel="새 가상 디스크"
    //         className="Modal"
    //         overlayClassName="Overlay"
    //         shouldCloseOnOverlayClick={false}
    //         >
    //         <div className="storage_disk_new_popup">
    //             <div className="popup_header">
    //             <h1>새 가상 디스크</h1>
    //             <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
    //             </div>
    //             <div className="disk_new_nav">
    //             <div
    //                 id="storage_img_btn"
    //                 onClick={() => handleTabClick('img')}
    //                 className={activeTab === 'img' ? 'active' : ''}
    //             >
    //                 이미지
    //             </div>
    //             <div
    //                 id="storage_directlun_btn"
    //                 onClick={() => handleTabClick('directlun')}
    //                 className={activeTab === 'directlun' ? 'active' : ''}
    //             >
    //                 직접LUN
    //             </div>
                
    //             </div>
              
    //             {activeTab === 'img' && (
    //             <div className="disk_new_img">
    //                 <div className="disk_new_img_left">
    //                     <div className="img_input_box">
    //                         <span>크기(GIB)</span>
    //                         <input type="text" />
    //                     </div>
    //                     <div className="img_input_box">
    //                         <span>별칭</span>
    //                         <input type="text" value='#' />
    //                     </div>
    //                     <div className="img_input_box">
    //                         <span>설명</span>
    //                         <input type="text" />
    //                     </div>
    //                     <div className="img_select_box">
    //                         <label htmlFor="interface">인터페이스</label>
    //                         <select id="interface">
    //                             <option value="#">#</option>
    //                         </select>
    //                         </div>
    //                         <div className="img_select_box">
    //                         <label htmlFor="storageDomain">스토리지 도메인</label>
    //                         <select id="storageDomain">
    //                             <option value="#">#</option>
    //                         </select>
    //                         </div>
    //                         <div className="img_select_box">
    //                         <label htmlFor="allocationPolicy">할당 정책</label>
    //                         <select id="allocationPolicy">
    //                             <option value="#">#</option>
    //                         </select>
    //                         </div>
    //                         <div className="img_select_box">
    //                         <label htmlFor="diskProfile">디스크 프로파일</label>
    //                         <select id="diskProfile">
    //                             <option value="#">#</option>
    //                         </select>
    //                     </div>
    //                 </div>
    //                 <div className="disk_new_img_right">
    //                 <div>
    //                   <input type="checkbox" id="disk_activation" defaultChecked />
    //                   <label htmlFor="disk_activation">디스크 활성화</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="reset_after_deletion" />
    //                   <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="bootable" disabled/>
    //                   <label htmlFor="bootable">부팅 가능</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="shareable" />
    //                   <label htmlFor="shareable">공유 가능</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="read_only" />
    //                   <label htmlFor="read_only">읽기전용</label>
    //                 </div>
                  
    //                 <div>
    //                   <input type="checkbox" id="incremental_backup" defaultChecked />
    //                   <label htmlFor="incremental_backup">중복 백업 사용</label>
    //                 </div>

    //                 </div>
    //             </div>
    //             )}
               
    //             {activeTab === 'directlun' && (
    //             <div id="storage_directlun_outer">
    //                 <div id="storage_lun_first">
    //                 <div className="disk_new_img_left">
    //                     <div className="img_input_box">
    //                     <span>별칭</span>
    //                     <input type="text" />
    //                     </div>
    //                     <div className="img_input_box">
    //                     <span>설명</span>
    //                     <input type="text" />
    //                     </div>
    //                     <div className="img_select_box">
    //                     <label htmlFor="os">데이터 센터</label>
    //                     <select id="os">
    //                         <option value="linux">Linux</option>
    //                     </select>
    //                     </div>
    //                     <div className="img_select_box">
    //                     <label htmlFor="os">호스트</label>
    //                     <select id="os">
    //                         <option value="linux">Linux</option>
    //                     </select>
    //                     </div>
    //                     <div className="img_select_box">
    //                     <label htmlFor="os">스토리지 타입</label>
    //                     <select id="os">
    //                         <option value="linux">Linux</option>
    //                     </select>
    //                     </div>
    //                 </div>
    //                 <div className="disk_new_img_right">
    //                     <div>
    //                     <input type="checkbox" className="shareable" />
    //                     <label htmlFor="shareable">공유 가능</label>
    //                     </div>
    //                 </div>
    //                 </div>
    //             </div>
    //             )}
            
    //             <div className="edit_footer">
    //             <button style={{ display: 'none' }}></button>
    //             <button>OK</button>
    //             <button onClick={closePopup}>취소</button>
    //             </div>
    //         </div>
    //         </Modal>
  
    //         {/* 디스크(연결) 팝업 */}
    //         <Modal
    //     isOpen={activePopup === 'disk_connection'}
    //     onRequestClose={closePopup}
    //     className="Modal"
    //     overlayClassName="Overlay"
    //     shouldCloseOnOverlayClick={false}
    //   >
    //     <div className="storage_disk_new_popup">
    //       <div className="popup_header">
    //         <h1>가상 디스크 연결</h1>
    //         <button onClick={closePopup}>
    //           <FontAwesomeIcon icon={faTimes} fixedWidth />
    //         </button>
    //       </div>
          
    //       {/* 탭 선택 */}
    //       <div id="join_header">
    //         <div className="disk_new_nav">
    // <div
    //   id="storage_img_btn"
    //   onClick={() => handleTabClick('img')}
    //   className={activeTab === 'img' ? 'active' : ''}
    //   style={{
    //     color: activeTab === 'img' ? 'rgb(35, 132, 243)' : '',
    //     fontWeight: activeTab === 'img' ? '600' : '',
    //     borderBottom: activeTab === 'img' ? '2px solid rgb(35, 132, 243)' : 'none',
    //   }}
    // >
    //   이미지
    // </div>
    // <div
    //   id="storage_directlun_btn"
    //   onClick={() => handleTabClick('directlun')}
    //   className={activeTab === 'directlun' ? 'active' : ''}
    //   style={{
    //     color: activeTab === 'directlun' ? 'rgb(35, 132, 243)' : '',
    //     fontWeight: activeTab === 'directlun' ? '600' : '',
    //     borderBottom: activeTab === 'directlun' ? '2px solid rgb(35, 132, 243)' : 'none',
    //   }}
    // >
    //   직접LUN
    // </div>
    //         </div>
    //       </div>
    //         {/* 이미지 테이블 */}
    //         {activeTab === 'img' && (
    //           <div className="section_table_outer">
    //             <table >
    //               <thead>
    //                 <tr>
    //                   <th></th>
    //                   <th>별칭</th>
    //                   <th>설명</th>
    //                   <th>ID</th>
    //                   <th>가상 크기</th>
    //                   <th>실제 크기</th>
    //                   <th>스토리지 도메인</th>
    //                   <th>인터페이스</th>
    //                   <th>R/O</th>
    //                   <th>icon</th>
    //                   <th>icon</th>
    //                 </tr>
    //               </thead>
    //               <tbody>
    //                 <tr>
    //                   <td>
    //                     <input type='checkbox'/>
    //                   </td>
    //                   <td>#</td>
    //                   <td>#</td>
    //                   <td>#</td>
    //                   <td>#</td>
    //                   <td>#</td>
    //                   <td>#</td>
    //                   <td>
    //                     <select>
    //                       <option>NFS (499 GiB)</option>
    //                       <option>Option 2</option>
    //                     </select>
    //                   </td>
    //                   <td>
    //                     <input type='checkbox'/>
    //                   </td>
    //                   <td>
    //                     <input type='checkbox'/>
    //                   </td>
    //                   <td>
    //                     <FontAwesomeIcon icon={faChevronCircleRight} fixedWidth />
    //                   </td>
    //                 </tr>
    //               </tbody>
    //             </table>
    //           </div>
    //         )}

    //         {/* LUN 테이블 */}
    //         {activeTab === 'directlun' && (
    //           <TableOuter columns={lunDiskColumns} data={lunDiskData} />
    //         )}


    //       <div className="edit_footer">
    //         <button>OK</button>
    //         <button onClick={closePopup}>취소</button>
    //       </div>
    //     </div>
       
    //         </Modal>
    //         {/*디스크(수정)팝업 */}
    //         <Modal
    //         isOpen={activePopup === 'disk_edit'}
    //         onRequestClose={closePopup}
    //         contentLabel="새 가상 디스크"
    //         className="Modal"
    //         overlayClassName="Overlay"
    //         shouldCloseOnOverlayClick={false}
    //       >
    //         <div className="storage_disk_new_popup">
    //           <div className="popup_header">
    //             <h1>새 가상 디스크</h1>
    //             <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
    //           </div>
    //           <div className="disk_new_nav">
    //             <div
    //               id="storage_img_btn"
    //               onClick={() => handleTabClick('img')}
    //               className={activeTab === 'img' ? 'active' : ''}
    //             >
    //               이미지
    //             </div>
    //             <div
    //               id="storage_directlun_btn"
    //               onClick={() => handleTabClick('directlun')}
    //               className={activeTab === 'directlun' ? 'active' : 'disabled'}
    //             >
    //               직접LUN
    //             </div>
                
    //           </div>
    //           {/*이미지*/}
    //           {activeTab === 'img' && (
    //             <div className="disk_new_img">
    //               <div className="disk_new_img_left">
    //                 <div className="img_input_box">
    //                   <span>크기(GIB)</span>
    //                   <input type="text" />
    //                 </div>
    //                 <div className="img_input_box">
    //                   <span>별칭</span>
    //                   <input type="text" />
    //                 </div>
    //                 <div className="img_input_box">
    //                   <span>설명</span>
    //                   <input type="text" />
    //                 </div>
    //                 <div className="img_select_box">
    //                   <label htmlFor="os">데이터 센터</label>
    //                   <select id="os">
    //                     <option value="linux">Linux</option>
    //                   </select>
    //                 </div>
    //                 <div className="img_select_box">
    //                   <label htmlFor="os">스토리지 도메인</label>
    //                   <select id="os">
    //                     <option value="linux">Linux</option>
    //                   </select>
    //                 </div>
    //                 <div className="img_select_box">
    //                   <label htmlFor="os">할당 정책</label>
    //                   <select id="os">
    //                     <option value="linux">Linux</option>
    //                   </select>
    //                 </div>
    //                 <div className="img_select_box">
    //                   <label htmlFor="os">디스크 프로파일</label>
    //                   <select id="os">
    //                     <option value="linux">Linux</option>
    //                   </select>
    //                 </div>
    //               </div>
    //               <div className="disk_new_img_right">
    //                 <div>
    //                   <input type="checkbox" id="disk_activation" defaultChecked />
    //                   <label htmlFor="disk_activation">디스크 활성화</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="reset_after_deletion" />
    //                   <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="bootable" disabled/>
    //                   <label htmlFor="bootable">부팅 가능</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="shareable" />
    //                   <label htmlFor="shareable">공유 가능</label>
    //                 </div>
    //                 <div>
    //                   <input type="checkbox" id="read_only" />
    //                   <label htmlFor="read_only">읽기전용</label>
    //                 </div>
                    
    //                 <div>
    //                   <input type="checkbox" id="incremental_backup" defaultChecked />
    //                   <label htmlFor="incremental_backup">중복 백업 사용</label>
    //                 </div>

    //                 </div>
    //             </div>
    //           )}
    //           {/*직접LUN*/}
    //           {activeTab === 'directlun' && (
    //             <div id="storage_directlun_outer">
    //               <div id="storage_lun_first">
    //                 <div className="disk_new_img_left">
    //                   <div className="img_input_box">
    //                     <span>별칭</span>
    //                     <input type="text" />
    //                   </div>
    //                   <div className="img_input_box">
    //                     <span>설명</span>
    //                     <input type="text" />
    //                   </div>
    //                   <div className="img_select_box">
    //                     <label htmlFor="os">데이터 센터</label>
    //                     <select id="os">
    //                       <option value="linux">Linux</option>
    //                     </select>
    //                   </div>
    //                   <div className="img_select_box">
    //                     <label htmlFor="os">호스트</label>
    //                     <select id="os">
    //                       <option value="linux">Linux</option>
    //                     </select>
    //                   </div>
    //                   <div className="img_select_box">
    //                     <label htmlFor="os">스토리지 타입</label>
    //                     <select id="os">
    //                       <option value="linux">Linux</option>
    //                     </select>
    //                   </div>
    //                 </div>
    //                 <div className="disk_new_img_right">
    //                   <div>
    //                     <input type="checkbox" className="shareable" />
    //                     <label htmlFor="shareable">공유 가능</label>
    //                   </div>
    //                 </div>
    //               </div>
    //             </div>
    //           )}
            
    //           <div className="edit_footer">
    //             <button style={{ display: 'none' }}></button>
    //             <button>OK</button>
    //             <button onClick={closePopup}>취소</button>
    //           </div>
    //         </div>
    //         </Modal>

    //      {/*디스크(삭제)팝업 */}
    //      <Modal
    //     isOpen={activePopup === 'delete'}
    //     onRequestClose={closePopup}
    //     contentLabel="디스크 업로드"
    //     className="Modal"
    //     overlayClassName="Overlay"
    //     shouldCloseOnOverlayClick={false}
    //   >
    //     <div className="storage_delete_popup">
    //       <div className="popup_header">
    //         <h1>디스크 삭제</h1>
    //         <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
    //       </div>
         
    //       <div className='disk_delete_box'>
    //         <div>
    //           <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
    //           <span>다음 항목을 삭제하시겠습니까?</span>
    //         </div>
    //       </div>


    //       <div className="edit_footer">
    //         <button style={{ display: 'none' }}></button>
    //         <button>OK</button>
    //         <button onClick={closePopup}>취소</button>
    //       </div>
    //     </div>
    //   </Modal>
    

  export default VmDisk;