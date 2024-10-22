import {useAllEventFromDomain, useAllTemplateFromDomain} from "../../../api/RQHook";
import EventDu from "../../duplication/EventDu";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";



const DomainEvent = ({ domain }) => {

    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useAllEventFromDomain(domain?.id, toTableItemPredicateEvents);
      function toTableItemPredicateEvents(event) {
        return {
          icon: '',                      
          time: event?.time ?? '',                
          description: event?.description ?? 'No message', 
          correlationId: event?.correlationId ?? '',
          source: event?.source ?? 'ovirt',     
          userEventId: event?.userEventId ?? '',   
        };
    }

    return (
          <EventDu 
            columns={TableColumnsInfo.EVENTS}
            data={events}
            handleRowClick={() => console.log('Row clicked')}
      />
    );
  };
  
  export default DomainEvent;