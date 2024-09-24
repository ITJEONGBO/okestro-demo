import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';


import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
    faArrowUp,
  faCheckCircle,
  faPencil,

} from '@fortawesome/free-solid-svg-icons'

import TableOuter from './table/TableOuter';
import HeaderButton from './button/HeaderButton';
import TableColumnsInfo from './table/TableColumnsInfo';
import Footer from './footer/Footer';
import { useAllClusters } from '../api/RQHook';

Modal.setAppElement('#root');

const Event = () => {
  const navigate = useNavigate();


  const sectionHeaderButtons = [
    { id: 'edit_btn', label: '편집', icon: faPencil, onClick:() => {}  },
    { id: 'delete_btn', label: '삭제', icon: faArrowUp, onClick: () => {} }
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
  // 이벤트
  const eventData = [
    {
      icon: <FontAwesomeIcon icon={faCheckCircle} style={{ color: 'green' }}fixedWidth/>,
      time: '2024. 8. 7. PM 12:24:14',
      message: 'Check for available updates on host host01.ittinfo.com was completed successfully with message \'no updates available.\'',
      correlationId: '2568d791:c08...',
      source: 'oVirt',
      userEventId: '',
    },
  ];
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
      version: e?.version ?? '0.0',
      cpuType: e?.cpuType ?? 'CPU 정보 없음',
      hostCount: e?.hostCnt ?? 0,
      vmCount: e?.vmCnt ?? 0,
      comment: e?.comment ?? '',
      description: e?.description ?? '설명없음',
    }
  });

  const handleRowClick = (row, column) => {
    if (column.accessor === 'name') { // 이름 컬럼일 때만 네비게이션
        navigate(
          `/computing/clusters/${row.id}`,
          { state: { name: row.name } }
        );
      }
    };

  return (
    <div id="section">
      <HeaderButton
        title="Event"
        subtitle="Chart"
        buttons={sectionHeaderButtons}
        popupItems={[]}
        openModal={[]}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">

                   <TableOuter
                     columns={TableColumnsInfo.EVENTS}
                     data={eventData}
                     onRowClick={() => console.log('Row clicked')} 
                    />
                </div>

      </div>

      <Footer />
    </div>
  );
};

export default Event;
