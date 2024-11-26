import React from 'react';
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";
import {useAllTemplateFromDomain} from "../../../api/RQHook";

const DomainTemplates = ({ domainId }) => {
  const { 
    data: templates, 
    status: templatesStatus, 
    isLoading: isTemplatesLoading, 
    isError: isTemplatesError,
  } = useAllTemplateFromDomain(domainId, (e) => ({
    
  }));
  
  return (
    <>
      <TableOuter 
        columns={TableInfo.TEMPLATE_FROM_DOMAIN}
        data={templates} 
      />
    </>
  );
};

export default DomainTemplates;