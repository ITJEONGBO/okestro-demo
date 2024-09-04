import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import './css/Cluster.css';
import { useAllClusters } from '../../api/RQHook';

Modal.setAppElement('#root');

const Cluster = () => {
  const navigate = useNavigate();

  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const sectionHeaderButtons = [
    { id: 'new_btn', label: '새로 만들기', onClick: () => {} },
    { id: 'edit_btn', label: '편집', icon: 'fa-pencil', onClick: () => {} },
    { id: 'upgrade_btn', label: '업그레이드', icon: 'fa-arrow-up', onClick: () => {} }
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

  /* 
  const [data, setData] = useState(DEFAULT_VALUES.FIND_ALL_CLUSTERS);
  useEffect(() => {
    const fetchData = async () => {
        const res = await ApiManager.findAllClusters()
        const items = res.map((e) => toTableItemPredicate(e))
        setData(items)
    }
    fetchData()
  }, [])
  */
  const { 
    data: clusters, 
    status: clustersStatus,
    isRefetching: isClustersRefetching,
    refetch: refetchClusters, 
    isError: isClustersError, 
    error: clustersError, 
    isLoading: isClustersLoading,
  } = useAllClusters(toTableItemPredicate);

  function toTableItemPredicate(e) {
    //CLUSTERS_ALT
    return {
      id: e?.id ?? '',
      name: e?.name ?? '',
      comment: e?.comment ?? '',
      description: e?.description ?? '설명없음',
      status: '',
      version: e?.version ?? '0.0',
      cpuType: e?.cpuType ?? 'CPU 정보 없음',
      hostCount: e?.hostSizeVo?.allCnt ?? 0,
      vmCount: e?.vmSizeVo?.allCnt ?? 0,
      upgradeStatus: '', // TODO: 무슨 정보 넣지?
    }
  };
  
  const handleRowClick = (row, column) => {
    console.log(`handleRowClick ... id: ${row.id}`)
    if (column.accessor === 'name') {
      navigate(
        `/computing/clusters/${row.id}`,
        { state: { name: row.name } }
      );
    }
  };

  return (
    <div id="section">
      <HeaderButton
        title="DataCenter > "
        subtitle="Cluster"
        buttons={sectionHeaderButtons}
        popupItems={sectionHeaderPopupItems}
        openModal={openModal}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <div className="section_table_outer">
            <button>
              <i className="fa fa-refresh"></i>
            </button>
            <Table 
              columns={TableColumnsInfo.CLUSTERS_ALT} 
              data={clusters} 
              onRowClick={handleRowClick}
              shouldHighlight1stCol={true}
            />
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Cluster;
