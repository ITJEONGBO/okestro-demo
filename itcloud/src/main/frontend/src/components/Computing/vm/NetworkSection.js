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

    

    const [activePopup, setActivePopup] = useState(null);

    const openPopup = (popupType) => {
      setActivePopup(popupType);
    };
    const closePopup = () => {
      setActivePopup(null);
    };
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
      
          <div className="header_right_btns">
            <button id="network_popup_new" onClick={() => openPopup('network_interface_new')}>새로 만들기</button>
            <button onClick={() => openPopup('network_interface_edit')}>편집</button>
            <button className='disabled'>제거</button>
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
        isOpen={activePopup === 'network_interface_new'}
        onRequestClose={closePopup}
        contentLabel="새로만들기"
        className="Modal"
        overlayClassName="Overlay"
        shouldCloseOnOverlayClick={false}
        >
            <div className='new_network_interface p'>
                <div className="popup_header">
                    <h1>새 네트워크 인터페이스</h1>
                    <button  onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
                </div>
    
                <div className="network_popup_content">
                    <div className="input_box pt-1">
                        <span>이름</span>
                        <input type="text" value={'#'}/>
                    </div>
                    <div className="select_box">
                        <label htmlFor="profile">프로파일</label>
                        <select id="profile">
                            <option value="#">#</option>
                        </select>
                    </div>
                    <div className="select_box">
                        <label htmlFor="type" className='disabled'>유형</label>
                        <select id="type" disabled>
                            <option value="test02">VirtIO</option>
                        </select>
                    </div>
                    
    
                    <div className="plug_radio_btn">
                        <span>링크 상태</span>
                        <div>
                            <div className="radio_outer">
                                <div>
                                    <input type="radio" name="status" id="status_up" checked/>
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
                                    <input type="radio" name="connection_status" id="connected" checked/>
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
                
                {/*삭제예정 */}
                {/* <div className="network_parameter_outer">
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
                </div> */}
    
                <div className="edit_footer">
                    <button style={{ display: 'none' }}></button>
                    <button>OK</button>
                    <button  onClick={closePopup}>취소</button>
                </div>
              </div>
    </Modal>

     {/*네트워크 인터페이스(편집) */}
     <Modal
         isOpen={activePopup === 'network_interface_edit'}
          onRequestClose={closePopup}
          contentLabel="새로만들기"
        className="Modal"
        overlayClassName="Overlay"
          shouldCloseOnOverlayClick={false}
          >
          <div className='new_network_interface p'>
            <div className="popup_header">
                <h1>새 네트워크 인터페이스</h1>
                <button  onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
            </div>

            <div className="network_popup_content">
                <div className="input_box pt-1">
                    <span>이름</span>
                    <input type="text" value={'#'}/>
                </div>
                <div className="select_box">
                    <label htmlFor="profile">프로파일</label>
                    <select id="profile">
                        <option value="#">#</option>
                    </select>
                </div>
                <div className="select_box">
                    <label htmlFor="type" className='disabled'>유형</label>
                    <select id="type" disabled>
                        <option value="test02">VirtIO</option>
                    </select>
                </div>
                

                <div className="plug_radio_btn">
                    <span>링크 상태</span>
                    <div>
                        <div className="radio_outer">
                            <div>
                                <input type="radio" name="status" id="status_up" checked/>
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
                                <input type="radio" name="connection_status" id="connected" checked/>
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
            
            <div className="edit_footer">
              <button style={{ display: 'none' }}></button>
              <button>OK</button>
              <button  onClick={closePopup}>취소</button>
          </div>
        </div>
    </Modal>
      </>
    );
  };
  export default NetworkSection;
  