import React from 'react';
import {faDesktop } from '@fortawesome/free-solid-svg-icons';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useAllTemplates } from '../../../api/RQHook';
import TemplateDupl from '../../computing/template/TemplateDupl';
import HeaderButton from '../../../components/button/HeaderButton';

const AllTemplates = () => {
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
        columns={TableColumnsInfo.TEMPLATES}
        type='rutil'
      />      
    </>
  );
};

export default AllTemplates;