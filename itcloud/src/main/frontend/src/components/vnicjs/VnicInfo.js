import React, { useEffect} from 'react';
import HeaderButton from '../button/HeaderButton';
import Footer from '../footer/Footer';
import { adjustFontSize } from '../../UIEvent';
import { useAllNetworks, useAllVnicProfiles } from '../../api/RQHook';
import {faLaptop, faServer} from '@fortawesome/free-solid-svg-icons'
import { useParams } from 'react-router-dom';

const VnicInfo = () => {
    const { id: nicId, section } = useParams();
    const {
        data: vnic,
    } = useAllVnicProfiles(nicId);

    const sections = [  
        { id: 'vms', label: '가상머신' },
        { id: 'template', label: '템플릿' },
    ];
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
            </div>
        
        <Footer/>
    </div>
    );
};

export default VnicInfo;
