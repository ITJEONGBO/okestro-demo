import React, { useEffect } from "react";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const VmBoot = ({ editMode, isos, formBootState, setFormBootState }) => {
  useEffect(() => {
    // editMode에서만 isCdDvdChecked를 설정
    if (editMode && formBootState.cdConn && !formBootState.isCdDvdChecked) {
      setFormBootState((prev) => ({
        ...prev,
        isCdDvdChecked: true,
      }));
    }

    // 새로 생성 모드에서만 초기화
    if (!editMode && !formBootState.cdConn) {
      setFormBootState((prev) => ({
        ...prev,
        isCdDvdChecked: false,
        cdConn: "",
      }));
    }
  }, [editMode]);

  const handleCdDvdCheckboxChange = (e) => {
    const isChecked = e.target.checked;
    setFormBootState((prev) => ({
      ...prev,
      isCdDvdChecked: isChecked,
      cdConn: isChecked ? prev.cdConn : "", // 체크 해제 시 선택값 초기화
    }));
  };

  const firstDeviceOptionList = [
    { value: "hd", label: "하드 디스크" },
    { value: "cdrom", label: "CD-ROM" },
    { value: "network", label: "네트워크(PXE)" },
  ];

  const secDeviceOptionList = [
    { value: "", label: "없음" },
    { value: "cdrom", label: "CD-ROM" },
    { value: "network", label: "네트워크(PXE)" },
  ];

  return (
    <div className="boot_outer_content">
      <div className="cpu-res">
        <span style={{ fontWeight: 600 }}>부트순서:</span>
        <div className="cpu-res-box">
          <span>첫 번째 장치</span>
          <select
            id="first_boot_device"
            value={formBootState.firstDevice}
            onChange={(e) =>
              setFormBootState((prev) => ({
                ...prev,
                firstDevice: e.target.value,
              }))
            }
          >
            {firstDeviceOptionList.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </div>

        <div className="cpu-res-box">
          <span>두 번째 장치</span>
          <select
            id="second_boot_device"
            value={formBootState.secDevice}
            onChange={(e) =>
              setFormBootState((prev) => ({
                ...prev,
                secDevice: e.target.value,
              }))
            }
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
              disabled={!formBootState.isCdDvdChecked || isos.length === 0}
              value={formBootState.cdConn}
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
            <FontAwesomeIcon
              icon={faInfoCircle}
              style={{ color: "rgb(83, 163, 255)" }}
              fixedWidth
            />
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
  );
};

export default VmBoot;
