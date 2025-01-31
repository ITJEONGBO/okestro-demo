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
  <div className="disk-new-nav">
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

const VmDiskConnectionModal = ({ isOpen, editMode=false, vm, dataCenterId, existingDisks = [], onSelectDisk = () => {}, onClose }) => {
  const [activeTab, setActiveTab] = useState("img");
  const [selectedDiskIds, setSelectedDiskIds] = useState([]); // 중복 선택을 위한 배열
  const [selectedInterfaces, setSelectedInterfaces] = useState({});
  const [selectedReadOnly, setSelectedReadOnly] = useState({});
  const [selectedOs, setSelectedOs] = useState({});

  const {
    data: disks = [], isLoading
  } = useFindDiskListFromDataCenter(dataCenterId, (e) => ({...e,}));
  
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
      [diskId]: newInterface, // ✅ diskId를 키로 새로운 인터페이스 값 저장
    }));
  };
  
  const handleOkClick = () => {
    if (selectedDiskIds.length > 0) {
      const selectedDisks = selectedDiskIds
        .map((diskId) => {
          const diskDetails = disks.find((disk) => disk.id === diskId);
          
          // 선택된 디스크가 존재할 경우에만 추가
          if (!diskDetails) return null;
  
          return {
            id: diskId,
            alias: diskDetails?.alias,
            interface: selectedInterfaces[diskId] || "VIRTIO_SCSI",
            readOnly: selectedReadOnly[diskId] || false,
            os: selectedOs[diskId] || false,
            virtualSize: (diskDetails.virtualSize / Math.pow(1024, 3)).toFixed(2),
            storageDomain: diskDetails?.storageDomainVo?.name ,
          };
        })
        .filter(Boolean);
  
      onSelectDisk(selectedDisks);
      onClose();
    } else {
      toast.error("디스크를 선택하세요!");
    }
  };  

  useEffect(() => {
    if (isOpen) {
      setSelectedDiskIds(existingDisks); // ✅ 기존 선택된 디스크 적용

      // 기존 디스크의 인터페이스 및 설정 유지
      const initialInterfaces = {};
      const initialReadOnly = {};
      const initialOs = {};
      disks.forEach((disk) => {
        initialInterfaces[disk.id] = "VIRTIO_SCSI";
        initialReadOnly[disk.id] = false;
        initialOs[disk.id] = false;
      });

      setSelectedInterfaces(initialInterfaces);
      setSelectedReadOnly(initialReadOnly);
      setSelectedOs(initialOs);
    }
  }, [isOpen, disks, existingDisks]);

  const handleCheckboxChange = (diskId) => {
    setSelectedDiskIds((prev) =>
      prev.includes(diskId) ? prev.filter((id) => id !== diskId) : [...prev, diskId]
    );
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
    >
      <div className="storage-disk-new-popup">
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
                  e?.status === "UNINITIALIZED" ? "초기화되지 않음" : "UP",
                check: (
                  <input
                    type="checkbox"
                    checked={selectedDiskIds.includes(e.id)} 
                    onChange={() => handleCheckboxChange(e.id)} // ✅ 체크박스를 클릭해야 선택됨
                  />
                ),
                interface: (
                  <select
                    id={`interface-select-${e.id}`}
                    value={selectedInterfaces[e.id] || "VIRTIO_SCSI"} // ✅ 개별 디스크 상태 유지
                    onChange={(event) => {
                      handleInterfaceChange(e.id, event.target.value); // ✅ 디스크 ID를 전달
                    }}
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
                    id={`readonly-${e.id}`}
                    checked={selectedReadOnly[e.id] || false} // ✅ 개별 디스크 상태 유지
                    onChange={() => {
                      setSelectedReadOnly((prev) => ({
                        ...prev,
                        [e.id]: !prev[e.id],
                      }));
                    }}
                  />
                ),
                os: (
                  <input
                    type="checkbox"
                    id={`os-${e.id}`}
                    checked={selectedOs[e.id] || false} // ✅ 개별 디스크 상태 유지
                    onChange={() => {
                      setSelectedOs((prev) => ({
                        ...prev,
                        [e.id]: !prev[e.id],
                      }));
                    }}
                  />
                ),
              }))}
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

