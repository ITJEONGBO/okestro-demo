import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';

const NetworkTable = ({
  columns,
  networks,
  setSelectedNetwork,
}) => {
  const navigate = useNavigate();
  
  const handleNameClick = (id) => {
    navigate(`/networks/${id}`);
  };

  return (
    <>
      <TablesOuter
        columns={columns}
        data={networks.map((network) => ({
          ...network,
          vlan: network.vlan === 0 ? '-' : network.vlan,
          mtu: network.mtu === 0 ? '기본값(1500)' : network.mtu,
          datacenter: (
            <TableRowClick type="datacenter" id={network.datacenterVo.id}>
              {network.datacenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedNetwork(row)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default NetworkTable;
