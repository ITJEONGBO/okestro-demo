import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import { 
  useAllActiveDataCenters,
  useAllActiveDomainFromDataCenter, 
  useAllDiskProfileFromDomain,
  useHostsFromDataCenter,
} from '../../api/RQHook';

const FormGroup = ({ label, children }) => (
  <div className="img_input_box">
    <label>{label}</label>
    {children}
  </div>
);

const DiskUploadModal = ({
  isOpen,
  onRequestClose,
}) => {
  const [formState, setFormState] = useState({
    id: '',
    size: '',
    alias: '',
    description: '',
    wipeAfterDelete: false,
    sharable: false,
    backup: true,
    sparse: true,
  });
  const [dataCenterVoId, setDataCenterVoId] = useState('');
  const [domainVoId, setDomainVoId] = useState('');
  const [diskProfileVoId, setDiskProfileVoId] = useState('');
  const [hostVoId, setHostVoId] = useState('');

  // const { mutate: addDomain } = useAddDomain();

  // 전체 데이터센터 가져오기
  const {
    data: datacenters = [],
    refetch: refetchDatacenters,
    isLoading: isDatacentersLoading
  } = useAllActiveDataCenters((e) => ({...e,}));

  // 선택한 데이터센터가 가진 도메인 가져오기
  const {
    data: domains = [],
    refetch: refetchDomains,
    isLoading: isDomainsLoading,
  } = useAllActiveDomainFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );

  // 선택한 도메인이 가진 디스크 프로파일 가져오기
  const {
    data: diskProfiles = [],
    refetch: diskProfilesRefetch,
    isLoading: isDiskProfilesLoading,
  } = useAllDiskProfileFromDomain(
    domainVoId ? domainVoId : undefined, 
    (e) => ({...e,})
  );  

  const {
    data: hosts = [],
    refetch : refetchHosts,
    isLoading: isHostsLoading
  } = useHostsFromDataCenter(
    dataCenterVoId ? dataCenterVoId : undefined, 
    (e) => ({...e,})
  );


  useEffect(() => {
    if (datacenters && datacenters.length > 0) {
      setDataCenterVoId(datacenters[0].id);
    }
  }, [datacenters]);

  useEffect(() => {
    if (domains && domains.length > 0) {
      setDomainVoId(domains[0].id);
    }
  }, [domains]);

  useEffect(() => {
    if (diskProfiles && diskProfiles.length > 0) {
      setDiskProfileVoId(diskProfiles[0].id);
    }
  }, [diskProfiles]);

  useEffect(() => {
    if (hosts && hosts.length > 0) {
      setHostVoId(hosts[0].id);
    }
  }, [hosts]);
  

  const validateForm = () => {
    if (!formState.alias) return '별칭을 입력해주세요.';
    if (!formState.size) return '크기를 입력해주세요.';
    if (!dataCenterVoId) return '데이터 센터를 선택해주세요.';
    if (!domainVoId) return '스토리지 도메인을 선택해주세요.';
    if (!diskProfileVoId) return '디스크 프로파일을 선택해주세요.';
    if (!hostVoId) return '호스트를 선택해주세요.';
    return null;
  };

  const handleFormSubmit = () => {
    const error = validateForm();
    if (error) {
      alert(error);
      return;
    }

    const sizeToBytes = parseInt(formState.size, 10) * 1024 * 1024 * 1024; // GB -> Bytes 변환

    const selectedDataCenter = datacenters.find((dc) => dc.id === dataCenterVoId);
    const selectedDomain = domains.find((dm) => dm.id === domainVoId);
    const selectedDiskProfile = diskProfiles.find((dp) => dp.id === diskProfileVoId);
    const selectedHost = hosts.find((h) => h.id === hostVoId);

    
    // 데이터 객체 생성
    const dataToSubmit = {
      alias: formState.alias,
      description: formState.description,
      dataCenterVo: { id: selectedDataCenter.id, name: selectedDataCenter.name },
      storageDomainVo: { id: selectedDomain.id, name: selectedDomain.name },
      diskProfileVo: { id: selectedDiskProfile.id, name: selectedDiskProfile.name },
      hostVo: {id: selectedHost.id, name: selectedHost.name},
      ...formState,
      size: sizeToBytes,
      backup: formState.backup,
      bootable: formState.bootable,
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    // editHost(
    //   { hostId: formState.id, hostData: dataToSubmit }, 
    //   {
    //     onSuccess: () => {
    //       alert("Host 편집 완료")
    //       onRequestClose();  // 성공 시 모달 닫기
    //     },
    //     onError: (error) => {
    //       console.error('Error editing Host:', error);
    //     }
    //   }
    // );
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="디스크 업로드"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
    <div className="storage_disk_upload_popup">
      <div className="popup_header">
        <h1>이미지 업로드</h1>
        <button onClick={onRequestClose}>
          <FontAwesomeIcon icon={faTimes} fixedWidth />
        </button>
      </div>
      <div className="storage_upload_first">
        <input 
          type="file" 
          id="file"
        />
      </div>

      <div className="storage_upload_second">
        <div className="disk_option">디스크 옵션</div>
          <div className="disk_new_img" style={{ paddingTop: '0.4rem' }}>
            <div className="disk_new_img_left">

              <FormGroup label="크기(GB)">
                <input
                  type="number"
                  min="0"
                  value={formState.size}
                  onChange={(e) => setFormState((prev) => ({ ...prev, size: e.target.value }))}
                  disabled
                />
              </FormGroup>

              <FormGroup label="별칭">
                <input
                  type="text"
                  value={formState.alias}
                  onChange={(e) => setFormState((prev) => ({ ...prev, alias: e.target.value }))}
                />
              </FormGroup>

              <FormGroup label="설명">
                <input
                  type="text"
                  value={formState.description}
                  onChange={(e) => setFormState((prev) => ({ ...prev, description: e.target.value }))}
                />
              </FormGroup>

              <FormGroup label="데이터 센터">
                {isDatacentersLoading ? (
                    <p>데이터 센터를 불러오는 중...</p>
                  ) : datacenters.length === 0 ? (
                    <p>사용 가능한 데이터 센터가 없습니다.</p>
                  ) : (
                  <select
                    value={dataCenterVoId}
                    onChange={(e) => setDataCenterVoId(e.target.value)}
                  >
                    {datacenters && datacenters.map((dc) => (
                      <option key={dc.id} value={dc.id}>
                        {dc.name}: {dataCenterVoId}
                      </option>
                    ))}
                  </select>
                )}
              </FormGroup>

              <FormGroup label="스토리지 도메인">
                <select
                  value={domainVoId}
                  onChange={(e) => setDomainVoId(e.target.value)}
                >
                  {domains && domains.map((dm) => (
                    <option key={dm.id} value={dm.id}>
                      {dm.name}: {domainVoId}
                    </option>
                  ))}
                </select>
              </FormGroup>

              <FormGroup label="디스크 프로파일">
                <select
                  value={diskProfileVoId}
                  onChange={(e) => setDiskProfileVoId(e.target.value)}
                >
                  {diskProfiles && diskProfiles.map((dp) => (
                    <option key={dp.id} value={dp.id}>
                      {dp.name}: {diskProfileVoId}
                    </option>
                  ))}
                </select>
              </FormGroup>

              <FormGroup label="호스트">
                <select
                  value={hostVoId}
                  onChange={(e) => setHostVoId(e.target.value)}
                >
                  {hosts && hosts.map((h) => (
                    <option key={h.id} value={h.id}>
                      {h.name}: {hostVoId}
                    </option>
                  ))}
                </select>
              </FormGroup>

            </div>
            <div className="disk_new_img_right">
            <div>
              <input
                type="checkbox"
                id="wipeAfterDelete"
                checked={formState.wipeAfterDelete}
                onChange={(e) => setFormState((prev) => ({ ...prev, wipeAfterDelete: e.target.checked }))}
              />
              <label htmlFor="wipeAfterDelete">삭제 후 초기화</label>
            </div>
            <div>
              <input 
                type="checkbox" 
                className="sharable"
                checked={formState.sharable}
                onChange={(e) => setFormState((prev) => ({ ...prev, sharable: e.target.checked }))}
              />
              <label htmlFor="sharable">공유 가능</label>
            </div>

            <div>
              <input 
                type="checkbox" 
                id="backup" 
                checked={formState.backup}
                onChange={(e) => setFormState((prev) => ({ ...prev, backup: e.target.checked }))}
              />
              <label htmlFor="backup">중복 백업 사용</label>
            </div>

            </div>
          </div>
        </div>
        <div className="edit_footer">
          <button onClick={handleFormSubmit}>{'생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default DiskUploadModal;
