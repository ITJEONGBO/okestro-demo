import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import NavButton from '../navigation/NavButton';
import HeaderButton from '../button/HeaderButton';
import { faBuilding, faTimes, faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import './css/RutilManager.css';
import Path from '../Header/Path';
import Footer from '../footer/Footer';
import Info from './Rutil/Info';
import DataCenters from './Rutil/DataCenters';
import Clusters from './Rutil/Clusters';
import Hosts from './Rutil/Hosts';
import Vms from './Rutil/Vms';
import Templates from './Rutil/Templates';
import StorageDomains from './Rutil/StorageDomains'
import Disks from './Rutil/Disks';
import Networks from './Rutil/Networks';
import VnicProfiles from './Rutil/VnicProfiles';

function RutilManager() {
  const { section } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState('info') // 기본 탭은 info로 

  const rootPath = location.pathname.split('/').slice(0, 2).join('/'); // '/computing' 또는 '/networks' 등 추출

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
    const path = tab === 'info' ? `${rootPath}/rutil-manager` : `${rootPath}/rutil-manager/${tab}`;
    navigate(path);
    setActiveTab(tab);
  };

  const pathData = ['Rutil Manager', sections.find(section => section.id === activeTab)?.label];

  const sectionComponents = {
    info: Info,
    datacenters: DataCenters,
    clusters: Clusters,
    hosts: Hosts,
    vms: Vms,
    templates: Templates,
    storageDomains: StorageDomains,
    disks: Disks,
    networks: Networks,
    vnicProfiles: VnicProfiles
  };

  const renderSectionContent = () => {
    const SectionComponent = sectionComponents[activeTab] || Info;
    return <SectionComponent />;
  };

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faBuilding}
        title="Rutil Manager"
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