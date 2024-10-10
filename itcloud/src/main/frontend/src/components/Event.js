import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';


import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
    faArrowUp,
  faCheckCircle,
  faListUl,
  faPencil,
  faRefresh,
  faSearch,

} from '@fortawesome/free-solid-svg-icons'

import TableOuter from './table/TableOuter';
import HeaderButton from './button/HeaderButton';
import TableColumnsInfo from './table/TableColumnsInfo';
import Footer from './footer/Footer';
import { useAllClusters, useAllEvents } from '../api/RQHook';
import PagingTable from './table/PagingTable';
import PagingTableOuter from './table/PagingTableOuter';

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

  const { 
    data: events, 
    status: eventsStatus,
    isRefetching: isEventsRefetching,
    refetch: refetchEvents, 
    isError: isEventsError, 
    error: eventsError, 
    isLoading: isEventsLoading,
  } = useAllEvents(toTableItemPredicateEvents);
  
  function toTableItemPredicateEvents(event) {
    return {
      id: event?.id ?? '',
      icon: '', 
      time: event?.time ?? 'Unknown',
      description: event?.description ?? 'No description',
      correlationId: event?.correlationId ?? 'N/A',
      source: event?.source ?? 'Unknown', 
      customEventId: event?.customEventId ?? 'N/A', 
    };
  }
  

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
        titleIcon={faListUl}
        title="Event"
        subtitle="Chart"
        buttons={[]}
        popupItems={[]}
        openModal={[]}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
            
            <PagingTableOuter
              columns={TableColumnsInfo.EVENTS}
              data={events}
              showSearchBox={false}
              onRowClick={(row, column) => {
                console.log('Row clicked', row, column); // 이 부분을 이렇게 감싸서 함수로 전달합니다.
              }}
            />
        </div>

      </div>

      <Footer />
    </div>
  );
};

export default Event;
