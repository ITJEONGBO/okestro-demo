import React, { useState } from 'react';
import { faDesktop, faMinusCircle, faPlusCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const DomainVm = ({disk}) => {

  // 데이터센터 박스떨어지게
  const [isFirstRowExpanded, setFirstRowExpanded] = useState(false);
  const [isSecondRowExpanded, setSecondRowExpanded] = useState(false);
  const toggleFirstRow = () => {
    setFirstRowExpanded(!isFirstRowExpanded);
  };

  const toggleSecondRow = () => {
    setSecondRowExpanded(!isSecondRowExpanded);
  };

    return (
        <>
 <div className="host_empty_outer">
      <div className="section_table_outer">
        <table>
          <thead>
            <tr>
              <th>별칭</th>
              <th>디스크</th>
              <th>템플릿</th>
              <th>가상 크기</th>
              <th>실제 크기</th>
              <th>생성 일자</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td onClick={toggleFirstRow} style={{ cursor: 'pointer' }}>
                <FontAwesomeIcon icon={isFirstRowExpanded ? faMinusCircle : faPlusCircle} fixedWidth />
                <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: '0 5px 0 10px' }} />
                test02
              </td>
              <td>1</td>
              <td>Blank</td>
              <td>1 GIB</td>
              <td>5 GIB</td>
              <td>2024.1.19 AM9:21:57</td>
            </tr>
          </tbody>

          {/* 첫번째 하위 행 */}
          {isFirstRowExpanded && (
            <>
              <tbody className="detail_machine_second">
                <tr>
                  <td onClick={toggleSecondRow} style={{ cursor: 'pointer' }}>
                    <FontAwesomeIcon icon={isSecondRowExpanded ? faMinusCircle : faPlusCircle} fixedWidth style={{ marginLeft: ' 15px' }}/>
                    <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: ' 0 5px 0 5px' }} />
                    he_virtio_disk
                  </td>
                  <td>90 GIB</td>
                  <td>5 GIB</td>
                  <td>90 GIB</td>
                  <td>5 GIB</td>
                  <td>2023.12.28 11:58:49</td>
                </tr>
              </tbody>

              {/* 두번째 하위 행 */}
              {isSecondRowExpanded && (
                <tbody className="detail_machine_last">
                  <tr>
                    <td>
                      <FontAwesomeIcon icon={faDesktop} fixedWidth style={{ margin: '0 5px 0 50px' }} />
                      Active VM
                    </td>
                    <td>90 GIB</td>
                    <td>5 GIB</td>
                    <td>90 GIB</td>
                    <td>5 GIB</td>
                    <td>2023.12.28 11:58:49</td>
                  </tr>
                </tbody>
              )}
            </>
          )}
        </table>
      </div>
    </div>
        </>
    );
  };
  
  export default DomainVm;