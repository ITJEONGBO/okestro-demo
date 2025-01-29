import React, { lazy, Suspense, useEffect, useState } from 'react';

const VmDiskModal = lazy(() => import('../VmDiskModal'));
const VmDiskConnectionModal = lazy(() => import('../VmDiskConnectionModal'));

const VmDisk = ({ editMode, vm, dataCenterId, diskState, setDiskState }) => {
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
  
  useEffect(() => {
    if (!editMode) {
      setDiskState([]); 
    }
  }, [editMode, setDiskState]);

  const handleRemoveDisk = (index) => {
    setDiskState((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center",}}>
        <div className="px-1 font-bold">인스턴스 이미지</div>
        <div style={{ display: "flex", gap: "8px" }}> {/* 버튼 간격 추가 */}
          <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
          <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
        </div>

        <Suspense fallback={<div>Loading...</div>}>
          {isConnectionPopupOpen && (
            <VmDiskConnectionModal
              isOpen={isConnectionPopupOpen}
              editMode={editMode}
              vm={vm}
              dataCenterId={dataCenterId}
              existingDisks={diskState.map((disk) => disk.id)} // ✅ 기존 연결된 디스크 전달
              onSelectDisk={(selectedDisks) => setDiskState(selectedDisks)}
              onClose={() => setIsConnectionPopupOpen(false)}
            />
          )}
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
      <div className="disk-list-container" style={{ marginTop: "12px", borderBottom: "1px solid gray", paddingBottom: "8px" }}>
          {diskState.length > 0 &&
            diskState.map((disk, index) => (
              <div key={index} className="disk-item">
                <div style={{ display: "flex", alignItems: "center" }}>
                  <span style={{ marginRight: "10px" }}><strong>이름:</strong> {disk.alias} ({disk.virtualSize} GB)</span>
                  <button onClick={() => handleRemoveDisk(index)}>삭제</button>
                </div>
              </div>
            ))
          }
        </div> 
    </>
  );
};

export default VmDisk;
