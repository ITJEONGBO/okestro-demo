import React, { useEffect, useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import { adjustFontSize } from '../UIEvent';
import DashboardBoxGroup from './DashboardBoxGroup';
import './Dashboard.css'
import {
  useDashboard,
  useDashboardCpuMemory,
  useDashboardStorage,
  useDashboardStorageMemory,
  useDashboardVmCpu, 
  useDashboardVmMemory
 } from '../api/RQHook';
import RadialBarChart from './Chart/RadialBarChart';
import BarChart from './Chart/BarChart';
import AreaChart from './Chart/AreaChart';

//region: RadialBarChart
const CpuApexChart = ({ cpu }) => { return (<RadialBarChart percentage={cpu} />); }
const MemoryApexChart = ({ memory }) => { return (<RadialBarChart percentage={memory} />); }
const StorageApexChart = ({ storage }) => { return (<RadialBarChart percentage={storage} />); }
//endregion: RadialBarChart

//region: BarChart
const CpuBarChart = ({vmCpu}) => {
  return (
    <BarChart 
      names={vmCpu?.map((e) => e.name) ?? []}
      percentages={vmCpu?.map((e) => e.cpuPercent) ?? []} 
    />
  );
}
const MemoryBarChart = ({vmMemory}) => {
  return (
    <BarChart 
      names={vmMemory?.map((e) => e.name) ?? []}
      percentages={vmMemory?.map((e) => e.memoryPercent) ?? []} 
    />
  );
}
const StorageMemoryBarChart = ({storageMemory}) => {
  return (
    <BarChart 
      names={storageMemory?.map((e) => e.name) ?? []}
      percentages={storageMemory?.map((e) => e.memoryPercent) ?? []} 
    />
  );
}
//endregion: BarChart


//region: AreaChart
const SuperAreaChart = () => {
  // 물결그래프
  const [series, setSeries] = useState([{
    name: 'series1',
    data: [31, 40, 28, 51, 42, 109, 100] // 물결그래프 값
  }, {
    name: 'series2',
    data: [11, 32, 45, 82, 34, 52, 41]
  }, {
    name: 'series3',
    data: [20, 30, 40, 50, 60, 70, 80],
  }]);

  return (
    <AreaChart 
      series={series}
      datetimes={[
        "2018-09-19T00:00:00.000Z",
        "2018-09-19T01:30:00.000Z",
        "2018-09-19T02:30:00.000Z",
        "2018-09-19T03:30:00.000Z",
        "2018-09-19T04:30:00.000Z",
        "2018-09-19T05:30:00.000Z",
        "2018-09-19T06:30:00.000Z"
      ]} 
    />
  );
}
//endregion: AreaChart

// 바둑판
function generateData(count, yrange) {
  var i = 0;
  var series = [];
  while (i < count) {
    var x = (i + 1).toString();
    var y = Math.floor(Math.random() * (yrange.max - yrange.min + 1)) + yrange.min;

    series.push({ x: x, y: y });
    i++;
  }
  return series;
}

//regino: HeatMapChart
class HeatMapChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      series: [
        {
          name: 'Metric1',
          data: generateData(8, { min: 0, max: 90 }),
        },
        {
          name: 'Metric2',
          data: generateData(8, { min: 0, max: 90 }),
        },
      ],
      options: {
        chart: {
          height: 140, // 높이 조정
          type: 'heatmap',
        },
        dataLabels: {
          enabled: false,
        },
        colors: ['#008FFB'],
      },
    };
  }

  render() {
    return (
      <div>
        <div id="chart">
          <ReactApexChart options={this.state.options} series={this.state.series} type="heatmap" height={140} />
        </div>
        <div id="html-dist"></div>
      </div>
    );
  }
}
//endregion: HeatMapChart


