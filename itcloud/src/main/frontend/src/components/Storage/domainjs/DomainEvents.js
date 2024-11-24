import {useAllEventFromDomain} from "../../../api/RQHook";
import EventTable from "../../table/EventTable";

const DomainEvents = ({ storageDomainId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useAllEventFromDomain(storageDomainId, (e) => ({ ...e}));
  console.log(events); // 데이터 확인
  return (
    <>
      <EventTable events={events} />
    </>
  );
};

export default DomainEvents;