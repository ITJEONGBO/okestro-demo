import React from 'react';
import '../css/DataCenter.css';
import { useEventsFromDataCenter } from '../../../api/RQHook';
import EventTable from '../../table/EventTable';

const DataCenterEvents = ({datacenterId}) => {
  const {
    data: events,
    status: eventsStatus,
    isLoading: isEventsLoading,
    isError: isEventsError,
  } = useEventsFromDataCenter(datacenterId, (e) => ({ ...e }));
  console.log('DataCenterEvents:', events);
  return (
    <>
      <EventTable events={events}/>
    </>
  );
};

export default DataCenterEvents;