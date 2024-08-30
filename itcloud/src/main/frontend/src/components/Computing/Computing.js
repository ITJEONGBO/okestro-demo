import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './css/Computing.css';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import ApiManager from '../../api/ApiManager';

// React Modal 설정
Modal.setAppElement('#root');

const Computing = () => {
    const { section } = useParams();
    const navigate = useNavigate();
    const [datacenters, setDatacenters] = useState([]);
    const [activeSection, setActiveSection] = useState('datacenter');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isFooterContentVisible, setFooterContentVisibility] = useState(false);
    const [selectedFooterTab, setSelectedFooterTab] = useState('recent');
    const [sectionContent, setSectionContent] = useState('default');

    useEffect(() => {
        navigate(`/computing/${activeSection}`);
        if (activeSection == 'datacenter') {
            
        }
    }, [activeSection, navigate]);
    
    useEffect(() => {
        async function fetchData() {
            console.log('fetching!!!')
            const res = await ApiManager.findAllDataCenters();
            setDatacenters(res);
        }
        fetchData()
    }, [])
    
    const toggleFooterContent = () => {
        setFooterContentVisibility(!isFooterContentVisible);
    };

    const handleFooterTabClick = (tab) => {
        setSelectedFooterTab(tab);
    };

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const handleNameClick = (name) => {
        navigate(`/computing/datacenter/${name}`);
    };

    const sectionHeaderButtons = [
        { id: 'new_btn', label: '새로 만들기', onClick: openModal }, // 여기서 openModal 연결
        { id: 'edit_btn', label: '편집', onClick: () => {} },
        { id: 'delete_btn', label: '삭제', onClick: () => {} },
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
                                    columns={TableColumnsInfo.DATACENTERS}
                                    data={datacenters}
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
                <div className="datacenter_new_popup">
                    <div className="network_popup_header">
                        <h1>새로운 데이터 센터</h1>
                        <button onClick={closeModal}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="datacenter_new_content">
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div>
                            <label htmlFor="comment">설명</label>
                            <input type="text" id="comment" />
                        </div>
                        <div>
                            <label htmlFor="cluster">클러스터</label>
                            <select id="cluster">
                                <option value="공유됨">공유됨</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="compatibility">호환버전</label>
                            <select id="compatibility">
                                <option value="4.7">4.7</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="quota_mode">쿼터 모드</label>
                            <select id="quota_mode">
                                <option value="비활성화됨">비활성화됨</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="comment">코멘트</label>
                            <input type="text" id="comment" />
                        </div>
                       
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
