import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import Table from '../table/Table';
import TableInfo from '../table/TableInfo';
import { 
  useAddDomain, 
  useAllDataCenters, 
  useDomainById, 
  useEditDomain ,
  useHostsFromDataCenter,
  useDataCenter,
  useIscsiFromHost,
  useFibreFromHost
} from '../../api/RQHook';

Modal.setAppElement('#root');

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
    warning: '10',
    spaceBlocker: '5',
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');  
  const [hostVoName, setHostVoName] = useState('');
  const [hostVoId, setHostVoId] = useState('');
  const [storageTypes, setStorageTypes] = useState([]);

  // nfs
  const [storagePath, setStoragePath] = useState('');
  const [storageAddress, setStorageAddress] = useState('');

  // iscsi, fibre
  const [lunId, setLunId] = useState('');

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

  // iscsi 가져오기
  const {
    data: iscsis = [],
    refetch: refetchIscsis,
    error: isIscsisError,
    isLoading: isIscsisLoading,
  } = useIscsiFromHost(
    hostVoId ? hostVoId : null, (e) => ({
      ...e,
      target: e?.logicalUnits[0].target,
      address: e?.logicalUnits[0].address,
      port: e?.logicalUnits[0].port,

      status: e?.logicalUnits[0].status,
      size: (e?.logicalUnits[0].size ? e?.logicalUnits[0].size / (1024 ** 3) + 'GB': e?.logicalUnits[0].size),
      paths: e?.logicalUnits[0].paths,   
      productId: e?.logicalUnits[0].productId,   
      vendorId: e?.logicalUnits[0].vendorId,
      serial: e?.logicalUnits[0].serial,
    })
  );

  // fibre 가져오기
  const {
    data: fibres = [],
    refetch: refetchFibres,
    error: isFibresError,
    isLoading: isFibresLoading,
  } = useFibreFromHost(
    hostVoId ? hostVoId : null, (e) => ({
      ...e,
      status: e?.logicalUnits[0].status,
      size: (e?.logicalUnits[0].size ? e?.logicalUnits[0].size / (1024 ** 3) + 'GB': e?.logicalUnits[0].size),
      paths: e?.logicalUnits[0].paths,   
      productId: e?.logicalUnits[0].productId,   
      vendorId: e?.logicalUnits[0].vendorId,   
      serial: e?.logicalUnits[0].serial,   
    })
  );

  const [activeTab, setActiveTab] = useState('target_lun'); // 기본 탭 설정
  // const handleTabChange = (tab) => {setActiveTab(tab);};

  const domainTypes = [
    { value: "DATA", label: "데이터" },
    { value: "ISO", label: "ISO" },
    { value: "EXPORT", label: "내보내기" },
  ];

  const storageTypeOptions = (dType) => {
    switch (dType) {
      case 'DATA':
        return ['NFS', 'ISCSI', 'FCP'];
      case 'ISO' || 'EXPORT':
        return ['NFS'];
      default: 
        return ['NFS', 'ISCSI', 'FCP'];
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
      });
      setDataCenterVoId(domain?.datacenterVo?.id);
      setHostVoName(domain?.hostVo?.name);
      setHostVoId(domain?.hostVo?.id);
      if(formState.storageType === 'NFS'){
        setFormState({
          id: domain.id || '',
          domainType: domain.domainType || '',
          storageType: domain.storageType || '',
          name: domain.name || '',    
          comment: domain.comment || '',
          description: domain.description || '',
          warning: domain.warning || '',
          spaceBlocker: domain.spaceBlocker || '',
        });
        setDataCenterVoId(domain?.datacenterVo?.id);
        setHostVoName(domain?.hostVo?.name);
        setHostVoId(domain?.hostVo?.id);
        setStoragePath(domain?.storagePath);
        setStorageAddress(domain?.storageAddress);
      }else if(formState.storageType === 'ISCSI' || formState.storageType === 'FCP'){
        setFormState({
          id: domain.id || '',
          domainType: domain.domainType || '',
          storageType: domain.storageType || '',
          name: domain.name || '',    
          comment: domain.comment || '',
          description: domain.description || '',
          warning: domain.warning || '',
          spaceBlocker: domain.spaceBlocker || '',
        });
        setDataCenterVoId(domain?.datacenterVo?.id);
        setHostVoName(domain?.hostVo?.name);
        setHostVoId(domain?.hostVo?.id);
        setLunId(domain.logicalUnits[0].id);
      }
    } else if (!editMode && !isDatacentersLoading) {
      resetForm();
      setDataCenterVoId(datacenterId);
      setFormState((prev) => ({
        ...prev,
        domainType: 'DATA', // 도메인 유형 기본값 설정
        storageType: 'NFS', // 스토리지 유형 기본값 설정
      }));
    }
  }, [editMode, domain, datacenterId, isDatacentersLoading]);

  
  useEffect(() => {
    if (!editMode && dataCenters && dataCenters.length > 0) {
      setDataCenterVoId(dataCenters[0].id);
    }
  }, [dataCenters, editMode]);
  
  useEffect(() => {
    if (!editMode && hosts && hosts.length > 0) {
      setHostVoName(hosts[0].name);
      setHostVoId(hosts[0].id);
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
      warning: '10',
      spaceBlocker: '5',
    });
    setStorageTypes(['NFS', 'ISCSI', 'FCP']); // 기본 도메인 유형에 따른 스토리지 유형
    setDataCenterVoId('');
    setHostVoName('');
    setHostVoId('');
    setStorageAddress('');
    setStoragePath('');
    setLunId('');
  };

  const handleRowClick = (row) => {
    console.log('선택한 행 데이터:', row);
    setLunId(row.id); // 선택된 LUN ID를 설정
  };

  const validateForm = () => {
    if (!formState.name) return '이름을 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    if (!hostVoName) return '호스트를 선택해주세요.';
    if (formState.storageType === 'NFS' && !storagePath) return '경로를 입력해주세요';
    return null;
  };

  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }
    const selectedDataCenter = dataCenters.find((dc) => dc.id === dataCenterVoId);
    const selectedHost = hosts.find((h) => h.name === hostVoName);
    const logicalUnit = 
      formState.storageType === 'ISCSI'
        ? iscsis.find((iLun) => iLun.id === lunId)
        : formState.storageType === 'FCP'
        ? fibres.find((fLun) => fLun.id === lunId)
        : null;

    console.log(`lun id: ${lunId}`);

    const dataToSubmit = {
      ...formState,
      dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      hostVo: { id: selectedHost.id, name: selectedHost.name },
      logicalUnits: logicalUnit ? [logicalUnit.id] : [], // logicalUnit.id 설정
      ...(formState.storageType === 'NFS' && { storageAddress, storagePath }),
    };

    // let dataToSubmit;

    // if (formState.storageType === 'NFS') {
    //   dataToSubmit = {
    //     dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
    //     hostVo: { id: selectedHost.id, name: selectedHost.name },
    //     storageAddress,
    //     storagePath,
    //     ...formState,
    //   };
    // } else if (formState.storageType === 'ISCSI' || formState.storageType === 'FCP') {
    //   dataToSubmit = {
    //     dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
    //     hostVo: { id: selectedHost.id, name: selectedHost.name },
    //     logicalUnits: [logicalUnit.id],
    //     storageType: formState.storageType === 'ISCSI' ? 'ISCSI' : 'FCP',
    //     ...formState,
    //   };
    // }

    console.log('Data to submit:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인

    if (editMode) {
      editDomain(
        { domainId: formState.id, domainData: dataToSubmit }, 
        {
          onSuccess: () => {
            alert('도메인 편집 완료');
            onRequestClose();
          },
        }
      );
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
              disabled={editMode}
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
        <div className="storage_popup_iSCSI">
          <div className="network_form_group">
            <label htmlFor="nfsPath">NFS 서버 경로</label>
            <input
              type="text"
              placeholder="예: myserver.mydomain.com"
              value={storageAddress}
              onChange={(e) => setStorageAddress(e.target.value)}
            />
            <input
              type="text"
              placeholder="예: /my/local/path"
              value={storagePath}
              onChange={(e) => setStoragePath(e.target.value)}
            />
          </div>
        </div>
      )}
      
      {formState.storageType === 'ISCSI' && (
        <div className="storage_popup_iSCSI">
          {/* <div className="iscsi_buttons">
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
          </div> */}

          {/* {activeTab === 'target_lun' && (
            <div className="tab_content">
              {isIscsisLoading ? (
                  <div className="loading-message">로딩 중...</div>
                ) : isIscsisError ? (
                  <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
                ) : (
                  <Table
                    columns={TableInfo.TARGETS_LUNS}
                    data={iscsis}
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                  />
                )}
            </div>
          )} */}

          {/* {activeTab === 'lun_target' && ( */}
            <div className="tab_content">
              {isFibresLoading ? (
                  <div className="loading-message">로딩 중...</div>
                ) : isFibresError ? (
                  <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
                ) : (
                  <Table
                    columns={TableInfo.LUNS_TARGETS}
                    data={iscsis}
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                  />
                )}
            </div>
          {/* )} */}
        </div>
      )}

      {formState.storageType === 'FCP' && (
        <div className="tab_content">
          {isIscsisLoading ? (
              <div className="loading-message">로딩 중...</div>
            ) : isIscsisError ? (
              <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
            ) : (
              <Table
                columns={TableInfo.FIBRE}
                data={fibres}
                onRowClick={(row) => console.log('선택한 행 데이터:', row)}
                shouldHighlight1stCol={true}
              />
            )}
        </div>
      )}
      <div><br/></div>

      <div className="domain_new_select">
        <label>디스크 공간 부족 경고 표시(%)</label>
        <input
          type="number"
          value={formState.warning}
          onChange={(e) => setFormState((prev) => ({ ...prev, warning: e.target.value }))}
        />
      </div>
      <div className="domain_new_select">
        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
        <input
          type="number"
          value={formState.spaceBlocker}
          onChange={(e) => setFormState((prev) => ({ ...prev, spaceBlocker: e.target.value }))}
        />
      </div>
    </div>

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
