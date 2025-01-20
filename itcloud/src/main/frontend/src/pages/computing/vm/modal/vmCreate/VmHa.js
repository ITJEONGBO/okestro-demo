import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useAllActiveDomainFromDataCenter } from "../../../../../api/RQHook";
import { useEffect, useState } from "react";

const VmHa = ({ editMode, dataCenterId, formHaState, setFormHaState }) => {
  const {
    data: domains = [],
    isLoading: isDomainsLoading
  } = useAllActiveDomainFromDataCenter(dataCenterId, (e) => ({...e,}));

  useEffect(() => {
    // 도메인 기본값 설정
    if (!editMode && domains.length > 0) {
      setFormHaState((prev) => ({
        ...prev,
        domainVoId: domains[0].id, // 첫 번째 도메인 ID 선택
      }));
    }
  }, [domains, editMode, setFormHaState]);

  // 고가용성 체크박스 변경 핸들러
  const handleHaCheckboxChange = (e) => {
    const isChecked = e.target.checked;
    setFormHaState((prev) => ({
      ...prev,
      ha: isChecked,
      domainVoId: isChecked && domains.length > 0 ? domains[0].id : "", // 체크 시 첫 번째 도메인 선택
    }));
  };

  return (
    <>
    <div className="ha-mode-second-content">
      <div className="checkbox_group">
        <input
          className="check_input"
          type="checkbox"
          id="ha_mode_box"
          checked={formHaState.ha}
          onChange={handleHaCheckboxChange}
        />
        <label className="check_label" htmlFor="ha_mode_box">
          고가용성
        </label>
      </div>

      <div>
        <div>
          <span>가상 머신 임대 대상 스토리지 도메인</span>
          <FontAwesomeIcon icon={faInfoCircle} style={{ color: "rgb(83, 163, 255)" }} fixedWidth />
        </div>
        <select
          id="no_lease"
          disabled={!formHaState.ha}
          value={formHaState.domainVoId || ""}
          onChange={(e) =>
            setFormHaState((prev) => ({
              ...prev,
              domainVoId: e.target.value,
            }))
          }
        >
          <option value="">가상 머신 임대 없음</option>
          {domains.map((opt) => (
            <option key={opt.id} value={opt.id}>
              {opt.name}
            </option>
          ))}
        </select>
      </div>
      
      {/* <div>
        <div>
          <span>재개 동작</span>
          <FontAwesomeIcon
            icon={faInfoCircle}
            style={{ color: "rgb(83, 163, 255)" }}
            fixedWidth
          />
        </div>
        <select id="force_shutdown">
          <option value="강제 종료">강제 종료</option>
        </select>
      </div> */}

      <div className="ha-mode-article">
        <span>실행/마이그레이션 큐에서 우선순위 : </span>
        <div>
          <span>우선 순위</span>
          <select
            id="priority"
            value={formHaState.priority} // 선택된 값
            onChange={(e) =>
              setFormHaState((prev) => ({
                ...prev,
                priority: e.target.value,
              }))
            }
          >
            {[
              { value: 1, label: "낮음" },
              { value: 50, label: "중간" },
              { value: 100, label: "높음" },
            ].map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
    </>
  );
};
export default VmHa;
  