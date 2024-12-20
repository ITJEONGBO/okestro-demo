import React from 'react';
import { faDesktop, faMinusCircle, faPlusCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableInfo from "../../table/TableInfo";
import TablesOuter from "../../table/TablesOuter";
import {useAllTemplateFromDomain} from "../../../api/RQHook";
import { formatBytesToGB } from '../../util/format';

const TemplateRow = ({ template, isExpanded, toggleRow }) => (
  <>
    <tr>
      <td onClick={() => toggleRow(template.id)} style={{ cursor: 'pointer' }}>
        <FontAwesomeIcon
          icon={isExpanded ? faMinusCircle : faPlusCircle}
          fixedWidth
        />
        <FontAwesomeIcon
          icon={faDesktop}
          fixedWidth
          style={{ margin: '0 5px 0 10px' }}
        />
        {template.name}
      </td>
      <td>{template.diskAttachments?.length || 0}</td>
      <td>{template.virtualSize}</td>
      <td>{template.actualSize}</td>
      <td>{template.creationTime || ''}</td>
    </tr>
    {isExpanded &&
      template.diskAttachments?.map((disk, index) => (
        <DiskRow key={`${template.id}-${index}`} disk={disk} />
      ))}
  </>
);

const DiskRow = ({ disk }) => (
  <tr className="detail_machine_second">
    <td style={{ paddingLeft: '30px' }}>
      <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: '0 5px' }} />
      {disk.diskImageVo?.alias || 'Unnamed Disk'}
    </td>
    <td></td>
    <td>{formatBytesToGB(disk.diskImageVo?.virtualSize || 0)}</td>
    <td>{formatBytesToGB(disk.diskImageVo?.actualSize || 0)}</td>
    <td>{disk.diskImageVo?.createDate || ''}</td>
  </tr>
);

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