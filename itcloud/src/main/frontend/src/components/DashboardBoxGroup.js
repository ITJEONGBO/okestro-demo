import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowUp, faArrowDown, faBatteryEmpty, faStarOfLife, faLink } from '@fortawesome/free-solid-svg-icons'
import './DashboardBoxGroup.css'

const DashboardBox = ({ title, cntTotal, cntUp, cntDown, alert, error, navigatePath }) => {
  const navigate = useNavigate();
  
  return (
    <div className="box" onClick={() => navigatePath && navigate(navigatePath)}>
      <span><p>{title}</p></span>
      <h1>{cntTotal}</h1>
      <div className="arrows">
        {cntUp && <><FontAwesomeIcon icon={faArrowUp} fixedWidth/> {cntUp}&nbsp;</>}
        {cntDown && <><FontAwesomeIcon icon={faArrowDown} fixedWidth/> {cntDown}</>}
        {alert && <><FontAwesomeIcon icon={faStarOfLife} fixedWidth/> {alert}&nbsp;</>}
        {error && <><FontAwesomeIcon icon={faBatteryEmpty} fixedWidth/> {error}</>}
        {error && <><FontAwesomeIcon icon={faLink} fixedWidth/> {error}</>}{/*샘플 */}
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
          alert={e.alert}
          error={e.error}
          navigatePath={e.navigatePath} />
      ))}
    </div>
  )
}

export default DashboardBoxGroup