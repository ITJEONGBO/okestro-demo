import React, { useState } from 'react';
import './Table.css';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
const PagingTable = ({ columns, data, onRowClick = () => {}, clickableColumnIndex = [], itemsPerPage = 10 }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리

  // 현재 페이지의 데이터를 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = data.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(data.length / itemsPerPage);

  // 페이지 변경 처리 함수
  const handlePageChange = (direction) => {
    if (direction === 'next' && currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    } else if (direction === 'prev' && currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <>
   
      {/* 페이지네이션 UI를 테이블 위로 이동 */}
    <div className="pagination">
      <div className='paging_btns'>
        <div className="search_box">
            <input type="text" />
            <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
            <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
        </div> 
        <div>
           <div className='flex'>
                <button
                className='paging_arrow'
                onClick={() => handlePageChange('prev')}
                disabled={currentPage === 1}
                >
                {'<'}
                </button>
                <span>{`${indexOfFirstItem + 1} - ${Math.min(indexOfLastItem, data.length)} `}</span>
                <button
                className='paging_arrow'
                onClick={() => handlePageChange('next')}
                disabled={currentPage === totalPages}
                >
                {'>'}
                </button>
            </div>
        </div>
      </div>

      <table className="custom-table">
        <thead>
          <tr>
            {columns.map((column, index) => (
              <th key={index}>{column.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {currentItems && currentItems.map((row, rowIndex) => (
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
    </div>
    </>
  );
};

export default PagingTable;
