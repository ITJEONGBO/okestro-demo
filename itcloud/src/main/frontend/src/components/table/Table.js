import React, { useState } from 'react';
import './Table.css';

const Table = ({ columns, data, onRowClick = () => {}, clickableColumnIndex = [] }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리

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
        {data && data.map((row, rowIndex) => (
          <tr key={rowIndex}
            onClick={() => setSelectedRowIndex(rowIndex)} // 클릭한 행의 인덱스를 상태에 저장
            style={{
              backgroundColor: selectedRowIndex === rowIndex ? 'rgb(218, 236, 245)' : 'transparent', // 선택된 행의 배경색을 변경
            }}
          >
            {columns.map((column, colIndex) => (
              <td 
                key={colIndex}
                style={{
                  textAlign: typeof row[column.accessor] === 'object' ? 'center' : 'left' // 아이콘이면 center, 텍스트면 left
                }}
              >
                {
                  // 특정 컬럼에만 클릭 이벤트 적용
                  clickableColumnIndex.includes(colIndex) ? (
                    <span
                      style={{ color: 'blue', cursor: 'pointer' }} // 클릭 가능 컬럼은 파란색 강조
                      onClick={(e) => {
                        e.stopPropagation(); // 부모 트리거가 되지 않도록 방지
                        onRowClick(row, column); // 선택된 행과 해당 컬럼 정보를 전달
                      }}
                    >
                      {row[column.accessor]}
                    </span>
                  ) : (
                    <span style={{ cursor: 'default' }}>
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
