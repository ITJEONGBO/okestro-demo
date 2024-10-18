import { useAllTemplateFromDomain, useHostsFromDataCenter } from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";

const Host = ({ dataCenter }) => { // dataCenter를 props로 받음
    const { 
        data: hosts, 
        status: hostsStatus, 
        isLoading: isHostsLoading, 
        isError: isHostsError,
    } = useHostsFromDataCenter(dataCenter?.id, toTableItemPredicateHosts);

    function toTableItemPredicateHosts(host) {
        return {
          icon: host?.icon ?? '',
          name: host?.name ?? '없음',
          comment: host?.comment ?? '없음',
          hostNameIP: host?.hostNameIP ?? '알 수 없음',
          cluster: host?.cluster ?? '알 수 없음',
          datacenter: host?.datacenter ?? '알 수 없음',
          status: host?.status ?? '알 수 없음',
          vm: host?.vm ?? '없음',
          memory: host?.memory ? `${host.memory} GiB` : '알 수 없음',
          cpu: host?.cpu ?? '알 수 없음',
          network: host?.network ?? '알 수 없음',
          spmStatus: host?.spmStatus ?? '알 수 없음',
        };
    }
  
    return (
        <>
            <div className="header_right_btns">
                <button>{dataCenter?.name ?? '데이터 센터 이름 없음'}</button> {/* 데이터 센터 이름 출력 */}
                <button>편집</button>
                <button>삭제</button>
                <button>관리</button>
                <button>설치</button>
                <button>호스트 네트워크 복사</button>
            </div>
            <TableOuter 
                columns={TableColumnsInfo.HOSTS_ALL_DATA} 
                data={hosts}
                onRowClick={() => console.log('Row clicked')} 
            />
        </> 
    );
};

export default Host;
