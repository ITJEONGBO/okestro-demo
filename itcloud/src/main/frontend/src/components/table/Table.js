import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip'; // react-tooltip의 Tooltip 컴포넌트 사용
import './Table.css';

const Table = ({ 
  columns = [], 
  data = [], 
  onRowClick = () => {}, 
  clickableColumnIndex = [], 
  onContextMenuItems = false, 
  onClickableColumnClick = () => {}
}) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  const [tooltips, setTooltips] = useState({}); // 툴팁 상태 관리
  const tableRef = useRef(null); 
  const [contextRowIndex, setContextRowIndex] = useState(null); // 우클릭한 행의 인덱스 관리
  
  // 우클릭 메뉴 위치 관리
  const [contextMenu, setContextMenu] = useState(null);
  const handleContextMenu = (e, rowIndex) => {
    e.preventDefault();
    const rowData = data[rowIndex];
    if (onContextMenuItems) {
      const menuItems = onContextMenuItems(rowData);
      setContextMenu({
        mouseX: e.clientX - 320,
        mouseY: e.clientY - 47,
        menuItems,
      });
    }
    setContextRowIndex(rowIndex);
    setSelectedRowIndex(null);   
  };
  
  // 테이블 외부 클릭 시 선택된 행 초기화, 단 메뉴 박스,모달,headerbutton 제외
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        tableRef.current && 
        !tableRef.current.contains(event.target) && 
        (!menuRef.current || !menuRef.current.contains(event.target)) &&
        !event.target.closest('.header_right_btns button') &&
        !event.target.closest('.Overlay') 
      ) {
        setSelectedRowIndex(null);
        setContextRowIndex(null);
        if (typeof onRowClick === 'function') onRowClick(null); // 열 선택 해제 시 onRowClick에 null 전달
      }
    };
  
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [onRowClick]);
  

  // 툴팁 설정
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

  // 우클릭 메뉴 외부 클릭 시 메뉴 닫기 + 배경색 초기화
  const menuRef = useRef(null);
  useEffect(() => {
    const handleClickOutsideMenu = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setContextMenu(null);
        setContextRowIndex(null); // 우클릭된 행의 배경색 초기화
      }
    };
    document.addEventListener('mousedown', handleClickOutsideMenu);
    return () => {
      document.removeEventListener('mousedown', handleClickOutsideMenu);
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
            {data.length === 0 ? ( // 데이터가 없을 때 메시지 표시
              <tr>
                <td colSpan={columns.length} style={{ textAlign: 'center' }}>
                  내용이 없습니다
                </td>
              </tr>
            ) : (
              data.map((row, rowIndex) => (
                <tr
                  key={rowIndex}
                  onClick={() => {
                    setSelectedRowIndex(rowIndex);
                    setContextRowIndex(null); // 다른 우클릭된 행을 초기화
                    onRowClick(row); // 클릭한 행의 전체 데이터를 onRowClick에 전달
                  }}
                  onContextMenu={(e) => handleContextMenu(e, rowIndex)} // 우클릭 시 메뉴 표시
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
        : 'center',
      verticalAlign: 'middle',
      cursor: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? 'pointer' : 'default',
      color: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? 'blue' : 'inherit',
      fontWeight: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? '800' : 'normal',
    }}
    onClick={(e) => {
      if (row[column.accessor] && clickableColumnIndex.includes(colIndex)) {
        e.stopPropagation();
        if (onClickableColumnClick) {
          onClickableColumnClick(row);
        }
      }
    }}
    onMouseOver={(e) => {
      if (row[column.accessor] && clickableColumnIndex.includes(colIndex)) {
        e.target.style.textDecoration = 'underline';
      }
    }}
    onMouseOut={(e) => {
      if (row[column.accessor] && clickableColumnIndex.includes(colIndex)) {
        e.target.style.textDecoration = 'none';
      }
    }}
  >
    {typeof row[column.accessor] === 'object' ? (
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        {row[column.accessor]}
      </div>
    ) : (
      row[column.accessor]
    )}
  </td>
))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
      {/* 우클릭 메뉴 박스 */}
      {contextMenu && (
        <div ref={menuRef}
          className='my_context_menu'
          style={{
            position: 'absolute',
            top: `${contextMenu.mouseY}px`,
            left: `${contextMenu.mouseX}px`,
            boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.16)',
            fontSize: '0.3rem',
            backgroundColor: 'white',
            border: '1px solid #eaeaea',
            zIndex: '3',
            borderRadius:'1px'
          }}
        >
          {contextMenu.menuItems.map((item, index) => (
            <div key={index}>{item}</div>
          ))} 
        </div>
      )}  
      {/* Tooltip */}
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
