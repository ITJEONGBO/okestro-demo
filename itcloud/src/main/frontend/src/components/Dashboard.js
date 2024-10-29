import React, { useEffect, useState } from 'react';
import { adjustFontSize } from '../UIEvent';
import DashboardBoxGroup from './DashboardBoxGroup';
import './Dashboard.css'
import {
  useDashboard,
  useDashboardCpuMemory,
  useDashboardStorage,
  useDashboardStorageMemory,
  useDashboardPerVmCpu,
  useDashboardPerVmMemory,
  useDashboardPerVmNetwork,
  useDashboardMetricVm,
  useDashboardVmCpu, 
  useDashboardVmMemory
 } from '../api/RQHook';
import RadialBarChart from './Chart/RadialBarChart';
import BarChart from './Chart/BarChart';
import SuperAreaChart from './Chart/SuperAreaChart';
import HeatMapChart from './Chart/HeatMapChart';
import { faCloud, faEarthAmericas, faLayerGroup, faListUl, faMicrochip, faUser } from '@fortawesome/free-solid-svg-icons';

//region: RadialBarChart
// 도넛
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

  const {
    data: vmCpuPer,
    status: vmCpuPerStatus,
    isRefetching: isVmCpuPerRefetching,
    refetch: vmCpuPerRefetch, 
    isError: isVmCpuPerError, 
    error: vmCpuPerError, 
    isLoading: isVmCpuPeroading,
  } = useDashboardPerVmCpu()

  const {
    data: vmMemoryPer,
    status: vmMemoryPerStatus,
    isRefetching: isVmMemoryPerRefetching,
    refetch: vmMemoryPerRefetch, 
    isError: isVmMemoryPerError, 
    error: vmMemoryPerError, 
    isLoading: isVmMemoryPeroading,
  } = useDashboardPerVmMemory()

  const {
    data: vmNetworkPer,
    status: vmNetworkPerStatus,
    isRefetching: isVmNetworkPerRefetching,
    refetch: vmNetworkPerRefetch, 
    isError: isVmNetworkPerError, 
    error: vmNetworkPerError, 
    isLoading: isVmvPeroading,
  } = useDashboardPerVmNetwork()
  
  const {
    data: vmMetric,
    status: vmMetricStatus,
    isRefetching: isVmMetricRefetching,
    refetch: vmMetricRefetch, 
    isError: isVmMetricError, 
    error: vmMetricError, 
    isLoading: isVmMetricoading,
  } = useDashboardMetricVm()


  useEffect(() => {
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => { window.removeEventListener('resize', adjustFontSize); };
  }, []);

  return (
    <>
      {/* 대시보드 section */}
      <div className="dash_board">

        <DashboardBoxGroup 
          boxItems={[
            { icon: faLayerGroup, title: "데이터센터", cntTotal: dashboard?.datacenters ?? 0, cntUp: dashboard?.datacentersUp === 0 ? "" : dashboard?.datacentersUp, cntDown: dashboard?.datacentersDown === 0 ? "" : dashboard?.datacentersDown, navigatePath: '/computing/datacenters' },
            { icon: faEarthAmericas, title: "클러스터", cntTotal: dashboard?.clusters ?? 0, navigatePath: '/computing/clusters' },
            { icon: faUser, title: "호스트", cntTotal: dashboard?.hosts ?? 0, cntUp: dashboard?.hostsUp === 0 ? "" : dashboard?.hostsUp, cntDown: dashboard?.hostsDown === 0 ? "" : dashboard?.hostsDown, navigatePath: '/computing/hosts' },
            { icon: faCloud, title: "스토리지 도메인", cntTotal: dashboard?.storageDomains ?? 0, navigatePath: '/storages/domains' },
            { icon: faMicrochip, title: "가상머신", cntTotal: dashboard?.vms ?? 0, cntUp: dashboard?.vmsUp === 0 ? "" : dashboard?.vmsUp, cntDown: dashboard?.vmsDown === 0 ? "" : dashboard?.vmsDown, navigatePath: '/computing/vms' },
            { icon: faListUl, title: "이벤트", cntTotal: dashboard?.events ?? 0, alert: dashboard?.eventsAlert === 0 ? "" : dashboard?.eventsAlert, error: dashboard?.eventsError === 0 ? "" : dashboard?.eventsError, warning: dashboard?.eventsWarning === 0 ? "" : dashboard?.eventsWarning, navigatePath: '/events' }
          ]}
        />

        <div className="dash_section">
          <div className="dash_section_contents">
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                {cpuMemory && <CpuApexChart cpu={cpuMemory?.totalCpuUsagePercent ?? 0} />/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {vmCpu && <CpuBarChart vmCpu={vmCpu} /> }
              </div>
            </div>
            
            <span>USED { Math.floor((cpuMemory?.usedCpuCore)/(cpuMemory?.totalCpuCore)*100 )} % /  Total { (cpuMemory?.totalCpuCore) } Core</span> {/*COMMIT { Math.floor((cpuMemory?.commitCpuCore)/(cpuMemory?.totalCpuCore)*100 )} % <br/> */}
            <div className="wave_graph">
              <h2>Per CPU</h2>
              <div><SuperAreaChart vmPer={vmCpuPer} /></div>
            </div>
          </div>

          <div className="dash_section_contents">
            <h1>MEMORY</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                {cpuMemory && <MemoryApexChart memory={cpuMemory?.totalMemoryUsagePercent}/> /* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {vmMemory && <MemoryBarChart vmMemory={vmMemory} />}
              </div>
            </div>
            <span>USED { (cpuMemory?.usedMemoryGB)?.toFixed(1) } GB / Total { (cpuMemory?.totalMemoryGB)?.toFixed(1) } GB</span>
            <div className="wave_graph">
              <h2>Per MEMORY</h2>
              <div><SuperAreaChart vmPer={vmMemoryPer} /></div>
            </div>
          </div>

          <div className="dash_section_contents" style={{ borderRight: 'none' }}>
            <h1>STORAGE</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                {storage && <StorageApexChart storage = {storage?.usedPercent} /> /* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                {storageMemory && <StorageMemoryBarChart storageMemory={storageMemory}/> }
              </div>
            </div>
            <span>USED {storage?.usedGB} GB / Total {storage?.freeGB} GB</span>
            <div className="wave_graph">
              <h2>Per Network</h2>
              <div><SuperAreaChart vmPer={vmNetworkPer} /></div>
            </div>
          </div>
        </div>
        
        <div className="bar">
          <div>
            <span>CPU</span>
            <div><HeatMapChart type={'cpu'} vmMetrics={vmMetric} /></div>
          </div>
          <div>
            <span>MEMORY</span>
            <div><HeatMapChart vmMetrics={vmMetric} /></div>
          </div>
          <div>
            <span>StorageDomain</span>
            <div><HeatMapChart vmMetrics={vmMetric} /></div>
          </div>
        </div>
      </div> {/* 대시보드 section끝 */}
    </>
  );
};
//endregion: Dashboard

export default Dashboard;
