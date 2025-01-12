import { useTemplate } from '../../../api/RQHook';

const TemplateGeneral = ({ templateId }) => {
  const { data: templateData } = useTemplate(templateId);

  return (
    <>
      <table className="table">
        <tbody>
          <tr>
            <th>이름:</th>
            <td>{templateData?.name}</td>
          </tr>
          <tr>
            <th>설명:</th>
            <td>{templateData?.description}</td>
          </tr>
          <tr>
            <th>호스트 클러스터:</th>
            <td>{templateData?.clusterVo?.name}</td>
          </tr>
          <tr>
            <th>운영 시스템:</th>
            <td>{templateData?.osSystem}</td>
          </tr>
          <tr>
            <th>칩셋/펌웨어 유형:</th>
            <td>{templateData?.chipsetFirmwareType}</td>
          </tr>
          <tr>
            <th>그래픽 프로토콜:</th>
            <td>N/A</td>
          </tr>
          <tr>
            <th>비디오 유형:</th>
            <td>N/A</td>
          </tr>
          <tr>
            <th>최적화 옵션:</th>
            <td>{templateData?.optimizeOption}</td>
          </tr>
        </tbody>
      </table>
      <table className="table">
        <tbody>
          <tr>
            <th>설정된 메모리:</th>
            <td>{templateData?.memorySize} GB</td>
          </tr>
          <tr>
            <th>CPU 코어 수:</th>
            <td>{templateData?.cpuTopologyCore}</td>
          </tr>
          <tr>
            <th>모니터 수:</th>
            <td>{templateData?.monitor}</td>
          </tr>
          <tr>
            <th>고가용성:</th>
            <td>{templateData?.ha ? "예" : "아니오"}</td>
          </tr>
          <tr>
            <th>우선 순위:</th>
            <td>{templateData?.priority}</td>
          </tr>
          <tr>
            <th>USB:</th>
            <td>{templateData?.usb ? "사용" : "사용 안 함"}</td>
          </tr>
          <tr>
            <th>소스:</th>
            <td>N/A</td>
          </tr>
          <tr>
            <th>상태 비저장:</th>
            <td>{templateData?.stateless ? "예" : "아니오"}</td>
          </tr>
          <tr>
            <th>템플릿 ID:</th>
            <td>{templateData?.id}</td>
          </tr>
        </tbody>
      </table>
    </>
  );
};

export default TemplateGeneral;
