import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TemplateTable from '../table/TemplateTable';
import TemplateActionButtons from '../button/TemplateActionButtons';
import TemplateModals from '../Modal/TemplateModals';
import HeaderButton from '../button/HeaderButton';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';

const TemplateDupl = ({
  templates, 
  columns, 
  onFetchTemplates, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedTemplate, setSelectedTemplate] = useState(null);
  const navigate = useNavigate();

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title={'템플릿'}
        buttons={[]}
      />
      <div className="host_btn_outer">
      <TemplateActionButtons
        onEdit={() => selectedTemplate?.id && handleActionClick('edit')} 
        onDelete={() => selectedTemplate?.id && handleActionClick('delete')} 
        isEditDisabled={!selectedTemplate?.id} 
      />
      <span>id = {selectedTemplate?.id || ''}</span>  
      
      <TemplateTable
        columns={columns}
        templates={templates}
        selectedTemplate={selectedTemplate}
        setSelectedTemplate={setSelectedTemplate}
      />
      
      <TemplateModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedTemplate={selectedTemplate}
      />
      </div>
    </div>
    </>
  );
};

export default TemplateDupl;