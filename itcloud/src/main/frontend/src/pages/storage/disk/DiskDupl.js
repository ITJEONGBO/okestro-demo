import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import DiskActionButtons from './button/DiskActionButtons';
import TablesOuter from '../../../components/table/TablesOuter';
import { formatBytesToGBToFixedZero, icon } from '../../../utils/format';
import TableRowClick from '../../../components/table/TableRowClick';
import DiskModals from './modal/DiskModals';

const DiskDupl = ({ disks = [], columns = [] }) => {
  const navigate = useNavigate();
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크
  const selectedIds = (Array.isArray(selectedDisks) ? selectedDisks : []).map((disk) => disk.id).join(', ');

  const handleNameClick = (id) => navigate(`/storages/disks/${id}`);
  
  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);  

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <DiskActionButtons
        openModal={openModal}
        isEditDisabled={selectedDisks?.length !== 1}
        isDeleteDisabled={selectedDisks?.length === 0}
        status={selectedDisks[0]?.status}
      />
      <span>ID: {selectedIds || ''}</span>

      {/* 타입값을 줘서 vmdisk와 disk구분해야할듯  */}
      <TablesOuter
        columns={columns}
        data={disks.map((d) => ({
          ...d,
          alias: d?.alias || d?.diskImageVo?.alias,
          icon: icon(d.status),
          storageDomain: <TableRowClick type="domains" id={d?.storageDomainVo?.id || d?.diskImageVo?.storageDomainVo?.id}>{d?.storageDomainVo?.name|| d?.diskImageVo?.storageDomainVo?.name}</TableRowClick>,
          sharable: d?.sharable ? 'O' : '',
          icon1: d?.bootable ? '🔑' : '',
          icon2: d?.readOnly ? '🔒' : '',
          sparse: d?.sparse ? '씬 프로비저닝' : '사전 할당',
          connectVm: (
            <TableRowClick type={d?.connectVm?.id ? "vms" : "templates"} id={d?.connectVm?.id || d?.connectTemplate?.id}>
              {d?.connectVm?.name || d?.connectTemplate?.name}
            </TableRowClick>
          ),
          virtualSize: formatBytesToGBToFixedZero(d?.virtualSize),
          actualSize: formatBytesToGBToFixedZero(d?.actualSize),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedDisks(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 디스크 모달창 */}
      <DiskModals
        activeModal={activeModal}
        disk={selectedDisks[0]}
        selectedDisks={selectedDisks}
        // datacenterId={datacenterId}
        onClose={closeModal}
      />
    </div>
  );
};

export default DiskDupl;
