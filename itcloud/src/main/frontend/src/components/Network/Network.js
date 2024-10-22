import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faTimes, faInfoCircle, faPencil, faArrowUp,
  faExclamationTriangle,
  faServer
} from '@fortawesome/free-solid-svg-icons'
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import { adjustFontSize } from '../../UIEvent';
import { useAllNetworks } from '../../api/RQHook';
import './css/Network.css';
import TableInfo from '../table/TableInfo';

Modal.setAppElement('#root');
const Network = () => {
    const { 
        data: networkdata,
        status: networksStatus,
        isRefetching: isNetworksRefetching,
        refetch: refetchNetworks, 
        isError: isNetworksError, 
        error: networksError, 
        isLoading: isNetworksLoading,
      } = useAllNetworks(toTableItemPredicateNetworks);
      
      function toTableItemPredicateNetworks(network) {
        return {
          id: network?.id ?? '',
          dataCenterId: network?.datacenterVo?.id ?? '',  // 데이터 센터의 ID     
          name: network?.name ?? '',
          comment: network?.comment ?? '',  // 코멘트
          datacenterVo: network?.datacenterVo?.name ?? '',  // 데이터 센터
          description: network?.description ?? '',  // 설명
          vlan: network?.vlan ?? '',  // VLAN 태그
          label: network?.label ?? '-',  // 레이블
          port: network?.port ?? '',  // port
        };
      }
      
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [activePopup, setActivePopup] = useState(null);
    const navigate = useNavigate();

    // const handleNetworkNameClick = (row, column) => {
    //     if (column.accessor === 'name') {
    //       navigate(
    //         `/networks/${row.id}`, 
    //         { state: { name: row.name } }
    //       );
    //     } else if (column.accessor === 'dataCenter') {
    //       navigate(
    //         `/computing/datacenters/${row.id}`
    //       );
    //     }
    //   };
      

    useEffect(() => {
      window.addEventListener('resize', adjustFontSize);
      adjustFontSize();
      return () => { window.removeEventListener('resize', adjustFontSize); };
    }, []);

    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

    const toggleFooterContent = () => setFooterContentVisibility(!isFooterContentVisible);
    const handleFooterTabClick = (tab) => setSelectedFooterTab(tab);

    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedTab('network_new_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };

    const closePopup = () => setActivePopup(null);
    const handleTabClick = (tab) => setSelectedTab(tab);


    
    return (
        <div id="network_section">
            <HeaderButton
              titleIcon={faServer}
              title="네트워크"
              buttons={[]}
              popupItems={[]}
            />

            <div className="host_btn_outer">
                <div className="header_right_btns">
                    <button onClick={() => openPopup('newNetwork')}>새로 만들기</button>
                    <button onClick={() => openPopup('editNetwork')}>편집</button>
                    <button onClick={() => openPopup('delete')}> 삭제</button>
                </div>
                <TableOuter
                    columns={TableInfo.NETWORKS}
                    data={networkdata}
                    onRowClick={(row, column, colIndex) => {
                        if (colIndex === 0) {
                          navigate(`/networks/${row.id}`);  // 1번 컬럼 클릭 시 이동할 경로
                        } else if (colIndex === 2) {
                          navigate(`/computing/datacenters/${row.dataCenterId}`);  // 2번 컬럼 클릭 시 이동할 경로
                        }
                      }}
                    clickableColumnIndex={[0, 2]} // 0번과 2번 열에서 클릭 시 navigate 처리
                    onContextMenuItems={(rowData) => [
                        <div key="새로 만들기" onClick={() => console.log()}>새로 만들기</div>,
                        <div key="가져오기" onClick={() => console.log()}>가져오기</div>,
                        <div key="편집" onClick={() => console.log()}>편집</div>,
                        <div key="삭제" onClick={() => console.log()}>삭제</div>
                    ]}
                />


                
            </div>

            <Footer/>

            {/* 새로 만들기 팝업(After) */}
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
            {/* 새로 만들기 팝업(Before) */}
            {/* <Modal
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
                        <div className="network_new_nav">
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
                        </div>

                        {/* 일반 */}
                        {/* {selectedTab === 'network_new_common_btn' && (
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
                                    <span>네트워크 매개변수</span>
                                    <div className="network_form_group">
                                        <label htmlFor="network_label">네트워크 레이블</label>
                                        <input type="text" id="network_label" />
                                    </div>
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
                                    <div className='popup_plus_btn'>
                                        <div className="popup_plus" onClick={() => setSecondModalOpen(true)}>새로만들기</div>
                                    </div>
                                    
                                        <Modal
                                            isOpen={secondModalOpen}
                                            onRequestClose={() => setSecondModalOpen(false)}
                                            contentLabel="추가 모달"
                                            className="SecondModal"
                                            overlayClassName="Overlay"
                                        >
                                                                
                                        <div className="plus_popup_outer">
                                            <div className="popup_header">
                                                <h1>새 호스트 네트워크 Qos</h1>
                                                <button  onClick={() => setSecondModalOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                                            </div>
                                            
                                            <div className='p-1' style={{ borderBottom: '1px solid #d3d3d3' }}>
                                                <div className="network_form_group">
                                                    <label htmlFor="network_provider">네트워크 공급자</label>
                                                    <select id="network_provider">
                                                    <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                                                    </select>
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="qos_name">QoS 이름</label>
                                                    <input type="text" id="qos_name" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="description">설명</label>
                                                    <input type="text" id="description" />
                                                </div>
                                                </div>

                                                <div className='p-1'>
                                                <span className="network_form_group font-bold">아웃바운드</span>
                                                <div className="network_form_group">
                                                    <label htmlFor="weighted_share">가중 공유</label>
                                                    <input type="text" id="weighted_share" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="speed_limit">속도 제한 [Mbps]</label>
                                                    <input type="text" id="speed_limit" />
                                                </div>
                                                <div className="network_form_group">
                                                    <label htmlFor="commit_rate">커밋 속도 [Mbps]</label>
                                                    <input type="text" id="commit_rate" />
                                                </div>
                                            </div>


                                            <div className="edit_footer">
                                                <button style={{ display: 'none' }}></button>
                                                <button>가져오기</button>
                                                <button onClick={() => setSecondModalOpen(false)}>취소</button>
                                            </div>
                                        </div>
                                        
                                        </Modal>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="dns_settings" name="dns_settings" />
                                        <label htmlFor="dns_settings">DNS 설정</label>
                                    </div>
                                    <span>DB서버</span>
                                    <div className="network_checkbox_type3">
                                        <input type="text" id="name" disabled />
                                        <div>
                                            <button>+</button>
                                            <button>-</button>
                                        </div>
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation" />
                                        <label htmlFor="external_vendor_creation">외부 업체에서 작성</label>
                                    </div>
                                    <span>외부</span>
                                    <div className="network_form_group" style={{ paddingTop: 0 }}>
                                        <label htmlFor="external_provider">외부 공급자</label>
                                        <select id="external_provider">
                                            <option value="default">ovirt-provider-ovn</option>
                                        </select>
                                    </div>
                                    <div className="network_form_group">
                                        <label htmlFor="network_port_security">네트워크 포트 보안</label>
                                        <select id="network_port_security">
                                            <option value="default">활성화</option>
                                        </select>
                                    </div>
                                    <div className="network_checkbox_type2">
                                        <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network" />
                                        <label htmlFor="connect_to_physical_network">물리적 네트워크에 연결</label>
                                    </div>
                                </div>
                            </form>
                        )} */}

                        {/* 클러스터 */}
                        {/* {selectedTab === 'network_new_cluster_btn' && (
                            <form id="network_new_cluster_form">
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
                            </form>
                        )} */}

                        {/* vNIC 프로파일 */}
                        {/* {selectedTab === 'network_new_vnic_btn' && (
                            <form id="network_new_vnic_form">
                                <span>vNIC 프로파일</span>
                                <div>
                                    <input type="text" id="vnic_profile" />
                                    <div className='checkbox_group'>
                                        <input type="checkbox" id="public" disabled />
                                        <label htmlFor="public">공개</label>
                                        <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/>
                                    </div>
                                    <label htmlFor="qos">QoS</label>
                                    <select id="qos">
                                        <option value="none">제한 없음</option>
                                    </select>
                                    <div className="network_new_vnic_buttons">
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                            </form>
                        )} */}
                    {/* </div>
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div> */}
            {/* </Modal> */}

            {/* 가져오기 팝업(삭제예정) */}
            {/* <Modal
                isOpen={activePopup === 'getNetwork'}
                onRequestClose={closePopup}
                contentLabel="가져오기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_bring_popup">
                    <div className="popup_header">
                        <h1>네트워크 가져오기</h1>
                        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="network_form_group">
                        <label htmlFor="cluster" style={{ fontSize: '0.33rem',fontWeight:'600' }}>네트워크 공급자</label>
                        <select id="cluster">
                            <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                        </select>
                    </div>

                    <div id="network_bring_table_outer">
                        <span className='font-bold'>공급자 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="provider_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화' },
                                ]}
                            />
                        </div>
                    </div>

                    <div id="network_bring_table_outer">
                        <span className='font-bold'>가져올 네트워크</span>
                        <div>
                            <Table 
                                columns={[
                                    { header: '', accessor: 'checkbox' },
                                    { header: '이름', accessor: 'name' },
                                    { header: '공급자의 네트워크 ID', accessor: 'networkId' },
                                    { header: '데이터 센터', accessor: 'dataCenter' },
                                    { header: '모두허용', accessor: 'allowAll' },
                                ]}
                                data={[
                                    { checkbox: <input type="checkbox" id="import_network_1" defaultChecked />, name: '디스크 활성화', networkId: '디스크 활성화', dataCenter: '디스크 활성화', allowAll: '디스크 활성화' },
                                ]}
                            />
                        </div>
                    </div>

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>가져오기</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal> */}


            {/* 편집 팝업(After) */}
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
            {/* 편집 팝업(Before) */}
            {/* <Modal
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
                                <span>네트워크 매개변수</span>
                                <div className="network_form_group">
                                    <label htmlFor="network_label">네트워크 레이블</label>
                                    <input type="text" id="network_label" />
                                </div>
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
                                <div className='popup_plus_btn'>
                                    <div className="popup_plus" onClick={() => setSecondModalOpen(true)}>새로만들기</div>
                                </div>
                                
                                    <Modal
                                        isOpen={secondModalOpen}
                                        onRequestClose={() => setSecondModalOpen(false)}
                                        contentLabel="추가 모달"
                                        className="SecondModal"
                                        overlayClassName="Overlay"
                                    >
                                                            
                                    <div className="plus_popup_outer">
                                        <div className="popup_header">
                                            <h1>새 호스트 네트워크 Qos</h1>
                                            <button  onClick={() => setSecondModalOpen(false)}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                                        </div>
                                        
                                        <div className='p-1' style={{ borderBottom: '1px solid #d3d3d3' }}>
                                            <div className="network_form_group">
                                                <label htmlFor="network_provider">네트워크 공급자</label>
                                                <select id="network_provider">
                                                <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
                                                </select>
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="qos_name">QoS 이름</label>
                                                <input type="text" id="qos_name" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="description">설명</label>
                                                <input type="text" id="description" />
                                            </div>
                                            </div>

                                            <div className='p-1'>
                                            <span className="network_form_group font-bold">아웃바운드</span>
                                            <div className="network_form_group">
                                                <label htmlFor="weighted_share">가중 공유</label>
                                                <input type="text" id="weighted_share" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="speed_limit">속도 제한 [Mbps]</label>
                                                <input type="text" id="speed_limit" />
                                            </div>
                                            <div className="network_form_group">
                                                <label htmlFor="commit_rate">커밋 속도 [Mbps]</label>
                                                <input type="text" id="commit_rate" />
                                            </div>
                                        </div>


                                        <div className="edit_footer">
                                            <button style={{ display: 'none' }}></button>
                                            <button>가져오기</button>
                                            <button onClick={() => setSecondModalOpen(false)}>취소</button>
                                        </div>
                                    </div>
                                     
                                    </Modal>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="dns_settings" name="dns_settings" />
                                    <label htmlFor="dns_settings">DNS 설정</label>
                                </div>
                                <span>DB서버</span>
                                <div className="network_checkbox_type3">
                                    <input type="text" id="name" disabled />
                                    <div>
                                        <button>+</button>
                                        <button>-</button>
                                    </div>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="external_vendor_creation" name="external_vendor_creation" />
                                    <label htmlFor="external_vendor_creation">외부 업체에서 작성</label>
                                </div>
                                <span>외부</span>
                                <div className="network_form_group" style={{ paddingTop: 0 }}>
                                    <label htmlFor="external_provider">외부 공급자</label>
                                    <select id="external_provider">
                                        <option value="default">ovirt-provider-ovn</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="network_port_security">네트워크 포트 보안</label>
                                    <select id="network_port_security">
                                        <option value="default">활성화</option>
                                    </select>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="connect_to_physical_network" name="connect_to_physical_network" />
                                    <label htmlFor="connect_to_physical_network">물리적 네트워크에 연결</label>
                                </div>
                            </div>
                        </form>
                   

                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal> */}

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
            <h1>디스크 삭제</h1>
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
        </div>
    );
};

export default Network;
