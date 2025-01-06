import { useLogicalFromCluster } from "../../../api/RQHook";
import React, { useState } from 'react';
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TablesOuter from "../../table/TablesOuter";
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCrown, faExclamationTriangle, faInfoCircle, faTimes } from "@fortawesome/free-solid-svg-icons";
import Modal from 'react-modal';
import { Suspense } from "react";
import NetworkModal from "../../Modal/NetworkModal";
import DeleteModal from "../../Modal/DeleteModal";

const ClusterNetworks = ({ clusterId }) => {
    const navigate = useNavigate();
    const [activePopup, setActivePopup] = useState(null);
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');


    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedPopupTab('cluster_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };
    const closePopup = () => {
        setActivePopup(null);
    };
    const [modals, setModals] = useState({ create: false, edit: false, delete: false });
    const [selectedNetwork, setSelectedNetwork] = useState(null);
    const toggleModal = (type, isOpen) => {
        setModals((prev) => ({ ...prev, [type]: isOpen }));
    };

    const { 
      data: networks, 
      status: networksStatus, 
      isLoading: isNetworksLoading, 
      isError: isNetworksError 
    } = useLogicalFromCluster(clusterId, (network) => {
    return {
        id: network?.id ?? '', 
        name: network?.name ?? 'Unknown',            
        status: network?.status ?? '',       
        role: network?.role ? <FontAwesomeIcon icon={faCrown} fixedWidth/> : '', 
        description: network?.description ?? 'No description', 
      };
    });

    return (
        <>
        <div className="header_right_btns">
            <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
            <button onClick={() => selectedNetwork?.id && toggleModal('edit', true)} disabled={!selectedNetwork?.id}>편집</button>
            <button onClick={() => selectedNetwork?.id && toggleModal('delete', true)} disabled={!selectedNetwork?.id}>제거</button>
        </div>
        <span>id = {selectedNetwork?.id || ''}</span>
        <TablesOuter
          columns={TableColumnsInfo.LUNS} 
          data={networks} 
          clickableColumnIndex={[0]} 
          onRowClick={(row, column, colIndex) => {
            console.log('Row Clicked:', row); // 디버깅 추가
            setSelectedNetwork(row);
            if (colIndex === 0) {
              navigate(`/networks/${row.id}`); 
            }
          }}
           />
            <Suspense>
                {(modals.create || (modals.edit && selectedNetwork)) && (
                    <NetworkModal
                        isOpen={modals.create || modals.edit}
                        onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
                        editMode={modals.edit}
                        networkId={selectedNetwork?.id || null}
                    />
                )}
                {modals.delete && selectedNetwork && (
                <DeleteModal
                    isOpen={modals.delete}
                    type='Network'
                    onRequestClose={() => toggleModal('delete', false)}
                    contentLabel={'네트워크'}
                    data={selectedNetwork}
                />
                )}
            </Suspense>
            {/* 논리네트워크 새로 만들기*/}
            <Modal

isOpen={activePopup === 'newNetwork'}
onRequestClose={closePopup}
contentLabel="새로 만들기"
className="Modal"
overlayClassName="Overlay"
shouldCloseOnOverlayClick={false}
>
<div className="network_new_popup">
<div className="popup_header">
<h1>새 논리적 네트워크</h1>
<button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
</div>

<div className='flex'>
{/* <div className="network_new_nav">
    <div
        id="network_new_common_btn"
        className={selectedTab === 'network_new_common_btn' ? 'active-tab' : 'inactive-tab'}
        onClick={() => handleTabClick('network_new_common_btn')}
    >
        일반
    </div>
    <div
        id="network_new_cluster_btn"
        className={selectedTab === 'network_new_cluster_btn' ? 'active-tab' : 'inactive-tab'}
        onClick={() => handleTabClick('network_new_cluster_btn')}
    >
        클러스터
    </div>
    <div
        id="network_new_vnic_btn"
        className={selectedTab === 'network_new_vnic_btn' ? 'active-tab' : 'inactive-tab'}
        onClick={() => handleTabClick('network_new_vnic_btn')}
        style={{ borderRight: 'none' }}
    >
        vNIC 프로파일
    </div>
</div> */}

{/* 일반 */}
{selectedTab === 'network_new_common_btn' && (
    <form id="network_new_common_form">
        <div className="network_first_contents">
            <div className="network_form_group">
                <label htmlFor="cluster">데이터 센터</label>
                <select id="cluster">
                    <option value="default">Default</option>
                </select>
            </div>
            <div className="network_form_group">
                <div  className='checkbox_group'>
                    <label htmlFor="name">이름</label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }}fixedWidth/>
                </div>
                <input type="text" id="name" />
            </div>
            <div className="network_form_group">
                <label htmlFor="description">설명</label>
                <input type="text" id="description" />
            </div>
            <div className="network_form_group">
                <label htmlFor="comment">코멘트</label>
                <input type="text" id="comment" />
            </div>
        </div>

        <div className="network_second_contents">
            
            <div className="network_checkbox_type1">
                <div className='checkbox_group'>
                    <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                    <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                </div>
                <input type="text" id="valn_tagging_input" disabled />
            </div>
            <div className="network_checkbox_type2">
                <input type="checkbox" id="vm_network" name="vm_network" />
                <label htmlFor="vm_network">가상 머신 네트워크</label>
            </div>
            <div className="network_checkbox_type2">
                <input type="checkbox" id="photo_separation" name="photo_separation" />
                <label htmlFor="photo_separation">포토 분리</label>
            </div>
            <div className="network_radio_group">
                <div style={{ marginTop: '0.2rem' }}>MTU</div>
                <div>
                    <div className="radio_option">
                        <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                        <label htmlFor="default_mtu">기본값 (1500)</label>
                    </div>
                    <div className="radio_option">
                        <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                        <label htmlFor="user_defined_mtu">사용자 정의</label>
                    </div>
                </div>
            
            </div>
            <div className="network_form_group">
                <label htmlFor="host_network_qos">호스트 네트워크 QoS</label>
                <select id="host_network_qos">
                    <option value="default">[제한없음]</option>
                </select>
        </div>
            
        
            <div className="network_checkbox_type2">
                <input type="checkbox" id="dns_settings" name="dns_settings" />
                <label htmlFor="dns_settings">DNS 설정</label>
            </div>
            <span>DNS서버</span>
            <div className="network_checkbox_type3">
                <input type="text" id="name" disabled />
                <div>
                    <button>+</button>
                    <button>-</button>
                </div>
            </div>
            
        </div>
        <div id="network_new_cluster_form">
        <span>클러스터에서 네트워크를 연결/분리</span>
        <div>
            <table className="network_new_cluster_table">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>
                            <div className="checkbox_group">
                                <input type="checkbox" id="connect_all" />
                                <label htmlFor="connect_all"> 모두 연결</label>
                            </div>
                        </th>
                        <th>
                            <div className="checkbox_group">
                                <input type="checkbox" id="require_all" />
                                <label htmlFor="require_all"> 모두 필요</label>
                            </div>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Default</td>
                        <td className="checkbox-group">
                            <div className="checkbox_group">
                                <input type="checkbox" id="connect_default" />
                                <label htmlFor="connect_default"> 연결</label>
                            </div>
                        </td>
                        <td className="checkbox-group">
                            <div className="checkbox_group">
                                <input type="checkbox" id="require_default" />
                                <label htmlFor="require_default"> 필수</label>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    </form>
)}




</div>
<div className="edit_footer">
<button style={{ display: 'none' }}></button>
<button>OK</button>
<button onClick={closePopup}>취소</button>
</div>
</div>
            </Modal>

            {/* 논리네트워크 편집 */}
            <Modal
                isOpen={activePopup === 'editNetwork'}
                onRequestClose={closePopup}
                contentLabel="편집"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_edit_popup">
                    <div className="popup_header">
                        <h1>논리 네트워크 수정</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>
                    
                    <form id="network_new_common_form">
                    <div className="network_first_contents">
                                <div className="network_form_group">
                                    <label htmlFor="cluster">데이터 센터</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <div  className='checkbox_group'>
                                        <label htmlFor="name">이름</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: '#1ba4e4' }}fixedWidth/>
                                    </div>
                                    <input type="text" id="name" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="description">설명</label>
                                    <input type="text" id="description" />
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="comment">코멘트</label>
                                    <input type="text" id="comment" />
                                </div>
                            </div>

                            <div className="network_second_contents">
                                
                                <div className="network_checkbox_type1">
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="valn_tagging" name="valn_tagging" />
                                        <label htmlFor="valn_tagging">VALN 태깅 활성화</label>
                                    </div>
                                    <input type="text" id="valn_tagging_input" disabled />
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">가상 머신 네트워크</label>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="photo_separation" name="photo_separation" />
                                    <label htmlFor="photo_separation">포토 분리</label>
                                </div>
                                <div className="network_radio_group">
                                    <div style={{ marginTop: '0.2rem' }}>MTU</div>
                                    <div>
                                        <div className="radio_option">
                                            <input type="radio" id="default_mtu" name="mtu" value="default" checked />
                                            <label htmlFor="default_mtu">기본값 (1500)</label>
                                        </div>
                                        <div className="radio_option">
                                            <input type="radio" id="user_defined_mtu" name="mtu" value="user_defined" />
                                            <label htmlFor="user_defined_mtu">사용자 정의</label>
                                        </div>
                                    </div>
                                   
                                </div>
                               
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DNS서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                              
                            </div>
                        </form>
                   

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>

            {/*삭제 팝업 */}
            <Modal
                isOpen={activePopup === 'delete'}
                onRequestClose={closePopup}
                contentLabel="디스크 업로드"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="storage_delete_popup">
                <div className="popup_header">
                    <h1>삭제</h1>
                    <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
                
                <div className='disk_delete_box'>
                    <div>
                    <FontAwesomeIcon style={{marginRight:'0.3rem'}} icon={faExclamationTriangle} />
                    <span>다음 항목을 삭제하시겠습니까?</span>
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
  
  export default ClusterNetworks;