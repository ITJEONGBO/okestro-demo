import React from 'react';
import Modal from 'react-modal';
import Table from '../table/Table';

const Permission = ({ isOpen, onRequestClose }) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="사용자에게 권한 추가"
      className="power_add"
      overlayClassName="power_add_outer"
      shouldCloseOnOverlayClick={false}
    >
      <div className="network_popup_header">
        <h1>사용자에게 권한 추가</h1>
        <button onClick={onRequestClose}><i className="fa fa-times"></i></button>
      </div>

      <div className="power_radio_group">
        <input type="radio" id="user" name="option" defaultChecked />
        <label htmlFor="user">사용자</label>
        
        <input type="radio" id="group" name="option" />
        <label htmlFor="group">그룹</label>
        
        <input type="radio" id="all" name="option" />
        <label htmlFor="all">모두</label>
        
        <input type="radio" id="my_group" name="option" />
        <label htmlFor="my_group">내 그룹</label>
      </div>

      <div className="power_contents_outer">
        <div>
          <label htmlFor="search">검색:</label>
          <select id="search">
            <option value="default">Default</option>
          </select>
        </div>
        <div>
          <label htmlFor="namespace">네임스페이스:</label>
          <select id="namespace">
            <option value="default">Default</option>
          </select>
        </div>
        <div>
          <label htmlFor="placeholder" style={{ color: 'white' }}>.</label>
          <select id="placeholder">
            <option value="default">Default</option>
          </select>
        </div>
        <div>
          <div style={{ color: 'white' }}>.</div>
          <input type="submit" value="검색" />
        </div>
      </div>

      <div className="power_table">
        <Table 
          columns={[
            { header: '이름', accessor: 'firstName' },
            { header: '성', accessor: 'lastName' },
            { header: '사용자 이름', accessor: 'username' },
          ]}
          data={[
            { firstName: 'dddddddddddddddddddddd', lastName: '2024. 1. 17. PM 3:14:39', username: "Snapshot 'on2o-ap01-Snapshot-2024_01_17' been completed." }
          ]}
        />
      </div>

      <div className="power_last_content">
        <label htmlFor="assigned_role">할당된 역할:</label>
        <select id="assigned_role" style={{ width: '65%' }}>
          <option value="default">UserRole</option>
        </select>
      </div>

      <div className="edit_footer">
        <button style={{ display: 'none' }}></button>
        <button>OK</button>
        <button onClick={onRequestClose}>취소</button>
      </div>
    </Modal>
  );
};

export default Permission;