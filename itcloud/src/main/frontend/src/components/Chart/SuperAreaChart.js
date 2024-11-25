import React, { useEffect, useState } from 'react';
import AreaChart from './AreaChart';

const SuperAreaChart = ({ vmPer }) => {
  const [series, setSeries] = useState([]);
  const [datetimes, setDatetimes] = useState([]);

  // 날짜 변환 함수
  const formatDate = (isoDate) => {
    const date = new Date(isoDate);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  };

  useEffect(() => {
    if (Array.isArray(vmPer) && vmPer.length > 0) {
      const defaultItem = { name: 'Unknown', time: [], dataList: [] };
      const sortedData = vmPer
        .filter(item => item) // Remove null/undefined entries
        .map(item => ({ ...defaultItem, ...item })) // Merge defaults with item
        .map((item) => {
          const combined = item.time.map((time, index) => ({
            time,
            value: item.dataList[index] || 0,
          })).sort((a, b) => new Date(a.time) - new Date(b.time));
  
          return {
            name: item.name,
            data: combined.map(({ time, value }) => ({
              x: formatDate(time),
              y: value,
            })),
          };
        });
  
      // Remove duplicate times
      const uniqueTimes = [...new Set(vmPer[0]?.time.map(formatDate))];
  
      setSeries(sortedData);
      setDatetimes(uniqueTimes);
    } else {
      setSeries([]);
      setDatetimes([]);
    }
  }, [vmPer]);
  

  return (
    <AreaChart 
      series={series}
      datetimes={datetimes}
    />
  );
};

export default SuperAreaChart;


// const SuperAreaChart = ({ type, vmUsage = [] }) => {
//   const [series, setSeries] = useState([]);
//   const [datetimes, setDatetimes] = useState([]);

//   useEffect(() => {
//     const seriesData = {};
    
//     vmUsage.forEach(item => {
//         if (!seriesData[item.name]) {
//           seriesData[item.name] = { name: item.name, data: [] };
//         }
//         if(type === 'cpu'){
//           seriesData[item.name].data = [item.dataList]
//         }else{
//           seriesData[item.name].data.push(item.memoryPercent);
//         }
//     })
    
//     setSeries(Object.values(seriesData));
//     setDatetimes(vmUsage.time);
//   }, [vmUsage]);

//   return (
//     <AreaChart 
//       series={series}
//       datetimes={datetimes} 
//     />
//   );
// };



// const SuperAreaChart = () => {
//   const [series, setSeries] = useState([{
//     name: 'series1',
//     data: [31, 40, 28, 51, 42, 109, 100] // 물결그래프 값
//   }, {
//     name: 'series2',
//     data: [11, 32, 45, 82, 34, 52, 41]
//   }, {
//     name: 'series3',
//     data: [20, 30, 40, 50, 60, 70, 80],
//   }]);

//   return (
//     <AreaChart 
//       series={series}
//       datetimes={[
//         "2018-09-19T00:00:00.000Z",
//         "2018-09-19T01:30:00.000Z",
//         "2018-09-19T02:30:00.000Z",
//         "2018-09-19T03:30:00.000Z",
//         "2018-09-19T04:30:00.000Z",
//         "2018-09-19T05:30:00.000Z",
//         "2018-09-19T06:30:00.000Z"
//       ]} 
//     />
//   );
// }