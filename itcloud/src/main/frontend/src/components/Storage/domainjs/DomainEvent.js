import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {useAllEventFromDomain, useAllTemplateFromDomain} from "../../../api/RQHook";
import EventDu from "../../duplication/EventDu";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { faCheckCircle, faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import TableInfo from "../../table/TableInfo";



const DomainEvent = ({ domain }) => {

    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useAllEventFromDomain(domain?.id, toTableItemPredicateEvents);
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
          <EventDu 
            columns={TableInfo.EVENTS}
            data={events}
            handleRowClick={() => console.log('Row clicked')}
      />
    );
  };
  
  export default DomainEvent;