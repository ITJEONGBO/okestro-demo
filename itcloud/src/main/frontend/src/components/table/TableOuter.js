import Table from './Table'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faRefresh, faSearch } from '@fortawesome/free-solid-svg-icons';
import './TableOuter.css';

const TableOuter = ({ columns, data, onRowClick, shouldHighlight1stCol=false }) => {
  return (
    <div className="section_table_outer">
      <div className="search_box">
        <input type="text" />
        <button><FontAwesomeIcon icon={faSearch} fixedWidth/></button>
        <button><FontAwesomeIcon icon={faRefresh} fixedWidth/></button>
      </div>
      <Table 
        columns={columns} 
        data={data}
        onRowClick={onRowClick} 
        shouldHighlight1stCol={shouldHighlight1stCol}/>
    </div>
  )
}

export default TableOuter