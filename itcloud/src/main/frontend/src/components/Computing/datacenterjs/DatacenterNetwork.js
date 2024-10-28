import {useNetworksFromDataCenter} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';


const DatacenterNetwork = ({ dataCenter }) => {
    const navigate = useNavigate();
    const { 
        data: networks, 
        status: networksStatus, 
        isLoading: isNetworksLoading, 
        isError: isNetworksError 
      } = useNetworksFromDataCenter(dataCenter?.id, toTableItemPredicateNetworks);
      function toTableItemPredicateNetworks(network) {
        return {
          id: network?.id ?? '', 
          name: network?.name ?? '없음', // 네트워크 이름을 logicalName으로 매핑
          description: network?.description ?? '설명 없음', // 네트워크 설명
        };
      }

    return (
<>
              <div className="header_right_btns">
                <button>새로 만들기</button>
                <button>편집</button>
                <button>삭제</button>
              </div>
              <TableOuter 
                columns={TableColumnsInfo.LUN_SIMPLE}
                data={networks}
                onRowClick={(row, column, colIndex) => {
                  if (colIndex === 0) {
                    navigate(`/networks/${row.id}`); 
                  }
                }}
                clickableColumnIndex={[0]} 
                onContextMenuItems={() => [
                  <div key="새로 만들기" onClick={() => console.log()}>새로 만들기</div>,
                  <div key="편집" onClick={() => console.log()}>편집</div>,
                  <div key="삭제" onClick={() => console.log()}>삭제</div>
                ]}
              />
            </>
    );
  };
  
  export default DatacenterNetwork;