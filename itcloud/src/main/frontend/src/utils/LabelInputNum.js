import React from 'react';

const LabelInputNum = ({ label, id, value, onChange, disabled }) => (
  <div>
    <label htmlFor={id}>{label}</label>
    <input
      type="number"
      id={id}
      value={value}
      onChange={onChange}
      disabled={disabled}
      min="1"
    />
  </div>
);

export default LabelInputNum;