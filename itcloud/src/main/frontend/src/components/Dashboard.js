import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import ReactApexChart from 'react-apexcharts';
import '../App.css';

// 도넛
class ApexChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      series: [70],
      options: {
        chart: {
          height: 150,
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
                }
              }
            },
            track: {
              background: '#f0f0f0',
              strokeWidth: '100%', // 선 두께 설정
              margin: 5, // 차트 간격 설정
            },
            stroke: {
              lineCap: 'round' // 선의 끝 모양 설정
            }
          },
        },
        labels: [], // 라벨을 제거합니다.
      },
    };
  }

  render() {
    return (
      <div>
        <div id="chart">
          <ReactApexChart options={this.state.options} series={this.state.series} type="radialBar" height={220} />
        </div>
        <div id="html-dist"></div>
      </div>
    );
  }
}
// 도넛옆에 막대
class BarChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      series: [{
        data: [100, 430, 448]
      }],
      options: {
        chart: {
          type: 'bar',
          height: 180
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
        colors: ['#33b2df', '#546E7A', '#d4526e'],
        dataLabels: {
          enabled: true,
          textAnchor: 'start',
          style: {
            colors: ['#fff'],
            fontSize: '0.25rem', // 텍스트 크기를 rem 단위로 설정합니다.
            fontWeight: '400'
          },
          formatter: function (val, opt) {
            return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val
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
          categories: ['South Korea', 'Canada', 'United Kingdom'],
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
                return ''
              }
            }
          }
        }
      },
    };
  }

  render() {
    return (
      <div>
        <div id="chart">
          <ReactApexChart options={this.state.options} series={this.state.series} type="bar" height={200} />
        </div>
        <div id="html-dist"></div>
      </div>
    );
  }
}
//물결그래프
class AreaChart extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      series: [{
        name: 'series1',
        data: [31, 40, 28, 51, 42, 109, 100]
      }, {
        name: 'series2',
        data: [11, 32, 45, 32, 34, 52, 41]
      }, {
        name: 'series3', // 새로운 시리즈 추가
        data: [20, 30, 40, 50, 60, 70, 80],
      }],
      options: {
        chart: {
          height: 350,
          type: 'area'
        },
        colors: ['#008FFB', '#00E396', '#FF4560'], // 새로운 시리즈의 색상을 빨간색으로 설정
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
      },
    };
  }

  render() {
    return (
      <div>
        <div id="chart">
          <ReactApexChart options={this.state.options} series={this.state.series} type="area" height={140} />
        </div>
        <div id="html-dist"></div>
      </div>
    );
  }
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
          height: 350,
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
  useEffect(() => {
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
            <span>UPTIME</span>
            <h1>2</h1>
          </div>
          <div className="box">
            <span>데이터센터</span>
            <h1>2</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up">1</i>
              <i className="fa fa-arrow-down">1</i>
            </div>
          </div>
          <div className="box">
            <span>클러스터</span>
            <h1>2</h1>
          </div>
          <div className="box">
            <span>호스트</span>
            <h1>2</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up">1</i>
              <i className="fa fa-arrow-down">1</i>
            </div>
          </div>
          <div className="box">
            <span>데이터스토리지도메인</span>
            <h1>2</h1>
          </div>
          <div className="box">
            <span>가상머신</span>
            <h1>2</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up">1</i>
              <i className="fa fa-arrow-down">1</i>
            </div>
          </div>
          <div className="box">
            <span>이벤트</span>
            <h1>0</h1>
            <div className="arrows">
              <i className="fa fa-arrow-up">1</i>
              <i className="fa fa-arrow-down">1</i>
            </div>
          </div>
        </div> {/* boxs 끝 */}

        <div id="dash_section">
          <div className="dash_section_contents">
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <ApexChart /> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <BarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED 64 Core / Total 192 Core</span>
            <div className="wave_graph">
              <h2>Per Host</h2>
              <div>
                <AreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
          </div>

          <div className="dash_section_contents">
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <ApexChart /> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <BarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED 64 Core / Total 192 Core</span>
            <div className="wave_graph">
              <h2>Per Host</h2>
              <div>
                <AreaChart /> {/* AreaChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
          </div>

          <div className="dash_section_contents" style={{ borderRight: 'none' }}>
            <h1>CPU</h1>
            <div className="graphs">
              <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                <ApexChart /> {/* ApexChart 컴포넌트를 여기에 삽입 */}
              </div>
              <div>
                <BarChart /> {/* BarChart 컴포넌트를 여기에 삽입 */}
              </div>
            </div>
            <span>USED 64 Core / Total 192 Core</span>
            <div className="wave_graph">
              <h2>Per Host</h2>
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
