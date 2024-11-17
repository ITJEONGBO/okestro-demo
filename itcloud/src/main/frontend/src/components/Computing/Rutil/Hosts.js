import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowUp, faArrowDown, faMinus, faPlus, faPlay } from '@fortawesome/free-solid-svg-icons';
import HostActionButtons from '../../button/HostActionButtons';

const HostModal = React.lazy(() => import('../../Modal/HostModal'))
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));
const HostActionModal = React.lazy(() => import('../../Modal/HostActionModal'))

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

  const navigate = useNavigate();
  const [modals, setModals] = useState({ 
    create: false, 
    edit: false, 
    delete: false, 
    deactivate: false , 
    activate: false, 
    restart: false, 
    stop: false, 
    reinstall: false, 
    register: false, 
    haon: false, 
    haoff: false
  });
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedHost, setSelectedHost] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  // const renderStatusIcon = (status) => {
  //   if (status === 'UP') {
  //     return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  //   } else if (status === 'DOWN') {
  //     return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  //   }
  //   return status;
  // };

  const handleNameClick = (id) => {
    navigate(`/computing/hosts/${id}`);
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

      <TablesOuter
        columns={TableInfo.HOSTS}
        data={hosts?.map((host)=> ({
          ...host,
          // status: renderStatusIcon(host.status),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedHost(row)}
        clickableColumnIndex={[1]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
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
                hId={selectedHost?.id || null}
              />
            )}
          </>
        )}
      </Suspense>
    </>
  );
};

export default Hosts;