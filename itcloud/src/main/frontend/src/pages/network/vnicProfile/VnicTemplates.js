import { useAllTemplatesFromVnicProfiles} from "../../../api/RQHook";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";
import TablesOuter from "../../../components/table/TablesOuter";


const VnicTemplates = ({  vnicProfileId  }) => {
  const { data: nic } = useAllTemplatesFromVnicProfiles(vnicProfileId, (e) => ({...e,}));
  console.log("template nic data: ", nic);
  console.log("받아온 nic아이디: ", vnicProfileId);
  return (
    <TablesOuter
      columns={TableColumnsInfo.TEMPLATE_FROM_VNIC_PROFILES}
      data={nic || []}
    />
  );
};

export default VnicTemplates;