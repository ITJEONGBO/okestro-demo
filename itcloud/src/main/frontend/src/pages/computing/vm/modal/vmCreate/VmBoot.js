import React, { useEffect } from "react";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import CustomSelect from "../../../../../utils/CustomSelect";

const VmBoot = ({ editMode, isos, formBootState, setFormBootState }) => {

  useEffect(() => {
    // Keep `isCdDvdChecked` consistent with `cdConn`
    if (formBootState.cdConn && !formBootState.isCdDvdChecked) {
      setFormBootState((prev) => ({
        ...prev,
        isCdDvdChecked: true,
      }));
    } else if (!formBootState.cdConn && formBootState.isCdDvdChecked) {
      setFormBootState((prev) => ({
        ...prev,
        isCdDvdChecked: false,
      }));
    }
  }, [formBootState.cdConn]);


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

        <CustomSelect
          className="cpu-res-box"
          label="첫 번째 장치"
          value={formBootState.firstDevice}
          onChange={(e) =>
            setFormBootState((prev) => ({
              ...prev,
              firstDevice: e.target.value,
            }))
          }
          options={firstDeviceOptionList}
        /> 
        <CustomSelect
          className="cpu-res-box"
          label="두 번째 장치"
          value={formBootState.secDevice}
            onChange={(e) =>
              setFormBootState((prev) => ({
                ...prev,
                secDevice: e.target.value,
              }))
            }
          options={secDeviceOptionList}
        /> 
      </div>

      <div className="boot-checkboxs">
        <div>
          <div className="checkbox_group">
            <input
              type="checkbox"
              id="connectCdDvd"
              name="connectCdDvd"
              checked={formBootState.isCdDvdChecked}
              onChange={(e) => {
                const isChecked = e.target.checked;
                setFormBootState((prev) => ({
                  ...prev,
                  isCdDvdChecked: isChecked,
                  cdConn: isChecked ? (prev.cdConn || (isos[0]?.id || "")) : "",
                }));
              }}
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
  );
};

export default VmBoot;
