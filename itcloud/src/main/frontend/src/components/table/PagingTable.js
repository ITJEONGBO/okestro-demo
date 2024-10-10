import React, { useState, useRef, useEffect } from 'react';
import { Tooltip } from 'react-tooltip'; // react-tooltip의 Tooltip 컴포넌트 사용
import './Table.css';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const PagingTable = ({ columns, data = [], onRowClick = () => {}, clickableColumnIndex = [], itemsPerPage = 10, showSearchBox = true }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedRowIndex, setSelectedRowIndex] = useState(null); // 선택된 행의 인덱스를 관리
  const [tooltips, setTooltips] = useState({}); // 툴팁 상태 관리
  const tableRef = useRef(null); // 테이블을 참조하는 ref 생성

  // data가 undefined 또는 null일 경우 기본값 빈 배열 설정
  const validData = Array.isArray(data) ? data : [];

  // 현재 페이지의 데이터를 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = validData.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(validData.length / itemsPerPage);

  // 페이지 변경 처리 함수
  const handlePageChange = (direction) => {
    if (direction === 'next' && currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    } else if (direction === 'prev' && currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

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
      <div className="pagination">
        <div className="paging_btns">
          {showSearchBox && ( // showSearchBox가 true일 때만 렌더링
            <div className="search_box">
              <input type="text" />
              <button><FontAwesomeIcon icon={faSearch} fixedWidth /></button>
              <button><FontAwesomeIcon icon={faRefresh} fixedWidth /></button>
            </div>
          )}
          <div className="paging_arrows">
            <div className="flex">
              <button
                className="paging_arrow"
                onClick={() => handlePageChange('prev')}
                disabled={currentPage === 1}
              >
                {'<'}
              </button>
              <span>{`${indexOfFirstItem + 1} - ${Math.min(indexOfLastItem, validData.length)} `}</span>
              <button
                className="paging_arrow"
                onClick={() => handlePageChange('next')}
                disabled={currentPage === totalPages}
              >
                {'>'}
              </button>
            </div>
          </div>
        </div>

        <table className="custom-table" ref={tableRef}>
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
                    data-tooltip-id={`tooltip-${rowIndex}-${colIndex}`} // 각 셀에 고유한 tooltip id 설정
                    data-tooltip-content={row[column.accessor]} // 툴팁에 표시할 전체 내용
                    style={{
                      maxWidth: '200px', // 최대 넓이 설정
                      whiteSpace: 'nowrap', // 한 줄로 표시
                      overflow: 'hidden', // 넘치는 텍스트 숨기기
                      textOverflow: 'ellipsis', // 넘치는 텍스트는 ...으로 표시
                      textAlign: typeof row[column.accessor] === 'object' ? 'center' : 'left', // 아이콘이면 center, 텍스트면 left
                    }}
                    onMouseEnter={(e) => handleMouseEnter(e, rowIndex, colIndex, row[column.accessor])} // 마우스를 올렸을 때 툴팁 설정
                    onClick={(e) => {
                      if (clickableColumnIndex.includes(colIndex)) {
                        e.stopPropagation();
                        onRowClick(row, column); // 선택된 행과 해당 컬럼 정보를 전달
                      }
                    }}
                  >
                    {clickableColumnIndex.includes(colIndex) ? (
                      <span style={{ color: 'blue', cursor: 'pointer' }}>
                        {row[column.accessor]}
                      </span>
                    ) : (
                      <span style={{ cursor: 'default' }}>
                        {row[column.accessor]}
                      </span>
                    )}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 각 셀에 대한 Tooltip 컴포넌트 */}
      {currentItems && currentItems.map((row, rowIndex) =>
        columns.map((column, colIndex) => (
          tooltips[`${rowIndex}-${colIndex}`] && (
            <Tooltip
              key={`tooltip-${rowIndex}-${colIndex}`}
              id={`tooltip-${rowIndex}-${colIndex}`}
              place="right"
              effect="solid"
              delayShow={400} // 700ms 지연 후 툴팁 표시
              content={tooltips[`${rowIndex}-${colIndex}`]} // 툴팁에 표시할 내용
            />
          )
        ))
      )}
    </>
  );
};

export default PagingTable;
