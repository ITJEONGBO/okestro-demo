import React from 'react';
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

function App() {
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
        </QueryClientProvider>
    );
}

export default App;
