const DEFAULT_VALUES = {
  FIND_ALL_TREE_NAVIGATIONS: {
    "type": "DATACENTER",
    "id": "32cfa74b-9adc-40cc-a2a1-0ada699b714c",
    "name": "de",
  },
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
  // 컴퓨팅
  FIND_ALL_DATA_CENTERS: [
      {
          iconStatus: <i className="fa fa-exclamation-triangle" style={{ color: 'yellowgreen' }}></i>, // TODO: raw 값만 부여하도록 구현
          name: '샘플값입니다',
          comment: '(샘플값입니다.)',
          storageType: '공유됨',
          status: 'Up',
          compatVersion: '4.7',
          description: 'The default Data Center',
      },
  ],

  // 클러스터
  FIND_ALL_CLUSTERS: [
    {
      status: '',
      name: 'Cluster1',
      comment: '',
      version: '4.6',
      description: 'This is the first cluster',
      cpuType: 'Intel Xeon',
      hostCount: 5,
      vmCount: 10,
      upgradeStatus: 'Up to date',
    }
  ],
   //클러스터세부(일반)
  FIND_CLUSTERS_BY_ID:[
    {
      id: '40dc4bc6-9016-4a90-ae86-f3d36095a29f',
      name: 'ovirtmgmt', 
      description: 'Management Network', 
      dataCenter: 'DC1', 
      provider: 'Provider1', 
      portSeparation: '아니요',
      vnicProfileVos: '',
    }
  ],
  //클러스터 논리네트워크
  FIND_LOGICAL_NETWORK_FROM_CLUSTER:[

  ],
  // 클러스터 호스트 
  FIND_HOST_FROM_CLUSTER:[

  ],
  // 클러스터 가상머신


  // 클러스터 선호도 그룹

  // 클러스터 선호도 레이블 

  // 클러스터 선호도 그룹
  
  // 클러스터 권한 

  // 클러스터 이벤트


  // 네트워크목록 나열
  FIND_ALL_NETWORKS: [
    { 
      id: '40dc4bc6-9016-4a90-ae86-f3d36095a29f',
      name: 'ovirtmgmt', 
      description: 'Management Network', 
      dataCenter: 'DC1', 
      provider: 'Provider1', 
      portSeparation: '아니요' 
    }
  ],
  // 네트워크 세부(일반)
  FIND_NETWORK_BY_ID: { 
    id: '40dc4bc6-9016-4a90-ae86-f3d36095a29f',
    name: 'ovirtmgmt', 
    description: 'Management Network', 
    dataCenter: 'DC1', 
    provider: 'Provider1', 
    portSeparation: '아니요',
    vnicProfileVos: [
      {
        id: '531162db-c6c3-40b2-a9a7-c9d91d7200fc',
        name: 'VNIC 가짜',
        network: 'ovirtmgmt',
        dataCenter: 'Default',
        compatVersion: '4.7',
        qosName: '',
        networkFilter: 'wdsm-no-mac-spoofing',
        portMirroring: '',
        passthrough: '아니요',
        overProfile: '',
        description: ''
      }
    ],
  },
  // 네트워크 vnic                   
  FIND_ALL_VNIC_PROFILES_FROM_NETWORK: [
    {
      id: "531162db-c6c3-40b2-a9a7-c9d91d7200fc",
      name: "ovirtmgmt",
      description: "",
      passThrough: "DISABLED",
      migration: false,
      portMirroring: false,
      networkFilterVo: {
        id: "d2370ab4-fee3-11e9-a310-8c1645ce738e",
        name: "vdsm-no-mac-spoofing"
      },
      dataCenterVo: {
        "id": "32cfa74b-9adc-40cc-a2a1-0ada699b714c",
        "name": "de"
      },
      networkVo: {
        "id": "40dc4bc6-9016-4a90-ae86-f3d36095a29f",
        "name": "ovirtmgmt"
      }
    }
  ],
  // 네트워크 클러스터
  FIND_ALL_CLUSTERS_FROM_NETWORK:[
    {
      id: "temp-id",
      name: "Default",
      version: "4.7",
      connectedNetwork: <input type="checkbox" />,
      networkStatus: <i className="fa fa-chevron-left"></i>,
      requiredNetwork: <input type="checkbox" />,
      networkRole: "Admin",
      description: "The default server cluster",
    },
  ],
  // 네트워크 호스트
  FIND_ALL_HOST_FROM_NETWORK:[
    {
      icon: '',
      name: '',
      cluster: '',
      dataCenter: '',
      networkDeviceStatus: '',
      async: '',
      networkDevice: '',
      speed: '',
      rx: '',
      tx: '',
      totalRx: '',
      totalTx: '',
    },
  ],
  // 네트워크 가상머신
  FIND_ALL_VM_FROM_NETWORK:[
    {
      icon: <i className="fa fa-chevron-left"></i>,
      name: 'HostedEngine',
      cluster: 'Default',
      ipAddress: '192.168.0.08 fe80::2342',
      fqdn: 'ovirt.ititinfo.com',
      vnicStatus: <i className="fa fa-chevron-left"></i>,
      vnic: 'vnet0',
      vnicRx: '1',
      vnicTx: '1',
      totalRx: '5,353,174,284',
      totalTx: '5,353,174,284',
      description: 'Hosted engine VM'
    }
  ],
  // 네트워크 템플릿
  FIND_ALL_TEMPLATE_FROM_NETWORK:[
    {
      name: 'test02',
      nicId: '1',
      status: 'OK',
      clusterName: 'Default',
      nicName: 'nic1',
    }
  ],
  // 네트워크 권한
  FIND_ALL_PERMISSION_FROM_NETWORK:[
    {
      icon: <i className="fa fa-user"></i>,
      user: 'ovirtmgmt',
      provider: '',
      nameSpace: '*',
      role: 'SuperUser',
      createDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
    }
  ],


// ----------------------스토리지    
      // 스토리지 도메인
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
    ],



}

export default DEFAULT_VALUES