import React from 'react';
import {useEventFromCluster} from "../../../api/RQHook";
import EventTable from '../../table/EventTable';

const ClusterEvents = ({ cId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useEventFromCluster(cId, (e) => ({ ...e }));
  console.log('ClusterEvents:', events);
  return (
    <>
      <EventTable events={events}/>
    </>
  );
};
  
export default ClusterEvents;