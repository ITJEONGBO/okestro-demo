import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';

const ClusterTable = ({
  columns,
  clusters,
  setSelectedCluster,
}) => {
  const navigate = useNavigate();
  
  const handleNameClick = (id) => {
    navigate(`/computing/clusters/${id}`);
  };

  return (
    <>
      <TablesOuter
        columns={columns}
        data={clusters.map((cluster) => ({
          ...cluster,
          hostCnt: cluster?.hostSize.allCnt,
          vmCnt: cluster?.vmSize.allCnt,
          dataCenter: (
            <TableRowClick type="datacenter" id={cluster.dataCenterVo.id}>
              {cluster.dataCenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedCluster(row)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default ClusterTable;
