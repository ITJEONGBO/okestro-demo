import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";


const ClusterGeneral = ({ cluster }) => {
    return (
        <div className="cluster_general">
        <div className="table_container_center">
            <table className="table">
                <tbody>
                    <tr>
                        <th>이름</th>
                        <td>{cluster?.name}</td>
                    </tr>
                    <tr>
                        <th>설명:</th>
                        <td>{cluster?.description}</td>
                    </tr>
                    <tr>
                        <th>데이터센터:</th>
                        <td>{cluster?.dataCenter?.id}</td>
                    </tr>
                    <tr>
                        <th>호환버전:</th>
                        <td>{cluster?.version}</td>
                    </tr>
                    <tr>
                        <th>클러스터 노드 유형:</th>
                        <td>Virt</td>
                    </tr>
                    <tr>
                        <th>클러스터 ID:</th>
                        <td>{cluster?.id}</td>
                    </tr>
                    <tr>
                        <th>클러스터 CPU 유형:</th>
                        <td>
                            {cluster?.cpuType}
                             <FontAwesomeIcon icon={faInfoCircle} style={{ color: 'rgb(83, 163, 255)',marginLeft:'3px' }}fixedWidth/> 
                        </td>
                    </tr>
                    <tr>
                        <th>스레드를 CPU 로 사용:</th>
                        <td>아니요</td>
                    </tr>
                    <tr>
                        <th>최대 메모리 오버 커밋:</th>
                        <td>{cluster?.memoryOverCommit}%</td>
                    </tr>
                    <tr>
                        <th>복구 정책:</th>
                        <td>예</td>
                    </tr>
                    <tr>
                        <th>칩셋/펌웨어 유형:</th>
                        <td>{cluster?.biosType}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div className="table_container_center">
            <table className="table">
                <tbody>
                    <tr>
                        <th>에뮬레이션된 시스템:</th>
                        <td></td>
                    </tr>
                    <tr>
                        <th>가상 머신 수:</th>
                        <td>{cluster?.vmSize?.allCnt}</td>
                    </tr>
                    <tr>
                        <th>총 볼륨 수:</th>
                        <td>해당 없음</td>
                    </tr>
                    <tr>
                        <th>Up 상태의 볼륨 수:</th>
                        <td>해당 없음</td>
                    </tr>
                    <tr>
                        <th>Down 상태의 볼륨 수:</th>
                        <td>해당 없음</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    );
  };
  
  export default ClusterGeneral;