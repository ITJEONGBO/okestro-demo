import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Grid.css';

const Grid = ({ type, data = [] }) => {
  const [gridData, setGridData] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    // 데이터를 15개로 채움
    const filledData = [...data];
    while (filledData.length < 15) {
      filledData.push({ id: `placeholder-${filledData.length}`, cpuPercent: null, memoryPercent: null, name: '' });
    }
    setGridData(filledData);
  }, [data]);

  const getBackgroundColor = (value) => {
    if (value === null || value === 0) return 'rgb(219 242 255)';
    if (value >= 0 && value <= 10) return 'rgb(255 231 163)';
    if (value > 10 && value <= 30) return 'rgb(255 185 98)';
    if (value > 30 && value <= 60) return '#fb9f2c';
    if (value > 60 && value <= 75) return 'rgb(255 106 0)';
    if (value > 75 && value <= 100) return 'rgb(255 97 120)';
    return 'white';
  };

  const handleClick = (id) => {
    if (id && id.startsWith('placeholder')) return; // placeholder 클릭 방지
    if (type === 'domain') {
      navigate(`/storages/domains/${id}`);
    } else {
      navigate(`/computing/vms/${id}`);
    } 
  };

  return (
    <div className="grid-container">
      {gridData.map((item, index) => (
        <div
          key={item.id || index}
          className="grid-item"
          onClick={() => handleClick(item.id)}
          title={item.name}
          style={{
            backgroundColor: type === 'cpu' ? getBackgroundColor(item.cpuPercent) : getBackgroundColor(item.memoryPercent),
          }}
        >
          
          {item.cpuPercent !== null && item.memoryPercent !== null ? (
            <div className="percent">{type === 'cpu' ? item.cpuPercent : item.memoryPercent}%</div>
          ) : (
            <div className="percent placeholder" style={{ color: 'rgb(219 242 255)' }}>.</div>
          )}
        </div>
      ))}
    </div>
  );
};

export default Grid;
