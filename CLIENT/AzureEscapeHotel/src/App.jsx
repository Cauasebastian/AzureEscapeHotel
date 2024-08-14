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
import RequireAuth from './Components/auth/RequireAuth';
import Login from './Components/auth/Login';
import Registration from './Components/auth/Registration';
import Profile from './Components/auth/Profile';
import { AuthProvider } from './Components/auth/AuthProvider';





function App() {
  return (
		<AuthProvider>
			<main>
				<Router>
					<NavBar />
					<Routes>
						<Route path="/" element={<Home />} />
						<Route path="/edit-room/:roomId" element={<EditRoom />} />
						<Route path="/existing-rooms" element={<ExistingRooms />} />
						<Route path="/add-room" element={<AddRoom />} />

						<Route
							path="/book-room/:roomId"
							element={
								<RequireAuth>
									<Checkout />
								</RequireAuth>
							}
						/>
						<Route path="/browse-all-rooms" element={<RoomListing />} />

						<Route path="/admin" element={<Admin />} />
						<Route path="/booking-success" element={<BookingSuccess />} />
						<Route path="/existing-bookings" element={<Bookings />} />
						<Route path="/find-booking" element={<FindBooking />} />

						<Route path="/login" element={<Login />} />
						<Route path="/register" element={<Registration />} />

						<Route path="/profile" element={<Profile />} />
						<Route path="/logout" element={<FindBooking />} />
					</Routes>
				</Router>
				<Footer />
			</main>
		</AuthProvider>
	)
}

export default App