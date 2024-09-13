import React, { useState } from 'react';
import './Table.css';

const Table = ({ columns, data, onRowClick = () => {}, shouldHighlight1stCol = false }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리

  return (
    <table className="custom-table">
      <thead>
        <tr>
          {columns.map((column, index) => (<th key={index}>{column.header}</th>))}
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
              <td key={colIndex}>
                {
                  // 1번째 컬럼은 강조되며 클릭 가능하게, 나머지 컬럼은 onRowClick 조건적으로 처리
                  (colIndex === 0 && shouldHighlight1stCol) ? (
                    <span
                      style={{ color: 'blue', cursor: 'pointer' }}
                      onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
                      onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
                      onClick={() => {
                        // 이름 클릭 시 이동 처리
                        onRowClick(row, column);
                      }}
                    >
                      {row[column.accessor]}
                    </span>
                  ) : (
                    <span
                      style={{ cursor: 'default' }} 
                      onClick={(e) => {
                        // 다른 컬럼 클릭 시 동작하지 않게 처리
                        e.stopPropagation(); // 클릭 이벤트가 상위로 전파되지 않도록 방지
                      }}
                    >
                      {row[column.accessor]}
                    </span>
                  )
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
