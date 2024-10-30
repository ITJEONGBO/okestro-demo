import Table from './Table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './Table.css';

const TableOuter = ({ 
  columns = [], 
  data = [], 
  shouldHighlight1stCol = false,
  onRowClick,  
  clickableColumnIndex, 
  showSearchBox = false, 
  onContextMenuItems,
  onClickableColumnClick
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
      
      <Table 
        columns={columns}  
        data={data}   // data가 없으면 Table 컴포넌트에서 처리
        onRowClick={onRowClick} 
        clickableColumnIndex={clickableColumnIndex} 
        shouldHighlight1stCol={shouldHighlight1stCol} 
        onContextMenuItems={onContextMenuItems}
        onClickableColumnClick={onClickableColumnClick}
      />
    </div>
  );
}

export default TableOuter;
