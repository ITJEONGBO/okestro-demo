import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import TableOuter from '../../table/TableOuter';
import { useNetworkInterfaceFromHost } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import NetworkHostModal from '../../Modal/NetworkHostModal';
import { renderUpDownStatusIcon } from '../../util/format';

const HostNics = ({ hostId }) => {

  const [isModalOpen, setIsModalOpen] = useState(false);
  const { 
    data: nics = [] 
  } = useNetworkInterfaceFromHost(hostId, (e) => ({ 
    // 단위 변환은 나중에
    ...e,
    id: e?.id,
    name: e?.name,
    bridged: e?.bridged,
    ipv4: e?.ipv4 || '',
    ipv6: e?.ipv6 || '',
    macAddress: e?.macAddress,
    mtu: e?.mtu,
    status: e?.status,
    icon: renderUpDownStatusIcon(e?.status),
    speed: e?.speed <= 0 ? '< 1' : e?.speed, // Mbps 단위 변환 후 숫자로만 반환
    rxSpeed: e?.rxSpeed <= 0 ? '< 1' : e?.rxSpeed, // Rx 속도 Mbps 숫자만 반환
    txSpeed: e?.txSpeed <= 0 ? '< 1' : e?.txSpeed, // Tx 속도 Mbps 숫자만 반환
    rxTotalSpeed: e?.rxTotalSpeed || 0, // 총 Rx 속도 GB 숫자만 반환
    txTotalSpeed: e?.txTotalSpeed || 0, // 총 Tx 속도 GB 숫자만 반환
    rxTotalError: e?.rxTotalError || 0, // Rx 에러
    txTotalError: e?.txTotalError || 0, // Tx 에러
    hostName: e?.hostVo?.name || '', // 호스트 이름
    hostId: e?.hostVo?.id || '', // 호스트 ID
    networkName: e?.networkVo?.name || '', // 네트워크 이름
    networkId: e?.networkVo?.id || '' // 네트워크 ID
  }));
  
  
  // 네트워크인터페이스 박스열고닫기
  const [visibleBoxes, setVisibleBoxes] = useState([]);

  const toggleHiddenBox = (index) => {
    setVisibleBoxes((prevVisibleBoxes) => {
      if (prevVisibleBoxes.includes(index)) {
        return prevVisibleBoxes.filter((i) => i !== index); // 이미 열려 있으면 닫기
      } else {
        return [...prevVisibleBoxes, index]; // 아니면 열기
      }
    });
  };

  // 모든 박스를 확장 또는 숨기기
  const toggleAllBoxes = () => {
    if (visibleBoxes.length === nics.length) {
      setVisibleBoxes([]); // 모두 닫기
    } else {
      setVisibleBoxes(nics.map((_, index) => index)); // 모두 열기
    }
  };

  console.log('networkInterfaceData:', nics);
  console.log('networkdata:', nics);
  
  // 연필 추가모달
  const [isSecondModalOpen, setIsSecondModalOpen] = useState(false);
  useEffect(() => {
    if (isSecondModalOpen) {
      handleTabModalClick('ipv4');
    }
  }, [isSecondModalOpen]);

  const [selectedModalTab, setSelectedModalTab] = useState('ipv4');
  const handleTabModalClick = (tab) => {
    setSelectedModalTab(tab);
  };

  // 추가 모달 닫기 핸들러
  const closeSecondModal = () => {
    setIsSecondModalOpen(false);
    setSelectedModalTab('ipv4'); // 모달이 닫힐 때 첫 번째 탭으로 초기화
  };

  const [selectedTab, setSelectedTab] = useState('network_new_common_btn');
  const [activePopup, setActivePopup] = useState(null);
      // 모달 관련 상태 및 함수
  const openPopup = (popupType) => {
    setActivePopup(popupType);
    setSelectedTab('network_new_common_btn'); // 모달을 열 때마다 '일반' 탭을 기본으로 설정
  };
  const closePopup = () => setActivePopup(null);
  const [activeButton, setActiveButton] = useState('network');
  const [isLabelVisible, setIsLabelVisible] = useState(false); // 라벨 표시 상태 관리

  const handleButtonClick = (button) => {
    setActiveButton(button);
    setIsLabelVisible(button === 'label'); // 'label' 버튼을 클릭하면 라벨을 표시
  };


  return (
    <>
      <div className="header_right_btns">
        <button>VF 보기</button>
        <button onClick={toggleAllBoxes}>
          {visibleBoxes.length === nics.length ? '모두 숨기기' : '모두 확장'}
        </button>
        <button onClick={() => setIsModalOpen(true)}>호스트 네트워크 설정</button>
        <button className="disabled">네트워크 설정 저장</button>
        <button className="disabled">모든 네트워크 동기화</button>
      </div>
              
      {nics.map((data, index) => (
        <div className="host_network_boxs" key={index} style={{ marginBottom: '0.2rem' }}>

          <div
            className="host_network_firstbox"
            onClick={() => toggleHiddenBox(index)} // 클릭 시 해당 박스만 열리거나 닫힘
          >
            <div className="section_table_outer">
              <TableOuter
                columns={TableInfo.HOST_NETWORK_INTERFACE}
                data={[data]} // 개별 NIC 데이터만 전달
                onRowClick={() => console.log('Row clicked')}
              />
            </div>
          </div>
          {visibleBoxes.includes(index) && ( // 박스가 열려 있을 때만 보임
            <div className="host_network_hiddenbox">
              <div className="section_table_outer" style={{ marginLeft: '1.43rem' }}>
                <TableOuter
                  columns={TableInfo.NETWORKS_FROM_HOST}
                  data={[data]} // 개별 NIC 데이터만 전달
                  onRowClick={() => console.log('Row clicked')}
                />
              </div>
            </div>
          )}
        </div>
      ))}

      <NetworkHostModal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        hostId={nics} 
      />
    </>
  );
};

export default HostNics;
  