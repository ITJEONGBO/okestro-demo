
import { useState, useEffect } from 'react';
import ReactApexChart from 'react-apexcharts';
import './BarChart.css'

const BarChart = ({ names, percentages }) => {
  const [series, setSeries] = useState([{ data: [] /* 막대 값 */ }]);
  const [chartOptions, setChartOptions] = useState({
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
    setSeries([{ data: percentages }]);
    setChartOptions(prevOptions => ({
      ...prevOptions,
      xaxis: {
        ...prevOptions.xaxis,
        categories: names,
      }
    }))
  }, [names, percentages])

  return (
    <div>
      <div id="chart">
        <ReactApexChart 
          options={chartOptions} 
          series={series}
          type="bar"
          height={180} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}

export default BarChart