import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';

const VnicProfileTable = ({
  columns,
  vnicProfiles,
  setSelectedVnicProfile,
}) => {

  return (
    <>
      <TablesOuter
        columns={columns}
        data={vnicProfiles.map((vnic) => {
          return {
            ...vnic,
            network: (
              <TableRowClick type="network" id={vnic?.networkVo.id}>
                {vnic?.networkVo.name}
              </TableRowClick>
            ),
            dataCenter: (
              <TableRowClick type="datacenter" id={vnic?.dataCenterVo.id}>
                {vnic?.dataCenterVo.name}
              </TableRowClick>
            ),
            portMirroring: vnic?.portMirroring === true ? '예': '아니요',
            // version: vnic?.compatVersion,
            networkFilter: vnic?.networkFilterVo.name
          };
        })}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedVnicProfile(row)}
      />
    </>
  );
};

export default VnicProfileTable;
