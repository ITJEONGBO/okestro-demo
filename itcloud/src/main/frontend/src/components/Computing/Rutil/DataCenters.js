import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import TablesOuter from '../../table/TablesOuter';
import DataCenterModal from '../../Modal/DataCenterModal';
import DeleteModal from '../../Modal/DeleteModal';
import { useAllDataCenters } from '../../../api/RQHook';

const DataCenters = () => {
  const navigate = useNavigate();

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
    navigate(`/computing/datacenters/${id}`);
  };

  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({
    ...e,
    storageType: e?.storageType ? '로컬' : '공유됨',
  }));

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedDataCenter?.id && toggleModal('edit', true)}>편집</button>
        <button onClick={() => selectedDataCenter?.id && toggleModal('delete', true)}>제거</button>
      </div>
      <span>id = {selectedDataCenter?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.DATACENTERS}
        data={datacenters}
        shouldHighlight1stCol={true}
        onRowClick={setSelectedDataCenter}
      />
      <DataCenterModal
        isOpen={modals.create || modals.edit}
        onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
        editMode={modals.edit}
        dcId={selectedDataCenter?.id || null}
      />
      <DeleteModal
        isOpen={modals.delete}
        onRequestClose={() => toggleModal('delete', false)}
        contentLabel={'데이터센터'}
        data={selectedDataCenter}
      />
    </>
  );
};

export default DataCenters;