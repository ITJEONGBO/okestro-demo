import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import ReactApexChart from 'react-apexcharts';
import '../App.css';
import axios from 'axios';

// cpu, memory api 불러오는 값
async function getCpuMemory() {
    const cpuMemory = await axios.get('/dashboard/cpumemory');
    return cpuMemory.data;
}

// storage api 불러오는 값
async function getStorage() {
    const storage = await axios.get('/dashboard/storage');
    return storage.data;
}

// vmCpu api 불러오는 값
async function getVmCpu() {
    const vmCpu = await axios.get('/dashboard/vmCpu');
    return vmCpu.data;
}

// vmCpu api 불러오는 값
async function getVmMemory() {
    const vmMemory = await axios.get('/dashboard/vmMemory');
    return vmMemory.data;
}


// 도넛
const CpuApexChart = ({ cpu }) => {
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


// CPU 막대그래프
const CpuBarChart = () => {
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
          const data = await getVmCpu();
          const names = data.map(item => item.name);
          const cpuPercents = data.map(item => item.cpuPercent);

          setSeries([{ data: cpuPercents }]);
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
          const data = await getVmMemory();
          const names = data.map(item => item.name);
          const memoryPercent = data.map(item => item.memoryPercent);

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



const BarChart = () => {
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
          const data = await getVmCpu();
          const names = data.map(item => item.name);
          const cpuPercents = data.map(item => item.cpuPercent);

          setSeries([{ data: cpuPercents }]);
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





//물결그래프
const AreaChart = () => {
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

class HeatMapChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      series: [{
        name: 'Metric1',
        data: generateData(18, { min: 0, max: 90 })
      },
      {
        name: 'Metric2',
        data: generateData(18, { min: 0, max: 90 })
      },
      {
        name: 'Metric3',
        data: generateData(18, { min: 0, max: 90 })
      },
      {
        name: 'Metric4',
        data: generateData(18, { min: 0, max: 90 })
      },
      {
        name: 'Metric5',
        data: generateData(18, { min: 0, max: 90 })
      },
      {
        name: 'Metric9',
        data: generateData(18, { min: 0, max: 90 })
      }],
      options: {
        chart: {
          height: 140,  // 높이 조정
          type: 'heatmap',
        },
        dataLabels: {
          enabled: false
        },
        colors: ["#008FFB"],
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

//폰트사이즈 조정 
const Dashboard = () => {
    const [data, setData] = useState({
        uptime: 0,
        datacenters: 0,
        datacentersUp: 0,
        datacentersDown: 0,
        clusters: 0,
        hosts: 0,
        hostsUp: 0,
        hostsDown: 0,
        vms: 0,
        vmsUp: 0,
        vmsDown: 0,
        storageDomains: 0,
      });

    const [memoryGb, setMemoryGb] = useState({
        totalCpuUsagePercent: 0,
        totalMemoryUsagePercent: 0,
        totalMemoryGB: 0,
        usedMemoryGB: 0,
        freeMemoryGB: 0
    });

    const [storageGb, setStorageGb] = useState({
        usedPercent: 0,
        totalGB: 0,
        usedGB: 0,
        freeGB: 0
    });

  useEffect(() => {
    const fetchData = async () => {
        try {
          const response = await axios.get('/dashboard');
          setData(response.data);

          const memorys = await getCpuMemory();
          setMemoryGb(memorys);

          const storages = await getStorage();
          setStorageGb(storages);


        } catch (error) {
          console.error('Error fetching dashboard data:', error);
        }
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
          <div className="box">
            <span>데이터센터</span>
            <h1>{data.datacenters}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.datacentersUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.datacentersDown}</i>
            </div>
          </div>
          <div className="box">
            <span>클러스터</span>
            <h1>{data.clusters}</h1>
          </div>
          <div className="box">
            <span>호스트</span>
            <h1>{data.hosts}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.hostsUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.hostsDown}</i>
            </div>
          </div>
          <div className="box">
            <span>데이터스토리지도메인</span>
            <h1>{data.storageDomains}</h1>
          </div>
          <div className="box">
            <span>가상머신</span>
            <h1>{data.vms}</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up"> {data.vmsUp}</i> &nbsp;
              <i className="fa fa-arrow-down"> {data.vmsDown}</i>
            </div>
          </div>
          <div className="box">
            <span>이벤트</span>
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
                <BarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
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
                <BarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
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

document.addEventListener('DOMContentLoaded', function() {
  const domContainer = document.querySelector('#app');
  ReactDOM.render(<Dashboard />, domContainer);
});

export default Dashboard;
