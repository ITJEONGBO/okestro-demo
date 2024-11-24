import React from 'react';
import TablesOuter from './TablesOuter';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';

const DomainTable = ({
  columns,
  domains,
  setSelectedDomain,
}) => {
  const navigate = useNavigate();
  
  const handleNameClick = (id) => {
    navigate(`/storages/domains/${id}`);
  };

  const renderStatusIcon = (status) => {
    if (status === 'ACTIVE') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
    } else if (status === 'DOWN') {
      return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
    }
    return status;
  };

  return (
    <>
      <TablesOuter
        columns={columns}
        data={domains.map((domain) => ({
          ...domain,
          icon: renderStatusIcon(domain.status),
          domainTypeMaster: domain?.domainTypeMaster == true ? "마스터" : "" ,
          hostedEngine: domain?.hostedEngine ? 'O' : 'X',
          diskSize: domain?.diskSize/(Math.pow(1024, 3))+" GB",
          availableSize: domain?.availableSize/(Math.pow(1024, 3))+" GB",
          usedSize: domain?.usedSize/(Math.pow(1024, 3))+" GB",
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDomain(row)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default DomainTable;
