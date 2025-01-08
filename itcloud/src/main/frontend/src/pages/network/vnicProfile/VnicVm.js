import RQHook, { useAllVmsFromVnicProfiles, useCluster } from '../../../api/RQHook';
import TablesOuter from "../../../components/table/TablesOuter";
import TableColumnsInfo from "../../../components/table/TableColumnsInfo";


const VnicVm = ({ vnicProfileId  }) => {
  const { data: nic } = useAllVmsFromVnicProfiles(vnicProfileId, (e) => ({...e,}));
  console.log("vm nic data: ", nic);
  console.log("받아온 nic아이디: ", vnicProfileId);
  return (
    <TablesOuter
      columns={TableColumnsInfo.VMS_FROM_VNIC_PROFILES}
      data={nic || []}
  />
  );
};

export default VnicVm;