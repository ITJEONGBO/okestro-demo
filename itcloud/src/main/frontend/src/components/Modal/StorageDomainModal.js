import React, { useState,useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faChevronCircleRight, faGlassWhiskey } from '@fortawesome/free-solid-svg-icons';
import Table from '../table/Table';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useAddDomain, useAllDataCenters, useDomainById, useEditDomain } from '../../api/RQHook';

const StorageDomainsModal = ({
  isOpen,
  onRequestClose,
  storageType,
  handleStorageTypeChange,
  editMode = false,
  domainId,
  isDomainHiddenBoxVisible,
  toggleDomainHiddenBox,
  isDomainHiddenBox2Visible,
  toggleDomainHiddenBox2,
}) => {
  const [id, setId] = useState('');
  const [datacenterVoId, setDatacenterVoId] = useState('');  
  const [name, setName] = useState('');
  const [comment, setComment] = useState('');
  const [description, setDescription] = useState('');
  const [activeLunTab, setActiveLunTab] = useState('target_lun');


  const [isDomainHiddenBox4Visible, setDomainHiddenBox4Visible] = useState(false);
  const toggleDomainHiddenBox4 = () => {
    setDomainHiddenBox4Visible(!isDomainHiddenBox4Visible);
  };
  const [isDomainHiddenBox5Visible, setDomainHiddenBox5Visible] = useState(false);
  const toggleDomainHiddenBox5 = () => {
    setDomainHiddenBox5Visible(!isDomainHiddenBox5Visible);
  };

  const handleLunTabClick = (tab) => {
    setActiveLunTab(tab); 
  };

  const { mutate: addDomain } = useAddDomain();
  const { mutate: editDomain } = useEditDomain();

// 도메인 데이터 가져오기
const {
  data: domain,
  status: domainStatus,
  isRefetching: isDomainRefetching,
  refetch: domainRefetch,
  isError: isDomainError,
  error: domainError,
  isLoading: isDomainLoading,
} = useDomainById(domainId);

// 데이터센터 가져오기
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


// 모달이 열릴 때 기존 데이터를 상태에 설정
useEffect(() => {
  if (isOpen) { // 모달이 열릴 때 상태를 설정
    if (editMode && domain) {
      console.log('Setting edit mode state with domain:', domain); // 디버깅 로그
      setId(domain.id);
      setDatacenterVoId(domain?.datacenterVo?.id || '');
      setName(domain.name);
      setDescription(domain.description);
      setComment(domain.comment);

    } else {
      console.log('Resetting form for create mode');
      resetForm();
    }
  }
}, [isOpen, editMode, domain, datacenters]);

const resetForm = () => {
  setName('');
  setDescription('');
  setComment('');

};

const handleFormSubmit = () => {
  const dataToSubmit = {
    name,
    description,
    comment,
    datacenterVoId
  };
  console.log('Data to submit:', dataToSubmit); // 데이터를 서버로 보내기 전에 확인

  if (editMode) {
    dataToSubmit.id = id; // 수정 모드에서는 id를 추가
    editDomain({
      domainId: id,
      domainData: dataToSubmit
    }, {
      onSuccess: () => {
        alert('도메인 편집 완료');
        onRequestClose();
      },
      onError: (error) => {
        console.error('Error editing domain:', error);
      }
    });
  } else {
    addDomain(dataToSubmit, {
      onSuccess: () => {
        alert('도메인 생성 완료');
        onRequestClose();
      },
      onError: (error) => {
        console.error('Error adding domain:', error);
      }
    });
  }
};



  // 팝업 테이블 컴포넌트
  const data = [
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer' }}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          he_metadata
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          디스크2
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
    {
      alias: (
        <span
          style={{ color: 'blue', cursor: 'pointer'}}
          onMouseEnter={(e) => (e.target.style.fontWeight = 'bold')}
          onMouseLeave={(e) => (e.target.style.fontWeight = 'normal')}
        >
          디스크3
        </span>
      ),
      id: '289137398279301798',
      icon1: '',
      icon2: <FontAwesomeIcon icon={faGlassWhiskey} fixedWidth/>,
      connectionTarget: 'on20-ap01',
      storageDomain: 'VirtIO-SCSI',
      virtualSize: '/dev/sda',
      status: 'OK',
      type: '이미지',
      description: '',
    },
  ];


  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel="도메인 관리"
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
   <div className="storage_domain_administer_popup">
          <div className="popup_header">
            <h1>도메인 생성</h1>
            <button onClick={onRequestClose}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
          </div>

          <div className="storage_domain_new_first">
                    <div className="domain_new_left">
                    <div className="domain_new_select">
                        <label htmlFor="data_hub_location">데이터 센터</label>
                        <select id="data_hub_location">
                        <option value="linux">Default(VS)</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="domain_feature_set">도메인 기능</label>
                        <select id="domain_feature_set">
                            <option value="data">데이터</option>
                            <option value="iso">ISO</option>
                            <option value="export">내보내기</option>
                        </select>
                    </div>
                    <div className="domain_new_select">
                        <label htmlFor="storage_option_type">스토리지 유형</label>
                        <select 
                        id="storage_option_type"
                        value={storageType}
                        onChange={handleStorageTypeChange} // 선택된 옵션에 따라 상태 변경
                        >
                        <option value="NFS">NFS</option>
                
                        <option value="iSCSI">iSCSI</option>
                        <option value="fc">파이버 채널</option>
                        </select>
                    </div>
                    <div className="domain_new_select" style={{ marginBottom: 0 }}>
                        <label htmlFor="host_identifier">호스트</label>
                        <select id="host_identifier">
                        <option value="linux">host02.ititinfo.com</option>
                        </select>
                    </div>
                    </div>
                    <div className="domain_new_right">
                    <div className="domain_new_select">
                        <label>이름</label>
                        <input
                          type="text"
                          id="name"
                          value={name}
                          onChange={(e) => setName(e.target.value)} 
                        />
                    </div>
                    <div className="domain_new_select">
                        <label>설명</label>
                        <input type="text" />
                    </div>
                    <div className="domain_new_select">
                        <label>코멘트</label>
                        <input type="text" />
                    </div>
                    </div>
                </div>

                {storageType === 'NFS' && (
                <div className="storage_popup_NFS">
                    <div className ="network_form_group">
                    <label htmlFor="data_hub">NFS 서버 경로</label>
                    <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                    </div>

                  
                   
                    <div id="domain_hidden_box">
                  
                        <div className="domain_new_select">
                        <label htmlFor="nfs_version">NFS 버전</label>
                        <select id="nfs_version">
                            <option value="host02_ititinfo_com">host02.ititinfo.com</option>
                        </select>
                        </div>
                        {/* <div className="domain_new_select">
                        <label htmlFor="data_hub">재전송</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">제한 시간(데시세컨드)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="data_hub">추가 마운트 옵션</label>
                        <input type="text" />
                        </div> */}
                    </div>
                
               
                   
                    <div id="domain_hidden_box2">
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>
                       

                    </div>
                   
                </div>
                )}

                

                {storageType === 'iSCSI' && (
                <div className="storage_popup_NFS">
                    <div className='target_btns flex'> 
                    <button 
                        className={`target_lun ${activeLunTab === 'target_lun' ? 'active' : ''}`}
                        onClick={() => handleLunTabClick('target_lun')}
                    >
                        대상 - LUN
                    </button>
                    <button 
                        className={`lun_target ${activeLunTab === 'lun_target' ? 'active' : ''}`}
                        onClick={() => handleLunTabClick('lun_target')}
                    > 
                        LUN - 대상
                    </button>
                    </div>


                
                    {activeLunTab === 'target_lun' &&(
                    <div className='target_lun_outer'>
                        <div className="search_target_outer">
                        <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn4" onClick={toggleDomainHiddenBox4}fixedWidth/>
                        <span>대상 검색</span>
                        <div id="domain_hidden_box4" style={{ display: isDomainHiddenBox4Visible ? 'block' : 'none' }}>
                            <div className="search_target ">

                            <div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                            </div>

                            <div>
                                <div className='input_checkbox'>
                                <input type="checkbox" id="reset_after_deletion"/>
                                <label htmlFor="reset_after_deletion">사용자 인증 :</label>
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                                <div className ="network_form_group">
                                <label htmlFor="data_hub">내보내기 경로</label>
                                <input type="text" placeholder="예:myserver.mydomain.com/my/local/path" />
                                </div>
                            </div>

                            
                            </div>
                            <button>검색</button>
                        </div>
                        </div>
                    

                        <div>
                        <button className='all_login'>전체 로그인</button>
                        <div className='section_table_outer'>
                            <Table
                            columns={TableColumnsInfo.CLUSTERS_ALT} 
                            data={data} 
                            onRowClick={[]}
                            shouldHighlight1stCol={true}
                            />
                        </div>
                        </div>
                    </div>
                    )}      

                    {activeLunTab === 'lun_target' && (
                    <div className='lun_target_outer'>
                        <div className='section_table_outer'>
                            <Table
                            columns={TableColumnsInfo.CLUSTERS_ALT} 
                            data={data} 
                            onRowClick={[]}
                            shouldHighlight1stCol={true}
                            />
                        </div>
                    </div>
                    )}
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>

                  
                    </div>
                    </div>

                </div>
                )}

                {storageType === 'fc' && (
                <div className="storage_popup_NFS">
                    <div className='section_table_outer'>
                        <Table
                        columns={TableColumnsInfo.CLUSTERS_ALT} 
                        data={data} 
                        onRowClick={[]}
                        shouldHighlight1stCol={true}
                        />
                    </div>
                    <div>
                    <FontAwesomeIcon icon={faChevronCircleRight} id="domain_hidden_box_btn5" onClick={toggleDomainHiddenBox5}fixedWidth/>
                    <span>고급 매개 변수</span>
                    <div id="domain_hidden_box5" style={{ display: isDomainHiddenBox5Visible ? 'block' : 'none' }}>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>심각히 부족한 디스크 공간의 동작 차단(GB)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label>디스크 공간 부족 경고 표시(%)</label>
                        <input type="text" />
                        </div>
                        <div className="domain_new_select">
                        <label htmlFor="format_type_selector" style={{ color: 'gray' }}>포맷</label>
                        <select id="format_type_selector" disabled>
                            <option value="linux">V5</option>
                        </select>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="reset_after_deletion"/>
                        <label htmlFor="reset_after_deletion">삭제 후 초기화</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">백업</label>
                        </div>
                        <div className="hidden_checkbox">
                        <input type="checkbox" id="backup_vault"/>
                        <label htmlFor="backup_vault">삭제 후 폐기</label>
                        </div>
                    </div>
                    </div>
                </div>
                )}

          <div className="edit_footer">
            <button style={{ display: 'none' }}></button>
            <button onClick={handleFormSubmit}>{editMode ? '편집' : '생성'}</button>
            <button onClick={onRequestClose}>취소</button>
          </div>
        </div>
    </Modal>
  );
};

export default StorageDomainsModal;
