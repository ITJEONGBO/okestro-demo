import { faCheck, faExclamation, faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableOuter from '../../table/TableOuter';

// 이벤트 섹션
const EventSection = () => {
    const columns = [
      { header: '', accessor: 'icon', clickable: false },
      { header: '시간', accessor: 'time', clickable: false },
      { header: '메세지', accessor: 'message', clickable: false },
      { header: '상관 관계 ID', accessor: 'correlationId', clickable: false },
      { header: '소스', accessor: 'source', clickable: false },
      { header: '사용자 지정 이벤트 ID', accessor: 'customEventId', clickable: false }
    ];
    const data = [
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2024. 1. 17. PM 3:14:39', message: "Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' has been completed.", correlationId: '4b4b417a-c...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2024. 1. 17. PM 3:14:21', message: "Snapshot 'on2o-ap01-Snapshot-2024_01_17' creation for 'VM on2o-ap01' was initiated by admin@intern...", correlationId: '4b4b417a-c...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faTimes} fixedWidth/>, time: '2024. 1. 5. AM 8:37:54', message: 'Failed to restart VM on2o-ap01 on host host01.ititinfo.com', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faTimes} fixedWidth/>, time: '2024. 1. 5. PM 8:37:10', message: 'VM on2o-ap01 is down with error. Exit message: VM terminated with error.', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2024. 1. 5. PM 8:34:29', message: 'Trying to restart VM on2o-ap01 on host host01.ititinfo.com', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faExclamation} fixedWidth/>, time: '2024. 1. 5. PM 8:29:10', message: 'VM on2o-ap01 was set to the Unknown status.', correlationId: '3400e0dc', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2023. 12. 29. PM 12:55:08', message: 'VM on2o-ap01 started on Host host01.ititinfo.com', correlationId: 'a99b6ae8-8d...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2023. 12. 29. PM 12:54:48', message: 'VM on2o-ap01 was started by admin@internal-authz (Host: host01.ititinfo.com).', correlationId: 'a99b6ae8-8d...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2023. 12. 29. PM 12:54:18', message: 'VM on2o-ap01 configuration was updated by admin@internal-authz.', correlationId: 'e3b8355e-06...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2023. 12. 29. PM 12:54:15', message: 'VM on2o-ap01 configuration was updated by admin@internal-authz.', correlationId: '793fb95e-6df...', source: 'oVirt', customEventId: '' },
      { icon: <FontAwesomeIcon icon={faCheck} fixedWidth/>, time: '2023. 12. 29. PM 12:53:53', message: 'VM on2o-ap01 has been successfully imported from the given configuration.', correlationId: 'ede53bc8-c6...', source: 'oVirt', customEventId: '' }
    ];
    return (
      <div className="host_empty_outer">
          <TableOuter 
            columns={columns}
            data={data}
            onRowClick={() => console.log('Row clicked')} 
          />
      </div>
    );
  };
  
  export default EventSection;
  