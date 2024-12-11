import React from 'react';
import {useHostdeviceFromHost} from "../../../api/RQHook";
import PagingTableOuter from "../../table/PagingTableOuter";
import TableInfo from '../../table/TableInfo';

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
        columns={TableInfo.DEVICE_FROM_HOST} 
        data={hostDevices} 
      />
    </>
  );
};
  
export default HostDevices;