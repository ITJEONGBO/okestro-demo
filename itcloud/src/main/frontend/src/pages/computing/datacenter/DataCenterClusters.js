import React from 'react';
import { useClustersFromDataCenter } from '../../../api/RQHook';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import ClusterDupl from '../../computing/cluster/ClusterDupl';

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
