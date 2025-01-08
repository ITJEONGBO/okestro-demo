import React from 'react';
import {useEventFromHost} from "../../../api/RQHook";
import EventTable from '../../../pages/event/EventTable';

const HostEvents = ({ hostId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useEventFromHost(hostId, (e) => ({ ...e }));
  console.log('HostEvents:', events);
  return (
    <>
      <EventTable events={events}/>
    </>
  );
};

export default HostEvents;