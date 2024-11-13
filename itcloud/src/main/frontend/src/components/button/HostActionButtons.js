import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faEllipsisV } from '@fortawesome/free-solid-svg-icons';

const HostActionButtons = ({
  onCreate,
  onEdit,
  onDelete,
  onManage,
  // onInstall,
  // toggleEllipsis,
  // isManageVisible,
  // isInstallVisible,
  // isEllipsisVisible,
  // handleManageClick,
  // handleInstallClick,
  // togglePopupMenu,
}) => {

  // // 토글 방식으로 열고 닫기(관리)
  // const [isManageBoxVisible, setIsManageBoxVisible] = useState(false);
  // // 설치 드롭다운 상태
  // const [isInstallBoxVisible, setIsInstallBoxVisible] = useState(false);
  // // 관리버튼
  // const handleManageClick = () => {
  //   setIsManageBoxVisible(!isManageBoxVisible);
  // };
  // // 설치 버튼 
  // const handleInstallClick = () => {
  //   setIsInstallBoxVisible(!isInstallBoxVisible);
  // };
  
  // useEffect(() => {
  //   const handleClickOutside = (event) => {
  //     const manageBox = document.getElementById('manage_hidden_box');
  //     const manageBtn = document.getElementById('manage_btn');
  //     const installBox = document.getElementById('install_hidden_box');
  //     const installBtn = document.getElementById('install_btn');
  //     const installContainer = document.getElementById('install_container');
  //     const ellipsisBox = document.getElementById('ellipsis_hidden_box');
  //     const ellipsisBtn = document.getElementById('ellipsis_btn');
      
  //     // 클릭한 요소가 각 팝업 내부의 li인지 확인
  //     const isLiElement = event.target.tagName === 'LI';

  //     // 관리, 설치, ... 버튼과 해당 요소 외부 클릭 시 팝업 닫기
  //     if (
  //       !(
  //         (manageBox && manageBox.contains(event.target)) ||
  //         (manageBtn && manageBtn.contains(event.target)) ||
  //         (installBox && installBox.contains(event.target)) ||
  //         (installBtn && installBtn.contains(event.target)) ||
  //         (installContainer && installContainer.contains(event.target)) ||
  //         (ellipsisBox && ellipsisBox.contains(event.target)) ||
  //         (ellipsisBtn && ellipsisBtn.contains(event.target)) ||
  //         isLiElement // li 요소 클릭 시 제외
  //       )
  //     ) {
  //       setIsManageBoxVisible(false);
  //       setIsInstallBoxVisible(false); // 설치 드롭다운 닫기
  //       setPopupOpen(false); // ellipsis 팝업 닫기
  //     }
  //   };
  //   document.addEventListener('mousedown', handleClickOutside);
  //   return () => {
  //     document.removeEventListener('mousedown', handleClickOutside);
  //   };
  // }, []);

  //   // ...버튼
  //   const [popupOpen, setPopupOpen] = useState(false);
  //   const togglePopupMenu = () => {
  //     setPopupOpen(!popupOpen);
  //   };

  return (
    <div className="header_right_btns">
      <button onClick={onCreate}>새로 만들기</button>
      <button onClick={onEdit}>편집</button>
      <button onClick={onDelete}>삭제</button>

      {/* <div className="manage_container">
        <button id="manage_btn" onClick={handleManageClick} className="btn">
          관리 <FontAwesomeIcon icon={faChevronDown} style={{ marginLeft: '3px' }} />
        </button>
        {isManageVisible && (
          <ul id="manage_hidden_box" className="dropdown-menu">
            <li onClick={() => onManage('maintenance')}>유지보수</li>
            <li>활성</li>
            <li>기능을 새로 고침</li>
            <li style={{ borderTop: '1px solid #DDDDDD' }}>다시 설치</li>
            <li style={{ borderBottom: '1px solid #DDDDDD' }}>인증서 등록</li>
            <li>재시작</li>
            <li>중지</li>
          </ul>
        )}
      </div>

      <div className="install_container">
        <button id="install_btn" onClick={handleInstallClick} className="btn">
          설치 <FontAwesomeIcon icon={faChevronDown} style={{ marginLeft: '3px' }} />
        </button>
        {isInstallVisible && (
          <ul id="install_hidden_box" className="dropdown-menu">
            <li>설치 옵션 1</li>
            <li>설치 옵션 2</li>
            <li style={{ borderTop: '1px solid #DDDDDD' }}>설치 옵션 3</li>
            <li style={{ borderBottom: '1px solid #DDDDDD' }}>설치 옵션 4</li>
          </ul>
        )}
      </div>
      <div className="ellipsis_container">
        <button id="ellipsis_btn" onClick={togglePopupMenu} className="btn">
          <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
        </button>
        {isEllipsisVisible && (
          <ul id="ellipsis_hidden_box" className="dropdown-menu">
            <li className="disabled">글로벌 HA 유지 관리를 활성화</li>
            <li>글로벌 HA 유지 관리를 비활성화</li>
          </ul>
        )}
      </div> */}
    </div>
  );
};

export default HostActionButtons;