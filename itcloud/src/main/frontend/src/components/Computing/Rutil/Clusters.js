import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import '../css/cluster.css'
import '../css/Computing.css';
import TableOuter from '../../table/TableOuter';
import TableInfo from '../../table/TableInfo';
import ClusterModal from '../../Modal/ClusterModal';
import DeleteModal from '../../Modal/DeleteModal';
import { useAllClusters } from '../../../api/RQHook';

const Clusters = () => {
    const navigate = useNavigate();
  
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [selectedCluster, setSelectedCluster] = useState(null);

    const openCreateModal = () => {
        setSelectedCluster(null); // No data for create mode
        setIsCreateModalOpen(true);
    };
    const closeCreateModal = () => setIsCreateModalOpen(false);

    const openEditModal = (cluster) => {
        setSelectedCluster(cluster); // Set data for edit mode
        setIsEditModalOpen(true);
    };
    const closeEditModal = () => setIsEditModalOpen(false);

    const openDeleteModal = () => setIsDeleteModalOpen(true);
    const closeDeleteModal = () => setIsDeleteModalOpen(false);

    const handleNameClick = (name) => {
        navigate(`/computing/clusters/${name}`);
    };

  const { 
    data: clusters, 
    status: clustersStatus,
    isRefetching: isClustersRefetching,
    refetch: refetchClusters, 
    isError: isClustersError, 
    error: clustersError, 
    isLoading: isClustersLoading,
  } = useAllClusters((e) => {
    return {
        ...e,
        hostCnt: e?.hostSize.allCnt,
        vmCnt: e?.vmSize.allCnt,
    }
  });

//   const handleRowClick = (row, column) => {
//     if (column.accessor === 'name') { // 이름 컬럼일 때만 네비게이션
//         navigate(
//           `/computing/clusters/${row.id}`,
//           { state: { name: row.name } }
//         );
//       }
//     };

  return (
    <>
    <div className="header_right_btns">
        <button onClick= {openCreateModal}>새로 만들기</button>
        <button onClick= {() =>openEditModal(selectedCluster)}>편집</button>
        <button onClick= {openDeleteModal}>제거</button>
    </div>

    <TableOuter 
        columns={TableInfo.CLUSTERS} 
        data={clusters} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedCluster(row)}
    />
    <ClusterModal 
        isOpen={isCreateModalOpen || isEditModalOpen}
        onRequestClose={isCreateModalOpen ? closeCreateModal : closeEditModal}
        editMode={isEditModalOpen}
        data={selectedCluster}
    />
    <DeleteModal
        isOpen={isDeleteModalOpen}
        onRequestClose={closeDeleteModal}
        contentLabel={'클러스터'}
        data={selectedCluster}  // 삭제할 데이터 전달
    />
    </>
  );
};

export default Clusters;

