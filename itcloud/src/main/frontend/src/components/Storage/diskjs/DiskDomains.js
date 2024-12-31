import React, { useState } from 'react';
import { useAllStorageDomainFromDisk } from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import DomainTable from '../../table/DomainTable';

const DiskDomains = ({ diskId }) => {
  const {
    data: domains,
    status: domainsStatus,
    isLoading: isDomainsLoading,
    isError: isDomainsError,
  } = useAllStorageDomainFromDisk(diskId, (e) => ({ ...e }));

  const [selectedDomains, setSelectedDomains] = useState([]); // 다중 선택된 도메인을 관리

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 이벤트 전파 방지 */}
      <DomainTable
        columns={TableInfo.STORAGE_DOMAINS_FROM_DISK}
        domains={domains || []}
        setSelectedDomains={setSelectedDomains} // 올바른 함수 이름 전달
      />
      <span>
        선택된 도메인:{" "}
        {Array.isArray(selectedDomains) && selectedDomains.length > 0
          ? selectedDomains.map((domain) => domain.name).join(", ")
          : "선택된 항목이 없습니다."}
      </span>
    </div>
  );
};

export default DiskDomains;
