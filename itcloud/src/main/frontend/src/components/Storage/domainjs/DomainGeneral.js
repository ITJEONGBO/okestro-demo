import React, { useState } from 'react';

const DomainGeneral = ({ domain }) => {
    const [activePopup, setActivePopup] = useState(null); // modal
    const openModal = (popupType) => setActivePopup(popupType);
    const closeModal = () => setActivePopup(null);
    return (
        <>

        <div className="tables">
          <div className="table_storage_domain_detail">
          <table className="table">
            <tbody>
              <tr>
                <th>ID:</th>
                <td>{domain?.id}</td>
              </tr>
              <tr>
                <th>크기:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>사용 가능:</th>
                <td>{domain?.availableSize}</td>
              </tr>
              <tr>
                <th>사용됨:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>할당됨:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>오버 할당 비율:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>이미지:</th>
                <td>{domain?.image}</td>
              </tr>
              <tr>
                <th>경로:</th>
                <td>{domain?.storageAddress}</td>
              </tr>
              <tr>
                <th>NFS 버전:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>디스크 공간 부족 경고 표시:</th>
                <td>#</td>
              </tr>
              <tr>
                <th>심각히 부족한 디스크 공간의 동작 차단:</th>
                <td>#</td>
              </tr>
            </tbody>
          </table>

          </div> 
        </div> 
        </>
    )
  }
  
  export default DomainGeneral     