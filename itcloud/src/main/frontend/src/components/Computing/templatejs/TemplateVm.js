import React from 'react';
import '../css/Computing.css'
import TableInfo from '../../table/TableInfo';
import { useAllVMs, useAllVmsFromTemplate } from '../../../api/RQHook';
import TableOuter from '../../table/TableOuter';

const TemplateVm = ({templateId}) => {
    
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
        <TableOuter
          columns={TableInfo.TEMPLATE_VMS} 
          data={vms} 
          className="template_chart"
          clickableColumnIndex={[1]} 
        />
    </>
  );
};

export default TemplateVm;
