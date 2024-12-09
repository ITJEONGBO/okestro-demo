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
  action,
  domainId,
  datacenterId,  // 데이터센터
  hostId,  // 호스트
}) => {
  const [formState, setFormState] = useState({
    id: '',
    domainType: 'data', // 기본값 설정
    storageType: 'nfs', // 기본값 설정
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
      abled: e?.logicalUnits[0].storageDomainId === "" ? 'OK' : 'NO',
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
    { value: "data", label: "데이터" },
    { value: "iso", label: "ISO" },
    { value: "export", label: "내보내기" },
  ];

  const storageTypeOptions = (dType) => {
    switch (dType) {
      case 'data':
        return [
          { value: 'nfs', label: 'NFS' },
          { value: 'iscsi', label: 'ISCSI' },
          { value: 'fcp', label: 'Fibre Channel' },
        ];
      case 'iso':
      case 'export':
        return [{ value: 'nfs', label: 'NFS' }];
      default:
        return [
          { value: 'nfs', label: 'NFS' },
          { value: 'iscsi', label: 'ISCSI' },
          { value: 'fcp', label: 'Fibre Channel' },
        ];
    }
  };

  useEffect(() => {
    if (action === 'edit' && domain) {
      const updatedState = {
        id: domain.id || '',
        domainType: domain.domainType || '', 
        storageType: domain.storageType || '', 
        name: domain.name || '',    
        comment: domain.comment || '',
        description: domain.description || '',
        warning: domain.warning || '',
        spaceBlocker: domain.spaceBlocker || '',
      };
      setFormState(updatedState);
      setDataCenterVoId(domain?.dataCenterVo?.id || '');
      setHostVoId(domain?.hostVo?.id || '');
      setHostVoName(domain?.hostVo?.name || '');     
      
      if (updatedState.storageType === 'nfs') {
        setStoragePath(domain?.storagePath || '');
        setStorageAddress(domain?.storageAddress || '');
      } else if (updatedState.storageType === 'iscsi' || updatedState.storageType === 'fcp') {
        setLunId(domain?.logicalUnits[0]?.id || '');
      }
    } else if (action !== 'edit') {
      resetForm();
    }
  }, [action, domain]);  
  
  
  useEffect(() => {
    if (action !== 'edit' && dataCenters && dataCenters.length > 0) {
      setDataCenterVoId(dataCenters[0].id);
    }
  }, [dataCenters, action]);
  
  useEffect(() => {
    if (action !== 'edit' && hosts && hosts.length > 0) {
      setHostVoName(hosts[0].name);
      setHostVoId(hosts[0].id);
    }
  }, [hosts, action]);

  useEffect(() => {
    const options = storageTypeOptions(formState.domainType);
    setStorageTypes(options);
  
    // 기본 storageType을 options의 첫 번째 값으로 설정
    if (action !== 'edit' && options.length > 0) {
      setFormState((prev) => ({
        ...prev,
        storageType: options[0].value,
      }));
    }
  }, [formState.domainType, action]);
  
  const resetForm = () => {
    setFormState({
      id: '',
      domainType: 'data', // 도메인 유형 기본값 설정
      storageType: 'nfs', // 스토리지 유형 기본값 설정
      name: '',
      comment: '',
      description: '',
      warning: '10',
      spaceBlocker: '5',
    });
    setStorageTypes(['nfs', 'iscsi', 'fcp']); // 기본 도메인 유형에 따른 스토리지 유형
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
    if (action === 'create' && !hostVoName) return '호스트를 선택해주세요.';
    if (formState.storageType === 'NFS' && !storagePath) return '경로를 입력해주세요.';
    if (action === 'create' && formState.storageType !== 'nfs' && lunId) {
      const selectedLogicalUnit =
        formState.storageType === 'iscsi'
          ? iscsis.find((iLun) => iLun.id === lunId)
          : fibres.find((fLun) => fLun.id === lunId);
      if (selectedLogicalUnit?.abled === 'NO') return '선택한 항목은 사용할 수 없습니다.';
    }
    return null;
  };

  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }
  
    let dataToSubmit;
  
    if (action === 'edit') {
      // 'edit' 액션에서는 formState 데이터만 제출
      dataToSubmit = {
        ...formState,
      };
    } else {
      // 'create' 또는 기타 액션에서는 데이터센터와 호스트 정보를 포함
      if (!dataCenters || dataCenters.length === 0) {
        alert("데이터 센터가 로드되지 않았습니다.");
        console.error("dataCenters is empty or undefined:", dataCenters);
        return;
      }
  
      if (!hosts || hosts.length === 0) {
        alert("호스트가 로드되지 않았습니다.");
        console.error("hosts is empty or undefined:", hosts);
        return;
      }
  
      const selectedDataCenter = dataCenters.find((dc) => dc.id === dataCenterVoId);
      if (!selectedDataCenter) {
        alert("선택된 데이터 센터가 유효하지 않습니다.");
        console.error("selectedDataCenter is undefined:", { dataCenterVoId, dataCenters });
        return;
      }
  
      const selectedHost = hosts.find((h) => h.name === hostVoName);
      if (!selectedHost) {
        alert("선택된 호스트가 유효하지 않습니다.");
        console.error("selectedHost is undefined:", { hostVoName, hosts });
        return;
      }
  
      const logicalUnit =
        formState.storageType === 'iscsi'
          ? iscsis.find((iLun) => iLun.id === lunId)
          : formState.storageType === 'fcp'
          ? fibres.find((fLun) => fLun.id === lunId)
          : null;
  
      dataToSubmit = {
        ...formState,
        dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
        hostVo: { id: selectedHost.id, name: selectedHost.name },
        logicalUnits: logicalUnit ? [logicalUnit.id] : [],
        ...(formState.storageType === 'nfs' && { storageAddress, storagePath }),
      };
    }
  
    console.log('Data to submit:', dataToSubmit);
  
    if (action === 'edit') {
      editDomain(
        { domainId: formState.id, domainData: dataToSubmit },
        {
          onSuccess: () => {
            alert('도메인 편집 완료');
            onRequestClose();
          },
        }
      );
    } else if (action === 'create') {
      addDomain(dataToSubmit, {
        onSuccess: () => {
          alert('도메인 생성 완료');
          onRequestClose();
        },
      });
    } else {
      // 기타 액션 처리
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
          <h1>
          {action === "create"
            ? "새로운 도메인 생성"
            : action === "edit"
            ? "도메인 편집"
            : "도메인 가져오기"
          }
          </h1>
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
                disabled={action === 'edit'}
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
              disabled={action === 'edit'}
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
              onChange={(e) =>
                setFormState((prev) => ({ ...prev, storageType: e.target.value }))
              }
              disabled={action === 'edit'}
            >
              {storageTypes.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
          </FormGroup>

          <FormGroup label="호스트">
            <select
              value={hostVoName}
              onChange={(e) => setHostVoName(e.target.value)}
              disabled={action === 'edit'}
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

      {(formState.storageType === 'nfs' || domain?.storageType === 'nfs') && (
        <div className="storage_popup_iSCSI">
          <div className="network_form_group">
            <label htmlFor="NFSPath">NFS 서버 경로</label>
            {action === 'edit' ? (
              <input
                type="text"
                placeholder="예: myserver.mydomain.com"
                value={storageAddress}
                onChange={(e) => setStorageAddress(e.target.value)}
                readOnly
              />
            ) : (
              <>
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
              </>
            )}
          </div>
        </div>
      )}
      
      {(formState.storageType === 'iscsi' || domain?.storageType === 'iscsi') && (
        <div className="storage_popup_iSCSI">
           <div className="tab_content">
              {isIscsisLoading ? (
                  <div className="loading-message">로딩 중...</div>
                ) : isIscsisError ? (
                  <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
                ) : (
                  <>
                  <Table
                    columns={TableInfo.LUNS_TARGETS}
                    data={iscsis}
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                  />
                  <div>
                    <span>id: {lunId}</span>
                  </div>
                  </>
                )}
            </div>
        </div>
      )}
      
      {(formState.storageType === 'fcp' || domain?.storageType === 'fcp') && (
        <div className="tab_content">
          {isFibresLoading ? (
              <div className="loading-message">로딩 중...</div>
            ) : isFibresError ? (
              <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
            ) : (
              <Table
                columns={TableInfo.FIBRE}
                data={fibres}
                onRowClick={handleRowClick}
                shouldHighlight1stCol={true}
              />
            )}
            <div>
              <span>${lunId}</span>
            </div>
        </div>
      )}
      
      <div><br/></div>

      <div className="storage_specific_content">
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
      
    </div>

      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button onClick={handleFormSubmit}>{action === 'edit' ? '편집' : '완료'}</button>
        <button onClick={onRequestClose}>취소</button>
      </div>
    </div>
    </Modal>
  );
};

export default DomainModal;
