import RQHook, { useAllVmsFromVnicProfiles, useCluster } from '../../../api/RQHook';
import TablesOuter from "../../../components/table/TablesOuter";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";


const VnicVm = ({ vnicProfileId  }) => {
  const { data: nic } = useAllVmsFromVnicProfiles(vnicProfileId);
  console.log("VM FROM NIC Data: ", nic);
  console.log("Fetched NICOID: ", vnicProfileId);
  return (
    <TablesOuter
      columns={TableColumnsInfo.VMS_FROMVNIC_PROFILES}
      data={nic || []}
  />
  );
};

export default VnicVm;