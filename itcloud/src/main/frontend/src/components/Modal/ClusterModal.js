import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTimes, faInfoCircle } from '@fortawesome/free-solid-svg-icons';

const ClusterModal = ({ 
  isOpen, 
  onRequestClose, 
  onSubmit, 
  editMode = false, 
  clusterData = {} 
}) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [comment, setComment] = useState('');
  const [selectedPopupTab, setSelectedPopupTab] = useState('cluster_common_btn');
  const [showTooltip, setShowTooltip] = useState(false);

  // 편집 모드일 때 기존 데이터를 불러와서 입력 필드에 채움
  useEffect(() => {
    if (editMode && clusterData) {
      setName(clusterData.name || '');
      setDescription(clusterData.description || '');
      setComment(clusterData.comment || '');
    }
  }, [editMode, clusterData]);

  // 폼 제출 핸들러
  const handleFormSubmit = () => {
    const requestData = {
      name,
      description,
      comment,
    };
    onSubmit(requestData); // 부모 컴포넌트로 데이터를 전달
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      contentLabel={editMode ? '클러스터 편집' : '새 클러스터'}
      className="Modal"
      overlayClassName="Overlay"
      shouldCloseOnOverlayClick={false}
    >
      <div className="cluster_new_popup">
        <div className="popup_header">
          <h1>{editMode ? '클러스터 편집' : '새 클러스터'}</h1>
          <button onClick={onRequestClose}>
            <FontAwesomeIcon icon={faTimes} fixedWidth />
          </button>
        </div>

        <div className="flex">
          <div className="network_new_nav">
            <div
              id="cluster_common_btn"
              className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => setSelectedPopupTab('cluster_common_btn')}
            >
              일반
            </div>
            <div
              id="cluster_migration_btn"
              className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
              onClick={() => setSelectedPopupTab('cluster_migration_btn')}
            >
              마이그레이션 정책
            </div>
          </div>

          {/* 일반 탭 */}
          {selectedPopupTab === 'cluster_common_btn' && (
            <form className="cluster_common_form py-1">
              <div className="network_form_group">
                <label htmlFor="data_center">데이터 센터</label>
                <select id="data_center">
                  <option value="default">Default</option>
                </select>
              </div>

              <div className="network_form_group">
                <div>
                  <label htmlFor="name">이름</label>
                </div>
                <input
                  type="text"
                  id="name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>

              <div className="network_form_group">
                <label htmlFor="description">설명</label>
                <input
                  type="text"
                  id="description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="network_form_group">
                <label htmlFor="comment">코멘트</label>
                <input
                  type="text"
                  id="comment"
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                />
              </div>
              {/* 나머지 폼 필드 생략 */}
            </form>
          )}

          {/* 마이그레이션 정책 탭 */}
          {selectedPopupTab === 'cluster_migration_btn' && (
            <form className="py-2">
              <div className="network_form_group">
                <label htmlFor="migration_policy">마이그레이션 정책</label>
                <select id="migration_policy">
                  <option value="default">Default</option>
                </select>
              </div>

              <div className="p-1.5">
                <span className="font-bold">최소 다운타임</span>
                <div>
                  일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다.
                </div>
              </div>

              <div className="p-1.5 mb-1">
                <span className="font-bold">대역폭</span>
                <div className="cluster_select_box">
                  <div className="flex">
                    <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
                    <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
                  </div>
                  <select id="bandwidth_policy">
                    <option value="default">Default</option>
                  </select>
                </div>
              </div>

              {/* 나머지 마이그레이션 필드 생략 */}
            </form>
          )}
        </div>

        <div className="edit_footer">
          <button style={{ display: 'none' }}></button>
          <button onClick={handleFormSubmit}>OK</button>
          <button onClick={onRequestClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default ClusterModal;


{/* 클러스터 새로 만들기 팝업 */}
{/* <Modal
isOpen={activePopup === 'cluster_new'}
onRequestClose={closePopup}
contentLabel="새로 만들기"
className="Modal"
overlayClassName="Overlay"
shouldCloseOnOverlayClick={false}
>
<div className="cluster_new_popup">
    <div className="popup_header">
        <h1 class="text-sm">새 클러스터</h1>
        <button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
    </div>

    <div className='flex'>
<div className="network_new_nav">

<div
    id="cluster_common_btn"
    className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
    onClick={() => setSelectedPopupTab('cluster_common_btn')}  // 여기서 상태를 업데이트
>
    일반
</div>
<div
    id="cluster_migration_btn"
    className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
    onClick={() => setSelectedPopupTab('cluster_migration_btn')}  // 상태 업데이트
>
마이그레이션 정책
</div>
</div>
*/}

{/* 일반 */}
// {selectedPopupTab === 'cluster_common_btn' && (
// <form className="cluster_common_form py-1">
//     <div className="network_form_group">
//     <label htmlFor="data_center">데이터 센터</label>
//     <select id="data_center">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <div>
//         <label htmlFor="name">이름</label>
//     </div>
//     <input type="text" id="name" />
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="description">설명</label>
//     <input type="text" id="description" />
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="comment">코멘트</label>
//     <input type="text" id="comment" />
//     </div>

//     {/* id 편집 */}
//     <div className="network_form_group">
//     <label htmlFor="management_network">관리 네트워크</label>
//     <select id="management_network">
//         <option value="ovirtmgmt">ovirtmgmt</option>
//         <option value="ddd">ddd</option>
//         <option value="hosted_engine">hosted_engine</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="cpu_architecture">CPU 아키텍처</label>
//     <select id="cpu_architecture">
//         <option value="정의되지 않음">정의되지 않음</option>
//         <option value="x86_64">x86_64</option>
//         <option value="ppc64">ppc64</option>
//         <option value="s390x">s390x</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="cpu_type">CPU 유형</label>
//     <select id="cpu_type">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="chipset_firmware_type">침셋/펌웨어 유형</label>
//     <select id="chipset_firmware_type">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="bios_change" name="bios_change" />
//     <label htmlFor="bios_change">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="fips_mode">FIPS 모드</label>
//     <select id="fips_mode">
//         <option value="자동 감지">자동 감지</option>
//         <option value="비활성화됨">비활성화됨</option>
//         <option value="활성화됨">활성화됨</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="compatibility_version">호환 버전</label>
//     <select id="compatibility_version">
//         <option value="4.7">4.7</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="switch_type">스위치 유형</label>
//     <select id="switch_type">
//         <option value="Linux Bridge">Linux Bridge</option>
//         <option value="OVS (기술 프리뷰)">OVS (기술 프리뷰)</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="firewall_type">방화벽 유형</label>
//     <select id="firewall_type">
//         <option value="iptables">iptables</option>
//         <option value="firewalld">firewalld</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="default_network_provider">기본 네트워크 공급자</label>
//     <select id="default_network_provider">
//         <option value="기본 공급자가 없습니다.">기본 공급자가 없습니다.</option>
//         <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="max_memory_limit">로그인 최대 메모리 한계</label>
//     <select id="max_memory_limit">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="virt_service_enabled" name="virt_service_enabled" />
//     <label htmlFor="virt_service_enabled">Virt 서비스 활성화</label>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="gluster_service_enabled" name="gluster_service_enabled" />
//     <label htmlFor="gluster_service_enabled">Gluster 서비스 활성화</label>
//     </div>

//     <div className="network_checkbox_type2">
//     <span>추가 난수 생성기 소스:</span>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="dev_hwrng_source" name="dev_hwrng_source" />
//     <label htmlFor="dev_hwrng_source">/dev/hwrng 소스</label>
//     </div>
// </form>

// )}

{/* 마이그레이션 정책 */}
// {selectedPopupTab === 'cluster_migration_btn' && (
// <form className="py-2">
//     <div className="network_form_group">
//     <label htmlFor="migration_policy">마이그레이션 정책</label>
//     <select id="migration_policy">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div class="p-1.5">
//     <span class="font-bold">최소 다운타임</span>
//     <div>
//         일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.
//     </div>
//     </div>

//     <div class="p-1.5 mb-1">
//     <span class="font-bold">대역폭</span>
//     <div className="cluster_select_box">
//         <div class="flex">
//         <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
//         <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
//         </div>
//         <select id="bandwidth_policy">
//         <option value="default">Default</option>
//         </select>
//     </div>
//     </div>

//     <div className="px-1.5 flex relative">
//     <span className="font-bold">복구정책</span>
//     <FontAwesomeIcon
//         icon={faInfoCircle}
//         style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }}
//         onMouseEnter={() => setShowTooltip(true)} // 마우스를 올리면 툴팁을 보여줌
//         onMouseLeave={() => setShowTooltip(false)} // 마우스를 떼면 툴팁을 숨김
//     />
//     {showTooltip && (
//         <div className="tooltip-box">
//         마이그레이션 암호화에 대한 설명입니다.
//         </div>
//     )}
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5'>
//     <input type="radio" id="password_option" name="encryption_option" />
//     <label htmlFor="password_option">암호</label>
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5'>
//     <input type="radio" id="certificate_option" name="encryption_option" />
//     <label htmlFor="certificate_option">암호</label>
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5 mb-2'>
//     <input type="radio" id="none_option" name="encryption_option" />
//     <label htmlFor="none_option">암호</label>
//     </div>

//     <div class="m-1.5">
//     <span class="font-bold">추가 속성</span>
//     <div className="cluster_select_box">
//         <label htmlFor="encryption_usage">마이그레이션 암호화 사용</label>
//         <select id="encryption_usage">
//         <option value="default">시스템 기본값 (암호화하지 마십시오)</option>
//         <option value="encrypt">암호화</option>
//         <option value="no_encrypt">암호화하지 마십시오</option>
//         </select>
//     </div>
    
//     <div className="cluster_select_box">
//         <label htmlFor="parallel_migration">마이그레이션 암호화 사용</label>
//         <select id="parallel_migration">
//         <option value="default">Disabled</option>
//         <option value="auto">Auto</option>
//         <option value="auto_parallel">Auto Parallel</option>
//         <option value="custom">Custom</option>
//         </select>
//     </div>

//     <div className="cluster_select_box">
//         <label htmlFor="migration_encryption_text">마이그레이션 암호화 사용</label>
//         <input type="text" id="migration_encryption_text" />
//     </div>
//     </div>
// </form>

// )}
// </div>   

//     <div className="edit_footer">
//         <button style={{ display: 'none' }}></button>
//         <button>OK</button>
//         <button onClick={closePopup}>취소</button>
//     </div>
// </div>
// </Modal>
{/*클러스터(편집) 팝업 */}
{/* <Modal
isOpen={activePopup === 'cluster_detail_edit'}
onRequestClose={closePopup}
contentLabel="새로 만들기"
className="Modal"
overlayClassName="Overlay"
shouldCloseOnOverlayClick={false}
>
<div className="cluster_new_popup">
<div className="popup_header">
<h1>클러스터 수정</h1>
<button onClick={closePopup}><FontAwesomeIcon icon={faTimes} fixedWidth/></button>
</div>

<div className='flex'>
<div className="network_new_nav">

<div
    id="cluster_common_btn"
    className={selectedPopupTab === 'cluster_common_btn' ? 'active-tab' : 'inactive-tab'}
    onClick={() => setSelectedPopupTab('cluster_common_btn')}  // 여기서 상태를 업데이트
>
    일반
</div>
<div
    id="cluster_migration_btn"
    className={selectedPopupTab === 'cluster_migration_btn' ? 'active-tab' : 'inactive-tab'}
    onClick={() => setSelectedPopupTab('cluster_migration_btn')}  // 상태 업데이트
>
마이그레이션 정책
</div>
</div> */}

{/* 일반 */}
// {selectedPopupTab === 'cluster_common_btn' && (
// <form className="cluster_common_form py-1">
//     <div className="network_form_group">
//     <label htmlFor="data_center">데이터 센터</label>
//     <select id="data_center">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <div>
//         <label htmlFor="name">이름</label>
//     </div>
//     <input type="text" id="name" />
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="description">설명</label>
//     <input type="text" id="description" />
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="comment">코멘트</label>
//     <input type="text" id="comment" />
//     </div>

//     {/* id 편집 */}
//     <div className="network_form_group">
//     <label htmlFor="management_network">관리 네트워크</label>
//     <select id="management_network">
//         <option value="ovirtmgmt">ovirtmgmt</option>
//         <option value="ddd">ddd</option>
//         <option value="hosted_engine">hosted_engine</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="cpu_architecture">CPU 아키텍처</label>
//     <select id="cpu_architecture">
//         <option value="정의되지 않음">정의되지 않음</option>
//         <option value="x86_64">x86_64</option>
//         <option value="ppc64">ppc64</option>
//         <option value="s390x">s390x</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="cpu_type">CPU 유형</label>
//     <select id="cpu_type">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="chipset_firmware_type">침셋/펌웨어 유형</label>
//     <select id="chipset_firmware_type">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="bios_change" name="bios_change" />
//     <label htmlFor="bios_change">BIOS를 사용하여 기존 가상 머신/템플릿을 1440fx에서 Q35 칩셋으로 변경</label>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="fips_mode">FIPS 모드</label>
//     <select id="fips_mode">
//         <option value="자동 감지">자동 감지</option>
//         <option value="비활성화됨">비활성화됨</option>
//         <option value="활성화됨">활성화됨</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="compatibility_version">호환 버전</label>
//     <select id="compatibility_version">
//         <option value="4.7">4.7</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="switch_type">스위치 유형</label>
//     <select id="switch_type">
//         <option value="Linux Bridge">Linux Bridge</option>
//         <option value="OVS (기술 프리뷰)">OVS (기술 프리뷰)</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="firewall_type">방화벽 유형</label>
//     <select id="firewall_type">
//         <option value="iptables">iptables</option>
//         <option value="firewalld">firewalld</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="default_network_provider">기본 네트워크 공급자</label>
//     <select id="default_network_provider">
//         <option value="기본 공급자가 없습니다.">기본 공급자가 없습니다.</option>
//         <option value="ovirt-provider-ovn">ovirt-provider-ovn</option>
//     </select>
//     </div>

//     <div className="network_form_group">
//     <label htmlFor="max_memory_limit">로그인 최대 메모리 한계</label>
//     <select id="max_memory_limit">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="virt_service_enabled" name="virt_service_enabled" />
//     <label htmlFor="virt_service_enabled">Virt 서비스 활성화</label>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="gluster_service_enabled" name="gluster_service_enabled" />
//     <label htmlFor="gluster_service_enabled">Gluster 서비스 활성화</label>
//     </div>

//     <div className="network_checkbox_type2">
//     <span>추가 난수 생성기 소스:</span>
//     </div>

//     <div className="network_checkbox_type2">
//     <input type="checkbox" id="dev_hwrng_source" name="dev_hwrng_source" />
//     <label htmlFor="dev_hwrng_source">/dev/hwrng 소스</label>
//     </div>
// </form>

// )}

{/* 마이그레이션 정책 */}
// {selectedPopupTab === 'cluster_migration_btn' && (
// <form className="py-2">
//     <div className="network_form_group">
//     <label htmlFor="migration_policy">마이그레이션 정책</label>
//     <select id="migration_policy">
//         <option value="default">Default</option>
//     </select>
//     </div>

//     <div class="p-1.5">
//     <span class="font-bold">최소 다운타임</span>
//     <div>
//         일반적인 상황에서 가상 머신을 마이그레이션할 수 있는 정책입니다. 가상 머신에 심각한 다운타임이 발생하면 안 됩니다. 가상 머신 마이그레이션이 오랫동안 수렴되지 않으면 마이그레이션이 중단됩니다. 게스트 에이전트 후크 메커니즘을 사용할 수 있습니다.
//     </div>
//     </div>

//     <div class="p-1.5 mb-1">
//     <span class="font-bold">대역폭</span>
//     <div className="cluster_select_box">
//         <div class="flex">
//         <label htmlFor="bandwidth_policy">마이그레이션 정책</label>
//         <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }} />
//         </div>
//         <select id="bandwidth_policy">
//         <option value="default">Default</option>
//         </select>
//     </div>
//     </div>

//     <div className="px-1.5 flex relative">
//     <span className="font-bold">복구정책</span>
//     <FontAwesomeIcon
//         icon={faInfoCircle}
//         style={{ color: 'blue', margin: '0.1rem', cursor: 'pointer' }}
//         onMouseEnter={() => setShowTooltip(true)} // 마우스를 올리면 툴팁을 보여줌
//         onMouseLeave={() => setShowTooltip(false)} // 마우스를 떼면 툴팁을 숨김
//     />
//     {showTooltip && (
//         <div className="tooltip-box">
//         마이그레이션 암호화에 대한 설명입니다.
//         </div>
//     )}
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5'>
//     <input type="radio" id="password_option" name="encryption_option" />
//     <label htmlFor="password_option">암호</label>
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5'>
//     <input type="radio" id="certificate_option" name="encryption_option" />
//     <label htmlFor="certificate_option">암호</label>
//     </div>

//     <div className='host_text_radio_box px-1.5 py-0.5 mb-2'>
//     <input type="radio" id="none_option" name="encryption_option" />
//     <label htmlFor="none_option">암호</label>
//     </div>

//     <div class="m-1.5">
//     <span class="font-bold">추가 속성</span>
//     <div className="cluster_select_box">
//         <label htmlFor="encryption_usage">마이그레이션 암호화 사용</label>
//         <select id="encryption_usage">
//         <option value="default">시스템 기본값 (암호화하지 마십시오)</option>
//         <option value="encrypt">암호화</option>
//         <option value="no_encrypt">암호화하지 마십시오</option>
//         </select>
//     </div>
    
//     <div className="cluster_select_box">
//         <label htmlFor="parallel_migration">마이그레이션 암호화 사용</label>
//         <select id="parallel_migration">
//         <option value="default">Disabled</option>
//         <option value="auto">Auto</option>
//         <option value="auto_parallel">Auto Parallel</option>
//         <option value="custom">Custom</option>
//         </select>
//     </div>

//     <div className="cluster_select_box">
//         <label htmlFor="migration_encryption_text">마이그레이션 암호화 사용</label>
//         <input type="text" id="migration_encryption_text" />
//     </div>
//     </div>
// </form>

// )}
// </div>

{/* <div className="edit_footer">
<button style={{ display: 'none' }}></button>
<button>OK</button>
<button onClick={closePopup}>취소</button>
</div>
</div>
</Modal> */}