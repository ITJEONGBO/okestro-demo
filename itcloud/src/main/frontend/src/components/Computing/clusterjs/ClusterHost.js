import {useHostFromCluster} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import React, { useState } from 'react';
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';


const ClusterHost = ({ cluster }) => {
    const [activePopup, setActivePopup] = useState(null);
    const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
    const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
    // 모달 관련 상태 및 함수
    const openPopup = (popupType) => {
        setActivePopup(popupType);
        setSelectedPopupTab('cluster_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
    };

    const closePopup = () => {
        setActivePopup(null);
    };
    
    const { 
        data: hosts, 
        status: hostsStatus, 
        isLoading: isHostsLoading, 
        isError: isHostsError 
      } = useHostFromCluster(cluster?.id, toTableItemPredicateHosts);
      function toTableItemPredicateHosts(host) {
        return {
          icon: '', 
          name: host?.name ?? 'Unknown',  // 호스트 이름, 없으면 'Unknown'
          hostNameIP: host?.name ?? 'Unknown',
          status: host?.status ?? 'Unknown',  
          loading: `${host?.vmCount ?? 0} 대의 가상머신`, // 0으로 기본값 설정
          displayAddress: host?.displayAddress ?? '아니요',
        };
      }

    return (
        <HostDu 
            data={hosts} 
            columns={TableColumnsInfo.HOSTS_ALL_DATA} 
            openPopup={openPopup}
        />   
    );
  };
  
  export default ClusterHost;