import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';

const NetworkTable = ({
  columns,
  networks,
  setSelectedNetworks, // 다중 선택된 네트워크를 관리하기 위한 함수
}) => {
  const navigate = useNavigate();

  const handleNameClick = (id) => {
    navigate(`/networks/${id}`);
  };
  const handleRowSelection = (selectedRows) => {
    setSelectedNetworks(selectedRows); // 선택된 데이터 전달
  };
  return (
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
      onRowClick={handleRowSelection} // 다중 선택된 행 데이터를 업데이트
      clickableColumnIndex={[0]}
      onClickableColumnClick={(row) => handleNameClick(row.id)}
      
    />
  );
};

export default NetworkTable;


// import React from 'react';
// import TablesOuter from './TablesOuter';
// import TableRowClick from './TableRowClick';
// import { useNavigate } from 'react-router-dom';

// const NetworkTable = ({
//   columns,
//   networks,
//   setSelectedNetwork,
// }) => {
//   const navigate = useNavigate();
  
//   const handleNameClick = (id) => {
//     navigate(`/networks/${id}`);
//   };

//   return (
//     <>
//       <TablesOuter
//         columns={columns}
//         data={networks.map((network) => ({
//           ...network,
//           vlan: network.vlan === 0 ? '-' : network.vlan,
//           mtu: network.mtu === 0 ? '기본값(1500)' : network.mtu,
//           datacenter: (
//             <TableRowClick type="datacenter" id={network.datacenterVo.id}>
//               {network.datacenterVo.name}
//             </TableRowClick>
//           ),
//         }))}
//         shouldHighlight1stCol={true}
//         onRowClick={(row) => setSelectedNetwork(row)}
//         clickableColumnIndex={[0]}
//         onClickableColumnClick={(row) => handleNameClick(row.id)}
//       />
//     </>
//   );
// };

// export default NetworkTable;
