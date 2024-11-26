import React, { useState } from 'react';
import { faDesktop, faMinusCircle, faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAllDisksFromTemplate, useAllVMFromDomain } from '../../../api/RQHook';

const TmplateDisks = ({ domain }) => { //스토리지아이디로 
  const [isRowExpanded, setRowExpanded] = useState({});

  const toggleRow = (id) => {
    setRowExpanded((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const { 
    data: vms = [],  // 기본값을 빈 배열로 설정하여 undefined 방지
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError,
  } = useAllDisksFromTemplate(domain?.id, toTableItemPredicateVms);

  function toTableItemPredicateVms(vm) {
    const firstDisk = vm?.diskAttachmentVos?.[0]?.diskImageVo || {};  // 첫 번째 디스크 정보 가져오기

    return {
      id: vm?.id ?? '',
      name: vm?.name ?? '',
      status: vm?.status ?? 'UNKNOWN',
      creationTime: vm?.creationTime ?? '',
      memoryInstalled: vm?.memoryInstalled ?? 0,
      memoryUsed: vm?.memoryUsed ?? 0,
      clusterName: vm?.clusterVo?.name ?? 'Unknown',
      templateName: vm?.templateVo?.name ?? 'None',
      virtualSize: firstDisk.virtualSize ?? 0,  // 첫 번째 디스크의 가상 크기
      actualSize: firstDisk.actualSize ?? 0,    // 첫 번째 디스크의 실제 크기
      diskAttachments: vm?.diskAttachmentVos || [], // undefined 방지를 위해 빈 배열 설정
    };
  }

  if (isVmsLoading) return <div>Loading...</div>;
  if (isVmsError) return <div>Error loading VMs data.</div>;

  return (
    <div className="host_empty_outer">
      <div className="section_table_outer">
        <table>
          <thead>
            <tr>
              <th>별칭</th>
              <th>디스크</th>
              <th>템플릿</th>
              <th>가상 크기</th>
              <th>실제 크기</th>
              <th>생성 일자</th>
            </tr>
          </thead>
          <tbody>
            {/*하위는 무슨 api를 넣어야?? 생성일자 컬럼 */}
            {vms.map((vm) => (
              <React.Fragment key={vm.id}>
                <tr>
                  <td onClick={() => toggleRow(vm.id)} style={{ cursor: 'pointer' }}>
                    <FontAwesomeIcon icon={isRowExpanded[vm.id] ? faMinusCircle : faPlusCircle} fixedWidth />
                    <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: '0 5px 0 10px' }} />
                    {vm.name}
                  </td>
                  <td>{vm.diskAttachments?.length || 0}</td>
                  <td>{vm.templateName}</td>
                  <td>{vm.virtualSize} GIB</td>  {/* 첫 번째 디스크의 가상 크기 */}
                  <td>{vm.actualSize} GIB</td>  {/* 첫 번째 디스크의 실제 크기 */}
                  <td>{vm.creationTime || 'N/A'}</td>
                </tr>
                
                {/* 하위 행 디스크 정보 */}
                {isRowExpanded[vm.id] && vm.diskAttachments && vm.diskAttachments.map((disk, index) => (
                  <tr key={`${vm.id}-${index}`} className="detail_machine_second">
                    <td style={{ paddingLeft: '30px' }}>
                      <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: '0 5px 0 5px' }} />
                      {disk.diskImageVo?.alias || 'Unnamed Disk'}
                    </td>
                    <td>{disk.diskImageVo?.virtualSize || 0} GIB</td>
                    <td>{disk.diskImageVo?.actualSize || 0} GIB</td>
                    <td>{disk.diskImageVo?.virtualSize || 0} GIB</td>
                    <td>{disk.diskImageVo?.actualSize || 0} GIB</td>
                    <td>{disk.diskImageVo?.createDate || 'N/A'}</td>
                  </tr>
                ))}
              </React.Fragment>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TmplateDisks;
