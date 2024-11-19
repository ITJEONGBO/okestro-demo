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
import { useAllEvents } from '../api/RQHook';
import PagingTable from './table/PagingTable';
import PagingTableOuter from './table/PagingTableOuter';
import TableInfo from './table/TableInfo';

Modal.setAppElement('#root');

const Event = () => {
  const navigate = useNavigate();



  const { 
    data: events, 
    status: eventsStatus,
    isRefetching: isEventsRefetching,
    refetch: refetchEvents, 
    isError: isEventsError, 
    error: eventsError, 
    isLoading: isEventsLoading,
  } = useAllEvents((e) => ({
    ...e,
    severity: (() => {
      switch (e?.severity) {
        case 'ALERT':
          return '알림';
        case 'ERROR':
          return '에러';
        case 'NORMAL':
          return '일반';
        case 'WARNING':
          return '위험';
        default:
          return e?.severity;
          // 이모티콕 수정 필요
      }
    })(),
  }));
  

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
              columns={TableInfo.EVENTS}
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
