import React from 'react';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useAllVMs, useAllVmsFromTemplate } from '../../../api/RQHook';
import TablesOuter from '../../../components/table/TablesOuter';

const TemplateVms = ({templateId}) => {
    
  const {
    data: vms, 
    status: vmsStatus,
    isRefetching: isVmsRefetching,
    refetch: refetchVms, 
    isError: isVmsError, 
    error: vmsError, 
    isLoading: isVmsLoading,
  } = useAllVmsFromTemplate(templateId, (e) => ({ ...e }));

  return (
    <>
        <TablesOuter
          columns={TableColumnsInfo.TEMPLATE_VMS} 
          data={vms} 
          className="template_chart"
          clickableColumnIndex={[1]} 
        />
    </>
  );
};

export default TemplateVms;
