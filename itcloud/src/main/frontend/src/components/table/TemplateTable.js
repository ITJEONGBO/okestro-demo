import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';

const TemplateTable = ({
  columns,
  templates,
  setSelectedTemplates, // 다중 선택된 템플릿을 관리하기 위한 함수
}) => {
  const navigate = useNavigate();

  const handleNameClick = (id) => {
    navigate(`/computing/templates/${id}`);
  };

  const handleRowSelection = (selectedRows) => {
    setSelectedTemplates(selectedRows); // 선택된 데이터 전달
  };

  return (
    <TablesOuter
      columns={columns}
      data={templates.map((temp) => ({
        ...temp,
        cluster: (
          <TableRowClick type="cluster" id={temp.clusterVo.id}>
            {temp.clusterVo.name}
          </TableRowClick>
        ),
        dataCenter: (
          <TableRowClick type="datacenter" id={temp.dataCenterVo.id}>
            {temp.dataCenterVo.name}
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

export default TemplateTable;

// import React from 'react';
// import TablesOuter from './TablesOuter';
// import TableRowClick from './TableRowClick';
// import { useNavigate } from 'react-router-dom';

// const TemplateTable = ({
//   columns,
//   templates,
//   setSelectedTemplate,
// }) => {
//   const navigate = useNavigate();

//   const handleNameClick = (id) => {
//     navigate(`/computing/templates/${id}`);
//   };

//   return (
//     <>
//       {/* 테이블 */}
//       <TablesOuter
//         columns={columns}
//         data={templates.map((temp) => ({
//           ...temp,
//           cluster: (
//             <TableRowClick type="cluster" id={temp.clusterVo.id}>
//               {temp.clusterVo.name}
//             </TableRowClick>
//           ),
//           dataCenter: (
//             <TableRowClick type="datacenter" id={temp.dataCenterVo.id}>
//               {temp.dataCenterVo.name}
//             </TableRowClick>
//           ),
//         }))}
//         shouldHighlight1stCol={true}
//         onRowClick={(row) => {
//           // 선택된 VM 정보와 데이터센터 ID 포함
//           setSelectedTemplate({
//             ...row,
//             dataCenterId: row?.dataCenterVo?.id, // 데이터센터 ID를 추가
//           });
//         }}
//         clickableColumnIndex={[0]}
//         onClickableColumnClick={(row) => handleNameClick(row.id)}
//       />
//     </>
//   );
// };

// export default TemplateTable;
