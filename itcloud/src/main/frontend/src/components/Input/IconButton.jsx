import './IconButton.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { } from '@fortawesome/free-solid-svg-icons';

const IconButton = ({
  id, 
  // key, 
  label, 
  icon, 
  onClick
}) => {
  return (
    // <button id={id} key={key} onClick={onClick} className="icon-button-container">
    <button id={id} onClick={onClick} className="icon-button-container">
      {icon && ( // icon이 존재할 경우에만 span과 FontAwesomeIcon 렌더링
        <span className="icon-button-container">
          <FontAwesomeIcon icon={icon} className="input-icon" fixedWidth />
        </span>
      )}
      <p>{label}</p>
    </button>
  );
}

export default IconButton;
