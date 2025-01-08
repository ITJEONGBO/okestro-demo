import React, { useState } from 'react';
import Modal from 'react-modal';
import HeaderButton from '../../../components/button/HeaderButton';
import './css/Vm.css';
import Footer from '../../../components/footer/Footer';
import {useAllVMs } from '../../../api/RQHook';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import VmDupl from './VmDupl';

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
          columns={TableColumnsInfo.VMS}
        />    
      </div>
      <Footer/>
    </div>
  );
};

export default AllVm;
