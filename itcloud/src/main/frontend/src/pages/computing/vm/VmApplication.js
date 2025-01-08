import { useApplicationFromVM } from "../../../api/RQHook";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TablesOuter from "../../../components/table/TablesOuter";

// 애플리케이션 섹션
const VmApplication = ({vm}) => {

  const { 
    data: applications = [], 
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
        <TablesOuter
          columns={TableColumnsInfo.APPLICATIONS_FROM_VM}
          data={applications}
          onRowClick={() => console.log('Row clicked')}
        />
      </div>
  );
};
  
export default VmApplication;