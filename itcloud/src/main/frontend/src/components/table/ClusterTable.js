import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';
import { renderClusterStatusIcon } from '../util/format';

const ClusterTable = ({
  columns,
  clusters,
  setSelectedClusters,
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
          hostCnt: cluster?.hostSize?.allCnt,
          vmCnt: cluster?.vmSize.allCnt,
          dataCenter: (
            <TableRowClick type="datacenter" id={cluster.dataCenterVo.id}>
              {cluster.dataCenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedClusters(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true} // 다중 선택 활성화
      />
    </>
  );
};

export default ClusterTable;
