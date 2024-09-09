// AffinityGroupModal.js
import React from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faTimes, faInfoCircle
} from '@fortawesome/free-solid-svg-icons'

const AffinityGroupModal = ({ isOpen, onRequestClose }) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="새 선호도 그룹"
      className="pregroup_create"
      overlayClassName="pregroup_create_outer"
      shouldCloseOnOverlayClick={false}
    >
      <div className="network_popup_header">
        <h1>새 선호도 그룹</h1>
        <button onClick={onRequestClose}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
      </div>
      <div id="pregroup_create_content">
        <div className="snap_create_inputbox">
          <span>이름</span>
          <input type="text" />
        </div>
        <div className="snap_create_inputbox">
          <span>설명</span>
          <input type="text" />
        </div>
        <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
          <div>
            <span>우선 순위</span>
            <FontAwesomeIcon icon={faInfoCircle} fixedWidth/>
          </div>
          <input type="text" />
        </div>
        <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
          <div>
            <label htmlFor="disk_profile">가상 머신 선호도 규칙</label>
            <FontAwesomeIcon icon={faInfoCircle} fixedWidth/>
          </div>
          <div className="pregroup_create_select">
            <div>
              <select id="disk_profile">
                <option value="disabled">비활성화됨</option>
              </select>
            </div>
            <div>
              <input type="checkbox" id="enforce_disk_profile" />
              <label htmlFor="enforce_disk_profile">강제 적용</label>
            </div>
          </div>
        </div>
        <div className="snap_create_inputbox" style={{ paddingLeft: '0.34rem' }}>
          <div>
            <label htmlFor="host_preference_rule">호스트 선호도 규칙</label>
            <FontAwesomeIcon icon={faInfoCircle} fixedWidth/>
          </div>
          <div className="pregroup_create_select">
            <div>
              <select id="host_preference_rule">
                <option value="disabled">비활성화됨</option>
              </select>
            </div>
            <div>
              <input type="checkbox" id="enforce_rule" />
              <label htmlFor="enforce_rule">강제 적용</label>
            </div>
          </div>
        </div>
      </div>
      <div className="pregroup_create_buttons">
        <div className="pregroup_buttons_content">
          <label htmlFor="cluster">가상머신</label>
          <div className="pregroup_buttons_select">
            <div>
              <select id="cluster">
                <option value="default">가상머신:on20-ap01</option>
              </select>
            </div>
            <div>
              <button>+</button>
              <button>-</button>
            </div>
          </div>
        </div>
        <div className="pregroup_buttons_content">
          <label htmlFor="cluster">호스트</label>
          <div className="pregroup_buttons_select">
            <div>
              <select id="cluster">
                <option value="default">호스트 선택</option>
              </select>
            </div>
            <div>
              <button>+</button>
              <button>-</button>
            </div>
          </div>
        </div>
      </div>
      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={onRequestClose}>취소</button>
      </div>
    </Modal>
  );
};

export default AffinityGroupModal;
