import React, { useState,useEffect, useMemo } from 'react';
import Modal from 'react-modal';
import toast from 'react-hot-toast';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import '../css/MVm.css';
import {
  useVmById,
  useAddVm, 
  useEditVm, 
  useAllUpClusters, 
  useAllTemplates,
  useDisksFromVM,
  useAllnicFromVM, 
} from '../../../../api/RQHook';
import VmCommon from './vmCreate/VmCommon';
import VmSystem from './vmCreate/VmSystem';
import VmInit from './vmCreate/VmInit';
import VmHost from './vmCreate/VmHost';
import VmHa from './vmCreate/VmHa';
import VmBoot from './vmCreate/VmBoot';


const CustomSelect = ({ label, value, onChange, options }) => (
  <div>
    <label>{label}</label>
    <select value={value} onChange={onChange}>
      {options.map((opt) => (
        <option key={opt.value} value={opt.value}>
          {opt.label}
        </option>
      ))}
    </select>
  </div>
);

const VmNewModal = ({ isOpen, editMode = false, vmId, onClose }) => {
  const { mutate: addVM } = useAddVm();
  const { mutate: editVM } = useEditVm();
  
  const [selectedModalTab, setSelectedModalTab] = useState('common');
  

  // 일반
  const [formInfoState, setFormInfoState] = useState({
    id: '',
    name: '',
    comment: '',
    description: '',
    stateless: false,// 무상태
    startPaused: false, // 일시중지상태로시작
    deleteProtected: false, //삭제보호
    diskVoList: [], // 
    nicVoList: [],  //
  });

  //시스템
  const [formSystemState, setFormSystemState] = useState({
    memorySize: 1024,  // 메모리 크기
    memoryMax: 1024,   // 최대 메모리
    memoryActual: 1024, // 할당할 실제메모리
    cpuTopologyCnt: 1, //총cpu
    cpuTopologyCore: 1, // 가상 소켓 당 코어
    cpuTopologySocket: 1, // 가상소켓
    cpuTopologyThread: 1, //코어당 스레드
  });

  // 초기실행
  const [formCloudInitState, setFormCloudInitState] = useState({
    cloudInit: false,// Cloud-lnit
    script: '', // 스크립트
  });
                                                              
  // 호스트
  const [formHostState, setFormHostState] = useState({
    hostInCluster: true,// 클러스터 내 호스트 버튼
    hostVos: [],
    migrationMode: 'migratable', // 마이그레이션 모드
    migrationEncrypt: 'INHERIT',  // 암호화
    // migrationPolicy: 'minimal_downtime',// 마이그레이션 정책

  });
  
  // 고가용성
  const [formHaState, setFormHaState] = useState({
    ha: false,// 고가용성(체크박스)
    priority: 1,// 초기값
    storageDomainVo: '',
  });

  // 부트옵션
  const [formBootState, setFormBootState] = useState({
    firstDevice: 'hd', // 첫번째 장치
    secDevice: '', // 두번째 장치
    bootingMenu: false,// 부팅메뉴 활성화
    cdConn: '',
  });

  const [dataCenterId, setDataCenterId] = useState('');
  const [clusterVoId, setClusterVoId] = useState('');
  const [templateVoId, setTemplateVoId] = useState('');
  const [osSystem, setOsSystem] = useState('other_os'); // 운영 시스템
  const [chipsetOption, setChipsetOption] = useState('Q35_OVMF'); // 칩셋
  const [optimizeOption, setOptimizeOption] = useState('SERVER'); // 최적화옵션
  
  const resetForm = () => {
    setFormInfoState({
      id : '',
      name : '',
      comment : '',
      description : '',
      stateless : false,
      startPaused : false,
      deleteProtected : false,
      diskVoList: [],
      nicVoList: [],
    });
    setFormSystemState({
      memorySize: 1024,
      memoryMax: 1024,
      memoryActual: 1024,
      cpuTopologyCnt: 1,
      cpuTopologyCore: 1,
      cpuTopologySocket: 1,
      cpuTopologyThread: 1,
    });
    setFormCloudInitState({
      cloudInit: false,
      script: ''
    });
    setFormHostState({
      hostInCluster: true,
      hostVos: [],
      migrationMode: 'migratable',
      // migrationPolicy: 'minimal_downtime',
      migrationEncrypt: 'INHERIT',
    });
    setFormHaState({
      ha: false,
      priority: 1,
      storageDomainVo: '',
    });
    setFormBootState({
      firstDevice: 'hd',
      secDevice: '',
      bootingMenu: false,
      cdConn: '',
    });
    setDataCenterId('');
    setClusterVoId('');
    setTemplateVoId('');
    setOsSystem('other_os');
    setChipsetOption('Q35_OVMF');
    setOptimizeOption('SERVER');
  };  


  // 가상머신 상세데이터 가져오기
  const { 
    data: vm, 
    refetch: refetchvms, 
  } = useVmById(vmId); 

  // 클러스터 목록 가져오기
  const { 
    data: clusters = [],
    isLoading: isClustersLoading
  } = useAllUpClusters((e) => ({...e,}));
  
  // 템플릿 가져오기
  const {
    data: templates = [],
    isLoading: isTemplatesLoading
  } = useAllTemplates((e) => ({...e,}));
  
  // 운영 시스템
  const osSystemList = [
    { value: 'windows_xp', label: 'Windows XP' },
    { value: 'windows_2003', label: 'Windows 2003' },
    { value: 'windows_2008', label: 'Windows 2008' },
    { value: 'other_linux', label: 'Linux' },
    { value: 'rhel_5', label: 'Red Hat Enterprise Linux 5.x' },
    { value: 'rhel_4', label: 'Red Hat Enterprise Linux 4.x' },
    { value: 'rhel_3', label: 'Red Hat Enterprise Linux 3.x' },
    { value: 'windows_2003x64', label: 'Windows 2003 x64' },
    { value: 'windows_7', label: 'Windows 7' },
    { value: 'windows_7x64', label: 'Windows 7 x64' },
    { value: 'rhel_5x64', label: 'Red Hat Enterprise Linux 5.x x64' },
    { value: 'rhel_4x64', label: 'Red Hat Enterprise Linux 4.x x64' },
    { value: 'rhel_3x64', label: 'Red Hat Enterprise Linux 3.x x64' },
    { value: 'windows_2008x64', label: 'Windows 2008 x64' },
    { value: 'windows_2008R2x64', label: 'Windows 2008 R2 x64' },
    { value: 'rhel_6', label: 'Red Hat Enterprise Linux 6.x' },
    { value: 'rhel_6x64', label: 'Red Hat Enterprise Linux 6.x x64' },
    { value: 'debian_7', label: 'Debian 7+' },
    { value: 'windows_8', label: 'Windows 8' },
    { value: 'debian_9', label: 'Debian 9+' },
    { value: 'windows_8x64', label: 'Windows 8 x64' },
    { value: 'windows_2012x64', label: 'Windows 2012 x64' },
    { value: 'rhel_7x64', label: 'Red Hat Enterprise Linux 7.x x64' },
    { value: 'windows_2012R2x64', label: 'Windows 2012R2 x64' },
    { value: 'windows_10', label: 'Windows 10' },
    { value: 'windows_10x64', label: 'Windows 10 x64' },
    { value: 'rhel_atomic7x64', label: 'Red Hat Atomic 7.x x64' },
    { value: 'windows_2016x64', label: 'Windows 2016 x64' },
    { value: 'rhel_8x64', label: 'Red Hat Enterprise Linux 8.x x64' },
    { value: 'windows_2019x64', label: 'Windows 2019 x64' },
    { value: 'other_linux_kernel_4', label: 'Other Linux (kernel 4.x)' },
    { value: 'rhel_9x64', label: 'Red Hat Enterprise Linux 9.x x64' },
    { value: 'rhcos_x64', label: 'Red Hat Enterprise Linux CoreOS' },
    { value: 'windows_11', label: 'Windows 11' },
    { value: 'windows_2022', label: 'Windows 2022' },
    { value: 'sles_11', label: 'SUSE Linux Enterprise Server 11+' },
    { value: 'other_s390x', label: 'Other OS' },
    { value: 'other_linux_s390x', label: 'Linux' },
    { value: 'rhel_7_s390x', label: 'Red Hat Enterprise Linux 7.x' },
    { value: 'sles_12_s390x', label: 'SUSE Linux Enterprise Server 12' },
    { value: 'ubuntu_16_04_s390x', label: 'Ubuntu Xenial Xerus LTS+' },
    { value: 'freebsd', label: 'FreeBSD 9.2' },
    { value: 'freebsdx64', label: 'FreeBSD 9.2 x64' },
    { value: 'ubuntu_12_04', label: 'Ubuntu Precise Pangolin LTS' },
    { value: 'ubuntu_12_10', label: 'Ubuntu Quantal Quetzal' },
    { value: 'ubuntu_13_04', label: 'Ubuntu Raring Ringtails' },
    { value: 'ubuntu_13_10', label: 'Ubuntu Saucy Salamander' },
    { value: 'ubuntu_14_04', label: 'Ubuntu Trusty Tahr LTS+' },
    { value: 'other_ppc64', label: 'Other OS' },
    { value: 'ubuntu_18_04', label: 'Ubuntu Bionic Beaver LTS+' },
    { value: 'other_linux_ppc64', label: 'Linux' },
    { value: 'rhel_6_ppc64', label: 'Red Hat Enterprise Linux up to 6.8' },
    { value: 'sles_11_ppc64', label: 'SUSE Linux Enterprise Server 11' },
    { value: 'ubuntu_14_04_ppc64', label: 'Ubuntu Trusty Tahr LTS+' },
    { value: 'rhel_7_ppc64', label: 'Red Hat Enterprise Linux 7.x' },
    { value: 'rhel_6_9_plus_ppc64', label: 'Red Hat Enterprise Linux 6.9+' },
    { value: 'rhel_8_ppc64', label: 'Red Hat Enterprise Linux 8.x' },
    { value: 'rhel_9_ppc64', label: 'Red Hat Enterprise Linux 9.x' },
    { value: 'other', label: 'Other OS   ' },
  ];

  // 칩셋 옵션
  const chipsetOptionList = [
    { value: 'I440FX_SEA_BIOS', label: 'BIOS의 I440FX 칩셋' },
    { value: 'Q35_OVMF', label: 'UEFI의 Q35 칩셋' },
    { value: 'Q35_SEA_BIOS', label: 'BIOS의 Q35 칩셋' },
    { value: 'Q35_SECURE_BOOT', label: 'UEFI SecureBoot의 Q35 칩셋' },
  ]; 

  // 최적화옵션
  const optimizeOptionList = [
    { value: 'DESKTOP', label: '데스크톱' },
    { value: 'HIGH_PERFORMANCE', label: '고성능' },
    { value: 'SERVER', label: '서버' }
  ];

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때만 초기화
      setSelectedModalTab('common'); // 탭 상태 초기화
    }
  }, [isOpen]);
  

  // 초기값 설정
  useEffect(() => {
    if (editMode && vm) {
      setFormInfoState({
        id:vm?.id || '',
        name: vm?.name || '',
        comment: vm?.comment || '',
        description: vm?.description || '',
        stateless: vm?.stateless || false,
        startPaused: vm?.startPaused || false,
        deleteProtected: vm?.deleteProtected || false,
        diskVoList: vm?.diskImageVo || [],
        nicVoList: vm?.nicVos || [],
      });
      setFormSystemState({
        memorySize: vm?.memorySize / (1024 * 1024), // 입력된 값는 mb, 보낼 단위는 byte
        memoryMax: vm?.memoryMax / (1024 * 1024),
        memoryActual: vm?.memoryActual / (1024 * 1024),
        cpuTopologyCnt: vm?.cpuTopologyCnt || 1,
        cpuTopologyCore: vm?.cpuTopologyCore || 1,
        cpuTopologySocket: vm?.cpuTopologySocket || 1,
        cpuTopologyThread: vm?.cpuTopologyThread || 1
      });
      setFormCloudInitState({
        cloudInit: vm?.cloudInit || false,
        script: vm?.setScript || ''
      });
      setFormHostState({
        hostInCluster: vm?.hostInCluster || true,
        hostVos: vm?.hostVos || [],
        migrationMode: vm?.migrationMode || 'migratable',
        migrationEncrypt: vm?.migrationEncrypt || 'INHERIT',
        // migrationPolicy: vm?.migrationPolicy || 'minimal_downtime',
      });
      setFormHaState({
        ha: vm?.ha || false,
        priority: vm?.priority || 1,
        domainVoId: vm?.storageDomainVo?.id || '',
      });
      setFormBootState({
        firstDevice: vm?.firstDevice || 'hd',
        secDevice: vm?.secDevice || '',
        bootingMenu: vm?.bootingMenu || false
      });
      setClusterVoId(vm?.clusterVo?.id || ''); 
      setTemplateVoId(vm?.templateVo?.id || '');
      setOsSystem(vm?.osSystem || 'other_os');
      setChipsetOption(vm?.chipsetFirmwareType || 'Q35_OVMF');
      setOptimizeOption(vm?.optimizeOption || 'SERVER');
    } else if (!editMode && !isClustersLoading) {
      resetForm();
    }
  }, [editMode, vm]);
  
  
  useEffect(() => {
    if (!editMode && clusters.length > 0) {
      setClusterVoId(clusters[0].id); // 첫 번째 클러스터로 초기화
      setDataCenterId(clusters[0]?.dataCenterVo?.id);
    }
  }, [clusters, editMode]);
  
  
  useEffect(() => {
    if (!editMode && templates.length > 0) {
      setTemplateVoId(templates[0].id);
    }
  }, [templates, editMode]);



  const validateForm = () => {
    if (!formInfoState.name) return '이름을 입력해주세요.';
    if (!clusterVoId) return '클러스터를 선택해주세요.';
    if (formSystemState.memorySize > '9223372036854775807') return '메모리 크기가 너무 큽니다.';
    return null;
  };

  const dataToSubmit = {
    // VmInfo
    clusterVo: { id: clusterVoId },
    templateVo: { id: templateVoId },
    name: formInfoState.name,
    description: formInfoState.description,
    comment: formInfoState.comment,
    stateless: formInfoState.stateless,
    startPaused: formInfoState.startPaused,
    deleteProtected: formInfoState.deleteProtected,
    chipsetFirmwareType: chipsetOption,
    optimizeOption: optimizeOption,
  
    // VmSystem
    memorySize: formSystemState.memorySize * 1024 * 1024,
    memoryMax: formSystemState.memoryMax * 1024 * 1024,
    memoryActual: formSystemState.memoryActual * 1024 * 1024,
    cpuTopologyCore: formSystemState.cpuTopologyCore,
    cpuTopologySocket: formSystemState.cpuTopologySocket,
    cpuTopologyThread: formSystemState.cpuTopologyThread,
  
    // VmInit
    cloudInit: formCloudInitState.cloudInit,
    script: formCloudInitState.script,
    // hostName: '',
    timeStandard: 'Asia/Seoul',
  
    // VmHost
    hostInCluster: formHostState.hostInCluster,
    hostVos: formHostState.hostVos.map((host) => ({ id: host.id })),
    migrationMode: formHostState.migrationMode,
    migrationEncrypt: formHostState.migrationEncrypt,
  
    // VmHa
    ha: formHaState.ha,
    priority: formHaState.priority,
    storageDomainVo: { id: formHaState.storageDomainVo },
  
    // VmBoot
    firstDevice: formBootState.firstDevice,
    secDevice: formBootState.secDevice,
    osSystem: osSystem,
    connVo: { id: formBootState.cdConn },

    vnicProfileVos: formInfoState.nicVoList.map((vnic) => ({ id: vnic.id }))
  };
  

  const handleFormSubmit = () => { // 디스크  연결은 id값 보내기 생성은 객체로 보내기
    const error = validateForm();
    if (error) {
      toast.error(error);
      return;
    }

    console.log('가상머신 데이터 확인:', dataToSubmit); 

    if (editMode) {
      editVM(
        { vmId: vmId, vmdata: dataToSubmit },
        {
          onSuccess: () => {
            onClose();
            toast.success('가상머신 편집 완료');
          },
          onError: (error) => toast.error('Error editing vm:', error),
        }
      );
    } else {
      addVM(dataToSubmit, {
        onSuccess: () => {
          onClose();
          toast.success('가상머신 생성 완료');
        },
        onError: (error) => toast.error('Error adding vm:', error),
      });
    }
  };

  const tabs = [
    { id: "common_tab", value: "common", label: "일반" },
    { id: "system_tab", value: "system", label: "시스템" },
    { id: "beginning_tab", value: "beginning", label: "초기 실행" },
    { id: "host_tab", value: "host", label: "호스트" },
    { id: "ha_mode_tab", value: "ha_mode", label: "고가용성" },
    { id: "boot_option_tab", value: "boot_outer", label: "부트 옵션" },
  ];

  
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel={editMode ? '가상머신 편집' : '가상머신 생성'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vm-edit-popup">
        <div className="popup-header">
          <h1>{editMode ? '가상머신 편집' : '가상머신 생성'}</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className='vm_edit_popup_content'>
          <div className='vm_new_nav' style={{
            fontSize: '0.33rem',
            height: '71vh',
            width: '30%',
            backgroundColor: '#FAFAFA',
            borderRight: '1px solid #ddd',
            boxShadow: '1px 0 5px rgba(0, 0, 0, 0.1)',
            fontWeight: 800
          }}>
            {tabs.map((tab) => (
              <div
                key={tab.id}
                id={tab.id}
                className={selectedModalTab === tab.value ? "active-tab" : "inactive-tab"}
                onClick={() => setSelectedModalTab(tab.value)}
              >
                {tab.label}
              </div>
            ))}
          </div>

          <div className="vm_edit_select_tab">
            <div className="edit-first-content">

              <div>
                <label htmlFor="cluster">클러스터</label>
                <select
                  id="cluster"
                  value={clusterVoId}
                  onChange={(e) => setClusterVoId(e.target.value) }
                >
                  {isClustersLoading ? (
                    <option>로딩중~</option>
                  ) : (
                    clusters && clusters.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.name}: {c.id} - 데이터센터: {c.dataCenterVo.name}
                      </option>
                    ))
                  )}
                </select>
                <span>{clusterVoId}</span>
              </div>

              <div>
                <label htmlFor="template" style={{ color: 'gray' }}>템플릿</label>
                <select
                  id="template"
                  value={templateVoId}
                  onChange={(e) => setTemplateVoId(e.target.value) }
                  disabled={editMode} // 편집 모드일 경우 비활성화
                >
                  {isTemplatesLoading ? (
                    <option>로딩중~</option>
                  ) : (
                    templates && templates.map((t) => (
                      <option key={t.id} value={t.id}>
                        {t.name}: {t.id}
                      </option>
                    ))
                  )}
                </select>
              </div>

              <CustomSelect
                label="운영 시스템"
                value={osSystem}
                onChange={(e) => setOsSystem(e.target.value)}
                options={osSystemList}
              /> 
              <CustomSelect
                label="칩셋/펌웨어 유형"
                value={chipsetOption}
                onChange={(e) => setChipsetOption(e.target.value)}
                options={chipsetOptionList}
              />
              <CustomSelect
                label="최적화 옵션"
                value={optimizeOption}
                onChange={(e) => setOptimizeOption(e.target.value)}
                options={optimizeOptionList}
              />
            </div>
            
            
            {selectedModalTab === 'common' && (
              <VmCommon
                editMode={editMode}
                vmId={vmId}
                clusterVoId={clusterVoId}
                dataCenterId={dataCenterId}
                formInfoState={formInfoState}
                setFormInfoState={setFormInfoState}
              />
            )}
            {selectedModalTab === 'system' && (
              <VmSystem
                editMode={editMode}
                vmId={vmId}
                formSystemState={formSystemState}
                setFormSystemState={setFormSystemState}
              />
            )}
            {selectedModalTab === 'beginning' && (
              <VmInit
                editMode={editMode}
                vmId={vmId}
                formCloudInitState={formCloudInitState}
                setFormCloudInitState={setFormCloudInitState}
              />
            )}
            {selectedModalTab === 'host' && (
              <VmHost
                editMode={editMode}
                vmId={vmId}
                clusterVoId={clusterVoId}
                formHostState={formHostState}
                setFormHostState={setFormHostState}
              />
            )}
            {selectedModalTab === 'ha_mode' && (
              <VmHa
                editMode={editMode}
                vmId={vmId}
                dataCenterId={dataCenterId}
                formHaState={formHaState}
                setFormHaState={setFormHaState}
              />
            )}
            {selectedModalTab === 'boot_outer' && (
              <VmBoot
                editMode={editMode}
                vmId={vmId}
                dataCenterId={dataCenterId}
                formBootState={formBootState}
                setFormBootState={setFormBootState}
              />
            )}
          </div>
        </div>

        <div className="edit-footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>  
  );
};

export default VmNewModal;


