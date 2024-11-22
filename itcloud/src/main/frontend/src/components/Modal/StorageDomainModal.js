import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { 
  useAddDomain, 
  useAllDataCenters, 
  useDomainById, 
  useEditDomain ,
  useHostsFromDataCenter,
  useDataCenter
} from '../../api/RQHook';

const StorageDomainsModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  domainId,
  datacenterId,  // 데이터센터
  hostId,  // 호스트
}) => {
  const [id, setId] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [domainType, setDomainType] = useState('');
  const [storageType, setStorageType] = useState('');
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [warning, setWarning] = useState('');
  const [hostVoName, setHostVoName] = useState('');
  const [spaceBlocker, setSpaceBlocker] = useState('');

  const { mutate: addDomain } = useAddDomain();
  const { mutate: editDomain } = useEditDomain();

  // 도메인 데이터 가져오기
  const {
    data: domain,
    status: domainStatus,
    isRefetching: isDomainRefetching,
    refetch: domainRefetch,
    isError: isDomainError,
    error: domainError,
    isLoading: isDomainLoading,
  } = useDomainById(domainId);

  // 데이터센터 가져오기
  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({
    ...e,
  }));

  const {
    data: dataCenter,
    status: dataCenterStatus,
    isRefetching: isDataCenterRefetching,
    refetch: dataCenterRefetch,
    isError: isDataCenterError,
    error: dataCenterError,
    isLoading: isDataCenterLoading,
  } = useDataCenter(datacenterId);

  // 호스트 가져오기
  const {
    data: hosts,
    status: hostsStatus,
    refetch: refetchHosts,
    isLoading: isHostsLoading,
    isError: isHostsError,
  } = useHostsFromDataCenter(datacenterVoId || 'default-datacenter-id', (e) => ({
    ...e,
  }));

  const [activeTab, setActiveTab] = useState('target_lun'); // 기본 탭 설정

  const handleTabChange = (tab) => {
    setActiveTab(tab); // 탭 변경 시 상태 업데이트
  };
  
  useEffect(() => {
    if (!datacenterVoId && datacenters?.length > 0) {
      setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터를 기본값으로 설정
    }
  }, [datacenters]);

  useEffect(() => {
    if (datacenterVoId) {
      refetchHosts({ datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setHostVoName(res.data[0].name); // 첫 번째 호스트를 기본값으로 설정
        }
      }).catch((error) => {
        console.error('Error fetching hosts:', error);
      });
    }
  }, [datacenterVoId]);

  const handleDomainTypeChange = (event) => {
    setDomainType(event.target.value);
  
    // domainType 변경 시 storageType 기본값 설정
    if (event.target.value === 'ISO' || event.target.value === 'EXPORT') {
      setStorageType('NFS'); // ISO와 EXPORT에서는 NFS만 선택 가능
    } else {
      setStorageType(''); // DATA 타입일 경우 초기화
    }
  };
  
  const handleStorageTypeChange = (event) => {
    setStorageType(event.target.value);
  };
  
  // 스토리지 유형 옵션 필터링
  const getStorageTypeOptions = () => {
    if (domainType === 'ISO' || domainType === 'EXPORT') {
      return ['NFS']; // ISO나 EXPORT일 경우 NFS만 허용
    }
    return ['NFS', 'iSCSI', 'fc']; // DATA일 경우 모든 옵션 허용
  };
  
  
  // 모달이 열릴 때 기존 데이터를 상태에 설정
  useEffect(() => {
    if (isOpen) { // 모달이 열릴 때 상태를 설정
      if (editMode && domain) {
        console.log('Setting edit mode state with domain:', domain); // 디버깅 로그
        setId(domain?.id);
        setDatacenterVoId(domain?.datacenterVo?.id);
        setName(domain?.name);
        setDescription(domain?.description);
        setComment(domain?.comment);
        setDomainType(domain?.domainType);
        setStorageType(domain?.storageType);
      } else if (!editMode) { // 생성 시
        resetForm();
        if (datacenterId) {
          setDatacenterVoId(datacenterId);
        } else if (datacenters && datacenters.length > 0) {
          setDatacenterVoId(datacenters[0].id);
        }
      }
    }
  }, [isOpen, editMode, domain, datacenters, datacenterId]);


  const resetForm = () => {
    setDatacenterVoId('');
    setName('');
    setDescription('');
    setComment('');
    setHostVoName('');
    setStorageType('NFS');
  };

  const handleFormSubmit = () => {
    const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
    if (!selectedDataCenter) {
      alert("데이터 센터를 선택해주세요.");
      return;
    }
    const selectedHost = hosts.find((host) => host.name === hostVoName);
    if (!selectedHost) {
      alert("호스트를 선택해주세요.");
      return;
    }

    const dataToSubmit = {
      datacenterVo: {
        id: selectedDataCenter.id,
        name: selectedDataCenter.name,
      },
      name,
      description,
      comment,
      hostVo: {
        id: selectedHost.id,
        name: selectedHost.name
      },
    };
    console.log('Data to submit:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인

    if (editMode) {
      dataToSubmit.id = id; // 수정 모드에서는 id를 추가
      editDomain({
        domainId: id,
        domainData: dataToSubmit
      }, {
        onSuccess: () => {
          alert('도메인 편집 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error editing domain:', error);
        }
      });
    } else {
      addDomain(dataToSubmit, {
        onSuccess: () => {
          alert('도메인 생성 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding domain:', error);
        }
      });
    }
  };

  
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="도메인 관리"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_domain_administer_popup">

        <div className="popup_header">
          <h1>{editMode ? '도메인 관리' : '새로운 도메인(가져오기)'}</h1>
          <button onClick={onRequestClose}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
        </div>

        <div className="storage_domain_new_first">
          <div className="domain_new_left">
            <div className="domain_new_select">
              <label htmlFor="data_hub_location">데이터 센터</label>
              {datacenterId ? (
                <input 
                  type="text" 
                  value={dataCenter?.name} 
                  readOnly 
                />
            ) : (
              <select
                id="data_center"
                value={datacenterVoId}
                onChange={(e) => setDatacenterVoId(e.target.value)}
                disabled={editMode}
              >
                {datacenters &&
                  datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name}
                    </option>
                  ))}
              </select>
            )}
            <span>{datacenterVoId}</span>
          </div>
        <div className="domain_new_select">
          <label htmlFor="domainType">도메인 기능</label>
          <select
            id="domainType"
            value={domainType}
            onChange={handleDomainTypeChange}
            disabled={editMode}
          >
            <option value="DATA">데이터</option>
            <option value="ISO">ISO</option>
            <option value="EXPORT">내보내기</option>
          </select>
        </div>

        <div className="domain_new_select">
          <label htmlFor="storageType">스토리지 유형</label>
          <select
            id="storageType"
            value={storageType}
            onChange={handleStorageTypeChange}
          >
            {getStorageTypeOptions().map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        <div className="domain_new_select" style={{ marginBottom: 0 }}>
          <label htmlFor="host">호스트</label>
          <select 
            id="host" 
            value={hostVoName}
            onChange={(e) => setHostVoName(e.target.value)}
            disabled={editMode}
          >
            {hosts && 
              hosts.map((n) => {
                return <option value={n.name}>
                  {n.name}
                </option>
              })
            }
          </select>
        </div>
      </div>

      <div className="domain_new_right">
        <div className="domain_new_select">
          <label>이름</label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)} 
          />
        </div>
        <div className="domain_new_select">
          <label>설명</label>
          <input
            type="text"
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)} 
          />
        </div>
        <div className="domain_new_select">
          <label>코멘트</label>
          <input
            type="text"
            id="comment"
            value={comment}
            onChange={(e) => setComment(e.target.value)} 
          />
        </div>
      </div>
    </div>

        <div className="storage_specific_content">
  {storageType === 'NFS' && (
    <div className="storage_popup_NFS">
      <div className="network_form_group">
        <label htmlFor="nfsPath">NFS 서버 경로</label>
        <input
          type="text"
          id="nfsPath"
          placeholder="예: myserver.mydomain.com/my/local/path"
        />
      </div>
    </div>
  )}

  {storageType === 'iSCSI' && (
    <div className="storage_popup_iSCSI">
      <div className="iscsi_buttons">
        <button
          className={`tab_button ${activeTab === 'target_lun' ? 'active' : ''}`}
          onClick={() => setActiveTab('target_lun')}
        >
          대상 - LUN
        </button>
        <button
          className={`tab_button ${activeTab === 'lun_target' ? 'active' : ''}`}
          onClick={() => setActiveTab('lun_target')}
        >
          LUN - 대상
        </button>
      </div>

      {activeTab === 'target_lun' && (
        <div className="tab_content">
          <p>LUN 관련 설정 화면 (대상 - LUN)</p>
        </div>
      )}

      {activeTab === 'lun_target' && (
        <div className="tab_content">
          <p>LUN 관련 설정 화면 (LUN - 대상)</p>
        </div>
      )}
    </div>
  )}

  {storageType === 'fc' && (
    <div className="storage_popup_fc">
      <div className="fc_table">
        <Table
          columns={TableColumnsInfo.CLUSTERS_ALT}
          data={domain} // 데이터가 적절히 전달되었는지 확인
          onRowClick={() => {}}
          shouldHighlight1stCol={true}
        />
      </div>
    </div>
  )}
