import React from 'react';

const LabelInput = ({ label, id, value, onChange, disabled }) => (
  <div>
    <label htmlFor={id}>{label}</label>
    <input
      type="text"
      id={id}
      value={value}
      onChange={onChange}
      disabled={disabled}
    />
  </div>
);

export default LabelInput;