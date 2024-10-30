import React, { useEffect, useState, useMemo  } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TableOuter from '../../table/TableOuter';
import TableInfo from '../../table/TableInfo';
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

  // const {
  //   data: datacenters,
  //   status: datacentersStatus,
  //   isRefetching: isDatacentersRefetching,
  //   refetch: refetchDatacenters,
  //   isError: isDatacentersError,
  //   error: datacentersError,
  //   isLoading: isDatacentersLoading
  // } = useAllDataCenters = ((e) => ({
  //   ...e,
  //   storageType: e?.storageType ? '로컬' : '공유됨',
  // }));

  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters();

  const transformedDataCenters = useMemo(() => {
    if (!datacenters) return [];
    return datacenters.map((e) => ({
      ...e,
      storageType: e?.storageType ? '로컬' : '공유됨',
    }));
  }, [datacenters]);

  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedDataCenter?.id && toggleModal('edit', true)} disabled={!selectedDataCenter?.id}>편집</button>
        <button onClick={() => selectedDataCenter?.id && toggleModal('delete', true)} disabled={!selectedDataCenter?.id}>제거</button>
      </div>
      <span>id = {selectedDataCenter?.id || ''}</span>

      <TableOuter
        columns={TableInfo.DATACENTERS}
        data={transformedDataCenters}
        // data={datacenters}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDataCenter(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
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