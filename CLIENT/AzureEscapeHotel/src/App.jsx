import React from 'react';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import './App.css';
import Home from './Components/home/Home';
import AddRoom from './Components/Room/AddRoom';
import ExistingRooms from './Components/Room/ExistingRooms';
import EditRoom from './Components/Room/EditRoom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Nav } from 'react-bootstrap';
import NavBar from './Components/layout/NavBar';
import Footer from './Components/layout/Footer';
import RoomListing from './Components/Room/RoomListing';
import Admin from './Components/admin/Admin';
import Checkout from './Components/booking/Checkout';
import BookingSuccess from './Components/booking/BookingSuccess';
import Bookings from './Components/booking/Bookings';
import FindBooking from './Components/booking/FindBooking';



function App() {
  return (
    <>
    <main>
    <Router>
      <NavBar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/existing-rooms" element={<ExistingRooms />} />
          <Route path="/edit-room/:roomId" element={<EditRoom />} />
          <Route path="/add-room" element={<AddRoom />} />
          <Route path="/book-room/:roomId" element={<Checkout />} />
          <Route path="/browse-all-rooms" element={<RoomListing />} />
          <Route path="/admin" element={<Admin/>} />
          <Route path="/existing-bookings" element={<Bookings />} />
          <Route path="/find-booking" element={<FindBooking />} />
          <Route path="/booking-success" element={<BookingSuccess />} />
        </Routes>
        <Footer />
    </Router>
      </main>
    </>
    
  );
}

export default App;
