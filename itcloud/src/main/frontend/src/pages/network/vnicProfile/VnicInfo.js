import React, { Suspense, useEffect, useState} from 'react';
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
import VnicTemplates from './VnicTemplates';
import DeleteModal from '../../../components/DeleteModal';
import VnicProfileDupl from './VnicProfileDupl';
import VnicProfileModal from './modal/VnicProfileModal';

const VnicInfo = () => {
    const { id: vnicProfileId, section } = useParams();
    const [modals, setModals] = useState({ edit: false, delete: false });

    const {
        data: vnic,
    } = useVnicProfile(vnicProfileId, (e) => ({...e,}));
    console.log("VNIC Data: ", vnic);

    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('vms');
    const sections = [  
        { id: 'vms', label: '가상머신' },
        { id: 'templates', label: '템플릿' },
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
        templates: VnicTemplates,
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

    const toggleModal = (type, isOpen) => {
        setModals((prev) => {
            if (prev[type] === isOpen) return prev;
            return { ...prev, [type]: isOpen };
        });
    };
    const sectionHeaderButtons = [
        { id: 'edit_btn', label: '편집', onClick: () => toggleModal('edit', true),},
        { id: 'delete_btn', label: '삭제', onClick: () => toggleModal('delete', true), },
    ]

    const pathData = [vnic?.name, sections.find((section) => section.id === activeTab)?.label];
    useEffect(() => {
        window.addEventListener('resize', adjustFontSize);
        adjustFontSize();
        return () => { window.removeEventListener('resize', adjustFontSize); };
    }, []);

    const renderModals = () => (
        <>
          {modals.edit && (
            <VnicProfileModal
              isOpen={modals.edit}
              onRequestClose={() => toggleModal('edit', false)}
              editMode={true}
            //   networkId={networkId}
            />
          )}
          {modals.delete && (
            <DeleteModal
              isOpen={modals.delete}
              type="Network"
              onRequestClose={() => toggleModal('delete', false)}
              contentLabel="네트워크"
            //   data={network}
            />
          )}
        </>
      );

    return (
        <div id="section">
            <div>
                <HeaderButton
                    titleIcon={faLaptop}
                    title={vnic?.name}
                    buttons={sectionHeaderButtons}
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
        <Suspense fallback={<div>Loading...</div>}>{renderModals()}</Suspense>
        <Footer/>
    </div>
    );
};

export default VnicInfo;
