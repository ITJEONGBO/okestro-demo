import React, { lazy, Suspense, useEffect, useState } from 'react';

const VmDiskModal = lazy(() => import('../VmDiskModal'));
const VmDiskConnectionModal = lazy(() => import('../VmDiskConnectionModal'));

const DivInput = ({ label, id, value, onChange }) => (
  <div>
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      id={id}
      value={value}
      onChange={onChange}
    />
  </div>
);

const VmCommon = ({ editMode, vmId, dataCenterId, nics, disks, formInfoState, setFormInfoState, nicState, setNicState }) => {
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);

  const handleNicChange = (index, value) => {
    const updatedNics = [...nicState];
    updatedNics[index] = {
      ...updatedNics[index],
      vnicProfileVo: { id: value },
    };
    setNicState(updatedNics);

    setFormInfoState((prev) => ({
      ...prev,
      nicVoList: updatedNics.filter((nic) => nic.vnicProfileVo.id),
    }));
  };

  const handleAddNic = (index) => {
    const selectedNic = nicState[index];
    if (!selectedNic.vnicProfileVo.id) return; // Don't add if no value selected

    const newNicNumber = nicState.length + 1;
    setNicState([
      ...nicState,
      { id: '', name: `nic${newNicNumber}`, vnicProfileVo: { id: '' } },
    ]);
  };

  const handleRemoveNic = (index) => {
    const updatedNics = nicState.filter((_, i) => i !== index);
    setNicState(updatedNics);

    setFormInfoState((prev) => ({
      ...prev,
      nicVoList: updatedNics.filter((nic) => nic.vnicProfileVo.id),
    }));
  };

  // NIC 목록 업데이트 (디스크 초기화와 비슷한 로직)
  // useEffect(() => {
  //   if (!editMode && disks?.length > 0) {
  //     setFormInfoState((prev) => ({ ...prev, diskVoList: disks }));
  //   }
  // }, [disks, editMode]);

  
  return (
    <>
    <div className="edit-second-content mb-1">
      <DivInput
        label="이름"
        id='name'
        value={formInfoState.name}
        onChange={(e) => setFormInfoState((prev) => ({ ...prev, name: e.target.value }))}
      />
      <DivInput
        label="설명"
        id='description'
        value={formInfoState.description}
        onChange={(e) => setFormInfoState((prev) => ({ ...prev, description: e.target.value }))}
      />
      <DivInput
        label="코멘트"
        id='comment'
        value={formInfoState.comment}
        onChange={(e) => setFormInfoState((prev) => ({ ...prev, comment: e.target.value }))}
      />
      {editMode &&
        <div>
          <label htmlFor="vmId">가상머신 ID</label>
          <input
            type="text"
            id="vmId"
            value={vmId}
            disabled
          />
        </div>
      }
    </div>

    <div className="px-1 font-bold">인스턴스 이미지</div>
      <div
        className="edit-third-content"
        style={{ borderBottom: "1px solid gray", marginBottom: "0.2rem" }}
      >
        <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
        <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
      
        <Suspense fallback={<div>Loading...</div>}>
          {/* 디스크 연결버튼 */}
          {isConnectionPopupOpen && (
            <VmDiskConnectionModal
            editMode={editMode}
            isOpen={isConnectionPopupOpen}
            dataCenterId={dataCenterId}
            onRequestClose={() => setIsConnectionPopupOpen(false)}
            />
          )}
          {/* 디스크 생성버튼 */}
          {isCreatePopupOpen && (
            <VmDiskModal
              editMode={editMode}
              isOpen={isCreatePopupOpen}
              dataCenterId={dataCenterId}
              onClose={() => setIsCreatePopupOpen(false)}
            />
          )}
        </Suspense>
      </div>

      {/* nic 설정란 */}
      <div className="edit_fourth_content" style={{ borderTop: 'none' }}>
        <p>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 설정하세요.</p>
        {nicState.map((nic, index) => (
          <div
            key={index}
            className="edit_fourth_content_row"
            style={{
              display: 'flex',
              alignItems: 'center',
              marginBottom: '10px',
            }}
          >
            <label style={{ marginRight: '10px', width: '100px' }}>
              {nic.name}
            </label>
            <select
              style={{ flex: 1 }}
              value={nic.vnicProfileVo.id}
              onChange={(e) => handleNicChange(index, e.target.value)}
            >
              <option value="">항목을 선택하십시오...</option>
              {nics.map((profile) => (
                <option key={profile.id} value={profile.id}>
                  {profile.name} / {profile.networkVo?.name || ''}
                </option>
              ))}
            </select>
            <button
              onClick={() => handleAddNic(index)}
              disabled={!nic.vnicProfileVo.id} // Disable if no value selected
              style={{ marginLeft: '10px' }}
            >
              +
            </button>
            {nicState.length > 1 && (
              <button
                onClick={() => handleRemoveNic(index)}
                style={{ marginLeft: '5px' }}
              >
                -
              </button>
            )}
          </div>
        ))}
      </div>
    </>
  );
};

export default VmCommon;
