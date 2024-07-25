import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Header from './components/Header';
import Computing from './components/Computing';
import MainOuter from './components/MainOuter';
import Storage from './components/Storage';
import Network from './components/Network';
import Setting from './components/Setting';

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
                </Routes>
            </MainOuter>
        </Router>
    );
}

export default App;