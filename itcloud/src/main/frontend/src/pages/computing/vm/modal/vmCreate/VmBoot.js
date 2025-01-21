import { useEffect } from "react";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useCDFromDataCenter } from "../../../../../api/RQHook";

const VmBoot = ({ editMode, vmId, dataCenterId, formBootState, setFormBootState }) => {
  const {
    data: isos = [],
    isLoading: isIsoLoading
  } = useCDFromDataCenter(dataCenterId, (e) => ({...e}));

  // ISO 목록 로드 시 초기 값 설정 (선택사항으로 두고 기본값 없음)
  useEffect(() => {
    if (!editMode && isos.length > 0) {
      setFormBootState((prev) => ({
        ...prev,
        cdConn: "", // 초기값을 빈 문자열로 설정
      }));
    }
  }, [isos, editMode, setFormBootState]);

  // CD/DVD 체크박스 핸들러
  const handleCdDvdCheckboxChange = (e) => {
    const isChecked = e.target.checked;
    setFormBootState((prev) => ({
      ...prev,
      isCdDvdChecked: isChecked,
      cdConn: isChecked ? prev.conn : "", // 체크 해제 시 선택값 초기화
    }));
  };
  
  // 부트옵션(첫번째 장치)
  const firstDeviceOptionList = [
    { value: 'hd', label: '하드 디스크' },
    { value: 'cdrom', label: 'CD-ROM' },
    { value: 'network', label: '네트워크(PXE)' },
  ];
  // 부트옵션(두번째 장치)
  const secDeviceOptionList = [
    { value: '', label: '없음' },
    { value: 'cdrom', label: 'CD-ROM' },
    { value: 'network', label: '네트워크(PXE)' },
  ];


  return (
    <>
    <div className='boot_outer_content'>
    <span>데이터센터ID: {dataCenterId}</span>
      <div className="cpu-res">
        <span style={{ fontWeight: 600 }}>부트순서:</span>
        <div className='cpu-res-box'>
          <span>첫 번째 장치</span>
          <select
            id="first_boot_device"
            value={formBootState.firstDevice}
            onChange={(e) => setFormBootState((prev) => ({ ...prev, firstDevice: e.target.value }))}
          >
            {firstDeviceOptionList.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>

        <div className='cpu-res-box'>
          <span>두 번째 장치</span>
          <select
            id="second_boot_device"
            value={formBootState.secDevice}
            onChange={(e) => setFormBootState((prev) => ({ ...prev, secDevice: e.target.value }))}
          >
            {secDeviceOptionList.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="boot-checkboxs">
        <div>
          <div className="checkbox_group">
            <input
              type="checkbox"
              id="connectCdDvd"
              name="connectCdDvd"
              checked={formBootState.isCdDvdChecked}
              onChange={handleCdDvdCheckboxChange}
            />
            <label htmlFor="connectCdDvd">CD/DVD 연결</label>
          </div>
          <div className="text_icon_box">
          <select
            id="cd_dvd_select"
            disabled={!formBootState.isCdDvdChecked || isos.length === 0} // 체크박스 상태와 ISO 목록 조건 추가
            value={formBootState.cdConn || ""}
            onChange={(e) =>
              setFormBootState((prev) => ({
                ...prev,
                cdConn: e.target.value,
              }))
            }
          >
            <option value="">CD/DVD 선택...</option>
            {isos.map((cd) => (
              <option key={cd.id} value={cd.id}>
                {cd.name}
              </option>
            ))}
          </select>
            <FontAwesomeIcon icon={faInfoCircle} style={{ color: "rgb(83, 163, 255)" }} fixedWidth />
          </div>
        </div>
        <div className="checkbox_group mb-1.5">
          <input
            className="check_input"
            type="checkbox"
            id="enableBootMenu"
            checked={formBootState.bootingMenu}
            onChange={(e) =>
              setFormBootState((prev) => ({
                ...prev,
                bootingMenu: e.target.checked,
              }))
            }
          />
          <label className="check_label" htmlFor="enableBootMenu">
            부팅 메뉴를 활성화
          </label>
        </div>
      </div>
    </div>
    </>
  );
};
export default VmBoot;
