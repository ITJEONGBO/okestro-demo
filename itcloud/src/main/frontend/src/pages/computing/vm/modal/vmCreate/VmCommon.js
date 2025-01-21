import React, { useCallback, useEffect, useState } from 'react';
import VmNic from './VmNic';
import VmDiskModal from '../VmDiskModal';
import VmDiskConnectionModal from '../VmDiskConnectionModal';
import { useAllnicFromVM, useDisksFromVM } from '../../../../../api/RQHook';

const VmCommon = ({ editMode, vmId, dataCenterId, clusterVoId, formInfoState, setFormInfoState }) => {
  const { 
    data: disks = [],
    isLoading: isDisksLoading
  } = useDisksFromVM(vmId, (e) => ({ ...e }));

  const { 
    data: nics = [],
    isLoading: isNicsLoading
  } = useAllnicFromVM(clusterVoId, (e) => ({ ...e }));

  const handleNicsChange = useCallback(
    (updatedNics) => {
      setFormInfoState((prev) => ({ ...prev, nicVoList: updatedNics }));
    },
    [setFormInfoState]
  );  

  useEffect(() => {
    if (!editMode && disks?.length > 0) {
      setFormInfoState((prev) => ({...prev, diskVoList: disks}));
    }
  }, [disks, editMode]);  
  
  useEffect(() => {
    if (!editMode && nics?.length > 0) {
      setFormInfoState((prev) => ({...prev, nicVoList: nics}))
    }
  }, [nics, editMode]);

  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
  
  return (
    <>
    <div className="edit-second-content mb-1">
      <span>데이터센터ID: {dataCenterId}</span>
      <div>
        <label htmlFor="name">이름</label>
        <input
          type="text"
          id="name"
          value={formInfoState.name}
          onChange={(e) => setFormInfoState((prev) => ({ ...prev, name: e.target.value }))}
        />
      </div>
      <div>
        <label htmlFor="description">설명</label>
        <input
          type="text"
          id="description"
          value={formInfoState.description}
          onChange={(e) => setFormInfoState((prev) => ({ ...prev, description: e.target.value }))}
        />
      </div>
      <div>
        <label htmlFor="comment">코멘트</label>
        <input
          type="text"
          id="comment"
          value={formInfoState.comment}
          onChange={(e) => setFormInfoState((prev) => ({ ...prev, comment: e.target.value }))}
        />
      </div>
    </div>

    <div className="px-1 font-bold">인스턴스 이미지</div>
      <div
        className="edit-third-content"
        style={{ borderBottom: "1px solid gray", marginBottom: "0.2rem" }}
      >
        <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
        <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
      
        <VmDiskConnectionModal
          editMode={editMode}
          isOpen={isConnectionPopupOpen}
          // vmId={vmId}
          dataCenterId = {dataCenterId}
          onRequestClose={() => setIsConnectionPopupOpen(false)}
        />
        <VmDiskModal
          editMode={editMode}
          isOpen={isCreatePopupOpen}
          dataCenterId = {dataCenterId}
          onClose={() => setIsCreatePopupOpen(false)}
        />
      </div>

      <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
        <VmNic
          editMode={editMode}
          initialNics={formInfoState.nicVoList}
          availableProfiles={nics}
          onNicsChange={handleNicsChange}
        />
      </div>
    </>
  );
};

export default VmCommon;
