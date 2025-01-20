import { useState } from 'react';

const useVmForm = (initialState) => {
  const [formState, setFormState] = useState(initialState);

  const updateField = (key, value) => {
    setFormState((prev) => ({ ...prev, [key]: value }));
  };

  const resetForm = () => {
    setFormState(initialState);
  };

  return { formState, updateField, resetForm };
};

export default useVmForm;