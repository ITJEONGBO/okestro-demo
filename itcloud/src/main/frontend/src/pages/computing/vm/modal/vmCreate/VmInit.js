const VmInit = ({ editMode, formCloudInitState, setFormCloudInitState }) => {
  
  return (
    <>
    <div className='p-1.5'>
      <div className='checkbox_group mb-1.5'>
        <input 
          type="checkbox" 
          id="enableBootMenu" 
          name="enableBootMenu" 
          checked={formCloudInitState.cloudInit} // cloudInit 상태를 checked 속성에 바인딩
          onChange={(e) => setFormCloudInitState((prev) => ({ ...prev, cloudInit: e.target.value }))}
          // onChange={(e) => {
          //   setCloudInit(e.target.checked); // 상태 업데이트
          //   if (!e.target.checked) {
          //     setDomainHiddenBoxVisible(false); // 체크 해제 시 숨김 처리
          //   }
          // }}
        />
        <label htmlFor="enableBootMenu">Cloud-lnit</label>
      </div>
    </div>
    </>
  );
};
export default VmInit;
