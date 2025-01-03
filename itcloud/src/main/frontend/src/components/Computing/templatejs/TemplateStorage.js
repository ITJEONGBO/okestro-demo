import React, { useState } from 'react';
import { faMinusCircle, faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAllStoragesFromTemplate } from '../../../api/RQHook';

const TemplateStorage = ({ templateId }) => {
  const [isRowExpanded, setRowExpanded] = useState({});

  const toggleRow = (id) => {
    setRowExpanded((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const { 
    data: storages = [], // 기본값 설정
    isLoading, 
    isError,
  } = useAllStoragesFromTemplate(templateId, toTableItemPredicateStorages);

  function toTableItemPredicateStorages(storage) {
    return {
      id: storage.id ?? '',
      name: storage.name || 'Unnamed Storage',
      status: storage.active ? '활성화' : '비활성화',
      domainType: storage.domainType || 'Unknown',
      usedSize: (storage.usedSize / (1024 ** 3)).toFixed(2),   // 사용된 공간 GiB 변환
      availableSize: (storage.availableSize / (1024 ** 3)).toFixed(2), // 가용 공간 GiB 변환
      totalSize: ((storage.availableSize + storage.usedSize) / (1024 ** 3)).toFixed(2), // 총 공간 계산
      format: storage.format || 'Unknown',
      storageType: storage.storageType || 'Unknown',
    };
  }

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error loading storage data.</div>;

  return (
    <div className="host_empty_outer">
        <div className="header_right_btns">
            <button >삭제</button>
        </div>
      <div className="section_table_outer">
        <table>
          <thead>
            <tr>
              <th>스토리지 이름</th>
              <th>도메인 유형</th>
              <th>상태</th>
              <th>가용 공간 (GB)</th>
              <th>사용된 공간 (GB)</th>
              <th>전체 공간 (GB)</th>
            </tr>
          </thead>
          <tbody>
            {storages.map((storage) => (
              <React.Fragment key={storage.id}>
                <tr>
                  <td onClick={() => toggleRow(storage.id)} style={{ cursor: 'pointer' }}>
                    <FontAwesomeIcon icon={isRowExpanded[storage.id] ? faMinusCircle : faPlusCircle} fixedWidth />
                    {storage.name}
                  </td>
                  <td>{storage.domainType}</td>
                  <td>{storage.status}</td>
                  <td>{storage.availableSize} GiB</td>
                  <td>{storage.usedSize} GiB</td>
                  <td>{storage.totalSize} GiB</td>
                </tr>

                {/* 하위 스토리지 상세 정보 */}
                {isRowExpanded[storage.id] && (
                  <tr className="detail_machine_second">
                    <td colSpan="6" style={{ paddingLeft: '30px' }}>
                      <table>
                        <thead>
                          <tr>
                            <th>포맷</th>
                            <th>스토리지 유형</th>
                            <th>크기</th>
                            <th>상태</th>
                            <th>할당</th>
                            <th>인터페이스</th>
                            <th>유형</th>
                            <th>생성일자</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>{storage.format}</td>
                            <td>{storage.storageType}</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                          </tr>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TemplateStorage;
