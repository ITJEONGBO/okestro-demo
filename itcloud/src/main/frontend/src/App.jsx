import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Dashboard from './components/Dashboard';
import Header from './components/Header/Header';
import Computing from './components/Computing/Computing';
import MainOuter from './components/MainOuter';
import Storage from './components/Storage/Storage';
import Network from './components/Network/Network';
import Setting from './components/Setting';
import Vm from './components/Computing/Vm';
import Cluster from './components/Computing/Cluster';
import ClusterName from './components/Computing/ClusterName';
import Host from './components/Computing/Host';
import HostDetail from './components/Computing/HostDetail';
import StorageDomainDetail from './components/Storage/StorageDomainDetail';
import StorageDiskDetail from './components/Storage/StorageDiskDetail';
import DataCenterDetail from './components/Computing/DataCenterDetail';
import VmHostChart from './components/Computing/VmHostChart';
import NetworkDetail from './components/Network/NetworkDetail';
import './App.css';
import DomainParts from './components/Storage/StorageDomainPart';
import STOMP from './Socket'
import { Toaster, toast } from 'react-hot-toast';

function App() {

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

  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Header />
        <MainOuter>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/computing/datacenter" element={<Computing />} />
            <Route path="/computing/datacenter/:name" element={<DataCenterDetail />} />
            <Route path="/storage" element={<Storage />} />
            <Route path="/setting" element={<Setting />} />
            <Route path="/computing/:name" element={<Vm />} />
            <Route path="/computing/host" element={<Host />} />
            <Route path="/computing/vmhost-chart" element={<VmHostChart />} />
            <Route path="/computing/host/:id" element={<HostDetail />}/>
            <Route path="/computing/clusters" element={<Cluster />} />
            <Route path="/computing/clusters/:id" element={<ClusterName />} /> 
            <Route path="/networks" element={<Network />} />
            <Route path="/networks/:id" element={<NetworkDetail />} /> 
            <Route path="/storage-domain/:name" element={<StorageDomainDetail />} />
            <Route path="/storage-domainpart" element={<DomainParts />} />
            <Route path="/storage-disk/:name" element={<StorageDiskDetail />} />
          </Routes>
        </MainOuter>
      </Router>
      <Toaster 
        position="top-center"
        reverseOrder={false}    
        gutter={8} 
      />
    </QueryClientProvider>
  );
}

export default App;
