import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip'; // react-tooltip의 Tooltip 컴포넌트 사용
import './Table.css';

const Tables = ({ 
  columns = [], 
  data = [], 
  onRowClick = () => {}, 
  clickableColumnIndex = [] 
}) => {
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
                  data-tooltip-id={`tooltip-${rowIndex}-${colIndex}`}
                  data-tooltip-content={row[column.accessor]}
                  style={{
                    maxWidth: '200px',
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                    textAlign: typeof row[column.accessor] === 'object' ? 'center' : 'left',
                    cursor: clickableColumnIndex.includes(colIndex) ? 'pointer' : 'default',
                    color: clickableColumnIndex.includes(colIndex) ? 'blue' : 'inherit', // 클릭 가능한 컬럼일 때 파란색으로 강조
                    fontWeight: clickableColumnIndex.includes(colIndex) ? '800' : 'normal',
                    textDecoration: clickableColumnIndex.includes(colIndex) ? 'none' : 'none' // 기본적으로 밑줄 없음
                  }}
                  onMouseEnter={(e) => handleMouseEnter(e, rowIndex, colIndex, row[column.accessor])}
                  onClick={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.stopPropagation();
                      onRowClick(row, column);
                    }
                  }}
                  onMouseOver={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.target.style.textDecoration = 'underline'; // 마우스를 올렸을 때 밑줄 추가
                    }
                  }}
                  onMouseOut={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.target.style.textDecoration = 'none'; // 마우스가 벗어나면 밑줄 제거
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

export default Tables;
