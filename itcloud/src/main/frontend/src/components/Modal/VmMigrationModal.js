import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import { useHostsForMigration } from '../../api/RQHook';

const VmMigrationModal = ({ isOpen, onRequestClose, selectedVm }) => {
  const [selectedHost, setSelectedHost] = useState('');
  const [isHaMode, setIsHaMode] = useState(false);

  // 연결가능한 호스트목록
  const { data: ableHost } = useHostsForMigration(selectedVm.id);

  useEffect(() => {
    if (selectedVm.id) {
      console.log('VM id:', selectedVm.id);
      console.log('ABLEHOST:', ableHost);
    }
  }, [selectedVm.id]);

  const handleSave = () => {
    console.log('Migrating VM:', {
      vm: selectedVm,
      host: selectedHost,
      haMode: isHaMode,
    });
    onRequestClose(); // 모달 닫기
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="마이그레이션"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="migration_popup_content">
        <div className="popup_header">
          <h1>가상머신 마이그레이션</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>
        <div id="migration_article_outer">
          <span>1대의 가상 머신이 마이그레이션되는 호스트를 선택하십시오.</span>

          <div id="migration_article">
            <div>
              <div id="migration_dropdown">
                <label htmlFor="host">
                  대상 호스트 <FontAwesomeIcon icon={faInfoCircle} fixedWidth />
                </label>

                <select
                  name="host_dropdown"
                  id="host"
                  value={selectedHost}
                  onChange={(e) => setSelectedHost(e.target.value)}
                >
                  <option value="">호스트 자동 선택</option>
                  {ableHost?.body.map(host => (
                    <option key={host.id} value={host.id}>{host.name}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="checkbox_group mb-2">
              <input
                className="check_input"
                type="checkbox"
                id="ha_mode_box"
                checked={isHaMode}
                onChange={() => setIsHaMode(!isHaMode)}
              />
              <label className="check_label" htmlFor="ha_mode_box">
                선택한 가상 머신을 사용하여 양극 강제 연결 그룹의 모든 가상 시스템을 마이그레이션합니다.
              </label>
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ color: 'rgb(83, 163, 255)' }}
                fixedWidth
              />
            </div>

            <div>
              <div className="font-bold">가상머신</div>
              <div>{selectedVm?.name || 'N/A'}</div>
            </div>
          </div>
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleSave}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmMigrationModal;
