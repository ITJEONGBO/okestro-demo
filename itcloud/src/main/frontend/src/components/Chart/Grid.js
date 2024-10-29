import React, { useEffect, useState } from 'react';
import './Grid.css';

const Grid = ({ type, data = [] }) => {
  const [gridData, setGridData] = useState([]);

  useEffect(() => {
    setGridData(data);
  }, [data]);

  const getBackgroundColor = (value) => {
    if (value >= 0 && value <= 60) {
      return '#00BFFF';
    } else if (value > 60 && value <= 75) {
      return '#1ec033';
    } else if (value > 75 && value <= 100) {
      return '#ff0000';
    }
    return 'white';
  };

  return (
    <div className="grid-container">
      {gridData.map((item) => (
        <div
          key={item.id}
          className="grid-item"
          title={`Name: ${item.name}`}
          style={{ backgroundColor: type === 'cpu' ? getBackgroundColor(item.cpuPercent) : getBackgroundColor(item.memoryPercent)}}
        >
          <div className="percent">{type==='cpu' ? item.cpuPercent: item.memoryPercent}%</div>
        </div>
      ))}
    </div>
  );
};

export default Grid;


// const Grid = ({ type, data = [] }) => {
//   const [gridData, setGridData] = useState([]);

//   useEffect(() => {
//     setGridData(data);
//   }, [data]);

//   return (
//     <div className="grid-container">
//       {gridData.map((item) => (
//         <div
//           key={item.id}
//           className="grid-item"
//           title={`Name: ${item.name}`}
//           // title={`Name: ${item.name}, CPU Percent: ${item.cpuPercent}%, Memory Percent: ${item.memoryPercent}%, Network Percent: ${item.networkPercent}%`}
//         >
//           {/* <div className="item-name">{item.name}</div> */}
          
//           <div className="cpu-percent">{item.cpuPercent}%</div>
//           {/* <div className="cpu-percent">CPU: {item.cpuPercent}%</div> */}
//           {/* <div className="memory-percent">Memory: {item.memoryPercent}%</div>
//           <div className="network-percent">Network: {item.networkPercent}%</div> */}
//         </div>
//       ))}
//     </div>
//   );
// };

// export default Grid;