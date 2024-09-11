import './IconButton.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const IconButton = ({id, key, label, icon, onClick}) => {

  return (
    <>
    <button id={id} key={key} onClick={onClick} className="icon-button-conatiner">
      <span className="icon-button-conatiner">
        <FontAwesomeIcon icon={icon} className="input-icon" fixedWidth/>
      </span>
      <p>{label}</p>
      </button>
    </>
  )
}

export default IconButton
