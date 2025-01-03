import React, { Suspense } from 'react';
import '../Modal/css/MTemplate.css';

const TemplateModals = ({ isModalOpen, action, onRequestClose, selectedTemplate,selectedTemplates }) => {
  const TemplateEditModal = React.lazy(() => import('../Modal/TemplateEditModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'add' ? (
        <span>생성</span>
      ) : action === 'edit' ? (
        <TemplateEditModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={true}
          templateId={selectedTemplate?.id}
          selectedTemplate={selectedTemplate}
        />
      ): action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Template"
          onRequestClose={onRequestClose}
          contentLabel="템플릿"
          data={selectedTemplates}
        />
      ): null}
    </Suspense>
  );
};

export default TemplateModals;
