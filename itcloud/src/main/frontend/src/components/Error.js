import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TableOuter from './table/TableOuter';
import HeaderButton from './button/HeaderButton';
import TableColumnsInfo from './table/TableColumnsInfo';
import Footer from './footer/Footer';
import { useAllClusters, useAllEvents } from '../api/RQHook';
import PagingTable from './table/PagingTable';
import PagingTableOuter from './table/PagingTableOuter';
import { faExclamationTriangle } from '@fortawesome/free-solid-svg-icons';


const Error = () => {

  return (
    <div id="section">
        <div className='error_text'>
            <FontAwesomeIcon icon={faExclamationTriangle} fixedWidth/> 
            <span>페이지를 표시할 수 없습니다.</span>
        </div>
    </div>
  );
};

export default Error;
