import React, { useState } from 'react';
import './Table.css';

const Table = ({ columns, data, onRowClick, shouldHighlight1stCol=false }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  return (
    <table className="custom-table">
      <thead>
        <tr>
          { columns.map((column, index) => (<th key={index}>{column.header}</th>)) }
        </tr>
      </thead>
      <tbody>
        {data && data.map((row, rowIndex) => (
          <tr key={rowIndex}
            onClick={() => setSelectedRowIndex(rowIndex)} // 클릭한 행의 인덱스를 상태에 저장
            style={{
              backgroundColor: selectedRowIndex === rowIndex ? 'lightblue' : 'transparent', // 선택된 행의 배경색을 변경
            }}
          >
            {columns.map((column, colIndex) => (
              <td key={colIndex}
                onClick={() => onRowClick(row, column)}
              >
                {
                  (colIndex === 0 && shouldHighlight1stCol) ? 
                  <span
                      style={{ color: 'blue', cursor: 'pointer'}}
                      onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
                      onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
                  >
                    {row[column.accessor]}
                  </span>
                  : <span>{row[column.accessor]}</span>
                }
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Table;
