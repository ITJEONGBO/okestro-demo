import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { 
  useDiskById,
  // useAddDisk,
  // useEditDisk, 
  // useAllActiveDataCenters,
  useAllActiveDomainFromDataCenter, 
  useAllDiskProfileFromDomain,
  useAddDiskFromVM,
} from '../../../../api/RQHook';
import toast from 'react-hot-toast';

const FormGroup = ({ label, children }) => (
  <div className="img_input_box">
    <label>{label}</label>
    {children}
  </div>
);

const VmDiskModal = ({ isOpen, editMode = false, diskId, vmId, dataCenterId, onClose }) => {
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
    // interface_: '', // vm 인터페이스 
    bootable: false, // vm 부팅가능
    logicalName:'',
    readOnly: false, // vm 읽기전용
    cancelActive: false, // vm 취소 활성화
  });
  const [domainVoId, setDomainVoId] = useState('');
  const [diskProfileVoId, setDiskProfileVoId] = useState('');
  const [interface_, setInterface_] = useState('');

  const [activeTab, setActiveTab] = useState('img');
  const handleTabClick = (tab) => { setActiveTab(tab); };

  // 디스크 데이터 가져오기
  const {
    data: disk,
    refetch: refetchDisk,
    isLoading: isDiskLoading
  } = useDiskById(diskId);


  // 선택한 데이터센터가 가진 도메인 가져오기
  const {
    data: domains = [],
    refetch: refetchDomains,
    isLoading: isDomainsLoading,
  } = useAllActiveDomainFromDataCenter(
    dataCenterId, (e) => ({...e,})
  );

  // 선택한 도메인이 가진 디스크 프로파일 가져오기
  const {
    data: diskProfiles = [],
    refetch: diskProfilesRefetch,
    isLoading: isDiskProfilesLoading,
  } = useAllDiskProfileFromDomain(
    domainVoId, (e) => ({...e,})
  );  

  const { mutate: addDiskVm } = useAddDiskFromVM(); // 가상머신 세부페이지 안에 디스크생성성

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const interfaceList = [
    { value: "VIRTIO_SCSI", label: "VirtIO-SCSI" },
    { value: "VIRTIO", label: "VirtIO" },
    { value: "SATA", label: "SATA" },
  ];

  useEffect(() => {
    if (editMode && disk) {
      setFormState({
        id: disk?.id || '',
        size: (disk?.virtualSize / (1024 * 1024 * 1024)).toFixed(0),
        appendSize: 0,
        alias: disk?.alias || '',
        description: disk?.description || '',
        wipeAfterDelete: disk?.wipeAfterDelete || false,
        sharable: disk?.sharable || false,
        backup: disk?.backup || false,
        sparse: disk?.sparse || false,
      });
      setDomainVoId(disk?.storageDomainVo?.id || '');
      setDiskProfileVoId(disk?.diskProfileVo?.id || '');
    } else if (!editMode) {
      resetForm(); // 초기화
    }
  }, [editMode, disk]); // 의존성 배열에서 필요한 값만 추가
  
  

  useEffect(() => {
    if (!editMode && domains.length > 0) {
      setDomainVoId(domains[0].id);
    }
  }, [domains, editMode]);

  useEffect(() => {
    if (!editMode && diskProfiles.length > 0) {
      setDiskProfileVoId(diskProfiles[0].id);
    }
  }, [diskProfiles, editMode]);

  useEffect(() => {
    if (!editMode && interfaceList.length > 0) {
      setInterface_(interfaceList[0].value);
    }
  }, [interfaceList, editMode]);
  

  const resetForm = () => {
    setFormState({
      id: '',
      size: '',
      appendSize: 0,
      alias: '',
      description: '',
      wipeAfterDelete: false,
      bootable: false,
      sharable: false,
      backup: true,
      sparse: true,
      readOnly: false,
      logicalName:'',
      passDiscard:true
    });
    setInterface_('');
    setDomainVoId('');
    setDiskProfileVoId('');
  };

  const validateForm = () => {
    if (!formState.alias) return '별칭을 입력해주세요.';
    if (!formState.size) return '크기를 입력해주세요.';
    if (!domainVoId) return '스토리지 도메인을 선택해주세요.';
    if (!diskProfileVoId) return '디스크 프로파일을 선택해주세요.';
    return null;
  };


  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      toast.error(error);
      return;
    }
    
    const sizeToBytes = parseInt(formState.size, 10) * 1024 * 1024 * 1024; // GB -> Bytes 변환
    const appendSizeToBytes = parseInt(formState.appendSize || 0, 10) * 1024 * 1024 * 1024; // GB -> Bytes 변환 (기본값 0)

    const selectedDomain = domains.find((dm) => dm.id === domainVoId);
    const selectedDiskProfile = diskProfiles.find((dp) => dp.id === diskProfileVoId);


    // 데이터 객체 생성
    const diskDataToSubmit = {
      storageDomainVo: { id: selectedDomain.id, name: selectedDomain.name },
      diskProfileVo: { id: selectedDiskProfile.id, name: selectedDiskProfile.name },
      ...formState,
      size: sizeToBytes
    };

    
    //가상머신  디스크
    const vmDataToSubmit = {
        bootable: formState.bootable,
        readOnly: formState.readOnly,
        passDiscard:formState.passDiscard,
        interface_:interface_,
        logicalName:formState.logicalName,
        diskImageVo: { 
          alias: formState.alias,
          size: sizeToBytes,
          description: formState.description,
          wipeAfterDelete:formState.wipeAfterDelete,
          backup:formState.backup,
          sparse:formState.sparse,
          storageDomainVo: { id: selectedDomain.id, name: selectedDomain.name },
          diskProfileVo: { id: selectedDiskProfile.id, name: selectedDiskProfile.name },
        }
    }

    console.log("Form Data: ", diskDataToSubmit); // 데이터를 확인하기 위한 로그
    console.log("Form vmDataToSubmit: ", vmDataToSubmit); // 데이터를 확인하기 위한 로그

    addDiskVm(
      {vmId: vmId, diskData: vmDataToSubmit },
      {
      onSuccess: () => {
        toast.success("VM 디스크 생성 완료");
        onClose(); // 성공 시 모달 닫기
      },
      onError: (error) => {
        console.error('vNIC 프로파일 추가 중 오류 발생:', error);
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? '디스크 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_disk_new_popup">
        <div className="popup-header">
          <h1>{editMode ? '디스크 편집' : '새 디스크 생성'}</h1>
          <button onClick={onClose}>
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
          <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접 LUN
          </div>
        </div>

        {/*이미지*/}
        {activeTab === 'img' && (
          <div className="disk_new_img">
            <div className="disk_new_img_left">

              <FormGroup label="크기(GB)">
                <input
                  type="number"
                  min="0"
                  value={formState.size}
                  autoFocus
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

              <FormGroup label="인터페이스">
                <select
                  value={interface_}
                  onChange={(e) => setInterface_(e.target.value)}
                >
                  {interfaceList.map((inter) => (
                    <option key={inter.value} value={inter.value}>
                      {inter.label} :{inter.value}
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
                      {dm.name}: {dm.id}
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
                <label htmlFor="backup">증분 백업 사용</label>
              </div>

            </div>
        </div>
     )} 
        {/* 직접LUN */}
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
        )}
        <div className="edit-footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmDiskModal;
