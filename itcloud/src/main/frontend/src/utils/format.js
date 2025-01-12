import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay, faPencil, faWrench, faQuestionCircle, faRefresh, faArrowsUpToLine, faFaceSmileBeam, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { Tooltip } from 'react-tooltip';


/***
 * 숫자 구분
 */
export function formatNumberWithCommas(number) {
  return number.toLocaleString(); // Locale 기반 쉼표 포맷
}

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
export function icon(status) {
  const tooltipId = `status-tooltip-${status}`;
  let iconProps = {};

  switch (status) {
    case 'UP':
      iconProps = {
        icon: faPlay,
        style: { color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' },
      };
      break;
    case 'ACTIVE':
      iconProps = {
        icon: faPlay,
        style: { color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' },
      };
      break;
    case 'DOWN':
      iconProps = {
        icon: faPlay,
        style: { color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' },
      };
      break;
    case 'POWERING_DOWN':
      iconProps = {
        icon: faArrowsUpToLine,
        style: { color: 'red', fontSize: '0.3rem', transform: 'rotate(180deg)' },
      };
      break;
    case 'POWERING_UP':
      iconProps = {
        icon: faSpinner,
        style: { color: 'orange', fontSize: '0.3rem', transform: 'rotate(180deg)' },
      };
      break;
    case 'MAINTENANCE':
      iconProps = {
        icon: faWrench,
        style: { color: 'black', fontSize: '0.3rem' },
      };
      break;
    case 'REBOOT':
      iconProps = {
        icon: faRefresh,
        style: { color: 'black', fontSize: '0.3rem' },
      };
      break;
    case 'INACTIVE':
      iconProps = {
        icon: faPlay,
        style: { color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' },
    };
      break;
    case 'UNINITIALIZED':
      iconProps = {
        icon: faPlay,
        style: { color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' },
    };
      break;
    default:
      return status;
  }

  return (
    <>
      <FontAwesomeIcon
        {...iconProps}
        fixedWidth
        data-tooltip-id={tooltipId}
      />
      <Tooltip id={tooltipId} place="top" effect="solid">
        {status}
      </Tooltip>
    </>
  );
}


// const sizeToGB = (data) => (data / Math.pow(1024, 3));
// const formatSize = (size) => (sizeToGB(size) < 1 ? '< 1 GB' : `${sizeToGB(size).toFixed(0)} GB`);


export function renderDataCenterStatus (status) {
  if (status === 'UNINITIALIZED') {
    return '초기화되지 않음';
  }
  return status;
};



export function renderHostStatus (status) {
  if (status === 'UP') {
    return '실행중';
  } else if (status === 'DOWN') {
    return '중지';
  } else if (status === 'MAINTENANCE') {
    return '유지보수';
  } else if (status === 'REBOOT') {
    return '재부팅중';
  }
  return status;
};

export function renderVmStatus (status) {
  if (status === 'UP') {
    return '실행중';
  } else if (status === 'DOWN') {
    return '중지';
  } else if (status === 'MAINTENANCE') {
    return '유지보수';
  } else if (status === 'REBOOT') {
    return '재부팅중';
  }
  return status;
};

export function renderDomainStatus (status) {
  if (status === 'ACTIVE') {
    return '활성화';
  } else if (status === 'DOWN') {
    return '중지';
  } else if (status === 'INACTIVE') {
    return '비활성화';
  }
  return status;
};

export function renderDataCenterStatusIcon (status) {
  if (status === 'ACTIVE') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if (status === 'DOWN') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  } else if (status === 'MAINTENANCE') {
    return <FontAwesomeIcon icon={faWrench} fixedWidth style={{ color: 'black', fontSize: '0.3rem', }} />;
  }
  return status;
};

export function renderStatusClusterIcon (connect, status) {
  if (connect && status === 'OPERATIONAL') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'lime', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if(connect && status === 'NON_OPERATIONAL'){
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  } else if(!connect){
    return ''
  }
  return status;
};

export const renderUpDownStatusIcon = (status) => {
  if (status === 'UP') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'green', fontSize: '0.3rem', transform: 'rotate(270deg)' }} />;
  } else if (status === 'DOWN') {
    return <FontAwesomeIcon icon={faPlay} fixedWidth style={{ color: 'red', fontSize: '0.3rem', transform: 'rotate(90deg)' }} />;
  }
  return status;
};

export const renderVmStatusIcon = (status) => {
  return icon(status);
};

export const renderDomainStatusIcon = (status) => {
  return icon(status);
}

export const renderEventStatusIcon = (status) => {
  return icon(status);
};

export const renderDatacenterStatusIcon = (status) => {
  return icon(status);
};
export const renderHostStatusIcon = (status) => {
  return icon(status);
};
