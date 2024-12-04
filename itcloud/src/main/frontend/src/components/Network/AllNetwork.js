import React, { useEffect} from 'react';
import {faServer} from '@fortawesome/free-solid-svg-icons'
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import { adjustFontSize } from '../../UIEvent';
import { useAllNetworks } from '../../api/RQHook';
import './css/Network.css';
import TableInfo from '../table/TableInfo';
import NetworkDupl from '../duplication/NetworkDupl';

const AllNetwork = () => {
  const { 
    data: networks,
    status: networksStatus,
    isRefetching: isNetworksRefetching,
    refetch: refetchNetworks, 
    isError: isNetworksError, 
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
