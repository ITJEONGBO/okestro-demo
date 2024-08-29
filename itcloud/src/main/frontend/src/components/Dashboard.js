import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import ReactApexChart from 'react-apexcharts';
import { useNavigate } from 'react-router-dom';
import ApiManager from '../api/ApiManager';
import DEFAULT_VALUES from '../api/DefaultValues';
import './Dashboard.css';

//region: CpuApexChart
const CpuApexChart = ({ cpu }) => {
    // 도넛
    const [series, setSeries] = useState([0]);
    const [chartOptions, setChartOptions] = useState({
      chart: {
        height: 180, // 높이 조정
        type: 'radialBar',
      },
      plotOptions: {
        radialBar: {
          hollow: {
            size: '70%',
          },
          dataLabels: {
            show: true,
            name: {
              show: false, // name 라벨을 제거합니다.
            },
            value: {
              show: true,
              fontSize: '0.6rem', // 값 크기를 rem 단위로 설정합니다.
              fontWeight: 'bold',
              color: '#111',
              formatter: function (val) {
                return parseInt(val) + "%"; // 값 포맷
              },
            },
          },
          track: {
            background: '#f0f0f0',
            strokeWidth: '100%', // 선 두께 설정
            margin: 5, // 차트 간격 설정
          },
          stroke: {
            lineCap: 'round', // 선의 끝 모양 설정
          },
        },
      },
      labels: [], // 라벨을 제거합니다.
      colors: ['#FF4560'], // 초기 색상 설정
    });


    useEffect(() => {
       setSeries([cpu]);

      let color = '#FF4560'; // 70 이상 빨강
      if (cpu < 30) {
        color = '#00E396';  // 30 미만이면 초록색
      } else if (cpu < 70) {
        color = '#FEB019'; // 30 이상 70 미만이면 노란색
      }

      setChartOptions((prevOptions) => ({
        ...prevOptions,
        colors: [color],
        plotOptions: {
          ...prevOptions.plotOptions,
          radialBar: {
            ...prevOptions.plotOptions.radialBar,
            dataLabels: {
              ...prevOptions.plotOptions.radialBar.dataLabels,
              value: {
                ...prevOptions.plotOptions.radialBar.dataLabels.value,
                formatter: function (val) {
                  return parseInt(val) + "%"; // 값 포맷
                },
              },
            },
          },
        },
      }));
    }, [cpu]);


  return (
    <div>
      <div id="chart">
        <ReactApexChart options={chartOptions} series={series} type="radialBar" height={200} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: CpuApexChart

//region: MemoryApexChart
const MemoryApexChart = ({ memory }) => {
    const [series, setSeries] = useState([0]);
    const [chartOptions, setChartOptions] = useState({
      chart: {
        height: 180, // 높이 조정
        type: 'radialBar',
      },
      plotOptions: {
        radialBar: {
          hollow: {
            size: '70%',
          },
          dataLabels: {
            show: true,
            name: {
              show: false, // name 라벨을 제거합니다.
            },
            value: {
              show: true,
              fontSize: '0.6rem', // 값 크기를 rem 단위로 설정합니다.
              fontWeight: 'bold',
              color: '#111',
              formatter: function (val) {
                return parseInt(val) + "%"; // 값 포맷
              },
            },
          },
          track: {
            background: '#f0f0f0',
            strokeWidth: '100%', // 선 두께 설정
            margin: 5, // 차트 간격 설정
          },
          stroke: {
            lineCap: 'round', // 선의 끝 모양 설정
          },
        },
      },
      labels: [], // 라벨을 제거합니다.
      colors: ['#FF4560'], // 초기 색상 설정
    });


    useEffect(() => {
       setSeries([memory]);

      let color = '#FF4560'; // 70 이상 빨강
      if (memory < 30) {
        color = '#00E396';  // 30 미만이면 초록색
      } else if (memory < 70) {
        color = '#FEB019'; // 30 이상 70 미만이면 노란색
      }

      setChartOptions((prevOptions) => ({
        ...prevOptions,
        colors: [color],
        plotOptions: {
          ...prevOptions.plotOptions,
          radialBar: {
            ...prevOptions.plotOptions.radialBar,
            dataLabels: {
              ...prevOptions.plotOptions.radialBar.dataLabels,
              value: {
                ...prevOptions.plotOptions.radialBar.dataLabels.value,
                formatter: function (val) {
                  return parseInt(val) + "%"; // 값 포맷
                },
              },
            },
          },
        },
      }));
    }, [memory]);


  return (
    <div>
      <div id="chart">
        <ReactApexChart options={chartOptions} series={series} type="radialBar" height={200} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: MemoryApexChart


//region: StorageApexChart
const StorageApexChart = ({ storage }) => {
    const [series, setSeries] = useState([0]);
    const [chartOptions, setChartOptions] = useState({
      chart: {
        height: 180, // 높이 조정
        type: 'radialBar',
      },
      plotOptions: {
        radialBar: {
          hollow: {
            size: '70%',
          },
          dataLabels: {
            show: true,
            name: {
              show: false, // name 라벨을 제거합니다.
            },
            value: {
              show: true,
              fontSize: '0.6rem', // 값 크기를 rem 단위로 설정합니다.
              fontWeight: 'bold',
              color: '#111',
              formatter: function (val) {
                return parseInt(val) + "%"; // 값 포맷
              },
            },
          },
          track: {
            background: '#f0f0f0',
            strokeWidth: '100%', // 선 두께 설정
            margin: 5, // 차트 간격 설정
          },
          stroke: {
            lineCap: 'round', // 선의 끝 모양 설정
          },
        },
      },
      labels: [], // 라벨을 제거합니다.
      colors: ['#FF4560'], // 초기 색상 설정
    });


    useEffect(() => {
       setSeries([storage]);

      let color = '#FF4560'; // 70 이상 빨강
      if (storage < 30) {
        color = '#00E396';  // 30 미만이면 초록색
      } else if (storage < 70) {
        color = '#FEB019'; // 30 이상 70 미만이면 노란색
      }

      setChartOptions((prevOptions) => ({
        ...prevOptions,
        colors: [color],
        plotOptions: {
          ...prevOptions.plotOptions,
          radialBar: {
            ...prevOptions.plotOptions.radialBar,
            dataLabels: {
              ...prevOptions.plotOptions.radialBar.dataLabels,
              value: {
                ...prevOptions.plotOptions.radialBar.dataLabels.value,
                formatter: function (val) {
                  return parseInt(val) + "%"; // 값 포맷
                },
              },
            },
          },
        },
      }));
    }, [storage]);


  return (
    <div>
      <div id="chart">
        <ReactApexChart options={chartOptions} series={series} type="radialBar" height={200} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: StorageApexChart

//region: CpuBarChart 
const CpuBarChart = () => {
  // CPU 막대그래프
  const [series, setSeries] = useState([{
    data: [] // 막대 값
  }]);

  const [options, setOptions] = useState({
    chart: {
      type: 'bar',
      height: 150  // 높이 조정
    },
    plotOptions: {
      bar: {
        barHeight: '100%',
        distributed: true,
        horizontal: true,
        dataLabels: {
          position: 'bottom'
        },
      }
    },
    colors: ['#1597E5', '#69DADB', '#7C7DEA'],
    dataLabels: {
      enabled: true,
      textAnchor: 'start',
      style: {
        colors: ['#fff'],
        fontSize: '0.25rem', // 텍스트 크기를 rem 단위로 설정합니다.
        fontWeight: '400'
      },
      formatter: function (val, opt) {
        return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val;
      },
      offsetX: 0,
      dropShadow: {
        enabled: true
      }
    },
    stroke: {
      width: 1,
      colors: ['#fff']
    },
    xaxis: {
      categories: [], // 목록이름
    },
    yaxis: {
      labels: {
        show: false // y축 레이블을 제거합니다.
      }
    },
    title: {
      text: '', // 제목을 제거합니다.
      align: 'center',
      floating: true
    },
    subtitle: {
      text: '', // 부제목을 제거합니다.
      align: 'center',
    },
    tooltip: {
      theme: 'dark',
      x: {
        show: false // x축 제목을 제거합니다.
      },
      y: {
        title: {
          formatter: function () {
            return '';
          }
        }
      }
    }
  });

  useEffect(() => {
      async function fetchData() {
        const data = await ApiManager.getVmCpu();
        const names = data?.map(item => item.name) ?? [];
        const cpuPercents = data?.map(item => item.cpuPercent) ?? [];

        setSeries([{ data: cpuPercents }]);
        setOptions(prevOptions => ({
          ...prevOptions,
          xaxis: {
            ...prevOptions.xaxis,
            categories: names,
          }
        }));
      }
      fetchData();
    }, []);

  return (
    <div>
      <div id="chart">
        <ReactApexChart options={options} series={series} type="bar" height={180} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: CpuBarChart

//region: MemoryBarChart
const MemoryBarChart = () => {
  const [series, setSeries] = useState([{
    data: [] // 막대 값
  }]);

  const [options, setOptions] = useState({
    chart: {
      type: 'bar',
      height: 150  // 높이 조정
    },
    plotOptions: {
      bar: {
        barHeight: '100%',
        distributed: true,
        horizontal: true,
        dataLabels: {
          position: 'bottom'
        },
      }
    },
    colors: ['#1597E5', '#69DADB', '#7C7DEA'],
    dataLabels: {
      enabled: true,
      textAnchor: 'start',
      style: {
        colors: ['#fff'],
        fontSize: '0.25rem', // 텍스트 크기를 rem 단위로 설정합니다.
        fontWeight: '400'
      },
      formatter: function (val, opt) {
        return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val;
      },
      offsetX: 0,
      dropShadow: {
        enabled: true
      }
    },
    stroke: {
      width: 1,
      colors: ['#fff']
    },
    xaxis: {
      categories: [], // 목록이름
    },
    yaxis: {
      labels: {
        show: false // y축 레이블을 제거합니다.
      }
    },
    title: {
      text: '', // 제목을 제거합니다.
      align: 'center',
      floating: true
    },
    subtitle: {
      text: '', // 부제목을 제거합니다.
      align: 'center',
    },
    tooltip: {
      theme: 'dark',
      x: {
        show: false // x축 제목을 제거합니다.
      },
      y: {
        title: {
          formatter: function () {
            return '';
          }
        }
      }
    }
  });

  useEffect(() => {
      async function fetchData() {
        try {
          const data = await ApiManager.getVmMemory();
          const names = data?.map(item => item.name) ?? [];
          const memoryPercent = data?.map(item => item.memoryPercent) ?? [];

          setSeries([{ data: memoryPercent }]);
          setOptions(prevOptions => ({
            ...prevOptions,
            xaxis: {
              ...prevOptions.xaxis,
              categories: names,
            }
          }));
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      }

      fetchData();
    }, []);

  return (
    <div>
      <div id="chart">
        <ReactApexChart options={options} series={series} type="bar" height={180} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: MemoryBarChart


//region: StorageBarChart
const StorageBarChart = () => {
  const [series, setSeries] = useState([{
    data: [] // 막대 값
  }]);

  const [options, setOptions] = useState({
    chart: {
      type: 'bar',
      height: 150  // 높이 조정
    },
    plotOptions: {
      bar: {
        barHeight: '100%',
        distributed: true,
        horizontal: true,
        dataLabels: {
          position: 'bottom'
        },
      }
    },
    colors: ['#1597E5', '#69DADB', '#7C7DEA'],
    dataLabels: {
      enabled: true,
      textAnchor: 'start',
      style: {
        colors: ['#fff'],
        fontSize: '0.25rem', // 텍스트 크기를 rem 단위로 설정합니다.
        fontWeight: '400'
      },
      formatter: function (val, opt) {
        return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val;
      },
      offsetX: 0,
      dropShadow: {
        enabled: true
      }
    },
    stroke: {
      width: 1,
      colors: ['#fff']
    },
    xaxis: {
      categories: [], // 목록이름
    },
    yaxis: {
      labels: {
        show: false // y축 레이블을 제거합니다.
      }
    },
    title: {
      text: '', // 제목을 제거합니다.
      align: 'center',
      floating: true
    },
    subtitle: {
      text: '', // 부제목을 제거합니다.
      align: 'center',
    },
    tooltip: {
      theme: 'dark',
      x: {
        show: false // x축 제목을 제거합니다.
      },
      y: {
        title: {
          formatter: function () {
            return '';
          }
        }
      }
    }
  });

  useEffect(() => {
      async function fetchData() {
        const data = await ApiManager.getStorageMemory();
        const names = data?.map(item => item.name) ?? [];
        const memoryPercent = data?.map(item => item.memoryPercent) ?? [];

        setSeries([{ data: memoryPercent }]);
        setOptions(prevOptions => ({
          ...prevOptions,
          xaxis: {
            ...prevOptions.xaxis,
            categories: names,
          }
        }));
      }
      fetchData();
    }, []);

  return (
    <div>
      <div id="chart">
        <ReactApexChart options={options} series={series} type="bar" height={180} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}
//endregion: StorageBarChart

//region: AreaChart
const AreaChart = () => {
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

  const [options, setOptions] = useState({
    chart: {
      height: 140,  // 높이 조정
      type: 'area'
    },
    colors: ['#1597E5', '#69DADB', 'rgb(231, 190, 231)'],
    dataLabels: {
      enabled: false
    },
    stroke: {
      curve: 'smooth'
    },
    xaxis: {
      type: 'datetime',
      categories: [
        "2018-09-19T00:00:00.000Z",
        "2018-09-19T01:30:00.000Z",
        "2018-09-19T02:30:00.000Z",
        "2018-09-19T03:30:00.000Z",
        "2018-09-19T04:30:00.000Z",
        "2018-09-19T05:30:00.000Z",
        "2018-09-19T06:30:00.000Z"
      ]
    },
    tooltip: {
      x: {
        format: 'dd/MM/yy HH:mm'
      },
    },
  });

  return (
    <div>
      <div id="chart">
        <ReactApexChart options={options} series={series} type="area" height={140} />
      </div>
      <div id="html-dist"></div>
    </div>
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
//폰트사이즈 조정 
  const navigate = useNavigate();
  const [data, setData] = useState(() => DEFAULT_VALUES.GET_DASHBOARD);
  const [memoryGb, setMemoryGb] = useState(() => DEFAULT_VALUES.GET_CPU_MEMERY);
  const [storageGb, setStorageGb] = useState(() => DEFAULT_VALUES.GET_STORAGE);

  useEffect(() => {
    const fetchData = async () => {
      const dashboardRes = await ApiManager.getDashboard();
      setData(dashboardRes);

      const memorys = await ApiManager.getCpuMemory();
      setMemoryGb(memorys);

      const storages = await ApiManager.getStorage();
      setStorageGb(storages);
    };
    fetchData();

    function adjustFontSize() {
      const width = window.innerWidth;
      const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
      document.documentElement.style.fontSize = fontSize + 'px';
    }
    window.addEventListener('resize', adjustFontSize);
    adjustFontSize();
    return () => {
      window.removeEventListener('resize', adjustFontSize);
    };
  }, []);

  return (
    <>
      {/* 대시보드 section */}
      <div id="dash_board">
        <div id="dash_boxs">

          <div className="box" onClick={() => navigate('/computing/datacenter')}>
            <span>Datacenter</span>
            <h1>{data.datacenters}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.datacentersUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.datacentersDown}</i>
            </div>
          </div>
          <div className="box"  onClick={() => navigate('/computing/cluster')}>
            <span>Cluster</span>
            <h1>{data.clusters}</h1>
          </div>
          <div className="box" onClick={() => navigate('/computing/host')}> 
            <span>Host</span>
            <h1>{data.hosts}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.hostsUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.hostsDown}</i>
            </div>
          </div>
          <div className="box" onClick={() => navigate('/storage-domain')}>
            <span>Datastoragedomain</span>
            <h1>{data.storageDomains}</h1>
          </div>
          
          {/*수정해야됨 */}
          <div className="box" onClick={() => navigate('/computing/vmhost-chart')}> 
            <span>Virtual machine</span>
            <h1>{data.vms}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.vmsUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.vmsDown}</i>
            </div>
          </div>
          <div className="box">
            <span>Event</span>
            <h1>0</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> 1</i> &nbsp;
              <i className="fa fa-arrow-down"> 1</i>
            </div>
          </div>
        </div> {/* boxs 끝 */}

        <div id="dash_section">
          <div className="dash_section_contents">
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <CpuApexChart cpu = {memoryGb.totalCpuUsagePercent} /> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <CpuBarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED 64 Core / Total 192 Core</span>
            <div className="wave_graph">
              <h2>Per CPU</h2>
              <div>
                <AreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
          </div>

          <div className="dash_section_contents">
            <h1>MEMORY</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <MemoryApexChart memory = {memoryGb.totalMemoryUsagePercent}/> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <MemoryBarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED { (memoryGb.usedMemoryGB).toFixed(1) } GB / Total { (memoryGb.totalMemoryGB).toFixed(1) } GB</span>
            <div className="wave_graph">
              <h2>Per MEMORY</h2>
              <div>
                <AreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
          </div>

          <div className="dash_section_contents" style={{ borderRight: 'none' }}>
            <h1>STORAGE</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <StorageApexChart storage = { storageGb.usedPercent } /> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <StorageBarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED { storageGb.usedGB } GB / Total { storageGb.freeGB } GB</span>
            <div className="wave_graph">
              <h2>Per STORAGE</h2>
              <div>
                <AreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
          </div>
        </div> {/* dash section 끝 */}
        
        <div id="bar">
          <div>
            <span>CPU(시간 경과에 따른 CPU사용량)</span>
            <div><HeatMapChart /></div>
          </div>
          <div>
            <span>MEMORY(시간 경과에 따른 Memory사용량)</span>
            <div><HeatMapChart /></div>
          </div>
          <div>
            <span>Ethernet(시간 경과에 따른 Ethernet속도)</span>
            <div><HeatMapChart /></div>
          </div>  
        </div>
      </div> {/* 대시보드 section끝 */}
    </>
  );
};
//endregion: Dashboard

document.addEventListener('DOMContentLoaded', function() {
  const domContainer = document.querySelector('#app');
  ReactDOM.render(<Dashboard />, domContainer);
});

export default Dashboard;
