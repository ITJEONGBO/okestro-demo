import Tables from './Tables';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './Table.css';

const TablesOuter = ({ 
  columns, 
  data, 
  onRowClick, 
  shouldHighlight1stCol = false, 
  clickableColumnIndex, 
  showSearchBox = false 
}) => {
  return (
    <div className="section_table_outer">
      {showSearchBox && ( // showSearchBox가 true일 때만 렌더링
        <div className="search_box">
          <input type="text" />
          <button><FontAwesomeIcon icon={faSearch} fixedWidth /></button>
          <button><FontAwesomeIcon icon={faRefresh} fixedWidth /></button>
        </div>
      )}
      
      <Tables 
        columns={columns} 
        data={data}
        onRowClick={onRowClick} 
        clickableColumnIndex={clickableColumnIndex} // 클릭 가능 컬럼 전달
        shouldHighlight1stCol={shouldHighlight1stCol} // 첫 번째 컬럼 강조 여부 (필요 시)
      />
    </div>
  );
}

export default TablesOuter;