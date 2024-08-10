import React from 'react'
import { Link } from 'react-router-dom'
import MainHeader from '../layout/MainHeader'
import HotelService from '../Common/HotelService'
import Parallax from '../Common/Parallax'
import RoomCarousel from '../Common/RoomCarousel'
import RoomSearch from '../Common/RoomSearch'

const Home = () => {
  return (
    <section>
      <MainHeader />
      <section className='container'>
        <RoomSearch />
        <RoomCarousel />
        <Parallax />
        <RoomCarousel />
        <HotelService />
        <Parallax />
        <RoomCarousel />
      </section>
    </section>
  )
}

export default Home