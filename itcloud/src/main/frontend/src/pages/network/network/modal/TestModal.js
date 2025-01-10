import React, { useState, useRef } from "react";
import Modal from "react-modal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes, faNetworkWired, faDesktop, faCheck, faArrowsAltH } from "@fortawesome/free-solid-svg-icons";
import "../css/MNetwork.css";

const HostNetworkModal = ({ isOpen, onRequestClose }) => {
  const dragItem = useRef(null);

  // 임시 데이터
  const [lefContent, setLefContent] = useState([
    { id: "1", name: "ens161", assignedNetworks: [] },
    { id: "2", name: "ens224", assignedNetworks: [] },
    { id: "3", name: "ens256", assignedNetworks: [] },
    { id: "4", name: "ens192", assignedNetworks: [{ id: "4", name: "ovirtmgmt" }] },
  ]);

  const [unassignedNetworks, setUnassignedNetworks] = useState([
    { id: "1", name: "Network A" },
    { id: "2", name: "Network B" },
    { id: "3", name: "Network C" },
  ]);

  const dragStart = (e, index, list) => {
    dragItem.current = { index, list };
  };

  const drop = (interfaceId) => {
    if (dragItem.current) {
      const { index, list } = dragItem.current;

      if (list === "unassigned") {
        const draggedItem = unassignedNetworks[index];

        // 인터페이스의 assignedNetworks에 추가
        setLefContent((prev) =>
          prev.map((item) =>
            item.id === interfaceId
              ? { ...item, assignedNetworks: [...item.assignedNetworks, draggedItem] }
              : item
          )
        );

        // 오른쪽 네트워크 목록에서 제거
        setUnassignedNetworks((prev) => prev.filter((_, idx) => idx !== index));
      }

      dragItem.current = null;
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="호스트 네트워크 설정"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="vnic_new_content_popup">
        <div className="popup_header">
          <h1>호스트 네트워크 설정</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="host_network_outer px-1.5 text-sm">
          <div className="py-2 font-bold underline">드래그 하여 변경</div>

          <div className="host_network_separation">
            {/* 왼쪽 인터페이스 */}
            <div className="network_separation_left">
              <div>인터페이스</div>

              {lefContent.map((interfaceItem) => (
                <div
                  key={interfaceItem.id}
                  className="separation_left_content"
                  onDragOver={(e) => e.preventDefault()}
                  onDrop={() => drop(interfaceItem.id)}
                  style={{
                    padding: "15px",
                    marginBottom: "10px",
                    backgroundColor: "skyblue",
                    borderRadius: "5px",
                    textAlign: "center",
                  }}
                >
                  <div className="interface" style={{ background: "yellow" }}>
                    <div
                      className="container"
                      draggable
                      onDragStart={(e) => dragStart(e, interfaceItem.id, "interface")}
                      style={{
                        backgroundColor: "olive",
                        borderRadius: "5px",
                        textAlign: "center",
                        padding: "10px",
                        fontSize: "16px",
                      }}
                    >
                      <FontAwesomeIcon icon={faDesktop} style={{ color: "#28a745", marginRight: "10px" }} />
                      {interfaceItem.name}
                    </div>
                    
                  </div>

                  <div className="flex items-center justify-center">
                    <FontAwesomeIcon
                      icon={faArrowsAltH}
                      style={{ color: "grey", width: "5vw", fontSize: "0.6rem" }}
                    />
                  </div>

                  {interfaceItem.assignedNetworks.length > 0 && (
                    <div className="assigned-network-outer">
                      {interfaceItem.assignedNetworks.map((network) => (
                        <div key={network.id} className="assigned-network">
                          <div className="left-section">
                            <FontAwesomeIcon icon={faCheck} className="icon green-icon" />
                            <span className="text">{network.name}</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>

            {/* 오른쪽 네트워크 */}
            <div className="network_separation_right">
              <div>할당되지 않은 네트워크</div>
              {unassignedNetworks.map((network, index) => (
                <div
                  key={network.id}
                  draggable
                  onDragStart={(e) => dragStart(e, index, "unassigned")}
                  onDragOver={(e) => e.preventDefault()}
                  style={{
                    padding: "15px",
                    marginBottom: "10px",
                    backgroundColor: "lightblue",
                    border: "1px solid #ddd",
                    borderRadius: "5px",
                    textAlign: "center",
                    fontSize: "16px",
                    cursor: "pointer",
                  }}
                >
                  <FontAwesomeIcon icon={faNetworkWired} style={{ color: "#007bff", marginRight: "10px" }} />
                  {network.name}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default HostNetworkModal;
