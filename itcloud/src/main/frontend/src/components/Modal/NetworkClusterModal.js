import React from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TablesOuter from '../table/TablesOuter';
import TableInfo from '../table/TableInfo';
import { faPlay, faTimes } from '@fortawesome/free-solid-svg-icons';
import { useAllClustersFromNetwork } from '../../api/RQHook';

const NetworkClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  networkId 
}) => {
  const renderStatusIcon = (status, connected) => {
    if (connected && status === 'OPERATIONAL') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (connected && status === 'NON_OPERATIONAL') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    } else {
      return '';
    }
  };

  const { 
    data: clusters 
  } = useAllClustersFromNetwork(networkId, (cluster) => ({
    name: cluster?.name,
    connect: cluster?.connected ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />,
    status: renderStatusIcon(cluster?.networkVo?.status, cluster?.connected),
    required: cluster?.networkVo?.required ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />,
    allAssigned: cluster?.connected ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 모두 할당
    allRequired: cluster?.networkVo?.required ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 모두 필요
    vmNetMgmt: cluster?.networkVo?.usage?.vm ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 가상 머신 네트워크 관리
    networkOutput: cluster?.networkVo?.usage?.display ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 네트워크 출력
    migrationNetwork: cluster?.networkVo?.usage?.migration ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 마이그레이션 네트워크
    glusterNetwork: cluster?.networkVo?.usage?.gluster ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // Gluster 네트워크
    defaultRouting: cluster?.networkVo?.usage?.defaultRoute ? <input type="checkbox" checked disabled /> : <input type="checkbox" disabled />, // 기본 라우팅
    networkRole: [
      cluster?.networkVo?.usage?.management ? '관리' : null,
      cluster?.networkVo?.usage?.display ? '출력' : null,
      cluster?.networkVo?.usage?.migration ? '마이그레이션' : null,
      cluster?.networkVo?.usage?.gluster ? '글러스터' : null,
      cluster?.networkVo?.usage?.defaultRoute ? '기본라우팅' : null,
    ].filter(Boolean).join('/'),
  }));

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="네트워크 관리"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="manage_network_popup">
        <div className="popup_header">
          <h1>네트워크 관리</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        
        <TablesOuter 
          columns={TableInfo.CLUSTERS_POPUP} 
          data={clusters || []}
          onRowClick={() => console.log('Row clicked')} 
        />
        
        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default NetworkClusterModal;
