import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { useAddTemplate, useClustersFromDataCenter, useDiskById, useDisksFromVM, useDomainsFromDataCenter } from '../../api/RQHook'; // 클러스터 가져오는 훅

const VmAddTemplateModal = ({ 
  isOpen, 
  onRequestClose, 
  selectedVm
}) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [isSubtemplate, setIsSubtemplate] = useState(false); 
  const [rootTemplate, setRootTemplate] = useState('');
  const [subVersionName, setSubVersionName] = useState(''); // 서브템플릿 버전 생성
  const [allowAllAccess, setAllowAllAccess] = useState(true); // 모든 사용자에게 허용
  const [copyVmPermissions, setCopyVmPermissions] = useState(false); // 권한 복사
  const [sealTemplate, setSealTemplate] = useState(false); // 템플릿봉인

  const [dataCenterId, setDataCenterId] = useState('');
  const [clusters, setClusters] = useState([]); // 클러스터 목록 상태
  const [selectedCluster, setSelectedCluster] = useState(''); // 선택된 클러스터 ID
  const [forceRender, setForceRender] = useState(false);// 선택된 대상

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
  useEffect(() => {
    if (clustersFromDataCenter) {
      setClusters(clustersFromDataCenter);
      if (clustersFromDataCenter.length > 0) {
        setSelectedCluster(clustersFromDataCenter[0].id); // 첫 번째 클러스터 기본값 설정
      }
    }
  }, [clustersFromDataCenter]);


 // 데이터센터 ID 기반으로 스토리지목록 가져오기
  const {  data: storageFromDataCenter, } = useDomainsFromDataCenter(dataCenterId , (e) =>({
    ...e,
  })); 
  useEffect(() => {
    console.log("Storage from Data Center:", storageFromDataCenter);
    console.log("Data Center ID:", dataCenterId);
}, [storageFromDataCenter]);


  // 디스크할당
  // 가상머신에 연결되어있는 디스크
  const { data: disks } = useDisksFromVM(selectedVm.id, (e) => ({
    ...e,
  }));
  useEffect(() => {
    if (disks) {
      console.log("가상머신에 연결된 디스크 데이터:", disks);
      console.log("가상머신 id:", selectedVm.id);
    }
  }, [disks]);

  const { mutate: addTemplate} = useAddTemplate();

// 포맷
const format = [
  { value: 'RAW', label: 'Raw' },
  { value: 'COW', label: 'cow' },
];
const [selectedFormat, setSelectedFormat] = useState('RAW');


  const handleFormSubmit = () => {
    // 디스크가 없을 때 기본값 설정
    const diskImageVo = disks && disks[0]?.diskImageVo ? disks[0].diskImageVo : {
      id: '',
      size: 0,
      alias: '',
      description: '',
      format: 'RAW', // 기본 포맷
      sparse: false,
      storageDomainVo: { id: '', name: '' },
      diskProfileVo: { id: '', name: '' },
    };
  
    const dataToSubmit = {
      name,
      description,
      comment,
      clusterVo: {
        id: selectedCluster || '', // 기본값 설정
        name: clusters.find(cluster => cluster.id === selectedCluster)?.name || '',
      },
      diskImageVo: {
        id: diskImageVo.id,
        size: diskImageVo.size,
        alias: diskImageVo.alias,
        description: diskImageVo.description,
        format: diskImageVo.format,
        sparse: diskImageVo.sparse,
      },
      storageDomainVo: {
        id: diskImageVo.storageDomainVo.id || '',
        name: diskImageVo.storageDomainVo.name || '',
      },
      diskProfileVo: {
        id: diskImageVo.diskProfileVo.id || '',
        name: diskImageVo.diskProfileVo.name || '',
      },
    };
  
    console.log('템플릿 생성데이터:', dataToSubmit);
  
    addTemplate(dataToSubmit, {
      onSuccess: () => {
        alert('템플릿 생성 완료');
        onRequestClose();
      },
      onError: (error) => {
        console.error('Error adding Template:', error);
      },
    });
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

{disks && disks.length > 0 && (
    <>
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
            {disks.map((disk,index) => (
              <tr key={disk.id}>
                <td>{disk.diskImageVo?.alias || "없음"}</td>
                <td>{(disk.diskImageVo?.virtualSize / (1024 ** 3) || 0).toFixed(0)} GiB</td>
                <td>
                <select
                  id={`format-${index}`}
                  value={disk.diskImageVo?.format || "RAW"} // 기본값 설정
                  onChange={(e) => {
                    const newFormat = e.target.value;
                    disks[index].diskImageVo.format = newFormat; // 디스크 데이터 업데이트
                    setSelectedFormat(newFormat); // 상태 업데이트 (선택적)
                  }}
                >
                  {format.map((option) => (
                    <option key={option.value} value={option.value}>
                      {option.label} {/* 화면에 표시될 한글 */}
                    </option>
                  ))}
                </select>
                <span> 선택된 포맷: {disk.diskImageVo?.format || "RAW"}</span>
              </td>
              <td>
  <select
    value={disk.diskImageVo?.storageDomainVo?.id || ""}
    onChange={(e) => {
      const selectedStorage = storageFromDataCenter.find(
        (storage) => storage.id === e.target.value
      );
      if (selectedStorage) {
        disk.diskImageVo.storageDomainVo = {
          id: selectedStorage.id,
          name: selectedStorage.name,
        };
        // 강제 리렌더링 트리거
        setForceRender((prev) => !prev);
      }
    }}
  >
    <option value="">선택</option>
    {storageFromDataCenter &&
      storageFromDataCenter.map((storage) => (
        <option key={storage.id} value={storage.id}>
          {storage.name}
        </option>
      ))}
  </select>
</td>



                <td>
                  <select defaultValue={disk.diskImageVo?.diskProfileVo?.name || "선택"}>
                    <option value={disk.diskImageVo?.diskProfileVo?.name || ""}>
                      {disk.diskImageVo?.diskProfileVo?.name || "선택"}
                    </option>
                    <option value="Option 2">Option 2</option>
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  )}
  {!disks || disks.length === 0 ? (
    <div className="font-bold">연결된 디스크 데이터가 없습니다.</div>
  ) : null}

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
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default VmAddTemplateModal;
