import { useApplicationFromVM } from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";

// 애플리케이션 섹션
const VmApplication = ({vm}) => {

    const { 
      data: applications, 
      status: templatesStatus, 
      isLoading: isTemplatesLoading, 
      isError: isTemplatesError 
    } = useApplicationFromVM(vm?.id, toTableItemPredicateTemplates);
  
    function toTableItemPredicateTemplates(application) {
      return {
        id:application?.id ?? '',
        name: application?.name ?? '없음'
      };
    }

    return (
        <div className="host_empty_outer">
          <TableOuter
            columns={TableInfo.APPLICATIONS_FROM_VM}
            data={applications}
            onRowClick={() => console.log('Row clicked')}
          />
        </div>
    );
  };
  
  export default VmApplication;