import React, { useState } from 'react'; 
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faWrench } from '@fortawesome/free-solid-svg-icons';
import { useAllDataCenterFromDomain, useDomainById } from "../../../api/RQHook";
import TablesOuter from '../../table/TablesOuter';
import TableRowClick from '../../table/TableRowClick';
import TableInfo from '../../table/TableInfo';
import DomainActionButtons from '../../button/DomainActionButtons';
import DomainModals from '../../Modal/DomainModals';

const DomainDatacenters = ({ domainId }) => {
  const {
    data: datacenters, 
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenterFromDomain(domainId, (e) => ({...e,}));  

  const {
    data: domain,
    refetch: domainRefetch,
    error: domainError,
    isLoading: isDomainLoading,
  } = useDomainById(domainId, (e) => ({...e,}));

  const renderStatusIcon = (status) => {
    if (status === 'ACTIVE') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    } else if (status === 'MAINTENANCE') {
      return <FontAwesomeIcon icon={faWrench} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
    }
    return status;
  };

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);

  if (!datacenters || datacenters.length === 0) {
    return <p>No data centers found.</p>;
  }

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <DomainActionButtons
        onActivate={() => selectedDataCenter?.id && handleActionClick('activate')}
        onAttach={() => selectedDataCenter?.id && handleActionClick('attach')}
        onDetach={() => selectedDataCenter?.id && handleActionClick('detach')}
        onMaintenance={() => selectedDataCenter?.id && handleActionClick('maintenance')}
        status={selectedDataCenter?.domainStatus}
      />

      <span>id = {selectedDataCenter?.id || ''}</span>  

      <TablesOuter
        columns={TableInfo.DATACENTERS_FROM_STORAGE_DOMAIN}
        data={datacenters.map((datacenter) => ({
          ...datacenter,
          icon: renderStatusIcon(datacenter.domainStatus),
          name: (
            <TableRowClick type="datacenter" id={datacenter.id}>
              {datacenter.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDataCenter(row)}
      />

      <DomainModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDomain={domain}
        datacenterId={selectedDataCenter?.id}
      />
    </>
  );
};
  
export default DomainDatacenters;