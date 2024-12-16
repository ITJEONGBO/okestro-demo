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
  useFibreFromHost,
  useImportIscsiFromHost,
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

  const editMode = (action === 'edit');

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

  // iscsi 목록 가져오기
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

  // fibre 목록 가져오기
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
  
  // iscsi 목록 가져오기
  const {
    data: iscsi = [],
    refetch: refetchIscsi,
    error: isIscsiError,
    isLoading: isIscsiLoading,
  } = useImportIscsiFromHost(
    hostVoId ? hostVoId : null, (e) => ({
      ...e,
      target: e?.logicalUnits[0].target,
      address: e?.logicalUnits[0].address,
      port: e?.logicalUnits[0].port,
    })
  );

  const domainTypes = [
    { value: "data", label: "데이터" },
    { value: "iso", label: "ISO" },
    { value: "export", label: "내보내기" },
  ];

  const storageTypeOptions = (dType) => {
    switch (dType) {
      case 'iso':
      case 'export':
        return [{ value: 'nfs', label: 'NFS' }];
      default: // data
        return [
          { value: 'nfs', label: 'NFS' },
          { value: 'iscsi', label: 'ISCSI' },
          { value: 'fcp', label: 'Fibre Channel' },
        ];
    }
  };

  const isNfs = formState.storageType === 'nfs' || domain?.storageType === 'nfs';
  const isIscsi = formState.storageType === 'iscsi' || domain?.storageType === 'iscsi';
  const isFibre = formState.storageType === 'fcp' || domain?.storageType === 'fcp';

  useEffect(() => {
    if (editMode && domain) {
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
        setLunId(domain?.hostStorageVo?.logicalUnits[0]?.id || '');
      }
      console.log('-------' + domain?.hostStorageVo?.logicalUnits[0]?.id)
    } else if (!editMode) {
      resetForm();
    }
  }, [editMode, domain]);  
  
  
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
  
    // 기본 storageType을 options의 첫 번째 값으로 설정
    if (!editMode && options.length > 0) {
      setFormState((prev) => ({
        ...prev,
        storageType: options[0].value,
      }));
    }
  }, [formState.domainType, editMode]);
  
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
    // setStorageTypes(['nfs', 'iscsi', 'fcp']); // 기본 도메인 유형에 따른 스토리지 유형
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
    if ((action === 'create' || action === 'imported') && !hostVoName) return '호스트를 선택해주세요.';
    if (formState.storageType === 'NFS' && !storagePath) return '경로를 입력해주세요.';
    if ((action === 'create' || action === 'imported') && formState.storageType !== 'nfs' && lunId) {
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
  
    if (editMode) {
      // 'edit' 액션에서는 formState 데이터만 제출
      dataToSubmit = {
        ...formState,
      };
    } else {
      const selectedDataCenter = dataCenters.find((dc) => dc.id === dataCenterVoId);
      const selectedHost = hosts.find((h) => h.name === hostVoName);

      const logicalUnit =
        formState.storageType === 'iscsi' ? iscsis.find((iLun) => iLun.id === lunId)
          : formState.storageType === 'fcp'? fibres.find((fLun) => fLun.id === lunId)
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
    } else if (action === 'imported') {
      addDomain(dataToSubmit, {
        onSuccess: () => {
          alert('도메인 생성 완료');
          onRequestClose();
        },
      });
    } else if (action === 'create') { // create
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
          <h1>
          {action === "create" ? "새로운 도메인 생성"
            : editMode ? "도메인 편집"
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
              onChange={(e) =>
                setFormState((prev) => ({ ...prev, storageType: e.target.value }))
              }
              disabled={editMode}
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
      
      {isNfs && (
        <div className="storage_popup_iSCSI">
          <div className="network_form_group">
            <label htmlFor="NFSPath" className='label_font_body'>NFS 서버 경로</label>
            {editMode ? (
              <input
                type="text"
                placeholder="예: myserver.mydomain.com"
                value={storageAddress}
                onChange={(e) => setStorageAddress(e.target.value)}
                disabled={editMode}
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
      
      {isIscsi && (        
        <div className="storage_popup_iSCSI">
          <div className="tab_content">
            {isIscsisLoading ? (
              <div className="label_font_body">로딩 중...</div>
            ) : isIscsisError ? (
              <div className="label_font_body">데이터를 불러오는 중 오류가 발생했습니다.</div>
            ) : (
              <>
                { editMode ? (
                  <Table
                    columns={TableInfo.LUNS_TARGETS}
                    data={
                      domain?.hostStorageVo?.logicalUnits?.map((logicalUnit) => ({
                        abled: logicalUnit.storageDomainId === "" ? "OK" : "NO",
                        status: logicalUnit.status,
                        id: logicalUnit.id,
                        size: logicalUnit.size ? `${(logicalUnit.size / (1024 ** 3)).toFixed(2)} GB` : "N/A",
                        paths: logicalUnit.paths || 0,
                        vendorId: logicalUnit.vendorId || "N/A",
                        productId: logicalUnit.productId || "N/A",
                        serial: logicalUnit.serial || "N/A",
                        target: logicalUnit.target || "N/A",
                        address: logicalUnit.address || "N/A",
                        port: logicalUnit.port || "N/A",
                      })) || []
                    }
                    // onRowClick={handleRowClick}
                    // shouldHighlight1stCol={true}
                  />
                ): action === 'create' ? (
                  <Table
                    columns={TableInfo.LUNS_TARGETS}
                    data={iscsis}
                    onRowClick={handleRowClick}
                    shouldHighlight1stCol={true}
                  />
                ): action === 'imported' ? (
                  <>
                    <label className='label_font_name'>대상 검색</label>

                    <FormGroup label="디스크 공간 부족 경고 표시(%)">
                      <input
                        type="number"
                        value={formState.warning}
                        onChange={(e) => setFormState((prev) => ({ ...prev, warning: e.target.value }))}
                      />
                    </FormGroup>
                    
                  </>
                ) : null }
                
              </>
            )}
          </div>
          <div>
                  <span>id: {lunId}</span>
                </div>
        </div>
      )}
      
      {isFibre && (
        <div className="storage_popup_iSCSI">
          <div className="tab_content">
            {isFibresLoading ? (
              <div className="label_font_body">로딩 중...</div>
            ) : isFibresError ? (
              <div className="label_font_body">데이터를 불러오는 중 오류가 발생했습니다.</div>
            ) : (
              <>
                {editMode ? (
                  <Table
                    columns={TableInfo.FIBRE}
                    data={
                      domain?.hostStorageVo?.logicalUnits?.map((logicalUnit) => ({
                        abled: logicalUnit.storageDomainId === "" ? "OK" : "NO",
                        status: logicalUnit.status,
                        id: logicalUnit.id,
                        size: logicalUnit.size ? `${(logicalUnit.size / (1024 ** 3)).toFixed(2)} GB` : "N/A",
                        paths: logicalUnit.paths || 0,
                        vendorId: logicalUnit.vendorId || "N/A",
                        productId: logicalUnit.productId || "N/A",
                        serial: logicalUnit.serial || "N/A",
                        target: logicalUnit.target || "N/A",
                        address: logicalUnit.address || "N/A",
                        port: logicalUnit.port || "N/A",
                      })) || []
                    }
                    // onRowClick={handleRowClick}
                    // shouldHighlight1stCol={true}
                  />
                ): (
                  <Table
                  columns={TableInfo.FIBRE}
                  data={fibres}
                  onRowClick={handleRowClick}
                  shouldHighlight1stCol={true}
                />
                )} 
                <div>
                  <span>id: {lunId}</span>
                </div>
              </> 
            )}
          </div>
        </div>
      )}
      
      <div><hr/></div>

      <div className="tab_content">
        <div className="storage_specific_content">
          <FormGroup>
            <label className='label_font_body'>디스크 공간 부족 경고 표시(%)</label>
            <input
              type="number"
              value={formState.warning}
              className='input_number'
              onChange={(e) => setFormState((prev) => ({ ...prev, warning: e.target.value }))}
            />
          </FormGroup>
          <FormGroup>
            <label className='label_font_body'>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
            <input
              type="number"
              value={formState.spaceBlocker}
              className='input_number'
              onChange={(e) => setFormState((prev) => ({ ...prev, spaceBlocker: e.target.value }))}
            />
          </FormGroup>
        </div>
      </div>
    </div>

      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button onClick={handleFormSubmit}>{editMode ? '편집' : '완료'}</button>
        <button onClick={onRequestClose}>취소</button>
      </div>
    </div>
    </Modal>
  );
};

export default DomainModal;
