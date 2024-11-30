import React, { useState, useEffect ,useParams, Suspense} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faArrowCircleUp, faChevronRight, faGlassWhiskey, faPlug, faTimes } from  '@fortawesome/free-solid-svg-icons'
import Modal from 'react-modal';
import { useHostdevicesFromVM, useNetworkInterfaceFromVM } from '../../../api/RQHook';
import VmNetworkNewInterfaceModal from '../../Modal/VmNetworkNewInterfaceModal';



// 네트워크 인터페이스
const VmNetwork = ({vm}) => {

    const [modals, setModals] = useState({ create: false, edit: false, delete: false });
    const [selectedNics, setSelectedNics] = useState(null);
    const toggleModal = (type, isOpen) => {
        setModals((prev) => ({ ...prev, [type]: isOpen }));
    };
    console.log('VM ID:', vm?.id);

    const { 
        data: nics, 
        status: disksStatus, 
        isLoading: isDisksLoading, 
        isError: isDisksError,
    } = useNetworkInterfaceFromVM(vm?.id, toTableItemPredicateDisks);

    // API 응답 데이터 확인
    useEffect(() => {
        console.log('네트워크 인터페이스 데이터:', nics);
    }, [nics]);

    function toTableItemPredicateDisks(nic) {
        return {
            id: nic?.id ?? '', 
            name: nic?.name ?? '', 
            status: nic?.status ?? '',
            ipv4: nic?.ipv4 ?? '',
            ipv6: nic?.ipv6 ?? '',
            macAddress: nic?.macAddress ?? '',
            networkVo: nic?.networkVo?.name ?? '',
            vnicProfileVo: nic?.vnicProfileVo?.name ?? '',
            interfaceType: nic?.interface_ ?? 'VIRTIO',
            linked: nic?.linked ?? false,
            rxSpeed: nic?.rxSpeed ?? '',
            txSpeed: nic?.txSpeed ?? '',
            rxTotalSpeed: nic?.rxTotalSpeed ?? '',
            txTotalSpeed: nic?.txTotalSpeed ?? '',
            rxTotalError: nic?.rxTotalError ?? '',
            txTotalError: nic?.txTotalError ?? ''
        };
    }

    const [visibleDetails, setVisibleDetails] = useState([]);
    useEffect(() => {
      setVisibleDetails(Array(3).fill(false)); // 초기 상태: 모든 detail 숨김
    }, []);
      
    // 팝업 열기/닫기 핸들러

    

    const [activePopup, setActivePopup] = useState(null);
    const openPopup = (popupType) => {
      setActivePopup(popupType);
    };
    const closePopup = () => {
      setActivePopup(null);
    };

  
    const toggleDetails = (id) => {
        setVisibleDetails((prevDetails) => ({
          ...prevDetails,
          [id]: !prevDetails[id]
        }));
      };
    
    return (
            <>
              <div className="header_right_btns">
                <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
                <button onClick={() => selectedNics?.id && toggleModal('edit', true)} disabled={!selectedNics?.id}>편집</button>
                <button className="disabled">제거</button>
              </div>
        
            <div className='network_interface_outer'>
              {nics?.map((nic, index) => (
                <div
                className={`network_content ${selectedNics?.id === nic.id ? 'selected' : ''}`}
                onClick={() => setSelectedNics(nic)} // NIC 선택 시 상태 업데이트
                key={nic.id}
              >
                  <div className="network_content">
                    <div>
                      <FontAwesomeIcon icon={faChevronRight} onClick={() => toggleDetails(nic.id)} fixedWidth />
                      <FontAwesomeIcon icon={faArrowCircleUp} style={{ color: '#21c50b', marginLeft: '0.3rem' }} fixedWidth />
                      <FontAwesomeIcon icon={faPlug} fixedWidth />
                      <span>{nic?.name || `NIC ${index + 1}`}</span>
                    </div>
                    <div>
                      <div>네트워크 이름</div>
                      <div>{nic?.networkVo}</div>
                    </div>
                    <div>
                        <div>IPv4</div>
                        <div>{nic.ipv4 || '해당 없음'}</div>
                    </div>
                    <div>
                        <div>IPv6</div>
                        <div>{nic.ipv6 || '해당 없음'}</div>
                    </div>
                    <div style={{ paddingRight: '3%' }}>
                        <div>MAC</div>
                        <div>{nic.macAddress}</div>
                    </div>
                  </div>
                  <div className="network_content_detail" style={{ display: visibleDetails[nic.id] ? 'flex' : 'none' }}>
                  <div className="network_content_detail_box">
                            <div>일반</div>
                            <table className="snap_table">
                                <tbody>
                                    <tr>
                                        <th>연결됨</th>
                                        <td>{nic.linked ? '연결됨' : '연결 안 됨'}</td>
                                    </tr>
                                    <tr>
                                        <th>네트워크 이름</th>
                                        <td>{nic.networkVo || ''}</td>
                                    </tr>
                                    <tr>
                                        <th>프로파일 이름</th>
                                        <td>{nic.vnicProfileVo || ''}</td>
                                    </tr>
                                    <tr>
                                        <th>QoS 이름</th>
                                        <td>{nic.qosName || '해당 없음'}</td>
                                    </tr>
                                    <tr>
                                        <th>링크 상태</th>
                                        <td>{nic.status || ''}</td>
                                    </tr>
                                    <tr>
                                        <th>유형</th>
                                        <td>{nic.interfaceType}</td>
                                    </tr>
                                    <tr>
                                        <th>속도 (Mbps)</th>
                                        <td>{nic.speed || '10000'}</td>
                                    </tr>
                                    <tr>
                                        <th>포트 미러링</th>
                                        <td>{nic.portMirroring || '비활성화됨'}</td>
                                    </tr>
                                    <tr>
                                        <th>게스트 인터페이스 이름</th>
                                        <td>{nic.guestInterfaceName || ''}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="network_content_detail_box">
                            <div>통계</div>
                            <table className="snap_table">
                                <tbody>
                                    <tr>
                                        <th>Rx 속도 (Mbps)</th>
                                        <td>{nic.rxSpeed || '<1'}</td>
                                    </tr>
                                    <tr>
                                        <th>Tx 속도 (Mbps)</th>
                                        <td>{nic.txSpeed || '<1'}</td>
                                    </tr>
                                    <tr>
                                        <th>총 Rx</th>
                                        <td>{nic.rxTotalSpeed || ''}</td>
                                    </tr>
                                    <tr>
                                        <th>총 Tx</th>
                                        <td>{nic.txTotalSpeed || ''}</td>
                                    </tr>
                                    <tr>
                                        <th>중단 (Pkts)</th>
                                        <td>{nic.rxTotalError || ''}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>


                    <div className="network_content_detail_box">
                        <div>네트워크 필터 매개변수</div>
                        <table className="snap_table">
          <tbody>
           
          </tbody>
                        </table>
                    </div>
                  </div>
                </div>
              ))}
            </div>
   
            <Suspense>
                {(modals.create || (modals.edit && selectedNics)) && (
                    <VmNetworkNewInterfaceModal
                        isOpen={modals.create || modals.edit}
                        onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
                        editMode={modals.edit}
                        nicData={selectedNics}
                        vmId={vm?.id || null}
                    />
                )}
            </Suspense>
      </>

         // {/*네트워크 인터페이스(새로만들기) */}
//     <Modal
//     isOpen={activePopup === 'network_interface_new'}
//     onRequestClose={closePopup}
//     contentLabel="새로만들기"
//     className="Modal"
//     overlayClassName="Overlay"
//     shouldCloseOnOverlayClick={false}
//     >
//         <div className='new_network_interface p'>
//             <div className="popup_header">
//                 <h1>새 네트워크 인터페이스</h1>
//                 <button  onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
//             </div>

//             <div className="network_popup_content">
//                 <div className="input_box pt-1">
//                     <span>이름</span>
//                     <input type="text" value={'#'}/>
//                 </div>
//                 <div className="select_box">
//                     <label htmlFor="profile">프로파일</label>
//                     <select id="profile">
//                         <option value="#">#</option>
//                     </select>
//                 </div>
//                 <div className="select_box">
//                     <label htmlFor="type" className='disabled'>유형</label>
//                     <select id="type" disabled>
//                         <option value="test02">VirtIO</option>
//                     </select>
//                 </div>
                

//                 <div className="plug_radio_btn">
//                     <span>링크 상태</span>
//                     <div>
//                         <div className="radio_outer">
//                             <div>
//                                 <input type="radio" name="status" id="status_up" checked/>
//                                 <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>
//                                 <label htmlFor="status_up">Up</label>
//                             </div>
//                             <div>
//                                 <input type="radio" name="status" id="status_down"/>
//                                 <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>
//                                 <label htmlFor="status_down">Down</label>
//                             </div>
//                         </div>
//                     </div>
//                 </div>
//                 <div className="plug_radio_btn">
//                     <span>카드 상태</span>
//                     <div>
//                         <div className="radio_outer">
//                             <div>
//                                 <input type="radio" name="connection_status" id="connected" checked/>
//                                 <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>
//                                 <label htmlFor="connected">연결됨</label>
//                             </div>
//                             <div>
//                                 <input type="radio" name="connection_status" id="disconnected"/>
//                                 <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>
//                                 <label htmlFor="disconnected">분리</label>
//                             </div>
//                         </div>
//                     </div>
//                 </div>
//             </div>
            
//             {/*삭제예정 */}
//             {/* <div className="network_parameter_outer">
//                 <span>네트워크 필터 매개변수</span>
//                 <div>
//                     <div>
//                         <span>이름</span>
//                         <input type="text"/>
//                     </div>
//                     <div>
//                         <span>값</span>
//                         <input type="text"/>
//                     </div>
//                     <div id="buttons">
//                         <button>+</button>
//                         <button>-</button>
//                     </div>
//                 </div>
//             </div> */}

//             <div className="edit_footer">
//                 <button style={{ display: 'none' }}></button>
//                 <button>OK</button>
//                 <button  onClick={closePopup}>취소</button>
//             </div>
//           </div>
// </Modal>

//  {/*네트워크 인터페이스(편집) */}
//  <Modal
//      isOpen={activePopup === 'network_interface_edit'}
//       onRequestClose={closePopup}
//       contentLabel="새로만들기"
//     className="Modal"
//     overlayClassName="Overlay"
//       shouldCloseOnOverlayClick={false}
//       >
//       <div className='new_network_interface p'>
//         <div className="popup_header">
//             <h1>새 네트워크 인터페이스</h1>
//             <button  onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
//         </div>

//         <div className="network_popup_content">
//             <div className="input_box pt-1">
//                 <span>이름</span>
//                 <input type="text" value={'#'}/>
//             </div>
//             <div className="select_box">
//                 <label htmlFor="profile">프로파일</label>
//                 <select id="profile">
//                     <option value="#">#</option>
//                 </select>
//             </div>
//             <div className="select_box">
//                 <label htmlFor="type" className='disabled'>유형</label>
//                 <select id="type" disabled>
//                     <option value="test02">VirtIO</option>
//                 </select>
//             </div>
            

//             <div className="plug_radio_btn">
//                 <span>링크 상태</span>
//                 <div>
//                     <div className="radio_outer">
//                         <div>
//                             <input type="radio" name="status" id="status_up" checked/>
//                             <img src=".//img/스크린샷 2024-05-24 150455.png" alt="status_up"/>
//                             <label htmlFor="status_up">Up</label>
//                         </div>
//                         <div>
//                             <input type="radio" name="status" id="status_down"/>
//                             <img src=".//img/Down.png" alt="status_down"/>
//                             <label htmlFor="status_down">Down</label>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//             <div className="plug_radio_btn">
//                 <span>카드 상태</span>
//                 <div>
//                     <div className="radio_outer">
//                         <div>
//                             <input type="radio" name="connection_status" id="connected" checked/>
//                             <img src=".//img/연결됨.png" alt="connected"/>
//                             <label htmlFor="connected">연결됨</label>
//                         </div>
//                         <div>
//                             <input type="radio" name="connection_status" id="disconnected"/>
//                             <img src=".//img/분리.png" alt="disconnected"/>
//                             <label htmlFor="disconnected">분리</label>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         </div>
        
//         <div className="edit_footer">
//           <button style={{ display: 'none' }}></button>
//           <button>OK</button>
//           <button  onClick={closePopup}>취소</button>
//       </div>
//     </div>
// </Modal>
    );
  };
  export default VmNetwork;
  