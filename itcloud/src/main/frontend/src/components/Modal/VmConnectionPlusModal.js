import React, { useState } from "react";
import Modal from "react-modal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import TableOuter from "../table/TableOuter";
import TableColumnsInfo from "../table/TableColumnsInfo";
import TableInfo from "../table/TableInfo";
import { useFindDiskListFromVM } from "../../api/RQHook";

const VmConnectionPlusModal = ({ isOpen, onRequestClose, vmId, onSelectDisk = () => {} }) => {
  const [activeTab, setActiveTab] = useState("img"); // 현재 선택된 탭 상태 관리
  const [selectedDiskId, setSelectedDiskId] = useState(null); // 선택된 디스크 ID 상태 관리

  const handleTabClick = (tab) => {
    setActiveTab(tab); // 탭 클릭 시 상태 업데이트
  };


  const handleOkClick = () => {
    if (selectedDiskId) {
      const selectedDiskDetails = disks.find(disk => disk.id === selectedDiskId); // 디스크 상세 정보
      onSelectDisk(selectedDiskId, selectedDiskDetails); // ID와 상세 정보 전달
      onRequestClose(); // 모달 닫기
    } else {
      alert("디스크를 선택하세요!");
    }
  };

  const { data: disks } = useFindDiskListFromVM((e) => ({
    ...e,
    radio: (
      <input
        type="radio"
        name="diskSelection"
        value={e.id} // 각 디스크의 ID를 값으로 설정
        onChange={() => setSelectedDiskId(e.id)} // 선택한 디스크 ID 업데이트
      />
    ),
    storageDomainVo: e?.storageDomainVo?.name,
    status: e?.status === "UNINITIALIZED" ? "초기화되지 않음" : "UP",
  }));

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="가상 디스크 연결"
      className="Modal"
      overlayClassName="Overlay newRolePopupOverlay"
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>가상 디스크 연결</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick("img")}
            className={activeTab === "img" ? "active" : ""}
          >
            이미지
          </div>
          <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick("directlun")}
            className={activeTab === "directlun" ? "active" : ""}
          >
            직접 LUN
          </div>
        </div>
        {activeTab === "img" && (
          <TableOuter
            columns={TableInfo.VIRTUAL_DISK}
            data={disks || []}
            onRowClick={() => console.log("Row clicked in 이미지 탭")}
          />
        )}
        {activeTab === "directlun" && (
          <TableOuter
            columns={TableColumnsInfo.VMS_STOP}
            data={[]} // 직접 LUN 데이터를 여기에 추가하세요.
            onRowClick={() => console.log("Row clicked in 직접 LUN 탭")}
          />
        )}

        {/* 선택한 디스크 ID 출력 */}
        <span>선택된 디스크 ID: {selectedDiskId || "없음"}</span>

        <div className="edit_footer">
          <button onClick={handleOkClick}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmConnectionPlusModal;
