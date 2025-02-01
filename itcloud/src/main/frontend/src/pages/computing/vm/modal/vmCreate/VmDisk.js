import React, { lazy, Suspense, useEffect, useState } from 'react';

const VmDiskModal = lazy(() => import('../VmDiskModal'));
const VmDiskConnectionModal = lazy(() => import('../VmDiskConnectionModal'));

const VmDisk = ({ editMode, vm, dataCenterId, diskListState, setDiskListState, disks }) => {
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false);
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false);
  const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
  
  useEffect(() => {
    if (!editMode) {
      setDiskListState([]); 
    }
  }, [editMode, setDiskListState]);

  console.log('disk', diskListState)

  // 디스크 편집 핸들러
  const handleEditDisk = (diskId) => {
    setDiskListState((prevDisks) => prevDisks.filter((disk) => disk.id !== diskId));
  };
    
  // 디스크 삭제 핸들러
  const handleRemoveDisk = (index) => {
    setDiskListState((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center",}}>
        <div className="px-1 font-bold">인스턴스 이미지</div>
        <div style={{ display: "flex", gap: "8px" }}>
          <button onClick={() => setIsConnectionPopupOpen(true)}>연결</button>
          <button onClick={() => setIsCreatePopupOpen(true)}>생성</button>
        </div>

        <Suspense fallback={<div>Loading...</div>}>
          {isConnectionPopupOpen && (
            <VmDiskConnectionModal
              isOpen={isConnectionPopupOpen}
              vm={vm}
              dataCenterId={dataCenterId}
              type="vm"
              existingDisks={diskListState.map((disk) => disk.id)} // 기존 연결된 디스크 전달
              onSelectDisk={(selectedDisks) => {
                // 기존 디스크 목록에 새로운 디스크 추가
                setDiskListState((prevDisks) => {
                  const uniqueDisks = selectedDisks.filter(
                    (newDisk) => !prevDisks.some((existingDisk) => existingDisk.id === newDisk.id)
                  );
                  return [...prevDisks, ...uniqueDisks];
                });
              }}
              onClose={() => setIsConnectionPopupOpen(false)}
            />
          )}
          {isCreatePopupOpen && (
            <VmDiskModal
              editMode={editMode}
              isOpen={isCreatePopupOpen}
              dataCenterId={dataCenterId}
              type="vm"
              onCreateDisk={(newDisk) => {
                // 새로 생성된 디스크를 기존 목록에 추가
                setDiskListState((prevDisks) => [...prevDisks, newDisk]);
                setIsCreatePopupOpen(false);
              }}
              onClose={() => setIsCreatePopupOpen(false)}
            />
          )}
          {isEditPopupOpen && (
            <VmDiskModal
              editMode={true}
              isOpen={isEditPopupOpen}
              dataCenterId={dataCenterId}
              type="vm"
              onCreateDisk={(newDisk) => {
                // 새로 생성된 디스크를 기존 목록에 추가
                setDiskListState((prevDisks) => [...prevDisks, newDisk]);
                setIsEditPopupOpen(false);
              }}
              onClose={() => setIsEditPopupOpen(false)}
            />
          )}
        </Suspense>
      </div>

      <div className="disk-list-container" style={{ marginTop: "12px", borderBottom: "1px solid gray", paddingBottom: "8px" }}>
        {diskListState.length > 0 &&
          diskListState.map((disk, index) => (
            <div key={index} className="disk-item">
              <div style={{ display: "flex", alignItems: "center" }}>
                <span style={{ marginRight: "10px" }}>
                  <strong>이름:</strong> {disk?.alias} ({disk?.virtualSize} GB)
                </span>

                <span style={{ marginRight: "10px" }}>
                  <strong>인터페이스:</strong> {disk?.interface_}
                </span>

                <span style={{ marginRight: "10px" }}>
                  <strong>읽기전용:</strong> {disk?.readOnly ? "✔" : "✖"}
                </span>

                <span style={{ marginRight: "10px" }}>
                  <strong>부팅가능:</strong> {disk?.bootable ? "✔" : "✖"}
                </span>

                <button onClick={() => setIsEditPopupOpen(true)}>편집</button>
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
