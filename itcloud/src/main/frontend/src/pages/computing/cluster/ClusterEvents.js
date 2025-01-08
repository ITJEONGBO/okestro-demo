import React from 'react';
import {useEventFromCluster} from "../../../api/RQHook";
import EventTable from '../../event/EventTable';

const ClusterEvents = ({ clusterId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useEventFromCluster(clusterId, (e) => ({ ...e }));
  console.log('ClusterEvents:', events);
  return (
    <>
      <EventTable events={events}/>
    </>
  );
};
  
export default ClusterEvents;