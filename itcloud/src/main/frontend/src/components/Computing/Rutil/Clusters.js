import React from 'react';
import '../css/Computing.css';
import { useAllClusters } from '../../../api/RQHook';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const Clusters = () => {
  const { 
    data: clusters = [], 
  } = useAllClusters((e) => ({ ...e }));

  return (
    <>
      <ClusterDupl
        columns={TableColumnsInfo.CLUSTERS}
        clusters={clusters || []}
      />
    </>
  );
};

export default Clusters;
