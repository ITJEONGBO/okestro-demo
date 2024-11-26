import React from 'react';
import {useAllEventFromTemplate, useEventFromCluster} from "../../../api/RQHook";
import EventTable from '../../table/EventTable';

const TemplateEvents = ({ templateId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useAllEventFromTemplate(templateId, (e) => ({ ...e }));
  console.log('ClusterEvents:', events);
  return (
    <>
      <EventTable events={events}/>
    </>
  );
};
  
export default TemplateEvents;