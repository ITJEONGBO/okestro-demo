import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip';
import './Table.css';

const Table = ({ columns = [], data = [], onRowClick = () => {}, clickableColumnIndex = [], onContextMenuItems = false }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null);
  const [tooltips, setTooltips] = useState({});
  const [sortedData, setSortedData] = useState(data); // 초기 데이터 유지
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'asc' });
  const tableRef = useRef(null); 
  const [contextRowIndex, setContextRowIndex] = useState(null);
  const [contextMenu, setContextMenu] = useState(null);

  // 데이터 변경 시 sortedData 초기화
  useEffect(() => {
    setSortedData(data);
  }, [data]);

  // 정렬 함수
  const handleSort = (column) => {
    if (column.isIcon) return; // 아이콘 열은 정렬 기능 제외

    const { accessor } = column;
    const direction = sortConfig.key === accessor && sortConfig.direction === 'asc' ? 'desc' : 'asc';
    setSortConfig({ key: accessor, direction });

    const sorted = [...data].sort((a, b) => {
      const aValue = a[accessor];
      const bValue = b[accessor];
      
      // 빈 값은 항상 리스트의 끝으로 이동
      if (aValue === null || aValue === undefined || aValue === "") return 1;
      if (bValue === null || bValue === undefined || bValue === "") return -1;

      if (aValue < bValue) return direction === 'asc' ? -1 : 1;
      if (aValue > bValue) return direction === 'asc' ? 1 : -1;
      return 0;
    });

    setSortedData(sorted);
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

  return (
    <>
      <div className='custom_outer'>
        <table className="custom-table" ref={tableRef} style={{ tableLayout: 'fixed', width: '100%' }}>
          <thead>
            <tr>
              {columns.map((column, index) => (
                <th 
                  key={index} 
                  onClick={() => handleSort(column)}
                  style={{ 
                    cursor: column.isIcon ? 'default' : 'pointer',
                    width: column.isEmpty || column.isIcon? '20px' : 'auto'
                   }}
                >
                  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                    {column.header}
                    {!column.isIcon && sortConfig.key === column.accessor && (
                      <span>{sortConfig.direction === 'asc' ? '▲' : '▼'}</span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {sortedData.length === 0 ? (
              <tr>
                <td colSpan={columns.length} style={{ textAlign: 'center' }}>내용이 없습니다</td>
              </tr>
            ) : (
              sortedData.map((row, rowIndex) => (
                <tr
                  key={rowIndex}
                  onClick={() => {
                    setSelectedRowIndex(rowIndex);
                    setContextRowIndex(null);
                    onRowClick(row);
                  }}
                  onContextMenu={(e) => handleContextMenu(e, rowIndex)}
                  style={{
                    backgroundColor: selectedRowIndex === rowIndex || contextRowIndex === rowIndex ? 'rgb(218, 236, 245)' : 'transparent',
                  }}
                >
                  {columns.map((column, colIndex) => (
                    <td
                      key={colIndex}
                      data-tooltip-id={`tooltip-${rowIndex}-${colIndex}`}
                      data-tooltip-content={row[column.accessor]}
                      style={{
                        whiteSpace: 'nowrap',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        textAlign: column.isIcon ? 'center' : 'left',
                        verticalAlign: 'middle',
                        cursor: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? 'pointer' : 'default',
                        color: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? 'blue' : 'inherit',
                        fontWeight: row[column.accessor] && clickableColumnIndex.includes(colIndex) ? '800' : 'normal',
                        width: column.isIcon ? '5%' : 'auto',
                        minWidth: column.isIcon ? '30px' : 'auto',
                        maxWidth: column.isIcon ? '30px' : 'auto',
                        padding: column.isIcon ? '0' : 'auto',
                      }}
                      onMouseEnter={(e) => handleMouseEnter(e, rowIndex, colIndex, row[column.accessor])}
                      onClick={(e) => {
                        if (row[column.accessor] && clickableColumnIndex.includes(colIndex)) {
                          e.stopPropagation();
                          onRowClick(row, column, colIndex);
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
      {data && data.map((row, rowIndex) =>
        columns.map((column, colIndex) => (
          tooltips[`${rowIndex}-${colIndex}`] && (
            <Tooltip
              key={`tooltip-${rowIndex}-${colIndex}`}
              id={`tooltip-${rowIndex}-${colIndex}`}
              place="right"
              effect="solid"
              delayShow={400}
              content={tooltips[`${rowIndex}-${colIndex}`]}
            />
          )
        ))
      )}
    </>
  );
};

export default Table;
