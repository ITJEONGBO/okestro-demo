import {useEventFromHost} from "../../../api/RQHook";
import React, { useState } from 'react';
import EventDu from "../../duplication/EventDu";
import TableInfo from "../../table/TableInfo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle, faTimesCircle } from "@fortawesome/free-solid-svg-icons";
import Path from "../../Header/Path";


const HostEvent = ({ host }) => {

      const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useEventFromHost(host?.id, toTableItemPredicateEvents);
      function toTableItemPredicateEvents(event) {
        const severity= event?.severity ?? '';
        const icon = severity === 'NORMAL' 
        ? <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />
        : <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />
        return {
          severity: icon,   
          time: event?.time ?? 'Unknown',
          description: event?.description ?? 'No message available',
          correlationId: event?.correlationId ?? 'N/A',
          source: event?.source ?? 'Unknown',
          userEventId: event?.userEventId ?? 'N/A',
        };
      }

    return (
       <>
            <Path pathElements={[]}/>
            <EventDu 
            columns={TableInfo.EVENTS}
            data={events}
            handleRowClick={() => console.log('Row clicked')}
            />
       </>
    );
  };
  
  export default HostEvent;