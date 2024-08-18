import React from 'react';

const Table = ({ columns, data, onRowClick }) => {
  return (
    <table className="custom-table">
      <thead>
        <tr>
          {columns.map((column, index) => (
            <th key={index}>{column.header}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((row, rowIndex) => (
          <tr key={rowIndex}>
            {columns.map((column, colIndex) => (
              <td
                key={colIndex}
                onClick={column.clickable ? () => onRowClick({ accessor: column.accessor, row }) : null}
              >
                {row[column.accessor]}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

const Table1 = ({ columns, data, onRowClick }) => {
  return <div></div>;
};

const TableRs = ({ data }) => {
  return <div></div>;
};

export { Table, Table1, TableRs };
