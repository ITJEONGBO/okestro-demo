import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import { faBuilding, faTimes, faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import './css/RutilManager.css';
import Path from '../Header/Path';
import Footer from '../footer/Footer';
import DataCenters from './Rutil/DataCenters';
import Clusters from './Rutil/Clusters';
import Hosts from './Rutil/Hosts';
import Info from './Rutil/Info';

function RutilManager() {
    const {section } = useParams();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('info') // 기본 탭은 info로 

    // Header와 Sidebar에 쓰일 섹션과 버튼 정보
    const sections = [
        { id: 'info', label: '일반' },
        { id: 'datacenters', label: '데이터센터' },
        { id: 'clusters', label: '클러스터' },
        { id: 'hosts', label: '호스트' },
        { id: 'vms', label: '가상머신' },
        { id: 'templates', label: '템플릿' },
        { id: 'storageDomains', label: '스토리지 도메인' },
        { id: 'disks', label: '디스크' },
        { id: 'networks', label: '네트워크' },
        { id: 'vnicProfiles', label: 'vNIC 프로파일' },
    ];

    // section이 변경될때 tab도 같이 변경
    useEffect(() => {
        if (!section) {
          setActiveTab('info');
        } else {
          setActiveTab(section);
        }
    }, [section]); 

    const handleTabClick = (tab) => {
        const path = tab === 'info' ? '/computing/rutil-manager' : `/computing/rutil-manager/${tab}`;
        navigate(path);
        setActiveTab(tab);
    };

    const pathData = ['Rutil Manager', sections.find(section => section.id === activeTab)?.label];

    const sectionComponents = {
        info: Info,
        datacenters: DataCenters,
        clusters: Clusters,
        hosts: Hosts,
        // vms: Vms,
        // 추가적인 섹션들: vms, templates, 등등
    };

    const renderSectionContent = () => {
        const SectionComponent = sectionComponents[activeTab] || Info;
        return <SectionComponent />;
    };

    return (
        <div id="section">
            <HeaderButton
                title="Rutil Manager"
                titleIcon={faBuilding}
            />
            <div className="content_outer">
                <NavButton 
                    sections={sections} 
                    activeSection={activeTab} 
                    handleSectionClick={handleTabClick} 
                />
                <div className="host_btn_outer">
                    <Path pathElements={pathData} />
                    {renderSectionContent()}
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default RutilManager;



/*
return (
    <div id="section">
        <HeaderButton
            title="Rutil Manager"
            titleIcon={faBuilding}
        />
        <div className="content_outer">
            <NavButton 
                sections={sections} 
                activeSection={activeTab} 
                handleSectionClick={handleTabClick} 
            />
            <div className="host_btn_outer">
                <Path pathElements={pathData} />
                {renderSectionContent()}


                {/* {activeTab !== 'general' && <Path pathElements={pathData} />}

                {activeTab === 'general' && (
                    <div className="rutil_general">
                        <div className="rutil_general_first_contents">
                            <div>
                                <img className="logo_general" src={logo} alt="logo" />
                                <span>버전: ###<br />빌드:###</span>
                            </div>
                            <div>
                                <div>
                                    <span>데이터센터: {dashboard?.datacenters ?? 0}</span><br/>
                                    <span>클러스터: {dashboard?.clusters ?? 0}</span><br/>
                                    <span>호스트: {dashboard?.hosts ?? 0}</span><br/>
                                    <span>가상머신: {dashboard?.vmsUp ?? 0} / {dashboard?.vms}</span><br/>
                                    <span>스토리지 도메인: {dashboard?.storageDomains ?? 0}</span><br/>
                                </div>
                                <br/>
                                <div>부팅시간(업타임): <strong>{dashboard?.bootTime ?? ""}</strong></div>
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
                    <DataCenters  />


                    
                        {/* <div className="header_right_btns">
                            <button onClick={() => openCreatePopup('datacenter')}>데이터 센터 생성</button>
                            <button onClick={() => openEditPopup('datacenter')}>편집</button>
                            <button onClick={() => openDeletePopup('datacenter')}>삭제</button>
                        </div>
                        <div>
                            <span>{selectedId ? `선택된 ID: ${selectedId}` : '선택된 데이터 센터가 없습니다'}</span>
                        </div>
                        
                        <TablesOuter 
                            columns={TableInfo.DATACENTERS} 
                            data={allDataCenters} 
                            onRowClick={handleRowClick} // Row 클릭 시 ID 및 Row 데이터 저장
                            clickableColumnIndex={[0]}
                        />
                        <DataCenterModal
                            isOpen={isModalOpen}
                            onRequestClose={closePopup}
                            editMode={editMode}
                            data={selectedDataCenter} // API에서 가져온 데이터 센터 정보 전달
                        />
                        <DeleteModal
                            isOpen={isDeleteModalOpen}
                            onRequestClose={closePopup}
                            contentLabel={'데이터센터'}
                            data={selectedDataCenter}  // 삭제할 데이터 전달
                        /> 
                    
                )}

                {activeTab === 'cluster' && (
                    <>
                    <Cluster />
                        {/* <div className="header_right_btns">
                            <button onClick={() => openCreatePopup('cluster')}>클러스터 생성</button>
                            <button onClick={() => openEditPopup('cluster')}>편집</button>
                            <button onClick={() => openDeletePopup('cluster')}>삭제</button>
                            
                        </div>
                        <TablesOuter 
                            columns={TableInfo.CLUSTERS} 
                            data={allClusters} 
                            onRowClick={handleRowClick} 
                            />
                        <ClusterModal
                            isOpen={isModalOpen}
                            onRequestClose={closePopup}
                            // onSubmit={handleSubmit}
                            editMode={editMode}          // editMode 전달
                            data={modalData}             // 편집할 데이터 전달
                        />
                    </>
                )}

                {activeTab === 'host' && (
                    <TablesOuter 
                        columns={TableInfo.HOSTS} 
                        data={allHosts} 
                        onRowClick={handleRowClick}  
                    />
                )}

                {activeTab === 'virtual_machine' && (
                    <>
                        <TablesOuter 
                            columns={TableInfo.VMS} 
                            data={allVMs} 
                            onRowClick={handleRowClick} 
                        />
                    </>
                )}

                {activeTab === 'template' && (
                    <TablesOuter 
                        columns={TableInfo.TEMPLATES} 
                        data={allTemplates} 
                        onRowClick={handleRowClick} 
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
                        <TablesOuter 
                            columns={TableInfo.STORAGE_DOMAINS} 
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

                        <TablesOuter 
                            columns={TableInfo.DISKS} 
                            data={allDisks} 
                            onRowClick={handleRowClick} 
                            showSearchBox={true} 
                        />
                    </>
                )}

                {activeTab === 'network' && (
                    <>
                        <TablesOuter 
                            columns={TableInfo.NETWORKS} 
                            data={allNetworks} 
                            onRowClick={handleRowClick} 
                        />
                    </>
                )}

                {activeTab === 'vnic_profile' && (
                    <>
                        <TablesOuter 
                            columns={TableInfo.VNIC_PROFILES} 
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
*/