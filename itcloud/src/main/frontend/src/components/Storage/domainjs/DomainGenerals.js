import React, { useState } from 'react';
import { useDomainById } from '../../../api/RQHook';

const DomainGenerals = ({ domainId }) => {
  const {
    data: domain,
    status: domainStatus,
    isRefetching: isDomainRefetching,
    refetch: domainRefetch,
    isError: isDomainError,
    error: domainError,
    isLoading: isDomainLoading,
  } = useDomainById(domainId, (e) => ({
    ...e,
  }));

  function formatBytesToGB(bytes) {
    return (bytes / (1024 * 1024 * 1024)).toFixed(0);
  }

  function overCommit() {
    return ((domain?.commitedSize / domain?.diskSize) * 100).toFixed(0);
  }

  function zeroValue(size) {
    return size < 1 ? "< 1 GB" : `${size} GB`;
  }

  return (
    <>
      <div className="tables">
        <div>
          <table className="table">
            <tbody>
              <tr>
                <th>ID:</th>
                <td>{domain?.id}</td>
              </tr>
              <tr>
                <th>크기:</th>
                <td>{zeroValue(formatBytesToGB(domain?.diskSize))}</td>
              </tr>
              <tr>
                <th>사용 가능:</th>
                <td>{zeroValue(formatBytesToGB(domain?.availableSize))}</td>
              </tr>
              <tr>
                <th>사용됨:</th>
                <td>{zeroValue(formatBytesToGB(domain?.usedSize))}</td>
              </tr>
              <tr>
                <th>할당됨:</th>
                <td>{zeroValue(formatBytesToGB(domain?.commitedSize))}</td>
              </tr>
              <tr>
                <th>오버 할당 비율:</th>
                <td>{overCommit()}%</td>
              </tr>
              <tr>
                <th>이미지: (약간의 문제)</th>
                <td>{domain?.diskImageVos?.length || 0}</td>
              </tr>
              <tr>
                <th>경로:</th>
                <td>{domain?.storageAddress}</td>
              </tr>
              {/* <tr>
                <th>NFS 버전:</th>
                <td>{domain?.nfsVersion}</td>
              </tr> */}
              <tr>
                <th>디스크 공간 부족 경고 표시:</th>
                <td>{domain?.warning}% ({(formatBytesToGB(domain?.diskSize) / domain?.warning).toFixed(0)} GB)</td>
                {/* virtualSize: sizeToGB(disk?.virtualSize) < 1 ? "< 1 GB" : `${sizeToGB(disk?.virtualSize).toFixed(0)} GB`,
            actualSize: sizeToGB(disk?.actualSize) < 1 ? "< 1 GB" : `${sizeToGB(disk?.actualSize).toFixed(0)} GB`, */}
              </tr>
              <tr>
                <th>심각히 부족한 디스크 공간의 동작 차단:</th>
                <td>{domain?.spaceBlocker} GB</td>
              </tr>
            </tbody>
          </table>
        </div> 
      </div> 
    </>
    );
  };
  
  export default DomainGenerals;