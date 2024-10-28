import React, { useEffect, useState } from 'react';
import AreaChart from './AreaChart';

const SuperAreaChart = ({ type, vmUsage = [] }) => {
  const [series, setSeries] = useState([]);
  const [datetimes, setDatetimes] = useState([]);

  useEffect(() => {
    const seriesData = {};
    const timeData = [];
    
    vmUsage.forEach(item => {
        if (!seriesData[item.name]) {
          seriesData[item.name] = { name: item.name, data: [] };
        }
        if(type === 'cpu'){
          seriesData[item.name].data.push(item.cpuPercent);
        }else{
          seriesData[item.name].data.push(item.memoryPercent);
        }
        timeData.push(item.time);
    })
    
    setSeries(Object.values(seriesData));
    setDatetimes(timeData);
  }, [vmUsage]);

  return (
    <AreaChart 
      series={series}
      datetimes={datetimes} 
    />
  );
};

export default SuperAreaChart;
