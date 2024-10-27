import React, { useEffect, useState } from 'react';
import AreaChart from './AreaChart';

const SuperAreaChart = ({ type, vmUsage = [] }) => {
  const [series, setSeries] = useState([]);
  const [datetimes, setDatetimes] = useState([]);

  useEffect(() => {
    const seriesData = {};
    const timeData = [];

    if(type === 'cpu'){
        vmUsage.forEach(item => {
            if (!seriesData[item.name]) {
            seriesData[item.name] = { name: item.name, data: [] };
            }
            seriesData[item.name].data.push(item.cpuPercent);
            timeData.push(item.time);
        })
    }
    else{
        vmUsage.forEach(item => {
            if (!seriesData[item.name]) {
            seriesData[item.name] = { name: item.name, data: [] };
            }
            seriesData[item.name].data.push(item.memoryPercent);
            timeData.push(item.time);
            
        })
    }
      setSeries(Object.values(seriesData));
      setDatetimes(timeData.reverse());
    
  }, [vmUsage]);

  return (
    <AreaChart 
      series={series}
      datetimes={datetimes} 
    />
  );
};

export default SuperAreaChart;
