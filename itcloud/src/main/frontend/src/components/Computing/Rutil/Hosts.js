import React  from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

const Hosts = () => {
  const {
      data: hosts,
      status: hostsStatus,
      isRefetching: isHostsRefetching,
      refetch: refetchHosts,
      isError: isHostsError,
      error: hostsError,
      isLoading: isHostsLoading
  } = useAllHosts((e) => ({ ...e }));

  return (
    <>
      <HostDupl
        hosts={hosts || []}
        columns={TableInfo.HOSTS}
      />
    </>
  );
};

export default Hosts;