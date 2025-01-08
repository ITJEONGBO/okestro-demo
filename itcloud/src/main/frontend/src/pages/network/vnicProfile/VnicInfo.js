import React, { useEffect, useState} from 'react';
import HeaderButton from '../../../components/button/HeaderButton';
import Footer from '../../../components/footer/Footer';
import { adjustFontSize } from '../../../UIEvent';
import { useAllNetworks, useAllVnicProfiles, useVnicProfile } from '../../../api/RQHook';
import {faLaptop, faServer} from '@fortawesome/free-solid-svg-icons'
import { useNavigate, useParams } from 'react-router-dom';
import NavButton from '../../../components/navigation/NavButton';
import { navigate } from '@storybook/addon-links';
import Path from '../../../components/Header/Path';
import VnicVm from './VnicVm';
import VnicTemplate from './VnicTemplate';

const VnicInfo = () => {
    const { id: vnicProfileId, section } = useParams();
    const {
        data: vnic,
    } = useVnicProfile(vnicProfileId, (e) => ({...e,}));
    console.log("VNIC Data: ", vnic);

    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('vms');
    const sections = [  
        { id: 'vms', label: '가상머신' },
        { id: 'template', label: '템플릿' },
    ];
    useEffect(() => {
    if (!section) {
        setActiveTab('vms');
    } else {
        setActiveTab(section);
    }
    }, [section]);
    const sectionComponents = {
        vms: VnicVm,
        template: VnicTemplate,
    };
    const renderSectionContent = () => {
        const SectionComponent = sectionComponents[activeTab];
        return SectionComponent ? <SectionComponent vnicProfileId={vnicProfileId} /> : null;
    };

    const handleTabClick = (tab) => {
        const path = tab === 'vms' ? `/vnicProfiles/${vnicProfileId}/vms` : `/vnicProfiles/${vnicProfileId}/${tab}`;
        navigate(path);
        setActiveTab(tab);
    };
    
    const pathData = [vnic?.name, sections.find((section) => section.id === activeTab)?.label];

    useEffect(() => {
        window.addEventListener('resize', adjustFontSize);
        adjustFontSize();
        return () => { window.removeEventListener('resize', adjustFontSize); };
    }, []);

    return (
        <div id="section">
            <div>
                <HeaderButton
                    titleIcon={faLaptop}
                    title={vnic?.name}
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
            </div>
        
        <Footer/>
    </div>
    );
};

export default VnicInfo;
