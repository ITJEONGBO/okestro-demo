import { faCheck, faCheckCircle, faExclamation, faTimes, faTimesCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableOuter from '../../table/TableOuter';
import EventDu from '../../duplication/EventDu';
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
        <EventDu 
          columns={TableColumnsInfo.EVENTS}
          data={events}
          handleRowClick={() => console.log('Row clicked')}
          showSearchBox={false}
        />
      </>
    );
  };
  
  export default VmEvent;
  