import Table from './Table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './TableOuter.css';

const TableOuter = ({ columns, data, onRowClick, shouldHighlight1stCol = false, clickableColumnIndex = [0] }) => {
  return (
    <div className="section_table_outer">
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
    </div>
  );
}

export default TableOuter;
