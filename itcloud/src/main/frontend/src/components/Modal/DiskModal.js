import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { 
  useDiskById,
  useAddDisk,
  useEditDisk, 
  useAllDataCenters,
  useDomainsFromDataCenter, 
  useAllDiskProfileFromDomain,
} from '../../api/RQHook';

const DiskModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  diskId,
  vmId,
  type='disk'
}) => {
  const [id, setId] = useState('');
  const [size, setSize] = useState('');
  const [appendSize, setAppendSize] = useState(0);
  const [alias, setAlias] = useState('');
  const [description, setDescription] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [domainVoId, setDomainVoId] = useState('');
  const [diskProfileVoId, setDiskProfileVoId] = useState('');
  const [wipeAfterDelete, setWipeAfterDelete] = useState(false);
  const [sharable, setSharable] = useState(false);
  const [backup, setBackup] = useState(false);
  const [sparse, setSparse] = useState(true); // 할당정책: 씬
  
  const { mutate: addDisk } = useAddDisk();
  const { mutate: editDisk } = useEditDisk();

  const [activeTab, setActiveTab] = useState('img');
  
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const {
    data: disk,
    status: diskStatus,
    isRefetching: isDiskRefetching,
    refetch: refetchDisk,
    isError: isDiskError,
    error: diskError,
    isLoading: isDiskLoading
  } = useDiskById(diskId);

  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({
    ...e,
  }));

  const {
    data: domains,
    status: domainsStatus,
    isRefetching: isDomainsRefetching,
    refetch: domainsRefetch,
    isError: isDomainsError,
    error: domainsError,
    isLoading: isDomainsLoading,
  } = useDomainsFromDataCenter(datacenterVoId || '', (e) => ({
    ...e,
  }));
  // 데이터센터 가 가지고 있는 스토리지도메인의 상태가 활성화 되어야 뜰수 잇음(백엔드 수정필요)

  const {
    data: diskProfiles,
    status: diskProfilesStatus,
    isRefetching: isDiskProfilesRefetching,
    refetch: diskProfilesRefetch,
    isError: isDiskProfilesError,
    error: diskProfilesError,
    isLoading: isDiskProfilesLoading,
  } = useAllDiskProfileFromDomain(domainVoId && domainVoId.trim() !== '' ? domainVoId : null, (e) => ({
    ...e,
  }));  


  useEffect(() => {
    if (isOpen) {
      if (editMode && disk) {
        setId(disk?.id);
        setSize(disk?.virtualSize);
        setAppendSize();
        setAlias(disk?.alias);
        setDescription(disk?.description);
        setDatacenterVoId(disk?.dataCenterVo?.id);
        setDomainVoId(disk?.storageDomainVo?.id);
        setDiskProfileVoId(disk?.diskProfileVo?.id);
        setWipeAfterDelete(disk?.wipeAfterDelete);
        setSharable(disk?.sharable);
        setBackup(disk?.backup);
        setSparse(disk?.sparse);
      } else if (!editMode && !isDatacentersLoading && !isDomainsLoading && !isDiskProfilesLoading) {
        resetForm();
      }
    }
  }, [isOpen, editMode, disk]);

  // 데이터센터 기본 상태
  useEffect(() => {
    if (datacenters && datacenters.length > 0 && !datacenterVoId) {
      setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터 선택
    }
  }, [datacenters]); // 의존성 배열에 datacenters 추가

  useEffect(() => {
    if (domains && domains.length > 0 && !domainVoId) {
      setDomainVoId(domains[0].id); // 첫 번째 도메인으로 초기화
    } else if (!domains || domains.length === 0) {
      setDomainVoId(''); // 도메인이 없으면 빈 값
    }
  }, [domains]);
  
  useEffect(() => {
    if (diskProfiles && diskProfiles.length > 0 && !diskProfileVoId) {
      setDiskProfileVoId(diskProfiles[0].id); // 첫 번째 도메인으로 초기화
    } else if (!diskProfiles || diskProfiles.length === 0) {
      setDiskProfileVoId(''); // 도메인이 없으면 빈 값
    }
  }, [diskProfiles]);
  

  // 데이터센터 변경 시 스토리지 도메인 로드
  useEffect(() => {
    if (datacenterVoId) {
      domainsRefetch({ datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setDomainVoId(res.data[0].id); // 첫 번째 도메인으로 기본값 설정
        } else {
          setDomainVoId(''); // 도메인이 없으면 빈 값으로 설정
          setDiskProfileVoId('');
        }
      }).catch((error) => {
        console.error('Error fetching domains:', error);
      });
    }
  }, [datacenterVoId, domainsRefetch]);  
  
  // 편집: 데이터센터에 따라 도메인 지정
  useEffect(() => {
    if (!editMode && datacenterVoId) {
      domainsRefetch({ datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setDomainVoId(res.data[0].id); 
        }
      }).catch((error) => {
        console.error('Error fetching disks:', error);
      });
    }
  }, [editMode, datacenterVoId]);


  const resetForm = () => {
    setSize('');
    setAppendSize('');
    setAlias('');
    setDescription('');
    setDatacenterVoId('');
    setDomainVoId('');
    setDiskProfileVoId('');
    setWipeAfterDelete('');
    setSharable('');
    setBackup('true');
    setSparse('true');
  };

  const handleFormSubmit = () => {
    const selectedDataCenter = datacenters.find((dc) => dc.id === datacenterVoId);
    if (!selectedDataCenter) {
      alert("데이터 센터를 선택해주세요.");
      return;
    }

    const selectedDomain = domains.find((domain) => domain.id === domainVoId);
    if (!selectedDomain) {
      alert("도메인를 선택해주세요.");
      return;
    }

    const selectedDiskProfile = diskProfiles.find((profile) => profile.id === diskProfileVoId);
    if (!selectedDomain) {
      alert("디스크 프로파일를 선택해주세요.");
      return;
    }

    if(alias === ''){
      alert("이름을 입력해주세요.");
      return;
    }

    if(size === ''){
      alert("크기를 입력해주세요.");
      return;
    }


    // 데이터 객체 생성
    const dataToSubmit = {
      // id
      size,
      // appendSize
      alias,
      description,
      datacenterVo: {
        id: selectedDataCenter.id,
        name: selectedDataCenter.name,
      },
      storageDomainVo:{
        id: selectedDomain.id,
        name: selectedDomain.name,
      },
      diskProfileVo:{
        id: selectedDiskProfile.id,
        name: selectedDiskProfile.name,
      },
      wipeAfterDelete: wipeAfterDelete,
      sharable: sharable,
      backup: backup,
      sparse: sparse,
    };
  
    console.log("Form Data: ", dataToSubmit); // 데이터를 확인하기 위한 로그
    
    if (editMode) {
      dataToSubmit.id = id;  // 수정 모드에서는 id를 추가
      dataToSubmit.appendSize = appendSize;      
      editDisk({
        diskId: id,              // 전달된 id
        diskData: dataToSubmit   // 수정할 데이터
      }, {
        onSuccess: () => {
          alert("disk 편집 완료")
          onRequestClose();  // 성공 시 모달 닫기
        },
        onError: (error) => {
          console.error('Error editing disk:', error);
        }
      });
    } else {
      addDisk(dataToSubmit, {
        onSuccess: () => {
          alert("Disk 생성 완료")
          onRequestClose();
        },
        onError: (error) => {
          console.error('Error adding Disk:', error);
        }
      });
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '디스크 편집' : '새로 만들기'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="storage_disk_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '디스크 편집' : '새 디스크'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth/>
          </button>
        </div>

        <div className="disk_new_nav">
          <div
            id="storage_img_btn"
            onClick={() => handleTabClick('img')}
            className={activeTab === 'img' ? 'active' : ''}
          >
            이미지
          </div>
          {/* <div></div> */}
          {/* <div
            id="storage_directlun_btn"
            onClick={() => handleTabClick('directlun')}
            className={activeTab === 'directlun' ? 'active' : ''}
          >
            직접LUN
          </div> */}
        </div>

        {/*이미지*/}
        {/* {activeTab === 'img' && ( */}
        <div><br/></div>
          <div className="disk_new_img">
            <div className="disk_new_img_left">

              <div className="img_input_box">
                <label htmlFor="size">크기(GB)</label>
                <input
                  type="number"
                  id="size"
                  value={
                    editMode ? (size / (1024 * 1024 * 1024)).toFixed(0) : size
                  }
                  onChange={
                    (e) => setSize(editMode ? e.target.value * 1024 * 1024 * 1024 : e.target.value)
                  }
                  disabled={editMode}
                />
              </div>
              {editMode && (
                <>
                  <div className="img_input_box">
                    <label htmlFor="appendsize">추가크기(GB)</label>
                    <input
                      type="number"
                      id="appendsize"
                      value={appendSize}
                      onChange={(e) => setAppendSize(e.target.value)}
                    />
                  </div>
                </>
              )}              

              <div className="img_input_box">
                <label htmlFor="alias">별칭</label>
                <input
                  type="text"
                  id="alias"
                  value={alias}
                  onChange={(e) => setAlias(e.target.value)}
                />
              </div>

              <div className="img_input_box">
                <label htmlFor="description">설명</label>
                <input
                  type="text"
                  id="description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="img_select_box">
                <label htmlFor="data_center">데이터 센터</label>
                <select
                  id="data_center"
                  value={datacenterVoId}
                  onChange={(e) => setDatacenterVoId(e.target.value)}
                  disabled={editMode}
                >
                  {datacenters && datacenters.map((dc) => (
                    <option key={dc.id} value={dc.id}>
                      {dc.name} {/* 디버깅용 */}
                    </option>
                  ))}
                </select>
              </div>
              <div className="img_select_box">
                <span>dc: {datacenterVoId}</span>
              </div>
              

              <div className="img_select_box">
                <label htmlFor="domains">스토리지 도메인</label>
                <select
                  id="domains"
                  value={domainVoId || ''}
                  onChange={(e) => setDomainVoId(e.target.value)}
                  disabled={editMode}
                >
                  {domains && domains.map((domain) => (
                    <option key={domain.id} value={domain.id}>
                      {domain.name || `도메인 ${domain.id}`} {/* 디버깅용 */}
                    </option>
                  ))}
                </select>
              </div>
              <div className="img_select_box">
                <span>domain: {domainVoId}</span>
              </div>
            
              <div className="img_select_box">
                <label htmlFor="sparse">할당 정책</label>
                <select 
                  id="sparse"
                  value={sparse}
                  onChange={(e) => setSparse(e.target.value)}
                  disabled={editMode}
                >
                  <option value="true">씬 프로비저닝</option>
                  <option value="false">사전 할당</option>
                </select>
              </div>

              <div className="img_select_box">
                <label htmlFor="disk_profile">디스크 프로파일</label>
                <select
                  id="disk_profile"
                  value={diskProfileVoId}
                  onChange={(e) => setDiskProfileVoId(e.target.value)}
                >
                  {diskProfiles &&
                    diskProfiles.map((dp) => (
                      <option key={dp.id} value={dp.id}>
                        {dp.name}
                      </option>
                    ))}
                </select>
              </div>
              <div className="img_select_box">
                <span>diskprofile: {diskProfileVoId}</span>
              </div>
            </div>
            
            <div className="disk_new_img_right">
              <div>
                <input
                  type="checkbox"
                  id="wipeAfterDelete"
                  checked={wipeAfterDelete}
                  onChange={(e) => setWipeAfterDelete(e.target.checked)}
                />
                <label htmlFor="wipeAfterDelete">삭제 후 초기화</label>
              </div>
              <div>
                <input 
                  type="checkbox" 
                  className="sharable" 
                  checked={sharable} // 상태와 연결
                  onChange={(e) => setSharable(e.target.checked)}
                />
                <label htmlFor="sharable">공유 가능</label>
              </div>
              <div>
                <input 
                  type="checkbox" 
                  id="backup" 
                  checked={backup} // 상태와 연결
                  onChange={(e) => setBackup(e.target.checked)}
                />
                <label htmlFor="backup">중복 백업 사용</label>
              </div>
            </div>
          </div>
        {/* )} */}
        {/* 직접LUN
        {activeTab === 'directlun' && (
          <div id="storage_directlun_outer">
            <div id="storage_lun_first">
              <div className="disk_new_img_left">
                <div className="img_input_box">
                  <span>별칭</span>
                  <input type="text" />
                </div>
                <div className="img_input_box">
                  <span>설명</span>
                  <input type="text" />
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">데이터 센터</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">호스트</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
                <div className="img_select_box">
                  <label htmlFor="os">스토리지 타입</label>
                  <select id="os">
                    <option value="linux">Linux</option>
                  </select>
                </div>
              </div>
              <div className="disk_new_img_right">
                <div>
                  <input type="checkbox" className="shareable" />
                  <label htmlFor="shareable">공유 가능</label>
                </div>
              </div>
            </div>
          </div>
        )} */}
        <div className="edit_footer">
          {/* <button style={{ display: 'none' }}></button> */}
          <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
      </Modal>
  );
};

export default DiskModal;
