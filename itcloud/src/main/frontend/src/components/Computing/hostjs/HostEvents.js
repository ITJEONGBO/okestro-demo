import React from 'react';
import TableInfo from "../../table/TableInfo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle, faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import {useEventFromHost} from "../../../api/RQHook";
import PagingTableOuter from '../../table/PagingTableOuter';

const HostEvents = ({ hostId }) => {
  const { 
    data: events, 
    status: eventsStatus, 
    isLoading: isEventsLoading, 
    isError: isEventsError 
  } = useEventFromHost(hostId, toTableItemPredicateEvents);
  function toTableItemPredicateEvents(event) {
    const severity= event?.severity ?? '';
      const icon = severity === 'NORMAL' 
      ? <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />
      : <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />
      return {
        severity: icon,               
        time: event?.time ?? '',                
        description: event?.description ?? '', 
        userEventId: event?.userEventId ?? '',   
      };
  }

  return (
      <>
        <PagingTableOuter
          columns={TableInfo.EVENTS}
          data={events}
          showSearchBox={true}
        />
      </>
  );
};

export default HostEvents;