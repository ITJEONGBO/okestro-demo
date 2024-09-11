import { faCamera, faChevronRight, faEye, faNewspaper, faServer, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


// 스냅샷
const SnapshotSection = () => {
    return (
      <>
          <div className="content_header_right">
            <button className="snap_create_btn">생성</button>
            <button>미리보기</button>
            <button>커밋</button>
            <button>되돌리기</button>
            <button>삭제</button>
            <button>복제</button>
            <button>템플릿 생성</button>
          </div>
          <div className="snapshot_content">
            <div className="snapshot_content_left">
              <div><FontAwesomeIcon icon={faCamera} fixedWidth/></div>
              <span>Active VM</span>
            </div>
            <div className="snapshot_content_right">
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>일반</span>
                <FontAwesomeIcon icon={faEye} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>디스크</span>
                <FontAwesomeIcon icon={faTrash} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>네트워크 인터페이스</span>
                <FontAwesomeIcon icon={faServer} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>설치된 애플리케이션</span>
                <FontAwesomeIcon icon={faNewspaper} fixedWidth/>
              </div>
            </div>
          </div>
          <div className="snapshot_content">
            <div className="snapshot_content_left">
              <div><FontAwesomeIcon icon={faCamera} fixedWidth/></div>
              <span>Active VM</span>
            </div>
            <div className="snapshot_content_right">
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>일반</span>
                <FontAwesomeIcon icon={faEye} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>디스크</span>
                <FontAwesomeIcon icon={faTrash} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>네트워크 인터페이스</span>
                <FontAwesomeIcon icon={faServer} fixedWidth/>
              </div>
              <div>
                <FontAwesomeIcon icon={faChevronRight} fixedWidth/>
                <span>설치된 애플리케이션</span>
                <FontAwesomeIcon icon={faNewspaper} fixedWidth/>
              </div>
            </div>
          </div>
        
      </>
    );
  };

  export default SnapshotSection;