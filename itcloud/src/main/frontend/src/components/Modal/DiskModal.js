import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { 
  useDiskById,
  useAddDisk,
  useEditDisk, 
  useAllActiveDataCenters,
  useAllActiveDomainFromDataCenter, 
  useAllDiskProfileFromDomain,
} from '../../api/RQHook';

const FormGroup = ({ label, children }) => (
  <div className="img_input_box">
    <label>{label}</label>
    {children}
  </div>
);

const DiskModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  diskId,
  vmId,
  type='disk'
}) => {
  const [formState, setFormState] = useState({
    id: '',
    size: '',
    appendSize: 0,
    alias: '',
    description: '',
    wipeAfterDelete: false,
    sharable: false,
    backup: true,
    sparse: true, //할당정책: 씬
    interfaced: '', // vm 인터페이스 
    bootable: true, // vm 부팅가능
    readOnly: false, // vm 읽기전용
    cancelActive: false, // vm 취소 활성화
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');
  const [domainVoId, setDomainVoId] = useState('');
  const [diskProfileVoId, setDiskProfileVoId] = useState('');

  const [activeTab, setActiveTab] = useState('img');
  const handleTabClick = (tab) => { setActiveTab(tab); };

  // 디스크 데이터 가져오기
  const {
    data: disk,
    refetch: refetchDisk,
    isLoading: isDiskLoading
  } = useDiskById(diskId);

  // 전체 데이터센터 가져오기
  const {
    data: datacenters = [],
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllActiveDataCenters((e) => ({...e,}));

  // 선택한 데이터센터가 가진 도메인 가져오기
  const {
    data: domains = [],
    refetch: refetchDomains,
    isLoading: isDomainsLoading,
  } = useAllActiveDomainFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  // 선택한 도메인이 가진 디스크 프로파일 가져오기
  const {
    data: diskProfiles = [],
    refetch: diskProfilesRefetch,
    isLoading: isDiskProfilesLoading,
  } = useAllDiskProfileFromDomain(
    domainVoId ? domainVoId : undefined, 
    (e) => ({...e,})
  );  

  const { mutate: addDisk } = useAddDisk();
  const { mutate: editDisk } = useEditDisk();

  const interfaceList = [
    { value: "VirtIO-SCSI", label: "VirtIO-SCSI" },
    { value: "VirtIO", label: "VirtIO" },
    { value: "SATA", label: "SATA" },
  ];

  useEffect(() => {
    if (editMode && disk) {
      console.log('Setting edit mode state with disk:', disk);
      setFormState({
        id: disk.id || '',
        size: (disk.virtualSize / (1024 * 1024 * 1024)).toFixed(0),
        alias: disk.alias || '',
        description: disk.description || '',
        wipeAfterDelete: disk.wipeAfterDelete || false,
        sharable: disk.sharable || false,
        backup: disk.backup || false,
        sparse: disk.sparse || false,
      });
      setDataCenterVoId(disk?.dataCenterVo?.id || '');
      setDomainVoId(disk?.storageDomainVo?.id || '');
      setDiskProfileVoId(disk?.diskProfileVo?.id || '');
    } else if (!editMode && !isDatacentersLoading) {
      resetForm();
    }
  }, [editMode, disk]);

  useEffect(() => {
    if (!editMode && datacenters && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters, editMode]);

  useEffect(() => {
    if (!editMode && domains && domains.length > 0) {
      setDomainVoId(domains[0].id);
    }
  }, [domains, editMode]);

  useEffect(() => {
    if (!editMode && diskProfiles && diskProfiles.length > 0) {
      setDiskProfileVoId(diskProfiles[0].id);
    }
  }, [diskProfiles, editMode]);


  const resetForm = () => {
    setFormState({
      id: '',
      size: '',
      alias: '',
      description: '',
      wipeAfterDelete: false,
      sharable: false,
      backup: true,
      sparse: true,
    });
    setDataCenterVoId('');
    setDomainVoId('');
    setDiskProfileVoId('');
  };

  const validateForm = () => {
    if (!formState.alias) return '별칭을 입력해주세요.';
    if (!formState.size) return '크기를 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    if (!domainVoId) return '스토리지 도메인을 선택해주세요.';
    if (!diskProfileVoId) return '디스크 프로파일을 선택해주세요.';
    return null;
  };


  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }

    const sizeToBytes = parseInt(formState.size, 10) * 1024 * 1024 * 1024; // GB -> Bytes 변환


    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);
    const selectedDomain = domains.find((dm) => dm.id === domainVoId);
    const selectedDiskProfile = diskProfiles.find((dp) => dp.id === diskProfileVoId);


    // 데이터 객체 생성
    const dataToSubmit = {
      dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      storageDomainVo: { id: selectedDomain.id, name: selectedDomain.name },
      diskProfileVo: { id: selectedDiskProfile.id, name: selectedDiskProfile.name },
      ...formState,
      size: sizeToBytes
    };

    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.appendSize = parseInt(formState.appendSize, 10) * 1024 * 1024 * 1024;   
      editDisk(
        { diskId: formState.id, diskData: dataToSubmit}, 
        {
          onSuccess: () => {
            alert("디스크 편집 완료")
            onRequestClose();  // 성공 시 모달 닫기
          },
        }
      );
    } else {
      addDisk(dataToSubmit, {
        onSuccess: () => {
          alert("디스크 생성 완료")
          onRequestClose();
        },
      });
    }
  };


  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '디스크 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '디스크 편집' : '새 디스크'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth/>
          </button>
        </div>

        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick('img')}
            className={activeTab === 'img' ? 'active' : ''}
          >
            이미지
          </div>
          {/* <div></div> */}
          {/* <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접LUN
          </div> */}
        </div>

        {/*이미지*/}
        {/* {activeTab === 'img' && ( */}
          <div className="disk_new_img">
            <div className="disk_new_img_left">

              <FormGroup label="크기(GB)">
                <input
                  type="number"
                  min="0"
                  // value={editMode ? (formState.size / (1024 * 1024 * 1024)).toFixed(0) : formState.size}
                  value={formState.size}
                  onChange={(e) => setFormState((prev) => ({ ...prev, size: e.target.value }))}
                  disabled={editMode}
                />
              </FormGroup>

            {editMode && (
              <FormGroup label="추가크기(GB)">
                <input
                  type="number"
                  min="0"
                  value={formState.appendSize}
                  onChange={(e) => setFormState((prev) => ({ ...prev, appendSize: e.target.value }))}
                />
              </FormGroup>
            )}              

              <FormGroup label="별칭">
                <input
                  type="text"
                  value={formState.alias}
                  onChange={(e) => setFormState((prev) => ({ ...prev, alias: e.target.value }))}
                />
              </FormGroup>

              <FormGroup label="설명">
                <input
                  type="text"
                  value={formState.description}
                  onChange={(e) => setFormState((prev) => ({ ...prev, description: e.target.value }))}
                />
              </FormGroup>

              

              {type === 'vm' && (
                <FormGroup label="인터페이스">
                <select
                  value={formState.interfaced}
                  onChange={(e) => setFormState((prev) => ({ ...prev, cpuArc: e.target.value }))}
                >
                  {interfaceList.map((inter) => (
                    <option key={inter.value} value={inter.value}>
                      {inter.label}
                    </option>
                  ))}
                </select>
              </FormGroup>

                // <div className="tab_content">
                //   {isIscsisLoading ? (
                //       <div className="loading-message">로딩 중...</div>
                //     ) : isIscsisError ? (
                //       <div className="error-message">데이터를 불러오는 중 오류가 발생했습니다.</div>
                //     ) : (
                //       <Table
                //         columns={TableInfo.FIBRE}
                //         data={fibres}
                //         onRowClick={(row) => console.log('선택한 행 데이터:', row)}
                //         shouldHighlight1stCol={true}
                //       />
                //     )}
                // </div>
              )}

              <FormGroup label="데이터 센터">
                <select
                  value={dataCenterVoId}
                  onChange={(e) => setDataCenterVoId(e.target.value)}
                  disabled={editMode}
                >
                  {datacenters && datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name}: {dataCenterVoId }
                    </option>
                  ))}
                </select>
              </FormGroup>

              <FormGroup label="스토리지 도메인">
                <select
                  value={domainVoId}
                  onChange={(e) => setDomainVoId(e.target.value)}
                  disabled={editMode}
                >
                  {domains && domains.map((dm) => (
                    <option key={dm.id} value={dm.id}>
                      {dm.name}: {domainVoId}
                    </option>
                  ))}
                </select>
              </FormGroup>

              <FormGroup label="할당 정책">
                <select
                  value={formState.sparse}
                  onChange={(e) =>setFormState((prev) => ({ ...prev, sparse: e.target.value }))}
                  disabled={editMode}
                >
                  <option value="true">씬 프로비저닝</option>
                  <option value="false">사전 할당</option>
                </select>
              </FormGroup>
            
              <FormGroup label="디스크 프로파일">
                <select
                  value={diskProfileVoId}
                  onChange={(e) => setDiskProfileVoId(e.target.value)}
                >
                  {diskProfiles && diskProfiles.map((dp) => (
                    <option key={dp.id} value={dp.id}>
                      {dp.name}: {diskProfileVoId}
                    </option>
                  ))}
                </select>
              </FormGroup>
              </div>
            
            <div className="disk_new_img_right">

              <div>
                <input
                  type="checkbox"
                  id="wipeAfterDelete"
                  checked={formState.wipeAfterDelete}
                  onChange={(e) => setFormState((prev) => ({ ...prev, wipeAfterDelete: e.target.checked }))}
                />
                <label htmlFor="wipeAfterDelete">삭제 후 초기화</label>
              </div>

              {type === 'vm' && (
                <>
                <div>
                  <input 
                    type="checkbox" 
                    id="backup" 
                    checked={formState.active}
                    onChange={(e) => setFormState((prev) => ({ ...prev, bootable: e.target.checked }))}
                  />
                  <label htmlFor="backup">디스크 활성화</label>
                </div>
                <div>
                  <input 
                    type="checkbox" 
                    id="backup" 
                    checked={formState.bootable}
                    onChange={(e) => setFormState((prev) => ({ ...prev, bootable: e.target.checked }))}
                  />
                  <label htmlFor="backup">부팅 가능</label>
                </div>
                <div>
                  <input 
                    type="checkbox" 
                    id="backup" 
                    checked={formState.readOnly}
                    onChange={(e) => setFormState((prev) => ({ ...prev, readOnly: e.target.checked }))}
                  />
                  <label htmlFor="backup">읽기전용</label>
                </div>
                {/* <div>
                  <input 
                    type="checkbox" 
                    id="backup" 
                    checked={formState.cancelActive}
                    onChange={(e) => setFormState((prev) => ({ ...prev, cancelActive: e.target.checked }))}
                  />
                  <label htmlFor="backup">취소 활성화</label>
                </div> */}
                </>
              )}
 
              <div>
                <input 
                  type="checkbox" 
                  className="sharable"
                  checked={formState.sharable}
                  onChange={(e) => setFormState((prev) => ({ ...prev, sharable: e.target.checked }))}
                />
                <label htmlFor="sharable">공유 가능</label>
              </div>

              <div>
                <input 
                  type="checkbox" 
                  id="backup" 
                  checked={formState.backup}
                  onChange={(e) => setFormState((prev) => ({ ...prev, backup: e.target.checked }))}
                />
                <label htmlFor="backup">중복 백업 사용</label>
              </div>

            </div>
         
        </div>
        {/* )} */}
        {/* 직접LUN
        {activeTab === 'directlun' && (
          <div id="storage_directlun_outer">
            <div id="storage_lun_first">
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">데이터 센터</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">호스트</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">스토리지 타입</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
              </div>
            </div>
          </div>
        )} */}
        <div className="edit_footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
      </Modal>
  );
};

export default DiskModal;
