import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './Computing.css';
import HeaderButton from './button/HeaderButton';

// React Modal 설정
Modal.setAppElement('#root');

const Computing = () => {
    const { section } = useParams();
    const navigate = useNavigate();
    const [activeSection, setActiveSection] = useState('datacenter');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
    const [sectionContent, setSectionContent] = useState('default');

    useEffect(() => {
        navigate(`/computing/${activeSection}`);
    }, [activeSection, navigate]);

    const toggleFooterContent = () => {
        setFooterContentVisibility(!isFooterContentVisible);
    };

    const handleFooterTabClick = (tab) => {
        setSelectedFooterTab(tab);
    };

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const handleRowClick = (name) => {
        // 클릭된 이름을 URL 파라미터로 전달하여 DataCenterDetail 페이지로 이동
        navigate(`/computing/datacenter/${name}`);
    };

    return (
        <div id="section">
            {sectionContent === 'default' ? (
                <>
                    <HeaderButton
                        title="데이터 센터"
                        subtitle=""
                        buttons={[]} // 기존에 정의된 버튼들을 여기에 추가합니다.
                        popupItems={[]} // 기존에 정의된 팝업 아이템들을 여기에 추가합니다.
                        openModal={openModal}
                        togglePopup={() => {}}
                    />
                    <div className="content_outer">
                        <div className="empty_nav_outer">
                            <div className="content_header_right">
                                <button>새로 만들기</button>
                                <button>편집</button>
                                <button>삭제</button>
                            </div>
                            <div className='section_table_outer'>
                                <table>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                            <th>이름</th>
                                            <th>코멘트</th>
                                            <th>스토리지 유형</th>
                                            <th>상태</th>
                                            <th>호환 버전</th>
                                            <th>설명</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><i className="fa fa-external-link"></i></td>
                                            <td></td>
                                            <td onClick={() => handleRowClick('Defaultfasdadsffds')}>Defaultfasdadsffds</td>
                                            <td></td>
                                            <td>공유됨</td>
                                            <td>Up</td>
                                            <td>4.7</td>
                                            <td>The default Data Center</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </>
            ) : null}

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

            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                {/* 모달 창 내용 그대로 유지 */}
                <div className="network_new_popup">
                    <div className="network_popup_header">
                        <h1>새 데이터 센터 만들기</h1>
                        <button onClick={closeModal}><i className="fa fa-times"></i></button>
                    </div>
                    <div className="edit_footer">
                        <button style={{ display: 'none' }}></button>
                        <button>OK</button>
                        <button onClick={closeModal}>취소</button>
                    </div>
                </div>
            </Modal>
        </div>
    );
};

export default Computing;
