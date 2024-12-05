import React from 'react';
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import {useAllTemplateFromDomain} from "../../../api/RQHook";

const DomainTemplates = ({ domainId }) => {
  const { 
    data: templates, 
    status: templatesStatus, 
    isLoading: isTemplatesLoading, 
    isError: isTemplatesError,
  } = useAllTemplateFromDomain(domainId, (e) => ({
    ...e,
  }));
  
  return (
    <>
      <TablesOuter 
        columns={TableInfo.TEMPLATES_FROM_STORAGE_DOMAIN}
        data={templates} 
      />
    </>
  );
};

export default DomainTemplates;