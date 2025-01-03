import React, { useState } from 'react';
import { useAllStorageDomainFromDisk } from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import { formatBytesToGBToFixedZero, renderDomainStatusIcon } from '../../util/format';
import TablesOuter from '../../table/TablesOuter';


const DiskDomains = ({ diskId }) => {
  const {
    data: domains,
  } = useAllStorageDomainFromDisk(diskId, (e) => ({ ...e }));

  const sizeCheck = (size) => {
    if (size === 0) {
      return 'N/A';
    } else {
      return formatBytesToGBToFixedZero(size) + ' GB';
    }
  };
  return (
    <div onClick={(e) => e.stopPropagation()}>
      <TablesOuter
        columns={TableInfo.STORAGE_DOMAINS_FROM_DISK}
        data={(domains || []).map((domain) => ({
          ...domain,
          icon: renderDomainStatusIcon(domain.status),
          domainType:
            domain?.domainType === 'data'
              ? '데이터'
              : domain?.domainType === 'iso'
              ? 'ISO'
              : 'EXPORT',
          diskSize: sizeCheck(domain?.diskSize),
          availableSize: sizeCheck(domain?.availableSize),
          usedSize: sizeCheck(domain?.usedSize),
        }))}
        shouldHighlight1stCol={true}  
      />

    </div>
  );
};

export default DiskDomains;
