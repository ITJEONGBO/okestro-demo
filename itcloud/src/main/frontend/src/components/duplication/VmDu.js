import React from 'react';
import TableOuter from '../table/TableOuter';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEllipsisV } from '@fortawesome/free-solid-svg-icons';

const VmDu = ({ data, columns, handleRowClick, openPopup, setActiveTab, togglePopup, isPopupOpen }) => {
  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => openPopup('vm_new')}>새로만들기</button>
        <button onClick={() => openPopup('vm_edit')}>편집</button>
        <button className="disabled">실행</button>
        <button className="disabled">일시중지</button>
        <button className="disabled">종료</button>
        <button className="disabled">재부팅</button>
        <button onClick={() => setActiveTab('template')}>템플릿</button>
        <button>콘솔</button>
        <button>스냅샷 생성</button>
        <button className="disabled">마이그레이션</button>
        <button className="content_header_popup_btn" onClick={togglePopup}>
          <FontAwesomeIcon icon={faEllipsisV} fixedWidth />
          {isPopupOpen && (
            <div className="content_header_popup">
              <div>OVF 업데이트</div>
              <div>파괴</div>
              <div>디스크 검사</div>
              <div>마스터 스토리지 도메인으로 선택</div>
            </div>
          )}
        </button>
      </div>

      <TableOuter 
        columns={columns}
        data={data}
        onRowClick={handleRowClick}
        showSearchBox={true}
      />
    </>
  );
};

export default VmDu;
