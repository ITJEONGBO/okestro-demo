import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBuilding, faTimes, faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import './css/RutilManager.css';
import Path from '../Header/Path';
import Table from '../table/Table';
import TableOuter from '../table/TableOuter';
import TableColumnsInfo from '../table/TableColumnsInfo';
import DataCenterModal from '../Modal/DataCenterModal';
import ClusterModal from '../Modal/ClusterModal';
import TemplateDu from '../duplication/TemplateDu';
import HostDu from '../duplication/HostDu';
import Footer from '../footer/Footer';
import { 
    useDashboard, 
    useDashboardCpuMemory, 
    useDashboardStorage,
    useAllDataCenters, 
    useDataCenter,
    useAllClusters, 
    useCluster,
    useAllHosts, 
    useAllVMs, 
    useAllTemplates, 
    useAllStorageDomains, 
    useAllDisks, 
    useAllNetworks, 
    useAllVnicProfiles
} from '../../api/RQHook';
import logo from '../../img/logo.png';

function RutilManager() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState(() => localStorage.getItem('activeTab') || 'general');
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [activeDiskType, setActiveDiskType] = useState('all');
    const [activeContentType, setActiveContentType] = useState('all');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalType, setModalType] = useState(null);
    const [selectedId, setSelectedId] = useState(null); // 모든 타입의 ID를 저장
    const [selectedRow, setSelectedRow] = useState(null); // 선택된 row 전체 저장
    const [editMode, setEditMode] = useState(false);
    const [modalData, setModalData] = useState(null);

    // API Hook: 데이터 가져오기
    const { data: dashboard } = useDashboard();
    const { data: cpuMemory } = useDashboardCpuMemory();
    const { data: storage } = useDashboardStorage();
    const { data: allDataCenters }= useAllDataCenters((dataCenter) => {
        return {
            ...dataCenter,
            // storageType: dataCenter.storageType == false ? "공유됨" : '로컬',
        };
    });
    const { data: selectedDataCenter } = useDataCenter(selectedId);

    const { data: allClusters } = useAllClusters((cluster) => {
        return {
            ...cluster,
        };
    });
    const { data: allHosts } = useAllHosts((host) => {
        return {
            ...host,
        };
    });
    const { data: allVMs } = useAllVMs((vm) => {
        return {
            ...vm,
            dataCenter: vm.dataCenterVo ? vm.dataCenterVo.name : '',
            dataCenterId: vm.dataCenterVo ? vm.dataCenterVo.id : null,
            host: vm.hostVo ? vm.hostVo.name : '',  // host 필드에 hostVo.name 매핑
            hostVoId: vm.hostVo ? vm.hostVo.id : null, // hostVo.id 값을 별도로 저장
            cluster: vm.clusterVo ? vm.clusterVo.name : '',
            clusterId: vm.clusterVo ? vm.clusterVo.id : null,
            cpu: vm.usageDto ? vm.usageDto.cpuPercent+'%' : 0+'%',
            memory: vm.usageDto ? vm.usageDto.memoryPercent+'%' : 0+'%',
            networkPer: vm.usageDto ? vm.usageDto.networkPercent+'%' : 0+'%',
        };
    });
    const { data: allTemplates } = useAllTemplates((template) => {
        return {
            ...template,
            id: template?.id ?? '',
            name: template?.name ?? 'Unknown', 
            status: template?.status ?? 'Unknown',                // 템플릿 상태
            version: template?.version ?? 'N/A',                  // 템플릿 버전 정보
            description: template?.description ?? 'No description',// 템플릿 설명
            cpuType: template?.cpuType ?? 'CPU 정보 없음',         // CPU 유형 정보
            hostCount: template?.hostCount ?? 0,                  // 템플릿에 연결된 호스트 수
            vmCount: template?.vmCount ?? 0,                      // 템플릿에 연결된 VM 수
        };
    });
    const { data: allStorageDomains } = useAllStorageDomains((storageDomain) => {
        return {
            ...storageDomain,
        };
    });
    const { data: allDisks } = useAllDisks((disk) => {
        return {
            ...disk,
        };
    });
    const { data: allNetworks } = useAllNetworks((network) => {
        return {
          ...network,
        };
    });
    const { data: allVnicProfiles } = useAllVnicProfiles((vnicProfile) => {
        return {
            ...vnicProfile,
        };
    });

    const handleTabClick = (tab) => {
        setActiveTab(tab);
        localStorage.setItem('activeTab', tab);
    };
    
    // const handleRowClick = (row, column) => {
    //     if (column.accessor === 'name') navigate(`/computing/datacenters/${row.id}`);
    // };
    const handleRowClick = (row) => {
        setSelectedId(row.id);  // 클릭된 Row의 ID 저장
        setSelectedRow(row);     // 클릭된 Row 전체 저장
    };

    const handleDiskTypeClick = (type) => setActiveDiskType(type);
    const handleContentTypeChange = (event) => setActiveContentType(event.target.value);
    const togglePopup = () => setIsPopupOpen(!isPopupOpen);


    // 모달 관리
    const openCreatePopup = (type) => {
        setEditMode(false);
        setModalData(null);
        setModalType(type);
        setIsModalOpen(true);
    };

    const openEditPopup = (type) => {
        if (selectedId) { // 선택된 ID가 있을 때만 편집 모달을 염
          setEditMode(true);
          setModalType(type);
          setIsModalOpen(true);
        } else {
          alert('편집할 데이터를 먼저 선택하세요.');
        }
    };

    const openDeletePopup = (type) => {
        if (selectedId) { // 선택된 ID가 있을 때만 편집 모달을 염
          setModalType(type);
        //   setIsModalOpen(true);
        } else {
          alert('삭제할 데이터를 먼저 선택하세요.');
        }
    };

    const closePopup = () => {
        setIsModalOpen(false);
        setModalType(null);
        setSelectedId(null);
        setModalData(null);
    };

    const handleSubmit = (requestData) => {
        console.log(editMode ? `편집:` : `생성:`, requestData);
        closePopup();
    };


    // HeaderButton 컴포넌트
    const buttons = [
    ];


    // Header와 Sidebar에 쓰일 섹션과 버튼 정보
    const sections = [
        { id: 'general', label: '일반' },
        { id: 'data_center', label: '데이터센터' },
        { id: 'cluster', label: '클러스터' },
        { id: 'host', label: '호스트' },
        { id: 'virtual_machine', label: '가상머신' },
        { id: 'template', label: '템플릿' },
        { id: 'storage_domain', label: '스토리지 도메인' },
        { id: 'disk', label: '디스크' },
        { id: 'network', label: '네트워크' },
        { id: 'vnic_profile', label: 'vNIC 프로파일' },
    ];

    const pathData = ['Rutil Manager', sections.find(section => section.id === activeTab)?.label];

    return (
        <div id="section">
            <HeaderButton
                titleIcon={faBuilding}
                title="Rutil Manager"
                additionalText="목록이름"
                buttons={buttons}
                popupItems={[]}
                uploadOptions={[]}
            />
            <div className="content_outer">
                <NavButton sections={sections} activeSection={activeTab} handleSectionClick={handleTabClick} />
                <div className="host_btn_outer">
                    {activeTab !== 'general' && <Path pathElements={pathData} />}

                    {activeTab === 'general' && (
                        <div className="rutil_general">
                            <div className="rutil_general_first_contents">
                                <div>
                                    <img className="logo_general" src={logo} alt="logo" />
                                    <span>버전: ###<br />빌드:###</span>
                                </div>
                                <div>
                                    <div>
                                        <span>데이터센터: {dashboard?.datacenters ?? 0}</span>
                                        <span>클러스터: {dashboard?.clusters ?? 0}</span>
                                        <span>호스트: {dashboard?.hosts ?? 0}</span>
                                        <span>가상머신: {dashboard?.vmsUp ?? 0} / {dashboard?.vms}</span>
                                        <span>스토리지 도메인: {dashboard?.storageDomains ?? 0}</span>
                                    </div>
                                    <div>부팅시간(업타임): <strong>2024-**-** 20:15:45</strong></div>
                                </div>
                            </div>
                            <div className="type_info_boxs">
                                <div className="type_info_box">CPU: {Math.floor(100 - (cpuMemory?.totalCpuUsagePercent ?? 0))}% 사용가능</div>
                                <div className="type_info_box">메모리: {Math.floor(100 - (cpuMemory?.totalMemoryUsagePercent ?? 0))}% 사용가능</div>
                                <div className="type_info_box">스토리지: {Math.floor(100 - (storage?.usedPercent ?? 0))}% 사용가능</div>
                            </div>
                        </div>
                    )}

                    {activeTab === 'data_center' && (
                        <>
                            <div className="header_right_btns">
                                <button onClick={() => openCreatePopup('datacenter')}>데이터 센터 생성</button>
                                <button onClick={() => openEditPopup('datacenter')}>편집</button>
                                <button onClick={() => openDeletePopup('datacenter')}>삭제</button>
                            </div>
                            <div>
                                <text>{selectedId ? `선택된 ID: ${selectedId}` : '선택된 데이터 센터가 없습니다'}</text>
                            </div>
                            <TableOuter 
                                columns={TableColumnsInfo.DATACENTERS} 
                                data={allDataCenters} 
                                onRowClick={handleRowClick} // Row 클릭 시 ID 및 Row 데이터 저장
                            />
                             <DataCenterModal
                                isOpen={isModalOpen}
                                onRequestClose={closePopup}
                                onSubmit={handleSubmit}
                                editMode={editMode}
                                data={selectedDataCenter} // API에서 가져온 데이터 센터 정보 전달
                            />
                        </>
                    )}

                    {activeTab === 'cluster' && (
                        <>
                            <div className="header_right_btns">
                                <button onClick={() => openCreatePopup('cluster')}>클러스터 생성</button>
                                <button onClick={() => openEditPopup('cluster')}>편집</button>
                                <button>삭제</button>
                                <ClusterModal
                                    isOpen={isModalOpen}
                                    onRequestClose={closePopup}
                                    onSubmit={handleSubmit}
                                    editMode={editMode}          // editMode 전달
                                    data={modalData}             // 편집할 데이터 전달
                                />
                            </div>
                            <TableOuter 
                                columns={TableColumnsInfo.CLUSTERS_DATA} 
                                data={allClusters} 
                                onRowClick={handleRowClick} 
                            />
                        </>
                    )}

                    {activeTab === 'host' && (
                        <HostDu 
                            data={allHosts} 
                            columns={TableColumnsInfo.HOSTS_ALL_DATA} 
                            handleRowClick={handleRowClick} 
                        />
                    )}

                    {activeTab === 'virtual_machine' && (
                        <>
                            <TableOuter 
                                columns={TableColumnsInfo.VMS} 
                                data={allVMs} 
                                onRowClick={handleRowClick} 
                            />
                        </>
                    )}

                    {activeTab === 'template' && (
                        <TemplateDu 
                            data={allTemplates} 
                            columns={TableColumnsInfo.TEMPLATE_CHART} 
                            handleRowClick={handleRowClick} 
                        />
                    )}

                    {activeTab === 'storage_domain' && (
                        <>
                            <div className="header_right_btns">
                                <button className="content_header_popup_btn" onClick={togglePopup}>
                                    <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
                                    {isPopupOpen && (
                                        <div className="content_header_popup">
                                            <div className="disabled">OVF 업데이트</div>
                                            <div className="disabled">파괴</div>
                                            <div className="disabled">디스크 검사</div>
                                            <div className="disabled">마스터 스토리지 도메인으로 선택</div>
                                        </div>
                                    )}
                                </button>
                            </div>
                            <TableOuter 
                                columns={TableColumnsInfo.STORAGE_DOMAINS} 
                                data={allStorageDomains} 
                                onRowClick={handleRowClick}
                            />
                        </>
                    )}

                    {activeTab === 'disk' && (
                        <>
                            <div className="disk_type">
                                <div>
                                    <span>디스크유형: </span>
                                    <div className="flex">
                                        <button className={activeDiskType === 'all' ? 'active' : ''} onClick={() => handleDiskTypeClick('all')}>모두</button>
                                        <button className={activeDiskType === 'image' ? 'active' : ''} onClick={() => handleDiskTypeClick('image')}>이미지</button>
                                    </div>
                                </div>
                                <div className="content_type">
                                    <label className="mr-1" htmlFor="contentType">컨텐츠 유형:</label>
                                    <select id="contentType" value={activeContentType} onChange={handleContentTypeChange}>
                                        <option value="all">모두</option>
                                        <option value="data">데이터</option>
                                        <option value="ovfStore">OVF 스토어</option>
                                        <option value="memoryDump">메모리 덤프</option>
                                        <option value="iso">ISO</option>
                                    </select>
                                </div>
                            </div>

                            {activeDiskType === 'all' && 
                                <TableOuter 
                                    columns={TableColumnsInfo.ALL_DISK} 
                                    data={allDisks} 
                                    onRowClick={handleRowClick} 
                                    showSearchBox={true} 
                                />
                            }
                            {activeDiskType === 'image' && 
                                <TableOuter 
                                    columns={TableColumnsInfo.IMG_DISK} 
                                    data={allDisks} 
                                    onRowClick={handleRowClick} 
                                    showSearchBox={true} 
                                />
                            }
                        </>
                    )}

                    {activeTab === 'network' && (
                        <>
                            <TableOuter 
                                columns={TableColumnsInfo.NETWORKS} 
                                data={allNetworks} 
                                onRowClick={handleRowClick} 
                            />
                        </>
                    )}

                    {activeTab === 'vnic_profile' && (
                        <>
                            <TableOuter 
                                columns={TableColumnsInfo.VNIC_PROFILES} 
                                data={allVnicProfiles} 
                                onRowClick={handleRowClick} 
                            />
                        </>
                    )}
                </div>
            </div>

            <Footer />
        </div>
    );
}

export default RutilManager;
