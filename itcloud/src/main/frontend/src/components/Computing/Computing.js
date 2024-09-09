import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import './css/Computing.css';
import { useAllDataCenters } from '../../api/RQHook';

// React Modal 설정
Modal.setAppElement('#root');

const Computing = () => {
    const navigate = useNavigate();

    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    const handleNameClick = (name) => {
        navigate(`/computing/datacenters/${name}`);
    };

    const sectionHeaderButtons = [
        { id: 'new_btn', label: '새로 만들기', onClick: openModal }, // 여기서 openModal 연결
        { id: 'edit_btn', label: '편집', onClick: () => {} },
        { id: 'delete_btn', label: '삭제', onClick: () => {} },
    ];

    /*
    const [datacenters, setDatacenters] = useState([]);
    useEffect(() => {
        async function fetchData() {
            console.log('fetching!!!')
            const res = await ApiManager.findAllDataCenters();
            setDatacenters(res);
        }
        fetchData()
    }, [])
    */
    const {
      data: datacenters,
      status: datacenterStatus,
      isRefetching: isDatacentersRefetching,
      refetch: refetchDatacenters,
      isError: isDatacentersError,
      error: datacenterError,
      isLoading: isDatacentersLoading
    } = useAllDataCenters((e) => {
        //DATACENTERS
        return {
          iconStatus: e?.iconStatus ?? '',
          name: e?.name ?? '',
          comment: e?.comment ?? '',
          storageType: e?.storageType ?? '',
          status: e?.status ?? '정보 없음',
          compatVersion: e?.version ?? '0.0',
          description: e?.description ?? '설명없음',
        }
    });

    const handleRowClick = (row, column) => {
        console.log(`handleRowClick ... id: ${row.id}`)
        if (column.accessor === 'name') {
          navigate(
            `/computing/datacenters/${row.id}`,
            { state: { name: row.name } }
          );
        }
    };

    return (
        <div id="section">
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
                            onRowClick={handleRowClick}
                            shouldHighlight1stCol={true}
                        />
                    </div>
                </div>
            </div>

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
                        <h1 class="text-sm">새로운 데이터 센터</h1>
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
