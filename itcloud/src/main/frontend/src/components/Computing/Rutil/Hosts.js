import React  from 'react';
import '../css/Computing.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllHosts } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

const Hosts = () => {
  const {
      data: hosts = [],
  } = useAllHosts((e) => ({ ...e }));

  return (
    <>
      <HostDupl
        columns={TableColumnsInfo.HOSTS}
        hosts={hosts || []}        
      />
    </>
  );
};

export default Hosts;