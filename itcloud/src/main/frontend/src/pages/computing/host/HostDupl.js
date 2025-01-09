import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TablesOuter from '../../../components/table/TablesOuter';
import HostActionButtons from './button/HostActionButtons'
import { renderHostStatusIcon } from '../../../utils/format';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPencil } from '@fortawesome/free-solid-svg-icons';
import TableRowClick from '../../../components/table/TableRowClick';

const HostModal = React.lazy(() => import('./modal/HostModal'));
const HostActionModal = React.lazy(() => import('./modal/HostActionModal'));
const HostDeleteModal = React.lazy(() => import('./modal/HostDeleteModal'));

const HostDupl = ({ hosts = [], columns, clusterId }) => {
  const navigate = useNavigate();

  const [activeModal, setActiveModal] = useState(null);
  const [selectedHosts, setSelectedHosts] = useState([]);
  const selectedIds = (Array.isArray(selectedHosts) ? selectedHosts : []).map(host => host.id).join(', ');

  const handleNameClick = (id) => navigate(`/computing/hosts/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <Suspense fallback={<div>Loading...</div>}>
      {activeModal === 'create' && (
        <HostModal
          clusterId={clusterId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'edit' && (
        <HostModal
          editMode
          hId={selectedHosts[0]?.id || null}
          clusterId={clusterId}
          onClose={closeModal}
        />
      )}
      {activeModal === 'delete' && (
        <HostDeleteModal
        data={selectedHosts}
        onClose={closeModal}
        />
      )}
      {['deactivate', 'activate', 'restart', 'reInstall', 'register', 'haOn', 'haOff'].includes(activeModal) && (
        <HostActionModal
          action={activeModal} // `type` 전달
          host={selectedHosts?.[0]}
          onRequestClose={closeModal}
        />
      )}
    </Suspense>
  );

  return (
    <>
      <HostActionButtons
        openModal={openModal}
        isEditDisabled={selectedHosts.length !== 1}
        isDeleteDisabled={selectedHosts.length === 0}
        status={selectedHosts[0]?.status}
        selectedHosts={selectedHosts}
      />
      <span>ID: {selectedIds || ''}</span>

      {/* <HostTable
        columns={columns}
        hosts={hosts}
        selectedHosts={selectedHosts} // 다중 선택 상태 전달
        setSelectedHosts={(selected) => Array.isArray(selected) && setSelectedHosts(selected)}
      /> */}

      <TablesOuter
        columns={columns}
        data={hosts.map((host) => ({
          ...host,
          icon: renderHostStatusIcon(host.status),
          hostedEngine:
            host?.hostedEngine && host?.hostedEngineVM ? (
              <FontAwesomeIcon 
                  icon={faPencil} 
                  fixedWidth 
                  style={{ color: 'gold', fontSize: '0.3rem', transform: 'rotate(90deg)' }} 
              />
            ) : host?.hostedEngine ? (
              <FontAwesomeIcon 
                  icon={faPencil} 
                  fixedWidth 
                  style={{ color: 'grey', fontSize: '0.3rem', transform: 'rotate(90deg)' }} 
              />
            ) : (
              ''
            ),
          status: host?.status,
          spmStatus: host?.spmStatus === 'NONE' ? '보통' : host?.spmStatus,
          vmCnt: host?.vmSizeVo.allCnt,
          memoryUsage: host?.usageDto.memoryPercent === null ? '' : host?.usageDto.memoryPercent + '%',
          cpuUsage: host?.usageDto.cpuPercent === null ? '' : host?.usageDto.cpuPercent + '%',
          networkUsage: host?.usageDto.networkPercent === null ? '' : host?.usageDto.networkPercent + '%',
          cluster: (
            <TableRowClick type="cluster" id={host.clusterVo.id}>
              {host?.clusterVo?.name}
            </TableRowClick>
          ),
          dataCenter: (
            <TableRowClick type="datacenter" id={host.dataCenterVo.id}>
              {host?.dataCenterVo?.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedHosts(selectedRows)}
        clickableColumnIndex={[2]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 호스트 모달창 */}
      { renderModals() }
    </>
  );
};

export default HostDupl;