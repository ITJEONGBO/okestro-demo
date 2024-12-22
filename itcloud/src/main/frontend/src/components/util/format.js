import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPencil, faWrench, faQuestionCircle, faRefresh } from '@fortawesome/free-solid-svg-icons';

/**
 * Converts bytes to megaabytes and formats the result to one decimal place.
 * @param {number} bytes - The number of bytes to convert.
 * @returns {string} The formatted size in MB.
 */
export function formatBytesToMB(bytes) {
  return (bytes / (1024 * 1024)).toFixed(0);
}

/**
 * Converts bytes to gigabytes and formats the result to one decimal place.
 * @param {number} bytes - The number of bytes to convert.
 * @returns {string} The formatted size in GB.
 */
export function formatBytesToGB(bytes) {
  return (bytes / (1024 * 1024 * 1024)).toFixed(1);
}

export function formatBytesToGBToFixedZero(bytes) {
  return (bytes / (1024 * 1024 * 1024)).toFixed(0);
}

export function sizeToBytes(size) {
  return parseInt(size, 10) * 1024 * 1024 * 1024
}

export function zeroValue(size) {
  return size < 1 ? "< 1 GB" : `${size} GB`;
}

// const sizeToGB = (data) => (data / Math.pow(1024, 3));
// const formatSize = (size) => (sizeToGB(size) < 1 ? '< 1 GB' : `${sizeToGB(size).toFixed(0)} GB`);


export function renderHostStatusIcon(status) {
  if (status === 'UP') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'green', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if (status === 'DOWN') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  } else if (status === 'MAINTENANCE') {
    return <FontAwesomeIcon icon={faWrench} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
  } else if (status === 'REBOOT') {
    return <FontAwesomeIcon icon={faRefresh} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
  }
  return status;
}

export const renderUpDownStatusIcon = (status) => {
  if (status === 'UP') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'green', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if (status === 'DOWN') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  }
  return status;
};

export const renderVmStatusIcon = (status) => {
  if (status === 'UP') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if (status === 'DOWN') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  }
  return status;
};