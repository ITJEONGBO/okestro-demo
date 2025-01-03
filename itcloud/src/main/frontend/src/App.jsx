import React, { useState, useEffect } from 'react';
import { HashRouter  as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import STOMP from './Socket'
import { Toaster, toast } from 'react-hot-toast';
import './App.css';
import Header from './components/Header/Header';
import MainOuter from './components/MainOuter';

import Login from './page/Login';
import Dashboard from './components/Dashboard';
import Setting from './components/Setting/Setting';
import Event from './components/Event';
import Error from './components/Error';

import AllVm from './components/Computing/AllVm';
import AllTemplates from './components/Computing/templatejs/AllTemplates';
import AllNetwork from './components/Network/AllNetwork';
import AllDomain from './components/Storage/AllDomain';
import AllDisk from './components/Storage/AllDisk';

import RutilManager from './components/Computing/RutilManager';
import DataCenterInfo from './components/Computing/datacenterjs/DataCenterInfo';
import ClusterInfo from './components/Computing/clusterjs/ClusterInfo';
import HostInfo from './components/Computing/hostjs/HostInfo';
import VmDetail from './components/Computing/VmDetail';
import TemplateInfo from './components/Computing/templatejs/TemplateInfo';
import NetworkInfo from './components/Network/networkjs/NetworkInfo';
import DomainInfo from './components/Storage/domainjs/DomainInfo';
import DiskInfo from './components/Storage/diskjs/DiskInfo';
import SettingInfo from './components/Setting/SettingInfo';
import { IconGallery } from '@storybook/blocks';

// import NetworkDetail from './components/Network/NetworkDetail';
// import TemplateDetail from './components/Computing/TemplateDetail';
// import Templates from './components/Computing/Rutil/Templates';


const App = () => {
  const [stompClient, setStompClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState('');
  useEffect(() => {
    // Connect using STOMP
    STOMP.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      // Subscribe to a topic
      STOMP.subscribe('/topic/public', (res) => {
        const receivedMessage = JSON.parse(res.body);
        console.log(`message received! ... ${receivedMessage}`)
        showMessage(receivedMessage);
        showToast(receivedMessage);  // Show toast on message receive
      });
    });

    // Set stomp client in state
    setStompClient(STOMP);

    // Cleanup on component unmount
    return () => {
      if (STOMP) {
        STOMP.disconnect(() => {
          console.log('Disconnected from STOMP');
        });
      }
    };
  }, []);

  const showMessage = (message) => {
    setMessages((prevMessages) => [...prevMessages, message]);
  };

  const showToast = (msg) => {
    toast(`${msg.content}`, {
      icon: `${msg.symbol}`,
      duration: 4000,
      ariaProps: {
        role: 'status',
        'aria-live': 'polite',
      },
    });
  };

  const queryClient = new QueryClient()
  
  const isAuthenticated = () => {
    // Implement your authentication logic here
    return localStorage.getItem('token') !== null; // Example: check if a token exists
  };
  
  const [authenticated, setAuthenticated] = useState(false);
  
  useEffect(() => {
    setAuthenticated(isAuthenticated());
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        {authenticated ? (
          <>
          <Header setAuthenticated={setAuthenticated} />
          <MainOuter>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/computing/rutil-manager" element={<RutilManager />} />
              <Route path="/computing/rutil-manager/:section" element={<RutilManager />} />

              <Route path="/computing/datacenters/:id/:section" element={<DataCenterInfo />} />

              <Route path="/computing/clusters/:id" element={<ClusterInfo />} />
              <Route path="/computing/clusters/:id/:section" element={<ClusterInfo />} />  
              
              <Route path="/computing/hosts/:id" element={<HostInfo />}/>
              <Route path="/computing/hosts/:id/:section" element={<HostInfo />}/>

              <Route path="/computing/vms" element={<AllVm />} />
              <Route path="/computing/vms/:id" element={<VmDetail />} />
              <Route path="/computing/vms/:id/:section" element={<VmDetail />} />
              
              <Route path="/computing/vms/templates" element={<AllTemplates />} />
              <Route path="/computing/templates/:id" element={<TemplateInfo />} />
              <Route path="/computing/templates/:id/:section" element={<TemplateInfo />} />
                            
              <Route path="/networks/rutil-manager" element={<RutilManager />} />
              <Route path="/networks/rutil-manager/:section" element={<RutilManager />} />

              <Route path="/networks" element={<AllNetwork />} />
              <Route path="/networks/datacenters/:id/:section" element={<DataCenterInfo />} />
              <Route path="/networks/:id" element={<NetworkInfo />} /> 
              <Route path="/networks/:id/:section" element={<NetworkInfo />} /> 

              <Route path="/storages/rutil-manager" element={<RutilManager />} />
              <Route path="/storages/rutil-manager/:section" element={<RutilManager />} />

              <Route path="/storages/domains" element={<AllDomain />} />
              <Route path="/storages/datacenters/:id/:section" element={<DataCenterInfo />} />
              <Route path="/storages/domains/:id" element={<DomainInfo />} /> 
              <Route path="/storages/domains/:id/:section" element={<DomainInfo />} /> 
        
              <Route path="/storages/disks" element={<AllDisk />} />
              <Route path="/storages/disks/:id" element={<DiskInfo />} />
              <Route path="/storages/disks/:id/:section" element={<DiskInfo />} />

              <Route path="/events" element={<Event />} />
              <Route path="/settings/:section" element={<SettingInfo />} />
              <Route path="/error" element={<Error />} />
         

            </Routes>
          </MainOuter>
          </>
          ) :
          (<Routes>
            <Route path="/" element={<Login setAuthenticated={setAuthenticated} />} />
          </Routes>)
        }
      </Router>
      <Toaster 
        position="top-center"
        reverseOrder={false}    
        gutter={4} 
      />
    </QueryClientProvider>
  );
}


export default App;
