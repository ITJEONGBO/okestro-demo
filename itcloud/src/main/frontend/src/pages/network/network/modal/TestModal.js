import React, { useState, useRef, useEffect } from "react";
import Modal from "react-modal";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowsAltH, faBan, faCrown, faDesktop, faExclamationTriangle, faFan, faPencilAlt, faTimes } from "@fortawesome/free-solid-svg-icons";
import "../css/MNetwork.css";
import { useHost, useNetworkFromCluster } from "../../../../api/RQHook";

const TestModal = ({ isOpen, onRequestClose, nicData,hostId  }) => {
  const dragItem = useRef(null);

  // 호스트상세정보 조회로 클러스터id뽑기기
  const { 
    data: host
  } = useHost(hostId);
  // 클러스터id로 네트워크정보조회
  const { 
    data: network, 
  } = useNetworkFromCluster(host?.clusterVo?.id, (network) => {
  return {
      id: network?.id ?? '', 
      name: network?.name ?? 'Unknown',            
      status: network?.status ?? '',       
      role: network?.role ? <FontAwesomeIcon icon={faCrown} fixedWidth/> : '', 
      description: network?.description ?? 'No description', 
    };
  });
    useEffect(() => {
      if (network) {
          console.log("클러스터에대한 네트워크 정보:", network);
      }
  }, [network]);
  
  // Outer
  const [outer, setOuter] = useState([
    { id: "outer1", name: "Outer 1", children: [{ id: "interface1" }], networks: [{ id: "network1" }] },
    { id: "outer2", name: "Outer 2", children: [{ id: "interface2" }], networks: [{ id: "network2" }] },
    { id: "outer3", name: "Outer 3", children: [{ id: "interface3" }], networks: [{ id: "network3" }] },
    { id: "outer4", name: "Outer 4", children: [{ id: "interface4" }], networks: [{ id: "network4" }] },
  ]);

  // Interfaces
  const [unassignedInterface, setUnassignedInterface] = useState([
    { id: "interface1", name: "Interface 1", children: [{ id: "1", name: "Container A" }] },
    { id: "interface2", name: "Interface 2", children: [{ id: "2", name: "Container B" }] },
    { id: "interface3", name: "Interface 3", children: [{ id: "3", name: "Container C" }] },
    { id: "interface4", name: "Interface 4", children: [{ id: "4", name: "Container D" }] },
  ]);

  // Networks in Outer
  const [unassignedNetworksOuter, setUnassignedNetworksOuter] = useState([
    { id: "network1", name: "Network Outer A", children: [{ id: "networkcontent1", name: "Network content A" }] },
    { id: "network2", name: "Network Outer B", children: [] },
    { id: "network3", name: "Network Outer C", children: [] },
    { id: "network4", name: "Network Outer D", children: [] },
  ]);

  // Networks
  const [unassignedNetworks, setUnassignedNetworks] = useState([
    { id: "networkcontent2", name: "Network content B" },
    { id: "networkcontent3", name: "Network content C" },
    { id: "networkcontent4", name: "Network content D" },
  ]);

  const dragStart = (e, item, source, parentId = null) => {
    dragItem.current = { item, source, parentId };
  };

  const drop = (targetId, targetType) => {
    const { item, source, parentId } = dragItem.current;

    if (source === "unassigned" && targetType === "networkOuter") {
      const targetOuter = unassignedNetworksOuter.find((outer) => outer.id === targetId);
      if (targetOuter.children.length > 0) {
        alert("1개의 네트워크만 걸 수 있습니다");
        return;
      }
      setUnassignedNetworks((prev) => prev.filter((network) => network.id !== item.id));
      setUnassignedNetworksOuter((prev) =>
        prev.map((networkOuter) => {
          if (networkOuter.id === targetId) {
            return {
              ...networkOuter,
              children: [...networkOuter.children, item],
            };
          }
          return networkOuter;
        })
      );
    } else if (source === "networkOuter" && targetType === "unassigned") {
      setUnassignedNetworksOuter((prev) =>
        prev.map((networkOuter) => {
          if (networkOuter.id === parentId) {
            return {
              ...networkOuter,
              children: networkOuter.children.filter((child) => child.id !== item.id),
            };
          }
          return networkOuter;
        })
      );
      setUnassignedNetworks((prev) => [...prev, item]);
    } else if (source === "container" && targetType === "interface") {
      setUnassignedInterface((prev) =>
        prev.map((interfaceItem) => {
          if (interfaceItem.id === parentId) {
            return {
              ...interfaceItem,
              children: interfaceItem.children.filter((child) => child.id !== item.id),
            };
          }
          if (interfaceItem.id === targetId) {
            return {
              ...interfaceItem,
              children: [...interfaceItem.children, item],
            };
          }
          return interfaceItem;
        })
      );
    }

    dragItem.current = null;
  };

  const renderNetworkOuter = (outerItem) => {
    return outerItem.networks.map((network) => {
      const matchedNetworkOuter = unassignedNetworksOuter.find((outer) => outer.id === network.id);
      if (matchedNetworkOuter) {
        return (
          <div
            key={matchedNetworkOuter.id}
            className="network-outer-item"
            onDragOver={(e) => e.preventDefault()}
            onDrop={() => drop(matchedNetworkOuter.id, "networkOuter")}
          >
            <div>{matchedNetworkOuter.name}</div>
            <div className="children">
              {matchedNetworkOuter.children.map((child) => (
                <div
                  key={child.id}
                  className="network-child"
                  draggable
                  onDragStart={(e) => dragStart(e, child, "networkOuter", matchedNetworkOuter.id)}
                >
                  {child.name}
                </div>
              ))}
            </div>
          </div>
        );
      }
      return null;
    });
  };

  const renderInterface = (interfaceItem) => (
    <div
      key={interfaceItem.id}
      className="interface"
      onDragOver={(e) => e.preventDefault()}
      onDrop={() => drop(interfaceItem.id, "interface")}
    >
      <div className="interface-header">{interfaceItem.name}</div>
      <div className="children">
        {interfaceItem.children.map((child) => (
          <div
            key={child.id}
            className="container-item"
            draggable
            onDragStart={(e) => dragStart(e, child, "container", interfaceItem.id)}
          >
            {child.name}
          </div>
        ))}
      </div>
    </div>
  );

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
          <h2>호스트 네트워크 설정</h2>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} />
          </button>
        </div>

        <div className="host_network_outer px-1.5 text-sm">
          <div className="py-2 font-bold underline">드래그 하여 변경</div>
          <div className="host-network-separation">
            <div className="network-separation-left">
              <div>
                                  <div>인터페이스</div>
                                  <div>할당된 논리 네트워크</div>
                              </div>
              {outer.map((outerItem) => (
                <div key={outerItem.id} className="separation-left-content">
                  {/* <h3>{outerItem.name}</h3> */}
                  <div className="interface">
                    {outerItem.children.map((child) => {
                      const matchedInterface = unassignedInterface.find((intf) => intf.id === child.id);
                      return matchedInterface ? renderInterface(matchedInterface) : null;
                    })}
                  </div>

                    <div className="flex items-center justify-center">
                                                              <FontAwesomeIcon
                                                                  icon={faArrowsAltH}
                                                                  style={{ color: "grey", width: "5vw", fontSize: "0.6rem" }}
                                                              />
                                                          </div>
                  
                  <div className="assigned-network-outer">
                    <div className="outer-networks">네트워크넣을자리{renderNetworkOuter(outerItem)}</div>
                    <div className="right-section">
                                                                            <FontAwesomeIcon icon={faFan} className="icon" />
                                                                            <FontAwesomeIcon icon={faDesktop} className="icon" />
                                                                            <FontAwesomeIcon icon={faDesktop} className="icon" />
                                                                            <FontAwesomeIcon icon={faBan} className="icon" />
                                                                            <FontAwesomeIcon
                                                                                icon={faExclamationTriangle}
                                                                                className="icon"
                                                                            />
                                                                            <FontAwesomeIcon
                                                                                icon={faPencilAlt}
                                                                                className="icon"
                                                                              
                                                                            />
                                                                        </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Unassigned Networks */}
            <div
              className="network_separation_right"
              onDragOver={(e) => e.preventDefault()}
              onDrop={() => drop(null, "unassigned")}
            >
              {unassignedNetworks.map((network) => (
                <div
                  key={network.id}
                  className="network-item"
                  draggable
                  onDragStart={(e) => dragStart(e, network, "unassigned")}
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
                  {network.name}
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="edit-footer">
          <button style={{ display: 'none' }}></button>
          <button>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default TestModal;