</div>


        {/* {storageType === 'NFS' && (
          <div className="storage_popup_NFS">
            <div className ="network_form_group">
              <label htmlFor="data_hub">NFS 서버 경로</label>
              <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
            </div>
            
            <div id="domain_hidden_box">
              <div className="domain_new_select">
                <label htmlFor="nfs_version">NFS 버전</label>
                <select id="nfs_version">
                  <option value="auto_negotiate">자동 협상(기본)</option>
                  <option value="v3">V3</option>
                  <option value="v4">V4</option>
                  <option value="v4_0">V4.0</option>
                  <option value="v4_1">V4.1</option>
                  <option value="v4_2">V4.2</option>
                </select>
              </div>
            </div>
            
            <div id="domain_hidden_box2">
              <div className="domain_new_select">
                <label>디스크 공간 부족 경고 표시(%)</label>
                <input type="text" value="10"/>
              </div>
              <div className="domain_new_select">
                <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                <input type="text" value="5"/>
              </div>
            </div>
          </div>
        )}

        {storageType === 'iSCSI' && (
          <div className="storage_popup_NFS">
            <div className='target_btns flex'> 
              <button 
                className={`target_lun ${activeLunTab === 'target_lun' ? 'active' : ''}`}
                onClick={() => handleLunTabClick('target_lun')}
              >
                대상 - LUN
              </button>
              <button 
                className={`lun_target ${activeLunTab === 'lun_target' ? 'active' : ''}`}
                onClick={() => handleLunTabClick('lun_target')}
              > 
                LUN - 대상
              </button>
            </div>
            
            {activeLunTab === 'target_lun' &&(
              <div className='target_lun_outer'>
                <div className="search_target_outer">
                  <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn4" onClick={toggleDomainHiddenBox4} fixedWidth/>
                  <span>대상 검색</span>
                  <div id="domain_hidden_box4" style={{ display: isDomainHiddenBox4Visible ? 'block' : 'none' }}>
                    <div className="search_target ">
                      <div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                      </div>

                      <div>
                        <div className='input_checkbox'>
                          <input type="checkbox" id="reset_after_deletion"/>
                          <label htmlFor="reset_after_deletion">사용자 인증 :</label>
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                        <div className ="network_form_group">
                          <label htmlFor="data_hub">내보내기 경로</label>
                          <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                        </div>
                      </div>
                    </div>
                    <button>검색</button>
                  </div>
                </div>
                
                <div>
                  <button className='all_login'>전체 로그인</button>
                  <div className='section_table_outer'>
                    <Table
                      columns={TableColumnsInfo.CLUSTERS_ALT} 
                      data={domain} 
                      onRowClick={[]}
                      shouldHighlight1stCol={true}
                    />
                  </div>
                </div>
              </div>
            )}      

            {activeLunTab === 'lun_target' && (
              <div className='lun_target_outer'>
                <div className='section_table_outer'>
                  <Table
                    columns={TableColumnsInfo.CLUSTERS_ALT} 
                    data={domain} 
                    onRowClick={[]}
                    shouldHighlight1stCol={true}
                  />
                </div>
              </div>
            )}
            
            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5} fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                  <input type="text" />
                </div>
              </div>
            </div>
          </div>
        )}

        {storageType === 'fc' && (
          <div className="storage_popup_NFS">
            <div className='section_table_outer'>
              <Table
                columns={TableColumnsInfo.CLUSTERS_ALT} 
                data={domain} 
                onRowClick={[]}
                shouldHighlight1stCol={true}
              />
            </div>
            <div>
              <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5} fixedWidth/>
              <span>고급 매개 변수</span>
              <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label>디스크 공간 부족 경고 표시(%)</label>
                  <input type="text" />
                </div>
                <div className="domain_new_select">
                  <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                  <select id="format_type_selector" disabled>
                    <option value="linux">V5</option>
                  </select>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="reset_after_deletion"/>
                  <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">백업</label>
                </div>
                <div className="hidden_checkbox">
                  <input type="checkbox" id="backup_vault"/>
                  <label htmlFor="backup_vault">삭제 후 폐기</label>
                </div>
              </div>
            </div>
          </div>
        )} */}

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>


    </Modal>
  );
};

export default StorageDomainsModal;
