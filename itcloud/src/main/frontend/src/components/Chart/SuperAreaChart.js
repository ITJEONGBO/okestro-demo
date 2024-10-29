import React, { useEffect, useState } from 'react';
import AreaChart from './AreaChart';

const SuperAreaChart = ({vmPer}) => {
  const [series, setSeries] = useState([]);
  const [datetimes, setDatetimes] = useState([]);

  useEffect(() => {
    if (vmPer) {
      const transformedSeries = vmPer.map(item => ({
        name: item.name,
        data: item.dataList,
      }));
      
      console.log(vmPer[2].name)
      console.log(transformedSeries)
      setSeries(transformedSeries);
      setDatetimes(vmPer[0].time);
    }
  }, [vmPer]);

  return (
    <AreaChart 
      series={series.reverse()}
      datetimes={datetimes.reverse()}
    />
  );
};



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



export default SuperAreaChart;