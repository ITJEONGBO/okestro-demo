import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle, faWarning, faSmog } from '@fortawesome/free-solid-svg-icons';
import PagingTableOuter from '../table/PagingTableOuter';
import TableInfo from '../table/TableInfo';

const EventTable = ({ events } ) => {  

  const renderSeverityIcon = (severity) => {
    switch (severity) {
      case 'ALERT':
        return '알림';
      case 'NORMAL':
        return <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />;      
      case 'ERROR':
        return <>에러&nbsp; <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'purple', fontSize: '0.3rem' }} /></>;
      case 'WARNING':
        return <FontAwesomeIcon icon={faWarning} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />;
      default:
        return severity;
    }
    // ALERT("alert"),
    // ERROR("error"),
    // NORMAL("normal"),
    // WARNING("warning");
  };

  // 데이터를 변환
  const transformedEvents = events?.map((event) => ({
    ...event,
    severity: renderSeverityIcon(event.severity),
  }));

  return (
    <>
      <PagingTableOuter
        columns={TableInfo.EVENTS}
        data={transformedEvents}
        showSearchBox={true}
      />
    </>
  );
};

export default EventTable;
