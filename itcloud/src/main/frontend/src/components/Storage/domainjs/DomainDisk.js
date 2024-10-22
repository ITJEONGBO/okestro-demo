import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useAllClustersFromNetwork, useAllDiskFromDomain} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate} from 'react-router-dom';
import { useState,useEffect } from 'react'; 
import { faAngleDown, faChevronLeft } from "@fortawesome/free-solid-svg-icons";
import TableInfo from "../../table/TableInfo";

const DomainDisk = ({ domain }) => {

    // 모달 관련 상태 및 함수
    const [activePopup, setActivePopup] = useState(null);
    const openModal = (popupType) => setActivePopup(popupType);
    const closeModal = () => setActivePopup(null);

    // 옵션박스 열고닫기
    const [isUploadOptionBoxVisible, setUploadOptionBoxVisible] = useState(false);
    const toggleUploadOptionBox = () => {
        setUploadOptionBoxVisible(!isUploadOptionBoxVisible);
    };
    // 바탕클릭하면 옵션박스 닫기
    useEffect(() => {
      const handleClickOutside = (event) => {
        if (
          isUploadOptionBoxVisible &&
          !event.target.closest('.upload_option_box') &&
          !event.target.closest('.upload_option_boxbtn')
        ) {
          setUploadOptionBoxVisible(false);
        }
      };
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [isUploadOptionBoxVisible]);

  const { 
    data: disks, 
    status: disksStatus, 
    isLoading: isDisksLoading, 
    isError: isDisksError,
  } = useAllDiskFromDomain(domain?.id, toTableItemPredicateDisks);
  function toTableItemPredicateDisks(disk) {
    return {
      alias: disk?.alias ?? '없음',  // 별칭
      icon1: <FontAwesomeIcon icon={faChevronLeft} fixedWidth />, 
      icon2: <FontAwesomeIcon icon={faChevronLeft} fixedWidth />,
      virtualSize: disk?.virtualSize ? disk.virtualSize : '알 수 없음', 
      actualSize: disk?.actualSize ? disk.actualSize : '알 수 없음',
      allocationPolicy: disk?.allocationPolicy ?? '알 수 없음', 
      storageDomain: disk?.storageDomain ?? '없음',  
      createDate: disk?.createDate ?? '알 수 없음', 
      lastUpdate: disk?.lastUpdate ?? '알 수 없음',
      icon3: <FontAwesomeIcon icon={faChevronLeft} fixedWidth />, 
      connectionTarget: disk?.connectionTarget ?? '없음',
      status: disk?.status ?? '알 수 없음',
      type: disk?.type ?? '알 수 없음', 
      description: disk?.description ?? '없음', 
    };
  }
    return (
        <>
        <div className="header_right_btns">
            <button  onClick={() => openModal('move')}>이동</button>
            <button  onClick={() => openModal('copy')}>복사</button>
            <button  onClick={() => openModal('delete')}>제거</button>
            <button className='upload_option_boxbtn'>업로드 
              <FontAwesomeIcon class={faAngleDown} onClick={toggleUploadOptionBox}fixedWidth/>
            </button>
            <button>다운로드</button>
            {/*업로드 버튼 옵션박스 */}
            {isUploadOptionBoxVisible &&(
            <div className='upload_option_box'>
              <div>시작</div>
              <div>취소</div>
              <div>일시정지</div>
              <div>다시시작</div>
            </div>
            )}
        </div>
        
        <TableOuter 
          columns={TableInfo.DISKS_FROM_STORAGE_DOMAIN}
          data={disks}
          onContextMenuItems={() => [
            <div key="이동" onClick={() => console.log()}>이동</div>,
            <div key="복사" onClick={() => console.log()}>복사</div>,
            <div key="제거" onClick={() => console.log()}>제거</div>,
            <div key="업로드" onClick={() => console.log()}>업로드</div>,
            <div key="다운로드" onClick={() => console.log()}>다운로드</div>
          ]}
        />
   </>
    );
  };
  
  export default DomainDisk;