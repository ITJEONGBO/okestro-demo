import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DomainActionButtons from './button/DomainActionButtons';
import { formatBytesToGBToFixedZero, renderDomainStatus, renderDomainStatusIcon } from '../../../utils/format';
import TablesOuter from '../../../components/table/TablesOuter';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencil } from '@fortawesome/free-solid-svg-icons';

const DomainModal = React.lazy(() => import('./modal/DomainModal'));
const DomainActionModal = React.lazy(() => import('./modal/DomainActionModal'));
const DomainDeleteModal = React.lazy(() => import('./modal/DomainDeleteModal'));

const DomainDupl = ({ domains = [], columns = [], actionType = 'domain', type, datacenterId }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDomains, setSelectedDomains] = useState([]); 
  const selectedIds = (Array.isArray(selectedDomains) ? selectedDomains : []).map(sd => sd.id).join(', ');
  
  const handleNameClick = (id) => navigate(`/storages/domains/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      <DomainModal
        isOpen={activeModal === 'create'}
        onClose={closeModal}
      />
      <DomainModal
        editMode
        isOpen={activeModal === 'edit'}
        domainId={selectedDomains[0]?.id || null}
        onClose={closeModal}
      />
      <DomainModal
        isOpen={activeModal === 'import'}
        domainId={selectedDomains[0]?.id || null}
        onClose={closeModal}
      />
      <DomainDeleteModal
        isOpen={activeModal === 'delete'}
        data={selectedDomains}
        onClose={closeModal}
      />
      <DomainActionModal
        isOpen={['attach', 'detach', 'activate', 'maintenance'].includes(activeModal)}
        action={activeModal} // `type` 전달
        data={selectedDomains}
        onClose={closeModal}
      />
    </Suspense>
  );
  
  return (
    <>
      <DomainActionButtons
        openModal={openModal}
        isEditDisabled={selectedDomains.length !== 1}
        isDeleteDisabled={selectedDomains.length === 0} 
        status={selectedDomains[0]?.status}
        actionType={actionType} // 도메인인지, 데이터센터인지
        // type={type}
      />
      <span>ID: {selectedIds || ''}</span>

      <TablesOuter
        columns={columns}
        data={domains.map((domain) => ({
          ...domain,
          icon: renderDomainStatusIcon(domain.status),
          status: renderDomainStatus(domain?.status),
          hostedEngine: domain?.hostedEngine === true ? (
            <FontAwesomeIcon icon={faPencil} fixedWidth style={{ color: 'gold', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />) : '',
          domainType: 
            domain?.domainType === 'data' ? '데이터' 
            : domain?.domainType === 'iso' ? 'ISO'
            : 'EXPORT',
          storageType: 
            domain?.storageType === 'nfs' ? 'NFS'
            : domain?.storageType === 'iscsi' ? 'iSCSI'
            : 'Fibre Channel',
          diskSize: formatBytesToGBToFixedZero(domain?.diskSize) + ' GB',
          availableSize: formatBytesToGBToFixedZero(domain?.availableSize) + ' GB',
          usedSize: formatBytesToGBToFixedZero(domain?.usedSize) + ' GB',
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedDomains(selectedRows)}
        clickableColumnIndex={[2]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />
      
      {/* 도메인 모달창 */}
      { renderModals() }
    </>
  );
};

export default DomainDupl;