import { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TableOuter from '../../table/TableOuter';
import TableInfo from '../../table/TableInfo';
import ClusterModal from '../../Modal/ClusterModal';
import DeleteModal from '../../Modal/DeleteModal';
import { useAllClusters } from '../../../api/RQHook';

const Clusters = () => {
    const navigate = useNavigate();
    const tableRef = useRef(null); // 테이블 참조를 위한 ref

    const [modals, setModals] = useState({ create: false, edit: false, delete: false });
    const [selectedCluster, setSelectedCluster] = useState(null);

    const toggleModal = (type, isOpen) => {
      setModals((prev) => ({ ...prev, [type]: isOpen }));
    };

    const handleNameClick = (id) => {
        navigate(`/computing/clusters/${id}`);
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


    return (
        <>
            <div className="header_right_btns">
                <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
                <button onClick={() => selectedCluster?.id && toggleModal('edit', true)} disabled={!selectedCluster?.id}>편집</button>
                <button onClick={() => selectedCluster?.id && toggleModal('delete', true)} disabled={!selectedCluster?.id}>제거</button>
            </div>
            <span>id = {selectedCluster?.id || ''}</span>

            <TableOuter 
                ref={tableRef} // 테이블 참조 연결
                columns={TableInfo.CLUSTERS} 
                data={clusters} 
                shouldHighlight1stCol={true}
                onRowClick={setSelectedCluster}
            />
            <ClusterModal 
                isOpen={modals.create || modals.edit}
                onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
                editMode={modals.edit}
                cId={selectedCluster?.id || null}
            />
            <DeleteModal
                isOpen={modals.delete}
                onRequestClose={() => toggleModal('delete', false)}
                contentLabel={'클러스터'}
                data={selectedCluster}
            />
        </>
    );
};

export default Clusters;
