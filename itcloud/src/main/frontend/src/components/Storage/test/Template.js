import { useAllTemplateFromDomain } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";

// 애플리케이션 섹션
const Template = (domain) => {
    const { 
        data: templates, 
        status: templatesStatus, 
        isLoading: isTemplatesLoading, 
        isError: isTemplatesError,
      } = useAllTemplateFromDomain(domain?.id, toTableItemPredicateTemplates);
      function toTableItemPredicateTemplates(template) {
        return {
          alias: template?.alias ?? '없음',
          disk: template?.disk ?? '없음',
          virtualSize: template?.virtualSize ? `${template.virtualSize} GiB` : '알 수 없음',
          actualSize: template?.actualSize ? `${template.actualSize} GiB` : '알 수 없음',
          creationDate: template?.creationDate ?? '알 수 없음',
        };
      }
  
    return (
        <>
          <div className="host_empty_outer">
            <TableOuter 
              columns={TableColumnsInfo.TEMPLATE_FROM_DOMAIN}
              data={templates}
              onRowClick={() => console.log('Row clicked')} 
            />
          </div>
        </>
    );
  };
  
  export default Template;