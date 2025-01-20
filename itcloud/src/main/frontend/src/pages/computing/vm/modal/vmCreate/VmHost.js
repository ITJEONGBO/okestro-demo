import { useEffect, useState } from "react";
import { useHostFromCluster } from "../../../../../api/RQHook";

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

const VmHost = ({ editMode, clusterVoId, formHostState, setFormHostState }) => {  
  const { 
    data: hosts = [],
    isLoading: isHostsLoading
  } = useHostFromCluster(clusterVoId, (e) => ({...e}));

  const handleHostSelectionChange = (isSpecificHostSelected) => {
    setFormHostState((prev) => ({
      ...prev,
      isSpecificHostSelected,
      hostVoList: isSpecificHostSelected ? prev.hostVoList : [],
    }));
  };
  
  useEffect(() => {
    if (clusterVoId && !isHostsLoading && hosts.length > 0) {
      setFormHostState((prev) => ({
        ...prev,
        hostVoList: hosts.map((host) => ({ id: host.id, name: host.name })),
      }));
    }
  }, [clusterVoId, hosts, isHostsLoading]);
  

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

  // 마이그레이션 암호화 사용
  const migrationEncryptionOptionList = [
    { value: 'inherit', label: '클러스터 기본값(암호화하지 마십시오)' },
    { value: 'false', label: '암호화하지 마십시오' },
    { value: 'true', label: '암호화' },
  ];

  return (
  <>
    <div className="host-second-content">
      <div style={{ fontWeight: 600 }}>실행 호스트:</div>
      <div className="form-checks">
      <div>
          <input
            className="form-check-input"
            type="radio"
            name="hostSelection"
            id="clusterHost"
            checked={!formHostState.isSpecificHostSelected}
            onChange={() => handleHostSelectionChange(false)}
          />
          <label className="form-check-label" htmlFor="clusterHost">
            클러스터 내의 호스트
          </label>
        </div>

        {/* 특정 호스트 선택 */}
        <div>
          <input
            className="form-check-input"
            type="radio"
            name="hostSelection"
            id="specificHost"
            checked={formHostState.isSpecificHostSelected}
            onChange={() => handleHostSelectionChange(true)}
          />
          <label className="form-check-label" htmlFor="specificHost">
            특정 호스트
          </label>
        </div>

        {/* 호스트 선택 Select Box */}
        <div>
          <label>호스트 목록</label>
          <select
            multiple
            id="specific_host_select"
            value={(formHostState.hostVoList || []).map((host) => host.id)}
            onChange={(e) => {
              const selectedIds = Array.from(e.target.selectedOptions, (option) => option.value);
              setFormHostState((prev) => ({
                ...prev,
                hostVoList: hosts.filter((host) => selectedIds.includes(host.id)),
              }));
            }}
            disabled={!formHostState.isSpecificHostSelected || isHostsLoading}
            style={{ height: "100px" }}
          >
            {isHostsLoading ? (
              <option>호스트를 불러오는 중...</option>
            ) : (
              hosts.map((opt) => (
                <option key={opt.id} value={opt.id}>
                  {opt.name}
                </option>
              ))
            )}
          </select>
          <div style={{ marginTop: "10px" }}>
            <label>선택된 호스트:</label>
            <span style={{ marginLeft: "10px", fontWeight: "bold" }}>
              {formHostState.hostVoList > 0
                ? formHostState.hostVoList.map((host) => host.name).join(", ")
                : "선택된 호스트가 없습니다."}
            </span>
          </div>
        </div>
      </div>
    

      <div className="host-third-content">
        <div style={{ fontWeight: 600 }}>마이그레이션 옵션:</div>

        <CustomSelect
          label={'마이그레이션 모드'}
          value={formHostState.migrationMode}
          onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationMode: e.target.value }))}
          options={migrationModeOptionList}
        />

        {/* <CustomSelect
          label={'마이그레이션 정책'}
          value={formHostState.migrationPolicy}
          onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationPolicy: e.target.value }))}
          options={migrationPolicyOptionList}
        />       */}

        <CustomSelect
          label={'마이그레이션 암호화 사용'}
          value={formHostState.migrationEncryption}
          onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationPolicy: e.target.value }))}
          options={migrationEncryptionOptionList}
        />      

        {/* <CustomSelect
          label={'Parallel Migrations'}
          value={formHostState.migrationEncryption}
          onChange={(e) => setFormHostState((prev) => ({ ...prev, migrationPolicy: e.target.value }))}
          options={migrationEncryptionOptionList}
        />      

        <div className='network_checkbox_type1 disabled'>
          <label htmlFor="memory_size">Parallel Migrations</label>
          <input type="text" id="memory_size" value="" readOnly disabled/>
        </div> */}
      </div>
    </div>
  </>
  );
};
export default VmHost;
  