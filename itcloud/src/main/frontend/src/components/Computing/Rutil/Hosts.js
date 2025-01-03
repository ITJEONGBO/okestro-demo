import React  from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

const Hosts = () => {
  const {
      data: hosts,
  } = useAllHosts((e) => ({ ...e }));

  return (
    <>
      <HostDupl
        columns={TableInfo.HOSTS}
        hosts={hosts || []}        
      />
    </>
  );
};

export default Hosts;