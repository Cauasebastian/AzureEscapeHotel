import React from 'react'
import { Link } from 'react-router-dom'

const Home = () => {
  return (
    <div>
        <h1>Welcome to Azure Escape Hotel</h1>
        <p>Enjoy your stay at our luxurious hotel</p>
        <Link to="/existing-rooms" className="btn btn-primary">View Rooms</Link>
        
    </div>
  )
}

export default Home