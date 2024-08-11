import React from 'react';
// import './App.css';
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
                    <Route path="/" element={<Cluster />} />
                    <Route path="/cluster-name" element={<ClusterName />} />
                    <Route path="/host" element={<Host />} />
                    <Route path="/host-detail/:name" element={<HostDetail />} />
                </Routes>
            </MainOuter>
        </Router>
    );
}

export default App;
