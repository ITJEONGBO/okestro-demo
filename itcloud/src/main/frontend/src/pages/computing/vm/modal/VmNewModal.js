import React, { useState,useEffect } from 'react';
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

  const initialState = {
    formInfo: {
      id: '',
      name: '',
      comment: '',
      description: '',
      stateless: false,
      startPaused: false,
      deleteProtected: false,
      diskVoList: [],
      nicVoList: [],
    },
    formSystem: {
      memorySize: 1024,
      maxMemory: 1024,
      allocatedMemory: 1024,
      cpuTopologyCnt: 1,
      cpuTopologyCore: 1,
      cpuTopologySocket: 1,
      cpuTopologyThread: 1,
    },
    formCloudInit: {
      cloudInit: false,
      script: '',
    },
    formHost: {
      isSpecificHostSelected: false,
      hostVoList: [],
      migrationMode: 'migratable',
      migrationEncryption: 'inherit',
    },
    formHa: {
      ha: false,
      priority: 1,
      domainVoId: '',
    },
    formBoot: {
      firstDevice: 'hd',
      secDevice: '',
      bootingMenu: false,
      cdConn: '',
    },
    dataCenterId: '',
    clusterVoId: '',
    templateVoId: '',
    osSystem: 'other_os',
    chipsetOption: 'Q35_OVMF',
    optimizeOption: 'SERVER',
  };
  
  const [state, setState] = useState(initialState);
  const resetForm = () => setState(initialState);
  

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

  useEffect(() => {
    if (!isOpen) {
      resetForm(); // 모달이 닫힐 때 상태를 초기화
      setSelectedModalTab("common"); // 탭상태 초기화
    }
  }, [isOpen]);

  useEffect(() => {
    if (editMode && vm) {
      setState((prev) => ({
        ...prev,
        formInfo: {
          id: vm?.id || '',
          name: vm?.name || '',
          comment: vm?.comment || '',
          description: vm?.description || '',
          stateless: vm?.stateless || false,
          startPaused: vm?.startPaused || false,
          deleteProtected: vm?.deleteProtected || false,
          diskVoList: vm?.diskVoList || [],
          nicVoList: vm?.nicVoList || [],
        },
        formSystem: {
          memorySize: vm?.memorySize / (1024 * 1024),
          maxMemory: vm?.memoryMax / (1024 * 1024),
          allocatedMemory: vm?.memoryActual / (1024 * 1024),
          cpuTopologyCnt: vm?.cpuTopologyCnt || 1,
          cpuTopologyCore: vm?.cpuTopologyCore || 1,
          cpuTopologySocket: vm?.cpuTopologySocket || 1,
          cpuTopologyThread: vm?.cpuTopologyThread || 1,
        },
        formCloudInit: {
          cloudInit: vm?.cloudInit || false,
          script: vm?.setScript || '',
        },
        formHost: {
          isSpecificHostSelected: false,
          hostVoList: vm?.hostVoList || [],
          migrationMode: vm?.migrationMode || 'migratable',
          migrationEncryption: vm?.migrationEncrypt || 'inherit',
        },
        formHa: {
          ha: vm?.ha || false,
          priority: vm?.priority || 1,
          domainVoId: vm?.domainVoId || '',
        },
        formBoot: {
          firstDevice: vm?.firstDevice || 'hd',
          secDevice: vm?.secDevice || '',
          bootingMenu: vm?.bootingMenu || false,
          cdConn: '',
        },        
        clusterVoId: vm?.clusterVo?.id || '',
        templateVoId: vm?.templateVo?.id || '',
        osSystem: vm?.osSystem || 'other_os',
        chipsetOption: vm?.chipsetFirmwareType || 'Q35_OVMF',
        optimizeOption: vm?.optimizeOption || 'SERVER',
      }));
    }
  }, [editMode, vm]);
  
   
  useEffect(() => {
    if (!editMode && clusters.length > 0) {
      setState((prev) => ({
        ...prev,
        clusterVoId: clusters[0].id,
        dataCenterId: clusters[0].dataCenterId,
      }));
    }
  }, [clusters, editMode]);
  
  useEffect(() => {
    if (!editMode && templates.length > 0) {
      setState((prev) => ({
        ...prev,
        templateVoId: templates[0].id,
      }));
    }
  }, [templates, editMode]);


  const validateForm = () => {
    const { formInfo, formSystem, clusterVoId } = state;
    if (!formInfo.name) return '이름을 입력해주세요.';
    if (!clusterVoId) return '클러스터를 선택해주세요.';
    if (formSystem.memorySize > 9223372036854775807) return '메모리 크기가 너무 큽니다.';
    return null;
  };

  const handleFormSubmit = () => { // 디스크  연결은 id값 보내기 생성은 객체로 보내기
    const error = validateForm();
    if (error) {
      toast.error(error);
      return;
    }

    const sizeToBytes = (data) => parseInt(data, 10) * 1024 * 1024; // MB -> Bytes 변환

    const dataToSubmit = {
      clusterVo: clusters.find((c) => c.id === state.clusterVoId) || {},
      templateVo: templates.find((t) => t.id === state.templateVoId) || {},
      ...state.formInfo,
      ...state.formSystem,
      ...state.formCloudInit,
      ...state.formHost,
      ...state.formHa,
      ...state.formBoot,
    };

    console.log('가상머신 데이터 확인:', dataToSubmit); 

    if (editMode) {
      editVM(
        { vmId: state.formInfo.id, vmdata: dataToSubmit },
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
                  value={state.clusterVoId}
                  onChange={(e) => setState((prev) => ({ ...prev, clusterVoId: e.target.value }))}
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
              </div>

              <div>
                <label htmlFor="template" style={{ color: 'gray' }}>템플릿</label>
                <select
                  id="template"
                  value={state.templateVoId}
                  onChange={(e) => setState((prev) => ({ ...prev, templateVoId: e.target.value }))}
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
                value={state.osSystem}
                onChange={(e) => (e.target.value)}
                options={osSystemList}
              /> 
              <CustomSelect
                label="칩셋/펌웨어 유형"
                value={state.chipsetOption}
                onChange={(e) => setState((prev) => ({ ...prev, chipsetOption: e.target.value }))}
                options={chipsetOptionList}
              />
              <CustomSelect
                label="최적화 옵션"
                value={state.optimizeOption}
                onChange={(e) => setState((prev) => ({ ...prev, optimizeOption: e.target.value }))}
                options={optimizeOptionList}
              />
            </div>
            
            {/* 일반 */}
            {selectedModalTab === 'common' && (
              <VmCommon
                editMode={editMode}
                dataCenterId={state.dataCenterId}
                formInfoState={state.formInfo}
                setFormInfoState={(newState) => setState((prev) => ({ ...prev, formInfo: newState }))}
              />
            )}
            {selectedModalTab === 'system' && (
              <VmSystem
                editMode={editMode}
                formSystemState={state.formSystem}
                setFormSystemState={(newState) => setState((prev) => ({ ...prev, formSystem: newState }))}
              />
            )}
            {selectedModalTab === 'beginning' && (
              <VmInit
                editMode={editMode}
                formCloudInitState={state.formCloudInit}
                setFormCloudInitState={(newState) => setState((prev) => ({ ...prev, formCloudInit: newState }))}
              />
            )}
            {selectedModalTab === 'host' && (
              <VmHost
                editMode={editMode}
                clusterVoId={state.clusterVoId}
                formHostState={state.formHost}
                setFormHostState={(newState) => setState((prev) => ({ ...prev, formHost: newState }))}
              />
            )}
            {selectedModalTab === 'ha_mode' && (
              <VmHa
                editMode={editMode}
                dataCenterId={state.dataCenterId}
                formHaState={state.formHa}
                setFormHaState={(newState) => setState((prev) => ({ ...prev, formHa: newState }))}
              />
            )}
            {selectedModalTab === 'boot_outer' && (
              <VmBoot
                editMode={editMode}
                dataCenterId={state.dataCenterId}
                formBootState={state.formBoot}
                setFormBootState={(newState) => setState((prev) => ({ ...prev, formBoot: newState }))}
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


