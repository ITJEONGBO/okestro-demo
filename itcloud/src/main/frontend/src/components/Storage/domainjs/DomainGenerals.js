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
                <td>{domain?.diskSize && `${formatBytesToGB(domain?.diskSize)} GB`}</td>
              </tr>
              <tr>
                <th>사용 가능:</th>
                <td>{domain?.availableSize && `${formatBytesToGB(domain?.availableSize)} GB`}</td>
              </tr>
              <tr>
                <th>사용됨:</th>
                <td>{domain?.usedSize && `${formatBytesToGB(domain?.usedSize)} GB`}</td>
              </tr>
              <tr>
                <th>할당됨:</th>
                <td>{domain?.commitedSize && `${formatBytesToGB(domain?.commitedSize)} GB`}</td>
              </tr>
              <tr>
                <th>오버 할당 비율: (수정)</th>
                <td>{domain?.overCommit}%</td>
              </tr>
              <tr>
                <th>이미지: (약간의 문제)</th>
                <td>{domain?.diskImageVos?.length || 0}</td>
              </tr>
              <tr>
                <th>경로:</th>
                <td>{domain?.storageAddress}</td>
              </tr>
              <tr>
                <th>NFS 버전:</th>
                <td>{domain?.nfsVersion}</td>
              </tr>
              <tr>
                <th>디스크 공간 부족 경고 표시:</th>
                <td>{domain?.warning}%</td>
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