import React, { useState } from 'react';
import DomainActionButtons from '../button/DomainActionButtons';
import DomainTable from '../table/DomainTable';
import DomainModals from '../Modal/DomainModals';

const DomainDupl = ({ 
  domains, 
  columns,
  type,  // 데이터센터, 스토리지 도메인
  onFetchDomains, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedDomain, setSelectedDomain] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  // 도메인 생성, 도메인 가져오기, 도메인 관리(편집), 삭제, connection, lun 새로고침, 파괴, 마스터 스토리지 도메인으로 선택
  // 데이터센터: 연결, 분리, 활성, 유지보수
  return (
    <>
    { type == 'domain' && 
      <DomainActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedDomain?.id && handleActionClick('edit')}
        onDelete={() => selectedDomain?.id && handleActionClick('delete')}
        isEditDisabled={!selectedDomain?.id}
        status={selectedDomain?.status}
      />
    } 
    { type == 'datacenter' &&
      <DomainActionButtons
        onAttach={() => handleActionClick('attach')}
        onSeparate={() => selectedDomain?.id && handleActionClick('separate')}
        onActive={() => selectedDomain?.id && handleActionClick('active')}
        onMaintenance={() => selectedDomain?.id && handleActionClick('maintenance')}
        status={selectedDomain?.status}
      />
    }
      <span>id = {selectedDomain?.id || ''}</span>

      <DomainTable
        columns={columns}
        domains={domains}
        selectedDomain={selectedDomain}
        setSelectedDomain={setSelectedDomain}
      />

      <DomainModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDomain={selectedDomain}
      />
    </>
  );
};

export default DomainDupl;
