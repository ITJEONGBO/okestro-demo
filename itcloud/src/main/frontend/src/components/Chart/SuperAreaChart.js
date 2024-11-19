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
    return `${hours}:${minutes}`; // 시간순으로 출력 (11:01, 11:02 등)
  };

  useEffect(() => {
    if (vmPer) {
      // 데이터를 시간순으로 정렬
      const sortedData = vmPer.map(item => {
        // time과 dataList를 묶어 정렬
        const combined = item.time.map((time, index) => ({
          time,
          value: item.dataList[index],
        })).sort((a, b) => new Date(a.time) - new Date(b.time)); // 시간순 정렬

        return {
          name: item.name,
          data: combined.map(({ time, value }) => ({
            x: formatDate(time),
            y: value,
          })),
        };
      });

      const sortedTimes = vmPer[0].time
        .map(formatDate)
        .sort((a, b) => new Date(a) - new Date(b)); // 시간순 정렬

      setSeries(sortedData);
      setDatetimes(sortedTimes);
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


