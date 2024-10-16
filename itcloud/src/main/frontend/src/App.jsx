import React, { useState, useEffect } from 'react';
import { HashRouter  as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Login from './page/Login';
import Dashboard from './components/Dashboard';
import Example from './components/Example';
import Header from './components/Header/Header';
import Computing from './components/Computing/Computing';
import MainOuter from './components/MainOuter';
import AllDomain from './components/Storage/AllDomain';
import Network from './components/Network/Network';
import Setting from './components/Setting/Setting';
import Vm from './components/Computing/Vm';
import Cluster from './components/Computing/Cluster';
import ClusterName from './components/Computing/ClusterName';
import Host from './components/Computing/Host';
import HostDetail from './components/Computing/HostDetail';
import StorageDomainDetail from './components/Storage/StorageDomainDetail';
import StorageDiskDetail from './components/Storage/StorageDiskDetail';
import DataCenter from './components/Computing/DataCenter';
import VmTemplateChart from './components/Computing/VmTemplateChart';
import NetworkDetail from './components/Network/NetworkDetail';
import DomainParts from './components/Storage/StorageDomainPart';
import STOMP from './Socket'
import { Toaster, toast } from 'react-hot-toast';
import './App.css';
import TemplateDetail from './components/Computing/TemplateDetail';
import Templates from './components/Computing/Templates';
import AllVm from './components/Computing/AllVm';
import AllDisk from './components/Storage/AllDisk';
import RutilManager from './components/Computing/RutilManager';
import Event from './components/Event';
import Error from './components/Error';



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
      /* 
      if (STOMP) {
        STOMP.disconnect();
      }
      */
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
        {/* {authenticated ? ( */}
          <>
          <Header setAuthenticated={setAuthenticated} />
          <MainOuter>
            <Routes>
            <Route path="/" element={<Dashboard />} />
              <Route path="/computing/datacenters" element={<Computing />} />
              <Route path="/computing/datacenters/:id" element={<DataCenter />} />
              
              <Route path="/computing/vms" element={<AllVm />} />
              <Route path="/computing/vms/:id" element={<Vm />} />
              <Route path="/computing/vms/:id/:section" element={<Vm />} />
              
              <Route path="/computing/host" element={<Host />} />
              <Route path="/computing/hosts/:id" element={<HostDetail />}/>
              <Route path="/computing/hosts/:id/:section" element={<HostDetail />}/>

              <Route path="/computing/templates" element={<Templates />} />
              <Route path="/computing/templates/:id" element={<TemplateDetail />} />

              <Route path="/computing/clusters" element={<Cluster />} />
              <Route path="/computing/clusters/:id" element={<ClusterName />} />
              <Route path="/computing/clusters/:id/:section" element={<ClusterName />} />  

              <Route path="/networks" element={<Network />} />
              <Route path="/networks/:id" element={<NetworkDetail />} /> 
              <Route path="/networks/:id/:section" element={<NetworkDetail />} /> 

              <Route path="/storages/domains" element={<AllDomain />} />
              <Route path="/storages/domains/:id" element={<StorageDomainDetail />} /> 
              <Route path="/storages/domains/:id/:section" element={<StorageDomainDetail />} /> 
              <Route path="/storage-domainpart" element={<DomainParts />} />

              <Route path="/storages/disks" element={<AllDisk />} />
              <Route path="/storages/disks/:id" element={<StorageDiskDetail />} />
              <Route path="/storages/disks/:id/:section" element={<StorageDiskDetail />} />

              <Route path="/events" element={<Event />} />
              <Route path="/settings" element={<Setting />} />
              <Route path="/settings/:section" element={<Setting />} />
              <Route path="/error" element={<Error />} />
 
              {/*임시(삭제예정) */}
              <Route path="/computing/rutil-manager" element={<RutilManager />} />
              <Route path="/computing/vm-template-chart" element={<VmTemplateChart />} />
              <Route path="/example" element={<Example />} />
              <Route path="/storages/disks/domain" element={<StorageDomainDetail />} />
              <Route path="/computing/clusters/detail" element={<ClusterName />} /> 
              
            </Routes>
          </MainOuter>
          </>
          {/* ) :
          (<Routes>
            <Route path="/" element={<Login setAuthenticated={setAuthenticated} />} />
          </Routes>)
        } */}
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
