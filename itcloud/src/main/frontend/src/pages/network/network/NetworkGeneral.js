import { useNetworkById } from "../../../api/RQHook";

const NetworkGeneral = ({ networkId }) => {
  const { data: network} = useNetworkById(networkId);

  return (
    <table className="table">
      <tbody>
        <tr>
          <th>이름</th>
          <td>{network?.name}</td>
          </tr>
        <tr>
          <th>ID:</th>
          <td>{network?.id}</td>
          </tr>
        <tr>
          <th>설명:</th>
          <td>{network?.description ?? ''}</td></tr>
        <tr>
          <th>VDSM 이름:</th>
          <td>{network?.vdsmName ?? ''}</td></tr>
        <tr>
          <th>가상 머신 네트워크:</th>
          <td>{network?.usage?.vm ? '예': '아니요'}</td></tr>
        <tr>
          <th>VLAN 태그:</th>
          <td>{network?.vlan === 0 ? '없음': network?.vlan}</td></tr>
        <tr>
          <th>MTU:</th>
          <td>{network?.mtu === 0 ? '기본값 (1500)': network?.mtu}</td></tr>
      </tbody>
    </table>
  );
};

export default NetworkGeneral;