//region: Dashboard
const Dashboard = () => {
  const {
    data: dashboard,
    status: dashboardStatus,
    isRefetching: isDashboardRefetching,
    refetch: dashboardRefetch, 
    isError: isDashboardError, 
    error: dashboardError, 
    isLoading: isDashboardLoading,
  } = useDashboard()

  const {
    data: cpuMemory,
    status: cpuMemoryStatus,
    isRefetching: isCpuMemoryRefetching,
    refetch: cpuMemoryRefetch, 
    isError: isCpuMemoryError, 
    error: cpuMemoryError, 
    isLoading: isCpuMemoryLoading,
  } = useDashboardCpuMemory()

  const {
    data: storage,
    status: storageStatus,
    isRefetching: isStorageRefetching,
    refetch: storageRefetch, 
    isError: isStorageError, 
    error: storageError, 
    isLoading: isStorageLoading,
  } = useDashboardStorage()
  
  const {
    data: vmCpu,
    status: vmCpuStatus,
    isRefetching: isVmCpuRefetching,
    refetch: vmCpuRefetch, 
    isError: isVmCpuError, 
    error: vmCpuError, 
    isLoading: isVmCpuLoading,
  } = useDashboardVmCpu()

  const {
    data: vmMemory,
    status: vmMemoryStatus,
    isRefetching: isVmMemoryRefetching,
    refetch: vmMemoryRefetch, 
    isError: isVmMemoryError, 
    error: vmMemoryError, 
    isLoading: isVmMemoryLoading,
  } = useDashboardVmMemory()

  const {
    data: storageMemory,
    status: storageMemoryStatus,
    isRefetching: isStorageMemoryRefetching,
    refetch: storageMemoryRefetch, 
    isError: isStorageMemoryError, 
    error: storageMemoryError, 
    isLoading: isStorageMemoryeLoading,
  } = useDashboardStorageMemory()

  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

  return (
    <>
      {/* 대시보드 section */}
      <div className="dash_board">
        {dashboard && <DashboardBoxGroup 
          boxItems={[
            { title: "Datacenter", cntTotal: dashboard?.datacenters ?? [], cntUp: dashboard?.datacentersUp === 0 ? "" : dashboard?.datacentersUp, cntDown: dashboard?.datacentersDown === 0 ? "" : dashboard?.datacentersDown, navigatePath: '/computing/datacenter' },
            { title: "Cluster",    cntTotal: dashboard?.clusters ?? 0, navigatePath: '/computing/datacenter' },
            { title: "Host",       cntTotal: dashboard?.hosts ?? 0, cntUp: dashboard?.hostsUp === 0 ? "" : dashboard?.hostsUp, cntDown: dashboard?.hostsDown === 0 ? "" : dashboard?.hostsDown, navigatePath: '/computing/host' },
            { title: "StorageDomain", cntTotal: dashboard?.storageDomains ?? 0, navigatePath: '/storage-domainpart' },
            /*편집해야됨 */
            { title: "Virtual machine", cntTotal: dashboard?.vms ?? 0, cntUp: dashboard?.vmsUp === 0 ? "" : dashboard?.vmsUp, cntDown: dashboard?.vmsDown === 0 ? "" : dashboard?.vmsDown, navigatePath: '/computing/vmhost-chart' },
            { title: "Event",       cntTotal: dashboard?.events ?? 0, alert: dashboard?.eventsAlert === 0 ? "" : dashboard?.eventsAlert, error: dashboard?.eventsError === 0 ? "" : dashboard?.eventsError, warning: dashboard?.eventsWarning === 0 ? "" : dashboard?.eventsWarning, navigatePath: '/events' }
          ]}

        />}
        <div className="dash_section">
          <div className="dash_section_contents">
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" 
                data-active-on-visible-callback-func-name="CircleRun"
              >
                {cpuMemory && <CpuApexChart cpu={cpuMemory?.totalCpuUsagePercent ?? 0} />/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {vmCpu && <CpuBarChart vmCpu={vmCpu} /> /* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED { Math.floor((cpuMemory?.usedCpuCore)/(cpuMemory?.totalCpuCore)*100 )} % / COMMIT { Math.floor((cpuMemory?.commitCpuCore)/(cpuMemory?.totalCpuCore)*100 )} % <br/> Total { (cpuMemory?.totalCpuCore) } Core</span>
            <div className="wave_graph">
              <h2>Per CPU</h2>
              <div><SuperAreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}</div>
            </div>
          </div>

          <div className="dash_section_contents">
            <h1>MEMORY</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" 
                data-active-on-visible-callback-func-name="CircleRun"
              >
                {cpuMemory && <MemoryApexChart memory={cpuMemory?.totalMemoryUsagePercent}/> /* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {vmMemory && <MemoryBarChart vmMemory={vmMemory} />/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED { (cpuMemory?.usedMemoryGB)?.toFixed(1) } GB / Total { (cpuMemory?.totalMemoryGB)?.toFixed(1) } GB</span>
            <div className="wave_graph">
              <h2>Per MEMORY</h2>
              <div><SuperAreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}</div>
            </div>
          </div>

          <div className="dash_section_contents" style={{ borderRight: 'none' }}>
            <h1>STORAGE</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" 
                data-active-on-visible-callback-func-name="CircleRun"
              >
                {storage && <StorageApexChart storage = {storage?.usedPercent} /> /* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {storageMemory && <StorageMemoryBarChart storageMemory={storageMemory}/> /* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED {storage?.usedGB} GB / Total {storage?.freeGB} GB</span>
            <div className="wave_graph">
              <h2>Per STORAGE</h2>
              <div><SuperAreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}</div>
            </div>
          </div>
        </div> {/* dash section 끝 */}
        
        <div className="bar">
          <div>
            <span>CPU</span>
            <div><HeatMapChart /></div>
          </div>
          <div>
            <span>MEMORY</span>
            <div><HeatMapChart /></div>
          </div>
          <div>
            <span>Ethernet</span>
            <div><HeatMapChart /></div>
          </div>  
        </div>
      </div> {/* 대시보드 section끝 */}
    </>
  );
};
//endregion: Dashboard

export default Dashboard;
