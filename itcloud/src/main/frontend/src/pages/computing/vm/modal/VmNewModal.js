import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import toast from 'react-hot-toast';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle, faChevronCircleRight } from '@fortawesome/free-solid-svg-icons';
import { Tooltip } from 'react-tooltip';
import '../css/MVm.css';
import {
  useVmById,
  useAddVm, 
  useEditVm, 
  useAllUpClusters, 
  useAllnicFromVM, 
  useAllTemplates, 
  useDisksFromVM, 
  useHostFromCluster, 
  // useCDFromDataCenter
} from '../../../../api/RQHook';
import VmDiskConnectionModal from './VmDiskConnectionModal';
import VmDiskModal from './VmDiskModal';
import VmNic from './VmNic';

const InfoTooltip = ({ tooltipId, message }) => (
  <>
    <FontAwesomeIcon 
      icon={faInfoCircle} 
      style={{ color: 'rgb(83, 163, 255)', marginLeft: '5px' }} 
      data-tooltip-id={tooltipId}
    />
    <Tooltip id={tooltipId} className="icon_tooltip" place="top" effect="solid">
      {message}
    </Tooltip>
  </>
);

const VmNewModal = ({ isOpen, editMode = false, vmId, onClose }) => {
  const { mutate: addVM } = useAddVm();
  const { mutate: editVM } = useEditVm();

  // 일반
  const [formInfoState, setFormInfoState] = useState({
    id: '',
    name: '',
    comment: '',
    description: '',
    stateless: false,// 무상태
    startPaused: false, // 일시중지상태로시작
    deleteProtected: false, //삭제보호
  });

  //시스템
  const [formSystemState, setFormSystemState] = useState({
    memorySize: 1024,
    maxMemory: 1024,
    allocatedMemory: 1024,
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
    isSpecificHostSelected: false,// 클러스터 호스트 라디오버튼클릭
    migrationMode: '', // 마이그레이션 모드
    migrationPolicy: '',// 마이그레이션 정책
  });
  
  // 고가용성
  const [formHaState, setFormHaState] = useState({
    ha: false,// 고가용성(체크박스)
    priority: 1,// 초기값
  });

  // 부트옵션
  const [formBootState, setFormBootState] = useState({
    firstDevice: 'hd', // 첫번째 장치
    secDevice: '', // 두번째 장치
    bootingMenu: false,// 부팅메뉴 활성화
  });

  const [dataCenterName, setDataCenterName] = useState('');
  const [dataCenterId, setDataCenterId] = useState('');
  const [clusterVoId, setClusterVoId] = useState('');
  const [templateVoId, setTemplateVoId] = useState('');
  const [osSystem, setOsSystem] = useState('other_os'); // 운영 시스템
  const [chipsetOption, setChipsetOption] = useState('Q35_OVMF'); // 칩셋
  const [optimizeOption, setOptimizeOption] = useState('SERVER'); // 최적화옵션
  const [migrationModeOption, setMigrationModeOption] = useState('migratable'); // 마이그레이션 모드
  const [migrationPolicyOption, setMigrationPolicyOption] = useState('minimal_downtime'); // 마이그레이션 정책
  const [diskVoList, setDiskVoList] = useState([]); // 인스턴스 이미지 
  const [nicVoList, setNicVoList] = useState([]); // nic 목록
  const [hostVoList, setHostVoList] = useState([]); // 클러스터 내 호스트
  const [cdConn, setCdConn] = useState('');

  const [selectedModalTab, setSelectedModalTab] = useState('common');
  
  // 추가 모달
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
  
  
  const resetForm = () => {
    setFormInfoState({
      id : '',
      name : '',
      comment : '',
      description : '',
      stateless : false,
      startPaused : false,
      deleteProtected : false,
    });
    setFormSystemState({
      memorySize: 1024,
      maxMemory: 1024,
      allocatedMemory: 1024,
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
      isSpecificHostSelected: false,
      migrationMode: '',
      migrationPolicy: '',
    });
    setFormHaState({
      ha: false,
      priority: 1,
    });
    setFormBootState({
      firstDevice: 'hd',
      secDevice: '',
      bootingMenu: false,
    });
    setDataCenterId('');
    setClusterVoId('');
    setTemplateVoId('');
    setOsSystem('other_os');
    setChipsetOption('Q35_OVMF');
    setOptimizeOption('SERVER');
    setMigrationModeOption('migratable');
    setMigrationPolicyOption('minimal_downtime');
    setDiskVoList([]);
    setNicVoList([])
    setHostVoList([]);
    setCdConn('');
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
  } = useAllUpClusters((e) => ({
    ...e,
    dataCenterId: e?.dataCenterVo?.id,
    dataCenterName: e?.dataCenterVo?.name,
  }));
  
  // 템플릿 가져오기
  const {
    data: templates = [],
    isLoading: isTemplatesLoading
  } = useAllTemplates((e) => ({...e,}));

  // 디스크 목록
  const { 
    data: disks = [],
    isLoading: isDisksLoading
  } = useDisksFromVM(vmId, (e) => ({...e}));
  
  // nic 목록
  const { 
    data: nics = [],
    isLoading: isNicsLoading
  } = useAllnicFromVM(clusterVoId, (e) => ({
    id: e?.id,
    name: e?.name,
    networkVoName: e?.networkVo?.name,
  }));

  // 클러스터 내 호스트 목록
  const { 
    data: hosts = [],
    isLoading: isHostsLoading
  } = useHostFromCluster(clusterVoId, (e) => ({...e}));

  // cd/dvd에 연결할 iso 목록
  // const {
  //   data: isos = [],
  //   isLoading: isIsoLoading
  // } = useCDFromDataCenter(dataCenterId, (e) => ({...e}));

  // 총 cpu 계산
  const calculateFactors = (num) => {
    const factors = [];
    for (let i = 1; i <= num; i++) {
      if (num % i === 0) factors.push(i);
    }
    return factors;
  };

  const handleCpuChange = (e) => {
    const totalCpu = parseInt(e.target.value, 10);
    if (!isNaN(totalCpu) && totalCpu > 0) {
      setFormSystemState({
        cpuTopologyCnt: totalCpu,
        cpuTopologySocket: totalCpu, // 기본적으로 소켓을 총 CPU로 설정
        cpuTopologyCore: 1,
        cpuTopologyThread: 1,
      });
    }
  };

  const handleSocketChange = (e) => {
    const socket = parseInt(e.target.value, 10);
    const remaining = formSystemState.cpuTopologyCnt / socket;

    setFormSystemState((prev) => ({
      ...prev,
      cpuTopologySocket: socket,
      cpuTopologyCore: remaining, // 나머지 값은 코어로 설정
      cpuTopologyThread: 1, // 스레드는 기본적으로 1
    }));
  };

  const handleCoreChange = (e) => {
    const core = parseInt(e.target.value, 10);
    const remaining = formSystemState.cpuTopologyCnt / (formSystemState.cpuTopologySocket * core);

    setFormSystemState((prev) => ({
      ...prev,
      cpuTopologyCore: core,
      cpuTopologyThread: remaining, // 나머지 값은 스레드로 설정
    }));
  };

  
  // 운영 시스템
  const osSystemList = [
    { value: 'debian_7', label: 'Debian 7+' },
    { value: 'debian_9', label: 'Debian 9+' },
    { value: 'freebsd_9_2', label: 'FreeBSD 9.2' },
    { value: 'freebsd_9_2_x64', label: 'FreeBSD 9.2 x64' },
    { value: 'Linux', label: 'Linux' },
    { value: 'other_linux_kernel_4', label: 'Other Linux(kernel 4.x)' },
    { value: 'other_os', label: 'Other OS' },
    { value: 'red_hat_atomic_7_x64', label: 'Red Hat Atomic 7.x x64' },
    { value: 'red_hat_enterprise_linux_3', label: 'Red Hat Enterprise Linux 3.x' },
    { value: 'red_hat_enterprise_linux_3_x64', label: 'Red Hat Enterprise Linux 3.x x64' },
    { value: 'red_hat_enterprise_linux_4', label: 'Red Hat Enterprise Linux 4.x' },
    { value: 'red_hat_enterprise_linux_4_x64', label: 'Red Hat Enterprise Linux 4.x x64' },
    { value: 'red_hat_enterprise_linux_5', label: 'Red Hat Enterprise Linux 5.x' },
    { value: 'red_hat_enterprise_linux_5_x64', label: 'Red Hat Enterprise Linux 5.x x64' },
    { value: 'red_hat_enterprise_linux_6', label: 'Red Hat Enterprise Linux 6.x' },
    { value: 'red_hat_enterprise_linux_6_x64', label: 'Red Hat Enterprise Linux 6.x x64' },
    { value: 'red_hat_enterprise_linux_7_x64', label: 'Red Hat Enterprise Linux 7.x x64' },
    { value: 'red_hat_enterprise_linux_8_x64', label: 'Red Hat Enterprise Linux 8.x x64' },
    { value: 'red_hat_enterprise_linux_9_x64', label: 'Red Hat Enterprise Linux 9.x x64' },
    { value: 'red_hat_enterprise_linux_coreos', label: 'Red Hat Enterprise Linux CoreOS' },
    { value: 'suse_linux_enterprise_server_11', label: 'SUSE Linux Enterprise Server 11+' },
    { value: 'ubuntu_bionic_beaver_lts', label: 'Ubuntu Bionic Beaver LTS+' },
    { value: 'ubuntu_precise_pangolin_lts', label: 'Ubuntu Precise Pangolin LTS' },
    { value: 'ubuntu_quantal_quetzal', label: 'Ubuntu Quantal Quetzal' },
    { value: 'ubuntu_raring_ringtail', label: 'Ubuntu Raring Ringtail' },
    { value: 'ubuntu_saucy_salamander', label: 'Ubuntu Saucy Salamander' },
    { value: 'ubuntu_trusty_tahr_lts', label: 'Ubuntu Trusty Tahr LTS+' },
    { value: 'windows_10', label: 'Windows 10' },
    { value: 'windows_10_x64', label: 'Windows 10 x64' },
    { value: 'windows_11', label: 'Windows 11' },
    { value: 'windows_2003', label: 'Windows 2003' },
    { value: 'windows_2003_x64', label: 'Windows 2003 x64' },
    { value: 'windows_2008', label: 'Windows 2008' },
    { value: 'windows_2008_r2_x64', label: 'Windows 2008 R2 x64' },
    { value: 'windows_2012_x64', label: 'Windows 2012 x64' },
    { value: 'windows_2012r2_x64', label: 'Windows 2012R2 x64' },
    { value: 'windows_2016_x64', label: 'Windows 2016 x64' },
    { value: 'windows_2019_x64', label: 'Windows 2019 x64' },
    { value: 'windows_2022', label: 'Windows 2022' },
    { value: 'windows_7', label: 'Windows 7' },
    { value: 'windows_7_x64', label: 'Windows 7 x64' },
    { value: 'windows_8', label: 'Windows 8' },
    { value: 'windows_8_x64', label: 'Windows 8 x64' },
    { value: 'windows_xp', label: 'Windows XP' },
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

  // 마이그레이션 모드
  const migrationModeOptionList = [
    { value: 'migratable', label: '수동 및 자동 마이그레이션 허용' },
    { value: 'user_migratable', label: '수동 마이그레이션만 허용' },
    { value: 'pinned', label: '마이그레이션 불가' },
  ];

    // 마이그레이션 정책
  const migrationPolicyOptionList = [
    { value: 'minimal_downtime', label: 'Minimal downtime' },
    { value: 'post_copy', label: 'Post-copy migration' },
    { value: 'suspend_workload', label: 'Suspend workload if needed' },
    { value: 'very_large_vms', label: 'Very large VMs' },
  ];

  // 고가용성
  const priorityOptionList = [
    { value: 1, label: '낮음' },
    { value: 50, label: '중간' },
    { value: 100, label: '높음' },
  ];

  // 부트옵션(첫번째 장치)
  const firstDeviceOptionList = [
    { value: 'hd', label: '하드 디스크' },
    { value: 'cdrom', label: 'CD-ROM' },
    { value: 'network', label: '네트워크(PXE)' },
  ];
  // 부트옵션(두번째 장치)
  const secDeviceOptionList = [
    { value: '', label: '없음' },
    { value: 'ha', label: '하드 디스크'  },
    { value: 'cdrom', label: 'CD-ROM' },
  ];

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
      setSelectedModalTab("common"); // 탭상태 초기화
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
        deleteProtected: vm?.deleteProtected || false
      });
      setFormSystemState({
        memorySize: vm?.memorySize / (1024 * 1024), // 입력된 값는 mb, 보낼 단위는 byte
        maxMemory: vm?.memoryMax / (1024 * 1024),
        allocatedMemory: vm?.memoryActual / (1024 * 1024),
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
        // isSpecificHostSelected: false,
        migrationMode: vm?.migrationMode || migrationModeOptionList[0].value,
        migrationPolicy: vm?.migrationPolicy || migrationPolicyOptionList[0].value,
      });
      setFormHaState({
        ha: vm?.ha || false,
        priority: vm?.priority || 1
      });
      setFormBootState({
        firstDevice: vm?.firstDevice || firstDeviceOptionList[0].value,
        secDevice: vm?.secDevice || secDeviceOptionList[0].value,
        bootingMenu: vm?.bootingMenu || false
      });
      setClusterVoId(vm?.clusterVo?.id || ''); 
      setTemplateVoId(vm?.templateVo?.id || '');
      setOsSystem(vm?.osSystem || 'other_os');
      setChipsetOption(vm?.chipsetFirmwareType || 'Q35_OVMF');
      setOptimizeOption(vm?.optimizeOption || 'SERVER');
      setMigrationModeOption('migratable');
      setMigrationPolicyOption('minimal_downtime');
      setDiskVoList([]);
      setNicVoList([]);
      setHostVoList([]);
      setCdConn('');
    } else if (!editMode && !isClustersLoading) {
      resetForm();
    }
  }, [editMode, vm]);
  
  // 클러스터 목록 가져오기 후 데이터센터 ID 설정
  useEffect(() => {
    if (clusters.length > 0) {
      const selectedCluster = clusters.find((cluster) => cluster.id === clusterVoId);
      if (selectedCluster) {
        setDataCenterId(selectedCluster.dataCenterId || ''); // 데이터센터 ID 설정
        setDataCenterName(selectedCluster.dataCenterName);
      }
    }
  }, [clusterVoId, clusters]);
  
  useEffect(() => {
    if (!editMode && clusters.length > 0) {
      setClusterVoId(clusters[0].id);
      setDataCenterId(clusters[0].dataCenterId);
    }
  }, [clusters, editMode]);
  
  useEffect(() => {
    if (!editMode && templates.length > 0) {
      setTemplateVoId(templates[0].id);
    }
  }, [templates, editMode]);

  useEffect(() => {
    if (!editMode && disks.length > 0) {
      setDiskVoList(disks);
    }
  }, [disks, editMode]);

  useEffect(() => {
    if (!editMode && nics.length > 0) {
      setNicVoList(nics);
    }
  }, [nics, editMode]);

  useEffect(() => {
    if (!editMode && hosts.length > 0) {
      setHostVoList(hosts);
    }
  }, [hosts, editMode]);

  // 근데 iso는 처음에는 지정되면 안됨(선택사항으로 두고)
  // useEffect(() => {
  //   if (!editMode && isos.length > 0) {
  //     setCdConn(hosts);
  //   }
  // }, [isos, editMode]);


  const validateForm = () => {
    if (!formInfoState.name) return '이름을 입력해주세요.';
    if (formSystemState.maxMemory > '9223372036854775807' || formSystemState.memorySize > '9223372036854775807') return '크기를 입력해주세요.';
    if (!clusterVoId) return '클러스터를 선택해주세요.';
    return null;
  };

  const handleFormSubmit = () => { // 디스크  연결은 id값 보내기 생성은 객체로 보내기
    const error = validateForm();
    if (error) {
      toast.error(error);
      return;
    }

    const sizeToBytes = (data) => parseInt(data, 10) * 1024 * 1024; // MB -> Bytes 변환

    const selectedCluster = clusters.find((c) => c.id === clusterVoId);
    const selectedTemplate = templates.find((t) => t.id === templateVoId);

    const dataToSubmit = {
      clusterVo: { id: selectedCluster.id,name: selectedCluster.name },
      templateVo: { id: selectedTemplate.id, name: selectedTemplate.name },
      diskVoList: {},
      nicVos: nicVoList.map((nic) => ({
        name: nic.name,
        vnicProfileVo: {
          id: nic.vnicProfileVo?.id || null,
          name: nic.vnicProfileVo?.name || '',
        },
      })),
      hostVoList: {},
      cdConn: {},
      ...formInfoState,
      ...formSystemState,
      ...formCloudInitState,
      ...formHostState,
      ...formHaState,
      ...formBootState
    };

    console.log('가상머신 생성or편집데이터 확인:', dataToSubmit); 

    if (editMode) {
      dataToSubmit.id = formInfoState.id;
      editVM(
        { vmId: formInfoState.id, vmdata: dataToSubmit }, 
        {
          onSuccess: () => {
            onClose();
            toast.success('가상머신 편집 완료');
          },
          onError: (error) => toast.error('Error editing vm:', error),
        });
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
                  onChange={(e) => setClusterVoId(e.target.value)}
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
                <span>
                  데이터센터:{" "}
                  {clusters.find((cluster) => cluster.id === clusterVoId)?.name}
                </span>
              </div>

              <div>
                <label htmlFor="template" style={{ color: 'gray' }}>템플릿</label>
                <select
                  id="template"
                  value={templateVoId}
                  onChange={(e) => setTemplateVoId(e.target.value)}
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

              <div className="network-form-group">
                <label htmlFor="os">운영 시스템</label>
                <select
                  id="os"
                  value={osSystem} 
                  onChange={(e) => setOsSystem(e.target.value)}
                >
                  {osSystemList.map((os) => (
                    <option key={os.value} value={os.value}>
                      {os.label} {/* UI에 표시되는 값 */}
                    </option>
                  ))}
                </select>
                <span>
                  선택된 운영 시스템: {osSystem}
                </span>
              </div>

              <div className="network-form-group">
                <label htmlFor="chipset">칩셋/펌웨어 유형</label>
                <select 
                  id="chipset" 
                  value={chipsetOption} 
                  onChange={(e) => setChipsetOption(e.target.value)}
                >
                  {chipsetOptionList.map((chipset) => (
                    <option key={chipset.value} value={chipset.value}>
                      {chipset.label} 
                    </option>
                  ))}
                </select>
                <span>선택된 칩셋: {chipsetOption}</span>
              </div>

              <div style={{ marginBottom: '2%' }}>
                <label htmlFor="optimization">최적화 옵션</label>
                <select
                  id="optimization"
                  value={optimizeOption}
                  onChange={(e) => setOptimizeOption(e.target.value)} 
                >
                  {optimizeOptionList.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label} 
                    </option>
                  ))}
                </select>
                <span>선택된 최적화 옵션: {optimizeOption}</span>
              </div>
            </div>
            
            {selectedModalTab === 'common' && 
            <>
              <div className="edit-second-content mb-1">
                <div>
                  <label htmlFor="name">이름</label>
                  <input
                    type="text"
                    id="name"
                    value={formInfoState.name}
                    onChange={(e) => setFormInfoState((prev) => ({ ...prev, name: e.target.value }))}
                  />
                </div>
                <div>
                  <label htmlFor="description">설명</label>
                  <input
                    type="text"
                    id="description"
                    value={formInfoState.description}
                    onChange={(e) => setFormInfoState((prev) => ({ ...prev, description: e.target.value }))}
                  />
                </div>
                <div>
                  <label htmlFor="comment">코멘트</label>
                  <input
                      type="text"
                      id="comment"
                      value={formInfoState.comment}
                      onChange={(e) => setFormInfoState((prev) => ({ ...prev, comment: e.target.value }))}
                  />
                </div>
              </div>
  
              <div className="px-1 font-bold">인스턴스 이미지</div>
              <div
                className="edit-third-content"
                style={{ borderBottom: "1px solid gray", marginBottom: "0.2rem" }}
              >
                <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
                <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
                
                <VmDiskConnectionModal
                  isOpen={isConnectionPopupOpen}
                  dataCenterId = {dataCenterId}
                  onRequestClose={() => setIsConnectionPopupOpen(false)}
                />
                <VmDiskModal
                  isOpen={isCreatePopupOpen}
                  dataCenterId = {dataCenterId}
                  onClose={() => setIsCreatePopupOpen(false)}
                />
              </div>

              <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                <VmNic
                  editMode={editMode}
                  // initialNics={nics}
                  availableProfiles={nics}
                />
              </div>
            </>
            }

            {selectedModalTab === 'system' && 
            <>
              <div className="edit-second-content">
                <div>
                  <label htmlFor="memory_size">메모리 크기(MB)</label>
                  <input
                    type="number"
                    id="memory_size"
                    autoFocus
                    value={formSystemState.memorySize} // 메모리 크기
                    onChange={(e) => setFormSystemState((prev) => ({ ...prev, memorySize: e.target.value }))}
                  />
                </div>
                <div>
                  <div>
                    <label htmlFor="max_memory">최대 메모리(MB)</label>
                    <InfoTooltip 
                      tooltipId="max-memory-tooltip"
                      message={'메모리 핫 플러그를 실행할 수 있는 가상 머신 메모리 상한'}
                    />
                  </div>
                  <input
                    type="number"
                    id="max_memory"
                    value={formSystemState.maxMemory}
                    onChange={(e) => setFormSystemState((prev) => ({ ...prev, maxMemory: e.target.value }))}
                  />
                </div>
                <div>
                  <div>
                    <label htmlFor="actual_memory">할당할 실제 메모리(MB)</label>
                    <InfoTooltip 
                      tooltipId="actual-memory-tooltip"
                      message={'ballooning 기능 사용 여부에 관계없이 가상 머신에 확보된 메모리 양입니다.'}
                    />
                  </div>
                  <input
                    type="number"
                    id="actual_memory"
                    value={formSystemState.allocatedMemory} // 실제 메모리
                    onChange={(e) => setFormSystemState((prev) => ({ ...prev, allocatedMemory: e.target.value }))}
                  />
                </div>
                <div>
                < div>
                    <label htmlFor="total_cpu">총 가상 CPU</label>
                    <input
                      type="number"
                      id="total_cpu"
                      value={formSystemState.cpuTopologyCnt}
                      onChange={handleCpuChange}
                      min="1"
                    />
                  </div>
                  <div className="network_form_group">
                    <label htmlFor="virtual_socket">가상 소켓</label>
                    <select
                      id="virtual_socket"
                      value={formSystemState.cpuTopologySocket}
                      onChange={handleSocketChange}
                    >
                      {calculateFactors(formSystemState.cpuTopologyCnt).map((factor) => (
                        <option key={factor} value={factor}>
                          {factor}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="network_form_group">
                    <label htmlFor="core_per_socket">가상 소켓 당 코어</label>
                    <select
                      id="core_per_socket"
                      value={formSystemState.cpuTopologyCore}
                      onChange={handleCoreChange}
                    >
                      {calculateFactors(
                        formSystemState.cpuTopologyCnt / formSystemState.cpuTopologySocket
                      ).map((factor) => (
                        <option key={factor} value={factor}>
                          {factor}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="network_form_group">
                    <label htmlFor="thread_per_core">코어당 스레드</label>
                    <select
                      id="thread_per_core"
                      value={formSystemState.cpuTopologyThread}
                      onChange={(e) =>
                        setFormSystemState((prev) => ({
                          ...prev,
                          cpuTopologyThread: parseInt(e.target.value, 10),
                        }))
                      }
                    >
                      {calculateFactors(
                        formSystemState.cpuTopologyCnt /
                          (formSystemState.cpuTopologySocket *
                            formSystemState.cpuTopologyCore)
                      ).map((factor) => (
                        <option key={factor} value={factor}>
                          {factor}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

              </div>
            </>
            } 

            {selectedModalTab === 'beginning' && 
            <>
              <div className='p-1.5'>
                <div className='checkbox_group mb-1.5'>
                  <input 
                    type="checkbox" 
                    id="enableBootMenu" 
                    name="enableBootMenu" 
                    checked={formCloudInitState.cloudInit} // cloudInit 상태를 checked 속성에 바인딩
                    onChange={(e) => setFormCloudInitState((prev) => ({ ...prev, cloudInit: e.target.value }))}
                    // onChange={(e) => {
                    //   setCloudInit(e.target.checked); // 상태 업데이트
                    //   if (!e.target.checked) {
                    //     setDomainHiddenBoxVisible(false); // 체크 해제 시 숨김 처리
                    //   }
                    // }}
                  />
                  <label htmlFor="enableBootMenu">Cloud-lnit</label>
                </div>

              </div>
            </>
            }

            {selectedModalTab === 'host' && 
            <>
              <div className="host-second-content">
                {/* <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                <div className="form-checks">
                  <div>
                    <input
                      className="form-check-input"
                      type="radio"
                      name="hostSelection"
                      id="flexRadioDefault1"
                      checked={!isSpecificHostSelected}
                      onChange={() => setIsSpecificHostSelected(false)} // 기본 클러스터 선택
                    />
                    <label className="form-check-label" htmlFor="flexRadioDefault1">
                      클러스터 내의 호스트
                    </label>
                  </div> */}
                  {/* <div>
                    <div>
                      <input
                        className="form-check-input"
                        type="radio"
                        name="hostSelection"
                        id="flexRadioDefault2"
                        checked={isSpecificHostSelected}
                        onChange={(e) => setIsSpecificHostSelected(true)} // 특정 호스트 선택
                      />
                      <label className="form-check-label" htmlFor="flexRadioDefault2">
                        특정 호스트
                      </label>
                    </div>
                    <div>
                      <select
                        id="specific_host_select"
                        disabled={!isSpecificHostSelected} // 특정 호스트 선택 여부에 따라 활성화
                      >                    
                        {hostVoList.map((host) => (
                          <option key={host.id} value={host.id}>
                            {host.name}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div> */}
                {/* </div> */}
              </div>

              <div className="host-third-content">
                <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                <div>
                  <label htmlFor="migration_mode">마이그레이션 모드</label>
                  <select
                    id="migration_mode"
                    value={formHaState.migrationMode}
                    onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationMode: e.target.value }))}
                  >
                    {migrationModeOptionList.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                  <p>선택된 마이그레이션 모드: {migrationModeOptionList.find((option) => option.value === formHaState.migrationMode)?.label || "선택되지 않음"}</p>
                </div>
                <div>
                  <label htmlFor="migration_policy">마이그레이션 정책</label>
                  <select
                    id="migration_policy"
                    value={formHaState.migrationPolicy}
                    onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationPolicy: e.target.value }))}
                  >
                    {migrationModeOptionList.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                </div>                            
                <div>
                  <div>
                    <span>마이그레이션 병행</span>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                  </div>
                  <select id="parallel_migrations" readOnly>
                    <option value="클러스터 기본값(Disabled)">클러스터 기본값(Disabled)</option>
                  </select>
                </div>
                <div className='network_checkbox_type1 disabled'>
                  <label htmlFor="memory_size">마이그레이션 병행 개수</label>
                  <input type="text" id="memory_size" value="" readOnly disabled/>
                </div>
              </div>
            </>
            }

            {selectedModalTab === 'ha_mode' && 
            <>
              <div className="ha-mode-second-content">
                <div className="checkbox_group">
                  <input
                    className="check_input"
                    type="checkbox"
                    id="ha_mode_box"
                    checked={formHaState.ha}
                    onChange={(e) => setFormHaState((prev) => ({ ...prev, ha: e.target.value }))}
                  />
                  <label className="check_label" htmlFor="ha_mode_box">
                    고가용성
                  </label>
                </div>
                <div>
                  <div>
                    <span>가상 머신 임대 대상 스토리지 도메인</span>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }} fixedWidth />
                  </div>
                  <select id="no_lease" disabled={!formHaState.ha}> {/* ha가 false면 disabled */}
                    <option value="가상 머신 임대 없음">가상 머신 임대 없음</option>
                  </select>
                </div>
                <div>
                  <div>
                    <span>재개 동작</span>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                  </div>
                  <select id="force_shutdown">
                    <option value="강제 종료">강제 종료</option>
                  </select>
                </div>

                <div className="ha-mode-article">
                  <span>실행/마이그레이션 큐에서 우선순위 : </span>
                  <div>
                    <span>우선 순위</span>
                    <select
                      id="priority"
                      value={formHaState.priority} // 선택된 값
                      onChange={(e) => formHaState((prev) => ({ ...prev, priority: e.target.value }))}
                    >
                      {priorityOptionList.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>    
              </div>
            </>
            }

            {selectedModalTab === 'boot_outer' && 
            <>  
              <div className='boot_outer_content'>
                <div className="cpu-res">
                  <span style={{ fontWeight: 600 }}>부트순서:</span>
                  <div className='cpu-res-box'>
                    <span>첫 번째 장치</span>
                    <select
                      id="first_boot_device"
                      value={formBootState.firstDevice}
                      onChange={(e) => setFormBootState((prev) => ({ ...prev, firstDevice: e.target.value }))}
                    >
                      {firstDeviceOptionList.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className='cpu-res-box'>
                    <span>두 번째 장치</span>
                    <select
                      id="second_boot_device"
                      value={formBootState.secDevice}
                      onChange={(e) => setFormBootState((prev) => ({ ...prev, secDevice: e.target.value }))}
                    >
                      {secDeviceOptionList.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
                <div className="boot-checkboxs">
                  {/* <div>
                    <div className="checkbox_group">
                      <input
                        type="checkbox"
                        id="connectCdDvd"
                        name="connectCdDvd"
                        checked={isCdDvdChecked} // 체크박스 상태
                        onChange={handleCdDvdCheckboxChange} // 핸들러 호출
                      />
                      <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                    </div>
                    <div className="text_icon_box">
                      <select
                        id="cd_dvd_select"
                        disabled={!isCdDvdChecked || cdList.length === 0} // 체크 여부와 데이터 유무에 따라 비활성화
                        value={connVoId}
                        onChange={handleCdDvdChange} // 핸들러 호출
                      >
                        <option value="">CD/DVD 선택...</option>
                        {cdList.map((cd) => (
                          <option key={cd.id} value={cd.id}>
                            {cd.name}
                          </option>
                        ))}
                      </select>
                      <FontAwesomeIcon
                        icon={faInfoCircle}
                        style={{ color: 'rgb(83, 163, 255)' }}
                        fixedWidth
                      />
                    </div>
                  </div> */}
                  <div className='checkbox_group mb-1.5'>
                    <input
                      className="check_input"
                      type="checkbox"
                      id="enableBootMenu"
                      checked={formBootState.bootingMenu} 
                      onChange={(e) => setFormBootState((prev) => ({ ...prev, bootingMenu: e.target.value }))}
                    />
                    <label className="check_label" htmlFor="enableBootMenu">
                      부팅 메뉴를 활성화
                    </label>
                  </div>
                </div>
              </div>
            </>
            }
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


