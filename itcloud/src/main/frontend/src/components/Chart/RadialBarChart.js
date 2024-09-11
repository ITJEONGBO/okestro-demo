
import { useState, useEffect } from 'react';
import ReactApexChart from 'react-apexcharts';
import './RadialBarChart.css'

const RadialBarChart =({ percentage }) => {
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
            formatter: (val) => {
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
    setSeries([percentage]);

    let color = '#FF4560'; // 70 이상 빨강
    if (percentage < 30) {
      color = '#00E396';  // 30 미만이면 초록색
    } else if (percentage < 70) {
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
              formatter: (val) => {
                return parseInt(val) + "%"; // 값 포맷
              },
            },
          },
        },
      },
    }));
  }, [percentage]);

  return (
    <div>
      <div id="chart">
        <ReactApexChart 
          options={chartOptions}
          series={series}
          type="radialBar"
          height={200} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}

export default RadialBarChart