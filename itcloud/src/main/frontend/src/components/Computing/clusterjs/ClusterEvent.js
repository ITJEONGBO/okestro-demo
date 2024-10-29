import {useEventFromCluster, useHostFromCluster, useVMFromCluster} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import React, { useState } from 'react';
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import VmDu from "../../duplication/VmDu";
import EventDu from "../../duplication/EventDu";
import TableInfo from "../../table/TableInfo";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle, faTimesCircle } from "@fortawesome/free-solid-svg-icons";


const ClusterEvent = ({ cluster }) => {

    const { 
        data: events, 
        status: eventsStatus, 
        isLoading: isEventsLoading, 
        isError: isEventsError 
      } = useEventFromCluster(cluster?.id, toTableItemPredicateEvents);
    function toTableItemPredicateEvents(event) {
        const severity= event?.severity ?? '';
        const icon = severity === 'NORMAL' 
        ? <FontAwesomeIcon icon={faCheckCircle} fixedWidth style={{ color: 'green', fontSize: '0.3rem' }} />
        : <FontAwesomeIcon icon={faTimesCircle} fixedWidth style={{ color: 'red', fontSize: '0.3rem' }} />
   

        return {
          severity: icon,               
          time: event?.time ?? '',                
          description: event?.description ?? 'No message', 
          correlationId: event?.correlationId ?? '',
          source: event?.source ?? 'ovirt',     
          userEventId: event?.userEventId ?? '',   
        };
      }

    return (
        <EventDu 
        columns={TableInfo.EVENTS}
        data={events}
        handleRowClick={() => console.log('Row clicked')}
    />
    );
  };
  
  export default ClusterEvent;