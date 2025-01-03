import React from 'react';
import '../css/Computing.css';
import { useAllClusters } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const Clusters = () => {
  const { 
    data: clusters, 
  } = useAllClusters((e) => ({ ...e }));

  return (
    <>
      <ClusterDupl
        columns={TableInfo.CLUSTERS}
        clusters={clusters || []}
      />
    </>
  );
};

export default Clusters;
