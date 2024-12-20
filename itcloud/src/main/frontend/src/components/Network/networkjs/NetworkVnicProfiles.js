import React from "react";
import { useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import VnicProfileDupl from "../../duplication/VnicProfileDupl";
import TableInfo from "../../table/TableInfo";

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
        columns={TableInfo.VNIC_PROFILES_FROM_NETWORK}
        vnicProfiles={vnicProfiles || []}
      />
    </>
  );
};
  
  export default NetworkVnicProfiles;