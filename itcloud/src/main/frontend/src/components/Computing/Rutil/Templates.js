import React from 'react';
import '../css/Computing.css';
import {faDesktop } from '@fortawesome/free-solid-svg-icons';
import TableInfo from '../../table/TableInfo';
import { useAllTemplates } from '../../../api/RQHook';
import TemplateDupl from '../../duplication/TemplateDupl';
import HeaderButton from '../../button/HeaderButton';

const Templates = () => {
  const { 
    data: templates, 
    status: templatesStatus,
    isRefetching: isTemplatesRefetching,
    refetch: refetchTemplates, 
    isError: isTemplates, 
    error: templatesError, 
    isLoading: isTemplatesLoading,
  } = useAllTemplates((e) => ({...e,}));

  return (
    <>
   
        <TemplateDupl
          templates={templates || []}
          columns={TableInfo.TEMPLATES}
        />      

    </>
  );
};

export default Templates;