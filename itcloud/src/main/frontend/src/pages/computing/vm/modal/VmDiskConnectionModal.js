import React, { useState } from "react";
import Modal from "react-modal";
import toast from "react-hot-toast";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import TablesOuter from "../../../../components/table/TablesOuter";
import TableColumnsInfo from "../../../../components/table/TableColumnsInfo";
import { useFindDiskListFromDataCenter } from "../../../../api/RQHook";

const Tab = ({ tabs, activeTab, onTabClick }) => (
  <div className="disk_new_nav">
    {tabs.map(({ id, label }) => (
      <div
        key={id}
        id={id}
        onClick={() => onTabClick(id)}
        className={activeTab === id ? "active" : ""}
      >
        {label}
      </div>
    ))}
  </div>
);

const VmDiskConnectionModal = ({ isOpen, vmId, dataCenterId, onSelectDisk = () => {}, onRequestClose, }) => {
  const [activeTab, setActiveTab] = useState("img");
  const [selectedDiskId, setSelectedDiskId] = useState(null);

  const {
    data: disks = [], isLoading
  } = useFindDiskListFromDataCenter(dataCenterId, (e) => ({...e,}));
  
  console.log("Disks data:", disks);
  
  const tabs = [
    { id: "img", label: "이미지"},
    { id: "directlun", label: "직접 LUN"}, // 직접 LUN 데이터 추가 필요
  ];

  const interfaceList = [
    { value: "VIRTIO_SCSI", label: "VirtIO-SCSI" },
    { value: "VIRTIO", label: "VirtIO" },
    { value: "SATA", label: "SATA" },
  ];

  // useEffect(() => {
  //     if (!editMode && interfaceList.length > 0) {
  //       setInterface_(interfaceList[0].value);
  //     }
  //   }, [interfaceList, editMode]);

  const handleOkClick = () => {
    if (selectedDiskId) {
      const selectedDiskDetails = disks.find((disk) => disk.id === selectedDiskId);
      onSelectDisk(selectedDiskId, selectedDiskDetails);
      onRequestClose();
    } else {
      toast.error("디스크를 선택하세요!");
    }
  };

  // 제외된 디스크 ID를 필터링
  // const disks = rawDisks?.filter((disk) => !excludedDiskIds.includes(disk.id)) || [];

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
    >
      <div className="storage_disk_new_popup">
        <div className="popup-header">
          <h1>가상 디스크 연결</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        
        <Tab 
          tabs={tabs}
          activeTab={activeTab} 
          onTabClick={setActiveTab} 
        />
        {isLoading ? (
            <div>로딩중</div>
          ): (
            <>
            <TablesOuter
              columns={activeTab === "img" ? 
                TableColumnsInfo.VIRTUAL_DISK : TableColumnsInfo.VMS_STOP
              }
              data={disks.map((e) => ({
                ...e,
                virtualSize: (e?.virtualSize / Math.pow(1024, 3)) + " GB",
                actualSize: (e?.actualSize / Math.pow(1024, 3)) + " GB",
                storageDomain: e?.storageDomainVo?.name,
                status: e?.status === "UNINITIALIZED" ? "초기화되지 않음" : "UP",
                radio: (
                  <input
                    type="radio"
                    name="diskSelection"
                    value={e.id}
                    checked={selectedDiskId === e.id} // 선택된 항목의 ID와 비교
                    onChange={() => setSelectedDiskId(e.id)} // 수동 변경도 지원
                  />
                ),
                // interface: (
                //   <select
                //     value={e?.interface}
                //     onChange={(e) => setDataCenterVoId(e.target.value)}
                //     disabled={editMode}
                //   >
                //     {isDatacentersLoading ? (
                //       <option>로딩중~</option>
                //     ) : (
                //       datacenters && datacenters.map((dc) => (
                //         <option key={dc.id} value={dc.id}>
                //           {dc.name}: {dc.id}
                //         </option>
                //       ))
                //     )}
                //   </select>
                // )
              }))}
              onRowClick={(selectRow) => {
                const clickedDiskId = selectRow[0]?.id; // 클릭한 행의 ID 가져오기
                setSelectedDiskId(clickedDiskId); // 상태 업데이트
              }}
            />
            </>
          )
        }
        
       
        <span>선택된 디스크 ID: {selectedDiskId || "없음"}</span>
        <div className="edit-footer">
          <button onClick={handleOkClick}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmDiskConnectionModal;

