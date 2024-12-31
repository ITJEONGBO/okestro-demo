import React, { useState } from 'react';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import './css/Vm.css';
import Footer from '../footer/Footer';
import {useAllVMs } from '../../api/RQHook';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';
import TableInfo from '../table/TableInfo';
import VmDupl from '../duplication/VmDupl';

// React Modal 설정
Modal.setAppElement('#root');

const AllVm = () => {
  const { 
    data: vms, 
    refetch: refetchVms, 
    error: vmsError, 
    isLoading: isVmsLoading,
  } = useAllVMs((e) => ({...e,}));

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title="가상머신"
      />

      <div className="host_btn_outer">
        <VmDupl
          vms={vms || []}
          columns={TableInfo.VMS}
        />    
      </div>
      <Footer/>
    </div>
  );
};

export default AllVm;
