import React from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllVnicProfiles } from '../../../api/RQHook';
import VnicProfileDupl from '../../duplication/VnicProfileDupl';

const VnicProfiles = () => {
  const { 
    data: vnicProfiles, 
    refetch: refetchVnicProfiles, 
    isError: isVnicProfilesError, 
    error: vnicProfilesError, 
    isLoading: isVnicProfilesLoading,
  } = useAllVnicProfiles((e) => ({...e,}));


  return (
    <>
      <VnicProfileDupl
        columns={TableInfo.VNIC_PROFILES}
        vnicProfiles={vnicProfiles || []}
      />
    </>
  );
};

export default VnicProfiles;
