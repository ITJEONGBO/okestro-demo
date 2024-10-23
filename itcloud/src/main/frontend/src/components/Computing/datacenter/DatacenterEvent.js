import {useEventsFromDataCenter} from "../../../api/RQHook";
import EventDu from "../../duplication/EventDu";
import TableColumnsInfo from "../../table/TableColumnsInfo";



const DatacenterEvent = ({ dataCenter }) => {

    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useEventsFromDataCenter(dataCenter?.id, toTableItemPredicateEvents);
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
  
  export default DatacenterEvent;