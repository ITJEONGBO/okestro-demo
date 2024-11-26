import React from 'react';
import TablesOuter from './TablesOuter';
import TableRowClick from './TableRowClick';
import { useNavigate } from 'react-router-dom';

const TemplateTable = ({
  columns,
  templates,
  setSelectedTemplate,
}) => {
  const navigate = useNavigate();

  const handleNameClick = (id) => {
    navigate(`/computing/templates/${id}`);
  };

  return (
    <>
      {/* 테이블 */}
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
        onRowClick={(row) => {
          // 선택된 VM 정보와 데이터센터 ID 포함
          setSelectedTemplate({
            ...row,
            dataCenterId: row?.dataCenterVo?.id, // 데이터센터 ID를 추가
          });
        }}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default TemplateTable;
