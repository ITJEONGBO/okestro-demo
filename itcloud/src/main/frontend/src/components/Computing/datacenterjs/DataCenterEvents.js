import React, { useState } from 'react';
import '../css/DataCenter.css';
import { useEventsFromDataCenter } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import PagingTableOuter from '../../table/PagingTableOuter';

const DataCenterEvents = ({datacenterId}) => {
  const {
    data: events,
    status: eventsStatus,
    isLoading: isEventsLoading,
    isError: isEventsError,
  } = useEventsFromDataCenter(datacenterId, (e) => ({
    ...e,
    severity: (() => {
      switch (e?.severity) {
        case 'ALERT':
          return '알림';
        case 'ERROR':
          return '에러';
        case 'NORMAL':
          return '일반';
        case 'WARNING':
          return '위험';
        default:
          return e?.severity;
          // 이모티콕 수정 필요
      }
    })(),
  }));

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

export default DataCenterEvents;