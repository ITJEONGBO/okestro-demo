
import logo from '../../../img/logo.png'
import '../css/RutilManager.css'
import { 
    useDashboard, 
    useDashboardCpuMemory, 
    useDashboardStorage,
} from '../../../api/RQHook';

const Info = ({  }) => {
    const { data: dashboard } = useDashboard();
    const { data: cpuMemory } = useDashboardCpuMemory();
    const { data: storage } = useDashboardStorage();
    
    return (
        <div className="rutil_general">
            <div className="rutil_general_first_contents">
                <div>
                    <img className="logo_general" src={logo} alt="logo" />
                    <span>버전: ###<br />빌드:###</span>
                </div>
                <div>
                    <div>
                        <span>데이터센터: {dashboard?.datacenters ?? 0}</span><br/>
                        <span>클러스터: {dashboard?.clusters ?? 0}</span><br/>
                        <span>호스트: {dashboard?.hosts ?? 0}</span><br/>
                        <span>가상머신: {dashboard?.vmsUp ?? 0} / {dashboard?.vms}</span><br/>
                        <span>스토리지 도메인: {dashboard?.storageDomains ?? 0}</span><br/>
                    </div>
                    <br/>
                    <div>부팅시간(업타임): <strong>{dashboard?.bootTime ?? ""}</strong></div>
                </div>
            </div>
            <div className="type_info_boxs">
                <div className="type_info_box">CPU: {Math.floor(100 - (cpuMemory?.totalCpuUsagePercent ?? 0))}% 사용가능</div>
                <div className="type_info_box">메모리: {Math.floor(100 - (cpuMemory?.totalMemoryUsagePercent ?? 0))}% 사용가능</div>
                <div className="type_info_box">스토리지: {Math.floor(100 - (storage?.usedPercent ?? 0))}% 사용가능</div>
            </div>
        </div>
    )
  }
  
  export default Info