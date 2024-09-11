import React, { useState, useEffect ,useParams} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faArrowCircleUp, faChevronRight, faPlug, faTimes } from  '@fortawesome/free-solid-svg-icons'
import Modal from 'react-modal';



// 네트워크 인터페이스
const NetworkSection = () => {

    // const { name} = useParams(); 
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [visibleDetails, setVisibleDetails] = useState([]);
    useEffect(() => {
      setVisibleDetails(Array(3).fill(false)); // 초기 상태: 모든 detail 숨김
    }, []);
      
    // 팝업 열기/닫기 핸들러
    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);
    
    //table반복
    // useEffect(() => {
    //   const container = document.getElementById("network_content_outer");
    //   const originalContent = document.querySelector('.network_content');
      
  
    //   for (let i = 0; i < 3; i++) {
    //     const clone = originalContent.cloneNode(true);
    //     container.appendChild(clone);
    //   }
    // }, []);
  
    const toggleDetails = (index) => {
      setVisibleDetails((prevDetails) => {
        const newDetails = [...prevDetails];
        newDetails[index] = !newDetails[index];
        return newDetails;
      });
    };
    
    return (
      <>
      
          <div className="content_header_right">
            <button id="network_popup_new" onClick={openModal}>새로 만들기</button>
            <button>수정</button>
            <button>제거</button>
          </div>
          {Array.from({ length: 3 }).map((_, index) => (
    <div key={index}>
      <div className="network_content">
        <div>
          <FontAwesomeIcon icon={faChevronRight} onClick={() => toggleDetails(index)}fixedWidth/>
          <FontAwesomeIcon icon={faArrowCircleUp} style={{ color: '#21c50b', marginLeft: '0.3rem' }}fixedWidth/>
          <FontAwesomeIcon icon={faPlug} fixedWidth/>
          {/* <FontAwesomeIcon icon={faUsb} fixedWidth/> */}
          <span>nic1</span>
        </div>
        <div>
          <div>네트워크 이름</div>
          <div>ddf</div>
        </div>
        <div>
          <div>IPv4</div>
          <div>192.168.10.147</div>
        </div>
        <div>
          <div>IPv6</div>
          <div>192.168.10.147</div>
        </div>
        <div style={{ paddingRight: '3%' }}>
          <div>MAC</div>
          <div>192.168.10.147</div>
        </div>
      </div>
      <div className='network_content_detail' style={{ display: visibleDetails[index] ? 'block' : 'none' }}>
        설명입력
      </div>
    </div>
  ))}

    {/*네트워크 인터페이스(새로만들기) */}
    <Modal
          isOpen={isModalOpen}
          onRequestClose={closeModal}
          contentLabel="새로만들기"
          className="network_popup"
          overlayClassName="network_popup_outer"
          shouldCloseOnOverlayClick={false}
        >
                      <div className="popup_header">
                  <h1>네트워크 인터페이스 수정</h1>
                  <button  onClick={closeModal}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
              </div>
  
              <div className="network_popup_content">
                  <div className="input_box">
                      <span>이름</span>
                      <input type="text"/>
                  </div>
                  <div className="select_box">
                      <label htmlFor="profile">프로파일</label>
                      <select id="profile">
                          <option value="test02">ovirtmgmt/ovirtmgmt</option>
                      </select>
                  </div>
                  <div className="select_box">
                      <label htmlFor="type" style={{ color: 'gray' }}>유형</label>
                      <select id="type" disabled>
                          <option value="test02">VirtIO</option>
                      </select>
                  </div>
                  <div className="select_box2" style={{ marginBottom: '2%' }}>
                      <div>
                          <input type="checkbox" id="custom_mac_box" disabled/>
                          <label htmlFor="custom_mac_box" style={{ color: 'gray' }}>
                              사용자 지정 MAC 주소
                          </label>
                      </div>
                      <div>
                          <select id="mac_address" disabled>
                              <option value="test02">VirtIO</option>
                          </select>
                      </div>
                  </div>
  
                  <div className="plug_radio_btn">
                      <span>링크 상태</span>
                      <div>
                          <div className="radio_outer">
                              <div>
                                  <input type="radio" name="status" id="status_up"/>
                                  <img src=".//img/스크린샷 2024-05-24 150455.png" alt="status_up"/>
                                  <label htmlFor="status_up">Up</label>
                              </div>
                              <div>
                                  <input type="radio" name="status" id="status_down"/>
                                  <img src=".//img/Down.png" alt="status_down"/>
                                  <label htmlFor="status_down">Down</label>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div className="plug_radio_btn">
                      <span>카드 상태</span>
                      <div>
                          <div className="radio_outer">
                              <div>
                                  <input type="radio" name="connection_status" id="connected"/>
                                  <img src=".//img/연결됨.png" alt="connected"/>
                                  <label htmlFor="connected">연결됨</label>
                              </div>
                              <div>
                                  <input type="radio" name="connection_status" id="disconnected"/>
                                  <img src=".//img/분리.png" alt="disconnected"/>
                                  <label htmlFor="disconnected">분리</label>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
  
              <div className="network_parameter_outer">
                  <span>네트워크 필터 매개변수</span>
                  <div>
                      <div>
                          <span>이름</span>
                          <input type="text"/>
                      </div>
                      <div>
                          <span>값</span>
                          <input type="text"/>
                      </div>
                      <div id="buttons">
                          <button>+</button>
                          <button>-</button>
                      </div>
                  </div>
              </div>
  
              <div className="edit_footer">
                  <button style={{ display: 'none' }}></button>
                  <button>OK</button>
                  <button  onClick={closeModal}>취소</button>
              </div>
    </Modal>
      </>
    );
  };
  export default NetworkSection;
  