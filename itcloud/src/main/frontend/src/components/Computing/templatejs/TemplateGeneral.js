// 애플리케이션 섹션
const TemplateGeneral = ({ templateId }) => {
  return (
    <div className="cluster_general">
      <div className="table_container_center">
          <table className="table">
            <tbody>
                <tr>
                  <th>이름:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>설명:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>호스트 클러스터:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>운영 시스템:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>칩셋/펌웨어 유형:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>그래픽 프로토콜:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>비디오 유형:</th>
                  <td></td>
                </tr>
                <tr>
                  <th>최적화 옵션:</th>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>
          <div className="table_container_center">
            <table className="table">
            <tbody>
              <tr>
                <th>설정된 메모리:</th>
                <td></td>
              </tr>
              <tr>
                <th>CPU 코어 수:</th>
                <td></td>
              </tr>
              <tr>
                <th>모니터 수:</th>
                <td></td>
              </tr>
              <tr>
                <th>고가용성:</th>
                <td></td>
              </tr>
              <tr>
                <th>우선 순위:</th>
                <td></td>
              </tr>
              <tr>
                <th>USB:</th>
                <td></td>
              </tr>
              <tr>
                <th>소스:</th>
                <td></td>
              </tr>
              <tr>
                <th>상태 비저장:</th>
                <td></td>
              </tr>
              <tr>
                <th>템플릿 ID:</th>
                <td></td>
              </tr>
            </tbody>
          </table>
      </div>
     </div>
 
  );
};

export default TemplateGeneral;