import React, { useState,  useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAllTemplates } from "../../api/RQHook";

import Table from "../table/Table";
import TableColumnsInfo from "../table/TableColumnsInfo";


// 애플리케이션 섹션
const Templates = () => {
  
    const navigate = useNavigate();

    const handleRowClick = (row, column) => {
        if (column.accessor === 'name') {
          navigate(`/computing/templates/${row.id}`); // 해당 이름을 URL로 전달하며 HostDetail로 이동합니다.
        }
      };
    const { 
        data: templates, 
        status: templatesStatus,
        isRefetching: isTemplatesRefetching,
        refetch: refetchTemplates, 
        isError: isTemplatesError, 
        error: templatesError, 
        isLoading: isTemplatesLoading,
      } = useAllTemplates(toTableItemPredicateTemplates);
      
      function toTableItemPredicateTemplates(template) {
        return {
          id: template?.id ?? '',
          name: template?.name ?? 'Unknown', 
          status: template?.status ?? 'Unknown',                // 템플릿 상태
          version: template?.version ?? 'N/A',                  // 템플릿 버전 정보
          description: template?.description ?? 'No description',// 템플릿 설명
          cpuType: template?.cpuType ?? 'CPU 정보 없음',         // CPU 유형 정보
          hostCount: template?.hostCount ?? 0,                  // 템플릿에 연결된 호스트 수
          vmCount: template?.vmCount ?? 0,                      // 템플릿에 연결된 VM 수
        };
      }
      
    return (
        <>
        <Table
        columns={TableColumnsInfo.TEMPLATE_CHART} 
        data={templates} onRowClick={handleRowClick} 
        className='template_chart'
        clickableColumnIndex={[0]} 
        />
        </>   
 
          
    );
  };
  
  export default Templates;