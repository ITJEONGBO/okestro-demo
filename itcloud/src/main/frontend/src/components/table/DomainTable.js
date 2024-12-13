import React from 'react';
import TablesOuter from './TablesOuter';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPencil, faWrench } from '@fortawesome/free-solid-svg-icons';

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
    } else if (status === 'MAINTENANCE') {
      return <FontAwesomeIcon icon={faWrench} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
    }
    return status;
  };

  const renderStatus = (status) => {
    if (status === 'ACTIVE') {
      return '활성화';
    } else if (status === 'DOWN') {
      return '중지?';
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
          status: renderStatus(domain?.status),
          hostedEngine: domain?.hostedEngine === true ? 
            <FontAwesomeIcon 
                icon={faPencil} 
                fixedWidth 
                style={{ color: 'gold', fontSize: '0.3rem', transform: 'rotate(90deg)' }} 
            /> : "" ,
          domainType: 
            domain?.domainType === 'data' ? '데이터' 
            : domain?.domainType === 'iso' ? 'ISO'
            : 'EXPORT',
          storageType: 
            domain?.storageType === 'nfs' ? 'NFS'
            : domain?.storageType === 'iscsi' ? 'iSCSI'
            : 'Fibre Channel',
          // hostedEngine: domain?.hostedEngine ? 'O' : 'X',
          diskSize: domain?.diskSize/(Math.pow(1024, 3))+" GB",
          availableSize: domain?.availableSize/(Math.pow(1024, 3))+" GB",
          usedSize: domain?.usedSize/(Math.pow(1024, 3))+" GB",
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDomain(row)}
        clickableColumnIndex={[2]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />
    </>
  );
};

export default DomainTable;
