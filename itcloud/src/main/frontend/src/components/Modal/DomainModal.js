import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import Table from '../table/Table';
import TableInfo from '../table/TableInfo';
import { 
  useAddDomain, 
  useAllDataCenters, 
  useDomainById, 
  useEditDomain ,
  useHostsFromDataCenter,
  useDataCenter
} from '../../api/RQHook';

const FormGroup = ({ label, children }) => (
  <div className="domain_new_select">
    <label>{label}</label>
    {children}
  </div>
);

const DomainModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  domainId,
  datacenterId,  // 데이터센터
  hostId,  // 호스트
}) => {
  const [formState, setFormState] = useState({
    id: '',
    domainType: 'DATA', // 기본값 설정
    storageType: 'NFS', // 기본값 설정
    name: '',    
    comment: '',
    description: '',
    warning: '',
    spaceBlocker: '',
    storagePath: '' , //nfs
    storageAddress: '',
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');  
  const [hostVoName, setHostVoName] = useState('');
  const [hostVoId, setHostVoId] = useState('');
  const [storageTypes, setStorageTypes] = useState([]);

  const { mutate: addDomain } = useAddDomain();
  const { mutate: editDomain } = useEditDomain();


  // 도메인 데이터 가져오기
  const {
    data: domain,
    refetch: refetchDomain,
    isLoading: isDomainLoading,
  } = useDomainById(domainId);

  // 데이터센터 가져오기
  const {
    data: dataCenters = [],
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({...e,}));

  const {
    data: dataCenter,
    refetch: refetchDataCenter,
    isLoading: isDataCenterLoading,
  } = useDataCenter(datacenterId);

  // 호스트 가져오기
  const {
    data: hosts = [],
    refetch: refetchHosts,
    isLoading: isHostsLoading,
  } = useHostsFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  const {
    data: iscsis = [],
    refetch: refetchIscsis,
    isLoading: isIscsisLoading,
  } = useIscsiFromHost(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  const [activeTab, setActiveTab] = useState('target_lun'); // 기본 탭 설정
  const handleTabChange = (tab) => {setActiveTab(tab);};

  const domainTypes = [
    { value: "DATA", label: "데이터" },
    { value: "ISO", label: "ISO" },
    { value: "EXPORT", label: "내보내기" },
  ];

  const storageTypeOptions = (dType) => {
    switch (dType) {
      case 'DATA':
        return ['NFS', 'iSCSI', 'fc'];
      case 'ISO':
        return ['NFS'];
      case 'EXPORT':
        return ['NFS'];
      default: 
        return ['NFS', 'iSCSI', 'fc'];
    }
  };
  
  useEffect(() => {
    if (editMode && domain) {
      console.log('Setting edit mode state with domain:', domain); // 디버깅 로그
      setFormState({
        id: domain.id || '',
        domainType: domain.domainType || '',
        storageType: domain.storageType || '',
        name: domain.name || '',    
        comment: domain.comment || '',
        description: domain.description || '',
        warning: domain.warning || '',
        spaceBlocker: domain.spaceBlocker || '',
        storagePath: domain.storagePath || '',
        // storageAddress
      });
      setDataCenterVoId(domain?.datacenterVo?.id);
      setHostVoName(domain?.hostVo?.name);
    } else if (!editMode && !isDatacentersLoading) {
      resetForm();
      setDataCenterVoId(datacenterId);
      setFormState((prev) => ({
        ...prev,
        domainType: 'DATA', // 도메인 유형 기본값 설정
        storageType: 'NFS', // 스토리지 유형 기본값 설정
      }));
    }
  }, [editMode, domain, datacenterId]);

  
  useEffect(() => {
    if (!editMode && dataCenters && dataCenters.length > 0) {
      setDataCenterVoId(dataCenters[0].id);
    }
  }, [dataCenters, editMode]);
  
  useEffect(() => {
    if (!editMode && hosts && hosts.length > 0) {
      setHostVoName(hosts[0].name);
    }
  }, [hosts, editMode]);

  useEffect(() => {
    const options = storageTypeOptions(formState.domainType);
    setStorageTypes(options);
    if (!editMode) {
      setFormState((prev) => ({
        ...prev,
        storageType: 'NFS',
      }));
    } else if (editMode && options.length > 0 && !options.includes(formState.storageType)) {
      // 현재 CPU 유형이 새 옵션 리스트에 없으면 초기화
      setFormState((prev) => ({
        ...prev,
        storageType: 'NFS',
      }));
    }
  }, [formState.domainType, editMode]);

  const resetForm = () => {
    setFormState({
      id: '',
      domainType: 'DATA', // 도메인 유형 기본값 설정
      storageType: 'NFS', // 스토리지 유형 기본값 설정
      name: '',
      comment: '',
      description: '',
      warning: '',
      spaceBlocker: '',
      storagePath: '', // NFS 기본값
      storageAddress: '',
    });
    setStorageTypes(['NFS', 'iSCSI', 'fc']); // 기본 도메인 유형에 따른 스토리지 유형
    setDataCenterVoId('');
    setHostVoName('');
  };

  const validateForm = () => {
    if (!formState.name) return '이름을 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    if (!hostVoName) return '호스트를 선택해주세요.';
    // if (!formState.storagePath) return '경로를 입력해주세요';
    return null;
  };

  const handleFormSubmit = () => {
    // const error = validateForm();
    // if (error) {
    //   alert(error);
    //   return;
    // }
    const selectedDataCenter = dataCenters.find((dc) => dc.id === dataCenterVoId);
    const selectedHost = hosts.find((h) => h.name === hostVoName);;

    const dataToSubmit = {
      dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      hostVo: { id: selectedHost.id, name: selectedHost.name },
      ...formState,
    };

    console.log('Data to submit:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인

    if (editMode) {
      editDomain(
        {domainId: formState.id, domainData: dataToSubmit}, 
        {
          onSuccess: () => {
            alert('도메인 편집 완료');
            onRequestClose();
          },
        });
    } else {
      addDomain(dataToSubmit, {
        onSuccess: () => {
          alert('도메인 생성 완료');
          onRequestClose();
        },
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

          <FormGroup label="데이터 센터">
            {datacenterId ? (
              <input 
                type="text" 
                value={dataCenter?.name} 
                readOnly 
              />
            ) : (
              <select
                value={dataCenterVoId}
                onChange={(e) => setDataCenterVoId(e.target.value)}
                disabled={editMode}
              >
                {dataCenters && dataCenters.map((dc) => (
                  <option key={dc.id} value={dc.id}>
                    {dc.name} : {dataCenterVoId}
                  </option>
                ))}
              </select>
            )}
          </FormGroup>

          <FormGroup label="도메인 유형">
            <select
              value={formState.domainType}
              onChange={(e) => setFormState((prev) => ({ ...prev, domainType: e.target.value }))}
              disabled={editMode}
            >
              {domainTypes.map((type) => (
                <option key={type.value} value={type.value}>
                  {type.label}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="스토리지 유형">
            <select
              value={formState.storageType}
              onChange={(e) =>setFormState((prev) => ({ ...prev, storageType: e.target.value }))}
            >
              {storageTypes.map((opt) => (
                <option key={opt} value={opt}>
                  {opt}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="호스트">
            <select
              value={hostVoName}
              onChange={(e) => setHostVoName(e.target.value)}
              disabled={editMode}
            >
              {hosts && hosts.map((h) => (
                <option key={h.name} value={h.name}>
                  {h.name} : {h.id}
                </option>
              ))}
            </select>
          </FormGroup>
      </div>

      <div className="domain_new_right">
        <FormGroup label="이름">
          <input
            type="text"
            value={formState.name}
            onChange={(e) => setFormState((prev) => ({ ...prev, name: e.target.value }))}
          />
        </FormGroup>
        
        <FormGroup label="설명">
          <input
            type="text"
            value={formState.description}
            onChange={(e) => setFormState((prev) => ({ ...prev, description: e.target.value }))}
          />
        </FormGroup>

        <FormGroup label="코멘트">
          <input
            type="text"
            value={formState.comment}
            onChange={(e) => setFormState((prev) => ({ ...prev, comment: e.target.value }))}
          />
        </FormGroup>
      </div>
    </div>

    <div className="storage_specific_content">
      {formState.storageType === 'NFS' && (
        <div className="storage_popup_NFS">
          <div className="network_form_group">
            <label htmlFor="nfsPath">NFS 서버 경로</label>
            <input
              type="text"
              placeholder="예: myserver.mydomain.com"
              value={formState.storageAddress}
              onChange={(e) => setFormState((prev) => ({ ...prev, storageAddress: e.target.value }))}
            />
            <input
              type="text"
              placeholder="예: /my/local/path"
              value={formState.storagePath}
              onChange={(e) => setFormState((prev) => ({ ...prev, storagePath: e.target.value }))}
            />
          </div>
        </div>
      )}

      {formState.storageType === 'iSCSI' && (
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
              <Table
                columns={TableInfo.ISCSI}
                // data={}
                onRowClick={() => {}}
                shouldHighlight1stCol={true}
              />
            </div>
          )}

          {activeTab === 'lun_target' && (
            <div className="tab_content">
              <p>LUN 관련 설정 화면 (LUN - 대상)</p>
            </div>
          )}
        </div>
      )}

      {formState.storageType === 'fc' && (
        <div className="storage_popup_fc">
          <div className="fc_table">
            <span>fc </span>
            <Table
              columns={TableInfo.ISCSI}
              // data={}
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

export default DomainModal;
