import React, { useState, useEffect } from 'react';
import Table from './Table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './TableOuter.css';

const TableOuter = ({ columns, data, onRowClick, shouldHighlight1stCol = false, clickableColumnIndex = [0] }) => {
  const [menuPosition, setMenuPosition] = useState(null); // 메뉴박스의 위치 상태
  const [showMenu, setShowMenu] = useState(false); // 메뉴박스 표시 여부 상태

  const handleContextMenu = (e) => {
    e.preventDefault(); // 브라우저 기본 우클릭 메뉴 차단
    setMenuPosition({ x: e.pageX, y: e.pageY }); // 우클릭한 위치에 메뉴박스 표시
    setShowMenu(true); // 메뉴박스 보이도록 설정
  };

  const handleClickOutside = () => {
    setShowMenu(false); // 메뉴박스 숨기기
  };

  // 우클릭 외의 영역을 클릭하면 메뉴박스가 닫히도록 처리
  useEffect(() => {
    if (showMenu) {
      document.addEventListener('click', handleClickOutside);
    } else {
      document.removeEventListener('click', handleClickOutside);
    }

    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, [showMenu]);

  return (
    <div className="section_table_outer" onContextMenu={handleContextMenu}>
      {/* <div className="search_box">
        <input type="text" />
        <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
        <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
      </div> */}
      
      <Table 
        columns={columns} 
        data={data}
        onRowClick={onRowClick} 
        clickableColumnIndex={clickableColumnIndex} // 클릭 가능 컬럼 전달
        shouldHighlight1stCol={shouldHighlight1stCol} // 첫 번째 컬럼 강조 여부 (필요 시)
      />

      {/* 우클릭 시 나타나는 메뉴박스 */}
      {showMenu && (
        <ul 
          className="context-menu" 
          style={{ top: `${menuPosition.y}px`, left: `${menuPosition.x}px` }} // 메뉴박스 위치 설정
        >
          <li>Option 1</li>
          <li>Option 2</li>
          <li>Option 3</li>
        </ul>
      )}
    </div>
  );
};

export default TableOuter;
