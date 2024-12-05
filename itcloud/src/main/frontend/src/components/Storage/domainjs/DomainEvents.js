import {useAllEventFromDomain} from "../../../api/RQHook";
import EventTable from "../../table/EventTable";

const DomainEvents = ({ domainId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useAllEventFromDomain(domainId, (e) => ({ ...e}));
  console.log(events); // 데이터 확인
  return (
    <>
      <EventTable 
        events={events} 
      />
    </>
  );
};

export default DomainEvents;