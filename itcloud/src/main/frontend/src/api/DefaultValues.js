const DEFAULT_VALUES = {
  GET_DASHBOARD: {
      uptime: 0,
      datacenters: 0,
      datacentersUp: 0,
      datacentersDown: 0,
      clusters: 0,
      hosts: 0,
      hostsUp: 0,
      hostsDown: 0,
      vms: 0,
      vmsUp: 0,
      vmsDown: 0,
      storageDomains: 0,
  },
  GET_CPU_MEMERY: {
      totalCpuUsagePercent: 0,
      totalMemoryUsagePercent: 0,
      totalMemoryGB: 0,
      usedMemoryGB: 0,
      freeMemoryGB: 0
  },
  GET_STORAGE: {
      usedPercent: 0,
      totalGB: 0,
      usedGB: 0,
      freeGB: 0
  },
  GET_VM_CPU: () =>         `/api/v1/dashboard/vmCpu`,
  GET_VM_MEMORY: () =>      `/api/v1/dashboard/vmMemory`,
  GET_STORAGE_MEMROY: () => `/api/v1/dashboard/storageMemory`,
  FIND_ALL_DATA_CENTERS: [
      {
          iconStatus: <i className="fa fa-exclamation-triangle" style={{ color: 'yellowgreen' }}></i>, // TODO: raw 값만 부여하도록 구현
          name: '이름데이터 (샘플값입니다.)',  
          comment: '(샘플값입니다.)',
          storageType: '공유됨',
          status: 'Up',
          compatVersion: '4.7',
          description: 'The default Data Center',
      },
  ],
  FIND_ALL_CLUSTERS: [
    {
      status: '',
      name: 'Default',
      comment: '',
      version: '4.7',
      description: 'The default server cluster',
      cpuType: 'Secure Intel Cascadelake',
      hostCount: 2,
      vmCount: 7,
      upgradeStatus: '',
    },
  ],
  FIND_ALL_NETWORKS: [
    { 
      name: 'ovirtmgmt', 
      description: 'Management Network', 
      dataCenter: 'DC1', 
      provider: 'Provider1', 
      portSeparation: '아니요' 
    }, { 
      name: 'example1', 
      description: 'Example Description 1', 
      dataCenter: 'DC2', 
      provider: 'Provider2', 
      portSeparation: '아니요' 
    },
  ],
  FIND_ALL_STORAGE_DOMAINS: [
    {
      domainName: 'ㅁㅎㅇㅁㄹㄹ', // 여기에 도메인 이름을 설정합니다.
      comment: '',
      domainType: '',
      storageType: '',
      format: '',
      dataCenterStatus: '',
      totalSpace: '',
      freeSpace: '',
      reservedSpace: '',
      description: '',
    }
  ]
}

export default DEFAULT_VALUES