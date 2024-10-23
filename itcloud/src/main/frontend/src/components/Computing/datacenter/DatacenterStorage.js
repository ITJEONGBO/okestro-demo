import {useDomainsFromDataCenter} from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableInfo from "../../table/TableInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';


const DatacenterStorage = ({ dataCenter }) => {
    const navigate = useNavigate();

    const { 
        data: domains, 
        status: domainsStatus, 
        isLoading: isDomainsLoading, 
        isError: isDomainsError 
      } = useDomainsFromDataCenter(dataCenter?.id, toTableItemPredicateDomains);
      function toTableItemPredicateDomains(domain) {
        return {
          icon: '📁', 
          icon2: '💾', // 두 번째 이모티콘을 고정적으로 표시
          name: domain?.name ?? '없음', // 도메인 이름
          domainType: domain?.domainType ?? '없음', // 도메인 유형
          status: domain?.status ? '활성화':'비활성화', // 상태
          availableSize: domain?.availableSize ?? '알 수 없음', // 여유 공간 (GiB)
          usedSize: domain?.usedSize ?? '알 수 없음', // 사용된 공간
          diskSize: domain?.diskSize ?? '알 수 없음', // 전체 공간 (GiB)
          description: domain?.description ?? '설명 없음', // 설명
        };
      }

    return (
            <>
              <div className="header_right_btns">
                <button>새로 만들기</button>
                <button className='disabled'>분리</button>
                <button className='disabled'>활성</button>
                <button>유지보수</button>
                <button onClick={() => {}}>디스크</button>
              </div>
              <TableOuter 
                columns={TableInfo.STORAGES_FROM_DATACENTER} 
                data={domains}
                onRowClick={() => {}}
                onContextMenuItems={() => [
                  <div key="새로 만들기" onClick={() => console.log()}>새로 만들기</div>,
                  <div key="분리" onClick={() => console.log()}>분리</div>,
                  <div key="활성" onClick={() => console.log()}>활성</div>,
                  <div key="유지보수" onClick={() => console.log()}>유지보수</div>,
                  <div key="디스크" onClick={() => console.log()}>디스크</div>
                ]}
              />
            </>
    );
  };
  
  export default DatacenterStorage;