import React, { Suspense } from 'react';
import TemplateEditModal from './TemplateEditModal';

const TemplateModals = ({ isModalOpen, action, onRequestClose, selectedTemplate }) => {
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
        />
      ): action === 'delete' ? (
        <DeleteModal // 삭제되는데 시간걸림
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
