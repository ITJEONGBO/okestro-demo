import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import toast from "react-hot-toast";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import TableColumnsInfo from "../../../../components/table/TableColumnsInfo";
import { useFindDiskListFromDataCenter } from "../../../../api/RQHook";
// import TableOuter from "../../../../components/table/TableOuter";
import TablesOuter from "../../../../components/table/TablesOuter";

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

const VmDiskConnectionModal = ({ isOpen, editMode, vm, onSelectDisk = () => {}, onClose, }) => {
  const [activeTab, setActiveTab] = useState("img");
  const [selectedDiskIds, setSelectedDiskIds] = useState([]); // 중복 선택을 위한 배열
  const [selectedInterfaces, setSelectedInterfaces] = useState({}); 

  const {
    data: disks = [], isLoading
  } = useFindDiskListFromDataCenter(vm?.dataCenterVo?.id, (e) => ({...e,}));
  
  const tabs = [
    { id: "img", label: "이미지"},
    { id: "directlun", label: "직접 LUN"}, // 직접 LUN 데이터 추가 필요
  ];

  const interfaceList = [
    { value: "VIRTIO_SCSI", label: "VirtIO-SCSI" }, // 기본
    { value: "VIRTIO", label: "VirtIO" },
    { value: "SATA", label: "SATA" },
  ];

  const handleInterfaceChange = (diskId, newInterface) => {
    setSelectedInterfaces((prev) => ({
      ...prev,
      [diskId]: newInterface,
    }));
  };

  const handleCheckboxChange = (diskId) => {
    setSelectedDiskIds((prev) =>
      prev.includes(diskId)
        ? prev.filter((id) => id !== diskId) // 선택 해제
        : [...prev, diskId] // 선택 추가
    );
  };

  const handleOkClick = () => {
    if (selectedDiskIds.length > 0) {
      const selectedDisks = selectedDiskIds.map((diskId) => {
        const selectedDiskDetails = disks.find((disk) => disk.id === diskId);
        return {
          ...selectedDiskDetails,
          interface_: selectedInterfaces[diskId] || "VIRTIO_SCSI",
        };
      });
      onSelectDisk(selectedDiskIds, selectedDisks);
      onClose();
    } else {
      toast.error("디스크를 선택하세요!");
    }
  };

  useEffect(() => {
    if (isOpen && disks.length > 0) {
      const initialInterfaces = {};
      disks.forEach((disk) => {
        initialInterfaces[disk.id] = "VIRTIO_SCSI"; // 기본 인터페이스
      });
      setSelectedInterfaces(initialInterfaces);
    }
  }, [isOpen, disks]);

  // 제외된 디스크 ID를 필터링
  // const disks = rawDisks?.filter((disk) => !excludedDiskIds.includes(disk.id)) || [];

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
    >
      <div className="storage_disk_new_popup">
        <div className="popup-header">
          <h1>가상 디스크 연결</h1>
          <button onClick={onClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        <Tab tabs={tabs} activeTab={activeTab} onTabClick={setActiveTab}  />
        {isLoading ? (
          <div>로딩중</div>
        ) : (
          <>
            <TablesOuter
              columns={ activeTab === "img" ? TableColumnsInfo.VIRTUAL_DISK : TableColumnsInfo.VMS_STOP }
              data={disks.map((e) => ({
                ...e,
                virtualSize: (e?.virtualSize / Math.pow(1024, 3)) + " GB",
                actualSize: (e?.actualSize / Math.pow(1024, 3)) + " GB",
                storageDomain: e?.storageDomainVo?.name,
                status:
                  e?.status === "UNINITIALIZED"
                    ? "초기화되지 않음"
                    : "UP",
                check: (
                  <input
                    type="checkbox"
                    id={`diskSelection-${e.id}`}
                    checked={selectedDiskIds.includes(e.id)} // 선택 상태 확인
                    onChange={(e) => handleCheckboxChange(e.id)} // 선택/해제 처리
                  />
                ),
                interface: (
                  <select
                    id={`interface-select-${e.id}`}
                    value={selectedInterfaces[e.id] || "VIRTIO"} // 디스크별 상태 유지
                    onChange={(ev) => handleInterfaceChange(e.id, ev.target.value)}
                  >
                    {interfaceList.map((iface) => (
                      <option key={iface.value} value={iface.value}>
                        {iface.label}
                      </option>
                    ))}
                  </select>
                ),
                readonly: (
                  <input
                    type="checkbox"
                    id={"readonly"}
                    // checked={} // 선택 상태 확인
                    // onChange={() => handleCheckboxChange(e.id)} // 선택/해제 처리
                  />
                ),
                os: (
                  <input
                    type="checkbox"
                    id={"os"}
                    // checked={} // 선택 상태 확인
                    // onChange={() => handleCheckboxChange(e.id)} // 선택/해제 처리
                  />
                ),
              }))}
              onRowClick={(selectRow) => {
                const clickedDiskId = selectRow[0]?.id;
                handleCheckboxChange(clickedDiskId); // 행 클릭 시 선택/해제 처리
              }}
            />
          </>
        )}
        
        <span>선택된 디스크 ID: {selectedDiskIds.join(", ") || ""}</span>
        <div className="edit-footer">
          <button onClick={handleOkClick}>OK</button>
          <button onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmDiskConnectionModal;

