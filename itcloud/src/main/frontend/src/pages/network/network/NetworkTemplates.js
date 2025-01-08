import { useAllTemplatesFromNetwork } from "../../../api/RQHook";
import TablesOuter from '../../../components/table/TablesOuter';
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";

// 애플리케이션 섹션
const NetworkTemplates = ({ networkId }) => {
  const { 
    data: templates = [], 
    status: templatesStatus, 
    isLoading: isTemplatesLoading, 
    isError: isTemplatesError 
  } = useAllTemplatesFromNetwork(networkId, (e) => ({ ...e }));


  return (
    <>
      <div className="header_right_btns">
      </div>
      <TablesOuter
        columns={TableColumnsInfo.TEMPLATES_FROM_NETWORK}
        data={templates}
        onRowClick={() => console.log("Row clicked")}
      />
    </>
  );
};

export default NetworkTemplates;
