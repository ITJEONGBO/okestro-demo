import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useClustersFromDataCenter } from '../../api/RQHook'; // 클러스터 가져오는 훅

const VmAddTemplateModal = ({ isOpen, onRequestClose, selectedVm }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [isSubtemplate, setIsSubtemplate] = useState(false);
  const [rootTemplate, setRootTemplate] = useState('');
  const [subVersionName, setSubVersionName] = useState('');
  const [allowAllAccess, setAllowAllAccess] = useState(true);
  const [copyVmPermissions, setCopyVmPermissions] = useState(false);
  const [sealTemplate, setSealTemplate] = useState(false);

  const [dataCenterId, setDataCenterId] = useState('');
  const [clusters, setClusters] = useState([]); // 클러스터 목록 상태
  const [selectedCluster, setSelectedCluster] = useState(''); // 선택된 클러스터 ID

  // 데이터센터 ID 가져오기
  useEffect(() => {
    if (selectedVm?.dataCenterId) {
      setDataCenterId(selectedVm.dataCenterId);
    }
  }, [selectedVm]);

  // 데이터센터 ID 기반으로 클러스터 목록 가져오기
  const { data: clustersFromDataCenter } = useClustersFromDataCenter(dataCenterId, (cluster) => ({
    id: cluster.id,
    name: cluster.name,
  }));

  // 클러스터 목록 업데이트
  useEffect(() => {
    if (clustersFromDataCenter) {
      setClusters(clustersFromDataCenter);
      if (clustersFromDataCenter.length > 0) {
        setSelectedCluster(clustersFromDataCenter[0].id); // 첫 번째 클러스터 기본값 설정
      }
    }
  }, [clustersFromDataCenter]);

  const handleSave = () => {
    console.log('Template saved with details:', {
      name,
      description,
      comment,
      isSubtemplate,
      rootTemplate,
      subVersionName,
      allowAllAccess,
      copyVmPermissions,
      sealTemplate,
      selectedCluster, // 선택된 클러스터 ID 추가
    });
    onRequestClose(); // 모달 닫기
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="새 템플릿"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="new_template_popup" style={{ height: isSubtemplate ? '88vh' : '77vh' }}>
        <div className="popup_header">
          <h1>새 템플릿</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="edit_first_content">
          <div className="host_textbox">
            <label htmlFor="user_name">이름</label>
            <input
              type="text"
              id="user_name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div className="host_textbox">
            <label htmlFor="description">설명</label>
            <input
              type="text"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <div className="host_textbox">
            <label htmlFor="comment">코멘트</label>
            <input
              type="text"
              id="comment"
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />
          </div>
          <div className="edit_fourth_content_select flex">
            <label htmlFor="cluster_select">클러스터</label>
            <select
              id="cluster_select"
              value={selectedCluster}
              onChange={(e) => setSelectedCluster(e.target.value)}
            >
              {clusters.length > 0 ? (
                clusters.map((cluster) => (
                  <option key={cluster.id} value={cluster.id}>
                    {cluster.name}
                  </option>
                ))
              ) : (
                <option value="">클러스터 없음</option>
              )}
            </select>
          </div>
          <div className="edit_fourth_content_select flex">
            <label htmlFor="cpu_profile_select">CPU 프로파일</label>
            <select id="cpu_profile_select">
              <option value="default">Default</option>
            </select>
          </div>
        </div>

        <div>
          <div className="vnic_new_checkbox">
            <input
              type="checkbox"
              id="create_as_subtemplate"
              checked={isSubtemplate}
              onChange={() => setIsSubtemplate(!isSubtemplate)}
            />
            <label htmlFor="create_as_subtemplate">서브 템플릿 버전으로 생성</label>
          </div>
        </div>

        {isSubtemplate && (
          <div className="subtemplate_fields">
            <div className="network_form_group">
              <label htmlFor="root_template">Root 템플릿</label>
              <select
                id="root_template"
                value={rootTemplate}
                onChange={(e) => setRootTemplate(e.target.value)}
              >
                <option value="">선택하세요</option>
                <option value="template-1">template-1</option>
                <option value="template-2">template-2</option>
              </select>
            </div>

            <div className="network_form_group mb-1.5">
              <label htmlFor="sub_version_name">하위 버전 이름</label>
              <input
                type="text"
                id="sub_version_name"
                value={subVersionName}
                onChange={(e) => setSubVersionName(e.target.value)}
              />
            </div>
          </div>
        )}

        <div className="font-bold">디스크 할당:</div>
        <div className="section_table_outer py-1">
          <table>
            <thead>
              <tr>
                <th>별칭</th>
                <th>가상 크기</th>
                <th>포맷</th>
                <th>대상</th>
                <th>디스크 프로파일</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>he_sanlock</td>
                <td>1 GiB</td>
                <td>
                  <select>
                    <option>NFS</option>
                    <option>Option 2</option>
                  </select>
                </td>
                <td>
                  <select>
                    <option>NFS (499 GiB)</option>
                    <option>Option 2</option>
                  </select>
                </td>
                <td>
                  <select>
                    <option>NFS</option>
                    <option>Option 2</option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="vnic_new_checkbox">
          <input
            type="checkbox"
            id="allow_all_access"
            checked={allowAllAccess}
            onChange={() => setAllowAllAccess(!allowAllAccess)}
          />
          <label htmlFor="allow_all_access">모든 사용자에게 이 템플릿 접근을 허용</label>
        </div>
        <div className="vnic_new_checkbox">
          <input
            type="checkbox"
            id="copy_vm_permissions"
            checked={copyVmPermissions}
            onChange={() => setCopyVmPermissions(!copyVmPermissions)}
          />
          <label htmlFor="copy_vm_permissions">가상 머신 권한 복사</label>
        </div>
        <div className="vnic_new_checkbox">
          <input
            type="checkbox"
            id="seal_template_linux_only"
            checked={sealTemplate}
            onChange={() => setSealTemplate(!sealTemplate)}
          />
          <label htmlFor="seal_template_linux_only">템플릿 봉인 (Linux만 해당)</label>
        </div>

        <div className="edit_footer">
          <button onClick={handleSave}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmAddTemplateModal;
