import TableOuter from "../../table/TableOuter";

// 애플리케이션 섹션
const ApplicationSection = () => {
    const columns = [
      { header: '설치된 애플리케이션', accessor: 'application', clickable: false },
    ];
  
    const data = [
      { application: 'kernel-3.10.0-1062.el7.x86_64' },
      { application: 'qemu-guest-agent-2.12.0' },
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
  
  export default ApplicationSection;