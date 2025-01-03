import React, { useEffect} from 'react';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import { adjustFontSize } from '../../UIEvent';
import { useAllNetworks } from '../../api/RQHook';
import './css/Network.css';
import NetworkDupl from '../duplication/NetworkDupl';
import TableInfo from '../table/TableInfo';
import {faServer} from '@fortawesome/free-solid-svg-icons'

const AllNetwork = () => {
  const { 
    data: networks,
    refetch: refetchNetworks, 
    error: networksError, 
    isLoading: isNetworksLoading,
  } = useAllNetworks((e) => ({...e,}));

  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

  return (
    <div id="network_section">
      <div>
        <HeaderButton
          titleIcon={faServer}
          title="네트워크"
        />
      </div>
      <div className="host_btn_outer">
        <NetworkDupl
          columns={TableInfo.NETWORKS}
          networks={networks || []}
        />
      </div>
    <Footer/>
  </div>
  );
};

export default AllNetwork;
