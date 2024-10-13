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
              <div>가져오기</div>
              <div>가상 머시 복제</div>
              <div>삭제</div>
              <div>마이그레이션 취소</div>
              <div>변환 취소</div>
              <div>템플릿 취소</div>
              <div>템플릿 생성</div>
              <div>내보내기 도메인으로 내보내기</div>
              <div>Export to Data Domain</div>
              <div>OVA로 내보내기</div>
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
