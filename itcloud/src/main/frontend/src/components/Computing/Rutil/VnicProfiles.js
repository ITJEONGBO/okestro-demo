import React from 'react';
import '../css/Computing.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllVnicProfiles } from '../../../api/RQHook';
import VnicProfileDupl from '../../duplication/VnicProfileDupl';

const VnicProfiles = () => {
  const { 
    data: vnicProfiles = [], 
    refetch: refetchVnicProfiles, 
    isError: isVnicProfilesError, 
    error: vnicProfilesError, 
    isLoading: isVnicProfilesLoading,
  } = useAllVnicProfiles((e) => ({...e,}));


  return (
    <>
      <VnicProfileDupl
        columns={TableColumnsInfo.VNIC_PROFILES}
        vnicProfiles={vnicProfiles || []}
      />
    </>
  );
};

export default VnicProfiles;
