import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { useAllEventFromVM } from '../../../api/RQHook';
import EventTable from '../../table/EventTable';

const VmEvent = ({ vm }) => {
  const {
    data: events = [], // 기본값 설정
    isLoading: isEventsLoading,
    isError: isEventsError,
  } = useAllEventFromVM(vm?.id, toTableItemPredicateEvents);

  function toTableItemPredicateEvents(event) {
    const severity = event?.severity ?? '';
    const icon =
      severity === 'NORMAL' ? (
        <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green' }} />
      ) : (
        <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red' }} />
      );

    return {
      severity: icon,
      time: event?.time ?? 'Unknown Time',
      description: event?.description ?? 'No description',
      correlationId: event?.correlationId ?? 'N/A',
      source: event?.source ?? 'Unknown',
      userEventId: event?.userEventId ?? 'N/A',
    };
  }

  return (
    <>
      <EventTable
        events={events}
        isLoading={isEventsLoading}
        isError={isEventsError}
      />
    </>
  );
};

export default VmEvent;

/*import { faCheck, faCheckCircle, faExclamation, faTimes, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableOuter from '../../table/TableOuter';
import EventDupl from '../../duplication/EventDupl';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllEventFromVM } from '../../../api/RQHook';

// 이벤트 섹션
const VmEvent = ({vm}) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useAllEventFromVM(vm?.id, toTableItemPredicateEvents);
  function toTableItemPredicateEvents(event) {
    const severity= event?.severity ?? '';
    const icon = severity === 'NORMAL' 
    ? <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />
    : <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />
    return {
      severity: icon,   
      time: event?.time ?? '',                
      description: event?.description ?? 'No message', 
      correlationId: event?.correlationId ?? '',
      source: event?.source ?? 'ovirt',     
      userEventId: event?.userEventId ?? '',   
    };
}
    return (
      <>
        <EventDupl 
          columns={TableColumnsInfo.EVENTS}
          data={events}
          handleRowClick={() => console.log('Row clicked')}
          showSearchBox={false}
        />
      </>
    );
  };
  
  export default VmEvent;
   */