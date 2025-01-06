import React from 'react';
import Modal from 'react-modal';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import {useAllVnicProfiles } from '../../api/RQHook';
import { faDesktop, faLaptop } from '@fortawesome/free-solid-svg-icons'; 
import TableColumnsInfo from '../table/TableColumnsInfo';
import VnicProfileDupl from '../duplication/VnicProfileDupl';

// React Modal 설정
Modal.setAppElement('#root');

const AllVnic = () => {

  const { 
    data: vnicProfiles = [], 
  } = useAllVnicProfiles((e) => ({...e,}));


  return (
    <div id="section">
      <HeaderButton
        titleIcon={faLaptop}
        title="VNIC 프로파일"
      />

      <div className="host_btn_outer">
        <VnicProfileDupl
            columns={TableColumnsInfo.VNIC_PROFILES}
            vnicProfiles={vnicProfiles || []}
        />
      </div>
      <Footer/>
    </div>
  );
};

export default AllVnic;
