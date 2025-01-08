import React from "react";
import { useAllVnicProfilesFromNetwork } from "../../../api/RQHook";
import VnicProfileDupl from "../../network/vnicProfile/VnicProfileDupl";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";

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