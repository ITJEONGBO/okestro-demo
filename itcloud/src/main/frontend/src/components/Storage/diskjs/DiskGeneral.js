
const DiskGeneral = ({ disk }) => {
    return (
        <div className="tables">
        <div className="table_container_center">
          <table className="table">
            <tbody>
              <tr>
                <th>별칭:</th>
                <td>{disk?.alias}</td>
              </tr>
              <tr>
                <th>설명:</th>
                <td>{disk?.description}</td>
              </tr>
              <tr>
                <th>ID:</th>
                <td>{disk?.id}</td>
              </tr>
              <tr>
                <th>디스크 프로파일:</th>
                <td>{disk?.diskProfileVo?.name}</td>
              </tr>
              
              <tr>
                <th>가상 크기:</th>
                <td>{disk?.virtualSize}</td>
              </tr>
              <tr>
                <th>실제 크기:</th>
                <td>{disk?.actualSize}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    )
  }
  
  export default DiskGeneral     