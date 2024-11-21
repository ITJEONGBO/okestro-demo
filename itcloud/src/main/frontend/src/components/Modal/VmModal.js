import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle, faChevronCircleRight } from '@fortawesome/free-solid-svg-icons';
import { Tooltip } from 'react-tooltip';
import { 
  useAddVm, 
  useAllClusters, 
  useAllTemplates, 
  useAllVMs, 
  useEditVm, 
  useHostFromCluster, 
  useVmById 
} from '../../api/RQHook';
import VmConnectionPlusModal from './VmConnectionPlusModal';
import VmCreatePlusModal from './VmCreatePlusModal';

const VmModal = ({ 
  isOpen, 
  onRequestClose, 
  editMode = false,
  vmdata,
  vmId,
}) => {

  // 일반
  const [id, setId] = useState('');
  const [clusterVoName, setClusterVoName] = useState(''); // 클러스터이름
  const [clusterVoId, setClusterVoId] = useState('');  
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [stateless, setStateless] = useState(false); // 무상태
  const [startPaused, setStartPaused] = useState(false); // 일시중지상태로시작
  const [deleteProtected, setDeleteProtected] = useState(false); //삭제보호
  const [templateId, setTemplateId] = useState('');

  //시스템
  const [memorySize, setMemorySize] = useState(''); // 메모리 크기
  const [maxMemory, setMaxMemory] = useState(''); //최대메모리
  const [allocatedMemory, setAllocatedMemory] = useState(''); //실제 메모리
  const [cpuTopologyCnt, setCpuTopologyCnt] = useState(1); //총cpu
  const [cpuTopologyCore, setCpuTopologyCore] = useState(1); // 가상 소켓 당 코어
  const [cpuTopologySocket, setCpuTopologySocket] = useState(1); // 가상소켓
  const [cpuTopologyThread, setCpuTopologyThread] = useState(1); //코어당 스레드

  // 초기실행
  const [cloudInit, setCloudInit] = useState(false); // Cloud-lnit
  const [script, setScript] = useState(''); // 스크립트
                                                              
  // 호스트
  const [isSpecificHostSelected, setIsSpecificHostSelected] = useState(false); // 클러스터 호스트 라디오버튼클릭
  const [hostInCluster, setHostInCluster] = useState(''); 
  const [hostVoId, setHostVoId] = useState(''); 
  const [migrationMode, setMigrationMode] = useState('');  // 마이그레이션 모드
  const [migrationPolicy, setMigrationPolicy] = useState('');  // 마이그레이션 정책
  const [hostsFromCluster, setHostsFromCluster] = useState([]); // 호스트 목록
  
  // 고가용성
  const [ha, setHa] = useState(false); // 고가용성(체크박스)


  const { mutate: addVM } = useAddVm();
  const { mutate: editVM } = useEditVm();


  const { 
    data: allvm, 
  } = useAllVMs(vmId);

// 가상머신 상세데이터 가져오기
  const { 
    data: vm, 
  } = useVmById(vmId);

  // vm 데이터 변경 시 콘솔에 출력
useEffect(() => {
  if (vm) {
    console.log('가져온 가상머신 데이터:', vm);
  }
}, [vm]);

  // 클러스터 가져오기
  const {
    data: clusters,
  } = useAllClusters((e) => ({
    ...e,
  }));
  useEffect(() => {
    if (clusters) {
      console.log('가져온 모든 클러스터목록:', clusters);
    }
  }, [clusters]);

  // 클러스터 ID에대한 호스트목록
  const { data: hostsData } = useHostFromCluster(clusterVoId, (e) => ({
    id: e.id,
    name: e.name,
  }));

  // 템플릿 가져오기
  const {
    data: templates,
  } = useAllTemplates((e) => ({
    ...e,
  }));

  useEffect(() => {
    // editMode가 아닐 때만 첫 번째 데이터센터 ID를 기본값으로 설정
    if (!editMode && clusters && clusters.length > 0) {
      setClusterVoId(clusters[0].id);
    }
  }, [editMode, clusters]);
useEffect(() => {
  if (editMode && vmdata?.templateId) {
    setTemplateId(vmdata.templateId); // 편집 모드: 기존 템플릿 ID
  } else if (!editMode && templates && templates.length > 0) {
    setTemplateId(templates[0].id); // 생성 모드: 첫 번째 템플릿 ID 설정
  }
}, [editMode, vmdata, templates]);

  // 특정 호스트 라디오 버튼 클릭 핸들러
  const handleSpecificHostSelection = (e) => {
    setIsSpecificHostSelected(e.target.checked); // 상태 업데이트
  };
  // useEffect로 hostsData를 상태로 업데이트
  useEffect(() => {
    if (hostsData) {
      setHostsFromCluster(hostsData);
    }
  }, [hostsData]);

// 운영 시스템 및 칩셋 옵션 상태
const [osOptions, setOsOptions] = useState([
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
]);
// 칩셋 옵션
const [chipsetOptions, setChipsetOptions] = useState([
  { value: 'CLUSTER_DEFAULT', label: '클러스터 기본값' },
  { value: 'I440FX_SEA_BIOS', label: 'BIOS의 I440FX 칩셋' },
  { value: 'Q35_OVMF', label: 'UEFI의 Q35 칩셋' },
  { value: 'Q35_SEA_BIOS', label: 'BIOS의 Q35 칩셋' },
  { value: 'Q35_SECURE_BOOT', label: 'UEFI SecureBoot의 Q35 칩셋' },
]); 
// 최적화옵션
const [optimizeOption, setOptimizeOption] = useState([
  { value: 'DESKTOP', label: '데스크톱' },
  { value: 'HIGH_PERFORMANCE', label: '고성능' },
  { value: 'SERVER', label: '서버' }
]);
// 마이그레이션 모드
const [migrationModeOptions, setMigrationModeOptions] = useState([
  { value: 'migratable', label: '수동 및 자동 마이그레이션 허용' },
  { value: 'user_migratable', label: '수동 마이그레이션만 허용' },
  { value: 'pinned', label: '마이그레이션 불가' },
]);
  // 마이그레이션 정책
const [migrationPolicyOptions, setMigrationPolicyOptions] = useState([
  { value: 'minimal_downtime', label: 'Minimal downtime' },
  { value: 'post_copy', label: 'Post-copy migration' },
  { value: 'suspend_workload', label: 'Suspend workload if needed' },
  { value: 'very_large_vms', label: 'Very large VMs' },
]);
// 고가용성
const [priority, setPriority] = useState([
  { value: 1, label: '낮음' },
  { value: 50, label: '중간' },
  { value: 100, label: '높음' },
]); 

// 선택된 값 상태
const [selectedOs, setSelectedOs] = useState('Linux'); // 운영 시스템 선택
const [selectedChipset, setSelectedChipset] = useState('Q35_OVMF'); // 칩셋 선택
const [selectedOptimizeOption, setSelectedOptimizeOption] = useState('SERVER'); // 칩셋 선택

useEffect(() => {
  // 초기 상태 설정 (필요하면 기본값 설정 가능)
  setSelectedOs('Linux'); // 초기 운영 시스템
  setSelectedChipset('Q35_OVMF'); // 초기 칩셋
  setSelectedChipset('SERVER'); // 초기 칩셋
}, []);




// 초기값 설정
useEffect(() => {
    if (isOpen) {
      if (editMode && vm) {
        console.log("편집 모드에서 가져온 VM 데이터:", vm);
  
        setId(vm?.id || '');
        setName(vm?.name || '');
        setClusterVoName(vm?.clusterVo?.name || '');
        setClusterVoId(vm?.clusterVo?.id || '');
        setDescription(vm?.description || '');
        setSelectedOs(vm?.osSystem || 'Linux'); // 운영 체제
        setSelectedChipset(vm?.chipsetFirmwareType || 'Q35_OVMF'); // 칩셋
        setSelectedOptimizeOption(vm?.optimizeOption || 'SERVER'); // 최적화 옵션

        setComment(vm?.comment || '');
        setStateless(vm?.stateless || false);
        setStartPaused(vm?.startPaused || false);
        setDeleteProtected(vm?.deleteProtected || false);

        // 시스템
        setMaxMemory(vm?.memoryMax);
        setAllocatedMemory(vm?.memoryActual);
        setMemorySize(vm?.memorySize);
        setCpuTopologyCnt(vm?.cpuTopologyCnt || 1);
        setCpuTopologyCore(vm?.cpuTopologyCore || 1);
        setCpuTopologySocket(vm?.cpuTopologySocket || 1);
        setCpuTopologyThread(vm?.cpuTopologyThread || 1);
    
        //초기실행
        setCloudInit(vm?.cloudInit);
        setScript(vm?.setScript);

        // 호스트
        // 마이그레이션 모드와 정책 기본값 설정
        setMigrationMode(vm?.migrationMode || migrationModeOptions[0].value);
        setMigrationPolicy(vm?.migrationPolicy || migrationPolicyOptions[0].value);

        // 고가용성
        setHa(vm?.ha || false);
        setPriority(vm?.priority || 1);

      } else if (!editMode) {
        resetForm();    
        setClusterVoId(vmId || clusters?.[0]?.id || '');
        setMaxMemory(4096 * 1024); // 기본 최대 메모리 (KB 단위)
        setAllocatedMemory(1024 * 1024); // 기본 할당 메모리 (KB 단위)
        setMemorySize(1024 * 1024); // 기본 메모리 크기 (KB 단위)
        setCpuTopologyCnt(1);
        // 마이그레이션 모드와 정책 기본값 설정
        setMigrationMode(vm?.migrationMode || migrationModeOptions[0].value);
        setMigrationPolicy(vm?.migrationPolicy || migrationPolicyOptions[0].value);
      }
    }
  }, [isOpen, editMode, vm, osOptions, chipsetOptions,optimizeOption]);

    // 메모리관련 -> KB를 MB로 변환하고 소수점 제거
  const formatMemory = (valueInKB) => {
    return `${Math.floor(valueInKB / 1024)} MB`;
  };  

  const resetForm = () => {
    setId('');
    setClusterVoId('');
    setName('');
    setDescription('');
    setComment('');
    setStateless(false); // 기본값 설정
    setStartPaused(false); // 기본값 설정
    setDeleteProtected(false); // 기본값 설정
    setSelectedOs('Linux'); // 기본값
    setSelectedChipset('Q35_SEA_BIOS'); // 기본값
    setCpuTopologyCnt(1);
    setCpuTopologyCore(1);
    setCpuTopologySocket(1);
    setCpuTopologyThread(1);
    setCloudInit(false);
    setScript('');

  };

  const handleFormSubmit = () => {
    const selectedCluster = clusters.find((c) => c.id === clusterVoId);
    if (!selectedCluster) {
        alert("클러스터를 선택해주세요.");
        return;
      }
    // 선택된 템플릿 찾기
    const selectedTemplate = templates.find((t) => t.id === templateId);
    if (!selectedTemplate) {
        alert("네트워크를 선택해주세요.");
        return;
      }
  
    const dataToSubmit = {
      clusterVo:{
        id: selectedCluster.id,
        name: selectedCluster.name,
      },
      tempatlateVo:{
        id: selectedTemplate.id,
        name: selectedTemplate.name
      },
      name,
      description,
      comment,
      osOptions:selectedOs,
      chipsetFirmwareType:selectedChipset,// 선택된 칩셋
      optimizeOption: selectedOptimizeOption, // 선택된 최적화 옵션
      stateless,  //boolean
      startPaused,   //boolean
      deleteProtected, //boolean
      //시스템데이터
      memorySize,
      maxMemory,
      allocatedMemory,
      cpuTopologyCnt, // 총가상 cpu
      cpuTopologyCore, // 가상 소켓 당 코어
      cpuTopologySocket, // 가상소켓
      cpuTopologyThread, // 코어당 스레드
    
      // 초기 실행 데이터 (예: cloud-init 관련)
      cloudInit, 
      script,

      //호스트
      migrationMode, // string 마이그레이션모드
      migrationPolicy, // 마이그레이션 정책

      // 고가용성
      ha, // boolean
      priority //int
      
    };
    console.log('가상머신 생성데이터 확인:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인



    if (editMode) {
      dataToSubmit.id = id; // 수정 모드에서는 id를 추가
      editVM({
        vmId: id,
        vmdata: dataToSubmit
      }, {
        onSuccess: () => {
          alert('가상머신 편집 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error editing vm:', error);
        }
      });
    } else {
      addVM(dataToSubmit, {
        onSuccess: () => {
          alert('가상머신 생성 완료');
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding vm:', error);
        }
      });
    }
  };

const [activeSection, setActiveSection] = useState('common_outer');
const [activeLunTab, setActiveLunTab] = useState('target_lun');
const handleSectionChange = (section) => setActiveSection(section);
const [selectedModalTab, setSelectedModalTab] = useState('common');


// 추가 모달
const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
  // 새로만들기->초기실행 화살표 누르면 밑에열리기
  const [isDomainHiddenBoxVisible, setDomainHiddenBoxVisible] = useState(false);
  const toggleDomainHiddenBox = () => {
    setDomainHiddenBoxVisible(!isDomainHiddenBoxVisible);
  };
return (
  <Modal
    isOpen={isOpen}
    onRequestClose={onRequestClose}
    contentLabel="가상머신 생성"
    className="Modal"
    overlayClassName="Overlay"
    shouldCloseOnOverlayClick={false}
  >
    <div className="vm_edit_popup">
      <div className="popup_header">
          <h1>{editMode ? '가상머신 편집' : '가상머신 생성'}</h1>
        <button onClick={onRequestClose}>
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
                  <div
                  id="common_tab"
                  className={selectedModalTab === 'common' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('common')}
                  >
                  일반
                  </div>
                  <div
                  id="system_tab"
                  className={selectedModalTab === 'system' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('system')}
                  >
                  시스템
                  </div>
                  <div
                  id="beginning_tab"
                  className={selectedModalTab === 'beginning' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('beginning')}
                  >
                  초기 실행
                  </div>
                  <div
                  id="host_tab"
                  className={selectedModalTab === 'host' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('host')}
                  >
                  호스트
                  </div>
                  <div
                  id="ha_mode_tab"
                  className={selectedModalTab === 'ha_mode' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('ha_mode')}
                  >
                  고가용성
                  </div>
                  <div
                  id="boot_option_tab"
                  className={selectedModalTab === 'boot_outer' ? 'active-tab' : 'inactive-tab'}
                  onClick={() => setSelectedModalTab('boot_outer')}
                  >
                  부트 옵션
                  </div>

              </div>

          {/* 탭 내용 */}
          <div className="vm_edit_select_tab">
            <div className="edit_first_content">
                        <div>
                              <label htmlFor="cluster">클러스터</label>
                              <select 
                                  id="cluster_location" 
                                  value={clusterVoId} 
                                  onChange={(e) => setClusterVoId(e.target.value)} 
                                  
                                  >
                                  {editMode && vm?.clusterName ? (
                                      // 편집 모드에서는 고정된 클러스터만 표시
                                      clusters && clusters.map((cluster) => (
                                      <option value={vm.clusterName}>{vm.clusterName}</option>
                                    ))
                                  ) : (
                                      // editMode가 아닐 경우 클러스터 목록 표시
                                      clusters && clusters.map((cluster) => (
                                      <option key={cluster.id} value={cluster.id}>{cluster.name}</option>
                                      ))
                                  )}
                              </select>
                             
                          </div>

                          <div>
                            <label htmlFor="template" style={{ color: 'gray' }}>템플릿</label>
                            <select
                              id="template"
                              value={templateId} // 선택된 템플릿 ID와 동기화
                              onChange={(e) => setTemplateId(e.target.value)} // 상태 업데이트
                              disabled={editMode} // 편집 모드일 경우 비활성화
                            >
                              {editMode && vmdata?.templateName ? (
                                // 편집 모드에서는 고정된 템플릿만 표시
                                <option value={vmdata.templateId}>{vmdata.templateName}</option>
                              ) : (
                                // 생성 모드에서는 템플릿 목록을 표시
                                templates &&
                                templates.map((template) => (
                                  <option key={template.id} value={template.id}>
                                    {template.name} {/* 템플릿 이름 표시 */}
                                  </option>
                                ))
                              )}
                            </select>
                          </div>

                          <div className="network_form_group">
                            <label htmlFor="os">운영 시스템</label>
                            <select
                              id="os"
                              value={selectedOs} // 선택된 값과 동기화
                              onChange={(e) => setSelectedOs(e.target.value)} // 값 변경 핸들러
                            >
                              {osOptions.map((os) => (
                                <option key={os.value} value={os.value}>
                                  {os.label} {/* UI에 표시되는 값 */}
                                </option>
                              ))}
                            </select>
                            <span>
                              선택된 운영 시스템: {osOptions.find((opt) => opt.value === selectedOs)?.label || ''}
                            </span>
                          </div>

                          <div className="network_form_group">
                              <label htmlFor="chipset">칩셋/펌웨어 유형</label>
                              <select 
                                id="chipset" 
                                value={selectedChipset} 
                                onChange={(e) => setSelectedChipset(e.target.value)}
                              >
                          
                                {chipsetOptions.map((chipset) => (
                                  <option key={chipset.value} value={chipset.value}>
                                    {chipset.label} {/* 화면에 표시될 한글 */}
                                  </option>
                                ))}
                              </select>
                              <span>선택된 칩셋: {selectedChipset}</span>
                            </div>

                            <div style={{ marginBottom: '2%' }}>
                              <label htmlFor="optimization">최적화 옵션</label>
                              <select
                                id="optimization"
                                value={selectedOptimizeOption} // 선택된 값과 동기화
                                onChange={(e) => setSelectedOptimizeOption(e.target.value)} // 값 변경 핸들러
                              >
                                {optimizeOption.map((option) => (
                                  <option key={option.value} value={option.value}>
                                    {option.label} {/* UI에 표시되는 값 */}
                                  </option>
                                ))}
                              </select>
                              <span>선택된 최적화 옵션: {optimizeOption.find(opt => opt.value === selectedOptimizeOption)?.label || ''}</span>
                            </div>

              </div>
              {selectedModalTab === 'common' && 
                <>
            
                        <div className="edit_second_content mb-1">
                            <div>
                                <label htmlFor="name">이름ddd</label>
                                <input
                                    type="text"
                                    id="name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)} 
                                />
                            </div>
                            <div>
                                <label htmlFor="description">설명</label>
                                <input
                                type="text"
                                id="description"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)} 
                            />
                            </div>
                            <div>
                                <label htmlFor="comment">코멘트</label>
                                <input
                                    type="text"
                                    id="comment"
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)} 
                                />
                            </div>
                        </div>
                        <div className="px-1 font-bold">인스턴스 이미지</div>
                          <div
                          className="edit_third_content"
                          style={{ borderBottom: "1px solid gray", marginBottom: "0.2rem" }}
                          >
                          {editMode ? (
                              // 편집 모드일 때
                              <div className='vm_plus_btn_outer'>
                        
                              <div className='vm_plus_btn'>
                                  <div >#</div>
                                  <div className='flex'>
                                      <button className="mr-1" onClick={() => setIsEditPopupOpen(true)}>
                                      편집
                                      </button>
                                      <div className="flex">
                                          <button>+</button>
                                          <button>-</button>
                                      </div>
                                  </div>
                              </div>
                              </div>
                          ) : (
                              // 생성 모드일 때
                              <div className='vm_plus_btn_outer'>
                            
                              <div className='vm_plus_btn'>
                                      <div style={{color:'white'}}>.</div>
                                      <div className='flex'>
                                          <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
                                          <button className="mr-1" onClick={() => setIsCreatePopupOpen(true)}>생성</button>
                                          <div className="flex">
                                              <button>+</button>
                                              <button>-</button>
                                          </div>
                                      </div>
                              </div>
                              </div>
                          )}
                          </div>

                        
                        <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
                            
                            <div className='edit_fourth_content_select flex'>
                                <label htmlFor="network_adapter">nic1</label>
                                <select id="network_adapter">
                                    <option value="default">Default</option>
                                </select>
                            </div>
                            <div className='flex'>
                                <button>+</button>
                                <button>-</button>
                            </div>
                        </div>
                </>
              }
              {selectedModalTab === 'system' && 
                <>
                  
                    <div className="edit_second_content">
                      <div>
                        <label htmlFor="memory_size">메모리 크기</label>
                        <input
                          type="text"
                          id="memory_size"
                          value={formatMemory(memorySize)} // 메모리 크기
                          readOnly
                        />
                      </div>
                      <div>
                        <div>
                          <label htmlFor="max_memory">최대 메모리</label>
                          <FontAwesomeIcon
                            icon={faInfoCircle}
                            style={{ color: 'rgb(83, 163, 255)', marginLeft: '5px' }}
                            data-tooltip-id="max-memory-tooltip"
                          />
                          <Tooltip
                            id="max-memory-tooltip"
                            className="icon_tooltip"
                            place="top"
                            effect="solid"
                          >
                            메모리 핫 플러그를 실행할 수 있는 가상 머신 메모리 상한
                          </Tooltip>
                        </div>
                        <input
                          type="text"
                          id="max_memory"
                          value={formatMemory(maxMemory)} // 최대 메모리
                          readOnly
                        />
                      </div>

                      <div>
                        <div>
                          <label htmlFor="actual_memory">할당할 실제 메모리</label>
                          <FontAwesomeIcon
                            icon={faInfoCircle}
                            style={{ color: 'rgb(83, 163, 255)', marginLeft: '5px' }}
                            data-tooltip-id="actual-memory-tooltip"
                          />
                          <Tooltip
                            id="actual-memory-tooltip"
                            className="icon_tooltip"
                            place="top"
                            effect="solid"
                          >
                            ballooning 기능 사용 여부에 관계없이 가상 머신에 확보된 메모리 양입니다.
                          </Tooltip>
                        </div>
                        <input
                          type="text"
                          id="actual_memory"
                          value={formatMemory(allocatedMemory)} // 실제 메모리
                          readOnly
                        />
                      </div>

                      <div>
    <div>
      <label htmlFor="total_cpu">총 가상 CPU</label>
      <FontAwesomeIcon
        icon={faInfoCircle}
        style={{ color: 'rgb(83, 163, 255)', marginLeft: '5px' }}
        data-tooltip-id="total-cpu-tooltip"
      />
      <Tooltip
        id="total-cpu-tooltip"
        className="icon_tooltip"
        place="top"
        effect="solid"
      >
        소켓 수를 변경하여 CPU를 핫애드합니다. CPU 핫애드가 올바르게 지원되는지
        확인하려면 게스트 운영 체제 관련 문서를 참조하십시오.
      </Tooltip>
    </div>
    <input
      type="text"
      id="total_cpu"
      value={cpuTopologyCnt}
      onChange={(e) => setCpuTopologyCnt(Number(e.target.value))}
      min="1"
    />
  </div>

                      <div className='network_form_group'>
    <label htmlFor="virtual_socket">가상 소켓</label>
    <select
      id="virtual_socket"
      value={cpuTopologySocket} // 현재 상태 값
      onChange={(e) => setCpuTopologySocket(e.target.value)} // 상태 업데이트
    >
      {editMode ? (
        // 편집 모드일 때 vm 값만 옵션으로 표시
        <option value={vm?.cpuTopologySocket || ''}>
          {vm?.cpuTopologySocket || '옵션 없음'}
        </option>
      ) : (
        // 생성 모드일 때 기본 옵션 목록 표시
        <>
          <option value="">가상 소켓 선택</option>
          <option value="1">1</option>
        </>
      )}
    </select>
                      </div>

                      <div className='network_form_group'>
                        <label htmlFor="core_per_socket">가상 소켓 당 코어</label>
                        <select
                          id="core_per_socket"
                          value={cpuTopologyCore} // 현재 상태 값
                          onChange={(e) => setCpuTopologyCore(e.target.value)} // 상태 업데이트
                        >
                          {editMode ? (
                            // 편집 모드일 때 vm 값만 옵션으로 표시
                            <option value={vm?.cpuTopologyCore || ''}>
                              {vm?.cpuTopologyCore || '옵션 없음'}
                            </option>
                          ) : (
                            // 생성 모드일 때 기본 옵션 목록 표시
                            <>
                              <option value="">코어 수 선택</option>
                              <option value="1">1</option>
                            
                            </>
                          )}
                        </select>
                      </div>

                      <div className='network_form_group'>
                        <label htmlFor="thread_per_core">코어당 스레드</label>
                        <select
                          id="thread_per_core"
                          value={cpuTopologyThread} // 현재 상태 값
                          onChange={(e) => setCpuTopologyThread(e.target.value)} // 상태 업데이트
                        >
                          {editMode ? (
                            // 편집 모드일 때 vm 값만 옵션으로 표시
                            <option value={vm?.cpuTopologyThread || ''}>
                              {vm?.cpuTopologyThread || '옵션 없음'}
                            </option>
                          ) : (
                            // 생성 모드일 때 기본 옵션 목록 표시
                            <>
                              <option value="">스레드 수 선택</option>
                              <option value="1">1</option>
                              <option value="2">2</option>
                  
                            </>
                          )}
                        </select>
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
                      checked={cloudInit} // cloudInit 상태를 checked 속성에 바인딩
                      onChange={(e) => {
                        setCloudInit(e.target.checked); // 상태 업데이트
                        if (!e.target.checked) {
                          setDomainHiddenBoxVisible(false); // 체크 해제 시 숨김 처리
                        }
                      }}
                    />
                    <label htmlFor="enableBootMenu">Cloud-lnit</label>
                  </div>

                  {cloudInit && ( // Cloud-init이 체크된 경우에만 표시
                    <div>
                      <FontAwesomeIcon 
                        icon={faChevronCircleRight} 
                        id="domain_hidden_box_btn2" 
                        onClick={toggleDomainHiddenBox} 
                        fixedWidth 
                      />
                      <span>사용자 지정 스크립트</span>
                      <div 
                        className='mt-0.5' 
                        id="domain_hidden_box2" 
                        style={{ display: isDomainHiddenBoxVisible ? 'block' : 'none' }} // 상태에 따라 표시 여부 제어
                      >
                        <textarea 
                          name="content" 
                          cols="40" 
                          rows="8" 
                          placeholder="여기에 스크립트를 입력하세요"
                          value={script} // script 상태와 바인딩
                          onChange={(e) => setScript(e.target.value)} // 상태 업데이트
                        />
                      </div>
                    </div>
                  )}
                </div>


                </>
              }
              {selectedModalTab === 'host' && 
                <>
                  <div id="host_second_content">
                    <div style={{ fontWeight: 600 }}>실행 호스트:</div>
                    <div className="form_checks">
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
                      </div>
                      <div>
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
                      
                            {hostsFromCluster.map((host) => (
                              <option key={host.id} value={host.id}>
                                {host.name}
                              </option>
                            ))}
                          </select>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div id="host_third_content">
                    <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>
                    {/* 마이그레이션 모드 */}
                    <div>
                      <label htmlFor="migration_mode">마이그레이션 모드</label>
                      <select
                        id="migration_mode"
                        value={migrationMode}
                        onChange={(e) => setMigrationMode(e.target.value)}
                      >

                        {migrationModeOptions.map((option) => (
                          <option key={option.value} value={option.value}>
                            {option.label}
                          </option>
                        ))}
                      </select>
                    </div>
                    {/* 마이그레이션 정책 */}
                    <div>
                      <label htmlFor="migration_policy">마이그레이션 정책</label>
                      <select
                        id="migration_policy"
                        value={migrationPolicy}
                        onChange={(e) => setMigrationPolicy(e.target.value)}
                      >
                  
                        {migrationPolicyOptions.map((option) => (
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
              
                <div id="ha_mode_second_content">
                          <div className="checkbox_group">
                              <input className="check_input" type="checkbox" value="" id="ha_mode_box" />
                              <label className="check_label" htmlFor="ha_mode_box">
                                  고가용성
                              </label>
                          </div>
                          <div>
                              <div>
                                  <span>가상 머신 임대 대상 스토리지 도메인</span>
                                  <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                              </div>
                              <select id="no_lease" disabled>
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
                          <div className="ha_mode_article">
                              <span>실행/마이그레이션 큐에서 우선순위 : </span>
                              <div>
  <span>우선 순위</span>
  <select 
    id="priority" 
    value={priority} // 선택된 값과 동기화
    onChange={(e) => setPriority(parseInt(e.target.value, 10))} // 값 변경 핸들러
  >
    {priority.map((option) => (
      <option key={option.value} value={option.value}>
        {option.label} {/* 드롭다운에 표시될 텍스트 */}
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
                  <div className="cpu_res">
                              <span style={{ fontWeight: 600 }}>부트순서:</span>
                              <div className='cpu_res_box'>
                                  <span>첫 번째 장치</span>
                                  <select id="watchdog_action">
                                      <option value="없음">하드디스크</option>
                                  </select>
                              </div>
                              <div className='cpu_res_box'>
                                  <span>두 번째 장치</span>
                                  <select id="watchdog_action">
                                      <option value="없음">Default</option>
                                  </select>
                              </div>
                  </div>
                  <div id="boot_checkboxs">
                      <div>
                          <div className='checkbox_group'>
                              <input type="checkbox" id="connectCdDvd" name="connectCdDvd" />
                              <label htmlFor="connectCdDvd">CD/DVD 연결</label>
                          </div>
                          <div className='text_icon_box'>
                              <input type="text" disabled />
                              <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)' }}fixedWidth/> 
                          </div>
                      </div>

                      <div className='checkbox_group mb-1.5'>
                          <input type="checkbox" id="enableBootMenu" name="enableBootMenu" />
                          <label htmlFor="enableBootMenu">부팅 메뉴를 활성화</label>
                      </div>
                  </div>
                </div>
              </>
              }
          </div>
          </div>

      <div className="edit_footer">
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
        <button onClick={onRequestClose}>취소</button>
      </div>

      <VmConnectionPlusModal
        isOpen={isConnectionPopupOpen}
        onRequestClose={() => setIsConnectionPopupOpen(false)}
      />
      <VmCreatePlusModal
        isOpen={isCreatePopupOpen}
        onRequestClose={() => setIsCreatePopupOpen(false)}
      />
    </div>
  </Modal>
);
};

export default VmModal;


