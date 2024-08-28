import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './css/Network.css';
import { Table } from '../table/Table';
import HeaderButton from '../button/HeaderButton';

Modal.setAppElement('#root');

const Network = () => {
    const [activeSection, setActiveSection] = useState('common_outer');
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [activePopup, setActivePopup] = useState(null);

    const navigate = useNavigate();

    // 테이블 행 클릭 시 NetworkDetail로 이동 (name 컬럼만 이동)
    const handleNetworkNameClick = (row, column) => {
        if (column.accessor === 'name') {
            navigate(`/network/${row.name.props.children}`); // name 값을 URL로 전달하여 상세 페이지로 이동
        }
    };

    // 폰트 사이즈 조절
    useEffect(() => {
        function adjustFontSize() {
            const width = window.innerWidth;
            const fontSize = width / 40;
            document.documentElement.style.fontSize = fontSize + 'px';
        }

        window.addEventListener('resize', adjustFontSize);
        adjustFontSize();

        return () => {
            window.removeEventListener('resize', adjustFontSize);
        };
    }, []);

    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');

    const toggleFooterContent = () => {
        setFooterContentVisibility(!isFooterContentVisible);
    };

    const handleFooterTabClick = (tab) => {
        setSelectedFooterTab(tab);
    };

    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
    };

    const closePopup = () => {
        setActivePopup(null);
    };

    const handleTabClick = (tab) => {
        setSelectedTab(tab);
    };

    // 테이블 데이터
    const data = [
        { name: (
            <span
            style={{ color: 'blue', cursor: 'pointer'}}
            onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
            onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
          >
            ovirtmgmt
          </span>
        ), 
        description: 'Management Network', dataCenter: 'DC1', provider: 'Provider1', portSeparation: '아니요' },
        { name: (
            <span
            style={{ color: 'blue', cursor: 'pointer'}}
            onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
            onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
          >
            example1
          </span>
        ), 
        description: 'Example Description 1', dataCenter: 'DC2', provider: 'Provider2', portSeparation: '아니요' },
        
    ];

    const columns = [
        { header: '이름', accessor: 'name', clickable: true },
        { header: '설명', accessor: 'description', clickable: false },
        { header: '데이터센터', accessor: 'dataCenter', clickable: false },
        { header: '공급자', accessor: 'provider', clickable: false },
        { header: '포트분리', accessor: 'portSeparation', clickable: false },
    ];

    const sectionHeaderButtons = [
        { id: 'network_first_edit_btn', label: '편집', onClick: () => {} },
        { label: '삭제', onClick: () => {} },
    ];

    const sectionHeaderPopupItems = [
        '가져오기',
        '가상 머신 복제',
        '삭제',
        '마이그레이션 취소',
        '변환 취소',
        '템플릿 생성',
        '도메인으로 내보내기',
        'Export to Data Domain',
        'OVA로 내보내기',
    ];

    return (
        <div id="network_section">
            <HeaderButton
                title="네트워크"
                buttons={sectionHeaderButtons}
                popupItems={sectionHeaderPopupItems}
            />

            <div className="content_outer">
                <div className='empty_nav_outer'>
                    <div className="content_header_right">
                        <button id="network_new_btn" onClick={() => openPopup('newNetwork')}>새로 만들기</button>
                        <button id="network_bring_btn" onClick={() => openPopup('getNetwork')}>가져오기</button>
                        <button>편집</button>
                        <button>삭제</button>
                    </div>
                    <div className="section_table_outer">
                        <Table columns={columns} data={data} onRowClick={handleNetworkNameClick} />
                    </div>
                </div>
            </div>

            <div className="footer_outer">
                <div className="footer">
                    <button onClick={toggleFooterContent}><i className="fa fa-chevron-down"></i></button>
                    <div>
                        <div
                            style={{
                                color: selectedFooterTab === 'recent' ? 'black' : '#4F4F4F',
                                borderBottom: selectedFooterTab === 'recent' ? '1px solid blue' : 'none'
                            }}
                            onClick={() => handleFooterTabClick('recent')}
                        >
                            최근 작업
                        </div>
                        <div
                            style={{
                                color: selectedFooterTab === 'alerts' ? 'black' : '#4F4F4F',
                                borderBottom: selectedFooterTab === 'alerts' ? '1px solid blue' : 'none'
                            }}
                            onClick={() => handleFooterTabClick('alerts')}
                        >
                            경보
                        </div>
                    </div>
                </div>
                {isFooterContentVisible && (
                    <div className="footer_content" style={{ display: 'block' }}>
                        <div className="footer_nav">
                            {/* Footer 내용 추가 */}
                            <div>
                                <div>작업이름</div>
                                <div><i className="fa fa-filter"></i></div>
                            </div>
                            {/* 여러 항목 추가 가능 */}
                        </div>
                        <div className="footer_img">
                            <img src="img/화면 캡처 2024-04-30 164511.png" alt="스크린샷" />
                            <span>항목을 찾지 못했습니다</span>
                        </div>
                    </div>
                )}
            </div>

            {/* 새로 만들기 팝업 */}
            <Modal
                isOpen={activePopup === 'newNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_new_popup">
                    <div className="network_popup_header">
                        <h1>새 논리적 네트워크</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

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
                                    <div>
                                        <label htmlFor="name">이름</label>
                                        <i className="fa fa-info-circle" style={{ color: '#1ba4e4' }}></i>
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
                                    <div>
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
                    )}

                    {/* 클러스터 */}
                    {selectedTab === 'network_new_cluster_btn' && (
                        <form id="network_new_cluster_form">
                            <span>클러스터에서 네트워크를 연결/분리</span>
                            <div>
                                <table className="network_new_cluster_table">
                                    <thead>
                                        <tr>
                                            <th>이름</th>
                                            <th><input type="checkbox" id="connect_all" /><label htmlFor="connect_all"> 모두 연결</label></th>
                                            <th><input type="checkbox" id="require_all" /><label htmlFor="require_all"> 모두 필요</label></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>Default</td>
                                            <td className="checkbox-group"><input type="checkbox" id="connect_default" /><label htmlFor="connect_default"> 연결</label></td>
                                            <td className="checkbox-group"><input type="checkbox" id="require_default" /><label htmlFor="require_default"> 필수</label></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    )}

                    {/* vNIC 프로파일 */}
                    {selectedTab === 'network_new_vnic_btn' && (
                        <form id="network_new_vnic_form">
                            <span>vNIC 프로파일</span>
                            <div>
                                <input type="text" id="vnic_profile" />
                                <div>
                                    <input type="checkbox" id="public" disabled />
                                    <label htmlFor="public">공개</label>
                                    <i className="fa fa-info-circle" style={{ color: 'rgb(83, 163, 255)' }}></i>
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
                    )}
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closePopup}>취소</button>
                    </div>
                </div>
            </Modal>

            {/* 가져오기 팝업 */}
            <Modal
                isOpen={activePopup === 'getNetwork'}
                onRequestClose={closePopup}
                contentLabel="가져오기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="network_bring_popup">
                    <div className="network_popup_header">
                        <h1>네트워크 가져오기</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="network_form_group">
                        <label htmlFor="cluster" style={{ fontSize: '0.33rem' }}>네트워크 공급자</label>
                        <select id="cluster">
                            <option value="default">Default</option>
                        </select>
                    </div>

                    <div id="network_bring_table_outer">
                        <span>공급자 네트워크</span>
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
                        <span>가져올 네트워크</span>
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
            </Modal>
        </div>
    );
};

export default Network;
