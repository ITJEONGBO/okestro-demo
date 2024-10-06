import React, { useState, useRef } from 'react';
import './Table.css';

const Table = ({ columns, data, onRowClick = () => {}, clickableColumnIndex = [] }) => {
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  const [menuPosition, setMenuPosition] = useState(null); // 우클릭 메뉴 위치 상태
  const [showMenu, setShowMenu] = useState(false); // 우클릭 메뉴 표시 여부 상태
  const tableRef = useRef(null); // 테이블 참조

  const handleContextMenu = (e, rowIndex, cellRef) => {
    e.preventDefault(); // 기본 우클릭 메뉴 차단

    // 셀의 위치를 계산하여 우클릭한 셀의 바로 옆에 메뉴 표시
    const rect = cellRef.current.getBoundingClientRect();
    setMenuPosition({ x: rect.right + 2, y: rect.top + window.scrollY }); // 셀의 오른쪽에 최소한의 여백을 두고 메뉴 표시
    setSelectedRowIndex(rowIndex); // 우클릭한 행을 선택된 행으로 설정
    setShowMenu(true); // 메뉴 보이기
  };

  const handleClickOutside = (e) => {
    if (tableRef.current && !tableRef.current.contains(e.target)) {
      setSelectedRowIndex(null); // 테이블 외부를 클릭했을 때 선택된 행을 초기화
      setShowMenu(false); // 우클릭 메뉴 숨기기
    }
  };

  // 외부 클릭 감지를 위해 이벤트 리스너 추가
  React.useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const onClickDeleteChattingRoom = (e) => {
    e.preventDefault();
    console.log('채팅방 삭제 처리');
    // 여기에서 실제 삭제 로직을 추가하면 됩니다.
    setShowMenu(false); // 메뉴 숨기기
  };

  return (
    <div className="table-container" ref={tableRef}>
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
            <tr
              key={rowIndex}
              onClick={() => setSelectedRowIndex(rowIndex)} // 클릭한 행의 인덱스를 상태에 저장
              style={{
                backgroundColor: selectedRowIndex === rowIndex ? 'rgb(218, 236, 245)' : 'transparent', // 선택된 행의 배경색을 변경
              }}
            >
              {columns.map((column, colIndex) => {
                const cellRef = React.createRef(); // 각 셀에 대한 참조 생성

                return (
                  <td
                    key={colIndex}
                    ref={cellRef}
                    onContextMenu={(e) => handleContextMenu(e, rowIndex, cellRef)} // 우클릭 이벤트 추가
                    style={{
                      textAlign: typeof row[column.accessor] === 'object' ? 'center' : 'left', // 아이콘이면 center, 텍스트면 left
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
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>

      {/* 우클릭 메뉴 표시 */}
      {showMenu && menuPosition && (
        <ul
          className="context-menu"
          style={{
            top: `${menuPosition.y}px`,
            left: `${menuPosition.x}px`,
            position: 'absolute',
            backgroundColor: 'white',
            border: '1px solid black',
            zIndex: 1000,
          }}
        >
          <li onClick={onClickDeleteChattingRoom}>채팅방 삭제</li>
          <li onClick={() => console.log('Option 2 clicked')}>Option 2</li>
          <li onClick={() => console.log('Option 3 clicked')}>Option 3</li>
        </ul>
      )}
    </div>
  );
};

export default Table;
