import React, { useState } from 'react'; 
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPencil } from '@fortawesome/free-solid-svg-icons';
import { useAllDataCenterFromDomain } from "../../../api/RQHook";
import TablesOuter from '../../table/TablesOuter';
import TableRowClick from '../../table/TableRowClick';
import TableInfo from '../../table/TableInfo';
import DomainActionButtons from '../../button/DomainActionButtons';

const DomainDatacenters = ({ domainId }) => {
  const {
    data: datacenters, 
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllDataCenterFromDomain(domainId, (e) => ({
    ...e,
  }));  

  const renderStatusIcon = (status) => {
    if (status === 'ACTIVE') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
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
        onAttach={() => handleActionClick('attach')}
        onSeparate={() => selectedDataCenter?.id && handleActionClick('separate')}
        onActive={() => selectedDataCenter?.id && handleActionClick('active')}
        onMaintenance={() => selectedDataCenter?.id && handleActionClick('maintenance')}
        status={selectedDataCenter?.status}
      />

      {/* <DomainModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDomain={selectedDomain}
      /> */}

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
    </>
  );
};
  
export default DomainDatacenters;