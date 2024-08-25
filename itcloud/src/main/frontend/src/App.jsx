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
import DataCenterDetail from './components/Computing/DataCenterDetail';
import './App.css';
function App() {
    return (
        <Router>
            <Header />
            <MainOuter>
                <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/computing/datacenter" element={<Computing />} />
                    <Route path="/computing/datacenter/:name" element={<DataCenterDetail />} />
                    <Route path="/storage" element={<Storage />} />
                    <Route path="/network" element={<Network />} />
                    <Route path="/setting" element={<Setting />} />
                    <Route path="/computing/:name" element={<Vm />} />
                    <Route path="/computing/host" element={<Host />} />
                    <Route path="/computing/host/:name" element={<HostDetail />}/>
                    <Route path="/computing/cluster" element={<Cluster />} />
                    <Route path="/computing/cluster/:name" element={<ClusterName />} /> 
                    <Route path="/network/:name" element={<NetworkDetail />} /> 
                    <Route path="/storage-domain/:name" element={<StorageDomain />} />
                    <Route path="/storage-disk/:name" element={<StorageDisk />} />
                </Routes>
            </MainOuter>
        </Router>
    );
}

export default App;
