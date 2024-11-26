import React, { Suspense } from 'react';

const TemplateModals = ({ isModalOpen, action, onRequestClose, selectedTemplate }) => {
  const TemplateModal = React.lazy(() => import('../Modal/TemplateModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'add' ? (
        <span>생성</span>
      ) : action === 'edit' ? (
        <TemplateModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          templateId={selectedTemplate?.id || null}
        />
      ): action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Template"
          onRequestClose={onRequestClose}
          contentLabel="템플릿"
          data={selectedTemplate}
        />
      ): null}
    </Suspense>
  );
};

export default TemplateModals;
