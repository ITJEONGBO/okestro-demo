import React from 'react';
import { useClustersFromDataCenter } from '../../../api/RQHook';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const DataCenterClusters = ({ datacenterId }) => {
  const { 
    data: clusters
  } = useClustersFromDataCenter(datacenterId, (e) => ({ ...e }));
    
  return (
    <>
      <ClusterDupl
        clusters={clusters || []}
        columns={TableColumnsInfo.CLUSTERS_FROM_DATACENTER}
        datacenterId={datacenterId}
      />
    </>
  );
};

export default DataCenterClusters;
