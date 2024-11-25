import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import './css/MDomain.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import { 
  useDiskById,
  useAddDisk,
  useEditDisk, 
  useAllDataCenters,
  useDomainsFromDataCenter, 
  useAllDiskProfileFromDomain
} from '../../api/RQHook';

const DiskModal = ({
  isOpen,
  onRequestClose,
  editMode = false,
  diskId,
  vmId
}) => {
  const [id, setId] = useState('');
  const [size, setSize] = useState('');
  const [appendSize, setAppendSize] = useState('');
  const [alias, setAlias] = useState('');
  const [description, setDescription] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [domainVoId, setDomainVoId] = useState('');
  const [storageType, setStorageType] = useState(''); // 씬
  const [diskProfileVoId, setDiskProfileVoId] = useState('');
  const [wipeAfterDelete, setWipeAfterDelete] = useState('');
  const [sharable, setSharable] = useState('');
  const [backup, setBackup] = useState('');
  const [sparse, setSparse] = useState('');
  
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
  } = useDomainsFromDataCenter(datacenterVoId);

  const {
    data: diskProfiles,
    status: diskProfilesStatus,
    isRefetching: isDiskProfilesRefetching,
    refetch: diskProfilesRefetch,
    isError: isDiskProfilesError,
    error: diskProfilesError,
    isLoading: isDiskProfilesLoading,
  } = useAllDiskProfileFromDomain(domainVoId);

  useEffect(() => {
    if (isOpen) {
      if (editMode && disk) {
        setId(disk?.id);
        setSize(disk?.size);
        setAppendSize(disk?.appendSize);
        setAlias(disk?.alias);
        setDescription(disk?.description);
        setDatacenterVoId(disk?.dataCenterVo.id);
        setDomainVoId(disk?.storageDomainVo.id);
        setStorageType(disk?.storageType);
        setDiskProfileVoId(disk?.diskProfileVo.id);
        setWipeAfterDelete(disk?.wipeAfterDelete);
        setSharable(disk?.sharable);
        setBackup(disk?.backup);
        setSparse(disk?.sparse);
      } else if (!editMode && !isDatacentersLoading) {
        resetForm();
        
        if (datacenters && datacenters.length > 0) {
          setDatacenterVoId(datacenters[0].id);
        }
      }
    }
  }, [editMode, disk, datacenters, datacenterVoId]);

  useEffect(() => {
    if (!datacenterVoId && datacenters?.length > 0) {
      setDatacenterVoId(datacenters[0].id); // 첫 번째 데이터센터를 기본값으로 설정
    }
  }, [datacenters]);

  useEffect(() => {
    if (!editMode && datacenterVoId) {
      domainsRefetch({ datacenterVoId }).then((res) => {
        if (res?.data && res.data.length > 0) {
          setDomainVoId(res.data[0].id); // 첫 번째 호스트를 기본값으로 설정
        }
      }).catch((error) => {
        console.error('Error fetching disks:', error);
      });
    }
  }, [editMode, datacenterVoId]);

  const resetForm = () => {
    setSize('');
    setAlias('');
    setDescription('');
    setDatacenterVoId('');
    setDomainVoId('');
    setStorageType('');
    setDiskProfileVoId('');
    setWipeAfterDelete('');
    setSharable('');
    setBackup('');
    setSparse('');
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
      storageType,
      diskProfileVo:{
        id: selectedDiskProfile.id,
        name: selectedDiskProfile.name,
      },
      wipeAfterDelete,
      sharable,
      backup,
      sparse
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
          <div></div>
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
          <div className="disk_new_img">
            <div className="disk_new_img_left">

              <div className="img_input_box">
                <label htmlFor="size">크기(GB)</label>
                <input
                  type="number"
                  id="size"
                  value={size}
                  onChange={(e) => setSize(e.target.value)}
                />
              </div>

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
                <label htmlFor="os">데이터 센터</label>
                <select
                  id="data_center"
                  value={datacenterVoId}
                  onChange={(e) => setDatacenterVoId(e.target.value)}
                  disabled={editMode}
                >
                  {datacenters &&
                    datacenters.map((dc) => (
                      <option key={dc.id} value={dc.id}>
                        {dc.name}
                      </option>
                    ))
                  }
                </select>
                <span>{datacenterVoId}</span>
              </div>

              <div className="img_select_box">
                <label htmlFor="os">스토리지 도메인</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
              <div className="img_select_box">
                <label htmlFor="storageType">할당 정책</label>
                <select id="storageType">
                  <option value="true">씬 프로비저닝</option>
                  <option value="false">사전 할당</option>
                </select>
              </div>
              <div className="img_select_box">
                <label htmlFor="os">디스크 프로파일</label>
                <select id="os">
                  <option value="linux">Linux</option>
                </select>
              </div>
            </div>

            <div className="disk_new_img_right">
              <div>
                <input type="checkbox" id="reset_after_deletion" />
                <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
              </div>
              <div>
                <input type="checkbox" className="shareable" />
                <label htmlFor="shareable">공유 가능</label>
              </div>
              <div>
                <input type="checkbox" id="incremental_backup" defaultChecked />
                <label htmlFor="incremental_backup">중복 백업 사용</label>
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
