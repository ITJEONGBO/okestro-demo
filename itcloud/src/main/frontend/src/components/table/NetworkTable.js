import React from 'react';

// 
// 네트워크 테이블
const NetworkTable = ({ data, onRowClick }) => {
  return (
    <>
      {data.map((item, index) => (
        <tr key={index}>
          <td onClick={onRowClick}>{item.name}</td>
          <td>{item.description}</td>
        </tr>
      ))}
    </>
  );
};


export default NetworkTable;
