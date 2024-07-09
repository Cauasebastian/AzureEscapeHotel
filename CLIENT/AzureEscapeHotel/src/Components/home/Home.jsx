import React from 'react'
import { Link } from 'react-router-dom'
import MainHeader from '../layout/MainHeader'
import HotelService from '../Common/HotelService'
import Parallax from '../Common/Parallax'

const Home = () => {
  return (
    <section>
      <MainHeader />
      <section className='container'>
        <Parallax />
        <HotelService />
        <Parallax />
      </section>
    </section>
  )
}

export default Home