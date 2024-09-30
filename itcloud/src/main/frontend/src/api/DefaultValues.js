import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { version } from 'react';

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
      events: 0,
      eventsAlert: 0,
      eventsError: 0,
      eventsWarning: 0
  },
  GET_CPU_MEMORY: {
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
  GET_STORAGE_MEMORY: () => `/api/v1/dashboard/storageMemory`,
  // 컴퓨팅
   FIND_ALL_DATA_CENTERS: [
    {
        name: 'DataCenter1',
        comment: '',
        storageType: '공유됨',
        status: 'Up',
        hostCnt:'',
        clusterCnt:'',
        compatVersion: '4.7',
        version: '4.7',
        description: 'The default Data Center',
    },
   ],
   // 데이터센터 세부(일반) 편집
   FIND_DATACENTER:[
    {
      id: '40dc4bc6-9016-4a90-ae86-f3d36095a29f',
      name: 'Default', 
      description: '', 
      storageType: "shared",
      version: "4.7",
      quotaMode: 'Disabled',
      comment: '',
    }
  ],
  FIND_CLUSTERS_FROM_DATA_CENTER:[
    {
      id: '',
      name: '', 
    }
  ],
  FIND_STORAGE_DOMAINS_FROM_DATA_CENTER:[
    {
      id: '',
      name: '', 
    }
  ],
  FIND_NETWORKS_FROM_DATA_CENTER:[
    {
      id: '',
      name: '', 
    }
  ],
  

//region:Cluster------ 클러스터
  FIND_ALL_CLUSTERS: [
    {
      name: 'Cluster1',
      description: 'This is the first cluster',
      cpuType: 'Intel Xeon',
      hostCount: 5,
      vmCount: 10,
      comment: '',
      version: '4.6',
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
    {
      name:'ovirtmgmt',
      status: '가동 중',
      role: <FontAwesomeIcon icon="fa-solid fa-crown" fixedWidth/>,
      description: 'Management Network',
    },
  ],
  // 클러스터 호스트 
  FIND_HOST_FROM_CLUSTER:[
    {
      icon: '',  
      name: 'host01.ititinfo.com',
      hostNameIP: 'host01.ititinfo.com',
      status: 'Up',
      loading: '1 대의 가상머신',
      displayAddress: '아니요',
    }
  ],
  // 클러스터 가상머신
  FIND_VM_FROM_CLUSTER:[
    {
      name: 'vm01',
      status: '실행 중',
      upTime: '12 days',
      cpu: '2 vCPU',
      memory: '4 GiB',
      network: 'virtio',
      ipv4: '192.168.0.101',
    }
  ],

  // 클러스터 선호도 그룹

  // 클러스터 선호도 레이블 

  // 이벤트
  FIND_EVENT_FROM_CLUSTER:[
    {
      icon: <FontAwesomeIcon icon="fa-solid fa-check-circle" style={{ color: 'green' }}fixedWidth/>,  // 상태 아이콘
      time: '2024. 8. 12. PM 12:24:11',
      description: 'Check for update of host host02.ititinfo.com. Delete yum_updates file from host.',
      correlationId: '',
      source: 'oVirt',
      userEventId: '',
    },
  ],

//region: Network------네트워크-----------
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
      networkStatus: <FontAwesomeIcon icon="fa-solid fa-chevron-left" fixedWidth/>,
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
  FIND_ALL_VMS_FROM_NETWORK:[
    {
      icon: <FontAwesomeIcon icon="fa-solid fa-chevron-left" fixedWidth/>,
      name: 'HostedEngine',
      cluster: 'Default',
      ipAddress: '192.168.0.08 fe80::2342',
      fqdn: 'ovirt.ititinfo.com',
      vnicStatus: <FontAwesomeIcon icon="fa-solid fa-chevron-left" fixedWidth/>,
      vnic: 'vnet0',
      vnicRx: '1',
      vnicTx: '1',
      totalRx: '5,353,174,284',
      totalTx: '5,353,174,284',
      description: 'Hosted engine VM'
    }
  ],
  // 네트워크 템플릿
  FIND_ALL_TEMPLATES_FROM_NETWORK:[
    {
      name: 'test02',
      nicId: '1',
      status: 'OK',
      clusterName: 'Default',
      nicName: 'nic1',
    }
  ],
  // 권한
  FIND_ALL_PERMISSION:[
    {
      icon: <FontAwesomeIcon icon="fa-solid fa-user" fixedWidth/>,
      user: 'ovirtmgmt',
      provider: '',
      nameSpace: '*',
      role: 'SuperUser',
      createDate: '2023.12.29 AM 11:40:58',
      inheritedFrom: '(시스템)',
    }
  ],
//region: Host------호스트-----------
FIND_ALL_HOSTS:[
  {
    status: 'Up',
    iconWarning: <i className="fa fa-exclamation-triangle" style={{ color: 'red' }}></i>,
    iconSPM: <i className="fa fa-crown" style={{ color: 'gold' }}></i>,
    name: 'host01.ititnfo.com',
    comment: '192.168.0.80',
    address: 'host01.ititinfo.com',
    cluster: 'Default',
    dataCenter: 'Default',
    vmCount: 1,
    memoryUsage: <div style={{ width: '50px', background: 'orange', color: 'white', textAlign: 'center' }}>80%</div>,
    cpuUsage: <div style={{ width: '50px', background: '#6699ff', color: 'white', textAlign: 'center' }}>6%</div>,
    networkUsage: <div style={{ width: '50px', background: '#99ccff', color: 'white', textAlign: 'center' }}>0%</div>,
    spmStatus: 'SPM',
  },
],
FIND_HOST:[
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
FIND_VM_FROM_HOST:[
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
FIND_DEVICE_FROM_HOST:[
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
//region: Vm/Template------가상머신,템플릿-----------
FIND_ALL_VMS:[
  {
    icon: '',                              
    name: '192.168.0.80',                     
    comment: '',                            
    host: 'Host1',                             
    ipv4: '192.168.0.80',                       
    fqdn: 'vm01.ititinfo.com',                 
    cluster: 'Cluster1',                       
    datacenter: 'DataCenter1',                 
    status: 'Up',                               
    upTime: '24h',                            
    description: 'The default server cluster',  
  },
],
FIND_ALL_TEMPLATES:[
  {
    status: '',
    name: '템플릿1',
    version: '4.7',
    description: 'The default server cluster',
    cpuType: 'Secure Intel Cascadelak',
    hostCount: 2,
    vmCount: 7,
  },
],
//region: Storage ----------------------스토리지    
      // 스토리지 도메인
    FIND_ALL_DISK:[

    ],
    FIND_ALL_STORAGE_DOMAINS: [
      {
        domainName: '스토리지도메인명', // 여기에 도메인 이름을 설정합니다.
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


    FIND_EVENT:[
      {
        icon: <FontAwesomeIcon icon="fa-solid fa-check-circle" style={{ color: 'green' }}fixedWidth/>,  // 상태 아이콘
        time: '2024. 8. 12. PM 12:24:11',
        description: 'Check for update of host host02.ititinfo.com. Delete yum_updates file from host.',
        correlationId: '',
        source: 'oVirt',
        userEventId: '',
      },
    ],
}


export default DEFAULT_VALUES