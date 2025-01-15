import React, { useState, useEffect ,useParams, Suspense} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faArrowCircleUp, faChevronRight, faGlassWhiskey, faPlug, faTimes } from  '@fortawesome/free-solid-svg-icons'
import { useNetworkInterfaceFromVM } from '../../../api/RQHook';
import VmNetworkNewInterfaceModal from './modal/VmNetworkNewInterfaceModal';
import DeleteModal from '../../../components/DeleteModal';

const VmNics = ({vmId}) => {
  const { 
    data: nics = [], isLoading: isDisksLoading
  } = useNetworkInterfaceFromVM(vmId, (e) => ({...e}));

  //   , (nic) => ({
  //   ...nic,
  //   id: nic?.id ?? '', 
  //   name: nic?.name ?? '', 
  //   status: nic?.status ?? '',
  //   ipv4: nic?.ipv4 ?? '',
  //   ipv6: nic?.ipv6 ?? '',
  //   macAddress: nic?.macAddress ?? '',
  //   network: nic?.networkVo?.name ?? '',
  //   vnicProfile: nic?.vnicProfileVo?.name ?? '',
  //   interfaceType: nic?.interface_ ?? 'VIRTIO',
  //   linked: nic?.linked ?? false,
  //   rxSpeed: nic?.rxSpeed ?? '',
  //   txSpeed: nic?.txSpeed ?? '',
  //   rxTotalSpeed: nic?.rxTotalSpeed ?? '',
  //   txTotalSpeed: nic?.txTotalSpeed ?? '',
  //   rxTotalError: nic?.rxTotalError ?? '',
  //   txTotalError: nic?.txTotalError ?? ''
  // }));

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedNics, setSelectedNics] = useState(null);
  const toggleModal = (type, isOpen) => {
      setModals((prev) => ({ ...prev, [type]: isOpen }));
  };
  console.log('VM ID:', vmId);


  // API 응답 데이터 확인
  useEffect(() => {
      console.log('네트워크 인터페이스 데이터:', nics);
  }, [nics]);

  const [visibleDetails, setVisibleDetails] = useState([]);
  useEffect(() => {
    setVisibleDetails(Array(3).fill(false)); // 초기 상태: 모든 detail 숨김
  }, []);
    
  // 팝업 열기/닫기 핸들러

  

  const [activePopup, setActivePopup] = useState(null);
  const openPopup = (popupType) => {
    setActivePopup(popupType);
  };
  const closePopup = () => {
    setActivePopup(null);
  };


  const toggleDetails = (id) => {
      setVisibleDetails((prevDetails) => ({
        ...prevDetails,
        [id]: !prevDetails[id]
      }));
    };

  const renderModals = () => (
    <Suspense>
      {(modals.create || (modals.edit && selectedNics)) && (
        <VmNetworkNewInterfaceModal
          isOpen={modals.create || modals.edit}
          onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
          editMode={modals.edit}
          nicData={selectedNics}
          vmId={vmId || null}
          nicId={selectedNics?.id}
        />
      )}
      {modals.delete && selectedNics && (
        <DeleteModal
          isOpen={modals.delete}
          type='NetworkInterface'
          onRequestClose={() => toggleModal('delete', false)}
          contentLabel={'네트워크 인터페이스'}
          data={ selectedNics}
          vmId={vmId}
        />
      )}
    </Suspense>
  );
    
  return (
    <>
    {/* nic 생성, 수정, 삭제 자체는 만드는게 나을듯(많이 쓰임) */}
      <div className="header-right-btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedNics?.id && toggleModal('edit', true)} disabled={!selectedNics?.id}>편집</button>
        <button onClick={() => selectedNics?.id && toggleModal('delete', true)} disabled={!selectedNics?.id}>제거</button>
      </div>
      <span>id = {selectedNics?.id || ''}</span>
      
      <div className='network_interface_outer'>
        {nics?.map((nic, index) => (
          <div className={`network_content2 ${selectedNics?.id === nic.id ? 'selected' : ''}`}
            onClick={() => setSelectedNics(nic)} // NIC 선택 시 상태 업데이트
            key={nic.id}
          >
            <div className="network_content">
              <div>
                <FontAwesomeIcon icon={faChevronRight} onClick={() => toggleDetails(nic.id)} fixedWidth />
                <FontAwesomeIcon icon={faArrowCircleUp} style={{ color: '#21c50b', marginLeft: '0.3rem' }} fixedWidth />
                <FontAwesomeIcon icon={faPlug} fixedWidth />
                <span>{nic?.name || `NIC ${index + 1}`}</span>
              </div>
              <div>
                <div>네트워크 이름</div>
                <div>{nic?.networkVo?.name}</div>
              </div>
              <div>
                <div>IPv4</div>
                <div>{nic?.ipv4 || '해당 없음'}</div>
              </div>
              <div>
                <div>IPv6</div>
                <div>{nic?.ipv6 || '해당 없음'}</div>
              </div>
              <div style={{ paddingRight: '3%' }}>
                <div>MAC</div>
                <div>{nic?.macAddress}</div>
              </div>
            </div>
            <div className="network_content_detail" style={{ display: visibleDetails[nic.id] ? 'flex' : 'none' }}>
              <div className="network_content_detail_box">
                <div>일반</div>
                  <table className="snap_table">
                    <tbody>
                      <tr>
                        <th>연결됨</th>
                        <td>{nic?.linked ? '연결됨' : '연결 안 됨'}</td>
                      </tr>
                      <tr>
                        <th>네트워크 이름</th>
                        <td>{nic?.networkVo?.name || ''}</td>
                      </tr>
                      <tr>
                        <th>프로파일 이름</th>
                        <td>{nic?.vnicProfileVo?.name || ''}</td>
                      </tr>
                      <tr>
                        <th>QoS 이름</th>
                        <td>{nic?.qosName || '해당 없음'}</td>
                      </tr>
                      <tr>
                        <th>링크 상태</th>
                        <td>{nic?.status || ''}</td>
                      </tr>
                      <tr>
                        <th>유형</th>
                        <td>{nic?.interfaceType}</td>
                      </tr>
                      <tr>
                        <th>속도 (Mbps)</th>
                        <td>{nic?.speed || '10000'}</td>
                      </tr>
                      <tr>
                        <th>포트 미러링</th>
                        <td>{nic?.portMirroring || '비활성화됨'}</td>
                      </tr>
                      <tr>
                        <th>게스트 인터페이스 이름</th>
                        <td>{nic?.guestInterfaceName || ''}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              <div className="network_content_detail_box">
                <div>통계</div>
                  <table className="snap_table">
                    <tbody>
                      <tr>
                        <th>Rx 속도 (Mbps)</th>
                        <td>{nic?.rxSpeed || '<1'}</td>
                      </tr>
                      <tr>
                        <th>Tx 속도 (Mbps)</th>
                        <td>{nic?.txSpeed || '<1'}</td>
                      </tr>
                      <tr>
                        <th>총 Rx</th>
                        <td>{nic?.rxTotalSpeed || ''}</td>
                      </tr>
                      <tr>
                        <th>총 Tx</th>
                        <td>{nic?.txTotalSpeed || ''}</td>
                      </tr>
                      <tr>
                        <th>중단 (Pkts)</th>
                        <td>{nic?.rxTotalError || ''}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div className="network_content_detail_box">
                  <div>네트워크 필터 매개변수</div>
                    <table className="snap_table">
                      <tbody>
                      
                      </tbody>
                    </table>
                </div>
              </div>
            </div>
          ))}
        </div>
        
        {/* 모달창 */}
        { renderModals() }
      </>
    );
  };
  export default VmNics;
  