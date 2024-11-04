import {useHostdeviceFromHost, useHostFromCluster, useVMFromCluster} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import React, { useState } from 'react';
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import VmDu from "../../duplication/VmDu";
import PagingTableOuter from "../../table/PagingTableOuter";


const HostDevice = ({ host }) => {


  // 호스트 장치
  const { 
    data: hostDevices,     
    status: hostDevicesStatus,  
    isLoading: isHostDevicesLoading,  
    isError: isHostDevicesError       
  } = useHostdeviceFromHost(host?.id, toTableItemPredicateHostDevices);  
  function toTableItemPredicateHostDevices(device) {
    return {
      name: device?.name ?? 'Unknown',
      capability: device?.capability ?? 'Unknown',
      vendorName: device?.vendorName ?? 'Unknown',
      productName: device?.productName ?? 'Unknown',
      driver: device?.driver ?? 'Unknown',
      currentlyUsed: device?.currentlyUsed ?? 'Unknown',
      connectedToVM: device?.connectedToVM ?? 'Unknown',
      iommuGroup: device?.iommuGroup ?? '해당 없음',
      mdevType: device?.mdevType ?? '해당 없음',
    };
  }

    return (
        <>
        <div className="host_empty_outer">
          <PagingTableOuter
            columns={TableColumnsInfo.DEVICE_FROM_HOST} 
            data={hostDevices} 
            onRowClick={() => console.log('Row clicked')} 
            
          />
        </div>
        </>
    );
  };
  
  export default HostDevice;