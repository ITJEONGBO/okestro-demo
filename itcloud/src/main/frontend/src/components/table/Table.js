import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip'; // react-tooltip의 Tooltip 컴포넌트 사용
import './Table.css';

const Table = ({  columns = [], data = [], onRowClick = () => {}, clickableColumnIndex = [],onContextMenuItems = false }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  const [tooltips, setTooltips] = useState({}); // 툴팁 상태 관리
  const tableRef = useRef(null); 
  const [contextRowIndex, setContextRowIndex] = useState(null); // 우클릭한 행의 인덱스 관리
 
  //우클릭메뉴 위치
  const [contextMenu, setContextMenu] = useState(null);
  const handleContextMenu = (e, rowIndex) => {
    e.preventDefault();
    const rowData = data[rowIndex];
    if (onContextMenuItems) {
      const menuItems = onContextMenuItems(rowData);
      setContextMenu({
        mouseX: e.clientX - 240,
        mouseY: e.clientY - 40,
        menuItems,
      });
    }
    setContextRowIndex(rowIndex);
    setSelectedRowIndex(null);   
  };
  
  
// 테이블 외부 클릭 시 선택된 행 초기화, 단 메뉴 박스를 제외
useEffect(() => {
  const handleClickOutside = (event) => {
    if (
      tableRef.current && 
      !tableRef.current.contains(event.target) && 
      (!menuRef.current || !menuRef.current.contains(event.target))  // 메뉴 박스를 제외
    ) {
      if (contextRowIndex !== selectedRowIndex) {
        setSelectedRowIndex(null); 
      }
    }
  };
  document.addEventListener('mousedown', handleClickOutside);
  return () => {
    document.removeEventListener('mousedown', handleClickOutside);
  };
}, [contextRowIndex, selectedRowIndex]);



  // 툴팁
  const handleMouseEnter = (e, rowIndex, colIndex, content) => {
    const element = e.target;
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

// 우클릭메뉴 외부를 클릭했을 때만 닫기 + 배경색 초기화
const menuRef = useRef(null);
useEffect(() => {
  const handleClickOutside = (event) => {
    if (menuRef.current && !menuRef.current.contains(event.target)) {
      setContextMenu(null);
      setContextRowIndex(null); // 우클릭된 행의 배경색 초기화
    }
  };
  document.addEventListener('mousedown', handleClickOutside);
  return () => {
    document.removeEventListener('mousedown', handleClickOutside);
  };
}, []);


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
              <tr
                key={rowIndex}
                onClick={() => {
                  setSelectedRowIndex(rowIndex);
                  setContextRowIndex(null); // 다른 우클릭된 행을 초기화
                }}
                onContextMenu={(e) => handleContextMenu(e, rowIndex)}  // 우클릭 시 메뉴 표시
                style={{
                  backgroundColor: selectedRowIndex === rowIndex || contextRowIndex === rowIndex
                    ? 'rgb(218, 236, 245)' // 선택된 행과 우클릭된 행만 색칠
                    : 'transparent', // 나머지는 초기화
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
                    textAlign: (typeof row[column.accessor] === 'string' || typeof row[column.accessor] === 'number') 
                      ? 'left' 
                      : 'center', // 체크박스 및 이모티콘 같은 요소는 가운데 정렬
                    verticalAlign: 'middle', // 수직 가운데 정렬
                    cursor: clickableColumnIndex.includes(colIndex) ? 'pointer' : 'default',
                    color: clickableColumnIndex.includes(colIndex) ? 'blue' : 'inherit',
                    fontWeight: clickableColumnIndex.includes(colIndex) ? '800' : 'normal',
                  }}
                  onMouseEnter={(e) => handleMouseEnter(e, rowIndex, colIndex, row[column.accessor])}
                  onClick={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.stopPropagation();
                      onRowClick(row, column, colIndex);
                    }
                  }}
                  onMouseOver={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.target.style.textDecoration = 'underline';
                    }
                  }}
                  onMouseOut={(e) => {
                    if (clickableColumnIndex.includes(colIndex)) {
                      e.target.style.textDecoration = 'none';
                    }
                  }}
                >
                  {typeof row[column.accessor] === 'object' ? (
                    <div style={{ display: 'flex', justifyContent: 'center' }}>
                      {row[column.accessor]} {/* 체크박스와 같은 요소는 flex로 가운데 정렬 */}
                    </div>
                  ) : (
                    row[column.accessor] // 텍스트나 숫자는 그대로 출력
                  )}
                </td>
                
                
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* 우클릭 메뉴 박스 */}
      {contextMenu && (
        <div ref={menuRef}
          style={{
            position: 'absolute',
            top: `${contextMenu.mouseY}px`,
            left: `${contextMenu.mouseX}px`,
            minWidth: '8%',
            fontSize: '0.3rem',
            backgroundColor: 'white',
            border: '1px solid #eaeaea',
            padding: '10px',
            zIndex: 3
          }}
  
        >
          {contextMenu.menuItems.map((item, index) => (
            <div key={index}>{item}</div>
          ))} 
          
        </div>
      )}  
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
