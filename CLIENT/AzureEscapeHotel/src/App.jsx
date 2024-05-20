import React from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import './App.css';
import Home from './Components/home/Home';
import AddRoom from './Components/Room/AddRoom';
import ExistingRooms from './Components/Room/ExistingRooms';
import EditRoom from './Components/Room/EditRoom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
  return (
    <Router>
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/existing-rooms" element={<ExistingRooms />} />
          <Route path="/edit-room/:roomId" element={<EditRoom />} />
          <Route path="/add-room" element={<AddRoom />} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;
