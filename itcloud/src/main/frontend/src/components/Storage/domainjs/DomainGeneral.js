import React from 'react';
import { useDomainById } from '../../../api/RQHook';
import { formatBytesToGBToFixedZero, zeroValue } from '../../util/format';

const DomainGeneral = ({ domainId }) => {
  const {
    data: domain,
    refetch: domainRefetch,
    error: domainError,
    isLoading: isDomainLoading,
  } = useDomainById(domainId, (e) => ({
    ...e,
  }));

  function overCommit() {
    return ((domain?.commitedSize / domain?.diskSize) * 100).toFixed(0);
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
                <td>{zeroValue(formatBytesToGBToFixedZero(domain?.diskSize))}</td>
              </tr>
              <tr>
                <th>사용 가능:</th>
                <td>{zeroValue(formatBytesToGBToFixedZero(domain?.availableSize))}</td>
              </tr>
              <tr>
                <th>사용됨:</th>
                <td>{zeroValue(formatBytesToGBToFixedZero(domain?.usedSize))}</td>
              </tr>
              <tr>
                <th>할당됨:</th>
                <td>{zeroValue(formatBytesToGBToFixedZero(domain?.commitedSize))}</td>
              </tr>
              <tr>
                <th>오버 할당 비율:</th>
                <td>{overCommit()}%</td>
              </tr>
              <tr>
                <th>이미지: (약간의 문제)</th>
                <td>{domain?.diskImageVos?.length || 0}</td>
              </tr>
              {domain?.storageType === 'nfs' && (
              <tr>
                <th>경로:</th>
                <td>{domain?.storageAddress}</td>
              </tr>
              )}
              {/* <tr>
                <th>NFS 버전:</th>
                <td>{domain?.nfsVersion}</td>
              </tr> */}
              <tr>
                <th>디스크 공간 부족 경고 표시:</th>
                <td>{domain?.warning}% ({(formatBytesToGBToFixedZero(domain?.diskSize) / domain?.warning).toFixed(0)} GB)</td>
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
  
  export default DomainGeneral;