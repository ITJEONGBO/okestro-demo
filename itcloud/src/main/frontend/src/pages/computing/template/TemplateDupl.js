import React, { useState } from 'react';
import TemplateTable from './TemplateTable';
import TemplateActionButtons from './button/TemplateActionButtons';
import TemplateModals from './modal/TemplateModals';
import HeaderButton from '../../../components/button/HeaderButton';
import { faDesktop } from '@fortawesome/free-solid-svg-icons';

const TemplateDupl = ({
  templates = [],
  columns = [],
  type
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [action, setAction] = useState(null);
  const [selectedTemplates, setSelectedTemplates] = useState([]); 

  const handleActionClick = (actionType) => {
    setAction(actionType);
    setIsModalOpen(true);
  };

  const selectedIds = (Array.isArray(selectedTemplates) ? selectedTemplates : []).map((template) => template.id).join(', ');


  const renderSection = () => (
    <div id="section">
      <HeaderButton
        titleIcon={faDesktop}
        title={'템플릿'}
        buttons={[]}
      />
      <div className="host_btn_outer">
        <TemplateActionButtons
          onEdit={() => selectedTemplates.length === 1 && handleActionClick('edit')} 
          onDelete={() => selectedTemplates.length > 0 && handleActionClick('delete')} 
          isEditDisabled={selectedTemplates.length !== 1}
          isDeleteDisabled={selectedTemplates.length === 0}
        />
        <span>선택된 템플릿 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>
    
        <TemplateTable
          columns={columns}
          templates={templates}
          setSelectedTemplates={(selected) => {
            if (Array.isArray(selected)) setSelectedTemplates(selected); 
          }}
        />
        
        <TemplateModals
          isModalOpen={isModalOpen}
          action={action}
          onRequestClose={() => setIsModalOpen(false)}
          selectedTemplate={selectedTemplates.length > 0 ? selectedTemplates[0] : null} 
          selectedTemplates={selectedTemplates}
        
        />
      </div>
    </div>
  );
  console.log('TemplateModals로 전달된 selectedTemplate:', selectedTemplates.length > 0 ? selectedTemplates[0] : null);
  if (type === 'rutil') {
    return renderSection();
  } else {
    return (
      <>
        <div onClick={(e) => e.stopPropagation()}> 
          <TemplateActionButtons
            onEdit={() => selectedTemplates.length === 1 && handleActionClick('edit')} 
            onDelete={() => selectedTemplates.length > 0 && handleActionClick('delete')} 
            isEditDisabled={selectedTemplates.length !== 1} 
            isDeleteDisabled={selectedTemplates.length === 0} 
          />
          <span>선택된 템플릿 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>
      
          <TemplateTable
            columns={columns}
            templates={templates}
            setSelectedTemplates={(selected) => {
              if (Array.isArray(selected)) setSelectedTemplates(selected); // 유효한 선택만 반영
            }}
          />
          
          <TemplateModals
            isModalOpen={isModalOpen}
            action={action}
            onRequestClose={() => setIsModalOpen(false)}
            selectedTemplate={selectedTemplates.length > 0 ? selectedTemplates[0] : null} 
            selectedTemplates={selectedTemplates}
          />
        </div>
      </>
    );
  }
};

export default TemplateDupl;

// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import TemplateTable from '../table/TemplateTable';
// import TemplateActionButtons from '../button/TemplateActionButtons';
// import TemplateModals from '../Modal/TemplateModals';
// import HeaderButton from '../button/HeaderButton';
// import { faDesktop } from '@fortawesome/free-solid-svg-icons';

// const TemplateDupl = ({
//   templates, 
//   columns, 
//   onFetchTemplates, 
//   status,
//   type
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false);
//   const [action, setAction] = useState(null);
//   const [selectedTemplate, setSelectedTemplate] = useState(null);
//   const navigate = useNavigate();

//   const handleActionClick = (actionType) => {
//     setAction(actionType);
//     setIsModalOpen(true);
//   };

//   const renderSection = () => (
//     <div id="section">
//       <HeaderButton
//         titleIcon={faDesktop}
//         title={'템플릿'}
//         buttons={[]}
//       />
//       <div className="host_btn_outer">
//         <TemplateActionButtons
//           onEdit={() => selectedTemplate?.id && handleActionClick('edit')} 
//           onDelete={() => selectedTemplate?.id && handleActionClick('delete')} 
//           isEditDisabled={!selectedTemplate?.id}
//         />
//         <span>id = {selectedTemplate?.id || ''}</span>
    
//         <TemplateTable
//           columns={columns}
//           templates={templates}
//           selectedTemplate={selectedTemplate}
//           setSelectedTemplate={setSelectedTemplate}
//         />
        
//         <TemplateModals
//           isModalOpen={isModalOpen}
//           action={action}
//           onRequestClose={() => setIsModalOpen(false)}
//           selectedTemplate={selectedTemplate}
//         />
//       </div>
//     </div>
//   );

//   if (type === 'rutil') {
//     return renderSection();
//   } else {
//     return (
//       <>
//         <TemplateActionButtons
//           onEdit={() => selectedTemplate?.id && handleActionClick('edit')} 
//           onDelete={() => selectedTemplate?.id && handleActionClick('delete')} 
//           isEditDisabled={!selectedTemplate?.id} 
//         />
//         <span>id = {selectedTemplate?.id || ''}</span>
    
//         <TemplateTable
//           columns={columns}
//           templates={templates}
//           selectedTemplate={selectedTemplate}
//           setSelectedTemplate={setSelectedTemplate}
//         />
        
//         <TemplateModals
//           isModalOpen={isModalOpen}
//           action={action}
//           onRequestClose={() => setIsModalOpen(false)}
//           selectedTemplate={selectedTemplate}
//         />
//       </>
//     );
//   }
// };

// export default TemplateDupl;
