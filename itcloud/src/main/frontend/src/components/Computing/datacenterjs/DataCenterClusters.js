import React from 'react';
import { useClustersFromDataCenter } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const DataCenterClusters = ({ datacenterId }) => {
  const { 
    data: clusters
  } = useClustersFromDataCenter(datacenterId, (e) => ({ ...e }));
    
  return (
    <>
      <ClusterDupl
        clusters={clusters || []}
        columns={TableInfo.CLUSTERS_FROM_DATACENTER}
        datacenterId={datacenterId}
      />
    </>
  );
};

export default DataCenterClusters;
