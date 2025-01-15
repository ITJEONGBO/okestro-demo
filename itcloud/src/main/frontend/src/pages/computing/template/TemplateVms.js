import React from 'react';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useAllVmsFromTemplate } from '../../../api/RQHook';
import TablesOuter from '../../../components/table/TablesOuter';

const TemplateVms = ({ templateId }) => {
  const {
    data: vms = [], isLoading: isVmsLoading
  } = useAllVmsFromTemplate(templateId, (e) => ({ ...e }));

  return (
    <>
        <TablesOuter
          columns={TableColumnsInfo.TEMPLATE_VMS} 
          data={vms} 
          clickableColumnIndex={[1]} 
        />
    </>
  );
};

export default TemplateVms;