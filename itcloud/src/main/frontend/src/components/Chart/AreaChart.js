import { useState, useEffect } from 'react';
import ReactApexChart from 'react-apexcharts';
import './AreaChart.css'

const AreaChart = ({ series, datetimes }) => {

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
      categories: datetimes
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
        <ReactApexChart 
          options={options}
          series={series}
          type="area"
          height={140} />
      </div>
      <div id="html-dist"></div>
    </div>
  );
}

export default AreaChart