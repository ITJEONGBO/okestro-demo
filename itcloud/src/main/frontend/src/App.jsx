import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Header from './components/Header';
import Computing from './components/Computing';
import MainOuter from './components/MainOuter';
import Storage from './components/Storage';
import Network from './components/Network';
import Setting from './components/Setting';
import Vm from './components/Computing/Vm';
import Cluster from './components/Computing/Cluster';
import ClusterName from './components/Computing/ClusterName';
import Host from './components/Computing/Host';
import HostDetail from './components/Computing/HostDetail';
import NetworkDetail from './detail/NetworkDetail';
import StorageDomain from './detail/StorageDomain';
import StorageDisk from './detail/StorageDisk';

function App() {
    return (
        <Router>
            <Header />
            <MainOuter>
                <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/computing/*" element={<Computing />} />
                    <Route path="/storage" element={<Storage />} />
                    <Route path="/network" element={<Network />} />
                    <Route path="/setting" element={<Setting />} />
                    <Route path="/computing/vm" element={<Vm />} />
                    <Route path="/cluster" element={<Cluster />} />
                    <Route path="/cluster-name" element={<ClusterName />} />
                    <Route path="/host" element={<Host />} />
                    <Route path="/host-detail/:name" element={<HostDetail />} />
                    <Route path="/network/:name" element={<NetworkDetail />} /> 
                    <Route path="/storage-domain/:name" element={<StorageDomain />} />
                    <Route path="/storage-disk/:name" element={<StorageDisk />} />
                </Routes>
            </MainOuter>
        </Router>
    );
}

export default App;
