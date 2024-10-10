import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip'; // react-tooltip의 Tooltip 컴포넌트 사용
import './Table.css';

const Table = ({ columns, data, onRowClick = () => {}, clickableColumnIndex = [] }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  const [tooltips, setTooltips] = useState({}); // 툴팁 상태 관리
  const tableRef = useRef(null); // 테이블을 참조하는 ref 생성

  // 테이블 외부 클릭 시 선택된 행 초기화
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (tableRef.current && !tableRef.current.contains(event.target)) {
        setSelectedRowIndex(null); // 테이블 외부 클릭 시 선택된 행 초기화
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleMouseEnter = (e, rowIndex, colIndex, content) => {
    const element = e.target;
    // 텍스트가 잘려서 overflow가 발생한 경우에만 툴팁을 설정
    if (element.scrollWidth > element.clientWidth) {
      setTooltips((prevTooltips) => ({
        ...prevTooltips,
        [`${rowIndex}-${colIndex}`]: content
      }));
    } else {
      setTooltips((prevTooltips) => ({
        ...prevTooltips,
        [`${rowIndex}-${colIndex}`]: null
      }));
    }
  };

  return (
    <>
      <div className='custom_outer'>
        <table className="custom-table" ref={tableRef}>
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
                    data-tooltip-id={`tooltip-${rowIndex}-${colIndex}`} // 각 셀에 고유한 tooltip id 설정
                    data-tooltip-content={row[column.accessor]} // 툴팁에 표시할 전체 내용
                    style={{
                      maxWidth: '200px', // 최대 넓이 설정
                      whiteSpace: 'nowrap', // 한 줄로 표시
                      overflow: 'hidden', // 넘치는 텍스트 숨기기
                      textOverflow: 'ellipsis', // 넘치는 텍스트는 ...으로 표시
                      textAlign: typeof row[column.accessor] === 'object' ? 'center' : 'left',
                      cursor: clickableColumnIndex.includes(colIndex) ? 'pointer' : 'default'
                    }}
                    onMouseEnter={(e) => handleMouseEnter(e, rowIndex, colIndex, row[column.accessor])} // 마우스를 올렸을 때 툴팁 설정
                    onClick={(e) => {
                      if (clickableColumnIndex.includes(colIndex)) {
                        e.stopPropagation();
                        onRowClick(row, column); // 클릭 시 특정 컬럼에만 이벤트 적용
                      }
                    }}
                  >
                    {row[column.accessor]}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* 각 셀에 대한 Tooltip 컴포넌트 */}
      {data && data.map((row, rowIndex) =>
        columns.map((column, colIndex) => (
          tooltips[`${rowIndex}-${colIndex}`] && (
            <Tooltip
              key={`tooltip-${rowIndex}-${colIndex}`}
              id={`tooltip-${rowIndex}-${colIndex}`}
              place="right"
              effect="solid"
              delayShow={400} // 1초 지연 후 표시
              content={tooltips[`${rowIndex}-${colIndex}`]} // 툴팁에 표시할 내용
            />
          )
        ))
      )}
    </>
  );
};

export default Table;
