import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllTemplates } from '../../../api/RQHook';

// const TemplateModal = React.lazy(() => import('../../Modal/TemplateModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const Templates = () => {
  const navigate = useNavigate();
  
  const { 
    data: templates, 
    status: templatesStatus,
    isRefetching: isTemplatesRefetching,
    refetch: refetchTemplates, 
    isError: isTemplatesError, 
    error: templatesError, 
    isLoading: isTemplatesLoading,
  } = useAllTemplates((e) => {
    return {
        ...e,
    }
  });

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedTemplate, setSelectedTemplate] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
      navigate(`/computing/templates/${id}`);
  };


  return (
    <>
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedTemplate?.id && toggleModal('edit', true)} disabled={!selectedTemplate?.id}>편집</button>
        <button onClick={() => selectedTemplate?.id && toggleModal('delete', true)} disabled={!selectedTemplate?.id}>제거</button>
      </div>
      <span>id = {selectedTemplate?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.TEMPLATES} 
        data={templates} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedTemplate(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {/* {(modals.create || (modals.edit && selectedTemplate)) && (
          <TemplateModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedTemplate?.id || null}
          />
        )} */}
        {modals.delete && selectedTemplate && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Template'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'템플릿'}
            data={selectedTemplate}
          />
        )}
      </Suspense>
    </>
  );
};

export default Templates;
