import React from "react";
import { useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import VnicProfileDupl from "../../network/vnicProfile/VnicProfileDupl";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";

const NetworkVnicProfiles = ({networkId}) => {
  const { 
    data: vnicProfiles, 
  } = useAllVnicProfilesFromNetwork(networkId, (profile) => ({
    ...profile,
    networkVo: profile.networkVo?.name || "N/A", 
    dataCenter: profile.dataCenter?.name || "N/A", 
    networkFilter: profile.networkFilter?.name || "N/A",
  }));
  
  return (
    <>
     <VnicProfileDupl
        columns={TableColumnsInfo.VNIC_PROFILES_FROM_NETWORK}
        vnicProfiles={vnicProfiles || []}
        networkId={networkId}
      />
    </>
  );
};
  
  export default NetworkVnicProfiles;