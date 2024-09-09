import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import Footer from '../footer/Footer';
import { useAllClusters } from '../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faRefresh
} from '@fortawesome/free-solid-svg-icons'
import './css/Cluster.css';

Modal.setAppElement('#root');

const Cluster = () => {
  const navigate = useNavigate();
  
  const [isModalOpen, setIsModalOpen] = useState(false);


  const closeModal = () => setIsModalOpen(false);
  const [selectedTab, setSelectedTab] = useState('cluster_common_btn');
  const [activePopup, setActivePopup] = useState(null);

    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
      setActivePopup(popupType);
      setSelectedTab('cluster_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };

    const closePopup = () => {
        setActivePopup(null);
    };

    const handleTabClick = (tab) => {
        setSelectedTab(tab);
    };
  const sectionHeaderButtons = [
    { id: 'new_btn', label: '새로 만들기', onClick: () => openPopup('newNetwork') },
    { id: 'edit_btn', label: '편집', icon: 'fa-pencil', onClick: () => openPopup('newNetwork')  },
    { id: 'delete_btn', label: '삭제', icon: 'fa-arrow-up', onClick: () => {} }
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
  } = useAllClusters((e) => {
    //CLUSTERS_ALT
    return {
      id: e?.id ?? '',
      name: e?.name ?? '',
      status: '',
      version: e?.version ?? '0.0',
      cpuType: e?.cpuType ?? 'CPU 정보 없음',
      hostCount: e?.hostSizeVo?.allCnt ?? 0,
      vmCount: e?.vmSizeVo?.allCnt ?? 0,
      comment: e?.comment ?? '',
      description: e?.description ?? '설명없음',
    }
  });

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
        popupItems={[]}
        openModal={openPopup}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <div className="section_table_outer">
            <button>
              <FontAwesomeIcon icon={faRefresh} fixedWidth/>
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

        {/* 새로 만들기 팝업 */}
        <Modal
                isOpen={activePopup === 'newNetwork'}
                onRequestClose={closePopup}
                contentLabel="새로 만들기"
                className="Modal"
                overlayClassName="Overlay"
                shouldCloseOnOverlayClick={false}
            >
                <div className="cluster_new_popup">
                    <div className="network_popup_header">
                        <h1 class="text-sm">새 논리적 네트워크</h1>
                        <button onClick={closePopup}><i className="fa fa-times"></i></button>
                    </div>

                    <div className="network_new_nav">
                        <div
                            id="cluster_common_btn"
                            className={selectedTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('cluster_common_btn')}
                        >
                            일반
                        </div>
                        <div
                            id="cluster_migration_btn"
                            className={selectedTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
                            onClick={() => handleTabClick('cluster_migration_btn')}
                        >
                           마이그레이션 정책
                        </div>
                    </div>

                    {/* 일반 */}
                    {selectedTab === 'cluster_common_btn' && (
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
                                {/* id 수정해야됨 */}
                                <div className="network_form_group">
                                    <label htmlFor="cluster">관리 네트워크</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">CPU 아키텍처</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">CPU 유형</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">침셋/펌웨어 유형</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">FIPS 모드</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">호환 버전</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">스위치 유형</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">방화벽 유형</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">기본 네트워크 공급자</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_form_group">
                                    <label htmlFor="cluster">로그인 최대 메모리 한계</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">Virt 서비스 활성화</label>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">Gluster 서비스 활성화</label>
                                </div>
                                <div className="network_checkbox_type2">
                                  <span>추가 난수 생성기 소스:</span>
                                </div>
                                <div className="network_checkbox_type2">
                                    <input type="checkbox" id="vm_network" name="vm_network" />
                                    <label htmlFor="vm_network">/dev/hwrng 소스</label>
                                </div>
                            </div>
                        </form>
                    )}

                    {/* 마이그레이션 정책 */}
                    {selectedTab === 'cluster_migration_btn' && (
                        <form id="network_new_cluster_form">
                            <div className="network_form_group">
                                    <label htmlFor="cluster">마이그레이션 정책</label>
                                    <select id="cluster">
                                        <option value="default">Default</option>
                                    </select>
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
      <Footer />
    </div>
  );
};

export default Cluster;
