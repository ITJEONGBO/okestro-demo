import React, { lazy, Suspense, useState } from 'react';

const VmDiskModal = lazy(() => import('../VmDiskModal'));
const VmDiskConnectionModal = lazy(() => import('../VmDiskConnectionModal'));

const VmDisk = ({ editMode, dataCenterId, diskState, setDiskState, disks, setFormInfoState }) => {
  const [isConnectionPopupOpen, setIsConnectionPopupOpen] = useState(false); // 디스크 연결
  const [isCreatePopupOpen, setIsCreatePopupOpen] = useState(false); // 디스크 생성

  const handleRemoveDisk = (index) => {
    const updatedDisks = diskState.filter((_, i) => i !== index);
    setDiskState(updatedDisks);
  };


  return (
    <>
    <div className="px-1 font-bold">인스턴스 이미지</div>
      <div
        className="edit-third-content"
        style={{ borderBottom: '1px solid gray', marginBottom: '0.2rem' }}
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

      {/* 연결된 디스크 목록 */}
      <div className="disk-list-container" style={{ marginTop: '20px' }}>
      <h3>연결된 디스크</h3>
      {diskState.length > 0 ? (
        diskState.map((disk, index) => (
          <div
            key={index}
            className="disk-item"
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '10px',
              marginBottom: '10px',
              border: '1px solid #ddd',
              borderRadius: '4px',
              backgroundColor: '#f9f9f9',
            }}
          >
            <div style={{ display: 'flex', flexDirection: 'column' }}>
              <span>
                <strong>이름:</strong> {disk.name || '이름 없음'}
              </span>
              <span>
                <strong>가상 크기:</strong>{' '}
                {disk.virtualSize ? `${disk.virtualSize} GB` : 'N/A'}
              </span>
              <span>
                <strong>스토리지 도메인:</strong>{' '}
                {disk.storageDomain || '미지정'}
              </span>
            </div>
            <button
              onClick={() => handleRemoveDisk(index)}
              style={{
                marginLeft: '20px',
                padding: '5px 10px',
                backgroundColor: '#f44336',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
              }}
            >
              삭제
            </button>
          </div>
        ))
      ) : (
        <div
          style={{
            textAlign: 'center',
            padding: '10px',
            backgroundColor: '#fafafa',
            border: '1px solid #ddd',
            borderRadius: '4px',
          }}
        >
          연결된 디스크가 없습니다.
        </div>
      )}
    </div>
    </>
  );
};

export default VmDisk;
