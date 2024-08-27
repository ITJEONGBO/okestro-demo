import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './css/Computing.css';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import { Table } from '../table/Table';

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

    const handleNameClick = (name) => {
        // 클릭된 이름을 URL 파라미터로 전달하여 DataCenterDetail 페이지로 이동
        navigate(`/computing/datacenter/${name}`);
    };

    const sectionHeaderButtons = [
        { id: 'new_btn', label: '새로 만들기', onClick: () => {} },
        { id: 'edit_btn', label: '편집', onClick: () => {} },
        { id: 'delete_btn', label: '삭제', onClick: () => {} },
    ];
    const columns = [
        { header: '', accessor: 'iconStatus' }, // 상태 아이콘 컬럼
        { header: '이름', accessor: 'name', clickable: true },  // name 컬럼을 클릭 가능하게 설정
        { header: '코멘트', accessor: 'comment' },
        { header: '스토리지 유형', accessor: 'storageType' },
        { header: '상태', accessor: 'status' },
        { header: '호환 버전', accessor: 'compatVersion' },
        { header: '설명', accessor: 'description' },
    ];
    
    const data = [
        {
            iconStatus: <i className="fa fa-exclamation-triangle" style={{ color: 'yellowgreen' }}></i>, // 상태 아이콘 추가
            name: '이름데이터',  // 이름 수정
            comment: '',  // 코멘트 값은 비어 있음
            storageType: '공유됨',  // 스토리지 유형
            status: 'Up',  // 상태
            compatVersion: '4.7',  // 호환 버전
            description: 'The default Data Center',  // 설명
        },
    ];
    return (
        <div id="section">
            {sectionContent === 'default' ? (
                <>
                    <HeaderButton
                        title="데이터 센터"
                        subtitle=""
                        buttons={sectionHeaderButtons}
                        popupItems={[]}
                        openModal={openModal}
                        togglePopup={() => {}}
                    />
                    <div className="content_outer">
                        <div className="empty_nav_outer">
                            <div className='section_table_outer'>
                                <button>
                                    <i className="fa fa-refresh"></i>
                                </button>
                                <Table
                                    columns={columns}
                                    data={data}
                                    onRowClick={(row, column) => {
                                        if (column.accessor === 'name') {
                                            handleNameClick(row.name);
                                        }
                                    }}
                                />
                            </div>
                        </div>
                    </div>
                </>
            ) : null}

           <Footer/>

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
