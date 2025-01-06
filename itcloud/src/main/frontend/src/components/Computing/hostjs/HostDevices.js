import React from 'react';
import {useHostdeviceFromHost} from "../../../api/RQHook";
import PagingTableOuter from "../../table/PagingTableOuter";
import TableColumnsInfo from '../../table/TableColumnsInfo';

const HostDevices = ({ hostId }) => {
  const { 
    data: hostDevices,     
    status: hostDevicesStatus,  
    isLoading: isHostDevicesLoading,  
    isError: isHostDevicesError       
  } = useHostdeviceFromHost(hostId, (e) => ({ ...e }));

  return (
    <>
      <PagingTableOuter
        columns={TableColumnsInfo.DEVICE_FROM_HOST} 
        data={hostDevices} 
      />
    </>
  );
};
  
export default HostDevices;