import React from "react";
import { useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import VnicProfileDupl from "../../duplication/VnicProfileDupl";
import TableColumnsInfo from "../../table/TableColumnsInfo";

const NetworkVnicProfiles = ({networkId}) => {
  const { 
    data: vnicProfiles, 
    refetch: refetchVnicProfiles, 
    isError: isVnicProfilesError, 
    error: vnicProfilesError, 
    isLoading: isVnicProfilesLoading,
  } = useAllVnicProfilesFromNetwork(networkId, (e) => ({...e,}));

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