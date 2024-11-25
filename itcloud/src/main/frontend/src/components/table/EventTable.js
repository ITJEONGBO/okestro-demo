import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle, faPlay } from '@fortawesome/free-solid-svg-icons';
import PagingTableOuter from '../table/PagingTableOuter';
import TableInfo from '../table/TableInfo';

const EventTable = ({ events } ) => {  

  const renderSeverityIcon = (severity) => {
    switch (severity) {
      case 'NORMAL':
        return <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />;
      case 'DOWN':
        return <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />;
      case 'ALERT':
        return '알림';
      case 'ERROR':
        return '에러';
      case 'WARNING':
        return '위험';
      default:
        return severity;
    }
  };

  // events 배열의 각 항목을 변환
  // const transformedEvents = events.map((e) => ({
  //   ...e,
  //   severity: renderSeverityIcon(e?.severity),
  // }));

  return (
    <>
      <PagingTableOuter
        columns={TableInfo.EVENTS}
        data={events}
        showSearchBox={true}
      />
    </>
  );
};

export default EventTable;
