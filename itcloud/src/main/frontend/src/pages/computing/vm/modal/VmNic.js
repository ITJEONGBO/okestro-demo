import React, { useState } from 'react';

const VmNic = ({ editMode, initialNics = [], availableProfiles = [] }) => {
  const [nics, setNics] = useState(initialNics.length > 0 ? initialNics : [{ id: '', name: 'nic1', vnicProfileVo: { id: '' } }]);

  const handleAddNic = () => {
    const newNicNumber = nics.length + 1;
    const newNic = { id: '', name: `nic${newNicNumber}`, vnicProfileVo: { id: '' } };
    setNics([...nics, newNic]);
  };

  const handleRemoveNic = (index) => {
    const updatedNics = nics.filter((_, nicIndex) => nicIndex !== index);
    setNics(updatedNics);
  };

  const handleNicChange = (index, field, value) => {
    const updatedNics = [...nics];
    updatedNics[index] = {
      ...updatedNics[index],
      [field]: value,
      vnicProfileVo: field === 'vnicProfileVo' ? { id: value } : updatedNics[index].vnicProfileVo,
    };
    setNics(updatedNics);
  };

  return (
    <div>
      <p>vNIC 프로파일을 선택하여 가상 머신 네트워크 인터페이스를 인스턴스화합니다.</p>
      {nics.map((nic, index) => (
        <div
          key={index}
          className="edit_fourth_content_row"
          style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}
        >
          <div
            className="edit_fourth_content_select"
            style={{ flex: 1, display: 'flex', alignItems: 'center' }}
          >
            <label
              htmlFor={`network_adapter_${index}`}
              style={{ marginRight: '10px', width: '100px' }}
            >
              {nic.name}
            </label>
            <select
              id={`network_adapter_${index}`}
              style={{ flex: 1 }}
              value={nic.vnicProfileVo?.id || ''}
              onChange={(e) => handleNicChange(index, 'vnicProfileVo', e.target.value)}
            >
              <option value="">항목을 선택하십시오...</option>
              {availableProfiles.map((profile) => (
                <option key={profile.id} value={profile.id}>
                  {profile.name}/{profile.networkVoName}
                </option>
              ))}
            </select>
          </div>
          <div style={{ display: 'flex', marginLeft: '10px' }}>
            {index === nics.length - 1 && (
              <button onClick={handleAddNic} style={{ marginRight: '5px' }}>+</button>
            )}
            {nics.length > 1 && (
              <button onClick={() => handleRemoveNic(index)}>-</button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

export default VmNic;
