import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './css/Computing.css';
import TableInfo from '../table/TableInfo';
import TablesOuter from '../table/TablesOuter';
import DataCenterModal from '../Modal/DataCenterModal';
import DeleteModal from '../Modal/DeleteModal';
import { useAllDataCenters, useDataCenter } from '../../api/RQHook';

const DataCenters = () => {
    const navigate = useNavigate();

    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [selectedDataCenter, setSelectedDataCenter] = useState(null);

    const openCreateModal = () => {
        setSelectedDataCenter(null); // No data for create mode
        setIsCreateModalOpen(true);
    };
    const closeCreateModal = () => setIsCreateModalOpen(false);
    
    const openEditModal = (dataCenter) => {
        setSelectedDataCenter(dataCenter); // Set data for edit mode
        setIsEditModalOpen(true);
    };
    const closeEditModal = () => setIsEditModalOpen(false);

    const openDeleteModal = () => setIsDeleteModalOpen(true);
    const closeDeleteModal = () => setIsDeleteModalOpen(false);

    const handleNameClick = (name) => {
        navigate(`/computing/datacenters/${name}`);
    };

    // const sectionHeaderButtons = [
    //     { id: 'new_btn', label: '새로 만들기', onClick: openCreateModal },
    //     { id: 'edit_btn', label: '편집', icon: faPencil, onClick: () => openEditModal(selectedDataCenter) },
    //     { id: 'delete_btn', label: '삭제', icon: faArrowUp, onClick: openDeleteModal },
    // ];

    // 데이터센터
    const {
      data: datacenters,
      status: datacentersStatus,
      isRefetching: isDatacentersRefetching,
      refetch: refetchDatacenters,
      isError: isDatacentersError,
      error: datacentersError,
      isLoading: isDatacentersLoading
    } = useAllDataCenters((e) => {
        return {
            ...e,
            storageType: e?.storageType ? '로컬' : '공유됨',
        }
    });

    return (
        <>
        <div className="header_right_btns">
            <button onClick= {openCreateModal}>새로 만들기</button>
            <button onClick= {() =>openEditModal(selectedDataCenter)}>편집</button>
            <button onClick= {openDeleteModal}>제거</button>
        </div>

        <TablesOuter
            columns={TableInfo.DATACENTERS}
            data={datacenters}
            shouldHighlight1stCol={true}
            onRowClick={(row) => setSelectedDataCenter(row)}
        />

        <DataCenterModal 
            isOpen={isCreateModalOpen || isEditModalOpen}
            onRequestClose={isCreateModalOpen ? closeCreateModal : closeEditModal}
            editMode={isEditModalOpen}
            data={selectedDataCenter}
        />
        <DeleteModal
            isOpen={isDeleteModalOpen}
            onRequestClose={closeDeleteModal}
            contentLabel={'데이터센터'}
            data={selectedDataCenter}  // 삭제할 데이터 전달
        />
        </>
    );
};

export default DataCenters;

{/* 
            <Modal
                isOpen={isCreateModalOpen}
                onRequestClose={closeCreateModal}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="datacenter_new_popup">
                    <div className="popup_header">
                        <h1>새로운 데이터 센터</h1>
                        <button onClick={closeCreateModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="datacenter_new_content">
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div className="network_form_group">
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
                        <button onClick={closeCreateModal}>취소</button>
                    </div>
                </div>
            </Modal>

            <Modal
                 isOpen={isEditModalOpen}
                 onRequestClose={closeEditModal}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="datacenter_new_popup">
                    <div className="popup_header">
                        <h1>데이터센터 편집</h1>
                        <button onClick={closeEditModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                    </div>

                    <div className="datacenter_new_content">
                        <div>
                            <label htmlFor="name1">이름</label>
                            <input type="text" id="name1" />
                        </div>
                        <div className="network_form_group">
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
                        <button onClick={closeEditModal}>취소</button>
                    </div>
                </div>
            </Modal>
        </div>
    );
}; */}
