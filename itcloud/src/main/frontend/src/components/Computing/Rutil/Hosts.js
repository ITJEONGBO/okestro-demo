import React, { useEffect, useState, Suspense } from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import HostActionButtons from '../../button/HostActionButtons';
import HostTable from '../../table/HostTable';
import HostModals from '../../Modal/HostModals';

const Hosts = () => {
  const {
      data: hosts,
      status: hostsStatus,
      isRefetching: isHostsRefetching,
      refetch: refetchHosts,
      isError: isHostsError,
      error: hostsError,
      isLoading: isHostsLoading
  } = useAllHosts((e) => {
    return {
      ...e,
      status: e?.status,
      cluster: e?.clusterVo.name,
      dataCenter: e?.dataCenterVo.name,
      spmStatus: e?.spmStatus === 'SPM' ? 'SPM' : '',
      vmCnt: e?.vmSizeVo.allCnt,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedHost, setSelectedHost] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <HostActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedHost?.id && handleActionClick('edit')}
        onDelete={() => selectedHost?.id && handleActionClick('delete')}
        onDeactivate={() => selectedHost?.id && handleActionClick('deactivate')}
        onActivate={() => selectedHost?.id && handleActionClick('activate')}
        onRestart={() => selectedHost?.id && handleActionClick('restart')}
        onStop={() => selectedHost?.id && handleActionClick('stop')}
        onReInstall={() => selectedHost?.id && handleActionClick('reinstall')}
        onRegister={() => selectedHost?.id && handleActionClick('register')}
        onHaOn={() => selectedHost?.id && handleActionClick('haon')}
        onHaOff={() => selectedHost?.id && handleActionClick('haoff')}
        isEditDisabled={!selectedHost?.id}
        status={selectedHost?.status} // 추가: 현재 호스트 상태 전달
      />
      <span>id = {selectedHost?.id || ''}</span>

      <HostTable
        columns={TableInfo.HOSTS}
        hosts={hosts || []}
        selectedHost={selectedHost}
        setSelectedHost={setSelectedHost}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <HostModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedHost={selectedHost}
      />



      
      {/* TODO 중복 불편함 코드 수정 필요 */}
      {/* <TablesOuter
        columns={TableInfo.HOSTS}
        data={hosts?.map((host)=> ({
          ...host,
          icon: renderStatusIcon(host.status),
          cluster: (
            <TableRowClick type="cluster" id={host.clusterVo.id}>
              {host.cluster}
            </TableRowClick>
          ),          
          dataCenter: (
            <TableRowClick type="datacenter" id={host.dataCenterVo.id}>
              {host.dataCenter}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedHost(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      /> */}
      {/* <Suspense>
        {isModalOpen && action && (
          <>
            {action === 'create' || action === 'edit' ? (
              <HostModal
                isOpen={isModalOpen}
                onRequestClose={() => setIsModalOpen(false)}
                editMode={action === 'edit'}
                hId={selectedHost?.id || null}
              />
            ) : action === 'delete' ? (
              <DeleteModal
                isOpen={isModalOpen}
                type={'Host'}
                onRequestClose={() => setIsModalOpen(false)}
                contentLabel={'호스트'}
                data={selectedHost}
              />
            ) : (
              <HostActionModal
                isOpen={isModalOpen}
                action={action}
                onRequestClose={() => setIsModalOpen(false)}
                contentLabel={
                  action === 'deactivate' ? '유지보수'
                  : action === 'activate' ? '활성'
                  : action === 'restart' ? '재시작'
                  : action === 'stop' ? '중지'
                  : action === 'reinstall' ? '다시 설치'
                  : action === 'register' ? '인증서 등록'
                  : action === 'haon' ? 'HA 활성화'
                  : action === 'haoff'? 'HA 비활성화'
                  : ''
                }
                data={selectedHost}
              />
            )}
          </>
        )}
      </Suspense> */}
    </>
  );
};

export default Hosts;