import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import '../css/DataCenter.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useHostsFromDataCenter } from '../../../api/RQHook';

const DataCenterHosts = ({datacenterId}) => {
  const {
    data: hosts,
    status: hostsStatus,
    isLoading: isHostsLoading,
    isError: isHostsError,
  } = useHostsFromDataCenter(datacenterId, (e) => ({ 
    ...e 
  }));
  
  const navigate = useNavigate();
  const [selectedHost, setSelectedHost] = useState(null);

  const handleNameClick = (id) => {
    navigate(`/computing/hosts/${id}`);
  };

  return (
    <>
      <span>id = {selectedHost?.id || ''}</span>
      <TablesOuter
        columns={TableInfo.HOSTS}
        data={hosts}
        shouldHighlight1stCol={true}
        onRowClick={(row) => {setSelectedHost(row)}}
        clickableColumnIndex={[0]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
      />

    </>
  );
};

export default DataCenterHosts;