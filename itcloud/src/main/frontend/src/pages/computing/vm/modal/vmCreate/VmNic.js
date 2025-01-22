// import { useEffect, useState } from "react";

// const VmNic = ({ editMode, value = [], nicList = [], onNicsChange }) => {
//   const [nics, setNics] = useState(
//     value.length > 0 ? value.map((nic, index) => ({
//         ...nic,
//         name: nic.name || `nic${index + 1}`,
//         vnicProfileVo: { id: nic.vnicProfileVo?.id || '' },
//       }))
//     : [{ id: '', name: 'nic1', vnicProfileVo: { id: '' } }]
//   );

//   // 선택된 vNIC 데이터만 부모 컴포넌트에 전달
//   useEffect(() => {
//     const selectedNics = nics
//       .filter((nic) => nic.vnicProfileVo?.id) // 선택된 vnicProfile만 포함
//       .map((nic) => ({ id: nic.vnicProfileVo.id }));
//     onNicsChange(selectedNics);
//   }, [nics, onNicsChange]);

//   // NIC 추가
//   const handleAddNic = () => {
//     const newNicNumber = nics.length + 1;
//     const newNic = { id: '', name: `nic${newNicNumber}`, vnicProfileVo: { id: '' } };
//     setNics([...nics, newNic]);
//   };

//   // NIC 제거
//   const handleRemoveNic = (index) => {
//     const updatedNics = nics.filter((_, nicIndex) => nicIndex !== index);
//     setNics(updatedNics);
//   };

//   // NIC 변경
//   const handleNicChange = (index, value) => {
//     const updatedNics = [...nics];
//     updatedNics[index] = {
//       ...updatedNics[index],
//       vnicProfileVo: { id: value },
//     };
//     setNics(updatedNics);
//   };

//   return (
//     <div>
//       <p>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 설정하세요.</p>
//       {nics.map((nic, index) => (
//         <div
//           key={index}
//           className="edit_fourth_content_row"
//           style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}
//         >
//           <div
//             className="edit_fourth_content_select"
//             style={{ flex: 1, display: 'flex', alignItems: 'center' }}
//           >
//             <label
//               htmlFor={`network_adapter_${index}`}
//               style={{ marginRight: '10px', width: '100px' }}
//             >
//               {nic.name}
//             </label>
//             <select
//               id={`network_adapter_${index}`}
//               style={{ flex: 1 }}
//               value={nic.vnicProfileVo?.id || ''}
//               onChange={(e) => handleNicChange(index, e.target.value)}
//             >
//               <option value="">항목을 선택하십시오...</option>
//               {nicList.map((profile) => (
//                 <option key={profile.id} value={profile.id}>
//                   {profile.name} / {profile.networkVo?.name || ''}
//                 </option>
//               ))}
//             </select>
//           </div>

//           <div style={{ display: 'flex', marginLeft: '10px' }}>
//             {index === nics.length - 1 && !!nic.vnicProfileVo.id && (
//               <button onClick={handleAddNic} style={{ marginRight: '5px' }}>+</button>
//             )}
//             {nics.length > 1 && (
//               <button onClick={() => handleRemoveNic(index)}>-</button>
//             )}
//           </div>
//         </div>
//       ))}
//     </div>
//   );
// };

// export default VmNic;
