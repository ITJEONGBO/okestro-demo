import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './css/Computing.css';
import TableInfo from '../table/TableInfo';
import TablesOuter from '../table/TablesOuter';
import HostModal from '../Modal/HostModal'
import DeleteModal from '../Modal/DeleteModal';
import { useAllHosts } from '../../api/RQHook';

const Hosts = () => {
    const navigate = useNavigate();

    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [selectedHost, setSelectedHost] = useState(null);

    const openCreateModal = () => {
        setSelectedHost(null); // No data for create mode
        setIsCreateModalOpen(true);
    };
    const closeCreateModal = () => setIsCreateModalOpen(false);
    
    const openEditModal = (host) => {
        setSelectedHost(host); // Set data for edit mode
        setIsEditModalOpen(true);
    };
    const closeEditModal = () => setIsEditModalOpen(false);

    const openDeleteModal = () => setIsDeleteModalOpen(true);
    const closeDeleteModal = () => setIsDeleteModalOpen(false);

    const handleNameClick = (name) => {
        navigate(`/computing/hosts/${name}`);
    };

    // const sectionHeaderButtons = [
    //     { id: 'new_btn', label: '새로 만들기', onClick: openCreateModal },
    //     { id: 'edit_btn', label: '편집', icon: faPencil, onClick: () => openEditModal(selectedDataCenter) },
    //     { id: 'delete_btn', label: '삭제', icon: faArrowUp, onClick: openDeleteModal },
    // ];

    // 데이터센터
    const {
      data: hosts,
      status: hostsStatus,
      isRefetching: isHostsRefetching,
      refetch: refetchHosts,
      isError: isHostsError,
      error: hostsError,
      isLoading: isHostsLoading
    } = useAllHosts((e) => {
        return {
            ...e,
            cluster: e?.clusterVo.name,
            dataCenter: e?.dataCenterVo.name,
            vmCnt: e?.vmSizeVo.allCnt,
        }
    });

    return (
        <>
        <div className="header_right_btns">
            <button onClick= {openCreateModal}>새로 만들기</button>
            <button onClick= {() =>openEditModal(selectedHost)}>편집</button>
            <button onClick= {openDeleteModal}>제거</button>
        </div>

        <TablesOuter
            columns={TableInfo.HOSTS}
            data={hosts}
            shouldHighlight1stCol={true}
            onRowClick={(row) => setSelectedHost(row)}
        />

        <HostModal 
            isOpen={isCreateModalOpen || isEditModalOpen}
            onRequestClose={isCreateModalOpen ? closeCreateModal : closeEditModal}
            editMode={isEditModalOpen}
            data={selectedHost}
        />
        <DeleteModal
            isOpen={isDeleteModalOpen}
            onRequestClose={closeDeleteModal}
            contentLabel={'호스트'}
            data={selectedHost}  // 삭제할 데이터 전달
        />
        </>
    );
};

export default Hosts;