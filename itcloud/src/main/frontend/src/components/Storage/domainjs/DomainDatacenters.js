import React, { useState } from 'react'; 
import { useNavigate } from 'react-router-dom';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllDataCenterFromDomain} from "../../../api/RQHook";

const DomainDatacenters = ({ domainId }) => {
  const {
    data: datacenters = [],
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenterFromDomain(domainId, (e) => ({
    ...e,
  }));
  console.log("Fetched datacenters:", datacenters);
  const navigate = useNavigate();
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);
  
  const handleNameClick = (id) => {
    navigate(`/computing/datacenters/${id}/clusters`);
  };

  return (
    <>
      <TablesOuter
        columns={TableInfo.DATACENTERS_FROM_STORAGE_DOMAIN}
        data={datacenters}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDataCenter(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};
  
export default DomainDatacenters;