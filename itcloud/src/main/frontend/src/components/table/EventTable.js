import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle, faPlay } from '@fortawesome/free-solid-svg-icons';
import PagingTableOuter from '../table/PagingTableOuter';
import TableInfo from '../table/TableInfo';

const EventTable = ({ events } ) => {  

  const renderSeverityIcon = (severity) => {
    if (severity === 'NORMAL' ) {
      return <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />;
    } else if (severity === 'DOWN') {
      return <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />;
    }
    return severity;
  };

  return (
    <>
      <PagingTableOuter
        columns={TableInfo.EVENTS}
        data={events}
        // .map((e) => ({
        //   ...e,
        //   severity: renderSeverityIcon(e?.severity)
        // }))}
        showSearchBox={true}
      />
    </>
  );
};

export default EventTable;
