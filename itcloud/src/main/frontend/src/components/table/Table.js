const Table = ({ columns, data, onRowClick, shouldHighlight1stCol=false }) => {
  return (
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
          <tr key={rowIndex}>
            {columns.map((column, colIndex) => (
              <td
                key={colIndex}
                onClick={() => onRowClick(row, column)}
              >
                {
                  (colIndex == 0 && shouldHighlight1stCol) ? 
                  <span
                      style={{ color: 'blue', cursor: 'pointer'}}
                      onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
                      onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
                  >
                    {row[column.accessor]}
                  </span>
                  : <span>{row[column.accessor]}</span>
                }
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default Table