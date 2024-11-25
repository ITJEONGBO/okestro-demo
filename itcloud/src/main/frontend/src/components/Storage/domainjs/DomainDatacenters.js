import React, { useState } from 'react'; 
import { useNavigate } from 'react-router-dom';
import { useAllDataCenterFromDomain } from "../../../api/RQHook";

const DomainDatacenters = ({ domainId }) => {
  const {
    data: datacenter, // 초기값을 빈 배열로 설정
    status: datacenterStatus,
    isRefetching: isDatacenterRefetching,
    refetch: refetchDatacenter,
    isError: isDatacenterError,
    error: datacenterError,
    isLoading: isDatacenterLoading
  } = useAllDataCenterFromDomain(domainId, (e) => ({
    ...e,
  }));
  const navigate = useNavigate();
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);
  
  const handleNameClick = (id) => {
    navigate(`/storages/datacenters/${id}/clusters`);
  };

  return (
    <>
      <div className="tables">
        <div>
          <table className="table">
            <tbody>
              <tr>
                <th>상태:</th>
                <td>{datacenter?.dataCenterVo.id}</td>
              </tr>
              <tr>
                <th>이름:</th>
                <td>{datacenter?.dataCenterVo.name}</td>
              </tr>
              <tr>
                <th>데이터센터간 도메인 상태:</th>
                <td>{datacenter?.status}</td>
              </tr>
            </tbody>
          </table>
        </div> 
      </div> 
    </>
  );
};
  
export default DomainDatacenters;