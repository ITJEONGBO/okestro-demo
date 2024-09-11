import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons'
import './DashboardBoxGroup.css'

const DashboardBox = ({ title, cntTotal, cntUp, cntDown, navigatePath }) => {
  const navigate = useNavigate();
  
  return (
    <div className="box" onClick={() => navigatePath && navigate(navigatePath)}>
      <span><p>{title}</p></span>
      <h1>{cntTotal}</h1>
      <div className="arrows">
        {cntUp && <><FontAwesomeIcon icon={faArrowUp} fixedWidth/> {cntUp}&nbsp;</>}
        {cntDown && <><FontAwesomeIcon icon={faArrowDown} fixedWidth/> {cntDown}</>}
      </div>
    </div>
  )
}

const DashboardBoxGroup = ({ boxItems }) => {
  return (
    <div className="dash_boxs">
      {boxItems && boxItems.map((e, i) => (
        <DashboardBox  
          key={i}
          title={e.title}
          cntTotal={e.cntTotal}
          cntUp={e.cntUp}
          cntDown={e.cntDown}
          navigatePath={e.navigatePath} />
      ))}
    </div>
  )
}

export default DashboardBoxGroup