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

  const dragStart = (e, index, list, item) => {
    dragItem.current = { index, list, item };
  };

  const drop = (interfaceId) => {
    if (dragItem.current) {
      const { index, list, item } = dragItem.current;

      if (list === "unassigned") {
        // 드래그된 네트워크를 인터페이스에 추가
        setLefContent((prev) =>
          prev.map((iface) =>
            iface.id === interfaceId
              ? { ...iface, assignedNetworks: [...iface.assignedNetworks, item] }
              : iface
          )
        );
        setUnassignedNetworks((prev) => prev.filter((_, idx) => idx !== index));
      } else if (list === "interface") {
        // 인터페이스 간 이동
        setLefContent((prev) =>
          prev.map((iface) => {
            if (iface.id === interfaceId) {
              return { ...iface, assignedNetworks: [...iface.assignedNetworks, item] };
            }
            if (iface.id === dragItem.current.sourceId) {
              return {
                ...iface,
                assignedNetworks: iface.assignedNetworks.filter(
                  (network) => network.id !== item.id
                ),
              };
            }
            return iface;
          })
        );
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
      <div className="vnic-new-content-popup">
        <div className="popup-header">
          <h1>호스트 네트워크 설정</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="host_network_outer px-1.5 text-sm">
          <div className="py-2 font-bold underline">드래그 하여 변경</div>

          <div className="host-network-separation">
            {/* 왼쪽 인터페이스 */}
            <div className="network-separation-left">
              <div>인터페이스</div>

              {lefContent.map((interfaceItem) => (
                <div
                  key={interfaceItem.id}
                  className="separation-left-content"
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
                      onDragStart={(e) =>
                        dragStart(e, null, "interface", {
                          ...interfaceItem,
                          sourceId: interfaceItem.id,
                        })
                      }
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
                        <div
                          key={network.id}
                          className="assigned-network"
                          draggable
                          onDragStart={(e) =>
                            dragStart(e, null, "interface", {
                              ...network,
                              sourceId: interfaceItem.id,
                            })
                          }
                        >
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
                  onDragStart={(e) => dragStart(e, index, "unassigned", network)}
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
